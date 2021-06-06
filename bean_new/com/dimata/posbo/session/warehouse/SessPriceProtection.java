/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.warehouse;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.entity.search.SrcMatReceive;
import com.dimata.posbo.entity.warehouse.PriceProtection;
import com.dimata.posbo.entity.warehouse.PstPriceProtection;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import com.dimata.util.LogicParser;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class SessPriceProtection {
    private String stockCode="";
    private double value=0.0;
    private long supplierID;
    private String personName;
    private long stockCodeId;

    /**
     * @return the stockCode
     */
    public String getStockCode() {
        return stockCode;
    }

    /**
     * @param stockCode the stockCode to set
     */
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the supplierID
     */
    public long getSupplierID() {
        return supplierID;
    }

    /**
     * @param supplierID the supplierID to set
     */
    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    /**
     * @return the personName
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName the personName to set
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * @return the stockCodeId
     */
    public long getStockCodeId() {
        return stockCodeId;
    }

    /**
     * @param stockCodeId the stockCodeId to set
     */
    public void setStockCodeId(long stockCodeId) {
        this.stockCodeId = stockCodeId;
    }

    public static Vector searchPriceProtection(SrcMatReceive srcmatreceive, int start, int recordToGet, String whereLocation) {
	Vector vectRecNumber = LogicParser.textSentence(srcmatreceive.getReceivenumber());
	for (int i = 0; i < vectRecNumber.size(); i++) {
	    String name = (String) vectRecNumber.get(i);
	    if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0]))) {
		vectRecNumber.remove(i);
	    }
	}
	return getListPriceProtection(vectRecNumber, srcmatreceive, start, recordToGet,whereLocation);
    }
    
    
    public static int countPriceProtection(SrcMatReceive srcmatreceive,String whereLocation) {
	Vector vectRecNumber = LogicParser.textSentence(srcmatreceive.getReceivenumber());
	for (int i = 0; i < vectRecNumber.size(); i++) {
	    String name = (String) vectRecNumber.get(i);
	    if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0]))) {
		vectRecNumber.remove(i);
	    }
	}
        
	return getCountPriceProtection(vectRecNumber, srcmatreceive, 0, 0,whereLocation);
        
    }
    
    
    public static Vector getListPriceProtection(Vector vectRecNumber, SrcMatReceive srcmatreceive, int start, int recordToGet, String whereLocation) {
        
       DBResultSet dbrs = null;
       Vector result = new Vector(1, 1);
       try {
       String sql = " SELECT REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID] +
		    " ,REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_NOMOR_PRICE_PROTECTION] +
		    " ,REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE] +
		    " ,REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS] +
                    " ,REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_TOTAL_AMOUNT] +
		    " ,REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_REMARK] +
                    " ,LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " ,REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_APPROVAL_DATE] +
		    " FROM " + PstPriceProtection.TBL_POS_PRICE_PROTECTION + " REC" +
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=REC."+PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID];

	    String strRecNumber = "";
	    if (vectRecNumber != null && vectRecNumber.size() > 0) {
		for (int i = 0; i < vectRecNumber.size(); i++) {
		    if (strRecNumber.length() > 0) {
			strRecNumber = strRecNumber + " OR (REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_NOMOR_PRICE_PROTECTION] + " LIKE '%" + vectRecNumber.get(i) + "%')";
		    } else {
			strRecNumber = "(REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_NOMOR_PRICE_PROTECTION] + " LIKE '%" + vectRecNumber.get(i) + "%')";
		    }
		}
		strRecNumber = "(" + strRecNumber + ")";
	    }

	    String strStatus = "";
	    if (srcmatreceive.getReceivestatus() != null && srcmatreceive.getReceivestatus().size() > 0) {
		for (int b = 0; b < srcmatreceive.getReceivestatus().size(); b++) {
		    if (strStatus.length() != 0) {
			strStatus = strStatus + " OR " + "(REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS] + " =" + srcmatreceive.getReceivestatus().get(b) + ")";
		    } else {
			strStatus = "(REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS] + " =" + srcmatreceive.getReceivestatus().get(b) + ")";
		    }
		}
		strStatus = "(" + strStatus + ")";
	    }

	    String strDate = "";
	    if (srcmatreceive.getReceivedatestatus() != 0) {
		String startDate = Formater.formatDate(srcmatreceive.getReceivefromdate(), "yyyy-MM-dd 00:00:00");
		String endDate = Formater.formatDate(srcmatreceive.getReceivetodate(), "yyyy-MM-dd 23:59:59");
		strDate = " REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
	    }


	    String strLocationId = "";
	    if (srcmatreceive.getLocationId() != 0) {
		strLocationId = " REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID] +
			" = " + srcmatreceive.getLocationId();
	    }


	    String whereClause = "";
	    if (strRecNumber.length() > 0) {
		whereClause = strRecNumber;
	    }
            
	    if (strStatus.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strStatus;
		} else {
		    whereClause = whereClause + " AND " + strStatus;
		}
	    }

	    if (strDate.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strDate;
		} else {
		    whereClause = whereClause + " AND " + strDate;
		}
	    }


	    if (strLocationId.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strLocationId;
		} else {
		    whereClause = whereClause + " AND " + strLocationId;
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

	    switch (srcmatreceive.getReceivesortby()) {
		case 0:
		    //sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_REC_CODE];
		    break;
		case 1:
		    sql = sql + " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
		    break;
		case 2:
		   // sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_INVOICE_SUPPLIER];
		    break;
                //lokasi terima
                case 3:
                    sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID];
                    break;
                //end of lokasi terima
		case 4:
		    //sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_RECEIVE_DATE]+" DESC ";
		    break;
		case 5:
		    //sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_RECEIVE_STATUS];
		    break;
		case 6:
		    sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID];
		    break;
	    }

	    switch (DBHandler.DBSVR_TYPE) {
                
		case DBHandler.DBSVR_MYSQL:
		    if (start == 0 && recordToGet == 0) {
			sql = sql + "";
		    } else {
			sql = sql + " LIMIT " + start + "," + recordToGet;
		    }
		    break;
		case DBHandler.DBSVR_POSTGRESQL:
		    if (start == 0 && recordToGet == 0) {
			sql = sql + "";
		    } else {
			sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
		    }
		    break;
		case DBHandler.DBSVR_SYBASE:
		    break;
		case DBHandler.DBSVR_ORACLE:
		    break;
		case DBHandler.DBSVR_MSSQL:
		    break;
		default:
                         ;
	    }

	    //System.out.println("#SessMatReceive.getListReceiveMaterial(#,#,#,#,#): \n" + sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		Vector vect = new Vector(1, 1);
		PriceProtection priceProtection = new PriceProtection();

		priceProtection.setOID(rs.getLong(1));
		priceProtection.setNumberPP(rs.getString(2));
		priceProtection.setDateCreated(rs.getDate(3));
		priceProtection.setStatus(rs.getInt(4));
                priceProtection.setTotalAmount(rs.getDouble(5));
		priceProtection.setRemark(rs.getString(6));
                priceProtection.setLocationName(rs.getString(7));
                priceProtection.setDateApproved(rs.getDate(8));
                
		vect.add(priceProtection);
                
		result.add(vect);
	    }
	} catch (Exception e) {
	    System.out.println("SessPriceProtection.getListPriceProtection() err : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return result;

    }
    
    
    public static int getCountPriceProtection(Vector vectRecNumber, SrcMatReceive srcmatreceive, int start, int recordToGet, String whereLocation) {
        
       DBResultSet dbrs = null;
       int count=0;
       try {
       String sql = " SELECT COUNT(REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID] +")"+
		    " FROM " + PstPriceProtection.TBL_POS_PRICE_PROTECTION + " REC" +
                    " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC "+
                    " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"=REC."+PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID];

	    String strRecNumber = "";
	    if (vectRecNumber != null && vectRecNumber.size() > 0) {
		for (int i = 0; i < vectRecNumber.size(); i++) {
		    if (strRecNumber.length() > 0) {
			strRecNumber = strRecNumber + " OR (REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_NOMOR_PRICE_PROTECTION] + " LIKE '%" + vectRecNumber.get(i) + "%')";
		    } else {
			strRecNumber = "(REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_NOMOR_PRICE_PROTECTION] + " LIKE '%" + vectRecNumber.get(i) + "%')";
		    }
		}
		strRecNumber = "(" + strRecNumber + ")";
	    }

	    String strStatus = "";
	    if (srcmatreceive.getReceivestatus() != null && srcmatreceive.getReceivestatus().size() > 0) {
		for (int b = 0; b < srcmatreceive.getReceivestatus().size(); b++) {
		    if (strStatus.length() != 0) {
			strStatus = strStatus + " OR " + "(REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS] + " =" + srcmatreceive.getReceivestatus().get(b) + ")";
		    } else {
			strStatus = "(REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS] + " =" + srcmatreceive.getReceivestatus().get(b) + ")";
		    }
		}
		strStatus = "(" + strStatus + ")";
	    }

	    String strDate = "";
	    if (srcmatreceive.getReceivedatestatus() != 0) {
		String startDate = Formater.formatDate(srcmatreceive.getReceivefromdate(), "yyyy-MM-dd 00:00:00");
		String endDate = Formater.formatDate(srcmatreceive.getReceivetodate(), "yyyy-MM-dd 23:59:59");
		strDate = " REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
	    }


	    String strLocationId = "";
	    if (srcmatreceive.getLocationId() != 0) {
		strLocationId = " REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID] +
			" = " + srcmatreceive.getLocationId();
	    }


	    String whereClause = "";
	    if (strRecNumber.length() > 0) {
		whereClause = strRecNumber;
	    }
            
	    if (strStatus.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strStatus;
		} else {
		    whereClause = whereClause + " AND " + strStatus;
		}
	    }

	    if (strDate.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strDate;
		} else {
		    whereClause = whereClause + " AND " + strDate;
		}
	    }


	    if (strLocationId.length() > 0) {
		if (whereClause.length() == 0) {
		    whereClause = whereClause + strLocationId;
		} else {
		    whereClause = whereClause + " AND " + strLocationId;
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

	    switch (srcmatreceive.getReceivesortby()) {
		case 0:
		    //sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_REC_CODE];
		    break;
		case 1:
		    sql = sql + " ORDER BY CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
		    break;
		case 2:
		   // sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_INVOICE_SUPPLIER];
		    break;
                //lokasi terima
                case 3:
                    sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID];
                    break;
                //end of lokasi terima
		case 4:
		    //sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_RECEIVE_DATE]+" DESC ";
		    break;
		case 5:
		    //sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_RECEIVE_STATUS];
		    break;
		case 6:
		    sql = sql + " ORDER BY REC." + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID];
		    break;
	    }

	    switch (DBHandler.DBSVR_TYPE) {
                
		case DBHandler.DBSVR_MYSQL:
		    if (start == 0 && recordToGet == 0) {
			sql = sql + "";
		    } else {
			sql = sql + " LIMIT " + start + "," + recordToGet;
		    }
		    break;
		case DBHandler.DBSVR_POSTGRESQL:
		    if (start == 0 && recordToGet == 0) {
			sql = sql + "";
		    } else {
			sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
		    }
		    break;
		case DBHandler.DBSVR_SYBASE:
		    break;
		case DBHandler.DBSVR_ORACLE:
		    break;
		case DBHandler.DBSVR_MSSQL:
		    break;
		default:
                         ;
	    }

	    System.out.println("#SessMatReceive.getListReceiveMaterial(#,#,#,#,#): \n" + sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
                count=rs.getInt(1);
	    }
	} catch (Exception e) {
	    System.out.println("SessPriceProtection.getListPriceProtection() err : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return count;

    }
    
    public static int getMaxPriceProtectionCounter(Date date, PriceProtection priceProtection) {
        int counter = 0;
        DBResultSet dbrs = null;
        try {

            Date startDate = (Date) date.clone();
            startDate.setDate(1);

            Date endDate = (Date) date.clone();
            endDate.setMonth(endDate.getMonth() + 1);
            endDate.setDate(1);
            endDate.setDate(endDate.getDate() - 1);

            //I_PstDocType i_PstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

            String sql = "SELECT MAX(" + PstPriceProtection.fieldNames[PstPriceProtection.FLD_COUNTER] + ") AS MAXIMUM " +
                    " FROM " + PstPriceProtection.TBL_POS_PRICE_PROTECTION+
                    " WHERE (" + PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE] +
                    " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd 00:00:01") +
                    " ' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd 23:59:59") +
                    " ') AND " + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID] + " = " + priceProtection.getLocationId();

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                counter = rs.getInt("MAXIMUM");
            }

            rs.close();
        } catch (Exception e) {
            DBResultSet.close(dbrs);
            System.out.println("Exception getMaxCounter : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return counter;
    }
    
   public String generateCostingCode(PriceProtection priceProtection) {
       
        String code = "PP";
        String dateCode = "";
        if (priceProtection.getDateCreated() != null) {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+priceProtection.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            int nextCounter = priceProtection.getPpCounter();//getMaxCounter(date);
            Date date = priceProtection.getDateCreated();

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
  
   
      public static int issuePriceProtection(HttpServletRequest req, Vector records){
          if(records!=null && records.size()>0){
                    for(int i=0; i<records.size(); i++){
                         Vector vt = (Vector)records.get(i);
                         PriceProtection priceProtection = (PriceProtection) vt.get(0);
                         int xxx = FRMQueryString.requestInt(req, "invoice_"+priceProtection.getOID());
                         Date dateUpdate = FRMQueryString.requestDate(req, "date_update_"+priceProtection.getOID());
                         if(xxx==1){
                             //Date dateUpdate = FRMQueryString.requestDate(req, "date_update");
                             int x = updateStatusPP(priceProtection.getOID(),I_DocStatus.DOCUMENT_STATUS_POSTED,dateUpdate);
                         }
                    }
          }
          return 0;
     } 
      
      
      public static int updateStatusPP(long ppId, int transStatus, Date date) {

            String sql = "UPDATE " + PstPriceProtection.TBL_POS_PRICE_PROTECTION
                    + " SET " + PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS] + " = '" + transStatus + "'"
                    + " , " + PstPriceProtection.fieldNames[PstPriceProtection.FLD_APPROVAL_DATE] + " = '" + Formater.formatDate(date, "yyyy-MM-dd hh:mm") + "'"
                    + " WHERE " + PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID] + " = '" + ppId + "'";

             try {
                    DBHandler.execUpdate(sql);
                } catch (Exception e) {

                }
        return transStatus;
    }
    
}
