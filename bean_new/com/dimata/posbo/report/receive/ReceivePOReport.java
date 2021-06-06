/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.report.receive;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstReceiveStockCode;
import com.dimata.posbo.entity.warehouse.ReceiveStockCode;
import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Regen
 */
public class ReceivePOReport extends HttpServlet {

    private boolean sessLogin = false;

    public static final String mainTitle[][] = {
        {"Penerimaan Barang"},
        {"Receive Item"}
    };

    public static final String textHeaderMain[][] = {
        {"Nomor", "Supplier", "Lokasi", "Nomor PO", "Tanggal", "Nota Supplier", "Nomor BC", "Jenis Dokumen", "Tanggal BC", "Lokasi Pabean"},
        {"Number", "Supplier", "Location", "PO Number", "Date", "Nota Supplier", "BC Number", "Document Tipe", "BC Date", "Pabean Location"}
    };

    public static final String textHeaderItem[][] = {
        {"No", "SKU", "Nama", "Unit", "Qty", "Keterangan"},
        {"No", "SKU", "Name", "Unit", "Qty", "Remark"}
    };

    public static final String textMessage[][] = {
        {"Mohon diterima dengan baik barang-barang berikut ini: "},
        {"Please accept the following items: "}
    };

    public static final String textApproval[][] = {
        {"Konsumen", "Pengirim", "Koordinator"},
        {"Consumer", "Courier", "Coordinator"}
    };

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public static Vector vect = new Vector();

    public static Color border = new Color(0x00, 0x00, 0x00);
    public static Color bgColor = new Color(220, 220, 220);

    public static Font fontTitle = new Font(Font.STRIKETHRU, 13, Font.BOLD, ReceivePOReport.border);
    public static Font fontTitleUnderline = new Font(Font.STRIKETHRU, 13, Font.BOLD + Font.UNDERLINE, ReceivePOReport.border);
    public static Font fontMainHeader = new Font(Font.STRIKETHRU, 10, Font.BOLD, ReceivePOReport.border);
    public static Font fontHeader = new Font(Font.STRIKETHRU, 10, Font.ITALIC, ReceivePOReport.border);
    public static Font fontHeaderUnderline = new Font(Font.STRIKETHRU, 10, Font.ITALIC + Font.UNDERLINE, ReceivePOReport.border);
    public static Font fontListHeader = new Font(Font.STRIKETHRU, 10, Font.BOLD, ReceivePOReport.border);
    public static Font fontLsContent = new Font(Font.STRIKETHRU, 8);
    public static Font fontLsContentUnderline = new Font(Font.STRIKETHRU, 8, Font.UNDERLINE, ReceivePOReport.border);
    public static Font fontNormal = new Font(Font.STRIKETHRU, 10);
    public static Font fontNormalBold = new Font(Font.STRIKETHRU, 8, Font.BOLD, ReceivePOReport.border);
    public static Font fontNormalHeader = new Font(Font.STRIKETHRU, 10);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Color bgColor = new Color(200, 200, 200);
        Rectangle rectangle = new Rectangle(20, 20, 20, 20);
        rectangle.rotate();
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            //step2.2: creating an instance of the writer
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // step 3.1: adding some metadata to the document
            document.addSubject("This is a subject.");
            document.addSubject("This is a subject two.");

            HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", ReceivePOReport.fontLsContent)), false);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(HeaderFooter.NO_BORDER);
            //document.setHeader(header);
            document.setFooter(footer);

            document.open();

            long oidReceive = 0;
            long oidReceiveItem = 0;
            int SESS_LANGUAGE = 0;
            String approot = "";

            int iCommand = FRMQueryString.requestCommand(request);
            MatReceive receive = new MatReceive();
            MatReceiveItem matItem = new MatReceiveItem();

            SESS_LANGUAGE = FRMQueryString.requestInt(request, "sess_language");
            oidReceive = FRMQueryString.requestLong(request, "hidden_receive_id");
            oidReceiveItem = FRMQueryString.requestLong(request, "hidden_receive_item_id");
            approot = FRMQueryString.requestString(request, "approot");
            int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");

            String signRec1 = PstSystemProperty.getValueByName("SIGN_RECEIVE_1");
            String signRec2 = PstSystemProperty.getValueByName("SIGN_RECEIVE_2");
            String signRec3 = PstSystemProperty.getValueByName("SIGN_RECEIVE_3");
            String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceive;
            Vector listBarang = PstMatReceiveItem.list(0, 0, whereClauseItem);

            PurchaseOrder po = new PurchaseOrder();
            try {
                receive = PstMatReceive.fetchExc(oidReceive);
                po = PstPurchaseOrder.fetchExc(receive.getPurchaseOrderId());
            } catch (Exception xxx) {
            }

            String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
            System.out.println("approot = " + pathImage);
            com.lowagie.text.Image gambar = null;

