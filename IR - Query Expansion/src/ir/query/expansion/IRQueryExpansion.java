package ir.query.expansion;

import static ir.query.expansion.DBpediaQuery.getDBpediaConnections;
import static ir.query.expansion.WikipediaQuery.getWikipediaResults;
import static ir.query.expansion.LevenshteinDistance.computeNormalizedLevenshteinDistance;
import static ir.query.expansion.Tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.lucene.queryparser.classic.ParseException;
/**
 *
 * @author Max Driessen, s4789628
 */
public class IRQueryExpansion {
    
    static final String QUERY = "Hiroshima Nagasaki";
    static final int WIKI_LIMIT = 30;
    static final int DBPEDIA_LIMIT = 20000;
    static final double EDIT_DISTANCE_LIMIT = 0.5;
    static final int K = 3;

    /**
     * 
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(expandQuery(QUERY));
    }
    
    public static String expandQuery(String query) throws IOException, ParseException {
        ArrayList<String> wikipediaResults = getWikipediaResults(query, WIKI_LIMIT);
        ArrayList<String> dbpediaResults = getDBpediaConnections(wikipediaResults.get(0), DBPEDIA_LIMIT);
        
        ArrayList<String> candidates = getIntersection(wikipediaResults, dbpediaResults);

        ESARanker ranker = new ESARanker("termdoc");
        ranker.rank(query, candidates);
        
        ArrayList<String> filteredCandidates = filter(query, candidates);
        List<String> topKCandidates = filteredCandidates.subList(0, Math.min(K,filteredCandidates.size()));
        
        Set<String> expansionTerms = removeDuplicates(query, topKCandidates);
        ArrayList<String> finalExpansionTerms = removeStopWords(expansionTerms, ranker);           
        
        StringBuilder sb = new StringBuilder(query);
        for(String term : finalExpansionTerms)
            sb.append(" ").append(term);
        
        return sb.toString();
    }
    
    private static ArrayList<String> filter(String query, ArrayList<String> candidates){
        ArrayList<String> filteredList = new ArrayList<>();
        filteredList.add(query);
        for(int i = 0; i<candidates.size(); i++){
            String currentCandidate = candidates.get(i);
            if (findLowestLevenshteinDistance(currentCandidate, filteredList) > EDIT_DISTANCE_LIMIT)
                filteredList.add(currentCandidate);
        }
        filteredList.remove(query);
        return filteredList;
    }
    
    private static float findLowestLevenshteinDistance(String s, ArrayList<String> toMatch){
        float min = 1;
        for(String other : toMatch){
            float dist = computeNormalizedLevenshteinDistance(s, other);
            if(dist < min)
                min = dist;
        }
        return min;
    }
    
    private static Set<String> removeDuplicates(String query, List<String> candidates) {  
        // Create bag of words (bow) and remove all query words from it
        Set<String> bow = candidates.stream()
                                    .flatMap(candidate -> Stream.of(candidate.toLowerCase().replaceAll("\\p{P}", "").split("\\s+")))
                                    .collect(Collectors.toSet());
        // Replaces all punctuation by empty word and splits on (multiple) whitespace
        String[] qWords = query.toLowerCase().replaceAll("\\p{P}", "").split("\\s+");
        for( String qWord : qWords )
            if( bow.contains(qWord) )
                bow.remove(qWord);
        
        return bow;
    }
    
    private static ArrayList<String> removeStopWords(Set<String> terms, ESARanker ranker){
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> stopWords = ranker.getStopWords();
        for(String term : terms)
            if(!stopWords.contains(term))
                result.add(term);
        return result;
    }
    
}
