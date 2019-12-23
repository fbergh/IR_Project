package ir.wikipedia.index;

import static ir.wikipedia.index.AskWikipediaIndex.getWikipediaResults;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * The main class of this project.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        issueIndexQuery("Hiroshima Nagasaki", "index_results/query_result.txt", "title");
    }
    
    /**
     * Issues a single query to the Wikipedia index, prints the resulting
     * documents, and stores the result in a text file for use in the main
     * project.
     * 
     * @param query - The query
     * @param out - Location of the output text file
     * @param field - The field in which the query should be searched (title/body)
     * @throws IOException
     * @throws ParseException 
     */
    private static void issueIndexQuery(String query, String out, String field) throws IOException, ParseException {
        // Create a new BufferedWriter
        BufferedWriter bw = new BufferedWriter(new FileWriter(out));
        
        // Issue the query and print the results
        ArrayList<String> titles = getWikipediaResults(query, field, "wiki_index", 300);
        System.out.println(titles);
        
        // Write the result to the text file in the correct format
        StringBuilder result = new StringBuilder("");
        for(String title : titles)
            result.append(title).append(";;;");
        bw.write(result.toString().substring(0,result.toString().length()-3));
        
        // Close the BufferedWriter
        bw.close();
    }
    
}