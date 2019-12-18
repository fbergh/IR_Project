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
import org.apache.lucene.queryparser.classic.ParseException;
import static org.apache.lucene.util.Version.LUCENE_48;

/**
 * @author Freek van den Bergh, s4801709
 */
public class ESARanking {
    
    private SemanticSimilarityTool sim;
    
    /**
     * Initialises the ESA Ranker
     * @param termDoc - The path to the term-to-document index
     * @throws IOException 
     */
    public ESARanking(String termDoc) throws IOException {
        WikiFactory factory = new EnwikiFactory();
        CharArraySet stopWords = factory.getStopWords();
        WikiAnalyzer an = new WikiAnalyzer(LUCENE_48, stopWords);
        File termDocIndexDirectory = new File(termDoc);
        Vectorizer vec = new Vectorizer(termDocIndexDirectory, an);
        this.sim = new SemanticSimilarityTool(vec);
    }
    
    /**
     * Ranks and sorts (in descending order) the list of query expansion candidates
     * by their ESA score.
     * @param query
     * @param candidates
     * @return - A sorted ArrayList of candidate-ESA_score pairs
     * @throws ParseException
     * @throws IOException 
     */
    public ArrayList<Pair<String, Float>> rank(String query, ArrayList<String> candidates) throws ParseException, IOException {
        ArrayList<Pair<String, Float>> esaScores = new ArrayList();
        for(String c : candidates)
            esaScores.add(Pair.createPair(c, sim.findSemanticSimilarity(query, c)));
        
        Collections.sort(esaScores, new ESARankingComparator());
        return esaScores;
    }

    /**
     * Comparator for sorting the candidates on ESA scores in descending order
     */
    private static class ESARankingComparator implements Comparator<Pair<String, Float>>
    {
        public ESARankingComparator(){}

        @Override
        public int compare( Pair<String, Float> o1, Pair<String, Float> o2 )
        {
            return (int)Math.signum(o2.getElement1()-o1.getElement1());
        }
    }
}
