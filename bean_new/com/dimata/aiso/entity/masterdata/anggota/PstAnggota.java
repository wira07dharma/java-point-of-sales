/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.region.PstCity;
import com.dimata.aiso.entity.masterdata.region.PstProvince;
import com.dimata.aiso.entity.masterdata.region.PstRegency;
import com.dimata.aiso.entity.masterdata.region.PstSubRegency;
import com.dimata.aiso.entity.masterdata.region.PstWard;
import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.contact.PstContactClassAssign;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author HaddyPuutraa (PKL) Created Kamis, 21 Pebruari 2013
 */
public class PstAnggota extends DBHandler implements I_Language, I_DBType, I_DBInterface, I_PersintentExc {

    public static final String TBL_ANGGOTA = "contact_list";

    public static final int FLD_ID_ANGGOTA = 0;
    public static final int FLD_NO_ANGGOTA = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_SEX = 3;
    public static final int FLD_BIRTH_PLACE = 4;
    public static final int FLD_BIRTH_DATE = 5;
    public static final int FLD_VOCATION_ID = 6;
    public static final int FLD_OFFICE_ADDRESS = 7;
    public static final int FLD_ADDR_OFFICE_CITY = 8;
    public static final int FLD_POSITION_ID = 9;
    public static final int FLD_ADDR_PERMANENT = 10;
    public static final int FLD_ADDR_CITY_PERMANENT = 11;
    public static final int FLD_ADDR_PROVINCE_ID = 12;
    public static final int FLD_ADDR_PMNT_REGENCY_ID = 13;
    public static final int FLD_ADDR_PMNT_SUBREGENCY_ID = 14;
    public static final int FLD_WARD_ID = 15;
    public static final int FLD_ID_CARD = 16;
    public static final int FLD_EXPIRED_DATE_KTP = 17;
    public static final int FLD_TLP = 18;
    public static final int FLD_HANDPHONE = 19;
    public static final int FLD_EMAIL = 20;

    //Tambahan Hari Selasa, 26 Pebruari 2013
    public static final int FLD_NO_NPWP = 21;
    public static final int FLD_STATUS = 22;

    //tambahn tanggal 5 maret 2013
    public static final int FLD_NO_REKENING = 23;
    public static final int FLD_REGRISTATION_DATE = 24;
    
    public static final int FLD_LAST_UPDATE = 25;
    
    public static final int FLD_NO_KARTU_KELUARGA = 26;
    public static final int FLD_FOTO_ANGGOTA = 27;
    public static final int FLD_ASSIGN_LOCATION_ID = 28;

    public static String[] fieldNames = {
        "CONTACT_ID",//0
        "CONTACT_CODE",//1 - cif
        "PERSON_NAME",//2
        "MEMBER_SEX",//3
        "MEMBER_BIRTH_PLACE",//4
        "MEMBER_BIRTH_DATE",//5
        "MEMBER_VOCATION_ID",//6
        "COMP_ADDRESS",//7
        "MEMBER_COMP_CITY", //update dari type string menjadi ID
        "POSITION_ID",//9
        "HOME_ADDRESS",//10
        "MEMBER_CITY_ID",//11
        "MEMBER_PROVINCE_ID",//12
        "MEMBER_PMNT_REGENCY_ID",//13
        "MEMBER_PMNT_SUBREGENCY_ID",//14
        "WARD_ID",//15
        "KTP_NO",//16
        "EXPIRED_DATE_KTP",//17
        "TELP_NR",//18
        "TELP_MOBILE",//19
        "EMAIL",//20
        "NO_NPWP",//21
        "MEMBER_STATUS",//22
        "NO_REKENING",//23
        "REGDATE",//24
        "LAST_UPDATE",//25
        "NO_KARTU_KELUARGA",//26
        "FOTO_ANGGOTA",//27
		"ASSIGN_LOCATION_ID"//28
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_ID + TYPE_PK,//0
        TYPE_STRING,//1
        TYPE_STRING,//2
        TYPE_INT,//3
        TYPE_STRING,//4
        TYPE_DATE,//5
        TYPE_LONG,//6
        TYPE_STRING,//7
        TYPE_LONG, // 8 update tanggal 27 Pebruari 2013 oleh Hadi
        TYPE_LONG,// 9
        TYPE_STRING,//10
        TYPE_LONG,//11
        TYPE_LONG,//12
        TYPE_LONG,//13
        TYPE_LONG,//14
        TYPE_LONG,//15
        TYPE_STRING,//16
        TYPE_DATE,//17
        TYPE_STRING,//18
        TYPE_STRING,//19
        TYPE_STRING,//20
        TYPE_STRING,//21
        TYPE_INT,//22
        TYPE_STRING,//23
        TYPE_DATE,//24
        TYPE_DATE,//25
        TYPE_STRING,//26
        TYPE_STRING, //27
		TYPE_LONG//28
    };

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static final String[][] sexKey = {{"Laki-Laki", "Perempuan"}, {"Male", "Female"}};
    public static final int[] sexValue = {0, 1};

