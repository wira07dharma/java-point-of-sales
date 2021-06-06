/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.posbo.entity.masterdata;

/* package java */

import java.sql.ResultSet;
import java.util.Vector;

/*
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
 */
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.pos.entity.billing.*;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.I_PersistentExcSynch;
import com.dimata.util.lang.I_Language;
import org.json.JSONObject;

public class PstMemberPoin extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_PersistentExcSynch {

    //public static final String TBL_POS_MEMBER_POIN = "MEMBER_POINT_STOCK";
    public static final String TBL_POS_MEMBER_POIN = "member_point_stock";

    public static final int FLD_MEMBER_POINT_ID = 0;
    public static final int FLD_CASH_BILL_MAIN_ID = 1;
    public static final int FLD_MEMBER_ID = 2;
    public static final int FLD_TRANSACTION_DATE = 3;
    public static final int FLD_DEBET = 4;
    public static final int FLD_CREDIT = 5;
	public static final int FLD_CURRENT_POINT = 6;
    /*public static final  int FLD_CONTACT_ID = 5;
    public static final  int FLD_MEMBER_RELIGION = 6;
    public static final  int FLD_MEMBER_BIRTH_DATE = 7;
    */
    public static final String[] fieldNames = {
        "MEMBER_POINT_ID",
        "CASH_BILL_MAIN_ID",
        "MEMBER_ID",
        "TRANSACTION_DATE",
        "DEBET",
        "CREDIT",
		"CURRENT_POINT"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG ,
        TYPE_LONG + TYPE_FK,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };


    /*public static final int TYPE_MEMBER_AGEN = 0;
    public static final int TYPE_MEMBER_UMUM 	= 1;

    public static final int STATUS_MEMBER_NORMAL = 0;
    public static final int STATUS_MEMBER_BLACK_LISTED    = 1;
    public static final int TYPE_LOCATION_CARGO 	= 2;
    public static final int TYPE_LOCATION_VENDOR 	= 3;
    public static final int TYPE_LOCATION_TRANSFER 	= 4;
    public static final int TYPE_GALLERY_CUSTOMER 	= 5;
    public static final int TYPE_GALLERY_CONSIGNOR 	= 6;
    public static final int TYPE_LOCATION_DEPARTMENT 	= 7;
    public static final int TYPE_LOCATION_PROJECT 	= 8;
    */
    public static final String[] fieldMemberType = {
        "Agen",
        "Umum"
    };

    public static final String[] fieldMemberStatus = {
        "Normal",
        "Black Listed"
    };

    public static final int RELIGION_HINDU = 0;
    public static final int RELIGION_BUDHA = 1;
    public static final int RELIGION_KATOLIK = 2;
    public static final int RELIGION_PROTESTAN = 3;
    public static final int RELIGION_KONG_HU_CU = 4;
    public static final int RELIGION_ISLAM = 5;

    public static final String[] fieldReligionType = {
        "Hindu",
        "Budha",
        "Katolik",
        "Protestan",
        "Kong Hu Cu",
        "Islam",
    };


    public PstMemberPoin() {
    }

    public PstMemberPoin(int i) throws DBException {
        super(new PstMemberPoin());
    }

    public PstMemberPoin(String sOid) throws DBException {
        super(new PstMemberPoin(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMemberPoin(long lOid) throws DBException {
        super(new PstMemberPoin(0));
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
        return TBL_POS_MEMBER_POIN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMemberPoin().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            MemberPoin memberPoin = (MemberPoin) ent;
            long oid = ent.getOID();
            PstMemberPoin pstMemberPoin = new PstMemberPoin(oid);
            memberPoin.setOID(oid);

            memberPoin.setMemberId(pstMemberPoin.getlong(FLD_MEMBER_ID));
            memberPoin.setCashBillMainId(pstMemberPoin.getlong(FLD_CASH_BILL_MAIN_ID));
            memberPoin.setTransactionDate(pstMemberPoin.getDate(FLD_TRANSACTION_DATE));
            memberPoin.setCredit(pstMemberPoin.getInt(FLD_CREDIT));
            memberPoin.setMemberId(pstMemberPoin.getInt(FLD_DEBET));
			memberPoin.setCurrentPoint(pstMemberPoin.getInt(FLD_CURRENT_POINT));
            return memberPoin.getOID();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberPoin(0), DBException.UNKNOWN);
        }
    }


    public long insertExc(Entity ent) throws Exception {
        return insertExc((MemberPoin) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MemberPoin) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        MemberPoin memberPoin = (MemberPoin) ent;
        deleteExc(ent.getOID());

        return ent.getOID();
    }


    public static MemberPoin fetchExc(long oid) throws DBException {
        try {
            MemberPoin memberPoin = new MemberPoin();
            PstMemberPoin pstMemberPoin = new PstMemberPoin(oid);
            memberPoin.setOID(oid);

            memberPoin.setMemberId(pstMemberPoin.getlong(FLD_MEMBER_ID));
            memberPoin.setCashBillMainId(pstMemberPoin.getlong(FLD_CASH_BILL_MAIN_ID));
            memberPoin.setTransactionDate(pstMemberPoin.getDate(FLD_TRANSACTION_DATE));
            memberPoin.setCredit(pstMemberPoin.getInt(FLD_CREDIT));
            memberPoin.setMemberId(pstMemberPoin.getInt(FLD_DEBET));
			memberPoin.setCurrentPoint(pstMemberPoin.getInt(FLD_CURRENT_POINT));

            return memberPoin;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberPoin(0), DBException.UNKNOWN);
        }
    }


