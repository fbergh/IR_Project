package ir.query.expansion;

import static ir.query.expansion.Tools.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 *
 * @author Max Driessen, s4789628
 */
public class DBpediaQuery {
   
    public static ArrayList<String> getDBpediaConnections(String docTitle, int limit) throws IOException{
        String underscoredDocTitle = docTitle.replaceAll(" ", "_");
        
        String objCommand =  "PREFIX : <http://dbpedia.org/resource/> "+
                             "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
                             "select ?description where { :"+underscoredDocTitle+" ?p ?o . ?o rdfs:label ?description . FILTER (LANG(?description) = 'en') .} "+
                             "LIMIT "+limit;
        String subjCommand = "PREFIX : <http://dbpedia.org/resource/> "+
                             "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
                             "select ?description where { ?s ?p :"+underscoredDocTitle+" . ?s rdfs:label ?description . FILTER (LANG(?description) = 'en') .} "+
                             "LIMIT "+limit;
        
        String objUrl = "http://dbpedia.org/sparql?query="+URLEncoder.encode(objCommand, "UTF-8");
        String subjUrl = "http://dbpedia.org/sparql?query="+URLEncoder.encode(subjCommand, "UTF-8");
        
        String objects = isMac() ? execCmd("curl "+objUrl) : execCmd("curl \""+objUrl+"\"");
        String subjects = isMac() ? execCmd("curl "+subjUrl) : execCmd("curl \""+subjUrl+"\"");

        ArrayList<String> labels = parseHTMLTitles(objects);
        labels.addAll(parseHTMLTitles(subjects));
        return labels;
    }
    
    private static ArrayList<String> parseHTMLTitles(String cmdOutput){
        String[] lines = cmdOutput.split("\n");
        ArrayList<String> labels = new ArrayList<>();
        for(String line:lines)
            if(line.contains("binding name"))
                labels.add(line.substring(54, line.length()-20));
        return labels;
    }
    
}
