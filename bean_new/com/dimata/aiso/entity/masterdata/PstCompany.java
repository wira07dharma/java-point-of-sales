/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.aiso.db.DBException;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.I_DBInterface;
import com.dimata.aiso.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class PstCompany extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language{
    
    public static final String TBL_COMPANY = "aiso_company";
       
    public static final int FLD_COMPANY_ID = 0;
    public static final int FLD_COMPANY_CODE = 1;
    public static final int FLD_COMPANY_NAME = 2;
    public static final int FLD_PERSON_NAME = 3;
    public static final int FLD_PERSON_LAST_NAME = 4;
    public static final int FLD_BUSS_ADDRESS = 5;
    public static final int FLD_TOWN = 6;
    public static final int FLD_PROVINCE = 7;
    public static final int FLD_COUNTRY = 8;
    public static final int FLD_TELP_NR = 9;
    public static final int FLD_TELP_MOBILE = 10;
    public static final int FLD_FAX = 11;
    public static final int FLD_EMAIL_COMPANY = 12;
    public static final int FLD_POSTAL_CODE = 13;
    public static final int FLD_COMPANY_IMAGE = 14;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };
    //person_lastname
    public static String[] fieldNames = {
        "COMPANY_ID",
        "COMPANY_CODE",
        "COMPANY_NAME",
        "PERSON_NAME",
        "PERSON_LASTNAME",
        "BUSS_ADDRESS",
        "TOWN",
        "PROVINCE",
        "COUNTRY",
        "TELP_NR",
        "TELP_MOBILE",
        "FAX",
        "EMAIL_COMPANY",
        "POSTAL_CODE",
        "COMPANY_IMAGE"
    };
    
     public PstCompany() {
    }

    public PstCompany(int i) throws DBException {
        super(new PstCompany());
    }


    public PstCompany(String sOid) throws DBException {
        super(new PstCompany(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstCompany(long lOid) throws DBException {
        super(new PstCompany(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }

        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;

    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_COMPANY;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCompany().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Company objCompany = PstCompany.fetchExc(ent.getOID());
        ent = (Entity) objCompany;
        return objCompany.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstCompany.insertExc((Company) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((BussinessCenter) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Company fetchExc(long oid) throws DBException {
        try {
            Company objCompany = new Company();
            PstCompany pCompany = new PstCompany(oid);

            objCompany.setOID(oid);
            objCompany.setCompanyCode(pCompany.getString(FLD_COMPANY_CODE));
            objCompany.setCompanyName(pCompany.getString(FLD_COMPANY_NAME));
            objCompany.setPersonName(pCompany.getString(FLD_PERSON_NAME));
            objCompany.setPersonLastName(pCompany.getString(FLD_PERSON_LAST_NAME));
            objCompany.setTown(pCompany.getString(FLD_TOWN));
            objCompany.setProvince(pCompany.getString(FLD_PROVINCE));
            objCompany.setCountry(pCompany.getString(FLD_COUNTRY));
            objCompany.setBussAddress(pCompany.getString(FLD_BUSS_ADDRESS));
            objCompany.setPhoneNr(pCompany.getString(FLD_TELP_NR));
            objCompany.setMobilePh(pCompany.getString(FLD_TELP_MOBILE));
            objCompany.setFax(pCompany.getString(FLD_FAX));
            objCompany.setCompEmail(pCompany.getString(FLD_EMAIL_COMPANY));
            objCompany.setPostalCode(pCompany.getString(FLD_POSTAL_CODE));
            objCompany.setCompImage(pCompany.getString(FLD_COMPANY_IMAGE));
            
            return objCompany;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Company objCompany) throws DBException {
        try {
            PstCompany pCompany = new PstCompany(0);

            pCompany.setString(FLD_COMPANY_CODE, objCompany.getCompanyCode());
            pCompany.setString(FLD_COMPANY_NAME, objCompany.getCompanyName());
            pCompany.setString(FLD_PERSON_NAME, objCompany.getPersonName());
            pCompany.setString(FLD_PERSON_LAST_NAME, objCompany.getPersonLastName());
            pCompany.setString(FLD_TOWN, objCompany.getTown());
            pCompany.setString(FLD_PROVINCE, objCompany.getProvince());
            pCompany.setString(FLD_COUNTRY, objCompany.getCountry());
            pCompany.setString(FLD_TELP_NR, objCompany.getPhoneNr());
            pCompany.setString(FLD_TELP_MOBILE, objCompany.getMobilePh());
            pCompany.setString(FLD_FAX, objCompany.getFax());
            pCompany.setString(FLD_BUSS_ADDRESS, objCompany.getBussAddress());
            pCompany.setString(FLD_EMAIL_COMPANY, objCompany.getCompEmail());
            pCompany.setString(FLD_POSTAL_CODE, objCompany.getPostalCode());
            pCompany.setString(FLD_COMPANY_IMAGE, objCompany.getCompImage());
            
            pCompany.insert();

            objCompany.setOID(pCompany.getlong(FLD_COMPANY_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }
        return objCompany.getOID();
    }

    public static long updateExc(Company objCompany) throws DBException {
        try {
            if (objCompany.getOID() != 0) {
                PstCompany pCompany = new PstCompany(objCompany.getOID());
                
                pCompany.setString(FLD_COMPANY_CODE, objCompany.getCompanyCode());
                pCompany.setString(FLD_COMPANY_NAME, objCompany.getCompanyName());
                pCompany.setString(FLD_PERSON_NAME, objCompany.getPersonName());
                pCompany.setString(FLD_PERSON_LAST_NAME, objCompany.getPersonLastName());
                pCompany.setString(FLD_TOWN, objCompany.getTown());
                pCompany.setString(FLD_PROVINCE, objCompany.getProvince());
                pCompany.setString(FLD_COUNTRY, objCompany.getCountry());
                pCompany.setString(FLD_TELP_NR, objCompany.getPhoneNr());
                pCompany.setString(FLD_TELP_MOBILE, objCompany.getMobilePh());
                pCompany.setString(FLD_FAX, objCompany.getFax());
                pCompany.setString(FLD_BUSS_ADDRESS, objCompany.getBussAddress());
                pCompany.setString(FLD_EMAIL_COMPANY, objCompany.getCompEmail());
                pCompany.setString(FLD_POSTAL_CODE, objCompany.getPostalCode());
                pCompany.setString(FLD_COMPANY_IMAGE, objCompany.getCompImage());
                
                pCompany.update();

                return objCompany.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCompany pCompany = new PstCompany(oid);
            pCompany.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_COMPANY;
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

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Company objCompany = new Company();
                resultToObject(rs, objCompany);
                lists.add(objCompany);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    private static void resultToObject(ResultSet rs, Company objCompany) {
        try {
            objCompany.setOID(rs.getLong(fieldNames[FLD_COMPANY_ID]));
            objCompany.setCompanyCode(rs.getString(fieldNames[FLD_COMPANY_CODE]));
            objCompany.setCompanyName(rs.getString(fieldNames[FLD_COMPANY_NAME]));
            objCompany.setPersonName(rs.getString(fieldNames[FLD_PERSON_NAME]));
            objCompany.setPersonLastName(rs.getString(fieldNames[FLD_PERSON_LAST_NAME]));
            objCompany.setTown(rs.getString(fieldNames[FLD_TOWN]));
            objCompany.setProvince(rs.getString(fieldNames[FLD_PROVINCE]));
            objCompany.setCountry(rs.getString(fieldNames[FLD_COUNTRY]));
            objCompany.setPhoneNr(rs.getString(fieldNames[FLD_TELP_NR]));
            objCompany.setMobilePh(rs.getString(fieldNames[FLD_TELP_MOBILE]));
            objCompany.setFax(rs.getString(fieldNames[FLD_FAX]));
            objCompany.setBussAddress(rs.getString(fieldNames[FLD_BUSS_ADDRESS]));
            objCompany.setCompEmail(rs.getString(fieldNames[FLD_EMAIL_COMPANY]));
            objCompany.setPostalCode(rs.getString(fieldNames[FLD_POSTAL_CODE]));
            objCompany.setCompImage(rs.getString(fieldNames[FLD_COMPANY_IMAGE]));
            
        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }

    public static String getLicenseKey() {
        DBResultSet dbrs = null;
        String result = "";
        try {
            String sql = "SELECT key_license "
                    + " FROM " + PstCompany.TBL_COMPANY + " LIMIT 0,1 ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getString("key_license");
            }
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getLinkAccountStr() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static long getOidCompany() {
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String sql = "SELECT COMPANY_ID "
                    + " FROM " + PstCompany.TBL_COMPANY + " LIMIT 0,1 ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong("COMPANY_ID");
            }
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getLinkAccountStr() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    public static long updateLicenseKey(String licenseKey) throws com.dimata.qdep.db.DBException {
        long hasil = 0;
        try {
            if(!licenseKey.equals("")){
                String sql ="UPDATE "+ PstCompany.TBL_COMPANY +" SET key_license='"+licenseKey+"'";
                int result = execUpdate(sql);
            }
        }
        catch(Exception e) {
            
        }
        return hasil;
    }
    
    public static Hashtable fetchExcHashtable(long oid) throws DBException {
        
        try {
            Hashtable hashCompany = new Hashtable();
            Company company = new Company();
            PstCompany pstCompany = new PstCompany(oid);
            company.setOID(oid);
            
            company.setCompanyCode(pstCompany.getString(FLD_COMPANY_CODE));
            hashCompany.put(fieldNames[FLD_COMPANY_CODE], company.getCompanyCode());
            
            company.setCompanyName(pstCompany.getString(FLD_COMPANY_NAME));
            hashCompany.put(fieldNames[FLD_COMPANY_NAME], company.getCompanyName());
            
            company.setPersonName(pstCompany.getString(FLD_PERSON_NAME));
            hashCompany.put(fieldNames[FLD_PERSON_NAME], company.getPersonName());
            
            company.setPersonLastName(pstCompany.getString(FLD_PERSON_LAST_NAME));
            hashCompany.put(fieldNames[FLD_PERSON_LAST_NAME], company.getPersonLastName());
            
            company.setBussAddress(pstCompany.getString(FLD_BUSS_ADDRESS));
            hashCompany.put(fieldNames[FLD_BUSS_ADDRESS], company.getBussAddress());
            
            company.setTown(pstCompany.getString(FLD_TOWN));
            hashCompany.put(fieldNames[FLD_TOWN], company.getTown());
            
            company.setProvince(pstCompany.getString(FLD_PROVINCE));
            hashCompany.put(fieldNames[FLD_PROVINCE], company.getProvince());
            
            company.setCountry(pstCompany.getString(FLD_COUNTRY));
            hashCompany.put(fieldNames[FLD_COUNTRY], company.getCountry());
            
            company.setPhoneNr(pstCompany.getString(FLD_TELP_NR));
            hashCompany.put(fieldNames[FLD_TELP_NR], company.getPhoneNr());
            
            company.setMobilePh(pstCompany.getString(FLD_TELP_MOBILE));
            hashCompany.put(fieldNames[FLD_TELP_MOBILE], company.getMobilePh());
            
            company.setFax(pstCompany.getString(FLD_FAX));
            hashCompany.put(fieldNames[FLD_FAX], company.getFax());
            
            company.setCompEmail(pstCompany.getString(FLD_EMAIL_COMPANY));
            hashCompany.put(fieldNames[FLD_EMAIL_COMPANY], company.getCompEmail());
            
            company.setPostalCode(pstCompany.getString(FLD_POSTAL_CODE));
            hashCompany.put(fieldNames[FLD_POSTAL_CODE], company.getPostalCode());
            
            return hashCompany;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }
    }
    
}
