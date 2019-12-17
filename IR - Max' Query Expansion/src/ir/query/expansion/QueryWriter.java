package ir.query.expansion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for writing expanded TREC queries to a new file in the same format
 * 
 * @author Freek van den Bergh, s4801709
 */
public class QueryWriter 
{
    private BufferedWriter bw;
    private BufferedReader br;
    private static final String Q = "Slavery";
    
    /**
     * Constructor for the writer
     * @param inFilename - The input file from which the original queries are extracted
     * @param outFilename - The output file to which the expanded queries are written
     */
    public QueryWriter(String inFilename, String outFilename) {
        try
        {
            bw = new BufferedWriter(new FileWriter(outFilename));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
        try
        {
            br = new BufferedReader(new FileReader(inFilename));
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Write expanded TREC queries to a new file in the same format
     * TODO: add query expansion
     */
    public void writeExpandedQueries(String wikiDumpPath, String queriesPath) {
        System.out.println("Begin writing expanded queries.\n");
        try
        {
            String readLine = br.readLine();
            
            while( readLine != null ) {
                if( readLine.startsWith("<title>") && readLine.length() > 7) {
                    String query = readLine.substring(8);
                    System.out.println("Original query: "+query);
                    String expansionTerms = "Jan Peter"; // Add expansion function here
//                    System.out.println("Levenshtein distance: "+(QueryExpansion.computeLevenshteinDistance(query, Q)/(float)Q.length()));
                    readLine += expansionTerms;
                    System.out.println("Expanded query: "+readLine.substring(8) + "\n");
                }
                bw.write(readLine);
                bw.newLine();
                readLine = br.readLine();
            }
            
            bw.close();
            br.close();
            System.out.println("Finished. Closed reader and writer.");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
