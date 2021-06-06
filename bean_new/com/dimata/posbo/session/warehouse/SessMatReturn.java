package com.dimata.posbo.session.warehouse;

import com.dimata.posbo.entity.search.SrcMatReturn;
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
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;
import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;
import com.dimata.posbo.entity.search.SrcAccPayable;
import com.dimata.posbo.session.arap.SessAccPayable;

//adding import for reposting stok
import com.dimata.posbo.entity.search.*;

public class SessMatReturn {
    
    public static final String SESS_SRC_MATRETURN = "SESSION_SRC_MATRETURN";
    public static final String SESS_SRC_MARRETURN = "SESSION_SRC_MARRETURN";
    public static final String SESS_SRC_ASSRETURN = "SESSION_SRC_ASSRETURN";
    
	public static int getStrDutyFree(){
		String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
		System.out.println("#Duty Free: " + strDutyFree);
		int dutyFree = Integer.parseInt(strDutyFree);
		return dutyFree;
	}
	
    /**
     * this method used to list Material Return
     * @param : object SrcMatReturn
     * @param : int start
     * @param : int recordToGet
     */
    public static Vector searchMatReturn(SrcMatReturn srcmatreturn, int start, int recordToGet, String whereLocation) {
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
        return getListReturnMaterial(vectRetNumber, vectVendorName, srcmatreturn, start, recordToGet,whereLocation);
    }
    
    public static Vector searchMatReturnToWh(SrcMatReturn srcmatreturn, int start, int recordToGet) {
        Vector vectRetNumber = LogicParser.textSentence(srcmatreturn.getReturnnumber());
        for (int i = 0; i < vectRetNumber.size(); i++) {
            String name = (String) vectRetNumber.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectRetNumber.remove(i);
        }
        
        return getListReturnMaterialToWh(vectRetNumber, srcmatreturn, start, recordToGet);
    }
    
