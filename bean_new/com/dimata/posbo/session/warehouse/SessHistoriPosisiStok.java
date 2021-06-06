/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.masterdata.HistoryPosisiStok;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.search.SrcMaterial;
import com.dimata.posbo.entity.search.SrcReportPotitionStock;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author dimata005
 */
public class SessHistoriPosisiStok {

    public static final int VIEW_DEAD_STOK_ALL = 0;
    public static final int VIEW_DEAD_STOK_NO_MOVEMENT = 1;
    public static final int VIEW_DEAD_STOK_YES_MOVEMENT_NO_SALE = 2;
    public static final int VIEW_DEAD_STOK_NO_MOVE_AND_YES_MOVE_NO_SALE = 3;

    public static void getReportStockAll(JspWriter out, SrcReportPotitionStock srcReportPotitionStock) {
        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);
        DBResultSet dbrs = null;
        SrcReportPotitionStock srcReport = (SrcReportPotitionStock) srcReportPotitionStock;
        try {
            String sql = "" + queryPosisiStok(srcReport);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int loop = 0;
            //Hashtable hashReportRekap = getHastableReportStockAll(srcReport);

            //cari stok dari awal periode
//            Vector vect = PstPeriode.list(0, 0, "", PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]);
//            Date startDateFromSaldoAwal = (Date) srcReport.getDateFrom().clone();
//            if (vect != null && vect.size() > 0) {
//                Periode periode = (Periode) vect.get(0);
//                srcReport.setDateFrom(periode.getStartDate());
//            }
            srcReport.setDateTo(null);
            //srcReport.setDateTo(dateTo);
            //UPDATED BY DEWOK 2018-08-11 - RUMUS SALDO AWAL DI BAWAH INI KELIRU
            //Hashtable hashReportRekapAwalPeriode = getHastableReportStockAll(srcReport);
            double saldo = 0;
            double qtyReceiveSupp = 0;
            double qtyReceiveWarehouse = 0;
            double qtyReturnSales = 0;
            double qtyTotalIn = 0;
            double qtyTransfer = 0;
            double qtyReturnSupplier = 0;
            double qtySales = 0;
            double qtyCost = 0;
            double qtyTotalOut = 0;
            double qtySubtotal = 0;
            double qtyOpname = 0;
            double qtySelisihOpnameMinus = 0;
            double qtySelisihOpnamePlus = 0;
            double qtyBalance = 0;
            double totalNilaiStok = 0;
            double qtyProductionOut = 0;
            double qtyProductionIn = 0;

            int typeDeadStok = srcReportPotitionStock.getViedDeadStok();

            while (rs.next()) {

                try {
                    HistoryPosisiStok historyPosisiStok = new HistoryPosisiStok();
                    try {
                        historyPosisiStok.setMaterialId(rs.getLong("MATERIAL_ID"));
                        historyPosisiStok.setSku(rs.getString("tab.SKU"));

                        historyPosisiStok.setBarcode(rs.getString("BARCODE"));//barcode
                        historyPosisiStok.setNamaItem(rs.getString("tab.NAME"));//nama
                        historyPosisiStok.setHpp(rs.getDouble("AVERAGE_PRICE"));//hpp
                        if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
                            for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
                                for (int j = 0; j < listCurrStandardX.size(); j++) {
                                    Vector temp = (Vector) listCurrStandardX.get(j);
                                    CurrencyType curr = (CurrencyType) temp.get(0);
                                    PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
                                }
                            }
                        }
                        historyPosisiStok.setUnit(rs.getString("UNIT"));//unit
                        historyPosisiStok.setSaldoAwal(0);//saldo
                        historyPosisiStok.setReceiveSupplier(rs.getDouble("QTY_RECEIVE_SUPP"));//receive supp
                        historyPosisiStok.setReceiveWarehouse(rs.getDouble("QTY_RECEIVE_WAREHOUSE"));//receive warehouse
                        historyPosisiStok.setReturnSales(rs.getDouble("QTY_SALES_RETURN"));//return sales
                        historyPosisiStok.setTotalIn(rs.getDouble("TOTAL_IN"));//total in
                        historyPosisiStok.setTransfer(rs.getDouble("QTY_TRANSFER"));//transfer
                        historyPosisiStok.setReturnSupplier(rs.getDouble("QTY_RETURN_SUPPLIER"));//return supplier
                        historyPosisiStok.setSales(rs.getDouble("QTY_SALES"));//sales
                        historyPosisiStok.setCost(rs.getDouble("QTY_COSTING"));//cost
                        historyPosisiStok.setTotalOut(rs.getDouble("TOTAL_OUT"));//total out
                        historyPosisiStok.setSubTotal(rs.getDouble("SUBTOTAL"));//subtotal
                        historyPosisiStok.setOpname(rs.getDouble("QTY_OPNAME"));//opname
                        historyPosisiStok.setProductionOut(rs.getDouble("QTY_PRODUCTION_OUT"));//production out
                        historyPosisiStok.setProductionIn(rs.getDouble("QTY_PRODUCTION_IN"));//production in
                        if (rs.getDouble("QTY_OPNAME_SELISIH") >= 0) {
                            historyPosisiStok.setSelisihOpnamePlus(rs.getDouble("QTY_OPNAME_SELISIH"));//selisih plus
                            historyPosisiStok.setSelisihOpnameMinus(0);
                        } else {

                            historyPosisiStok.setSelisihOpnameMinus(rs.getDouble("QTY_OPNAME_SELISIH"));//selisih plus
                            historyPosisiStok.setSelisihOpnamePlus(0);
                        }
                    } catch (Exception ex) {

                    }

                    boolean loopNext = true;
                    if (typeDeadStok != VIEW_DEAD_STOK_ALL) {
                        if (typeDeadStok == VIEW_DEAD_STOK_NO_MOVEMENT) {
                            if (historyPosisiStok.getTotalIn() != 0 || historyPosisiStok.getTotalOut() != 0) {
                                loopNext = false;
                            }
                        } else if (typeDeadStok == VIEW_DEAD_STOK_YES_MOVEMENT_NO_SALE) {
                            if (historyPosisiStok.getSales() != 0) {
                                loopNext = false;
                            }
                        } else if (typeDeadStok == VIEW_DEAD_STOK_NO_MOVE_AND_YES_MOVE_NO_SALE) {
                            if (historyPosisiStok.getTotalIn() == 0 && historyPosisiStok.getTotalOut() == 0) {
                                loopNext = false;
                            }
                        }
                    }

                    if (loopNext) {
                        loop = loop + 1;
                        out.print("<tr>");
                        out.print("<td class=\"tg-yw4l\">" + loop + "</td>");//no
                        out.print("<td style='mso-number-format:\"\\@\"' class=\"tg-yw4l\">" + rs.getString("SKU") + "</td>");//sku
                        out.print("<td style='mso-number-format:\"\\@\"' class=\"tg-yw4l\">" + rs.getString("BARCODE") + "</td>");//barcode
                        //out.print("<td class=\"tg-yw4l\">"+rs.getString("NAME")+"</td>");//nama
                        out.print("<td class=\"tg-yw4l\"><a href=\"javascript:cmdViewKartuStock('" + rs.getLong("MATERIAL_ID") + "','" + rs.getString("SKU") + "','" + rs.getString("NAME") + "')\">" + rs.getString("NAME") + "</a></td>");

                        out.print("<td class=\"tg-yw4l\">" + FRMHandler.userFormatStringDecimal(rs.getDouble("AVERAGE_PRICE")) + "</td>");//hpp

                        if (srcReport.getvPriceTypeId() != null && srcReport.getvPriceTypeId().size() > 0) {
                            SrcMaterial srcMaterial = new SrcMaterial();
                            Material material = new Material();
                            material.setOID(rs.getLong("MATERIAL_ID"));
                            Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, material, rs.getLong("MATERIAL_ID"));
                            for (int i = 0; i < srcReport.getvPriceTypeId().size(); i++) {
                                PriceType pricetype = (PriceType) srcReport.getvPriceTypeId().get(i);
                                for (int j = 0; j < listCurrStandardX.size(); j++) {
                                    Vector tempStand = (Vector) listCurrStandardX.get(j);
                                    CurrencyType currx = (CurrencyType) tempStand.get(0);
                                    StandartRate standart = (StandartRate) tempStand.get(1);

                                    PriceTypeMapping pTypeMapping = null;
                                    if (memberPrice != null && !memberPrice.isEmpty()) {

                                        pTypeMapping = (PriceTypeMapping) memberPrice.get("" + pricetype.getOID() + "_" + standart.getOID());
                                    }

                                    if (pTypeMapping == null) {
                                        pTypeMapping = new PriceTypeMapping();
                                        pTypeMapping.setMaterialId(material.getOID());
                                        pTypeMapping.setPriceTypeId(pricetype.getOID());
                                        pTypeMapping.setStandartRateId(currx.getOID());
                                    }
                                    //out.print("<td class=\"" + cellStyle + "\" div align=\"center\">"+FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice())+"</div>");
                                    out.print("<td class=\"tg-yw4l\">" + FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()) + "</td>");//unit
                                }
                            }
                        }

