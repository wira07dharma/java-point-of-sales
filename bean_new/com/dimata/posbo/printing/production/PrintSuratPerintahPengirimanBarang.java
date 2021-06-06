/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.printing.production;

import com.dimata.aiso.session.admin.SessUserSession;
import com.dimata.common.entity.logger.DocLogger;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.form.billing.FrmBillMain;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.sedana.entity.kredit.PstPinjaman;
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
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author arise
 */
public class PrintSuratPerintahPengirimanBarang extends HttpServlet {

	private boolean sessLogin = false;

	public static final String mainTitle[][] = {
		{"Surat Perintah Pengiriman Barang"},
		{"Transfer from the Store to the Airport"}
	};

	public static final String textHeaderMain[][] = {
		{"Nama Petugas", "Tanggal", "Plat Nomor"},
		{"Number", "Date", "License Plate"}
	};

	public static final String textHeaderItem[][] = {
		{"No", "Nomor Invoice", "Nama Barang", "Ukuran", "Qty", "Keterangan", "Cek"},
		{"No", "Invoice Number", "Item Name", "Unit", "Qty", "Remark", "Cek"}
	};
	
	public static final String textMessage[][] = {
		{"Formulir surat perintah pengiriman dari koordinator delivery kepada delivery (rangkap 2)"},
		{"Delivery order form from delivery coordinator to delivery (in 2 copy)"}
	};
	
	public static final String textApproval[][] = {
		{"Delivery", "Koordinator"},
		{"Delivery", "Coordinator"}
	};

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public static Vector vect = new Vector();
	// setting the color values
	public static Color border = new Color(0x00, 0x00, 0x00);
	public static Color bgColor = new Color(220, 220, 220);
	// setting some fonts in the color chosen by the user
	public static com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontTitleUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD + com.lowagie.text.Font.UNDERLINE, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontMainHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontHeaderUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC + com.lowagie.text.Font.UNDERLINE, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontListHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontLsContent = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8);
	public static com.lowagie.text.Font fontLsContentUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.UNDERLINE, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontNormal = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
	public static com.lowagie.text.Font fontNormalBold = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.BOLD, PrintSuratPerintahPengirimanBarang.border);
	public static com.lowagie.text.Font fontNormalHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
        private int count = 0;
	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		//CHECK USER LOGIN SESSION
