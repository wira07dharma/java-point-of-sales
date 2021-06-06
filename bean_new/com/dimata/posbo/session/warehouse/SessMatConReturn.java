package com.dimata.posbo.session.warehouse;

import com.dimata.posbo.entity.search.SrcMatConReturn;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.util.LogicParser;
import com.dimata.util.Formater;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;
import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessMatConReturn {
    
    public static final String SESS_SRC_MATRETURN = "SESSION_SRC_MATRETURN";
    public static final String SESS_SRC_MARRETURN = "SESSION_SRC_MARRETURN";
    public static final String SESS_SRC_ASSRETURN = "SESSION_SRC_ASSRETURN";
    
    /**
     * this method used to list Material Return
     * @param : object SrcMatConReturn
     * @param : int start
     * @param : int recordToGet
     */
    public static Vector searchMatConReturn(SrcMatConReturn srcmatreturn, int start, int recordToGet) {
        Vector vectRetNumber = LogicParser.textSentence(srcmatreturn.getReturnnumber());
        for (int i = 0; i < vectRetNumber.size(); i++) {
            String name = (String) vectRetNumber.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectRetNumber.remove(i);
        }
        
        Vector vectVendorName = LogicParser.textSentence(srcmatreturn.getVendorname());
        for (int i = 0; i < vectVendorName.size(); i++) {
            String name = (String) vectVendorName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectVendorName.remove(i);
        }
        return getListReturnMaterial(vectRetNumber, vectVendorName, srcmatreturn, start, recordToGet);
    }
    
    public static Vector searchMatConReturnToWh(SrcMatConReturn srcmatreturn, int start, int recordToGet) {
        Vector vectRetNumber = LogicParser.textSentence(srcmatreturn.getReturnnumber());
        for (int i = 0; i < vectRetNumber.size(); i++) {
            String name = (String) vectRetNumber.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectRetNumber.remove(i);
        }
        
        return getListReturnMaterialToWh(vectRetNumber, srcmatreturn, start, recordToGet);
    }
    
    /**
     * this method used by searchMatConReturn method to list material Return
     */
    public static Vector getListReturnMaterial(Vector vectRetNumber, Vector vectVendorName, SrcMatConReturn srcmatreturn, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] +
                         " , CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_REMARK] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] +
                         " , RCV." + PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " , RCV." + PstMatConReceive.fieldNames[PstMatConReceive.FLD_REC_CODE] +
                         " , RCV." + PstMatConReceive.fieldNames[PstMatConReceive.FLD_INVOICE_SUPPLIER] +
                         " FROM " + PstMatConReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                         " ON RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_SUPPLIER_ID] +
                         " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                         " LEFT JOIN " + PstMatConReceive.TBL_MAT_RECEIVE + " RCV " +
                         " ON RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RECEIVE_MATERIAL_ID] +
                         " = RCV. " + PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID];
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
                    }
                }
                strRetNumber = "(" + strRetNumber + ")";
            }
            
            String strVendorName = "";
            if (vectVendorName != null && vectVendorName.size() > 0) {
                for (int a = 0; a < vectVendorName.size(); a++) {
                    if (strVendorName.length() > 0) {
                        strVendorName = strVendorName + " OR (CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + vectVendorName.get(a) + "%')";
                    } else {
                        strVendorName = "(CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + vectVendorName.get(a) + "%')";
                    }
                }
                strVendorName = "(" + strVendorName + ")";
            }
            
            String strStatus = "";
            if (srcmatreturn.getReturnstatus() != null && srcmatreturn.getReturnstatus().size() > 0) {
                for (int b = 0; b < srcmatreturn.getReturnstatus().size(); b++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd 00:00:00");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd 23:59:59");
                strDate = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            //System.out.println(".::LOCATION_TYPE = " + srcmatreturn.getLocationType());
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationFrom = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
            /*String strWithInvoice = "";
            if (srcmatreturn.getTypeInvoice()) {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                        " <> \"\"";
            } else {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                        " = \"\"";
            }*/
            
            String whereClause = "";
            if (strRetNumber.length() > 0) {
                whereClause = strRetNumber;
            }
            
            if (strVendorName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strVendorName;
                } else {
                    whereClause = whereClause + " AND " + strVendorName;
                }
            }
            
            if (strDate.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strDate;
                } else {
                    whereClause = whereClause + " AND " + strDate;
                }
            }
            
            if (strStatus.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strStatus;
                } else {
                    whereClause = whereClause + " AND " + strStatus;
                }
            }
            
            if (strLocationType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationType;
                } else {
                    whereClause = whereClause + " AND " + strLocationType;
                }
            }
            
            if (strLocationFrom.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationFrom;
                } else {
                    whereClause = whereClause + " AND " + strLocationFrom;
                }
            }
            
            /*if (strWithInvoice.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strWithInvoice;
                } else {
                    whereClause = whereClause + " AND " + strWithInvoice;
                }
            }*/
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            switch (srcmatreturn.getReturnsortby()) {
                case 0:
                    sql = sql + " ORDER BY RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIMIT " + start + "," + recordToGet;
                    break;
                case 1:
                    sql = sql + " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIMIT " + start + "," + recordToGet;
                    break;
                case 2:
                    sql = sql + " ORDER BY RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " LIMIT " + start + "," + recordToGet;
                    break;
                case 3:
                    sql = sql + " ORDER BY RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " LIMIT " + start + "," + recordToGet;
                    break;
            }
            
            System.out.println("#SessMatConReturn.getListReturnMaterial(): \n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                MatConReturn matreturn = new MatConReturn();
                ContactList contactList = new ContactList();
                MatConReceive matReceive = new MatConReceive();
                
                matreturn.setOID(rs.getLong(1));
                matreturn.setRetCode(rs.getString(2));
                matreturn.setReturnDate(rs.getDate(3)); 
                matreturn.setReturnStatus(rs.getInt(5));
                matreturn.setRemark(rs.getString(6));
                matreturn.setLocationId(rs.getLong(7));
                vect.add(matreturn);
                
                contactList.setCompName(rs.getString(4));
                vect.add(contactList);
                
                matReceive.setOID(rs.getLong("RCV." + PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID]));
                matReceive.setRecCode(rs.getString("RCV." + PstMatConReceive.fieldNames[PstMatConReceive.FLD_REC_CODE]));
                matReceive.setInvoiceSupplier(rs.getString("RCV." + PstMatConReceive.fieldNames[PstMatConReceive.FLD_INVOICE_SUPPLIER]));
                vect.add(matReceive);
                
                result.add(vect);
            }
        } catch (Exception e) {
            System.out.println("SessMatConReturn.getListReturnMaterial " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector getListReturnMaterialToWh(Vector vectRetNumber, SrcMatConReturn srcmatreturn, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] +
                         " , LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_REMARK] +
                         " , RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] +
                         " FROM " + PstMatConReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                         " ON RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_TO] +
                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
                    }
                }
                strRetNumber = "(" + strRetNumber + ")";
            }
            
            String strStatus = "";
            if (srcmatreturn.getReturnstatus() != null && srcmatreturn.getReturnstatus().size() > 0) {
                for (int b = 0; b < srcmatreturn.getReturnstatus().size(); b++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd 00:00:00");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd 23:59:59");
                strDate = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationType = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
            String strWithInvoice = "";
            if (srcmatreturn.getTypeInvoice()) {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                " <> \"\"";
            } else {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                " = \"\"";
            }
            
            String whereClause = "";
            if (strRetNumber.length() > 0) {
                whereClause = strRetNumber;
            }
            
            if (strDate.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strDate;
                } else {
                    whereClause = whereClause + " AND " + strDate;
                }
            }
            
            if (strStatus.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strStatus;
                } else {
                    whereClause = whereClause + " AND " + strStatus;
                }
            }
            
            if (strLocationType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationType;
                } else {
                    whereClause = whereClause + " AND " + strLocationType;
                }
            }
            
            if (strLocationFrom.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationFrom;
                } else {
                    whereClause = whereClause + " AND " + strLocationFrom;
                }
            }
            
            if (strWithInvoice.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strWithInvoice;
                } else {
                    whereClause = whereClause + " AND " + strWithInvoice;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            switch (srcmatreturn.getReturnsortby()) {
                case 0:
                    sql = sql + " ORDER BY RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIMIT " + start + "," + recordToGet;
                    break;
                case 1:
                    sql = sql + " ORDER BY RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " LIMIT " + start + "," + recordToGet;
                    break;
                case 2:
                    sql = sql + " ORDER BY RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " LIMIT " + start + "," + recordToGet;
                    break;
            }
            System.out.println("getListReturnMaterialToWh >>>\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                MatConReturn matreturn = new MatConReturn();
                Location loc = new Location();
                
                matreturn.setOID(rs.getLong(1));
                matreturn.setRetCode(rs.getString(2));
                matreturn.setReturnDate(rs.getDate(3));
                matreturn.setReturnStatus(rs.getInt(5));
                matreturn.setRemark(rs.getString(6));
                matreturn.setLocationId(rs.getLong(7));
                vect.add(matreturn);
                
                loc.setName(rs.getString(4));
                vect.add(loc);
                
                result.add(vect);
            }
        } catch (Exception e) {
            System.out.println("SessMatConReturn.getListReturnMaterial " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /**
     *  this method used to count record of Material Receive List
     */
    public static int getCountSearch(SrcMatConReturn srcmatreturn) {
        Vector vectRetNumber = LogicParser.textSentence(srcmatreturn.getReturnnumber());
        for (int i = 0; i < vectRetNumber.size(); i++) {
            String name = (String) vectRetNumber.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectRetNumber.remove(i);
        }
        
        Vector vectVendorName = LogicParser.textSentence(srcmatreturn.getVendorname());
        for (int i = 0; i < vectVendorName.size(); i++) {
            String name = (String) vectVendorName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectVendorName.remove(i);
        }
        return getCountListReturnMaterial(vectRetNumber, vectVendorName, srcmatreturn);
    }
    
    public static int getCountSearchToWh(SrcMatConReturn srcmatreturn) {
        Vector vectRetNumber = LogicParser.textSentence(srcmatreturn.getReturnnumber());
        for (int i = 0; i < vectRetNumber.size(); i++) {
            String name = (String) vectRetNumber.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectRetNumber.remove(i);
        }
        
        return getCountListReturnMaterialToWh(vectRetNumber, srcmatreturn);
    }
    
    /**
     * this method used by getCountListReceiveMaterial method to count list material Receive
     */
    public static int getCountListReturnMaterial(Vector vectRetNumber, Vector vectVendorName, SrcMatConReturn srcmatreturn) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
                         ") AS CNT FROM " + PstMatConReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                         " ON RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_SUPPLIER_ID] +
                         " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
                    }
                }
                strRetNumber = "(" + strRetNumber + ")";
            }
            
            String strVendorName = "";
            if (vectVendorName != null && vectVendorName.size() > 0) {
                for (int a = 0; a < vectVendorName.size(); a++) {
                    if (strVendorName.length() > 0) {
                        strVendorName = strVendorName + " OR (CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + vectVendorName.get(a) + "%')";
                    } else {
                        strVendorName = "(CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + vectVendorName.get(a) + "%')";
                    }
                }
                strVendorName = "(" + strVendorName + ")";
            }
            
            String strStatus = "";
            if (srcmatreturn.getReturnstatus() != null && srcmatreturn.getReturnstatus().size() > 0) {
                for (int b = 0; b < srcmatreturn.getReturnstatus().size(); b++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd");
                strDate = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationType = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
            String strWithInvoice = "";
            if (srcmatreturn.getTypeInvoice()) {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                " <> \"\"";
            } else {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                " = \"\"";
            }
            
            String whereClause = "";
            if (strRetNumber.length() > 0) {
                whereClause = strRetNumber;
            }
            
            if (strVendorName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strVendorName;
                } else {
                    whereClause = whereClause + " AND " + strVendorName;
                }
            }
            
            if (strDate.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strDate;
                } else {
                    whereClause = whereClause + " AND " + strDate;
                }
            }
            
            if (strStatus.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strStatus;
                } else {
                    whereClause = whereClause + " AND " + strStatus;
                }
            }
            
            if (strLocationType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationType;
                } else {
                    whereClause = whereClause + " AND " + strLocationType;
                }
            }
            
            if (strLocationFrom.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationFrom;
                } else {
                    whereClause = whereClause + " AND " + strLocationFrom;
                }
            }
            
            if (strWithInvoice.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strWithInvoice;
                } else {
                    whereClause = whereClause + " AND " + strWithInvoice;
                }
            }
            
            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt("CNT");
            }
        } catch (Exception e) {
            System.out.println("SessMatConReturn.getCountListReturnMaterial() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    public static int getCountListReturnMaterialToWh(Vector vectRetNumber, SrcMatConReturn srcmatreturn) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
                         ") AS CNT FROM " + PstMatConReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                         " ON RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_TO] +
                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
                    }
                }
                strRetNumber = "(" + strRetNumber + ")";
            }
            
            String strStatus = "";
            if (srcmatreturn.getReturnstatus() != null && srcmatreturn.getReturnstatus().size() > 0) {
                for (int b = 0; b < srcmatreturn.getReturnstatus().size(); b++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd");
                strDate = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationType = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
            String strWithInvoice = "";
            if (srcmatreturn.getTypeInvoice()) {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                " <> \"\"";
            } else {
                strWithInvoice = " RET." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] +
                " = \"\"";
            }
            
            String whereClause = "";
            if (strRetNumber.length() > 0) {
                whereClause = strRetNumber;
            }
            
            if (strDate.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strDate;
                } else {
                    whereClause = whereClause + " AND " + strDate;
                }
            }
            
            if (strStatus.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strStatus;
                } else {
                    whereClause = whereClause + " AND " + strStatus;
                }
            }
            
            if (strLocationType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationType;
                } else {
                    whereClause = whereClause + " AND " + strLocationType;
                }
            }
            
            if (strLocationFrom.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strLocationFrom;
                } else {
                    whereClause = whereClause + " AND " + strLocationFrom;
                }
            }
            
            if (strWithInvoice.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strWithInvoice;
                } else {
                    whereClause = whereClause + " AND " + strWithInvoice;
                }
            }
            
            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt("CNT");
            }
        } catch (Exception e) {
            System.out.println("SessMatConReturn.getCountListReturnMaterial() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    
    public static String getCodeReturn(MatConReturn matReturn) {
        String code = "RET";
        String dateCode = "";
        if (matReturn.getReturnDate() != null) {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+matReturn.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            int nextCounter = matReturn.getRetCodeCnt();//getMaxCounter(date);
            Date date = matReturn.getReturnDate();
            
            int tgl = date.getDate();
            int bln = date.getMonth() + 1;
            int thn = date.getYear() + 1900;
            
            dateCode = (String.valueOf(thn)).substring(2, 4);
            
            if (bln < 10) {
                dateCode = dateCode + "0" + bln;
            } else {
                dateCode = dateCode + bln;
            }
            
            if (tgl < 10) {
                dateCode = dateCode + "0" + tgl;
            } else {
                dateCode = dateCode + tgl;
            }
            
            String counter = "";
            if (nextCounter < 10) {
                counter = "00" + nextCounter;
            } else {
                if (nextCounter < 100) {
                    counter = "0" + nextCounter;
                } else {
                    counter = "" + nextCounter;
                }
            }
            code = location.getCode() + "-" + dateCode + "-" + code + "-" + counter;
        }
        return code;
    }
    
    /* this method used to getNextIndex of maximum number of Return number
     * return : int new number
     **/
    public static int getIntCode(MatConReturn matReturn, Date pDate, long oid, int counter) {
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
        try {
            String sql = "SELECT MAX(" + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE_CNT] + ") AS PMAX" +
                         " FROM " + PstMatConReturn.TBL_MAT_RETURN +
                         " WHERE YEAR(" + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + ") = " + (matReturn.getReturnDate().getYear() + 1900) +
                         " AND MONTH(" + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + ") = " + (matReturn.getReturnDate().getMonth() + 1) +
                         " AND " + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] + " <> " + oid +
                         " AND " + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] +
                         " = " + matReturn.getLocationId();
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                max = rs.getInt("PMAX");
            }
            
            if (oid == 0) {
                max = max + 1;
            } else {
                if (matReturn.getReturnDate() != pDate)
                    max = max + 1;
                else
                    max = counter;
            }
            
        } catch (Exception e) {
            System.out.println("SessMatConReturn.getIntCode() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return max;
    }
    
    /**GADNYANA
     * untuk mencari item qty return yang tampa invoice
     * @return
     */
    public static Vector getReturnItemOutInvoice(long oidLocation, long oidSupplier) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                         " SUM(RTI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_QTY] + ") AS " + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_QTY] +
                         " ,M." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_MATERIAL_ID] +
                         " FROM " + PstMatConReturnItem.TBL_MAT_RETURN_ITEM + " AS RTI " +
                         " INNER JOIN " + PstMatConReturn.TBL_MAT_RETURN + " AS RM " +
                         " ON RTI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " = RM." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                         " ON RTI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_MATERIAL_ID] +
                         " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                         " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " WHERE RM." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] + "=" + oidLocation;
            
            if (oidSupplier != 0) {
                sql = sql + " AND RM." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_SUPPLIER_ID] + "=" + oidSupplier;
            }
            
            sql = sql + " AND RM." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER] + " = \"\"" +
                        " AND RM." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " GROUP BY M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            
            //System.out.println("= >> SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                MatConReturnItem matReturnItem = new MatConReturnItem();
                
                matReturnItem.setQty(rs.getDouble(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_QTY]));
                matReturnItem.setMaterialId(rs.getLong(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_MATERIAL_ID]));
                
                list.add(matReturnItem);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("==>> ERR " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }
    
    
    /**
     * ini di gunakan untuk pencarian data retur ke supplier
     * yang di pakai nanti pada laporan stock card
     * @param srcStockCard
     * @return
     */
    public static void getDataMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                         " R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] +
                         " ,R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE] +
                         " ,SUM(RI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_QTY] + ") AS SUM_QTY " +
                         " ,RI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_UNIT_ID] +
                         " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " FROM " + PstMatConReturn.TBL_MAT_RETURN + " AS R " +
                         " INNER JOIN " + PstMatConReturnItem.TBL_MAT_RETURN_ITEM + " AS RI " +
                         " ON R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
                         " = RI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                         " ON RI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_MATERIAL_ID] +
                         " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }
            
            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = " R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }
            
            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause = " R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                }
            }else{
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause = " R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + " GROUP BY R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE] +
                        " ,R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE];
            sql = sql + " ORDER BY R." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE];
            
            // System.out.println("sql return : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = DBHandler.convertDate(rs.getDate(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE]),rs.getTime(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE]));
                stockCardReport.setDate(date);
                
                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_ROMR);
                stockCardReport.setDocCode(rs.getString(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE]));
                double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setKeterangan("Pengembalian barang");
                
                PstStockCardReport.insertExc(stockCardReport);
            }
        } catch (Exception e) {
            System.out.println("getDataMaterial > err : "+e.toString());
        }
    }
    
    /**
     * Fungsi ini digunakan untuk mendapatkan return berdasarkan dokumen receive
     * @param oidRcv OID dari dokumen receive
     * @return result Nilai total dari sebuah return
     */
    public static double getTotalReturnByReceive(long oidReceive) {
        DBResultSet dbrs = null;
        double result = 0;
        String sql = "";
        try {
            sql  = " select sum(rtni."+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_TOTAL]+")";
            sql += " from "+PstMatConReceive.TBL_MAT_RECEIVE+" rcv inner join "+PstMatConReturn.TBL_MAT_RETURN+" rtn";
            sql += " on rcv."+PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID]+" = ";
            sql += " rtn."+PstMatConReturn.fieldNames[PstMatConReturn.FLD_RECEIVE_MATERIAL_ID];
            sql += " inner join "+PstMatConReturnItem.TBL_MAT_RETURN_ITEM+" rtni";
            sql += " on rtn."+PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID]+" = ";
            sql += " rtni."+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID];
            sql += " where rtn."+PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_CLOSED;
            if(oidReceive != 0) {
                sql += " and rtn."+PstMatConReturn.fieldNames[PstMatConReturn.FLD_RECEIVE_MATERIAL_ID]+" = "+oidReceive;;
            }
            
            //System.out.println("sql getTotalReturnByReceive(#): \n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                result = rs.getDouble(1);
            }
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return result;
    }
}
