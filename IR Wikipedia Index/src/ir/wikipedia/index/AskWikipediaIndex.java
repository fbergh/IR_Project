package ir.wikipedia.index;

import static ir.wikipedia.index.Tools.escapeNonAlphanumeric;
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

/**
 * Class for querying the Wikipedia index.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class AskWikipediaIndex {
    
    /**
     * Queries the Wikipedia index for a given query, and returns a list of 
     * resulting titles.
     * 
     * @param query - The query that will be issued to Wikipedia
     * @param field - The field in which the query should be searched (title/body)
     * @param indexLoc - The location of the Wikipedia index
     * @param limit - The number of results to return
     * @return - ArrayList of retreived document titles
     * @throws IOException 
     * @throws ParseException
     */
    public static ArrayList<String> getWikipediaResults(String query, String field, String indexLoc, int limit) throws IOException, ParseException {
        // Create a searcher for the index
        IndexSearcher searcher = createSearcher(indexLoc);
        
        // Run the query against the index
        TopDocs hits = searcher.search(createQuery(query, field), limit*2);
        
        // Filter the results to remove pages that are not desired
        ArrayList<String> titles = getFilteredTopNTitles(searcher, hits, limit);
        
        // Return the result
        return titles;
    }
    
    /**
     * Parses a Lucene query from a given query string.
     * 
     * @param query - The query in string format
     * @param field - The field in which the query should be searched (title/body)
     * @return - The parsed Lucene query
     * @throws ParseException 
     */
    private static Query createQuery(String query, String field) throws ParseException{
        return new QueryParser(field, new StandardAnalyzer()).parse(escapeNonAlphanumeric(query));    
    }
    
    /**
     * Creates the searcher for a Lucene index.
     * 
     * @param indexLoc - The folder in which the index is located
     * @return - The generated searcher
     * @throws IOException 
     */
    static IndexSearcher createSearcher(String indexLoc) throws IOException {
        File outputDir = new File(indexLoc);
        FSDirectory dir = FSDirectory.open(outputDir.toPath());
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    } 
    
    /**
     * Gets the titles of the top documents from the list of retrieved 
     * documents, filtered to remove "Template", "Module", "Wikipedia", 
     * "Category" and "File" pages.
     * 
     * @param searcher - The searcher that was used to retrieve documents
     * @param hits - The list of retrieved documents
     * @param limit - The number of documents for which the title should be stored
     * @return - An ArrayList containing the titles of the top documents
     * @throws IOException 
     */
    static ArrayList<String> getFilteredTopNTitles(IndexSearcher searcher, TopDocs hits, int limit) throws IOException {
        ArrayList<String> titles = new ArrayList<>();
        for(ScoreDoc hit: hits.scoreDocs) {
            Document document = searcher.doc(hit.doc);
            String title = document.get("title");
            if(titles.size() < limit && ! title.matches("^((Template)|(Module)|(Wikipedia)|(Category)|(File)):.*"))
                titles.add(title);   
        }
        return titles;
    }
    
}