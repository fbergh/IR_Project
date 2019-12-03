package ir.cranfield.query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Freek van den Bergh, s4801709
 */
public class CranfieldParser 
{
    private final String DATASET = "../data/cran/cran.all.1400.trec-format";
    
    public CranfieldParser() {}
    
    /**
     * Retrieves the content of a document given the ID of the document
     * @param docID - ID of the document
     * @return - string with the content of the document
     * @throws IOException 
     */
    public String getDocumentContent(int docID) throws IOException {
        if( docID < 1 )
            throw new IllegalArgumentException("Document ID < 1");
        
        BufferedReader docReader = new BufferedReader(new FileReader(DATASET));
        
        String curLine = docReader.readLine();
        StringBuilder content = new StringBuilder();
        
        // Look for correct document
        while( !curLine.contains("<DOCNO>"+docID+"</DOCNO>") )
            curLine = docReader.readLine();
        
        // Look for document content
        while( !curLine.contains(".W") )
            curLine = docReader.readLine();
        
        // Append content until end of document
        while( !curLine.contains("</TEXT>")) {
            curLine = docReader.readLine();
            content.append(" "+curLine);
        }
        
        return content.toString();
    }
}
