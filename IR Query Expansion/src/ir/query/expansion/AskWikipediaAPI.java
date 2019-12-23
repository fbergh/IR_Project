package ir.query.expansion;

import static ir.query.expansion.Tools.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for querying Wikipedia, using the Wikipedia API.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class AskWikipediaAPI {

    /**
     * Queries the Wikipedia API for a given query, and returns a list of 
     * resulting titles.
     * 
     * @param query - The query that will be issued to Wikipedia
     * @param limit - The number of results to return
     * @return - ArrayList of retreived document titles
     * @throws IOException 
     */
    public static ArrayList<String> getWikipediaResults(String query, int limit) throws IOException {
        // Encode the query for usage in an URL, and add it to the URL that will be used to query Wikipedia, together with the limit
        String urlEncodedQuery = URLEncoder.encode(query, "UTF-8");
        String url = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+urlEncodedQuery+"&srlimit="+limit+"&format=json";
        
        // Retrieve the JSON results using the "curl" command
        String jsonResult = isMac() ? execCmd("curl "+url) : execCmd("curl \""+url+"\"");
       
        // Parse the titles from the JSON result and return them
        ArrayList<String> titles = parseJSONTitles(jsonResult);
        return titles;
    }
    
    /**
     * Parses a list of Wikipedia titles from the Wikipedia API results.
     * 
     * @param jsonResult - The Wikipedia API results, in JSON format
     * @return - ArrayList of parsed titles
     */
    private static ArrayList<String> parseJSONTitles(String jsonResult){
        ArrayList<String> titles = new ArrayList<>();
        Matcher m = Pattern.compile("\"title\":\"[^\"]*\"").matcher(jsonResult);
        while(m.find()){
            String result = m.group();
            titles.add(result.substring(9, result.length()-1));
        }
        return titles;
    }

}