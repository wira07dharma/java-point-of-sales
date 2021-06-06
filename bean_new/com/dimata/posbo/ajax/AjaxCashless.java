/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.ClsUserModule;
import com.dimata.posbo.entity.masterdata.Event;
import com.dimata.posbo.entity.masterdata.PstClsUserModule;
import com.dimata.posbo.entity.masterdata.PstEvent;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.ClsSpending;
import com.dimata.posbo.entity.masterdata.Company;
import com.dimata.posbo.entity.masterdata.Event;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstClsSpending;
import com.dimata.posbo.entity.masterdata.PstCompany;
import com.dimata.posbo.entity.masterdata.PstEvent;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstSubCategory;
import com.dimata.posbo.entity.masterdata.SubCategory;
import com.dimata.posbo.form.FrmClsSpending;
import com.dimata.posbo.form.masterdata.CtrlClsSpending;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class AjaxCashless extends HttpServlet {

    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();

    //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

    //LONG
    private long oid = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    
    public static final int MODULE_LOGIN = 0;
    public static final int MODULE_ADMIN = 1;
    public static final int MODULE_REGISTRATION = 2;
    public static final int MODULE_TOPUP = 3;
    public static final int MODULE_CHECK_BALANCE = 4;
    public static final int MODULE_TRANSAKSI = 5;
    public static final int MODULE_REFUND = 6;
    public static final int MODULE_INVENTORY_REQUEST = 7;
    public static final int MODULE_INVENTORY_TRANSFER = 8;

    public static final String[] strModule = {
        "Login", "Admin", "Registration", "Top Up", "Check Balance",
        "Transaksi", "Refund", "Inventory Request", "Inventory Transfer"
    };

    private String message = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		
        //OBJECT
        this.jSONObject = new JSONObject();
        this.jSONArray = new JSONArray();

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.htmlReturn = "";

        switch (this.iCommand) {
            case Command.SAVE:
                commandSave(request);
                break;

            case Command.LIST:
                commandList(request, response);
                break;

            case Command.DELETEALL:
                commandDeleteAll(request);
                break;

            case Command.DELETE:
                commandDelete(request);
                break;

            default:
                commandNone(request);
        }

        try {
            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("MESSAGE_RETURN", this.message);
            this.jSONObject.put("ERROR_RETURN", this.iErrCode);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("saveMapModul")) {
            saveForm(request);
            this.htmlReturn = loadMapUserTable(request);
        } else if (this.dataFor.equals("saveEventItem")) {
            saveMappingEventItem(request);
        } else if (this.dataFor.equals("saveMappingBarItem")) {
            saveMappingBarItem(request);
        } else if (this.dataFor.equals("saveTicket")){
			saveTicket(request);
		}
    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
		
        if (this.dataFor.equals("loadUserMap")) {
            this.htmlReturn = loadMapUserTable(request);
        } else if (this.dataFor.equals("listEvent")) {
            String[] cols = {
                PstEvent.fieldNames[PstEvent.FLD_EVENT_CODE],
                PstEvent.fieldNames[PstEvent.FLD_EVENT_CODE],
                PstEvent.fieldNames[PstEvent.FLD_EVENT_TITLE],
                PstEvent.fieldNames[PstEvent.FLD_DESCRIPTION],
                PstEvent.fieldNames[PstEvent.FLD_EVENT_DATETIME],
                PstEvent.fieldNames[PstEvent.FLD_EVENT_END_DATETIME],
                PstEvent.fieldNames[PstEvent.FLD_COMPANY_ID],
                PstEvent.fieldNames[PstEvent.FLD_EVENT_OID]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listItem")) {
            String[] cols = {
                PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID],
                PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID],
				PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID],
                PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("loadTicket")){
			this.htmlReturn = loadTicket(request);
		}
    }

    public void commandDeleteAll(HttpServletRequest request) {
        if (this.dataFor.equals("")) {

        }
    }

    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("deleteMap")) {
			long oidEvent = FRMQueryString.requestLong(request, "EVENT_ID");
			long oidUser = FRMQueryString.requestLong(request, "USER_ID");

			Vector listModule = PstClsUserModule.list(0, 0, 
					PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID]+"="+oidUser+" AND "+
					PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID]+"="+oidEvent, "");
			
			
			if (listModule.size()>0){
				for (int xx = 0; xx < listModule.size(); xx++){
					ClsUserModule clsUserModule = (ClsUserModule) listModule.get(xx);
					try {
						PstClsUserModule.deleteExc(clsUserModule.getOID());
					} catch (Exception exc){}
				}
			}
			
			this.htmlReturn = loadMapUserTable(request);
        } else if (this.dataFor.equals("deleteTicket")){
			long oid = FRMQueryString.requestLong(request, "TICKET_ID");
			try {
				oid = PstClsSpending.deleteExc(oid);
			} catch (Exception exc){}
		}
    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("formAdd")) {
            this.htmlReturn = formAdd(request);
		} else if (this.dataFor.equals("formEdit")) {
            this.htmlReturn = formEdit(request);
		} else if (this.dataFor.equals("getItemByCategory")) {

        } else if (this.dataFor.equals("formAddTicket")){
			this.htmlReturn = formAddTicket(request);
		}
    }

    public JSONObject listDataTables(HttpServletRequest request, HttpServletResponse response, String[] cols, String dataFor, JSONObject result) {
        this.searchTerm = FRMQueryString.requestString(request, "sSearch");
        int amount = 10;
        int start = 0;
        int col = 0;
        String dir = "asc";
        String sStart = request.getParameter("iDisplayStart");
        String sAmount = request.getParameter("iDisplayLength");
        String sCol = request.getParameter("iSortCol_0");
        String sdir = request.getParameter("sSortDir_0");

        if (sStart != null) {
            start = Integer.parseInt(sStart);
            if (start < 0) {
                start = 0;
            }
        }
        if (sAmount != null) {
            amount = Integer.parseInt(sAmount);
            if (amount < 10) {
                amount = 10;
            }
        }
        if (sCol != null) {
            col = Integer.parseInt(sCol);
            if (col < 0) {
                col = 0;
            }
        }
        if (sdir != null) {
            if (!sdir.equals("asc")) {
                dir = "desc";
            }
        }

        String whereClause = "";
        if (dataFor.equals("listEvent")) {
            whereClause += " ("
                    + "" + PstEvent.fieldNames[PstEvent.FLD_EVENT_CODE] + " LIKE '%" + searchTerm + "%' "
                    + " OR " + PstEvent.fieldNames[PstEvent.FLD_EVENT_TITLE] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstEvent.fieldNames[PstEvent.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstEvent.fieldNames[PstEvent.FLD_TAG_DEPOSIT] + " LIKE '%" + searchTerm + "%'"
                    + ")";
        } else if (dataFor.equals("listItem")) {
            whereClause += " ("
                    + "" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            String multiCategoryId = FRMQueryString.requestString(request, "FRM_FLD_CATEGORY_ID");
			String multiSubId = FRMQueryString.requestString(request, "FRM_FLD_SUB_CATEGORY_ID");
            if (multiCategoryId.length() > 0 && !multiCategoryId.equals("null")) {
				if (multiSubId.length() > 0 && !multiSubId.equals("null")){
					whereClause += " AND (" + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " IN (" + multiCategoryId + ") OR "
							+ PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " IN (" + multiSubId + "))";
				} else {
					whereClause += " AND " + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " IN (" + multiCategoryId + ")";
				}
            } else if (multiSubId.length() > 0 && !multiSubId.equals("null")){
				whereClause += " AND " + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " IN (" + multiSubId + ")";
			}
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listEvent")) {
            total = PstEvent.getCount(whereClause);
        } else if (dataFor.equals("listItem")) {
            total = PstMaterial.getCount(whereClause);
        }

        this.amount = amount;
        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor, whereClause);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor, String whereClause) {
        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        String order = "";

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listEvent")) {
            listData = PstEvent.list(start, amount, whereClause, order);
        } else if (dataFor.equals("listItem")) {
            listData = PstMaterial.list(start, amount, whereClause, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listEvent")) {
                Event event = (Event) listData.get(i);
                Company company = new Company();
                if (event.getCompanyId() > 0) {
                    try {
                        company = PstCompany.fetchExc(event.getCompanyId());
                    } catch (Exception e) {
                    }
                }

                ja.put("" + (this.start + i + 1) + ".");
                ja.put(event.getEventCode());
                ja.put(event.getEventTitle());
                ja.put(event.getDescription());
                ja.put(Formater.formatDate(event.getEventDatetime(), "yyyy-MM-dd HH:mm"));
                ja.put(Formater.formatDate(event.getEventEndDatetime(), "yyyy-MM-dd HH:mm"));
                ja.put(company.getCompanyName());
                ja.put("<div class='text-center'><a href='event.jsp?command=" + Command.EDIT + "&event_id=" + event.getOID() + "' class='btn btn-xs btn-warning'><i class='fa fa-pencil'></i></a></div>");
                array.put(ja);
            } else if (dataFor.equals("listItem")) {
                Material material = (Material) listData.get(i);
                long eventId = FRMQueryString.requestLong(request, "FRM_FLD_EVENT_ID");
                Event event = new Event();
                Category category = new Category();
				SubCategory subCategory = new SubCategory();
                try {
                    event = PstEvent.fetchExc(eventId);
                    category = PstCategory.fetchExc(material.getCategoryId());
					subCategory = PstSubCategory.fetchExc(material.getSubCategoryId());
                } catch (Exception e) {
                }
				
                long standardRate = getStandardRateActive(event.getCurrencyTypeId());
                double price = getItemPrice(material.getOID(), event.getPriceTypeId(), standardRate);
                ja.put("" + (this.start + i + 1) + ".");
                ja.put(material.getName());
                ja.put(category.getName());
				ja.put(subCategory.getName());
                ja.put(String.format("%,.2f", price));
                int exist = PstEvent.checkMappingEventItemExist(eventId, material.getOID());
                String checked = (exist > 0) ? "checked" : "";
                String disabled = (exist > 0) ? "disabled" : "";
                ja.put(""
                        + "<div class='text-center'>"
                        + "<label><input type='checkbox' " + checked + " " + disabled + " name='FRM_ITEM_ID' value='" + material.getOID() + "'></label>"
                        + "</div>"
                        + "");
                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
    }

    public long getStandardRateActive(long currencyTypeId) {
        String whereRate = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " = '" + currencyTypeId + "'";
        whereRate += " AND " + PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS] + " = '" + PstStandartRate.ACTIVE + "'";
        Vector<StandartRate> listStandardRate = PstStandartRate.list(0, 0, whereRate, "");
        long standardRateId = 0;
        for (StandartRate sr : listStandardRate) {
            standardRateId = sr.getOID();
        }
        return standardRateId;
    }

    public double getItemPrice(long materialId, long priceTypeId, long standardRateId) {
        String wherePrice = PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] + " = '" + materialId + "'";
        wherePrice += " AND " + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = '" + priceTypeId + "'";
        wherePrice += " AND " + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = '" + standardRateId + "'";
        Vector<PriceTypeMapping> listPrice = PstPriceTypeMapping.list(0, 0, wherePrice, "");
        double price = 0;
        for (PriceTypeMapping ptm : listPrice) {
            price = ptm.getPrice();
        }
        return price;
    }

    public void saveMappingEventItem(HttpServletRequest request) {
        try {
            long eventId = FRMQueryString.requestLong(request, "FRM_EVENT_ID");
            String[] arrayItemId = FRMQueryString.requestStringValues(request, "FRM_ITEM_ID");
            
            if (eventId == 0) {
                this.message = "Event OID is 0";
                this.iErrCode++;
                return;
            }
            if (arrayItemId == null) {
                this.message = "Please select item";
                this.iErrCode++;
                return;
            }

            Event event = PstEvent.fetchExc(eventId);
            long standardRate = getStandardRateActive(event.getCurrencyTypeId());
            int saved = 0;
            for (String itemId : arrayItemId) {
                double price = getItemPrice(Long.valueOf(itemId), event.getPriceTypeId(), standardRate);
                int exist = PstEvent.checkMappingEventItemExist(eventId, Long.valueOf(itemId));
                if (exist == 0) {
                    saved += PstEvent.saveEventItem(eventId, Long.valueOf(itemId), price, event.getCurrencyTypeId());
                }
            }
            this.message = saved + " data have been saved";

        } catch (DBException e) {
            this.message = e.getMessage();
            this.iErrCode++;
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            this.iErrCode++;
            this.message = e.getMessage();
            System.out.println(e.getMessage());
        }

    }
	
	private static String loadMapUserTable(HttpServletRequest request){
		
		String html = "";
		
		long oidEvent = FRMQueryString.requestLong(request, "EVENT_ID");
		
		Event event = new Event();
		
		try {
			event = PstEvent.fetchExc(oidEvent);
		} catch (Exception exc){
			
		}
		
		if (event.getOID()>0){
		
			Vector listMapUser = PstClsUserModule.listUser(0, 0, 
					PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID]
					+" = "+oidEvent, PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID]);

			if (listMapUser.size()>0){

				html = "<button type=\"button\" class=\"btn btn-sm btn-primary\" id='btnAdd'>Add New Map</button><br><br>"
						+ "<table class=\"table table-bordered table-hover\" id='tableMap'>"
						+ "<thead>"
							+ "<tr class=\"label-default\">"
								+ "<th style=\"width: 1%\">No.</th>"
								+ "<th style=\"width: 15%\">User</th>"
								+ "<th>Name</th>"
								+ "<th style=\"width: 30%\">Module</th>"
								+ "<th style=\"width: 15%\">Action</th>"
							+ "</tr>"
						+ "</thead>"
						+ "<tbody>";

				for (int i = 0; i < listMapUser.size(); i++){
					long user = (long) listMapUser.get(i);

					Vector listModule = PstClsUserModule.list(0, 0, 
						PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID]
						+" = "+oidEvent + " AND "+ PstClsUserModule.fieldNames
								[PstClsUserModule.FLD_USER_ID]+"="+user
							, PstClsUserModule.fieldNames[PstClsUserModule.FLD_ID_MODUL_ENABLE]);

					AppUser appUser = new AppUser();
					try {
						appUser = PstAppUser.fetch(user);
					} catch (Exception exc){}

					html += "<tr>"
								+ "<td>"+(i+1)+"</td>"
								+ "<td>"+appUser.getLoginId()+"</td>"
								+ "<td>"+appUser.getFullName()+"</td>";

					if (listModule.size()>0){
						String moduleLi = "<ul class='col-md-12'>";
						for (int x = 0; x < listModule.size(); x++){
							ClsUserModule module = (ClsUserModule) listModule.get(x);
							moduleLi += "<li class='col-md-6'>"+strModule[module.getIdModulEnable()]+"</li>";
						}
						moduleLi += "</ul>";
						html += "<td>"+moduleLi+"</td>";
					} else {
						html += "<td>-</td>";
					}
					html += "<td style=\"text-align: center;\"><button type=\"button\" class=\"btn btn-sm btn-warning btnEdit\" data-oid='"+user+"'><i class=\"fa fa-pencil\"></i></button> "
							+ "<button type=\"button\" class=\"btn btn-sm btn-danger btnDelete\" data-oid='"+user+"'><i class=\"fa fa-trash\"></i></button></td>"
							+ "</tr>";
				}

				html += "</tbody></table>";

			} else {
				html = "<button type=\"button\" class=\"btn btn-sm btn-primary\" id='btnAdd'>Add New Map</button>";
			}
		}
		return html;
	}
	
	public static String formAdd(HttpServletRequest request){
		String html = "";
		long oidEvent = FRMQueryString.requestLong(request, "EVENT_ID");
		long oidCompany = FRMQueryString.requestLong(request, "COMPANY_ID");
		Vector listMapUser = PstClsUserModule.listUser(0, 0, 
					PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID]
					+" = "+oidEvent, "");
		
		String whereClause = PstAppUser.fieldNames[PstAppUser.FLD_COMPANY_ID]+"="+oidCompany;
		
		if (listMapUser.size()>0){
			String notIn = "";
			for (int xx = 0; xx < listMapUser.size(); xx++){
				long userId = (long) listMapUser.get(xx);
				notIn += ","+ userId;
			}
			if (notIn.length()>0){
				notIn = notIn.substring(1);
				whereClause += " AND "+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]+" NOT IN ("+notIn+")";
			}
		}
		
		
		Vector listUser = PstAppUser.listFullObj(0, 0, whereClause, PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]);
		
		html+="<input type='hidden' name='EVENT_ID' value='"+oidEvent+"'>"
				+ "<div class=\"form-group\">"
				+ "<select class=\"form-control\" name=\"user_id\" multiple=\"multiple\" id='user' style='width: 100%'>";
					if (listUser.size()>0){
						for (int i=0; i < listUser.size();i++){
							AppUser appUser = (AppUser) listUser.get(i);
							html += "<option value='"+appUser.getOID()+"'>"+appUser.getLoginId()+" - "+appUser.getFullName()+"</option>";
						}
					}
				html+= "</select>"
			+ "</div>"
			+ "<div class=\"form-group\">"
				+ "<h5>User Module</h5>";
				int id = 0;
				for (String s: strModule) {           
					html += "<div class=\"form-check\">"
								+ "<label>"
									+ "<input type=\"checkbox\" name=\"module\" value='"+id+"'> <span class=\"label-text\">"+s+"</span>"
								+ "</label>"
							+ "</div>";
					id++;
				}
			html += "</div>";
		
		
		return html;
	}
	
	public static String formAddTicket(HttpServletRequest request){
		String html = "";
		long oidEvent = FRMQueryString.requestLong(request, "EVENT_ID");
		long oidSpending = FRMQueryString.requestLong(request, "CLS_SPENDING_ID");
		
		ClsSpending clsSpending = new ClsSpending();
		
		try {
			clsSpending = PstClsSpending.fetchExc(oidSpending);
		} catch (Exception exc){}
		
		
		html+="<input type='hidden' name='"+FrmClsSpending.fieldNames[FrmClsSpending.FRM_FIELD_SPENDING_ID]+"' value='"+clsSpending.getOID()+"'>"
				+ "<input type='hidden' name='"+FrmClsSpending.fieldNames[FrmClsSpending.FRM_FIELD_EVENT_OID]+"' value='"+oidEvent+"'>"
				+ "<div class=\"form-group\">"
				+ "<label>Ticket Name</label>"
				+ "<input type='text' class='form-control' name='"+FrmClsSpending.fieldNames
					[FrmClsSpending.FRM_FIELD_TICKET_NAME]+"' value='"
					+(clsSpending.getOID() > 0 ? clsSpending.getTicketName() : "" )+"'>"
			+ "</div>"
			+ "<div class=\"form-group\">"
				+ "<label>Price</label>"
				+ "<input type='text' class='form-control' name='"+FrmClsSpending.fieldNames
					[FrmClsSpending.FRM_FIELD_PRICE]+"' value='"
					+(clsSpending.getOID() > 0 ? clsSpending.getPrice() : "" )+"'>"
			+ "</div>"
			+ "<div class=\"form-group\">"
				+ "<label>Value</label>"
				+ "<input type='text' class='form-control' name='"+FrmClsSpending.fieldNames
					[FrmClsSpending.FRM_FIELD_VALUE]+"' value='"
					+(clsSpending.getOID() > 0 ? clsSpending.getValue(): "" )+"'>"
			+ "</div>"
			+ "<div class=\"form-group\">"
				+ "<label>Minimum Spending</label>"
				+ "<input type='text' class='form-control' name='"+FrmClsSpending.fieldNames
					[FrmClsSpending.FRM_FIELD_MINIMUM_SPENDING]+"' value='"
					+(clsSpending.getOID() > 0 ? clsSpending.getMinimumSpending() : "" )+"'>"
			+ "</div>"
			+ "<div class=\"form-group\">"
				+ "<label>Detail</label>"
				+ "<textarea class='form-control' name='"+FrmClsSpending.fieldNames
					[FrmClsSpending.FRM_FIELD_DETAILS]+"' >"
					+(clsSpending.getOID() > 0 ? clsSpending.getDetails(): "" )+"</textarea>"
			+ "</div>";
		
		
		return html;
	}
	
	public static String formEdit(HttpServletRequest request){
		String html = "";
		long oidEvent = FRMQueryString.requestLong(request, "EVENT_ID");
		long oidUser = FRMQueryString.requestLong(request, "USER_ID");
		
		AppUser appUser = new AppUser();
		try {
			appUser = PstAppUser.fetch(oidUser);
		} catch (Exception exc){}
		
		Vector listModule = PstClsUserModule.list(0, 0, 
				PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID]+"="+oidUser+" AND "+
				PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID]+"="+oidEvent, "");
		
		Hashtable hashModule = new Hashtable();
		
		if (listModule.size()>0){
			for (int xx = 0; xx < listModule.size(); xx++){
				ClsUserModule clsUserModule = (ClsUserModule) listModule.get(xx);
				hashModule.put(""+clsUserModule.getIdModulEnable(), true);
			}
		}
		
		html+="<input type='hidden' name='EVENT_ID' value='"+oidEvent+"'>"
				+ "<input type='hidden' name='user_id' value='"+oidUser+"'>"
			+ "<div class=\"form-group\">"
				+ "<h5>"+appUser.getFullName()+" User Module</h5>";
				int id = 0;
				for (String s: strModule) {   
					String check = "";
					if (hashModule.get(""+id) != null){
						check = "checked";
					}
					html += "<div class=\"form-check\">"
								+ "<label>"
									+ "<input type=\"checkbox\" name=\"module\" "+check+" value='"+id+"'> <span class=\"label-text\">"+s+"</span>"
								+ "</label>"
							+ "</div>";
					id++;
				}
			html += "</div>";
		
		
		return html;
	}
	
	public static void saveForm(HttpServletRequest request){
		
		long oidEvent = FRMQueryString.requestLong(request, "EVENT_ID");
				
		String[] userId = FRMQueryString.requestStringValues(request, "user_id");
		String[] module = FRMQueryString.requestStringValues(request, "module");
		
		
		if (userId.length>0){
			for (int i = 0; i < userId.length;i++){
				try {
					long userOid = Long.valueOf(userId[i]);
					
					Vector listModule = PstClsUserModule.list(0, 0, 
					PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID]+"="+userOid, "");

					if (listModule.size()>0){
						for (int xx = 0; xx < listModule.size(); xx++){
							ClsUserModule clsUserModule = (ClsUserModule) listModule.get(xx);
							try {
								PstClsUserModule.deleteExc(clsUserModule.getOID());
							} catch (Exception exc){}
						}
					}
					
					for (int x = 0; x < module.length; x++){
						int iModule = Integer.valueOf(module[x]);
						ClsUserModule clsUserModule = new ClsUserModule();
						clsUserModule.setEventOID(oidEvent);
						clsUserModule.setUserId(userOid);
						clsUserModule.setIdModulEnable(iModule);
						try {
							PstClsUserModule.insertExc(clsUserModule);
						} catch (Exception exc){}
					}
				} catch (Exception exc){}
			}
		}
	}
        
    public void saveMappingBarItem(HttpServletRequest request) {
        try {
            long eventId = FRMQueryString.requestLong(request, "FRM_EVENT_ID");
            String[] arrayBarId = FRMQueryString.requestStringValues(request, "FRM_BAR_ID");
            String[] arrayItemId = FRMQueryString.requestStringValues(request, "FRM_ITEM_ID");

            if (eventId == 0) {
                this.message = "Event OID is 0";
                this.iErrCode++;
                return;
            }
            if (arrayBarId == null) {
                this.message = "Please select bar";
                this.iErrCode++;
                return;
            }
            if (arrayItemId == null) {
                this.message = "Please select item";
                this.iErrCode++;
                return;
            }

            int saved = 0;
            for (String roomId : arrayBarId) {
                for (String itemId : arrayItemId) {
                    int exist = PstEvent.checkMappingBarItemExist(Long.valueOf(roomId), eventId, Long.valueOf(itemId));
                    if (exist == 0) {
                        saved += PstEvent.saveMappingBarItem(Long.valueOf(roomId), eventId, Long.valueOf(itemId));
                    }
                }
            }
            this.message = saved + " data have been saved";
        } catch (Exception e) {
            this.message = e.getMessage();
            this.iErrCode++;
        }
    }
	
	public void saveTicket(HttpServletRequest request){
		
		long oid = FRMQueryString.requestLong(request, FrmClsSpending.fieldNames[FrmClsSpending.FRM_FIELD_SPENDING_ID]);
		CtrlClsSpending ctrlClsSpending = new CtrlClsSpending(request);
		ctrlClsSpending.action(Command.SAVE, oid);
	}
	
	private static String loadTicket(HttpServletRequest request){
		
		String html = "";
		
		long oidEvent = FRMQueryString.requestLong(request, "EVENT_ID");
		
		Event event = new Event();
		
		try {
			event = PstEvent.fetchExc(oidEvent);
		} catch (Exception exc){
			
		}
		
		if (event.getOID()>0){
		
			Vector listTicket = PstClsSpending.list(0, 0, 
					PstClsSpending.fieldNames[PstClsSpending.FLD_EVENT_OID]
					+" = "+oidEvent, "");

			if (listTicket.size()>0){

				html =  "<table class=\"table table-bordered table-hover\" id='tableMap'>"
						+ "<thead>"
							+ "<tr class=\"label-default\">"
								+ "<th style=\"width: 1%\">No.</th>"
								+ "<th style=\"width: 30%\">Ticket Name</th>"
								+ "<th>Price</th>"
								+ "<th>Value</th>"
								+ "<th>Minimum Spending</th>"
								+ "<th>Action</th>"
							+ "</tr>"
						+ "</thead>"
						+ "<tbody>";

					for (int i = 0; i < listTicket.size(); i++){
						ClsSpending clsSpending = (ClsSpending) listTicket.get(i);

						html += "<tr>"
									+ "<td>"+(i+1)+"</td>"
									+ "<td>"+clsSpending.getTicketName()+"</td>"
									+ "<td>"+String.format("%,.2f",clsSpending.getPrice())+"</td>"
									+ "<td>"+String.format("%,.2f",clsSpending.getValue())+"</td>"
									+ "<td>"+String.format("%,.2f",clsSpending.getMinimumSpending())+"</td>"
									+ "<td style=\"text-align: center;\"><button type=\"button\" class=\"btn btn-sm btn-warning btnEdit\" data-oid='"+clsSpending.getOID()+"'><i class=\"fa fa-pencil\"></i></button> "
									+ "<button type=\"button\" class=\"btn btn-sm btn-danger btnDelete\" data-oid='"+clsSpending.getOID()+"'><i class=\"fa fa-trash\"></i></button></td>"
								+ "</tr>";
					}

				} else {
					html += "<div><h5>No Data Found</h5></div>";
				}

				html += "</tbody></table>";

			} 
		return html;
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
