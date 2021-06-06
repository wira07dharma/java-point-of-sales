/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.MatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author Ed
 */
public class AjaxSrcProduksi extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;

    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    private JSONArray arrayPromo = new JSONArray();
    private JSONArray arrayItem = new JSONArray();

    //LONG
    private long oid = 0;
    private long oidBaru = 0;
    private long oidPromo = 0;
    private long oidPromoBaru = 0;

    //STRING
    private String oidReturn = "";
    private String dataFor = "";
    private String dataFor2 = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String oidLocation = "";
    private String oidMember = "";
    private String oidPrice = "";

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

    // ANALYSIS
    private double subjectSalesPrice = 0;
    private double subjectPurchasePrice = 0;
    private int subjectQuantity = 0;
    private int subjectTarget = 0;

    private int objectQuantity = 0;
    private int objectQuantityAll = 0;
    private int objectTarget = 0;
    private double objectRegularPrice = 0;
    private double objectCost = 0;
    private double objectPromotionPrice = 0;

    private double profitNonPromo = 0;
    private double profitPromo = 0;
    private double valuePromo = 0;
    private double percentPromo = 0;
    private double totalProfitNonPromo = 0;
    private double totalMarginPromo = 0;
    private double totalValuePromo = 0;

    // SEARCH TERM
    private String searchTagline = "";
    private String searchReason = "";
    private String searchItem = "";
    private String searchLocation = "";
    private String searchMember = "";
    private String searchPrice = "";
    private String searchSubcomb = "";
    private String searchObcomb = "";

    //SRC
    String nomor = "";
    long oidLocationFrom = 0;
    long oidLocationTo = 0;
    String startDate = "";
    String endDate = "";
    int status = 0;
    String sortBy = "";
    String whereList = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.oidPromo = FRMQueryString.requestLong(request, "FRM_FLD_PROMO_OID");
        this.oidReturn = "";
        this.oidBaru = 0;
        this.oidPromoBaru = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.dataFor2 = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR2");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.oidLocation = FRMQueryString.requestString(request, "FRM_FLD_LOCATION_ID");
        this.oidMember = FRMQueryString.requestString(request, "FRM_FLD_MEMBER_TYPE_ID");
        this.oidPrice = FRMQueryString.requestString(request, "FRM_FLD_PRICE_TYPE_ID");
        this.htmlReturn = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;

        //OBJECT
        this.jSONObject = new JSONObject();
        this.jSONArray = new JSONArray();
        this.arrayPromo = new JSONArray();
        this.arrayItem = new JSONArray();

        //SEARCH TERM
        this.searchTagline = FRMQueryString.requestString(request, "term-tagline");
        this.searchReason = FRMQueryString.requestString(request, "term-reason");
        this.searchItem = FRMQueryString.requestString(request, "term-item");
        this.searchLocation = FRMQueryString.requestString(request, "term-location");
        this.searchMember = FRMQueryString.requestString(request, "term-member");
        this.searchPrice = FRMQueryString.requestString(request, "term-price");
        this.searchSubcomb = FRMQueryString.requestString(request, "term-subcomb");
        this.searchObcomb = FRMQueryString.requestString(request, "term-obcomb");

        //SRC
        nomor = FRMQueryString.requestString(request, "FRM_FIELD_DISPATCH_CODE");
        oidLocationFrom = FRMQueryString.requestLong(request, "FRM_FIELD_DISPATCH_FROM");
        oidLocationTo = FRMQueryString.requestLong(request, "FRM_FIELD_DISPATCH_TO");
        startDate = FRMQueryString.requestString(request, "FRM_FIELD_DISPATCH_DATE_FROM");
        endDate = FRMQueryString.requestString(request, "FRM_FIELD_DISPATCH_DATE_TO");
        status = FRMQueryString.requestInt(request, "FRM_FIELD_STATUS");
        sortBy = FRMQueryString.requestString(request, "FRM_FIELD_SORT_BY");

        whereList = "";

        whereList += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = " + PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI;
        if (!nomor.isEmpty()) {
            whereList += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
        }
        if (oidLocationFrom != 0) {
            if (nomor.isEmpty()) {
                whereList += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
            } else {
                whereList += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
            }
        }
        if (oidLocationTo != 0) {
            if (oidLocationFrom == 0) {
                whereList += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
            } else {
                whereList += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
            }
        }
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            whereList += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        }
        if (status != 99) {
            whereList += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
        }

        switch (this.iCommand) {
            case Command.SAVE:
                commandSave(request);
                break;

            case Command.LIST:
                commandList(request, response);
                break;

            case Command.RESET:
                //commandDeleteAll(request);
                break;

            case Command.DELETE:
                commandDelete(request);
                break;

            default:
                commandNone(request);
        }
        try {
            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
            this.jSONObject.put("FRM_FIELD_OID_BARU", "" + this.oidBaru);
            this.jSONObject.put("FRM_FIELD_PROMO", this.arrayPromo);
            this.jSONObject.put("FRM_FIELD_COMBO", this.jSONArray);
            this.jSONObject.put("FRM_FIELD_ITEM", this.arrayItem);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);
    }

    private void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listevent")) {
            String[] cols = {
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE],
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID],
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO],
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listeventSrc")) {
            String[] cols = {
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE],
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID],
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO],
                "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
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

        if (dataFor.equals("listevent")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 3";
                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }
            } else {
                whereClause += " "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR loc1." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR loc2." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 3";
                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }
            }
        } else if (dataFor.equals("listeventSrc")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 4";
                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }
            } else {
                whereClause += " "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR loc1." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR loc2." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 4";
                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listevent")) {
            total = PstMatDispatch.getCountProduksi(whereClause);
        } else if (dataFor.equals("listeventSrc")) {
            total = PstMatDispatch.getCountProduksi(whereClause);
        }

        this.amount = amount;

        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor) {

        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();

        MatDispatch matDispatch = new MatDispatch();

        String whereClause = "";
        String order = "";

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (datafor.equals("listevent")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 3";
                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }

            } else {
                whereClause += " "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR loc1." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR loc2." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 3";

                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }

            }
        } else if (datafor.equals("listeventSrc")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 4";
                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }

            } else {
                whereClause += " "
                        + "(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR loc1." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR loc2." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " = 4";

                if (!nomor.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " = '" + nomor + "'";
                }
                if (oidLocationFrom != 0) {
                    if (nomor.isEmpty()) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    } else {
                        whereClause += " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidLocationFrom;
                    }
                }
                if (oidLocationTo != 0) {
                    if (oidLocationFrom == 0) {
                        whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    } else {
                        whereClause += "" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidLocationTo;
                    }
                }
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
                            + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
                if (status != 99) {
                    whereClause += " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + status;
                }

            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listevent")) {
            listData = PstMatDispatch.listSearchProduksi(start, amount, whereClause, sortBy);
        } else if (datafor.equals("listeventSrc")) {
            listData = PstMatDispatch.listSearchProduksi(start, amount, whereClause, sortBy);
        }
        Vector l1 = new Vector();
        Vector l2 = new Vector();
        Location loc1 = new Location();
        Location loc2 = new Location();

        Vector statusDocKey = new Vector();
        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);

        int stts = 0;

        for (int i = 0; i <= listData.size() - 1; i++) {

            JSONArray ja = new JSONArray();
            if (datafor.equals("listevent")) {
                matDispatch = (MatDispatch) listData.get(i);

                l1 = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + matDispatch.getLocationId(), null);
                loc1 = (Location) l1.get(0);
                l2 = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + matDispatch.getDispatchTo(), null);
                loc2 = (Location) l2.get(0);

                if (matDispatch.getDispatchStatus() == 0) {
                    stts = 0;
                } else if (matDispatch.getDispatchStatus() == 5) {
                    stts = 1;
                } else if (matDispatch.getDispatchStatus() == 7) {
                    stts = 2;
                } else if (matDispatch.getDispatchStatus() == 2) {
                    stts = 3;
                } else {
                    stts = 4;
                }

                ja.put("" + (this.start + i + 1));
                ja.put("<a id='btnResult' class='haha' href='add_produksi.jsp?hidden_dispatch_id=" + matDispatch.getOID() + "&command=" + Command.EDIT + "' data-oid =" + matDispatch.getOID() + ">" + matDispatch.getDispatchCode() + "</a>");
                ja.put("" + loc1.getName());
                ja.put("" + loc2.getName());
                ja.put("" + statusDocKey.get(stts));
                array.put(ja);
            } else if (datafor.equals("listeventSrc")) {
                matDispatch = (MatDispatch) listData.get(i);

                l1 = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + matDispatch.getLocationId(), null);
                loc1 = (Location) l1.get(0);
                l2 = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + matDispatch.getDispatchTo(), null);
                loc2 = (Location) l2.get(0);

                if (matDispatch.getDispatchStatus() == 0) {
                    stts = 0;
                } else if (matDispatch.getDispatchStatus() == 5) {
                    stts = 1;
                } else if (matDispatch.getDispatchStatus() == 7) {
                    stts = 2;
                } else if (matDispatch.getDispatchStatus() == 2) {
                    stts = 3;
                } else {
                    stts = 4;
                }

                ja.put("" + (this.start + i + 1));
                ja.put("<a id='btnResult' class='haha' href='add_harga_pokok_group.jsp?hidden_dispatch_id=" + matDispatch.getOID() + "&command=" + Command.EDIT + "' data-oid =" + matDispatch.getOID() + ">" + matDispatch.getDispatchCode() + "</a>");
                ja.put("" + loc1.getName());
                ja.put("" + loc2.getName());
                ja.put("" + statusDocKey.get(stts));
                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            //result.put("iUrlToGetPicture","imgupload/marketing/");
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
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

    private void commandSave(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void commandDelete(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void commandNone(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
