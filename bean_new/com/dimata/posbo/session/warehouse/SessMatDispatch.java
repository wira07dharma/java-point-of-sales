package com.dimata.posbo.session.warehouse;

import com.dimata.qdep.form.Control;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.search.SrcMatDispatch;
import com.dimata.posbo.entity.search.SrcMatRequest;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.util.Formater;
import com.dimata.util.Command;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;

import com.dimata.posbo.entity.purchasing.PstPurchaseRequest;
import com.dimata.util.LogicParser;
import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

//adding import for reposting stok
import com.dimata.posbo.entity.search.*;

public class SessMatDispatch extends Control {

    public static final String SESS_SRC_MATDISPATCH = "SESSION_MATERIAL_DISPATCH";
    public static final String SESSION_MATERIAL_DISPATCH_EXC = "SESSION_MATERIAL_DISPATCH_EXC";
    public static final String SESSION_TRANSFER_MR_TO_DF = "SESSION_TRANSFER_MR_TO_DF";
    public static final String SESSION_MATERIAL_DISPATCH_RECEIVED = "SESSION_MATERIAL_DISPATCH_RECEIVED";
    public static final String[] orderBy = {
		PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE],
		PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID],
		PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO],
		PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE],
		PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]
    };

	public static int getStrDutyFree(){
		String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
		System.out.println("#Duty Free: " + strDutyFree);
		int dutyFree = Integer.parseInt(strDutyFree);
		return dutyFree;
	}
	
    /* list for material request */
    public static Vector listMatDispatch(SrcMatDispatch srcMatDispatch, int start, int limit, String whereLocation) {
	Vector result = new Vector(1, 1);
	String sql = "SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		" , LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_REMARK] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_JENIS_DOKUMEN] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_NOMOR_BC] +
		" FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
		" INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
		" ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		" = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

	if (srcMatDispatch != null) {
	    String fromLocation = "";  
	    if (srcMatDispatch.getDispatchFrom() != 0) {
		fromLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcMatDispatch.getDispatchFrom() + ")";
	    }

	    String toLocation = "";
	    if (srcMatDispatch.getDispatchTo() != 0) {
		toLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + "=" + srcMatDispatch.getDispatchTo() + ")";
	    }

	    String date = "";
	    if (!srcMatDispatch.getIgnoreDate()) {
		date = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMatDispatch.getDispatchDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" +
			Formater.formatDate(srcMatDispatch.getDispatchDateTo(), "yyyy-MM-dd 23:59:59") + "')";
	    }

	    String dfCode = ""; 
	    if (srcMatDispatch.getDispatchCode().length() > 0) {
		Vector vectDfNumber = LogicParser.textSentence(srcMatDispatch.getDispatchCode());
		for (int i = 0; i < vectDfNumber.size(); i++) {
		    String name = (String) vectDfNumber.get(i);
		    if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0]))) {
			vectDfNumber.remove(i);
		    }
		}
		
		for( int k=0;k < vectDfNumber.size();k++){ 		    
		    if(dfCode.length()>0){
			dfCode = dfCode + " OR (DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }else{
			dfCode = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }
		}
	    }

	    String status = "";
	    if (srcMatDispatch.getStatus() >= 0) {
                if (srcMatDispatch.getTestCheck()>0){
                    status = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + (srcMatDispatch.getStatus()) + ")";
                }
		
	    }

            //+location type
            String locationType = "";
            //UPDATE BY DEWOK 20190907 - PENGECEKAN DENGAN CARA NEGASI SEPERTI DIBAWAH INI TIDAK BAGUS
            //LEBIH BAIK PENGECEKAN DILAKUKAN SPESIFIK TIPE YG DIINGINKAN
            /*
                locationType = " (DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_UNIT
                        + " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI
                        + " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_HPP
                        + " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR
                        + ")";
            */
            locationType = " ("
                    + "DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " IN (" + "" + PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_STORE + "," + PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE + ")"
                    + ")";

            //update opie-eyek 20131126
            /*
             * untuk transfer dari penjualan
             */
            String statusDelivery = "";
	    if (srcMatDispatch.getStatusDeliveryOrder() != 0) {
		statusDelivery = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_CASH_BILL_MAIN_ID] + " !='0')";
	    }else{
                statusDelivery = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_CASH_BILL_MAIN_ID] + " ='0' OR DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_CASH_BILL_MAIN_ID] + " ='0')";
            }

	    String where = "";
	    if (fromLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + fromLocation;
		} else {
		    where = fromLocation;
		}
	    }

	    if (toLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + toLocation;
		} else {
		    where = toLocation;
		}
	    }

	    if (date.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + date;
		} else {
		    where = date;
		}
	    }

	    if (dfCode.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + dfCode;
		} else {
		    where = dfCode;
		}
	    }

	    if (status.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + status;
		} else {
		    where = status;
		}
	    }

            //location type
            if (locationType.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + locationType;
                } else {
		    where = locationType;
		}
            }

            //update opie-eyek 20131126
            /*
             * untuk transfer dari penjualan
             */
            if (statusDelivery.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + statusDelivery;
		} else {
		    where = statusDelivery;
		}
	    }
            
                   
            if(!"".equals(whereLocation)){
               if (where.length() > 0) {
                   where= where + " AND " + whereLocation;
               }else{
                   where= where + whereLocation;
               }
               
            }

	    if (where.length() > 0) {
		where = " WHERE	" + where;
	    }

	    sql += where;
	    sql += " ORDER BY DF." + orderBy[srcMatDispatch.getSortBy()] + " DESC";
	    sql += ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]+" DESC ";
            /** defaultnya, list diurut berdasarkan DATE */
	}

	switch (DBHandler.DBSVR_TYPE) {
	    case DBHandler.DBSVR_MYSQL:
		if (start == 0 && limit == 0) {
		    sql = sql + "";
		} else {
		    sql = sql + " LIMIT " + start + "," + limit;
		}
		break;
	    case DBHandler.DBSVR_POSTGRESQL:
		if (start == 0 && limit == 0) {
		    sql = sql + "";
		} else {
		    sql = sql + " LIMIT " + limit + " OFFSET " + start;
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

	System.out.println("sql listMatDispatch:" + sql);
	DBResultSet dbrs = null;
	try {
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		Vector temp = new Vector();
		MatDispatch matDf = new MatDispatch();
		Location loc1 = new Location();

		matDf.setOID(rs.getLong(1));
		matDf.setDispatchCode(rs.getString(2));
		matDf.setDispatchDate(rs.getDate(3));
		matDf.setDispatchTo(rs.getLong(5));
		matDf.setDispatchStatus(rs.getInt(6));
		matDf.setRemark(rs.getString(7));
		matDf.setJenisDokumen(rs.getString(8));
		matDf.setNomorBeaCukai(rs.getString(9));
		temp.add(matDf);

		loc1.setName(rs.getString(4));
		temp.add(loc1);

		result.add(temp);
	    }
	    rs.close();
	} catch (Exception e) {
	    System.out.println("exception on search DF : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return result;
    }



    public static int getCountMatDispatch(SrcMatDispatch srcMatDispatch, String whereLocation) {

	int result = 0;
	String sql = "SELECT COUNT(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		" ) AS CNT FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
		" INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
		" ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		" = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

	if (srcMatDispatch != null) {
	    String fromLocation = "";

	    if (srcMatDispatch.getDispatchFrom() != 0) {
		fromLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcMatDispatch.getDispatchFrom() + ")";
	    }

	    String toLocation = "";
	    if (srcMatDispatch.getDispatchTo() != 0) {
		toLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + "=" + srcMatDispatch.getDispatchTo() + ")";
	    }

	    String date = "";
	    if (!srcMatDispatch.getIgnoreDate()) {
		date = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMatDispatch.getDispatchDateFrom(), "yyyy-MM-dd") + "' AND '" +
			Formater.formatDate(srcMatDispatch.getDispatchDateTo(), "yyyy-MM-dd") + "')";
	    }

	    /*String dfCode = "";
	    if (srcMatDispatch.getDispatchCode().length() > 0) {
		dfCode = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '" + srcMatDispatch.getDispatchCode() + "%')";
	    }*/

	    String dfCode = ""; 
	    if (srcMatDispatch.getDispatchCode().length() > 0) {
		Vector vectDfNumber = LogicParser.textSentence(srcMatDispatch.getDispatchCode());
		for (int i = 0; i < vectDfNumber.size(); i++) {
		    String name = (String) vectDfNumber.get(i);
		    if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0]))) {
			vectDfNumber.remove(i);
		    }
		} 
		
		for( int k=0;k < vectDfNumber.size();k++){ 		    
		    if(dfCode.length()>0){
			dfCode = dfCode + " OR (DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }else{
			dfCode = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }
		}
	    }
	    

	    String status = "";
	    if (srcMatDispatch.getStatus() >= 0) {
                if (srcMatDispatch.getTestCheck()>0){
                    status = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + (srcMatDispatch.getStatus()) + ")";
                }
	    }

            //update opie-eyek untuk membedakan transfer antar warehouse yang di anggap sebagai pengiriman barang
            String statusDelivery = "";
	    if (srcMatDispatch.getStatusDeliveryOrder() != 0) {
		statusDelivery = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_CASH_BILL_MAIN_ID] + " !='0')";
	    }else{
                statusDelivery = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_CASH_BILL_MAIN_ID] + " ='0')";
            }

            //+location type
            String locationType = "";
                locationType = " (DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_UNIT
                        + " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI
                        + " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_HPP
                        + " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR
                        + ")";

	    String where = "";
	    if (fromLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + fromLocation;
		} else {
		    where = fromLocation;
		}
	    }

	    if (toLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + toLocation;
		} else {
		    where = toLocation;
		}
	    }

	    if (date.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + date;
		} else {
		    where = date;
		}
	    }

	    if (dfCode.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + dfCode;
		} else {
		    where = dfCode;
		}
	    }

	    if (status.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + status;
		} else {
		    where = status;
		}
	    }

            //location type
            if (locationType.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + locationType;
                } else {
		    where = locationType;
		}
            }

            //update opie-eyek 20131126
            /*
             * untuk transfer dari penjualan
             */
            if (statusDelivery.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + statusDelivery;
		} else {
		    where = statusDelivery;
		}
	    }
            
            if(!"".equals(whereLocation)){
               if (where.length() > 0) {
                   where= where + " AND " + whereLocation;
               }else{
                   where= where + whereLocation;
               }
               
            }

	    if (where.length() > 0) {
		where = " WHERE	" + where;
	    }
	    sql += where;
	}
	DBResultSet dbrs = null;
	try {
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		result = rs.getInt(1);
	    }
	    rs.close();
	} catch (Exception e) {
	    System.out.println("exception on count search DF : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return result;
    }

    /**
     * untuk mencari document transfer
     * @return
     */
     public static Vector getMatDispatchInformation(String where) {
        DBResultSet dbrs = null;
	Vector vListDispatch = new Vector(1, 1);
	String sql = "SELECT COUNT(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +" ) AS CNT "+
                " , " + "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + ") AS STATUS "+
                " FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
		" INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
		" ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		" = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

	/*
         * SELECT COUNT(DF.DISPATCH_MATERIAL_ID ) AS CNT FROM pos_dispatch_material DF INNER JOIN location LOC ON DF.LOCATION_ID = LOC.LOCATION_ID
         WHERE	(DF.DISPATCH_STATUS = 2) AND  DF.LOCATION_TYPE != 2
         */
	 String strStatus = "";

            strStatus = "((DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]+ " = 0)"+
                        " OR " + "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = 10)"+
                        " OR " + "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = 1))"+
                    
                        " AND  DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] +" != 2";
            if(where.length()>0){
               strStatus=strStatus + where;
            }
            strStatus=strStatus+" GROUP BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS];


            sql = sql + " WHERE " + strStatus;
	try {
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		//result = rs.getInt(1);
                Vector vlist = new Vector(1, 1);
                vlist.add(rs.getInt("CNT"));
                 vlist.add(rs.getInt("STATUS"));
                vListDispatch.add(vlist);
	    }
            rs.close();
            return vListDispatch;
	} catch (Exception e) {
	    System.out.println("exception on count search DF : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return new Vector(1, 1);
    }

     /**
     * mencari jumlah document transfer unit
     * @return
     */
    public static Vector getMatDispatchUnitInformation(String where) {
        DBResultSet dbrs = null;
	Vector vListDispatch = new Vector(1, 1);
	String sql = "SELECT COUNT(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +" ) AS CNT "+
                " , " + "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + ") AS STATUS "+
                " FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
		" INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
		" ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		" = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

          
          /**
           * SELECT COUNT(DF.DISPATCH_MATERIAL_ID ) AS CNT FROM pos_dispatch_material DF INNER JOIN location LOC ON DF.LOCATION_ID = LOC.LOCATION_ID 
           * WHERE	(DF.DISPATCH_STATUS = 2) AND  DF.LOCATION_TYPE = 2
           */
          String strStatus = "";

          strStatus = "((DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]+ " = 0)"+
                        " OR " + "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = 10)"+
                        " OR " + "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = 1)"+
                        ")"+
                        " AND  DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] +" = 2";
          if(where.length()>0){
               strStatus=strStatus + where;
            }
          strStatus=strStatus+" GROUP BY DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS];


            sql = sql + " WHERE " + strStatus;
	try {
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		Vector vlist = new Vector(1, 1);
                vlist.add(rs.getInt("CNT"));
                vlist.add(rs.getInt("STATUS"));
                vListDispatch.add(vlist);
	    }
	    rs.close();
            return vListDispatch;
	} catch (Exception e) {
	    System.out.println("exception on count search DF : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return new Vector(1, 1);
    }
    
    
    public static Vector getTransferRequestInformation(String where) {
         DBResultSet dbrs = null;
	 Vector vListReceive = new Vector(1, 1);
         // Vector vlist = new Vector(1, 1);
	try {
	    String sql = " SELECT COUNT(REC." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCHASE_REQUEST_ID] + ") AS CNT " +
                         ", " + "(REC." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + ") AS STATUS"+
                         " FROM " + PstPurchaseRequest.TBL_PURCHASE_REQUEST + " REC"+
                         " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LC"+
                         " ON LC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+
                         " = REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID];

	    String strStatus = "";

            strStatus = "((REC." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " = 0)"+
                        " OR " + "(REC." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " = 1)"+
                        " OR " + "(REC." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " = 10))"+
                        " AND REC."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+"!='0'";
            
            if(where.length()>0){
               strStatus=strStatus + where;
            }
            
            strStatus=strStatus+" GROUP BY REC." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS];
		    
	    
            sql = sql + " WHERE " + strStatus;
	    

	    //System.out.println("getCountListReceiveMaterial: " + sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
                 Vector vlist = new Vector(1, 1);
                 vlist.add(rs.getInt("CNT"));
		 vlist.add(rs.getInt("STATUS"));
                vListReceive.add(vlist);
	    }
            rs.close();
            return vListReceive;
	} catch (Exception e) {
	    System.out.println("SessMatReceive.getCountListReceiveMaterial() err : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return new Vector(1, 1);
    }

     /* list for material request */
    public static Vector listMatDispatchReceive(SrcMatDispatch srcMatDispatch, int start, int limit) {
        return listMatDispatchReceive(srcMatDispatch, start, limit, "");
    }
    
    public static Vector listMatDispatchReceive(SrcMatDispatch srcMatDispatch, int start, int limit, String whereAdd) {
	Vector result = new Vector(1, 1);
	String sql = "SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		" , LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
		" , DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_REMARK] +
		" FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
		" INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
		" ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		" = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
               // " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] +
                //" = " + 2;


	if (srcMatDispatch != null) {
	    String fromLocation = "";
	    if (srcMatDispatch.getDispatchFrom() != 0) {
		fromLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcMatDispatch.getDispatchFrom() + ")";
	    }

	    String toLocation = "";
	    if (srcMatDispatch.getDispatchTo() != 0) {
		toLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + "=" + srcMatDispatch.getDispatchTo() + ")";
	    }

	    String date = "";
	    if (!srcMatDispatch.getIgnoreDate()) {
		date = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMatDispatch.getDispatchDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" +
			Formater.formatDate(srcMatDispatch.getDispatchDateTo(), "yyyy-MM-dd 23:59:59") + "')";
	    }

	    String dfCode = "";
	    if (srcMatDispatch.getDispatchCode().length() > 0) {
		Vector vectDfNumber = LogicParser.textSentence(srcMatDispatch.getDispatchCode());
		for (int i = 0; i < vectDfNumber.size(); i++) {
		    String name = (String) vectDfNumber.get(i);
		    if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0]))) {
			vectDfNumber.remove(i);
		    }
		}

		for( int k=0;k < vectDfNumber.size();k++){
		    if(dfCode.length()>0){
			dfCode = dfCode + " OR (DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }else{
			dfCode = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }
		}
	    }

	    String status = "";
	    if (srcMatDispatch.getStatus() >= 0) {
		status = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + (srcMatDispatch.getStatus()) + ")";
	    }

            //+location type
            String locationType = "";
                locationType = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = " + srcMatDispatch.getLocationType();

	    String where = "";
	    if (fromLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + fromLocation;
		} else {
		    where = fromLocation;
		}
	    }

	    if (toLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + toLocation;
		} else {
		    where = toLocation;
		}
	    }

	    if (date.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + date;
		} else {
		    where = date;
		}
	    }

	    if (dfCode.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + dfCode;
		} else {
		    where = dfCode;
		}
	    }

	    if (status.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + status;
		} else {
		    where = status;
		}
	    }


            //location type
            if (locationType.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + locationType;
                } else {
		    where = locationType;
		}
            }
            
            if (!whereAdd.equals("")) {
                where += whereAdd;
            }

	    if (where.length() > 0) {
		where = " WHERE	" + where;
	    }

	    sql += where;
	    sql += " ORDER BY DF." + orderBy[srcMatDispatch.getSortBy()];
	    sql += ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]+" DESC ";
	/** defaultnya, list diurut berdasarkan DATE */
	}

	switch (DBHandler.DBSVR_TYPE) {
	    case DBHandler.DBSVR_MYSQL:
		if (start == 0 && limit == 0) {
		    sql = sql + "";
		} else {
		    sql = sql + " LIMIT " + start + "," + limit;
		}
		break;
	    case DBHandler.DBSVR_POSTGRESQL:
		if (start == 0 && limit == 0) {
		    sql = sql + "";
		} else {
		    sql = sql + " LIMIT " + limit + " OFFSET " + start;
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

	System.out.println("sql listMatDispatch:" + sql);
	DBResultSet dbrs = null;
	try {
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		Vector temp = new Vector();
		MatDispatch matDf = new MatDispatch();
		Location loc1 = new Location();

		matDf.setOID(rs.getLong(1));
		matDf.setDispatchCode(rs.getString(2));
		matDf.setDispatchDate(rs.getDate(3));
		matDf.setDispatchTo(rs.getLong(5));
		matDf.setDispatchStatus(rs.getInt(6));
		matDf.setRemark(rs.getString(7));
		temp.add(matDf);

		loc1.setName(rs.getString(4));
		temp.add(loc1);

		result.add(temp);
	    }
	    rs.close();
	} catch (Exception e) {
	    System.out.println("exception on search DF : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return result;
    }

    public static int getCountMatDispatchReceive(SrcMatDispatch srcMatDispatch) {
        return getCountMatDispatchReceive(srcMatDispatch, "");
    }

    public static int getCountMatDispatchReceive(SrcMatDispatch srcMatDispatch, String whereAdd) {

	int result = 0;
	String sql = "SELECT COUNT(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		" ) AS CNT FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
		" INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
		" ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		" = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
                //" WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] +
                //" = " + 2;

	if (srcMatDispatch != null) {
	    String fromLocation = "";

	    if (srcMatDispatch.getDispatchFrom() != 0) {
		fromLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcMatDispatch.getDispatchFrom() + ")";
	    }

	    String toLocation = "";
	    if (srcMatDispatch.getDispatchTo() != 0) {
		toLocation = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + "=" + srcMatDispatch.getDispatchTo() + ")";
	    }

	    String date = "";
	    if (!srcMatDispatch.getIgnoreDate()) {
		date = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMatDispatch.getDispatchDateFrom(), "yyyy-MM-dd") + "' AND '" +
			Formater.formatDate(srcMatDispatch.getDispatchDateTo(), "yyyy-MM-dd") + "')";
	    }

	    /*String dfCode = "";
	    if (srcMatDispatch.getDispatchCode().length() > 0) {
		dfCode = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '" + srcMatDispatch.getDispatchCode() + "%')";
	    }*/

	    String dfCode = "";
	    if (srcMatDispatch.getDispatchCode().length() > 0) {
		Vector vectDfNumber = LogicParser.textSentence(srcMatDispatch.getDispatchCode());
		for (int i = 0; i < vectDfNumber.size(); i++) {
		    String name = (String) vectDfNumber.get(i);
		    if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0]))) {
			vectDfNumber.remove(i);
		    }
		}

		for( int k=0;k < vectDfNumber.size();k++){
		    if(dfCode.length()>0){
			dfCode = dfCode + " OR (DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }else{
			dfCode = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + vectDfNumber.get(k) + "%')";
		    }
		}
	    }


	    String status = "";
	    if (srcMatDispatch.getStatus() >= 0) {
		status = "(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + (srcMatDispatch.getStatus()) + ")";
	    }

             //+location type
            String locationType = "";
                locationType = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = " + srcMatDispatch.getLocationType();

	    String where = "";
	    if (fromLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + fromLocation;
		} else {
		    where = fromLocation;
		}
	    }

	    if (toLocation.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + toLocation;
		} else {
		    where = toLocation;
		}
	    }

	    if (date.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + date;
		} else {
		    where = date;
		}
	    }

	    if (dfCode.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + dfCode;
		} else {
		    where = dfCode;
		}
	    }

	    if (status.length() > 0) {
		if (where.length() > 0) {
		    where = where + " AND " + status;
		} else {
		    where = status;
		}
	    }

            //location type
            if (locationType.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + locationType;
                } else {
		    where = locationType;
		}
            }
            
            if (!whereAdd.equals("")) {
                where += whereAdd;
            }

	    if (where.length() > 0) {
		where = " WHERE	" + where;
	    }
	    sql += where;
	}
	DBResultSet dbrs = null;
	try {
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		result = rs.getInt(1);
	    }
	    rs.close();
	} catch (Exception e) {
	    System.out.println("exception on count search DF : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return result;
    }


    public static int findLimitStart(long oid, int recordToGet, SrcMatDispatch srcMaterial) {
	/*String order = "";
	int size = getCount(srcMaterial);
	int start = 0;
	boolean found =false;
	for(int i=0; (i < size) && !found ; i=i+recordToGet){
	Vector list =  listMatDispatch(srcMaterial,i,recordToGet);
	start = i;
	if(list.size()>0){
	for(int ls=0;ls<list.size();ls++){
	MatDispatch materialdispatch = (MatDispatch)list.get(ls);
	if(oid == materialdispatch.getOID())
	found=true;
	}
	}
	}
	if((start >= size) && (size > 0))
	start = start - recordToGet;
	return start;*/
	return 0;
    }

    public String generateRequestCode(MatDispatch materialDf) {

	String code = "DR";
	/*String dateCode = "";
	if(materialDf.getDfmDate()!=null){
	int nextCounter = materialDf.getDispatchCodeCounter();//getMaxCounter(date);
	Date date = materialDf.getDfmDate();
	int tgl = date.getDate();
	int bln = date.getMonth() + 1;
	int thn = date.getYear() + 1900;
	if(tgl<10){
	dateCode = "0"+tgl;
	}
	else{
	dateCode = ""+tgl;
	}
	if(bln<10){
	dateCode = dateCode+"0"+bln;
	}
	else{
	dateCode = dateCode+""+bln;
	}
	dateCode = dateCode + (String.valueOf(thn)).substring(2,4);
	String counter = "";
	if(nextCounter<10){
	counter = "00"+nextCounter;
	}
	else{
	if(nextCounter<100){
	counter = "0"+nextCounter;
	}
	else{
	counter = ""+counter;
	}
	}
	code = code+"-"+dateCode+"-"+counter;
	}
	 */
	return code;
    }

    public static int getMaxReqCounter(Date date) {
	int counter = 0;
	/*DBResultSet dbrs = null;
	Date startDate = (Date)date.clone();
	startDate.setDate(1);
	Date endDate = (Date)date.clone();
	endDate.setMonth(endDate.getMonth()+1);
	endDate.setDate(1);
	endDate.setDate(endDate.getDate()-1);
	try{
	I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	String sql = "SELECT MAX("+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE_COUNTER]+") AS MAXIMUM "+
	" FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" WHERE ("+
	PstMatDispatch.fieldNames[PstMatDispatch.FLD_DFM_DATE]+" BETWEEN \""+
	Formater.formatDate(startDate, "yyyy-MM-dd")+"\" AND \""+Formater.formatDate(endDate, "yyyy-MM-dd")+"\""+
	") AND ("+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TYPE]+">99)"+
	" AND ("+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR)+")";
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	counter = rs.getInt("MAXIMUM");
	}
	rs.close();
	}
	catch(Exception e){
	DBResultSet.close(dbrs);
	System.out.println("Exception getMaxCounter : "+e.toString());
	return 0;
	}
	finally{
	DBResultSet.close(dbrs);
	}
	 */
	return counter;
    }

    //-------------------------------------------

    /* material dispatch */

    /* list for material dispatch */
    /*public static Vector listMatDispatch(SrcMatDispatch srcMaterial, int start, int limit)
    {
    Vector result = new Vector(1,1);
    /*if(srcMaterial!=null){
    String sql = "SELECT * FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" AS DF ";
    String innerJoin = "";
    String materialCode = "";
    String materialName = "";
    if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
    innerJoin = "INNER JOIN "+PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
    " ON MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
    " DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+" "+
    " INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
    " ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
    " MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_STOCK_ID]+" "+
    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
    PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];
    if(srcMaterial.getMaterialCode().length()>0){
    materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
    }
    if(srcMaterial.getMaterialName().length()>0){
    materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
    }
    }
    String fromLocation = "";
    if(srcMaterial.getDispatchFrom()!=0){
    fromLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
    }
    String toLocation = "";
    if(srcMaterial.getDispatchTo()!=0){
    toLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
    }
    String date = "";
    if(!srcMaterial.getIgnoreDate()){
    date = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
    Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
    }
    String dfCode = "";
    if(srcMaterial.getDispatchCode().length()>0){
    dfCode = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
    }
    String status = "";
    if(srcMaterial.getStatus()>=0){
    status =  "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
    }
    String referency = "";
    String innerReferency = "";
    if(srcMaterial.getRequestRefCode().length()>0){
    innerReferency = " INNER JOIN "+PstMatDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
    " DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" ";
    referency = "(REF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
    }
    String where = "";
    String lastInner = "";
    if(materialCode.length()>0 || materialName.length()>0){
    lastInner = innerJoin;
    if(materialCode.length()>0){
    where = materialCode;
    }
    if( materialName.length()>0){
    if( where.length()>0){
    where = where + " AND " + materialName;
    }
    else{
    where = materialName;
    }
    }
    }
    if( fromLocation.length()>0){
    if( where.length()>0){
    where = where + " AND " + fromLocation;
    }
    else{
    where = fromLocation;
    }
    }
    if( toLocation.length()>0){
    if( where.length()>0){
    where = where + " AND " + toLocation;
    }
    else{
    where = toLocation;
    }
    }
    if( date.length()>0){
    if( where.length()>0){
    where = where + " AND " + date;
    }
    else{
    where = date;
    }
    }
    if( dfCode.length()>0){
    if( where.length()>0){
    where = where + " AND " + dfCode;
    }
    else{
    where = dfCode;
    }
    }
    if(status.length()>0){
    if( where.length()>0){
    where = where + " AND " + status;
    }
    else{
    where = status;
    }
    }
    if(referency.length()>0){
    if( where.length()>0){
    where = where + " AND " + referency;
    }
    else{
    where = referency;
    }
    }
    try{
    I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
    if( where.length()>0){
    where = where + " AND (DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
    }
    else{
    where = "(DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
    }
    }
    catch(Exception e){
    System.out.println("Exception e - interface : "+e.toString());
    }
    if(where.length()>0){
    where = " WHERE	("+where+")";
    }
    sql = sql + lastInner + innerReferency + where +" ORDER BY DF."+orderBy[srcMaterial.getSortBy()];//+" LIMIT "+start+","+limit;
    switch (DBHandler.DBSVR_TYPE) {
    case DBHandler.DBSVR_MYSQL :
    if(start == 0 && limit == 0)
    sql = sql + "";
    else
    sql = sql + " LIMIT " + start + ","+ limit ;
    break;
    case DBHandler.DBSVR_POSTGRESQL :
    if(start == 0 && limit == 0)
    sql = sql + "";
    else
    sql = sql + " LIMIT " +limit + " OFFSET "+ start ;
    break;
    case DBHandler.DBSVR_SYBASE :
    break;
    case DBHandler.DBSVR_ORACLE :
    break;
    case DBHandler.DBSVR_MSSQL :
    break;
    default:
    ;
    }
    DBResultSet dbrs = null;
    try{
    dbrs = DBHandler.execQueryResult(sql);
    ResultSet rs = dbrs.getResultSet();
    while(rs.next()){
    MatDispatch matDf = new MatDispatch();
    PstMatDispatch.resultToObject(rs, matDf);
    result.add(matDf);
    }
    rs.close();
    }
    catch(Exception e){
    DBResultSet.close(dbrs);
    System.out.println("exception on search DF REG : "+e.toString());
    }
    finally{
    DBResultSet.close(dbrs);
    }
    return result;
    }
    else
    {
    }
    return result;
    }
     */
    /*
    for material dispatch
     */
    public static int getCount(SrcMatDispatch srcMaterial) {

	int result = 0;

	/*if(srcMaterial!=null){
	String sql = "SELECT COUNT(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+") FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" AS DF ";
	String innerJoin = "";
	String materialCode = "";
	String materialName = "";
	if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
	innerJoin = "INNER JOIN "+PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
	" ON MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+" "+
	" INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
	" ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
	" MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_STOCK_ID]+" "+
	" INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
	PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];
	if(srcMaterial.getMaterialCode().length()>0){
	materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
	}
	if(srcMaterial.getMaterialName().length()>0){
	materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
	}
	}
	String fromLocation = "";
	if(srcMaterial.getDispatchFrom()!=0){
	fromLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
	}
	String toLocation = "";
	if(srcMaterial.getDispatchTo()!=0){
	toLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
	}
	String date = "";
	if(!srcMaterial.getIgnoreDate()){
	date = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
	Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
	}
	String dfCode = "";
	if(srcMaterial.getDispatchCode().length()>0){
	dfCode = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
	}
	String status = "";
	if(srcMaterial.getStatus()>=0){
	status =  "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
	}
	String referency = "";
	String innerReferency = "";
	if(srcMaterial.getRequestRefCode().length()>0){
	innerReferency = " INNER JOIN "+PstMatDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" ";
	referency = "(REF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
	}
	String where = "";
	String lastInner = "";
	if(materialCode.length()>0 || materialName.length()>0){
	lastInner = innerJoin;
	if(materialCode.length()>0){
	where = materialCode;
	}
	if( materialName.length()>0){
	if( where.length()>0){
	where = where + " AND " + materialName;
	}
	else{
	where = materialName;
	}
	}
	}
	if( fromLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + fromLocation;
	}
	else{
	where = fromLocation;
	}
	}
	if( toLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + toLocation;
	}
	else{
	where = toLocation;
	}
	}
	if( date.length()>0){
	if( where.length()>0){
	where = where + " AND " + date;
	}
	else{
	where = date;
	}
	}
	if( dfCode.length()>0){
	if( where.length()>0){
	where = where + " AND " + dfCode;
	}
	else{
	where = dfCode;
	}
	}
	if(status.length()>0){
	if( where.length()>0){
	where = where + " AND " + status;
	}
	else{
	where = status;
	}
	}
	if(referency.length()>0){
	if( where.length()>0){
	where = where + " AND " + referency;
	}
	else{
	where = referency;
	}
	}
	try{
	I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	if( where.length()>0){
	where = where + " AND (DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
	}
	else{
	where = "(DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
	}
	}
	catch(Exception e){
	System.out.println("Exception e - interface : "+e.toString());
	}
	if(where.length()>0){
	where = " WHERE	("+where+")";
	}
	sql = sql + lastInner + innerReferency + where ;//+" ORDER BY "+orderBy[srcMaterial.getSortBy()]+" LIMIT "+start+","+limit;
	DBResultSet dbrs = null;
	try{
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	result = rs.getInt(1);
	}
	rs.close();
	}
	catch(Exception e){
	DBResultSet.close(dbrs);
	System.out.println("exception on search DF REG : "+e.toString());
	}
	finally{
	DBResultSet.close(dbrs);
	}
	return result;
	}else{
	}*/
	return result;
    }


    /* for material request */
    public static int findLimitStart(long oid, int recordToGet, SrcMatRequest srcMaterial) {
	/*String order = "";
	int size = getCount(srcMaterial);
	int start = 0;
	boolean found =false;
	for(int i=0; (i < size) && !found ; i=i+recordToGet){
	Vector list =  listMatDispatch(srcMaterial,i,recordToGet);
	start = i;
	if(list.size()>0){
	for(int ls=0;ls<list.size();ls++){
	MatDispatch materialdispatch = (MatDispatch)list.get(ls);
	if(oid == materialdispatch.getOID())
	found=true;
	}
	}
	}
	if((start >= size) && (size > 0))
	start = start - recordToGet;
	return start;
	 */
	return 0;
    }

    public static String generateDispatchCode(MatDispatch matDispatch) {
	// belum mengambl ke workflow, karena tidak di gunakan workflownya.
	String code = "DF";
	String dateCode = "";

	// penambahan baru request dari taiga: 18/03/2005
	String kodeLoc = ""; 
	try {
	    Location loc = PstLocation.fetchExc(matDispatch.getDispatchTo());
	    kodeLoc = loc.getCode();
	} catch (Exception e) {
	}
 
	if (matDispatch.getDispatchDate() != null) {
	    /** get location code; gwawan@21juni2007 */
	    Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=" + matDispatch.getLocationId(), "");
	    Location location = (Location) vctLocation.get(0);

	    int nextCounter = matDispatch.getDispatchCodeCounter();//getMaxCounter(date);
	    Date date = matDispatch.getDispatchDate();

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
	    code = location.getCode() + "-" + dateCode + "-" + code + "-" + kodeLoc + "-" + counter;
	}
	return code;
    }

    public static int getMaxDispatchCounter(Date date, MatDispatch matDispatch) {
	int counter = 0;
	DBResultSet dbrs = null;
	try {

	    Date startDate = (Date) date.clone();
	    startDate.setDate(1);

	    Date endDate = (Date) date.clone();
	    endDate.setMonth(endDate.getMonth() + 1);
	    endDate.setDate(1);
	    endDate.setDate(endDate.getDate() - 1);

	    I_PstDocType i_PstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

	    String sql = "SELECT MAX(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE_COUNTER] + ") AS MAXIMUM " +
		    " FROM " + PstMatDispatch.TBL_DISPATCH +
		    " WHERE (" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd 00:00:00") +
		    " ' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd 23:59:59") +
		    " ') AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + matDispatch.getLocationId() +
                    //For diferrent between code dispatch and dispatch unit
                    " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = " + matDispatch.getLocationType();

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

    /*
     * Untuk transfer unit
     * Generate code
     * By Mirahu
     */
        public String generateDispatchUnitCode(MatDispatch matDispatch) {
	// belum mengambl ke workflow, karena tidak di gunakan workflownya.
	String code = "DU";
	String dateCode = "";

	// penambahan baru request dari taiga: 18/03/2005
	String kodeLoc = "";
	try {
	    Location loc = PstLocation.fetchExc(matDispatch.getDispatchTo());
	    kodeLoc = loc.getCode();
	} catch (Exception e) {
	}

	if (matDispatch.getDispatchDate() != null) {
	    /** get location code; gwawan@21juni2007 */
	    Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=" + matDispatch.getLocationId(), "");
	    Location location = (Location) vctLocation.get(0);

	    int nextCounter = matDispatch.getDispatchCodeCounter();//getMaxCounter(date);
	    Date date = matDispatch.getDispatchDate();

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
	    code = location.getCode() + "-" + dateCode + "-" + code + "-" + kodeLoc + "-" + counter;
	}
	return code;
    }

    public static int getMaxDispatchUnitCounter(Date date, MatDispatch matDispatch) {
	int counter = 0;
	DBResultSet dbrs = null;
	try {

	    Date startDate = (Date) date.clone();
	    startDate.setDate(1);

	    Date endDate = (Date) date.clone();
	    endDate.setMonth(endDate.getMonth() + 1);
	    endDate.setDate(1);
	    endDate.setDate(endDate.getDate() - 1);

	    I_PstDocType i_PstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

	    String sql = "SELECT MAX(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE_COUNTER] + ") AS MAXIMUM " +
		    " FROM " + PstMatDispatch.TBL_DISPATCH +
		    " WHERE (" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd 00:00:00") +
		    " ' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd 23:59:59") +
		    " ') AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + matDispatch.getLocationId()+
                    " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = " + matDispatch.getLocationType();

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
    /*
     * End Of transfer unit
     */

    //----------------------------------
    /* transfer MR to DF */

    /* list for material transfer */
    public static Vector listMaterialTransfer(SrcMatRequest srcMaterial, int start, int limit) {
	Vector result = new Vector(1, 1);
	/*if(srcMaterial!=null){
	String sql = "SELECT * FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" AS DF ";
	String innerJoin = "";
	String materialCode = "";
	String materialName = "";
	if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
	innerJoin = "INNER JOIN "+PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
	" ON MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+" "+
	" INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
	" ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
	" MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_STOCK_ID]+" "+
	" INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
	PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];
	if(srcMaterial.getMaterialCode().length()>0){
	materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
	}
	if(srcMaterial.getMaterialName().length()>0){
	materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
	}
	}
	String fromLocation = "";
	if(srcMaterial.getFromLocation()!=0){
	fromLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getFromLocation()+")";
	}
	String toLocation = "";
	if(srcMaterial.getToLocation()!=0){
	toLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getToLocation()+")";
	}
	String date = "";
	if(!srcMaterial.getIgnoreDate()){
	date = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getFromDate(), "yyyy-MM-dd")+"\" AND \""+
	Formater.formatDate(srcMaterial.getEndDate(), "yyyy-MM-dd")+"\")";
	}
	String dfCode = "";
	if(srcMaterial.getDfCode().length()>0){
	dfCode = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDfCode()+"%\")";
	}
	String status = "";
	String where = "";
	String lastInner = "";
	if(materialCode.length()>0 || materialName.length()>0){
	lastInner = innerJoin;
	if(materialCode.length()>0){
	where = materialCode;
	}
	if( materialName.length()>0){
	if( where.length()>0){
	where = where + " AND " + materialName;
	}
	else{
	where = materialName;
	}
	}
	}
	if( fromLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + fromLocation;
	}
	else{
	where = fromLocation;
	}
	}
	if( toLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + toLocation;
	}
	else{
	where = toLocation;
	}
	}
	if( date.length()>0){
	if( where.length()>0){
	where = where + " AND " + date;
	}
	else{
	where = date;
	}
	}
	if( dfCode.length()>0){
	if( where.length()>0){
	where = where + " AND " + dfCode;
	}
	else{
	where = dfCode;
	}
	}
	if(status.length()>0){
	if( where.length()>0){
	where = where + " AND " + status;
	}
	else{
	where = status;
	}
	}
	try{
	I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	}
	catch(Exception e){
	System.out.println("Exception e - interface : "+e.toString());
	}
	try{
	I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	if( where.length()>0){
	where = where + " AND (DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
	" AND (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
	}
	else{
	where = "(DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
	" AND (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
	}
	}
	catch(Exception e){
	System.out.println("exception e : "+e.toString());
	}
	if(where.length()>0){
	where = " WHERE	("+where+")";
	}
	sql = sql + lastInner + where +" ORDER BY DF."+orderBy[srcMaterial.getSortBy()];//+" LIMIT "+start+","+limit;
	switch (DBHandler.DBSVR_TYPE) {
	case DBHandler.DBSVR_MYSQL :
	if(start == 0 && limit == 0)
	sql = sql + "";
	else
	sql = sql + " LIMIT " + start + ","+ limit ;
	break;
	case DBHandler.DBSVR_POSTGRESQL :
	if(start == 0 && limit == 0)
	sql = sql + "";
	else
	sql = sql + " LIMIT " +limit + " OFFSET "+ start ;
	break;
	case DBHandler.DBSVR_SYBASE :
	break;
	case DBHandler.DBSVR_ORACLE :
	break;
	case DBHandler.DBSVR_MSSQL :
	break;
	default:
	;
	}
	DBResultSet dbrs = null;
	try{
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	MatDispatch matDf = new MatDispatch();
	PstMatDispatch.resultToObject(rs, matDf);
	result.add(matDf);
	}
	rs.close();
	}
	catch(Exception e){
	DBResultSet.close(dbrs);
	System.out.println("exception on search DF REG : "+e.toString());
	}
	finally{
	DBResultSet.close(dbrs);
	}
	return result;
	}else{
	}
	 */
	return result;
    }


    /*
    for material transfer --> namanya sudah terlanjur
    (untuk material dispatch querynya juga disini)
     */
    public static int getCountTransfer(SrcMatRequest srcMaterial) {

	int result = 0;

	/*if(srcMaterial!=null){
	String sql = "SELECT COUNT(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+") FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" AS DF ";
	String innerJoin = "";
	String materialCode = "";
	String materialName = "";
	if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
	innerJoin = "INNER JOIN "+PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
	" ON MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+" "+
	" INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
	" ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
	" MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_STOCK_ID]+" "+
	" INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
	PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];
	if(srcMaterial.getMaterialCode().length()>0){
	materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
	}
	if(srcMaterial.getMaterialName().length()>0){
	materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
	}
	}
	String fromLocation = "";
	if(srcMaterial.getFromLocation()!=0){
	fromLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getFromLocation()+")";
	}
	String toLocation = "";
	if(srcMaterial.getToLocation()!=0){
	toLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getToLocation()+")";
	}
	String date = "";
	if(!srcMaterial.getIgnoreDate()){
	date = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getFromDate(), "yyyy-MM-dd")+"\" AND \""+
	Formater.formatDate(srcMaterial.getEndDate(), "yyyy-MM-dd")+"\")";
	}
	String dfCode = "";
	if(srcMaterial.getDfCode().length()>0){
	dfCode = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDfCode()+"%\")";
	}
	String status = "";
	String where = "";
	String lastInner = "";
	if(materialCode.length()>0 || materialName.length()>0){
	lastInner = innerJoin;
	if(materialCode.length()>0){
	where = materialCode;
	}
	if( materialName.length()>0){
	if( where.length()>0){
	where = where + " AND " + materialName;
	}
	else{
	where = materialName;
	}
	}
	}
	if( fromLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + fromLocation;
	}
	else{
	where = fromLocation;
	}
	}
	if( toLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + toLocation;
	}
	else{
	where = toLocation;
	}
	}
	if( date.length()>0){
	if( where.length()>0){
	where = where + " AND " + date;
	}
	else{
	where = date;
	}
	}
	if( dfCode.length()>0){
	if( where.length()>0){
	where = where + " AND " + dfCode;
	}
	else{
	where = dfCode;
	}
	}
	if(status.length()>0){
	if( where.length()>0){
	where = where + " AND " + status;
	}
	else{
	where = status;
	}
	}
	try{
	I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	if( where.length()>0){
	where = where + " AND (DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
	" AND (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
	}
	else{
	where = "(DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
	" AND (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
	}
	}
	catch(Exception e){
	System.out.println("Exception e : "+e.toString());
	}
	if(where.length()>0){
	where = " WHERE ("+where+")";
	}
	sql = sql + lastInner + where;
	DBResultSet dbrs = null;
	try{
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	result = rs.getInt(1);
	}
	rs.close();
	}
	catch(Exception e){
	DBResultSet.close(dbrs);
	System.out.println("exception on search DF REG : "+e.toString());
	}
	finally{
	DBResultSet.close(dbrs);
	}
	return result;
	}else{
	return 0;
	}*/
	return result;
    }

    //------------------------------------------
    public static int findLimitStartTransfer(long oid, int recordToGet, SrcMatRequest srcMaterial) {
	/*String order = "";
	int size = getCountTransfer(srcMaterial);
	int start = 0;
	boolean found =false;
	for(int i=0; (i < size) && !found ; i=i+recordToGet){
	Vector list =  listMaterialTransfer(srcMaterial,i,recordToGet);
	start = i;
	if(list.size()>0){
	for(int ls=0;ls<list.size();ls++){
	MatDispatch materialdispatch = (MatDispatch)list.get(ls);
	if(oid == materialdispatch.getOID())
	found=true;
	}
	}
	}
	if((start >= size) && (size > 0))
	start = start - recordToGet;
	return start;*/
	return 0;
    }

    //-------------------------
    /* general  */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
	int cmd = Command.LIST;
	/*int mdl = vectSize % recordToGet;
	vectSize = vectSize + mdl;
	if(start == 0)
	cmd =  Command.FIRST;
	else{
	if(start == (vectSize-recordToGet))
	cmd = Command.LAST;
	else{
	start = start + recordToGet;
	if(start <= (vectSize - recordToGet)){
	cmd = Command.NEXT;
	}else{
	start = start - recordToGet;
	if(start > 0){
	cmd = Command.PREV;
	}
	}
	}
	}
	 */
	return cmd;
    }


    //----------------
    //DISPATCH RECEIVED

    /* list for material rec */
    public static Vector listMatDispatchRec(SrcMatDispatch srcMaterial, int start, int limit) {
	Vector result = new Vector(1, 1);
	/*if(srcMaterial!=null){
	String sql = "SELECT * FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" AS DF ";
	String innerJoin = "";
	String materialCode = "";
	String materialName = "";
	if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
	innerJoin = "INNER JOIN "+PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
	" ON MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+" "+
	" INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
	" ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
	" MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_STOCK_ID]+" "+
	" INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
	PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];
	if(srcMaterial.getMaterialCode().length()>0){
	materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
	}
	if(srcMaterial.getMaterialName().length()>0){
	materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
	}
	}
	String fromLocation = "";
	if(srcMaterial.getDispatchFrom()!=0){
	fromLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
	}
	String toLocation = "";
	if(srcMaterial.getDispatchTo()!=0){
	toLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
	}
	String date = "";
	if(!srcMaterial.getIgnoreDate()){
	date = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
	Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
	}
	String dfCode = "";
	if(srcMaterial.getDispatchCode().length()>0){
	dfCode = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
	}
	String status = "";
	if(srcMaterial.getStatus()>=0){
	status =  "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
	}
	String referency = "";
	String innerReferency = "";
	if(srcMaterial.getRequestRefCode().length()>0){
	innerReferency = " INNER JOIN "+PstMatDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" ";
	referency = "(REF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
	}
	String where = "";
	String lastInner = "";
	if(materialCode.length()>0 || materialName.length()>0){
	lastInner = innerJoin;
	if(materialCode.length()>0){
	where = materialCode;
	}
	if( materialName.length()>0){
	if( where.length()>0){
	where = where + " AND " + materialName;
	}
	else{
	where = materialName;
	}
	}
	}
	if( fromLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + fromLocation;
	}
	else{
	where = fromLocation;
	}
	}
	if( toLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + toLocation;
	}
	else{
	where = toLocation;
	}
	}
	if( date.length()>0){
	if( where.length()>0){
	where = where + " AND " + date;
	}
	else{
	where = date;
	}
	}
	if( dfCode.length()>0){
	if( where.length()>0){
	where = where + " AND " + dfCode;
	}
	else{
	where = dfCode;
	}
	}
	if(status.length()>0){
	if( where.length()>0){
	where = where + " AND " + status;
	}
	else{
	where = status;
	}
	}
	if(referency.length()>0){
	if( where.length()>0){
	where = where + " AND " + referency;
	}
	else{
	where = referency;
	}
	}
	try{
	I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	if( where.length()>0){
	where = where + " AND (DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
	" AND ((DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";
	}
	else{
	where = "(DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
	" AND ((DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";
	}
	}
	catch(Exception e){
	System.out.println("Exception e : "+e.toString());
	}
	if(where.length()>0){
	where = " WHERE	("+where+")";
	}
	sql = sql + lastInner + innerReferency + where +" ORDER BY DF."+orderBy[srcMaterial.getSortBy()];//+" LIMIT "+start+","+limit;
	switch (DBHandler.DBSVR_TYPE) {
	case DBHandler.DBSVR_MYSQL :
	if(start == 0 && limit == 0)
	sql = sql + "";
	else
	sql = sql + " LIMIT " + start + ","+ limit ;
	break;
	case DBHandler.DBSVR_POSTGRESQL :
	if(start == 0 && limit == 0)
	sql = sql + "";
	else
	sql = sql + " LIMIT " +limit + " OFFSET "+ start ;
	break;
	case DBHandler.DBSVR_SYBASE :
	break;
	case DBHandler.DBSVR_ORACLE :
	break;
	case DBHandler.DBSVR_MSSQL :
	break;
	default:
	;
	}
	DBResultSet dbrs = null;
	try{
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	MatDispatch matDf = new MatDispatch();
	PstMatDispatch.resultToObject(rs, matDf);
	result.add(matDf);
	}
	rs.close();
	}
	catch(Exception e){
	DBResultSet.close(dbrs);
	System.out.println("exception on search DF REG : "+e.toString());
	}
	finally{
	DBResultSet.close(dbrs);
	}
	return result;
	}else{
	}
	 */
	return result;
    }


    /*
    for material dispatch
     */
    public static int getCountRec(SrcMatDispatch srcMaterial) {

	int result = 0;

	/*if(srcMaterial!=null){
	String sql = "SELECT COUNT(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+") FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" AS DF ";
	String innerJoin = "";
	String materialCode = "";
	String materialName = "";
	if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
	innerJoin = "INNER JOIN "+PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
	" ON MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+" "+
	" INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
	" ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
	" MAT."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MAT_STOCK_ID]+" "+
	" INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
	PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];
	if(srcMaterial.getMaterialCode().length()>0){
	materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
	}
	if(srcMaterial.getMaterialName().length()>0){
	materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
	}
	}
	String fromLocation = "";
	if(srcMaterial.getDispatchFrom()!=0){
	fromLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
	}
	String toLocation = "";
	if(srcMaterial.getDispatchTo()!=0){
	toLocation = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
	}
	String date = "";
	if(!srcMaterial.getIgnoreDate()){
	date = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
	Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
	}
	String dfCode = "";
	if(srcMaterial.getDispatchCode().length()>0){
	dfCode = "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
	}
	String status = "";
	if(srcMaterial.getStatus()>=0){
	status =  "(DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
	}
	String referency = "";
	String innerReferency = "";
	if(srcMaterial.getRequestRefCode().length()>0){
	innerReferency = " INNER JOIN "+PstMatDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
	" DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" ";
	referency = "(REF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
	}
	String where = "";
	String lastInner = "";
	if(materialCode.length()>0 || materialName.length()>0){
	lastInner = innerJoin;
	if(materialCode.length()>0){
	where = materialCode;
	}
	if( materialName.length()>0){
	if( where.length()>0){
	where = where + " AND " + materialName;
	}
	else{
	where = materialName;
	}
	}
	}
	if( fromLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + fromLocation;
	}
	else{
	where = fromLocation;
	}
	}
	if( toLocation.length()>0){
	if( where.length()>0){
	where = where + " AND " + toLocation;
	}
	else{
	where = toLocation;
	}
	}
	if( date.length()>0){
	if( where.length()>0){
	where = where + " AND " + date;
	}
	else{
	where = date;
	}
	}
	if( dfCode.length()>0){
	if( where.length()>0){
	where = where + " AND " + dfCode;
	}
	else{
	where = dfCode;
	}
	}
	if(status.length()>0){
	if( where.length()>0){
	where = where + " AND " + status;
	}
	else{
	where = status;
	}
	}
	if(referency.length()>0){
	if( where.length()>0){
	where = where + " AND " + referency;
	}
	else{
	where = referency;
	}
	}
	try{
	I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	if( where.length()>0){
	where = where + " AND (DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
	" AND ((DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";
	}
	else{
	where = "(DF."+ PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
	" AND ((DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";
	}
	}
	catch(Exception e){
	System.out.println(e.toString());
	}
	if(where.length()>0){
	where = " WHERE	("+where+")";
	}
	sql = sql + lastInner + innerReferency + where ;//+" ORDER BY "+orderBy[srcMaterial.getSortBy()]+" LIMIT "+start+","+limit;
	//  System.out.println(sql);
	DBResultSet dbrs = null;
	try{
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	result = rs.getInt(1);
	}
	rs.close();
	}
	catch(Exception e){
	DBResultSet.close(dbrs);
	System.out.println("exception on search DF REG : "+e.toString());
	}
	finally{
	DBResultSet.close(dbrs);
	}
	return result;
	}else{
	}
	 */
	return result;
    }


    /* for material receive */
    public static int findLimitStartRec(long oid, int recordToGet, SrcMatDispatch srcMaterial) {
	/*String order = "";
	int size = getCountRec(srcMaterial);
	int start = 0;
	boolean found =false;
	for(int i=0; (i < size) && !found ; i=i+recordToGet){
	Vector list =  listMatDispatchRec(srcMaterial,i,recordToGet);
	start = i;
	if(list.size()>0){
	for(int ls=0;ls<list.size();ls++){
	MatDispatch materialdispatch = (MatDispatch)list.get(ls);
	if(oid == materialdispatch.getOID())
	found=true;
	}
	}
	}
	if((start >= size) && (size > 0))
	start = start - recordToGet;
	return start;*/
	return 0;
    }

    public static int getDispatchRequestType(long fromLocId, long toLocId) {
	/*if(fromLocId!=0 && toLocId!=0){
	int dftype = -1;
	try{
	Location from = PstLocation.fetchExc(fromLocId);
	Location to = PstLocation.fetchExc(toLocId);
	switch(from.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE :
	switch(to.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_WH; break;
	case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_DEPARTMENT ; break;
	case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
	}
	break;
	case PstLocation.TYPE_LOCATION_OUTLET :
	switch(to.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_DEPARTMENT; break;
	case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatDispatch.TYPE_REQUEST_DEP_TO_DEP ; break;
	case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
	}
	break;
	case PstLocation.TYPE_LOCATION_SUB_CONTRACT :
	switch(to.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
	case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT ; break;
	//case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
	}
	break;
	}//end case 1
	}
	catch(Exception e){
	}
	return dftype;
	}*/

	return -1;
    }

    public static int getDispatchType(long fromLocId, long toLocId) {
	/*if(fromLocId!=0 && toLocId!=0){
	int dftype = -1;
	try{
	Location from = PstLocation.fetchExc(fromLocId);
	Location to = PstLocation.fetchExc(toLocId);
	switch(from.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE :
	switch(to.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatDispatch.TYPE_DISPATCH_WH_TO_WH; break;
	case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatDispatch.TYPE_DISPATCH_WH_TO_DEPARTMENT ; break;
	case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT; break;
	}
	break;
	case PstLocation.TYPE_LOCATION_OUTLET :
	switch(to.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatDispatch.TYPE_DISPATCH_WH_TO_DEPARTMENT; break;
	case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatDispatch.TYPE_DISPATCH_DEP_TO_DEP ; break;
	case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT; break;
	}
	break;
	case PstLocation.TYPE_LOCATION_SUB_CONTRACT :
	switch(to.getType()){
	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT; break;
	case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT ; break;
	//case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
	}
	break;
	}//end case 1
	}
	catch(Exception e){
	}
	return dftype;
	}*/

	return -1;
    }

    public static int countMaterialDispatch(int status, int docType) {
	DBResultSet dbrs = null;
	int result = 0;
	/*try{
	String sql = "SELECT COUNT("+PstMatDispatch.fieldNames[PstMatDispatch.FLD_MAT_DISPATCH_ID]+")"+
	" FROM "+PstMatDispatch.TBL_MAT_DISPATCH+" WHERE "+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DOC_TYPE]+
	" = "+docType+" AND "+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DF_STATUS]+" = "+status;
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	result = rs.getInt(1);
	}
	}
	catch(Exception e){
	System.out.println("Exception e : "+e.toString());
	}
	finally{
	DBResultSet.close(dbrs);
	}*/
	return result;
    }

    public static void getDataMaterial(SrcStockCard srcStockCard) {
	DBResultSet dbrs = null;
	try {
	    String sql = "SELECT" +
		    " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
		    " ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] +
                    " ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		    " ,SUM(RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SUM_QTY " +
		    " ,RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
		    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    //-- added by dewok-2017 for nilai berat
                    " ,SUM(RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_BERAT_CURRENT] + ") AS SUM_BERAT " +
                    //--
		    " FROM " + PstMatDispatch.TBL_DISPATCH + " AS R " +
		    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS RI " +
		    " ON R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		    " = RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
		    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
		    " ON RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
		    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
	    String whereClause = "";
	    if (srcStockCard.getMaterialId() != 0) {
		whereClause = "RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
	    }

	    if (srcStockCard.getLocationId() != 0) {
		if (whereClause.length() > 0) {
		    whereClause += " AND ";
		}
                if (srcStockCard.getWarehouseLocationId() != 0) {
                    whereClause += " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " IN (" + srcStockCard.getLocationId() + "," + srcStockCard.getWarehouseLocationId() + ")";
                } else {
		    whereClause += " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + srcStockCard.getLocationId();
		}
	    }

	    if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
		if (whereClause.length() > 0) {
		    whereClause += " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
		} else {
		    whereClause += " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
		}
	    } else { // data privous 
                //update opie-eyek 20161021
		if (whereClause.length() > 0) {
		    whereClause += " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
		} else {
		    whereClause += " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
		}
	    }

	    String strStatus = "";
	    if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
		for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
		    if (strStatus.length() != 0) {
			strStatus = strStatus + " OR " + "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
		    } else {
			strStatus = "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
		    }
		}
		strStatus = "(" + strStatus + ")";
	    }

	    if (strStatus.length() > 0) {
		if (whereClause.length() > 0) {
		    whereClause += " AND " + strStatus;
		} else {
		    whereClause = strStatus;
		}
	    }

	    if (whereClause.length() > 0) {
		sql += " WHERE " + whereClause;
	    }

	    sql = sql + " GROUP BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];
	    sql = sql + " ORDER BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];
	    //System.out.println("sql dispatch : "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		StockCardReport stockCardReport = new StockCardReport();

		Date date = DBHandler.convertDate(rs.getDate(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]), rs.getTime(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]));
		stockCardReport.setDate(date);

		stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_DF);
		stockCardReport.setDocCode(rs.getString(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]));
		stockCardReport.setTransaction_type(rs.getInt(PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE]));
		double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
		stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                //-- added by dewok-2017 for nilai berat
                stockCardReport.setBerat(rs.getDouble("SUM_BERAT"));
                stockCardReport.setLocationId(rs.getLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID]));
                //--
                if(srcStockCard.getLanguage()==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
                    switch (stockCardReport.getTransaction_type()) {
                        case PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_STORE:
                        case PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE:
                            stockCardReport.setKeterangan("Transfer out");
                            break;
                        case PstMatDispatch.FLD_TYPE_TRANSFER_UNIT:
                            stockCardReport.setKeterangan("Transfer unit out");
                            break;
                        case PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI:
                            stockCardReport.setKeterangan("Transfer produksi out");
                            break;
                        case PstMatDispatch.FLD_TYPE_TRANSFER_HPP:
                            stockCardReport.setKeterangan("Transfer HPP out");
                            break;
                        case PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR:
                            stockCardReport.setKeterangan("Transfer lebur out");
                            break;
                        default:
                            stockCardReport.setKeterangan("Transfer");
                    }
                }else{
                    stockCardReport.setKeterangan("Dispatch");
                }
                
		

		PstStockCardReport.insertExc(stockCardReport);
	    }

	} catch (Exception e) {
	    System.out.println("err getDataMaterial : " + e.toString());
	}
    }
    
     /**
     * fungsi ini di gunakan untuk mencari daftar transfer barang
     * di pakai untuk perhitungan reposting stock berdasarkan stock card.
     * by Mirahu
     * 20120803
     * @return
     */
     public static void getQtyStockMaterialReposting(SrcMaterialRepostingStock srcMaterialRepostingStock) {
	DBResultSet dbrs = null;
	try {
	    String sql = "SELECT" +
		    " SUM(RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SUM_QTY " +		    
		    " ,RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
		    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,SUM(RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_BERAT_CURRENT] + ") AS SUM_BERAT " +
		    " FROM " + PstMatDispatch.TBL_DISPATCH + " AS R " +
		    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS RI " +
		    " ON R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		    " = RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
		    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
		    " ON RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
		    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
	    String whereClause = "";
	    if (srcMaterialRepostingStock.getMaterialId() != 0) {
		whereClause = "RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
	    }

	    if (srcMaterialRepostingStock.getLocationId() != 0) {
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
		} else {
		    whereClause = "R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
		}
	    }

	    if (srcMaterialRepostingStock.getDateFrom() != null && srcMaterialRepostingStock.getDateTo() != null) {
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
		} else {
		    whereClause = " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
		}
	    } else { // data privous
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
		} else {
		    whereClause = " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
		}
	    }

	    String strStatus = "";
	    if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
		for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
		    if (strStatus.length() != 0) {
			strStatus = strStatus + " OR " + "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
		    } else {
			strStatus = "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
		    }
		}
		strStatus = "(" + strStatus + ")";
	    }

	    if (strStatus.length() > 0) {
		if (whereClause.length() > 0) {
		    whereClause += " AND " + strStatus;
		} else {
		    whereClause = strStatus;
		}
	    }

	    if (whereClause.length() > 0) {
		sql += " WHERE " + whereClause;
	    }

	    sql = sql + " GROUP BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    " ,RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID];
	    sql = sql + " ORDER BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];
	    System.out.println("sql dispatch : "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            double qtyMaterial = 0;
            double qtyAll = 0;
            //added by dewok 2018
            double beratAll = 0;
	    while (rs.next()) {
		//StockCardReport stockCardReport = new StockCardReport();

                //stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_DF);
		//double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
		//qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                beratAll += rs.getDouble("SUM_BERAT");
                srcMaterialRepostingStock.setQty(qtyAll);
                srcMaterialRepostingStock.setBerat(beratAll);
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
		//stockCardReport.setKeterangan("Pengiriman barang");

		//PstStockCardReport.insertExc(stockCardReport);
	    }
             if (qtyAll== 0){
                srcMaterialRepostingStock.setQty(0);
            }
            if (beratAll== 0){
                srcMaterialRepostingStock.setBerat(0);
            }

	} catch (Exception e) {
	    System.out.println("err getDataMaterial : " + e.toString());
	}
    }

    /**
     * fungsi ini di gunakan untuk mencari daftar transfer barang
     * di pakai untuk perhitungan stock pada saat opname.
     * by Mirahu
     * 20110803
     * @return
     */

    public static void getDataMaterialTime(SrcStockCard srcStockCard) {
	DBResultSet dbrs = null;
	try {
	    String sql = "SELECT" +
		    " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
		    " ,SUM(RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SUM_QTY " +
		    " ,RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
		    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
		    " FROM " + PstMatDispatch.TBL_DISPATCH + " AS R " +
		    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS RI " +
		    " ON R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		    " = RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
		    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
		    " ON RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
		    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
	    String whereClause = "";
	    if (srcStockCard.getMaterialId() != 0) {
		whereClause = "RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
	    }

	    if (srcStockCard.getLocationId() != 0) {
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
		} else {
		    whereClause = "R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
		}
	    }

	    if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
		} else {
		    whereClause = " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
		}
	    } else { // data privous
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
		} else {
		    whereClause = " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
		}
	    }

	    String strStatus = "";
	    if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
		for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
		    if (strStatus.length() != 0) {
			strStatus = strStatus + " OR " + "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
		    } else {
			strStatus = "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
		    }
		}
		strStatus = "(" + strStatus + ")";
	    }

	    if (strStatus.length() > 0) {
		if (whereClause.length() > 0) {
		    whereClause += " AND " + strStatus;
		} else {
		    whereClause = strStatus;
		}
	    }

	    if (whereClause.length() > 0) {
		sql += " WHERE " + whereClause;
	    }

	    //sql = sql + " GROUP BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    //" ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];
            sql = sql + " GROUP BY R." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID];
	    sql = sql + " ORDER BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];
	    //System.out.println("sql dispatch : "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		StockCardReport stockCardReport = new StockCardReport();

		Date date = DBHandler.convertDate(rs.getDate(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]), rs.getTime(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]));
		stockCardReport.setDate(date);

		stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_DF);
		stockCardReport.setDocCode(rs.getString(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]));
		//double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
		 double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
		stockCardReport.setKeterangan("Pengiriman barang");

		PstStockCardReport.insertExc(stockCardReport);
	    }

	} catch (Exception e) {
	    System.out.println("err getDataMaterial : " + e.toString());
	}
    }

    /**
     * fungsi ini di gunakan untuk mencari  mencari qy stok transfer barang
     * di pakai untuk perhitungan stock pada saat opname.
     * by Mirahu
     * 20110809
     * @return
     */

    public static void getQtyStockMaterial(SrcStockCard srcStockCard) {
	DBResultSet dbrs = null;
	try {
	    String sql = "SELECT" +
		    " SUM(RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SUM_QTY " +
		    " ,RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
		    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
		    " FROM " + PstMatDispatch.TBL_DISPATCH + " AS R " +
		    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS RI " +
		    " ON R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		    " = RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
		    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
		    " ON RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
		    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
	    String whereClause = "";
	    if (srcStockCard.getMaterialId() != 0) {
		whereClause = "RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
	    }

	    if (srcStockCard.getLocationId() != 0) {
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
		} else {
		    whereClause = "R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
		}
	    }

	    if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
		} else {
		    whereClause = " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
		}
	    } else { // data privous
		if (whereClause.length() > 0) {
		    whereClause = whereClause + " AND R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
		} else {
		    whereClause = " R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
		}
	    }

	    String strStatus = "";
	    if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
		for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
		    if (strStatus.length() != 0) {
			strStatus = strStatus + " OR " + "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
		    } else {
			strStatus = "(R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
		    }
		}
		strStatus = "(" + strStatus + ")";
	    }

	    if (strStatus.length() > 0) {
		if (whereClause.length() > 0) {
		    whereClause += " AND " + strStatus;
		} else {
		    whereClause = strStatus;
		}
	    }

	    if (whereClause.length() > 0) {
		sql += " WHERE " + whereClause;
	    }

	    sql = sql + " GROUP BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " ,R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    " ,RI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID];
	    sql = sql + " ORDER BY R." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE];
	    //System.out.println("sql dispatch : "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            double qtyMaterial = 0;
            double qtyAll = 0;
	    while (rs.next()) {
		//StockCardReport stockCardReport = new StockCardReport();

                //stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_DF);
		//double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
		//qtyMaterial = rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcStockCard.setQty(qtyAll);
                //stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
		//stockCardReport.setKeterangan("Pengiriman barang");

		//PstStockCardReport.insertExc(stockCardReport);
	    }
             if (qtyAll== 0){
                srcStockCard.setQty(0);
            }

	} catch (Exception e) {
	    System.out.println("err getDataMaterial : " + e.toString());
	}
        
    }



    /**
     * proses approve transaksi dengan barcode
     * @param list
     * @return
     */
    public static Vector approveDispatchProcess(Vector list) {
	Vector result = new Vector(1, 1);
	try {
	    if (list.size() > 0) {
		int docType = -1;
		try {
		    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
		    docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);
		} catch (Exception e) {
		    System.out.println("Exc : " + e.toString());
		}
		SessMatDispatch sessMatDispatch = new SessMatDispatch();
		for (int k = 0; k < list.size(); k++) {
		    Vector listdf = (Vector) list.get(k);
		    MatDispatch matDispatch = (MatDispatch) listdf.get(0);

		    int maxCounter = getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
		    maxCounter = maxCounter + 1;
		    matDispatch.setDispatchCodeCounter(maxCounter);
		    matDispatch.setDispatchCode(sessMatDispatch.generateDispatchCode(matDispatch));
		    matDispatch.setLast_update(new Date());

		    long oid = PstMatDispatch.insertExc(matDispatch);
		    try {
			matDispatch = PstMatDispatch.fetchExc(oid); 
			result.add(matDispatch);
		    } catch (Exception e) {
		    }

		    Vector listData = (Vector) listdf.get(1);
		    for (int j = 0; j < listData.size(); j++) {
			MatDispatchItem matDisaptchItem = (MatDispatchItem) listData.get(j);
			matDisaptchItem.setDispatchMaterialId(oid);
			try {
			    PstMatDispatchItem.insertExc(matDisaptchItem);
			} catch (Exception e) {
			}
		    }
		}
	    }
	} catch (Exception e) {
	}
	return result;
    }
}
