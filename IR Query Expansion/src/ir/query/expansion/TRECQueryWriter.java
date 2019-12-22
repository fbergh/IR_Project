package ir.query.expansion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Class for writing expanded TREC queries to a new file in the same format
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class TRECQueryWriter  {
    
    /**
     * To run query expansion on all TREC queries for all conditions, run this file.
     * 
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        String querySourceLoc = "topics.robust04.txt";
        String expandedDestination = "expanded_queries/";
        
        QueryExpander qe1 = new QueryExpander(1, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe1, querySourceLoc, expandedDestination+"k_1.txt", "k = 1");
        
        QueryExpander qe2 = new QueryExpander(3, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe2, querySourceLoc, expandedDestination+"k_3.txt", "k = 3");
        
        QueryExpander qe3 = new QueryExpander(5, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe3, querySourceLoc, expandedDestination+"k_5.txt", "k = 5");
        
        QueryExpander qe4 = new QueryExpander(10, 30, 20000, 0.5, false, false);
        writeExpandedQueries(qe4, querySourceLoc, expandedDestination+"k_10.txt", "k = 10");
        
        QueryExpander qe5 = new QueryExpander(3, 30, 20000, 0.5, true, false);
        writeExpandedQueries(qe5, querySourceLoc, expandedDestination+"stopwords_removed.txt", "stopwords removed");
        
        QueryExpander qe6 = new QueryExpander(3, 30, 20000, 0.5, false, true);
        writeExpandedQueries(qe6, querySourceLoc, expandedDestination+"duplicates_removed.txt", "duplicates removed");
        
        QueryExpander qe7 = new QueryExpander(3, 30, 20000, 0.5, true, true);
        writeExpandedQueries(qe7, querySourceLoc, expandedDestination+"both_removed.txt", "stopwords + duplicates removed");
    }
    
    /**
     * Write expanded TREC queries to a new file in the same format.
     * 
     * @param qe - QueryExpander object with which the queries will be expanded
     * @param in - The filename of the file containing the TREC queries
     * @param out - The output filename where the expanded TREC queries will be written
     * @param condition - The current experimental condition
     * @throws IOException
     * @throws ParseException 
     */
    public static void writeExpandedQueries(QueryExpander qe, String in, String out, String condition) throws IOException, ParseException {
        System.out.println("Begin writing expanded queries for condition "+condition+".\n");
        try (BufferedReader br = initializeBufferedReader(in); BufferedWriter bw = initializeBufferedWriter(out)) {
            int nrQuery = 1;
            // Read until end of file
            String readLine = br.readLine();
            while(readLine != null) {
                // If a query is encountered, expand it using the QueryExpander and write 
                // the output to the output document
                if(readLine.startsWith("<title>")) {
                    String query = readLine.substring(8).trim();
                    String expandedQuery = qe.expandQuery(query);
                    System.out.format("Query #%d\n",nrQuery++);
                    bw.write("<title> " + expandedQuery);
                }
                // Otherwise, write the line to the output file without modification
                else
                    bw.write(readLine);
                // Move the reader and writer to a new line
                bw.newLine();
                readLine = br.readLine();
            }
            
        }
        System.out.println("Finished. Closed reader and writer.\n");
    }
    
    /**
     * Initializes the BufferedReader
     * @param in - The filename of the input file
     * @return - The BufferedReader
     */
    private static BufferedReader initializeBufferedReader(String in) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(in));
        } catch(IOException e) {
            System.out.println("Something went wrong while creating the BufferedReader");
        }
        return br;
    }
    
    /**
     * Initializes the BufferedWriter
     * @param out - The filename of the output file
     * @return - The BufferedWriter
     */
    private static BufferedWriter initializeBufferedWriter(String out) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(out));
        } catch(IOException e) {
            System.out.println("Something went wrong while creating the BufferedWriter");
        }
        return bw;
    }
}