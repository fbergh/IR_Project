import subprocess
import os
print(os.getcwd())
print(os.path.dirname(os.path.realpath(__file__)))

# Initialise experiment variables
qe_methods=["test"] #["api","index"]
ks = [0] #[1,3,5,10]

# Initialise file (base) locations. If you run this file from experiment/, you won't have to change anything
# The code assumes the query files have name structure: "topics.robust04.expanded.k.method.txt", 
# e.g. k=[1,3,5,10] and method=["api","index"] specified above
log_file = "output/experiment.log"
robust04_index_loc = "../robust04/lucene-index.robust04.pos+docvectors+rawdocs"
queries_base = "queries/topics.robust04.expanded"
retr_output_base = "output/run.robust04.bm25.topics.robust04.expanded"
qrels = "../anserini/src/main/resources/topics-and-qrels/qrels.robust04.txt"
anserini_class_loc = "../anserini/target/appassembler/bin/SearchCollection"
anserini_eval_loc = "../anserini/eval/trec_eval.9.0.4/trec_eval"

for method in qe_methods:
    for k in ks:
        with open("output/experiment.log","w") as log:
            # Step 1: perform retrieval with Anserini
            log.write("Retrieval for method="+method+" and k="+str(k)+"\n---------------------------------")
            query_file = queries_base+"."+str(k)+"."+method+".txt"
            retr_output_file = retr_output_base+"."+str(k)+"."+method+".txt"
            subprocess.run(["nohup",anserini_class_loc,"-index",robust04_index_loc,"-topicreader","Trec",
                            "-topics",query_file,"-bm25","-output",retr_output_file], 
                            stdout=log, text=True)

            # Step 2: perform evaluation with Anserini
            log.write("\nRetrieval for method="+method+" and k="+str(k)+"\n---------------------------------")
            subprocess.run([anserini_eval_loc,qrels, retr_output_file], 
                           stdout=log, text=True)

            log.close()