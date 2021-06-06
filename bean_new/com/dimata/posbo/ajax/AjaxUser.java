
package com.dimata.posbo.ajax;

import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.FingerPatern;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.admin.PstFingerPatern;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.lowagie.text.pdf.codec.Base64;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class AjaxUser 
extends HttpServlet{
    public static final String textListFinger[][]={
        {"Kelingking Kiri","Jari Manis Kiri","Jari Tengah Kiri","Telunjuk Kiri","Ibu Jari Kiri","Ibu Jari Kanan","Telunjuk Kanan","Jari Tengah Kanan","Jari Manis Kanan","Kelingking Kanan"},
        {"Left Little Finger","Left Ring Finger","Left Middle Finger","Left Fore Finger","Left Thumb","Right Thumb","Right Fore Finger","Right Middle Finger","Right Ring Finger","Right Little Finger"}   
    };
    
    private String checkUser(HttpServletRequest request){
        String result ="";
        String whereClause="";
        String whereFinger="";
        String whereFinger2="";
        Hashtable<Integer, Boolean> fingerType = new Hashtable<Integer, Boolean>();
        AppUser appUser= new AppUser();
        
        String login = FRMQueryString.requestString(request,"login");
        int language = FRMQueryString.requestInt(request, "language");
        String base = FRMQueryString.requestString(request, "base");
        
        whereClause = " "+PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+"='"+login+"'";
        Vector listUser = PstAppUser.listFullObj(0, 0, whereClause, "");
        if (listUser.size()>0){
            appUser = (AppUser) listUser.get(0);
            //update status user menjadi 0
            appUser.setUserStatus(0);
            PstAppUser.update(appUser);
            //dapatkan data jari yang sudah didaftarkan oleh user
            whereFinger = " "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+"="+appUser.getEmployeeId()+"";
            Vector listFinger = PstFingerPatern.list(0, 0, whereFinger, "");
            
            //masukkan ke hash table
            for (int i = 0; i<listFinger.size();i++){
                FingerPatern fingerPatern = (FingerPatern)listFinger.get(i);
                fingerType.put(fingerPatern.getFingerType(), true);  
            }
            
            //buat sepuluh kotak
            for (int j= 0; j<10;j++){
                Boolean found = false;
                try{
                    if (fingerType.size()>0){
                        found = fingerType.containsKey(j);
                    }

                }catch(Exception ex){
                    found= false;
                }
                if (found==true){
                    //jika jari tersebut sudah didaftarkan, backgorund kotak hijau, dan langsung berisi link verification
   
                    result +="<div class=\"finger\">";
                    //memberi link untuk masing-masing kotak jari
                    whereFinger2 =" "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+"= "+appUser.getEmployeeId()+" and "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE]+"="+j+"";
                    Vector listFinger2 = PstFingerPatern.list(0, 0, whereFinger2, "");
                    FingerPatern fingerPatern = (FingerPatern) listFinger2.get(0);
                    //String urlVerification =""+base+"verification.php?user_id="+appUser.getEmployeeId()+"="+j+"="+base+"";
                    
                    //setiap link di enkripsi dengan metode base64
                    //byte[] urlByte = urlVerification.getBytes();
                    //String urlBase= new String(Base64.encodeBytes(urlByte));
                    
                    //SDK BARU
                    String urlBase = "verification&"+appUser.getEmployeeId()+"&"+fingerPatern.getFingerPatern()+"&"+base+"process_verification.jsp";
                    
                    result +="<a class='loginFinger' href='findspot:findspot protocol;"+urlBase+"'><div class=\"finger_spot\" style='background-color:#449D44'>"+textListFinger[1][j]+"</div></a>";
                    result +="</div>";
                  
                }else{
                    //jika jari tersebut belum didaftarkan, background kotak putih, dan link verifikasi tidak ada
                    result +="<div class=\"finger\">";
                    result +="<div class=\"finger_spot\">"+textListFinger[1][j]+"</div>";
                    result +="</div>";
                }
            }
            
            
        }else{
            for (int j= 0; j<10;j++){
                result +="<div class=\"finger\">";
                result +="<div class=\"finger_spot\"></div>";
                result +="</div>"; 
            }
        }
        
        return result;
    }
    
    private int checkStatusUser(HttpServletRequest request){
        int result=0;
        String whereClause="";
        
        String loginId = FRMQueryString.requestString(request,"loginId");
        whereClause = " "+PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+"='"+loginId+"'";
        Vector listAppUser = PstAppUser.listFullObj(0, 0, whereClause, "");
        if(listAppUser.size()>0){
            AppUser appUser = (AppUser)listAppUser.get(0);
            result = appUser.getUserStatus();
        }
        
        return result;
    }
    
    private String checkUser2(HttpServletRequest request){
        String result ="";
        String whereClause="";
        String whereFinger="";
        String whereFinger2="";
        String css="";
        Hashtable<Integer, Boolean> fingerType = new Hashtable<Integer, Boolean>();
        AppUser appUser= new AppUser();
        
        String login = FRMQueryString.requestString(request,"login");
        int language = FRMQueryString.requestInt(request, "language");
        String base = FRMQueryString.requestString(request, "base");
        int function = FRMQueryString.requestInt(request, "base");
        if (function==1){
            css="loginFinger";
        }else{
            css="loginFinger2";
        }
        
        whereClause = " "+PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+"='"+login+"' AND ("+PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW]+"=4 OR "+PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW]+"=1)";
        Vector listUser = PstAppUser.listFullObj(0, 0, whereClause, "");
        
        if (listUser.size()>0){
            appUser = (AppUser) listUser.get(0);
            //update status user menjadi 0
            appUser.setUserStatus(0);
            PstAppUser.update(appUser);
            //dapatkan data jari yang sudah didaftarkan oleh user
            whereFinger = " "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+"="+appUser.getEmployeeId()+"";
            Vector listFinger = PstFingerPatern.list(0, 0, whereFinger, "");
            
            //masukkan ke hash table
            for (int i = 0; i<listFinger.size();i++){
                FingerPatern fingerPatern = (FingerPatern)listFinger.get(i);
                fingerType.put(fingerPatern.getFingerType(), true);  
            }
            
            //buat sepuluh kotak
            for (int j= 0; j<10;j++){
                Boolean found = false;
                try{
                    if (fingerType.size()>0){
                        found = fingerType.containsKey(j);
                    }

                }catch(Exception ex){
                    found= false;
                }
                if (found==true){
                    //jika jari tersebut sudah didaftarkan, backgorund kotak hijau, dan langsung berisi link verification
   
                    result +="<div class=\"finger\">";
                    //memberi link untuk masing-masing kotak jari
                    whereFinger2 =" "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+"= "+appUser.getEmployeeId()+" and "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE]+"="+j+"";
                    Vector listFinger2 = PstFingerPatern.list(0, 0, whereFinger2, "");
                    FingerPatern fingerPatern = (FingerPatern) listFinger2.get(0);
                    String urlVerification =""+base+"verification.php?user_id="+appUser.getEmployeeId()+"="+j+"="+base+"";
                    
                    //setiap link di enkripsi dengan metode base64
                    byte[] urlByte = urlVerification.getBytes();
                    String urlBase= new String(Base64.encodeBytes(urlByte));
                    
                    result +="<a class='"+css+"' onclick=\"javascript:fingerClick()\" href='finspot:FingerspotVer;"+urlBase+"'><div class=\"finger_spot\" style='background-color:#449D44'>"+textListFinger[language][j]+"</div></a>";
                    result +="</div>";
                  
                }else{
                    //jika jari tersebut belum didaftarkan, background kotak putih, dan link verifikasi tidak ada
                    result +="<div class=\"finger\">";
                    result +="<div class=\"finger_spot\"></div>";
                    result +="</div>";
                }
            }
            
            
        }else{
            for (int j= 0; j<10;j++){
                result +="<div class=\"finger\">";
                result +="<div class=\"finger_spot\"></div>";
                result +="</div>"; 
            }
        }
        
        return result;
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        String result ="";
        int command = FRMQueryString.requestInt(request,"command");
        response.setContentType("text/html;charset=UTF-8");
        
        switch (command) {
            case Command.ASK :
                result= checkUser(request);
                response.getWriter().write(result);
                break;
            case Command.LIST :
                response.getWriter().write(result);
                break;
            case Command.SEARCH:
                result = String.valueOf(checkStatusUser(request));
                response.getWriter().write(result);
                break;
            case Command.ASSIGN:
                result = String.valueOf(checkUser2(request));
                response.getWriter().write(result);
                break;
            default:

        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        
    }
    
}
