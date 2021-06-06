/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.purchasing;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import java.util.*;
import java.util.Date;
import java.sql.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;

import com.dimata.posbo.entity.purchasing.PstPurchaseRequest;
import com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem;
import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.posbo.entity.purchasing.PurchaseRequestItem;
import com.dimata.posbo.entity.search.SrcPurchaseRequest;
import com.dimata.posbo.session.purchasing.SessPurchaseWithPurchaseRequets;
import com.dimata.qdep.form.FRMQueryString;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author dimata005
 */
public class SessPurchaseRequest {

    public static final String SESS_SRC_REQUESTMATERIAL = "SESSION_SRC_REQUESTMATERIAL";

    public static final int TYPE_GET_YEAR = 0;
    public static final int TYPE_GET_MONTH = 1;


    public static String getYearMonth(Date date, int getType) {
        String str = "";
        try {
            switch (getType) {
                case TYPE_GET_YEAR:
                    str = "" + date.getYear();
                    str = str.substring(str.length() - 2, str.length());
                    break;

                case TYPE_GET_MONTH:
                    str = "" + (date.getMonth() + 1);
                    if (str.length() != 2)
                        str = "0" + str;
                    break;

                default:
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        }
        return str;
    }

    public static String getCounter(int counter) {
        String strCounter = "";
        String str = String.valueOf(counter);
        switch (str.length()) {
            case 1:
                strCounter = "00" + counter;
                break;
            case 2:
                strCounter = "0" + counter;
                break;
            case 3:
                strCounter = "" + counter;
                break;
            default:
                strCounter = "" + counter;
        }
        return strCounter;
    }

    public static int getIntCode(PurchaseRequest purchaseRequest, Date pDate, long oid, int counter, boolean isIncrement) {
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
        try {
            String sql = "SELECT MAX(" + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE_COUNTER] + ") AS PMAX" +
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST +
                    " WHERE YEAR(" + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] + ") " +
                    " = " + (purchaseRequest.getPurchRequestDate().getYear() + 1900) + "" +
                    " AND MONTH(" + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] + ") " +
                    " = " + (purchaseRequest.getPurchRequestDate().getMonth() + 1) +
                    " AND " + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] +
                    " <> " + oid +
                    " AND " + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID] +
                    " = " + purchaseRequest.getLocationId();

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                max = rs.getInt("PMAX");
            }

            if (oid == 0) {
                max = max + 1;
            } else {
                if (purchaseRequest.getPurchRequestDate() != pDate)
                    max = max + 1;
                else
                    max = counter;
            }
        } catch (Exception e) {
            System.out.println("!!!!!SessOrderMaterial.getIntCode() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return max;
    }

    public static String getCodeOrderMaterial(PurchaseRequest purchaseRequest) {
        String strCode = "";
        try {
            //I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();
            String documentCode = "PR";

            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+purchaseRequest.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);

            strCode = location.getCode() + "-" +
                    getYearMonth(purchaseRequest.getPurchRequestDate(), TYPE_GET_YEAR) +
                    "" + getYearMonth(purchaseRequest.getPurchRequestDate(), TYPE_GET_MONTH) +
                    "-" + documentCode +
                    "-" + getCounter(purchaseRequest.getPeCodeCounter());

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        }
        return strCode;
    }


    public static int getCountPurchaseRequestMaterial(SrcPurchaseRequest srcpurchaserequest, int docType, int typeRequest, String whereClausex) {
        if (srcpurchaserequest != null)
            System.out.println("Not null!!!");
        else
            System.out.println("Null");
        Vector vectOrderCode = LogicParser.textSentence(srcpurchaserequest.getPrmnumber());
        for (int i = 0; i < vectOrderCode.size(); i++) {
            String name = (String) vectOrderCode.get(i);
            //System.out.println("name : " + name);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectOrderCode.remove(i);
        }

        return getCountPRMaterialList(docType, vectOrderCode, srcpurchaserequest, 0, typeRequest,"",whereClausex);
    }
    
     public static int xGetCountPurchaseRequestMaterial(SrcPurchaseRequest srcpurchaserequest, int docType, int typeRequest, String whereClausex) {
        if (srcpurchaserequest != null)
            System.out.println("Not null!!!");
        else
            System.out.println("Null");
        Vector vectOrderCode = LogicParser.textSentence(srcpurchaserequest.getPrmnumber());
        for (int i = 0; i < vectOrderCode.size(); i++) {
            String name = (String) vectOrderCode.get(i);
            //System.out.println("name : " + name);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectOrderCode.remove(i);
        }

        return xgetCountPRMaterialList(docType, vectOrderCode, srcpurchaserequest, 0, typeRequest,"",whereClausex);
    }
    
    public static int getCountTransferRequestMaterial(SrcPurchaseRequest srcpurchaserequest, int docType, int typeRequest, String whereClausex) {
        if (srcpurchaserequest != null)
            System.out.println("Not null!!!");
        else
            System.out.println("Null");
        Vector vectOrderCode = LogicParser.textSentence(srcpurchaserequest.getPrmnumber());
        for (int i = 0; i < vectOrderCode.size(); i++) {
            String name = (String) vectOrderCode.get(i);
            //System.out.println("name : " + name);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectOrderCode.remove(i);
        }
        String strStatus = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " = '2' )";
        return getCountPRMaterialList(docType, vectOrderCode, srcpurchaserequest, 0, typeRequest,strStatus, whereClausex);
    }
    
   

    public static int getCountPRMaterialList(int docType, Vector vectNumber, SrcPurchaseRequest srcpurchaserequest, long oidVendor, int typeRequest, String where, String whereLocation ) {
        DBResultSet dbrs = null;
        int count = 0;
        try {

           // I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();

            String sql = "SELECT " +
                    " COUNT(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] + ") AS CNT " +
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " AS MAT"+
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"= MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID];

            String strPoNumber = "";
            if (vectNumber != null && vectNumber.size() > 0) {
                for (int a = 0; a < vectNumber.size(); a++) {
                    if (strPoNumber.length() != 0) {
                        strPoNumber = strPoNumber + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    } else {
                        strPoNumber = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    }
                }
                strPoNumber = "(" + strPoNumber + ")";
            }
            
           String strStatus = "";//(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " = '2' ) AND ";
           if(typeRequest==0){//type purchase request
               strStatus=strStatus+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"=0) ";
           }else{//type transfer request
               strStatus=strStatus+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"!=0) ";
           }
            
            String strDate = "";
            if (srcpurchaserequest.getStatusdate() != 0) {
                String startDate = Formater.formatDate(srcpurchaserequest.getPrmdatefrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcpurchaserequest.getPrmdateto(), "yyyy-MM-dd");
                strDate = "MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            
            String strStatusDoc = "";
            if (srcpurchaserequest.getPrmstatus() != null && srcpurchaserequest.getPrmstatus().size() > 0) {
                for (int a = 0; a < srcpurchaserequest.getPrmstatus().size(); a++) {
                    if (strStatusDoc.length() != 0) {
                        strStatusDoc = strStatusDoc + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcpurchaserequest.getPrmstatus().get(a) + ")";
                    } else {
                        strStatusDoc = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcpurchaserequest.getPrmstatus().get(a) + ")";
                    }
                }
                strStatusDoc = "(" + strStatusDoc + ")";
            }

            
            
            
            String whereClause = "";
            if (strPoNumber.length() > 0)
                whereClause = strPoNumber;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            
            if (strStatusDoc.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatusDoc;
                } else {
                    whereClause = whereClause + strStatusDoc;
                }
            }

          //kenapa ada ini?  
//           String strStatusWare ="";
//           if(typeRequest==0){//type purchase request
//               strStatusWare=strStatusWare+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"=0) ";
//           }else{//type transfer request
//               strStatusWare=strStatusWare+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"!=0) ";
//           }
           
           if(!"".equals(where)){
               strStatus= strStatus + " AND " + where;
           }
           
           if(!"".equals(whereLocation)){
               strStatus= strStatus + " AND " + whereLocation;
           }
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strStatus;
            } else {
                whereClause = whereClause + strStatus;
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                count = rs.getInt("CNT");
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static int xgetCountPRMaterialList(int docType, Vector vectNumber, SrcPurchaseRequest srcpurchaserequest, long oidVendor, int typeRequest, String where, String whereLocation ) {
        DBResultSet dbrs = null;
        int count = 0;
        try {

           // I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();

            String sql = "SELECT " +
                    " COUNT(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] + ") AS CNT " +
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " AS MAT"+
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"= MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID];

            String strPoNumber = "";
            if (vectNumber != null && vectNumber.size() > 0) {
                for (int a = 0; a < vectNumber.size(); a++) {
                    if (strPoNumber.length() != 0) {
                        strPoNumber = strPoNumber + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    } else {
                        strPoNumber = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    }
                }
                strPoNumber = "(" + strPoNumber + ")";
            }
            
           String strStatus = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " = '2' )  ";
           if(typeRequest==0){//type purchase request
               strStatus=strStatus+" AND (MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"=0) ";
           }else{//type transfer request
               strStatus=strStatus+" AND (MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"!=0) ";
           }
            
            String strDate = "";
            if (srcpurchaserequest.getStatusdate() != 0) {
                String startDate = Formater.formatDate(srcpurchaserequest.getPrmdatefrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcpurchaserequest.getPrmdateto(), "yyyy-MM-dd");
                strDate = "MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            
            String strStatusDoc = "";
            if (srcpurchaserequest.getPrmstatus() != null && srcpurchaserequest.getPrmstatus().size() > 0) {
                for (int a = 0; a < srcpurchaserequest.getPrmstatus().size(); a++) {
                    if (strStatusDoc.length() != 0) {
                        strStatusDoc = strStatusDoc + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcpurchaserequest.getPrmstatus().get(a) + ")";
                    } else {
                        strStatusDoc = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcpurchaserequest.getPrmstatus().get(a) + ")";
                    }
                }
                strStatusDoc = "(" + strStatusDoc + ")";
            }

            
            
            
            String whereClause = "";
            if (strPoNumber.length() > 0)
                whereClause = strPoNumber;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            
            if (strStatusDoc.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatusDoc;
                } else {
                    whereClause = whereClause + strStatusDoc;
                }
            }

          //kenapa ada ini?  
