package com.dimata.qdep.form;

public class FRMMessage
{
    
    public static final int NONE                = 0;    
    
    
    // error form constanta
    public static final int ERR_NONE            = 0;
    public static final int ERR_UNKNOWN         = 1;
    public static final int ERR_REQUIRED        = 2;
    public static final int ERR_TYPE            = 3;
    public static final int ERR_FORMAT          = 4;    
    public static final int ERR_SAVED           = 5;
    public static final int ERR_UPDATED         = 6;
    public static final int ERR_DELETED         = 7;
    public static final int ERR_PWDSYNC         = 8;
    
    // message form constanta
    public static final int MSG_NONE            = 1000;
    public static final int MSG_SAVED           = 1001;
    public static final int MSG_UPDATED         = 1002;
    public static final int MSG_DELETED         = 1003;
    public static final int MSG_ASKDEL          = 1004;    
    public static final int MSG_INCOMPLATE      = 1005;
    public static final int MSG_IN_USED			= 1006;
    
    

    private static String[] errString = {
        "",
        "Unknown error occured", 
        "Required data",
        "Invalid entry type",        
        "Invalid entry format",
        
        "Can not save data",
        "Can not update data",
        "Can not delete data",
        "Password doesn't match"
    };
    

    private static String[] msgString = {
        "",
        "Data have been saved", 
        "Data have been updated",
        "Data have been deleted",
        "Are you sure to delete these data ?",
        "Form contain some incomplete data",
        "Can't delete .. !!<br>Object is currently in used by another module on this application"
                
    };
    
    
    /**
     *  Get Error String.
     *  idx is range from 0..errString.length
     */
    public static String getErr(int idx)    
    {
        if(idx < 0 || idx >= errString.length) return "";
        return errString[idx];
    }


    /**
     *  Get Message String.
     *  idx is 
     *  reange from 0..msgString.length 
     *  or
     *  reange from MSG_NONE..msgString.length + MSG_NONE
     *  so
     *  we can call it as getMsg(MSG_SAVED) or getMsg(1)
     *  both return the same value     
     */
    public static String getMsg(int idx)    
    {
        if(idx >= MSG_NONE) {
            if(idx >= msgString.length + MSG_NONE)
                return "";
            return msgString[idx - MSG_NONE];
        }else {
            if(idx < 0 || idx >= msgString.length) 
                return "";
            return msgString[idx];
        }                
    }
    

    /**
     *  Get Message String or Error String.
     *  if idx is < MSG_NOTE it will return Error String from Error array
     *  if idx is >= MSG_NOTE it will return Message String from Message array     
     *
     *  Use this function to get kind of String that you never known before,
     *  whether it's Message String or Error String !
     */
    public static String getMessage(int idx)    
    {
        if(idx >= MSG_NONE) {
            if(idx >= msgString.length + MSG_NONE)
                return "";
            return msgString[idx - MSG_NONE];
        }else {
            if(idx < 0 || idx >= errString.length) 
                return "";
            return errString[idx];
        }        
    }




} // end of FRMMessage
