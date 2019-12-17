package ir.query.expansion;

import static ir.query.expansion.DBpediaQuery.getDBpediaConnections;
import static ir.query.expansion.WikipediaQuery.getWikipediaResults;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * @author Freek van den Bergh, s4801709
 */
public class QueryExpansion 
{
   
    public void getQueryCandidates(String query, int wikiLimit, int dbpediaLimit) throws IOException, ParseException {
        ArrayList<String> wikipediaResults = getWikipediaResults(query, wikiLimit);
        ArrayList<String> dbpediaResults = getDBpediaConnections(wikipediaResults.get(0), dbpediaLimit);
        
        ArrayList<String> candidates = getIntersection(wikipediaResults, dbpediaResults);
        
        ESARanking ranker = new ESARanking("termdoc");
        ArrayList<Pair<String, Float>> esaScores = ranker.rank(query, candidates);
        System.out.println(esaScores);
    }
    
    private static ArrayList<String> getIntersection(ArrayList<String> a, ArrayList<String> b){
        ArrayList<String> intersection = new ArrayList<>();
        intersection.addAll(a);
        intersection.retainAll(b);
        return intersection;
    }
}
