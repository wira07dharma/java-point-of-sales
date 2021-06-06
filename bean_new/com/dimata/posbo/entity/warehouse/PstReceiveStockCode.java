/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:29:15 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstReceiveStockCode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_RECEIVE_MATERIAL_CODE = "pos_receive_material_item_code";

    public static final int FLD_RECEIVE_MATERIAL_CODE_ID = 0;
    public static final int FLD_RECEIVE_MATERIAL_ITEM_ID = 1;
    public static final int FLD_RECEIVE_MATERIAL_ID = 2;
    public static final int FLD_STOCK_CODE = 3;
    public static final int FLD_STOCK_VALUE = 4;

    public static final String[] fieldNames = {
        "RECEIVE_MATERIAL_CODE_ID",
        "RECEIVE_MATERIAL_ITEM_ID",
        "RECEIVE_MATERIAL_ID",
        "STOCK_CODE",
        "VALUE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT
    };

    public PstReceiveStockCode() {
    }

    public PstReceiveStockCode(int i) throws DBException {
        super(new PstReceiveStockCode());
    }

    public PstReceiveStockCode(String sOid) throws DBException {
        super(new PstReceiveStockCode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstReceiveStockCode(long lOid) throws DBException {
        super(new PstReceiveStockCode(0));
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
        return TBL_POS_RECEIVE_MATERIAL_CODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstReceiveStockCode().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        ReceiveStockCode stockCode = fetchExc(ent.getOID());
        ent = (Entity) stockCode;
        return stockCode.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((ReceiveStockCode) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((ReceiveStockCode) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ReceiveStockCode fetchExc(long oid) throws DBException {
        try {
            ReceiveStockCode receiveStockCode = new ReceiveStockCode();
            PstReceiveStockCode pstReceiveStockCode = new PstReceiveStockCode(oid);
            receiveStockCode.setOID(oid);

            receiveStockCode.setReceiveMaterialItemId(pstReceiveStockCode.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));

            //dyas 20131130
            receiveStockCode.setReceiveMaterialId(pstReceiveStockCode.getlong(FLD_RECEIVE_MATERIAL_ID));
            receiveStockCode.setStockCode(pstReceiveStockCode.getString(FLD_STOCK_CODE));
            receiveStockCode.setStockValue(pstReceiveStockCode.getdouble(FLD_STOCK_VALUE));
             
            return receiveStockCode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReceiveStockCode(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(ReceiveStockCode materialStockCode) throws DBException {
        try {
            PstReceiveStockCode pstReceiveStockCode = new PstReceiveStockCode(0);

            pstReceiveStockCode.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, materialStockCode.getReceiveMaterialItemId());

            //dyas 20131130
            pstReceiveStockCode.setLong(FLD_RECEIVE_MATERIAL_ID, materialStockCode.getReceiveMaterialId());
            pstReceiveStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
            pstReceiveStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
            pstReceiveStockCode.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstReceiveStockCode.getInsertSQL());
            materialStockCode.setOID(pstReceiveStockCode.getlong(FLD_RECEIVE_MATERIAL_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReceiveStockCode(0), DBException.UNKNOWN);
        }
        return materialStockCode.getOID();
    }

    public static long updateExc(ReceiveStockCode materialStockCode) throws DBException {
        try {
            if (materialStockCode.getOID() != 0) {
                PstReceiveStockCode pstReceiveStockCode = new PstReceiveStockCode(materialStockCode.getOID());

                pstReceiveStockCode.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, materialStockCode.getReceiveMaterialItemId());

                //dyas 20131130
                pstReceiveStockCode.setLong(FLD_RECEIVE_MATERIAL_ID, materialStockCode.getReceiveMaterialId());
                pstReceiveStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
                 pstReceiveStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
                pstReceiveStockCode.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstReceiveStockCode.getUpdateSQL());
                return materialStockCode.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReceiveStockCode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstReceiveStockCode pstReceiveStockCode = new PstReceiveStockCode(oid);
            pstReceiveStockCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstReceiveStockCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReceiveStockCode(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RECEIVE_MATERIAL_CODE;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ReceiveStockCode materialStockCode = new ReceiveStockCode();
                resultToObject(rs, materialStockCode);
                lists.add(materialStockCode);
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

    private static void resultToObject(ResultSet rs, ReceiveStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID]));
            materialStockCode.setReceiveMaterialItemId(rs.getLong(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]));

            //dyas 20131130
            materialStockCode.setReceiveMaterialId(rs.getLong(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ID]));
            materialStockCode.setStockCode(rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockValue(rs.getDouble(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_VALUE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RECEIVE_MATERIAL_CODE + " WHERE " +
                    PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID] + " = " + oid;

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
            String sql = "SELECT COUNT(" + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID] + ") FROM " + TBL_POS_RECEIVE_MATERIAL_CODE;
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
                    ReceiveStockCode materialStockCode = (ReceiveStockCode) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    /** gadnyana
     * get serial receive
     * @param oidMat
     * @param oidDf
     * @return
     */
    public static Vector getSerialCodeReceiveBy(long oidMat, long oidRec) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DCODE .* FROM " + TBL_POS_RECEIVE_MATERIAL_CODE + " AS DCODE " +
                    " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS IT " +
                    " ON IT.RECEIVE_MATERIAL_ITEM_ID = DCODE.RECEIVE_MATERIAL_ITEM_ID " +
                    " WHERE IT.RECEIVE_MATERIAL_ID = " + oidRec +
                    " AND IT.MATERIAL_ID = " + oidMat;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ReceiveStockCode materialStockCode = new ReceiveStockCode();
                resultToObject(rs, materialStockCode);
                list.add(materialStockCode);
            }
        } catch (Exception e) {
            System.out.println("");
        }
        return list;
    }

    // ini untuk men insert data jika berisi serial code (untuk sementara)
    public static void getInsertSerialFromReturn(long oidItem, long oidRet, long oidMat) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String where = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidRet + " AND " +
                    PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + oidMat;
            Vector vet = PstMatReturnItem.list(0, 0, where, "");
            if (vet != null && vet.size() > 0) {
                MatReturnItem matReturnItem = (MatReturnItem) vet.get(0);
                where = PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID] + "=" + matReturnItem.getOID();
                Vector vCode = PstReturnStockCode.list(0, 0, where, "");
                if (vCode != null && vCode.size() > 0) {
                    for(int k=0;k<vCode.size();k++){
                        ReturnStockCode returnStockCode = (ReturnStockCode) vCode.get(k);
                        ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                        receiveStockCode.setReceiveMaterialItemId(oidItem);
                        receiveStockCode.setStockCode(returnStockCode.getStockCode());
                        try{
                            PstReceiveStockCode.insertExc(receiveStockCode);
                        }catch(Exception e){}
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("");
        }
    }


    public static Vector listReceieStockCodePosting(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {

            /**
             * SELECT PRMIC.* FROM pos_receive_material_item_code  AS PRMIC
                INNER JOIN pos_receive_material_item AS PRMI ON PRMI.RECEIVE_MATERIAL_ITEM_ID=PRMIC.RECEIVE_MATERIAL_ITEM_ID
                INNER JOIN pos_receive_material AS PRM ON PRM.RECEIVE_MATERIAL_ID=PRMI.RECEIVE_MATERIAL_ID
                WHERE PRM.RECEIVE_MATERIAL_ID=504404544231312161;
             */


            String sql = " SELECT PRMIC.*,PRMI.MATERIAL_ID FROM " + TBL_POS_RECEIVE_MATERIAL_CODE+" AS PRMIC "+
                         " INNER JOIN pos_receive_material_item AS PRMI ON PRMI.RECEIVE_MATERIAL_ITEM_ID=PRMIC.RECEIVE_MATERIAL_ITEM_ID"+
                         " INNER JOIN pos_receive_material AS PRM ON PRM.RECEIVE_MATERIAL_ID=PRMI.RECEIVE_MATERIAL_ID" ;
            
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //ReceiveStockCode materialStockCode = new ReceiveStockCode();
                SourceStockCode sourceStockCode = new SourceStockCode();//(SourceStockCode)
                resultToObjectSerialCode(rs, sourceStockCode);
                lists.add(sourceStockCode);
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

    private static void resultToObjectSerialCode(ResultSet rs, SourceStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID]));
            materialStockCode.setSourceId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]));
            materialStockCode.setStockCode(rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockValue(rs.getDouble(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_VALUE]));
        } catch (Exception e) {
        }
    }
    
     /**
     * jika stok FIFO maka semua item mempunyai serial number
     * @param cnt ; jumlah qty
     * @param oid : oid receive material id
     * @param oidReceive : oid receive
     * @param strVal : harga beli
     * @return 
     */
      synchronized public static boolean automaticInsertSerialNumber(double cnt, long oid, long oidReceive, double strVal){
          boolean insert = true;
          try{
              //int counter = Integer.parseInt(cnt);
              for(int k=0;k<cnt;k++){
                            ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                            long automaticSerial = OIDFactory.generateOID();
                            receiveStockCode.setReceiveMaterialItemId(oid);
                            receiveStockCode.setStockCode(""+automaticSerial);
                            receiveStockCode.setStockValue(strVal);
                            //dyas 20131130
                            //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                            receiveStockCode.setReceiveMaterialId(oidReceive);
                            try{
                                PstReceiveStockCode.insertExc(receiveStockCode);
                            }catch(Exception e){}
              }
          }catch(Exception e){
              System.out.print("upss"+e);
          }
          return insert;
     }
     
     
      
       public static int deleteReceiveMaterialStockWithReceiveMaterialItemId(String whereClause) throws DBException {
        int hasil = 0;
        try {
            String sql = "DELETE FROM " + TBL_POS_RECEIVE_MATERIAL_CODE ;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            int result = execUpdate(sql);
            hasil = 1;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
        return hasil;
    }

	public static String getLastSN(int year){
            String buffer="";
            boolean result = false;
            DBResultSet dbrs = null;
            try {
                String sql = " SELECT MAX("+PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]+")+1"+
							 " FROM " + PstReceiveStockCode.TBL_POS_RECEIVE_MATERIAL_CODE+ " WHERE " +
                               PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE] + " LIKE '" + year+"%'";

                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();

                while (rs.next()) {
                   buffer=rs.getString(1);
                }
                rs.close();
				
				if (buffer == null){
					buffer = year+"000000001";
				}

            } catch (Exception e) {
                System.out.println("err : " + e.toString());
            } finally {
                DBResultSet.close(dbrs);
            }
        return buffer;
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         ReceiveStockCode receiveStockCode = PstReceiveStockCode.fetchExc(oid);
         object.put(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID], receiveStockCode.getReceiveMaterialCodeId());
         object.put(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID], receiveStockCode.getReceiveMaterialItemId());
         object.put(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ID], receiveStockCode.getReceiveMaterialId());
         object.put(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE], receiveStockCode.getStockCode());
         object.put(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_VALUE], receiveStockCode.getStockValue());
      }catch(Exception exc){}
      return object;
   }
}
