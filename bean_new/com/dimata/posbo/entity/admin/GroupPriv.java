/*
 * PstAppPriv.java
 *
 * Created on April 3, 2002, 9:29 AM
 */

package com.dimata.posbo.entity.admin;

/**
 *
 * @author  ktanjana
 * @version 
 */

import java.util.*;
import com.dimata.qdep.entity.*;

public class GroupPriv extends Entity {
    
    public GroupPriv() {
    }
        
    public long getGroupID() {
        return this.getOID(0) ;
    }

    public void setGroupID(long groupID) {
        this.setOID(0, groupID);
    }
    
    public long getPrivID() {
        return this.getOID(1) ;
    }

    public void setPrivID(long privID) {
        this.setOID(1, privID);
    }            
}

