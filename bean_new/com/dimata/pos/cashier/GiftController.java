/*
 * GiftController.java
 *
 * Created on January 10, 2005, 9:30 PM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstSales;
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.pos.entity.billing.*;
import com.dimata.posbo.entity.masterdata.MemberPoin;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.posbo.entity.masterdata.PstMemberPoin;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.admin.PstMappingUserGroup;
import com.dimata.util.Command;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author  pulantara
 */
public class GiftController {
   
    /** search methode */
    private static final int PRE_AND_SUF = 0;
    private static final int PRE_ONLY = 1;
    private static final int SUF_ONLY = 2;
    private static int searchMethode = CashierMainApp.getDSJ_CashierXML().getConfig(0).searchMethode;
    private static String prefix = (searchMethode==SUF_ONLY?"":"%");
    private static String sufix = (searchMethode==PRE_ONLY?"":"%");
    
    /** service type constants */
    public static final int GIFT_INFO = 0;
    public static final int GIFT_TAKING = 1;
    public static final String[] serviceType = {
        "Point Info",
        "Gift Taking"
    };
    /** error messages constants */
    public static final int NONE = 0;
    public static final int RECORD_NOT_FOUND = 1;
    public static final int NO_ITEM_SELECTED = 2;
    public static final int CANT_ADD_ITEM_ERR = 3;
    public static final int CANT_UPDATE_ITEM_ERR = 4;
    public static final int CANT_REMOVE_ITEM_ERR = 5;
    public static final int CANT_UPDATE_PST_ERR = 6;
    public static final int INSUFFICIENT_AVAILABLE_POINT_ERR = 7;
    public static final int CODE_REQUIRED_ITEM_AMOUNT_ERR = 8;
    public static final int SERIAL_CODE_UNIQUE_ERR = 9;
    public static final int AMOUNT_UNDERFLOW_ERR = 10;
    public static final int NUMBER_FORMAT_ERR = 11;
    public static final int UNKNOWN_ERR = 12;
    
    public static final String[] errMsg = {
        "",
        "Record Not Found",
        "No Item Selected",
        "Can't Add Current Selected Item",
        "Can't Update Current Selected Item",
        "Can't Remove Current Selected Item",
        "Can't Update into Database",
        "Insufficient Available Point",
        "Code Required Item Amount Must Set 1",
        "Serial Code Must Be A Unique Code",
        "Amount Less Than 1",
        "Number Format Error",
        "Unknown Error"
    };
    
    /** current command
     *  default : Command.ADD
     */
    private static int iCommand=Command.ADD;
    
    /** dialog form for gift item search */
    private static GiftSearchDialog giftSearchDialog;
    
    /** selected item from gift search result */
    private static Material giftItemChoosen = null;
    
    /** current service type */
    private static int currentServiceType = GIFT_INFO;
    
    /** error message
     *  default : errMsg[NONE]
     */
    private static String err = errMsg[NONE];
    
    /** vector contains all valid MemberPoint objects
     *  for current MemberReg object
     */
    private static Vector memberPointList;
    
    /** Creates a new instance of GiftController */
    public GiftController() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    /** getGiftWith(int,int,String,String,int)
     *  return a Vector contains gift item objects for current
     *  given parameter
     *  @return Vector
     *  @param int start, int recordToGet,String itemCode,String itemName,int poinAmount
     */
    public static Vector getGiftWith(int start, int recordToGet,String itemCode,String itemName,int poinAmount){
        
        Vector result= new Vector();
        String sqlGiftSearch = "";
        String itemNameSql = "";
        String itemCodeSql = "";
        String itemPointSql = "";
        if(poinAmount<=0) {
            poinAmount =1;
        }
        if(itemName.length()>0){
            StringTokenizer st = new StringTokenizer(itemName);
            while(st.hasMoreTokens()){
                String temp = st.nextToken();
                itemNameSql = itemNameSql + " " +
                " "+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" like '%"+temp+"%' " +
                " AND" ;
            }
            itemNameSql = itemNameSql.substring(0,itemNameSql.length()-3);
            sqlGiftSearch = itemNameSql;
        }
        
        if(itemCode.length()>0){
            itemCodeSql = PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" LIKE '"+prefix+itemCode+sufix+"' ";
            if(sqlGiftSearch.length()>0){
                sqlGiftSearch = sqlGiftSearch + " AND " + itemCodeSql;
            }
            else{
                sqlGiftSearch = itemCodeSql;
            }
        }
        if(poinAmount>0){
            itemPointSql = PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT]+" >="+poinAmount+" ";
            if(sqlGiftSearch.length()>0){
                sqlGiftSearch = sqlGiftSearch + " AND " + itemPointSql;
            }
            else{
                sqlGiftSearch = itemPointSql;
            }
        }
        
