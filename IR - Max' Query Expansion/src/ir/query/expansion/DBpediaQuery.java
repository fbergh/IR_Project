package ir.query.expansion;

import static ir.query.expansion.Tools.execCmd;
import static ir.query.expansion.Tools.isMac;
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
        String urlEncodedDocTitle = URLEncoder.encode(underscoredDocTitle, "UTF-8");
        String outgoing, incoming;
        if(isMac()){
            outgoing = execCmd("curl http://dbpedia.org/sparql?query=PREFIX%20%3A%20%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%20"
                                       + "PREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%20"
                                       + "select%20%3Fdescription%20where%20%7B%20%3A"+urlEncodedDocTitle+"%20%3Fp%20%3Fo%20.%20%3Fo%20rdfs%3Alabel%20%3Fdescription%20."
                                       + "%20FILTER%20%28LANG%28%3Fdescription%29%20%3D%20%27en%27%29%20.%7D%20LIMIT%20"+limit);
            incoming = execCmd("curl http://dbpedia.org/sparql?query=PREFIX%20%3A%20%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%20"
                                       + "PREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%20"
                                       + "select%20%3Fdescription%20where%20%7B%20%3Fs%20%3Fp%20%3A"+urlEncodedDocTitle+"%20.%20%3Fs%20rdfs%3Alabel%20%3Fdescription%20."
                                       + "%20FILTER%20%28LANG%28%3Fdescription%29%20%3D%20%27en%27%29%20.%7D%20LIMIT%20"+limit);
        }
        else {
            outgoing = execCmd("curl \"http://dbpedia.org/sparql?query=PREFIX%20%3A%20%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%20"
                                       + "PREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%20"
                                       + "select%20%3Fdescription%20where%20%7B%20%3A"+urlEncodedDocTitle+"%20%3Fp%20%3Fo%20.%20%3Fo%20rdfs%3Alabel%20%3Fdescription%20."
                                       + "%20FILTER%20%28LANG%28%3Fdescription%29%20%3D%20%27en%27%29%20.%7D%20LIMIT%20"+limit+"\"");
            incoming = execCmd("curl \"http://dbpedia.org/sparql?query=PREFIX%20%3A%20%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%20"
                                       + "PREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%20"
                                       + "select%20%3Fdescription%20where%20%7B%20%3Fs%20%3Fp%20%3A"+urlEncodedDocTitle+"%20.%20%3Fs%20rdfs%3Alabel%20%3Fdescription%20."
                                       + "%20FILTER%20%28LANG%28%3Fdescription%29%20%3D%20%27en%27%29%20.%7D%20LIMIT%20"+limit+"\"");
        }
        ArrayList<String> labels = parseHTMLTitles(outgoing);
        labels.addAll(parseHTMLTitles(incoming));
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
