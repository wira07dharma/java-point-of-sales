package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.util.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.common.entity.location.*;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;
import com.dimata.posbo.entity.masterdata.*;

//adding import for reposting stok
import com.dimata.posbo.entity.search.*;

public class SessMatStockOpname {

    public static final String SESS_SRC_MATSTOCKOPNAME = "SESSION_SRC_MATSTOCKOPNAME";
    public static final String SESS_SRC_MATSTOCKCORRECTION = "SESSION_SRC_MATSTOCKCORRECTION";

   
    public static Vector searchMatStockOpname(SrcMatStockOpname srcmatstockopname, int start, int recordToGet) {
        return getListOpname(srcmatstockopname, start, recordToGet);
    }
    
    public static Vector searchMatStockOpnameLostList(SrcMatStockOpname srcmatstockopname, int start, int recordToGet) {
        return getListLostCorrectionStokPerlocationPercategory(srcmatstockopname, start, recordToGet);
    }

    public static Vector getListOpname(SrcMatStockOpname srcmatstockopname, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_REMARK] +
                    " ,LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_ETALASE_ID] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE] +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME +
                    " OPNAME INNER JOIN " + PstLocation.TBL_P2_LOCATION +
                    " LOC ON OPNAME.LOCATION_ID" +
                    " = LOC.LOCATION_ID";

            // Lokasi tidak boleh kosong !!!
            if (srcmatstockopname.getLocationId() != 0) {
                sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                        " = " + srcmatstockopname.getLocationId();
            }
	    
	    // untuk pencarian opname per number
            String strdatanumber = "";
	    if(srcmatstockopname.getOpnameNumber().length()>0 ) {
		Vector vectOpNumber = LogicParser.textSentence(srcmatstockopname.getOpnameNumber());
		if(vectOpNumber.size()>0){
		    for (int i = 0; i < vectOpNumber.size(); i++) {
			String name = (String) vectOpNumber.get(i);
			if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
			    vectOpNumber.remove(i);
		    }
		    for (int i = 0; i < vectOpNumber.size(); i++) {
			if(strdatanumber.length()>0){
			    strdatanumber = strdatanumber + " OR (OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
			    " LIKE '%" + srcmatstockopname.getLocationId()+"%')";
			}else{
			    strdatanumber = " (OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
			    " LIKE '%" + srcmatstockopname.getOpnameNumber()+"%')";
			}
		    }		    
		    strdatanumber = "("+strdatanumber+")";
		} 
	    }	    

            if (srcmatstockopname.getSupplierId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +
                        " = " + srcmatstockopname.getSupplierId();
            }

            if (srcmatstockopname.getCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                        " = " + srcmatstockopname.getCategoryId();
            }

            if (srcmatstockopname.getSubCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmatstockopname.getSubCategoryId();
            }