    /**
     * this method used by searchMatReturn method to list material Return
     */
    public static Vector getListReturnMaterial(Vector vectRetNumber, Vector vectVendorName, SrcMatReturn srcmatreturn, int start, int recordToGet, String whereLocation) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
		int dutyFree = getStrDutyFree();
        try {
            String sql = "SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                         " , CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_REMARK] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                         " , RCV." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " , RCV." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
                         " , RCV." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER];
			
			if(dutyFree == 1){
				sql = sql + ", LC." + PstLocation.fieldNames[PstLocation.FLD_NAME];
			}
			
			String sql2 = " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
                         " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                         " LEFT JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " RCV " +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID] +
                         " = RCV. " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+
                         " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                         " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID]; 
			
			sql = sql + sql2;
                         
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
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
                        strStatus = strStatus + " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd 00:00:00");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd 23:59:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            //System.out.println(".::LOCATION_TYPE = " + srcmatreturn.getLocationType());
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationFrom = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
              String strPurchaseOrderId = "";
	    if (srcmatreturn.getPurchaseOrderId() == -1) {//
		strPurchaseOrderId = " RET." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
			" = 0";
	    }
            
            if (srcmatreturn.getPurchaseOrderId() == -2) {
		strPurchaseOrderId = " RET." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
			" !=0";
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
            
            if (strPurchaseOrderId.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strPurchaseOrderId;
		} else {
		    whereClause = whereClause + " AND " + strPurchaseOrderId;
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
            
            switch (srcmatreturn.getReturnsortby()) {
                case 0:
                    sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];
                    break;
                case 1:
                    sql = sql + " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                    break;
                case 2:
                    sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]+" DESC ";
                    break;
                case 3:
                    sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS];
                    break;
            }
            
            sql += ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]; /** defaultnya, list diurut berdasarkan DATE */
            sql +=  " LIMIT " + start + "," + recordToGet;
            
            System.out.println("#SessMatReturn.getListReturnMaterial(): \n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                MatReturn matreturn = new MatReturn();
                ContactList contactList = new ContactList();
                MatReceive matReceive = new MatReceive();
                
                matreturn.setOID(rs.getLong(1));
                matreturn.setRetCode(rs.getString(2));
                matreturn.setReturnDate(rs.getDate(3));
                matreturn.setReturnStatus(rs.getInt(5));
                matreturn.setRemark(rs.getString(6));
                matreturn.setLocationId(rs.getLong(7));
                vect.add(matreturn);
                
                contactList.setCompName(rs.getString(4));
                vect.add(contactList);
                
                matReceive.setOID(rs.getLong("RCV." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]));
                matReceive.setRecCode(rs.getString("RCV." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
                matReceive.setInvoiceSupplier(rs.getString("RCV." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]));
                vect.add(matReceive);
                
				if(dutyFree == 1){
					Location location = new Location();
					location.setName(rs.getString("LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] ));
					vect.add(location);
				}
				
                result.add(vect);
            }
        } catch (Exception e) {
            System.out.println("SessMatReturn.getListReturnMaterial " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector getListReturnMaterialToWh(Vector vectRetNumber, SrcMatReturn srcmatreturn, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                         " , LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_REMARK] +
                         " , RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '%" + vectRetNumber.get(i) + "%')";
                    }
                }
                strRetNumber = "(" + strRetNumber + ")";
            }
            
            String strStatus = "";
            if (srcmatreturn.getReturnstatus() != null && srcmatreturn.getReturnstatus().size() > 0) {
                for (int b = 0; b < srcmatreturn.getReturnstatus().size(); b++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd 00:00:00");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd 23:59:59");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationType = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
            String strWithInvoice = "";
            if (srcmatreturn.getTypeInvoice()) {
                strWithInvoice = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER] +
                " <> \"\"";
            } else {
                strWithInvoice = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER] +
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
                    sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];
                    break;
                case 1:
                    sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE];
                    break;
                case 2:
                    sql = sql + " ORDER BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS];
                    break;
            }
            
            sql += ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE];
            sql += " LIMIT " + start + "," + recordToGet;
            
            System.out.println("getListReturnMaterialToWh >>>\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                MatReturn matreturn = new MatReturn();
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
            System.out.println("SessMatReturn.getListReturnMaterial " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /**
     *  this method used to count record of Material Receive List
     */
    public static int getCountSearch(SrcMatReturn srcmatreturn, String whereLocation) {
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
        return getCountListReturnMaterial(vectRetNumber, vectVendorName, srcmatreturn,whereLocation);
    }
    
    public static int getCountSearchToWh(SrcMatReturn srcmatreturn) {
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
    public static int getCountListReturnMaterial(Vector vectRetNumber, Vector vectVendorName, SrcMatReturn srcmatreturn, String whereLocation) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         ") AS CNT FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
                         " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+
                         " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                         " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID];  
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
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
                        strStatus = strStatus + " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationType = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
            String strPurchaseOrderId = "";
	    if (srcmatreturn.getPurchaseOrderId() == -1) {//
		strPurchaseOrderId = " RET." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
			" = 0";
	    }
            
            if (srcmatreturn.getPurchaseOrderId() == -2) {
		strPurchaseOrderId = " RET." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
			" !=0";
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
            
            if (strPurchaseOrderId.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strPurchaseOrderId;
		} else {
		    whereClause = whereClause + " AND " + strPurchaseOrderId;
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
            
            
            if(!"".equals(whereLocation)){
               if (whereClause.length() > 0) {
                   whereClause= whereClause + " AND " + whereLocation;
               }else{
                   whereClause= whereClause + whereLocation;
               }
               
            }
            
            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }
            System.out.println("sql getCountListReturnMaterial: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt("CNT");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("SessMatReturn.getCountListReturnMaterial() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static Vector getListReturnMaterialInformation(String where) {
        DBResultSet dbrs = null;
	Vector vListReturn = new Vector(1, 1);
        try {
            String sql = "SELECT COUNT(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +") AS CNT "+
                         " , " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + ") STATUS "+
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
                         " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
                        " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                        " = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

          /**
           * SELECT COUNT(RET.RETURN_MATERIAL_ID) AS CNT FROM pos_return_material RET INNER JOIN contact_list CNT ON RET.SUPPLIER_ID = CNT.CONTACT_ID
           * WHERE ((RET.RETURN_STATUS =0) OR (RET.RETURN_STATUS =2)) AND  RET.LOCATION_TYPE = 0;
           */
          String strStatus = "";
          strStatus =   "((RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS]+ " = 0)"+
                        " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = 10)"+
                        " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = 1))"+
                        
                        " AND  RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +" = 0"+
                        " AND  RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_PURCHASE_ORDER_ID] +"!= 0";
          
          if(where.length()>0){
               strStatus=strStatus + where;
            }
          strStatus=strStatus+" GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS];


            sql = sql + " WHERE " + strStatus;
            //System.out.println("sql getListReturnMaterialInformation: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vlist = new Vector(1, 1);
                vlist.add(rs.getInt("CNT"));
                 vlist.add(rs.getInt("STATUS"));
                vListReturn.add(vlist);
            }
            rs.close();
            return vListReturn;
        } catch (Exception e) {
            System.out.println("SessMatReturn.getListReturnMaterialInformation() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
       return new Vector(1, 1);
    }

    
    public static Vector getListReturnMaterialWithoutPOInformation(String where) {
         DBResultSet dbrs = null;
	Vector vListReturn = new Vector(1, 1);
        try {
            String sql = "SELECT COUNT(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +") AS CNT "+
                         " , " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + ") STATUS "+
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] +
                         " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
                        " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                        " = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

          /**
           * SELECT COUNT(RET.RETURN_MATERIAL_ID) AS CNT FROM pos_return_material RET INNER JOIN contact_list CNT ON RET.SUPPLIER_ID = CNT.CONTACT_ID
           * WHERE ((RET.RETURN_STATUS =0) OR (RET.RETURN_STATUS =2)) AND  RET.LOCATION_TYPE = 0;
           */
          String strStatus = "";
          strStatus =   "((RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS]+ " = 0)"+
                        " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = 10)"+
                        " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = 1))"+
                        
                        " AND  RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +" = 0"+
                        " AND  RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_PURCHASE_ORDER_ID] +"= 0";
          
          if(where.length()>0){
               strStatus=strStatus + where;
            }
          strStatus=strStatus+" GROUP BY RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS];


            sql = sql + " WHERE " + strStatus;
            //System.out.println("sql getListReturnMaterialInformation: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vlist = new Vector(1, 1);
                vlist.add(rs.getInt("CNT"));
                 vlist.add(rs.getInt("STATUS"));
                vListReturn.add(vlist);
            }
            rs.close();
            return vListReturn;
        } catch (Exception e) {
            System.out.println("SessMatReturn.getListReturnMaterialInformation() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
       return new Vector(1, 1);
    }
    
    public static int getCountListReturnMaterialToWh(Vector vectRetNumber, SrcMatReturn srcmatreturn) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         ") AS CNT FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO] +
                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
            String strRetNumber = "";
            if (vectRetNumber != null && vectRetNumber.size() > 0) {
                for (int i = 0; i < vectRetNumber.size(); i++) {
                    if (strRetNumber.length() > 0) {
                        strRetNumber = " OR (RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
                    } else {
                        strRetNumber = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] + " LIKE '" + vectRetNumber.get(i) + "%')";
                    }
                }
                strRetNumber = "(" + strRetNumber + ")";
            }
            
            String strStatus = "";
            if (srcmatreturn.getReturnstatus() != null && srcmatreturn.getReturnstatus().size() > 0) {
                for (int b = 0; b < srcmatreturn.getReturnstatus().size(); b++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    } else {
                        strStatus = "(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " =" + srcmatreturn.getReturnstatus().get(b) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            String strDate = "";
            if (srcmatreturn.getReturndatestatus() != 0) {
                String startDate = Formater.formatDate(srcmatreturn.getReturnfromdate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcmatreturn.getReturntodate(), "yyyy-MM-dd");
                strDate = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            String strLocationType = "";
            if (srcmatreturn.getLocationType() != -1) {
                strLocationType = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE] +
                " = " + srcmatreturn.getLocationType();
            }
            
            String strLocationFrom = "";
            if (srcmatreturn.getLocationFrom() != 0) {
                strLocationType = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                " = " + srcmatreturn.getLocationFrom();
            }
            
            String strWithInvoice = "";
            if (srcmatreturn.getTypeInvoice()) {
                strWithInvoice = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER] +
                " <> \"\"";
            } else {
                strWithInvoice = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER] +
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
            System.out.println("SessMatReturn.getCountListReturnMaterial() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    
    public static String getCodeReturn(MatReturn matReturn) {
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
    public static int getIntCode(MatReturn matReturn, Date pDate, long oid, int counter) {
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
        try {
            String sql = "SELECT MAX(" + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE_CNT] + ") AS PMAX" +
                         " FROM " + PstMatReturn.TBL_MAT_RETURN +
                         " WHERE YEAR(" + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + ") = " + (matReturn.getReturnDate().getYear() + 1900) +
                         " AND MONTH(" + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + ") = " + (matReturn.getReturnDate().getMonth() + 1) +
                         " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] + " <> " + oid +
                         " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
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
            System.out.println("SessMatReturn.getIntCode() err : " + e.toString());
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
                         " SUM(RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS " + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
                         " ,M." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                         " FROM " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS RTI " +
                         " INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " AS RM " +
                         " ON RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " = RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                         " ON RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                         " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                         " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U " +
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " WHERE RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + oidLocation;
            
            if (oidSupplier != 0) {
                sql = sql + " AND RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID] + "=" + oidSupplier;
            }
            
            sql = sql + " AND RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER] + " = \"\"" +
                        " AND RM." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " GROUP BY M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            
            //System.out.println("= >> SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                MatReturnItem matReturnItem = new MatReturnItem();
                
                matReturnItem.setQty(rs.getDouble(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]));
                matReturnItem.setMaterialId(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID]));
                
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
                         " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                         " ,R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                         " ,R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                         " ,SUM(RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SUM_QTY " +
                         " ,RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
                         " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
						" ,SUM(RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_BERAT] + ") AS SUM_BERAT " +
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " AS R " +
                         " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS RI " +
                         " ON R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " = RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                         " ON RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                         " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }
            
            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND ";
                }
                if (srcStockCard.getWarehouseLocationId() != 0) {
                    whereClause += " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " IN (" + srcStockCard.getLocationId() + "," + srcStockCard.getWarehouseLocationId() + ")";
                } else {
                    whereClause += " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + " = " + srcStockCard.getLocationId();
                }
            }
            
            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause += " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause += " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                }
            }else{
                ////update opie-eyek 20161021
                if (whereClause.length() > 0) {
                    whereClause += " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause += " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }
            
            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }
            
            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }
            
            sql = sql + " GROUP BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                        " ,R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];
            sql = sql + " ORDER BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];
            
            // System.out.println("sql return : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = DBHandler.convertDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]),rs.getTime(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
                stockCardReport.setDate(date);
                
                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_ROMR);
                stockCardReport.setDocCode(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE]));
                double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setBerat(rs.getDouble("SUM_BERAT"));
                stockCardReport.setLocationId(rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID]));
                
                if(srcStockCard.getLanguage()==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
                    switch (stockCardReport.getDocType()) {
                        case I_DocType.MAT_DOC_TYPE_ROMR:
                            stockCardReport.setKeterangan("Pengembalian ke supplier");
                            break;
                        default:
                            stockCardReport.setKeterangan("Pengembalian");
                    }
                }else{
                    switch (stockCardReport.getDocType()) {
                        case I_DocType.MAT_DOC_TYPE_ROMR:
                            stockCardReport.setKeterangan("Return to supplier");
                            break;
                        default:
                            stockCardReport.setKeterangan("Return");
                    }
                }
                
                
                
                PstStockCardReport.insertExc(stockCardReport);
            }
        } catch (Exception e) {
            System.out.println("getDataMaterial > err : "+e.toString());
        }
    }

     /**
     * ini di gunakan untuk pencarian data retur ke supplier
     * yang di pakai nanti pada reposting stok berdasarkan kartu stok
     * @param srcStockCard
     * by Mirahu 20120803
     * @return
     */
    public static void getQtyStockMaterialReposting(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                         " SUM(RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SUM_QTY " +
                         " ,RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
                         " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " ,SUM(RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_BERAT] + ") AS SUM_BERAT " +
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " AS R " +
                         " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS RI " +
                         " ON R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " = RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                         " ON RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                         " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcMaterialRepostingStock.getMaterialId() != 0) {
                whereClause = " RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
            }

            if (srcMaterialRepostingStock.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                }
            }

            if (srcMaterialRepostingStock.getDateFrom() != null && srcMaterialRepostingStock.getDateTo() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }else{
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
                for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                        " ,R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                        " , RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];

             System.out.println("sql return : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double qtyMaterial = 0;
            double qtyAll = 0;
            //added by dewok 2018
            double beratAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();

                //stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_ROMR);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
               //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);

                //PstStockCardReport.insertExc(stockCardReport);
                qtyAll += qtyMaterial;
                beratAll += rs.getDouble("SUM_BERAT");
                srcMaterialRepostingStock.setQty(qtyAll);
                srcMaterialRepostingStock.setBerat(beratAll);
            }
             if (qtyAll== 0){
                srcMaterialRepostingStock.setQty(0);
            }
             if (beratAll== 0){
                srcMaterialRepostingStock.setBerat(0);
            }

        } catch (Exception e) {
            System.out.println("getDataMaterial > err : "+e.toString());
        }
    }

    
     /**
     * ini di gunakan untuk pencarian data retur ke supplier
     * di pakai untuk perhitungan stock pada saat opname.
     * by Mirahu
     * 20110803
     * @return
     */
    public static void getDataMaterialTime(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                         " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                         " ,R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                         " ,SUM(RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SUM_QTY " +
                         " ,RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
                         " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " AS R " +
                         " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS RI " +
                         " ON R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " = RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                         " ON RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                         " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            }else{
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            //sql = sql + " GROUP BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                       // " ,R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];
             sql = sql + " GROUP BY RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];

            // System.out.println("sql return : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = DBHandler.convertDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]),rs.getTime(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
                stockCardReport.setDate(date);

                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_ROMR);
                stockCardReport.setDocCode(rs.getString(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE]));
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setKeterangan("Pengembalian barang");

                PstStockCardReport.insertExc(stockCardReport);
            }
        } catch (Exception e) {
            System.out.println("getDataMaterial > err : "+e.toString());
        }
    }

    /**
     * ini di gunakan untuk  mencari qy stok retur
     * di pakai untuk perhitungan stock pada saat opname.
     * by Mirahu
     * 20110809
     * @return
     */
    public static void getQtyStockMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                         " SUM(RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SUM_QTY " +
                         " ,RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
                         " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " AS R " +
                         " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS RI " +
                         " ON R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " = RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                         " ON RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                         " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = " RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }else{
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                        " ,R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                        " , RI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE];

            // System.out.println("sql return : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double qtyMaterial = 0;
            double qtyAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();

                //stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_ROMR);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
               //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);

                //PstStockCardReport.insertExc(stockCardReport);
                qtyAll += qtyMaterial;
                srcStockCard.setQty(qtyAll);
            }
             if (qtyAll== 0){
                srcStockCard.setQty(0);
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
            sql  = " select sum(rtni."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL]+")";
            sql += " from "+PstMatReceive.TBL_MAT_RECEIVE+" rcv inner join "+PstMatReturn.TBL_MAT_RETURN+" rtn";
            sql += " on rcv."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+" = ";
            sql += " rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID];
            sql += " inner join "+PstMatReturnItem.TBL_MAT_RETURN_ITEM+" rtni";
            sql += " on rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]+" = ";
            sql += " rtni."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID];

            sql += " where rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_CLOSED;
            if(oidReceive != 0) {
                sql += " and rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID]+" = "+oidReceive;;
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
    
/**
     * Fungsi ini digunakan untuk mendapatkan return berdasarkan dokumen receive
     * @param oidRcv OID dari dokumen receive
     * @return result Nilai total dari sebuah return
     */
    public static double getTotalReturnByReceive(SrcAccPayable srcAccPayable, long oidReceive) {
        DBResultSet dbrs = null;
        double result = 0;
        String sql = "";
        try {
            sql  = " select sum(rtni."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_TOTAL]+")";
            sql += " from "+PstMatReceive.TBL_MAT_RECEIVE+" RCV inner join "+PstMatReturn.TBL_MAT_RETURN+" rtn";
            sql += " on RCV."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+" = ";
            sql += " rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID];
            sql += " inner join "+PstMatReturnItem.TBL_MAT_RETURN_ITEM+" rtni";
            sql += " on rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]+" = ";
            sql += " rtni."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID];
            //supplier
            sql += " left JOIN "+PstContactList.TBL_CONTACT_LIST+" CL";
            sql += " ON rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID];
            sql += " = CL."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            //sql += " where rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_CLOSED;

            //String  whereClause = SessAccPayable.getWhereClauseReceive(srcAccPayable);
              //String whereClause = SessAccPayable.getWhereClauseReturn(srcAccPayable);
                String whereClause = SessAccPayable.getWhereClauseReturn(srcAccPayable, oidReceive);
            if(whereClause != null && whereClause.length() > 0) {
                sql += " WHERE "+whereClause+" 1 = 1";
            }
            if(oidReceive != 0) {
                sql += " and rtn."+PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID]+" = "+oidReceive;;
            }
            //String  whereClause = SessAccPayable.getWhereClauseReceive(srcAccPayable);
            //if(whereClause != null && whereClause.length() > 0) {
               //sql += " AND "+whereClause+" 1=1";
            //}
                        
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
