/*
 * I_Billing.java
 *
 * Created on February 26, 2005, 1:34 PM
 */

package com.dimata.interfaces.BOCashier;

import com.dimata.ObjLink.BOCashier.*; 
/**
 *
 * @author  wpradnyana
 */
public interface I_Billing
{
    
    public static String classNameHanoman = "com.dimata.hanoman.entity.cashier.PstBillingRec";

    public long insertSale(SaleModel sale);

    public long synchronizeOID(long oidOld, long oidNew);

    public long synchronizeOIDItem(long oidOld, long oidNew);

}
