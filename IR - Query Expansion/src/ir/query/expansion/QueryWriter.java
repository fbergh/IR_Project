package ir.query.expansion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Class for writing expanded TREC queries to a new file in the same format
 * 
 * @author Freek van den Bergh, s4801709
 */
public class QueryWriter 
{
    
    /**
     * Write expanded TREC queries to a new file in the same format
     * @param qe - QueryExpander object with which the queries will be expanded
     * @param in - The input filename containing the TREC queries
     * @param out - The output filename where the expanded TREC queries will be written
     * @throws IOException
     * @throws ParseException 
     */
    public static void writeExpandedQueries(QueryExpander qe, String in, String out) throws IOException, ParseException {
        System.out.println("Begin writing expanded queries.\n");
        int nrQuery = 1;
        BufferedReader br = initializeBufferedReader(in);
        BufferedWriter bw = initializeBufferedWriter(out);
        String readLine = br.readLine();
            
        // Read until end of file
        while( readLine != null ) {
            String expandedQuery = "";
            // Valid query only if it is longer than a and has a <title> tag
            if( readLine.startsWith("<title>") && readLine.length() > 7) {
                String query = readLine.substring(8);
                expandedQuery = qe.expandQuery(query);
                System.out.format("Query #%d\n",nrQuery++);
                bw.write("<title> " + expandedQuery);
            }
            else
                bw.write(readLine);
            bw.newLine();
            readLine = br.readLine();
        }

        bw.close();
        br.close();
        System.out.println("Finished. Closed reader and writer.");
    }
    
    private static BufferedReader initializeBufferedReader(String in) {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(in));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return br;
    }
    
    private static BufferedWriter initializeBufferedWriter(String out) {
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter(out));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return bw;
    }
}
