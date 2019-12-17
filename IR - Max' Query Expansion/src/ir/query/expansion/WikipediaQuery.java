package ir.query.expansion;

import static ir.query.expansion.Tools.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Max Driessen, s4789628
 */
public class WikipediaQuery {

    public static ArrayList<String> getWikipediaResults(String query, int limit) throws IOException {
        String urlEncodedQuery = URLEncoder.encode(query, "UTF-8");
        String jsonResult;
        if(isMac())
            jsonResult = execCmd("curl https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+urlEncodedQuery+"&srlimit="+limit+"&format=json");
        else
            jsonResult = execCmd("curl \"https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+urlEncodedQuery+"&srlimit="+limit+"&format=json\"");
        
        ArrayList<String> titles = parseJSONTitles(jsonResult);
        
        return titles;
    }
    
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
    
      
   

