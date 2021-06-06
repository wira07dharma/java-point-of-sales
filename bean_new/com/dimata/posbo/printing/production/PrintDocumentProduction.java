/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.printing.production;

import com.dimata.aiso.entity.masterdata.anggota.Anggota;
import com.dimata.aiso.entity.masterdata.anggota.PstAnggota;
import com.dimata.common.entity.location.Kabupaten;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstKabupaten;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.DocLogger;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.common.session.convert.ConvertAngkaToHuruf;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.sedana.common.I_Sedana;
import com.dimata.sedana.entity.kredit.JadwalAngsuran;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.sedana.entity.kredit.PstJadwalAngsuran;
import com.dimata.sedana.entity.kredit.PstPinjaman;
import com.dimata.sedana.entity.masterdata.JangkaWaktu;
import com.dimata.sedana.entity.masterdata.PstJangkaWaktu;
import com.dimata.sedana.entity.tabungan.PstTransaksi;
import com.dimata.services.WebServices;
import com.dimata.util.Formater;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
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
 * @author Regen
 */
public class PrintDocumentProduction extends HttpServlet {

    //BUKTI MUTASI DOCUMENT============================================
    private boolean sessLogin = false;

    public static final String mainTitleMutasi[][] = {
        {"Bukti Mutasi Barang"},
        {"Bukti Mutasi Barang"}
    };

    public static final String textHeaderMainMutasi[][] = {
        {"Dari", "Kepada", "Tanggal", "No. Order"},
        {"From", "To", "Date", "Order No."}
    };

    public static final String textHeaderItemMutasi[][] = {
        {"No", "SKU", "Nama Barang", "Kategori", "Qty", "Keterangan"},
        {"No", "SKU", "Item Name", "Category", "Qty", "Remark"}
    };

    public static final String textMessageMutasi[][] = {
        {""},
        {""}
    };

    public static final String textHeaderMainKwitansi[][] = {
        {"No. Kredit", "No. Kwitansi", "Terima Dari", "Alamat", "No. Telp.", "Angsuran", "Terbilang", "Barang", "Sales", "Kolektor", "Analis", "Saldo Akhir", "Angsuran ke"},
        {"Loan Num.", "Receipt Num.", "Accepted From", "Address", "Phone Num.", "Installments", "In Text", "Goods", "Sales", "Collector", "Analyst", "Final Balance", "Installment "}
    };

    //SURAT JALAN DOCUMENT============================================
    public static final String mainTitleSuratJalan[][] = {
        {"Surat Jalan"},
        {"Delivery Order"}
    }; 

    public static final String textHeaderMainSuratJalan[][] = {
        {"Konsumen", "Alamat"},
        {"Consumer", "Address"}
    };

    public static final String textHeaderItemSuratJalan[][] = {
        {"No", "Nama Barang", "Kategori", "Qty", "Keterangan"},
        {"No", "Item Name", "Category", "Qty", "Remark"}
    };

    public static final String textMessageSuratJalan[][] = {
        {"Mohon diterima dengan baik barang-barang berikut ini: "},
        {"Please accept the following items: "}
    };

    public static final String textApprovalSuratJalan[][] = {
        {"Konsumen", "Pengirim", "Koordinator"},
        {"Consumer", "Courier", "Coordinator"}
    };

    //SURAT PERINTAH PENGIRIMAN DOCUMENT============================================
    public static final String mainTitleSuratPengiriman[][] = {
        {"Surat Perintah Pengiriman Barang"},
        {"Transfer from the Store to the Airport"}
    };

    public static final String textHeaderMainSuratPengiriman[][] = {
        {"Nama Petugas", "Tanggal", "Plat Nomor"},
        {"Number", "Date", "License Plate"}
    };

    public static final String textHeaderItemSuratPengiriman[][] = {
        {"No", "No PK / No Invoice", "Nama Barang", "Ukuran", "Qty", "Keterangan", "Cek"},
        {"No", "Invoice Number", "Item Name", "Unit", "Qty", "Remark", "Cek"}
    };

    public static final String textMessageSuratPengiriman[][] = {
        {"Formulir surat perintah pengiriman dari koordinator delivery kepada delivery (rangkap 2)"},
        {"Delivery order form from delivery coordinator to delivery (in 2 copy)"}
    };

    public static final String textApprovalSuratPengiriman[][] = {
        {"Delivery", "Koordinator"},
        {"Delivery", "Coordinator"}
    };

    public static final String textApprovalKwitansi[][] = {
        {"Konsumen", "Kolektor"},
        {"Consumer", "COllector"}
    };

    //BERITA ACARA DOCUMENT============================================
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public static final String TTD_SYSPROP_MUTASI = "SIGN_BUKTI_MUTASI_BARANG_";

    public static Vector vect = new Vector();
    // setting the color values
    public static Color border = new Color(0x00, 0x00, 0x00);
    public static Color bgColor = new Color(220, 220, 220);
    // setting some fonts in the color chosen by the user

