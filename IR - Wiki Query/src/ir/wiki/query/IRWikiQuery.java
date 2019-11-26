package ir.wiki.query;

import java.io.File;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;

/**
 *
 * @author dries
 */
public class IRWikiQuery {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        File outputDir = new File("Index");
        
        FSDirectory dir = FSDirectory.open(outputDir.toPath());
        
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        searcher.setSimilarity(new BM25Similarity());

        TermQuery query = new TermQuery(new Term("body", "banana"));
        TopDocs hits = searcher.search(query, 50);
        for(ScoreDoc hit: hits.scoreDocs) {
            Document document = searcher.doc(hit.doc);
            System.out.println("Hit: ");
            for(IndexableField iff : document.getFields()) {
                String content = document.get(iff.name());
                if(content.length() > 40) content = content.substring(0,40)+"...";
                System.out.println(iff.name()+ " : " + content);
            }
        }
    }
    
}
