/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.warehouse;

import com.dimata.qdep.form.Control;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.search.SrcMatDispatch;
import com.dimata.posbo.entity.search.SrcMatRequest;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.util.Formater;
import com.dimata.util.Command;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.search.SrcMatDispatchReceiveItem;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.common.entity.payment.*;
import com.dimata.posbo.db.DBException;

import com.dimata.util.LogicParser;
import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

/**
 *
 * @author mirah
 */
public class SessMatDispatchReceive {
    public static final String SESS_SRC_MATDISPATCHRECEIVE = "SESSION_MATERIAL_DISPATCH_RECEIVE";
    public static final String SESSION_MATERIAL_DISPATCH_RECEIVE_EXC = "SESSION_MATERIAL_DISPATCH_RECEIVE_EXC";
    public static final String SESSION_TRANSFER_MR_TO_DF = "SESSION_TRANSFER_MR_TO_DF";
    public static final String SESSION_MATERIAL_DISPATCH_RECEIVED = "SESSION_MATERIAL_DISPATCH_RECEIVED";
    public static final String[] orderBy =
	    {
	PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE],
	PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID],
	PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO],
	PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE],
	PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]
    };

    /* list for material request */
    public static Vector listMatDispatchReceiveItem(SrcMatDispatch srcMatDispatch, int start, int limit) {
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

	    if (where.length() > 0) {
		where = " WHERE	" + where;
	    }

	    sql += where;
	    sql += " ORDER BY DF." + orderBy[srcMatDispatch.getSortBy()];
	    sql += ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE];
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

    public static int getCountMatDispatch(SrcMatDispatch srcMatDispatch) {

	int result = 0;
	String sql = "SELECT COUNT(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
		" ) AS CNT FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
		" INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
		" ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
		" = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

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

    
    public String generateDispatchCode(MatDispatch matDispatch) {
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
		    " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd") +
		    " ' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd") +
		    " ') AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + matDispatch.getLocationId();

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


   /*Untuk menampilkan qty target
    * Transfer Unit
    * By mirahu
    */
    public static Vector getQtyTarget(int limitStart, int recordToGet, long oidDfRecGroupId) {
          if(limitStart<0){
             limitStart=0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_ITEM_ID]+ " AS DF_REC_ITEM_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ITEM_ID]+ " AS DF_ITEM_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+ " AS REC_ITEM_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]+ " AS DF_REC_GROUP_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID]+ " AS DF_ID "
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]+ " AS DFI_MATERIAL_ID "
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+ " AS DFI_QTY "
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]+ " AS DFI_UNIT_ID "
                    //+ " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]+ " AS RMI_MATERIAL_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+ " AS RMI_QTY "
                    //+ " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]+ " AS RMI_UNIT_ID "
                    //+ " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]+ " AS RMI_COST "
                    //+ " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]+ " AS RMI_TOTAL "
                    //+ " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ " AS DFI_SKU "
                    //+ " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ " AS DFI_NAME "
                    //+ " , UNT1." + PstUnit.fieldNames[PstUnit.FLD_CODE]+ " AS DFI_CODE "
                    //+ " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ " AS RMI_SKU "
                    //+ " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ " AS RMI_NAME "
                    //+ " , UNT2." + PstUnit.fieldNames[PstUnit.FLD_CODE]+ " AS RMI_CODE "
                    + " FROM " + PstMatDispatchReceiveItem.TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI"
                    + " ON DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
                    + " = MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
                    + " = UNT1." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
                    + " = UNT2." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " WHERE DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]
                    + " = " + oidDfRecGroupId
                    + " AND RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " != 0";


            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
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

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //Vector temp = new Vector();
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();

                matDispatchReceiveItem.setOID(rs.getLong("DF_REC_ITEM_ID"));
                matDispatchReceiveItem.getSourceItem().setOID(rs.getLong("DF_ITEM_ID"));
                matDispatchReceiveItem.getTargetItem().setOID(rs.getLong("REC_ITEM_ID"));
                matDispatchReceiveItem.setDfRecGroupId(rs.getLong("DF_REC_GROUP_ID"));
                matDispatchReceiveItem.setDispatchMaterialId(rs.getLong("DF_ID"));


                matDispatchReceiveItem.getTargetItem().setQty(rs.getDouble("RMI_QTY"));
                //matDispatchReceiveItem.getTargetItem().getMaterialTarget().setAveragePrice(rs.getDouble("RMI_AVERAGE_PRICE"));
                //matDispatchReceiveItem.getTargetItem().getMaterialTarget().setDefaultCost(rs.getDouble("RMI_DEFAULT_COST"));

                lists.add(matDispatchReceiveItem);


            }
            rs.close();

        } catch (Exception e) {
            lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    //Untuk menampilkan current stock values material target
     public static Vector getStockValueCurrentTarget(int limitStart, int recordToGet, long oidDfRecGroupId) {
          if(limitStart<0){
             limitStart=0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_ITEM_ID]+ " AS DF_REC_ITEM_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ITEM_ID]+ " AS DF_ITEM_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+ " AS REC_ITEM_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]+ " AS DF_REC_GROUP_ID "
                    + " , DFRI." + PstMatDispatchReceiveItem.fieldNames [PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID]+ " AS DF_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+ " AS RMI_REC_MATERIAL_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]+ " AS RMI_MATERIAL_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+ " AS RMI_QTY "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]+ " AS RMI_UNIT_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]+ " AS RMI_COST "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]+ " AS RMI_TOTAL "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ " AS RMI_SKU "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ " AS RMI_NAME "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+ " AS RMI_AVERAGE_PRICE "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]+ " AS RMI_DEFAULT_COST "
                    + " FROM " + PstMatDispatchReceiveItem.TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT "
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " WHERE DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]
                    + " = " + oidDfRecGroupId
                    + " AND RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " != 0";


            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
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

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //Vector temp = new Vector();
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();

                matDispatchReceiveItem.setOID(rs.getLong("DF_REC_ITEM_ID"));
                matDispatchReceiveItem.getSourceItem().setOID(rs.getLong("DF_ITEM_ID"));
                matDispatchReceiveItem.getTargetItem().setOID(rs.getLong("REC_ITEM_ID"));
                matDispatchReceiveItem.setDfRecGroupId(rs.getLong("DF_REC_GROUP_ID"));
                matDispatchReceiveItem.setDispatchMaterialId(rs.getLong("DF_ID"));

                matDispatchReceiveItem.getTargetItem().setReceiveMaterialId(rs.getLong("RMI_REC_MATERIAL_ID"));
                matDispatchReceiveItem.getTargetItem().setMaterialId(rs.getLong("RMI_MATERIAL_ID"));
                matDispatchReceiveItem.getTargetItem().setUnitId(rs.getLong("RMI_UNIT_ID"));
                matDispatchReceiveItem.getTargetItem().setQty(rs.getDouble("RMI_QTY"));
                matDispatchReceiveItem.getTargetItem().getMaterialTarget().setAveragePrice(rs.getDouble("RMI_AVERAGE_PRICE"));
                matDispatchReceiveItem.getTargetItem().getMaterialTarget().setDefaultCost(rs.getDouble("RMI_DEFAULT_COST"));

                lists.add(matDispatchReceiveItem);
            }
            rs.close();

        } catch (Exception e) {
            lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }



     /*
     * For Summary Existing HPP from target dispatch Unit
     * By Mirahu
     */
     public static double getTotalHppExistingTarget(long oidDfRecGroup) {
        double total = 0.0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM( RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
            "* MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + ")" +
            " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI" +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " INNER JOIN " + PstMatDispatchReceiveItem.TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI" +
            " ON DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
            " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
            " WHERE DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]+"="+oidDfRecGroup;
                    //" GROUP BY "+fieldNames[FLD_DISPATCH_MATERIAL_ID];
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getDouble(1);
            }
        }catch(Exception e){}
        return total;
    }

    /*
     * End Of Summary HPP
     */



     

   public static Vector updateHppTarget (double totalSource, long oidDfRecGroupCost){

        Vector vctGetQty = new Vector(1, 1);
        Vector vctGetCurrentHppTarget = new Vector(1,1);
        double qtyTarget = 0;
        double averagePrice = 0;
        double costTarget = 0;
        double costTargetNew = 0;
        double costTotal = 0;
        long oidDfRecItem = 0;
        double targetStok = 0;
        double pembagiHppTarget = 0;
        double oidCurrencyType = 0;

      //vctGetQty = PstMatDispatchReceiveItem.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
        vctGetCurrentHppTarget = SessMatDispatchReceive.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
        vctGetQty = SessMatDispatchReceive.getQtyTarget(0, 0, oidDfRecGroupCost);
        if( vctGetQty!=null &&  vctGetQty.size() > 0) {
            for(int k=0;k<vctGetQty.size();k++){
                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)vctGetQty.get(k);
                for(int l=0;l<vctGetCurrentHppTarget.size();l++){
                    MatDispatchReceiveItem dfRecItemHpp = (MatDispatchReceiveItem)vctGetCurrentHppTarget.get(l);
                        qtyTarget = dfRecItem.getTargetItem().getQty();
                        averagePrice = dfRecItemHpp.getTargetItem().getMaterialTarget().getAveragePrice();
                        oidDfRecItem = dfRecItem.getTargetItem().getOID();

                        costTarget = totalSource / qtyTarget ;

                        dfRecItem.getTargetItem().setCost(costTarget);
                        costTotal = costTarget * qtyTarget;
                        dfRecItem.getTargetItem().setTotal(costTotal);
                }

            }

       }
      return vctGetQty;
     }

   

   public static Vector updateHppTarget2 (double totalSource, long oidDfRecGroupCost, double summaryExistingStockValue){

        Vector vctGetQty = new Vector(1, 1);
        Vector vctGetCurrentHppTarget = new Vector(1,1);
        double qtyTarget = 0;
        double averagePrice = 0;
        double costTarget = 0;
        double costTargetNew = 0;
        double costTotal = 0;
        long oidDfRecItem = 0;
        double targetStok = 0;
        long oidDispatchItem = 0;
        long oidReceiveItem = 0;
        long recMaterialId = 0;
        long materialId = 0;
        long unitId = 0;
        long oidCurrencyType = 0;
        



      //vctGetQty = PstMatDispatchReceiveItem.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
        vctGetCurrentHppTarget = SessMatDispatchReceive.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
        //vctGetQty = SessMatDispatchReceive.getQtyTarget(0, 0, oidDfRecGroupCost);
        if( vctGetCurrentHppTarget!=null &&  vctGetCurrentHppTarget.size() > 0) {
            for(int j=0;j<vctGetCurrentHppTarget.size();j++){
                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)vctGetCurrentHppTarget.get(j);
                qtyTarget = dfRecItem.getTargetItem().getQty();
                averagePrice = dfRecItem.getTargetItem().getMaterialTarget().getAveragePrice();
                oidDfRecItem = dfRecItem.getTargetItem().getOID();
                oidDispatchItem = dfRecItem.getSourceItem().getOID();
                oidReceiveItem = dfRecItem.getTargetItem().getOID();
                recMaterialId = dfRecItem.getTargetItem().getReceiveMaterialId();
                materialId = dfRecItem.getTargetItem().getMaterialId();
                unitId = dfRecItem.getTargetItem().getUnitId();

                if(summaryExistingStockValue == 0){
                    summaryExistingStockValue = 1;
                }

                targetStok = (averagePrice * qtyTarget / summaryExistingStockValue) * totalSource;
                if(targetStok == 0){
                    targetStok = totalSource;
                }
                costTargetNew = targetStok/qtyTarget;
                costTotal = costTargetNew * qtyTarget;

                oidCurrencyType = PstStandartRate.getLocalCurrencyId();

                MatReceiveItem matReceiveItem = new MatReceiveItem();
                matReceiveItem.setOID(oidReceiveItem);
                matReceiveItem.setReceiveMaterialId(recMaterialId);
                matReceiveItem.setMaterialId(materialId);
                matReceiveItem.setUnitId(unitId);
                matReceiveItem.setQty(qtyTarget);
                matReceiveItem.setCost(costTargetNew);
                matReceiveItem.setTotal(costTotal);
                matReceiveItem.setCurrencyId(oidCurrencyType);
                
                 try {
                    PstMatReceiveItem.updateExc(matReceiveItem);
                } catch (Exception e) {
                }









            }
       }
      return vctGetQty;
     }


     
}
