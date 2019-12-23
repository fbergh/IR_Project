package ir.wikipedia.index;

/**
 * Class containing some utility functions.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class Tools {
    
    /**
     * Puts an escape character (\) in front of all non-alphanumeric characters
     * of the input String.
     * 
     * @param input
     * @return - The String in which non-alphanumeric characters have been escaped
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