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
import java.util.Iterator;
import org.apache.lucene.queryparser.classic.ParseException;
import static org.apache.lucene.util.Version.LUCENE_48;

/**
 * Class that can be used to order a list of strings in order of decreasing 
 * semantic similarity to a given query String.
 * 
 * @author Freek van den Bergh, s4801709
 * @author Max Driessen, s4789628
 * @author Marlous Nijman, s4551400
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
    
    /**
     * Converts the list of stopwords into an ArrayList of Strings and returns
     * the result.
     * 
     * @return - An ArrayList of stopwords.
     */
    public ArrayList<String> getStopWords(){
        ArrayList<String> stopWordStrings = new ArrayList<>();
        Iterator iter = stopWords.iterator();
        while(iter.hasNext()) {
            char[] stopWord = (char[]) iter.next();
            stopWordStrings.add(new String (stopWord));
        }
        return stopWordStrings;
    }
    
    /**
     * Sorts all candidate Strings in an ArrayList based on their semantic
     * similarity to a given query String.
     * 
     * @param query - The query String
     * @param candidates - The ArrayList that should be ordered
     * @throws IOException
     * @throws ParseException 
     */
    public void rank(String query, ArrayList<String> candidates) throws IOException, ParseException {
        candidates.sort((String a, String b) -> {
            try{
                return (int) Math.signum(sim.findSemanticSimilarity(query, b) - sim.findSemanticSimilarity(query, a));
            }catch(ParseException e){
                System.out.println("Parse Exception while comparing ESA ranks");
            }catch(IOException e){
                System.out.println("IO Exception while comparing ESA ranks");
            }
            return 0;
        });
    }

}