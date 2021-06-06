/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.printing.production;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.MaterialTypeMapping;
import com.dimata.posbo.entity.masterdata.PstMaterialMappingType;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.sedana.entity.kredit.PstPinjaman;
import com.dimata.sedana.entity.kredit.PstTypeKredit;
import com.dimata.services.WebServices;
import com.dimata.util.Formater;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author WiraDharma
 */
public class DocumentLaporanBarang extends HttpServlet {

    private boolean sessLogin = false;

    public static final String mainTitle[][] = {
        {"Laporan Barang Produksi"},
        {"Production Item Report"}
    };

    public static final String textHeaderMain[][] = {
        {"Tanggal"},
        {"Date"}
    };

    public static final String textHeaderItem[][] = {
        {"No", "No Kredit/Invoice", "SKU", "Nama Barang", "Tipe", "Qty", "Petugas Delivery"},
        {"No", "No Kredit/Invoice", "SKU", "Item Name", "Type", "Qty", "Staff Delivery"}
    };

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public static Vector vect = new Vector();
    // setting the color values
    public static Color border = new Color(0x00, 0x00, 0x00);
    public static Color bgColor = new Color(220, 220, 220);
    // setting some fonts in the color chosen by the user
    public static com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontTitleUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD + com.lowagie.text.Font.UNDERLINE, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontMainHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontHeaderUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC + com.lowagie.text.Font.UNDERLINE, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontListHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontLsContent = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8);
    public static com.lowagie.text.Font fontLsContentUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.UNDERLINE, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontNormal = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
    public static com.lowagie.text.Font fontNormalBold = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.BOLD, DocumentLaporanBarang.border);
    public static com.lowagie.text.Font fontNormalHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);

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
        com.lowagie.text.Rectangle rectangle = new com.lowagie.text.Rectangle(20, 20, 20, 20);
        rectangle.rotate();
        Document document = new Document(PageSize.A4, 20, 20, 30, 30);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            //step2.2: creating an instance of the writer
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // step 3.1: adding some metadata to the document
            document.addSubject("This is a subject.");
            document.addSubject("This is a subject two.");

            HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", DocumentLaporanBarang.fontLsContent)), false);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(HeaderFooter.NO_BORDER);
            //document.setHeader(header);
            document.setFooter(footer);

            document.open();

            BillMain billMain = new BillMain();
            DataCustom dc = new DataCustom();
            Location location = new Location();
            JenisKredit typeKredit = new JenisKredit();
            Vector user = new Vector();
            Vector listData = new Vector();
            String approot = "";
            String hrApiUrl = "";
            String sedanaAppUrl = "";
            String start_date = "";
            String end_date = "";
            long userId = 0;
            int SESS_LANGUAGE = 0;

            AppUser au = new AppUser();
            try {

                hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
                sedanaAppUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
                approot = FRMQueryString.requestString(request, "approot");
                SESS_LANGUAGE = FRMQueryString.requestInt(request, "sess_language");
                start_date = FRMQueryString.requestString(request, "start_date");
                end_date = FRMQueryString.requestString(request, "end_date");
                userId = FRMQueryString.requestLong(request, "userId");

                au = PstAppUser.fetch(userId);
            } catch (Exception e) {
                System.out.println("Err: " + e.getMessage());
            }

            String where = PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] + " = 'user_create_document_location' " + " AND " + PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + " = " + userId;
            user = PstDataCustom.list(0, 0, where, "");
            for (int i = 0; i < user.size(); i++) {
                dc = (DataCustom) user.get(i);
            }
            String whereClause = "";
            whereClause += " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + "<>" + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
            String stt = "";
            if (dc.getOwnerId() != 0) {
                for (int i = 0; i < user.size(); i++) {
                    dc = (DataCustom) user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            if ((start_date != null && start_date.length() > 0) && (end_date != null && end_date.length() > 0)) {
                whereClause += " AND ("
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + ">= TO_DAYS('" + start_date + "')) AND "
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + "<= TO_DAYS('" + end_date + "'))"
                        + ")";
            }
            String order = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
            listData = PstPinjaman.listBarangProduksi(0, 0, whereClause, order);

            String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
            System.out.println("approot = " + pathImage);
            com.lowagie.text.Image gambar = null;

            try {
                gambar = com.lowagie.text.Image.getInstance(pathImage);
            } catch (Exception ex) {
                System.out.println("gambar >>>>>> = " + gambar.getImageMask());
            }

            document.add(DocumentLaporanBarang.getHeaderImage(SESS_LANGUAGE, gambar));
            document.add(DocumentLaporanBarang.getSubHeader(SESS_LANGUAGE, start_date, end_date));

            document = DocumentLaporanBarang.getContent(listData, document, writer, SESS_LANGUAGE);

        } catch (Exception e) {
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
            table.addCell(new Phrase(DocumentLaporanBarang.mainTitle[SESS_LANGUAGE][0].toUpperCase(), DocumentLaporanBarang.fontTitleUnderline));

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
            table.addCell(new Phrase("", DocumentLaporanBarang.fontLsContent));
        }

    }

    private static Table getSubHeader(int SESS_LANGUAGE, String start_date, String end_date)
            throws BadElementException, DocumentException {
        Table table = new Table(7);
        int ctnInt[] = {15, 2, 35, 15, 10, 2, 25};
        table.setBorderColor(new Color(255, 255, 255));
        table.setWidth(100);
        table.setWidths(ctnInt);
        table.setSpacing(1);
        table.setPadding(1);

        table.setDefaultCellBorder(Table.NO_BORDER);
        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

        // Nama -
        table.addCell(new Phrase(DocumentLaporanBarang.textHeaderMain[SESS_LANGUAGE][0], DocumentLaporanBarang.fontNormal));
        table.addCell(new Phrase(":", DocumentLaporanBarang.fontNormal));
        table.addCell(new Phrase(start_date + " s/d " + end_date, DocumentLaporanBarang.fontNormal));
        table.addCell(new Phrase("", DocumentLaporanBarang.fontNormal));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));

        createEmptySpace(table, 2);

        return table;
    }

    private static Table getListHeader(int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = new Table(7);
        int ctnInt[] = {10, 20, 15, 20, 10, 10, 15};
        table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
        table.setDefaultVerticalAlignment(Table.ALIGN_CENTER);
        table.setDefaultCellBackgroundColor(DocumentLaporanBarang.bgColor);
        table.setCellsFitPage(true);
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setSpacing(1);
            table.setPadding(1);

            Cell cell = new Cell(new Phrase(DocumentLaporanBarang.textHeaderItem[SESS_LANGUAGE][0],
                    DocumentLaporanBarang.fontListHeader));
            table.addCell(cell);

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(DocumentLaporanBarang.textHeaderItem[SESS_LANGUAGE][1],
                    DocumentLaporanBarang.fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(DocumentLaporanBarang.textHeaderItem[SESS_LANGUAGE][2],
                    DocumentLaporanBarang.fontListHeader));
            
            table.addCell(new Phrase(DocumentLaporanBarang.textHeaderItem[SESS_LANGUAGE][3],
                    DocumentLaporanBarang.fontListHeader));
            
            table.addCell(new Phrase(DocumentLaporanBarang.textHeaderItem[SESS_LANGUAGE][4],
                    DocumentLaporanBarang.fontListHeader));
            
            table.addCell(new Phrase(DocumentLaporanBarang.textHeaderItem[SESS_LANGUAGE][5],
                    DocumentLaporanBarang.fontListHeader));
            
            table.addCell(new Phrase(DocumentLaporanBarang.textHeaderItem[SESS_LANGUAGE][6],
                    DocumentLaporanBarang.fontListHeader));

        } catch (Exception e) {
            System.out.println("Err table header: " + e.toString());
        }

        return table;
    }

    private static Document getContent(Vector lists, Document document, PdfWriter writer,
            int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = DocumentLaporanBarang.getListHeader(SESS_LANGUAGE);
        table.setDefaultCellBackgroundColor(Color.WHITE);

        Cell cell;
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        String sedanaAppUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
        try {
            if (!lists.isEmpty()) {
                for (int i = 0; i <= lists.size() - 1; i++) {
                    Vector data = (Vector) lists.get(i);
                    BillMain billMain = (BillMain) data.get(0);
                    Billdetail bd = (Billdetail) data.get(1);
                    Pinjaman pinjaman = new Pinjaman();
                    String whereMapping = PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + "=" + bd.getMaterialId()
                            + " AND " + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_TYPE_ID] + "<> 0";
                    Vector listGroupMapping = PstMaterialMappingType.list(0, 0, whereMapping, "");
                    try {
                        Location location = PstLocation.fetchExc(billMain.getLocationId());

                        String url = sedanaAppUrl + "/kredit/pengajuan/status-by-bill/" + billMain.getOID();
                        JSONObject jsonObj = WebServices.getAPI("", url);

                        PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);

                        JenisKredit typeKredit = PstTypeKredit.fetchExc(pinjaman.getTipeKreditId());

                    } catch (Exception e) {
                    }
                    String petugas = "";
                    String urll = hrApiUrl + "/employee/employee-fetch/" + billMain.getDoPersonId();
                    JSONObject obj = WebServices.getAPI("", urll);
                    if (obj.length() > 0) {
                        petugas = obj.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");
                    } else {
                        petugas = "Petugas Delivery perlu di set";
                    }
                    String typeMaster = "";
                    MasterType masterType = new MasterType();
                    if (!listGroupMapping.isEmpty()) {
                        for (int x = 0; x < listGroupMapping.size(); x++) {
                            MaterialTypeMapping materialTypeMapping = (MaterialTypeMapping) listGroupMapping.get(x);
                            long oidType = materialTypeMapping.getTypeId();
                            String whereMas = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "= 2"
                                    + " AND " + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID] + "=" + oidType;
                            Vector listMasType = PstMasterType.list(0, 0, whereMas, "");
                            for (int xx = 0; xx < listMasType.size(); xx++) {
                                masterType = (MasterType) listMasType.get(xx);
                                if (typeMaster.length() != 0) {
                                    typeMaster += ", " + masterType.getMasterName();
                                } else {
                                    typeMaster = masterType.getMasterName();
                                }
                            }
                        }
                    } else {
                        typeMaster = "-";
                    }

                    cell = new Cell(new Phrase(String.valueOf(i + 1), DocumentLaporanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase((pinjaman.getNoKredit().equals("") ? billMain.getInvoiceNumber() : pinjaman.getNoKredit()), DocumentLaporanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(bd.getSku(), DocumentLaporanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(bd.getItemName(), DocumentLaporanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(typeMaster, DocumentLaporanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(""+bd.getQty(), DocumentLaporanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(petugas, DocumentLaporanBarang.fontLsContent));
                    table.addCell(cell);
                }
            } else {
                table.setDefaultColspan(7);
                cell = new Cell(new Phrase(" Data Tidak Ditemukan ", DocumentLaporanBarang.fontLsContent));
                table.addCell(cell);
            }

            Table descTable = new Table(1);
            descTable.setBorderColor(new Color(255, 255, 255));
            descTable.setWidth(80);
            descTable.setSpacing(1);
            descTable.setPadding(1);
            descTable.setDefaultCellBorder(Table.NO_BORDER);


            document.add(descTable);
            document.add(table);

        } catch (Exception e) {
            System.out.println("err: " + e.toString());
        }
        return document;
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