            try {
                gambar = com.lowagie.text.Image.getInstance(pathImage);
            } catch (Exception ex) {
                System.out.println("gambar >>>>>> = " + gambar.getImageMask());
            }

            document.add(ReceivePOReport.getHeaderImage(SESS_LANGUAGE, gambar));
            document.add(ReceivePOReport.getSubHeader(SESS_LANGUAGE, receive));

            document = ReceivePOReport.getContent(listBarang, document, writer, SESS_LANGUAGE, typePrint, oidReceive);
            document.add(ReceivePOReport.getApproval(SESS_LANGUAGE, receive));

        } catch (Exception e) {
            System.out.println("Err: " + e.getMessage());
        }

        document.close();
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        ServletOutputStream sos = response.getOutputStream();
        baos.writeTo(sos);
        sos.flush();
    }

    private static Table getHeaderImage(int SESS_LANGUAGE, com.lowagie.text.Image gambar)
            throws BadElementException, DocumentException {
        Table table = new Table(1);

        try {
            int ctnInt[] = {100};
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setSpacing(1);
            table.setPadding(0);
            table.setDefaultCellBorder(Table.NO_BORDER);

            createEmptySpace(table, 10);

            //image in header
            gambar.setAlignment(com.lowagie.text.Image.MIDDLE);
            gambar.scaleAbsolute(100, 100);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase(new Chunk(gambar, 0, 0)));

            createEmptySpace(table, 1);

            //sub title
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase(ReceivePOReport.mainTitle[SESS_LANGUAGE][0].toUpperCase(), ReceivePOReport.fontTitleUnderline));

            createEmptySpace(table, 2);

        } catch (Exception e) {
        }
        return table;
    }

    private static void createEmptySpace(Table table, int space) throws BadElementException, DocumentException {
        for (int i = 0; i < space; i++) {
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("", ReceivePOReport.fontLsContent));
        }

    }

    private static Table getSubHeader(int SESS_LANGUAGE, MatReceive receive)
            throws BadElementException, DocumentException {
        Table table = new Table(7);
        int ctnInt[] = {15, 2, 30, 10, 15, 2, 30};
        table.setBorderColor(new Color(255, 255, 255));
        table.setWidth(100);
        table.setWidths(ctnInt);
        table.setSpacing(1);
        table.setPadding(1);

        String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
        table.setDefaultCellBorder(Table.NO_BORDER);
        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

        ContactList cnt = new ContactList();
        PurchaseOrder po = new PurchaseOrder();
        try {
            cnt = PstContactList.fetchExc(receive.getSupplierId());
            po = PstPurchaseOrder.fetchExc(receive.getPurchaseOrderId());
        } catch (Exception e) {
        }

        String cntName = cnt.getCompName();
        if (cntName.length() == 0) {
            cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
        }
        Location loc = new Location();
        Location loca = new Location();
        try {
            loc = PstLocation.fetchExc(receive.getLocationId());
            if (receive.getLocationPabean() != 0) {
                loca = PstLocation.fetchExc(receive.getLocationPabean());
            }
        } catch (Exception e) {
        }

        table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][0], ReceivePOReport.fontNormal));
        table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(receive.getRecCode(), ReceivePOReport.fontNormal));
        table.addCell(new Phrase("", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][1], ReceivePOReport.fontNormal));
        table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(cntName, ReceivePOReport.fontNormal));

        table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][2], ReceivePOReport.fontNormal));
        table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(loc.getName(), ReceivePOReport.fontNormal));
        table.addCell(new Phrase("", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][3], ReceivePOReport.fontNormal));
        table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(po.getPoCode(), ReceivePOReport.fontNormal));

        table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][4], ReceivePOReport.fontNormal));
        table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(Formater.formatDate(receive.getReceiveDate(), "dd MMMM yyyy"), ReceivePOReport.fontNormal));
        table.addCell(new Phrase("", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][5], ReceivePOReport.fontNormal));
        table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
        table.addCell(new Phrase(receive.getInvoiceSupplier(), ReceivePOReport.fontNormal));

        if (strDutyFree.equals("1")) {
            table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][6], ReceivePOReport.fontNormal));
            table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
            table.addCell(new Phrase(receive.getNomorBc(), ReceivePOReport.fontNormal));
            table.addCell(new Phrase("", ReceivePOReport.fontNormal));
            table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][7], ReceivePOReport.fontNormal));
            table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
            table.addCell(new Phrase(receive.getJenisDokumen(), ReceivePOReport.fontNormal));

            table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][8], ReceivePOReport.fontNormal));
            table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
            table.addCell(new Phrase(Formater.formatDate(receive.getTglBc(), "dd MMMM yyyy"), ReceivePOReport.fontNormal));
            table.addCell(new Phrase("", ReceivePOReport.fontNormal));
            table.addCell(new Phrase(ReceivePOReport.textHeaderMain[SESS_LANGUAGE][9], ReceivePOReport.fontNormal));
            table.addCell(new Phrase(":", ReceivePOReport.fontNormal));
            table.addCell(new Phrase(loca.getName(), ReceivePOReport.fontNormal));
        }
        createEmptySpace(table, 2);

        return table;
    }

    private static Table getListHeader(int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = new Table(5);
        int ctnInt[] = {3, 20, 15, 5, 15};
        table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
        table.setDefaultVerticalAlignment(Table.ALIGN_CENTER);
        table.setDefaultCellBackgroundColor(ReceivePOReport.bgColor);
        table.setCellsFitPage(true);
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setSpacing(1);
            table.setPadding(1);

            //nomor
            Cell cell = new Cell(new Phrase(ReceivePOReport.textHeaderItem[SESS_LANGUAGE][0],
                    ReceivePOReport.fontListHeader));
            table.addCell(cell);

            //SKU
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.addCell(new Phrase(ReceivePOReport.textHeaderItem[SESS_LANGUAGE][1],
                    ReceivePOReport.fontListHeader));
            //Name
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(ReceivePOReport.textHeaderItem[SESS_LANGUAGE][2],
                    ReceivePOReport.fontListHeader));
            //Unit
            table.addCell(new Phrase(ReceivePOReport.textHeaderItem[SESS_LANGUAGE][3],
                    ReceivePOReport.fontListHeader));
            //Qty
            table.addCell(new Phrase(ReceivePOReport.textHeaderItem[SESS_LANGUAGE][4],
                    ReceivePOReport.fontListHeader));

        } catch (Exception e) {
            System.out.println("Err table header: " + e.toString());
        }

        return table;
    }

    private static Document getContent(Vector lists, Document document, PdfWriter writer,
            int SESS_LANGUAGE, int typePrint, long oidReceive) throws BadElementException, DocumentException {
        Table table = ReceivePOReport.getListHeader(SESS_LANGUAGE);
        table.setDefaultCellBackgroundColor(Color.WHITE);

        Cell cell;
        try {
            int start = 0;
            double totalSellPrice = 0.0;
            for (int i = 0; i < lists.size(); i++) {
                Vector temp = (Vector) lists.get(i);
                MatReceiveItem recItemx = (MatReceiveItem) temp.get(0);
                Material mat = (Material) temp.get(1);
                Unit unit = (Unit) temp.get(2);
                CurrencyType currencyType = (CurrencyType) temp.get(3);
                String listStockCode = "";
                mat.setOID(recItemx.getMaterialId());
                if (typePrint == 1) {
                    double priceSales = SessMaterial.getPriceSale(mat);
                    recItemx.setCost(priceSales);
                    recItemx.setTotal(priceSales * recItemx.getQty());
                    totalSellPrice = totalSellPrice + recItemx.getTotal();
                }
                
                Category cat = new Category();
                try {
                    cat = PstCategory.fetchExc(mat.getCategoryId());
                } catch (Exception e) {
                }

                double sellPrice = 0;
                double totalSalesPrice = 0;
                if (typePrint == 2) {
                    sellPrice = SessMaterial.getPriceSale(mat);
                    totalSalesPrice = sellPrice * recItemx.getQty();
                    totalSellPrice = totalSellPrice + totalSalesPrice;
                }

                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + recItemx.getOID();
                    Vector cntStockCode = PstReceiveStockCode.list(0, 0, where, "");
                    for (int s = 0; s < cntStockCode.size(); s++) {
                        ReceiveStockCode materialStockCode = (ReceiveStockCode) cntStockCode.get(s);
                        if (s == 0) {
                            listStockCode = listStockCode + "<br>&nbsp;SN : " + materialStockCode.getStockCode();
                        } else {
                            listStockCode = listStockCode + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + materialStockCode.getStockCode();
                        }

                    }
                }
                start = start + 1;

                cell = new Cell(new Phrase(String.valueOf(i + 1), ReceivePOReport.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(mat.getSku(), ReceivePOReport.fontLsContent));
                table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                table.addCell(cell);
                table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                cell = new Cell(new Phrase(mat.getName(), ReceivePOReport.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(unit.getCode(), ReceivePOReport.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(Formater.formatNumber(recItemx.getQty(), "###"), ReceivePOReport.fontLsContent));
                table.addCell(cell);
            }

            Table descTable = new Table(1);
            descTable.setBorderColor(new Color(255, 255, 255));
            descTable.setWidth(100);
            descTable.setSpacing(1);
            descTable.setPadding(1);
            descTable.setDefaultCellBorder(Table.NO_BORDER);

            descTable.addCell(new Phrase(textMessage[SESS_LANGUAGE][0], ReceivePOReport.fontLsContent));

            document.add(descTable);
            document.add(table);
            document.add(ReceivePOReport.getHeaderLast(SESS_LANGUAGE, oidReceive));

        } catch (Exception e) {
            System.out.println("err: " + e.toString());
        }
        return document;
    }

    private static Table getApproval(int SESS_LANGUAGE, MatReceive mat) throws BadElementException, DocumentException {
        Table table = new Table(3);
        String signRec1 = PstSystemProperty.getValueByName("SIGN_RECEIVE_1");
        String signRec2 = PstSystemProperty.getValueByName("SIGN_RECEIVE_2");
        String signRec3 = PstSystemProperty.getValueByName("SIGN_RECEIVE_3");
        String[] sign1 = signRec1.split(",");
        String[] sign2 = signRec2.split(",");
        String[] sign3 = signRec3.split(",");

        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setBorderWidth(0);
            table.setSpacing(1);
            table.setPadding(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);

            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            if (signRec1.equals(signRec1) && !signRec1.equals("Not initialized")) {
                table.addCell(new Phrase(sign1[0], ReceivePOReport.fontLsContent));

            } else {
                table.addCell(new Phrase("Diterima Oleh,", ReceivePOReport.fontLsContent));

            }
            if (signRec2.equals(signRec2) && !signRec2.equals("Not initialized")) {
                table.addCell(new Phrase(sign2[0], ReceivePOReport.fontLsContent));

            } else {
                table.addCell(new Phrase("Dikirim Oleh,", ReceivePOReport.fontLsContent));

            }
            if (signRec3.equals(signRec3) && !signRec3.equals("Not initialized")) {
                table.addCell(new Phrase(sign3[0], ReceivePOReport.fontLsContent));

            } else {
                table.addCell(new Phrase("Mengetahui,", ReceivePOReport.fontLsContent));

            }

            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            createEmptySpace(table, 3);

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("(_________________________)", ReceivePOReport.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("(_________________________)", ReceivePOReport.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("(_________________________)", ReceivePOReport.fontLsContent));

        } catch (Exception e) {
            System.out.println("Err aproval: " + e.toString());
        }
        return table;
    }

    private static Table getHeaderLast(int SESS_LANGUAGE, long oidReceive) throws BadElementException, DocumentException {
        int ctnInt[] = {10, 1, 60, 10, 1, 15};
        //Table table = new Table(10);
        Table table = new Table(6);
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setBorderWidth(0);
            table.setCellpadding(0);
            table.setCellspacing(0);

            double ppn = 0;
            double valuePpn = 0;
            double lastPpn = 0;
            boolean include = false;
            double subTotal = 0;
            double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
            MatReceive receive = new MatReceive();
            receive = PstMatReceive.fetchExc(oidReceive);
            String whereItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + receive.getOID();
            double totalBeli = (PstMatReceiveItem.getTotal(whereItem));
            ppn = (receive.getTotalPpn());
            if (ppn == 0) {
                ppn = defaultPpn;
            }
            int includePpn = receive.getIncludePpn();

            double totalBeliWithPPN = ((totalBeli * (receive.getTotalPpn() / 100)) + totalBeli);
            //inlude ppn
            valuePpn = 0.0;
            if (receive.getIncludePpn() == 1) {
                include = true;
                valuePpn = (totalBeli - (totalBeli / 1.1));
            } else if (receive.getIncludePpn() == 0) {
                include = false;
                valuePpn = (totalBeli * (receive.getTotalPpn() / 100));
            }

            table.setDefaultColspan(3);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("", ReceivePOReport.fontLsContent));

            table.setDefaultColspan(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("Sub Total", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase(":", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(totalBeli), ReceivePOReport.fontLsContent));

            table.setDefaultColspan(3);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("", ReceivePOReport.fontLsContent));

            table.setDefaultColspan(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("PPN", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase(":", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(valuePpn) + " (" + ppn + " %)", ReceivePOReport.fontLsContent));

            table.setDefaultColspan(3);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("", ReceivePOReport.fontLsContent));

            table.setDefaultColspan(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("Total", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase(":", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            if (include) {
                table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(totalBeli), ReceivePOReport.fontLsContent));
            } else {
                table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(totalBeliWithPPN), ReceivePOReport.fontLsContent));
            }

            table.setDefaultColspan(3);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("", ReceivePOReport.fontLsContent));

            table.setDefaultColspan(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            table.addCell(new Phrase("", ReceivePOReport.fontLsContent));

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
            if (include) {
                table.addCell(new Phrase("(Include PPN)", ReceivePOReport.fontLsContent));
            } else {
                table.addCell(new Phrase("(Not include PPN)", ReceivePOReport.fontLsContent));
            }

        } catch (Exception e) {
            System.out.println("exc header" + e.toString());
        }
        return table;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}


