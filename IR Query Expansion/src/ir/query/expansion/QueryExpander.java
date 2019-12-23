package ir.query.expansion;

import static ir.query.expansion.AskDBpedia.getDBpediaConnections;
import static ir.query.expansion.AskWikipediaAPI.getWikipediaResults;
import static ir.query.expansion.LevenshteinDistance.computeNormalizedLevenshteinDistance;
import static ir.query.expansion.Tools.getIntersection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Class for expanding queries using Wikipedia and DBpedia.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class QueryExpander { 
    
    private final int K;
    private final int WIKI_LIMIT;
    private final int DBPEDIA_LIMIT;
    private final double EDIT_DISTANCE_THRESHOLD;
    private final boolean REMOVE_DUPLICATES;
    private final boolean REMOVE_STOPWORDS;
    
    public QueryExpander(int k, int wikiLimit, int dbpediaLimit, double editDistanceThreshold, boolean removeDuplicates, boolean removeStopWords) {
        this.K = k;
        this.WIKI_LIMIT = Math.min(wikiLimit,50);
        this.DBPEDIA_LIMIT = dbpediaLimit;
        this.EDIT_DISTANCE_THRESHOLD = editDistanceThreshold;
        this.REMOVE_DUPLICATES = removeDuplicates;
        this.REMOVE_STOPWORDS = removeStopWords;
    }
    
    /**
     * Builder function that allows expandQuery to be called without wikiResults
     * (to use the Wikipedia API).
     * 
     * @param query - The initial query
     * @return - The expanded query
     * @throws IOException
     * @throws ParseException 
     */
    public String expandQuery(String query) throws IOException, ParseException {
        return expandQuery(query, null);
    }
    
    /**
     * Expands the given query using Wikipedia and DBpedia.
     * 
     * @param query - The input query
     * @param wikiResults - The results from the Wikipedia index, if the index
     *                      is used instead of the API
     * @return - The expanded query
     * @throws IOException
     * @throws ParseException 
     */
    public String expandQuery(String query, String[] wikiResults) throws IOException, ParseException {
        // Retrieve the top results from Wikipedia
        ArrayList<String> wikipediaResults = (wikiResults == null) ? getWikipediaResults(query, WIKI_LIMIT) : new ArrayList<>(Arrays.asList(wikiResults));

        // Use the top result to retrieve titles of connected documents from DBpedia
        ArrayList<String> dbpediaResults = getDBpediaConnections(wikipediaResults.get(0), DBPEDIA_LIMIT);
        
        // Compute the intersection of the Wikipedia and DBpedia results to obtain a list of candidates
        ArrayList<String> candidates = getIntersection(wikipediaResults, dbpediaResults);
        
        // Sort the candidates on their semantic similarity to the query
        ESARanker ranker = new ESARanker("termdoc");
        ranker.rank(query, candidates);
        
        // Filter the candidates on edit distance and select the top k candidates as final candidates
        ArrayList<String> filteredCandidates = filterEditDistance(query, candidates);
        ArrayList<String> finalCandidates = new ArrayList<>(filteredCandidates.subList(0, Math.min(K,filteredCandidates.size())));
        
        // Split the final candidates into words
        ArrayList<String> finalCandidateTerms = new ArrayList<>();
        for(String candidate : finalCandidates){
            ArrayList<String> words = new ArrayList<>(Arrays.asList(candidate.toLowerCase().replaceAll("\\p{P}", "").split("\\s+")));
            finalCandidateTerms.addAll(words);
        }
        
        // Remove stopwords and/or duplicates, if needed
        if(REMOVE_DUPLICATES)
            finalCandidateTerms = removeDuplicates(query, finalCandidateTerms);
        if(REMOVE_STOPWORDS)
            finalCandidateTerms = removeStopWords(finalCandidateTerms, ranker);           
        
        // Build the final expanded query and return it
        StringBuilder sb = new StringBuilder(query);
        for(String term : finalCandidateTerms)
            sb.append(" ").append(term);
        return sb.toString();
    }
    
    /**
     * Filters the list of candidates based on their edit distance (Levenshtein
     * distance) to the query and to the candidates that have already passed
     * through the filter.
     * 
     * @param query - The query
     * @param candidates - The list of candidates that needs to be filtered
     * @return - The filtered list of candidates
     */
    private ArrayList<String> filterEditDistance(String query, ArrayList<String> candidates){
        // Create a list that will store the result and add the query to that list (for ease of implementation)
        ArrayList<String> filteredList = new ArrayList<>();
        filteredList.add(query);
        
        // Loop through all candidates
        for(int i = 0; i<candidates.size(); i++){
            // If there is no candidate in the filteredList such that its edit distance to the current 
            // candidate is lower than the threshold, add the current candidate to the filteredList 
            String currentCandidate = candidates.get(i);
            if (findLowestLevenshteinDistance(currentCandidate, filteredList) > EDIT_DISTANCE_THRESHOLD)
                filteredList.add(currentCandidate);
        }
        
        // Remove the original query from the results, and return the remaining results
        filteredList.remove(query);
        return filteredList;
    }
    
    /**
     * Finds the lowest Levenshtein distance between given String and the elements 
     * of a list of Strings.
     * 
     * @param s - The String to which all elements of the ArrayList should be compared
     * @param toMatch - The ArrayList
     * @return - The lowest Levenshtein distance found
     */
    private static float findLowestLevenshteinDistance(String s, ArrayList<String> toMatch){
        float min = 1;
        for(String other : toMatch){
            float dist = computeNormalizedLevenshteinDistance(s, other);
            if(dist < min)
                min = dist;
        }
        return min;
    }
    
    /**
     * Removes duplicate words from a given list of candidates.
     * 
     * @param query - The original query
     * @param candidateWords - The list of candidate words
     * @return - A list of candidate words, without duplicates
     */
    private static ArrayList<String> removeDuplicates(String query, ArrayList<String> candidateWords) {  
        // Remove all punctuation from the query and split on (multiple) whitespace
        List<String> qWords = Arrays.asList(query.toLowerCase().replaceAll("\\p{P}", "").split("\\s+"));

        // Create a list of final candidate words and loop through the candidates
        ArrayList<String> cWords = new ArrayList<>();
        for(String cw : candidateWords)
            // For each candidate word, if the word is not in the query or the 
            // final candidate words, add it to the final candidate words
            if(!qWords.contains(cw) && !cWords.contains(cw))
                cWords.add(cw);

        // Return the list of candidate words without duplicates
        return cWords;
    }
    
    /**
     * Removes stopwords from an ArrayList of candidate words.
     * 
     * @param candidateWords - The original ArrayList of candidate words
     * @param ranker - The ESA ranker from which a list of stopwords can be retrieved
     * @return - The ArrayList in which stopwords have been removed
     */
    private static ArrayList<String> removeStopWords(ArrayList<String> candidateWords, ESARanker ranker){
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> stopWords = ranker.getStopWords();
        for(String term : candidateWords)
            if(!stopWords.contains(term))
                result.add(term);
        return result;
    }

}
