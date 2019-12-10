package ir.query.expansion;

/**
 * @author Freek van den Bergh, s4801709
 */
public class Main 
{

    public final static String QUERIES = "../anserini/src/main/resources/topics-and-qrels/topics.robust04.txt";
    public final static String OUTPUT = "topics.robust04.expanded.txt";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        QueryWriter qw = new QueryWriter(QUERIES, OUTPUT);
        qw.writeExpandedQueries();
    }

}
