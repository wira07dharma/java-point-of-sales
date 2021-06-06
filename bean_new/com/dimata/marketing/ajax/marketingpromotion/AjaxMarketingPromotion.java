/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.ajax.marketingpromotion;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.marketing.entity.marketingpromotion.MarketingPromotion;
import com.dimata.marketing.entity.marketingpromotion.PstMarketingPromotion;
import com.dimata.marketing.entity.marketingpromotiondetail.MarketingPromotionDetail;
import com.dimata.marketing.entity.marketingpromotiondetail.PstMarketingPromotionDetail;
import com.dimata.marketing.entity.marketingpromotiondetailobject.MarketingPromotionDetailObject;
import com.dimata.marketing.entity.marketingpromotiondetailobject.PstMarketingPromotionDetailObject;
import com.dimata.marketing.entity.marketingpromotiondetailsubject.MarketingPromotionDetailSubject;
import com.dimata.marketing.entity.marketingpromotiondetailsubject.PstMarketingPromotionDetailSubject;
import com.dimata.marketing.entity.marketingpromotionlocation.MarketingPromotionLocation;
import com.dimata.marketing.entity.marketingpromotionlocation.PstMarketingPromotionLocation;
import com.dimata.marketing.entity.marketingpromotionmembertype.MarketingPromotionMemberType;
import com.dimata.marketing.entity.marketingpromotionmembertype.PstMarketingPromotionMemberType;
import com.dimata.marketing.entity.marketingpromotionpricetype.MarketingPromotionPriceType;
import com.dimata.marketing.entity.marketingpromotionpricetype.PstMarketingPromotionPriceType;
import com.dimata.marketing.entity.marketingpromotiontype.MarketingPromotionType;
import com.dimata.marketing.entity.marketingpromotiontype.PstMarketingPromotionType;
import com.dimata.marketing.form.marketingpromotion.CtrlMarketingPromotion;
import com.dimata.marketing.form.marketingpromotion.FrmMarketingPromotion;
import com.dimata.marketing.form.marketingpromotiondetail.CtrlMarketingPromotionDetail;
import com.dimata.marketing.form.marketingpromotiondetail.FrmMarketingPromotionDetail;
import com.dimata.marketing.form.marketingpromotiondetailobject.CtrlMarketingPromotionDetailObject;
import com.dimata.marketing.form.marketingpromotiondetailobject.FrmMarketingPromotionDetailObject;
import com.dimata.marketing.form.marketingpromotiondetailsubject.CtrlMarketingPromotionDetailSubject;
import com.dimata.marketing.form.marketingpromotiondetailsubject.FrmMarketingPromotionDetailSubject;
import com.dimata.marketing.form.marketingpromotionlocation.CtrlMarketingPromotionLocation;
import com.dimata.marketing.form.marketingpromotionlocation.FrmMarketingPromotionLocation;
import com.dimata.marketing.form.marketingpromotionmembertype.CtrlMarketingPromotionMemberType;
import com.dimata.marketing.form.marketingpromotionmembertype.FrmMarketingPromotionMemberType;
import com.dimata.marketing.form.marketingpromotionpricetype.CtrlMarketingPromotionPriceType;
import com.dimata.marketing.form.marketingpromotionpricetype.FrmMarketingPromotionPriceType;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MemberGroup;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMemberGroup;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author Witar
 */
