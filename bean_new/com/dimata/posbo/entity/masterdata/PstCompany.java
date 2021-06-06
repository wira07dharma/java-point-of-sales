/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Acer
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import static com.dimata.aiso.db.DBHandler.execUpdate;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;
import org.json.JSONObject;


/**
 *
 * @author Dimata 007
 */
public class PstCompany extends DBHandler 
    implements I_DBInterface, I_DBType, I_Language,I_PersintentExc {

    public static final String TBL_AISO_COMPANY = "aiso_company";
    public static final int FLD_COMPANY_ID=0;  
    public static final int FLD_COMPANY_CODE=1; 
    public static final int FLD_COMPANY_NAME=2;
    public static final int FLD_PERSON_NAME=3;
    public static final int FLD_PERSON_LAST_NAME=4;
    public static final int FLD_BUSS_ADDRESS=5;  
    public static final int FLD_TOWN=6; 
    public static final int FLD_PROVINCE=7;
    public static final int FLD_COUNTRY=8;
    public static final int FLD_TELP_NR=9;
    public static final int FLD_TELP_MOBILE=10;  
    public static final int FLD_FAX=11; 
    public static final int FLD_EMAIL_COMPANY=12;
    public static final int FLD_POSTAL_CODE=13;
   
    /**
     * KETERANGAN:NAMA FIELD YANG ADA PADA TABLE EMPLOYEE
     */
    public static String[] fieldNames = {
        "company_id",
        "company_code",
        "company_name",
        "person_name",
        "person_lastname",
        "buss_address",
        "town",
        "province",
        "country",
        "telp_nr",
        "telp_mobile",
        "fax",
        "email_company",
        "postal_code"
        
    };
    /**
     * KETERANGAN : TYPE DATA YG ADA DI TABALE EMPLOYEE
     */
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
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
       
    };

    /**
     * Keterangan: ini untuk membuat constractor dari sebuah object, 
     * impelamentasinya yaitu new PstCompany();
     */
    public PstCompany() {
    }

    public PstCompany(int i) throws DBException {

        super(new PstCompany());//merupakan induk constarktor dari DBHandler lalu membuat baru PstCompany


    }

    public PstCompany(String sOid) throws DBException {

        super(new PstCompany(0));//merupakan induk construktor dari DBHandler lalu membuat new PstCompany lalu memberi nilai defaoult 0

        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }

    }

    public PstCompany(long lOid) throws DBException {

        super(new PstCompany(0));//merupakan induk construktor dari DBHandler, 0 fungsinya sebagai default PSTGEJALA

        String sOid = "0";

        try {

            sOid = String.valueOf(lOid);

        } catch (Exception e) {

            throw new DBException(this, DBException.RECORD_NOT_FOUND);

        }

        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }

    }
    
    
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_AISO_COMPANY;
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

       /**
     * Keterangan: untuk mengambil data dari 
     * database berdasarkan oid oidEmployee dan kemudaian di set objecknya
     * @param oid : oidEmployee
     * @return
     * @throws DBException 
     */
    
    public static Company fetchExc(long oid) throws DBException {

        try {
            Company company = new Company();
            PstCompany pstCompany = new PstCompany(oid);
            company.setOID(oid);
            company.setCompanyCode(pstCompany.getString(FLD_COMPANY_CODE));
            company.setCompanyName(pstCompany.getString(FLD_COMPANY_NAME));
            company.setPersonName(pstCompany.getString(FLD_PERSON_NAME));
            company.setPersonLastName(pstCompany.getString(FLD_PERSON_LAST_NAME));
             company.setBusAddress(pstCompany.getString(FLD_BUSS_ADDRESS));
            company.setTown(pstCompany.getString(FLD_TOWN));
            company.setProvince(pstCompany.getString(FLD_PROVINCE));
            company.setCountry(pstCompany.getString(FLD_COUNTRY));
             company.setTelpNr(pstCompany.getString(FLD_TELP_NR));
            company.setTelpMobile(pstCompany.getString(FLD_TELP_MOBILE));
            company.setFax(pstCompany.getString(FLD_FAX));
            company.setEmailCompany(pstCompany.getString(FLD_EMAIL_COMPANY));
            company.setPostalCode(pstCompany.getString(FLD_POSTAL_CODE));
            
            return company;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }
    }
   public long fetchExc(Entity entity) throws Exception {
        Company company = fetchExc(entity.getOID());
        entity = (Entity)company;
        return company.getOID();
    }
    
 /**
  * Keterangan: fungsi untuk update data to database
  * create by  satrya 2013-09-27
  * @param company
  * @return
  * @throws DBException 
  */
    public static synchronized long updateExc(Company company) throws DBException {
        try {
            if (company.getOID() != 0) {
                PstCompany pstCompany = new PstCompany(company.getOID());
                pstCompany.setString(FLD_COMPANY_CODE, company.getCompanyCode());//getNamaEmployee = value();
                pstCompany.setString(FLD_COMPANY_NAME, company.getCompanyName());
                pstCompany.setString(FLD_PERSON_NAME, company.getPersonName());
                pstCompany.setString(FLD_PERSON_LAST_NAME, company.getPersonLastName());
                pstCompany.setString(FLD_BUSS_ADDRESS, company.getBusAddress());//getNamaEmployee = value();
                pstCompany.setString(FLD_TOWN, company.getTown());
                pstCompany.setString(FLD_PROVINCE, company.getProvince());
              
                pstCompany.setString(FLD_COUNTRY, company.getCountry());
                pstCompany.setString(FLD_TELP_NR, company.getTelpNr());//getNamaEmployee = value();
                pstCompany.setString(FLD_TELP_MOBILE, company.getTelpMobile());
                pstCompany.setString(FLD_FAX, company.getFax());
                pstCompany.setString(FLD_EMAIL_COMPANY, company.getEmailCompany());
                pstCompany.setString(FLD_POSTAL_CODE, company.getPostalCode());
                pstCompany.update();

                return company.getOID();
                
                
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }

        return 0;

    }

    
    public long updateExc(Entity entity) throws Exception {
        return updateExc((Company)entity);
    }

    
 /**
 * Keterangan: delete data Company
 * @param oid: oid dari Company Id
 * @return
 * @throws DBException 
 */
    public static synchronized long deleteExc(long oid) throws DBException {

        try {

            PstCompany pstCompany = new PstCompany(oid);

            pstCompany.delete();

        } catch (DBException dbe) {

            throw dbe;

        } catch (Exception e) {

            throw new DBException(new PstCompany(0), DBException.UNKNOWN);

        }

        return oid;

    }
    public long deleteExc(Entity entity) throws Exception {
        if(entity==null){
            throw  new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }
    
    
/**
     * Ketrangan: fungsi untuk melakukan insert to database
     * @param Company
     * @return
     * @throws DBException 
     */
    public static synchronized long insertExc(Company company) 
            throws DBException {
    try {

            PstCompany pstCompany = new PstCompany(0);
            pstCompany.setString(FLD_COMPANY_CODE, company.getCompanyCode());//getNamaEmployee = value();
                pstCompany.setString(FLD_COMPANY_NAME, company.getCompanyName());
                pstCompany.setString(FLD_PERSON_NAME, company.getPersonName());
                pstCompany.setString(FLD_PERSON_LAST_NAME, company.getPersonLastName());
                pstCompany.setString(FLD_BUSS_ADDRESS, company.getBusAddress());//getNamaEmployee = value();
                pstCompany.setString(FLD_TOWN, company.getTown());
                pstCompany.setString(FLD_PROVINCE, company.getProvince());
              
                pstCompany.setString(FLD_COUNTRY, company.getCountry());
                pstCompany.setString(FLD_TELP_NR, company.getTelpNr());//getNamaEmployee = value();
                pstCompany.setString(FLD_TELP_MOBILE, company.getTelpMobile());
                pstCompany.setString(FLD_FAX, company.getFax());
                pstCompany.setString(FLD_EMAIL_COMPANY, company.getEmailCompany());
                pstCompany.setString(FLD_POSTAL_CODE, company.getPostalCode());
            pstCompany.insert();
            
            long oidDataSync=PstDataSyncSql.insertExc(pstCompany.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            company.setOID(pstCompany.getlong(FLD_COMPANY_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCompany(0), DBException.UNKNOWN);
        }
        return company.getOID();
    }
    public long insertExc(Entity entity) throws Exception {
         return insertExc((Company)entity);
    }
    
     public static void resultToObject(ResultSet rs, Company company) { //memasukkan nilai atribut ke object

        try {

company.setOID(rs.getLong(PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]));
//set OID company dari FLD_company_ID
company.setCompanyCode(rs.getString(PstCompany.fieldNames[PstCompany.FLD_COMPANY_CODE]));
company.setCompanyName(rs.getString(PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME]));
company.setPersonName(rs.getString(PstCompany.fieldNames[PstCompany.FLD_PERSON_NAME]));
company.setPersonLastName(rs.getString(PstCompany.fieldNames[PstCompany.FLD_PERSON_LAST_NAME]));
company.setBusAddress(rs.getString(PstCompany.fieldNames[PstCompany.FLD_BUSS_ADDRESS]));
company.setTown(rs.getString(PstCompany.fieldNames[PstCompany.FLD_TOWN]));
company.setProvince(rs.getString(PstCompany.fieldNames[PstCompany.FLD_PROVINCE]));
company.setCountry(rs.getString(PstCompany.fieldNames[PstCompany.FLD_COUNTRY]));
company.setTelpNr(rs.getString(PstCompany.fieldNames[PstCompany.FLD_TELP_NR]));
company.setTelpMobile(rs.getString(PstCompany.fieldNames[PstCompany.FLD_TELP_MOBILE]));
company.setFax(rs.getString(PstCompany.fieldNames[PstCompany.FLD_FAX]));
company.setEmailCompany(rs.getString(PstCompany.fieldNames[PstCompany.FLD_EMAIL_COMPANY]));
company.setPostalCode(rs.getString(PstCompany.fieldNames[PstCompany.FLD_POSTAL_CODE]));
        } catch (Exception e) {
        }

    }
    /**
     * KETERANGAN: Fungsi untuk melakukan list table company , berdasarkan parameter di bawah
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return 
     */
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {

        Vector lists = new Vector();

        DBResultSet dbrs = null;

        try {

            String sql = " SELECT * FROM " + TBL_AISO_COMPANY;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);

            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Company company = new Company();
                resultToObject(rs, company);
                lists.add(company);
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
    
    public static Vector listAll() {
        return list(0, 500, "", "");
    }
    
    public static boolean checkOID(long mSId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_COMPANY + " WHERE "
                    + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] + " = " + mSId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] 
                    + ") FROM " + TBL_AISO_COMPANY;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);//execute query sql
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);//ambil isi ResultSet yg 1 atau PstCompany.fieldNames[PstCompany.FLD_JENIS_ITEM_ID] 
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    /**
     * keterangan: limit
     * @param oid : ini merupakan oid jenis Item
     * @param recordToGet
     * @param whereClause
     * @param orderClause
     * @return 
     */
    public static int findLimitStart(long oid, int recordToGet
            , String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Company jenisItems = (Company) list.get(ls);
                    if (oid == jenisItems.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }
        return start;
    }
    /* This method used to find command where current data */

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }

    
    public static String getLicenseKey() {
        com.dimata.aiso.db.DBResultSet dbrs = null;
        String result = "";
        try {
            String sql = "SELECT key_license "
                    + " FROM " + PstCompany.TBL_AISO_COMPANY + " LIMIT 0,1 ";

            dbrs = com.dimata.aiso.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getString("key_license");
            }
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getLinkAccountStr() err : " + e.toString());
        } finally {
            com.dimata.aiso.db.DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    public static long updateLicenseKey(String licenseKey) throws com.dimata.qdep.db.DBException {
        long hasil = 0;
        try {
            if(!licenseKey.equals("")){
                String sql ="UPDATE "+ PstCompany.TBL_AISO_COMPANY +" SET key_license='"+licenseKey+"'";
                int result = execUpdate(sql);
            }
        }
        catch(Exception e) {
            
        }
        return hasil;
    }
        
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Company company = PstCompany.fetchExc(oid);
                object.put(PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID], company.getOID());
                object.put(PstCompany.fieldNames[PstCompany.FLD_BUSS_ADDRESS], company.getBusAddress());
                object.put(PstCompany.fieldNames[PstCompany.FLD_COMPANY_CODE], company.getCompanyCode());
                object.put(PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME], company.getCompanyName());
                object.put(PstCompany.fieldNames[PstCompany.FLD_COUNTRY], company.getCountry());
                object.put(PstCompany.fieldNames[PstCompany.FLD_FAX], company.getFax());
                object.put(PstCompany.fieldNames[PstCompany.FLD_EMAIL_COMPANY], company.getEmailCompany());
                object.put(PstCompany.fieldNames[PstCompany.FLD_PERSON_LAST_NAME], company.getPersonLastName());
                object.put(PstCompany.fieldNames[PstCompany.FLD_PERSON_NAME], company.getPersonName());
                object.put(PstCompany.fieldNames[PstCompany.FLD_POSTAL_CODE], company.getPostalCode());
                object.put(PstCompany.fieldNames[PstCompany.FLD_PROVINCE], company.getProvince());
                object.put(PstCompany.fieldNames[PstCompany.FLD_TELP_MOBILE], company.getTelpMobile());
                object.put(PstCompany.fieldNames[PstCompany.FLD_TELP_NR], company.getTelpNr());
                object.put(PstCompany.fieldNames[PstCompany.FLD_TOWN], company.getTown());
            }catch(Exception exc){}

            return object;
        }
}