        try{
            result = PstMaterial.list(start,recordToGet,sqlGiftSearch,PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT]);
        }
        catch(Exception e){
            System.out.println("Err on getGiftWith: "+e.toString());
        }
        return result;
    }
    
    /** getGiftCountWith(int,int,String,String,int)
     *  return count of record of gift item objects for current
     *  given parameter
     *  @return int
     *  @param String itemCode,String itemName,int poinAmount
     */
    public static int getGiftCountWith(String itemCode,String itemName,int poinAmount){
        int result = 0;
        String sqlGiftSearch = "";
        String itemNameSql = "";
        String itemCodeSql = "";
        String itemPointSql = "";
        if(poinAmount<=0) {
            poinAmount =1;
        }
        if(itemName.length()>0){
            StringTokenizer st = new StringTokenizer(itemName);
            while(st.hasMoreTokens()){
                String temp = st.nextToken();
                itemNameSql = itemNameSql + " " +
                " "+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" like '%"+temp+"%' " +
                " AND" ;
            }
            itemNameSql = itemNameSql.substring(0,itemNameSql.length()-3);
            sqlGiftSearch = itemNameSql;
        }
        
        if(itemCode.length()>0){
            itemCodeSql = PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" LIKE '"+prefix+itemCode+sufix+"' ";
            if(sqlGiftSearch.length()>0){
                sqlGiftSearch = sqlGiftSearch + " AND " + itemCodeSql;
            }
            else{
                sqlGiftSearch = itemCodeSql;
            }
        }
        if(poinAmount>0){
            itemPointSql = PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT]+" >="+poinAmount+" ";
            if(sqlGiftSearch.length()>0){
                sqlGiftSearch = sqlGiftSearch + " AND " + itemPointSql;
            }
            else{
                sqlGiftSearch = itemPointSql;
            }
        }
        System.out.println("sql gift search: "+sqlGiftSearch);
        try{
            result = PstMaterial.getCount(sqlGiftSearch);
        }
        catch(Exception e){
            System.out.println("Err on getGiftCountWith: "+e.toString());
        }
        
        return result;
    }
    
    /** searchSalesByCode(String, String)
     *  return a Sales object as a search result by sales code
     *  @return Sales
     *  @param String salesCode, String err
     */
    public static Sales searchSalesByCode(String salesCode){
        Sales salesPerson = new Sales();
        String whereClause = PstSales.fieldNames[PstSales.FLD_CODE]+" = "+salesCode+" ";
        try{
            Vector vctSalesPerson = PstSales.list(0,1, whereClause, "");
            if(vctSalesPerson.size()>0){
                salesPerson = (Sales)vctSalesPerson.get(0);
            }
            else{
                err = errMsg[RECORD_NOT_FOUND];
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return salesPerson;
        
    }
  public static AppUser searchSalesById(long oid) {
    AppUser ap = new AppUser();
    try {
      ap = PstAppUser.fetch(oid);
      String whereClause = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID] + " = " + ap.getOID() + " AND " + PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6";
      Vector sales = PstMappingUserGroup.list(0, 0, whereClause, "");
      if (sales.size() > 0) {
        ap = (AppUser) sales.get(0);
      } else {
        err = errMsg[RECORD_NOT_FOUND];
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return ap;

  }
    
    /** searchMember(String, String, String)
     *  return a MemberReg object as a search result by member code or member name
     *  @return MemberReg
     *  @param String memberCode, String memberName, String err
     */
    public static MemberReg searchMember(String memberCode, String memberName){
        
        String sqlMemberSearch = "";
        String nameSql = "";
        String codeSql = "";
        if(memberName.length()>0){
            nameSql = PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]+" like '%"+memberName+"%' OR "+
            PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME]+" like '%"+memberName+"%' ";
        }
        if(memberCode.length()>0){
            codeSql = PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE]+" = "+memberCode+" ";
        }
        if(nameSql.length()>0){
            sqlMemberSearch = nameSql;
        }
        if(codeSql.length()>0){
            if(nameSql.length()>0){
                sqlMemberSearch = sqlMemberSearch + " OR "+ codeSql;
            }
            else{
                sqlMemberSearch = codeSql;
            }
        }
        
        System.out.println(sqlMemberSearch);
        
        Vector rs = new Vector();
        MemberReg result = new MemberReg();
        
        rs = PstMemberReg.list(0,1,sqlMemberSearch,PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]);
        
        if(rs.size()>0){
            result = (MemberReg) rs.get(0);
        }
        else{
            err = errMsg[RECORD_NOT_FOUND];
        }
        
        return result;
    }
    
    /** countAvailablePointOf(long)
     *  count available point for given MemberReg OID
     *  @param long memberId
     *  edited for new business process for gift
     */
    public static int countAvailablePointOf(long memberId){
        int result = 0;
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT SUM("+PstMemberPoin.fieldNames[PstMemberPoin.FLD_CREDIT]+") "+
            " - SUM("+PstMemberPoin.fieldNames[PstMemberPoin.FLD_DEBET]+") "+
            " FROM "+PstMemberPoin.TBL_POS_MEMBER_POIN+
            " WHERE "+PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_ID]+
            " = "+memberId;
            
            System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    /** update all persistent from model
     *  basically this methode update the related table in database
     *  with the value contains in the model
     *  @see GiftModel
     */
    public static void updatePstFromModel(GiftModel model){
        try{
            long billMainOid = PstBillMain.insertExc(model.getGiftTrans());
            
            Vector listDetail = model.getGiftTransDetail();
            Hashtable listCode = model.getGifTransDetailCode();
            
            // insert Billdetail and BillDetailCode if any
            int sizeDetail = listDetail.size();
            Billdetail detail;
            BillDetailCode code;
            long billDetailOid;
            for(int i = 0; i < sizeDetail; i++){
                detail = (Billdetail) listDetail.get(i);
                detail.setBillMainId(billMainOid);
                billDetailOid = PstBillDetail.insertExc(detail);
                if((code = (BillDetailCode) listCode.get(detail))!=null){
                    code.setSaleItemId(billDetailOid);
                    PstBillDetailCode.insertExc(code);
                }
            }
            
            // insert new member point stock
            MemberPoin poin = new MemberPoin();
            poin.setCashBillMainId(billMainOid);
            poin.setMemberId(model.getCustomerServed().getOID());
            poin.setDebet((int) model.getRequestedPoint());
            
            PstMemberPoin.insertExc(poin);
            
        }
        catch(Exception e){
            System.out.println("Error on GiftController:updatePstFromModel"+e);
        }
    }
    
    
    /** showGiftSearchDialog(Object parent, String code, String name)
     *  show GiftSearchDialog
     *  search result seted in giftItemChoosen propertry
     */
    public static void showGiftSearchDialog(Object parent, String code, String name, int point){
        giftItemChoosen = null;
        if(giftSearchDialog==null){
            giftSearchDialog = new GiftSearchDialog(null,true);
            giftSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(giftSearchDialog));
        }
        giftSearchDialog.initAllFields();
        giftSearchDialog.setKeyword(code, name,point);
        if(code.length()+name.length()+point>0)
            giftSearchDialog.cmdSearch(0);
        giftSearchDialog.show();
    }
    
    /**
     * Getter for property giftItemChoosen.
     * @return Value of property giftItemChoosen.
     */
    public static Material getGiftItemChoosen() {
        return giftItemChoosen;
    }
    
    /**
     * Setter for property giftItemChoosen.
     * @param giftItemChoosen New value of property giftItemChoosen.
     */
    public static void setGiftItemChoosen(Material giftItem) {
        giftItemChoosen = giftItem;
    }
    
    public static void putDataSet(GiftModel giftModel){
        
    }
    
    /**
     * Getter for property currentServiceType.
     * @return Value of property currentServiceType.
     */
    public static int getCurrentServiceType() {
        return currentServiceType;
    }
    
    /**
     * Setter for property currentServiceType.
     * @param currentServiceType New value of property currentServiceType.
     */
    public static void setCurrentServiceType(int currentService) {
        currentServiceType = currentService;
    }
    
    /**
     * Getter for property iCommand.
     * @return Value of property iCommand.
     */
    public static int getICommand() {
        return iCommand;
    }
    
    /**
     * Setter for property iCommand.
     * @param iCommand New value of property iCommand.
     */
    public static void setICommand(int command) {
        iCommand = command;
    }
    
    /**
     * Getter for property err.
     * err value setted ""
     * @return Value of property err.
     */
    public static java.lang.String getErr() {
        String result = err;
        err = "";
        return result;
    }
    
    /**
     * Setter for property err.
     * @param err New value of property err.
     */
    public static void setErr(java.lang.String error) {
        err = error;
    }
    
    /**
     * Getter for property memberPointList.
     * @return Value of property memberPointList.
     */
    public static Vector getMemberPointList() {
        return memberPointList;
    }
    
    /**
     * Setter for property memberPointList.
     * @param memberPointList New value of property memberPointList.
     */
    public static void setMemberPointList(Vector list) {
        memberPointList = list;
    }
    
}