    //Tambahan tanggal 26 Pebruari 2013
    public static final int DRAFT = 0;
    public static final int ACTIVE = 1;
    public static final int MUTASI = 2;

    public static final String[] statusKey = {"Draft", "Active", "Mutasi"};
    public static final int[] statusValue = {0, 1, 2};

    public static Vector getStatusKey() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < statusKey.length; i++) {
            result.add(statusKey[i]);
        }
        return result;
    }

    public static Vector getStatusValue() {
        Vector value = new Vector(1, 1);
        for (int i = 0; i < statusValue.length; i++) {
            value.add(Integer.toString(i));
        }
        return value;
    }

    public int index;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_ANGGOTA;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAnggota().getClass().getName();
    }

    public PstAnggota() {
    }

    public PstAnggota(int i) throws DBException {
        super(new PstAnggota());
    }

    public PstAnggota(String sOid) throws DBException {
        super(new PstAnggota(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAnggota(long lOid) throws DBException {
        super(new PstAnggota(0));
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

    public static Anggota fetchExc(long oid) {
        Anggota anggota = new Anggota();
        try {
            PstAnggota pstAnggota = new PstAnggota(oid);
            anggota.setOID(oid);

            anggota.setNoAnggota(pstAnggota.getString(FLD_NO_ANGGOTA));
            anggota.setName(pstAnggota.getString(FLD_NAME));
            anggota.setSex(pstAnggota.getInt(FLD_SEX));
            anggota.setBirthPlace(pstAnggota.getString(FLD_BIRTH_PLACE));
            anggota.setBirthDate(pstAnggota.getDate(FLD_BIRTH_DATE));
            anggota.setVocationId(pstAnggota.getlong(FLD_VOCATION_ID));
        //    anggota.setAgencies(pstAnggota.getString(FLD_AGENCIES));
            anggota.setOfficeAddress(pstAnggota.getString(FLD_OFFICE_ADDRESS));
            anggota.setAddressOfficeCity(pstAnggota.getlong(FLD_ADDR_OFFICE_CITY)); //update tanggal 26 Pebruari 2013 oleh Hadi
//            anggota.setPositionId(pstAnggota.getlong(FLD_POSITION_ID));
            anggota.setAddressPermanent(pstAnggota.getString(FLD_ADDR_PERMANENT));
//            anggota.setAddressCityPermanentId(pstAnggota.getlong(FLD_ADDR_CITY_PERMANENT));
//            anggota.setAddressProvinceId(pstAnggota.getlong(FLD_ADDR_PROVINCE_ID));
//            anggota.setAddressPermanentRegencyId(pstAnggota.getlong(FLD_ADDR_PMNT_REGENCY_ID));
//            anggota.setAddressPermanentSubRegencyId(pstAnggota.getlong(FLD_ADDR_PMNT_SUBREGENCY_ID));
//            anggota.setWardId(pstAnggota.getlong(FLD_WARD_ID));
            anggota.setIdCard(pstAnggota.getString(FLD_ID_CARD));
//            anggota.setExpiredDateKtp(pstAnggota.getDate(FLD_EXPIRED_DATE_KTP));
            anggota.setTelepon(pstAnggota.getString(FLD_TLP));
            anggota.setHandPhone(pstAnggota.getString(FLD_HANDPHONE));
            anggota.setEmail(pstAnggota.getString(FLD_EMAIL));

            anggota.setNoNpwp(pstAnggota.getString(FLD_NO_NPWP));
            anggota.setStatus(pstAnggota.getInt(FLD_STATUS));

//            anggota.setNoRekening(pstAnggota.getString(FLD_NO_REKENING));
            anggota.setTanggalRegistrasi(pstAnggota.getDate(FLD_REGRISTATION_DATE));
            anggota.setNoKartuKeluarga(pstAnggota.getString(FLD_NO_KARTU_KELUARGA));
            anggota.setFotoAnggota(pstAnggota.getString(FLD_FOTO_ANGGOTA));
            anggota.setAssignLocation(pstAnggota.getlong(FLD_ASSIGN_LOCATION_ID));

        } catch (DBException dbe) {
            System.err.println(dbe);
        } catch (Exception e) {
            System.err.println(e);
        }
        return anggota;
    }

    public long fetchExc(Entity ent) throws Exception {
        Anggota anggota = fetchExc(ent.getOID());
        ent = (Entity) anggota;
        return anggota.getOID();
    }

    public static long insertExc(Anggota anggota) throws DBException {
        try {
            PstAnggota pstAnggota = new PstAnggota(0);

            pstAnggota.setString(FLD_NO_ANGGOTA, anggota.getNoAnggota());
            pstAnggota.setString(FLD_NAME, anggota.getName());
            pstAnggota.setInt(FLD_SEX, anggota.getSex());
            pstAnggota.setString(FLD_BIRTH_PLACE, anggota.getBirthPlace());
            pstAnggota.setDate(FLD_BIRTH_DATE, anggota.getBirthDate());
            pstAnggota.setLong(FLD_VOCATION_ID, anggota.getVocationId());
          //  pstAnggota.setString(FLD_AGENCIES, anggota.getAgencies());
            pstAnggota.setString(FLD_OFFICE_ADDRESS, anggota.getOfficeAddress());
            pstAnggota.setLong(FLD_ADDR_OFFICE_CITY, anggota.getAddressOfficeCity()); //update tanggal 26 Pebruari 2013 oleh Hadi
//            pstAnggota.setLong(FLD_POSITION_ID, anggota.getPositionId());
            pstAnggota.setString(FLD_ADDR_PERMANENT, anggota.getAddressPermanent());
//            pstAnggota.setLong(FLD_ADDR_CITY_PERMANENT, anggota.getAddressCityPermanentId());
//            pstAnggota.setLong(FLD_ADDR_PROVINCE_ID, anggota.getAddressProvinceId());
//            pstAnggota.setLong(FLD_ADDR_PMNT_REGENCY_ID, anggota.getAddressPermanentRegencyId());
//            pstAnggota.setLong(FLD_ADDR_PMNT_SUBREGENCY_ID, anggota.getAddressPermanentSubRegencyId());
//            pstAnggota.setLong(FLD_WARD_ID, anggota.getWardId());
            pstAnggota.setString(FLD_ID_CARD, anggota.getIdCard());
//            pstAnggota.setDate(FLD_EXPIRED_DATE_KTP, anggota.getExpiredDateKtp());
            pstAnggota.setString(FLD_TLP, anggota.getTelepon());
            pstAnggota.setString(FLD_HANDPHONE, anggota.getHandPhone());
            pstAnggota.setString(FLD_EMAIL, anggota.getEmail());

            pstAnggota.setString(FLD_NO_NPWP, anggota.getNoNpwp());
            pstAnggota.setInt(FLD_STATUS, anggota.getStatus());

//            pstAnggota.setString(FLD_NO_REKENING, anggota.getNoRekening());
            pstAnggota.setDate(FLD_REGRISTATION_DATE, new Date());
            pstAnggota.setString(FLD_NO_KARTU_KELUARGA, anggota.getNoKartuKeluarga());
            pstAnggota.setString(FLD_FOTO_ANGGOTA, anggota.getFotoAnggota());
            pstAnggota.setLong(FLD_ASSIGN_LOCATION_ID, anggota.getAssignLocation());
            
            pstAnggota.insert();
            anggota.setOID(pstAnggota.getlong(FLD_ID_ANGGOTA));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggota(0), DBException.UNKNOWN);
        }
        return anggota.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Anggota) ent);
    }

    public static long updateExc(Anggota anggota) throws DBException {
        try {
            if (anggota.getOID() != 0) {
                PstAnggota pstAnggota = new PstAnggota(anggota.getOID());

                pstAnggota.setString(FLD_NO_ANGGOTA, anggota.getNoAnggota());
                pstAnggota.setString(FLD_NAME, anggota.getName());
                pstAnggota.setInt(FLD_SEX, anggota.getSex());
                pstAnggota.setString(FLD_BIRTH_PLACE, anggota.getBirthPlace());
                pstAnggota.setDate(FLD_BIRTH_DATE, anggota.getBirthDate());
                pstAnggota.setLong(FLD_VOCATION_ID, anggota.getVocationId());
              //  pstAnggota.setString(FLD_AGENCIES, anggota.getAgencies());
                pstAnggota.setString(FLD_OFFICE_ADDRESS, anggota.getOfficeAddress());
                pstAnggota.setLong(FLD_ADDR_OFFICE_CITY, anggota.getAddressOfficeCity());  //update tanggal 26 Pebruari 2013 oleh Hadi
//                pstAnggota.setLong(FLD_POSITION_ID, anggota.getPositionId());
                pstAnggota.setString(FLD_ADDR_PERMANENT, anggota.getAddressPermanent());
//                pstAnggota.setLong(FLD_ADDR_CITY_PERMANENT, anggota.getAddressCityPermanentId());
//                pstAnggota.setLong(FLD_ADDR_PROVINCE_ID, anggota.getAddressProvinceId());
//                pstAnggota.setLong(FLD_ADDR_PMNT_REGENCY_ID, anggota.getAddressPermanentRegencyId());
//                pstAnggota.setLong(FLD_ADDR_PMNT_SUBREGENCY_ID, anggota.getAddressPermanentSubRegencyId());
//                pstAnggota.setLong(FLD_WARD_ID, anggota.getWardId());
                pstAnggota.setString(FLD_ID_CARD, anggota.getIdCard());
//                pstAnggota.setDate(FLD_EXPIRED_DATE_KTP, anggota.getExpiredDateKtp());
                pstAnggota.setString(FLD_TLP, anggota.getTelepon());
                pstAnggota.setString(FLD_HANDPHONE, anggota.getHandPhone());
                pstAnggota.setString(FLD_EMAIL, anggota.getEmail());

                pstAnggota.setString(FLD_NO_NPWP, anggota.getNoNpwp());
                pstAnggota.setInt(FLD_STATUS, anggota.getStatus());

//                pstAnggota.setString(FLD_NO_REKENING, anggota.getNoRekening());
                //pstAnggota.setDate(FLD_REGRISTATION_DATE, anggota.getTanggalRegistrasi());
                pstAnggota.setString(FLD_NO_KARTU_KELUARGA, anggota.getNoKartuKeluarga());
                pstAnggota.setString(FLD_FOTO_ANGGOTA, anggota.getFotoAnggota());
	            pstAnggota.setLong(FLD_ASSIGN_LOCATION_ID, anggota.getAssignLocation());
                
                pstAnggota.update();
                return anggota.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggota(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Anggota) ent);
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstAnggota pstAnggota = new PstAnggota(oid);
            pstAnggota.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggota(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA] + ") " + " FROM " + TBL_ANGGOTA;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
   public static int getCountJoin(String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
         int count = 0;
        try {
            String sql = "SELECT COUNT(cl."+PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA] +") FROM " + TBL_ANGGOTA +" cl "
                       + " INNER JOIN "+PstContactClassAssign.TBL_CNT_CLS_ASSIGN+" cca ON cl."+fieldNames[FLD_ID_ANGGOTA]+"=cca."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+" " 
                       + " INNER JOIN "+PstContactClass.TBL_CONTACT_CLASS+" cc ON cca."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+" =cc."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
             return count;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
         return count;
    }
    
    public static Vector listAll() {
        return list(0, 0, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_ANGGOTA;

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
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Anggota anggota = new Anggota();
                resultToObject(rs, anggota);
                lists.add(anggota);
            }
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT cl.* FROM " + TBL_ANGGOTA +" cl "
                       + " INNER JOIN "+PstContactClassAssign.TBL_CNT_CLS_ASSIGN+" cca ON cl."+fieldNames[FLD_ID_ANGGOTA]+"=cca."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+" " 
                       + " INNER JOIN "+PstContactClass.TBL_CONTACT_CLASS+" cc ON cca."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+" =cc."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
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
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Anggota anggota = new Anggota();
                resultToObject(rs, anggota);
                lists.add(anggota);
            }
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    private static void resultToObject(ResultSet rs, Anggota anggota) {
        try {
            anggota.setOID(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA]));
            anggota.setNoAnggota(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_NO_ANGGOTA]));
            anggota.setName(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_NAME]));
            anggota.setSex(rs.getInt(PstAnggota.fieldNames[PstAnggota.FLD_SEX]));
            anggota.setBirthPlace(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_BIRTH_PLACE]));
            anggota.setBirthDate(rs.getDate(PstAnggota.fieldNames[PstAnggota.FLD_BIRTH_DATE]));
            anggota.setVocationId(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_VOCATION_ID]));
           // anggota.setAgencies(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_AGENCIES]));
            anggota.setOfficeAddress(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_OFFICE_ADDRESS]));
            anggota.setAddressOfficeCity(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_ADDR_OFFICE_CITY])); //update tanggal 26 Pebruari 2013 oleh Hadi
