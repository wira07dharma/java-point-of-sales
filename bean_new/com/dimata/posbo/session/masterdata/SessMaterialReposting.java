/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.masterdata;


import java.util.*;
import java.sql.*;

import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.payment.PstDiscountType;
import com.dimata.common.entity.payment.DiscountType;

//adding priceType
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstPriceType;

/**
 *
 * @author Dimata 007
 */
public class SessMaterialReposting {
    
    public static final String SESS_SRC_MATERIAL_REPOSTING = "SESSION_SRC_MATERIAL_REPOSTING";
    
    public static Vector searchMaterial(SrcMaterial srcmaterial, int start, int recordToGet) {
        String strCode = srcmaterial.getMatcode();
        Vector vectMaterialCode = new Vector(1,1);
        vectMaterialCode.add(strCode);
        
        /*Vector vectMaterialCode = LogicParser.textSentence(strCode); // srcmaterial.getMatcode()
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }*/
        
        String strName = srcmaterial.getMatname();
        Vector vectMaterialName = new Vector(1,1);
        vectMaterialName.add(strName);
        
        /*Vector vectMaterialName = LogicParser.textSentence(strName);
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        }*/
        return getListMaterial(vectMaterialCode, vectMaterialName, srcmaterial, start, recordToGet);
    }
    
    //adding search for reposting stok
    // by mirahu 20120731
    public static Vector searchMaterialRepostingStock(SrcMaterialRepostingStock srcMaterialRepostingStock, int start, int recordToGet) {
        String strCode = srcMaterialRepostingStock.getMatcode();
        Vector vectMaterialCode = new Vector(1,1);
        vectMaterialCode.add(strCode);
        
        /*Vector vectMaterialCode = LogicParser.textSentence(strCode); // srcmaterial.getMatcode()
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }*/
        
        String strName = srcMaterialRepostingStock.getMatname();
        Vector vectMaterialName = new Vector(1,1);
        vectMaterialName.add(strName);
        
        /*Vector vectMaterialName = LogicParser.textSentence(strName);
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        }*/
        return getListMaterialRepostingStock(vectMaterialCode, vectMaterialName, srcMaterialRepostingStock, start, recordToGet);
    }
    
    //adding search price tag 
    //by mirahu 20120420
    public static Vector searchMaterialPriceTag(SrcMaterial srcmaterial, int start, int recordToGet, long oidPriceType) {
        String strCode = srcmaterial.getMatcode();
        Vector vectMaterialCode = new Vector(1,1);
        vectMaterialCode.add(strCode);
        
        /*Vector vectMaterialCode = LogicParser.textSentence(strCode); // srcmaterial.getMatcode()
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }*/
        
        String strName = srcmaterial.getMatname();
        Vector vectMaterialName = new Vector(1,1);
        vectMaterialName.add(strName);
        
        /*Vector vectMaterialName = LogicParser.textSentence(strName);
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        }*/
        return getListMaterialPriceTag(vectMaterialCode, vectMaterialName, srcmaterial, start, recordToGet, oidPriceType);
    }
    
    
    