    public static long insertExc(MemberPoin memberPoin) throws DBException {
        try {
            PstMemberPoin pstMemberPoin = new PstMemberPoin(0);
            //System.out.println("memberPoin.getCashBillMainId() : "+memberPoin.getCashBillMainId());
            pstMemberPoin.setLong(FLD_CASH_BILL_MAIN_ID, memberPoin.getCashBillMainId());
            pstMemberPoin.setInt(FLD_CREDIT, memberPoin.getCredit());
            pstMemberPoin.setInt(FLD_DEBET, memberPoin.getDebet());
            pstMemberPoin.setLong(FLD_MEMBER_ID, memberPoin.getMemberId());
            pstMemberPoin.setDate(FLD_TRANSACTION_DATE, memberPoin.getTransactionDate());
			pstMemberPoin.setInt(FLD_CURRENT_POINT, memberPoin.getCurrentPoint());
            pstMemberPoin.insert();
            memberPoin.setOID(pstMemberPoin.getlong(FLD_MEMBER_POINT_ID));


        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberPoin(0), DBException.UNKNOWN);
        }
        return memberPoin.getOID();
    }


    public static long updateExc(MemberPoin memberPoin) throws DBException {
        try {
            if (memberPoin.getOID() != 0) {
                PstMemberPoin pstMemberPoin = new PstMemberPoin(memberPoin.getOID());


                pstMemberPoin.setLong(FLD_CASH_BILL_MAIN_ID, memberPoin.getCashBillMainId());
                pstMemberPoin.setLong(FLD_CREDIT, memberPoin.getCredit());
                pstMemberPoin.setLong(FLD_DEBET, memberPoin.getDebet());
                pstMemberPoin.setLong(FLD_MEMBER_ID, memberPoin.getMemberId());
                pstMemberPoin.setDate(FLD_TRANSACTION_DATE, memberPoin.getTransactionDate());
				pstMemberPoin.setInt(FLD_CURRENT_POINT, memberPoin.getCurrentPoint());
                pstMemberPoin.update();

                /*ContactList contactList = member.getContactList();
                PstContactList.updateExc(contactList);
                */

                /*Vector vctMemberRegistHist = member.getVctMemberRegistHist();
                MemberRegistHist memberRegistHist = new MemberRegistHist();
                PstMemberRegistHist.updateExc(memberRegistHist);
                */
                return memberPoin.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberPoin(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMemberPoin pstMember = new PstMemberPoin(oid);

            pstMember.delete();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberPoin(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MEMBER_POIN;
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
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MemberPoin memberPoin = new MemberPoin();
                resultToObject(rs, memberPoin);
                lists.add(memberPoin);
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


    private static void resultToObject(ResultSet rs, MemberPoin memberPoin) {
        try {

            memberPoin.setOID(rs.getLong(PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_POINT_ID]));
            memberPoin.setMemberId(rs.getLong(PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_ID]));
            memberPoin.setCashBillMainId(rs.getLong(PstMemberPoin.fieldNames[PstMemberPoin.FLD_CASH_BILL_MAIN_ID]));
            memberPoin.setTransactionDate(rs.getDate(PstMemberPoin.fieldNames[PstMemberPoin.FLD_TRANSACTION_DATE]));
            memberPoin.setCredit(rs.getInt(PstMemberPoin.fieldNames[PstMemberPoin.FLD_CREDIT]));
            memberPoin.setDebet(rs.getInt(PstMemberPoin.fieldNames[PstMemberPoin.FLD_DEBET]));
			memberPoin.setCurrentPoint(rs.getInt(PstMemberPoin.fieldNames[PstMemberPoin.FLD_CURRENT_POINT]));
        } catch (Exception e) {
        }
    }


    public static boolean checkOID(long memberPoinId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MEMBER_POIN + " WHERE " +
                    PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_POINT_ID] + " = " + memberPoinId;

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

        }
        return result;
    }


    /**
     * untuk mencari total point member
     * @param fld
     * @return
     */
    public static MemberPoin getTotalPoint(long oidMember) {
        DBResultSet dbrs = null;
        MemberPoin memberPoin = new MemberPoin();
        try {
            String sql = "SELECT SUM(" + PstMemberPoin.fieldNames[PstMemberPoin.FLD_CREDIT] + "),SUM(" + PstMemberPoin.fieldNames[PstMemberPoin.FLD_DEBET] + ") FROM " + TBL_POS_MEMBER_POIN + " WHERE " +
                            PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_ID] + " = " + oidMember;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                memberPoin.setCredit(rs.getInt(1));
                memberPoin.setDebet(rs.getInt(2));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return memberPoin;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_POINT_ID] + ") FROM " + TBL_POS_MEMBER_POIN;
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
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    MemberPoin memberPoin = (MemberPoin) list.get(ls);
                    if (oid == memberPoin.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    public long insertExcSynch(Entity ent) throws Exception {
        throw new UnsupportedOperationException();
    }

    public static long acquirePoinFrom(long billMainId, long newOwnerId) {
        long poinTransferedId = 0;
        MemberPoin poinTransfered = new MemberPoin();

        try {
            String whereClause = PstMemberPoin.fieldNames[PstMemberPoin.FLD_CASH_BILL_MAIN_ID] + "=" + billMainId + "";
            Vector list = PstMemberPoin.list(0, 0, whereClause, "");
            if (list.size() > 0) {
                poinTransfered = (MemberPoin) list.get(0);
                poinTransfered.setMemberId(newOwnerId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (poinTransfered.getOID() > 0) {
            try {
                PstMemberPoin.updateExc(poinTransfered);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return poinTransferedId;
    }

    public static long releasePoin(long poinTransferedId) {

        //poinTransfered.setMemberId (newOwner.getOID ());
        //String whereClause = "";
        //whereClause = " "+PstBillMain.FLD_BILL_MAIN_ID;
        MemberPoin poinTransfered = new MemberPoin();
        //Mem
        try {
            poinTransfered = PstMemberPoin.fetchExc(poinTransferedId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        long billMainId = poinTransfered.getCashBillMainId();
        BillMain billMain = null;
        try {
            billMain = PstBillMain.fetchExc(billMainId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (billMain != null && billMain.getOID() > 0) {
            poinTransfered.setMemberId(billMain.getCustomerId());
            try {
                PstMemberPoin.updateExc(poinTransfered);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return poinTransferedId;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MemberPoin memberPoin = PstMemberPoin.fetchExc(oid);
                object.put(PstMemberPoin.fieldMemberStatus[PstMemberPoin.FLD_MEMBER_POINT_ID], memberPoin.getOID());
                object.put(PstMemberPoin.fieldMemberStatus[PstMemberPoin.FLD_CASH_BILL_MAIN_ID], memberPoin.getCashBillMainId());
                object.put(PstMemberPoin.fieldMemberStatus[PstMemberPoin.FLD_CREDIT], memberPoin.getCredit());
                object.put(PstMemberPoin.fieldMemberStatus[PstMemberPoin.FLD_CURRENT_POINT], memberPoin.getCurrentPoint());
                object.put(PstMemberPoin.fieldMemberStatus[PstMemberPoin.FLD_DEBET], memberPoin.getDebet());
                object.put(PstMemberPoin.fieldMemberStatus[PstMemberPoin.FLD_MEMBER_ID], memberPoin.getMemberId());
                object.put(PstMemberPoin.fieldMemberStatus[PstMemberPoin.FLD_TRANSACTION_DATE], memberPoin.getTransactionDate());
            }catch(Exception exc){}

            return object;
        }
}
