package ir.query.expansion;

import static ir.query.expansion.DBpediaQuery.getDBpediaConnections;
import static ir.query.expansion.WikipediaQuery.getWikipediaResults;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author Max Driessen, s4789628
 */
public class IRQueryExpansion {
    
    static final String QUERY = "Hiroshima Nagasaki";
    static final int WIKI_LIMIT = 30;
    static final int DBPEDIA_LIMIT = 20000;

    /**
     * 
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        ArrayList<String> wikipediaResults = getWikipediaResults(QUERY, WIKI_LIMIT);
        System.out.println(wikipediaResults);
        ArrayList<String> dbpediaResults = getDBpediaConnections(wikipediaResults.get(0), DBPEDIA_LIMIT);
        System.out.println(dbpediaResults);
        
        ArrayList<String> candidates = getIntersection(wikipediaResults, dbpediaResults);
        System.out.println(candidates);
    }
    
    private static ArrayList<String> getIntersection(ArrayList<String> a, ArrayList<String> b){
        ArrayList<String> intersection = new ArrayList<>();
        intersection.addAll(a);
        intersection.retainAll(b);
        return intersection;
    }
    
 
}
