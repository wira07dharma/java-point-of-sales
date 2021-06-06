/*
 * I_IJBoSys.java
 *
 * Created on April 18, 2005, 8:24 AM
 */

package com.dimata.qdep.entity;

import java.util.Vector;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.payment.*;

/**
 *
 * @author  Administrator
 * @version 
 */
public interface I_IJBOSystem 
{      
    
    // Class name that will implement this Interface (on BO)  
    public static final String implClassName = "com.dimata.ij.ijimpl.IjImplementation";
    
    /**
     * this method used to get list Currency Type used in BO System
     * return 'vector of obj com.dimata.common.entity.payment.CurrencyType'
     */                                                       
    public Vector getListCurrencyType();    
    
    
    /**
     * this method used to get list Standart Rate used in BO System
     * return 'vector of obj com.dimata.common.entity.payment.StandartRate'
     */                                                       
    public Vector getListStandartRate();    
    
    
    /**
     * this method used to get list Location used in BO System
     * return 'vector of obj com.dimata.common.entity.location.Location'
     */                                                       
    public Vector getListLocation();      
    
    
    /**
     * this method used to get list Product Department
     * return 'vector of obj com.dimata.prochain.entity.masterdata.ProductDepartment'
     */                                                       
    public Vector getListProductDepartment();          
    
    
    /**
     * this method used to get list Sale Type
     * look reference constant index & name in 'com.dimata.qdep.entity.I_IJGeneral'
     * return 'vector of obj com.dimata.ij.ibosys.IjSaleTypeData'
     */                                                       
    public Vector getListSaleType();        
    
    
    /**
     * this method used to get list Payment System
     * return 'vector of obj com.dimata.ij.ibosys.IjPaymentSystemData'
     */                                                       
    public Vector getListPaymentSystem();            

    
    /**
     * this method used to insert Contact Class (AISO) into BO system.      
     * @param <CODE>objContactClass</CODE>object Contact Class that wil be save into BO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncContactClassAISO(ContactClass objContactClass);  
    
    
    /**
     * this method used to insert Contact Class Assign (AISO) into BO system.      
     * @param <CODE>objContactClassAssign</CODE>object Contact Class Assign that wil be save into BO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncContactClassAssignAISO(ContactClassAssign objContactClassAssign);    
    
    
    /**
     * this method used to insert Contact List (AISO) into BO system. 
     * @param <CODE>objContactList</CODE>object Contact List that wil be save into BO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncContactListAISO(ContactList objContactList);    
    
    
    /** 
     * this method used to insert Standart Rate (AISO) into BO system.      
     * @param <CODE>objStandartRate</CODE>object Standart Rate that wil be save into BO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncStandartRateAISO(StandartRate objStandartRate);    

    
    /**
     * this method used to insert Trans Currency (AISO) into BO system.      
     * @param <CODE>objCurrencyType</CODE>object Trans Currency that wil be save into BO System
     * return 'OID of insertted process' 
     */                                                       
    public long insertSyncTransCurrencyAISO(CurrencyType objCurrencyType);        

    
    /**
     * this method used to update Contact Class in BO system.      
     * @param <CODE>objContactClass</CODE>object Contact Class that wil be update in BO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncContactClassAISO(ContactClass objContactClass);  
    
    
    /**
     * this method used to update Contact Class Assign in BO system.      
     * @param <CODE>objContactClassAssign</CODE>object Contact Class Assign that wil be update in BO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncContactClassAssignAISO(ContactClassAssign objContactClassAssign);    
    
    
    /**
     * this method used to update Contact List in BO system. 
     * @param <CODE>objContactList</CODE>object Contact List that wil be update in BO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncContactListAISO(ContactList objContactList);    
    
    
    /** 
     * this method used to update Standart Rate in BO system.      
     * @param <CODE>objStandartRate</CODE>object Standart Rate that wil be update in BO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncStandartRateAISO(StandartRate objStandartRate);    

    
    /**
     * this method used to update Trans Currency in BO system.      
     * @param <CODE>objCurrencyType</CODE>object Trans Currency that wil be update in BO System
     * return 'OID of updated process' 
     */                                                       
    public long updateSyncTransCurrencyAISO(CurrencyType objCurrencyType);        
    

    /**
     * this method used to delete Contact Class from BO system.      
     * @param <CODE>lContactClassOid</CODE>OID of object Contact Class that wil be delete from BO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncContactClassAISO(long lContactClassOid);  
    
    
    /**
     * this method used to delete Contact Class Assign from BO system.      
     * @param <CODE>lContactClassOid</CODE>OID of object Contact Class that wil be delete from BO System
     * @param <CODE>lContactListOid</CODE>OID of object Contact List that wil be delete from BO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncContactClassAssignAISO(long lContactClassOid, long lContactListOid);    
    
    
    /**
     * this method used to delete Contact List from BO system. 
     * @param <CODE>lContactListOid</CODE>OID of object Contact List that wil be delete from BO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncContactListAISO(long lContactListOid);    
    
    
    /** 
     * this method used to delete Standart Rate from BO system.      
     * @param <CODE>lStandartRateOid</CODE>OID of object Standart Rate that wil be delete from BO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncStandartRateAISO(long lStandartRateOid);    

    
    /**
     * this method used to delete Trans Currency from BO system.      
     * @param <CODE>lCurrencyTypeOid</CODE>OID of object Trans Currency that wil be delete from BO System
     * return 'OID of deleted process' 
     */                                                       
    public long deleteSyncTransCurrencyAISO(long lCurrencyTypeOid);           
    
}
