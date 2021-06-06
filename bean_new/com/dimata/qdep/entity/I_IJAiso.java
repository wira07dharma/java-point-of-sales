/*
 * I_IJAiso.java
 *
 * Created on April 18, 2005, 8:27 AM
 */

package com.dimata.qdep.entity;

import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.payment.*;

/**
 *
 * @author  Administrator
 * @version 
 */
public interface I_IJAiso       
{ 
    // Class name that will implement this Interface (on AISO)
    public static final String implClassName = "com.dimata.aiso.ijimpl.IjImplementation";
    
    
    /**
     * this method used to insert Contact Class (BO) into AISO system.      
     * @param <CODE>objContactClass</CODE>object Contact Class that wil be save into AISO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncContactClassBO(ContactClass objContactClass);  
    
    
    /**
     * this method used to insert Contact Class Assign (BO) into AISO system.      
     * @param <CODE>objContactClassAssign</CODE>object Contact Class Assign that wil be save into AISO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncContactClassAssignBO(ContactClassAssign objContactClassAssign);    
    
    
    /**
     * this method used to insert Contact List (BO) into AISO system.      
     * @param <CODE>objContactList</CODE>object Contact List that wil be save into AISO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncContactListBO(ContactList objContactList);    
    
    
    /** 
     * this method used to insert Standart Rate (BO) into AISO system.      
     * @param <CODE>objStandartRate</CODE>object Standart Rate that wil be save into AISO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncStandartRateBO(StandartRate objStandartRate);    

    
    /**
     * this method used to insert Trans Currency (BO) into AISO system.      
     * @param <CODE>objCurrencyType</CODE>object Trans Currency that wil be save into AISO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncTransCurrencyBO(CurrencyType objCurrencyType); 
    
    
    /**
     * this method used to update Contact Class in AISO system.      
     * @param <CODE>objContactClass</CODE>object Contact Class that wil be update in AISO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncContactClassBO(ContactClass objContactClass);  
    
    
    /**
     * this method used to update Contact Class Assign in AISO system.      
     * @param <CODE>objIjContactClassAssign</CODE>object Contact Class Assign that wil be update in AISO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncContactClassAssignBO(ContactClassAssign objContactClassAssign);    
    
    
    /**
     * this method used to update Contact List in AISO system.      
     * @param <CODE>objContactList</CODE>object Contact List that wil be update in AISO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncContactListBO(ContactList objContactList);    
    
    
    /** 
     * this method used to update Standart Rate in AISO system.      
     * @param <CODE>objStandartRate</CODE>object Standart Rate that wil be update in AISO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncStandartRateBO(StandartRate objStandartRate);    

    
    /**
     * this method used to update Trans Currency in AISO system.      
     * @param <CODE>objCurrencyType</CODE>object Trans Currency that wil be update in AISO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncTransCurrencyBO(CurrencyType objCurrencyType);
    
    
    /**
     * this method used to delete Contact Class from AISO system.      
     * @param <CODE>lContactClassOid</CODE>OID of object Contact Class that wil be delete from AISO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncContactClassBO(long lContactClassOid);  
    
    
    /**
     * this method used to delete Contact Class Assign from AISO system.      
     * @param <CODE>lContactClassOid</CODE>OID 0f object Contact Class that wil be delete from AISO System
     * @param <CODE>lContactListOid</CODE>OID 0f object Contact List that wil be delete from AISO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncContactClassAssignBO(long lContactClassOid, long lContactListOid);    
    
    
    /**
     * this method used to delete Contact List from AISO system.      
     * @param <CODE>lContactListOid</CODE>OID of object Contact List that wil be delete from AISO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncContactListBO(long lContactListOid);     
    
    
    /** 
     * this method used to delete Standart Rate from AISO system.      
     * @param <CODE>lStandartRateOid</CODE>OID of object Standart Rate that wil be delete from AISO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncStandartRateBO(long lStandartRateOid);    

    
    /**
     * this method used to delete Trans Currency from AISO system.      
     * @param <CODE>lCurrencyTypeOid</CODE>OID of object Trans Currency that wil be save into AISO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncTransCurrencyBO(long lCurrencyTypeOid); 
    
}
