/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.query.expansion;

import org.apache.lucene.analysis.util.CharArraySet;
import be.vanoosten.esa.tools.Vectorizer;
import be.vanoosten.esa.tools.SemanticSimilarityTool;
import be.vanoosten.esa.WikiAnalyzer;
import be.vanoosten.esa.WikiFactory;
import be.vanoosten.esa.EnwikiFactory;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
import static org.apache.lucene.util.Version.LUCENE_48;

/**
 *
 * @author dries
 */
public class ESARanking {
    public static void test() throws IOException, ParseException {
        WikiFactory factory = new EnwikiFactory();
        CharArraySet stopWords = factory.getStopWords();
        WikiAnalyzer an = new WikiAnalyzer(LUCENE_48, stopWords);
        File termDocIndexDirectory = new File("termdoc");
        Vectorizer vec = new Vectorizer(termDocIndexDirectory, an);
        SemanticSimilarityTool sim = new SemanticSimilarityTool(vec);
        double score0 = sim.findSemanticSimilarity("Pyramid", "Egypt");
        System.out.println("Pyramid  + Egypt      = "+score0);
    }
}
