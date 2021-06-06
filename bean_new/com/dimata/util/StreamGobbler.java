/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 19, 2006
 * Time: 11:31:14 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.util;

import com.dimata.util.log.TXTLogger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class StreamGobbler extends Thread {
    InputStream is;
    String type;
    String strMsg = "";
    String pathFile = "";

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public String getMsg(){
        return strMsg;
    }

    public void setPathFile(String pathFile){
        this.pathFile = pathFile;
    }

    public String getPathFile(){
        return this.pathFile;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
               lineNum++;

                if(getPathFile().length()>0)
                    TXTLogger.logger(getPathFile(),line);
            }
           System.out.println("  >>" + lineNum + " Line of data read");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
