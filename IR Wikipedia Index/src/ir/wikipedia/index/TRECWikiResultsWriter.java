package ir.wikipedia.index;

import static ir.wikipedia.index.AskWikipediaIndex.getWikipediaResults;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Class for finding the results of the Wikipedia index for all TREC queries.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class TRECWikiResultsWriter {
    
    /**
     * To find the results of the Wikipedia index for all TREC queries, run this file.
     * WARNING: Running this function takes a long time
     * 
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        // Specify where the results should be stored
        String resultLoc = "index_results/";
        
        // Perform all queries using the "title" and "body" fields
        writeWikipediaIndexResults("topics.robust04.txt", resultLoc+"results_title.txt", "title");
        writeWikipediaIndexResults("topics.robust04.txt", resultLoc+"results_body.txt", "body");
    }
    
    /**
     * Retrieves Wikipedia index results for all queries in the TREC dataset.
     * 
     * @param in - The filename of the file containing the TREC queries
     * @param out - The output filename where the expanded TREC queries will be written
     * @param field - The field in which the query should be searched (title/body)
     * @throws IOException
     * @throws ParseException 
     */
    public static void writeWikipediaIndexResults(String in, String out, String field) throws IOException, ParseException {
        System.out.println("Begin retrieving results from the Wikipedia index.");
        
        // Create a BufferedReader and a BufferedWriter
        BufferedReader br = new BufferedReader(new FileReader(in));
        BufferedWriter bw = new BufferedWriter(new FileWriter(out));

        // Read until end of file
        int nrQuery = 0;
        for(String readLine = br.readLine(); readLine != null; readLine = br.readLine())
            // If a query is encountered, retrieve the Wikipedia results and 
            // write the output to the output document in the correct format
            if(readLine.startsWith("<title>")) {
                System.out.format("Query #%d\n",++nrQuery);
                String query = readLine.substring(8).trim();
                ArrayList<String> titles = getWikipediaResults(query, field, "wiki_index", 300);
                StringBuilder result = new StringBuilder("");
                for(String title : titles)
                    result.append(title).append(";;;");
                bw.write(result.toString().substring(0, result.toString().length()-3));
                bw.newLine();
            }
        
        // Close the BufferedReader and the BufferedWriter
        br.close();
        bw.close();
        System.out.println("Finished. Closed reader and writer.\n");
    }
    
}