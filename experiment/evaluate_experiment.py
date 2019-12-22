import subprocess
import os
print(os.getcwd())
print(os.path.dirname(os.path.realpath(__file__)))

# Initialise experiment variables
qe_methods = ["api","index"]
filter_methods = ["duplicates","stopwords","both"]
ks = [1,3,5,10]
K = 3

# Initialise file locations. If you run this file from experiment/, you won't have to change anything
ROBUST04_INDEX_LOC = "../robust04/lucene-index.robust04.pos+docvectors+rawdocs"
ANSERINI_CLASS_LOC = "../anserini/target/appassembler/bin/SearchCollection"
ANSERINI_EVAL_LOC = "../anserini/eval/trec_eval.9.0.4/trec_eval"
QRELS = "../anserini/src/main/resources/topics-and-qrels/qrels.robust04.txt"
# The start for every query/retrieval output file in the experiments has the same base (also see comment above)
queries_base = "queries/topics.robust04.expanded"
retr_output_base = "output/retrieval/run.robust04.bm25.topics.robust04.expanded"
results_output_base = "output/results/"

'''
BASELINE
The baseline model is plain BM25 without query expansion
'''
print("BASELINE")
with open(results_output_base+"baseline.log", "w") as log:
    # Retrieval
    query_file = queries_base[:-9]+".txt"
    retr_output_file = retr_output_base[:-9]+".txt"
    subprocess.run(["nohup", ANSERINI_CLASS_LOC, "-index", ROBUST04_INDEX_LOC, "-topicreader", "Trec",
                    "-topics", query_file, "-bm25", "-output", retr_output_file],
                    stdout=log, text=True)

    # Evaluation
    subprocess.run([ANSERINI_EVAL_LOC, QRELS, retr_output_file],
                    stdout=log, text=True)

'''
EXPERIMENT 1
Uses query files in format "topics.robust04.expanded.k.method.txt"
where k=3 (default K) and method=[api,index]
'''
print("EXPERIMENT 1")
for method in qe_methods:
    print("method="+method)
    with open(results_output_base+"experiment_method="+method+".log", "w") as log:
        # Retrieval
        query_file = queries_base+"."+str(K)+"."+method+".txt"
        retr_output_file = retr_output_base+"."+str(K)+"."+method+".txt"
        subprocess.run(["nohup", ANSERINI_CLASS_LOC, "-index", ROBUST04_INDEX_LOC, "-topicreader", "Trec",
                        "-topics", query_file, "-bm25", "-output", retr_output_file],
                        stdout=log, text=True)

        # Evaluation
        subprocess.run([ANSERINI_EVAL_LOC, QRELS, retr_output_file],
                        stdout=log, text=True)
    log.close()

'''
EXPERIMENT 2
Uses query files in format "topics.robust04.expanded.k.txt"
where k=[1,3,5,10]
'''
print("EXPERIMENT 2")
for k in ks:
    print("K="+str(k))
    with open(results_output_base+"experiment_k="+str(k)+".log", "w") as log:
        # Retrieval
        query_file = queries_base+"."+str(k)+".txt"
        retr_output_file = retr_output_base+"."+str(k)+".txt"
        subprocess.run(["nohup",ANSERINI_CLASS_LOC,"-index",ROBUST04_INDEX_LOC,"-topicreader","Trec",
                        "-topics",query_file,"-bm25","-output",retr_output_file], 
                        stdout=log, text=True)

        # Evaluation
        subprocess.run([ANSERINI_EVAL_LOC,QRELS, retr_output_file], 
                        stdout=log, text=True)
        log.close()

'''
EXPERIMENT 3
Uses query files in format "topics.robust04.expanded.k.filter_method.txt"
where k=3 (default K) and filter_method=[duplicates, stop words, both]
'''
print("EXPERIMENT 3")
for filter_method in filter_methods:
    print("Filter method="+filter_method)
    with open(results_output_base+"experiment_filtermethod="+filter_method+".log", "w") as log:
        # Retrieval
        query_file = queries_base+"."+str(K)+"."+filter_method+".txt"
        retr_output_file = retr_output_base+"."+str(K)+"."+filter_method+".txt"
        subprocess.run(["nohup", ANSERINI_CLASS_LOC, "-index", ROBUST04_INDEX_LOC, "-topicreader", "Trec",
                        "-topics", query_file, "-bm25", "-output", retr_output_file],
                        stdout=log, text=True)

        # Evaluation
        subprocess.run([ANSERINI_EVAL_LOC, QRELS, retr_output_file],
                        stdout=log, text=True)
        log.close()
