package ir.query.expansion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class containing some utility functions.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class Tools {
    
    /**
     * Executes a given command and returns the result.
     * 
     * @param cmd - The command in String format
     * @return - Result (in String format) if there is one, otherwise an empty String
     * @throws IOException 
     */
    public static String execCmd(String cmd) throws IOException {
        Process proc = Runtime.getRuntime().exec(cmd);
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String val = s.hasNext() ? s.next() : "";
        return val;
    }
    
    /**
     * Checks whether the code is being run on MacOS.
     * 
     * @return - true if the computer is a Mac, false otherwise
     */
    public static boolean isMac() {
        return System.getProperty("os.name").startsWith("Mac");
    }
    
    /**
     * Computes the intersection of two ArrayLists of Strings and returns it.
     * 
     * @param a
     * @param b
     * @return - The intersection of a and b
     */
    public static ArrayList<String> getIntersection(ArrayList<String> a, ArrayList<String> b){
        ArrayList<String> intersection = new ArrayList<>();
        intersection.addAll(a);
        intersection.retainAll(b);
        return intersection;
    }
    
    /**
     * Puts an escape character (\) in front of all non-alphanumeric characters
     * of the input String.
     * 
     * @param input
     * @return - The String in which non-alphanumeric characters have been 
     *           escaped
     */
    public static String escapeNonAlphanumeric(String input){
        StringBuilder sb = new StringBuilder("");
        for(String c : input.split("")){
            if(c.matches("[^A-Za-z0-9 ]"))
                sb.append("\\");
            sb.append(c);
        }
        return sb.toString();
    }
    
    /**
     * Parses an ArrayList of String arrays containing Wikipedia results obtained
     * from the Wikipedia index (using the "IR Wikipedia Index" project).
     * 
     * @param in - The filename of the input file where Wikipedia results were stored
     * @param limit - The number of results to retrieve per query
     * @return - The parsed Wikipedia results
     * @throws IOException 
     */
    public static ArrayList<String[]> parseWikipediaIndexResult(String in, int limit) throws IOException {
        ArrayList<String[]> results = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(in));
        for(String readLine = br.readLine(); readLine != null; readLine = br.readLine())
            results.add(Arrays.copyOfRange(readLine.split(";;;"), 0, Math.min(limit, readLine.split(";;;").length)));
        return results;
    }
    
}
