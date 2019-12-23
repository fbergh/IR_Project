package ir.query.expansion;

import static ir.query.expansion.Tools.parseWikipediaIndexResult;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Main class of the project; can be used to test our implementation.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        QueryExpander qe = new QueryExpander(3, 30, 20000, 0.5, false, false);
        
        // Test using the Wikipedia API
        System.out.println(qe.expandQuery("Hiroshima Nagasaki"));
        
        // Test using the Wikipedia index (requires a text file from the "IR Wikipedia Query" project)
        String[] indexResult = parseWikipediaIndexResult("index_results/query_result.txt", 30).get(0);
        System.out.println(qe.expandQuery("Hiroshima Nagasaki", indexResult));
    }
    
}
