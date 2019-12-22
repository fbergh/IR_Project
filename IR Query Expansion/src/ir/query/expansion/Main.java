package ir.query.expansion;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Main class of the project; can be used for testing our implementation.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class Main {

    /**
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        QueryExpander qe = new QueryExpander(3, 30, 20000, 0.5, false, false);
        System.out.println(qe.expandQuery("Barack Obama"));
    }
    
}
