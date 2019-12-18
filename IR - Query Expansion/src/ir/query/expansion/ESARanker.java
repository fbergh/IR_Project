package ir.query.expansion;

import org.apache.lucene.analysis.util.CharArraySet;
import be.vanoosten.esa.tools.Vectorizer;
import be.vanoosten.esa.tools.SemanticSimilarityTool;
import be.vanoosten.esa.WikiAnalyzer;
import be.vanoosten.esa.WikiFactory;
import be.vanoosten.esa.EnwikiFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.apache.lucene.queryparser.classic.ParseException;
import static org.apache.lucene.util.Version.LUCENE_48;

/**
 *
 * @author Max Driessen, s4789628
 */
public class ESARanker {
    
    private final SemanticSimilarityTool sim;
    private final CharArraySet stopWords;
    
    public ESARanker(String termDoc) throws IOException {
        WikiFactory factory = new EnwikiFactory();
        stopWords = factory.getStopWords();
        WikiAnalyzer an = new WikiAnalyzer(LUCENE_48, stopWords);
        File termDocIndexDirectory = new File(termDoc);
        Vectorizer vec = new Vectorizer(termDocIndexDirectory, an);
        this.sim = new SemanticSimilarityTool(vec);
    }
    
    public ArrayList<String> getStopWords(){
        ArrayList<String> stopWordStrings = new ArrayList<>();
        Iterator iter = stopWords.iterator();
        while(iter.hasNext()) {
            char[] stopWord = (char[]) iter.next();
            stopWordStrings.add(new String (stopWord));
        }
        return stopWordStrings;
    }
    
    public void rank(String query, ArrayList<String> candidates) throws IOException, ParseException {
        Collections.sort(candidates, new ESARankingComparator(query, sim));
    }
    
    private static class ESARankingComparator implements Comparator<String>{
        
        private final String query;
        private final SemanticSimilarityTool sim;
        
        public ESARankingComparator(String query, SemanticSimilarityTool sim){
            this.query = query; 
            this.sim = sim;
        }

        @Override
        public int compare( String a, String b ){
            try{
                return (int) Math.signum(sim.findSemanticSimilarity(query, b) - sim.findSemanticSimilarity(query, a));
            }catch(ParseException e){
                System.out.println("Parse Exception while comparing ESA ranks");
                return 0;
            }catch(IOException e){
                System.out.println("IO Exception while comparing ESA ranks");
                return 0;
            }
        }
    }
}
