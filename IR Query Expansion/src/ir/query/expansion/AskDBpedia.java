package ir.query.expansion;

import static ir.query.expansion.Tools.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Class for getting all documents connected to a particular DBpedia document, 
 * via both incoming and outgoing connections.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
 */
public class AskDBpedia {
   
    /**
     * Queries DBpedia to obtain all documents that are linked to a given 
     * document via incoming or outgoing connections.
     * 
     * @param docTitle - Title of the current document
     * @param limit - The maximum number of results to be retrieved
     * @return - ArrayList of titles of all results
     * @throws IOException 
     */
    public static ArrayList<String> getDBpediaConnections(String docTitle, int limit) throws IOException{
        // Escape all non-alphanumeric characters and replace all whitespaces with underscores
        String underscoredDocTitle = escapeNonAlphanumeric(docTitle).replaceAll(" ", "_");
        
        // Create the queries for outgoing and incoming connections
        String objQuery =  "PREFIX : <http://dbpedia.org/resource/> "+
                           "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
                           "select ?description where { :"+underscoredDocTitle+" ?p ?o . ?o rdfs:label ?description . FILTER (LANG(?description) = 'en') .} "+
                           "LIMIT "+limit;
        String subjQuery = "PREFIX : <http://dbpedia.org/resource/> "+
                           "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
                           "select ?description where { ?s ?p :"+underscoredDocTitle+" . ?s rdfs:label ?description . FILTER (LANG(?description) = 'en') .} "+
                           "LIMIT "+limit;
        
        // Transform the queries into URLs that can be used with the "curl" command to get results
        String objUrl = "http://dbpedia.org/sparql?query="+URLEncoder.encode(objQuery, "UTF-8");
        String subjUrl = "http://dbpedia.org/sparql?query="+URLEncoder.encode(subjQuery, "UTF-8");
        
        // Query DBpedia using "curl"
        String objResult = isMac() ? execCmd("curl "+objUrl) : execCmd("curl \""+objUrl+"\"");
        String subjResult = isMac() ? execCmd("curl "+subjUrl) : execCmd("curl \""+subjUrl+"\"");

        // Parse and combine the results into a single ArrayList
        ArrayList<String> titles = parseDBpediaTitles(objResult);
        titles.addAll(parseDBpediaTitles(subjResult));
        
        // If the original docTitle is not in the list of titles, add it
        if(!titles.contains(docTitle))
            titles.add(docTitle);
        
        // Return the of titles
        return titles;
    }
    
    /**
     * Parses the output of the DBpedia query to obtain a list of titles.
     * 
     * @param cmdOutput - Output of a DBpedia query in HTML format
     * @return - ArrayList containing the titles of the obtained results
     */
    private static ArrayList<String> parseDBpediaTitles(String cmdOutput){
        String[] lines = cmdOutput.split("\n");
        ArrayList<String> labels = new ArrayList<>();
        for(String line:lines)
            if(line.contains("binding name"))
                labels.add(line.substring(54, line.length()-20));
        return labels;
    }
    
}