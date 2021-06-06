/*
 * PstAppPriv.java
 *
 * Created on April 3, 2002, 9:29 AM
 */

package com.dimata.aiso.entity.admin;

/**
 *
 * @author  ktanjana
 * @version
 */


import java.util.*;
import com.dimata.qdep.entity.*;

public class AppPrivilegeObj extends Entity {
    
    private long privObjId=0;
    
    private long privId=0;
    
    private long code=-1;
    
    private Vector commands= new Vector(); // vector of Integer of  command indexes
    
    public Vector getCommands(){
        if(this.commands==null){
            commands= new Vector();
            return commands;
        }
        return commands;
    }
    
    public void setCommands(Vector commands){
        if(commands==null)
            this.commands= new Vector();
        
        this.commands=commands;
    }
    
    public int getCommandsSize(){
        if(commands==null)
            return 0;
        return commands.size();
    }
    
    public int getCommand(int idx){
        if( (this.commands==null) || (this.commands.size()<1)  || (idx>=this.commands.size())){
            return -1;
        }
        try{
            return ( (Integer) commands.get(idx)).intValue();
        } catch (Exception exc){
            return -1;
        }
    }
    
    public boolean existCommand(int cmd){
        if( (this.commands==null) || (this.commands.size()<1) ){
            return false;
        }
        try{
            if(commands!=null &&commands.size()>0){
	            for(int i =0; i<this.commands.size();i++){
	                if(cmd==( (Integer) commands.get(i)).intValue())
	                    return true;
	            }
            }
        } catch (Exception exc){
        }
        return false;
    }
    
    public AppPrivilegeObj() {
        super();
    }
    
    public long getPrivObjId() {
        return privObjId;
    }
    
    public void setPrivObjId(long privObjId) {
        this.privObjId = privObjId;
    }
    
    public long getPrivId() {
        return privId;
    }
    
    public void setPrivId(long privId) {
        this.privId = privId;
    }
    
    public long getCode() {
        return code;
    }
    
    public void setCode(long code) {
        this.code = code;
    }
    
}