                        out.print("<td class=\"tg-yw4l\">" + rs.getString("UNIT") + "</td>");//unit
                        double saldoAwal = 0;
                        //ADDED BY DEWOK 2018-08-11, GET SALDO AWAL BASE ON STOCK CARD
                        saldoAwal = getPreviousStockForHistoryStockReport(rs.getLong("MATERIAL_ID"), srcReport.getLocationId(), srcReport.getDateFrom());
                        out.print("<td class=\"tg-yw4l\">" + saldoAwal + "</td>");//saldo

                        //UPDATED BY DEWOK 2018-08-11
                        //SALDO AWAL DIPEROLEH DENGAN CARA YG SAMA DENGAN KARTU STOK
                            /* CODE DI BAWAH INI TIDAK DIPAKAI LAGI
                         if(hashReportRekapAwalPeriode!=null && hashReportRekapAwalPeriode.size()>0 ){
                         HistoryPosisiStok historyPosisiStokAwal  = new HistoryPosisiStok();
                         try{
                         historyPosisiStokAwal  = (HistoryPosisiStok) hashReportRekapAwalPeriode.get(""+rs.getString("MATERIAL_ID"));
                         out.print("<td class=\"tg-yw4l\">"+
                         (historyPosisiStokAwal.getTotalInCalculation()-
                         historyPosisiStokAwal.getTotalOutCalculation()+
                         historyPosisiStokAwal.getSelisihOpnameMinus()+
                         historyPosisiStokAwal.getSelisihOpnamePlus())
                         +"</td>");//saldo
                         saldoAwal=(historyPosisiStokAwal.getTotalInCalculation()-historyPosisiStokAwal.getTotalOutCalculation()+
                         historyPosisiStokAwal.getSelisihOpnameMinus()+historyPosisiStokAwal.getSelisihOpnamePlus());
                         saldo=saldo+saldoAwal;
                         }catch(Exception ex){
                         out.print("<td class=\"tg-yw4l\"></td>");//saldo
                         saldoAwal=0;
                         }
                         }else{
                         out.print("<td class=\"tg-yw4l\">-</td>");//saldo
                         }
                         */
                        //munculkan hastable
                        if (true) {

                            try {
                                qtyReceiveSupp = qtyReceiveSupp + historyPosisiStok.getReceiveSupplier();
                                qtyReceiveWarehouse = qtyReceiveWarehouse + historyPosisiStok.getReceiveWarehouse();
                                qtyReturnSales = qtyReturnSales + historyPosisiStok.getReturnSales();
                                qtyTotalIn = qtyTotalIn + historyPosisiStok.getTotalIn();
                                qtyTransfer = qtyTransfer + historyPosisiStok.getTransfer();
                                qtyReturnSupplier = qtyReturnSupplier + historyPosisiStok.getReturnSupplier();
                                qtySales = qtySales + historyPosisiStok.getSales();
                                qtyCost = qtyCost + historyPosisiStok.getCost();
                                qtySubtotal = qtySubtotal + historyPosisiStok.getSubTotal();
                                qtyOpname = qtyOpname + historyPosisiStok.getOpname();
                                qtySelisihOpnameMinus = qtySelisihOpnameMinus + historyPosisiStok.getSelisihOpnameMinus();
                                qtySelisihOpnamePlus = qtySelisihOpnamePlus + historyPosisiStok.getSelisihOpnamePlus();
                                qtyTotalOut = qtyTotalOut + historyPosisiStok.getTotalOut();
                                qtyProductionIn = qtyProductionIn + historyPosisiStok.getProductionIn();
                                qtyProductionOut = qtyProductionOut + historyPosisiStok.getProductionOut();

                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReceiveSupplier() + "</td>");//receive supp
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReceiveWarehouse() + "</td>");//receive warehouse
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReturnSales() + "</td>");//return sales
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getProductionIn()+ "</td>");//production in
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getTotalIn() + "</td>");//total in
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getTransfer() + "</td>");//transfer
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReturnSupplier() + "</td>");//return supplier
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSales() + "</td>");//sales
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getCost() + "</td>");//cost
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getProductionOut()+ "</td>");//production out
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getTotalOut() + "</td>");//total out
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSubTotal() + "</td>");//subtotal
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getOpname() + "</td>");//opname
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSelisihOpnamePlus() + "</td>");//selisih plus
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSelisihOpnameMinus() + "</td>");//selisih minus
                                double totalStok = (saldoAwal + historyPosisiStok.getTotalInCalculation()
                                        - historyPosisiStok.getTotalOutCalculation()
                                        + historyPosisiStok.getSelisihOpnameMinus()
                                        + historyPosisiStok.getSelisihOpnamePlus());
                                out.print("<td class=\"tg-yw4l\">" + totalStok + "</td>");//balance
                                out.print("<td class=\"tg-yw4l\">" + FRMHandler.userFormatStringDecimal(totalStok * rs.getDouble("AVERAGE_PRICE")) + "</td>");//balance

                                qtyBalance = qtyBalance + totalStok;
                                totalNilaiStok = totalNilaiStok + (totalStok * rs.getDouble("AVERAGE_PRICE"));
                            } catch (Exception ex) {
                                out.print("<td class=\"tg-yw4l\">-</td>");//receive supp
                                out.print("<td class=\"tg-yw4l\">-</td>");//receive warehouse
                                out.print("<td class=\"tg-yw4l\">-</td>");//return sales
                                out.print("<td class=\"tg-yw4l\">-</td>");//production in
                                out.print("<td class=\"tg-yw4l\">-</td>");//total in
                                out.print("<td class=\"tg-yw4l\">-</td>");//transfer
                                out.print("<td class=\"tg-yw4l\">-</td>");//return supplier
                                out.print("<td class=\"tg-yw4l\">-</td>");//sales
                                out.print("<td class=\"tg-yw4l\">-</td>");//cost
                                out.print("<td class=\"tg-yw4l\">-</td>");//production out
                                out.print("<td class=\"tg-yw4l\">-</td>");//total out
                                out.print("<td class=\"tg-yw4l\">-</td>");//subtotal
                                out.print("<td class=\"tg-yw4l\">-</td>");//opname
                                out.print("<td class=\"tg-yw4l\">-</td>");//selisih plus
                                out.print("<td class=\"tg-yw4l\">-</td>");//selisih minus
                                out.print("<td class=\"tg-yw4l\">-</td>");//balance
                                out.print("<td class=\"tg-yw4l\">-</td>");//balance
                            }

                        } else {
                            out.print("<td class=\"tg-yw4l\">-</td>");//receive supp
                            out.print("<td class=\"tg-yw4l\">-</td>");//receive warehouse
                            out.print("<td class=\"tg-yw4l\">-</td>");//return sales
                            out.print("<td class=\"tg-yw4l\">-</td>");//production in
                            out.print("<td class=\"tg-yw4l\">-</td>");//total in
                            out.print("<td class=\"tg-yw4l\">-</td>");//transfer
                            out.print("<td class=\"tg-yw4l\">-</td>");//return supplier
                            out.print("<td class=\"tg-yw4l\">-</td>");//sales
                            out.print("<td class=\"tg-yw4l\">-</td>");//cost
                            out.print("<td class=\"tg-yw4l\">-</td>");//production out
                            out.print("<td class=\"tg-yw4l\">-</td>");//total out
                            out.print("<td class=\"tg-yw4l\">-</td>");//subtotal
                            out.print("<td class=\"tg-yw4l\">-</td>");//opname
                            out.print("<td class=\"tg-yw4l\">-</td>");//selisih plus
                            out.print("<td class=\"tg-yw4l\">-</td>");//selisih minus
                            out.print("<td class=\"tg-yw4l\">-</td>");//balance
                            out.print("<td class=\"tg-yw4l\">-</td>");//balance
                        }
                        out.print("</tr>");
                    }
                } catch (Exception ex) {

                }
            }

            //grand total
            out.print("<tr>");
            out.print("<td class=\"tg-yw4l\"></td>");//no
            out.print("<td class=\"tg-yw4l\"></td>");//sku
            out.print("<td class=\"tg-yw4l\"></td>");//barcode
            out.print("<td class=\"tg-yw4l\"><b>Grand Total</b></td>");//nama
            out.print("<td class=\"tg-yw4l\"></td>");//hpp
            if (srcReport.getvPriceTypeId() != null && srcReport.getvPriceTypeId().size() > 0) {
                for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
                    for (int j = 0; j < listCurrStandardX.size(); j++) {
                        Vector temp = (Vector) listCurrStandardX.get(j);
                        CurrencyType curr = (CurrencyType) temp.get(0);
                        PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
                        out.print("<td class=\"tg-yw4l\"></td>");
                    }
                }
            }
            out.print("<td class=\"tg-yw4l\"></td>");//unit
            out.print("<td class=\"tg-yw4l\"><b>" + saldo + "</b></td>");//saldo
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReceiveSupp + "</b></td>");//receive supp
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReceiveWarehouse + "</b></td>");//receive warehouse
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReturnSales + "</b></td>");//return sales
            out.print("<td class=\"tg-yw4l\"><b>" + qtyProductionIn + "</b></td>");//production in
            out.print("<td class=\"tg-yw4l\"><b>" + qtyTotalIn + "</b></td>");//total in
            out.print("<td class=\"tg-yw4l\"><b>" + qtyTransfer + "</b></td>");//transfer
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReturnSupplier + "</b></td>");//return supplier
            out.print("<td class=\"tg-yw4l\"><b>" + qtySales + "</b></td>");//sales
            out.print("<td class=\"tg-yw4l\"><b>" + qtyCost + "</b></td>");//cost
            out.print("<td class=\"tg-yw4l\"><b>" + qtyProductionOut + "</b></td>");//production out
            out.print("<td class=\"tg-yw4l\"><b>" + qtyTotalOut + "</b></td>");//total out
            out.print("<td class=\"tg-yw4l\"><b>" + qtySubtotal + "</b></td>");//subtotal
            out.print("<td class=\"tg-yw4l\"><b>" + qtyOpname + "</b></td>");//opname
            out.print("<td class=\"tg-yw4l\"><b>" + qtySelisihOpnamePlus + "</b></td>");//selisih plus
            out.print("<td class=\"tg-yw4l\"><b>" + qtySelisihOpnameMinus + "</b></td>");//selisih minus
            out.print("<td class=\"tg-yw4l\"><b>" + qtyBalance + "</b></td>");//selisih minus
            out.print("<td class=\"tg-yw4l\"><b>" + FRMHandler.userFormatStringDecimal(totalNilaiStok) + "</b></td>");//balance
            out.print("</tr>");

        } catch (Exception e) {
            System.out.println("Exc on SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return;
        }

    }

    /**
     * salah fungsi
     *
     * @param out
     * @param srcReportPotitionStock
     */
    public static void getReportStockAllxxxx(JspWriter out, SrcReportPotitionStock srcReportPotitionStock) {
        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);
        DBResultSet dbrs = null;
        SrcReportPotitionStock srcReport = (SrcReportPotitionStock) srcReportPotitionStock;
        try {
            String sql = "" + queryMaterial(srcReport);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int loop = 0;
            Hashtable hashReportRekap = getHastableReportStockAll(srcReport);

            //cari stok dari awal periode
            Vector vect = PstPeriode.list(0, 0, "", PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]);
            Date startDateFromSaldoAwal = (Date) srcReport.getDateFrom().clone();
            if (vect != null && vect.size() > 0) {
                Periode periode = (Periode) vect.get(0);
                srcReport.setDateFrom(periode.getStartDate());
            }
            Date startMTD = (Date) startDateFromSaldoAwal.clone();
            startMTD.setDate(startDateFromSaldoAwal.getDate() - 1);
            srcReport.setDateTo(startMTD);
            Hashtable hashReportRekapAwalPeriode = getHastableReportStockAll(srcReport);
            double saldo = 0;
            double qtyReceiveSupp = 0;
            double qtyReceiveWarehouse = 0;
            double qtyReturnSales = 0;
            double qtyTotalIn = 0;
            double qtyTransfer = 0;
            double qtyReturnSupplier = 0;
            double qtySales = 0;
            double qtyCost = 0;
            double qtyTotalOut = 0;
            double qtySubtotal = 0;
            double qtyOpname = 0;
            double qtySelisihOpnameMinus = 0;
            double qtySelisihOpnamePlus = 0;
            double qtyBalance = 0;
            double totalNilaiStok = 0;

            int typeDeadStok = srcReportPotitionStock.getViedDeadStok();

            while (rs.next()) {
                loop = loop + 1;
                try {
                    boolean loopNext = true;
                    if (typeDeadStok == VIEW_DEAD_STOK_NO_MOVEMENT) {
                        if (hashReportRekap != null && hashReportRekap.size() > 0) {
                            try {
                                HistoryPosisiStok historyPosisiStokxx = (HistoryPosisiStok) hashReportRekap.get("" + rs.getString("MATERIAL_ID"));
                                if (historyPosisiStokxx != null) {
                                    loopNext = false;
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }

                    if (loopNext) {
                        out.print("<tr>");
                        out.print("<td class=\"tg-yw4l\">" + loop + "</td>");//no
                        out.print("<td class=\"tg-yw4l\">" + rs.getString("SKU") + "</td>");//sku
                        out.print("<td class=\"tg-yw4l\">" + rs.getString("BARCODE") + "</td>");//barcode
                        //out.print("<td class=\"tg-yw4l\">"+rs.getString("NAME")+"</td>");//nama
                        out.print("<td class=\"tg-yw4l\"><a href=\"javascript:cmdViewKartuStock('" + rs.getLong("MATERIAL_ID") + "','" + rs.getString("SKU") + "','" + rs.getString("NAME") + "')\">" + rs.getString("NAME") + "</a></td>");

                        out.print("<td class=\"tg-yw4l\">" + FRMHandler.userFormatStringDecimal(rs.getDouble("AVERAGE_PRICE")) + "</td>");//hpp

                        if (srcReport.getvPriceTypeId() != null && srcReport.getvPriceTypeId().size() > 0) {
                            SrcMaterial srcMaterial = new SrcMaterial();
                            Material material = new Material();
                            material.setOID(rs.getLong("MATERIAL_ID"));
                            Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, material, rs.getLong("MATERIAL_ID"));
                            for (int i = 0; i < srcReport.getvPriceTypeId().size(); i++) {
                                PriceType pricetype = (PriceType) srcReport.getvPriceTypeId().get(i);
                                for (int j = 0; j < listCurrStandardX.size(); j++) {
                                    Vector tempStand = (Vector) listCurrStandardX.get(j);
                                    CurrencyType currx = (CurrencyType) tempStand.get(0);
                                    StandartRate standart = (StandartRate) tempStand.get(1);

                                    PriceTypeMapping pTypeMapping = null;
                                    if (memberPrice != null && !memberPrice.isEmpty()) {

                                        pTypeMapping = (PriceTypeMapping) memberPrice.get("" + pricetype.getOID() + "_" + standart.getOID());
                                    }

                                    if (pTypeMapping == null) {
                                        pTypeMapping = new PriceTypeMapping();
                                        pTypeMapping.setMaterialId(material.getOID());
                                        pTypeMapping.setPriceTypeId(pricetype.getOID());
                                        pTypeMapping.setStandartRateId(currx.getOID());
                                    }
                                    //out.print("<td class=\"" + cellStyle + "\" div align=\"center\">"+FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice())+"</div>");
                                    out.print("<td class=\"tg-yw4l\">" + FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()) + "</td>");//unit
                                }
                            }
                        }

                        out.print("<td class=\"tg-yw4l\">" + rs.getString("UNIT") + "</td>");//unit
                        double saldoAwal = 0;
                        if (hashReportRekapAwalPeriode != null && hashReportRekapAwalPeriode.size() > 0) {
                            HistoryPosisiStok historyPosisiStok = new HistoryPosisiStok();
                            try {
                                historyPosisiStok = (HistoryPosisiStok) hashReportRekapAwalPeriode.get("" + rs.getString("MATERIAL_ID"));
                                out.print("<td class=\"tg-yw4l\">"
                                        + (historyPosisiStok.getTotalInCalculation()
                                        - historyPosisiStok.getTotalOutCalculation()
                                        + historyPosisiStok.getSelisihOpnameMinus()
                                        + historyPosisiStok.getSelisihOpnamePlus())
                                        + "</td>");//saldo
                                saldoAwal = (historyPosisiStok.getTotalInCalculation() - historyPosisiStok.getTotalOutCalculation()
                                        + historyPosisiStok.getSelisihOpnameMinus() + historyPosisiStok.getSelisihOpnamePlus());
                                saldo = saldo + saldoAwal;
                            } catch (Exception ex) {
                                out.print("<td class=\"tg-yw4l\"></td>");//saldo
                                saldoAwal = 0;
                            }
                        } else {
                            out.print("<td class=\"tg-yw4l\">-</td>");//saldo
                        }

                        //munculkan hastable
                        if (hashReportRekap != null && hashReportRekap.size() > 0) {
                            HistoryPosisiStok historyPosisiStok = new HistoryPosisiStok();
                            try {
                                historyPosisiStok = (HistoryPosisiStok) hashReportRekap.get("" + rs.getString("MATERIAL_ID"));

                                qtyReceiveSupp = qtyReceiveSupp + historyPosisiStok.getReceiveSupplier();
                                qtyReceiveWarehouse = qtyReceiveWarehouse + historyPosisiStok.getReceiveWarehouse();
                                qtyReturnSales = qtyReturnSales + historyPosisiStok.getReturnSales();
                                qtyTotalIn = qtyTotalIn + historyPosisiStok.getTotalIn();
                                qtyTransfer = qtyTransfer + historyPosisiStok.getTransfer();
                                qtyReturnSupplier = qtyReturnSupplier + historyPosisiStok.getReturnSupplier();
                                qtySales = qtySales + historyPosisiStok.getSales();
                                qtyCost = qtyCost + historyPosisiStok.getCost();
                                qtySubtotal = qtySubtotal + historyPosisiStok.getSubTotal();
                                qtyOpname = qtyOpname + historyPosisiStok.getOpname();
                                qtySelisihOpnameMinus = qtySelisihOpnameMinus + historyPosisiStok.getSelisihOpnameMinus();
                                qtySelisihOpnamePlus = qtySelisihOpnamePlus + historyPosisiStok.getSelisihOpnamePlus();
                                qtyTotalOut = qtyTotalOut + historyPosisiStok.getTotalOut();

                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReceiveSupplier() + "</td>");//receive supp
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReceiveWarehouse() + "</td>");//receive warehouse
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReturnSales() + "</td>");//return sales
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getTotalIn() + "</td>");//total in
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getTransfer() + "</td>");//transfer
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getReturnSupplier() + "</td>");//return supplier
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSales() + "</td>");//sales
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getCost() + "</td>");//cost
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getTotalOut() + "</td>");//total out
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSubTotal() + "</td>");//subtotal
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getOpname() + "</td>");//opname
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSelisihOpnamePlus() + "</td>");//selisih plus
                                out.print("<td class=\"tg-yw4l\">" + historyPosisiStok.getSelisihOpnameMinus() + "</td>");//selisih minus
                                double totalStok = (saldoAwal + historyPosisiStok.getTotalInCalculation()
                                        - historyPosisiStok.getTotalOutCalculation()
                                        + historyPosisiStok.getSelisihOpnameMinus()
                                        + historyPosisiStok.getSelisihOpnamePlus());
                                out.print("<td class=\"tg-yw4l\">" + totalStok + "</td>");//balance
                                out.print("<td class=\"tg-yw4l\">" + FRMHandler.userFormatStringDecimal(totalStok * rs.getDouble("AVERAGE_PRICE")) + "</td>");//balance

                                qtyBalance = qtyBalance + totalStok;
                                totalNilaiStok = totalNilaiStok + (totalStok * rs.getDouble("AVERAGE_PRICE"));
                            } catch (Exception ex) {
                                out.print("<td class=\"tg-yw4l\">-</td>");//receive supp
                                out.print("<td class=\"tg-yw4l\">-</td>");//receive warehouse
                                out.print("<td class=\"tg-yw4l\">-</td>");//return sales
                                out.print("<td class=\"tg-yw4l\">-</td>");//total in
                                out.print("<td class=\"tg-yw4l\">-</td>");//transfer
                                out.print("<td class=\"tg-yw4l\">-</td>");//return supplier
                                out.print("<td class=\"tg-yw4l\">-</td>");//sales
                                out.print("<td class=\"tg-yw4l\">-</td>");//cost
                                out.print("<td class=\"tg-yw4l\">-</td>");//total out
                                out.print("<td class=\"tg-yw4l\">-</td>");//subtotal
                                out.print("<td class=\"tg-yw4l\">-</td>");//opname
                                out.print("<td class=\"tg-yw4l\">-</td>");//selisih plus
                                out.print("<td class=\"tg-yw4l\">-</td>");//selisih minus
                                out.print("<td class=\"tg-yw4l\">-</td>");//balance
                                out.print("<td class=\"tg-yw4l\">-</td>");//balance
                            }

                        } else {
                            out.print("<td class=\"tg-yw4l\">-</td>");//receive supp
                            out.print("<td class=\"tg-yw4l\">-</td>");//receive warehouse
                            out.print("<td class=\"tg-yw4l\">-</td>");//return sales
                            out.print("<td class=\"tg-yw4l\">-</td>");//total in
                            out.print("<td class=\"tg-yw4l\">-</td>");//transfer
                            out.print("<td class=\"tg-yw4l\">-</td>");//return supplier
                            out.print("<td class=\"tg-yw4l\">-</td>");//sales
                            out.print("<td class=\"tg-yw4l\">-</td>");//cost
                            out.print("<td class=\"tg-yw4l\">-</td>");//total out
                            out.print("<td class=\"tg-yw4l\">-</td>");//subtotal
                            out.print("<td class=\"tg-yw4l\">-</td>");//opname
                            out.print("<td class=\"tg-yw4l\">-</td>");//selisih plus
                            out.print("<td class=\"tg-yw4l\">-</td>");//selisih minus
                            out.print("<td class=\"tg-yw4l\">-</td>");//balance
                            out.print("<td class=\"tg-yw4l\">-</td>");//balance
                        }
                        out.print("</tr>");
                    }
                } catch (Exception ex) {

                }
            }

            //grand total
            out.print("<tr>");
            out.print("<td class=\"tg-yw4l\"></td>");//no
            out.print("<td class=\"tg-yw4l\"></td>");//sku
            out.print("<td class=\"tg-yw4l\"></td>");//barcode
            out.print("<td class=\"tg-yw4l\"></td>");//nama
            out.print("<td class=\"tg-yw4l\"></td>");//hpp
            if (srcReport.getvPriceTypeId() != null && srcReport.getvPriceTypeId().size() > 0) {
                for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
                    for (int j = 0; j < listCurrStandardX.size(); j++) {
                        Vector temp = (Vector) listCurrStandardX.get(j);
                        CurrencyType curr = (CurrencyType) temp.get(0);
                        PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
                        out.print("<td class=\"tg-yw4l\"></td>");
                    }
                }
            }
            out.print("<td class=\"tg-yw4l\"><b>Grand Total</b></td>");//unit
            out.print("<td class=\"tg-yw4l\"><b>" + saldo + "</b></td>");//saldo
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReceiveSupp + "</b></td>");//receive supp
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReceiveWarehouse + "</b></td>");//receive warehouse
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReturnSales + "</b></td>");//return sales
            out.print("<td class=\"tg-yw4l\"><b>" + qtyTotalIn + "</b></td>");//total in
            out.print("<td class=\"tg-yw4l\"><b>" + qtyTransfer + "</b></td>");//transfer
            out.print("<td class=\"tg-yw4l\"><b>" + qtyReturnSupplier + "</b></td>");//return supplier
            out.print("<td class=\"tg-yw4l\"><b>" + qtySales + "</b></td>");//sales
            out.print("<td class=\"tg-yw4l\"><b>" + qtyCost + "</b></td>");//cost
            out.print("<td class=\"tg-yw4l\"><b>" + qtyTotalOut + "</b></td>");//total out
            out.print("<td class=\"tg-yw4l\"><b>" + qtySubtotal + "</b></td>");//subtotal
            out.print("<td class=\"tg-yw4l\"><b>" + qtyOpname + "</b></td>");//opname
            out.print("<td class=\"tg-yw4l\"><b>" + qtySelisihOpnamePlus + "</b></td>");//selisih plus
            out.print("<td class=\"tg-yw4l\"><b>" + qtySelisihOpnameMinus + "</b></td>");//selisih minus
            out.print("<td class=\"tg-yw4l\"><b>" + qtyBalance + "</b></td>");//selisih minus
            out.print("<td class=\"tg-yw4l\"><b>" + FRMHandler.userFormatStringDecimal(totalNilaiStok) + "</b></td>");//balance
            out.print("</tr>");

        } catch (Exception e) {
            System.out.println("Exc on SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return;
        }

    }

    public static Hashtable getHastableReportStockAll(SrcReportPotitionStock srcReportPotitionStock) {
        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);
        DBResultSet dbrs = null;
        Hashtable hashPosisiStok = new Hashtable();
        try {
            String sql = "" + queryPosisiStok(srcReportPotitionStock);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int loop = 0;
            while (rs.next()) {
                try {

                    HistoryPosisiStok historyPosisiStok = new HistoryPosisiStok();
                    historyPosisiStok.setMaterialId(rs.getLong("MATERIAL_ID"));
                    historyPosisiStok.setSku(rs.getString("tab.SKU"));

                    historyPosisiStok.setBarcode(rs.getString("BARCODE"));//barcode
                    historyPosisiStok.setNamaItem(rs.getString("tab.NAME"));//nama
                    historyPosisiStok.setHpp(rs.getDouble("AVERAGE_PRICE"));//hpp
                    if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
                        for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
                            for (int j = 0; j < listCurrStandardX.size(); j++) {
                                Vector temp = (Vector) listCurrStandardX.get(j);
                                CurrencyType curr = (CurrencyType) temp.get(0);
                                PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
                            }
                        }
                    }
                    historyPosisiStok.setUnit(rs.getString("UNIT"));//unit
                    historyPosisiStok.setSaldoAwal(0);//saldo
                    historyPosisiStok.setReceiveSupplier(rs.getDouble("QTY_RECEIVE_SUPP"));//receive supp
                    historyPosisiStok.setReceiveWarehouse(rs.getDouble("QTY_RECEIVE_WAREHOUSE"));//receive warehouse
                    historyPosisiStok.setReturnSales(rs.getDouble("QTY_SALES_RETURN"));//return sales
                    historyPosisiStok.setTotalIn(rs.getDouble("TOTAL_IN"));//total in
                    historyPosisiStok.setTransfer(rs.getDouble("QTY_TRANSFER"));//transfer
                    historyPosisiStok.setReturnSupplier(rs.getDouble("QTY_RETURN_SUPPLIER"));//return supplier
                    historyPosisiStok.setSales(rs.getDouble("QTY_SALES"));//sales
                    historyPosisiStok.setCost(rs.getDouble("QTY_COSTING"));//cost
                    historyPosisiStok.setTotalOut(rs.getDouble("TOTAL_OUT"));//total out
                    historyPosisiStok.setSubTotal(rs.getDouble("SUBTOTAL"));//subtotal
                    historyPosisiStok.setOpname(rs.getDouble("QTY_OPNAME"));//opname
                    if (rs.getDouble("QTY_OPNAME_SELISIH") >= 0) {
                        historyPosisiStok.setSelisihOpnamePlus(rs.getDouble("QTY_OPNAME_SELISIH"));//selisih plus
                        historyPosisiStok.setSelisihOpnameMinus(0);
                    } else {

                        historyPosisiStok.setSelisihOpnameMinus(rs.getDouble("QTY_OPNAME_SELISIH"));//selisih plus
                        historyPosisiStok.setSelisihOpnamePlus(0);
                    }
                    hashPosisiStok.put("" + rs.getLong("MATERIAL_ID"), historyPosisiStok);
                } catch (Exception ex) {

                }

            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Exc on SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashPosisiStok;
    }

    public static String queryPosisiStok(SrcReportPotitionStock srcReportPotitionStock) {
        String sql = "";
        /*cara dua*/
        /*receive, kurang receive dari supplier atau receive dari warehouse, kurang opname dan selisih opname*/
        sql = "SELECT "
                + "MATERIAL_ID, "
                + "tab.NAME, "
                + "tab.SKU, "
                + "BARCODE, "
                + "pu.NAME AS UNIT, "
                + "pc.NAME AS CATEGORY, "
                + "prk.NAME AS MERK, "
                + "AVERAGE_PRICE, "
                + "pk.CODE AS RAK, "
                + "SUM(QTY_RECEIVE) AS QTY_RECEIVE_SUPP,  "
                + "SUM(QTY_RECEIVE_WAREHOUSE) AS QTY_RECEIVE_WAREHOUSE,  "
                + "SUM(QTY_TRANSFER) AS QTY_TRANSFER,  "
                + "SUM(QTY_RETURN_SUPPLIER) AS QTY_RETURN_SUPPLIER,  "
                + "SUM(QTY_SALES) AS QTY_SALES, "
                + "SUM(QTY_SALES_RETURN) AS QTY_SALES_RETURN, "
                + "SUM(QTY_COSTING) AS QTY_COSTING, "
                + "SUM(QTY_OPNAME) AS QTY_OPNAME, "
                + "SUM(QTY_OPNAME_SELISIH) AS QTY_OPNAME_SELISIH, "
                + "SUM(QTY_PRODUCTION_OUT) AS QTY_PRODUCTION_OUT, "
                + "SUM(QTY_PRODUCTION_IN) AS QTY_PRODUCTION_IN, "
                + "SUM(QTY_RECEIVE+QTY_RECEIVE_WAREHOUSE+QTY_SALES_RETURN+QTY_PRODUCTION_IN) AS TOTAL_IN, "
                + "SUM(QTY_TRANSFER+QTY_RETURN_SUPPLIER+QTY_SALES+QTY_COSTING+QTY_PRODUCTION_OUT) AS TOTAL_OUT, "
                + "SUM((QTY_RECEIVE+QTY_RECEIVE_WAREHOUSE+QTY_SALES_RETURN+QTY_PRODUCTION_IN)-(QTY_TRANSFER+QTY_RETURN_SUPPLIER+QTY_SALES+QTY_COSTING+QTY_PRODUCTION_OUT)) AS SUBTOTAL  "
                + "FROM  "
                + "( "
                + "SELECT  "
                + //"SUM(RI.QTY) AS QTY_RECEIVE,  "+
                "IF(R.REC_CODE IS NULL, 0,SUM(RI.QTY)) AS QTY_RECEIVE, "
                + "0 AS QTY_RECEIVE_WAREHOUSE,  "
                + "0 AS QTY_TRANSFER,  "
                + "0 AS QTY_RETURN_SUPPLIER,  "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM "
                + " pos_material AS M  "
                + " INNER JOIN pos_receive_material_item AS RI  ON RI.MATERIAL_ID = M.MATERIAL_ID   "
                + " INNER JOIN pos_receive_material AS R ON R.RECEIVE_MATERIAL_ID = RI.RECEIVE_MATERIAL_ID "
                + " AND R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";

        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.RECEIVE_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.RECEIVE_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }

        sql = sql + " AND ((R.RECEIVE_STATUS = 5) OR (R.RECEIVE_STATUS = 7)) "
                + " AND (R.RECEIVE_SOURCE = 0 OR R.RECEIVE_SOURCE=1) ";

        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }

        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*peneriman warehouse*/
                + "UNION "
                + "SELECT  "
                + "0 AS QTY_RECEIVE, "
                + //"SUM(RI.QTY) AS QTY_RECEIVE_WAREHOUSE, "+
                "IF(R.REC_CODE IS NULL, 0,SUM(RI.QTY)) AS QTY_RECEIVE_WAREHOUSE, "
                + "0 AS QTY_TRANSFER, "
                + "0 AS QTY_RETURN_SUPPLIER, "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM "
                + " pos_material AS M "
                + " INNER JOIN pos_receive_material_item AS RI  ON RI.MATERIAL_ID = M.MATERIAL_ID  "
                + " INNER JOIN pos_receive_material AS R  ON R.RECEIVE_MATERIAL_ID = RI.RECEIVE_MATERIAL_ID AND R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";

        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.RECEIVE_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.RECEIVE_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }

        sql = sql + "AND ((R.RECEIVE_STATUS = 5) OR (R.RECEIVE_STATUS = 7)) "
                + " AND R.RECEIVE_SOURCE=2 ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;

        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*transfer*/
                + "UNION  "
                + "SELECT "
                + "0 AS QTY_RECEIVE,  "
                + "0 AS QTY_RECEIVE_WAREHOUSE,  "
                + //"SUM(RI.QTY) AS QTY_TRANSFER,  "+
                "IF(R.DISPATCH_CODE IS NULL, 0,SUM(RI.QTY)) AS QTY_TRANSFER, "
                + "0 AS QTY_RETURN_SUPPLIER,  "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_material AS M   "
                + " INNER JOIN pos_dispatch_material_item AS RI ON RI.MATERIAL_ID = M.MATERIAL_ID  "
                + " INNER JOIN pos_dispatch_material AS R  ON  R.DISPATCH_MATERIAL_ID = RI.DISPATCH_MATERIAL_ID AND R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";

        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.DISPATCH_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.DISPATCH_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }

        sql = sql + " AND ((R.DISPATCH_STATUS = 5) OR (R.DISPATCH_STATUS = 7)) ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*return supplier*/
                + "UNION  "
                + "SELECT  "
                + "0 AS QTY_RECEIVE,  "
                + "0 AS QTY_RECEIVE_WAREHOUSE,  "
                + "0 AS QTY_TRANSFER,  "
                + //"SUM(RI.QTY) AS QTY_RETURN_SUPPLIER,  "+
                "IF(R.RETURN_CODE IS NULL, 0,SUM(RI.QTY)) AS QTY_RETURN_SUPPLIER, "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_material AS M "
                + " INNER JOIN pos_return_material_item AS RI ON RI.MATERIAL_ID = M.MATERIAL_ID  "
                + " INNER JOIN pos_return_material AS R  ON R.RETURN_MATERIAL_ID = RI.RETURN_MATERIAL_ID AND R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";

        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.RETURN_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.RETURN_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }

        sql = sql + " AND ((R.RETURN_STATUS = 5) OR (R.RETURN_STATUS = 7)) ";

        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*sales*/
                + "UNION  "
                + "SELECT "
                + "0 AS QTY_RECEIVE,  "
                + "0 AS QTY_RECEIVE_WAREHOUSE,  "
                + "0 AS QTY_TRANSFER,  "
                + "0 AS QTY_RETURN_SUPPLIER,  "
                + //"SUM(RI.QTY) AS QTY_SALES, "+
                "IF(R.BILL_NUMBER IS NULL, 0,SUM(RI.QTY)) AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_material AS M "
                + " INNER JOIN cash_bill_detail AS RI ON RI.MATERIAL_ID = M.MATERIAL_ID "
                + " INNER JOIN cash_bill_main AS R  ON R.CASH_BILL_MAIN_ID = RI.CASH_BILL_MAIN_ID  AND R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";
        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.BILL_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.BILL_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }
        sql = sql + " AND ((R.BILL_STATUS = 5) OR (R.BILL_STATUS = 7))  "
                + " AND ((R.DOC_TYPE = \"0\" AND   R.TRANSACTION_TYPE = \"0\"  AND R.TRANSACTION_STATUS = \"0\") OR  (R.DOC_TYPE = \"0\" AND R.TRANSACTION_TYPE = \"1\"  AND   ( R.TRANSACTION_STATUS = \"1\" OR     R.TRANSACTION_STATUS = \"0\") ) OR  (R.DOC_TYPE = \"1\" AND   R.TRANSACTION_TYPE = \"0\"  AND   R.TRANSACTION_STATUS = \"0\") OR  (R.DOC_TYPE = \"1\" AND   R.TRANSACTION_TYPE = \"1\"  AND   R.TRANSACTION_STATUS = \"0\")) ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*return sales*/
                + "UNION  "
                + "SELECT   "
                + "0 AS QTY_RECEIVE,  "
                + "0 AS QTY_RECEIVE_WAREHOUSE, "
                + "0 AS QTY_TRANSFER, "
                + "0 AS QTY_RETURN_SUPPLIER, "
                + "0 AS QTY_SALES, "
                + //"SUM(RI.QTY) AS QTY_SALES_RETURN, "+
                "IF(R.BILL_NUMBER IS NULL, 0,SUM(RI.QTY)) AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_material AS M   "
                + " INNER JOIN cash_bill_detail AS RI  ON RI.MATERIAL_ID = M.MATERIAL_ID   "
                + " INNER JOIN cash_bill_main AS R ON R.CASH_BILL_MAIN_ID = RI.CASH_BILL_MAIN_ID AND R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";
        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.BILL_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.BILL_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }
        sql = sql + " AND ((R.BILL_STATUS = 5) OR (R.BILL_STATUS = 7))  "
                + " AND ((R.DOC_TYPE = \"1\" AND   R.TRANSACTION_TYPE = \"1\"  AND   R.TRANSACTION_STATUS = \"0\") OR (R.DOC_TYPE = \"1\" AND   R.TRANSACTION_TYPE = \"0\"  AND   R.TRANSACTION_STATUS = \"0\")) ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*costing*/
                + "UNION  "
                + "SELECT "
                + "0 AS QTY_RECEIVE, "
                + "0 AS QTY_RECEIVE_WAREHOUSE, "
                + "0 AS QTY_TRANSFER, "
                + "0 AS QTY_RETURN_SUPPLIER, "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + //"SUM(RI.QTY) AS QTY_COSTING, "+
                "IF(R.COSTING_CODE IS NULL, 0,SUM(RI.QTY)) AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_material AS M "
                + " INNER JOIN pos_costing_material_item AS RI  ON RI.MATERIAL_ID = M.MATERIAL_ID "
                + " INNER JOIN pos_costing_material AS R ON  R.COSTING_MATERIAL_ID = RI.COSTING_MATERIAL_ID  AND  R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";
        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.COSTING_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.COSTING_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }
        sql = sql + " AND ((R.COSTING_STATUS = 5) OR (R.COSTING_STATUS = 7)) ";

        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*opname*/
                + "UNION "
                + "SELECT  "
                + "0 AS QTY_RECEIVE, "
                + "0 AS QTY_RECEIVE_WAREHOUSE, "
                + "0 AS QTY_TRANSFER, "
                + "0 AS QTY_RETURN_SUPPLIER, "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + //"SUM(RI.QTY_OPNAME) AS QTY_OPNAME, "+
                "IF(R.STOCK_OPNAME_NUMBER IS NULL, 0,SUM(RI.QTY_OPNAME)) AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_material AS M  "
                + " INNER JOIN pos_stock_opname_item AS RI ON RI.MATERIAL_ID = M.MATERIAL_ID  "
                + " INNER JOIN  pos_stock_opname AS R ON  R.STOCK_OPNAME_ID = RI.STOCK_OPNAME_ID AND  R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";
        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.STOCK_OPNAME_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " R.STOCK_OPNAME_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }
        sql = sql + " AND ((R.STOCK_OPNAME_STATUS = 5) OR (R.STOCK_OPNAME_STATUS = 7)) ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*selisih opname*/
                + "UNION "
                + "SELECT  "
                + "0 AS QTY_RECEIVE,  "
                + "0 AS QTY_RECEIVE_WAREHOUSE,  "
                + "0 AS QTY_TRANSFER,  "
                + "0 AS QTY_RETURN_SUPPLIER,  "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + //"SUM(RI.QTY_OPNAME - RI.QTY_SYSTEM) AS QTY_OPNAME_SELISIH, "+      
                "IF(R.STOCK_OPNAME_NUMBER IS NULL, 0,SUM(RI.QTY_OPNAME - RI.QTY_SYSTEM)) AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "RI.UNIT_ID , "
                + "RI.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_stock_opname AS R  "
                + " INNER JOIN pos_stock_opname_item AS RI  ON R.STOCK_OPNAME_ID = RI.STOCK_OPNAME_ID "
                + " INNER JOIN pos_material AS M ON RI.MATERIAL_ID = M.MATERIAL_ID AND R.LOCATION_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";

        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " R.STOCK_OPNAME_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + "R.STOCK_OPNAME_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }

        sql = sql + " AND ((R.STOCK_OPNAME_STATUS = 5) OR (R.STOCK_OPNAME_STATUS = 7)) ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;

        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY RI.MATERIAL_ID "
                /*production out*/
                + "UNION "
                + "SELECT  "
                + "0 AS QTY_RECEIVE,  "
                + "0 AS QTY_RECEIVE_WAREHOUSE,  "
                + "0 AS QTY_TRANSFER,  "
                + "0 AS QTY_RETURN_SUPPLIER,  "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "IF(P.PRODUCTION_NUMBER IS NULL,0,SUM(PC.STOCK_QTY)) AS QTY_PRODUCTION_OUT, "
                + "0 AS QTY_PRODUCTION_IN, "
                + "M.BUY_UNIT_ID , "
                + "PC.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_production AS P  "
                + " INNER JOIN pos_production_group AS PG  ON PG.PRODUCTION_ID = P.PRODUCTION_ID "
                + " INNER JOIN pos_production_cost AS PC ON PC.PRODUCTION_GROUP_ID = PG.PRODUCTION_GROUP_ID  "
                + " INNER JOIN pos_material AS M ON PC.MATERIAL_ID = M.MATERIAL_ID "
                + " AND P.LOCATION_FROM_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";

        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " P.PRODUCTION_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " P.PRODUCTION_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }

        sql = sql + " AND ((P.PRODUCTION_STATUS = 5) OR (P.PRODUCTION_STATUS = 7)) ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;

        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY PC.MATERIAL_ID "
                /*production in*/
                + "UNION "
                + "SELECT  "
                + "0 AS QTY_RECEIVE,  "
                + "0 AS QTY_RECEIVE_WAREHOUSE,  "
                + "0 AS QTY_TRANSFER,  "
                + "0 AS QTY_RETURN_SUPPLIER,  "
                + "0 AS QTY_SALES, "
                + "0 AS QTY_SALES_RETURN, "
                + "0 AS QTY_COSTING, "
                + "0 AS QTY_OPNAME, "
                + "0 AS QTY_OPNAME_SELISIH, "
                + "0 AS QTY_PRODUCTION_OUT, "
                + "IF(P.PRODUCTION_NUMBER IS NULL,0,SUM(PP.STOCK_QTY)) AS QTY_PRODUCTION_IN, "
                + "M.BUY_UNIT_ID , "
                + "PP.MATERIAL_ID, "
                + "M.CATEGORY_ID, "
                + "M.BARCODE, "
                + "M.MERK_ID, "
                + "M.AVERAGE_PRICE, "
                + "M.GONDOLA_CODE, "
                + "M.NAME, "
                + "M.SKU "
                + "FROM pos_production AS P  "
                + " INNER JOIN pos_production_group AS PG  ON PG.PRODUCTION_ID = P.PRODUCTION_ID "
                + " INNER JOIN pos_production_product AS PP ON PP.PRODUCTION_GROUP_ID = PG.PRODUCTION_GROUP_ID  "
                + " INNER JOIN pos_material AS M ON PP.MATERIAL_ID = M.MATERIAL_ID "
                + " AND P.LOCATION_TO_ID='" + srcReportPotitionStock.getLocationId() + "' AND ";

        if ((srcReportPotitionStock.getDateTo() == null)) {
            sql = sql + " P.PRODUCTION_DATE <= '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "'";
        } else {
            sql = sql + " P.PRODUCTION_DATE BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd") + " 00:00:01' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd") + " 23:59:59' ";
        }

        sql = sql + " AND ((P.PRODUCTION_STATUS = 5) OR (P.PRODUCTION_STATUS = 7)) ";
        sql = sql + " WHERE 1=1 AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != " + PstMaterial.EDIT_NON_AKTIVE;

        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        sql = sql + " GROUP BY PP.MATERIAL_ID "
                + ")"
                + "AS tab INNER JOIN pos_category AS pc "
                + "ON pc.CATEGORY_ID=tab.CATEGORY_ID "
                + "INNER JOIN pos_merk AS prk "
                + "ON prk.MERK_ID=tab.MERK_ID "
                + "INNER JOIN pos_unit AS pu "
                + "ON pu.UNIT_ID=tab.UNIT_ID "
                + "LEFT JOIN pos_ksg AS pk "
                + "ON pk.KSG_ID=tab.GONDOLA_CODE "
                + "GROUP BY tab.MATERIAL_ID";

        return sql;
    }

    public static String queryMaterial(SrcReportPotitionStock srcReportPotitionStock) {

        String sql = "SELECT"
                + "  M.MATERIAL_ID,"
                + "  M.NAME,"
                + "  M.SKU,"
                + "  M.BARCODE,"
                + "  put.NAME AS UNIT,"
                + "  pck.NAME AS CATEGORY,"
                + "  mk.NAME AS MERK,"
                + "  M.AVERAGE_PRICE,"
                + "  ksg.CODE AS RAK,"
                + "  0 AS QTY_RECEIVE_SUPP,"
                + "  0 AS QTY_RECEIVE_WAREHOUSE,"
                + "  0 AS QTY_TRANSFER,"
                + "  0 AS QTY_RETURN_SUPPLIER,"
                + "  0 AS QTY_SALES,"
                + "  0 AS QTY_SALES_RETURN,"
                + "  0 AS QTY_COSTING,"
                + "  0 AS QTY_OPNAME,"
                + "  0 AS QTY_OPNAME_SELISIH,"
                + "  0 AS TOTAL_IN,"
                + "  0 AS TOTAL_OUT,"
                + "  0 AS SUBTOTAL"
                + "  FROM pos_material AS M"
                + " INNER JOIN pos_category AS pck"
                + "  ON pck.CATEGORY_ID = M.CATEGORY_ID"
                + " INNER JOIN pos_merk AS mk"
                + "  ON mk.MERK_ID = M.MERK_ID"
                + " INNER JOIN pos_unit AS put"
                + "  ON put.UNIT_ID = M.DEFAULT_STOCK_UNIT_ID"
                + " LEFT JOIN pos_ksg AS ksg"
                + "  ON ksg.KSG_ID = M.GONDOLA_CODE"
                + " WHERE 1=1 ";
        if (srcReportPotitionStock.getSupplierId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
        }
        if (srcReportPotitionStock.getCategoryId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        }
        if (srcReportPotitionStock.getMerkId() != 0) {
            sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + srcReportPotitionStock.getMerkId();
        }
        return sql;
    }

    synchronized public static double getPreviousStockForHistoryStockReport(long materialId, long locationId, Date dateFrom) {
        double stokAwal = 0;
        try {
            PstStockCardReport.deleteExc();
            //
            SrcStockCard srcStockCard = new SrcStockCard();
            srcStockCard.setMaterialId(materialId);
            srcStockCard.setLocationId(locationId);
            srcStockCard.setStardDate(dateFrom);
            Vector listDocStatus = new Vector(1, 1);
            listDocStatus.add("5");
            listDocStatus.add("7");
            srcStockCard.setDocStatus(listDocStatus);
            //
            if (srcStockCard.getStardDate() != null) {
                srcStockCard.setEndDate(null);
                // pencarian stock sebelum rentang waktu pencarian
                SessMatReceive.getDataMaterial(srcStockCard);
                SessMatReturn.getDataMaterial(srcStockCard);
                SessMatDispatch.getDataMaterial(srcStockCard);
                SessMatStockOpname.getDataMaterial(srcStockCard);
                SessReportSale.getDataMaterial(srcStockCard);
                SessMatCosting.getDataMaterial(srcStockCard);
                // hitung stok awal
                Vector list = new Vector(1, 1);
                String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                list = PstStockCardReport.list(0, 0, "", order);
                stokAwal = prosesGetPrivousDataStockCard(list);
                PstStockCardReport.deleteExc();
            }
        } catch (Exception e) {
            System.out.println("err>>> createHistoryStockCard : " + e.toString());
        }
        return stokAwal;
    }

    public static double prosesGetPrivousDataStockCard(Vector objectClass) {
        double qtyawal = 0;
        try {
            for (int i = 0; i < objectClass.size(); i++) {
                StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);
                switch (stockCardReport.getDocType()) {
                    case I_DocType.MAT_DOC_TYPE_LMRR:
                        qtyawal = qtyawal + stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_ROMR:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_DF:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_OPN:
                        qtyawal = stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_COS:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_SALE:
                        switch (stockCardReport.getTransaction_type()) {
                            case PstBillMain.TYPE_INVOICE:
                                qtyawal = qtyawal - stockCardReport.getQty();
                                break;
                            case PstBillMain.TYPE_RETUR:
                                qtyawal = qtyawal + stockCardReport.getQty();
                                break;
                            case PstBillMain.TYPE_GIFT:

                                break;
                            case PstBillMain.TYPE_COST:

                                break;
                            case PstBillMain.TYPE_COMPLIMENT:

                                break;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("prosesGetPrivousDataStockCard : " + e.toString());
        }
        return qtyawal;
    }
}