//            anggota.setPositionId(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_POSITION_ID]));
            anggota.setAddressPermanent(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_ADDR_PERMANENT]));
//            anggota.setAddressCityPermanentId(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_ADDR_CITY_PERMANENT]));
//            anggota.setAddressProvinceId(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_ADDR_PROVINCE_ID]));
//            anggota.setAddressPermanentRegencyId(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_ADDR_PMNT_REGENCY_ID]));
//            anggota.setAddressPermanentSubRegencyId(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_ADDR_PMNT_SUBREGENCY_ID]));
//            anggota.setWardId(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_WARD_ID]));
            anggota.setIdCard(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_ID_CARD]));
//            anggota.setExpiredDateKtp(rs.getDate(PstAnggota.fieldNames[PstAnggota.FLD_EXPIRED_DATE_KTP]));
            anggota.setTelepon(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_TLP]));
            anggota.setHandPhone(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_HANDPHONE]));
            anggota.setEmail(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_EMAIL]));

            anggota.setNoNpwp(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_NO_NPWP]));
            anggota.setStatus(rs.getInt(PstAnggota.fieldNames[PstAnggota.FLD_STATUS]));

//            anggota.setNoRekening(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_NO_REKENING]));
            anggota.setTanggalRegistrasi(rs.getDate(PstAnggota.fieldNames[PstAnggota.FLD_REGRISTATION_DATE]));
            
            anggota.setLastUpdate(rs.getDate(PstAnggota.fieldNames[PstAnggota.FLD_LAST_UPDATE]));
            anggota.setNoKartuKeluarga(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_NO_KARTU_KELUARGA]));
            anggota.setFotoAnggota(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_FOTO_ANGGOTA]));
            anggota.setAssignLocation(rs.getLong(PstAnggota.fieldNames[PstAnggota.FLD_ASSIGN_LOCATION_ID]));
		
        } catch (Exception e) {
        }
    }

    /*
     * Join Tabel untuk mencari alamat geo anggota berdasarkan OId update tanggal 9 Maret 2013
     * @param oidAnggota 
     */
    public static void addressAnggotaGeo(Anggota objEntity) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT a." + fieldNames[FLD_NO_ANGGOTA] + ", "
                    + "b." + PstProvince.fieldNames[PstProvince.FLD_PROVINCE_NAME] + ", "
                    + "c." + PstCity.fieldNames[PstCity.FLD_CITY_NAME] + ", "
                    + "d." + PstRegency.fieldNames[PstRegency.FLD_REGENCY_NAME] + ", "
                    + "e." + PstSubRegency.fieldNames[PstSubRegency.FLD_SUBREGENCY_NAME] + ", "
                    + "f." + PstWard.fieldNames[PstWard.FLD_WARD_NAME] + " FROM " + TBL_ANGGOTA + " a "
                    + "JOIN " + PstProvince.TBL_PROVINCE + " b ON a." + fieldNames[FLD_ADDR_PROVINCE_ID] + " = b." + PstProvince.fieldNames[PstProvince.FLD_PROVINCE_ID] + " "
                    + "JOIN " + PstCity.TBL_TB_CITY + " c ON a." + fieldNames[FLD_ADDR_CITY_PERMANENT] + " = c." + PstCity.fieldNames[PstCity.FLD_CITY_ID] + " "
                    + "JOIN " + PstRegency.TBL_REGENCY + " d ON a." + fieldNames[FLD_ADDR_PMNT_REGENCY_ID] + " = d." + PstRegency.fieldNames[PstRegency.FLD_ADDR_REGENCY_ID] + " "
                    + "JOIN " + PstSubRegency.TBL_SUBREGENCY + " e ON a." + fieldNames[FLD_ADDR_PMNT_SUBREGENCY_ID] + " = e." + PstSubRegency.fieldNames[PstSubRegency.FLD_ADDR_SUBREGENCY_ID] + " "
                    + "JOIN " + PstWard.TBL_WARD + " f ON a." + fieldNames[FLD_WARD_ID] + " = f." + PstWard.fieldNames[PstWard.FLD_WARD_ID] + " "
                    + "AND " + fieldNames[FLD_ID_ANGGOTA] + " = " + objEntity.getOID();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                objEntity.setGeoAddressPermanent(resultAnggotaGeo(rs));
            }
        } catch (Exception e) {

        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static String resultAnggotaGeo(ResultSet rs) {
        String anggotaGeo = "";
        try {
            Vector row = new Vector(1, 1);
            row.add(rs.getString(PstProvince.fieldNames[PstProvince.FLD_PROVINCE_NAME]) + ",");
            row.add(rs.getString(PstCity.fieldNames[PstCity.FLD_CITY_NAME]) + ",");
            row.add(rs.getString(PstRegency.fieldNames[PstRegency.FLD_REGENCY_NAME]) + ",");
            row.add(rs.getString(PstSubRegency.fieldNames[PstSubRegency.FLD_SUBREGENCY_NAME]) + ",");
            row.add(rs.getString(PstWard.fieldNames[PstWard.FLD_WARD_NAME]) + "");

            for (int i = 0; i < row.size(); i++) {
                anggotaGeo = anggotaGeo + row.get(i);
            }
            return anggotaGeo;
        } catch (Exception e) {

        }
        return anggotaGeo;
    }
    
    public static Hashtable fetchExcHashtable(long oid) throws DBException {
        
        try {
            Hashtable hashtableAnggota = new Hashtable();
            Anggota anggota = new Anggota();
            PstAnggota pstAnggota = new PstAnggota(oid);
            anggota.setOID(oid);
            
            anggota.setNoAnggota(pstAnggota.getString(FLD_NO_ANGGOTA));
            hashtableAnggota.put(fieldNames[FLD_NO_ANGGOTA], (anggota.getNoAnggota() != null ? anggota.getNoAnggota() : "-"));
            
            anggota.setName(pstAnggota.getString(FLD_NAME));
            hashtableAnggota.put(fieldNames[FLD_NAME], (anggota.getName() != null ? anggota.getName() : "-"));
            
            anggota.setSex(pstAnggota.getInt(FLD_SEX));
            if (anggota.getSex()==PstAnggota.MALE){
                hashtableAnggota.put(fieldNames[FLD_SEX], "Laki-Laki");
            } else {
                hashtableAnggota.put(fieldNames[FLD_SEX], "Perempuan");
            }
            
            anggota.setBirthPlace(pstAnggota.getString(FLD_BIRTH_PLACE));
            hashtableAnggota.put(fieldNames[FLD_BIRTH_PLACE], (anggota.getBirthPlace() != null ? anggota.getBirthPlace() : "-"));
            
            anggota.setBirthDate(pstAnggota.getDate(FLD_BIRTH_DATE));
            hashtableAnggota.put(fieldNames[FLD_BIRTH_DATE], (anggota.getBirthDate() != null ? Formater.formatDate(anggota.getBirthDate(), "yyyy-MM-dd") : "-"));
            
            anggota.setVocationId(pstAnggota.getlong(FLD_VOCATION_ID));
            Vocation vocation = new Vocation();
            try {
                vocation = PstVocation.fetchExc(anggota.getVocationId());
            } catch (Exception exc){}
            hashtableAnggota.put(fieldNames[FLD_VOCATION_ID], vocation.getVocationName());
            
            anggota.setOfficeAddress(pstAnggota.getString(FLD_OFFICE_ADDRESS));
            hashtableAnggota.put(fieldNames[FLD_OFFICE_ADDRESS], (anggota.getOfficeAddress() != null ? anggota.getOfficeAddress() : "-"));
 
            anggota.setAddressPermanent(pstAnggota.getString(FLD_ADDR_PERMANENT));
            hashtableAnggota.put(fieldNames[FLD_ADDR_PERMANENT], (anggota.getAddressPermanent() != null ? anggota.getAddressPermanent() : "-"));
            
            anggota.setIdCard(pstAnggota.getString(FLD_ID_CARD));
            hashtableAnggota.put(fieldNames[FLD_ID_CARD], (anggota.getIdCard() != null ? anggota.getIdCard() : "-"));
            
            anggota.setExpiredDateKtp(pstAnggota.getDate(FLD_EXPIRED_DATE_KTP));
            hashtableAnggota.put(fieldNames[FLD_EXPIRED_DATE_KTP], (anggota.getExpiredDateKtp() != null ? anggota.getExpiredDateKtp() : "-"));
            
            anggota.setTelepon(pstAnggota.getString(FLD_TLP));
            hashtableAnggota.put(fieldNames[FLD_TLP], (anggota.getTelepon() != null ? anggota.getTelepon() : "-"));
            
            anggota.setHandPhone(pstAnggota.getString(FLD_HANDPHONE));
            hashtableAnggota.put(fieldNames[FLD_HANDPHONE], (anggota.getHandPhone() != null ? anggota.getHandPhone() : "-"));
            
            anggota.setEmail(pstAnggota.getString(FLD_EMAIL));
            hashtableAnggota.put(fieldNames[FLD_EMAIL], (anggota.getEmail() != null ? anggota.getEmail() : "-"));
            
            anggota.setNoNpwp(pstAnggota.getString(FLD_NO_NPWP));
            hashtableAnggota.put(fieldNames[FLD_NO_NPWP], (anggota.getNoNpwp() != null ? anggota.getNoNpwp() : "-"));
            
            anggota.setNoRekening(pstAnggota.getString(FLD_NO_REKENING));
            hashtableAnggota.put(fieldNames[FLD_NO_REKENING], (anggota.getNoRekening() != null ? anggota.getNoRekening() : "-"));
            
            anggota.setNoKartuKeluarga(pstAnggota.getString(FLD_NO_KARTU_KELUARGA));
            hashtableAnggota.put(fieldNames[FLD_NO_KARTU_KELUARGA], (anggota.getNoKartuKeluarga() != null ? anggota.getNoKartuKeluarga() : "-"));
            
            int age=getAge(anggota.getBirthDate());
            hashtableAnggota.put("MEMBER_AGE", age);
            
            return hashtableAnggota;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggota(0), DBException.UNKNOWN);
        }
    }
    
    public static int getAge(Date birthDate){
        int years = 0;
        int months = 0;
        int days = 0;
        
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());
        
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(new Date().getTime());

        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        
        months = currMonth - birthMonth;
        
        if (months < 0)
        {
           years--;
           months = 12 - birthMonth + currMonth;
           if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
              months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
           years--;
           months = 11;
        }
        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
           days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
           int today = now.get(Calendar.DAY_OF_MONTH);
           now.add(Calendar.MONTH, -1);
           days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else
        {
           days = 0;
           if (months == 12)
           {
              years++;
              months = 0;
           }
        }
        return years;
    }
    
}
