package ir.query.expansion;

import java.io.IOException;
import java.util.ArrayList;

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
    
}