public class AjaxMarketingPromotion
        extends HttpServlet {

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

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listevent")) {
            String[] cols = {
                " marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID],
                " marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_PURPOSE],
                " marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_OBJECTIVES],
                " marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_EVENT],
                " marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_START],
                " marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_END],
                " marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_RECURRING],
                " currency." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listsubject")) {
            String[] cols = {
                " subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID],
                " material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                " material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE],
                " material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                " kategori." + PstCategory.fieldNames[PstCategory.FLD_NAME],
                " subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_QUANTITY],
                " subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_TARGET_QUANTITY],
                " subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_VALID_FOR_MULTIPLICATION],
                " subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_SALES_PRICE],
                " subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_PURCHASE_PRICE],
                " subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_GROSS_PROFIT]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listobject")) {
            String[] cols = {
                " objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID],
                " material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                " material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE],
                " material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                " category." + PstCategory.fieldNames[PstCategory.FLD_NAME],
                " objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_QUANTITY],
                " promo." + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_MARKETING_PROMOTION_TYPE_ID],
                " objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_VALID_FOR_MULTIPLICATION],
                " objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_REGULAR_PRICE],
                " objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_PROMOTION_PRICE],
                " objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_COST]
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
                        + "(marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_PURPOSE] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_OBJECTIVES] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_EVENT] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_RECURRING] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_START] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_END] + " LIKE '%" + searchTerm + "%'"
                        + " OR currency." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        
                        + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR member." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_REASON_OF_PROMOTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_PROMOTION_TAGLINE] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            } else {
                whereClause += " "
                        + "(marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_PURPOSE] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_OBJECTIVES] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_EVENT] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_RECURRING] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_START] + " LIKE '%" + searchTerm + "%'"
                        + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_END] + " LIKE '%" + searchTerm + "%'"
                        + " OR currency." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        
                        + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR member." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_REASON_OF_PROMOTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_PROMOTION_TAGLINE] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            }
        } else if (dataFor.equals("listsubject")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND (subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR kategori." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_TARGET_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_SALES_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_PURCHASE_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_GROSS_PROFIT] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            } else {
                whereClause += " "
                        + "(subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR kategori." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_TARGET_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_SALES_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_PURCHASE_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_GROSS_PROFIT] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            }
        } else if (dataFor.equals("listobject")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND (objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR category." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                        + " OR promo." + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_REGULAR_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_PROMOTION_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_COST] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            } else {
                whereClause += " "
                        + "(objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR category." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                        + " OR promo." + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_REGULAR_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_PROMOTION_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_COST] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listevent")) {
            total = PstMarketingPromotion.getCountJoin(whereClause);
        } else if (dataFor.equals("listsubject")) {
            total = PstMarketingPromotionDetailSubject.getCountJoin(whereClause + " AND " + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = " + oidPromo);
        } else if (dataFor.equals("listobject")) {
            total = PstMarketingPromotionDetailObject.getCountJoin(whereClause + " AND " + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = " + oidPromo);
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
        MarketingPromotion marketingPromotion = new MarketingPromotion();
        MarketingPromotionDetailSubject promotionDetailSubject = new MarketingPromotionDetailSubject();
        MarketingPromotionDetailObject promotionDetailObject = new MarketingPromotionDetailObject();

        String whereClause = "";
        String order = "";

        if (this.searchTerm == null) {
            whereClause += "";
        } else {
            if (datafor.equals("listevent")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                            + "AND (marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_PURPOSE] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_OBJECTIVES] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_EVENT] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_RECURRING] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_END] + " LIKE '%" + searchTerm + "%'"
                            + " OR currency." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            
                            + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR member." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_REASON_OF_PROMOTION] + " LIKE '%" + searchTerm + "%'"
                            + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_PROMOTION_TAGLINE] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                } else {
                    whereClause += " "
                            + "(marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_PURPOSE] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_OBJECTIVES] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_EVENT] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_RECURRING] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_END] + " LIKE '%" + searchTerm + "%'"
                            + " OR currency." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            
                            + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR member." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_REASON_OF_PROMOTION] + " LIKE '%" + searchTerm + "%'"
                            + " OR marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_PROMOTION_TAGLINE] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                }
            } else if (datafor.equals("listsubject")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                            + "AND (subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR kategori." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE'%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_TARGET_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_SALES_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_PURCHASE_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_GROSS_PROFIT] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                } else {
                    whereClause += " "
                            + "(subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR kategori." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_TARGET_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_SALES_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_PURCHASE_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_GROSS_PROFIT] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                }
            } else if (datafor.equals("listobject")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                            + "AND (objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR category." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                            + " OR promo." + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_REGULAR_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_PROMOTION_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_COST] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                } else {
                    whereClause += " "
                            + "(objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + " LIKE '%" + searchTerm + "%' "
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                            + " OR material." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR category." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                            + " OR promo." + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_VALID_FOR_MULTIPLICATION] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_REGULAR_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_PROMOTION_PRICE] + " LIKE '%" + searchTerm + "%'"
                            + " OR objek." + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_COST] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                }
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listevent")) {
            listData = PstMarketingPromotion.listJoin(start, amount, whereClause, order);
        } else if (datafor.equals("listsubject")) {
            listData = PstMarketingPromotionDetailSubject.listSaveJoin(start, amount, whereClause + " AND " + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", order);
        } else if (datafor.equals("listobject")) {
            listData = PstMarketingPromotionDetailObject.listSaveJoin(start, amount, whereClause + " AND " + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listevent")) {
                marketingPromotion = (MarketingPromotion) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + Formater.formatDate(marketingPromotion.getMarketingPromotionPurpose(), "yyyy-MM-dd"));
                ja.put("" + marketingPromotion.getMarketingPromotionObjectives());
                ja.put("" + marketingPromotion.getMarketingPromotionEvent());
                ja.put("" + Formater.formatDate(marketingPromotion.getMarketingPromotionStart(), "yyyy-MM-dd kk:mm:ss"));
                ja.put("" + Formater.formatDate(marketingPromotion.getMarketingPromotionEnd(), "yyyy-MM-dd kk:mm:ss"));
                ja.put("" + marketingPromotion.getMarketingPromotionRecurring());
                ja.put("" + marketingPromotion.getCurrencyName() + " <b>(" + marketingPromotion.getCurrencyCode() + ")</b>");
                ja.put("<div class='text-center'>"
                        //+ "<a class='btn-detail' data-oid='" + marketingPromotion.getOID() + "' href='#'><i class='fa fa-eye'></i></a>"
                        //+ " | "
                        + "<button class='btn btn-box-tool btn-editevent' data-oid='" + marketingPromotion.getOID() + "' data-for='showform' data-command='" + Command.NONE + "' href='#'><i class='fa fa-pencil'></i></button>"
                        //+ "<i class='btn-box-tool'>  </i>"
                        + "<button class='btn btn-box-tool btn-deleteevent' data-oid='" + marketingPromotion.getOID() + "' data-for='deleteevent' data-command = '" + Command.DELETE + "' href='#'><i class='fa fa-remove'></i></button>"
                        + "</div>");
                array.put(ja);
            } else if (datafor.equals("listsubject")) {
                promotionDetailSubject = (MarketingPromotionDetailSubject) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + promotionDetailSubject.getItemSku());
                ja.put("" + promotionDetailSubject.getItemBarcode());
                ja.put("" + promotionDetailSubject.getItemName());
                ja.put("" + promotionDetailSubject.getItemCategory());
                ja.put("" + promotionDetailSubject.getQuantity());
                ja.put("" + promotionDetailSubject.getTargetQuantity());
                String valid = promotionDetailSubject.getValidForMultiplication();
                if (valid.equals("1")) {
                    valid = "Yes";
                } else if (valid.equals("0")) {
                    valid = "No";
                }
                ja.put("" + valid);
                ja.put("" + Formater.formatNumber(promotionDetailSubject.getSalesPrice(), "#.##"));
                ja.put("" + Formater.formatNumber(promotionDetailSubject.getPurchasePrice(), "#.##"));
                ja.put("" + Formater.formatNumber(promotionDetailSubject.getGrossProfit(), "#.##"));
                ja.put("<div class='text-center'>"
                        + "<button class='btn btn-box-tool btn-editsubject' data-oid='" + promotionDetailSubject.getOID() + "' data-oidpromo='" + promotionDetailSubject.getMarketingPromotionDetailId() + "' data-for='editsubject' data-command = '" + Command.NONE + "' href='#'><i class='fa fa-pencil'></i></button>"
                        //+ " | "
                        + "<button class='btn btn-box-tool btn-deletesubject' data-oid='" + promotionDetailSubject.getOID() + "' data-oidpromo='" + promotionDetailSubject.getMarketingPromotionDetailId() + "' data-for='deletesubject' data-command = '" + Command.DELETE + "' href='#'><i class='fa fa-remove'></i></button>"
                        + "</div>");
                array.put(ja);
            } else if (datafor.equals("listobject")) {
                promotionDetailObject = (MarketingPromotionDetailObject) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + promotionDetailObject.getItemSku());
                ja.put("" + promotionDetailObject.getItemBarcode());
                ja.put("" + promotionDetailObject.getItemName());
                ja.put("" + promotionDetailObject.getItemCategory());
                ja.put("" + promotionDetailObject.getQuantity());
                ja.put("" + promotionDetailObject.getPromoTypeName());
                String valid = promotionDetailObject.getValidForMultiplication();
                if (valid.equals("1")) {
                    valid = "Yes";
                } else if (valid.equals("0")) {
                    valid = "No";
                }
                ja.put("" + valid);
                ja.put("" + Formater.formatNumber(promotionDetailObject.getRegularPrice(), "#.##"));
                ja.put("" + Formater.formatNumber(promotionDetailObject.getPromotionPrice(), "#.##"));
                ja.put("" + Formater.formatNumber(promotionDetailObject.getCost(), "#.##"));
                ja.put("<div class='text-center'>"
                        + "<button class='btn btn-box-tool btn-editobject' data-oid='" + promotionDetailObject.getOID() + "' data-oidpromo='" + promotionDetailObject.getMarketingPromotionDetailId() + "' data-for='editobject' data-command = '" + Command.NONE + "' href='#'><i class='fa fa-pencil'></i></button>"
                        //+ " | "
                        + "<button class='btn btn-box-tool btn-deleteobject' data-oid='" + promotionDetailObject.getOID() + "' data-oidpromo='" + promotionDetailObject.getMarketingPromotionDetailId() + "' data-for='deleteobject' data-command = '" + Command.DELETE + "' href='#'><i class='fa fa-remove'></i></button>"
                        + "</div>");
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

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = showFormPromotion(request);
        } else if (this.dataFor.equals("showpromo")) {
            this.htmlReturn = showPromo(request, oid);
        } else if (this.dataFor.equals("showdetail")) {
            this.htmlReturn = showPromoDetail(request, oid, oidPromo);
        } else if (this.dataFor.equals("getlocation")) {
            getLocation(request);
        } else if (this.dataFor.equals("getmembertype")) {
            getMember(request);
        } else if (this.dataFor.equals("getpricetype")) {
            getPrice(request);
        } else if (this.dataFor.equals("getPromotionType")) {
            this.htmlReturn = addOptionPromo(request);
        } else if (this.dataFor.equals("editpromo")) {
            getDataPromo(request);
        } else if (this.dataFor.equals("getitemname")) {
            getItemName(request);
        } else if (this.dataFor.equals("getitembarcode")) {
            getItemBarcode(request);
        } else if (this.dataFor.equals("getitemsku")) {
            getItemSku(request);
        } else if (this.dataFor.equals("getitemby")) {
            getItemBy(request);
        } else if (this.dataFor.equals("editsubject")) {
            getItemSubject(request);
        } else if (this.dataFor.equals("editobject")) {
            getItemObject(request);
        } else if (this.dataFor.equals("getanalysis")) {
            getAnalysis(request);
        } else if (this.dataFor.equals("getrealization")) {
            getRealization(request);
        }else if (this.dataFor.equals("showformsearch")) {
            this.htmlReturn = showFormSearch(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("saveevent")) {
            this.htmlReturn = saveMarketingPromotion(request);
            if (!oidLocation.equals("")) {
                this.htmlReturn = saveMarProLocation(request);
            }
            if (!oidMember.equals("")) {
                this.htmlReturn = saveMarProMember(request);
            }
            if (!oidPrice.equals("")) {
                this.htmlReturn = saveMarProPrice(request);
            }
        } else if (this.dataFor.equals("savepromo")) {
            this.htmlReturn = savePromo(request);
            showPromo(request, oidPromoBaru);
        } else if (this.dataFor.equals("savesubject")) {
            this.htmlReturn = saveSubject(request);
        } else if (this.dataFor.equals("saveobject")) {
            this.htmlReturn = saveObject(request);
        } else if (this.dataFor.equals("approvepromo")) {
            this.htmlReturn = approvePromo(request);
        } else if (this.dataFor.equals("disapprovepromo")) {
            this.htmlReturn = disapprovePromo(request);
        }
    }

    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("deleteevent")) {
            this.htmlReturn = deleteMarketingPromotion(request);
        } else if (this.dataFor.equals("deletepromo")) {
            this.htmlReturn = deletePromotionDetail(request);
        } else if (this.dataFor.equals("deletesubject")) {
            this.htmlReturn = deleteItemSubject(request);
        } else if (this.dataFor.equals("deleteobject")) {
            this.htmlReturn = deleteItemObject(request);
        }
    }

    // SAVE PROMOTION
    public String saveMarketingPromotion(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotion ctrlMarketingPromotion = new CtrlMarketingPromotion(request);
        ctrlMarketingPromotion.action(iCommand, oid, oidDelete);
        MarketingPromotion marketingPromotion = new MarketingPromotion();
        marketingPromotion = ctrlMarketingPromotion.getMarketingPromotion();
        oidBaru = marketingPromotion.getOID();
        returnData = ctrlMarketingPromotion.getMessage();
        return returnData;
    }

    // SAVE MULTI LOCATION
    public String saveMarProLocation(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionLocation promotionLocation = new CtrlMarketingPromotionLocation(request);
        String location = oidLocation;
        if (location.equals("")) {
            location = "0";
        }
        String[] oidLoc = location.split(",");
        if (oid == 0) {
            for (int i = 0; i < oidLoc.length; i++) {
                long[] oidLong = new long[oidLoc.length];
                oidLong[i] = Long.parseLong(oidLoc[i]);
                promotionLocation.actionSave(iCommand, oid, oidDelete, oidBaru, oidLong[i]);
                returnData = promotionLocation.getMessage();
            }
        } else if (oid != 0) {
            deleteMarketingPromotionLocation(request, oid);
            for (int i = 0; i < oidLoc.length; i++) {
                long[] oidLong = new long[oidLoc.length];
                oidLong[i] = Long.parseLong(oidLoc[i]);
                promotionLocation.actionSave(iCommand, oid, oidDelete, oidBaru, oidLong[i]);
                returnData = promotionLocation.getMessage();
            }
        }
        return returnData;
    }

    // SAVE MULTI MEMBER TYPE
    public String saveMarProMember(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionMemberType promotionMemberType = new CtrlMarketingPromotionMemberType(request);
        String member = oidMember;
        if (member.equals("")) {
            member = "0";
        }
        String[] oidMember = member.split(",");
        if (oid == 0) {
            for (int i = 0; i < oidMember.length; i++) {
                long[] oidLong = new long[oidMember.length];
                oidLong[i] = Long.parseLong(oidMember[i]);
                promotionMemberType.actionSave(iCommand, oid, oidDelete, oidBaru, oidLong[i]);
                returnData = promotionMemberType.getMessage();
            }
        } else if (oid != 0) {
            deleteMarketingPromotionMember(request, oid);
            for (int i = 0; i < oidMember.length; i++) {
                long[] oidLong = new long[oidMember.length];
                oidLong[i] = Long.parseLong(oidMember[i]);
                promotionMemberType.actionSave(iCommand, oid, oidDelete, oidBaru, oidLong[i]);
                returnData = promotionMemberType.getMessage();
            }
        }
        return returnData;
    }

    // SAVE MULTI PRICE TYPE
    public String saveMarProPrice(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionPriceType promotionPriceType = new CtrlMarketingPromotionPriceType(request);
        String price = oidPrice;
        if (price.equals("")) {
            price = "0";
        }
        String[] oidPrice = price.split(",");
        if (oid == 0) {
            for (int i = 0; i < oidPrice.length; i++) {
                long[] oidLong = new long[oidPrice.length];
                oidLong[i] = Long.parseLong(oidPrice[i]);
                promotionPriceType.actionSave(iCommand, oid, oidDelete, oidBaru, oidLong[i]);
                returnData = promotionPriceType.getMessage();
            }
        } else if (oid != 0) {
            deleteMarketingPromotionPrice(request, oid);
            for (int i = 0; i < oidPrice.length; i++) {
                long[] oidLong = new long[oidPrice.length];
                oidLong[i] = Long.parseLong(oidPrice[i]);
                promotionPriceType.actionSave(iCommand, oid, oidDelete, oidBaru, oidLong[i]);
                returnData = promotionPriceType.getMessage();
            }
        }
        return returnData;
    }

    // SAVE PROMO DETAIL
    public String savePromo(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetail ctrlMarketingPromotionDetail = new CtrlMarketingPromotionDetail(request);
        ctrlMarketingPromotionDetail.action(iCommand, oid, oidDelete);
        MarketingPromotionDetail promotionDetail1 = new MarketingPromotionDetail();
        promotionDetail1 = ctrlMarketingPromotionDetail.getMarketingPromotionDetail();
        oidPromoBaru = promotionDetail1.getOID();
        returnData = ctrlMarketingPromotionDetail.getMessage();
        return returnData;
    }

    // SAVE SUBJECT
    public String saveSubject(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetailSubject promotionDetailSubject = new CtrlMarketingPromotionDetailSubject(request);
        promotionDetailSubject.action(iCommand, oid, oidDelete);
        returnData = promotionDetailSubject.getMessage();
        return returnData;
    }

    // SAVE OBJECT
    public String saveObject(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetailObject promotionDetailObject = new CtrlMarketingPromotionDetailObject(request);
        promotionDetailObject.action(iCommand, oid, oidDelete);
        returnData = promotionDetailObject.getMessage();
        return returnData;
    }

    // DELETE PROMOTION
    public String deleteMarketingPromotion(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotion ctrlMarketingPromotion = new CtrlMarketingPromotion(request);
        ctrlMarketingPromotion.action(iCommand, oid, oidDelete);
        deleteMarketingPromotionLocation(request, oid);
        deleteMarketingPromotionMember(request, oid);
        deleteMarketingPromotionPrice(request, oid);
        returnData = ctrlMarketingPromotion.getMessage();
        return returnData;
    }

    // DELETE PROMOTION LOCATION
    public String deleteMarketingPromotionLocation(HttpServletRequest request, long oid) {
        String returnData = "";
        CtrlMarketingPromotionLocation promotionLocation = new CtrlMarketingPromotionLocation(request);
        promotionLocation.actionDelete(6, oid, oidDelete);
        returnData = promotionLocation.getMessage();
        return returnData;
    }

    // DELETE PROMOTION MEMBER TYPE
    public String deleteMarketingPromotionMember(HttpServletRequest request, long oid) {
        String returnData = "";
        CtrlMarketingPromotionMemberType memberType = new CtrlMarketingPromotionMemberType(request);
        memberType.actionDelete(6, oid, oidDelete);
        returnData = memberType.getMessage();
        return returnData;
    }

    // DELETE PROMOTION PRICE TYPE
    public String deleteMarketingPromotionPrice(HttpServletRequest request, long oid) {
        String returnData = "";
        CtrlMarketingPromotionPriceType priceType = new CtrlMarketingPromotionPriceType(request);
        priceType.actionDelete(6, oid, oidDelete);
        returnData = priceType.getMessage();
        return returnData;
    }

    // DELETE PROMO DETAIL
    public String deletePromotionDetail(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetail promotionDetail = new CtrlMarketingPromotionDetail(request);
        promotionDetail.action(iCommand, oid, oidDelete);
        returnData = promotionDetail.getMessage();
        return returnData;
    }

    // DELETE SUBJECT
    public String deleteItemSubject(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetailSubject detailSubject = new CtrlMarketingPromotionDetailSubject(request);
        detailSubject.action(iCommand, oid, oidDelete);
        returnData = detailSubject.getMessage();
        return returnData;
    }

    // DELETE OBJECT
    public String deleteItemObject(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetailObject detailObject = new CtrlMarketingPromotionDetailObject(request);
        detailObject.action(iCommand, oid, oidDelete);
        returnData = detailObject.getMessage();
        return returnData;
    }

    // GET ALL DATA LOCATION FOR SELECT
    public void getLocation(HttpServletRequest request) {
        this.jSONArray = new JSONArray();
        String term = FRMQueryString.requestString(request, "term");
        Vector listItem = PstLocation.list(0, 0, "" + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + term + "%'", PstLocation.fieldNames[PstLocation.FLD_NAME] + " ASC ");
        for (int i = 0; i < listItem.size(); i++) {
            Location location = (Location) listItem.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("data_value", "" + location.getName());
                jSONObject.put("data_key", "" + location.getOID());
                this.jSONArray.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET ALL DATA MEMBER TYPE FOR SELECT
    public void getMember(HttpServletRequest request) {
        this.jSONArray = new JSONArray();
        String term = FRMQueryString.requestString(request, "term");
        Vector listItem = PstMemberGroup.list(0, 0, "" + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] + " LIKE '%" + term + "%'", PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] + " ASC ");
        for (int i = 0; i < listItem.size(); i++) {
            MemberGroup memberGroup = (MemberGroup) listItem.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("data_value", "" + memberGroup.getName());
                jSONObject.put("data_key", "" + memberGroup.getOID());
                this.jSONArray.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET ALL DATA PRICE TYPE FOR SELECT
    public void getPrice(HttpServletRequest request) {
        this.jSONArray = new JSONArray();
        String term = FRMQueryString.requestString(request, "term");
        Vector listItem = PstPriceType.list(0, 0, "" + PstPriceType.fieldNames[PstPriceType.FLD_NAME] + " LIKE '%" + term + "%'", PstPriceType.fieldNames[PstPriceType.FLD_NAME] + " ASC ");
        for (int i = 0; i < listItem.size(); i++) {
            PriceType priceType = (PriceType) listItem.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("data_value", "" + priceType.getName());
                jSONObject.put("data_key", "" + priceType.getOID());
                this.jSONArray.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET DATA PROMO DETAIL FOR EDIT DATA PROMO
    public void getDataPromo(HttpServletRequest request) {
        this.arrayPromo = new JSONArray();
        Vector listDataPromo = PstMarketingPromotionDetail.listJoin(0, 0, "" + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oid + "'", "");
        for (int i = 0; i < listDataPromo.size(); i++) {
            MarketingPromotionDetail detail = (MarketingPromotionDetail) listDataPromo.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("promo_type", "" + detail.getPromotionTypeId());
                jSONObject.put("promo_reason", "" + detail.getReasonOfPromotion());
                jSONObject.put("promo_tagline", "" + detail.getPromotionTagline());
                jSONObject.put("subject_combination", "" + detail.getSubjectCombination());
                jSONObject.put("object_combination", "" + detail.getObjectCombination());
                jSONObject.put("discount_quantity", "" + detail.getDiscountQuantityStatus());
                jSONObject.put("status_approve", "" + detail.getStatusApprove());
                this.arrayPromo.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET ALL DATA ITEM / MATERIAL BY ITEM NAME FOR SELECT ITEM
    public void getItemName(HttpServletRequest request) {
        this.jSONArray = new JSONArray();
        String term = FRMQueryString.requestString(request, "term");
        Vector listItem = PstMaterial.list(0, 0, "" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + term + "%' AND " + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != 4 AND LENGTH(" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ") > 2 ", PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " ASC ");
        for (int i = 0; i < listItem.size(); i++) {
            Material material = (Material) listItem.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("data_value", "" + material.getName());
                jSONObject.put("data_key", "" + material.getOID());
                this.jSONArray.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET ALL DATA ITEM / MATERIAL BY ITEM BARCODE FOR SELECT ITEM
    public void getItemBarcode(HttpServletRequest request) {
        this.jSONArray = new JSONArray();
        String term = FRMQueryString.requestString(request, "term");
        Vector listItem = PstMaterial.list(0, 0, "" + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + term + "%' AND " + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != 4 AND LENGTH(" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ") > 2 ", PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " ASC ");
        for (int i = 0; i < listItem.size(); i++) {
            Material material = (Material) listItem.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("data_value", "" + material.getBarCode());
                jSONObject.put("data_key", "" + material.getOID());
                this.jSONArray.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET ALL DATA ITEM / MATERIAL BY ITEM SKU FOR SELECT ITEM
    public void getItemSku(HttpServletRequest request) {
        this.jSONArray = new JSONArray();
        String term = FRMQueryString.requestString(request, "term");
        Vector listItem = PstMaterial.list(0, 0, "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + term + "%' AND " + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " != 4 AND LENGTH(" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ") > 2 ", PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " ASC ");
        for (int i = 0; i < listItem.size(); i++) {
            Material material = (Material) listItem.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("data_value", "" + material.getSku());
                jSONObject.put("data_key", "" + material.getOID());
                this.jSONArray.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET DETAIL DATA ITEM AFTER SELECT
    public void getItemBy(HttpServletRequest request) {
        this.arrayItem = new JSONArray();
        String termEvent = FRMQueryString.requestString(request, "termEvent");
        String termItem = FRMQueryString.requestString(request, "termItem");
        Vector listItem = null;
        if (this.dataFor2.equals("getitembysubject")) {
            listItem = PstMarketingPromotionDetailSubject.listGetJoin(0, 0, "material." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = '" + termItem + "' AND pricetype." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + termEvent + "'", "");
        } else if (this.dataFor2.equals("getitembyobject")) {
            listItem = PstMarketingPromotionDetailObject.listGetJoin(0, 0, "material." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = '" + termItem + "' AND pricetype." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + termEvent + "'", "");
        }
        for (int i = 0; i < listItem.size(); i++) {
            if (this.dataFor2.equals("getitembysubject")) {
                MarketingPromotionDetailSubject subject = (MarketingPromotionDetailSubject) listItem.get(i);
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("item_id", "" + subject.getItemId());
                    jSONObject.put("item_sku", "" + subject.getItemSku());
                    jSONObject.put("item_barcode", "" + subject.getItemBarcode());
                    jSONObject.put("item_name", "" + subject.getItemName());
                    jSONObject.put("item_category", "" + subject.getItemCategory());
                    jSONObject.put("item_sales", "" + Formater.formatNumber(subject.getSalesPrice(), "#.##"));
                    jSONObject.put("item_purchase", "" + Formater.formatNumber(subject.getPurchasePrice(), "#.##"));
                    double profit = subject.getSalesPrice() - subject.getPurchasePrice();
                    jSONObject.put("item_profit", "" + Formater.formatNumber(profit, "#.##"));
                    this.arrayItem.put(jSONObject);
                } catch (JSONException jSONException) {
                    jSONException.printStackTrace();
                }
            } else if (this.dataFor2.equals("getitembyobject")) {
                MarketingPromotionDetailObject object = (MarketingPromotionDetailObject) listItem.get(i);
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("item_id", "" + object.getItemId());
                    jSONObject.put("item_sku", "" + object.getItemSku());
                    jSONObject.put("item_barcode", "" + object.getItemBarcode());
                    jSONObject.put("item_name", "" + object.getItemName());
                    jSONObject.put("item_category", "" + object.getItemCategory());
                    jSONObject.put("item_regular", "" + Formater.formatNumber(object.getRegularPrice(), "#.##"));
                    jSONObject.put("item_cost", "" + Formater.formatNumber(object.getCost(), "#.##"));
                    this.arrayItem.put(jSONObject);
                } catch (JSONException jSONException) {
                    jSONException.printStackTrace();
                }
            }
        }
    }

    // GET DATA SUBJECT FOR EDIT ITEM
    public void getItemSubject(HttpServletRequest request) {
        this.arrayItem = new JSONArray();
        String term = FRMQueryString.requestString(request, "FRM_FIELD_OID");
        Vector listItem = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, "" + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " LIKE '" + term + "%'", PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " ASC ");
        for (int i = 0; i < listItem.size(); i++) {
            MarketingPromotionDetailSubject subject = (MarketingPromotionDetailSubject) listItem.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("item_id", "" + subject.getMaterialId());
                jSONObject.put("item_sku", "" + subject.getItemSku());
                jSONObject.put("item_barcode", "" + subject.getItemBarcode());
                jSONObject.put("item_name", "" + subject.getItemName());
                jSONObject.put("item_category", "" + subject.getItemCategory());
                jSONObject.put("item_quantity", "" + subject.getQuantity());
                jSONObject.put("item_target", "" + subject.getTargetQuantity());
                jSONObject.put("item_valid", "" + subject.getValidForMultiplication());
                jSONObject.put("item_sales", "" + Formater.formatNumber(subject.getSalesPrice(), "#.##"));
                jSONObject.put("item_purchase", "" + Formater.formatNumber(subject.getPurchasePrice(), "#.##"));
                jSONObject.put("item_profit", "" + Formater.formatNumber(subject.getGrossProfit(), "#.##"));
                this.arrayItem.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET DATA OBJECT FOR EDIT ITEM
    public void getItemObject(HttpServletRequest request) {
        this.arrayItem = new JSONArray();
        String term = FRMQueryString.requestString(request, "FRM_FIELD_OID");
        Vector listSubject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, "" + PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_OBJECT_ID] + " LIKE '%" + term + "%'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            MarketingPromotionDetailObject object = (MarketingPromotionDetailObject) listSubject.get(i);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("item_id", "" + object.getMaterialId());
                jSONObject.put("item_sku", "" + object.getItemSku());
                jSONObject.put("item_barcode", "" + object.getItemBarcode());
                jSONObject.put("item_name", "" + object.getItemName());
                jSONObject.put("item_category", "" + object.getItemCategory());
                jSONObject.put("item_quantity", "" + object.getQuantity());
                jSONObject.put("item_promotype", "" + object.getMarketingPromotionTypeId());
                jSONObject.put("item_valid", "" + object.getValidForMultiplication());
                jSONObject.put("item_regular", "" + Formater.formatNumber(object.getRegularPrice(), "#.##"));
                jSONObject.put("item_promotion", "" + Formater.formatNumber(object.getPromotionPrice(), "#.##"));
                jSONObject.put("item_cost", "" + Formater.formatNumber(object.getCost(), "#.##"));
                this.arrayItem.put(jSONObject);
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    // GET DATA COMBINATION FOR ANALYSIS
    public void getAnalysis(HttpServletRequest request) {
        MarketingPromotionDetail detail = new MarketingPromotionDetail();
        Vector listPromo = PstMarketingPromotionDetail.listJoin(0, 0, "" + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listPromo.size(); i++) {
            detail = (MarketingPromotionDetail) listPromo.get(i);
            String subComb = detail.getSubjectCombination();
            String obComb = detail.getObjectCombination();
            if (subComb.equals("1")) {
                if (obComb.equals("1")) {
                    this.htmlReturn = countAndAnd(request);
                } else if (obComb.equals("0")) {
                    this.htmlReturn = countAndOr(request);
                }
            } else if (subComb.equals("0")) {
                if (obComb.equals("1")) {
                    this.htmlReturn = countOrAnd(request);
                } else if (obComb.equals("0")) {
                    this.htmlReturn = countOrOr(request);
                }
            }
        }
    }
    
    // GET DATA COMBINATION FOR REALIZATION
    public void getRealization(HttpServletRequest request) {
        MarketingPromotionDetail detail = new MarketingPromotionDetail();
        Vector listPromo = PstMarketingPromotionDetail.listJoin(0, 0, "" + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listPromo.size(); i++) {
            detail = (MarketingPromotionDetail) listPromo.get(i);
            String subComb = detail.getSubjectCombination();
            String obComb = detail.getObjectCombination();
            if (subComb.equals("1")) {
                if (obComb.equals("1")) {
                    this.htmlReturn = getRealizationAndAnd(request);
                } else if (obComb.equals("0")) {
                    this.htmlReturn = getRealizationAndOr(request);
                }
            } else if (subComb.equals("0")) {
                if (obComb.equals("1")) {
                    this.htmlReturn = getRealizationOrAnd(request);
                } else if (obComb.equals("0")) {
                    this.htmlReturn = getRealizationOrOr(request);
                }
            }
        }
    }

    // COUNT ANALYSIS FOR COMBINATION AND / AND
    public String countAndAnd(HttpServletRequest request) {
        String data = "";

        // GET SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();

            data += "<tr>"
                    + "<td class='subjectname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='targetquantitysubject'>" + subjectTarget + "</td>";

            if (i > 0) {
                data += ""
                        + "<td class='objectname'>-</td>"
                        + "<td class='targetquantityobject'>-</td>"
                        + "<td class='profitnonpromo'>-</td>"
                        + "<td class='profitpromo'>-</td>"
                        + "<td class='valuepromo'>-</td>"
                        + "<td class='percentpromo'>-</td>"
                        + "<td class='totalprofitnonpromo'>-</td>"
                        + "<td class='totalmarginpromo'>-</td>"
                        + "<td class='totalvaluepromo'>-</td>"
                        + "</tr>";
            }

            // GET OBJECT DATA
            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            if (i < 1) {
                for (int j = 0; j < listObject.size(); j++) {
                    itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                    // SET OBJECT DATA
                    objectQuantity = itemObject.getQuantity();
                    objectTarget = subjectTarget / subjectQuantity * objectQuantity;
                    objectRegularPrice = itemObject.getRegularPrice();
                    objectCost = itemObject.getCost();
                    objectPromotionPrice = itemObject.getPromotionPrice();

                    // COUNT ANALYSIS
                    profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    valuePromo = profitNonPromo - profitPromo;
                    percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                    totalProfitNonPromo = subjectTarget * profitNonPromo;
                    totalMarginPromo = subjectTarget * profitPromo;
                    totalValuePromo = totalProfitNonPromo - totalMarginPromo;

                    data += "";
                    if (j > 0) {
                        data += "<tr>"
                                + "<td class='subjectname'>-</td>"
                                + "<td class='targetquantitysubject'>-</td>";
                    }
                    data += ""
                            + "<td class='objectname'>" + itemObject.getItemName() + "</td>"
                            + "<td class='targetquantityobject'>" + objectTarget + "</td>"
                            + "<td class='profitnonpromo'>" + Formater.formatNumber(profitNonPromo, "#.##") + "</td>"
                            + "<td class='profitpromo'>" + Formater.formatNumber(profitPromo, "#.##") + "</td>"
                            + "<td class='valuepromo'>" + Formater.formatNumber(valuePromo, "#.##") + "</td>"
                            + "<td class='percentpromo'>" + Formater.formatNumber(percentPromo, "#.##") + "</td>"
                            + "<td class='totalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                            + "<td class='totalmarginpromo'>" + Formater.formatNumber(totalMarginPromo, "#.##") + "</td>"
                            + "<td class='totalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                            + "</tr>";
                }
            }
        }
        return data;
    }

    // COUNT ANALYSIS FOR COMBINATION AND / OR
    public String countAndOr(HttpServletRequest request) {
        String data = "";

        // GET SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();

            data += "<tr>"
                    + "<td class='subjectname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='targetquantitysubject'>" + subjectTarget + "</td>";

            if (i > 0) {
                data += ""
                        + "<td class='objectname'>-</td>"
                        + "<td class='targetquantityobject'>-</td>"
                        + "<td class='profitnonpromo'>-</td>"
                        + "<td class='profitpromo'>-</td>"
                        + "<td class='valuepromo'>-</td>"
                        + "<td class='percentpromo'>-</td>"
                        + "<td class='totalprofitnonpromo'>-</td>"
                        + "<td class='totalmarginpromo'>-</td>"
                        + "<td class='totalvaluepromo'>-</td>"
                        + "</tr>";
            }

            // GET ALL OBJECT QUANTITY
            objectQuantityAll = 0;
            MarketingPromotionDetailObject itemObjectTarget = new MarketingPromotionDetailObject();
            Vector listObjectTarget = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObjectTarget.size(); j++) {
                itemObjectTarget = (MarketingPromotionDetailObject) listObjectTarget.get(j);
                objectQuantityAll += itemObjectTarget.getQuantity();
            }

            // GET OBJECT DATA
            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            if (i < 1) {
                for (int j = 0; j < listObject.size(); j++) {
                    itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                    // SET OBJECT DATA
                    objectQuantity = itemObject.getQuantity();
                    objectTarget = subjectTarget / subjectQuantity * objectQuantity / objectQuantityAll;
                    objectRegularPrice = itemObject.getRegularPrice();
                    objectCost = itemObject.getCost();
                    objectPromotionPrice = itemObject.getPromotionPrice();

                    // COUNT ANALYSIS
                    profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    valuePromo = profitNonPromo - profitPromo;
                    percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                    totalProfitNonPromo = subjectTarget * profitNonPromo;
                    totalMarginPromo = subjectTarget * profitPromo;
                    totalValuePromo = totalProfitNonPromo - totalMarginPromo;

                    data += "";
                    if (j > 0) {
                        data += "<tr>"
                                + "<td class='subjectname'>-</td>"
                                + "<td class='targetquantitysubject'>-</td>";
                    }
                    data += ""
                            + "<td class='objectname'>" + itemObject.getItemName() + "</td>"
                            + "<td class='targetquantityobject'> " + objectTarget + " </td>"
                            + "<td class='profitnonpromo'>" + Formater.formatNumber(profitNonPromo, "#.##") + "</td>"
                            + "<td class='profitpromo'>" + Formater.formatNumber(profitPromo, "#.##") + "</td>"
                            + "<td class='valuepromo'>" + Formater.formatNumber(valuePromo, "#.##") + "</td>"
                            + "<td class='percentpromo'>" + Formater.formatNumber(percentPromo, "#.##") + "</td>"
                            + "<td class='totalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                            + "<td class='totalmarginpromo'>" + Formater.formatNumber(totalMarginPromo, "#.##") + "</td>"
                            + "<td class='totalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                            + "</tr>";
                }
            }
        }
        return data;
    }

    // COUNT ANALYSIS FOR COMBINATION OR / AND
    public String countOrAnd(HttpServletRequest request) {
        String data = "";

        // GET ALL SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();

            data += "<tr>"
                    + "<td class='subjectname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='targetquantitysubject'>" + subjectTarget + "</td>";

            // GET ALL OBJECT DATA
            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObject.size(); j++) {
                itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                // SET OBJECT DATA
                objectQuantity = itemObject.getQuantity();
                objectTarget = subjectTarget / subjectQuantity * objectQuantity;
                objectRegularPrice = itemObject.getRegularPrice();
                objectCost = itemObject.getCost();
                objectPromotionPrice = itemObject.getPromotionPrice();

                // COUNT ANALYSIS
                profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                valuePromo = profitNonPromo - profitPromo;
                percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                totalProfitNonPromo = subjectTarget * profitNonPromo;
                totalMarginPromo = subjectTarget * profitPromo;
                totalValuePromo = totalProfitNonPromo - totalMarginPromo;

                data += "";
                if (j > 0) {
                    data += "<tr>"
                            + "<td class='subjectname'>-</td>"
                            + "<td class='targetquantitysubject'>-</td>";
                }
                data += ""
                        + "<td class='objectname'>" + itemObject.getItemName() + "</td>"
                        + "<td class='targetquantityobject'>" + objectTarget + "</td>"
                        + "<td class='profitnonpromo'>" + Formater.formatNumber(profitNonPromo, "#.##") + "</td>"
                        + "<td class='profitpromo'>" + Formater.formatNumber(profitPromo, "#.##") + "</td>"
                        + "<td class='valuepromo'>" + Formater.formatNumber(valuePromo, "#.##") + "</td>"
                        + "<td class='percentpromo'>" + Formater.formatNumber(percentPromo, "#.##") + "</td>"
                        + "<td class='totalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                        + "<td class='totalmarginpromo'>" + Formater.formatNumber(totalMarginPromo, "#.##") + "</td>"
                        + "<td class='totalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                        + "</tr>";
            }
        }
        return data;
    }

    // COUNT ANALYSIS FOR COMBINATION OR / OR
    public String countOrOr(HttpServletRequest request) {
        String data = "";

        // GET ALL SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();

            data += "<tr>"
                    + "<td class='subjectname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='targetquantitysubject'>" + subjectTarget + "</td>";

            // GET ALL OBJECT QUANTITY
            objectQuantityAll = 0;
            MarketingPromotionDetailObject itemObjectTarget = new MarketingPromotionDetailObject();
            Vector listObjectTarget = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObjectTarget.size(); j++) {
                itemObjectTarget = (MarketingPromotionDetailObject) listObjectTarget.get(j);
                objectQuantityAll += itemObjectTarget.getQuantity();
            }

            // GET ALL OBJECT DATA
            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObject.size(); j++) {
                itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                // SET OBJECT DATA
                objectQuantity = itemObject.getQuantity();
                objectTarget = subjectTarget / subjectQuantity * objectQuantity / objectQuantityAll;
                objectRegularPrice = itemObject.getRegularPrice();
                objectCost = itemObject.getCost();
                objectPromotionPrice = itemObject.getPromotionPrice();

                // COUNT ANALYSIS
                profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                valuePromo = profitNonPromo - profitPromo;
                percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                totalProfitNonPromo = subjectTarget * profitNonPromo;
                totalMarginPromo = subjectTarget * profitPromo;
                totalValuePromo = totalProfitNonPromo - totalMarginPromo;

                data += "";
                if (j > 0) {
                    data += "<tr>"
                            + "<td class='subjectname'>-</td>"
                            + "<td class='targetquantitysubject'>-</td>";
                }
                data += ""
                        + "<td class='objectname'>" + itemObject.getItemName() + "</td>"
                        + "<td class='targetquantityobject'> " + objectTarget + " </td>"
                        + "<td class='profitnonpromo'>" + Formater.formatNumber(profitNonPromo, "#.##") + "</td>"
                        + "<td class='profitpromo'>" + Formater.formatNumber(profitPromo, "#.##") + "</td>"
                        + "<td class='valuepromo'>" + Formater.formatNumber(valuePromo, "#.##") + "</td>"
                        + "<td class='percentpromo'>" + Formater.formatNumber(percentPromo, "#.##") + "</td>"
                        + "<td class='totalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                        + "<td class='totalmarginpromo'>" + Formater.formatNumber(totalMarginPromo, "#.##") + "</td>"
                        + "<td class='totalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                        + "</tr>";
            }
        }
        return data;
    }

    public String getRealizationAndAnd(HttpServletRequest request) {
        String data = "";
        int subjectQuantityTunai = 0;
        int subjectQuantityReturnTunai = 0;
        int subjectItemQuantity = 0;                

        // GET SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            subjectQuantityTunai = PstBillDetail.getCountRealizationTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectQuantityReturnTunai = PstBillDetail.getCountRealizationReturnTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectItemQuantity = subjectQuantityTunai - subjectQuantityReturnTunai;
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            //subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();
            
            data += "<tr>"
                    + "<td class='realsubjekitemname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='realsubjekitemquantity'>" + subjectItemQuantity + "</td>";

            if (i > 0) {
                data += ""
                        + "<td class='realobjekitemname'>-</td>"
                        + "<td class='realobjekitemquantity'>-</td>"
                        + "<td class='realtotalprofitnonpromo'>-</td>"
                        + "<td class='realtotalprofitpromo'>-</td>"
                        + "<td class='realtotalvaluepromo'>-</td>"
                        + "</tr>";
            }

            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            if (i < 1) {
                for (int j = 0; j < listObject.size(); j++) {
                    itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                    // SET OBJECT DATA
                    objectQuantity = itemObject.getQuantity();
                    //objectTarget = subjectTarget / subjectQuantity * objectQuantity;
                    objectRegularPrice = itemObject.getRegularPrice();
                    objectCost = itemObject.getCost();
                    objectPromotionPrice = itemObject.getPromotionPrice();

                    // COUNT ANALYSIS
                    profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    //valuePromo = profitNonPromo - profitPromo;
                    //percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                    //totalProfitNonPromo = subjectTarget * profitNonPromo;
                    //totalMarginPromo = subjectTarget * profitPromo;
                    //totalValuePromo = totalProfitNonPromo - totalMarginPromo;
                    
                    double objectItemQuantity = subjectItemQuantity * objectQuantity / subjectQuantity;
                    double totalProfitNonPromo = subjectItemQuantity * profitNonPromo;
                    double totalPromoProfit = subjectItemQuantity * profitPromo;
                    double totalValuePromo = totalProfitNonPromo - totalPromoProfit;

                    data += "";
                    if (j > 0) {
                        data += "<tr>"
                                + "<td class='realsubjekitemname'>-</td>"
                                + "<td class='realsubjekitemquantity'>-</td>";
                    }
                    data += ""
                            + "<td class='realobjekitemname'>" + itemObject.getItemName() + "</td>"
                            + "<td class='realobjekitemquantity'>" + Formater.formatNumber(objectItemQuantity, "#.##") + "</td>"
                            + "<td class='realtotalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                            + "<td class='realtotalprofitpromo'>" + Formater.formatNumber(totalPromoProfit, "#.##") + "</td>"
                            + "<td class='realtotalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                            + "</tr>";
                }
            }
        }
        return data;
    }
    
    public String getRealizationAndOr(HttpServletRequest request) {
        String data = "";
        int subjectQuantityTunai = 0;
        int subjectQuantityReturnTunai = 0;
        int subjectItemQuantity = 0;
        
        // GET SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            subjectQuantityTunai = PstBillDetail.getCountRealizationTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectQuantityReturnTunai = PstBillDetail.getCountRealizationReturnTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectItemQuantity = subjectQuantityTunai - subjectQuantityReturnTunai;
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            //subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();

            data += "<tr>"
                    + "<td class='realsubjekitemname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='realsubjekitemquantity'>" + subjectItemQuantity + "</td>";

            if (i > 0) {
                data += ""
                        + "<td class='realobjekitemname'>-</td>"
                        + "<td class='realobjekitemquantity'>-</td>"
                        + "<td class='realtotalprofitnonpromo'>-</td>"
                        + "<td class='realtotalprofitpromo'>-</td>"
                        + "<td class='realtotalvaluepromo'>-</td>"
                        + "</tr>";
            }

            // GET ALL OBJECT QUANTITY
            objectQuantityAll = 0;
            MarketingPromotionDetailObject itemObjectTarget = new MarketingPromotionDetailObject();
            Vector listObjectTarget = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObjectTarget.size(); j++) {
                itemObjectTarget = (MarketingPromotionDetailObject) listObjectTarget.get(j);
                objectQuantityAll += itemObjectTarget.getQuantity();
            }

            // GET OBJECT DATA
            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            if (i < 1) {
                for (int j = 0; j < listObject.size(); j++) {
                    itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                    // SET OBJECT DATA
                    objectQuantity = itemObject.getQuantity();
                    //objectTarget = subjectTarget / subjectQuantity * objectQuantity / objectQuantityAll;
                    objectRegularPrice = itemObject.getRegularPrice();
                    objectCost = itemObject.getCost();
                    objectPromotionPrice = itemObject.getPromotionPrice();

                    // COUNT ANALYSIS
                    profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                    //valuePromo = profitNonPromo - profitPromo;
                    //percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                    //totalProfitNonPromo = subjectTarget * profitNonPromo;
                    //totalMarginPromo = subjectTarget * profitPromo;
                    //totalValuePromo = totalProfitNonPromo - totalMarginPromo;

                    double objectItemQuantity = subjectItemQuantity * objectQuantity / subjectQuantity;
                    double totalProfitNonPromo = subjectItemQuantity * profitNonPromo;
                    double totalPromoProfit = subjectItemQuantity * profitPromo;
                    double totalValuePromo = totalProfitNonPromo - totalPromoProfit;

                    data += "";
                    if (j > 0) {
                        data += "<tr>"
                                + "<td class='realsubjekitemname'>-</td>"
                                + "<td class='realsubjekitemquantity'>-</td>";
                    }
                    data += ""
                            + "<td class='realobjekitemname'>" + itemObject.getItemName() + "</td>"
                            + "<td class='realobjekitemquantity'>" + Formater.formatNumber(objectItemQuantity, "#.##") + "</td>"
                            + "<td class='realtotalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                            + "<td class='realtotalprofitpromo'>" + Formater.formatNumber(totalPromoProfit, "#.##") + "</td>"
                            + "<td class='realtotalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                            + "</tr>";
                }
            }
        }
        return data;
    }
    
    public String getRealizationOrAnd(HttpServletRequest request) {
        String data = "";
        int subjectQuantityTunai = 0;
        int subjectQuantityReturnTunai = 0;
        int subjectItemQuantity = 0;
        
        // GET ALL SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            subjectQuantityTunai = PstBillDetail.getCountRealizationTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectQuantityReturnTunai = PstBillDetail.getCountRealizationReturnTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectItemQuantity = subjectQuantityTunai - subjectQuantityReturnTunai;
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            //subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();

            data += "<tr>"
                    + "<td class='realsubjekitemname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='realsubjekitemquantity'>" + subjectItemQuantity + "</td>";

            // GET ALL OBJECT DATA
            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObject.size(); j++) {
                itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                // SET OBJECT DATA
                objectQuantity = itemObject.getQuantity();
                //objectTarget = subjectTarget / subjectQuantity * objectQuantity;
                objectRegularPrice = itemObject.getRegularPrice();
                objectCost = itemObject.getCost();
                objectPromotionPrice = itemObject.getPromotionPrice();

                // COUNT ANALYSIS
                profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                //valuePromo = profitNonPromo - profitPromo;
                //percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                //totalProfitNonPromo = subjectTarget * profitNonPromo;
                //totalMarginPromo = subjectTarget * profitPromo;
                //totalValuePromo = totalProfitNonPromo - totalMarginPromo;
                
                double objectItemQuantity = subjectItemQuantity * objectQuantity / subjectQuantity;
                double totalProfitNonPromo = subjectItemQuantity * profitNonPromo;
                double totalPromoProfit = subjectItemQuantity * profitPromo;
                double totalValuePromo = totalProfitNonPromo - totalPromoProfit;
                    
                data += "";
                if (j > 0) {
                    data += "<tr>"
                            + "<td class='realsubjekitemname'>-</td>"
                            + "<td class='realsubjekitemquantity'>-</td>";
                }
                data += ""
                        + "<td class='realobjekitemname'>" + itemObject.getItemName() + "</td>"
                        + "<td class='realobjekitemquantity'>" + Formater.formatNumber(objectItemQuantity, "#.##") + "</td>"
                        + "<td class='realtotalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                        + "<td class='realtotalprofitpromo'>" + Formater.formatNumber(totalPromoProfit, "#.##") + "</td>"
                        + "<td class='realtotalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                        + "</tr>";
            }
        }
        return data;
    }
    
    public String getRealizationOrOr(HttpServletRequest request) {
        String data = "";
        int subjectQuantityTunai = 0;
        int subjectQuantityReturnTunai = 0;
        int subjectItemQuantity = 0;
        
        // GET ALL SUBJECT DATA
        MarketingPromotionDetailSubject itemSubject = new MarketingPromotionDetailSubject();
        Vector listSubject = PstMarketingPromotionDetailSubject.listSaveJoin(0, 0, PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
        for (int i = 0; i < listSubject.size(); i++) {
            itemSubject = (MarketingPromotionDetailSubject) listSubject.get(i);
            subjectQuantityTunai = PstBillDetail.getCountRealizationTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectQuantityReturnTunai = PstBillDetail.getCountRealizationReturnTunai("marpro." + PstMarketingPromotion.fieldNames[PstMarketingPromotion.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'"
                    + " AND marde." + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'"
                    + " AND subjek." + PstMarketingPromotionDetailSubject.fieldNames[PstMarketingPromotionDetailSubject.FLD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID] + " = '" + itemSubject.getOID() + "'");
            subjectItemQuantity = subjectQuantityTunai - subjectQuantityReturnTunai;
            // SET SUBJECT DATA
            subjectQuantity = itemSubject.getQuantity();
            //subjectTarget = itemSubject.getTargetQuantity();
            subjectSalesPrice = itemSubject.getSalesPrice();
            subjectPurchasePrice = itemSubject.getPurchasePrice();

            data += "<tr>"
                    + "<td class='realsubjekitemname'>" + itemSubject.getItemName() + "</td>"
                    + "<td class='realsubjekitemquantity'>" + subjectItemQuantity + "</td>";

            // GET ALL OBJECT QUANTITY
            objectQuantityAll = 0;
            MarketingPromotionDetailObject itemObjectTarget = new MarketingPromotionDetailObject();
            Vector listObjectTarget = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObjectTarget.size(); j++) {
                itemObjectTarget = (MarketingPromotionDetailObject) listObjectTarget.get(j);
                objectQuantityAll += itemObjectTarget.getQuantity();
            }

            // GET ALL OBJECT DATA
            MarketingPromotionDetailObject itemObject = new MarketingPromotionDetailObject();
            Vector listObject = PstMarketingPromotionDetailObject.listSaveJoin(0, 0, PstMarketingPromotionDetailObject.fieldNames[PstMarketingPromotionDetailObject.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromo + "'", "");
            for (int j = 0; j < listObject.size(); j++) {
                itemObject = (MarketingPromotionDetailObject) listObject.get(j);
                // SET OBJECT DATA
                objectQuantity = itemObject.getQuantity();
                //objectTarget = subjectTarget / subjectQuantity * objectQuantity / objectQuantityAll;
                objectRegularPrice = itemObject.getRegularPrice();
                objectCost = itemObject.getCost();
                objectPromotionPrice = itemObject.getPromotionPrice();

                // COUNT ANALYSIS
                profitNonPromo = objectQuantity * (objectRegularPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                profitPromo = objectQuantity * (objectPromotionPrice - objectCost) + subjectQuantity * (subjectSalesPrice - subjectPurchasePrice);
                //valuePromo = profitNonPromo - profitPromo;
                //percentPromo = valuePromo / (objectQuantity * objectRegularPrice);
                //totalProfitNonPromo = subjectTarget * profitNonPromo;
                //totalMarginPromo = subjectTarget * profitPromo;
                //totalValuePromo = totalProfitNonPromo - totalMarginPromo;
                
                double objectItemQuantity = subjectItemQuantity * objectQuantity / subjectQuantity;
                double totalProfitNonPromo = subjectItemQuantity * profitNonPromo;
                double totalPromoProfit = subjectItemQuantity * profitPromo;
                double totalValuePromo = totalProfitNonPromo - totalPromoProfit;
                
                data += "";
                if (j > 0) {
                    data += "<tr>"
                            + "<td class='realsubjekitemname'>-</td>"
                            + "<td class='realsubjekitemquantity'>-</td>";
                }
                data += ""
                        + "<td class='realobjekitemname'>" + itemObject.getItemName() + "</td>"
                        + "<td class='realobjekitemquantity'>" + Formater.formatNumber(objectItemQuantity, "#.##") + "</td>"
                        + "<td class='realtotalprofitnonpromo'>" + Formater.formatNumber(totalProfitNonPromo, "#.##") + "</td>"
                        + "<td class='realtotalprofitpromo'>" + Formater.formatNumber(totalPromoProfit, "#.##") + "</td>"
                        + "<td class='realtotalvaluepromo'>" + Formater.formatNumber(totalValuePromo, "#.##") + "</td>"
                        + "</tr>";
            }
        }
        return data;
    }
    
    // ADD DATA OPTION FOR SELECT PROMOTION TYPE
    public String addOptionPromo(HttpServletRequest request) {
        String optionPromo = "";
        MarketingPromotionType promotionType = new MarketingPromotionType();
        Vector listPromo = PstMarketingPromotionType.list(0, 0, "" + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE] + " LIKE '1'", "");
        for (int i = 0; i < listPromo.size(); i++) {
            promotionType = (MarketingPromotionType) listPromo.get(i);
            optionPromo += "<option value='" + promotionType.getOID() + "'>" + promotionType.getPromotionTypeName() + "</option>";
        }
        return optionPromo;
    }

    // SET STATUS APPROVE PROMO TO APPROVED
    public String approvePromo(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetail detail = new CtrlMarketingPromotionDetail(request);
        detail.actionApprove(iCommand, oid, "1");
        MarketingPromotionDetail promotionDetail = new MarketingPromotionDetail();
        promotionDetail = detail.getMarketingPromotionDetail();
        oidReturn = promotionDetail.getStatusApprove();
        returnData = detail.getMessage();
        return returnData;
    }

    // SET STATUS APPROVE PROMO TO DISAPPROVED
    public String disapprovePromo(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionDetail detail = new CtrlMarketingPromotionDetail(request);
        detail.actionApprove(iCommand, oid, "0");
        MarketingPromotionDetail promotionDetail = new MarketingPromotionDetail();
        promotionDetail = detail.getMarketingPromotionDetail();
        oidReturn = promotionDetail.getStatusApprove();
        returnData = detail.getMessage();
        return returnData;
    }        
    
    // SHOW MODAL FORM PROMOTION
    public String showFormPromotion(HttpServletRequest request) {
        MarketingPromotion marketingPromotion = new MarketingPromotion();
        if (oid != 0) {
            try {
                marketingPromotion = PstMarketingPromotion.fetchExc(oid);
                // GET MULTI DATA LOCATION TO SHOW IN FORM EDIT
                Vector listDataLocation = PstMarketingPromotionLocation.listJoin(0, 0, "" + PstMarketingPromotionLocation.fieldNames[PstMarketingPromotionLocation.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'", "");
                for (int i = 0; i < listDataLocation.size(); i++) {
                    MarketingPromotionLocation location = (MarketingPromotionLocation) listDataLocation.get(i);
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("location_id", "" + location.getLocationId());
                        jSONObject.put("location_name", "" + location.getLocationName());
                        this.jSONArray.put(jSONObject);
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                    }
                }
                // GET MULTI DATA MEMBER TYPE TO SHOW IN FORM EDIT
                Vector listDataMember = PstMarketingPromotionMemberType.listJoin(0, 0, "" + PstMarketingPromotionMemberType.fieldNames[PstMarketingPromotionMemberType.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'", "");
                for (int i = 0; i < listDataMember.size(); i++) {
                    MarketingPromotionMemberType memberType = (MarketingPromotionMemberType) listDataMember.get(i);
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("member_id", "" + memberType.getMemberTypeId());
                        jSONObject.put("member_name", "" + memberType.getMemberTypeName());
                        this.jSONArray.put(jSONObject);
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                    }
                }
                // GET MULTI DATA PRICE TYPE TO SHOW IN FORM EDIT
                Vector listDataPrice = PstMarketingPromotionPriceType.listJoin(0, 0, "" + PstMarketingPromotionPriceType.fieldNames[PstMarketingPromotionPriceType.FLD_MARKETING_PROMOTION_ID] + " = '" + oid + "'", "");
                for (int i = 0; i < listDataPrice.size(); i++) {
                    MarketingPromotionPriceType priceType = (MarketingPromotionPriceType) listDataPrice.get(i);
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("price_id", "" + priceType.getPriceTypeId());
                        jSONObject.put("price_name", "" + priceType.getPriceTypeName());
                        this.jSONArray.put(jSONObject);
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                    }
                }
            } catch (Exception e) {
            }
        }

        String datePurpose = "";
        String dateStart = "";
        String dateEnd = "";
        if (oid == 0) {
            DateFormat purposeFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            Date date = new Date();
            datePurpose = purposeFormat.format(date);
            dateStart = dateFormat.format(date);
            dateEnd = dateFormat.format(date);
        } else {
            datePurpose = marketingPromotion.getMarketingPromotionPurpose().toString();
            dateStart = Formater.formatDate(marketingPromotion.getMarketingPromotionStart(), "yyyy-MM-dd kk:mm:ss");
            dateEnd = Formater.formatDate(marketingPromotion.getMarketingPromotionEnd(), "yyyy-MM-dd kk:mm:ss");
        }

        StandartRate standartRate = new StandartRate();
        Vector valStandartRate = new Vector(1, 1);
        Vector keyStandartRate = new Vector(1, 1);
        Vector listStandartRate = PstStandartRate.listJoinCurrency(0, 0, "", "");

        for (int i = 0; i < listStandartRate.size(); i++) {
            standartRate = (StandartRate) listStandartRate.get(i);
            valStandartRate.add("" + standartRate.getOID());
            keyStandartRate.add("" + standartRate.getCurrencyName());
        }

        MarketingPromotionType promotionType = new MarketingPromotionType();
        Vector valPromoType = new Vector(1, 1);
        Vector keyPromoType = new Vector(1, 1);
        Vector listPromoType = PstMarketingPromotionType.list(0, 0, "" + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE] + " LIKE '1'", "");

        for (int i = 0; i < listPromoType.size(); i++) {
            promotionType = (MarketingPromotionType) listPromoType.get(i);
            valPromoType.add("" + promotionType.getOID() + "");
            keyPromoType.add(promotionType.getPromotionTypeName());
        }

        //Location location = new Location();

        String returnData = ""
                + "<form id='formevent' enctype='multipart/form-data'>"
                    //+ "<input type='hidden' id='oidevent' name='"+FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_ID]+"' value='"+marketingPromotion.getOID()+"'>"
                    + "<div class='row'>"
                
                        + "<div class='col-sm-2'>"
                            + "<div class='form-group'>"
                                + "<label>Purposed on : </label>"
                                + "<input type='text' required class='form-control input-sm dates' placeholder='Entry purpose date' name='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_PURPOSE] + "' id='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_PURPOSE] + "' value='" + datePurpose + "'>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-2'>"
                            + "<div class='form-group'>"
                                + "<label>Objectives : </label>"
                                + "<input type='text' required class='form-control input-sm' placeholder='Entry objective' name='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_OBJECTIVES] + "' id='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_OBJECTIVES] + "' value='" + marketingPromotion.getMarketingPromotionObjectives() + "'>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-2'>"
                            + "<div class='form-group'>"
                                + "<label>Event : </label>"
                                + "<input type='text' required class='form-control input-sm' placeholder='Entry promo event' name='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_EVENT] + "' id='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_EVENT] + "' value='" + marketingPromotion.getMarketingPromotionEvent() + "'>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-2'>"
                            + "<div class='form-group'>"
                                + "<label>Recurring : </label>"
                                + " <input type='text' required class='form-control input-sm' rows='5' placeholder='Entry recurring' name='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_RECURRING] + "' id='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_RECURRING] + "' value='" + marketingPromotion.getMarketingPromotionRecurring() + "'>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-2'>"
                            + "<div class='form-group'>"
                                + "<label>Start on : </label>"
                                + "<input type='text' required id='datestart' class='form-control input-sm dates' placeholder='Entry start date' name='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_START] + "' id='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_START] + "' value='" + dateStart + "'>"
                                + "<span class='add-on'><i class='icon-calendar'></i></span>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-2'>"
                            + "<div class='form-group'>"
                                + "<label>End on : </label>"
                                + "<input type='text' required id='dateend' class='form-control input-sm dates' placeholder='Entry end date' name='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_END] + "' id='" + FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_END] + "' value='" + dateEnd + "'>"
                                + "<span class='add-on'><i class='icon-calendar'></i></span>"
                            + "</div>"
                        + "</div>"
                
                        //------------------------------------------------------

                        + "<div class='col-sm-3'>"
                            + "<div class='form-group'>"
                                + "<label>Location : </label>"
                                + "<select required name='" + FrmMarketingPromotionLocation.fieldNames[FrmMarketingPromotionLocation.FRM_FIELD_LOCATION_ID] + "' id='" + FrmMarketingPromotionLocation.fieldNames[FrmMarketingPromotionLocation.FRM_FIELD_LOCATION_ID] + "' class='selectlocation form-control input-sm' multiple='multiple'></select>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-3'>"
                            + "<div class='form-group'>"
                                + "<label>Member Type : </label>"
                                + "<select required name='" + FrmMarketingPromotionMemberType.fieldNames[FrmMarketingPromotionMemberType.FRM_FIELD_MEMBER_TYPE_ID] + "' id='" + FrmMarketingPromotionMemberType.fieldNames[FrmMarketingPromotionMemberType.FRM_FIELD_MEMBER_TYPE_ID] + "' class='selectmember form-control input-sm' multiple='multiple'></select>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-3'>"
                            + "<div class='form-group'>"
                                + "<label>Price Type : </label>"
                                + "<select required name='" + FrmMarketingPromotionPriceType.fieldNames[FrmMarketingPromotionPriceType.FRM_FIELD_PRICE_TYPE_ID] + "' id='" + FrmMarketingPromotionPriceType.fieldNames[FrmMarketingPromotionPriceType.FRM_FIELD_PRICE_TYPE_ID] + "' class='selectprice form-control input-sm' multiple='multiple'></select>"
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-2' style='padding-right:30px'>"
                            + "<div class='form-group'>"
                                + "<label>Currency : </label>"
                                + ControlCombo.draw(FrmMarketingPromotion.fieldNames[FrmMarketingPromotion.FRM_FIELD_MARKETING_PROMOTION_STANDARD_RATE_ID], null, ""+marketingPromotion.getMarketingPromotionStandardRateId(), valStandartRate, keyStandartRate, "", "form-control input-sm combocurrency") + ""
                            + "</div>"
                        + "</div>"
                        + "<div class='col-sm-1' style='padding-left:0px'>"
                            + "<div class='form-group'>"
                                + "<label>Duration : </label>"
                                //+ "<input type='text' readonly style='border:0;background-color:transparent' id='duration' class='form-control input-sm' placeholder='Duration' value=''>"
                                + "<h6 id='duration'></h6>"
                            + "</div>"
                        + "</div>"
                        //------------------------------------------------------
                        + "<div class='col-sm-12'>"
                            + "<div class=''>"
                                + "<button type='submit' class='btn btn-primary btn-sm' id='btn-saveevent' data-for='saveevent' data-command = '" + Command.SAVE + "'>Save Event</button>\t";
                                //if(marketingPromotion.getOID() != 0){
                                //returnData += "<button type='button' class='btn btn-primary btn-sm hidden' id='btn-addpromo'><i class='fa fa-plus'></i> New Promotion</button>";
                                //}
//                                int jumlahData = PstMarketingPromotionDetail.getCount("" + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_ID] + " LIKE '" + marketingPromotion.getOID() + "'");
//                                if (jumlahData > 0) {
//                                    returnData += "";
//                                            
//                                }
                                returnData += ""
                                + "<div class='pull-right allpromo hidden'>"
                                    + "<a id='expandAll' href='#' class='btn btn-default btn-sm' role='button'>Show All</a>\t"
                                    + "<a id='collapseAll' href='#' class='btn btn-default btn-sm' role='button'>Hide All</a>"
                                + "</div>"
                            + "</div>"
                        + "</div>"
                    + "</div>"
                + "</form>"
                                        
                // INPUT TAGLINE -----------------------------------------------
                + "<hr class='hidden'>"
                + "<form id='formpromo' enctype='multipart/form-data'>"
                    + "<div class='hidden' id='showaddpromo'>"
                        + "<input type='hidden' id='oidbaru' name='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_MARKETING_PROMOTION_ID] + "' value='"+marketingPromotion.getOID()+"'>"
                        + "<div class='row'>"
                                                                                              
                            + "<div class='col-sm-2'>"
                                + "<div class='form-group'>"
                                    + "<label>Promotion Type : </label>"
                                    //+ "<select class='form-control input-sm combopromo'>"
                                    //+ "</select>"
                                    + ControlCombo.draw(FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_PROMOTION_TYPE_ID], null, null, valPromoType, keyPromoType, "", "form-control input-sm combopromo") + ""
                                    + "<div style='margin-top:5px'>"
                                        + "<button type='button' class='btn btn-box-tool btn-editpromotype btn-sm' data-oid='0' data-for='showformpromotype' data-command='" + Command.NONE + "'><i class='fa fa-pencil'></i></button>"
                                    + "\t"
                                        + "<button type='button' class='btn btn-box-tool btn-addpromotype btn-sm' data-oid='0' data-for='showformpromotype' data-command='" + Command.NONE + "'><i class='fa fa-plus'></i></button>"
                                    + "</div>"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-2'>"
                                + "<div class='form-group'>"
                                    + "<label>Reason of Promotion : </label>"
                                    + "<input type='text' required class='form-control input-sm reasonpromo' placeholder='Entry promo reason' name='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_REASON_OF_PROMOTION] + "' id='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_REASON_OF_PROMOTION] + "' value=''>"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-2'>"
                                + "<div class='form-group'>"
                                    + "<label>Promotion Tagline : </label>"
                                    + "<input type='text' required class='form-control input-sm taglinepromo' placeholder='Entry promo tagline' name='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_PROMOTION_TAGLINE] + "' id='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_PROMOTION_TAGLINE] + "' value=''>"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-2'>"
                                + "<div class='form-group'>"
                                    + "<label>Subject Combination : </label>"
                                    + "<select class='form-control input-sm combosubjectcomb' name='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_SUBJECT_COMBINATION] + "' id='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_SUBJECT_COMBINATION] + "'>"
                                        + "<option value='1'>AND</option>"
                                        + "<option value='0'>OR</option>"
                                    + "</select>"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-2'>"
                                + "<div class='form-group'>"
                                    + "<label>Object Combination : </label>"
                                    + "<select class='form-control input-sm comboobjectcomb' name='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_OBJECT_COMBINATION] + "' id='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_OBJECT_COMBINATION] + "'>"
                                        + "<option value='1'>AND</option>"
                                        + "<option value='0'>OR</option>"
                                    + "</select>"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-2'>"
                                + "<div class='form-group'>"
                                    + "<div class='checkbox'>"
                                        + "<div style='margin-top:20px'>"
                                            + "<label><input type='checkbox' class='discountquantity' value='1' name='" + FrmMarketingPromotionDetail.fieldNames[FrmMarketingPromotionDetail.FRM_FIELD_DISCOUNT_QUANTITY_STATUS] + "'>Discount quantity still valid</label>"
                                        + "</div>"
                                    + "</div>"
                                    + "<div class='pull-right' style='margin-top:10px'>"
                                        + "<button type='submit' class='btn btn-primary btn-sm' id='btn-savepromo' data-for='savepromo' data-command = '" + Command.SAVE + "'>Save Promo</button>"
                                    + "</div>"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-12'>"
                                
                            + "</div>"
                                        
                        + "</div>"
                    + "</div>"
                + "</form>"
                + "<div class='list-promo'></div>";
                            
        return returnData;
    }
    
    // SHOW MODAL FORM DETAIL PROMOTION
    public String showPromo(HttpServletRequest request, Long oidMarketingPromotion) {
        String returnData = "";
        
        Vector listPromo = new Vector();
        listPromo = PstMarketingPromotionDetail.listJoin(0, 0, "" + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_ID] + " = '" + oidMarketingPromotion + "'", "");
        if (listPromo.size() > 0) {
            MarketingPromotionDetail promotionDetail = new MarketingPromotionDetail();
            for (int i = 0; i < listPromo.size(); i++) {
                promotionDetail = (MarketingPromotionDetail) listPromo.get(i);
                
                String status = "";
                if (promotionDetail.getStatusApprove().equals("1")){
                    status = "<b style='color:green;font-size:12'> Aproved</b>";
                } else if(promotionDetail.getStatusApprove().equals("0")) {
                    status = "<b style='color:red;font-size:12'> Disaproved</b>";
                } else {
                    status = "";
                }
                
                returnData += ""
                + "<div class='panel-group'>"
                    + "<div class='panel panel-default'>"
                        + "<a type='button' class='btn-deletepromo pull-right' data-oid='" + promotionDetail.getOID() + "' data-for='deletepromo' data-command = '" + Command.DELETE + "' href='#'><i class='fa fa-remove'></i></a>"
                        + "<div class='panel-heading' data-toggle='collapse' data-target='." + promotionDetail.getOID() + "' data-oid='" + promotionDetail.getOID() + "'>"                            
                            + "<h4 class='panel-title'>"                                
                                + "<a data-toggle='collapse' href='." + promotionDetail.getOID() + "'>" + promotionDetail.getPromotionTagline() + "</a>" + status                                 
                            + "</h4>"
                        + "</div>"
                
                        + "<div class='panel-collapse collapse " + promotionDetail.getOID() + "'>"
                            + "<div class='panel-body'>"
                                + "<input type='hidden' class='dataoidpromo' value='"+ promotionDetail.getOID() +"'>"
                                + "<div class='promo-" + promotionDetail.getOID() + "'></div>"
                            + "</div>"
                            + "<div class='panel-footer'><a data-toggle='collapse' href='." + promotionDetail.getOID() + "'>Hide Detail</a></div>"
                        + "</div>"
                
                    + "</div>"
                + "</div>";
            }
        }
        return returnData;
    }
    
    // SHOW MODAL FORM DETAIL PROMOTION
    public String showPromoDetail(HttpServletRequest request, Long oidMarketingPromotion, Long oidPromotionDetail) {
        String returnData = "";
        Vector listPromo = new Vector();
        listPromo = PstMarketingPromotionDetail.listJoin(0, 0, "" + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_ID] + " = '" + oidMarketingPromotion + "' AND " + PstMarketingPromotionDetail.fieldNames[PstMarketingPromotionDetail.FLD_MARKETING_PROMOTION_DETAIL_ID] + " = '" + oidPromotionDetail + "'", "");
        if (listPromo.size() > 0) {
            MarketingPromotionDetail promotionDetail = new MarketingPromotionDetail();
            for (int i = 0; i < listPromo.size(); i++) {
                promotionDetail = (MarketingPromotionDetail) listPromo.get(i);
                long idPromo = promotionDetail.getOID();
                
                MarketingPromotionType promotionType = new MarketingPromotionType();
                Vector valPromoType = new Vector(1, 1);
                Vector keyPromoType = new Vector(1, 1);
                Vector listPromoType = PstMarketingPromotionType.list(0, 0, "" + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE] + " LIKE '0'", "");

                for (int j = 0; j < listPromoType.size(); j++) {
                    promotionType = (MarketingPromotionType) listPromoType.get(j);
                    valPromoType.add("" + promotionType.getOID() + "");
                    keyPromoType.add(promotionType.getPromotionTypeName());
                }
                
                String subjectCombination = "";
                String objectCombination = "";
                if (promotionDetail.getSubjectCombination().equals("1")) {
                    subjectCombination = " AND ";
                } else if (promotionDetail.getSubjectCombination().equals("0")) {
                    subjectCombination = " OR ";
                }
                if (promotionDetail.getObjectCombination().equals("1")) {
                    objectCombination = " AND ";
                } else if (promotionDetail.getObjectCombination().equals("0")) {
                    objectCombination = " OR ";
                }
                
                returnData += ""
                        + "<div class='row'>"
                            + "<div class='col-sm-4'>"
                                + "<div class='form-group form-group-sm form-inline'>"
                                    + "<label>Promotion Type : " + promotionDetail.getPromotionName() + "</label>\t"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-4'>"
                                + "<div class='form-group form-group-sm form-inline'>"
                                    + "<label>Reason of Promotion : " + promotionDetail.getReasonOfPromotion() + "</label>\t"
                                + "</div>"
                            + "</div>"
                            + "<div class='col-sm-4'>"
                                + "<div class='form-inline'>"
                                    + "<div class='form-group form-group-sm pull-left'>"
                                        + "<label>Promotion Tagline : " + promotionDetail.getPromotionTagline() + "</label>\t"
                                    + "</div>"
                                    + "<div class='form-group form-group-sm pull-right'>"
                                        + "<button type='button' class='btn btn-box-tool btn-sm btn-editpromo' data-oid='" + promotionDetail.getOID() + "' data-for='editpromo' data-command='" + Command.NONE + "'><i class='fa fa-pencil'></i></button>"
                                        //+ "<button type='button' class='btn btn-box-tool btn-sm btn-deletepromo' data-oid='" + promotionDetail.getOID() + "' data-for='deletepromo' data-command='" + Command.DELETE + "'><i class='fa fa-remove'></i></button>"
                                    + "</div>"
                                + "</div>"
                            + "</div>"
                        + "</div>"
                        
                        + "<hr style='margin-top:0px'>"

                        + "<h5 class='text-center'><b>- Subject Item -</b></h5>"                        
                        + "<div class='table-responsive detailSubjectElement" + promotionDetail.getOID() + "'>"
                            + "<table class='table table-bordered'>"
                                + "<thead>"
                                    + "<tr>"
                                        + "<th>No</th>"
                                        + "<th>SKU</th>"
                                        + "<th>Barcode</th>"
                                        + "<th>Name</th>"
                                        + "<th>Category</th>"
                                        + "<th>Quantity</th>"
                                        + "<th>Target</th>"
                                        + "<th>Multiplication</th>"
                                        + "<th>Sales Price</th>"
                                        + "<th>Purchase Price</th>"
                                        + "<th>Gross Profit</th>"
                                        + "<th>Action</th>"
                                    + "</tr>"
                                + "</thead>"
                            + "</table>"
                        + "</div>"
                        + "<div class='table-responsive'>"
                            + "<form class='formsubject-" + idPromo + "' enctype='multipart/form-data'>"
                                + "<table class='table'>"
                                    + "<tbody>"
                                        + "<tr>"
                                            + "<input type='hidden' name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID] + "' value='" + promotionDetail.getOID() + "'>"
                                            + "<td><select style='width:100px;' class='form-control input-sm itemsubjectsku-" + idPromo + "' data-oid-promo='" + idPromo + "' value=''></select></td>"
                                            + "<td><select style='width:140px;' class='form-control input-sm itemsubjectbarcode-" + idPromo + "' data-oid-promo='" + idPromo + "' value=''></select></td>"
                                            + "<td><select style='width:170px;' class='form-control input-sm itemsubjectname-" + idPromo + "' data-oid-promo='" + idPromo + "' required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_MATERIAL_ID] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_MATERIAL_ID] + "' value=''></select></td>"
                                            + "<td><input type='text' style='width:100px;' class='form-control input-sm itemsubjectcategory-" + idPromo + "' placeholder='Category' readonly name='' value=''></td>"
                                            + "<td><input type='text' style='width:70px;' class='form-control input-sm itemsubjectquantity-" + idPromo + "' placeholder='Quantity' required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_QUANTITY] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_QUANTITY] + "' value=''></td>"
                                            + "<td><input type='text' style='width:70px;' class='form-control input-sm itemsubjecttargetquantity-" + idPromo + "' placeholder='Target' required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_TARGET_QUANTITY] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_TARGET_QUANTITY] + "' value=''></td>"
                                            + "<td>"
                                                + "<select style='width:70px;' class='form-control input-sm itemsubjectvalid-" + idPromo + "' required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_VALID_FOR_MULTIPLICATION] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_VALID_FOR_MULTIPLICATION] + "'>"
                                                    + "<option value='1'>Yes</option>"
                                                    + "<option value='0'>No</option>"
                                                + "</select>"
                                            + "</td>"
                                            + "<td><input type='text' style='width:100px;' class='form-control input-sm itemsubjectsales-" + idPromo + "' placeholder='Sales price' readonly required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_SALES_PRICE] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_SALES_PRICE] + "' value=''></td>"
                                            + "<td><input type='text' style='width:120px;' class='form-control input-sm itemsubjectpurchase-" + idPromo + "' placeholder='Purchase price' readonly required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_PURCHASE_PRICE] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_PURCHASE_PRICE] + "' value=''></td>"
                                            + "<td><input type='text' style='width:100px;' class='form-control input-sm itemsubjectprofit-" + idPromo + "' placeholder='Gross profit' readonly required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_GROSS_PROFIT] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_GROSS_PROFIT] + "' value=''></td>"
                                            + "<td><button type='submit' style='color:blue' class='btn btn-default btn-sm btn-savesubject-" + idPromo + "' data-oidpromo='" + idPromo + "' data-for='savesubject' data-command = '" + Command.SAVE + "'><i class='fa fa-plus'></i></button></td>"
                                        + "</tr>"
                                    + "</tbody>"
                                + "</table>"
                            + "</form>"
                        + "</div>"
                                        
                        + "<hr>"
                
                        + "<h5 class='text-center'><b>- Object Item -</b></h5>"
                        + "<div class='table-responsive detailObjectElement" + promotionDetail.getOID() + "'>"
                            + "<table class='table table-bordered'>"
                                + "<thead>"
                                    + "<tr>"
                                        + "<th>No</th>"
                                        + "<th>SKU</th>"
                                        + "<th>Barcode</th>"
                                        + "<th>Name</th>"
                                        + "<th>Category</th>"
                                        + "<th>Quantity</th>"
                                        + "<th>Promotion Type</th>"
                                        + "<th>Multiplication</th>"
                                        + "<th>Regular Price</th>"
                                        + "<th>Promotion Price</th>"
                                        + "<th>Cost</th>"
                                        + "<th>Action</th>"
                                    + "</tr>"
                                + "</thead>"
                            + "</table>"
                        + "</div>"
                        + "<div class='table-responsive'>"
                            + "<form class='formobject-" + idPromo + "' enctype='multipart/form-data'>"
                                + "<table class='table'>"
                                    + "<tbody>"
                                        + "<tr>"
                                            + "<input type='hidden' name='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID] + "' value='" + promotionDetail.getOID() + "'>"
                                            + "<td><select style='width:100px;' class='form-control input-sm itemobjectsku-" + idPromo + "' data-oid-promo='" + idPromo + "' value=''></select></td>"
                                            + "<td><select style='width:140px;' class='form-control input-sm itemobjectbarcode-" + idPromo + "' data-oid-promo='" + idPromo + "' value=''></select></td>"
                                            + "<td><select style='width:170px;' class='form-control input-sm itemobjectname-" + idPromo + "' data-oid-promo='" + idPromo + "' required name='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_MATERIAL_ID] + "' id='" + FrmMarketingPromotionDetailSubject.fieldNames[FrmMarketingPromotionDetailSubject.FRM_FIELD_MATERIAL_ID] + "' value=''></select></td>"
                                            + "<td><input type='text' style='width:100px;' class='form-control input-sm itemobjectcategory-" + idPromo + "' placeholder='Category' readonly name='' value=''></td>"
                                            + "<td>" + ControlCombo.draw(FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_MARKETING_PROMOTION_TYPE_ID], null, "" + promotionType.getPromotionType(), valPromoType, keyPromoType, "", "form-control input-sm itemobjectpromotype-" + idPromo + "") + "</td>"
                                            + "<td><input type='text' style='width:70px;' class='form-control input-sm itemobjectquantity-" + idPromo + "' placeholder='Quantity' required name='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_QUANTITY] + "' id='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_QUANTITY] + "' value=''></td>"
                                            + "<td>"
                                                + "<select style='width:70px;' class='form-control input-sm itemobjectvalid-" + idPromo + "' name='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_VALID_FOR_MULTIPLICATION] + "' id='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_VALID_FOR_MULTIPLICATION] + "'>"
                                                    + "<option value='1'>Yes</option>"
                                                    + "<option value='0'>No</option>"
                                                + "</select></td>"
                                            + "<td><input type='text' style='width:70px;' class='form-control input-sm itemobjectregular-" + idPromo + "' placeholder='Regular price' readonly required name='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_REGULAR_PRICE] + "' id='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_REGULAR_PRICE] + "' value=''></td>"
                                            + "<td><input type='text' style='width:100px;' class='form-control input-sm itemobjectpromotion-" + idPromo + "' placeholder='Promo price' required name='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_PROMOTION_PRICE] + "' id='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_PROMOTION_PRICE] + "' value=''></td>"
                                            + "<td><input type='text' style='width:70px;' class='form-control input-sm itemobjectcost-" + idPromo + "' placeholder='Cost' readonly required name='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_COST] + "' id='" + FrmMarketingPromotionDetailObject.fieldNames[FrmMarketingPromotionDetailObject.FRM_FIELD_COST] + "' value=''></td>"
                                            + "<td><button type='submit' style='color:blue' class='btn btn-default btn-sm btn-saveobject-" + idPromo + "' data-oidpromo='" + idPromo + "' data-for='saveobject' data-command = '" + Command.SAVE + "'><i class='fa fa-plus'></i></button></td>"                                            
                                        + "</tr>"
                                    + "</tbody>"
                                + "</table>"
                            + "</form>"
                        + "</div>"
                
                        + "<hr>"
                
                        + "<h5 class='text-center'><b>- Analysis -</b></h5>"
                        + "<div class='col-sm-6' style='padding-left:0px'>"
                            + "<div>"
                                + "<label>Subject Combination : " + subjectCombination + "</label>\n"
                            + "</div>"
                            + "<div>"
                                + "<label>Object Combination : " + objectCombination + "</label>"
                            + "</div>"
                        + "</div>"
                        + "<div class='form-group pull-right'>"
                            + "<button class='btn btn-primary btn-sm btn-analysis'>Save Analysis</button>"
                        + "</div>"

                        + "<div class='table-responsive'>"
                            + "<table class='table table-bordered'>"
                                + "<thead>"
                                    + "<tr>"
                                        + "<th>Subject Item</th>"
                                        + "<th>Target Quantity Subject</th>"
                                        + "<th>Object Item</th>"
                                        + "<th>Target Quantity Object</th>"
                                        + "<th>Profit (Non Promo)</th>"
                                        + "<th>Profit (Promo)</th>"
                                        + "<th>Value (Promo)</th>"
                                        + "<th>% (Promo)</th>"
                                        + "<th>Total Profit (Non Promo)</th>"
                                        + "<th>Total Margin (Promo)</th>"
                                        + "<th>Total Value (Promo)</th>"
                                    + "</tr>"
                                + "</thead>"
                                + "<tbody  class='analysis-" + promotionDetail.getOID() + "'>"

                                + "</tbody>"
                            + "</table>"
                        + "</div>"

                        + "<hr>"

                        + "<h5 class='text-center'><b>- Realization -</b></h5>"
                        + "<div class='col-sm-6' style='padding-left:0px'>"
                            + "<div>"
                                + "<label>Subject Combination : " + subjectCombination + "</label>\n"
                            + "</div>"
                            + "<div>"
                                + "<label>Object Combination : " + objectCombination + "</label>"
                            + "</div>"
                        + "</div>"
                        + "<div class='form-group pull-right'>"
                            + "<button class='btn btn-primary btn-sm btn-refresh' data-oidpromo='" + idPromo + "'>Realization</button>"
                        + "</div>"
                        + "<div class='table-responsive'>"
                            + "<table class='table table-bordered'>"
                                + "<thead>"
                                    + "<tr>"
                                        + "<th>Subject Item</th>"
                                        + "<th>Item Quantity (Realization Subject)</th>"
                                        + "<th>Object Item</th>"                                        
                                        + "<th>Item Quantity (Realization Object)</th>"
                                        + "<th>Total Profit (Non Promo)</th>"
                                        + "<th>Total Profit (Promo)</th>"
                                        + "<th>Total Value (Promo)</th>"
                                    + "</tr>"
                                + "</thead>"
                                + "<tbody class='realization-" + promotionDetail.getOID() + "'>"

                                + "</tbody>"
                            + "</table>"
                        + "</div>"
                        
                        + "<input type='hidden' class='approvestatus' value='" + promotionDetail.getStatusApprove() + "'>"
                        + "<div class='pull-right'>"
                            + "<button type='button' class='btn btn-success btn-sm btn-approve' data-oid='" + promotionDetail.getOID() + "' data-for='approvepromo' data-command='" + Command.SAVE + "'><i class='fa fa-check'></i> Approve</button>"
                            + "\t"
                            + "<button type='button' class='btn btn-danger btn-sm btn-disapprove' data-oid='" + promotionDetail.getOID() + "' data-for='disapprovepromo' data-command='" + Command.SAVE + "'><i class='fa fa-ban'></i> Disapprove</button>"
                        + "</div>";
            }
        }
        return returnData;
    }
    
    public String showFormSearch(HttpServletRequest request) {
        String data = ""
                + "<div class='row'>"
                
                    + "<div class='col-sm-4'>"
                        + "<div class='form-group'>"
                            + "<label>Tagline : </label>"
                            + "<input type='text' id='search-tagline' class='form-control input-sm'>"
                        + "</div>"
                    + "</div>"
                    + "<div class='col-sm-4'>"
                        + "<div class='form-group'>"
                            + "<label>Reason : </label>"
                            + "<input type='text' id='search-reason' class='form-control input-sm'>"
                        + "</div>"
                    + "</div>"
                    + "<div class='col-sm-4'>"
                        + "<div class='form-group'>"
                            + "<label>Item : </label>"
                            + "<input type='text' id='search-item' class='form-control input-sm'>"
                        + "</div>"
                    + "</div>"
                    + "<div class='col-sm-4'>"
                        + "<div class='form-group'>"
                            + "<label>Location : </label>"
                            + "<input type='text' id='search-location' class='form-control input-sm'>"
//                            + "<select id='search-location' class='form-control input-sm'>"
//                                + "<option value='1'>AND</option>"
//                                + "<option value='0'>OR</option>"
//                            + "</select>"
                        + "</div>"
                    + "</div>"
                    + "<div class='col-sm-4'>"
                        + "<div class='form-group'>"
                            + "<label>Member type : </label>"
                            + "<input type='text' id='search-member' class='form-control input-sm'>"
//                            + "<select id='search-member' class='form-control input-sm'>"
//                                + "<option value='1'>AND</option>"
//                                + "<option value='0'>OR</option>"
//                            + "</select>"
                        + "</div>"
                    + "</div>"
                    + "<div class='col-sm-4'>"
                        + "<div class='form-group'>"
                            + "<label>Price type : </label>"
                            + "<input type='text' id='search-price' class='form-control input-sm'>"
//                            + "<select id='search-price' class='form-control input-sm'>"
//                                + "<option value='1'>AND</option>"
//                                + "<option value='0'>OR</option>"
//                            + "</select>"
                        + "</div>"
                    + "</div>"                    
//                    + "<div class='col-sm-3'>"
//                        + "<div class='form-group'>"
//                            + "<label>Subject Comb : </label>"
//                            + "<select id='search-subcomb' class='form-control input-sm'>"
//                                + "<option value='1'>AND</option>"
//                                + "<option value='0'>OR</option>"
//                            + "</select>"
//                        + "</div>"
//                    + "</div>"
//                    + "<div class='col-sm-3'>"
//                        + "<div class='form-group'>"
//                            + "<label>Object Comb : </label>"
//                            + "<select id='search-obcomb' class='form-control input-sm'>"
//                                + "<option value='1'>AND</option>"
//                                + "<option value='0'>OR</option>"
//                            + "</select>"
//                        + "</div>"
//                    + "</div>"
                
                + "</div>";
        return data;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    }

}
