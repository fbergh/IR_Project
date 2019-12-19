package ir.query.expansion;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
/**
 *
 * @author Max Driessen, s4789628
 */
public class Main {
    
    static final String QUERY = "Hiroshima Nagasaki";
    static final int WIKI_LIMIT = 30;
    static final int DBPEDIA_LIMIT = 20000;
    static final double EDIT_DISTANCE_THRESHOLD = 0.5;
    static final int K = 3;

    /**
     * 
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        QueryExpander qe = new QueryExpander(K, WIKI_LIMIT, DBPEDIA_LIMIT, EDIT_DISTANCE_THRESHOLD);
        System.out.println(qe.expandQuery(QUERY));
        QueryWriter.writeExpandedQueries(qe, "../topics.robust04.txt", "../topics.robust04.expanded.txt");
    }
    
}