//           String strStatusWare ="";
//           if(typeRequest==0){//type purchase request
//               strStatusWare=strStatusWare+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"=0) ";
//           }else{//type transfer request
//               strStatusWare=strStatusWare+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"!=0) ";
//           }
           
           if(!"".equals(where)){
               strStatus= strStatus + " AND " + where;
           }
           
           if(!"".equals(whereLocation)){
               strStatus= strStatus + " AND " + whereLocation;
           }
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strStatus;
            } else {
                whereClause = whereClause + strStatus;
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                count = rs.getInt("CNT");
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    public static Vector searchPurchaseRequestMaterial(SrcPurchaseRequest srcpurchaserequest, int docType, int start, int recordToGet,int typeRequest, String whereLoc) {
        Vector vectOrderCode = LogicParser.textSentence(srcpurchaserequest.getPrmnumber());
        for (int i = 0; i < vectOrderCode.size(); i++) {
            String name = (String) vectOrderCode.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectOrderCode.remove(i);
        }
        return getPurchaseRequestMaterialList(docType, vectOrderCode, srcpurchaserequest, start, recordToGet, 0, typeRequest,whereLoc);
    }
    
    /**
     * created: opie-eyek-20140220 
     * @param srcpurchaserequest
     * @param docType
     * @param start
     * @param recordToGet
     * @return 
     * @see untuk mencari purchase request yang bisa di create po, status pr harus status final
     */
    public static Vector searchPurchaseRequestForPOMaterial(SrcPurchaseRequest srcpurchaserequest, int docType, int start, int recordToGet, int typeRequest, String whereLocation) {
        Vector vectOrderCode = LogicParser.textSentence(srcpurchaserequest.getPrmnumber());
        for (int i = 0; i < vectOrderCode.size(); i++) {
            String name = (String) vectOrderCode.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectOrderCode.remove(i);
        }
        return getPurchaseRequestForPoMaterialList(docType, vectOrderCode, srcpurchaserequest, start, recordToGet, 0, typeRequest,whereLocation);
    }

    public static Vector getPurchaseRequestMaterialList(int docType, Vector vectNumber, SrcPurchaseRequest srcpurchaserequest, int start, int recordToGet, long oidVendor, int typeRequest, String whereLocation) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
           // I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();
            String sql = "SELECT MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID] +
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " AS MAT"+
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID];

            String strPoNumber = "";
            if (vectNumber != null && vectNumber.size() > 0) {
                for (int a = 0; a < vectNumber.size(); a++) {
                    if (strPoNumber.length() != 0) {
                        strPoNumber = strPoNumber + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    } else {
                        strPoNumber = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    }
                }
                strPoNumber = "(" + strPoNumber + ")";
            }

            String strDate = "";
            if (srcpurchaserequest.getStatusdate() != 0) {
                String startDate = Formater.formatDate(srcpurchaserequest.getPrmdatefrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcpurchaserequest.getPrmdateto(), "yyyy-MM-dd");
                strDate = " MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String strStatus = "";
            if (srcpurchaserequest.getPrmstatus() != null && srcpurchaserequest.getPrmstatus().size() > 0) {
                for (int a = 0; a < srcpurchaserequest.getPrmstatus().size(); a++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcpurchaserequest.getPrmstatus().get(a) + ")";
                    } else {
                        strStatus = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcpurchaserequest.getPrmstatus().get(a) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            String whereClause = "";
            
            if(typeRequest==0){
                whereClause= " MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"='0'";
            }else{
                whereClause= " MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"!='0'";
            }
            
            if (strPoNumber.length() > 0)
                whereClause = strPoNumber;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatus;
                } else {
                    whereClause = whereClause + strStatus;
                }
            }
            
            if(!"".equals(whereLocation)){
               whereClause= whereClause + " AND " + whereLocation;
           }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            switch (srcpurchaserequest.getSortby()) {
                case 0:
                    //sql = sql + " ORDER BY MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]+" DESC ";
                    break;
                case 1:
                    sql = sql + " ORDER BY MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE];
                    break;
                case 2:
                    sql = sql + " ORDER BY MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS];
                    break;
            }

            //sql += ", MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]; /** defaultnya, list diurut berdasarkan DATE */
            sql +=  " LIMIT " + start + "," + recordToGet;

            System.out.println("sql getPurchaseOrderMaterialList: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                PurchaseRequest purchaseOrder = new PurchaseRequest();
                Vector vt = new Vector(1, 1);

                purchaseOrder.setOID(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]));
                purchaseOrder.setPrCode(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]));
                purchaseOrder.setPurchRequestDate(rs.getDate(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]));
                purchaseOrder.setPrStatus(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]));
                purchaseOrder.setRemark(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK]));
                purchaseOrder.setLocationId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]));
                purchaseOrder.setWarehouseSupplierId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID])); 
                vt.add(purchaseOrder);

                result.add(vt);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * created : opie-eyek-20140220
     * @param docType
     * @param vectNumber
     * @param srcpurchaserequest
     * @param start
     * @param recordToGet
     * @param oidVendor
     * @return : untuk mencari list dari pr yang ingin di buatkan po, status pr harus final
     */ 
    public static Vector getPurchaseRequestForPoMaterialList(int docType, Vector vectNumber, SrcPurchaseRequest srcpurchaserequest, int start, int recordToGet, long oidVendor, int typeRequest, String whereLocation) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
           // I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();
            String sql = "SELECT MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID] +
                    ", LC."+PstLocation.fieldNames[PstLocation.FLD_NAME]+
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " AS MAT "+
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID];

            String strPoNumber = "";
            if (vectNumber != null && vectNumber.size() > 0) {
                for (int a = 0; a < vectNumber.size(); a++) {
                    if (strPoNumber.length() != 0) {
                        strPoNumber = strPoNumber + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    } else {
                        strPoNumber = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] + " LIKE '%" + vectNumber.get(a) + "%')";
                    }
                }
                strPoNumber = "(" + strPoNumber + ")";
            }

            String strDate = "";
            if (srcpurchaserequest.getStatusdate() != 0) {
                String startDate = Formater.formatDate(srcpurchaserequest.getPrmdatefrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcpurchaserequest.getPrmdateto(), "yyyy-MM-dd");
                strDate = " MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

            String whereClause = "";
            if (strPoNumber.length() > 0)
                whereClause = strPoNumber;

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }
            
            String strStatus = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " = '2' ) AND ";
                   if(typeRequest==0){//type purchase request
                       strStatus=strStatus+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"=0) ";
                   }else{//type transfer request
                       strStatus=strStatus+"(MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"!=0) ";
                   }
                   
            
            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND " + strStatus;
            } else {
                whereClause = whereClause + strStatus;
            }

            if(!"".equals(whereLocation)){
               if (whereClause.length() > 0) {
                   whereClause= whereClause + " AND " + whereLocation;
               }else{
                   whereClause= whereClause + whereLocation;
               }
               
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            

            switch (srcpurchaserequest.getSortby()) {
                case 0:
                    sql = sql + " ORDER BY MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE];
                    break;
                case 1:
                    sql = sql + " ORDER BY MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE];
                    break;
                case 2:
                    sql = sql + " ORDER BY MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS];
                    break;
            }

            sql += ", MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]; /** defaultnya, list diurut berdasarkan DATE */
            sql +=  " LIMIT " + start + "," + recordToGet;

            System.out.println("sql getPurchaseOrderMaterialList: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                PurchaseRequest purchaseOrder = new PurchaseRequest();
                Vector vt = new Vector(1, 1);

                purchaseOrder.setOID(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]));
                purchaseOrder.setPrCode(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]));
                purchaseOrder.setPurchRequestDate(rs.getDate(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]));
                purchaseOrder.setPrStatus(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]));
                purchaseOrder.setRemark(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK]));
                purchaseOrder.setLocationName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
                
                vt.add(purchaseOrder);

                result.add(vt);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    public static Vector getPurchaseRequestForPo(HttpServletRequest request){
         Vector vectQty = new Vector(1,1);
         Vector vectMaterialList = new Vector(1,1);
         Vector vectMaterialListFilter = new Vector(1,1);
         String[] pomaterial = request.getParameterValues("pomaterial");
		 String createFrom = FRMQueryString.requestString(request, "create_from");
		 if(createFrom.equals("storeRequest")){
			 long documentPrId = FRMQueryString.requestLong(request, "documentPrId");
			 String[] newPomMaterial = {String.valueOf(documentPrId)};
			 pomaterial = newPomMaterial;
		 }
         String whereClause="";
         if(pomaterial.length > 0){
             boolean first = true;
             for(int k=0;k < pomaterial.length;k++){
                    long oidMaterial = Long.parseLong(pomaterial[k]);
                    if(FRMQueryString.requestInt(request, "pomaterial_"+oidMaterial)==1){
                         //buatkan kondisi untuk list yang di centang saja
                         if(first){
                             whereClause= " PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"='"+oidMaterial+"'";
                             first=false;
                         }else{
                              whereClause += " OR PR."+ PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"='"+oidMaterial+"'";
                         }
                    }   
                }
               //buatkan list material dan kuantity yang di inginkan 
               if(whereClause.length()>0){
                        vectMaterialList = getListPrToPo(whereClause);  
                        vectQty=getVectorPrToPo(vectMaterialList);
//                        vectMaterialList = getListMaterialFromPRForPO(whereClause);  
//                        vectMaterialListFilter=getFilterListMaterialFromPRForPO(whereClause); 
//                        vectQty=getListMaterialFromPRForPOFilter(vectMaterialList,vectMaterialListFilter);
               }
          }
        
        return vectQty;
    }
    
    
     public static Vector getListMaterialFromPRForPO(String whereClause) {
         Vector ListMaterialFromPRForPO = new Vector();
         DBResultSet dbrs = null;
         try{
          String sql =  " SELECT LC.NAME,PM.SKU,PM.NAME, PRI.MATERIAL_ID, PRI.UNIT_ID,PRI.QTY,PM.SKU, PRI.PURCHASE_REQUEST_ITEM_ID  "+
                        " FROM "+PstPurchaseRequestItem.TBL_PURCHASE_REQUEST_ITEM+" AS PRI "+
                        " INNER JOIN "+PstPurchaseRequest.TBL_PURCHASE_REQUEST+" AS PR "+
                        " ON PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+
                        " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                        " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]+
                        " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS PM "+
                        " ON PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID]+
                        " WHERE "+whereClause+
                        " ORDER BY PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_ID]+", PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_TERM_PURCHASE_REQUEST];
          System.out.println("getListMaterialFromPRForPO : "+sql);
          dbrs = DBHandler.execQueryResult(sql);
           ResultSet rs = dbrs.getResultSet();

           while (rs.next()) {
                Vector v3 = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong("PRI.MATERIAL_ID"));
                material.setSku(rs.getString("PM.SKU"));
                material.setName(rs.getString("PM.NAME"));
                material.setDefaultStockUnitId(rs.getLong("PRI.UNIT_ID"));
                v3.add(material);
                
                double qty = rs.getDouble("PRI.QTY");
                v3.add(String.valueOf(qty));
                
                String locationName = rs.getString("LC.NAME");
                v3.add(String.valueOf(locationName));
                
                ListMaterialFromPRForPO.add(v3);
            }
            rs.close();
         }catch (Exception e) {
            System.out.println("Err : " + e.toString());
         } finally {
            DBResultSet.close(dbrs);
         }
         
         return ListMaterialFromPRForPO;
     }
     
     public static Vector getListMaterialFromPRForPOFilter(Vector listMaterial,Vector vectMaterialListFilter) {
         Vector filterListMaterialForPo = new Vector();
         for(int i=0; i<vectMaterialListFilter.size(); i++){
             
            Vector v1 = (Vector)vectMaterialListFilter.get(i);
            
            SessPurchaseWithPurchaseRequets material = (SessPurchaseWithPurchaseRequets) v1.get(0);
            
            Vector v3 = new Vector();
            
            v3.add(material); 
            
            double qty = getOidMaterialByVector(listMaterial,material.getOidMaterial(), material.getPurchaseRequestID());
            
            v3.add(String.valueOf(qty));
            
            //qty order
            v3.add(String.valueOf(""));
            
            //add current stok
            v3.add(String.valueOf(""));
            if(v1.size() > 1){
            String locName = String.valueOf( v1.get(1)); 
            v3.add(String.valueOf(locName));
            }
            filterListMaterialForPo.add(v3); 
         }
         
         return filterListMaterialForPo;
     }
     public static Vector getVectorPrToPo(Vector listMaterial) {
         Vector filterListMaterialForPo = new Vector();
         for(int i=0; i<listMaterial.size(); i++){
             
            Vector v1 = (Vector)listMaterial.get(i);
            SessPurchaseWithPurchaseRequets material = (SessPurchaseWithPurchaseRequets) v1.get(0);
            
            Vector v3 = new Vector();
            v3.add(material); 
            
            double qty = Double.parseDouble((String)v1.get(1));
            v3.add(String.valueOf(qty));

            String locName = String.valueOf( v1.get(2)); 
            v3.add(String.valueOf(locName));
            
            filterListMaterialForPo.add(v3); 
         }
         
         return filterListMaterialForPo;
     }
     
     public static double getCurrentStok(long locationId,long oidMaterial,long periodeId){
          double currentStok=0.0;
          DBResultSet dbrs = null;
          try{
               String sql =  " SELECT SUM("+PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]+") FROM "+PstMaterialStock.TBL_MATERIAL_STOCK+
                             " WHERE "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"='"+locationId+"'"+
                             " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"='"+oidMaterial+"'"+
                             " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"='"+periodeId+"'";
               
               dbrs = DBHandler.execQueryResult(sql);
               ResultSet rs = dbrs.getResultSet();
               while (rs.next()) {
                    currentStok=rs.getDouble(1);
               }
            rs.close();
         }catch (Exception e) {
            System.out.println("Err : " + e.toString());
         } finally {
            DBResultSet.close(dbrs);
         }
          return currentStok;
     }
     
     
      public static double getMinimumStock(long locationId,long oidMaterial,long periodeId){
          double minimumStock=0.0;
          DBResultSet dbrs = null;
          try{
                String sql =  " SELECT SUM("+PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN]+") FROM "+PstMaterialStock.TBL_MATERIAL_STOCK+
                             " WHERE "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"='"+locationId+"'"+
                             " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"='"+oidMaterial+"'"+
                             " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"='"+periodeId+"'";
               
               dbrs = DBHandler.execQueryResult(sql);
               ResultSet rs = dbrs.getResultSet();
               while (rs.next()) {
                    minimumStock=rs.getDouble(1);
               }
            rs.close();
          }catch (Exception e) {
            System.out.println("Err : " + e.toString());
          } finally {
            DBResultSet.close(dbrs);
          }        
          return minimumStock;
      }
     
     public static double getOidMaterialByVector(Vector list, long oidMaterial, long purchaseId){
        double qty = 0;
        try{
            if(list!=null && list.size()>0){
                long oidPur = 0;
                for(int k=0;k<list.size();k++){
                    Vector v1 = (Vector)list.get(k);
                    Material material = (Material)v1.get(0);
                    long oidmat = material.getOID();
                    if(purchaseId != 0){
                    if(oidMaterial == oidmat && purchaseId != oidPur){
                        oidPur = purchaseId;
                        qty += Double.parseDouble((String)v1.get(1));
                        //list.remove(k);
                    }
                    }else{
                    if(oidMaterial == oidmat){
                        qty += Double.parseDouble((String)v1.get(1));
                        //list.remove(k);
                    }
                    }
                }
            }
        }catch(Exception e){
        
        }
        
        return qty;
    }
     public static String getLocationByVector(Vector list, long oidMaterial, long purchaseId, int i){
        String locName = "";
        try{
            if(list!=null && list.size()>0){
                long oidPur = 0;
                for(int k=0;k<list.size();k++){
                    Vector v1 = (Vector)list.get(k);
                    Material material = (Material)v1.get(0);
                    String locNameTemp =String.valueOf( v1.get(2)); 
                    long oidmat = material.getOID();
                   if(oidMaterial == oidmat && purchaseId != oidPur){
                        oidPur = purchaseId;
                        locName += String.valueOf((String)v1.get(2)); 
                        //list.remove(k);
                    }
                }
            }
        }catch(Exception e){
          System.out.println("Get Location exception: " + e.getMessage());
          e.printStackTrace();
        }
        
        return locName;
    }
 public static Vector listPurchase(int start , int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs=null;
        try {
            String sql = "SELECT MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID] +
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " AS MAT"+
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID];


            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;


            if((start == 0)&&(recordToGet == 0))
                sql = sql + "" ;  //nothing to do
            else
                sql = sql + " LIMIT " + start + ","+ recordToGet ;

            System.out.println("Purchase : "+sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                PurchaseRequest pr = new PurchaseRequest();
                pr.setOID(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]));
                pr.setPrCode(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]));
                pr.setPurchRequestDate(rs.getDate(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]));
                pr.setPrStatus(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]));
                pr.setRemark(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK]));
                pr.setLocationId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]));
                pr.setWarehouseSupplierId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID])); 
                lists.add(pr);
            }
            rs.close();

        }catch(Exception e) {
            System.out.println(e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        return lists;
    }
 
 public static Vector listPurchaseForPO(int start , int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs=null;
        try {
            String sql = "SELECT MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID] +
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " AS MAT"+
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID];


            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;


            if((start == 0)&&(recordToGet == 0))
                sql = sql + "" ;  //nothing to do
            else
                sql = sql + " LIMIT " + start + ","+ recordToGet ;

            //System.out.println(sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                PurchaseRequest pr = new PurchaseRequest();
                pr.setOID(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]));
                pr.setPrCode(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]));
                pr.setPurchRequestDate(rs.getDate(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]));
                pr.setPrStatus(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]));
                pr.setRemark(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK]));
                pr.setLocationId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]));
                pr.setWarehouseSupplierId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID])); 
                lists.add(pr);
            }
            rs.close();

        }catch(Exception e) {
            System.out.println(e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        return lists;
    }
 
 public static Vector listPurchaseRequest(int start , int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs=null;
        try {
            String sql = "SELECT MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID] +
                    ", MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID] +
                    " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " AS MAT"+
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID];


            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;


            if((start == 0)&&(recordToGet == 0))
                sql = sql + "" ;  //nothing to do
            else
                sql = sql + " LIMIT " + start + ","+ recordToGet ;

            System.out.println("Transfer : "+sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                PurchaseRequest pr = new PurchaseRequest();
                pr.setOID(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]));
                pr.setPrCode(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]));
                pr.setPurchRequestDate(rs.getDate(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]));
                pr.setPrStatus(rs.getInt(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]));
                pr.setRemark(rs.getString(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_REMARK]));
                pr.setLocationId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]));
                pr.setWarehouseSupplierId(rs.getLong(PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID])); 
                lists.add(pr);
                
            }
            rs.close();

        }catch(Exception e) {
            System.out.println(e);
        }
        finally{
            DBResultSet.close(dbrs);
        }
        return lists;
    }
      public static Vector getFilterListMaterialFromPRForPO(String whereClause) {
         Vector ListMaterialFromPRForPO = new Vector();
         DBResultSet dbrs = null;
         try{
          String sql =  " SELECT DISTINCT PM.SKU, PM.NAME, PRI.UNIT_ID, PRI.MATERIAL_ID, PRI.UNIT_ID, PRI.SUPPLIER_ID,PRI.PRICE_BUYING,PRI.TOTA_PRICE_BUYING, PU.NAME, PRI.SUPPLIER_NAME, PRI.TERM_PURCHASE_REQUEST, PRI.PURCHASE_REQUEST_ITEM_ID, LC.NAME "+
                        " FROM "+PstPurchaseRequestItem.TBL_PURCHASE_REQUEST_ITEM+" AS PRI "+
                        " INNER JOIN "+PstPurchaseRequest.TBL_PURCHASE_REQUEST+" AS PR "+
                        " ON PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+
                        " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                        " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]+
                        " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS PM "+
                        " ON PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID]+
                        " INNER JOIN "+PstUnit.TBL_P2_UNIT+" AS PU "+
                        " ON PU."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_UNIT_ID]+
                        
                  
                        " WHERE "+whereClause+
                        " ORDER BY PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_ID]+", PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_TERM_PURCHASE_REQUEST];
          System.out.println("getFilterListMaterialFromPRForPO : "+sql);
          dbrs = DBHandler.execQueryResult(sql);
           ResultSet rs = dbrs.getResultSet();

           while (rs.next()) {
                Vector v3 = new Vector();
                SessPurchaseWithPurchaseRequets material = new SessPurchaseWithPurchaseRequets();
                material.setOidMaterial(rs.getLong("PRI.MATERIAL_ID"));
                material.setSku(rs.getString("PM.SKU"));
                material.setName(rs.getString("PM.NAME"));
                material.setStockUnitRequestId(rs.getLong("PRI.UNIT_ID"));
                material.setStockUnitId(rs.getLong("UNIT_ID"));
                material.setSupplierId(rs.getLong("PRI.SUPPLIER_ID"));
                material.setPriceBuying(rs.getDouble("PRI.PRICE_BUYING"));
                material.setTotalPriceBuying(rs.getDouble("PRI.TOTA_PRICE_BUYING"));
                material.setUnitKode(rs.getString("PU.NAME"));
                material.setSupplierName(rs.getString("PRI.SUPPLIER_NAME"));
                material.setTermOf(rs.getInt("TERM_PURCHASE_REQUEST"));
                material.setPurchaseRequestID(rs.getLong("PURCHASE_REQUEST_ITEM_ID"));
                v3.add(material);
                String locationName = rs.getString("LC.NAME");
                v3.add(String.valueOf(locationName));
                ListMaterialFromPRForPO.add(v3);
            }
            rs.close();
         }catch (Exception e) {
            System.out.println("Err : " + e.toString());
         } finally {
            DBResultSet.close(dbrs);
         }
         
         return ListMaterialFromPRForPO;
     }
      public static Vector getListPrToPo(String whereClause) {
         Vector list = new Vector();
         DBResultSet dbrs = null;
         try{
          String sql =  " SELECT "
                  + "PM.SKU, "
                  + "PM.NAME, "
                  + "PRI.UNIT_ID, "
                  + "PRI.MATERIAL_ID, "
                  + "PRI.SUPPLIER_ID, "
                  + "PRI.PRICE_BUYING, "
                  + "PRI.TOTA_PRICE_BUYING, "
                  + "PU.NAME, "
                  + "PRI.QTY, "
                  + "PRI.SUPPLIER_NAME, "
                  + "PRI.TERM_PURCHASE_REQUEST, "
                  + "PRI.PURCHASE_REQUEST_ITEM_ID, "
                  + "LC.NAME "+
                      " FROM "+PstPurchaseRequestItem.TBL_PURCHASE_REQUEST_ITEM+" AS PRI "+
                      " INNER JOIN "+PstPurchaseRequest.TBL_PURCHASE_REQUEST+" AS PR "+
                      " ON PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+
                      " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                      " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]+
                      " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS PM "+
                      " ON PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID]+
                      " INNER JOIN "+PstUnit.TBL_P2_UNIT+" AS PU "+
                      " ON PU."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_UNIT_ID]+
                      " WHERE "+whereClause+
                      " ORDER BY PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_ID]+", PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_TERM_PURCHASE_REQUEST];
          System.out.println("getListPR > PO : "+sql);
          dbrs = DBHandler.execQueryResult(sql);
           ResultSet rs = dbrs.getResultSet();

           while (rs.next()) {
                Vector v3 = new Vector();
                SessPurchaseWithPurchaseRequets material = new SessPurchaseWithPurchaseRequets();
                material.setOidMaterial(rs.getLong("PRI.MATERIAL_ID"));
                material.setSku(rs.getString("PM.SKU"));
                material.setName(rs.getString("PM.NAME"));
                material.setStockUnitRequestId(rs.getLong("PRI.UNIT_ID"));
                material.setStockUnitId(rs.getLong("UNIT_ID"));
                material.setSupplierId(rs.getLong("PRI.SUPPLIER_ID"));
                material.setPriceBuying(rs.getDouble("PRI.PRICE_BUYING"));
                material.setTotalPriceBuying(rs.getDouble("PRI.TOTA_PRICE_BUYING"));
                material.setUnitKode(rs.getString("PU.NAME"));
                material.setSupplierName(rs.getString("PRI.SUPPLIER_NAME"));
                material.setTermOf(rs.getInt("TERM_PURCHASE_REQUEST"));
                material.setPurchaseRequestID(rs.getLong("PURCHASE_REQUEST_ITEM_ID"));
                v3.add(material);
                
                double qty = rs.getDouble("PRI.QTY");
                v3.add(String.valueOf(qty));
                
                String locationName = rs.getString("LC.NAME");
                v3.add(String.valueOf(locationName));
                list.add(v3);
            }
            rs.close();
         }catch (Exception e) {
            System.out.println("Err : " + e.toString());
         } finally {
            DBResultSet.close(dbrs);
         }
         
         return list;
     }
      
      
      /**
       * add opie-eyek 20130423
       * @param request
       * @return 
       */
      public static Vector getPurchaseRequestForTransfer(HttpServletRequest request){
         Vector vectQty = new Vector(1,1);
         Vector vectMaterialList = new Vector(1,1);
         Vector vectMaterialListFilter = new Vector(1,1);
         String pomaterial[] = request.getParameterValues("pomaterial");
         String whereClause="";
         if(pomaterial.length > 0){
             boolean first = true;
             for(int k=0;k < pomaterial.length;k++){
                    long oidMaterial = Long.parseLong(pomaterial[k]);
                    if(FRMQueryString.requestInt(request, "pomaterial_"+oidMaterial)==1){
                         //buatkan kondisi untuk list yang di centang saja
                         if(first){
                             whereClause= " PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"='"+oidMaterial+"'";
                             first=false;
                         }else{
                              whereClause += " OR PR."+ PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"='"+oidMaterial+"'";
                         }
                    }   
                }
               //buatkan list material dan kuantity yang di inginkan 
               if(whereClause.length()>0){
                        vectMaterialList = getListMaterialFromPRForPO(whereClause); 
                        vectMaterialListFilter=getFilterListMaterialFromPRForTransfer(whereClause); 
                        vectQty=getListMaterialFromPRForPOFilter(vectMaterialList,vectMaterialListFilter);
               }
          }
        
        return vectQty;
    }
      
    /**
       * add opie-eyek 20140323
       * @param whereClause
       * @return 
       */
    public static Vector getFilterListMaterialFromPRForTransfer(String whereClause) {
         Vector ListMaterialFromPRForPO = new Vector();
         DBResultSet dbrs = null;
         try{
          String sql =  " SELECT DISTINCT PM.SKU, PM.NAME, PM.AVERAGE_PRICE, PRI.UNIT_ID, PRI.MATERIAL_ID, PRI.UNIT_ID, PRI.SUPPLIER_ID,PRI.PRICE_BUYING,PRI.TOTA_PRICE_BUYING, PU.NAME, PRI.SUPPLIER_NAME "+
                        " FROM "+PstPurchaseRequestItem.TBL_PURCHASE_REQUEST_ITEM+" AS PRI "+
                        " INNER JOIN "+PstPurchaseRequest.TBL_PURCHASE_REQUEST+" AS PR "+
                        " ON PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+
                        " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                        " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=PR."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]+
                        " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS PM "+
                        " ON PM."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_MATERIAL_ID]+
                        " INNER JOIN "+PstUnit.TBL_P2_UNIT+" AS PU "+
                        " ON PU."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]+"=PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_UNIT_ID]+
                        
                  
                        " WHERE "+whereClause+
                        " ORDER BY PRI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_SUPPLIER_ID];
          
          dbrs = DBHandler.execQueryResult(sql);
           ResultSet rs = dbrs.getResultSet();

           while (rs.next()) {
                Vector v3 = new Vector();
                SessPurchaseWithPurchaseRequets material = new SessPurchaseWithPurchaseRequets();
                material.setOidMaterial(rs.getLong("PRI.MATERIAL_ID"));
                material.setSku(rs.getString("PM.SKU"));
                material.setName(rs.getString("PM.NAME"));
                material.setStockUnitRequestId(rs.getLong("PRI.UNIT_ID"));
                material.setStockUnitId(rs.getLong("UNIT_ID"));
                material.setSupplierId(rs.getLong("PRI.SUPPLIER_ID"));
                material.setPriceBuying(rs.getDouble("PM.AVERAGE_PRICE"));
                material.setTotalPriceBuying(rs.getDouble("PRI.TOTA_PRICE_BUYING"));
                material.setUnitKode(rs.getString("PU.NAME"));
                material.setSupplierName(rs.getString("PRI.SUPPLIER_NAME"));
                v3.add(material);
                ListMaterialFromPRForPO.add(v3);
            }
            rs.close();
         }catch (Exception e) {
            System.out.println("Err : " + e.toString());
         } finally {
            DBResultSet.close(dbrs);
         }
         
         return ListMaterialFromPRForPO;
     }

}