    public static Vector getListMaterial(Vector vtMaterialCode, Vector vtMaterialName,
            SrcMaterial srcmaterial, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE];
            //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
            //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE];
            if (srcmaterial.getSupplierId() != -1) {
                sql = sql + " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                        " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
            }
            //if(srcmaterial.getMerkId()!=0){
            sql = sql + " ,MERK." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " AS MERKNM ";
            //}
            sql = sql + " FROM " + PstMaterial.TBL_MATERIAL +
                    " MAT LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "";
            /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
            " SCAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " ) ";*/
            if (srcmaterial.getSupplierId() != -1) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VDR " +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                        " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            }
            //if(srcmaterial.getMerkId()!=0){
            sql = sql + " LEFT JOIN " + PstMerk.TBL_MAT_MERK + " AS MERK " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                    " = MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID];
            //}
            
            String strMaterialCode = "";
            if (srcmaterial.getJenisCode() == 0) {
                if (vtMaterialCode != null && vtMaterialCode.size() > 0) {
                    for (int a = 0; a < vtMaterialCode.size(); a++) {
                        if (strMaterialCode.length() == 0) {
                            strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        } else {
                            strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        }
                    }
                    strMaterialCode = "( " + strMaterialCode + " )";
                }
            } else {
                if (srcmaterial.getMatcode().length() > 0) {
                    strMaterialCode = " SUBSTRING(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",3,3) = '" + srcmaterial.getMatcode() + "'";
                }
                if (srcmaterial.getCodeShip().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcmaterial.getCodeShip() + "'";
                    else
                        strMaterialCode = " LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcmaterial.getCodeShip() + "'";
                }
                if (srcmaterial.getCodeCounter().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcmaterial.getCodeCounter().length() + ") = '" + srcmaterial.getCodeCounter() + "'";
                    else
                        strMaterialCode = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcmaterial.getCodeCounter().length() + ") = '" + srcmaterial.getCodeCounter() + "'";
                }
                
                if (strMaterialCode.length() > 0)
                    strMaterialCode = "(" + strMaterialCode + ")";
            }
            
            String strMaterialName = "";
            if (vtMaterialName != null && vtMaterialName.size() > 0) {
                for (int a = 0; a < vtMaterialName.size(); a++) {
                    if (strMaterialName.length() == 0) {
                        strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    } else {
                        strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    }
                }
                strMaterialName = "( " + strMaterialName + " )";
            }
            
            String strSupplier = "";
            if (srcmaterial.getSupplierId() > 0) {
                strSupplier = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + srcmaterial.getSupplierId();
            }
            
            String strMerk = "";
            if (srcmaterial.getMerkId() > 0) {
                strMerk = " MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] +
                        " = " + srcmaterial.getMerkId();
            }
            
            String strGroup = "";
            if (srcmaterial.getCategoryId() > 0) {
                strGroup = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcmaterial.getCategoryId();
            }
            
            String strSubCategory = "";
            if (srcmaterial.getSubCategoryId() > 0) {
                //strSubCategory = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                //       " = " + srcmaterial.getSubCategoryId();
            }
            
            String strItemType = "";
            if (srcmaterial.getTypeItem() != -1) {
                if (srcmaterial.getTypeItem() == 0) {//not for gift
                    //strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                    //    " = 0 ";
                } else {// for gift
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                            " > 0 ";
                }
            }
            
            String strCodeRange = "";
            if (srcmaterial.getOidCodeRange() != 0) {
                try{
                    CodeRange codeRange = PstCodeRange.fetchExc(srcmaterial.getOidCodeRange());
                    strCodeRange = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            " BETWEEN '" +codeRange.getFromRangeCode()+"' AND '"+codeRange.getToRangeCode()+"'";
                }catch(Exception e){}
            }

            //group Item
            String strGroupTypeItem = "";
            if (srcmaterial.getGroupItem() != -1) {
                if (srcmaterial.getGroupItem() == PstMaterial.MATERIAL_TYPE_REGULAR) { // for material
                    strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_REGULAR;
                } else if (srcmaterial.getGroupItem() == PstMaterial.MAT_TYPE_COMPOSITE) {// for composite
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_COMPOSITE;
                } else if (srcmaterial.getGroupItem() == PstMaterial.MAT_TYPE_SERVICE) {// for service
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_SERVICE;
                }
            }
            
            String whereClause = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if (strMaterialCode.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialCode;
                } else {
                    whereClause = whereClause + " AND " + strMaterialCode;
                }
            }
            
            if (strMaterialName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialName;
                } else {
                    whereClause = whereClause + " AND " + strMaterialName;
                }
            }
            
            if (strSupplier.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSupplier;
                } else {
                    whereClause = whereClause + " AND " + strSupplier;
                }
            }
            
            if (strMerk.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMerk;
                } else {
                    whereClause = whereClause + " AND " + strMerk;
                }
            }
            
            if (strGroup.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroup;
                } else {
                    whereClause = whereClause + " AND " + strGroup;
                }
            }
            
            if (strSubCategory.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSubCategory;
                } else {
                    whereClause = whereClause + " AND " + strSubCategory;
                }
            }
            
            if (strItemType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strItemType;
                } else {
                    whereClause = whereClause + " AND " + strItemType;
                }
            }
            
            if (strCodeRange.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strCodeRange;
                } else {
                    whereClause = whereClause + " AND " + strCodeRange;
                }
            }
              if (strGroupTypeItem.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroupTypeItem;
                } else {
                    whereClause = whereClause + " AND " + strGroupTypeItem;
                }
            }

            //query update harga jual pada katalog
              String strfromDate = Formater.formatDate(srcmaterial.getDateFrom(), "yyyy-MM-dd 00:00:00");
              String strtoDate = Formater.formatDate(srcmaterial.getDateTo(), "yyyy-MM-dd 23:59:59");
              if (srcmaterial.getShowUpdateCatalog()== 1) {
                   // whereClause = whereClause;
              //}
                  if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
               }
           }
           else {
                 whereClause = whereClause;
           }
           //if (srcmaterial.getShowUpdateCatalog()== 1) {
              //get name material > 2
               if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2" ;
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2";
               }
            //}

               

            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            switch (srcmaterial.getSortby()) {
                case 0:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                case 1:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                case 2:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
            }
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                    break;
                    
                case DBHandler.DBSVR_SYBASE:
                    break;
                    
                case DBHandler.DBSVR_ORACLE:
                    break;
                    
                case DBHandler.DBSVR_MSSQL:
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            
            while (rs.next()) {
                Vector temp = new Vector();
                Material material = new Material();
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                ContactList cnt = new ContactList();
                
                material.setOID(rs.getLong(1));
                material.setSku(rs.getString(2));
                material.setBarCode(rs.getString(3));
                material.setName(rs.getString(4));
                material.setDefaultSupplierType(rs.getInt(5));
                material.setDefaultPrice(rs.getDouble(6));
                material.setDefaultCost(rs.getDouble(7));
                temp.add(material);
                
                category.setName(rs.getString(8));
                category.setCode(rs.getString(9));
                temp.add(category);
                
                //subCategory.setName(rs.getString(10));
                //subCategory.setCode(rs.getString(11));
                temp.add(subCategory);
                
                Merk merk = new Merk();
                merk.setName(rs.getString("MERKNM"));
                temp.add(merk);
                //
                //cnt.setCompName(rs.getString(12));
                //cnt.setContactCode(rs.getString(13));
                //temp.add(cnt);
                
                result.add(temp);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Err get list opname material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    //add getListMaterial for reposting stok
    //by mirahu 20120730
    public static Vector getListMaterialRepostingStock(Vector vtMaterialCode, Vector vtMaterialName,
            SrcMaterialRepostingStock srcMaterialRepostingStock, int start, int recordToGet) {
        DBResultSet dbrs = null;
        
        
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE];
            //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
            //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE];
            if (srcMaterialRepostingStock.getSupplierId() != -1) {
                sql = sql + " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                        " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
            }
            //if(srcmaterial.getMerkId()!=0){
            sql = sql + " ,MERK." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " AS MERKNM ";
            //}
            sql = sql + " FROM " + PstMaterial.TBL_MATERIAL +
                    " MAT LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "";
            /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
            " SCAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " ) ";*/
            if (srcMaterialRepostingStock.getSupplierId() != -1) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VDR " +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                        " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            }
            //if(srcmaterial.getMerkId()!=0){
            sql = sql + " LEFT JOIN " + PstMerk.TBL_MAT_MERK + " AS MERK " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                    " = MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID];
            //}
            
            String strMaterialCode = "";
            if (srcMaterialRepostingStock.getJenisCode() == 0) {
                if (vtMaterialCode != null && vtMaterialCode.size() > 0) {
                    for (int a = 0; a < vtMaterialCode.size(); a++) {
                        if (strMaterialCode.length() == 0) {
                            strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        } else {
                            strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        }
                    }
                    strMaterialCode = "( " + strMaterialCode + " )";
                }
            } else {
                if (srcMaterialRepostingStock.getMatcode().length() > 0) {
                    strMaterialCode = " SUBSTRING(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",3,3) = '" + srcMaterialRepostingStock.getMatcode() + "'";
                }
                if (srcMaterialRepostingStock.getCodeShip().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcMaterialRepostingStock.getCodeShip() + "'";
                    else
                        strMaterialCode = " LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcMaterialRepostingStock.getCodeShip() + "'";
                }
                if (srcMaterialRepostingStock.getCodeCounter().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcMaterialRepostingStock.getCodeCounter().length() + ") = '" + srcMaterialRepostingStock.getCodeCounter() + "'";
                    else
                        strMaterialCode = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcMaterialRepostingStock.getCodeCounter().length() + ") = '" + srcMaterialRepostingStock.getCodeCounter() + "'";
                }
                
                if (strMaterialCode.length() > 0)
                    strMaterialCode = "(" + strMaterialCode + ")";
            }
            
            String strMaterialName = "";
            if (vtMaterialName != null && vtMaterialName.size() > 0) {
                for (int a = 0; a < vtMaterialName.size(); a++) {
                    if (strMaterialName.length() == 0) {
                        strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    } else {
                        strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    }
                }
                strMaterialName = "( " + strMaterialName + " )";
            }
            
            String strSupplier = "";
            if (srcMaterialRepostingStock.getSupplierId() > 0) {
                strSupplier = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + srcMaterialRepostingStock.getSupplierId();
            }
            
            String strMerk = "";
            if (srcMaterialRepostingStock.getMerkId() > 0) {
                strMerk = " MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] +
                        " = " + srcMaterialRepostingStock.getMerkId();
            }
            
            String strGroup = "";
            if (srcMaterialRepostingStock.getCategoryId() > 0) {
                strGroup = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcMaterialRepostingStock.getCategoryId();
            }
            
            String strSubCategory = "";
            if (srcMaterialRepostingStock.getSubCategoryId() > 0) {
                //strSubCategory = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                //       " = " + srcmaterial.getSubCategoryId();
            }
            
            String strItemType = "";
            if (srcMaterialRepostingStock.getTypeItem() != -1) {
                if (srcMaterialRepostingStock.getTypeItem() == 0) {//not for gift
                    //strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                    //    " = 0 ";
                } else {// for gift
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                            " > 0 ";
                }
            }
            
            String strCodeRange = "";
            if (srcMaterialRepostingStock.getOidCodeRange() != 0) {
                try{
                    CodeRange codeRange = PstCodeRange.fetchExc(srcMaterialRepostingStock.getOidCodeRange());
                    strCodeRange = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            " BETWEEN '" +codeRange.getFromRangeCode()+"' AND '"+codeRange.getToRangeCode()+"'";
                }catch(Exception e){}
            }

            //group Item
            String strGroupTypeItem = "";
            if (srcMaterialRepostingStock.getGroupItem() != -1) {
                if (srcMaterialRepostingStock.getGroupItem() == PstMaterial.MATERIAL_TYPE_REGULAR) { // for material
                    strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_REGULAR;
                } else if (srcMaterialRepostingStock.getGroupItem() == PstMaterial.MAT_TYPE_COMPOSITE) {// for composite
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_COMPOSITE;
                } else if (srcMaterialRepostingStock.getGroupItem() == PstMaterial.MAT_TYPE_SERVICE) {// for service
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_SERVICE;
                }
            }
            
            String whereClause = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if (strMaterialCode.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialCode;
                } else {
                    whereClause = whereClause + " AND " + strMaterialCode;
                }
            }
            
            if (strMaterialName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialName;
                } else {
                    whereClause = whereClause + " AND " + strMaterialName;
                }
            }
            
            if (strSupplier.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSupplier;
                } else {
                    whereClause = whereClause + " AND " + strSupplier;
                }
            }
            
            if (strMerk.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMerk;
                } else {
                    whereClause = whereClause + " AND " + strMerk;
                }
            }
            
            if (strGroup.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroup;
                } else {
                    whereClause = whereClause + " AND " + strGroup;
                }
            }
            
            if (strSubCategory.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSubCategory;
                } else {
                    whereClause = whereClause + " AND " + strSubCategory;
                }
            }
            
            if (strItemType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strItemType;
                } else {
                    whereClause = whereClause + " AND " + strItemType;
                }
            }
            
            if (strCodeRange.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strCodeRange;
                } else {
                    whereClause = whereClause + " AND " + strCodeRange;
                }
            }
              if (strGroupTypeItem.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroupTypeItem;
                } else {
                    whereClause = whereClause + " AND " + strGroupTypeItem;
                }
            }

            //query update harga jual pada katalog
              String strfromDate = Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd 00:00:00");
              String strtoDate = Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd 23:59:59");
              if (srcMaterialRepostingStock.getShowUpdateCatalog()== 1) {
                   // whereClause = whereClause;
              //}
                  if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
               }
           }
           else {
                 whereClause = whereClause;
           }
           //if (srcmaterial.getShowUpdateCatalog()== 1) {
              //get name material > 2
               if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " LENGTH(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2" ;
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND LENGTH(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2";
               }
            //}

               

            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            switch (srcMaterialRepostingStock.getSortby()) {
                case 0:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                case 1:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                case 2:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
            }
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                    break;
                    
                case DBHandler.DBSVR_SYBASE:
                    break;
                    
                case DBHandler.DBSVR_ORACLE:
                    break;
                    
                case DBHandler.DBSVR_MSSQL:
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            
            while (rs.next()) {
                Vector temp = new Vector();
                Material material = new Material();
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                ContactList cnt = new ContactList();
                
                material.setOID(rs.getLong(1));
                material.setSku(rs.getString(2));
                material.setBarCode(rs.getString(3));
                material.setName(rs.getString(4));
                material.setDefaultSupplierType(rs.getInt(5));
                material.setDefaultPrice(rs.getDouble(6));
                material.setDefaultCost(rs.getDouble(7));
                temp.add(material);
                
                category.setName(rs.getString(8));
                category.setCode(rs.getString(9));
                temp.add(category);
                
                //subCategory.setName(rs.getString(10));
                //subCategory.setCode(rs.getString(11));
                temp.add(subCategory);
                
                Merk merk = new Merk();
                merk.setName(rs.getString("MERKNM"));
                temp.add(merk);
                //
                //cnt.setCompName(rs.getString(12));
                //cnt.setContactCode(rs.getString(13));
                //temp.add(cnt);
                
                result.add(temp);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Err get list opname material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
     public static Vector getListMaterialPriceTag(Vector vtMaterialCode, Vector vtMaterialName,
            SrcMaterial srcmaterial, int start, int recordToGet, long oidPriceType) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE];
            //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
            //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE];
            if (srcmaterial.getSupplierId() != -1) {
                sql = sql + " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                        " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
            }
            //if(srcmaterial.getMerkId()!=0){
            sql = sql + " ,MERK." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " AS MERKNM ";
            //}
            sql = sql + " FROM " + PstMaterial.TBL_MATERIAL +
                    " MAT LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + 
              //adding price type
                    " LEFT JOIN " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING +
                    " PRC ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +"";
            if (srcmaterial.getSupplierId() != -1) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VDR " +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                        " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            }
            //if(srcmaterial.getMerkId()!=0){
            sql = sql + " LEFT JOIN " + PstMerk.TBL_MAT_MERK + " AS MERK " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                    " = MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID];
            //}
            
            String strMaterialCode = "";
            if (srcmaterial.getJenisCode() == 0) {
                if (vtMaterialCode != null && vtMaterialCode.size() > 0) {
                    for (int a = 0; a < vtMaterialCode.size(); a++) {
                        if (strMaterialCode.length() == 0) {
                            strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        } else {
                            strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        }
                    }
                    strMaterialCode = "( " + strMaterialCode + " )";
                }
            } else {
                if (srcmaterial.getMatcode().length() > 0) {
                    strMaterialCode = " SUBSTRING(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",3,3) = '" + srcmaterial.getMatcode() + "'";
                }
                if (srcmaterial.getCodeShip().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcmaterial.getCodeShip() + "'";
                    else
                        strMaterialCode = " LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcmaterial.getCodeShip() + "'";
                }
                if (srcmaterial.getCodeCounter().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcmaterial.getCodeCounter().length() + ") = '" + srcmaterial.getCodeCounter() + "'";
                    else
                        strMaterialCode = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcmaterial.getCodeCounter().length() + ") = '" + srcmaterial.getCodeCounter() + "'";
                }
                
                if (strMaterialCode.length() > 0)
                    strMaterialCode = "(" + strMaterialCode + ")";
            }
            
            String strMaterialName = "";
            if (vtMaterialName != null && vtMaterialName.size() > 0) {
                for (int a = 0; a < vtMaterialName.size(); a++) {
                    if (strMaterialName.length() == 0) {
                        strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    } else {
                        strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    }
                }
                strMaterialName = "( " + strMaterialName + " )";
            }
            
            String strSupplier = "";
            if (srcmaterial.getSupplierId() > 0) {
                strSupplier = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + srcmaterial.getSupplierId();
            }
            
            String strMerk = "";
            if (srcmaterial.getMerkId() > 0) {
                strMerk = " MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] +
                        " = " + srcmaterial.getMerkId();
            }
            
            String strGroup = "";
            if (srcmaterial.getCategoryId() > 0) {
                strGroup = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcmaterial.getCategoryId();
            }
            
            String strSubCategory = "";
            if (srcmaterial.getSubCategoryId() > 0) {
                //strSubCategory = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                //       " = " + srcmaterial.getSubCategoryId();
            }
            
            String strItemType = "";
            if (srcmaterial.getTypeItem() != -1) {
                if (srcmaterial.getTypeItem() == 0) {//not for gift
                    //strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                    //    " = 0 ";
                } else {// for gift
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                            " > 0 ";
                }
            }
            
            String strCodeRange = "";
            if (srcmaterial.getOidCodeRange() != 0) {
                try{
                    CodeRange codeRange = PstCodeRange.fetchExc(srcmaterial.getOidCodeRange());
                    strCodeRange = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            " BETWEEN '" +codeRange.getFromRangeCode()+"' AND '"+codeRange.getToRangeCode()+"'";
                }catch(Exception e){}
            }

            //group Item
            String strGroupTypeItem = "";
            if (srcmaterial.getGroupItem() != -1) {
                if (srcmaterial.getGroupItem() == PstMaterial.MATERIAL_TYPE_REGULAR) { // for material
                    strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_REGULAR;
                } else if (srcmaterial.getGroupItem() == PstMaterial.MAT_TYPE_COMPOSITE) {// for composite
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_COMPOSITE;
                } else if (srcmaterial.getGroupItem() == PstMaterial.MAT_TYPE_SERVICE) {// for service
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_SERVICE;
                }
            }
          
             String strPriceType = "";
	    if (oidPriceType > 0) {
                strPriceType = " PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] +
                               " = " + oidPriceType;
            }
            
            String whereClause = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if (strMaterialCode.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialCode;
                } else {
                    whereClause = whereClause + " AND " + strMaterialCode;
                }
            }
            
            if (strMaterialName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialName;
                } else {
                    whereClause = whereClause + " AND " + strMaterialName;
                }
            }
            
            if (strSupplier.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSupplier;
                } else {
                    whereClause = whereClause + " AND " + strSupplier;
                }
            }
            
            if (strMerk.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMerk;
                } else {
                    whereClause = whereClause + " AND " + strMerk;
                }
            }
            
            if (strGroup.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroup;
                } else {
                    whereClause = whereClause + " AND " + strGroup;
                }
            }
            
            if (strSubCategory.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSubCategory;
                } else {
                    whereClause = whereClause + " AND " + strSubCategory;
                }
            }
            
            if (strItemType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strItemType;
                } else {
                    whereClause = whereClause + " AND " + strItemType;
                }
            }
            
            if (strCodeRange.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strCodeRange;
                } else {
                    whereClause = whereClause + " AND " + strCodeRange;
                }
            }
              if (strGroupTypeItem.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroupTypeItem;
                } else {
                    whereClause = whereClause + " AND " + strGroupTypeItem;
                }
            }
              
              if (strPriceType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strPriceType;
                } else {
                    whereClause = whereClause + " AND " + strPriceType;
                }
            }

            //query update harga jual pada katalog
              String strfromDate = Formater.formatDate(srcmaterial.getDateFrom(), "yyyy-MM-dd 00:00:00");
              String strtoDate = Formater.formatDate(srcmaterial.getDateTo(), "yyyy-MM-dd 23:59:59");
              if (srcmaterial.getShowUpdateCatalog()== 1) {
                   // whereClause = whereClause;
              //}
                  if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
               }
           }
           else {
                 whereClause = whereClause;
           }
           //if (srcmaterial.getShowUpdateCatalog()== 1) {
              //get name material > 2
               if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2" ;
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2";
               }
            //}

               

            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            switch (srcmaterial.getSortby()) {
                case 0:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                case 1:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                case 2:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
            }
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                    break;
                    
                case DBHandler.DBSVR_SYBASE:
                    break;
                    
                case DBHandler.DBSVR_ORACLE:
                    break;
                    
                case DBHandler.DBSVR_MSSQL:
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            
            while (rs.next()) {
                Vector temp = new Vector();
                Material material = new Material();
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                ContactList cnt = new ContactList();
                
                
                material.setOID(rs.getLong(1));
                material.setSku(rs.getString(2));
                material.setBarCode(rs.getString(3));
                material.setName(rs.getString(4));
                material.setDefaultSupplierType(rs.getInt(5));
                material.setDefaultPrice(rs.getDouble(6));
                material.setDefaultCost(rs.getDouble(7));
                temp.add(material);
                
                category.setName(rs.getString(8));
                category.setCode(rs.getString(9));
                temp.add(category);
                
                //subCategory.setName(rs.getString(10));
                //subCategory.setCode(rs.getString(11));
                temp.add(subCategory);
                
                Merk merk = new Merk();
                merk.setName(rs.getString("MERKNM"));
                temp.add(merk);
                
                PriceTypeMapping priceTypeMapping = new PriceTypeMapping();
                priceTypeMapping.setPrice(rs.getDouble(10));
                temp.add(priceTypeMapping);
                //
                //cnt.setCompName(rs.getString(12));
                //cnt.setContactCode(rs.getString(13));
                //temp.add(cnt);
                
                result.add(temp);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Err get list opname material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
     
        /*
         * Get list PriceType 
         * by Mirahu
         * 20120420 
         */
        public static Vector listPriceType(SrcMaterial srcMaterial) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT PRC." + PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] +
                         " ,PRC." + PstPriceType.fieldNames[PstPriceType.FLD_CODE] +
                         " ,PRC." + PstPriceType.fieldNames[PstPriceType.FLD_NAME] +
                         " FROM " + PstPriceType.TBL_POS_PRICE_TYPE + " PRC ";
            
             String strPriceType = "";
	    if (srcMaterial.getPriceTypeId() != null && srcMaterial.getPriceTypeId().size() > 0) {
		for (int b = 0; b < srcMaterial.getPriceTypeId().size(); b++) {
		    if (strPriceType.length() != 0) {
			strPriceType = strPriceType + " OR " + "(PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " =" + srcMaterial.getPriceTypeId().get(b) + ")";
		    } else {
			strPriceType = "(PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " =" + srcMaterial.getPriceTypeId().get(b) + ")";
		    }
		}
		strPriceType = "(" + strPriceType + ")";
	    }
            
            String whereClause = "";
            
             if (strPriceType.length() > 0) {
                    whereClause = " WHERE " + strPriceType;
                
            }
             
             sql = sql + whereClause;
          
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PriceType priceType = new PriceType();
                
                priceType.setOID(rs.getLong(1));
                priceType.setCode(rs.getString(2));
                priceType.setName(rs.getString(3));
                lists.add(priceType);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static Vector getListPointMaterial(int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]+
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT];
            
            sql = sql + " FROM ((" + PstMaterial.TBL_MATERIAL +
                    " MAT INNER JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")";
            
            /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
            " SCAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
            " ) ";*/
            
            sql = sql + " WHERE MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] + " > 0 " +
                    " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] + " DESC " +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                    break;
                    
                case DBHandler.DBSVR_SYBASE:
                    break;
                    
                case DBHandler.DBSVR_ORACLE:
                    break;
                    
                case DBHandler.DBSVR_MSSQL:
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            
            while (rs.next()) {
                Vector temp = new Vector();
                Material material = new Material();
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                ContactList cnt = new ContactList();
                
                material.setOID(rs.getLong(1));
                material.setSku(rs.getString(2));
                material.setBarCode(rs.getString(3));
                material.setName(rs.getString(4));
                material.setDefaultSupplierType(rs.getInt(5));
                material.setDefaultPrice(rs.getDouble(6));
                material.setDefaultCost(rs.getDouble(7));
                material.setMinimumPoint(rs.getInt(12));
                temp.add(material);
                
                category.setName(rs.getString(8));
                category.setCode(rs.getString(9));
                temp.add(category);
                
                //subCategory.setName(rs.getString(10));
                //subCategory.setCode(rs.getString(11));
                temp.add(subCategory);
                //
                //cnt.setCompName(rs.getString(12));
                //cnt.setContactCode(rs.getString(13));
                //temp.add(cnt);
                
                result.add(temp);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Err get list point material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static int getCountSearch(SrcMaterial srcmaterial) {
        String strCode = srcmaterial.getMatcode();
        Vector vectMaterialCode = new Vector(1,1);
        vectMaterialCode.add(strCode);
        
        String strName = srcmaterial.getMatname();
        Vector vectMaterialName = new Vector();
        vectMaterialName.add(strName);
        /*Vector vectMaterialCode = LogicParser.textSentence(strCode);
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }
         
        String strName = srcmaterial.getMatname();
        Vector vectMaterialName = LogicParser.textSentence(strName);
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        } */
        
        return getCountListMaterial(vectMaterialCode, vectMaterialName, srcmaterial);
    }
    
     public static int getCountSearchRepostingStok(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        String strCode = srcMaterialRepostingStock.getMatcode();
        Vector vectMaterialCode = new Vector(1,1);
        vectMaterialCode.add(strCode);
        
        String strName = srcMaterialRepostingStock.getMatname();
        Vector vectMaterialName = new Vector();
        vectMaterialName.add(strName);
        /*Vector vectMaterialCode = LogicParser.textSentence(strCode);
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }
         
        String strName = srcmaterial.getMatname();
        Vector vectMaterialName = LogicParser.textSentence(strName);
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        } */
        
        return getCountListMaterialRepostingStok(vectMaterialCode, vectMaterialName, srcMaterialRepostingStock);
    }
    
    public static int getCountSearchPriceTag(SrcMaterial srcmaterial) {
        String strCode = srcmaterial.getMatcode();
        Vector vectMaterialCode = new Vector(1,1);
        vectMaterialCode.add(strCode);
        
        String strName = srcmaterial.getMatname();
        Vector vectMaterialName = new Vector();
        vectMaterialName.add(strName);
        /*Vector vectMaterialCode = LogicParser.textSentence(strCode);
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }
         
        String strName = srcmaterial.getMatname();
        Vector vectMaterialName = LogicParser.textSentence(strName);
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        } */
        
        return getCountListMaterial(vectMaterialCode, vectMaterialName, srcmaterial);
    }
    
    
    public static int getCountListMaterial(Vector vtMaterialCode, Vector vtMaterialName, SrcMaterial srcmaterial) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ) AS CNT FROM (" + PstMaterial.TBL_MATERIAL +
                    " MAT LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")";
            /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
            " SCAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            if (srcmaterial.getSupplierId() != -1) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VDR " +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                        " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            }
            //            if(srcmaterial.getMerkId()!=0){
            sql = sql + " LEFT JOIN " + PstMerk.TBL_MAT_MERK + " AS MERK " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                    " = MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID];
            //            }
            
            String strMaterialCode = "";
            if (srcmaterial.getJenisCode() == 0) {
                if (vtMaterialCode != null && vtMaterialCode.size() > 0) {
                    for (int a = 0; a < vtMaterialCode.size(); a++) {
                        if (strMaterialCode.length() == 0) {
                            strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        } else {
                            strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        }
                    }
                    strMaterialCode = "( " + strMaterialCode + " )";
                }
            } else {
                if (srcmaterial.getMatcode().length() > 0) {
                    strMaterialCode = " SUBSTRING(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",3,3) = '" + srcmaterial.getMatcode() + "'";
                }
                if (srcmaterial.getCodeShip().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcmaterial.getCodeShip() + "'";
                    else
                        strMaterialCode = " LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcmaterial.getCodeShip() + "'";
                }
                if (srcmaterial.getCodeCounter().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcmaterial.getCodeCounter().length() + ") = '" + srcmaterial.getCodeCounter() + "'";
                    else
                        strMaterialCode = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcmaterial.getCodeCounter().length() + ") = '" + srcmaterial.getCodeCounter() + "'";
                }
                
                
                if (strMaterialCode.length() > 0)
                    strMaterialCode = "(" + strMaterialCode + ")";
            }
            
            
            String strMaterialName = "";
            if (vtMaterialName != null && vtMaterialName.size() > 0) {
                for (int a = 0; a < vtMaterialName.size(); a++) {
                    if (strMaterialName.length() == 0) {
                        strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    } else {
                        strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    }
                }
                strMaterialName = "( " + strMaterialName + " )";
            }
            
            String strSupplier = "";
            if (srcmaterial.getSupplierId() != -1) {
                strSupplier = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + srcmaterial.getSupplierId();
            }
            
            String strMerk = "";
            if (srcmaterial.getMerkId() != -1) {
                strMerk = " MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] +
                        " = " + srcmaterial.getMerkId();
            }
            
            String strGroup = "";
            if (srcmaterial.getCategoryId() != -1) {
                strGroup = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcmaterial.getCategoryId();
            }
            
            String strSubCategory = "";
            if (srcmaterial.getSubCategoryId() != -1) {
                //strSubCategory = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                //        " = " + srcmaterial.getSubCategoryId();
            }
            
            String strItemType = "";
            if (srcmaterial.getTypeItem() != -1) {
                if (srcmaterial.getTypeItem() == 0) {//not for gift
                    //                    strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                    //                    " = 0 ";
                } else {// for gift
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                            " > 0 ";
                }
            }
            
            String strCodeRange = "";
            if (srcmaterial.getOidCodeRange() != 0) {
                try{
                    CodeRange codeRange = PstCodeRange.fetchExc(srcmaterial.getOidCodeRange());
                    strCodeRange = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            " BETWEEN '" +codeRange.getFromRangeCode()+"' AND '"+codeRange.getToRangeCode()+"'";
                }catch(Exception e){}
            }

            //group Item
            String strGroupTypeItem = "";
            if (srcmaterial.getGroupItem() != -1) {
                if (srcmaterial.getGroupItem() == PstMaterial.MATERIAL_TYPE_REGULAR) { // for material
                    strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_REGULAR;
                } else if (srcmaterial.getGroupItem() == PstMaterial.MAT_TYPE_COMPOSITE) {// for composite
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_COMPOSITE;
                } else if (srcmaterial.getGroupItem() == PstMaterial.MAT_TYPE_SERVICE) {// for service
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_SERVICE;
                }
            }
            
            String whereClause = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if (strMaterialCode.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialCode;
                } else {
                    whereClause = whereClause + " AND " + strMaterialCode;
                }
            }
            
            if (strMaterialName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialName;
                } else {
                    whereClause = whereClause + " AND " + strMaterialName;
                }
            }
            
            if (strSupplier.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSupplier;
                } else {
                    whereClause = whereClause + " AND " + strSupplier;
                }
            }
            
            if (strMerk.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMerk;
                } else {
                    whereClause = whereClause + " AND " + strMerk;
                }
            }
            
            if (strGroup.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroup;
                } else {
                    whereClause = whereClause + " AND " + strGroup;
                }
            }
            
            if (strSubCategory.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSubCategory;
                } else {
                    whereClause = whereClause + " AND " + strSubCategory;
                }
            }
            
            if (strItemType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strItemType;
                } else {
                    whereClause = whereClause + " AND " + strItemType;
                }
            }
            
            if (strCodeRange.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strCodeRange;
                } else {
                    whereClause = whereClause + " AND " + strCodeRange;
                }
            }

            if (strGroupTypeItem.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroupTypeItem;
                } else {
                    whereClause = whereClause + " AND " + strGroupTypeItem;
                }
            }




            String strfromDate = Formater.formatDate(srcmaterial.getDateFrom(), "yyyy-MM-dd 00:00:00");
              String strtoDate = Formater.formatDate(srcmaterial.getDateTo(), "yyyy-MM-dd 23:59:59");
              if (srcmaterial.getShowUpdateCatalog()== 1) {
                   // whereClause = whereClause;
              //}
                  if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
               }
           }
           else {
                 whereClause = whereClause;
           }
            //if (srcmaterial.getShowUpdateCatalog()== 1) {
               //get name material > 2
               if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2" ;
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2";
               }
           // }

            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Count get list material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    public static int getCountListMaterialRepostingStok(Vector vtMaterialCode, Vector vtMaterialName, SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ) AS CNT FROM (" + PstMaterial.TBL_MATERIAL +
                    " MAT LEFT JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")";
            /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
            " SCAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            if (srcMaterialRepostingStock.getSupplierId() != -1) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VDR " +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                        " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                        " ON VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            }
            //            if(srcmaterial.getMerkId()!=0){
            sql = sql + " LEFT JOIN " + PstMerk.TBL_MAT_MERK + " AS MERK " +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                    " = MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID];
            //            }
            
            String strMaterialCode = "";
            if (srcMaterialRepostingStock.getJenisCode() == 0) {
                if (vtMaterialCode != null && vtMaterialCode.size() > 0) {
                    for (int a = 0; a < vtMaterialCode.size(); a++) {
                        if (strMaterialCode.length() == 0) {
                            strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        } else {
                            strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + vtMaterialCode.get(a) + "%'";
                            strMaterialCode += " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + vtMaterialCode.get(a) + "%')";
                        }
                    }
                    strMaterialCode = "( " + strMaterialCode + " )";
                }
            } else {
                if (srcMaterialRepostingStock.getMatcode().length() > 0) {
                    strMaterialCode = " SUBSTRING(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",3,3) = '" + srcMaterialRepostingStock.getMatcode() + "'";
                }
                if (srcMaterialRepostingStock.getCodeShip().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcMaterialRepostingStock.getCodeShip() + "'";
                    else
                        strMaterialCode = " LEFT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",2) = '" + srcMaterialRepostingStock.getCodeShip() + "'";
                }
                if (srcMaterialRepostingStock.getCodeCounter().length() > 0) {
                    if (strMaterialCode.length() > 0)
                        strMaterialCode = strMaterialCode + " AND RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcMaterialRepostingStock.getCodeCounter().length() + ") = '" + srcMaterialRepostingStock.getCodeCounter() + "'";
                    else
                        strMaterialCode = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," + srcMaterialRepostingStock.getCodeCounter().length() + ") = '" + srcMaterialRepostingStock.getCodeCounter() + "'";
                }
                
                
                if (strMaterialCode.length() > 0)
                    strMaterialCode = "(" + strMaterialCode + ")";
            }
            
            
            String strMaterialName = "";
            if (vtMaterialName != null && vtMaterialName.size() > 0) {
                for (int a = 0; a < vtMaterialName.size(); a++) {
                    if (strMaterialName.length() == 0) {
                        strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    } else {
                        strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + vtMaterialName.get(a) + "%')";
                    }
                }
                strMaterialName = "( " + strMaterialName + " )";
            }
            
            String strSupplier = "";
            if (srcMaterialRepostingStock.getSupplierId() != -1) {
                strSupplier = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + srcMaterialRepostingStock.getSupplierId();
            }
            
            String strMerk = "";
            if (srcMaterialRepostingStock.getMerkId() != -1) {
                strMerk = " MERK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] +
                        " = " + srcMaterialRepostingStock.getMerkId();
            }
            
            String strGroup = "";
            if (srcMaterialRepostingStock.getCategoryId() != -1) {
                strGroup = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcMaterialRepostingStock.getCategoryId();
            }
            
            String strSubCategory = "";
            if (srcMaterialRepostingStock.getSubCategoryId() != -1) {
                //strSubCategory = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                //        " = " + srcmaterial.getSubCategoryId();
            }
            
            String strItemType = "";
            if (srcMaterialRepostingStock.getTypeItem() != -1) {
                if (srcMaterialRepostingStock.getTypeItem() == 0) {//not for gift
                    //                    strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                    //                    " = 0 ";
                } else {// for gift
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] +
                            " > 0 ";
                }
            }
            
            String strCodeRange = "";
            if (srcMaterialRepostingStock.getOidCodeRange() != 0) {
                try{
                    CodeRange codeRange = PstCodeRange.fetchExc(srcMaterialRepostingStock.getOidCodeRange());
                    strCodeRange = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            " BETWEEN '" +codeRange.getFromRangeCode()+"' AND '"+codeRange.getToRangeCode()+"'";
                }catch(Exception e){}
            }

            //group Item
            String strGroupTypeItem = "";
            if (srcMaterialRepostingStock.getGroupItem() != -1) {
                if (srcMaterialRepostingStock.getGroupItem() == PstMaterial.MATERIAL_TYPE_REGULAR) { // for material
                    strItemType = " MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_REGULAR;
                } else if (srcMaterialRepostingStock.getGroupItem() == PstMaterial.MAT_TYPE_COMPOSITE) {// for composite
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_COMPOSITE;
                } else if (srcMaterialRepostingStock.getGroupItem() == PstMaterial.MAT_TYPE_SERVICE) {// for service
                    strItemType = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_SERVICE;
                }
            }
            
            String whereClause = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
            
            if (strMaterialCode.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialCode;
                } else {
                    whereClause = whereClause + " AND " + strMaterialCode;
                }
            }
            
            if (strMaterialName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialName;
                } else {
                    whereClause = whereClause + " AND " + strMaterialName;
                }
            }
            
            if (strSupplier.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSupplier;
                } else {
                    whereClause = whereClause + " AND " + strSupplier;
                }
            }
            
            if (strMerk.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMerk;
                } else {
                    whereClause = whereClause + " AND " + strMerk;
                }
            }
            
            if (strGroup.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroup;
                } else {
                    whereClause = whereClause + " AND " + strGroup;
                }
            }
            
            if (strSubCategory.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSubCategory;
                } else {
                    whereClause = whereClause + " AND " + strSubCategory;
                }
            }
            
            if (strItemType.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strItemType;
                } else {
                    whereClause = whereClause + " AND " + strItemType;
                }
            }
            
            if (strCodeRange.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strCodeRange;
                } else {
                    whereClause = whereClause + " AND " + strCodeRange;
                }
            }

            if (strGroupTypeItem.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroupTypeItem;
                } else {
                    whereClause = whereClause + " AND " + strGroupTypeItem;
                }
            }




            String strfromDate = Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd 00:00:00");
              String strtoDate = Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd 23:59:59");
              if (srcMaterialRepostingStock.getShowUpdateCatalog()== 1) {
                   // whereClause = whereClause;
              //}
                  if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_UPDATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
               }
           }
           else {
                 whereClause = whereClause;
           }
            //if (srcmaterial.getShowUpdateCatalog()== 1) {
               //get name material > 2
               if (whereClause.length() == 0) {
                      //whereClause = whereClause + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2" ;
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND LENGTH (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2";
               }
           // }

            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Count get list material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    public static int getCountListPointMaterial() {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ) AS CNT FROM (" + PstMaterial.TBL_MATERIAL +
                    " MAT INNER JOIN " + PstCategory.TBL_CATEGORY +
                    " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")";
            
            /*" INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
            " SCAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
            " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];*/
            
            sql = sql + " WHERE MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MINIMUM_POINT] + " > 0 ";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Count get list point material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    
    // ------------------- used to search material with multiple supplier ----------
    
    
    public static Vector searchMaterialWithMultiSupp(SrcMaterial srcmaterial, int start, int recordToGet) {
        Vector vectMaterialCode = LogicParser.textSentence(srcmaterial.getMatcode());
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }
        
        Vector vectMaterialName = LogicParser.textSentence(srcmaterial.getMatname());
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        }
        
        return getListMaterialWithMultiSupp(vectMaterialCode, vectMaterialName, srcmaterial, start, recordToGet);
    }
    
    
    /**
     * @param vtMaterialCode
     * @param vtMaterialName
     * @param srcmaterial
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector getListMaterialWithMultiSupp(Vector vtMaterialCode, Vector vtMaterialName,
            SrcMaterial srcmaterial, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    " ,CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    //" ,SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    " FROM ((" + PstMaterial.TBL_MATERIAL + " MAT" +
                    " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")" +
                    // " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    // " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    // " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + ")" +
                    " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VND" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = VND." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + ")" +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                    " ON VND." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strMaterialCode = "";
            if (vtMaterialCode != null && vtMaterialCode.size() > 0) {
                for (int a = 0; a < vtMaterialCode.size(); a++) {
                    if (strMaterialCode.length() == 0) {
                        strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vtMaterialCode.get(a) + "%')";
                    } else {
                        strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vtMaterialCode.get(a) + "%')";
                    }
                }
                strMaterialCode = "( " + strMaterialCode + " )";
            }
            
            String strMaterialName = "";
            if (vtMaterialName != null && vtMaterialName.size() > 0) {
                for (int a = 0; a < vtMaterialName.size(); a++) {
                    if (strMaterialName.length() == 0) {
                        strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '" + vtMaterialName.get(a) + "%')";
                    } else {
                        strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '" + vtMaterialName.get(a) + "%')";
                    }
                }
                strMaterialName = "( " + strMaterialName + " )";
            }
            
            String strSupplier = "";
            if (srcmaterial.getSupplierId() != -1) {
                strSupplier = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                        " = " + srcmaterial.getSupplierId();
            }
            
            String strGroup = "";
            if (srcmaterial.getCategoryId() != -1) {
                strGroup = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcmaterial.getCategoryId();
            }
            
            String strSubCategory = "";
            if (srcmaterial.getSubCategoryId() != -1) {
                //strSubCategory = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                //        " = " + srcmaterial.getSubCategoryId();
            }
            
            String whereClause = "";
            
            if (strMaterialCode.length() > 0)
                whereClause = strMaterialCode;
            
            if (strMaterialName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialName;
                } else {
                    whereClause = whereClause + " AND " + strMaterialName;
                }
            }
            
            if (strSupplier.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSupplier;
                } else {
                    whereClause = whereClause + " AND " + strSupplier;
                }
            }
            
            if (strGroup.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroup;
                } else {
                    whereClause = whereClause + " AND " + strGroup;
                }
            }
            
            if (strSubCategory.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSubCategory;
                } else {
                    whereClause = whereClause + " AND " + strSubCategory;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            switch (srcmaterial.getSortby()) {
                case 0:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                case 1:
                    sql = sql + " ORDER BY CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                case 2:
                    sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
            }
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                    break;
                    
                case DBHandler.DBSVR_SYBASE:
                    break;
                    
                case DBHandler.DBSVR_ORACLE:
                    break;
                    
                case DBHandler.DBSVR_MSSQL:
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            
            while (rs.next()) {
                Vector temp = new Vector();
                Material material = new Material();
                Category category = new Category();
                SubCategory subCategory = new SubCategory();
                ContactList cnt = new ContactList();
                
                material.setOID(rs.getLong(1));
                material.setSku(rs.getString(2));
                material.setBarCode(rs.getString(3));
                material.setName(rs.getString(4));
                material.setDefaultSupplierType(rs.getInt(5));
                material.setDefaultPrice(rs.getDouble(6));
                material.setDefaultCost(rs.getDouble(7));
                temp.add(material);
                
                category.setName(rs.getString(8));
                category.setCode(rs.getString(9));
                temp.add(category);
                
                //subCategory.setName(rs.getString(10));
                //subCategory.setCode(rs.getString(11));
                temp.add(subCategory);
                
                cnt.setCompName(rs.getString(10));
                cnt.setContactCode(rs.getString(11));
                temp.add(cnt);
                
                result.add(temp);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Err get list opname material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * @param srcmaterial
     * @return
     */
    public static int getCountSearchWithMultiSupp(SrcMaterial srcmaterial) {
        Vector vectMaterialCode = LogicParser.textSentence(srcmaterial.getMatcode());
        for (int i = 0; i < vectMaterialCode.size(); i++) {
            String code = (String) vectMaterialCode.get(i);
            if ((code.equals(LogicParser.SIGN)) || (code.equals(LogicParser.ENGLISH[0])))
                vectMaterialCode.remove(i);
        }
        
        Vector vectMaterialName = LogicParser.textSentence(srcmaterial.getMatname());
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        }
        
        return getCountListMaterialWithMultiSupp(vectMaterialCode, vectMaterialName, srcmaterial);
    }
    
    
    /**
     * @param vtMaterialCode
     * @param vtMaterialName
     * @param srcmaterial
     * @return
     */
    public static int getCountListMaterialWithMultiSupp(Vector vtMaterialCode, Vector vtMaterialName, SrcMaterial srcmaterial) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            /*
            String sql = "SELECT COUNT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                " ) AS CNT FROM (" + PstMaterial.TBL_MATERIAL +
                " MAT LEFT JOIN " + PstCategory.TBL_CATEGORY +
                " CAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")" +
                " LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY +
                " SCAT ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];
             */
            
            String sql = "SELECT COUNT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ")" +
                    " FROM ((" + PstMaterial.TBL_MATERIAL + " MAT" +
                    " LEFT JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ")" +
                    " LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " SCAT" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    " = SCAT." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + ")" +
                    " LEFT JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " VND" +
                    " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = VND." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + ")" +
                    " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT" +
                    " ON VND." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            String strMaterialCode = "";
            if (vtMaterialCode != null && vtMaterialCode.size() > 0) {
                for (int a = 0; a < vtMaterialCode.size(); a++) {
                    if (strMaterialCode.length() == 0) {
                        strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vtMaterialCode.get(a) + "%')";
                    } else {
                        strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + vtMaterialCode.get(a) + "%')";
                    }
                }
                strMaterialCode = "( " + strMaterialCode + " )";
            }
            
            String strMaterialName = "";
            if (vtMaterialName != null && vtMaterialName.size() > 0) {
                for (int a = 0; a < vtMaterialName.size(); a++) {
                    if (strMaterialName.length() == 0) {
                        strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '" + vtMaterialName.get(a) + "%')";
                    } else {
                        strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '" + vtMaterialName.get(a) + "%')";
                    }
                }
                strMaterialName = "( " + strMaterialName + " )";
            }
            
            String strSupplier = "";
            if (srcmaterial.getSupplierId() != -1) {
                strSupplier = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                        " = " + srcmaterial.getSupplierId();
            }
            
            String strGroup = "";
            if (srcmaterial.getCategoryId() != -1) {
                strGroup = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + srcmaterial.getCategoryId();
            }
            
            String strSubCategory = "";
            if (srcmaterial.getSubCategoryId() != -1) {
                strSubCategory = " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                        " = " + srcmaterial.getSubCategoryId();
            }
            
            String whereClause = "";
            
            if (strMaterialCode.length() > 0)
                whereClause = strMaterialCode;
            
            if (strMaterialName.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strMaterialName;
                } else {
                    whereClause = whereClause + " AND " + strMaterialName;
                }
            }
            
            if (strSupplier.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSupplier;
                } else {
                    whereClause = whereClause + " AND " + strSupplier;
                }
            }
            
            if (strGroup.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strGroup;
                } else {
                    whereClause = whereClause + " AND " + strGroup;
                }
            }
            
            if (strSubCategory.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + strSubCategory;
                } else {
                    whereClause = whereClause + " AND " + strSubCategory;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Count get list material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    
    /**
     * untuk pengecekan barang apakah sudah pernah terjadi transaksi
     * jika ya maka return false, sebaliknya
     * @return
     */
    public static boolean readyDataToDelete(long materialId) {
        boolean status = true;
        try {
            // pengecekan di item PO
            String where = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_MATERIAL_ID] + "=" + materialId;
            Vector vlist = PstPurchaseOrderItem.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            } else {
                // pengecekan di penerimaan
                where = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + "=" + materialId;
                vlist = PstMatReceiveItem.list(0, 0, where, "");
                if (vlist != null && vlist.size() > 0) {
                    status = false;
                } else {
                    // pengecekan di return
                    where = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + materialId;
                    vlist = PstMatReturnItem.list(0, 0, where, "");
                    if (vlist != null && vlist.size() > 0) {
                        status = false;
                    } else {
                        // pengecekan di dispatch
                        where = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + "=" + materialId;
                        vlist = PstMatDispatchItem.list(0, 0, where, "");
                        if (vlist != null && vlist.size() > 0) {
                            status = false;
                        } else {
                            // pengecekan di costing
                            where = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + "=" + materialId;
                            vlist = PstMatCostingItem.list(0, 0, where, "");
                            if (vlist != null && vlist.size() > 0) {
                                status = false;
                            } else {
                                // pengecekan di opname barang
                                where = PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + materialId;
                                vlist = PstMatStockOpnameItem.list(0, 0, where, "");
                                if (vlist != null && vlist.size() > 0) {
                                    status = false;
                                } else {
                                    // pengecekan di penjualan barang
                                    where = PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + "=" + materialId;
                                    vlist = PstBillDetail.list(0, 0, where, "");
                                    if (vlist != null && vlist.size() > 0) {
                                        status = false;
                                    } /*else {
                                        // pengecekan di stock barang
                                        where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + "=" + materialId;
                                        vlist = PstMaterialStock.list(0, 0, where, "");
                                        if (vlist != null && vlist.size() > 0) {
                                            status = false;
                                        }
                                    }*/
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return status;
    }
    
    /**
     * @return
     */
    public static Vector getDoubleGoodsOnOpname() {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SO.STOCK_OPNAME_NUMBER, SOI.MATERIAL_ID, MAT. NAME " +
                    "FROM POS_STOCK_OPNAME AS SO " +
                    "INNER JOIN POS_STOCK_OPNAME_ITEM SOI " +
                    "ON SO.STOCK_OPNAME_ID = SOI.STOCK_OPNAME_ID " +
                    "INNER JOIN POS_MATERIAL MAT " +
                    "ON SOI.MATERIAL_ID = MAT.MATERIAL_ID " +
                    "ORDER BY SOI.MATERIAL_ID";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                vectTemp.add(rs.getString(1));
                vectTemp.add(rs.getString(2));
                vectTemp.add(rs.getString(3));
                
                result.add(vectTemp);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Count get list material : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    
    /* get sales price from price mapping */
    public static double getPriceSale(Material material) {
        double priceSale = 0;
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT (PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE] +
                    " * STD." + PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE] +
                    ") FROM " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + " AS PRC " +
                    " INNER JOIN " + PstStandartRate.TBL_POS_STANDART_RATE + " AS STD " +
                    " ON PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] +
                    " = STD." + PstStandartRate.fieldNames[PstStandartRate.FLD_STANDART_RATE_ID] +
                    " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS CURR " +
                    " ON STD." + PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] +
                    " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                    " INNER JOIN " + PstPriceType.TBL_POS_PRICE_TYPE + " AS PRT " +
                    " ON PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] +
                    " = PRT." + PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] +
                    " INNER JOIN " + PstMemberGroup.TBL_MEMBER_GROUP + " AS MBR " +
                    " ON PRT." + PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] +
                    " = MBR." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_PRICE_TYPE_ID] +
                    " WHERE PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                    " = " + material.getOID() +
                    " AND CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX] +
                    " = 1 " +
                    " AND MBR." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                    " = " + PstMemberGroup.UMUM;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                priceSale = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err price sale : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return priceSale;
        }
    }
    
    /**
     * Fungsi ini digunakan untuk pengecekan data qty po berdasarkan oid material
     * @param list
     * @param oidMaterial
     * @return
     */
    public static double getOidMaterialByVector(Vector list, long oidMaterial){
        double qty = 0;
        try{
            if(list!=null && list.size()>0){
                for(int k=0;k<list.size();k++){
                    Vector v1 = (Vector)list.get(k);
                    long oidmat = Long.parseLong((String)v1.get(0));
                    if(oidMaterial == oidmat){
                        qty = Double.parseDouble((String)v1.get(1));
                        break;
                    }
                }
            }
        }catch(Exception e){}
        return qty;
    }
    
    public static Vector getListItemForPO(Vector v1){
        DBResultSet dbrs = null;
        Vector v2 = new Vector();
        try{
            String sql = "SELECT "+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            sql += ", "+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            sql += ", "+PstMaterial.fieldNames[PstMaterial.FLD_NAME];
            sql += ", "+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID];
            sql += " FROM "+PstMaterial.TBL_MATERIAL;
            String where ="";
            if(v1!=null && v1.size()>0){
                for(int i=0;i<v1.size();i++){
                    Vector list = (Vector)v1.get(i);
                    long materialOid = Long.parseLong((String)list.get(0));
                    if(where.length() > 0){
                        where = where +" OR ("+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+materialOid+")";
                    }else{
                        where = "("+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+materialOid+")";
                    }
                }
            }
            
            if(where.length() > 0)
                sql = sql + " WHERE " +where;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector v3 = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                v3.add(material);
                
                double qty = getOidMaterialByVector(v1,material.getOID());
                v3.add(String.valueOf(qty));
                
                v2.add(v3);
            }
        }catch(Exception e){
            System.out.println("Errr getListItemForPO : "+e.toString());
        }
        return v2;
    }


 /**
     * @param srcDiscountQtyMapping
     * @return
     * @created and modified by Mirah
     */
    public static Vector getDiscountQtyMapping(SrcMaterial srcmaterial, long materialId) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
              String sql = "SELECT " +
                         " DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID]+
                         ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID]+
                         ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID]+
                         ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]+
                         //" CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
                         //", DISC." + PstDiscountType.fieldNames[PstDiscountType.FLD_CODE] +
                         //", LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE] +
                         //", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                         ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_START_QTY] +
                         ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_TO_QTY] +
                         ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_VALUE]+
                         ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE] +
                         " FROM " + PstDiscountQtyMapping.TBL_POS_DISCOUNT_QTY_MAPPING + " DISCQTY " +
                         " INNER JOIN " + com.dimata.common.entity.payment.PstDiscountType.TBL_POS_DISCOUNT_TYPE+ " DISC " +
                         " ON DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID] +
                         " = DISC." + com.dimata.common.entity.payment.PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID] +
                         " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE+ " CURR " +
                         " ON DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID] +
                         " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION+ " LOC " +
                         " ON DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID] +
                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " MAT " +
                         " ON DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

             // String strMaterialId = "";
                //strMaterialId = " DISCQTY. "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]+"="+materialId;

            String strMemberTypeId = "";
                if (srcmaterial.getMemberTypeId() != null && srcmaterial.getMemberTypeId().size() > 0) {
                    for (int b = 0; b < srcmaterial.getMemberTypeId().size(); b++) {
                        if (strMemberTypeId.length() != 0) {
                            strMemberTypeId = strMemberTypeId + " OR " + "(DISCQTY ." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID] + " =" + srcmaterial.getMemberTypeId().get(b) + ")";
                        } else {
                            strMemberTypeId = "(DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID] + " =" + srcmaterial.getMemberTypeId().get(b) + ")";
                        }
		}
               }

            String strDate = "";
            if ((srcmaterial.getDateFrom() != null) && (srcmaterial.getDateTo() != null)) {
                String startDate = Formater.formatDate(srcmaterial.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcmaterial.getDateTo(), "yyyy-MM-dd");
                strDate = " DISCQTY." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + startDate + " 00:00:01' AND '" + endDate + " 23:23:59'";
            }

            String whereClause = "";

            //where strMemberTypeId
            if (strMemberTypeId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strMemberTypeId;
                } else {
                    whereClause = whereClause + strMemberTypeId;
                }
            }

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }


            sql += " WHERE DISCQTY. "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]+"="+materialId;

            if (whereClause.length() > 0) {
                sql = sql + " AND " + whereClause;
            }


            sql = sql + " ORDER BY LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE] +
                    ", DISC." + com.dimata.common.entity.payment.PstDiscountType.fieldNames[PstDiscountType.FLD_CODE]+
                    ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
                    ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE];

            System.out.println("SQL Lap.DiscountQtyMapping : " +sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                DiscountQtyMapping discountqtymapping = new DiscountQtyMapping();

                discountqtymapping.setDiscountTypeId(rs.getLong(1));
                discountqtymapping.setCurrencyTypeId(rs.getLong(2));
                discountqtymapping.setLocationId(rs.getLong(3));
                discountqtymapping.setMaterialId(rs.getLong(4));
                discountqtymapping.setStartQty(rs.getDouble(5));
                discountqtymapping.setToQty(rs.getDouble(6));
                discountqtymapping.setDiscountValue(rs.getDouble(7));
                discountqtymapping.setDiscountType(rs.getInt(8));
                result.add(discountqtymapping);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
}