//		HttpSession session = request.getSession();
//		SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
//		if (userSession != null) {
//			if (userSession.isLoggedIn()) {
//				this.sessLogin = true;
//			}
//		}
		this.sessLogin = true;
		if (!sessLogin) {
			response.sendRedirect("login.jsp");
		} else {

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

				HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", PrintSuratPerintahPengirimanBarang.fontLsContent)), false);
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
                                DocLogger doc = new DocLogger();
                                String noDoc = Formater.formatDate(new Date(), "yyyyMMddHHmmss");
                                String nameDoc = "SP";
                                String desc = nameDoc+"-"+noDoc;
				try {
					SESS_LANGUAGE = FRMQueryString.requestInt(request, "sess_language");
                                        String[] oids = FRMQueryString.requestStringValues(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
					billMainOid = FRMQueryString.requestLong(request, "bill_main_oid");
					approot = FRMQueryString.requestString(request, "approot");
					
					String apiUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
					String url = apiUrl + "/kredit/pengajuan/status-by-bill/" + billMainOid;
					JSONObject jsonObj = WebServices.getAPI("", url);
                                        if(jsonObj != null && jsonObj.length() > 0){
					PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
					pinjamanoid = pinjaman.getOID();
                                        }
                                        
                                        String where = PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_OID]+" = "+billMainOid;
                                        Vector <DocLogger> listDocument = PstDocLogger.list(0, 0, where, "");
                                        if(!listDocument.isEmpty()){
                                        for(DocLogger log : listDocument){
                                            this.count++;
                                        }
                                        }else{
                                            this.count = 1;
                                        }
                                            if(oids.length > 0){
                                                BillMain cbm = new BillMain();
                                                for (int i = 0; i < oids.length; i++) {
                                                    cbm = PstBillMain.fetchExc(Long.parseLong(oids[i]));
                                                    doc.setDescription(desc);
                                                    doc.setDocDate(new Date());
                                                    doc.setDocType(this.count);
                                                    doc.setDocOid(cbm.getOID());
                                                    long newOid = PstDocLogger.insertExc(doc);
                                                }
                                            }
                                        
				} catch (Exception e) {
					System.out.println("Err: " + e.getMessage());
				}
                                
				BillMain bm = PstBillMain.fetchExc(billMainOid);
				String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
				System.out.println("approot = " + pathImage);
				com.lowagie.text.Image gambar = null; 

				try {
					gambar = com.lowagie.text.Image.getInstance(pathImage);
				} catch (Exception ex) {
					System.out.println("gambar >>>>>> = " + gambar.getImageMask());
				}
				
				document.add(PrintSuratPerintahPengirimanBarang.getHeaderImage(SESS_LANGUAGE, gambar));
				document.add(PrintSuratPerintahPengirimanBarang.getSubHeader(SESS_LANGUAGE, bm));
				
				document = PrintSuratPerintahPengirimanBarang.getContent(pinjaman, this.count, document, writer, SESS_LANGUAGE);
				document.add(PrintSuratPerintahPengirimanBarang.getApproval(SESS_LANGUAGE, bm));

			} catch (Exception e) {
			}

			document.close();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			ServletOutputStream sos = response.getOutputStream();
			baos.writeTo(sos);
			sos.flush();

		}

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
			table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.mainTitle[SESS_LANGUAGE][0].toUpperCase(), PrintSuratPerintahPengirimanBarang.fontTitleUnderline));

			createEmptySpace(table, 2);

		} catch (Exception e) {
		}
		return table;
	}
	
	
	
	private static void createEmptySpace(Table table, int space) throws BadElementException, DocumentException {
		for (int i = 0; i < space; i++) {
			table.setDefaultCellBorder(Table.NO_BORDER);
			table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
			table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			table.addCell(new Phrase("", PrintSuratPerintahPengirimanBarang.fontLsContent));
		}

	}
	
	private static Table getSubHeader(int SESS_LANGUAGE, BillMain bm)
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
		table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderMain[SESS_LANGUAGE][0], PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase(":", PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase(nameEmp, PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase("", PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderMain[SESS_LANGUAGE][1], PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase(":", PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase(Formater.formatDate(bm.getShippingDate(), "dd-MM-yyyy"), PrintSuratPerintahPengirimanBarang.fontNormal));

		// Plat Nomor -
		table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderMain[SESS_LANGUAGE][2], PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase(":", PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase("____________", PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase("", PrintSuratPerintahPengirimanBarang.fontNormal));
		table.addCell(new Phrase(new Chunk("")));
		table.addCell(new Phrase(new Chunk("")));
		table.addCell(new Phrase(new Chunk("")));


		createEmptySpace(table, 2);

		return table;
	}
	
	private static Table getListHeader(int SESS_LANGUAGE) throws BadElementException, DocumentException {
		Table table = new Table(7);
		int ctnInt[] = {3, 14, 20, 10, 5, 15, 15};
		table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
		table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
		table.setDefaultCellBackgroundColor(PrintSuratPerintahPengirimanBarang.bgColor);
		table.setCellsFitPage(true);
		try {
			table.setBorderColor(new Color(255, 255, 255));
			table.setWidth(100);
			table.setWidths(ctnInt);
			table.setSpacing(1);
			table.setPadding(1);

			//nomor
			Cell cell = new Cell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderItem[SESS_LANGUAGE][0], 
					PrintSuratPerintahPengirimanBarang.fontListHeader));
			table.addCell(cell);  

			//invoice number
			cell = new Cell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderItem[SESS_LANGUAGE][1], 
					PrintSuratPerintahPengirimanBarang.fontListHeader));
			table.addCell(cell);

			//nama barang
			table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderItem[SESS_LANGUAGE][2], 
					PrintSuratPerintahPengirimanBarang.fontListHeader));
			//unit
			table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderItem[SESS_LANGUAGE][3], 
					PrintSuratPerintahPengirimanBarang.fontListHeader));
			//qty
			table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderItem[SESS_LANGUAGE][4], 
					PrintSuratPerintahPengirimanBarang.fontListHeader));
			//keterangan
			table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderItem[SESS_LANGUAGE][5], 
					PrintSuratPerintahPengirimanBarang.fontListHeader));
			//Cek
			table.addCell(new Phrase(PrintSuratPerintahPengirimanBarang.textHeaderItem[SESS_LANGUAGE][6], 
					PrintSuratPerintahPengirimanBarang.fontListHeader));
			
		} catch (Exception e) {
			System.out.println("Err table header: " + e.toString());
		}

		return table;
	}
	
	private static Document getContent(Pinjaman p, int typeDoc, Document document, PdfWriter writer,
			int SESS_LANGUAGE) throws BadElementException, DocumentException {
		Table table = PrintSuratPerintahPengirimanBarang.getListHeader(SESS_LANGUAGE);
		table.setDefaultCellBackgroundColor(Color.WHITE);
                long oidBillMain = 0;
		Cell cell;
		try {
                    
                        String where = PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_OID]+" = "+p.getBillMainId()+" AND "
                                + ""+PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_TYPE]+" = "+typeDoc;
                        Vector <DocLogger> listDocument = PstDocLogger.list(0, 0, where, "");
                        
                        if(listDocument.isEmpty()){
                        String whereClause = " BM." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + p.getBillMainId(); 
                        Vector lists = PstBillDetail.listPemesananDokumen(0, 0, whereClause, ""); 
                        
                            for (int i = 0; i < lists.size(); i++) {
                                    Vector tempList = (Vector) lists.get(i);
                                    BillMain bm = (BillMain) tempList.get(0);
                                    Material material = (Material) tempList.get(1);
                                    Category cat = (Category)tempList.get(2);
                                    Unit unit = (Unit) tempList.get(3);
                                    Merk mk = (Merk) tempList.get(4);
                                    Billdetail bd = (Billdetail) tempList.get(5);

                                    cell = new Cell(new Phrase(String.valueOf(i+1), PrintSuratPerintahPengirimanBarang.fontLsContent));
                                    table.addCell(cell);
                                    cell = new Cell(new Phrase(bm.getInvoiceNo(), PrintSuratPerintahPengirimanBarang.fontLsContent));
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
                        }else{
                            for(DocLogger doc : listDocument){
                                
                                String clause = PstDocLogger.fieldNames[PstDocLogger.FLD_DESCRIPTION]+" = '"+doc.getDescription()+"'";
                                Vector <DocLogger> listData = PstDocLogger.list(0, 0, clause, "");
                                int count = 0;
                                for(DocLogger dol : listData){
                        
                                    String whereClause = " BM." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + dol.getDocOid(); 
                                    Vector lists = PstBillDetail.listPemesananDokumen(0, 0, whereClause, ""); 
                                    for (int i = 0; i < lists.size(); i++) {
                                            count++;
                                            Vector tempList = (Vector) lists.get(i);
                                            BillMain bm = (BillMain) tempList.get(0);
                                            Material material = (Material) tempList.get(1);
                                            Category cat = (Category)tempList.get(2);
                                            Unit unit = (Unit) tempList.get(3);
                                            Merk mk = (Merk) tempList.get(4);
                                            Billdetail bd = (Billdetail) tempList.get(5);

                                            cell = new Cell(new Phrase(String.valueOf(count), PrintSuratPerintahPengirimanBarang.fontLsContent));
                                            table.addCell(cell);
                                            cell = new Cell(new Phrase(bm.getInvoiceNo(), PrintSuratPerintahPengirimanBarang.fontLsContent));
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
			
			descTable.addCell(new Phrase(textMessage[SESS_LANGUAGE][0], PrintSuratPerintahPengirimanBarang.fontLsContent)); 
			
			
			document.add(table);
			document.add(descTable);

		} catch (Exception e) {
			System.out.println("err: " + e.toString());
		}
		return document;
	}
	
	private static Table getApproval(int SESS_LANGUAGE, BillMain bm) throws BadElementException, DocumentException {
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

			createEmptySpace(table, 3);
			createEmptySpace(table, 3);
			createEmptySpace(table, 3);
			createEmptySpace(table, 3);
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase("( " + nameEmp + " )", PrintSuratPerintahPengirimanBarang.fontLsContent));
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase("(___________________________________)", PrintSuratPerintahPengirimanBarang.fontLsContent));
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase(textApproval[SESS_LANGUAGE][0], PrintSuratPerintahPengirimanBarang.fontLsContent));
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase(textApproval[SESS_LANGUAGE][1] + " " + textApproval[SESS_LANGUAGE][0], PrintSuratPerintahPengirimanBarang.fontLsContent));

		} catch (Exception e) {
			System.out.println("Err aproval: " + e.toString());
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


