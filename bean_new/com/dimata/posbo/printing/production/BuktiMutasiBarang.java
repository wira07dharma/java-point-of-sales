/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.printing.production;

import com.dimata.aiso.entity.masterdata.anggota.Anggota;
import com.dimata.aiso.entity.masterdata.anggota.PstAnggota;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
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
 * @author arise
 */
public class BuktiMutasiBarang extends HttpServlet {

	private boolean sessLogin = false;

	public static final String mainTitle[][] = {
		{"Bukti Mutasi Barang"},
		{"Bukti Mutasi Barang"}
	};

	public static final String textHeaderMain[][] = {
		{"Dari", "Kepada", "Tanggal", "No. Order"},
		{"From", "To","Date","Order No."}
	};

	public static final String textHeaderItem[][] = {
		{"No", "SKU", "Nama Barang", "Kategori", "Qty", "Keterangan"},
		{"No", "SKU", "Item Name","Category", "Qty", "Remark"}
	};

	public static final String textMessage[][] = {
		{""},
		{""}
	};

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public static final String TTD_SYSPROP = "SIGN_BUKTI_MUTASI_BARANG_";
	
	public static Vector vect = new Vector();
	// setting the color values
	public static Color border = new Color(0x00, 0x00, 0x00);
	public static Color bgColor = new Color(220, 220, 220);
	// setting some fonts in the color chosen by the user
	public static com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontTitleUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD + com.lowagie.text.Font.UNDERLINE, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontMainHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontHeaderUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC + com.lowagie.text.Font.UNDERLINE, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontListHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontLsContent = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8);
	public static com.lowagie.text.Font fontLsContentUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.UNDERLINE, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontNormal = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
	public static com.lowagie.text.Font fontNormalBold = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.BOLD, BuktiMutasiBarang.border);
	public static com.lowagie.text.Font fontNormalHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
	
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

				HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", BuktiMutasiBarang.fontLsContent)), false);
				footer.setAlignment(Element.ALIGN_CENTER);
				footer.setBorder(HeaderFooter.NO_BORDER);
				//document.setHeader(header);
				document.setFooter(footer);

				document.open();

				long pinjamanoid = 0;
				long billMainOid = 0;
				int SESS_LANGUAGE = 0;
				String approot = "";
                                BillMain bm = new BillMain();
				Pinjaman pinjaman = new Pinjaman();
				try {
					SESS_LANGUAGE = FRMQueryString.requestInt(request, "sess_language");
					billMainOid = FRMQueryString.requestLong(request, "bill_main_oid");
					approot = FRMQueryString.requestString(request, "approot");
					
                                        bm = PstBillMain.fetchExc(billMainOid);
					String apiUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
					String url = apiUrl + "/kredit/pengajuan/status-by-bill/" + billMainOid;
					JSONObject jsonObj = WebServices.getAPI("", url);
                                        if(jsonObj != null && jsonObj.length() > 0){
                                            PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
                                            pinjamanoid = pinjaman.getOID();
                                        }
					
				} catch (Exception e) {
					System.out.println("Err: " + e.getMessage());
				}

				String whereClause = " " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMainOid; 
				Vector listBarang = PstBillDetail.list(0, 0, whereClause, ""); 
				String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
				System.out.println("approot = " + pathImage);
				com.lowagie.text.Image gambar = null; 

				try {
					gambar = com.lowagie.text.Image.getInstance(pathImage);
				} catch (Exception ex) {
					System.out.println("gambar >>>>>> = " + gambar.getImageMask());
				}
				
				document.add(BuktiMutasiBarang.getHeaderImage(SESS_LANGUAGE, gambar));
				document.add(BuktiMutasiBarang.getSubHeader(SESS_LANGUAGE, pinjaman, bm));
				
				document = BuktiMutasiBarang.getContent(listBarang, document, writer, SESS_LANGUAGE);
				document.add(BuktiMutasiBarang.getApproval(SESS_LANGUAGE, pinjaman, bm));
			
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
			table.addCell(new Phrase(BuktiMutasiBarang.mainTitle[SESS_LANGUAGE][0].toUpperCase(), BuktiMutasiBarang.fontTitleUnderline));

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
			table.addCell(new Phrase("", BuktiMutasiBarang.fontLsContent));
		}

	}
	
	private static Table getSubHeader(int SESS_LANGUAGE, Pinjaman pinjaman, BillMain bm)
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
			if(pinjaman.getOID() != 0){
                            anggota = PstAnggota.fetchExc(pinjaman.getAnggotaId());
                        }else{
                            anggota = PstAnggota.fetchExc(bm.getCustomerId());
                        }
		} catch (Exception e) {
		}
		
		// Nama -
		table.addCell(new Phrase(BuktiMutasiBarang.textHeaderMain[SESS_LANGUAGE][0], BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(":", BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase("Raditya", BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase("", BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(BuktiMutasiBarang.textHeaderMain[SESS_LANGUAGE][2], BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(":", BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(Formater.formatDate(bm.getBillDate(), "dd-MM-yyyy"), BuktiMutasiBarang.fontNormal));

		// Alamat -
		table.addCell(new Phrase(BuktiMutasiBarang.textHeaderMain[SESS_LANGUAGE][1], BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(":", BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(anggota.getName(), BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase("", BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(BuktiMutasiBarang.textHeaderMain[SESS_LANGUAGE][3], BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(":", BuktiMutasiBarang.fontNormal));
		table.addCell(new Phrase(bm.getInvoiceNo(), BuktiMutasiBarang.fontNormal));

		createEmptySpace(table, 2);

		return table;
	}
	
	private static Table getListHeader(int SESS_LANGUAGE) throws BadElementException, DocumentException {
		Table table = new Table(6);
		int ctnInt[] = {3,15, 20, 15, 5, 15};
		table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
		table.setDefaultVerticalAlignment(Table.ALIGN_CENTER);
		table.setDefaultCellBackgroundColor(BuktiMutasiBarang.bgColor);
		table.setCellsFitPage(true);
		try {
			table.setBorderColor(new Color(255, 255, 255));
			table.setWidth(80);
			table.setWidths(ctnInt);
			table.setSpacing(1);
			table.setPadding(1);

			//nomor
			Cell cell = new Cell(new Phrase(BuktiMutasiBarang.textHeaderItem[SESS_LANGUAGE][0], 
					BuktiMutasiBarang.fontListHeader));
			table.addCell(cell);  
			//nama barang
			table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
			table.addCell(new Phrase(BuktiMutasiBarang.textHeaderItem[SESS_LANGUAGE][1], 
					BuktiMutasiBarang.fontListHeader));
			//nama barang
			table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
			table.addCell(new Phrase(BuktiMutasiBarang.textHeaderItem[SESS_LANGUAGE][2], 
					BuktiMutasiBarang.fontListHeader));
			//category
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase(BuktiMutasiBarang.textHeaderItem[SESS_LANGUAGE][3], 
					BuktiMutasiBarang.fontListHeader));
			//qty
			table.addCell(new Phrase(BuktiMutasiBarang.textHeaderItem[SESS_LANGUAGE][4], 
					BuktiMutasiBarang.fontListHeader));
			//keterangan
			table.addCell(new Phrase(BuktiMutasiBarang.textHeaderItem[SESS_LANGUAGE][5], 
					BuktiMutasiBarang.fontListHeader));
			
		} catch (Exception e) {
			System.out.println("Err table header: " + e.toString());
		}

		return table;
	}
	
	private static Document getContent(Vector lists, Document document, PdfWriter writer,
			int SESS_LANGUAGE) throws BadElementException, DocumentException {
		Table table = BuktiMutasiBarang.getListHeader(SESS_LANGUAGE); 
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
				
				cell = new Cell(new Phrase(String.valueOf(i+1), BuktiMutasiBarang.fontLsContent));
				table.addCell(cell);
				cell = new Cell(new Phrase(bd.getSku(), BuktiMutasiBarang.fontLsContent));
				table.addCell(cell);
				cell = new Cell(new Phrase(bd.getItemName(), BuktiMutasiBarang.fontLsContent));
				table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(cat.getName(), BuktiMutasiBarang.fontLsContent));
				table.addCell(cell);
				cell = new Cell(new Phrase(Formater.formatNumber(bd.getQty(), "###"), BuktiMutasiBarang.fontLsContent));
				table.addCell(cell);
				cell = new Cell(new Phrase(bd.getNote(), BuktiMutasiBarang.fontLsContent));
				table.addCell(cell);
			}
			
			Table descTable = new Table(1);
			descTable.setBorderColor(new Color(255, 255, 255));
			descTable.setWidth(80);
			descTable.setSpacing(1);
			descTable.setPadding(1);
			descTable.setDefaultCellBorder(Table.NO_BORDER);
			
//			descTable.addCell(new Phrase(textMessage[SESS_LANGUAGE][0], BuktiMutasiBarang.fontLsContent)); 		
//			document.add(descTable);
			document.add(table);

		} catch (Exception e) {
			System.out.println("err: " + e.toString());
		}
		return document;
	}
	
	private static Table getApproval(int SESS_LANGUAGE, Pinjaman p, BillMain bm) throws BadElementException, DocumentException {
		Table table = new Table(2);
				
		try {
                        Anggota ang = new Anggota();
			if(p.getOID() != 0){
                            ang = PstAnggota.fetchExc(p.getAnggotaId());
                        }else{
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

			createEmptySpace(table, 2); 
			createEmptySpace(table, 2); 
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase("Diserahkan oleh,", BuktiMutasiBarang.fontLsContent));
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase("Diterima Oleh", BuktiMutasiBarang.fontLsContent));
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			createEmptySpace(table, 2); 
			createEmptySpace(table, 2); 
			createEmptySpace(table, 2); 
			createEmptySpace(table, 2); 
			
			table.addCell(new Phrase("( " + nameEmp + " )", BuktiMutasiBarang.fontLsContent));
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.addCell(new Phrase("( " + ang.getName() + " )", BuktiMutasiBarang.fontLsContent));

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









