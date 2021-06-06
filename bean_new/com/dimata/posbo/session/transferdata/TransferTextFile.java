package com.dimata.posbo.session.transferdata;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jul 11, 2007
 * Time: 10:13:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class TransferTextFile {

    /**
     * gadnyana
     * for restore data from file
     * @param path
     * @param table_names
     */
    public static void restoreTransactionFile(String path, String[] table_names){
        try {
            if(table_names.length>0){
                for(int k=0;k<table_names.length;k++){
                    System.out.println(path+"cashier_"+table_names[k]+"_dump.sql");
                    BufferedReader in = new BufferedReader(new FileReader(path+"cashier_"+table_names[k]+"_dump.sql"));
                    String str;
                    DBResultSet dbrs = null;
                    while ((str = in.readLine()) != null) {
                        System.out.println(str);
                        try{
                        dbrs = DBHandler.execQueryResult("describe "+table_names[k]);
                        ResultSet rs = dbrs.getResultSet();
                        while(rs.next()){
                            
                        }
                        str = str.replaceAll("\t",",");
                        str = str.replaceAll("\n","),(");
                        System.out.println("=>> "+str);

                            DBHandler.execUpdate("INSERT INTO `"+table_names[k]+"` VALUES ("+str+")");
                        }catch(Exception e){}
                    }
                    in.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
