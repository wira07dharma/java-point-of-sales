/*
 * SessMemberPoin.java
 *
 * Created on February 25, 2005, 4:57 PM
 */

package com.dimata.posbo.session.memberpoin;

import java.util.*;
import java.util.Date;
import java.sql.*;
import com.dimata.common.entity.contact.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;



/**
 *
 * @author  wpradnyana
 */
public class SessMemberPoin
{
    public static final int LOG_MODE_NONE = 0;
    public static final int LOG_MODE_CONSOLE = 1;
    public static final int LOG_MODE_FILE = 2;
    
    public static final String SRC_MEMBER_POIN="SRC_MEMBER_POIN"; 
    public static final String SRC_TRANSACTION_POIN="SRC_TRANSACTION_POIN"; 
    
    /** Creates a new instance of SessMemberPoin */
    public SessMemberPoin ()
    {
    }
    
    
    
    public static Vector getMemberPoinList(SrcMemberPoin srcMemberPoin,int start, int recordToGet,int logMode){
        
        
        /*
         *select 
            member.contact_id
            ,member.person_name
            ,cbm.cash_bill_main_id
            ,cbm.invoice_number
            ,sum((mps.debet - mps.credit  )) as mps_total
            , sum(mps.credit) as mps_credit
            , sum(mps.debet) as mps_debet
            from member_point_stock as mps
            , cash_bill_main as cbm
            , contact_list as member
            where 
            cbm.cash_bill_main_id = mps.cash_bill_main_id
            and 
            member.contact_id = mps.member_id 
            group by member.person_name

            order by
            cbm.invoice_number asc 

         *
         */
        Vector result = new Vector();
        DBResultSet dbrs = null;
        try{
            String sql = " select " +
            " mps.member_point_id,"+
            " mps.transaction_date,"+
            " member.contact_id," +
            " member.person_name," +
            " member.contact_code," +
            " cbm.cash_bill_main_id," +
            " cbm.invoice_number," +
            " cbm.bill_date," +
            " sum((mps.credit - mps.debet)) as mps_total" +
            " , sum(mps.credit) as mps_credit" +
            " , sum(mps.debet) as mps_debet " +
            " from " +
            " member_point_stock as mps" +
            " , cash_bill_main as cbm " +
            " , contact_list as member ";
            
            //" group by " +
            //" member.person_name " +
            //" order by " +
            //" cbm.invoice_number asc ";
            
            String memberId = "";
            if(srcMemberPoin.getMemberId()>0 ){
                memberId = "\n member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+" = '"+srcMemberPoin.getMemberId ()+"' "; 
            }
            
            String memberCode = "";
            if(srcMemberPoin.getMemberCode ()!=null &&srcMemberPoin.getMemberCode ().length ()>0){
                memberCode = "\n member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]+" like '%"+srcMemberPoin.getMemberCode ()+"%'"; 
            }
            String memberName = "";
            if(srcMemberPoin.getMemberName ()!=null &&srcMemberPoin.getMemberName().length ()>0){
                memberName = "\n member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" like '%"+srcMemberPoin.getMemberName ()+"%'";  
            }
            
            String invoiceNumber = ""; 
            if(srcMemberPoin.getInvoiceNumber ()!=null && srcMemberPoin.getInvoiceNumber ().length ()>0){
                invoiceNumber = "\n cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" like '%"+srcMemberPoin.getInvoiceNumber ()+"%'";  
            }
            
            String poinRange = "";
            //if(!srcMemberPoin.isUsePoinRange ()){
            //    if(srcMemberPoin.getPoinFrom()>=0&&srcMemberPoin.getPoinTo()>=0 && srcMemberPoin.getPoinTo()>srcMemberPoin.getPoinFrom()){ 
            //        poinRange = "\n sum(mps.debet) >= "+srcMemberPoin.getPoinFrom()+"  and  sum(mps.debet) <= "+srcMemberPoin.getPoinTo()+" ";  
            //    }
           // }
            
            
            
            String whereClause = " where " +
            " cbm.cash_bill_main_id = mps.cash_bill_main_id " +
            " and " +
            " member.contact_id = mps.member_id " ;
            
            if (memberId.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberId;
                } else {
                    whereClause = whereClause + memberId;
                }
            }
            if (memberCode.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberCode;
                } else {
                    whereClause = whereClause + memberCode;
                }
            }
            if (memberName.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberName;
                } else {
                    whereClause = whereClause + memberName;
                }
            }
            if (invoiceNumber.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceNumber;
                } else {
                    whereClause = whereClause + invoiceNumber;
                }
            }
            
            if (poinRange.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + poinRange;
                } else {
                    whereClause = whereClause + poinRange;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + "\n " + whereClause;
            }
            
            
            switch (srcMemberPoin.getGroupBy ()) {
                case SrcMemberPoin.GROUP_BY_MEMBER:
                    sql = sql + "\n  group by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "; 
                    break;                
                case SrcMemberPoin.GROUP_BY_ITEM:
                    sql = sql + "\n  group by cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" "; 
                    break;                
                default:
                    sql = sql + "\n  group by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "; 
                    break;
            }
            
            
            String descSort = "";
            if(srcMemberPoin.getSortMethod ()==srcMemberPoin.SORT_DESC){
                descSort = " DESC ";
            }else{
                descSort = " ASC ";
            }
            
            switch (srcMemberPoin.getSortBy()) {
                case SrcMemberPoin.SORT_BY_MEMBER_CODE: 
                    sql = sql + "\n  order by cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" "+descSort+" ";
                    break;
                case SrcMemberPoin.SORT_BY_MEMBER_NAME: 
                    sql = sql + "\n  order by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "+descSort+" ";
                    break;                
                default:
                    sql = sql + "\n  order by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "+descSort+" ";
                    break;
            }
            
            sql = sql + " LIMIT "+start+","+recordToGet; 
            
            
            if(logMode == LOG_MODE_CONSOLE){ 
                System.out.println("SALE SQl : " + sql);
            }else if(logMode == LOG_MODE_FILE){
                
            }else if(logMode == LOG_MODE_NONE){
                
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //int i =0;
            //System.out.println (i++); 
            while (rs.next()) {
                MemberPoinItem item = new MemberPoinItem();
                    item.setPoinId (rs.getLong("mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_POINT_ID]+""));
                    item.setTransactionDate (rs.getDate("mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_TRANSACTION_DATE]+""));
                    item.setInvoiceId (rs.getLong("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+""));
                    item.setInvoiceDate (rs.getDate("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+""));
                    item.setInvoiceNumber (rs.getString("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+""));
                    item.setMemberCode (rs.getString("member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]+""));
                    item.setMemberId (rs.getLong("member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+""));
                    item.setMemberName (rs.getString("member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+""));
                    item.setTotalPoin (rs.getInt("mps_debet"));
                    item.setTotalPoinFree (rs.getInt("mps_total"));
                    item.setTotalPoinUsed (rs.getInt("mps_credit"));
                    //if(logMode == LOG_MODE_CONSOLE){ 
                    //    System.out.println("SALE SQl : " + sql);
                    //}else if(logMode == LOG_MODE_FILE){
                
                   // }else if(logMode == LOG_MODE_NONE){
                
                    //} 
                if(!srcMemberPoin.isUsePoinRange ()){
                        if(item.getTotalPoinFree()>= srcMemberPoin.getPoinFrom () && item.getTotalPoinFree()<= srcMemberPoin.getPoinTo () ){
                            result.add (item);
                    }
                }else{
                            result.add (item);
                }
                
            }
            
            rs.close ();   
        }catch(Exception e){
            e.printStackTrace ();
        }finally{
            DBResultSet.close (dbrs);
        }
        
        return result;
        
    }
    
    public static Vector getInvoicePoinList(SrcMemberPoin srcMemberPoin,int start, int recordToGet,int logMode){
        
        
        /*
         *select 
            member.contact_id
            ,member.person_name
            ,cbm.cash_bill_main_id
            ,cbm.invoice_number
            ,sum((mps.debet - mps.credit  )) as mps_total
            , sum(mps.credit) as mps_credit
            , sum(mps.debet) as mps_debet
            from member_point_stock as mps
            , cash_bill_main as cbm
            , contact_list as member
            where 
            cbm.cash_bill_main_id = mps.cash_bill_main_id
            and 
            member.contact_id = mps.member_id 
            group by member.person_name

            order by
            cbm.invoice_number asc 

         *
         */
        Vector result = new Vector();
        DBResultSet dbrs = null;
        try{
            String sql = " select " +
            " mps.member_point_id,"+
            " mps.transaction_date,"+
            " member.contact_id," +
            " member.person_name," +
            " member.contact_code," +
            " cbm.cash_bill_main_id," +
            " cbm.invoice_number," +
            " cbm.bill_date," +
            " sum((mps.credit - mps.debet  )) as mps_total" +
            " , sum(mps.credit) as mps_credit" +
            " , sum(mps.debet) as mps_debet " +
            " from " +
            " member_point_stock as mps" +
            " , cash_bill_main as cbm " +
            " , contact_list as member ";
            
            //" group by " +
            //" member.person_name " +
            //" order by " +
            //" cbm.invoice_number asc ";
            String poinId = "";
            
            if(srcMemberPoin.getPoinId ()>0 ){
                poinId = "\n mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_POINT_ID]+" = '"+srcMemberPoin.getPoinId ()+"' "; 
            }
            
            String memberId = "";
            if(srcMemberPoin.getMemberId()>0 ){
                memberId = "\n member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+" = '"+srcMemberPoin.getMemberId ()+"' "; 
            }
            
            String memberCode = "";
            if(srcMemberPoin.getMemberCode ()!=null &&srcMemberPoin.getMemberCode ().length ()>0){
                memberCode = "\n member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]+" like '%"+srcMemberPoin.getMemberCode ()+"%'"; 
            }
            String memberName = "";
            if(srcMemberPoin.getMemberName ()!=null &&srcMemberPoin.getMemberName().length ()>0){
                memberName = "\n member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" like '%"+srcMemberPoin.getMemberName ()+"%'";  
            }
            
            String invoiceNumber = ""; 
            if(srcMemberPoin.getInvoiceNumber ()!=null && srcMemberPoin.getInvoiceNumber ().length ()>0){
                invoiceNumber = "\n cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" like '%"+srcMemberPoin.getInvoiceNumber ()+"%'";  
            }
            
            String poinRange = "";
            //if(!srcMemberPoin.isUsePoinRange ()){
            //    if(srcMemberPoin.getPoinFrom()>=0&&srcMemberPoin.getPoinTo()>=0 && srcMemberPoin.getPoinTo()>srcMemberPoin.getPoinFrom()){ 
            //        poinRange = "\n sum(mps.debet) >= "+srcMemberPoin.getPoinFrom()+"  and  sum(mps.debet) <= "+srcMemberPoin.getPoinTo()+" ";  
            //    }
           // }
            
            
            
            String whereClause = " where " +
            " cbm.cash_bill_main_id = mps.cash_bill_main_id " +
            " and " +
            " member.contact_id = cbm.customer_id " ;
            
            if (poinId.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + poinId;
                } else {
                    whereClause = whereClause + poinId;
                }
            }
            if (memberId.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberId;
                } else {
                    whereClause = whereClause + memberId;
                }
            }
            if (memberCode.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberCode;
                } else {
                    whereClause = whereClause + memberCode;
                }
            }
            if (memberName.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + memberName;
                } else {
                    whereClause = whereClause + memberName;
                }
            }
            if (invoiceNumber.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invoiceNumber;
                } else {
                    whereClause = whereClause + invoiceNumber;
                }
            }
            
            if (poinRange.length() > 0) {   
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + poinRange;
                } else {
                    whereClause = whereClause + poinRange;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + "\n " + whereClause;
            }
            
            
            switch (srcMemberPoin.getGroupBy ()) {
                case SrcMemberPoin.GROUP_BY_MEMBER:
                    sql = sql + "\n  group by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "; 
                    break;                
                case SrcMemberPoin.GROUP_BY_ITEM:
                    sql = sql + "\n  group by cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" "; 
                    break;                
                default:
                    sql = sql + "\n  group by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "; 
                    break;
            }
            
            
            String descSort = "";
            if(srcMemberPoin.getSortMethod ()==srcMemberPoin.SORT_DESC){
                descSort = " DESC ";
            }else{
                descSort = " ASC ";
            }
            
            switch (srcMemberPoin.getSortBy()) {
                case SrcMemberPoin.SORT_BY_MEMBER_CODE: 
                    sql = sql + "\n  order by cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" "+descSort+" ";
                    break;
                case SrcMemberPoin.SORT_BY_MEMBER_NAME: 
                    sql = sql + "\n  order by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "+descSort+" ";
                    break;                
                default:
                    sql = sql + "\n  order by member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" "+descSort+" ";
                    break;
            }
            
            sql = sql + " LIMIT "+start+","+recordToGet; 
            
            
            if(logMode == LOG_MODE_CONSOLE){ 
                System.out.println("SALE SQl : " + sql);
            }else if(logMode == LOG_MODE_FILE){
                
            }else if(logMode == LOG_MODE_NONE){
                
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //int i =0;
            //System.out.println (i++); 
            while (rs.next()) {
                MemberPoinItem item = new MemberPoinItem();
                    item.setPoinId (rs.getLong("mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_POINT_ID]+""));
                    Date tDate = rs.getDate("mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_TRANSACTION_DATE]+"");
                    item.setTransactionDate (tDate);
                    item.setInvoiceId (rs.getLong("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+""));
                    tDate = rs.getDate("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+"");
                    item.setInvoiceDate (tDate);
                    item.setInvoiceNumber (rs.getString("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+""));
                    item.setMemberCode (rs.getString("member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]+""));
                    item.setMemberId (rs.getLong("member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+""));
                    item.setMemberName (rs.getString("member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+""));
                    item.setTotalPoin (rs.getInt("mps_debet"));
                    item.setTotalPoinFree (rs.getInt("mps_total"));
                    item.setTotalPoinUsed (rs.getInt("mps_credit"));
                    
                    //if(logMode == LOG_MODE_CONSOLE){ 
                    //    System.out.println("SALE SQl : " + sql);
                    //}else if(logMode == LOG_MODE_FILE){
                
                   // }else if(logMode == LOG_MODE_NONE){
                
                    //} 
                if(!srcMemberPoin.isUsePoinRange ()){
                    //    if(item.getTotalPoinFree()>= srcMemberPoin.getPoinFrom () && item.getTotalPoinFree()<= srcMemberPoin.getPoinTo () ){
                            result.add (item);
                    //}
                }else{
                            result.add (item);
                }
                
            }
            
            rs.close ();   
        }catch(Exception e){
            e.printStackTrace ();
        }finally{
            DBResultSet.close (dbrs);
        }
        
        return result;
        
    }
    public static Vector getInvoiceList(SrcMemberPoin srcMemberPoin,int start,int recordToGet,int logMode){ 
        Vector result = new Vector();
        /*
         *select 
            cbm.cash_bill_main_id,
            cbm.invoice_number,
            mps.member_point_id,
            member.person_name,
            member.contact_id
            from 
            cash_bill_main as cbm
            left join member_point_stock as mps 
            on cbm.cash_bill_main_id = mps.cash_bill_main_id
            inner join contact_list as member
            on member.contact_id = mps.member_id
            where 
            (doc_type=0 or doc_type=3)

         */
        DBResultSet dbrs = null;
        
        String sql="select "+
            "\n mps.member_point_id,"+
            "\n cbm.cash_bill_main_id, "+
            "\n cbm.invoice_number, "+
            "\n cbm.bill_date, "+
            "\n mps.member_point_id, "+
            "\n mps.transaction_date, "+
            "\n member.person_name, "+
            "\n member.contact_id, "+
            "\n member.contact_code, "+
            "\n mps.debet "+
            "\n from "+
            "\n cash_bill_main as cbm "+
            "\n left join member_point_stock as mps "+
            "\n on cbm.cash_bill_main_id = mps.cash_bill_main_id "+
            "\n inner join contact_list as member "+
            "\n on member.contact_id = mps.member_id ";
            
        String whereClause = "\n where "+
                    "\n (doc_type=0 or doc_type=3)";
        sql = sql + whereClause;
        
        try{
            dbrs = DBHandler.execQueryResult (sql);
            ResultSet rs = dbrs.getResultSet ();
            //System.out.println(sql);
            while(rs.next ()){
                MemberPoinItem item = new MemberPoinItem();
                item.setPoinId (rs.getLong("mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_POINT_ID]+""));
                item.setInvoiceId (rs.getLong ("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
                item.setInvoiceNumber ( rs.getString("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                item.setInvoiceDate ( rs.getDate("cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                item.setTotalPoin (rs.getInt("mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_DEBET])); 
                item.setTransactionDate (rs.getDate("mps."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_TRANSACTION_DATE])); 
                item.setMemberName (rs.getString("member."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME])); 
                item.setMemberCode (rs.getString("member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE])); 
                item.setMemberId (rs.getLong("member."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));  
                //System.out.println (item.printValue ()); 
                result.add(item); 
            }
            
        }catch(Exception e){
            e.printStackTrace ();
        }finally{
            DBResultSet.close (dbrs);
        }
        return result; 
    }
    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) 
    {
        SessMemberPoin sessMemberPoin = new SessMemberPoin ();
        SrcMemberPoin srcMemberPoin = new SrcMemberPoin();
        //srcMemberPoin.setMemberName ("nyoman");   
        srcMemberPoin.setPoinFrom (100);
        srcMemberPoin.setPoinTo(100000);
        //srcMemberPoin.setMemberId (Long.parseLong ("504404263865586630"));
        //srcMemberPoin.setPoinFrom (0);
        //srcMemberPoin.setPoinTo (10000);
        
        Vector result = sessMemberPoin.getMemberPoinList (srcMemberPoin, 0, 100, 1);
        Enumeration en = result.elements ();
        while(en.hasMoreElements ()){
            MemberPoinItem item = (MemberPoinItem)en.nextElement ();
            //System.out.println(item.printValue ());
        }
        // TODO code application logic here
        //Vector result = SessMemberPoin.getInvoiceList (srcMemberPoin, 0, 100, 1);
        //Enumeration en = result.elements ();
        //while(en.hasMoreElements ()){
        //    MemberPoinItem item = (MemberPoinItem)en.nextElement ();
        //    System.out.println(item.printValue ()); 
        //}
    }
    
}
