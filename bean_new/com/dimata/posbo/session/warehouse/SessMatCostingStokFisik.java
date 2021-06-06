/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.entity.search.SrcMaterialRepostingStock;
import com.dimata.posbo.entity.warehouse.MatStockOpname;
import com.dimata.posbo.entity.warehouse.MatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatStockOpname;
import static com.dimata.posbo.entity.warehouse.PstMatStockOpname.TBL_MAT_STOCK_OPNAME;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SessMatCostingStokFisik {
    
    
    public static double qtyMaterialBasedOnStockCardNoSale(long materialOid, long locationOid, Date dtstartReposting, int status) {
        
        double qtyReal = 0.0;
        try {
            Formater.formatDate(dtstartReposting, "dd-MM-yyyy", "dd-mm-yyyy");
	    Date dtstart = dtstartReposting;
            Vector LastOpname = getLastDateOpnameReposting(locationOid, materialOid, dtstart);
            Date dtLastOpnameDate = null;
            double qtyLastOpname = 0.0;

           if(LastOpname!=null && LastOpname.size()>0){   
                for(int i=0; i<1; i++) {
                    Vector vetTemp = (Vector)LastOpname.get(i);
                    MatStockOpname matStockOpname = (MatStockOpname)vetTemp.get(0);
                    MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem)vetTemp.get(1);
                    dtLastOpnameDate = matStockOpname.getStockOpnameDate();
                    qtyLastOpname = matStockOpnameItem.getQtyOpname();
                }
           }
            if (dtLastOpnameDate != null){
                dtstart = dtLastOpnameDate;
            } else {
                Periode periode = PstPeriode.getPeriodeRunning();
                dtstart = periode.getStartDate();
            }
            
            SrcMaterialRepostingStock srcMaterialRepostingStock = new SrcMaterialRepostingStock();
            Vector vectSt = new Vector(1,1);
            Vector listDocStatus = new Vector(1, 1);
            String strPrStatus = "";
            
            listDocStatus.add("5");
            listDocStatus.add("7");
            
            srcMaterialRepostingStock.setDateFrom(dtstart);
            srcMaterialRepostingStock.setDateTo(dtstartReposting);
            srcMaterialRepostingStock.setMaterialId(materialOid);
            srcMaterialRepostingStock.setLocationId(locationOid);
            srcMaterialRepostingStock.setDocStatus(listDocStatus);

            StockCardReport stockCardReportRec = new StockCardReport();
            double qtyOpname = 0.0;
            double qtyReceive = 0.0;
            double qtyDispatch = 0.0;
            double qtyReturn = 0.0;
            double qtySale =0.0;
            double qtySaleReturn =0.0;
            double qtyCosting = 0.0;

                SessMatReceive.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyReceive = srcMaterialRepostingStock.getQty();
                SessMatDispatch.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyDispatch = srcMaterialRepostingStock.getQty();
                SessMatReturn.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyReturn = srcMaterialRepostingStock.getQty();
                
//                SessReportSale.getQtyStockMaterialReposting(srcMaterialRepostingStock);
//                qtySale = 0.0;//srcMaterialRepostingStock.getQty();
//                SessReportSale.getQtyStockMaterialRepostingReturn(srcMaterialRepostingStock);
//                qtySaleReturn = srcMaterialRepostingStock.getQty();
//                
                SessMatCosting.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyCosting = srcMaterialRepostingStock.getQty();

                qtyReal = qtyLastOpname + qtyReceive - qtyDispatch - qtyReturn - qtySale +qtySaleReturn  - qtyCosting;
                

        } catch (Exception ee) {
            System.out.println (ee.toString());
        }
        return qtyReal;
    }
    
    public static double qtyMaterialBasedOnStockCard(long materialOid, long locationOid, Date dtstartReposting, int status) {
        double qtyReal = 0.0;
        try {
            Formater.formatDate(dtstartReposting, "dd-MM-yyyy", "dd-mm-yyyy");
            Date dtstart = dtstartReposting;
            Vector LastOpname = getLastDateOpnameReposting(locationOid, materialOid, dtstart);
            Date dtLastOpnameDate = null;
            double qtyLastOpname = 0.0;

            if (LastOpname != null && LastOpname.size() > 0) {
                for (int i = 0; i < 1; i++) {
                    Vector vetTemp = (Vector) LastOpname.get(i);
                    MatStockOpname matStockOpname = (MatStockOpname) vetTemp.get(0);
                    MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem) vetTemp.get(1);
                    dtLastOpnameDate = matStockOpname.getStockOpnameDate();
                    qtyLastOpname = matStockOpnameItem.getQtyOpname();
                }
            }
            if (dtLastOpnameDate != null) {
                //cek opie-eyek 20160620
//                if(dtLastOpnameDate.before(dtstart)){
//                    dtstart = dtLastOpnameDate;
//                }else{
//                    Periode periode = PstPeriode.getPeriodeRunning();
//                    dtstart = periode.getStartDate();
//                }
                dtstart = dtLastOpnameDate;
            } else {
                Periode periode = PstPeriode.getPeriodeRunning();
                dtstart = periode.getStartDate();
            }

            SrcMaterialRepostingStock srcMaterialRepostingStock = new SrcMaterialRepostingStock();
            Vector vectSt = new Vector(1, 1);
            Vector listDocStatus = new Vector(1, 1);
            String strPrStatus = "";

            String checkDocStatusFinal = PstSystemProperty.getValueByName("CHECK_STOK_DOC_STATUS_FINAL");
            if (checkDocStatusFinal.equals("1")) {
                listDocStatus.add("2");
            }
            listDocStatus.add("5");
            listDocStatus.add("7");

            srcMaterialRepostingStock.setDateFrom(dtstart);
            srcMaterialRepostingStock.setDateTo(dtstartReposting);
            srcMaterialRepostingStock.setMaterialId(materialOid);
            srcMaterialRepostingStock.setLocationId(locationOid);
            srcMaterialRepostingStock.setDocStatus(listDocStatus);

            StockCardReport stockCardReportRec = new StockCardReport();
            double qtyOpname = 0.0;
            double qtyReceive = 0.0;
            double qtyDispatch = 0.0;
            double qtyReturn = 0.0;
            double qtySale = 0.0;
            double qtySaleReturn = 0.0;
            double qtyCosting = 0.0;
            double qtyProductionCost = 0.0;
            double qtyProductionProduct = 0.0;

            SessMatReceive.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            qtyReceive = srcMaterialRepostingStock.getQty();
            SessMatDispatch.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            qtyDispatch = srcMaterialRepostingStock.getQty();
            SessMatReturn.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            qtyReturn = srcMaterialRepostingStock.getQty();
            SessReportSale.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            qtySale = srcMaterialRepostingStock.getQty();
            SessReportSale.getQtyStockMaterialRepostingReturn(srcMaterialRepostingStock);
            qtySaleReturn = srcMaterialRepostingStock.getQty();
            SessMatCosting.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            qtyCosting = srcMaterialRepostingStock.getQty();
            SessProduction.getQtyStockMaterialRepostingCost(srcMaterialRepostingStock);
            qtyProductionCost = srcMaterialRepostingStock.getQty();
            SessProduction.getQtyStockMaterialRepostingProduct(srcMaterialRepostingStock);
            qtyProductionProduct = srcMaterialRepostingStock.getQty();

            qtyReal = qtyLastOpname + qtyReceive - qtyDispatch - qtyReturn - qtySale + qtySaleReturn - qtyCosting - qtyProductionCost + qtyProductionProduct;

        } catch (Exception ee) {
            System.out.println(ee.toString());
        }
        return qtyReal;
    }

    public static double beratMaterialBasedOnStockCard(long materialOid, long locationOid, Date dtstartReposting, int status) {

        double beratReal = 0.0;
        try {
            Formater.formatDate(dtstartReposting, "dd-MM-yyyy", "dd-mm-yyyy");
            Date dtstart = dtstartReposting;
            Vector LastOpname = getLastDateOpnameReposting(locationOid, materialOid, dtstart);
            Date dtLastOpnameDate = null;
            double beratLastOpname = 0.0;

            if (LastOpname != null && LastOpname.size() > 0) {
                for (int i = 0; i < 1; i++) {
                    Vector vetTemp = (Vector) LastOpname.get(i);
                    MatStockOpname matStockOpname = (MatStockOpname) vetTemp.get(0);
                    MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem) vetTemp.get(1);
                    dtLastOpnameDate = matStockOpname.getStockOpnameDate();
                    beratLastOpname = matStockOpnameItem.getBeratOpname();
                }
            }
            if (dtLastOpnameDate != null) {
                //cek opie-eyek 20160620
//                if(dtLastOpnameDate.before(dtstart)){
//                    dtstart = dtLastOpnameDate;
//                }else{
//                    Periode periode = PstPeriode.getPeriodeRunning();
//                    dtstart = periode.getStartDate();
//                }
                dtstart = dtLastOpnameDate;
            } else {
                Periode periode = PstPeriode.getPeriodeRunning();
                dtstart = periode.getStartDate();
            }

            SrcMaterialRepostingStock srcMaterialRepostingStock = new SrcMaterialRepostingStock();
            Vector vectSt = new Vector(1, 1);
            Vector listDocStatus = new Vector(1, 1);
            String strPrStatus = "";

            listDocStatus.add("5");
            listDocStatus.add("7");

            srcMaterialRepostingStock.setDateFrom(dtstart);
            srcMaterialRepostingStock.setDateTo(dtstartReposting);
            srcMaterialRepostingStock.setMaterialId(materialOid);
            srcMaterialRepostingStock.setLocationId(locationOid);
            srcMaterialRepostingStock.setDocStatus(listDocStatus);

            StockCardReport stockCardReportRec = new StockCardReport();
            double beratOpname = 0.0;
            double beratReceive = 0.0;
            double beratDispatch = 0.0;
            double beratReturn = 0.0;
            double beratSale = 0.0;
            double beratSaleReturn = 0.0;
            double beratCosting = 0.0;

            SessMatReceive.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            beratReceive = srcMaterialRepostingStock.getBerat();
            srcMaterialRepostingStock.setBerat(0);
            SessMatDispatch.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            beratDispatch = srcMaterialRepostingStock.getBerat();
            srcMaterialRepostingStock.setBerat(0);
            SessMatReturn.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            beratReturn = srcMaterialRepostingStock.getBerat();
            srcMaterialRepostingStock.setBerat(0);
            SessReportSale.getQtyStockMaterialReposting(srcMaterialRepostingStock);
            beratSale = srcMaterialRepostingStock.getBerat();
            srcMaterialRepostingStock.setBerat(0);
            SessReportSale.getQtyStockMaterialRepostingReturn(srcMaterialRepostingStock);
            beratSaleReturn = srcMaterialRepostingStock.getBerat();
            srcMaterialRepostingStock.setBerat(0);
            SessMatCosting.getQtyStockMaterialReposting(srcMaterialRepostingStock);//???
            beratCosting = srcMaterialRepostingStock.getBerat();
            srcMaterialRepostingStock.setBerat(0);

            beratReal = beratLastOpname + beratReceive - beratDispatch - beratReturn - beratSale + beratSaleReturn - beratCosting;

        } catch (Exception ee) {
            System.out.println(ee.toString());
        }
        return beratReal;
    }

    public static Vector getLastDateOpnameReposting(long oidLocation, long oidMaterial, Date dtstart){
        Vector list = new Vector();
        DBResultSet dbrs = null;
        //MatStockOpname matOpname = new MatStockOpname();
        try{
            String sql = " SELECT OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                         " , SUM(OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + ") AS SUM_QTY" +
                         " , SUM(OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_OPNAME] + ") AS SUM_BERAT" +
                         " FROM "+ TBL_MAT_STOCK_OPNAME + " OPN " +
                         " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " OPNITEM " +
                         " ON OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]+
                         " = OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+
                         " WHERE OPNITEM."+ PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]+"="+oidMaterial+
                         " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + oidLocation +
                         " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + "<='"+dtstart+"'"+
                         " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + "='7'" +
                         " GROUP BY " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]+
                         " ORDER BY OPN. " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]+ " DESC ";
          System.out.println("getLastDateOpnameReposting : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                MatStockOpnameItem matStockOpnameItem =new MatStockOpnameItem();

                Date date = DBHandler.convertDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]), rs.getTime(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
                matStockOpname.setStockOpnameDate(date);
                vt.add(matStockOpname);

                matStockOpnameItem.setQtyOpname(rs.getDouble("SUM_QTY"));
                matStockOpnameItem.setBeratOpname(rs.getDouble("SUM_BERAT"));
                vt.add(matStockOpnameItem);

                list.add(vt);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("err getLastOpnameDate : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
        return list;
    }
    
    public static Vector getTotalCosting(long oidMaterial, String multiLocationId, Date dateStartCosting, Date dateEndCosting) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        String dStart = Formater.formatDate(dateStartCosting, "yyyy-MM-dd");
        String dEnd = Formater.formatDate(dateEndCosting, "yyyy-MM-dd");
        try {
            String sql = " SELECT "
                    + " SUM(cmi." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS qty_item "
                    + ", SUM(cmi." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + " * cmi." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP] + ") AS sub_total_biaya "
                    + " FROM " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " AS cmi "
                    + " INNER JOIN " + PstMatCosting.TBL_COSTING + " AS cm "
                    + " ON cm." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]
                    + " = cmi." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]
                    + " WHERE DATE(" + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + ") "
                    + " BETWEEN '" + dStart + "' AND '" + dEnd + "'";
            if (multiLocationId.length() > 0) {
                sql += " AND cm." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] + " IN (" + multiLocationId + ")";
            }
            sql += ""
                    + " AND cmi." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + " = " + oidMaterial
                    + " GROUP BY cmi." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArrayList<Double> data = new ArrayList<Double>();
                data.add(rs.getDouble("qty_item"));
                data.add(rs.getDouble("sub_total_biaya"));
                list.add(data);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err getLastOpnameDate : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

}


