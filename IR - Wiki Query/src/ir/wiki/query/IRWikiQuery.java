package ir.wiki.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;


/**
 *
 * @author Max Driessen, s4789628
 */
public class IRWikiQuery {

    // Query Variables
    private static final String QUERY = "strawberry";
    private static final String FIELD = "body";
    private static final int NR_RETRIEVE = 30;
    private static final int NR_STORE = 20;
    private static final boolean VERBOSE = false;
    
    // Settings
    private static final Similarity SIMILARITY = new BM25Similarity();
    private static final String INDEX = "Index";

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create the searcher
        IndexSearcher searcher;
        try{
            searcher = createSearcher(INDEX, SIMILARITY);
        }catch(IOException e){
            System.out.println("Something went wrong while creating the searcher.");
            System.out.println(e);
            return;
        }

        // Run the query
        TopDocs hits;
        try{
            hits = searcher.search(createQuery(QUERY, FIELD), NR_RETRIEVE);
        }catch(IOException e){
            System.out.println("Something went wrong while running the query.");
            System.out.println(e);
            return;
        }catch(ParseException e){
            System.out.println("Something went wrong while parsing the query.");
            System.out.println(e);
            return;
        }
        
        // Gather results
        try{
            ArrayList<String> titles = getFilteredTopNTitles(searcher, hits, NR_STORE);
            System.out.println(titles);
            if(VERBOSE)
                printResults(searcher, hits);
        }catch(IOException e){
            System.out.println("Something went wrong while gathering results.");
            System.out.println(e);
        }
        
 
    }
    
    
    /**
     * Parses a Lucene query from a given query string
     * @param query - the query in string format
     * @param field - the field in which the query should be searched (title/body/etc.)
     * @return - the parsed Lucene query
     * @throws ParseException 
     */
    static Query createQuery(String query, String field) throws ParseException{
        return new QueryParser(field, new StandardAnalyzer()).parse(query);    
    }
    
    
    /**
     * Creates the searcher for an index, using a particular similarity measure
     * @param folderName - the folder in which the index is located
     * @param similarityMeasure - the similarity measure the searcher should use
     * @return - the generated searcher
     * @throws IOException 
     */
    static IndexSearcher createSearcher(String folderName, Similarity similarityMeasure) throws IOException {
        File outputDir = new File("Index");
        FSDirectory dir = FSDirectory.open(outputDir.toPath());
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(similarityMeasure);
        return searcher;
    } 
    

    /**
     * Gets the titles of the top N documents from the list of retrieved documents, filtered to remove "Template", "Module" and "Wikipedia" pages
     * @param searcher - the searcher that was used to retrieve documents
     * @param hits - the list of retrieved documents
     * @param nrStore - the number of documents for which the title should be stored
     * @return - a list of the titles of the top N documents
     * @throws IOException 
     */
    static ArrayList<String> getFilteredTopNTitles(IndexSearcher searcher, TopDocs hits, int nrStore) throws IOException {
        ArrayList<String> titles = new ArrayList<>();
        for(ScoreDoc hit: hits.scoreDocs) {
            Document document = searcher.doc(hit.doc);
            String title = document.get("title");
            if(!title.startsWith("Template:") && !title.startsWith("Module:") && !title.startsWith("Wikipedia:") && titles.size() < nrStore)
                titles.add(title);   
        }
        return titles;
    }
    
    
    /**
     * Prints all retrieved documents
     * @param searcher - the searcher that was used to retrieve documents
     * @param hits - the list of retrieved documents
     * @throws IOException 
     */
    static void printResults(IndexSearcher searcher, TopDocs hits) throws IOException {
        for(ScoreDoc hit: hits.scoreDocs) {
            Document document = searcher.doc(hit.doc);
            System.out.println("Hit: ");
            for(IndexableField iff : document.getFields()) {
                String content = document.get(iff.name());
                if(content.length() > 80) content = content.substring(0,80)+"...";
                System.out.println(iff.name()+ " : " + content);
            }
        }
    }
    

}
