package ir.cranfield.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * @author Max Driessen, s4789628
 * @author Freek van den Bergh, s4801709
 */
public class IRCranfieldQuery {

    // Query Variables
    private static final String QUERY = "aerodynamic aircraft";
    private static final String FIELD = "text";
    private static final int NR_RETRIEVE = 30;
    private static final int NR_STORE = 20;
    private static final boolean VERBOSE = true;
    
    // Settings
    private static final Similarity SIMILARITY = new BM25Similarity();
    private static final String INDEX = "../data/cran/lucene_index";

    
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
        
        System.out.println("The total number of hits: "+hits.totalHits);
        
        // Gather results
        try{
            ArrayList<Integer> docIDs = getTopNDocIDs(searcher, hits, NR_STORE);
            System.out.println(docIDs);
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
        File outputDir = new File(folderName);
        FSDirectory dir = FSDirectory.open(outputDir.toPath());
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(similarityMeasure);
        return searcher;
    } 
    

    /**
     * Gets the document IDs of the top N documents from the list of retrieved documents
     * @param searcher - the searcher that was used to retrieve documents
     * @param hits - the list of retrieved documents
     * @param nrStore - the number of documents for which the IDs should be stored
     * @return - a list of the IDs of the top N documents
     * @throws IOException 
     */
    static ArrayList<Integer> getTopNDocIDs(IndexSearcher searcher, TopDocs hits, int nrStore) throws IOException {
        ArrayList<Integer> docIDs = new ArrayList<>();
        for(ScoreDoc hit: hits.scoreDocs) {
            Document document = searcher.doc(hit.doc);
            // Ugly but indexing with the name of the field did not work
            String docID = document.get(document.getFields().get(0).name());
            docIDs.add(Integer.parseInt(docID));
        }
        return docIDs;
    }
    
    
    /**
     * Prints all retrieved documents
     * @param searcher - the searcher that was used to retrieve documents
     * @param hits - the list of retrieved documents
     * @throws IOException 
     */
    static void printResults(IndexSearcher searcher, TopDocs hits) throws IOException {
        CranfieldParser cp = new CranfieldParser();
        
        for(ScoreDoc hit: hits.scoreDocs) {
            Document document = searcher.doc(hit.doc);
            int docID = 0;
            System.out.println("Hit: ");
            for(IndexableField iff : document.getFields()) {
                String fieldName = iff.name();
                String content = document.get(fieldName);
                if( fieldName.equals("doc_id") ) // Save docID to retrieve content
                    docID = Integer.parseInt(content);
                System.out.println(iff.name()+ " : " + content);
            }
            // Retrieve content of document (since it's not in the fields)
            String content = cp.getDocumentContent(docID);
            if(content.length() > 80) content = content.substring(0,80)+"...";
            System.out.println(content);
        }
    }
    

}

