package ir.query.expansion;

import static ir.query.expansion.Tools.parseWikipediaIndexResult;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Class for writing expanded TREC queries to a new file in the same format.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class TRECQueryWriter  {
    
    /**
     * To run query expansion on all TREC queries for all conditions, run this file.
     * WARNING: Running this function takes a long time
     * 
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        // Specify where sources are located and where results should be stored
        String querySourceLoc = "topics.robust04.txt";
        String wikiIndexTitleLoc = "index_results/results_title.txt";
        String wikiIndexBodyLoc = "index_results/results_body.txt";
        String expandedDestination = "expanded_queries/";
        
        // Expand all queries with different k values
        QueryExpander qe1 = new QueryExpander(1, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe1, querySourceLoc, expandedDestination+"k_1.txt", null, "k = 1");
        
        QueryExpander qe2 = new QueryExpander(3, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe2, querySourceLoc, expandedDestination+"k_3.txt", null, "k = 3");
        
        QueryExpander qe3 = new QueryExpander(5, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe3, querySourceLoc, expandedDestination+"k_5.txt", null, "k = 5");
        
        QueryExpander qe4 = new QueryExpander(10, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe4, querySourceLoc, expandedDestination+"k_10.txt", null, "k = 10");
        
        // Expand all queries while removing stopwords/duplicates
        QueryExpander qe5 = new QueryExpander(3, 30, 20000, 0.5, true, false);
        writeExpandedQueries(qe5, querySourceLoc, expandedDestination+"duplicates_removed.txt", null, "duplicates removed");
        
        QueryExpander qe6 = new QueryExpander(3, 30, 20000, 0.5, false, true);
        writeExpandedQueries(qe6, querySourceLoc, expandedDestination+"stopwords_removed.txt", null, "stopwords removed");
        
        QueryExpander qe7 = new QueryExpander(3, 30, 20000, 0.5, true, true);
        writeExpandedQueries(qe7, querySourceLoc, expandedDestination+"both_removed.txt", null, "stopwords + duplicates removed");
        
        // Expand all queries using the Wikipedia index
        QueryExpander qe8 = new QueryExpander(3, 30, 20000, 0.5, false, false);
        ArrayList<String[]> titleResults = parseWikipediaIndexResult(wikiIndexTitleLoc, 30);
        writeExpandedQueries(qe8, querySourceLoc, expandedDestination+"index_title.txt", titleResults, "index title");
        
        QueryExpander qe9 = new QueryExpander(3, 30, 20000, 0.5, false, false);
        ArrayList<String[]> bodyResults = parseWikipediaIndexResult(wikiIndexBodyLoc, 30);
        writeExpandedQueries(qe9, querySourceLoc, expandedDestination+"index_body.txt", bodyResults, "index body");
    }
    
    /**
     * Write expanded TREC queries to a new file in the same format.
     * 
     * @param qe - QueryExpander object with which the queries will be expanded
     * @param in - The filename of the file containing the TREC queries
     * @param out - The output filename where the expanded TREC queries will be written
     * @param wikiResults - The results of the wikipediaIndex, if needed (null otherwise)
     * @param condition - The current experimental condition
     * @throws IOException
     * @throws ParseException 
     */
    public static void writeExpandedQueries(QueryExpander qe, String in, String out, ArrayList<String[]> wikiResults, String condition) throws IOException, ParseException {
        System.out.println("Begin writing expanded queries for condition "+condition+".");
        
        // Create a BufferedReader and a BufferedWriter
        BufferedReader br = new BufferedReader(new FileReader(in));
        BufferedWriter bw = new BufferedWriter(new FileWriter(out));
        
        // Read until end of file
        int nrQuery = 0;
        for(String readLine = br.readLine(); readLine != null; readLine = br.readLine()) {
            // If a query is encountered, expand it using the QueryExpander and 
            // write the output to the output document
            if(readLine.startsWith("<title>")) {
                System.out.format("Query #%d\n",++nrQuery);
                String query = readLine.substring(8).trim();
                String expandedQuery = qe.expandQuery(query, (wikiResults == null) ? null : wikiResults.get(nrQuery-1));
                bw.write("<title> " + expandedQuery);
            }
            // Otherwise, write the line to the output file without modification
            else
                bw.write(readLine);
            // Move the writer to a new line
            bw.newLine();
        }
        
        // Close the BufferedReader and the BufferedWriter
        br.close();
        bw.close();
        System.out.println("Finished. Closed reader and writer.\n");
    }

}