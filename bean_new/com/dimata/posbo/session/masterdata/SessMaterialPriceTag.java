/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.CodeRange;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstCodeRange;
import com.dimata.posbo.entity.masterdata.PstMatVendorPrice;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.masterdata.SubCategory;
import com.dimata.posbo.entity.search.SrcMaterial;

import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SessMaterialPriceTag {
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
        if(!strName.equals("")){
            vectMaterialName.add(strName);
        }
        /*Vector vectMaterialName = LogicParser.textSentence(strName);
        for (int i = 0; i < vectMaterialName.size(); i++) {
            String name = (String) vectMaterialName.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectMaterialName.remove(i);
        }*/
        return getListMaterialPriceTag(vectMaterialCode, vectMaterialName, srcmaterial, start, recordToGet, oidPriceType);
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
                if (srcmaterial.getvBarcode() != null && srcmaterial.getvBarcode().size() > 0) {
                    for (int a = 0; a < srcmaterial.getvBarcode().size(); a++) {
                        if (strMaterialCode.length() == 0) {
                            strMaterialCode = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + srcmaterial.getvBarcode().get(a) + "%'";
                            strMaterialCode += " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + srcmaterial.getvBarcode().get(a) + "%')";
                        } else {
                            strMaterialCode = strMaterialCode + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + srcmaterial.getvBarcode().get(a) + "%'";
                            strMaterialCode += " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + srcmaterial.getvBarcode().get(a) + "%')";
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
            if(srcmaterial.getvNameMaterial()!=null && srcmaterial.getvNameMaterial().size()>0){
                 for (int a = 0; a < srcmaterial.getvNameMaterial().size(); a++) {
                        if (strMaterialName.length() == 0) {
                            strMaterialName = "(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = '" + srcmaterial.getvNameMaterial().get(a) + "')";
                        } else {
                            strMaterialName = strMaterialName + " OR (MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = '" + srcmaterial.getvNameMaterial().get(a) + "')";
                        }
                    }
                    strMaterialName = "( " + strMaterialName + " )";
            }else{
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
            }
            
            String strSupplier = "";
            if (srcmaterial.getSupplierId() > 0) {
                strSupplier = " VDR." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + srcmaterial.getSupplierId();
            }

            String standartRate = "";
            if (srcmaterial.getSelectCurrencyTypeId()> 0) {
                standartRate = " PRC." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] +
                        " = " + srcmaterial.getSelectCurrencyTypeId();
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
                    whereClause = whereClause + " AND (" + strMaterialName+" )";
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

            if (standartRate.length() > 0) {
                if (whereClause.length() == 0) {
                    whereClause = whereClause + standartRate;
                } else {
                    whereClause = whereClause + " AND " + standartRate;
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
                      whereClause = whereClause + " LENGTH(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2" ;
                  } else {
                     //whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
                      whereClause = whereClause + " AND LENGTH(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ")>2";
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
}
