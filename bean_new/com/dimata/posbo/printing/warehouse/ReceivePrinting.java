/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: May 20, 2004 
 * Time: 9:13:37 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.printing.warehouse;

import com.dimata.printman.DSJ_PrintObj;
import com.dimata.printman.DSJ_PrinterService;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.util.Formater;
import com.dimata.util.NumberSpeller;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.qdep.form.FRMHandler;

import java.util.Vector;
import java.util.StringTokenizer;

public class ReceivePrinting {

    static int line = 0;

    public static DSJ_PrintObj printForm(long oid, Vector addressComp) {
        DSJ_PrintObj obj = new DSJ_PrintObj();
        line = 0;
        try {
            MatReceive matReceive = new MatReceive();
            try {
                matReceive = PstMatReceive.fetchExc(oid);
            } catch (Exception e) {
            }

            // main purchase oder
            PurchaseOrder purchOrder = new PurchaseOrder();
            try {
                purchOrder = PstPurchaseOrder.fetchExc(matReceive.getPurchaseOrderId());
            } catch (Exception e) {
            }

            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            obj.setPrnIndex(1);
            obj.setTopMargin(1);
            obj.setLeftMargin(0);
            obj.setRightMargin(0);
            obj.setObjDescription("=====> PRINT," + matReceive.getRecCode());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE);

            // start creater object value
            line++;
            obj.newColumn(1, "");
            obj.setColumnValue(0, line, addressComp.get(0).toString().toUpperCase(), DSJ_PrintObj.TEXT_LEFT);
            line++;
            obj.setColumnValue(0, line, addressComp.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            int[] intCols = {10, 80};
            obj.newColumn(2, "", intCols);

            Vector vt = (Vector) addressComp.get(2);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            vt = (Vector) addressComp.get(3);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            vt = (Vector) addressComp.get(4);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            vt = (Vector) addressComp.get(5);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            obj.newColumn(1, "");
            line++;
            obj.setColumnValue(0, line, "BUKTI PENERIMAAN BARANG", DSJ_PrintObj.TEXT_CENTER);
            line++;
            obj.setColumnValue(0, line, "", DSJ_PrintObj.TEXT_CENTER);

            int[] intCols1 = {13, 35, 13, 40, 13, 20};

            obj.newColumn(6, "", intCols1);
            line++;
            obj.setColumnValue(0, line, "No.BPB", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + matReceive.getRecCode(), DSJ_PrintObj.TEXT_LEFT);

            ContactList contactList = new ContactList();
            try {
                contactList = PstContactList.fetchExc(matReceive.getSupplierId());
            } catch (Exception e) {
            }

            obj.setColumnValue(2, line, "Supplier", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getCompName(), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(4, line, "No.PO", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(5, line, ": " + purchOrder.getPoCode(), DSJ_PrintObj.TEXT_LEFT);

            line++;
            obj.setColumnValue(0, line, "Tanggal BPB", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + Formater.formatDate(matReceive.getReceiveDate(), "dd/MM/yyyy"), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(2, line, "Contact", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getPersonName(), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(4, line, "Tanggal PO", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(5, line, ": " + Formater.formatDate(purchOrder.getPurchDate(), "dd/MMM/yyyy"), DSJ_PrintObj.TEXT_LEFT);

            line++;
            obj.setColumnValue(0, line, "Terms", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + PstPurchaseOrder.fieldsPaymentType[matReceive.getTermOfPayment()], DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(2, line, "Address", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(4, line, "Nota Sup.", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(5, line, ": " + matReceive.getInvoiceSupplier(), DSJ_PrintObj.TEXT_LEFT);

            line++;
            obj.setColumnValue(0, line, "Days", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + matReceive.getCreditTime(), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(2, line, "Telp/Fax", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(4, line, "Jatuh Tempo", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(5, line, ": " + Formater.formatDate(matReceive.getExpiredDate(), "dd/MMM/yyyy"), DSJ_PrintObj.TEXT_LEFT);

            obj = setInternalDetail(obj, oid);
            obj = setLastHeader(obj, matReceive);

        } catch (Exception exc) {
            System.out.println("Exc printForm : " + exc);
        } 
        return obj; 
    }

    /** gede
     * print untuk document penerimaan tampa po
     * @param oid
     * @param addressComp
     * @return
     */
    public static DSJ_PrintObj printFormOutPo(long oid, Vector addressComp) {
        DSJ_PrintObj obj = new DSJ_PrintObj();
        line = 0;
        try {
            MatReceive matReceive = new MatReceive();
            try {
                matReceive = PstMatReceive.fetchExc(oid);
            } catch (Exception e) {
            }

            // main purchase oder
            PurchaseOrder purchOrder = new PurchaseOrder();
            try {
                purchOrder = PstPurchaseOrder.fetchExc(matReceive.getPurchaseOrderId());
            } catch (Exception e) {
            }

            DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
            obj.setPrnIndex(1);
            obj.setTopMargin(1);
            obj.setLeftMargin(0);
            obj.setRightMargin(0);
            obj.setObjDescription("=====> TANPA PO, PRINT," + matReceive.getRecCode());
            obj.setCpiIndex(DSJ_PrintObj.PRINTER_CONDENSED_MODE);

            // start creater object value
            line++;
            obj.newColumn(1, "");
            obj.setColumnValue(0, line, addressComp.get(0).toString().toUpperCase(), DSJ_PrintObj.TEXT_LEFT);
            line++;
            obj.setColumnValue(0, line, addressComp.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            int[] intCols = {10, 80};
            obj.newColumn(2, "", intCols);

            Vector vt = (Vector) addressComp.get(2);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            vt = (Vector) addressComp.get(3);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            vt = (Vector) addressComp.get(4);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            vt = (Vector) addressComp.get(5);
            line++;
            obj.setColumnValue(0, line, vt.get(0).toString(), DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + vt.get(1).toString(), DSJ_PrintObj.TEXT_LEFT);

            obj.newColumn(1, "");
            line++;
            obj.setColumnValue(0, line, "BUKTI PENERIMAAN BARANG", DSJ_PrintObj.TEXT_CENTER);
            line++;
            obj.setColumnValue(0, line, "", DSJ_PrintObj.TEXT_CENTER);

            int[] intCols1 = {13, 35, 13, 40, 13, 20};

            obj.newColumn(6, "", intCols1);
            line++;
            obj.setColumnValue(0, line, "No.BPB", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + matReceive.getRecCode(), DSJ_PrintObj.TEXT_LEFT);

            ContactList contactList = new ContactList();
            try {
                contactList = PstContactList.fetchExc(matReceive.getSupplierId());
            } catch (Exception e) {
            }

            obj.setColumnValue(2, line, "Supplier", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getCompName(), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(4, line, "Nota Sup.", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(5, line, ": " + matReceive.getInvoiceSupplier(), DSJ_PrintObj.TEXT_LEFT);

            line++;
            obj.setColumnValue(0, line, "Tanggal BPB", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, ": " + Formater.formatDate(matReceive.getReceiveDate(), "dd/MM/yyyy"), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(2, line, "Kontak", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getPersonName(), DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(4, line, "Alasan", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(5, line, ": " + PstMatReceive.strReason[0][matReceive.getReason()], DSJ_PrintObj.TEXT_LEFT);

            line++;
            obj.setColumnValue(0, line, "", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, "", DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(2, line, "Alamat", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getBussAddress(), DSJ_PrintObj.TEXT_LEFT);

            line++;
            obj.setColumnValue(0, line, "", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, "", DSJ_PrintObj.TEXT_LEFT);

            obj.setColumnValue(2, line, "Telp/Fax", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(3, line, ": " + contactList.getTelpNr(), DSJ_PrintObj.TEXT_LEFT);

            obj = setInternalDetail(obj, oid);
            obj = setLastHeader(obj, matReceive);

        } catch (Exception exc) {
            System.out.println("Exc printForm : " + exc);
        }
        return obj;
    }

    /**
     * print header detail dan data detail
     * untuk ke supplier/external
     * @param obj
     * @return
     */
    public static DSJ_PrintObj setInternalDetail(DSJ_PrintObj obj, long oid) {
        try {
            // get purcase order item
            String whereClause = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oid;
            Vector list = PstMatReceiveItem.list(0, 0, whereClause);

            line++;
            obj.setLineRptStr(line, 0, "-", obj.getCharacterSelected());
            int[] intCols = {15, 40, 7, 7, 12, 8, 8, 12, 12, 15};
            obj.newColumn(10, "|", intCols);
            line++;
            obj.setColumnValue(0, line, "SKU", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(1, line, "NAMA BARANG", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(2, line, "QTY", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(3, line, "STN", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(4, line, "HRG.BELI", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(5, line, "DISC.1", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(6, line, "DISC.2", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(7, line, "DISC.NOM", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(8, line, "NET.BELI", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(9, line, "SUB TOTAL", DSJ_PrintObj.TEXT_CENTER);

            line++;
            obj.setLineRptStr(line, 0, "-", obj.getCharacterSelected());

            //int totQty = 0;
            //double total = 0.0;
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector temp = (Vector) list.get(k);
                    MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
                    Material mat = (Material) temp.get(1);
                    Unit unit = (Unit) temp.get(2);
                    //MatCurrency matCurrency = (MatCurrency)temp.get(3);

                    line++;
                    obj.setColumnValue(0, line, mat.getSku(), DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(1, line, mat.getName(), DSJ_PrintObj.TEXT_LEFT);
                    obj.setColumnValue(2, line, "" + recItem.getQty(), DSJ_PrintObj.TEXT_CENTER);
                    obj.setColumnValue(3, line, unit.getCode(), DSJ_PrintObj.TEXT_CENTER);
                    obj.setColumnValue(4, line, FRMHandler.userFormatStringDecimal(recItem.getCost()), DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(5, line, FRMHandler.userFormatStringDecimal(recItem.getDiscount()), DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(6, line, FRMHandler.userFormatStringDecimal(recItem.getDiscount2()), DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(7, line, FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()), DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(8, line, FRMHandler.userFormatStringDecimal(recItem.getCurrBuyingPrice()), DSJ_PrintObj.TEXT_RIGHT);
                    obj.setColumnValue(9, line, FRMHandler.userFormatStringDecimal(recItem.getTotal()), DSJ_PrintObj.TEXT_RIGHT);

                //totQty = totQty + recItem.getQty();
                //total = total + recItem.getTotal();
                }
            }

            line++;
            obj.setLineRptStr(line, 0, "-", obj.getCharacterSelected());

        } catch (Exception e) {
        }
        return obj;
    }

    /**
     * print terakhir header description dan approval
     * @param obj
     * @return
     */
    public static DSJ_PrintObj setLastHeader(DSJ_PrintObj obj, MatReceive matReceive) {
        try {
            NumberSpeller numberSpeller = new NumberSpeller();
            String whereItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matReceive.getOID();
            double total = PstMatReceiveItem.getTotal(whereItem);

            double totDisc = total * matReceive.getDiscount() / 100;
            double ppn = total * matReceive.getTotalPpn() / 100;
            double lastTotal = (total - totDisc) + ppn;

            String numStr = String.valueOf(lastTotal);
            StringTokenizer strToken = new StringTokenizer(numStr, ".");
            String firstStr = strToken.nextToken();
            String lastStr = strToken.nextToken();
            if (lastStr.length() > 2) {
                lastStr = lastStr.substring(0, 2);
            }
            lastTotal = Double.parseDouble(firstStr + "." + lastStr);

            line++;
            int[] intColsx = {106, 15, 15};
            obj.newColumn(3, "", intColsx);
            obj.setColumnValue(0, line, "Terbilang : ( " + numberSpeller.spellNumberToIna(lastTotal) + " )", DSJ_PrintObj.TEXT_LEFT);
            obj.setColumnValue(1, line, "Sub Total :", DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(2, line, FRMHandler.userFormatStringDecimal(total), DSJ_PrintObj.TEXT_RIGHT);

            int[] intColsxx = {53, 53, 15, 15};
            obj.newColumn(4, "", intColsxx);
            line++;
            obj.setColumnValue(0, line, "Purchasing", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(1, line, "Accounting", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(2, line, "Disc. (" + FRMHandler.userFormatStringDecimal(matReceive.getDiscount()) + "%):", DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3, line, FRMHandler.userFormatStringDecimal(totDisc), DSJ_PrintObj.TEXT_RIGHT);

            line++;
            obj.setColumnValue(2, line, "PPn (" + FRMHandler.userFormatStringDecimal(matReceive.getTotalPpn()) + "):", DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3, line, FRMHandler.userFormatStringDecimal(ppn), DSJ_PrintObj.TEXT_RIGHT);

            line++;
            obj.setColumnValue(3, line, "------------------", DSJ_PrintObj.TEXT_RIGHT);

            line++;
            obj.setColumnValue(2, line, "Total :", DSJ_PrintObj.TEXT_RIGHT);
            obj.setColumnValue(3, line, FRMHandler.userFormatStringDecimal(lastTotal), DSJ_PrintObj.TEXT_RIGHT);

            line++;
            obj.setColumnValue(0, line, "", DSJ_PrintObj.TEXT_CENTER);

            line++;
            obj.setColumnValue(0, line, "--------------------", DSJ_PrintObj.TEXT_CENTER);
            obj.setColumnValue(1, line, "--------------------", DSJ_PrintObj.TEXT_CENTER);

            line = line + 4;
            obj.setPageLength(line);
        } catch (Exception e) {
            System.out.println("EXC setLastHeader " + e.toString());
        }
        return obj;
    }
}