            if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
            }

           if(srcmatstockopname.getOpnameNumber().length()>0 ) {
               sql += " AND "+strdatanumber;
           }
            //if (srcmatstockopname.getStatus() != -1) {
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcmatstockopname.getStatus();
            //}

            //Status Document
            String strStatus = "";
                if (srcmatstockopname.getDocStatus() != null && srcmatstockopname.getDocStatus().size() > 0) {
                    for (int b = 0; b < srcmatstockopname.getDocStatus().size(); b++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        } else {
                            strStatus = "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        }
		}
		sql = sql +" AND (" + strStatus + ") ";
            }
            else {
                sql = sql +"";
            }
            //end of Document Status
                
                //added by dewok 2018
                if(srcmatstockopname.getEtalaseId() > 0 ) {
                    sql += " AND OPNAME."+ PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_ETALASE_ID] + "='"+srcmatstockopname.getEtalaseId()+"'";
                }
                if(srcmatstockopname.getOpnameItemType() == 3 ) {
                    sql += " AND OPNAME."+ PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE] + "='"+Material.MATERIAL_TYPE_EMAS_LANTAKAN+"'";
                } else {
                    sql += " AND OPNAME."+ PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE] + "!='"+Material.MATERIAL_TYPE_EMAS_LANTAKAN+"'";
                }
            
            String orderBy = "";
            
            if (srcmatstockopname.getSortBy()!=0){
                switch(srcmatstockopname.getSortBy()){
                    case 0:
                    break;
                    case 1:
                        orderBy = orderBy + " ORDER BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] + " DESC";
                    break;
                    case 2:
                        orderBy = orderBy + " ORDER BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " DESC";
                    break;
                    case 3:
                        orderBy = orderBy + " ORDER BY LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME] + " DESC";
                    break;
                    case 4:
                        orderBy = orderBy + " ORDER BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " DESC";
                    break;
                    case 5:
                        orderBy = orderBy + " ORDER BY LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME];
                    break;
                }
            }

            sql = sql + " " + orderBy;

            /*
            switch(srcmatstockopname.getSortBy())
            {
            case 0:
            break;
            case 1:
            sql = sql + " ORDER BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE];
            break;
            case 2:
            sql = sql + " ORDER BY LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME];
            break;
            }
             */

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

            //System.out.println("sql list > " + sql);
            System.out.println("sql opname list > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                Location location = new Location();

                matStockOpname.setOID(rs.getLong(1));
                matStockOpname.setStockOpnameDate(rs.getDate(2));
                matStockOpname.setStockOpnameNumber(rs.getString(3));
                matStockOpname.setRemark(rs.getString(4));
                matStockOpname.setStockOpnameStatus(rs.getInt(6));
                matStockOpname.setEtalaseId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_ETALASE_ID]));
                matStockOpname.setOpnameItemType(rs.getInt(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE]));
                vt.add(matStockOpname);

                location.setName(rs.getString(5));
                vt.add(location);

                result.add(vt);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //add counter
    public static Vector getListOpnameCounter(SrcMatStockOpname srcmatstockopname, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT DISTINCT OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_REMARK] +
                    " ,LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                    " ,OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_COUNTER] +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME +
                    " OPNAME INNER JOIN " + PstLocation.TBL_P2_LOCATION +
                    " LOC ON OPNAME.LOCATION_ID" +
                    " = LOC.LOCATION_ID";

            // Lokasi tidak boleh kosong !!!
            if (srcmatstockopname.getLocationId() != 0) {
                sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                        " = " + srcmatstockopname.getLocationId();
            }

	    // untuk pencarian opname per number
	    if(srcmatstockopname.getOpnameNumber().length()>0 ) {
		Vector vectOpNumber = LogicParser.textSentence(srcmatstockopname.getOpnameNumber());
		if(vectOpNumber.size()>0){
		    for (int i = 0; i < vectOpNumber.size(); i++) {
			String name = (String) vectOpNumber.get(i);
			if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
			    vectOpNumber.remove(i);
		    }

		    String strdatanumber = "";
		    for (int i = 0; i < vectOpNumber.size(); i++) {
			if(strdatanumber.length()>0){
			    strdatanumber = strdatanumber + " OR (OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
			    " LIKE '%" + srcmatstockopname.getLocationId()+"%')";
			}else{
			    strdatanumber = " (OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
			    " LIKE '%" + srcmatstockopname.getLocationId()+"%')";
			}
		    }
		    strdatanumber = "("+strdatanumber+")";
		}
	    }

            if (srcmatstockopname.getSupplierId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +
                        " = " + srcmatstockopname.getSupplierId();
            }

            if (srcmatstockopname.getCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                        " = " + srcmatstockopname.getCategoryId();
            }

            if (srcmatstockopname.getSubCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmatstockopname.getSubCategoryId();
            }

            if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
            }

            //if (srcmatstockopname.getStatus() != -1) {
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcmatstockopname.getStatus();
            //}

            //Status Document
            String strStatus = "";
                if (srcmatstockopname.getDocStatus() != null && srcmatstockopname.getDocStatus().size() > 0) {
                    for (int b = 0; b < srcmatstockopname.getDocStatus().size(); b++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        } else {
                            strStatus = "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        }
		}
		sql = sql +" AND (" + strStatus + ") ";
            }
            else {
                sql = sql +"";
            }
            //end of Document Status

            sql = sql + " ORDER BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];

            /*
            switch(srcmatstockopname.getSortBy())
            {
            case 0:
            break;
            case 1:
            sql = sql + " ORDER BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE];
            break;
            case 2:
            sql = sql + " ORDER BY LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME];
            break;
            }
             */

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

            //System.out.println("sql list > " + sql);
            System.out.println("sql opname list > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                Location location = new Location();

                matStockOpname.setOID(rs.getLong(1));
                matStockOpname.setStockOpnameDate(rs.getDate(2));
                matStockOpname.setStockOpnameNumber(rs.getString(3));
                matStockOpname.setRemark(rs.getString(4));
                matStockOpname.setStockOpnameStatus(rs.getInt(6));
                vt.add(matStockOpname);

                location.setName(rs.getString(5));
                vt.add(location);

                result.add(vt);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector getListLostCorrectionStokPerlocationPercategory(SrcMatStockOpname srcmatstockopname, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_REMARK] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]+
                    " ,LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    
                    " ,CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " ,CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " OPNAME" +
                    " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                    " ON OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]+
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+
                    //Ngambil CategoryId dari material
                    " LEFT JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " OPNITEM" +
                    " ON OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]+
                    " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+
                    " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CL " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]+
                    " = CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
                    
                    
                    //end of Ngambil CategoryId
                    //" LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    //" ON OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                    //" = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    //" WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                    //"= 7";



            //if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
            //}
            // Lokasi tidak boleh kosong !!!
            if (srcmatstockopname.getLocationId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                        " = " + srcmatstockopname.getLocationId();
            }
       
// opie-eyek 20160820
//            if (srcmatstockopname.getCategoryId() != 0) {
//                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
//                        //" = " + srcmatstockopname.getCategoryId();
//                sql += " AND CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
//                        " = " + srcmatstockopname.getCategoryId();
//            }

            //Status Document
            String strStatus = "";
                if (srcmatstockopname.getDocStatus() != null && srcmatstockopname.getDocStatus().size() > 0) {
                    for (int b = 0; b < srcmatstockopname.getDocStatus().size(); b++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        } else {
                            strStatus = "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        }
		}
		sql = sql +" AND (" + strStatus + ") ";
            }
            //end of Document Status

            if (srcmatstockopname.getSubCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmatstockopname.getSubCategoryId();
            }
            
            //hide opie-eyek 20160820
