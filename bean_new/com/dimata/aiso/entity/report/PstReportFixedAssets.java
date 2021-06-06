/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.report;

import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.aktiva.*;
import com.dimata.aiso.entity.masterdata.AktivaLocation;
import com.dimata.aiso.entity.masterdata.ModulAktiva;
import com.dimata.aiso.entity.masterdata.PstAktivaLocation;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;

import com.dimata.util.Formater;
import java.util.Date;
import java.util.Vector;
import java.sql.ResultSet;

/**
 *
 * @author dwi
 */
public class PstReportFixedAssets extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc{

	public static final String TBL_REPORT_FIXED_ASSETS = "aiso_report_fixed_assets";
	
	public static final int FLD_REPORT_FIXED_ASSETS_ID = 0;
	public static final int FLD_CODE = 1;
	public static final int FLD_NAME = 2;
	public static final int FLD_LOCATION = 3;
	public static final int FLD_AQC_DATE = 4;
	public static final int FLD_LIFE = 5;
	public static final int FLD_QTY = 6;
	public static final int FLD_AQC_LAST_MONTH = 7;
	public static final int FLD_AQC_INCREMENT = 8;
	public static final int FLD_AQC_DECREMENT = 9;
	public static final int FLD_AQC_THIS_MONTH = 10;
	public static final int FLD_DEP_LAST_MONTH = 11;
	public static final int FLD_DEP_INCREMENT = 12;
	public static final int FLD_DEP_DECREMENT = 13;
	public static final int FLD_DEP_THIS_MONTH = 14;
	public static final int FLD_BOOK_VALUE = 15;
	public static final int FLD_LOCATION_ID = 16;
	public static final int FLD_PERIOD_ID = 17;
	public static final int FLD_ACCOUNT_ID = 18;
	public static final int FLD_GROUP_ID = 19;
	public static final int FLD_DEP_METHOD_ID = 20;
	public static final int FLD_DEP_TYPE_ID = 21;
	public static final int FLD_FIXED_ASSETS_ID = 22;
	
	public static String[] fieldNames = {
	    "REPORT_FIXED_ASSETS_ID",
	    "CODE",
	    "NAME",
	    "LOCATION",
	    "AQC_DATE",
	    "LIFE",
	    "QTY",
	    "AQC_VALUE_TILL_LAST_MONTH",
	    "AQC_INCREMENT",
	    "AQC_DECREMENT",
	    "AQC_TILL_THIS_MONTH",
	    "DEP_TILL_LAST_MONTH",
	    "DEP_INCREMENT",
	    "DEP_DECREMENT",
	    "DEP_TILL_THIS_MONTH",
	    "BOOK_VALUE",
	    "LOC_ID",
	    "PERIOD_ID",
	    "ACC_ID",
	    "GROUP_ID",
	    "DEP_METHOD_ID",
	    "DEP_TYPE_ID",
	    "FIXED_ASSETS_ID"
	};
	
	public static int[] fieldTypes = {
	    TYPE_PK + TYPE_ID + TYPE_LONG,
	    TYPE_STRING,
	    TYPE_STRING,
	    TYPE_STRING,
	    TYPE_DATE,
	    TYPE_INT,
	    TYPE_INT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_FLOAT,
	    TYPE_LONG,
	    TYPE_LONG,
	    TYPE_LONG,
	    TYPE_LONG,
	    TYPE_LONG,
	    TYPE_LONG,
	    TYPE_LONG,
	};
	
	public PstReportFixedAssets(){
	
	}
	
	public PstReportFixedAssets(int i) throws DBException{
	    super(new PstReportFixedAssets());
	}
	
	public PstReportFixedAssets(String sOid) throws DBException{
	    super(new PstReportFixedAssets(0));
	    if(!locate(sOid))
		throw new DBException(this, DBException.RECORD_NOT_FOUND);
	    else
		return;
	}
	
