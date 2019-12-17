package ir.query.expansion;

import java.io.IOException;

/**
 *
 * @author Max Driessen, s4789628
 */
public class Tools {
    
    public static String execCmd(String cmd) throws IOException {
        Process proc = Runtime.getRuntime().exec(cmd);
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String val = s.hasNext() ? s.next() : "";
        return val;
    }
    
    public static boolean isMac() {
        return System.getProperty("os.name").startsWith("Mac");
    }
    
}