//            if (srcmatstockopname.getSupplierId() != 0) {
//                sql += " AND CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
//                        " = " + srcmatstockopname.getSupplierId();
//            }

            sql = sql + " GROUP BY LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                    //", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
            //sql = sql + " ORDER BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    //", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
              sql = sql + " ORDER BY LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE];

            /*
            switch(srcmatstockopname.getSortBy())
            {
            case 0:
            break;
            case 1:
            sql = sql + " ORDER BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE];
            break;
            case 2:
            sql = sql + " ORDER BY LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME];
            break;
            }
             */

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

            System.out.println("sql list koreksi stok perkategori perlokasi > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                Location location = new Location();
                Category category = new Category();
                ContactList contactList = new ContactList();
                
                
                matStockOpname.setOID(rs.getLong(1));
                matStockOpname.setStockOpnameDate(rs.getDate(2));
                matStockOpname.setStockOpnameNumber(rs.getString(3));
                matStockOpname.setRemark(rs.getString(4));
                matStockOpname.setLocationId(rs.getLong(5));
                matStockOpname.setStockOpnameStatus(rs.getInt(7));
                matStockOpname.setCategoryId(rs.getLong(8));
                vt.add(matStockOpname);

                location.setName(rs.getString(6));
                vt.add(location);

                category.setName(rs.getString(9));
                vt.add(category);
                
                /*
                " ,CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " ,CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                */
                contactList.setOID(rs.getLong("CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contactList.setCompName(rs.getString("CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                vt.add(contactList);
                
                result.add(vt);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static Vector getListLostCorrectionStokPercategory(SrcMatStockOpname srcmatstockopname, int start, int recordToGet, long locationOpname) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_REMARK] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]+
                    " ,LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                    " ,OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    //id category dari pos_material
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+
                    " ,CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " ,CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " OPNAME" +
                    " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                    " ON OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]+
                    " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+
                     //Ngambil CategoryId dari material
                    " LEFT JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " OPNITEM" +
                    " ON OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]+
                    " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+
                    " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CL " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]+
                    " = CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
                    
            
                    //end of Ngambil CategoryId
                    //" LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    //" ON OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                    //" = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+
                    //" WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                    //"= 7";

            //if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                //sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
            //}
            // Lokasi tidak boleh kosong !!!
            if (srcmatstockopname.getLocationId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                        " = " + srcmatstockopname.getLocationId();
            }
            else
                sql += "AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                       " = " + locationOpname;

            if (srcmatstockopname.getCategoryId() != 0) {
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                       // " = " + srcmatstockopname.getCategoryId();
                sql += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcmatstockopname.getCategoryId();
            }

            //Status Document
            String strStatus = "";
                if (srcmatstockopname.getDocStatus() != null && srcmatstockopname.getDocStatus().size() > 0) {
                    for (int b = 0; b < srcmatstockopname.getDocStatus().size(); b++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        } else {
                            strStatus = "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        }
		}
		sql = sql +" AND (" + strStatus + ") ";
            }
            //end of Document Status

            if (srcmatstockopname.getSubCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmatstockopname.getSubCategoryId();
            }
            
             if (srcmatstockopname.getSupplierId() != 0) {
                sql += " AND CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                        " = " + srcmatstockopname.getSupplierId();
            }

            //sql = sql + " GROUP BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
              //sql = sql + " GROUP BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE];
              //sql = sql + " GROUP BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID];
            
            if (srcmatstockopname.getGroupBy()==SrcSaleReport.GROUP_BY_CATEGORY) {
                sql = sql + " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID];
                sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE];
            }else{
                sql = sql + " GROUP BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
                sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" ASC ";
            }  
            
            
            
            //sql = sql + " ORDER BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    //", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
             
                    //", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];

            /*
            switch(srcmatstockopname.getSortBy())
            {
            case 0:
            break;
            case 1:
            sql = sql + " ORDER BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE];
            break;
            case 2:
            sql = sql + " ORDER BY LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME];
            break;
            }
             */

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

            System.out.println("sql list koreksi stok perkategori> " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                Location location = new Location();
                Category category = new Category();
                Material material = new Material();

                matStockOpname.setOID(rs.getLong(1));
                matStockOpname.setStockOpnameDate(rs.getDate(2));
                matStockOpname.setStockOpnameNumber(rs.getString(3));
                matStockOpname.setRemark(rs.getString(4));
                matStockOpname.setLocationId(rs.getLong(5));
                matStockOpname.setStockOpnameStatus(rs.getInt(7));
                matStockOpname.setCategoryId(rs.getLong(8));
                vt.add(matStockOpname);

                location.setName(rs.getString(6));
                vt.add(location);

                category.setName(rs.getString(9));
                vt.add(category);
                
                material.setCategoryId(rs.getLong(10));
                vt.add(material);

                ContactList contactList = new ContactList();
                contactList.setOID(rs.getLong("CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contactList.setCompName(rs.getString("CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                vt.add(contactList);

                result.add(vt);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    //list for report lost correction stock
    public static Vector getListLostCorrectionStok(SrcMatStockOpname srcmatstockopname, int start, int recordToGet, long locationOpname, long categoryOpname) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                         ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                         ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    
                         " ,CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                         " ,CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    
                         " FROM ((((" + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " SOI" +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID] +
                         " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " ) LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                         " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                         " ) INNER JOIN " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " OPNAME" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                         " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                         ")" +
                    
                         //add opie-eyek 
                        " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CL " +
                        " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]+
                        " = CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]

                         ;
                         //" WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                         //"= 7";

           // if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                //sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
           // }

            // Lokasi tidak boleh kosong !!!
            if (srcmatstockopname.getLocationId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                        " = " + srcmatstockopname.getLocationId();
            }
            else
                sql+= " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                      " = " + locationOpname;

//            if (srcmatstockopname.getSupplierId() != 0) {
//                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +
//                        " = " + srcmatstockopname.getSupplierId();
//            }

            //if (srcmatstockopname.getCategoryId() != 0) {
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                        //" = " + srcmatstockopname.getCategoryId();
            if (srcmatstockopname.getCategoryId() != 0) {
                sql += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +" = " + srcmatstockopname.getCategoryId();
            }
            
            if (srcmatstockopname.getSupplierId() != 0) {
                sql += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +" = " + srcmatstockopname.getSupplierId();
            }
