/*
 * ExecCommand.java
 *
 * Created on October 8, 2004, 4:15 PM
 */

package com.dimata.util;

import java.io.OutputStream;

/**
 *
 * @author  gedhy
 */
public class ExecCommand {
    String strMsg = "";
    String  pathFile = "";
    public void setPathFile(String pathFile){
        this.pathFile = pathFile;
    }

    public String getPathFile(){
        return this.pathFile;
    }

    public String getMsg(){
        return strMsg;
    }

    /**
     * @param stCommand
     * @return
     */
    public int runCommmand(String stCommand) {
        int result = 0;

        if (stCommand.length() < 1) {
            System.out.println("--- Command script cannot NULL ---");
            System.exit(1);
            return 1;
        }

        try {
            String cmd = stCommand;
            Runtime rt = Runtime.getRuntime();
            System.out.println("cmd >>> : " + cmd);
            Process proc = rt.exec(cmd);

            // any error message?
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERR");

            // any output?
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.setPathFile(getPathFile());
            outputGobbler.start();
            outputGobbler.setPathFile(getPathFile());

            // any error???
            int exitVal = proc.waitFor();
//            System.out.println("ExitValue: " + exitVal);

            result = exitVal;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }


    /**
     * @param args
     */
    public static void main(String args[]) {
        String stCommand = " mysqldump -uroot --skip-opt pos_anugerah_01 -t --tables pos_category D:/transferout/data/pos_merk.sql";
        ExecCommand execCommand = new ExecCommand();
        execCommand.setPathFile("D:/test.sql");
        int result = execCommand.runCommmand(stCommand);
        if (result == 0) {
            System.out.println("--- Execute command succesfully ---");
        } else {
            System.out.println("--- Execute command failed ---");
        }
    }
}
