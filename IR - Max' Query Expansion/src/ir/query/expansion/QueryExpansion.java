package ir.query.expansion;

import static ir.query.expansion.DBpediaQuery.getDBpediaConnections;
import static ir.query.expansion.WikipediaQuery.getWikipediaResults;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * @author Freek van den Bergh, s4801709
 */
public class QueryExpansion 
{
    static final String QUERY = "Hiroshima Nagasaki";
   
    /**
     * Gets the expansion words from the top-K candidates retrieved from Wikipedia 
     * and DBPedia for a given query.
     * @param k - The number of candidates from which expansion words will be retrieved
     * @param query - The query for which expansion words will be retrieved
     * @param wikiLimit - The number of Wikipedia results that will be retrieved maximally
     * @param dbpediaLimit - The number of DBPedia results that will be retrieved maximally
     * @throws IOException
     * @throws ParseException 
     */
    public static void getExpansionWords(int k, String query, int wikiLimit, int dbpediaLimit) throws IOException, ParseException {
        ArrayList<String> wikipediaResults = getWikipediaResults(query, wikiLimit);
        ArrayList<String> dbpediaResults = getDBpediaConnections(wikipediaResults.get(0), dbpediaLimit);
        
        ArrayList<String> candidates = getIntersection(wikipediaResults, dbpediaResults);
        
        ESARanking ranker = new ESARanking("termdoc");
        ArrayList<Pair<String, Float>> esaScores = ranker.rank(query, candidates);
        System.out.println(esaScores);
        List<Pair<String, Float>> candidatesTopK = filter(k, query, esaScores);
        System.out.println(candidatesTopK);
        Set<String> expansionWords = removeDuplicates(query, candidatesTopK);
        System.out.println(expansionWords);
    }

    /**
     * Filters the list of candidates on their Levenshtein/edit distance to the query
     * and subsequently retrieves the top-K candidates.
     * @param k - The number of candidates from which expansion words will be retrieved
     * @param query - The query with which the Levenshtein/edit distance is computed
     * @param esaScores - The (sorted) list of candidates that will be filtered
     * @return - A (sorted) list of the top-K candidates with their ESA scores
     */
    private static List<Pair<String, Float>> filter( int k, String query, ArrayList<Pair<String, Float>> esaScores )
    {
        for( int i=esaScores.size()-1; i>=0; i--) {
            String candidate = esaScores.get(i).getElement0();
            float normDist = Tools.computeNormalizedLevenshteinDistance(query, candidate);
            if(normDist < 0.5) // Threshold for Levenshtein distance
                esaScores.remove(i);
        }
        return esaScores.subList(0, k);
    }
     
    /**
     * Removes the duplicate words among the candidates and the words that are in the
     * query from the candidates.
     * @param query - The query which will be removed from the candidates
     * @param candidates - The candidates that will be filtered on duplicates
     * @return - A set of non-duplicate strings, i.e. the expansion words
     */
    private static Set<String> removeDuplicates(String query, List<Pair<String, Float>> candidates) {  
        // Create bag of words (bow) and remove all query words from it
        Set<String> bow = candidates.stream()
                                    .map(pair -> pair.getElement0())
                                    .flatMap(candidate -> Stream.of(candidate.replaceAll("\\p{P}", "").split("\\s+")))
                                    .collect(Collectors.toSet());
        // Replaces all punctuation by empty word and splits on (multiple) whitespace
        String[] qWords = query.replaceAll("\\p{P}", "").split("\\s+");
        for( String qWord : qWords )
            if( bow.contains(qWord) )
                bow.remove(qWord);
        
        return bow;
    }
    
    private static ArrayList<String> getIntersection(ArrayList<String> a, ArrayList<String> b){
        ArrayList<String> intersection = new ArrayList<>();
        intersection.addAll(a);
        intersection.retainAll(b);
        return intersection;
    }
    
    public static void main(String[] args) throws IOException, ParseException {
        QueryExpansion.getExpansionWords(5, QueryExpansion.QUERY, IRQueryExpansion.WIKI_LIMIT, IRQueryExpansion.DBPEDIA_LIMIT);
    }
}