//            else{
//                if(srcmatstockopname.getGroupBy()==SrcSaleReport.GROUP_BY_CATEGORY){
//                    sql+= " AND MAT." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +" = " + categoryOpname;
//                }else{
//                    sql+= " AND MAT." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +" = " + categoryOpname;
//                }
//            }
            
            if(srcmatstockopname.getGroupBy()==SrcSaleReport.GROUP_BY_CATEGORY){
                sql+= " AND MAT." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +" = " + categoryOpname;
            }else{
                sql+= " AND MAT." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +" = " + categoryOpname;
            }

             //Status Document
            String strStatus = "";
                if (srcmatstockopname.getDocStatus() != null && srcmatstockopname.getDocStatus().size() > 0) {
                    for (int b = 0; b < srcmatstockopname.getDocStatus().size(); b++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        } else {
                            strStatus = "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        }
		}
		sql = sql +" AND (" + strStatus + ") ";
            }
            //end of Document Status

            if (srcmatstockopname.getSubCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmatstockopname.getSubCategoryId();
            }

            

            //sql = sql + " ORDER BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                   // ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
              sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

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

            System.out.println("sql list > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                MatStockOpnameItem matStockOpnameItem =new MatStockOpnameItem();
                Material material = new Material();
                Unit unit = new Unit();
                Category category = new Category();
                
                matStockOpname.setOID(rs.getLong(1));
                matStockOpname.setLocationId(rs.getLong(17));
                //matStockOpname.setStockOpnameDate(rs.getDate(18));
                matStockOpname.setStockOpnameDate(rs.getTimestamp(18));
                matStockOpname.setStockOpnameNumber(rs.getString(19));
                matStockOpname.setStockOpnameStatus(rs.getInt(20));
                vt.add(matStockOpname);//0

                matStockOpnameItem.setOID(rs.getLong(1));
                matStockOpnameItem.setQtyOpname(rs.getDouble(6));
                matStockOpnameItem.setQtySold(rs.getDouble(7));
                matStockOpnameItem.setQtySystem(rs.getDouble(8));
                matStockOpnameItem.setCost(rs.getDouble(9));
                matStockOpnameItem.setMaterialId(rs.getLong(10));
                matStockOpnameItem.setUnitId(rs.getLong(11));
                matStockOpnameItem.setPrice(rs.getDouble(12));
                vt.add(matStockOpnameItem);//1

                material.setSku(rs.getString(2));
                material.setName(rs.getString(3));
                material.setDefaultPrice(rs.getDouble(13));
                material.setRequiredSerialNumber(rs.getInt(14));
                material.setAveragePrice(rs.getDouble(15));
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                
                vt.add(material);//2

                unit.setCode(rs.getString(4));
                vt.add(unit);//3

                category.setName(rs.getString(5));
                vt.add(category);//4
                
                

                ContactList contactList = new ContactList();
                contactList.setOID(rs.getLong("CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contactList.setCompName(rs.getString("CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                vt.add(contactList);//5
                
                result.add(vt);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //Grand Total selisih list lost opname
     //by Mirahu
    //6 Juni 2011
    public static Vector grandTotalLostCorrectionStok(SrcMatStockOpname srcmatstockopname, int start, int recordToGet, long locationOpname, long categoryOpname) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + " AS QTY_OPNAME " +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD] + " AS QTY_SOLD " +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM] + " AS QTY_SYSTEM " +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST] + " AS COST " +
                         ", SUM(SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + " ) AS SUM_QTY_OPNAME " +
                         ", SUM(SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD] + " ) AS SUM_QTY_SOLD " +
                         ", SUM(SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM] + " ) AS SUM_QTY_SYSTEM " +
                         ", SUM(SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST] + " ) AS SUM_COST " +
                         ", SUM((SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] +
                         "+ SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD] +
                         ")- SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM] + ") AS SUM_LOST " +
                         ", SUM(((SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] +
                         "+ SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD] +
                         ")- SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM] + ")* " +
                         "SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST] +  ") AS SUM_LOST_VALUE " +
                         " FROM ((((" + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " SOI" +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID] +
                         " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " ) LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                         " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                         " ) INNER JOIN " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " OPNAME" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                         " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                         ")" ;
                         //" WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                         //"= 7";

           // if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                //sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
           // }

            // Lokasi tidak boleh kosong !!!
            if (srcmatstockopname.getLocationId() != 0) {
                sql+= " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                      " = " + locationOpname;
            }

            if (srcmatstockopname.getSupplierId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +
                        " = " + srcmatstockopname.getSupplierId();
            }

              if (srcmatstockopname.getCategoryId() != 0) {

                sql+= " AND MAT." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                      " = " + categoryOpname;
            }

             //Status Document
            String strStatus = "";
                if (srcmatstockopname.getDocStatus() != null && srcmatstockopname.getDocStatus().size() > 0) {
                    for (int b = 0; b < srcmatstockopname.getDocStatus().size(); b++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        } else {
                            strStatus = "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        }
		}
		sql = sql +" AND (" + strStatus + ") ";
            }
            //end of Document Status

            if (srcmatstockopname.getSubCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmatstockopname.getSubCategoryId();
            }



            //sql = sql + " ORDER BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                   // ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
              sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

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

            System.out.println("sql list > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                 Vector temp = new Vector();

                temp.add(rs.getDouble("SUM_QTY_OPNAME"));
                temp.add(rs.getDouble("SUM_QTY_SOLD"));
                temp.add(rs.getDouble("SUM_QTY_SYSTEM"));
                temp.add(rs.getDouble("SUM_COST"));

                temp.add(rs.getDouble("SUM_LOST"));
                temp.add(rs.getDouble("SUM_LOST_VALUE"));


                result.add(temp);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    // end of grand total

    public static Vector getListLostCorrectionStok2(SrcMatStockOpname srcmatstockopname, int start, int recordToGet, long oidLocation) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = " SELECT SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                         ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                         ", CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + // ", SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID] +
                         ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                         ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                         " FROM (((((" + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " SOI" +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID] +
                         " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " ) LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                         " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                         " ) INNER JOIN " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " OPNAME" +
                         " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                         " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                         ") INNER JOIN " + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " LOC" +
                         " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
                         " ) WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                         "= 7" +
                         " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                         "= " + oidLocation;

           // if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
           // }

            // Lokasi tidak boleh kosong !!!
           //if (srcmatstockopname.getLocationId() != 0) {
               // sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                       // " = " + srcmatstockopname.getLocationId();
            //}

           // if (srcmatstockopname.getSupplierId() != 0) {
               //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +
                      //  " = " + srcmatstockopname.getSupplierId();
           // }

           // if (srcmatstockopname.getCategoryId() != 0) {
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                      // " = " + srcmatstockopname.getCategoryId();
          //  }

           // if (srcmatstockopname.getSubCategoryId() != 0) {
               // sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                      //  " = " + srcmatstockopname.getSubCategoryId();
          //  }



            sql = sql + " ORDER BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    ", OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];

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

            System.out.println("sql list > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                MatStockOpnameItem matStockOpnameItem =new MatStockOpnameItem();
                Material material = new Material();
                Unit unit = new Unit();
                Category category = new Category();

                matStockOpname.setOID(rs.getLong(1));
                matStockOpname.setLocationId(rs.getLong(17));
                matStockOpname.setStockOpnameDate(rs.getDate(18));
                matStockOpname.setStockOpnameNumber(rs.getString(19));
                matStockOpname.setStockOpnameStatus(rs.getInt(20));
                vt.add(matStockOpname);

                matStockOpnameItem.setOID(rs.getLong(1));
                matStockOpnameItem.setQtyOpname(rs.getDouble(6));
                matStockOpnameItem.setQtySold(rs.getDouble(7));
                matStockOpnameItem.setQtySystem(rs.getDouble(8));
                matStockOpnameItem.setCost(rs.getDouble(9));
                matStockOpnameItem.setMaterialId(rs.getLong(10));
                matStockOpnameItem.setUnitId(rs.getLong(11));
                matStockOpnameItem.setPrice(rs.getDouble(12));
                vt.add(matStockOpnameItem);

                material.setSku(rs.getString(2));
                material.setName(rs.getString(3));
                material.setDefaultPrice(rs.getDouble(13));
                material.setRequiredSerialNumber(rs.getInt(14));
                material.setAveragePrice(rs.getDouble(15));
                vt.add(material);

                unit.setCode(rs.getString(4));
                vt.add(unit);

                category.setName(rs.getString(5));
                vt.add(category);

                result.add(vt);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }


    public static int getCountSearch(SrcMatStockOpname srcmatstockopname) {
        return getCountListOpname(srcmatstockopname);
    }

    
    public static int getCountListOpname(SrcMatStockOpname srcmatstockopname) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " ) AS CNT" +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME +
                    " OPNAME INNER JOIN " + PstLocation.TBL_P2_LOCATION +
                    " LOC ON OPNAME.LOCATION_ID" +
                    " = LOC.LOCATION_ID";

            //Lokasi tidak boleh kosong !!!
            if (srcmatstockopname.getLocationId() != 0) {
                sql += " WHERE OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                        " = " + srcmatstockopname.getLocationId();
            }  
 
	    // untuk pencarian opname per number
            String strdatanumber = "";
	    if(srcmatstockopname.getOpnameNumber().length()>0 ) {
		Vector vectOpNumber = LogicParser.textSentence(srcmatstockopname.getOpnameNumber());
		if(vectOpNumber.size()>0){
		    for (int i = 0; i < vectOpNumber.size(); i++) {
			String name = (String) vectOpNumber.get(i);
			if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
			    vectOpNumber.remove(i);
		    }
		    
		    
		    for (int i = 0; i < vectOpNumber.size(); i++) {
			if(strdatanumber.length()>0){
			    strdatanumber = strdatanumber + " OR (OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
			    " LIKE '%" + srcmatstockopname.getLocationId()+"%')";
			}else{
			    strdatanumber = " (OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
			    " LIKE '%" +srcmatstockopname.getOpnameNumber()+"%')";
			}
		    }		    
		    strdatanumber = "("+strdatanumber+")";
		} 
	    }	    
	    
            if (srcmatstockopname.getSupplierId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID] +
                        " = " + srcmatstockopname.getSupplierId();
            }

            if (srcmatstockopname.getCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID] +
                        " = " + srcmatstockopname.getCategoryId();
            }

            if (srcmatstockopname.getSubCategoryId() != 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmatstockopname.getSubCategoryId();
            }

            if (srcmatstockopname.getStatusDate() != 0) {
                String strfromDate = Formater.formatDate(srcmatstockopname.getFromDate(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcmatstockopname.getToDate(), "yyyy-MM-dd 23:59:59");
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
            }

            //if (srcmatstockopname.getStatus() >= 0) {
                //sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcmatstockopname.getStatus();
           // }
           if(srcmatstockopname.getOpnameNumber().length()>0 ) {
               sql += " AND "+strdatanumber;
           }
            //Status Document
            String strStatus = "";
                if (srcmatstockopname.getDocStatus() != null && srcmatstockopname.getDocStatus().size() > 0) {
                    for (int b = 0; b < srcmatstockopname.getDocStatus().size(); b++) {
                        if (strStatus.length() != 0) {
                            strStatus = strStatus + " OR " + "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        } else {
                            strStatus = "(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " =" + srcmatstockopname.getDocStatus().get(b) + ")";
                        }
		}
		sql = sql +" AND (" + strStatus + ") ";
            }
            else {
                sql = sql +"";
            }
              //end of Document Status
                
            //added by dewok 2018
            if (srcmatstockopname.getEtalaseId() > 0) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_ETALASE_ID] + "='" + srcmatstockopname.getEtalaseId() + "'";
            }
            if (srcmatstockopname.getOpnameItemType() == 2) {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE] + "='" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
            } else {
                sql += " AND OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE] + "!='" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
            }
                
            switch (srcmatstockopname.getSortBy()) {
                case 0:
                    break;
                case 1:
                    sql = sql + " ORDER BY OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE];
                    break;
                case 2:
                    sql = sql + " ORDER BY LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                    break;
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


    // add by fitra
    
    
    public static Vector getCountListOpnameHome(String whereClause) {
        DBResultSet dbrs = null;
        Vector vListReceive = new Vector(1, 1);
        
        try {
            String sql = "SELECT COUNT(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " ) AS CNT, " +
                    " ( OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+ ") AS STATUS " +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME +
                    " OPNAME INNER JOIN " + PstLocation.TBL_P2_LOCATION +
                    " LC ON OPNAME.LOCATION_ID" +
                    " = LC.LOCATION_ID ";
                    

            String strStatus = "";
              
              strStatus = "((OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+ " = 0)"+ 
                           " OR " + "(OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+ " = 1 )"+ 
                           " OR " + "(OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+ " = 2 )"+ 
                           " OR " + "(OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+ " = 10 ))";  
              
              
             if(whereClause.length() > 0){
               strStatus=strStatus + whereClause; 
              }
             
              strStatus = strStatus + "GROUP BY OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS];
          
               sql = sql + " WHERE " + strStatus;
          

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

    public static int getListOpnameInformation(String where) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +" ) AS CNT" +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME +
                    " OPNAME INNER JOIN " + PstLocation.TBL_P2_LOCATION +
                    " LC ON OPNAME.LOCATION_ID" +
                    " = LC.LOCATION_ID" +
                    " AND ((OPNAME."+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+"=2))";

            //Lokasi tidak boleh kosong !!!
            /**
             * SELECT COUNT(OPNAME.STOCK_OPNAME_ID ) AS CNT FROM pos_stock_opname OPNAME INNER JOIN location LOC ON OPNAME.LOCATION_ID = LOC.LOCATION_ID
             * AND ((OPNAME.STOCK_OPNAME_STATUS =2))
             */
            
            if(where.length()>0){
               sql=sql + where;
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

    //count for counter
    public static int getCountCounterList(long stockOpnameId) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_COUNTER] +
                    " ) AS CNT" +
                    " FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " OPNITEM "+
                    " INNER JOIN " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " OPNAME "+
                    " ON OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " WHERE OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + stockOpnameId +
                    " GROUP BY OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_COUNTER] +
                    " ORDER BY CNT ASC";

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
    //end of count counter

     //count for counter
    public static int getCountSameMaterialList(long stockOpnameId) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                    " ) AS CNT" +
                    " FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " OPNITEM "+
                    " INNER JOIN " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " OPNAME "+
                    " ON OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = OPNAME." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " WHERE OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + stockOpnameId +
                    " GROUP BY OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] +
                    " ORDER BY CNT ASC";

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
    //end of count counter


   

    public static Vector getListOpnameMaterial(int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        /*try{
        String sql = "SELECT "+
        " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ //MATERIAL_ID
        " ,MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+ // CODE
        " ,MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ //NAME
        " ,MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_GROUP_ID]+ //GROUP
        " ,GRP."+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_CODE]+" AS GROUP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_CODE]+ //CODE
        " ,GRP."+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_NAME]+" AS GROUP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_NAME]+//NAME
        " ,IF(ISNULL(MRK."+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_CODE]+"),\"\",MRK."+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_CODE]+
        ") AS "+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_CODE]+ //MERK_CODE
        " ,IF(ISNULL(MRK."+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_NAME]+"),\"\",MRK."+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_NAME]+
        ") AS "+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_NAME]+ //MERK_NAME
        " FROM "+PstMaterial.TBL_MATERIAL+" AS MAT "+ // MAT_MATERIAL
        " LEFT JOIN "+PstMaterialGroup.TBL_MATERIAL_GROUP+" AS GRP"+ //GROUP
        " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_GROUP_ID]+" = GRP."+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_MATERIAL_GROUP_ID]+
        " LEFT JOIN "+PstMatMerk.TBL_MAT_MERK+" AS MRK ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID]+" = MRK."+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_ID]+
        " GROUP BY "+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+
        " ORDER BY MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_GROUP_ID];
        switch (DBHandler.DBSVR_TYPE) {
        case DBHandler.DBSVR_MYSQL :
        if(start == 0 && recordToGet == 0)
        sql = sql + "";
        else
        sql = sql + " LIMIT " + start + ","+ recordToGet ;
        break;
        case DBHandler.DBSVR_POSTGRESQL :
        if(start == 0 && recordToGet == 0)
        sql = sql + "";
        else
        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ start ;
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
        // System.out.println("---------- > sql : "+sql);
        dbrs = DBHandler.execQueryResult(sql);
        ResultSet rs = dbrs.getResultSet();
        while(rs.next()){
        Vector vt = new Vector(1,1);
        Material material = new Material();
        MatMerk matMerk = new MatMerk();
        MaterialGroup matGroup = new MaterialGroup();
        material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
        material.setCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_CODE]));
        material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
        material.setMaterialGroupId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_GROUP_ID]));
        vt.add(material);
        matGroup.setCode(rs.getString("GROUP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_CODE]));
        matGroup.setName(rs.getString("GROUP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_NAME]));
        vt.add(matGroup);
        matMerk.setMerkCode(rs.getString(PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_CODE]));
        matMerk.setMerkName(rs.getString(PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_NAME]));
        vt.add(matMerk);
        result.add(vt);
        }
        rs.close();
        }catch(Exception e){
        System.out.println("Err : "+e.toString());
        }finally{
        DBResultSet.close(dbrs);
        }*/
        return result;
    }

    public static int getCountListOpnameMaterial() {
        DBResultSet dbrs = null;
        int count = 0;
        /*try{
        String sql = "SELECT COUNT("+
        "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ //MATERIAL_ID
        ") AS CNT "+
        " FROM "+PstMaterial.TBL_MATERIAL+" AS MAT "+ // MAT_MATERIAL
        " LEFT JOIN "+PstMaterialGroup.TBL_MATERIAL_GROUP+" AS GRP"+ //GROUP
        " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_GROUP_ID]+" = GRP."+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_MATERIAL_GROUP_ID]+
        " LEFT JOIN "+PstMatMerk.TBL_MAT_MERK+" AS MRK ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID]+" = MRK."+PstMatMerk.fieldNames[PstMatMerk.FLD_MERK_ID];
        // System.out.println("------------ > sql : "+sql);
        dbrs = DBHandler.execQueryResult(sql);
        ResultSet rs = dbrs.getResultSet();
        while(rs.next()){
        count = rs.getInt("CNT");
        }
        rs.close();
        }catch(Exception e){
        System.out.println("Err : "+e.toString());
        }finally{
        DBResultSet.close(dbrs);
        }*/
        return count;
    }


    /*	public static Vector getVectorSess(Vector vtItem, Vector vtSess, int recordToGet){
    try{
    int start = 0;
    for(int i=0; i<vtSess.size();i++){
    //  System.out.println("start : "+start);
    //Vector listMcdStockOpnameItem = SessMcdStockOpname.getListStockOpnameItem(start,recordToGet);
    Vector listMatStockOpnameDetail = SessMatStockOpname.getListOpnameMaterial(start,recordToGet);
    //  System.out.println("listMatStockOpnameDetail > : "+listMatStockOpnameDetail);
    Vector vectOpname = new Vector(1,1);
    if(listMatStockOpnameDetail!= null && listMatStockOpnameDetail.size()>0){
    for(int a=0;a<listMatStockOpnameDetail.size();a++){
    Vector vt = (Vector)listMatStockOpnameDetail.get(a);
    Material material = (Material)vt.get(0);
    // System.out.println("oid : ");
    for(int p=0;p<vtItem.size();p++){
    //System.out.println("oid ...: ");
    MatStockOpnameDetail matStockOpnameDetail = (MatStockOpnameDetail)vtItem.get(p);
    //System.out.println("oid : "+matStockOpnameDetail.getMaterialId());
    if(matStockOpnameDetail.getMaterialId()== material.getOID()){
    //MatStockOpnameDetail matOpnamedetail = new MatStockOpnameDetail();
    //matOpnamedetail.setMaterialId(material.getOID());
    //matOpnamedetail.setRealQty(matStockOpnameDetail.getRealQty());
    //matOpnamedetail.setShouldQty(matStockOpnameDetail.getShouldQty());
    vectOpname.add(matStockOpnameDetail);
    //break;
    }
    }
    }
    }
    vtSess.setElementAt(vectOpname,i);
    start = start + recordToGet;
    }
    }catch(Exception e){
    System.out.println("Err : "+e.toString());
    }
    return vtSess;
    }
    public static boolean cekOpnameNotPosted(long oidLocation, int status){
    boolean bool = false;
    String whereClause = ""+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]+"="+oidLocation+
    " AND "+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_STATUS]+"<>"+status;
    Vector vt = PstMatStockOpname.list(0,0,whereClause,"");
    if(vt!=null && vt.size()>0){
    bool = true;
    }
    return bool;
    }
     */
    public static void getDataMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] +
                    " ,SUM(RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + ") AS SUM_QTY " +
					" ,SUM(RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_OPNAME] + ") AS SUM_BERAT " +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " AS R " +
                    " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " AS RI " +
                    " ON R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " = RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND ";
                }
                if (srcStockCard.getWarehouseLocationId() != 0) {
                    whereClause += " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + " IN (" + srcStockCard.getLocationId() + "," + srcStockCard.getWarehouseLocationId() + ")";
                } else {
                    whereClause += " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + " = " + srcStockCard.getLocationId();
                }
            }

            if ((srcStockCard.getStardDate() != null) && (srcStockCard.getEndDate() != null)) {
                if (whereClause.length() > 0) {
                    whereClause += " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause += " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                }
            } else {
                //update opie-eyek 20161021
                if (whereClause.length() > 0) {
                    whereClause += " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                    //whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause += " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                    //whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
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

            sql = sql + " GROUP BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
            sql = sql + " ORDER BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];

            //System.out.println("sql receive : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = DBHandler.convertDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]), rs.getTime(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
                stockCardReport.setDate(date);

                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_OPN);
                stockCardReport.setDocCode(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY"));
                stockCardReport.setBerat(rs.getDouble("SUM_BERAT"));
                stockCardReport.setLocationId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]));
                
                if(srcStockCard.getLanguage()==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
                    stockCardReport.setKeterangan("Opname");
                }else{
                    stockCardReport.setKeterangan("Opname");
                }

                PstStockCardReport.insertExc(stockCardReport);
            }

        } catch (Exception e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
    }

    
    /**
     * fungsi ini di gunakan untuk mencari daftar opname barang
     * di pakai untuk perhitungan reposting stock berdasarkan kartu stok.
     * by Mirahu
     * 20120803
     * @return
     */
    
    public static void getDataMaterialReposting(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    " ,SUM(RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + ") AS SUM_QTY " +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " AS R " +
                    " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " AS RI " +
                    " ON R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " = RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID];
            String whereClause = "";
            if (srcMaterialRepostingStock.getMaterialId() != 0) {
                whereClause = "RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
            }

            if (srcMaterialRepostingStock.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                } else {
                    whereClause = "R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                }
            }

            if ((srcMaterialRepostingStock.getDateFrom() != null) && (srcMaterialRepostingStock.getDateTo() != null)) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
                    //whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
                    //whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }

            String strStatus = "";
            if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
                for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
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

            sql = sql + " GROUP BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
            sql = sql + " ORDER BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];

            //System.out.println("sql receive : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = DBHandler.convertDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]), rs.getTime(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
                stockCardReport.setDate(date);

                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_OPN);
                stockCardReport.setDocCode(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY"));
                stockCardReport.setKeterangan("Opname Barang");

                PstStockCardReport.insertExc(stockCardReport);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
    }
    
    /**
     * fungsi ini di gunakan untuk mencari daftar opname barang
     * di pakai untuk perhitungan stock pada saat opname.
     * by Mirahu
     * 20110803
     * @return
     */

    public static void getDataMaterialTime(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    " ,SUM(RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + ") AS SUM_QTY " +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " AS R " +
                    " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " AS RI " +
                    " ON R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " = RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = "R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if ((srcStockCard.getStardDate() != null) && (srcStockCard.getEndDate() != null)) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                    //whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                    //whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
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

            sql = sql + " GROUP BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];
            sql = sql + " ORDER BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];

            //System.out.println("sql receive : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                Date date = DBHandler.convertDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]), rs.getTime(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
                stockCardReport.setDate(date);

                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_OPN);
                stockCardReport.setDocCode(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY"));
                stockCardReport.setKeterangan("Opname Barang");

                PstStockCardReport.insertExc(stockCardReport);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
    }

     /**
     * fungsi ini di gunakan untuk mencari qy stokdaftar opname barang
     * di pakai untuk perhitungan stock pada saat opname.
     * by Mirahu
     * 20110803
     * @return
     */

    public static void getQtyStockMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " SUM(RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + ") AS SUM_QTY " +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " AS R " +
                    " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " AS RI " +
                    " ON R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " = RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = "R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if ((srcStockCard.getStardDate() != null) && (srcStockCard.getEndDate() != null)) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            } else {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                    //whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                    //whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " >= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
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

            sql = sql + " GROUP BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " ,R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]+
                    " ,RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER];

            //System.out.println("sql receive : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double qtyMaterial = 0;
            double qtyAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();

                //stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_OPN);
                qtyMaterial = rs.getDouble("SUM_QTY");
                //stockCardReport.setQty(rs.getDouble("SUM_QTY"));
               
                //PstStockCardReport.insertExc(stockCardReport);
            }
            qtyAll += qtyMaterial;
            srcStockCard.setQty(qtyAll);

        } catch (Exception e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
        
    }


    public static Date getLastDataMaterial(SrcStockCard srcStockCard) {
        Date opDate = new Date(srcStockCard.getStardDate().getTime());
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MAX(" +
                    " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + ") AS MAX_DATE " +
                    " FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " AS R " +
                    " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " AS RI " +
                    " ON R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] +
                    " = RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = "R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " < '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "'";
                } else {
                    whereClause = " R." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " < '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "'";
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                opDate = rs.getDate("MAX_DATE");
            }
        } catch (Exception e) {
            System.out.println("err getLastDataMaterial : " + e.toString());
        }
        return opDate;
    }

    public static Vector approveOpnameProcessX(Vector list) {
        Vector result = new Vector(1, 1);
        try {
            if (list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector listopname = (Vector) list.get(k);
                    MatStockOpname matStockOpname = (MatStockOpname) listopname.get(0);
                    long oid = PstMatStockOpname.insertExc(matStockOpname);
                    try {
                        matStockOpname = PstMatStockOpname.fetchExc(oid);
                        result.add(matStockOpname);
                    } catch (Exception e) { 
                    }
                    Vector listData = (Vector) listopname.get(1);
                    for (int j = 0; j < listData.size(); j++) {
                        MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem) listData.get(j);
                        matStockOpnameItem.setStockOpnameId(oid);
                        try {
                            PstMatStockOpnameItem.insertExc(matStockOpnameItem);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }


      public static Vector approveOpnameProcess(Vector list) {
        Vector result = new Vector(1, 1);
        try {
            if (list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    MatStockOpname matStockOpname = (MatStockOpname) list.get(k);
                    long oid = 0;
                    if(matStockOpname.getSizeListOpnameItem()>0){
                       matStockOpname.setStockOpnameNumber(PstMatStockOpname.generateStockOpnameNumber(matStockOpname));
                       oid = PstMatStockOpname.insertExc(matStockOpname);
                    }
                    try {
                        //PstMatStockOpname.insertExc(matStockOpname);
                        //matStockOpname = PstMatStockOpname.fetchExc(oid);
                        result.add(matStockOpname);
                    } catch (Exception e) {
                    }
                    
                    for (int j = 0; j < matStockOpname.getSizeListOpnameItem(); j++) {
                        //MatStockOpnameItem matStockOpnameItem = matStockOpname.getListOpnameItem(j);
                        MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem)matStockOpname.getListOpnameItem().get(j);
                        matStockOpnameItem.setStockOpnameId(oid);
                        try {
                            PstMatStockOpnameItem.insertExc(matStockOpnameItem);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static void main(String[] args) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT M.MATERIAL_ID" +
                    " ,M.DEFAULT_SELL_UNIT_ID " +
                    " ,BRG.QTY " +
                    " ,M.DEFAULT_COST " +
                    " ,M.DEFAULT_PRICE " +
                    " FROM POS_MATERIAL AS M " +
                    " INNER JOIN BRG ON M.MATERIAL_ID = BRG.OID " +
                    " WHERE M.CATEGORY_ID = 6";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int i = 0;
            while (rs.next()) {
                i++;
                MatStockOpnameItem matStockOpnameItem = new MatStockOpnameItem();

                matStockOpnameItem.setStockOpnameId(504404237663640841L);
                matStockOpnameItem.setUnitId(rs.getLong("DEFAULT_SELL_UNIT_ID"));
                matStockOpnameItem.setMaterialId(rs.getLong("MATERIAL_ID"));
                matStockOpnameItem.setPrice(rs.getDouble("DEFAULT_PRICE"));
                matStockOpnameItem.setCost(rs.getDouble("DEFAULT_COST"));
                matStockOpnameItem.setQtyOpname(rs.getDouble("QTY"));
                matStockOpnameItem.setQtySold(0);
                matStockOpnameItem.setQtySystem(0);

                //System.out.println("=>> OPNAME ID : " + matStockOpnameItem.getStockOpnameId());
                //System.out.println("=>> MATERIAL ID : " + matStockOpnameItem.getMaterialId());
                //System.out.println("=>> PRICE  : " + matStockOpnameItem.getPrice());
                //System.out.println("=>> COST : " + matStockOpnameItem.getCost());
                //System.out.println("=>> QTY OPNAME : " + matStockOpnameItem.getQtyOpname());
                //System.out.println("=>> QTY SOLD : " + matStockOpnameItem.getQtySold());
                //System.out.println("=>> QTY SYSTEM : " + matStockOpnameItem.getQtySystem());

                PstMatStockOpnameItem.insertExc(matStockOpnameItem);
            //System.out.println("====================================================================== INSERT NO > " + i);
            }
            rs.close();

        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
    
    public static Vector getListSales(String where) {
        DBResultSet dbrs = null;
        Vector vListReturn = new Vector(1, 1);
        try {
            String sql = "SELECT COUNT(RET." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +") AS CNT "+
                         " , " + "(RET." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + ") STATUS "+
                         " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " RET" +
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
                         " ON RET." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                         " = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+
                         " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " CC " +
                         " ON RET." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                         " = CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID];

          /**
           * SELECT COUNT(RET.RETURN_MATERIAL_ID) AS CNT FROM pos_return_material RET INNER JOIN contact_list CNT ON RET.SUPPLIER_ID = CNT.CONTACT_ID
           * WHERE ((RET.RETURN_STATUS =0) OR (RET.RETURN_STATUS =2)) AND  RET.LOCATION_TYPE = 0;
           */
          String strStatus = "";
          strStatus =   "((RET." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS]+ " = 0)"+
                        " OR " + "(RET." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = 2)) AND"
                        + " CC."+PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+"!=1"

                        + " AND RET." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                              " AND (RET." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " OR RET." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " AND (RET." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " OR RET." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
          
          if(where.length()>0){
               strStatus=strStatus + where;
            }
          strStatus=strStatus+" GROUP BY RET." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS];


            sql = sql + " WHERE " + strStatus;
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
    
}