    public static String namaHari[][] = {
        {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu"},
        {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}
    };
    public static Font fontPublic = new Font(Font.STRIKETHRU, 8);
    public static Font fontLineAtas = new Font(Font.STRIKETHRU, 8);
    public static Font fontLineBawah = new Font(Font.STRIKETHRU, 4);
    public static Font fontUnderSubTitle = new Font(Font.STRIKETHRU, 12);
    public static com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontTitleUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD + com.lowagie.text.Font.UNDERLINE, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontMainHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontHeaderUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC + com.lowagie.text.Font.UNDERLINE, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontListHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontLsContent = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8);
    public static com.lowagie.text.Font fontLsContentUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.UNDERLINE, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontNormal = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
    public static com.lowagie.text.Font fontNormalBold = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontNormalHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
    public static com.lowagie.text.Font fontSection = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 12, com.lowagie.text.Font.BOLD, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontSubSection = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, PrintDocumentProduction.border);
    public static com.lowagie.text.Font fontSectionContent = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8);
    public static com.lowagie.text.Font fontSectionContentBold = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.BOLD, PrintDocumentProduction.border);

    public static NumberFormat formatNumber = NumberFormat.getInstance(new Locale("id", "ID"));

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

            HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", PrintDocumentProduction.fontLsContent)), false);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(HeaderFooter.NO_BORDER);
            //document.setHeader(header);
            document.setFooter(footer);

            document.open();

            long pinjamanoid = 0;
            long billMainOid = 0;
            int SESS_LANGUAGE = 0;
            String approot = "";

            Pinjaman pinjaman = new Pinjaman();
            Location loc = new Location();
            BillMain bm = new BillMain();
            AppUser sales = new AppUser();
            try {
                SESS_LANGUAGE = FRMQueryString.requestInt(request, "sess_language");
                billMainOid = FRMQueryString.requestLong(request, "bill_main_oid");
                approot = FRMQueryString.requestString(request, "approot");

                String apiUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
                String url = apiUrl + "/kredit/pengajuan/status-by-bill/" + billMainOid;
                JSONObject jsonObj = WebServices.getAPI("", url);
                if (jsonObj != null) {
                    PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
                    pinjamanoid = pinjaman.getOID();
                }
                bm = PstBillMain.fetchExc(billMainOid);
                loc = PstLocation.fetchExc(bm.getLocationId());
                sales = PstAppUser.fetch(bm.getAppUserSalesId());
            } catch (Exception e) {
                System.out.println("Err: " + e.getMessage());
            }

            String whereClause = " " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMainOid;
            Vector listBarang = PstBillDetail.list(0, 0, whereClause, "");
            String where = " BM." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMainOid;
            Vector listBarangPengiriman = PstBillDetail.listPemesananDokumen(0, 0, where, "");
            String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
            System.out.println("approot = " + pathImage);
            com.lowagie.text.Image gambar = null;

            try {
                gambar = com.lowagie.text.Image.getInstance(pathImage);
            } catch (Exception ex) {
                System.out.println("gambar >>>>>> = " + gambar.getImageMask());
            }

            //BUKTI MUTASI DOCUMENT============================================
            document.add(PrintDocumentProduction.getHeaderImageMutasi(SESS_LANGUAGE, gambar));
            document.add(PrintDocumentProduction.getSubHeaderMutasi(SESS_LANGUAGE, pinjaman, bm));

            document = PrintDocumentProduction.getContentMutasi(listBarang, document, writer, SESS_LANGUAGE);
            document.add(PrintDocumentProduction.getApprovalMutasi(SESS_LANGUAGE, pinjaman, bm));

            document.newPage();

            //SURAT JALAN DOCUMENT============================================
            document.add(PrintDocumentProduction.getHeaderImageSuratJalan(SESS_LANGUAGE, gambar));
            document.add(PrintDocumentProduction.getSubHeaderSuratJalan(SESS_LANGUAGE, pinjaman, bm));

            document = PrintDocumentProduction.getContentSuratJalan(listBarang, document, writer, SESS_LANGUAGE);
            document.add(PrintDocumentProduction.getApprovalSuratJalan(SESS_LANGUAGE, pinjaman, bm));

            document.newPage();

            //SURAT PERINTAH PENGIRIMAN DOCUMENT============================================
            document.add(PrintDocumentProduction.getHeaderImageSuratPengiriman(SESS_LANGUAGE, gambar));
            document.add(PrintDocumentProduction.getSubHeaderSuratPengiriman(SESS_LANGUAGE, bm));

            document = PrintDocumentProduction.getContentSuratPengiriman(pinjaman, document, writer, SESS_LANGUAGE, bm);
            document.add(PrintDocumentProduction.getApprovalSuratPengiriman(SESS_LANGUAGE, bm));

            document.newPage();

            //BERITA ACARA DOCUMENT============================================
            //KWITANSI GAN =====================================================
            if (pinjamanoid != 0) {
                document.add(PrintDocumentProduction.getHeaderKwitansi(SESS_LANGUAGE, gambar, loc));
                document = PrintDocumentProduction.getContentKwitansi(document, SESS_LANGUAGE, pinjaman, bm, loc, sales);
                document.add(PrintDocumentProduction.getApprovalKwitansi(SESS_LANGUAGE, pinjaman, loc, bm, sales));
                document.newPage();
            }

        } catch (Exception e) {
            System.out.println(""+e.toString());
        }

        document.close();
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        ServletOutputStream sos = response.getOutputStream();
        baos.writeTo(sos);
        sos.flush();

    }

    //BUKTI MUTASI DOCUMENT============================================
    private static Table getHeaderImageMutasi(int SESS_LANGUAGE, com.lowagie.text.Image gambar)
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
            table.addCell(new Phrase(PrintDocumentProduction.mainTitleMutasi[SESS_LANGUAGE][0].toUpperCase(), PrintDocumentProduction.fontTitleUnderline));

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
            table.addCell(new Phrase("", PrintDocumentProduction.fontLsContent));
        }

    }

    private static void createEmptySpacePengiriman(Table table, int space) throws BadElementException, DocumentException {
        for (int i = 0; i < space; i++) {
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("", PrintDocumentProduction.fontLsContent));
        }

    }

    private static Table getSubHeaderMutasi(int SESS_LANGUAGE, Pinjaman pinjaman, BillMain bm)
            throws BadElementException, DocumentException {
        Table table = new Table(7);
        int ctnInt[] = {15, 2, 25, 20, 15, 2, 30};
        table.setBorderColor(new Color(255, 255, 255));
        table.setWidth(80);
        table.setWidths(ctnInt);
        table.setSpacing(1);
        table.setPadding(1);

        table.setDefaultCellBorder(Table.NO_BORDER);
        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

        Anggota anggota = new Anggota();
        try {
            if (pinjaman.getOID() != 0) {
                anggota = PstAnggota.fetchExc(pinjaman.getAnggotaId());
            } else {
                anggota = PstAnggota.fetchExc(bm.getCustomerId());
            }
        } catch (Exception e) {
        }

        Location location = new Location();
        try {
            location = PstLocation.fetchExc(bm.getLocationId());
        } catch (Exception exc){}
        
        // Nama -
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainMutasi[SESS_LANGUAGE][0], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(location.getName(), PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase("", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainMutasi[SESS_LANGUAGE][2], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(Formater.formatDate(bm.getShippingDate(), "dd-MM-yyyy"), PrintDocumentProduction.fontNormal));

        // Alamat -
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainMutasi[SESS_LANGUAGE][1], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(anggota.getName(), PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase("", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainMutasi[SESS_LANGUAGE][3], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase((pinjaman.getOID() > 0 ? pinjaman.getNoKredit() : bm.getInvoiceNo()), PrintDocumentProduction.fontNormal));

        createEmptySpace(table, 2);

        return table;
    }

    private static Table getListHeaderMutasi(int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = new Table(6);
        int ctnInt[] = {3, 15, 20, 15, 5, 15};
        table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
        table.setDefaultVerticalAlignment(Table.ALIGN_CENTER);
        table.setDefaultCellBackgroundColor(PrintDocumentProduction.bgColor);
        table.setCellsFitPage(true);
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(80);
            table.setWidths(ctnInt);
            table.setSpacing(1);
            table.setPadding(1);

            //nomor
            Cell cell = new Cell(new Phrase(PrintDocumentProduction.textHeaderItemMutasi[SESS_LANGUAGE][0],
                    PrintDocumentProduction.fontListHeader));
            table.addCell(cell);
            //nama barang
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemMutasi[SESS_LANGUAGE][1],
                    PrintDocumentProduction.fontListHeader));
            //nama barang
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemMutasi[SESS_LANGUAGE][2],
                    PrintDocumentProduction.fontListHeader));
            //category
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemMutasi[SESS_LANGUAGE][3],
                    PrintDocumentProduction.fontListHeader));
            //qty
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemMutasi[SESS_LANGUAGE][4],
                    PrintDocumentProduction.fontListHeader));
            //keterangan
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemMutasi[SESS_LANGUAGE][5],
                    PrintDocumentProduction.fontListHeader));

        } catch (Exception e) {
            System.out.println("Err table header: " + e.toString());
        }

        return table;
    }

    private static Document getContentMutasi(Vector lists, Document document, PdfWriter writer,
            int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = PrintDocumentProduction.getListHeaderMutasi(SESS_LANGUAGE);
        table.setDefaultCellBackgroundColor(Color.WHITE);

        Cell cell;
        try {
            for (int i = 0; i < lists.size(); i++) {
                Billdetail bd = (Billdetail) lists.get(i);

                Material mt = new Material();
                Category cat = new Category();
                try {
                    mt = PstMaterial.fetchExc(bd.getMaterialId());
                    cat = PstCategory.fetchExc(mt.getCategoryId());
                } catch (Exception e) {
                }

                cell = new Cell(new Phrase(String.valueOf(i + 1), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(bd.getSku(), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(bd.getItemName(), PrintDocumentProduction.fontLsContent));
                table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                table.addCell(cell);
                table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                cell = new Cell(new Phrase(cat.getName(), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(Formater.formatNumber(bd.getQty(), "###"), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(bd.getNote(), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
            }

            Table descTable = new Table(1);
            descTable.setBorderColor(new Color(255, 255, 255));
            descTable.setWidth(80);
            descTable.setSpacing(1);
            descTable.setPadding(1);
            descTable.setDefaultCellBorder(Table.NO_BORDER);

//			descTable.addCell(new Phrase(textMessage[SESS_LANGUAGE][0], PrintDocumentProduction.fontLsContent)); 		
//			document.add(descTable);
            document.add(table);

        } catch (Exception e) {
            System.out.println("err: " + e.toString());
        }
        return document;
    }

    private static Table getApprovalMutasi(int SESS_LANGUAGE, Pinjaman p, BillMain bm) throws BadElementException, DocumentException {
        Table table = new Table(3);

        try {
            Anggota ang = new Anggota();
            if (p.getOID() != 0) {
                ang = PstAnggota.fetchExc(p.getAnggotaId());
            } else {
                ang = PstAnggota.fetchExc(bm.getCustomerId());
            }

            String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
            String nameEmp = "";
            if (bm.getDoPersonId() != 0) {
                JSONArray jArr = new JSONArray();
                String url = hrApiUrl + "/employee/employee-fetch/" + bm.getDoPersonId();
                JSONObject jo = WebServices.getAPI("", url);
                if (jo.length() > 0) {

                    nameEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");

                }
            }

            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(80);
            table.setBorderWidth(0);
            table.setSpacing(1);
            table.setPadding(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);

            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("Gudang,", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("Diserahkan oleh,", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("Diterima Oleh", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            createEmptySpace(table, 3);

            table.addCell(new Phrase("(_______________________)", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("( " + nameEmp + " )", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("( " + ang.getName() + " )", PrintDocumentProduction.fontLsContent));

        } catch (Exception e) {
            System.out.println("Err aproval: " + e.toString());
        }
        return table;
    }

    //SURAT JALAN DOCUMENT==================================================================
    private static Table getHeaderImageSuratJalan(int SESS_LANGUAGE, com.lowagie.text.Image gambar)
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
            table.addCell(new Phrase(PrintDocumentProduction.mainTitleSuratJalan[SESS_LANGUAGE][0].toUpperCase(), PrintDocumentProduction.fontTitleUnderline));

            createEmptySpace(table, 2);

        } catch (Exception e) {
        }
        return table;
    }

    private static Table getSubHeaderSuratJalan(int SESS_LANGUAGE, Pinjaman pinjaman, BillMain bm)
            throws BadElementException, DocumentException {
        Table table = new Table(7);
        int ctnInt[] = {15, 2, 25, 25, 10, 2, 25};
        table.setBorderColor(new Color(255, 255, 255));
        table.setWidth(80);
        table.setWidths(ctnInt);
        table.setSpacing(1);
        table.setPadding(1);

        table.setDefaultCellBorder(Table.NO_BORDER);
        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

        Anggota anggota = new Anggota();
        try {
            if (pinjaman.getOID() != 0) {
                anggota = PstAnggota.fetchExc(pinjaman.getAnggotaId());
            } else {
                anggota = PstAnggota.fetchExc(bm.getCustomerId());
            }
        } catch (Exception e) {
        }

        // Nama -
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainSuratJalan[SESS_LANGUAGE][0], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(anggota.getName(), PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase("", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));

        // Alamat -
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainSuratJalan[SESS_LANGUAGE][1], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        Cell cell = new Cell(new Phrase(anggota.getAddressPermanent(), PrintDocumentProduction.fontNormal));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));

        createEmptySpace(table, 2);

        return table;
    }

    private static Table getListHeaderSuratJalan(int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = new Table(5);
        int ctnInt[] = {3, 20, 15, 5, 15};
        table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
        table.setDefaultVerticalAlignment(Table.ALIGN_CENTER);
        table.setDefaultCellBackgroundColor(PrintDocumentProduction.bgColor);
        table.setCellsFitPage(true);
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(80);
            table.setWidths(ctnInt);
            table.setSpacing(1);
            table.setPadding(1);

            //nomor
            Cell cell = new Cell(new Phrase(PrintDocumentProduction.textHeaderItemSuratJalan[SESS_LANGUAGE][0],
                    PrintDocumentProduction.fontListHeader));
            table.addCell(cell);

            //nama barang
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratJalan[SESS_LANGUAGE][1],
                    PrintDocumentProduction.fontListHeader));
            //category
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratJalan[SESS_LANGUAGE][2],
                    PrintDocumentProduction.fontListHeader));
            //qty
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratJalan[SESS_LANGUAGE][3],
                    PrintDocumentProduction.fontListHeader));
            //keterangan
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratJalan[SESS_LANGUAGE][4],
                    PrintDocumentProduction.fontListHeader));

        } catch (Exception e) {
            System.out.println("Err table header: " + e.toString());
        }

        return table;
    }

    private static Document getContentSuratJalan(Vector lists, Document document, PdfWriter writer,
            int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = PrintDocumentProduction.getListHeaderSuratJalan(SESS_LANGUAGE);
        table.setDefaultCellBackgroundColor(Color.WHITE);

        Cell cell;
        try {
            for (int i = 0; i < lists.size(); i++) {
                Billdetail bd = (Billdetail) lists.get(i);

                Material mt = new Material();
                Category cat = new Category();
                try {
                    mt = PstMaterial.fetchExc(bd.getMaterialId());
                    cat = PstCategory.fetchExc(mt.getCategoryId());
                } catch (Exception e) {
                }

                cell = new Cell(new Phrase(String.valueOf(i + 1), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(bd.getItemName(), PrintDocumentProduction.fontLsContent));
                table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                table.addCell(cell);
                table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                cell = new Cell(new Phrase(cat.getName(), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(Formater.formatNumber(bd.getQty(), "###"), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
                cell = new Cell(new Phrase(bd.getNote(), PrintDocumentProduction.fontLsContent));
                table.addCell(cell);
            }

            Table descTable = new Table(1);
            descTable.setBorderColor(new Color(255, 255, 255));
            descTable.setWidth(80);
            descTable.setSpacing(1);
            descTable.setPadding(1);
            descTable.setDefaultCellBorder(Table.NO_BORDER);

            descTable.addCell(new Phrase(textMessageSuratJalan[SESS_LANGUAGE][0], PrintDocumentProduction.fontLsContent));

            document.add(descTable);
            document.add(table);

        } catch (Exception e) {
            System.out.println("err: " + e.toString());
        }
        return document;
    }

    private static Table getApprovalSuratJalan(int SESS_LANGUAGE, Pinjaman p, BillMain bm) throws BadElementException, DocumentException {
        Table table = new Table(3);

        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        Anggota konsumen = new Anggota();
        try {
            if (p.getOID() != 0) {
                konsumen = PstAnggota.fetchExc(p.getAnggotaId());
            } else {
                konsumen = PstAnggota.fetchExc(bm.getCustomerId());
            }
        } catch (Exception e) {
        }
        String nameEmp = "";
        if (bm.getDoPersonId() != 0) {
            JSONArray jArr = new JSONArray();
            String url = hrApiUrl + "/employee/employee-fetch/" + bm.getDoPersonId();
            JSONObject jo = WebServices.getAPI("", url);
            if (jo.length() > 0) {

                nameEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");

            }
        }
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(80);
            table.setBorderWidth(0);
            table.setSpacing(1);
            table.setPadding(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);

            createEmptySpace(table, 3);
            createEmptySpace(table, 3);

            table.addCell(new Phrase(""));
            table.addCell(new Phrase(""));
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.addCell(new Phrase("\t\tDenpasar, " + Formater.formatDate(new Date(), "dd MMMM yyyy"), fontLsContent));

            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            createEmptySpace(table, 3);
            createEmptySpace(table, 3);

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("( " + konsumen.getName() + " )", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("( " + nameEmp + " )", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("(_________________________)", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(textApprovalSuratJalan[SESS_LANGUAGE][0], PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(textApprovalSuratJalan[SESS_LANGUAGE][1], PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(textApprovalSuratJalan[SESS_LANGUAGE][2] + " " + textApprovalSuratJalan[SESS_LANGUAGE][1], PrintDocumentProduction.fontLsContent));

        } catch (Exception e) {
            System.out.println("Err aproval: " + e.toString());
        }
        return table;
    }

    //SURAT PERINTAH PENGIRIMAN DOCUMENT============================================
    private static Table getHeaderImageSuratPengiriman(int SESS_LANGUAGE, com.lowagie.text.Image gambar)
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

            createEmptySpacePengiriman(table, 10);

            //image in header
            gambar.setAlignment(com.lowagie.text.Image.MIDDLE);
            gambar.scaleAbsolute(100, 100);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase(new Chunk(gambar, 0, 0)));

            createEmptySpacePengiriman(table, 1);

            //sub title
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase(PrintDocumentProduction.mainTitleSuratPengiriman[SESS_LANGUAGE][0].toUpperCase(), PrintDocumentProduction.fontTitleUnderline));

            createEmptySpacePengiriman(table, 2);

        } catch (Exception e) {
        }
        return table;
    }

    private static Table getSubHeaderSuratPengiriman(int SESS_LANGUAGE, BillMain bm)
            throws BadElementException, DocumentException {
        Table table = new Table(7);
        int ctnInt[] = {15, 2, 25, 25, 10, 2, 25};
        table.setBorderColor(new Color(255, 255, 255));
        table.setWidth(100);
        table.setWidths(ctnInt);
        table.setSpacing(1);
        table.setPadding(1);

        table.setDefaultCellBorder(Table.NO_BORDER);
        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        String nameEmp = "";
        if (bm.getDoPersonId() != 0) {
            JSONArray jArr = new JSONArray();
            String url = hrApiUrl + "/employee/employee-fetch/" + bm.getDoPersonId();
            JSONObject jo = WebServices.getAPI("", url);
            if (jo.length() > 0) {

                nameEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");

            }
        }

        // Nama --- Tanggal
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainSuratPengiriman[SESS_LANGUAGE][0], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(nameEmp, PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase("", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainSuratPengiriman[SESS_LANGUAGE][1], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(Formater.formatDate(bm.getShippingDate(), "dd-MM-yyyy"), PrintDocumentProduction.fontNormal));

        // Plat Nomor -
        table.addCell(new Phrase(PrintDocumentProduction.textHeaderMainSuratPengiriman[SESS_LANGUAGE][2], PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(":", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase("____________", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase("", PrintDocumentProduction.fontNormal));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));
        table.addCell(new Phrase(new Chunk("")));

        createEmptySpacePengiriman(table, 2);

        return table;
    }

    private static Table getListHeaderSuratPengiriman(int SESS_LANGUAGE) throws BadElementException, DocumentException {
        Table table = new Table(7);
        int ctnInt[] = {3, 14, 20, 10, 5, 15, 15};
        table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
        table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
        table.setDefaultCellBackgroundColor(PrintDocumentProduction.bgColor);
        table.setCellsFitPage(true);
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setSpacing(1);
            table.setPadding(1);

            //nomor
            Cell cell = new Cell(new Phrase(PrintDocumentProduction.textHeaderItemSuratPengiriman[SESS_LANGUAGE][0],
                    PrintDocumentProduction.fontListHeader));
            table.addCell(cell);

            //invoice number
            cell = new Cell(new Phrase(PrintDocumentProduction.textHeaderItemSuratPengiriman[SESS_LANGUAGE][1],
                    PrintDocumentProduction.fontListHeader));
            table.addCell(cell);

            //nama barang
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratPengiriman[SESS_LANGUAGE][2],
                    PrintDocumentProduction.fontListHeader));
            //unit
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratPengiriman[SESS_LANGUAGE][3],
                    PrintDocumentProduction.fontListHeader));
            //qty
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratPengiriman[SESS_LANGUAGE][4],
                    PrintDocumentProduction.fontListHeader));
            //keterangan
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratPengiriman[SESS_LANGUAGE][5],
                    PrintDocumentProduction.fontListHeader));
            //Cek
            table.addCell(new Phrase(PrintDocumentProduction.textHeaderItemSuratPengiriman[SESS_LANGUAGE][6],
                    PrintDocumentProduction.fontListHeader));

        } catch (Exception e) {
            System.out.println("Err table header: " + e.toString());
        }

        return table;
    }

    private static Document getContentSuratPengiriman(Pinjaman p, Document document, PdfWriter writer,
            int SESS_LANGUAGE, BillMain bim) throws BadElementException, DocumentException {
        Table table = PrintDocumentProduction.getListHeaderSuratPengiriman(SESS_LANGUAGE);
        table.setDefaultCellBackgroundColor(Color.WHITE);

        Cell cell;
        try {
            String whereType = PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_OID] + " = " + bim.getOID();
            int typeDoc = PstDocLogger.getMaxTypeLog(whereType);

            String where = PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_OID] + " = " + bim.getOID() + " AND "
                    + "" + PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_TYPE] + " = " + typeDoc;
            Vector<DocLogger> listDocument = PstDocLogger.list(0, 0, where, "");

            if (listDocument.isEmpty()) {
                String whereClause = " BM." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + bim.getOID();
                Vector lists = PstBillDetail.listPemesananDokumen(0, 0, whereClause, "");

                for (int i = 0; i < lists.size(); i++) {
                    Vector tempList = (Vector) lists.get(i);
                    BillMain bm = (BillMain) tempList.get(0);
                    Material material = (Material) tempList.get(1);
                    Category cat = (Category) tempList.get(2);
                    Unit unit = (Unit) tempList.get(3);
                    Merk mk = (Merk) tempList.get(4);
                    Billdetail bd = (Billdetail) tempList.get(5);

                    cell = new Cell(new Phrase(String.valueOf(i + 1), PrintSuratPerintahPengirimanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase((p.getOID() > 0 ? p.getNoKredit() : bm.getInvoiceNo()), PrintSuratPerintahPengirimanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(material.getName(), PrintSuratPerintahPengirimanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(unit.getName(), PrintSuratPerintahPengirimanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase(Formater.formatNumber(bd.getQty(), "###"), PrintSuratPerintahPengirimanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase("", PrintSuratPerintahPengirimanBarang.fontLsContent));
                    table.addCell(cell);
                    cell = new Cell(new Phrase("", PrintSuratPerintahPengirimanBarang.fontLsContent));
                    table.addCell(cell);
                }
            } else {
                for (DocLogger doc : listDocument) {

                    String clause = PstDocLogger.fieldNames[PstDocLogger.FLD_DESCRIPTION] + " = '" + doc.getDescription() + "'";
                    Vector<DocLogger> listData = PstDocLogger.list(0, 0, clause, "");
                    int count = 0;
                    for (DocLogger dol : listData) {

                        String whereClause = " BM." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + dol.getDocOid();
                        Vector lists = PstBillDetail.listPemesananDokumen(0, 0, whereClause, "");
                        for (int i = 0; i < lists.size(); i++) {
                            count++;
                            Vector tempList = (Vector) lists.get(i);
                            BillMain bm = (BillMain) tempList.get(0);
                            Material material = (Material) tempList.get(1);
                            Category cat = (Category) tempList.get(2);
                            Unit unit = (Unit) tempList.get(3);
                            Merk mk = (Merk) tempList.get(4);
                            Billdetail bd = (Billdetail) tempList.get(5);

                            cell = new Cell(new Phrase(String.valueOf(count), PrintSuratPerintahPengirimanBarang.fontLsContent));
                            table.addCell(cell);
                            cell = new Cell(new Phrase((p.getOID() > 0 ? p.getNoKredit() : bm.getInvoiceNo()), PrintSuratPerintahPengirimanBarang.fontLsContent));
                            table.addCell(cell);
                            cell = new Cell(new Phrase(material.getName(), PrintSuratPerintahPengirimanBarang.fontLsContent));
                            table.addCell(cell);
                            cell = new Cell(new Phrase(unit.getName(), PrintSuratPerintahPengirimanBarang.fontLsContent));
                            table.addCell(cell);
                            cell = new Cell(new Phrase(Formater.formatNumber(bd.getQty(), "###"), PrintSuratPerintahPengirimanBarang.fontLsContent));
                            table.addCell(cell);
                            cell = new Cell(new Phrase("", PrintSuratPerintahPengirimanBarang.fontLsContent));
                            table.addCell(cell);
                            cell = new Cell(new Phrase("", PrintSuratPerintahPengirimanBarang.fontLsContent));
                            table.addCell(cell);
                        }
                    }
                }
            }

            Table descTable = new Table(1);
            descTable.setBorderColor(new Color(255, 255, 255));
            descTable.setWidth(100);
            descTable.setSpacing(1);
            descTable.setPadding(1);
            descTable.setDefaultCellBorder(Table.NO_BORDER);

            descTable.addCell(new Phrase(textMessageSuratPengiriman[SESS_LANGUAGE][0], PrintDocumentProduction.fontLsContent));

            document.add(table);
            document.add(descTable);

        } catch (Exception e) {
            System.out.println("err: " + e.toString());
        }
        return document;
    }

    private static Table getApprovalSuratPengiriman(int SESS_LANGUAGE, BillMain bm) throws BadElementException, DocumentException {
        Table table = new Table(2);

        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setBorderWidth(0);
            table.setSpacing(1);
            table.setPadding(1);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_TOP);

            String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
            String nameEmp = "";
            if (bm.getDoPersonId() != 0) {
                JSONArray jArr = new JSONArray();
                String url = hrApiUrl + "/employee/employee-fetch/" + bm.getDoPersonId();
                JSONObject jo = WebServices.getAPI("", url);
                if (jo.length() > 0) {

                    nameEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");

                }
            }

            createEmptySpacePengiriman(table, 3);
            createEmptySpacePengiriman(table, 3);
            createEmptySpacePengiriman(table, 3);
            createEmptySpacePengiriman(table, 3);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("( " + nameEmp + " )", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("(___________________________________)", PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(textApprovalSuratPengiriman[SESS_LANGUAGE][0], PrintDocumentProduction.fontLsContent));
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase(textApprovalSuratPengiriman[SESS_LANGUAGE][1] + " " + textApprovalSuratPengiriman[SESS_LANGUAGE][0], PrintDocumentProduction.fontLsContent));

        } catch (Exception e) {
            System.out.println("Err aproval: " + e.toString());
        }
        return table;
    }

    public static Table getHeaderKwitansi(int SESS_LANGUAGE, Image gambar, Location loc) throws BadElementException, DocumentException {
        Table table = new Table(1);

        try {
            int ctnInt[] = {100};
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setSpacing(1);
            table.setPadding(0);
            table.setDefaultCellBorder(Table.NO_BORDER);

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase(loc.getName().toUpperCase(), fontTitle));
            table.addCell(new Phrase("ELECTRONIC, MESIN, FURNITURE", fontUnderSubTitle));
            table.addCell(new Phrase(loc.getAddress(), fontUnderSubTitle));
            table.addCell(new Phrase("Telp: " + loc.getTelephone() + ", Fax: " + loc.getFax(), fontUnderSubTitle));

            Cell cell = new Cell(new Phrase("", fontLineAtas));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidth(3f);
            table.addCell(cell);

            cell = new Cell(new Phrase("", fontLineBawah));
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            table.setDefaultCellBorder(Table.NO_BORDER);

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
        return table;
    }

    private static Document getContentKwitansi(Document document, int SESS_LANGUAGE,
            Pinjaman p, BillMain bm, Location loc, AppUser sales) throws BadElementException, DocumentException {

        String apiSedana = PstSystemProperty.getValueByName("SEDANA_URL");
        String apiProchain = PstSystemProperty.getValueByName("POS_API_URL");
        String apiUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
        JSONObject dataJadwalAngsuran = new JSONObject();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ID", "id"));

        int bagianAtasLebar[] = {20, 1, 20, 5, 20};
        Table bagianAtas = createTable(bagianAtasLebar.length, bagianAtasLebar);
        bagianAtas.setDefaultCellBorder(Table.NO_BORDER);

        Anggota a = new Anggota();
        Employee kol = new Employee();
        Employee analis = new Employee();
        Employee salesEmp = new Employee();
        JangkaWaktu jk = new JangkaWaktu();
        Cell cell;

        try {

            String noKwitansi = "";
            double jumlahAngsuran = 0;
            JSONArray semuaAngsuran = new JSONArray();
            
            double jumlahBiaya = 0;
            double totalPengajuan = 0;
            double totalBayar = 0;
            int angsuranKe = 0;
            int banyakAngsuran = 0;

            a = PstAnggota.fetchExc(p.getAnggotaId());
            kol = PstEmployee.fetchFromApi(p.getCollectorId());
            analis = PstEmployee.fetchFromApi(p.getAccountOfficerId());
            if (sales.getEmployeeId() != 0) {
                salesEmp = PstEmployee.fetchFromApi(sales.getEmployeeId());
            }
            String listBarang = "";
            Vector barangs = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + bm.getOID(), "");
            for (int i = 0; i < barangs.size(); i++) {
                Billdetail bd = (Billdetail) barangs.get(i);
                if (i != 0) {
                    listBarang += ", ";
                }
                listBarang += bd.getItemName();
            }

            String linkJangkaWaktu = apiUrl + "/kredit/jangka-waktu/" + p.getJangkaWaktuId();
            JSONObject objJangkaWaktu = WebServices.getAPI("", linkJangkaWaktu);
            long oidJangkaWaktu = PstJangkaWaktu.syncExc(objJangkaWaktu);
            if (oidJangkaWaktu != 0) {
                jk = PstJangkaWaktu.fetchExc(oidJangkaWaktu);
            }

            JSONObject jaObj = new JSONObject();
            JSONObject biayaJson = new JSONObject();
            JSONArray daftarBiayaArr = new JSONArray();
            dataJadwalAngsuran = WebServices.getAPI("", apiSedana + "/angsuran/angsuran-pertama/" + p.getOID());
                System.out.println("Url Kwitansi : "+ apiSedana + "/angsuran/angsuran-pertama/" + p.getOID());
            try {
                JSONObject dataJson = dataJadwalAngsuran.optJSONObject("DATA");
                System.out.println("dataJson : "+ dataJson);
                if (dataJson.optJSONObject("LIST_BIAYA") != null) {
                    biayaJson = dataJson.optJSONObject("LIST_BIAYA");
                    daftarBiayaArr = biayaJson.optJSONArray("DATA");
                }
                JSONArray jadwalAngsuranArr = dataJson.optJSONArray("JADWAL_ANGSURAN");

                jaObj = jadwalAngsuranArr.optJSONObject(0);
                noKwitansi = jaObj.optString("NO_KWITANSI", "-");
                
                semuaAngsuran = dataJson.optJSONArray("ANGSURAN_SEMUA");
                System.out.println("semuaAngsuran : "+ semuaAngsuran);
                for(int x = 0; x < semuaAngsuran.length(); x++){
                    JSONObject angs = semuaAngsuran.getJSONObject(x);
                    double price = angs.optDouble("jumlahANgsuran", 0);
                    totalPengajuan += price;
                }
                jumlahAngsuran = dataJson.optDouble("TOTAL_ANGSURAN", 0);
                angsuranKe = dataJson.optInt("ANGSURAN_KE", 0);
                banyakAngsuran = dataJson.optInt("ANGSURAN_JUMLAH", 0);
                jumlahBiaya = biayaJson.optDouble("TOTAL_BIAYA", 0);

            } catch (Exception e) {
                printErrorMessage(e.toString());
            }

            totalPengajuan = totalPengajuan + jumlahBiaya;
            totalBayar = jumlahAngsuran + jumlahBiaya;

            bagianAtas.addCell(new Phrase("No Kwitansi", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(noKwitansi.equals("") ? "-" : noKwitansi, PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            bagianAtas.addCell(new Phrase("No Kredit", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(p.getNoKredit().equals("") ? "-" : p.getNoKredit(), PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            //sisi kiri
            bagianAtas.addCell(new Phrase("Sudah Terima Dari", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(a.getName() + "/" + a.getAddressPermanent(), PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            bagianAtas.addCell(new Phrase("Banyaknya Uang", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(" Rp. " + numberFormat.format(totalBayar), PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            bagianAtas.addCell(new Phrase("Terbilang", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            ConvertAngkaToHuruf angkaToHuruf = new ConvertAngkaToHuruf(new Double(totalBayar).longValue());
            String text = angkaToHuruf.getText() + "rupiah.";
            cell = new Cell(new Phrase(capitalizeWord(text), PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            bagianAtas.addCell(new Phrase("Untuk Pembayaran", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase("Angsuran I", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase("Rp.", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(numberFormat.format(jumlahAngsuran), PrintDocumentProduction.fontSectionContent));

            for (int i = 0; i < daftarBiayaArr.length(); i++) {
                JSONObject temp = daftarBiayaArr.optJSONObject(i);
                if (temp != null) {
                    String name = temp.optString("JENIS", "");
                    double biaya = temp.optDouble("JUMLAH", 0);
                    bagianAtas.addCell(new Phrase("", PrintDocumentProduction.fontSectionContent));
                    bagianAtas.addCell(new Phrase("", PrintDocumentProduction.fontSectionContent));
                    bagianAtas.addCell(new Phrase(name, PrintDocumentProduction.fontSectionContent));
                    bagianAtas.addCell(new Phrase("Rp.", PrintDocumentProduction.fontSectionContent));
                    bagianAtas.addCell(new Phrase(numberFormat.format(biaya), PrintDocumentProduction.fontSectionContent));
                }
            }

            bagianAtas.addCell(new Phrase("", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase("", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase("Harga Total", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase("Rp.", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(numberFormat.format(p.getJumlahPinjaman()+p.getDownPayment()) + " / " + jk.getJangkaWaktu() + " x Angsuran", PrintDocumentProduction.fontSectionContent));

            createEmptySpace(bagianAtas, bagianAtasLebar.length, 1);

            bagianAtas.addCell(new Phrase("Pembelian/Sewa beli barang/Type", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(listBarang, PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            createEmptySpace(bagianAtas, bagianAtasLebar.length, 1);

            bagianAtas.addCell(new Phrase("Sales", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(salesEmp.getFullName().equals("") ? "-" : salesEmp.getFullName(), PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            bagianAtas.addCell(new Phrase("Analis", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(analis.getFullName(), PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            bagianAtas.addCell(new Phrase("Kolektor", PrintDocumentProduction.fontSectionContent));
            bagianAtas.addCell(new Phrase(":", PrintDocumentProduction.fontSectionContent));
            cell = new Cell(new Phrase(kol.getFullName(), PrintDocumentProduction.fontSectionContent));
            cell.setColspan(3);
            bagianAtas.addCell(cell);

            createEmptySpace(bagianAtas, bagianAtasLebar.length, 2);

            document.add(bagianAtas);

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }

        return document;
    }

    private static Table getApprovalKwitansi(int SESS_LANGUAGE, Pinjaman p, Location loc, BillMain bm, AppUser sales) throws BadElementException, DocumentException {
        int ctnInt[] = {25, 25, 25, 25};
        Table table = createTable(ctnInt.length, ctnInt);
        String emptySpace = "                                      ";
        try {

            AppUser cashierUser = new AppUser();
            Anggota a = PstAnggota.fetchExc(p.getAnggotaId());
            Employee kol = PstEmployee.fetchFromApi(p.getCollectorId());
            Employee kurir = PstEmployee.fetchFromApi(bm.getDoPersonId());
            Employee cashier = PstEmployee.fetchFromApi(cashierUser.getEmployeeId());
            Kabupaten kab = new Kabupaten();
            try {
                if (loc.getRegencyId() != 0) {
                    kab = PstKabupaten.fetchExc(loc.getRegencyId());
                    if (kab.getOID() == 0) {
                        kab = PstKabupaten.fetchExc(loc.getRegencyId());
                    }
                }

                cashierUser = PstAppUser.fetch(bm.getAppUserId());

            } catch (Exception e) {
                printErrorMessage(e.toString());
            }
            table.setDefaultCellBorder(Table.NO_BORDER);

            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            Cell cell = new Cell(new Phrase(kab.getNmKabupaten() + ", " + Formater.formatDate(new Date(), "dd MMMM yyyy"), PrintDocumentProduction.fontNormal));
            cell.setColspan(ctnInt.length);
            table.addCell(cell);

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.addCell(new Phrase("Bagian Barang", PrintDocumentProduction.fontNormalBold));
            table.addCell(new Phrase("Pengirim", PrintDocumentProduction.fontNormalBold));
            table.addCell(new Phrase("Penerima", PrintDocumentProduction.fontNormalBold));
            table.addCell(new Phrase("Kasir", PrintDocumentProduction.fontNormalBold));

            createEmptySpace(table, ctnInt.length, 3);

            table.addCell(new Phrase("( " + emptySpace + " )", PrintDocumentProduction.fontNormal));
            table.addCell(new Phrase("( " + kurir.getFullName() + " )", PrintDocumentProduction.fontNormal));
            table.addCell(new Phrase("( " + a.getName() + " )", PrintDocumentProduction.fontNormal));
            table.addCell(new Phrase("( " + (cashier.getFullName().equals("") ? emptySpace : cashier.getFullName()) + " )", PrintDocumentProduction.fontNormal));

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
        return table;
    }

    public static String capitalizeWord(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }

    public static Table createTable(int col, int[] widths) throws BadElementException, DocumentException {
        Table tempTable = new Table(col);

        int ctnInt[] = widths;
        tempTable.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
        tempTable.setDefaultVerticalAlignment(Table.ALIGN_CENTER);
        tempTable.setDefaultCellBackgroundColor(bgColor);
        tempTable.setCellsFitPage(true);
        tempTable.setDefaultCellBackgroundColor(Color.WHITE);
        tempTable.setBorderColor(new Color(255, 255, 255));
        tempTable.setWidth(100);
        tempTable.setWidths(ctnInt);
        tempTable.setSpacing(1f);
        tempTable.setPadding(1);

        return tempTable;
    }

    public static void createEmptySpace(Table table, int col, int row) throws BadElementException, DocumentException {
        for (int i = 0; i < row; i++) {
            Cell cell = new Cell(new Phrase("", fontLsContent));
            cell.setColspan(col);
            table.addCell(cell);
        }

    }

    public static void printErrorMessage(String errorMessage) {
        System.out.println("");
        System.out.println("========================================>>> WARNING <<<========================================");
        System.out.println("");
        System.out.println("MESSAGE : " + errorMessage);
        System.out.println("");
        System.out.println("========================================<<< * * * * >>>========================================");
        System.out.println("");
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









