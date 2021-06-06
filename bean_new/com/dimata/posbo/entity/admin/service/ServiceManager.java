package com.dimata.posbo.entity.admin.service;

import java.sql.*;
import java.util.*;
import java.util.Date;

import com.dimata.util.*;
import com.dimata.posbo.db.*;

import com.dimata.posbo.entity.admin.service.*;

public class ServiceManager{

    public static boolean running = false;
    public static Service lck = new Service();

    public ServiceManager() {
    }

    public Vector getLog(int start,int limit) {
       String sql = "SELECT * FROM LOGGER ORDER BY DATE_CREATED DESC"+ 
                    " LIMIT " + start + "," + limit;                     

       DBResultSet dbrs = null;
       Vector vct = new Vector(1,1);
       try{
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()){
                    Logger logger  = new Logger();
                    PstLogger.resultToObject(rs, logger);
                    vct.add(logger);
                }

                rs.close();
       }catch(Exception e) {
              DBResultSet.close(dbrs);
              System.out.println("exception : "+e.toString());
              return null;
       }finally{
              DBResultSet.close(dbrs);
              return vct;
       }       
    }

    
    public int getMaxLog() {
       String sql = "SELECT COUNT(DATE_CREATED)AS TOTAL FROM LOGGER";
       int count =0;
       DBResultSet dbrs = null;
       try{
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()){
                count = rs.getInt(1);
            }
            rs.close();
       }catch(Exception e) {
            System.out.println("Exception : "+e.toString());
            DBResultSet.close(dbrs);
            return 0;
       }finally{
            DBResultSet.close(dbrs);
            return count;
       }       
    }

    public int clearLog(){
        String sql="DELETE FROM LOGGER";
        try{
            return DBHandler.execUpdate(sql);
        }catch(Exception e){
            System.out.println("Err in clearLog :"+e);
            return -1;
        }
    }

    public String httpDrawLog(int start,int limit){
        String str="";
        Vector data=getLog(start,limit);
        if(data!=null && data.size()>0){
            String[] header={"No.","Date","Time","Target 1","Target 2", "Target 3"};
            str="<table width='100%' cellPadding='2' cellSpacing='1'><tr>";
            for(int i=0;i<header.length;i++){
                str=str+"<td class='tableheader'>"+header[i]+"</td>";
            }
            str=str+"</tr>";
            for(int a=0;a<data.size();a++){
                str=str+"<tr><td class='cellStyle' align='right'>"+(a+1+start)+"</td>";
                Vector row=(Vector)data.get(a);
                for(int b=0;b<row.size();b++){
                    if(b==0){
                        str=str+"<td class='cellStyle'>"+Formater.formatDate((Date)row.get(b),"MMM dd, yyyy")+"</td>";
                        Date dt=(Date)row.get(0);
                        str=str+"<td class='cellStyle'>"+dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds()+"</td>";
                    }else{
                        str=str+"<td class='cellStyle'>"+row.get(b)+"</td>";
                    }
                }
                str=str+"</tr>";
            }
            str=str+"</table>";
            return str;
        }else{
            return "<br><font color='red'>Log list is empty...</font>";
        }
    }

    public void startService() {
        if(running) return;
        ServiceManager objMan = new  ServiceManager();
        Thread thLocker = new Thread(new Service());
        thLocker.setDaemon(false);
        running = true;
        thLocker.start();
    }


    public void stopService() {
        running = false;
    }


    public boolean getStatus() {
        return running;
    }

    /*
     *  LITLE NOTE ABOUT THIS CLASS - THREAD
     *	1. comment this main method if this runs from jsp
     *	2. set thread deamon to be false -> refers to deamon and user thread in RTFM :)
     *	3. you need a statis variable in the jvm to control the thread
     *	   (in this case "running" variable)
	 *  4. the thread will be start ufter start() method is called,
     *	   and run immidiately after return from main or from other caller method that calls the start()
     *  5. set the "running" var to be false to stop the tread
     *
     */
    public static void main(String[] args) {
 		try{
            ServiceManager objMan = new  ServiceManager();
            Thread thLocker = new Thread(objMan.lck);
            objMan.running = true;
            thLocker.setDaemon(false);
            thLocker.start();
            return;

        }catch(Exception e) {
            System.out.println(e.toString());
        }
    } // end of main

   
    public Date getStartTime(){ return startTime; }

    public void setStartTime(Date startTime){ this.startTime = startTime; }

    public String getSourceDir(){ return sourceDir==null?"":sourceDir; }

    public void setSourceDir(String sourceDir){ this.sourceDir = sourceDir; }

    public String getTargetDir1(){ return targetDir1==null?"":targetDir1; }

    public void setTargetDir1(String targetDir1){ this.targetDir1 = targetDir1; }

    public String getTargetDir2(){ return targetDir2==null?"":targetDir2; }

    public void setTargetDir2(String targetDir2){ this.targetDir2 = targetDir2; }

    public String getTargetDir3(){ return targetDir3==null?"":targetDir3; }

    public void setTargetDir3(String targetDir3){ this.targetDir3 = targetDir3; }

    public int getPeriode(){ return periode; }

    public void setPeriode(int periode){ this.periode = periode; }


    private Date startTime;
    private String sourceDir;
    private String targetDir1;
    private String targetDir2;
    private String targetDir3;
    private int periode;
}
