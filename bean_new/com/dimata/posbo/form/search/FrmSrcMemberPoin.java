/*
 * FrmSrcMemberPoin.java
 *
 * Created on February 25, 2005, 4:21 PM
 */

package com.dimata.posbo.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
import com.dimata.pos.entity.search.*; 
import com.dimata.pos.entity.billing.*;
/* project package */

import com.dimata.posbo.entity.search.*;


/**
 *
 * @author  wpradnyana
 */
public class FrmSrcMemberPoin extends FRMHandler implements I_FRMInterface, I_FRMType 
{
    
    SrcMemberPoin srcMemberPoin = new SrcMemberPoin(); 
    public static String FRM_SRC_MEMBER_POIN= "FRM_SRC_MEMBER_POIN";
    
    public static final int FRM_FLD_MEMBER_CODE=0;
    public static final int FRM_FLD_MEMBER_NAME=1;
    public static final int FRM_FLD_POIN_FROM=2;
    public static final int FRM_FLD_POIN_TO=3;
    public static final int FRM_FLD_INVOICE_ID=4;
    public static final int FRM_FLD_INVOICE_NUMBER=5;
    public static final int FRM_FLD_MEMBER_ID=6;
    public static final int FRM_FLD_USE_POIN_RANGE=7;
    
    public static String[] fieldNames={ 
        "FRM_FLD_MEMBER_CODE",
        "FRM_FLD_MEMBER_NAME",
        "FRM_FLD_POIN_FROM",
        "FRM_FLD_POIN_TO",
        "FRM_FLD_INVOICE_ID",
        "FRM_FLD_INVOICE_NUMBER",
        "FRM_FLD_MEMBER_ID",
        "FRM_FLD_USE_POIN_RANGE"
    };
    
    public static int [] fieldTypes={
        TYPE_STRING, 
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_BOOL
    };
    
    /** Creates a new instance of FrmSrcMemberPoin */
    public FrmSrcMemberPoin ()
    {
    }
    
    
	
    public FrmSrcMemberPoin (SrcMemberPoin srcMemberPoin)
    {
        this.srcMemberPoin = srcMemberPoin;
    }

    public FrmSrcMemberPoin (HttpServletRequest request, SrcMemberPoin srcMemberPoin)
    {
        super(new FrmSrcMemberPoin(srcMemberPoin), request);
        this.srcMemberPoin = srcMemberPoin;
    }
    public String[] getFieldNames ()
    {
        return fieldNames;
        //throw new UnsupportedOperationException ();
    }
    
    public int getFieldSize ()
    {
        return fieldNames.length;
        //throw new UnsupportedOperationException ();
    }
    
    public int[] getFieldTypes ()
    {
        return fieldTypes;
        //throw new UnsupportedOperationException ();
    }
    
    public String getFormName ()
    {
        return FRM_SRC_MEMBER_POIN; 
        //throw new UnsupportedOperationException ();
    }
    
    public void requestEntityObject(SrcMemberPoin secMemberPoin){
       try{
          	this.requestParam ();
                srcMemberPoin.setMemberCode (getString(FRM_FLD_MEMBER_CODE));
                srcMemberPoin.setMemberName (getString(FRM_FLD_MEMBER_NAME));
                srcMemberPoin.setPoinFrom (getInt(FRM_FLD_POIN_FROM));
                srcMemberPoin.setPoinTo (getInt(FRM_FLD_POIN_TO));
                srcMemberPoin.setInvoiceId (getLong (FRM_FLD_INVOICE_ID));
                srcMemberPoin.setInvoiceNumber (getString (FRM_FLD_INVOICE_NUMBER));
                srcMemberPoin.setMemberId (getLong (FRM_FLD_MEMBER_ID));
                srcMemberPoin.setUsePoinRange (getBoolean (FRM_FLD_USE_POIN_RANGE));
       }catch(Exception e){
           e.printStackTrace ();
       }
    }
    
    public static String toString(SrcMemberPoin srcMemberPoin){
        StringBuffer buff = new StringBuffer();
        buff.append ("\n "+srcMemberPoin.getMemberCode ());
        buff.append ("\n "+srcMemberPoin.getMemberName ());
        buff.append ("\n "+srcMemberPoin.getPoinFrom ());
        buff.append ("\n "+srcMemberPoin.getPoinTo ());        
        String result = new String(buff);
        return result;
        
        
    }
    
}
