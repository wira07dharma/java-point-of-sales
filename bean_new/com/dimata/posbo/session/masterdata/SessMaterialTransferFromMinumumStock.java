/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.MatDispatch;
import com.dimata.posbo.entity.warehouse.MatDispatchItem;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatchItem;
import com.dimata.posbo.session.warehouse.SessMatDispatch;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SessMaterialTransferFromMinumumStock {

    public static Vector getListItemForTransfer(Vector v1) {
        DBResultSet dbrs = null;
        Vector v2 = new Vector();
        try {
            String sql = "SELECT " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            sql += ", " + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            sql += ", " + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
            sql += ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID];
            sql += " FROM " + PstMaterial.TBL_MATERIAL;
            String where = "";
            if (v1 != null && v1.size() > 0) {
                for (int i = 0; i < v1.size(); i++) {
                    Vector list = (Vector) v1.get(i);
                    long materialOid = Long.parseLong((String) list.get(0));
                    if (where.length() > 0) {
                        where = where + " OR (" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + materialOid + ")";
                    } else {
                        where = "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + materialOid + ")";
                    }
                }
            }

            if (where.length() > 0) {
                sql = sql + " WHERE " + where;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector v3 = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                v3.add(material);

                double qty = getOidMaterialByVector(v1, material.getOID());
                v3.add(String.valueOf(qty));

                v2.add(v3);
            }
        } catch (Exception e) {
            System.out.println("Errr getListItemForPO : " + e.toString());
        }
        return v2;
    }

    /**
     * Fungsi ini digunakan untuk pengecekan data qty po berdasarkan oid
     * material
     *
     * @param list
     * @param oidMaterial
     * @return
     */
    public static double getOidMaterialByVector(Vector list, long oidMaterial) {
        double qty = 0;
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidmat = Long.parseLong((String) v1.get(0));
                    if (oidMaterial == oidmat) {
                        qty = Double.parseDouble((String) v1.get(1));
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        return qty;
    }

    public static long autoInsertTransfer(Vector list) {
        long hasil = 0;
        try {
            //hasil di urutkan terlebih dahulu berdasarkan supplier
            Vector listOrderTransfer = structureListOrderLocation(list);

            //proses
            Vector vPO = getRekapForTransferMain(listOrderTransfer);
            if (vPO.size() > 0) {
                for (int i = 0; i < vPO.size(); i++) {
                    MatDispatch matDispatch = (MatDispatch) vPO.get(i);
                    int maxCounter = getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                    maxCounter = maxCounter + 1;
                    matDispatch.setDispatchCodeCounter(maxCounter);
                    matDispatch.setDispatchCode(generateDispatchCode(matDispatch));
                    Vector v1 = (Vector) matDispatch.getListItem();
                    long oidPo = PstMatDispatch .insertExc(matDispatch);
                    if (oidPo != 0) {
                        for (int k = 0; k < v1.size(); k++) {
                            MatDispatchItem matDispatchItem = (MatDispatchItem) v1.get(k);
                            matDispatchItem.setDispatchMaterialId(oidPo);
                            PstMatDispatchItem.insertExc(matDispatchItem);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("autoInsertPo : " + ex);
        }
        return hasil;
    }

    public static Vector structureListOrderLocation(Vector list) {

        if (list == null || list.size() < 1) {
            return new Vector();
        }

        Vector resultTotal = new Vector();
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidsupplier = Long.parseLong((String) v1.get(3));
                    long oidlokasi = Long.parseLong((String) v1.get(2));
                    if (resultTotal.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < resultTotal.size(); i++) {
                            bool = false;
                            Vector v2 = (Vector) resultTotal.get(i);
                            long oidsupplier2 = Long.parseLong((String) v2.get(3));
                            long oidlokasi2 = Long.parseLong((String) v2.get(2));
                            if ((oidsupplier == oidsupplier2) && (oidlokasi == oidlokasi2)) {
                                bool = true;
                                resultTotal.insertElementAt(v1, i + 1);
                                break;
                            }
                        }
                        if (!bool) {
                            resultTotal.add(v1);
                            //list.remove(k);
                        }
                    } else {
                        resultTotal.add(v1);
                        //list.remove(k);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return resultTotal;
    }

    public static Vector getRekapForTransferMain(Vector list) {
        Vector vpo = new Vector();
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidmaterial = Long.parseLong((String) v1.get(0));
                    double qty = Double.parseDouble((String) v1.get(1));
                    long oidDispatchTo = Long.parseLong((String) v1.get(2));
                    long oidlokasiSource = Long.parseLong((String) v1.get(3));
                    long oidUnit = ((Long) v1.get(4)).longValue();
                    if (vpo.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < vpo.size(); i++) {
                            bool = false;
                            MatDispatch matDispatch = (MatDispatch) vpo.get(i);
                            if ((matDispatch.getLocationId() == oidlokasiSource) && (matDispatch.getDispatchTo() == oidDispatchTo)) {
                                bool = true;
                                MatDispatchItem dispatchItem = new MatDispatchItem();
                                dispatchItem.setMaterialId(oidmaterial);
                                Material material = (Material) PstMaterial.fetchExc(oidmaterial);
                                dispatchItem.setUnitId(material.getBuyUnitId());
                                dispatchItem.setQty(qty);
                                dispatchItem.setHpp(material.getAveragePrice());
                                dispatchItem.setHppTotal(qty*material.getAveragePrice());
                                /*
                                poItem.setMaterialId(oidmaterial);
                                poItem.setQuantity(qty);
                                //MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                                poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                                poItem.setPrice(matVendorPrice.getCurrBuyingPrice());
                                poItem.setOrgBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                                poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                                poItem.setDiscount(matVendorPrice.getLastDiscount());
                                poItem.setTotal((qty * matVendorPrice.getCurrBuyingPrice()) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                                poItem.setCurBuyingPrice(matVendorPrice.getCurrBuyingPrice());
                                poItem.setUnitId(oidUnit);
                                purchOrder.setListItem(poItem);
                                */
                                matDispatch.setListItem(dispatchItem);
                                vpo.setElementAt(matDispatch, i);
                            }
                        }
                        
                        if (!bool) {
                            
                            /*PurchaseOrder mypo = new PurchaseOrder();
                            mypo.setPoStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            mypo.setPurchDate(new Date());
                            mypo.setSupplierId(oidsupplier);
                            mypo.setLocationId(oidlokasi);
                            mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                            mypo.setRemark("");*/
                            MatDispatch matDispatch = new MatDispatch();
                            matDispatch.setLocationId(oidlokasiSource);
                            matDispatch.setDispatchTo(oidDispatchTo);
                            matDispatch.setLocationType(1);
                            matDispatch.setDispatchDate(new Date());
                            matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            matDispatch.setRemark("Auto Document Transfer From Minimum Stock");
                            matDispatch.setInvoiceSupplier("");
                            
                           
                            
                            MatDispatchItem dispatchItem = new MatDispatchItem();
                            dispatchItem.setMaterialId(oidmaterial);
                            Material material = (Material) PstMaterial.fetchExc(oidmaterial);
                            dispatchItem.setUnitId(material.getBuyUnitId());
                            dispatchItem.setQty(qty);
                            dispatchItem.setHpp(material.getAveragePrice());
                            dispatchItem.setHppTotal(qty*material.getAveragePrice());
                            
                            matDispatch.setListItem(dispatchItem);
                            vpo.add(matDispatch);
                        }
                        
                    } else {
                        
                        MatDispatch matDispatch = new MatDispatch();
                        matDispatch.setLocationId(oidlokasiSource);
                        matDispatch.setDispatchTo(oidDispatchTo);
                        matDispatch.setLocationType(1);
                        matDispatch.setDispatchDate(new Date());
                        matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                        matDispatch.setRemark("Auto Document Transfer From Minimum Stock");
                        matDispatch.setInvoiceSupplier("");



                        MatDispatchItem dispatchItem = new MatDispatchItem();
                        dispatchItem.setMaterialId(oidmaterial);
                        Material material = (Material) PstMaterial.fetchExc(oidmaterial);
                        dispatchItem.setUnitId(material.getBuyUnitId());
                        dispatchItem.setQty(qty);
                        dispatchItem.setHpp(material.getAveragePrice());
                        dispatchItem.setHppTotal(qty*material.getAveragePrice());

                        matDispatch.setListItem(dispatchItem);
                        vpo.add(matDispatch);
                        
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return vpo;
    }
    
    
     public static int getMaxDispatchCounter(Date date, MatDispatch matDispatch) {
	int counter = 0;
	DBResultSet dbrs = null;
	try {

	    Date startDate = (Date) date.clone();
	    startDate.setDate(1);

	    Date endDate = (Date) date.clone();
	    endDate.setMonth(endDate.getMonth() + 1);
	    endDate.setDate(1);
	    endDate.setDate(endDate.getDate() - 1);

	    I_PstDocType i_PstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

	    String sql = "SELECT MAX(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE_COUNTER] + ") AS MAXIMUM " +
		    " FROM " + PstMatDispatch.TBL_DISPATCH +
		    " WHERE (" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
		    " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd 00:00:00") +
		    " ' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd 23:59:59") +
		    " ') AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + matDispatch.getLocationId() +
                    //For diferrent between code dispatch and dispatch unit
                    " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE] + " != " + PstMatDispatch.FLD_TYPE_TRANSFER_UNIT;

	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();

	    while (rs.next()) {
		counter = rs.getInt("MAXIMUM");
	    }

	    rs.close();
	} catch (Exception e) {
	    DBResultSet.close(dbrs);
	    System.out.println("Exception getMaxCounter : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return counter;
    }
    
     
    public static String generateDispatchCode(MatDispatch matDispatch) {
	// belum mengambl ke workflow, karena tidak di gunakan workflownya.
	String code = "DF";
	String dateCode = "";

	// penambahan baru request dari taiga: 18/03/2005
	String kodeLoc = ""; 
	try {
	    Location loc = PstLocation.fetchExc(matDispatch.getDispatchTo());
	    kodeLoc = loc.getCode();
	} catch (Exception e) {
	}
 
	if (matDispatch.getDispatchDate() != null) {
	    /** get location code; gwawan@21juni2007 */
	    Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=" + matDispatch.getLocationId(), "");
	    Location location = (Location) vctLocation.get(0);

	    int nextCounter = matDispatch.getDispatchCodeCounter();//getMaxCounter(date);
	    Date date = matDispatch.getDispatchDate();

	    int tgl = date.getDate();
	    int bln = date.getMonth() + 1;
	    int thn = date.getYear() + 1900;

	    dateCode = (String.valueOf(thn)).substring(2, 4);

	    if (bln < 10) {
		dateCode = dateCode + "0" + bln;
	    } else {
		dateCode = dateCode + bln;
	    }

	    if (tgl < 10) {
		dateCode = dateCode + "0" + tgl;
	    } else {
		dateCode = dateCode + tgl;
	    }

	    String counter = "";
	    if (nextCounter < 10) {
		counter = "00" + nextCounter;
	    } else {
		if (nextCounter < 100) {
		    counter = "0" + nextCounter;
		} else {
		    counter = "" + nextCounter;
		}
	    }
	    code = location.getCode() + "-" + dateCode + "-" + code + "-" + kodeLoc + "-" + counter;
	}
	return code;
    } 
     

}
