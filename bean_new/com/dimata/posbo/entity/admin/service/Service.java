package com.dimata.posbo.entity.admin.service;

import java.util.*;
import com.dimata.util.*;

import com.dimata.util.log.*;
import com.dimata.posbo.db.*;

public class Service implements Runnable{

    public Service(){
    }

    public void run(){
        System.out.println("start .... ");
        TXTLogger.logger("c:\\temp\\x.log", "", "start...."); 

        while (ServiceManager.running){
            try{
                ServiceManager svcMan = WP_ServiceManager.fetchServiceConfig();
                TXTLogger.logger("c:\\temp\\x.log", "",
                new Date().toString());
                TXTLogger.logger("c:\\temp\\x.log", "", "backing up...");
                doBackUp();
                System.out.println("sleep.... ");

                int sleepTmp=svcMan.getPeriode()==0?1:svcMan.getPeriode();
                int sleepTime=(int)(sleepTmp*60*1000);
                System.out.println("sleeptime ="+sleepTime);
                Thread.sleep(sleepTime);

                if (!ServiceManager.running)
                    break;

                /*TXTLogger.logger("c:\\temp\\x.log", "", "backing up...");
                System.out.println("backing up .... ");
                doBackUp(); */


                }
            catch (Exception e)
                {
                System.out.println("Interrupted " + e);
                }
            }
        System.out.println("stop .... ");
        TXTLogger.logger("c:\\temp\\x.log", "", "stop....");
        }

    public void doBackUp()
        {
        try{
            ServiceManager svcMan = WP_ServiceManager.fetchServiceConfig();
            CopyJava cj=new CopyJava();
            String target1 = svcMan.getTargetDir1();
            String target2 = svcMan.getTargetDir2();
            String target3 = svcMan.getTargetDir3();
            Vector msg = null;
            String errMsg1 = "";
            String errMsg2 = "";
            String errMsg3 = "";
            System.out.println("Starting copying files into "+target1);
            cj.copyDirFiles(svcMan.getSourceDir(), target1);
            msg=cj.getListErr();
            if(msg!=null && msg.size()>0){
                for(int i=0;i<msg.size();i++)
                    errMsg1=errMsg1+msg.get(i);
            }else{
                errMsg1="OK...";
            }
            errMsg1=target1+", status:"+errMsg1;
    
            if(target2 != null && target2.length()>0){
                System.out.println("Starting copying files into "+target2);
                cj.copyDirFiles(svcMan.getSourceDir(), target2);
                msg=cj.getListErr();
                if(msg!=null && msg.size()>0){
                     for(int i=0;i<msg.size();i++)
                         errMsg2=errMsg2+msg.get(i);
                }else{
                    errMsg2="OK...";
                }
                errMsg2=target2+", status:"+errMsg2;
            }
            if(errMsg2.equals("") || target2==null || target2.equals(""))
                errMsg2="Target2 directory has not been set yet..";
            if(target3 != null && target3.length()>0){
                System.out.println("Starting copying files into "+target3);
                cj.copyDirFiles(svcMan.getSourceDir(), target3);
                msg=cj.getListErr();
                if(msg!=null && msg.size()>0){
                     for(int i=0;i<msg.size();i++)
                         errMsg3=errMsg1+msg.get(i);
                }else{
                    errMsg3="OK...";
                }
                errMsg3=target3+", status:"+errMsg3;
            }
            if(errMsg3.equals("") || target3==null || target3.equals(""))
                errMsg3="Target3 directory has not been set yet..";
            System.out.println("target1 note >"+errMsg1+" ; target2 note >"+errMsg2+" ; target3 note >"+errMsg3);
            addLog(new Date(),errMsg1,errMsg2,errMsg3);
        }catch(Exception e){
            System.out.println("Err doBackUp :"+e);
        }
    }

    public static int addLog(Date dt,String str1,String str2, String str3){
        String stTime = Formater.formatDate(dt, "yyyy-MM-dd k:mm:ss");
        String sql = "INSERT INTO LOGGER (DATE_CREATED,TARGET1_NOTE,TARGET2_NOTE,TARGET3_NOTE) VALUES (DATE_FORMAT('" + stTime + "','%Y-%m-%d %T'),'" +str1+ "','" +str2+"','"+str3+"')";
        System.out.println("sql Statement : "+sql);
        try{
            return DBHandler.execUpdate(sql);
        }catch (Exception e){
            System.out.println("Err addLog :"+e);
            return -1;
        }
    }

    }
