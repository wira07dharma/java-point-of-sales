/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Feb 28, 2006
 * Time: 11:30:51 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.warehouse;

import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.entity.search.SrcMinimumStock;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.common.entity.location.Location;

import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.Hashtable;

public class SessMinimumStock {

    public static String SESSION_MINIMUM_NAME = "SESSION_MINIMUM_NAME";

    private static Hashtable materialrequestsearch = null;
    private static Hashtable materialComponentrequestsearch = null;

    // count minimun stock
    public static int getCountReportMinimumStock(SrcMinimumStock srcMinimumStock) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sqlUnion = "";
            String sql = "SELECT COUNT(" + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + ") AS CNT "
                    + " FROM (";
            for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                Location location = (Location) srcMinimumStock.getvLocation().get(k);
                sqlUnion = sqlUnion + "SELECT periode_id, material_unit_id "
                        + " FROM pos_material_stock where qty <= qty_min "
                        + " and periode_id = '" + srcMinimumStock.getPeriodId() + "'"
                        + " and location_id = '" + location.getOID() + "'";
                if (k < (srcMinimumStock.getvLocation().size() - 1)) {
                    sqlUnion = sqlUnion + " union ";
                }
            }
            sql = sql + sqlUnion + " ) as tbl "
                    + " inner join pos_material as m on tbl.material_unit_id = m.material_id where m.material_type=0"
                    + " group by periode_id ";

            //update opie-eyek 2014-14-02 agar yang dimunculkan cuma type barang saja
            //sql = sql + " where m.material_type=0 ";
            // System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt("CNT");
            }
        } catch (Exception e) {
            System.out.println("err minimum stock : " + e.toString());
        }
        return count;
    }

    // minimun stock
    public static Vector getReportMinimumStock(SrcMinimumStock srcMinimumStock, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sqlUnion = "";
            String sql = "SELECT m." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                    + ",m." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                    + ",m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + ",m." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + "," + createFieldSum(srcMinimumStock.getvLocation()) + " FROM (";

            for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                Location location = (Location) srcMinimumStock.getvLocation().get(k);
                sqlUnion = sqlUnion + "SELECT ms.location_id, material_unit_id, "
                        + createField(srcMinimumStock.getvLocation(), location.getOID(), k)
                        + " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS ms";

                String where = "";
                boolean viewedData = false;
                if ((srcMinimumStock.getCategoryId() != 0) && (srcMinimumStock.getOidMerk() != 0)) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";
                    where = " pm.category_id=" + srcMinimumStock.getCategoryId()
                            + " and pm.merk_id=" + srcMinimumStock.getOidMerk();

                    viewedData = true;

                } else if (srcMinimumStock.getCategoryId() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";
                    where = " pm.category_id=" + srcMinimumStock.getCategoryId();
                    viewedData = true;
                } else if (srcMinimumStock.getOidMerk() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";

                    where = " pm.merk_id=" + srcMinimumStock.getOidMerk();
                    viewedData = true;
                }

                if (viewedData) {
                    if (where.length() > 0) {
                        where = where + " and pm.process_status !=" + PstMaterial.DELETE;
                    } else {
                        where = " pm.process_status !=" + PstMaterial.DELETE;
                    }
                }

                if (srcMinimumStock.getOidSupplier() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_vendor_price as pvp on "
                            + " ms.material_unit_id = pvp.material_id ";

                    if (where.length() > 0) {

                        where = where + " and pvp.vendor_id=" + srcMinimumStock.getOidSupplier();

                    } else {
                        where = " pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
                    }
                }

                //if(srcMinimumStock.getLocationId()!=0){
                if (where.length() > 0) {

                    where = where + " and ms.location_id=" + location.getOID();
                } else {
                    where = " ms.location_id=" + location.getOID();
                }
                //}

                if (srcMinimumStock.getPeriodId() != 0) {
                    if (where.length() > 0) {
                        where = where + " and ms.periode_id = " + srcMinimumStock.getPeriodId();
                    } else {
                        where = " ms.periode_id = " + srcMinimumStock.getPeriodId();
                    }
                }

                if (srcMinimumStock.getListOidMaterial().size() > 0) {
                    String wherematerial = "";
                    for (int j = 0; j < srcMinimumStock.getListOidMaterial().size(); j++) {
                        long oidMaterial = Long.parseLong((String) srcMinimumStock.getListOidMaterial().get(j));
                        if (wherematerial.length() == 0) {
                            wherematerial = " ms.material_unit_id=" + oidMaterial;
                        } else {
                            wherematerial = wherematerial + " or " + " ms.material_unit_id=" + oidMaterial;
                        }
                    }
                    wherematerial = "(" + wherematerial + ")";
                    if (where.length() > 0) {
                        where = where + " and " + wherematerial;
                    } else {
                        where = wherematerial;
                    }
                }

                if (where.length() > 0) {
                    sqlUnion = sqlUnion + " where " + where;
                }

                if (k < (srcMinimumStock.getvLocation().size() - 1)) {
                    sqlUnion = sqlUnion + " union ";
                }
            }

            sql = sql + sqlUnion + " ) as tbl ";
            sql = sql + " right join pos_material as m on tbl.material_unit_id = m.material_id ";

            if (srcMinimumStock.getOidSupplier() != 0) {
                sql = sql + " inner join pos_vendor_price as pvp on "
                        + " m.material_id = pvp.material_id ";
            }

            //update opie-eyek 2014-14-02 agar yang dimunculkan cuma type barang saja
            sql = sql + " where m.material_type=0 ";

            if (srcMinimumStock.getCategoryId() != 0) {
                sql = sql + " and m.category_id=" + srcMinimumStock.getCategoryId();
            }

            if (srcMinimumStock.getListOidMaterial().size() > 0) {
                String wherematerial = "";
                for (int k = 0; k < srcMinimumStock.getListOidMaterial().size(); k++) {
                    long oidMaterial = Long.parseLong((String) srcMinimumStock.getListOidMaterial().get(k));
                    if (wherematerial.length() == 0) {
                        wherematerial = "m.material_id=" + oidMaterial;
                    } else {
                        wherematerial = wherematerial + " or " + "m.material_id=" + oidMaterial;
                    }
                }

                wherematerial = "(" + wherematerial + ")";
                if (srcMinimumStock.getCategoryId() != 0) {
                    sql = sql + " and " + wherematerial;
                } else if (wherematerial.length() == 0) {
                    sql = sql + " where " + wherematerial;
                } else {
                    sql = sql + " and " + wherematerial;
                }
            }

            if (srcMinimumStock.getOidMerk() != 0) {
                if (srcMinimumStock.getCategoryId() != 0) {
                    sql = sql + " and m.merk_id=" + srcMinimumStock.getOidMerk();
                } else if (srcMinimumStock.getListOidMaterial().size() > 0) {
                    sql = sql + " and m.merk_id=" + srcMinimumStock.getOidMerk();
                } else {
                    sql = sql + " where m.merk_id=" + srcMinimumStock.getOidMerk();
                }
            }

            if (srcMinimumStock.getListOidMaterial().size() > 0
                    || srcMinimumStock.getOidMerk() != 0 || srcMinimumStock.getCategoryId() != 0) {
                if (srcMinimumStock.getOidSupplier() != 0) {
                    sql = sql + " and pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
                }
            } else if (srcMinimumStock.getOidSupplier() != 0) {
                sql = sql + " and pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
            }

            sql = sql + " group by m.sku";

            if (recordToGet != 0) {
                sql = sql + " limit " + start + "," + recordToGet;
            }

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vitem = new Vector();
                Vector vMin = new Vector();
                Vector vStock = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                vitem.add(material);

                // ini untuk object minimum stock 
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vMin.add(String.valueOf(rs.getDouble(getFieldSum(1, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vMin);

                // ini untuk object stock on hand
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vStock.add(String.valueOf(rs.getDouble(getFieldSum(0, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vStock);

                list.add(vitem);
            }

        } catch (Exception e) {
            System.out.println("err minimum stock : " + e.toString());
        }
        return list;
    }

    // untuk menggabungkan field dari minimal stock
    private static String createField(Vector vectLoc, long oidLocation, int idx) {
        String str = "";
        if (vectLoc != null && vectLoc.size() > 0) {
            for (int k = 0; k < vectLoc.size(); k++) {
                Location loc = (Location) vectLoc.get(k);
                if (idx == 0) {
                    if (loc.getOID() == oidLocation) {
                        if (str.length() == 0) {
                            str = "qty as q" + k + ",qty_min as qm" + k;
                        } else {
                            str = str + ",qty as q" + k + ",qty_min as qm" + k;
                        }
                    } else if (str.length() == 0) {
                        str = "0 as q" + k + ",0 as qm" + k;
                    } else {
                        str = str + ",0 as q" + k + ",0 as qm" + k;
                    }
                } else if (loc.getOID() == oidLocation) {
                    if (str.length() == 0) {
                        str = "qty as q" + k + ",qty_min as qm" + k;
                    } else {
                        str = str + ",qty as q" + k + ",qty_min as qm" + k;
                    }
                } else if (str.length() == 0) {
                    str = "0 as q" + k + ",0 as qm" + k;
                } else {
                    str = str + ",0 as q" + k + ",0 as qm" + k;
                }
            }
        }
        return str;
    }

    // untuk menggabungkan field dari minimal stock
    private static String createFieldSum(Vector vectLoc) {
        String str = "";
        if (vectLoc != null && vectLoc.size() > 0) {
            for (int k = 0; k < vectLoc.size(); k++) {
                Location loc = (Location) vectLoc.get(k);
                if (str.length() == 0) {
                    str = "sum(q" + k + "),sum(qm" + k + ")";
                } else {
                    str = str + ",sum(q" + k + "),sum(qm" + k + ")";
                }
            }
        }
        return str;
    }

    // untuk menggabungkan field dari minimal stock
    private static String getFieldSum(int type, int idx, Vector vectLoc) {
        String str = "";
        if (vectLoc != null && vectLoc.size() > 0) {
            for (int k = 0; k < vectLoc.size(); k++) {
                // Location loc = (Location) vectLoc.get(k);
                if (idx == k) {
                    if (type == 0) {
                        str = "sum(q" + k + ")";
                    } else {
                        str = "sum(qm" + k + ")";
                    }
                    break;
                }
            }
        }
        return str;
    }

    //
    private Vector getQtyMinimumStock(long oidPeriod, long oidLocation) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT * FROM POS_MATERIAL_STOCK WHERE PERIODE_ID = 504404252370511713 AND LOCATION_ID = 504404271285127208";

        } catch (Exception e) {
            System.out.println("err minimum stock : " + e.toString());
        }
        return new Vector();
    }

    public static Vector getMaterialListMinimumStock(SrcMinimumStock srcMinimumStock, int start, int recordToGet) {
        Material objMaterial = new Material();

        return getMaterialListMinimumStock(srcMinimumStock, start, recordToGet, objMaterial);
    }

    /**
     * add opie-eyek 20140303
     *
     * @param srcMinimumStock
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector getMaterialListMinimumStock(SrcMinimumStock srcMinimumStock, int start, int recordToGet, Material objMaterial) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sqlUnion = "";
            String sql = "SELECT m." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
                    + ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE]
                    + ", UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + ", " + createFieldSum(srcMinimumStock.getvLocation()) + " FROM (";

            for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                Location location = (Location) srcMinimumStock.getvLocation().get(k);
                sqlUnion = sqlUnion + "SELECT ms.location_id, material_unit_id, "
                        + createField(srcMinimumStock.getvLocation(), location.getOID(), k)
                        + " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS ms";

                String where = "";
                boolean viewedData = false;
                if ((srcMinimumStock.getCategoryId() != 0) && (srcMinimumStock.getOidMerk() != 0)) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";
                    where = " pm.category_id=" + srcMinimumStock.getCategoryId()
                            + " and pm.merk_id=" + srcMinimumStock.getOidMerk();

                    viewedData = true;

                } else if (srcMinimumStock.getCategoryId() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";
                    where = " pm.category_id=" + srcMinimumStock.getCategoryId();
                    viewedData = true;
                } else if (srcMinimumStock.getOidMerk() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";

                    where = " pm.merk_id=" + srcMinimumStock.getOidMerk();
                    viewedData = true;
                }

                if (viewedData) {
                    if (where.length() > 0) {
                        where = where + " and pm.process_status !=" + PstMaterial.DELETE;
                    } else {
                        where = " pm.process_status !=" + PstMaterial.DELETE;
                    }
                }

                if (srcMinimumStock.getOidSupplier() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_vendor_price as pvp on "
                            + " ms.material_unit_id = pvp.material_id ";

                    if (where.length() > 0) {

                        where = where + " and pvp.vendor_id=" + srcMinimumStock.getOidSupplier();

                    } else {
                        where = " pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
                    }
                }

                //if(srcMinimumStock.getLocationId()!=0){
                if (where.length() > 0) {

                    where = where + " and ms.location_id=" + location.getOID();
                } else {
                    where = " ms.location_id=" + location.getOID();
                }
                //}

                if (srcMinimumStock.getPeriodId() != 0) {
                    if (where.length() > 0) {
                        where = where + " and ms.periode_id = " + srcMinimumStock.getPeriodId();
                    } else {
                        where = " ms.periode_id = " + srcMinimumStock.getPeriodId();
                    }
                }

                if (srcMinimumStock.getListOidMaterial().size() > 0) {
                    String wherematerial = "";
                    for (int j = 0; j < srcMinimumStock.getListOidMaterial().size(); j++) {
                        long oidMaterial = Long.parseLong((String) srcMinimumStock.getListOidMaterial().get(j));
                        if (wherematerial.length() == 0) {
                            wherematerial = " ms.material_unit_id=" + oidMaterial;
                        } else {
                            wherematerial = wherematerial + " or " + " ms.material_unit_id=" + oidMaterial;
                        }
                    }
                    wherematerial = "(" + wherematerial + ")";
                    if (where.length() > 0) {
                        where = where + " and " + wherematerial;
                    } else {
                        where = wherematerial;
                    }
                }

                if (where.length() > 0) {
                    sqlUnion = sqlUnion + " where " + where;
                }

                if (k < (srcMinimumStock.getvLocation().size() - 1)) {
                    sqlUnion = sqlUnion + " union ";
                }
            }

            sql = sql + sqlUnion + " ) as tbl ";
            sql = sql + " right join pos_material as m on tbl.material_unit_id = m.material_id ";

            sql = sql + " left join " + PstUnit.TBL_P2_UNIT
                    + " UNT ON m." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
                    + " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            if (srcMinimumStock.getOidSupplier() != 0) {
                sql = sql + " inner join pos_vendor_price as pvp on "
                        + " m.material_id = pvp.material_id ";
            }

            //update opie-eyek 2014-14-02 agar yang dimunculkan cuma type barang saja
            sql = sql + " where m.material_type=0 ";

            if (srcMinimumStock.getCategoryId() != 0) {
                //sql = sql + " and m.category_id=" + srcMinimumStock.getCategoryId();
                   String strGroup = " AND ( m." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcMinimumStock.getCategoryId();
                
                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak,srcMinimumStock.getCategoryId()) ;
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         strGroup=strGroup + " OR m." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                
                strGroup=strGroup+")";
                sql = sql + strGroup;
            }

            if (srcMinimumStock.getListOidMaterial().size() > 0) {
                String wherematerial = "";
                for (int k = 0; k < srcMinimumStock.getListOidMaterial().size(); k++) {
                    long oidMaterial = Long.parseLong((String) srcMinimumStock.getListOidMaterial().get(k));
                    if (wherematerial.length() == 0) {
                        wherematerial = "m.material_id=" + oidMaterial;
                    } else {
                        wherematerial = wherematerial + " or " + "m.material_id=" + oidMaterial;
                    }
                }
                wherematerial = "(" + wherematerial + ")";
                if (srcMinimumStock.getCategoryId() != 0) {
                    sql = sql + " and " + wherematerial;
                } else {
                    sql = sql + " where " + wherematerial;
                }
            }

            // addd by fitra
            String strCode = "";
            if (objMaterial.getSku() != "" && objMaterial.getSku().length() > 0) {
                sql = sql + " AND (m." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                        + " LIKE '%" + objMaterial.getSku() + "%')";
            }

            String strName = "";
            if (objMaterial.getName() != "" && objMaterial.getName().length() > 0) {
                sql = sql + " AND (m." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                        + " LIKE '%" + objMaterial.getName() + "%')";
            }

            if (srcMinimumStock.getOidMerk() != 0) {
                if (srcMinimumStock.getCategoryId() != 0) {
                    sql = sql + " and m.merk_id=" + srcMinimumStock.getOidMerk();
                } else if (srcMinimumStock.getListOidMaterial().size() > 0) {
                    sql = sql + " and m.merk_id=" + srcMinimumStock.getOidMerk();
                } else {
                    sql = sql + " where m.merk_id=" + srcMinimumStock.getOidMerk();
                }
            }

            if (srcMinimumStock.getListOidMaterial().size() > 0
                    || srcMinimumStock.getOidMerk() != 0 || srcMinimumStock.getCategoryId() != 0) {
                if (srcMinimumStock.getOidSupplier() != 0) {
                    sql = sql + " and pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
                }
            } else if (srcMinimumStock.getOidSupplier() != 0) {
                sql = sql + " where pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
            }

            sql = sql + " group by m.sku";

            if (recordToGet != 0) {
                sql = sql + " limit " + start + "," + recordToGet;
            }

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vitem = new Vector();
                Vector vMin = new Vector();
                Vector vStock = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                material.setAveragePrice(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                vitem.add(material);

                // ini untuk object minimum stock 
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vMin.add(String.valueOf(rs.getDouble(getFieldSum(1, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vMin);

                // ini untuk object stock on hand
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vStock.add(String.valueOf(rs.getDouble(getFieldSum(0, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vStock);

                Unit matUnit = new Unit();
                matUnit.setOID(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]));
                matUnit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                vitem.add(matUnit);

                list.add(vitem);
            }

        } catch (Exception e) {
            System.out.println("err minimum stock : " + e.toString());
        }
        return list;
    }

    public static Vector getSearchMaterialListMinimumStock(SrcMinimumStock srcMinimumStock, int start, int recordToGet, Material objMaterial) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        String strGroup = "";
        try {
            String sqlUnion = "";
            String sql = "SELECT m." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
                    + ", m." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]
                    + ", UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE]
                    + ", UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + ", " + createFieldSum(srcMinimumStock.getvLocation()) + " FROM (";

            for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                Location location = (Location) srcMinimumStock.getvLocation().get(k);
                sqlUnion = sqlUnion + "SELECT ms.location_id, material_unit_id, "
                        + createField(srcMinimumStock.getvLocation(), location.getOID(), k)
                        + " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS ms";

                String where = "";
                boolean viewedData = false;
                if ((objMaterial.getCategoryId() != 0) && (objMaterial.getMerkId() != 0)) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";
                    where = " pm.category_id=" + srcMinimumStock.getCategoryId()
                            + " and pm.merk_id=" + srcMinimumStock.getOidMerk();

                    viewedData = true;

                } else if (srcMinimumStock.getCategoryId() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";

                    if (srcMinimumStock.getCategoryId() > 0) {
                        where = " (pm.category_id=" + srcMinimumStock.getCategoryId();

                        String wherex = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID] + "='" + srcMinimumStock.getCategoryId() + "' OR "
                                + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "='" + srcMinimumStock.getCategoryId() + "'";

//                        Vector masterCatAcak = PstCategory.list(0,0,wherex,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
//
//                        if(masterCatAcak.size()>1){
//                            //Vector materGroup = PstCategory.structureList(masterCatAcak) ;
//                            //Vector<Long> levelParent = new Vector<Long>();
//                            for(int i=0; i<masterCatAcak.size(); i++) {
//                                 Category mGroup = (Category)masterCatAcak.get(i);
//                                 where=where + " OR pm.category_id"+
//                                    " = " + mGroup.getOID();
//                            }
//                        }
                        Vector masterCatAcak = PstCategory.list(0, 0, wherex, PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);

                        if (masterCatAcak.size() > 1) {
                            //Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                            for (int i = 0; i < masterCatAcak.size(); i++) {
                                Category mGroup = (Category) masterCatAcak.get(i);
                                where = where + " OR pm.category_id"
                                        + " = " + mGroup.getOID();
                            }
                        }

                        where = where + ")";
                    }
                    viewedData = true;
                } else if (srcMinimumStock.getOidMerk() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_material as pm on "
                            + " ms.material_unit_id = pm.material_id ";

                    where = " pm.merk_id=" + srcMinimumStock.getOidMerk();
                    viewedData = true;
                }
                if (viewedData) {
                    if (where.length() > 0) {
                        where = where + " and pm.process_status !=" + PstMaterial.DELETE;
                    } else {
                        where = " pm.process_status !=" + PstMaterial.DELETE;
                    }
                }

                if (srcMinimumStock.getOidSupplier() != 0) {
                    sqlUnion = sqlUnion + " inner join pos_vendor_price as pvp on "
                            + " ms.material_unit_id = pvp.material_id ";

                    if (where.length() > 0) {

                        where = where + " and pvp.vendor_id=" + srcMinimumStock.getOidSupplier();

                    } else {
                        where = " pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
                    }
                }

                //if(srcMinimumStock.getLocationId()!=0){
                if (where.length() > 0) {

                    where = where + " and ms.location_id=" + location.getOID();
                } else {
                    where = " ms.location_id=" + location.getOID();
                }
                //}

                if (srcMinimumStock.getPeriodId() != 0) {
                    if (where.length() > 0) {
                        where = where + " and ms.periode_id = " + srcMinimumStock.getPeriodId();
                    } else {
                        where = " ms.periode_id = " + srcMinimumStock.getPeriodId();
                    }
                }

                if (srcMinimumStock.getListOidMaterial().size() > 0) {
                    String wherematerial = "";
                    for (int j = 0; j < srcMinimumStock.getListOidMaterial().size(); j++) {
                        long oidMaterial = Long.parseLong((String) srcMinimumStock.getListOidMaterial().get(j));
                        if (wherematerial.length() == 0) {
                            wherematerial = " ms.material_unit_id=" + oidMaterial;
                        } else {
                            wherematerial = wherematerial + " or " + " ms.material_unit_id=" + oidMaterial;
                        }
                    }
                    wherematerial = "(" + wherematerial + ")";
                    if (where.length() > 0) {
                        where = where + " and " + wherematerial;
                    } else {
                        where = wherematerial;
                    }
                }

                if (where.length() > 0) {
                    sqlUnion = sqlUnion + " where " + where;
                }

                if (k < (srcMinimumStock.getvLocation().size() - 1)) {
                    sqlUnion = sqlUnion + " union ";
                }
            }

            sql = sql + sqlUnion + " ) as tbl ";
            sql = sql + " right join pos_material as m on tbl.material_unit_id = m.material_id ";

            sql = sql + " left join " + PstUnit.TBL_P2_UNIT
                    + " UNT ON m." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
                    + " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            if (srcMinimumStock.getOidSupplier() != 0) {
                sql = sql + " inner join pos_vendor_price as pvp on "
                        + " m.material_id = pvp.material_id ";
            }

            //update opie-eyek 2014-14-02 agar yang dimunculkan cuma type barang saja
            sql = sql + " where m.material_type=0 ";

            if (srcMinimumStock.getCategoryId() != 0) {
                //sql = sql + " and m.category_id=" + srcMinimumStock.getCategoryId();

                if (srcMinimumStock.getCategoryId() > 0) {
                    sql = sql + " and (m.category_id=" + srcMinimumStock.getCategoryId();

                    String wherex = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID] + "='" + srcMinimumStock.getCategoryId() + "' OR "
                            + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "='" + srcMinimumStock.getCategoryId() + "'";
//
//                        Vector masterCatAcak = PstCategory.list(0,0,wherex,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
//
//                        if(masterCatAcak.size()>1){
//                            Vector materGroup = PstCategory.structureList(masterCatAcak) ;
//                            Vector<Long> levelParent = new Vector<Long>();
//                            for(int i=0; i<materGroup.size(); i++) {
//                                 Category mGroup = (Category)materGroup.get(i);
//                                 sql=sql + " OR m.category_id"+
//                                    " = " + mGroup.getOID();
//                            }
//                        }
//                        sql=sql+")";
                    Vector masterCatAcak = PstCategory.list(0, 0, wherex, PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);

                    if (masterCatAcak.size() > 1) {
                        //Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                        for (int i = 0; i < masterCatAcak.size(); i++) {
                            Category mGroup = (Category) masterCatAcak.get(i);
                            sql = sql + " OR m.category_id"
                                    + " = " + mGroup.getOID();
                        }
                    }

                    sql = sql + ")";

                }

            }

            //ditambah
            String strCode = "";
            if (objMaterial.getSku() != "" && objMaterial.getSku().length() > 0) {
                strCode = "(m." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                        + " LIKE '%" + objMaterial.getSku() + "%')";
            }

            String strBarcode = "";
            if (objMaterial.getBarCode() != "" && objMaterial.getBarCode().length() > 0) {
                strBarcode = "(m." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]
                        + " LIKE '%" + objMaterial.getBarCode() + "%')";
            }

            String strName = "";
            if (objMaterial.getName() != "" && objMaterial.getName().length() > 0) {
                sql = sql + " AND (m." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                        + " LIKE '%" + objMaterial.getName() + "%')";
            }

            if (srcMinimumStock.getListOidMaterial().size() > 0) {
                String wherematerial = "";
                for (int k = 0; k < srcMinimumStock.getListOidMaterial().size(); k++) {
                    long oidMaterial = Long.parseLong((String) srcMinimumStock.getListOidMaterial().get(k));
                    if (wherematerial.length() == 0) {
                        wherematerial = "m.material_id=" + oidMaterial;
                    } else {
                        wherematerial = wherematerial + " or " + "m.material_id=" + oidMaterial;
                    }
                }
                wherematerial = "(" + wherematerial + ")";
                if (srcMinimumStock.getCategoryId() != 0) {
                    sql = sql + " and " + wherematerial;
                } else {
                    sql = sql + " where " + wherematerial;
                }
            }

            if (srcMinimumStock.getOidMerk() != 0) {
                if (srcMinimumStock.getCategoryId() != 0) {
                    sql = sql + " and m.merk_id=" + srcMinimumStock.getOidMerk();
                } else if (srcMinimumStock.getListOidMaterial().size() > 0) {
                    sql = sql + " and m.merk_id=" + srcMinimumStock.getOidMerk();
                } else {
                    sql = sql + " where m.merk_id=" + srcMinimumStock.getOidMerk();
                }
            }

            if (srcMinimumStock.getListOidMaterial().size() > 0
                    || srcMinimumStock.getOidMerk() != 0 || srcMinimumStock.getCategoryId() != 0) {
                if (srcMinimumStock.getOidSupplier() != 0) {
                    sql = sql + " and pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
                }
            } else if (srcMinimumStock.getOidSupplier() != 0) {
                sql = sql + " where pvp.vendor_id=" + srcMinimumStock.getOidSupplier();
            }

            //sampai sini
            if (strCode.length() > 0) {
                if (strBarcode.length() > 0) { // kondisi pencarian berdasarkan code/barcode material
                    if (sql.length() > 0) {
                        sql = sql + " AND (" + strCode + " OR " + strBarcode + ")";
                    } else {
                        sql = sql + "(" + strCode + " OR " + strBarcode + ")";
                    }
                } else // kondisi pencarian hanya berdasarkan code material
                if (sql.length() > 0) {
                    sql = sql + " AND " + strCode;
                } else {
                    sql = sql + strCode;
                }
            }

            sql = sql + " group by m.sku";

            if (recordToGet != 0) {
                sql = sql + " limit " + start + "," + recordToGet;
            }

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vitem = new Vector();
                Vector vMin = new Vector();
                Vector vStock = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                vitem.add(material);

                // ini untuk object minimum stock 
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vMin.add(String.valueOf(rs.getDouble(getFieldSum(1, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vMin);

                // ini untuk object stock on hand
                for (int k = 0; k < srcMinimumStock.getvLocation().size(); k++) {
                    vStock.add(String.valueOf(rs.getDouble(getFieldSum(0, k, srcMinimumStock.getvLocation()))));
                }
                vitem.add(vStock);

                Unit matUnit = new Unit();
                matUnit.setOID(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]));
                matUnit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                vitem.add(matUnit);

                list.add(vitem);
            }

        } catch (Exception e) {
            System.out.println("err minimum stock : " + e.toString());
        }
        return list;
    }

    public static void loadMaterialRequestSearchByHash() {
        int multiLanguageName = 0;
        try {
            multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueByName("NAME_MATERIAL_MULTI_LANGUAGE"));
        } catch (Exception e) {
            multiLanguageName = 0;
        }

        try {

            if (materialrequestsearch == null) {
                materialrequestsearch = new Hashtable();
            }

            String where = PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = '0' AND " + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "!='" + PstMaterial.EDIT_NON_AKTIVE + "'";
            Vector listMaterialRequestSearch = PstMaterial.listHastableAutoComplate(0, 0, where, "");
            if (listMaterialRequestSearch != null) {
                for (int i = 0; i < listMaterialRequestSearch.size(); i++) {
                    Material material = (Material) listMaterialRequestSearch.get(i);
                    //materialrequestsearch.put(""+i, material.getName());
                    if (multiLanguageName == 1) {
                        String[] smartPhonesSplits;
                        smartPhonesSplits = material.getName().split("\\;");
                        String goodName = "";
                        try {
                            goodName = smartPhonesSplits[0];
                        } catch (Exception ex) {
                            goodName = material.getName();
                        }
                        materialrequestsearch.put("" + i, goodName);
                    } else {
                        materialrequestsearch.put("" + i, material.getName());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            return;
        }
    }

    public static void loadMaterialComponentRequestSearchByHash() {

        int multiLanguageName = 0;
        try {
            multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueByName("NAME_MATERIAL_MULTI_LANGUAGE"));
        } catch (Exception e) {
            multiLanguageName = 0;
        }

        try {

            if (materialComponentrequestsearch == null) {
                materialComponentrequestsearch = new Hashtable();
            }

            String where = "( " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = '1' OR " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = '2') AND " + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "!='" + PstMaterial.EDIT_NON_AKTIVE + "'";
            Vector listMaterialRequestSearch = PstMaterial.listHastableAutoComplate(0, 0, where, "");
            if (listMaterialRequestSearch != null) {
                for (int i = 0; i < listMaterialRequestSearch.size(); i++) {
                    Material material = (Material) listMaterialRequestSearch.get(i);

                    if (multiLanguageName == 1) {
                        String[] smartPhonesSplits;
                        smartPhonesSplits = material.getName().split("\\;");
                        String goodName = "";
                        try {
                            goodName = smartPhonesSplits[0];
                        } catch (Exception ex) {
                            goodName = material.getName();
                        }
                        materialComponentrequestsearch.put("" + i, goodName);
                    } else {
                        materialComponentrequestsearch.put("" + i, material.getName());
                    }

                }
            }

        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            return;
        }
    }

    public static Hashtable getMaterialRequestSearchByHash() {
        try {
            if (materialrequestsearch == null) {
                loadMaterialRequestSearchByHash();
            }
        } catch (Exception e) {
            System.out.println("materialrequestsearch : " + e.toString());
        } finally {
            return materialrequestsearch;
        }
    }

    public static Hashtable getMaterialComponentRequestSearchByHash() {
        try {
            if (materialComponentrequestsearch == null) {
                loadMaterialComponentRequestSearchByHash();
            }
        } catch (Exception e) {
            System.out.println("materialrequestsearch : " + e.toString());
        } finally {
            return materialComponentrequestsearch;
        }
    }

}
