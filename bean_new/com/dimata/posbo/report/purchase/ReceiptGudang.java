/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.report.purchase;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.posbo.entity.warehouse.MatDispatch;
import com.dimata.posbo.entity.warehouse.MatDispatchBill;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatchBill;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.report.purchase.PurchaseOrderNoShippingPrintPDF;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
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

/**
 *
 * @author arise
 */
public class ReceiptGudang extends HttpServlet {

	public static final String mainTitle[][] = {
		{"PENERIMAAN BARANG DI GUDANG/TOKO"},
		{"RECEPTION ITEM IN THE WAREHOUSE/STORE"}
	};

	public static final String textHeaderMain[][] = {
		{"Nomor", "Lokasi Asal", "Tanggal", "Jenis Dokumen", "Nomor BC", "Lokasi Tujuan", "Nomor Pengiriman", "Tanggal BC"},
		{"Number", "Origin", "Date", "Document Type", "Customs Number", "Destination", "Shipment  Number", "Customs Date"}
	};

	public static final String textHeaderItem[][] = {
		{"No", "SKU", "Barcode", "Nama", "Unit", "Qty Order", "Qty Transfer", "Qty Terima", "Total Beli", "Total", "Keterangan"},
		{"No", "SKU", "Barcode", "Name", "Unit", "Qty Order", "Qty Dispatch", "Qty Receive", "Total Buy", "Total", "Information"}
	};

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public static Vector vect = new Vector();
	// setting the color values
	public static Color border = new Color(0x00, 0x00, 0x00);
	public static Color bgColor = new Color(220, 220, 220);
	// setting some fonts in the color chosen by the user
	public static com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD, ReceiptGudang.border);
	public static com.lowagie.text.Font fontTitleUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD + com.lowagie.text.Font.UNDERLINE, ReceiptGudang.border);
	public static com.lowagie.text.Font fontMainHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, ReceiptGudang.border);
	public static com.lowagie.text.Font fontHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC, ReceiptGudang.border);
	public static com.lowagie.text.Font fontHeaderUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC + com.lowagie.text.Font.UNDERLINE, ReceiptGudang.border);
	public static com.lowagie.text.Font fontListHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, ReceiptGudang.border);
	public static com.lowagie.text.Font fontLsContent = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8);
	public static com.lowagie.text.Font fontLsContentUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.UNDERLINE, ReceiptGudang.border);
	public static com.lowagie.text.Font fontNormal = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
	public static com.lowagie.text.Font fontNormalBold = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.BOLD, ReceiptGudang.border);
	public static com.lowagie.text.Font fontNormalHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
	public static int dutyFree = Integer.parseInt(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE"));

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
			HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", ReceiptGudang.fontLsContent)), false);
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.setBorder(HeaderFooter.NO_BORDER);
			//document.setHeader(header);
			document.setFooter(footer);
			document.open();
			long oidReceiveMaterial = 0;
			int SESS_LANGUAGE = 0;
			String approot = "";

			try {
				SESS_LANGUAGE = FRMQueryString.requestInt(request, "sess_language");
				oidReceiveMaterial = FRMQueryString.requestLong(request, "receive_id");
				approot = FRMQueryString.requestString(request, "approot");
			} catch (Exception e) {
				System.out.println("Err: " + e.getMessage());
			}
			MatReceive matReceive = PstMatReceive.fetchExc(oidReceiveMaterial);
			String whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
			Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0, 0, whereClause, "");
			String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
			System.out.println("approot = " + pathImage);
			com.lowagie.text.Image gambar = null;
			try {
				gambar = com.lowagie.text.Image.getInstance(pathImage);
			} catch (Exception ex) {
				System.out.println("gambar >>>>>> = " + gambar.getImageMask());
			}

			ReceiptGudang.vect = new Vector(1, 1);
			document.add(ReceiptGudang.getHeaderImage(SESS_LANGUAGE, gambar, SESS_LANGUAGE, null));
			document.add(ReceiptGudang.getSubHeader(SESS_LANGUAGE, matReceive));
			document = ReceiptGudang.getContent(listMatReceiveItem, document, writer, SESS_LANGUAGE, matReceive);
			document.add(ReceiptGudang.getApproval(3));
		} catch (Exception e) {
		}

		document.close();
		response.setContentType("application/pdf");
		response.setContentLength(baos.size());
		ServletOutputStream sos = response.getOutputStream();
		baos.writeTo(sos);
		sos.flush();
	}

	private static Table getHeaderImage(int SESS_LANGUAGE, com.lowagie.text.Image gambar,
			int typeRequest, PurchaseOrder purchaseOrder)
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
			gambar.scaleAbsolute(100, 49);
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			table.addCell(new Phrase(new Chunk(gambar, 0, 0)));
			createEmptySpace(table, 1);

			//sub title
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			table.addCell(new Phrase(ReceiptGudang.mainTitle[SESS_LANGUAGE][0].toUpperCase(), ReceiptGudang.fontTitleUnderline));
			createEmptySpace(table, 2);
		} catch (Exception e) {
		}
		return table;
	}

	private static Table getSubHeader(int SESS_LANGUAGE, MatReceive matReceive)
			throws BadElementException, DocumentException {
		Table table = new Table(7);
		int ctnInt[] = {20, 2, 30, 10, 15, 2, 25};
		table.setBorderColor(new Color(255, 255, 255));
		table.setWidth(100);
		table.setWidths(ctnInt);
		table.setSpacing(1);
		table.setPadding(1);
		table.setDefaultCellBorder(Table.NO_BORDER);
		table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
		table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

		MatDispatch df = new MatDispatch();
		try {
			if (matReceive != null && matReceive.getOID() != 0) {
				df = PstMatDispatch.fetchExc(matReceive.getDispatchMaterialId());
			}
		} catch (Exception e) {
		}
		PurchaseRequest pr = new PurchaseRequest();
		Location asal = null;
		Location tujuan = null;

		try {
			asal = PstLocation.fetchExc(matReceive.getReceiveFrom());
			tujuan = PstLocation.fetchExc(matReceive.getLocationId());
		} catch (Exception e) {
		}

		boolean showDF = false;
		if(ReceiptGudang.dutyFree == 1){
			showDF = true;
		}
		
		// Jenis Dokumen --- nomor 
		table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][0], ReceiptGudang.fontNormal));
		table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
		table.addCell(new Phrase(matReceive.getRecCode(), ReceiptGudang.fontNormal));
		table.addCell(new Phrase("", ReceiptGudang.fontNormal));
		table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][2], ReceiptGudang.fontNormal));
		table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
		table.addCell(new Phrase(Formater.formatDate(matReceive.getReceiveDate()), ReceiptGudang.fontNormal));
		// lokasi --- nomor bc
		table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][1], ReceiptGudang.fontNormal));
		table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
		table.addCell(new Phrase(asal.getName(), ReceiptGudang.fontNormal));
		table.addCell(new Phrase("", ReceiptGudang.fontNormal));
		if(showDF){
			table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][4], ReceiptGudang.fontNormal));
			table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
			table.addCell(new Phrase(matReceive.getNomorBc(), ReceiptGudang.fontNormal));
		}else{
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
  }
		// tanggal --- supplier
		table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][5], ReceiptGudang.fontNormal));
		table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
		table.addCell(new Phrase(tujuan.getName(), ReceiptGudang.fontNormal));
		table.addCell(new Phrase("", ReceiptGudang.fontNormal));
		if(showDF){
			table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][3], ReceiptGudang.fontNormal));
			table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
			table.addCell(new Phrase(matReceive.getJenisDokumen(), ReceiptGudang.fontNormal));		
		}else{
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
  }
		// nomor pengiriman
		table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][6], ReceiptGudang.fontNormal));
		table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
		table.addCell(new Phrase(df.getDispatchCode(), ReceiptGudang.fontNormal));
		table.addCell(new Phrase("", ReceiptGudang.fontNormal));
		if(showDF){
			Date tglBc = matReceive.getTglBc();
			table.addCell(new Phrase(ReceiptGudang.textHeaderMain[SESS_LANGUAGE][7], ReceiptGudang.fontNormal));
			table.addCell(new Phrase(":", ReceiptGudang.fontNormal));
			table.addCell(new Phrase((tglBc == null) ? "-" : Formater.formatDate(tglBc), ReceiptGudang.fontNormal)); 		
		}else{
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
      table.addCell(new Phrase("", ReceiptGudang.fontNormal));
  }
		createEmptySpace(table, 2);
		return table;
	}

	private static Table getListHeader(int SESS_LANGUAGE) throws BadElementException, DocumentException {
		Table table = new Table(8);
		int ctnInt[] = {4, 14, 14, 20, 10, 8, 10, 12};
		table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
		table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
		table.setDefaultCellBackgroundColor(PurchaseOrderNoShippingPrintPDF.bgColor);
		table.setCellsFitPage(true);
		try {
			table.setBorderColor(new Color(255, 255, 255));
			table.setWidth(100);
			table.setWidths(ctnInt);
			table.setSpacing(1);
			table.setPadding(1);

//     {"No", "SKU", "Barcode", "HS Code", "Nama", "Unit", "Harga Beli", "Qty", "Total", "Keterangan"},
//		{"No", "SKU", "Barcode", "HS Code", "Name", "Unit", "Price", "Qty", "Total", "Information"}
			//nomor
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][0],
					ReceiptGudang.fontListHeader));
			//sku
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][1],
					ReceiptGudang.fontListHeader));
			//barcode
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][2],
					ReceiptGudang.fontListHeader));
			//code hs
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][3],
					ReceiptGudang.fontListHeader));
			//nama
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][4],
					ReceiptGudang.fontListHeader));
			//unit
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][5],
					ReceiptGudang.fontListHeader));
			//harga
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][6],
					ReceiptGudang.fontListHeader));
			//qty
			table.addCell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][7],
					ReceiptGudang.fontListHeader));

		} catch (Exception e) {
			System.out.println("Err table header: " + e.toString());
		}

		return table;
	}

	private static Document getContent(Vector lists, Document document, PdfWriter writer,
			int SESS_LANGUAGE, MatReceive matReceive) throws BadElementException, DocumentException {
		Table table = ReceiptGudang.getListHeader(SESS_LANGUAGE);
		table.setDefaultCellBackgroundColor(Color.WHITE);
		double grandTotal = 0;
		boolean status = true;
		int tempStats = 0;
		Cell cell;
		try {
			for (int i = 0; i < lists.size(); i++) {
				if (tempStats == i) {
					status = true;
				}
				//initialize list item before display the details
				Vector temp = (Vector) lists.get(i);
				MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
				Material mat = (Material) temp.get(1);
				Unit unit = (Unit) temp.get(2);

				cell = new Cell(new Phrase(String.valueOf(i + 1), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(mat.getSku(), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(mat.getBarCode(), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(mat.getName(), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(unit.getCode(), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(FRMHandler.userFormatStringDecimal(recItem.getQty()), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(FRMHandler.userFormatStringDecimal(recItem.getQty()), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(FRMHandler.userFormatStringDecimal(recItem.getQty()), ReceiptGudang.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);

				grandTotal += recItem.getTotal();
				tempStats = i + 1;
			}

			Table descTable = new Table(1);
			descTable.setBorderColor(new Color(255, 255, 255));
			descTable.setWidth(100);
			descTable.setSpacing(1);
			descTable.setPadding(1);
			descTable.setDefaultCellBorder(Table.NO_BORDER);
			//keterangan
			cell = new Cell(new Phrase(ReceiptGudang.textHeaderItem[SESS_LANGUAGE][10]
					+ ": " + matReceive.getRemark(), ReceiptGudang.fontNormal));
			descTable.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
			descTable.addCell(cell);

			document.add(table);
			document.add(descTable);

		} catch (Exception e) {
			System.out.println("err: " + e.toString());
		}
		return document;
	}

	private static Table getApproval(int length) throws BadElementException, DocumentException {
		Table table = new Table(length);
		String sign1 = PstSystemProperty.getValueByName("SIGN_RECEIVE_DF_1");
		String sign2 = PstSystemProperty.getValueByName("SIGN_RECEIVE_DF_2");
		String sign3 = PstSystemProperty.getValueByName("SIGN_RECEIVE_DF_3");
		try {
			table.setBorderColor(new Color(255, 255, 255));
			table.setWidth(100);
			table.setBorderWidth(0);
			table.setSpacing(1);
			table.setPadding(1);
			table.setDefaultCellBorder(Table.NO_BORDER);
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.setDefaultVerticalAlignment(Table.ALIGN_TOP);
			for (int i = 0; i < length; i++) {
				table.addCell("");
			}
			table.addCell(new Phrase(sign1, ReceiptGudang.fontNormalHeader));
			table.addCell(new Phrase(sign2, ReceiptGudang.fontNormalHeader));
			table.addCell(new Phrase(sign3, ReceiptGudang.fontNormalHeader));
			for (int i = 0; i < length; i++) {
				table.addCell("");
				table.addCell("");
				table.addCell("");
			}
			for (int i = 0; i < length; i++) {
				table.addCell(new Phrase("..............................................", ReceiptGudang.fontLsContent));
			}

		} catch (Exception e) {
			System.out.println("Err aproval: " + e.toString());
		}
		return table;
	}

	private static void createEmptySpace(Table table, int space) throws BadElementException, DocumentException {
		for (int i = 0; i < space; i++) {
			table.setDefaultCellBorder(Table.NO_BORDER);
			table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
			table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			table.addCell(new Phrase("", ReceiptGudang.fontLsContent));
		}

	}

	private static void getLocation(Location source, long oid) {
		try {
			Location temp = PstLocation.fetchExc(oid);
			source.setName(temp.getName());
		} catch (Exception e) {
			System.out.println("Ambil data lokasi dari oid: " + e.toString());
		}

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
