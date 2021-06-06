/*
 * Created on Nov 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.dimata.pos.cashier;

import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.PstShift;
import com.dimata.posbo.entity.masterdata.Shift;
import com.dimata.posbo.db.DBException;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.masterCashier.CashUser;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * @author wpradnyana
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SessionManager {
    
    public static Vector getSupervisorLogin(){
        
        Vector userSession = null;
        AppUser appUser = PstAppUser.getByLoginIDAndPassword(userName, userPwd);
        if(appUser.getOID()>1){
            CashCashier cashCashier = new CashCashier();
            cashCashier.setCashMasterId(CashierMainApp.getCashMaster().getOID());
            cashCashier.setAppUserId(CashierMainApp.getCashUser().getOID());
            cashCashier.setSpvOid(appUser.getOID());
            cashCashier.setSpvName(appUser.getFullName());
            try{
                long cashierOID = PstCashCashier.insertExc(cashCashier);
                cashCashier.setOID(cashierOID);
                CashierMainApp.setCashCashier(cashCashier);
                //cashCashier.set
                userSession = new Vector(1,1);
            }catch(DBException dbe){
                dbe.printStackTrace();
            }
            
        }
        
        return userSession;
    }
    public static Shift getShiftByTime(Date date){
        Shift shift = new Shift();
        Vector vctShift = PstShift.listAll();
        int size = vctShift.size();
        int startHourDate = date.getHours();
        
        for(int i=0;i<size;i++){
            Shift tmp = (Shift)vctShift.get(i);
            int startShiftHour = tmp.getStartTime().getHours();
            int endShiftHour = tmp.getEndTime().getHours();
            if(startHourDate>=startShiftHour&&startHourDate<=endShiftHour){
                shift = tmp;
            }
        }
        return shift;
    }
    public static long getLogOutUser() {
        
        CashUser cashUser = CashierMainApp.getCashUser();
        long cashUserID = cashUser.getUserId();
        AppUser appUser = PstAppUser.fetch(cashUserID);
        PstAppUser.updateUserStatus(cashUserID, AppUser.STATUS_LOGOUT);
        PstAppUser.updateByLoggedIn(appUser);
        return appUser.getOID();
        //CashierMainApp.setCashUser (null);
        
        
    }
    public static CashUser getLoginUser() {
       
        CashUser cashUser = null;
        
        AppUser appUser = PstAppUser.getByLoginIDAndPassword(userName, userPwd);
        if(appUser.getOID()>1) {
            if(appUser.getUserGroupNew()==PstAppUser.GROUP_CASHIER){
                cashUser = new CashUser();
                cashUser.setUserName(appUser.getFullName());
                cashUser.setUserId(appUser.getOID());
                //PstAppUser.updateUserStatus (appUser.getOID (), AppUser.STATUS_LOGIN);
                //PstAppUser.updateByLoggedIn(appUser);
                Date date = new Date();
                Shift shift = getShiftByTime(date);
                CashierMainApp.setShift(shift);
                return cashUser;
            }else{
                JOptionPane.showMessageDialog(null," Unauthorized acces","Unauthorized acces",JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
        return cashUser;
    }
    private static String userName ;
    private static String userPwd;
    /**
     * @return Returns the userName.
     */
    public static String getUserName() {
        
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public static void setUserName(String sentUserName) {
        
        userName = sentUserName;
        
    }
    /**
     * @return Returns the userPwd.
     */
    public static String getUserPwd() {
       
        return userPwd;
    }
    /**
     * @param userPwd The userPwd to set.
     */
    public static void setUserPwd(String sentUserPwd) {
       
        userPwd = sentUserPwd;
        
    }
    
    
}
