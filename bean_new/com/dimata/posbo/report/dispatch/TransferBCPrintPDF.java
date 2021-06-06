/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.report.dispatch;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.warehouse.MatDispatch;
import com.dimata.posbo.entity.warehouse.MatDispatchBill;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatchBill;
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
public class TransferBCPrintPDF extends HttpServlet {

	public static final String mainTitle[][] = {
		{"Transfer dari Toko ke Ruang Penyerahan"},
		{"Transfer from the Store to the Airport"}
	};

	public static final String textHeaderMain[][] = {
		{"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Jenis Dokumen", "Nomor BC"},
		{"Number", "Location", "Destination", "Date", "Document Type", "Customs Number"}
	};

	public static final String textHeaderItem[][] = {
		{"No", "Nomor Invoice", "SKU", "Barang", "Qty", "Harga", "Total", "Status", "Detail", "Grand", "Keterangan"},
		{"No", "Invoice Number", "SKU", "Items", "Qty", "Price", "Total", "Status", "Detail", "Grand", "Description"}
	};

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public static Vector vect = new Vector();
	// setting the color values
	public static Color border = new Color(0x00, 0x00, 0x00);
	public static Color bgColor = new Color(220, 220, 220);
	// setting some fonts in the color chosen by the user
	public static com.lowagie.text.Font fontTitle = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontTitleUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 13, com.lowagie.text.Font.BOLD + com.lowagie.text.Font.UNDERLINE, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontMainHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontHeaderUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.ITALIC + com.lowagie.text.Font.UNDERLINE, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontListHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10, com.lowagie.text.Font.BOLD, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontLsContent = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8);
	public static com.lowagie.text.Font fontLsContentUnderline = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.UNDERLINE, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontNormal = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);
	public static com.lowagie.text.Font fontNormalBold = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 8, com.lowagie.text.Font.BOLD, TransferBCPrintPDF.border);
	public static com.lowagie.text.Font fontNormalHeader = new com.lowagie.text.Font(com.lowagie.text.Font.STRIKETHRU, 10);

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

			HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", TransferBCPrintPDF.fontLsContent)), false);
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.setBorder(HeaderFooter.NO_BORDER);
			//document.setHeader(header);
			document.setFooter(footer);

			document.open();

			long dispatchOid = 0;
			int SESS_LANGUAGE = 0;
			String approot = "";

			try {
				SESS_LANGUAGE = FRMQueryString.requestInt(request, "sess_language");
				dispatchOid = FRMQueryString.requestLong(request, "dispatch_id");
				approot = FRMQueryString.requestString(request, "approot");
			} catch (Exception e) {
				System.out.println("Err: " + e.getMessage());
			}
			MatDispatch md = PstMatDispatch.fetchExc(dispatchOid);
			String whereClause = PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_DISPATCH_MATERIAL_ITEM_ID] + "="+ dispatchOid;
			Vector listInvoices = PstMatDispatchBill.getListInvoices(0, 0, whereClause, "");
			String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
			System.out.println("approot = " + pathImage);
			com.lowagie.text.Image gambar = null;
			try {
				gambar = com.lowagie.text.Image.getInstance(pathImage);
			} catch (Exception ex) {
				System.out.println("gambar >>>>>> = " + gambar.getImageMask());
			}

			TransferBCPrintPDF.vect = new Vector(1, 1);

			document.add(TransferBCPrintPDF.getHeaderImage(SESS_LANGUAGE, gambar, SESS_LANGUAGE, null));
			document.add(TransferBCPrintPDF.getSubHeader(SESS_LANGUAGE, md));

			document = TransferBCPrintPDF.getContent(listInvoices, document, writer, SESS_LANGUAGE, md);

			document.add(TransferBCPrintPDF.getApproval(3));

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
			table.addCell(new Phrase(TransferBCPrintPDF.mainTitle[SESS_LANGUAGE][0].toUpperCase(), TransferBCPrintPDF.fontTitleUnderline));

			createEmptySpace(table, 2);

		} catch (Exception e) {
		}
		return table;
	}

	private static Table getSubHeader(int SESS_LANGUAGE, MatDispatch md)
			throws BadElementException, DocumentException {
		Table table = new Table(7);
		int ctnInt[] = {15, 2, 25, 20, 15, 2, 25};
		table.setBorderColor(new Color(255, 255, 255));
		table.setWidth(100);
		table.setWidths(ctnInt);
		table.setSpacing(1);
		table.setPadding(1);

		table.setDefaultCellBorder(Table.NO_BORDER);
		table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
		table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

		Location loc = new Location();
				
		// nomor --- jenis dokumen
		table.addCell(new Phrase(TransferBCPrintPDF.textHeaderMain[SESS_LANGUAGE][0], TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(":", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(md.getDispatchCode(), TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase("", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(TransferBCPrintPDF.textHeaderMain[SESS_LANGUAGE][4], TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(":", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(md.getJenisDokumen(), TransferBCPrintPDF.fontNormal));

		// lokasi asal --- nomor BC
		getLocation(loc, md.getLocationId());
		table.addCell(new Phrase(TransferBCPrintPDF.textHeaderMain[SESS_LANGUAGE][1], TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(":", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(loc.getName(), TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase("", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(TransferBCPrintPDF.textHeaderMain[SESS_LANGUAGE][5], TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(":", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(md.getNomorBeaCukai(), TransferBCPrintPDF.fontNormal));

		// lokasi tujuan ---
		getLocation(loc, md.getDispatchTo());
		table.addCell(new Phrase(TransferBCPrintPDF.textHeaderMain[SESS_LANGUAGE][2], TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(":", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(loc.getName(), TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(""));
		table.addCell(new Phrase(""));
		table.addCell(new Phrase(""));
		table.addCell(new Phrase(""));

		// tanggal ---
		table.addCell(new Phrase(TransferBCPrintPDF.textHeaderMain[SESS_LANGUAGE][3], TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(":", TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(Formater.formatDate(md.getDispatchDate(), "dd-MM-yyyy"), TransferBCPrintPDF.fontNormal));
		table.addCell(new Phrase(""));
		table.addCell(new Phrase(""));
		table.addCell(new Phrase(""));
		table.addCell(new Phrase(""));

		createEmptySpace(table, 2);

		return table;
	}

	private static Table getListHeader(int SESS_LANGUAGE) throws BadElementException, DocumentException {
		Table table = new Table(8);
		int ctnInt[] = {3, 14, 15, 15, 5, 8, 10, 8};
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

			//nomor
			Cell cell = new Cell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][0], 
					TransferBCPrintPDF.fontListHeader));
			table.addCell(cell);

			//invoice number
			cell = new Cell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][1], 
					TransferBCPrintPDF.fontListHeader));
			table.addCell(cell);

			//sku
			table.addCell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][2], 
					TransferBCPrintPDF.fontListHeader));
			//barang
			table.addCell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][3], 
					TransferBCPrintPDF.fontListHeader));
			//qty
			table.addCell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][4], 
					TransferBCPrintPDF.fontListHeader));
			//harga
			table.addCell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][5], 
					TransferBCPrintPDF.fontListHeader));
			//total harga
			table.addCell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][6] 
					+ " " + TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][5], 
					TransferBCPrintPDF.fontListHeader));
			
			//status
			cell = new Cell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][7], 
					TransferBCPrintPDF.fontListHeader));
			table.addCell(cell);
		} catch (Exception e) {
			System.out.println("Err table header: " + e.toString());
		}

		return table;
	}

	private static Document getContent(Vector lists, Document document, PdfWriter writer,
			int SESS_LANGUAGE, MatDispatch md) throws BadElementException, DocumentException {
		Table table = TransferBCPrintPDF.getListHeader(SESS_LANGUAGE);
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
				MatDispatchBill mdb = (MatDispatchBill) temp.get(0);
				BillMain bm = (BillMain) temp.get(1);
				Vector billDetails = PstMatDispatchBill.getInvoiceDetail(0, 0, "", "", bm.getOID());
				int detailSize = billDetails.size() + 1;

				cell = new Cell(new Phrase(String.valueOf(i + 1), TransferBCPrintPDF.fontLsContent));
				cell.setRowspan(detailSize);
				table.addCell(cell);
				cell = new Cell(new Phrase(bm.getInvoiceNumber(), TransferBCPrintPDF.fontLsContent));
				cell.setRowspan(detailSize);
				table.addCell(cell);

				double totalQty = 0.0;
				double totalPrice = 0.0;

				for (int j = 0; j < billDetails.size(); j++) {
					Billdetail bd = (Billdetail) billDetails.get(j);

					totalQty += bd.getQty();
					totalPrice += bd.getTotalPrice();

					cell = new Cell(new Phrase(bd.getSku(), TransferBCPrintPDF.fontLsContent));
					table.addCell(cell);
					table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
					cell = new Cell(new Phrase(bd.getItemName(), TransferBCPrintPDF.fontLsContent));
					table.addCell(cell);
					table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
					cell = new Cell(new Phrase(Double.toString(bd.getQty()), TransferBCPrintPDF.fontLsContent));
					table.addCell(cell);
					table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
					cell = new Cell(new Phrase(FRMHandler.userFormatStringDecimal(bd.getItemPrice()), TransferBCPrintPDF.fontLsContent));
					table.addCell(cell);
					cell = new Cell(new Phrase(FRMHandler.userFormatStringDecimal(bd.getTotalPrice()), TransferBCPrintPDF.fontLsContent));
					table.addCell(cell);
					table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
					if (status) {
						cell = new Cell(new Phrase(PstBillMain.deliveryStatus[bm.getStatus()], TransferBCPrintPDF.fontLsContent));
						cell.setRowspan(detailSize);
						table.addCell(cell);
						status = false;
					}

				}

				cell = new Cell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][6], TransferBCPrintPDF.fontNormalBold));
				cell.setColspan(2);
				table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
				cell = new Cell(new Phrase(Double.toString(totalQty), TransferBCPrintPDF.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
				cell = new Cell(new Phrase("", TransferBCPrintPDF.fontLsContent));
				table.addCell(cell);
				cell = new Cell(new Phrase(FRMHandler.userFormatStringDecimal(totalPrice), TransferBCPrintPDF.fontLsContent));
				table.addCell(cell);
				table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);

				grandTotal += totalPrice;
				tempStats = i + 1;

			}
			
			Table descTable = new Table(1);
			descTable.setBorderColor(new Color(255, 255, 255));
			descTable.setWidth(100);
			descTable.setSpacing(1);
			descTable.setPadding(1);
			descTable.setDefaultCellBorder(Table.NO_BORDER);
			//keterangan
			cell = new Cell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][10] 
					+ ": " + md.getRemark(), TransferBCPrintPDF.fontNormal));
			descTable.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
			descTable.addCell(cell);
			//grand total
			cell = new Cell(new Phrase(TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][9] 
					+ " " + TransferBCPrintPDF.textHeaderItem[SESS_LANGUAGE][6] 
					+ ": " + FRMHandler.userFormatStringDecimal(grandTotal), 
					TransferBCPrintPDF.fontMainHeader));
			descTable.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
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
		String sign1 = PstSystemProperty.getValueByName("SIGN_TRANSFER_CUSTOMS_1");
		String sign2 = PstSystemProperty.getValueByName("SIGN_TRANSFER_CUSTOMS_2");
		String sign3 = PstSystemProperty.getValueByName("SIGN_TRANSFER_CUSTOMS_3");
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
			table.addCell(new Phrase(sign1, TransferBCPrintPDF.fontNormalHeader));
			table.addCell(new Phrase(sign2, TransferBCPrintPDF.fontNormalHeader));
			table.addCell(new Phrase(sign3, TransferBCPrintPDF.fontNormalHeader));
			for (int i = 0; i < length; i++) {
				table.addCell("");
				table.addCell("");
				table.addCell("");
			}
			for (int i = 0; i < length; i++) {
				table.addCell(new Phrase("..............................................", TransferBCPrintPDF.fontLsContent));
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
			table.addCell(new Phrase("", TransferBCPrintPDF.fontLsContent));
		}

	}
	
	private static void getLocation(Location source,long oid){
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
