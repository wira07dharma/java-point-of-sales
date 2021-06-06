/*
 * Ctrl Name  		:  CtrlMemberRegistrationHistory.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  		:  [authorName]
 * @version  		:  [version]
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.posbo.form.masterdata;

/* java package */
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.entity.I_DocType;

public class CtrlMemberRegistrationHistory extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private MemberRegistrationHistory memberRegistrationHistory;
    private PstMemberRegistrationHistory pstMemberRegistrationHistory;
    private FrmMemberRegistrationHistory frmMemberRegistrationHistory;
    int language = LANGUAGE_DEFAULT;
    Date dateLog = new  Date();
    MemberReg prevMember = new MemberReg();
    MemberRegistrationHistory prevMemberRegistrationHistory=null;

    public CtrlMemberRegistrationHistory(HttpServletRequest request){
        msgString = "";
        memberRegistrationHistory = new MemberRegistrationHistory();
        try{
            pstMemberRegistrationHistory = new PstMemberRegistrationHistory(0);
        }catch(Exception e){;}
        frmMemberRegistrationHistory = new FrmMemberRegistrationHistory(request, memberRegistrationHistory);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmMemberRegistrationHistory.addError(frmMemberRegistrationHistory.FRM_FIELD_MEMBER_REGISTRATION_HISTORY_ID, resultText[language][RSLT_EST_CODE_EXIST] );
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public MemberRegistrationHistory getMemberRegistrationHistory() { return memberRegistrationHistory; }
    
    public FrmMemberRegistrationHistory getForm() { return frmMemberRegistrationHistory; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }

    public int action(int cmd , long oidMemberRegistrationHistory, long oidMember){
        return action(cmd, oidMemberRegistrationHistory, oidMember,0,"");
    }

    public int action(int cmd , long oidMemberRegistrationHistory, long oidMember,long oidUser, String userName){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd){
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidMemberRegistrationHistory != 0){
                    try{
                        memberRegistrationHistory = PstMemberRegistrationHistory.fetchExc(oidMemberRegistrationHistory);
                    }catch(Exception exc){
                    }
                    try{
                        prevMemberRegistrationHistory = PstMemberRegistrationHistory.fetchExc(oidMemberRegistrationHistory);
                    }catch(Exception exc){
                    }
                }

                if(oidMember != 0){
                  try {
                        prevMember = PstMemberReg.fetchExc(oidMember);
                  } catch (Exception exc) {
                  }
                }

                frmMemberRegistrationHistory.requestEntityObject(memberRegistrationHistory);
                
                if(frmMemberRegistrationHistory.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                memberRegistrationHistory.setMemberId(oidMember);
                
                
                if(memberRegistrationHistory.getOID()==0){
                    try{
                        long oid = pstMemberRegistrationHistory.insertExc(this.memberRegistrationHistory);
                        if(oid!=0)
                        {
                            insertHistory(oidUser, userName, cmd, oidMember);
                        }
                    }catch(DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }else{
                    try {
                        long oid = pstMemberRegistrationHistory.updateExc(this.memberRegistrationHistory);
                        int cmdUpdate = Command.UPDATE;
                        if(oid!=0)
                        {
                            insertHistory(oidUser, userName, cmdUpdate, oidMember);
                        }
                    }catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }
                
                /* menset status member aktif */
                MemberReg member = new MemberReg();
                
                if(oidMember!=0){
                    try{
                        member = PstMemberReg.fetchExc(oidMember);
                    }catch(Exception e){
                        System.out.println("errrrr di fetch member : "+e.toString());
                        e.printStackTrace();
                    }
                    
                    if(member.getMemberStatus()==PstMemberReg.NOT_VALID&&memberRegistrationHistory.getValidExpiredDate().after(new Date())&&memberRegistrationHistory.getValidStartDate().before(new Date())){
                        member.setMemberStatus(PstMemberReg.VALID);
                        try{
                            member.setRegdate(memberRegistrationHistory.getRegistrationDate());
                            member.setMemberBarcode(member.getContactCode());
                            member.setMemberCounter(PstMemberReg.setCounter(member));
                            PstMemberReg.updateExc(member);
                            //System.out.println("update---------------> ");
                        }catch(Exception e){
                        }
                    }
                }
                break;
                
            case Command.EDIT :
                if (oidMemberRegistrationHistory != 0) {
                    try {
                        memberRegistrationHistory = PstMemberRegistrationHistory.fetchExc(oidMemberRegistrationHistory);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (oidMemberRegistrationHistory != 0) {
                    try {
                        memberRegistrationHistory = PstMemberRegistrationHistory.fetchExc(oidMemberRegistrationHistory);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidMemberRegistrationHistory != 0){
                    try{
                        long oid = PstMemberRegistrationHistory.deleteExc(oidMemberRegistrationHistory);
                        if(oidMember != 0){
                          try {
                                prevMember = PstMemberReg.fetchExc(oidMember);
                          } catch (Exception exc) {
                          }
                        }
                        if(oid!=0)
                        {
                            insertHistory(oidUser, userName, cmd, oidMember);
                        }
                        if(oid!=0){
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }else{
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }catch(DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }catch(Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            default :
                
        }
        return rsCode;
    }

     /**
     * Insert Data ke log History
     */
    public  void insertHistory(long userID, String nameUser, int cmd, long oid)
    {
        try
        {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("masterdata/memberreg_edit.jsp");
           logSysHistory.setLogUpdateDate(dateLog);
           logSysHistory.setLogDocumentType(I_DocType.systemModulMenu[0]);
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber(prevMember.getContactCode());
           logSysHistory.setLogDocumentId(oid);
           logSysHistory.setLogDetail(this.memberRegistrationHistory.getLogDetail(prevMemberRegistrationHistory));

           if(!"".equals(logSysHistory.getLogDetail())|| cmd==Command.DELETE )
           {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
           }
           
      }
      catch(Exception e)
      {

      }
    }
}
