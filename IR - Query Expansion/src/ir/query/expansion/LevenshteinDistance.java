package ir.query.expansion;

/**
 *
 * @author Freek van den Bergh, s4801709
 */
public class LevenshteinDistance {
    
    /**
     * Calculates minimum of three integers.
     * Retrieved from https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     * @param a
     * @param b
     * @param c
     * @return - Minimum of three integers
     */
    private static int minimum(int a, int b, int c) {                            
        return Math.min(Math.min(a, b), c);                                      
    }                                                                            
                 
    /**
     * Computes the Levenshtein/edit distance between two character sequences.
     * Retrieved from https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     * @param lhs
     * @param rhs
     * @return - Integer of Levenshtein distance between two character sequences
     */
    public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {      
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];        
                                                                                 
        for (int i = 0; i <= lhs.length(); i++)                                 
            distance[i][0] = i;                                                  
        for (int j = 1; j <= rhs.length(); j++)                                 
            distance[0][j] = j;                                                  
                                                                                 
        for (int i = 1; i <= lhs.length(); i++)                                 
            for (int j = 1; j <= rhs.length(); j++)                             
                distance[i][j] = minimum(                                        
                        distance[i - 1][j] + 1,                                  
                        distance[i][j - 1] + 1,                                  
                        distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));
                                                                                 
        return distance[lhs.length()][rhs.length()];                           
    }
    
    /**
     * Computes the normalised Levenshtein distance between two character sequences
     * by dividing the normal Levenshtein distance by the length of the longest 
     * character sequence.
     * @param lhs
     * @param rhs
     * @return - Float (range [0,1]) of normalised Levenshtein distance between 
     *           two characters sequences
     */
    public static float computeNormalizedLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int lshtDist = computeLevenshteinDistance(lhs, rhs);
        float normalize = Math.max(lhs.length(), rhs.length());
        return lshtDist/normalize;
    }
    
}