	public PstReportFixedAssets(long lOid) throws DBException{
	    super(new PstReportFixedAssets(0));
	    String sOid = "0";
	    try{
		sOid = String.valueOf(lOid);
	    }catch(Exception e){
		throw new DBException(this, DBException.RECORD_NOT_FOUND);
	    }
	    
	    if(!locate(sOid))
		throw new DBException(this, DBException.RECORD_NOT_FOUND);
	    else
		return;
	}
	
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_REPORT_FIXED_ASSETS;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstReportFixedAssets().getClass().getName();
    }

    public long fetchExc(Entity ent) throws DBException {
        ReportFixedAssets objReportFixedAssets = fetchExc(ent.getOID()); 
        ent = (Entity) objReportFixedAssets;
        return objReportFixedAssets.getOID();
    }

    public long insertExc(Entity ent) throws DBException {
        return insertExc((ReportFixedAssets) ent);
    }

    public long updateExc(Entity ent) throws DBException {
        return updateExc((ReportFixedAssets) ent);
    }

    public long deleteExc(Entity ent) throws DBException {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ReportFixedAssets fetchExc(long Oid) throws DBException {
        try {
            ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
            PstReportFixedAssets pstReportFixedAssets = new PstReportFixedAssets(Oid);
            objReportFixedAssets.setOID(Oid); 

            objReportFixedAssets.setCode(pstReportFixedAssets.getString(FLD_CODE));
            objReportFixedAssets.setName(pstReportFixedAssets.getString(FLD_NAME));
            objReportFixedAssets.setLocation(pstReportFixedAssets.getString(FLD_LOCATION));
            objReportFixedAssets.setAqcDate(pstReportFixedAssets.getDate(FLD_AQC_DATE));
            objReportFixedAssets.setLife(pstReportFixedAssets.getInt(FLD_LIFE));
	    objReportFixedAssets.setQty(pstReportFixedAssets.getInt(FLD_QTY));
            objReportFixedAssets.setAqcLastMonth(pstReportFixedAssets.getdouble(FLD_AQC_LAST_MONTH));
            objReportFixedAssets.setAqcIncrement(pstReportFixedAssets.getdouble(FLD_AQC_INCREMENT));
            objReportFixedAssets.setAqcDecrement(pstReportFixedAssets.getdouble(FLD_AQC_DECREMENT));
            objReportFixedAssets.setAqcThisMonth(pstReportFixedAssets.getdouble(FLD_AQC_THIS_MONTH));
            objReportFixedAssets.setDepLastMonth(pstReportFixedAssets.getdouble(FLD_DEP_LAST_MONTH));
            objReportFixedAssets.setDepDecrement(pstReportFixedAssets.getdouble(FLD_DEP_DECREMENT));
            objReportFixedAssets.setDepIncrement(pstReportFixedAssets.getdouble(FLD_DEP_INCREMENT));
            objReportFixedAssets.setDepThisMonth(pstReportFixedAssets.getdouble(FLD_DEP_THIS_MONTH));
            objReportFixedAssets.setBookValue(pstReportFixedAssets.getdouble(FLD_BOOK_VALUE));
            objReportFixedAssets.setLocationId(pstReportFixedAssets.getlong(FLD_LOCATION_ID));
            objReportFixedAssets.setPeriodId(pstReportFixedAssets.getlong(FLD_PERIOD_ID));
            objReportFixedAssets.setGroupId(pstReportFixedAssets.getlong(FLD_GROUP_ID));
            objReportFixedAssets.setAccountId(pstReportFixedAssets.getlong(FLD_ACCOUNT_ID));
            objReportFixedAssets.setDepMethodId(pstReportFixedAssets.getlong(FLD_DEP_METHOD_ID));
            objReportFixedAssets.setDepTypeId(pstReportFixedAssets.getlong(FLD_DEP_TYPE_ID));
            objReportFixedAssets.setFixedAssetsId(pstReportFixedAssets.getlong(FLD_FIXED_ASSETS_ID));

            return objReportFixedAssets;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportFixedAssets(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(ReportFixedAssets objReportFixedAssets) throws DBException {
        try {
            PstReportFixedAssets pstReportFixedAssets = new PstReportFixedAssets(0);

            pstReportFixedAssets.setString(FLD_CODE, objReportFixedAssets.getCode());
            pstReportFixedAssets.setString(FLD_NAME, objReportFixedAssets.getName());
            pstReportFixedAssets.setString(FLD_LOCATION, objReportFixedAssets.getLocation());
	    pstReportFixedAssets.setDate(FLD_AQC_DATE, objReportFixedAssets.getAqcDate());
            pstReportFixedAssets.setInt(FLD_LIFE, objReportFixedAssets.getLife());
            pstReportFixedAssets.setInt(FLD_QTY, objReportFixedAssets.getQty());	    
	    pstReportFixedAssets.setDouble(FLD_AQC_LAST_MONTH, objReportFixedAssets.getAqcLastMonth());
	    pstReportFixedAssets.setDouble(FLD_AQC_INCREMENT, objReportFixedAssets.getAqcIncrement());
	    pstReportFixedAssets.setDouble(FLD_AQC_DECREMENT, objReportFixedAssets.getAqcDecrement());
	    pstReportFixedAssets.setDouble(FLD_AQC_THIS_MONTH, objReportFixedAssets.getAqcThisMonth());
	    pstReportFixedAssets.setDouble(FLD_DEP_LAST_MONTH, objReportFixedAssets.getDepLastMonth());
	    pstReportFixedAssets.setDouble(FLD_DEP_INCREMENT, objReportFixedAssets.getDepIncrement());
	    pstReportFixedAssets.setDouble(FLD_DEP_DECREMENT, objReportFixedAssets.getDepDecrement());
	    pstReportFixedAssets.setDouble(FLD_DEP_THIS_MONTH, objReportFixedAssets.getDepThisMonth());
	    pstReportFixedAssets.setDouble(FLD_BOOK_VALUE, objReportFixedAssets.getBookValue());
	    pstReportFixedAssets.setLong(FLD_LOCATION_ID, objReportFixedAssets.getLocationId());
	    pstReportFixedAssets.setLong(FLD_PERIOD_ID, objReportFixedAssets.getPeriodId());
	    pstReportFixedAssets.setLong(FLD_GROUP_ID, objReportFixedAssets.getGroupId());
	    pstReportFixedAssets.setLong(FLD_ACCOUNT_ID, objReportFixedAssets.getAccountId());
	    pstReportFixedAssets.setLong(FLD_DEP_METHOD_ID, objReportFixedAssets.getDepMethodId());
	    pstReportFixedAssets.setLong(FLD_DEP_TYPE_ID, objReportFixedAssets.getDepTypeId());
	    pstReportFixedAssets.setLong(FLD_FIXED_ASSETS_ID, objReportFixedAssets.getFixedAssetsId());
	   
	    pstReportFixedAssets.insert();
	   
            objReportFixedAssets.setOID(pstReportFixedAssets.getlong(FLD_REPORT_FIXED_ASSETS_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportFixedAssets(0), DBException.UNKNOWN);
        }
        return objReportFixedAssets.getOID();
    }

    
    public static long updateExc(ReportFixedAssets objReportFixedAssets) throws DBException {
        try {
            if (objReportFixedAssets != null && objReportFixedAssets.getOID() != 0) {
                PstReportFixedAssets pstReportFixedAssets = new PstReportFixedAssets(objReportFixedAssets.getOID());

                pstReportFixedAssets.setString(FLD_CODE, objReportFixedAssets.getCode());
		pstReportFixedAssets.setString(FLD_NAME, objReportFixedAssets.getName());
		pstReportFixedAssets.setString(FLD_LOCATION, objReportFixedAssets.getLocation());
		System.out.println("PstReportFixedAssets.objReportFixedAssets.getAqcDate() UPDATE ::::: "+objReportFixedAssets.getAqcDate());
		pstReportFixedAssets.setDate(FLD_AQC_DATE, objReportFixedAssets.getAqcDate());
		pstReportFixedAssets.setInt(FLD_LIFE, objReportFixedAssets.getLife());
		pstReportFixedAssets.setInt(FLD_QTY, objReportFixedAssets.getQty());
		pstReportFixedAssets.setDouble(FLD_AQC_LAST_MONTH, objReportFixedAssets.getAqcLastMonth());
		pstReportFixedAssets.setDouble(FLD_AQC_INCREMENT, objReportFixedAssets.getAqcIncrement());
		pstReportFixedAssets.setDouble(FLD_AQC_DECREMENT, objReportFixedAssets.getAqcDecrement());
		pstReportFixedAssets.setDouble(FLD_AQC_THIS_MONTH, objReportFixedAssets.getAqcThisMonth());
		pstReportFixedAssets.setDouble(FLD_DEP_LAST_MONTH, objReportFixedAssets.getDepLastMonth());
		pstReportFixedAssets.setDouble(FLD_DEP_INCREMENT, objReportFixedAssets.getDepIncrement());
		pstReportFixedAssets.setDouble(FLD_DEP_DECREMENT, objReportFixedAssets.getDepDecrement());
		pstReportFixedAssets.setDouble(FLD_DEP_THIS_MONTH, objReportFixedAssets.getDepThisMonth());
		pstReportFixedAssets.setDouble(FLD_BOOK_VALUE, objReportFixedAssets.getBookValue());
		pstReportFixedAssets.setLong(FLD_LOCATION_ID, objReportFixedAssets.getLocationId());
		pstReportFixedAssets.setLong(FLD_PERIOD_ID, objReportFixedAssets.getPeriodId());
		pstReportFixedAssets.setLong(FLD_GROUP_ID, objReportFixedAssets.getGroupId());
		pstReportFixedAssets.setLong(FLD_ACCOUNT_ID, objReportFixedAssets.getAccountId());
		pstReportFixedAssets.setLong(FLD_DEP_METHOD_ID, objReportFixedAssets.getDepMethodId());
		pstReportFixedAssets.setLong(FLD_DEP_TYPE_ID, objReportFixedAssets.getDepTypeId());
		pstReportFixedAssets.setLong(FLD_FIXED_ASSETS_ID, objReportFixedAssets.getFixedAssetsId());
		
		
		pstReportFixedAssets.update();
		
		return objReportFixedAssets.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportFixedAssets(0), DBException.UNKNOWN);
        }
        return 0;
    }

    
    public static long deleteExc(long Oid) throws DBException {
        try {
            PstReportFixedAssets pstReportFixedAssets = new PstReportFixedAssets(Oid);
	  
	    pstReportFixedAssets.delete();
	    
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReportFixedAssets(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = getStringQuery(limitStart, recordToGet, whereClause, order);
	    
	    System.out.println("PstReportFixedAssets.list ::::::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
                resultToObject(rs, objReportFixedAssets);
                lists.add(objReportFixedAssets);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstReportFixedAssets().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, ReportFixedAssets objReportFixedAssets) {
        try {
            objReportFixedAssets.setOID(rs.getLong(fieldNames[PstReportFixedAssets.FLD_REPORT_FIXED_ASSETS_ID]));
            objReportFixedAssets.setCode(rs.getString(fieldNames[PstReportFixedAssets.FLD_CODE]));
            objReportFixedAssets.setName(rs.getString(fieldNames[PstReportFixedAssets.FLD_NAME]));
            objReportFixedAssets.setLocation(rs.getString(fieldNames[PstReportFixedAssets.FLD_LOCATION]));
            objReportFixedAssets.setAqcDate(rs.getDate(fieldNames[PstReportFixedAssets.FLD_AQC_DATE]));
            objReportFixedAssets.setLife(rs.getInt(fieldNames[PstReportFixedAssets.FLD_LIFE]));
            objReportFixedAssets.setQty(rs.getInt(fieldNames[PstReportFixedAssets.FLD_QTY]));
            objReportFixedAssets.setAqcLastMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_AQC_LAST_MONTH]));
            objReportFixedAssets.setAqcIncrement(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_AQC_INCREMENT]));
            objReportFixedAssets.setAqcDecrement(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_AQC_DECREMENT]));
            objReportFixedAssets.setAqcThisMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_AQC_THIS_MONTH]));
            objReportFixedAssets.setDepLastMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_DEP_LAST_MONTH]));
            objReportFixedAssets.setDepIncrement(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_DEP_INCREMENT]));
            objReportFixedAssets.setDepDecrement(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_DEP_DECREMENT]));
            objReportFixedAssets.setDepThisMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_DEP_THIS_MONTH]));
            objReportFixedAssets.setBookValue(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_BOOK_VALUE]));
            objReportFixedAssets.setLocationId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_LOCATION_ID]));
            objReportFixedAssets.setPeriodId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_PERIOD_ID]));
            objReportFixedAssets.setGroupId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_GROUP_ID]));
            objReportFixedAssets.setAccountId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_ACCOUNT_ID]));
            objReportFixedAssets.setDepMethodId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_DEP_METHOD_ID]));
            objReportFixedAssets.setDepTypeId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_DEP_TYPE_ID]));
            objReportFixedAssets.setFixedAssetsId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_FIXED_ASSETS_ID]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static void setReportFixedAssetsFrMaster(ReportFixedAssets objReportFixedAssets, ModulAktiva objModulAktiva, int qty, double acmDep){
	try{    
	objReportFixedAssets.setCode(objModulAktiva.getKode());
            objReportFixedAssets.setName(objModulAktiva.getName());
            objReportFixedAssets.setLocation(getLocation(objModulAktiva.getIdFixedAssetsLoc()));
	    System.out.println("PstReportFixedAssets.aktiva.getTglPerolehan() :::::::::::::::: "+objModulAktiva.getTglPerolehan());
            objReportFixedAssets.setAqcDate(objModulAktiva.getTglPerolehan());
            objReportFixedAssets.setLife(objModulAktiva.getMasaManfaat());
            objReportFixedAssets.setQty(qty);
	    if(isOpeningBalance(objModulAktiva.getTglPerolehan())){
		objReportFixedAssets.setAqcLastMonth(objModulAktiva.getHargaPerolehan());
		objReportFixedAssets.setAqcIncrement(0);
	    }else{
		objReportFixedAssets.setAqcLastMonth(0);
		objReportFixedAssets.setAqcIncrement(objModulAktiva.getHargaPerolehan());		
	    }	    
	    objReportFixedAssets.setAqcDecrement(0);
	    objReportFixedAssets.setAqcThisMonth(objModulAktiva.getHargaPerolehan());
            objReportFixedAssets.setDepLastMonth(0);
            objReportFixedAssets.setDepDecrement(0);
            objReportFixedAssets.setDepIncrement(acmDep);
            objReportFixedAssets.setDepThisMonth(acmDep);
            objReportFixedAssets.setBookValue(objModulAktiva.getHargaPerolehan() - acmDep);
            objReportFixedAssets.setLocationId(objModulAktiva.getIdFixedAssetsLoc());
            objReportFixedAssets.setPeriodId(PstPeriode.getCurrPeriodId());
            objReportFixedAssets.setGroupId(objModulAktiva.getAktivaGroupOid());
            objReportFixedAssets.setAccountId(objModulAktiva.getIdPerkiraanAktiva());
            objReportFixedAssets.setDepMethodId(objModulAktiva.getMetodePenyusutanOid());
            objReportFixedAssets.setDepTypeId(objModulAktiva.getTypePenyusutanOid());
            objReportFixedAssets.setFixedAssetsId(objModulAktiva.getOID());
	}catch(Exception e){System.out.println("Exception PstReportFixedAssets.setReportFixedAssetsFrMaster :::: "+e.toString());}
    }
    
    public static synchronized void updateReportFAOnReceive(){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Date startDate = new Date();
	Date endDate = new Date();
	Periode objPeriod = new Periode();
	long lPeriodId = 0;
	try{
	    lPeriodId = PstPeriode.getCurrPeriodId();
	}catch(Exception e){}
	
	if(lPeriodId != 0){
	    try{
		objPeriod = PstPeriode.fetchExc(lPeriodId);
		startDate = objPeriod.getTglAwal();
		endDate = objPeriod.getTglAkhir();
	    }catch(Exception e){}
	}
	
	try{
	    String sql = " SELECT DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
			 ", SUM(DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_QTY]+") AS QTY "+
			 ", SUM(DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE]+") AS VALUE "+
			 " FROM "+PstReceiveAktiva.TBL_RECEIVE_AKTIVA+" AS MN "+
			 " INNER JOIN "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS DT "+
			 " ON MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]+
			 " = DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID]+
			 " WHERE MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]+
			 " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+"'"+
			 " GROUP BY DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID];
	    
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
		long lFixedAssetsId = 0;
		long lReportFAId = 0;
		int iQtyRec = 0;
		double dValueRec = 0.0;
		iQtyRec = rs.getInt("QTY");
		dValueRec = rs.getDouble("VALUE");
		lFixedAssetsId = rs.getLong(PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]);
		if(lFixedAssetsId != 0 && lPeriodId != 0){
		    lReportFAId = getReportFixedAssetsId(lFixedAssetsId, lPeriodId);
		    if(lReportFAId != 0){
			try{
			    objReportFixedAssets = fetchExc(lReportFAId);			    
			    int iQty = 0;
			    double dValue = 0.0;
			    double dDepValue = 0.0;
			    iQty = objReportFixedAssets.getQty();
			    dValue = objReportFixedAssets.getAqcThisMonth();
			    dDepValue = objReportFixedAssets.getDepThisMonth();
			    objReportFixedAssets.setQty(iQty + iQtyRec);
			    objReportFixedAssets.setAqcIncrement(dValueRec);
			    objReportFixedAssets.setAqcThisMonth(dValue + dValueRec);
			    objReportFixedAssets.setBookValue(dValue + dValueRec - dDepValue);
			    try{
				long lUpdate = updateExc(objReportFixedAssets);
			    }catch(Exception e){}
			}catch(Exception e){}
		    }
		}
	    }
	}catch(Exception e){}
    }
    
    public static synchronized void updateReportFAOnOutGoing(){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Date startDate = new Date();
	Date endDate = new Date();
	Periode objPeriod = new Periode();
	long lPeriodId = 0;
	try{
	    lPeriodId = PstPeriode.getCurrPeriodId();
	}catch(Exception e){}
	
	if(lPeriodId != 0){
	    try{
		objPeriod = PstPeriode.fetchExc(lPeriodId);
		startDate = objPeriod.getTglAwal();
		endDate = objPeriod.getTglAkhir();
	    }catch(Exception e){}
	}
	
	try{
	    String sql = " SELECT DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID]+
			 ", SUM(DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_QTY]+") AS QTY "+
			 ", SUM(DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_TOTAL_PRICE]+") AS VALUE "+
			 " FROM "+PstSellingAktiva.TBL_SELLING_AKTIVA+" AS MN "+
			 " INNER JOIN "+PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM+" AS DT "+
			 " ON MN."+PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]+
			 " = DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ID]+
			 " WHERE MN."+PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING]+
			 " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+"'"+
			 " GROUP BY DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID];
	    
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
		long lFixedAssetsId = 0;
		long lReportFAId = 0;
		int iQtyRec = 0;
		double dValueOut = 0.0;
		double dTotAccDep = 0.0;
		iQtyRec = rs.getInt("QTY");
		dValueOut = rs.getDouble("VALUE");
		lFixedAssetsId = rs.getLong(PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID]);
		
		if(lFixedAssetsId != 0 && lPeriodId != 0){
		    lReportFAId = getReportFixedAssetsId(lFixedAssetsId, lPeriodId);
		    if(lReportFAId != 0){
			try{
			    objReportFixedAssets = fetchExc(lReportFAId);			    
			    int iQty = 0;
			    double dValue = 0.0;
			    double dDepValue = 0.0;
			    try{
				dTotAccDep = PstPenyusutanAktiva.getTotalNilaiSusut(lFixedAssetsId);
			    }catch(Exception e){}
			    iQty = objReportFixedAssets.getQty();
			    dValue = objReportFixedAssets.getAqcThisMonth();
			    dDepValue = objReportFixedAssets.getDepThisMonth();
			    if(iQty > iQtyRec){
				objReportFixedAssets.setQty(iQty - iQtyRec);
			    }
			    objReportFixedAssets.setAqcDecrement(dValueOut);
			    if(dValue > dValueOut){
				objReportFixedAssets.setAqcThisMonth(dValue - dValueOut);
			    }
			    objReportFixedAssets.setDepDecrement(dTotAccDep);
			    objReportFixedAssets.setDepThisMonth(dDepValue - dTotAccDep);
			    objReportFixedAssets.setBookValue(dValue - dValueOut - dDepValue);
			    
			    try{
				long lUpdate = updateExc(objReportFixedAssets);
			    }catch(Exception e){}
			}catch(Exception e){}
		    }
		}
	    }
	}catch(Exception e){}
    }
    
    public static synchronized long getReportFixedAssetsId(long fixedAssetsId, long lPeriodId){
	DBResultSet dbrs = null;
	long lResult = 0;
	if(fixedAssetsId != 0 && lPeriodId != 0){
	    try{
		String sql = " SELECT "+fieldNames[PstReportFixedAssets.FLD_REPORT_FIXED_ASSETS_ID]+
			     " FROM "+TBL_REPORT_FIXED_ASSETS+
			     " WHERE "+fieldNames[PstReportFixedAssets.FLD_FIXED_ASSETS_ID]+
			     " = "+fixedAssetsId+
			     " AND "+fieldNames[PstReportFixedAssets.FLD_PERIOD_ID]+
			     " = "+lPeriodId;
		System.out.println("SQL PstReportFixedAsset.getReportFixedAssetsId ::::::::: "+sql);
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    lResult = rs.getLong(fieldNames[PstReportFixedAssets.FLD_REPORT_FIXED_ASSETS_ID]);
		}
		rs.close();
	    }catch(Exception e){}
	}
	return lResult;
    }
    
    
    public static String getLocation(long locationId){
	String sResult = "";
	AktivaLocation objLocation = new AktivaLocation();
	if(locationId != 0){
	    try{
		objLocation = PstAktivaLocation.fetchExc(locationId);
		sResult = objLocation.getAktivaLocName();
	    }catch(Exception e){}
	}
	return sResult;
    }
    
    public static boolean isOpeningBalance(Date aqcDate){
	boolean bResult = false;
	Periode objPeriod = new Periode();
	Date startDate = new Date();
	long lPeriodId = 0;
	try{
	    lPeriodId = PstPeriode.getCurrPeriodId();
	}catch(Exception e){}
	
	if(lPeriodId != 0){
	    try{
		objPeriod = PstPeriode.fetchExc(lPeriodId);
		startDate = objPeriod.getTglAwal();
	    }catch(Exception e){}
	}
	
	if(startDate != null && aqcDate != null){
	    if(aqcDate.before(startDate)){
	          bResult = true;
	    }
	}
	return bResult;
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[PstReportFixedAssets.FLD_REPORT_FIXED_ASSETS_ID] + ") " +
                    " FROM " + TBL_REPORT_FIXED_ASSETS;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static synchronized String getWhClause(long periodeOid, long typeOid, long metodeOid, long aktivaGroupOid, long perkAktivaOid, long lLocationId){
	String sResult = "";
	try{
	    if(periodeOid != 0){
		sResult = fieldNames[PstReportFixedAssets.FLD_PERIOD_ID]+" = "+periodeOid;
	    }
	    if(typeOid != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+fieldNames[PstReportFixedAssets.FLD_DEP_TYPE_ID]+" = "+typeOid;
		}else{
		    sResult += fieldNames[PstReportFixedAssets.FLD_DEP_TYPE_ID]+" = "+typeOid;
		}
	    }
	    if(metodeOid != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+fieldNames[PstReportFixedAssets.FLD_DEP_METHOD_ID]+" = "+metodeOid;
		}else{
		    sResult += fieldNames[PstReportFixedAssets.FLD_DEP_METHOD_ID]+" = "+metodeOid;
		}
	    }
	    if(aktivaGroupOid != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+fieldNames[PstReportFixedAssets.FLD_GROUP_ID]+" = "+aktivaGroupOid;
		}else{
		    sResult += fieldNames[PstReportFixedAssets.FLD_GROUP_ID]+" = "+aktivaGroupOid;
		}
	    }
	    if(perkAktivaOid != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+fieldNames[PstReportFixedAssets.FLD_ACCOUNT_ID]+" = "+perkAktivaOid;
		}else{
		    sResult += fieldNames[PstReportFixedAssets.FLD_ACCOUNT_ID]+" = "+perkAktivaOid;
		}
	    }
	    if(lLocationId != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+fieldNames[PstReportFixedAssets.FLD_LOCATION_ID]+" = "+lLocationId;
		}else{
		    sResult += fieldNames[PstReportFixedAssets.FLD_LOCATION_ID]+" = "+lLocationId;
		}
	    }
	}catch(Exception e){}
	return sResult;
    }

    public static synchronized Vector reportFixedAssets(long periodeOid, long typeOid, long metodeOid, 
	long aktivaGroupOid, long perkAktivaOid, long lLocationId, int limitStart, int recordToGet, 
	String order){
	Vector vResult = new Vector(1,1);
	String sAutoRecFA = "";
	try{
	    sAutoRecFA = PstSystemProperty.getValueByName("AUTO_REC_FA");
	}catch(Exception e){}
	
	try{
	    if(sAutoRecFA != null && sAutoRecFA.length() > 0 && sAutoRecFA.equalsIgnoreCase("N")){
	           updateReportFAOnReceive();
	    }
	    updateReportFAOnOutGoing();
	    String sWhClause = getWhClause(periodeOid, typeOid, metodeOid, aktivaGroupOid, perkAktivaOid, lLocationId);
	    vResult = list(limitStart, recordToGet, sWhClause, order);
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized int getTotalRecord(long periodeOid, long typeOid, long metodeOid, 
	long aktivaGroupOid, long perkAktivaOid, long lLocationId){
	int iResult = 0;
	try{
	    String sWhClause = getWhClause(periodeOid, typeOid, metodeOid, aktivaGroupOid, perkAktivaOid, lLocationId);
	    iResult = getCount(sWhClause);
	}catch(Exception e){}
	return iResult;
    }
    
    public static synchronized ReportFixedAssets getTotalValue(long periodeOid, long typeOid, long metodeOid, 
	long aktivaGroupOid, long perkAktivaOid, long lLocationId){
	DBResultSet dbrs = null;
	ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
	try{
	    String sWhClause = getWhClause(periodeOid, typeOid, metodeOid, aktivaGroupOid, perkAktivaOid, lLocationId);
	    String sql = " SELECT SUM("+fieldNames[PstReportFixedAssets.FLD_AQC_LAST_MONTH]+") AS QLM "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_AQC_INCREMENT]+") AS QIN "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_AQC_DECREMENT]+") AS QDR "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_AQC_THIS_MONTH]+") AS QTM "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_DEP_LAST_MONTH]+") AS DLM "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_DEP_INCREMENT]+") AS DIN "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_DEP_DECREMENT]+") AS DDR "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_DEP_THIS_MONTH]+") AS DTM "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_BOOK_VALUE]+") AS BKV "+
			 ", SUM("+fieldNames[PstReportFixedAssets.FLD_QTY]+") AS QTY "+
			 " FROM "+TBL_REPORT_FIXED_ASSETS+" WHERE "+sWhClause;
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		objReportFixedAssets.setAqcLastMonth(rs.getDouble("QLM"));
		objReportFixedAssets.setAqcIncrement(rs.getDouble("QIN"));
		objReportFixedAssets.setAqcDecrement(rs.getDouble("QDR"));
		objReportFixedAssets.setAqcThisMonth(rs.getDouble("QTM"));
		objReportFixedAssets.setDepLastMonth(rs.getDouble("DLM"));		
		objReportFixedAssets.setDepIncrement(rs.getDouble("DIN"));		
		objReportFixedAssets.setDepDecrement(rs.getDouble("DDR"));		
		objReportFixedAssets.setDepThisMonth(rs.getDouble("DTM"));		
		objReportFixedAssets.setBookValue(rs.getDouble("BKV"));
		objReportFixedAssets.setQty(rs.getInt("QTY"));
	    }
	    rs.close();
	}catch(Exception e){}
	return objReportFixedAssets;
    }
    
    public static synchronized long closingFAReport(long lOldPeriod, long lNewPeriod){
	long lResult = 0;
	DBResultSet dbrs = null;
	try{
	    String whereClause = fieldNames[PstReportFixedAssets.FLD_PERIOD_ID]+" = "+lOldPeriod;
	    String sql = getStringQueryClosingReportFA(whereClause);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
		objReportFixedAssets.setCode(rs.getString(fieldNames[PstReportFixedAssets.FLD_CODE]));
		objReportFixedAssets.setName(rs.getString(fieldNames[PstReportFixedAssets.FLD_NAME]));
		objReportFixedAssets.setLocation(rs.getString(fieldNames[PstReportFixedAssets.FLD_LOCATION]));
		objReportFixedAssets.setAqcDate(rs.getDate(fieldNames[PstReportFixedAssets.FLD_AQC_DATE]));
		objReportFixedAssets.setLife(rs.getInt(fieldNames[PstReportFixedAssets.FLD_LIFE]));
		objReportFixedAssets.setQty(rs.getInt(fieldNames[PstReportFixedAssets.FLD_QTY]));
		objReportFixedAssets.setAqcLastMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_AQC_THIS_MONTH]));
		objReportFixedAssets.setAqcIncrement(0);
		objReportFixedAssets.setAqcDecrement(0);
		objReportFixedAssets.setAqcThisMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_AQC_THIS_MONTH]));
		objReportFixedAssets.setDepLastMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_DEP_THIS_MONTH]));
		objReportFixedAssets.setDepIncrement(0);
		objReportFixedAssets.setDepDecrement(0);
		objReportFixedAssets.setDepThisMonth(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_DEP_THIS_MONTH]));
		objReportFixedAssets.setBookValue(rs.getDouble(fieldNames[PstReportFixedAssets.FLD_BOOK_VALUE]));
		objReportFixedAssets.setLocationId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_LOCATION_ID]));
		objReportFixedAssets.setPeriodId(lNewPeriod);
		objReportFixedAssets.setGroupId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_GROUP_ID]));
		objReportFixedAssets.setAccountId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_ACCOUNT_ID]));
		objReportFixedAssets.setDepMethodId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_DEP_METHOD_ID]));
		objReportFixedAssets.setDepTypeId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_DEP_TYPE_ID]));
		objReportFixedAssets.setFixedAssetsId(rs.getLong(fieldNames[PstReportFixedAssets.FLD_FIXED_ASSETS_ID]));
		
		try{
		    lResult = insertExc(objReportFixedAssets);
		}catch(Exception e){}
	    }
	    rs.close();
	}catch(Exception e){}
	return lResult;
    }
    
    public static synchronized String getStringQueryClosingReportFA(String whereClause){
	return getStringQuery(0, 0, whereClause, "");
    }
    
    public static synchronized String getStringQuery(int limitStart, int recordToGet, String whereClause, String order){
	String sResult = "";
	try{
	     String sql = "SELECT * FROM " + TBL_REPORT_FIXED_ASSETS + " ";
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }
	    sResult = sql;
	}catch(Exception e){}
	return sResult;
    }
}
