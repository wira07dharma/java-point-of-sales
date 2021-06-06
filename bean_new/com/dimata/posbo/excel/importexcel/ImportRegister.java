/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.excel.importexcel;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.hanoman.entity.masterdata.MasterGroup;
import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.hanoman.entity.masterdata.PstContactClassAssign;
import com.dimata.hanoman.entity.masterdata.PstMasterGroup;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.pos.entity.currency.Currency;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.posbo.entity.masterdata.Ksg;
import com.dimata.posbo.entity.masterdata.MasterGroupMapping;
import com.dimata.posbo.entity.masterdata.MatMappKsg;
import com.dimata.posbo.entity.masterdata.MatMappLocation;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.MaterialTypeMapping;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstColor;
import com.dimata.posbo.entity.masterdata.PstKsg;
import com.dimata.posbo.entity.masterdata.PstMasterGroupMapping;
import com.dimata.posbo.entity.masterdata.PstMatMappKsg;
import com.dimata.posbo.entity.masterdata.PstMatMappLocation;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialMappingType;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstSubCategory;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.SubCategory;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.form.masterdata.CtrlMatVendorPrice;
import com.dimata.util.Formater;
import com.dimata.util.blob.TextLoader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author dimata005
 */
public class ImportRegister {
	
	public static boolean running = false;
	
	public static double countTotalData = 0.0;
	
	public static double currentProgress = 0.0;
	
	public static String table = "";
	
    public static String drawImport(ServletConfig config, HttpServletRequest request, HttpServletResponse response, JspWriter output, int typeTemp){
        String html = "";
        int NUM_HEADER = 2;
        int NUM_CELL = 0;
        String tdColor = "#FFF;";
        
        long defaultSellUnitId = Long.valueOf(PstSystemProperty.getValueByName("DEFAULT_SELL_UNIT_ID"));
        long currencyId = Long.valueOf(PstSystemProperty.getValueByName("DEFAULT_PRICE_CURRENCY_ID"));
        String etalaseName = PstSystemProperty.getValueByName("DEFAULT_ETALASE_NAME_REGISTER");
        String etalaseCode = PstSystemProperty.getValueByName("DEFAULT_ETALASE_CODE_REGISTER");
        
        int templateType = 0;
        List<String> location = new ArrayList<String>();
        long supplierId = 0;
        long gondolaId = 0;
		int consignment = 0;
        try {
            List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : multiparts) {
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    if (fieldName.equals("template")){
                        templateType = Integer.valueOf(fieldValue);
                    }
                    if (templateType == 1){
                        if (fieldName.equals("location")){
                            location.add(fieldValue);
                        } else if (fieldName.equals("supplier")){
                            supplierId = Long.valueOf(fieldValue);
                        } else if (fieldName.equals("consignment")){
							consignment = Integer.valueOf(fieldValue);
						}  
                    } else if (templateType == 2){
                        if (fieldName.equals("location2")){
                            location.add(fieldValue);
                        } else if (fieldName.equals("supplier2")){
                            supplierId = Long.valueOf(fieldValue);
                        } else if (fieldName.equals("consignment2")){
							consignment = Integer.valueOf(fieldValue);
						}  
                    }
                    
                } else {
                    String fieldName = item.getFieldName();
                    String fileName = FilenameUtils.getName(item.getName());
					
					String path = PstSystemProperty.getValueByName("IMG_CACHE");
					
                    InputStream inStream = item.getInputStream();
                    POIFSFileSystem fs = new POIFSFileSystem(inStream);

                    HSSFWorkbook wb = new HSSFWorkbook(fs);
                    System.out.println("creating workbook");

                    int numOfSheets = wb.getNumberOfSheets();
                    int succInsert = 0;
                    int errInsert = 0;
                    int succUpdate = 0;
                    for (int i = 0; i < 1; i++) {
                        int r = 0;
                        int col = 0;
                        HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(i);
                        html += "<strong> Sheet name : " + sheet.getSheetName() + "</strong>";

                        int rows = sheet.getPhysicalNumberOfRows();

                        int start = (i == 0) ? 0 : NUM_HEADER;
                        String barcode = "";
                        html += "<table class=\"tblStyle\">";
                        for (r = start; r < rows; r++) {
                            long oidMat = 0;
                            boolean checkItem = false;
                            Location loc = null;
                            Unit unit = null;
                            Category cat = null;
                            Merk merk = null;
                            MasterGroup group = null;
                            MasterType type =null;
                            Color color = null;
                            SubCategory subCat = null;
                            CurrencyType currency = null;
                            try {
                                currency = PstCurrencyType.fetchExc(currencyId);
                            } catch (Exception exc){
                                
                            }
                            Ksg ksg = null;
                            ContactList con = null;
                            MaterialTypeMapping mapSize = null;
                            MaterialTypeMapping mapGender = null;
                            MaterialTypeMapping mapSeason = null;
                            MaterialTypeMapping mapStyle = null;
                            MaterialTypeMapping mapPromosi = null;
                            Material material = new Material();
                            double itemPrice = 0;
                            long oidCategory = 0;

                            String colorCode = "";
                            String sizeCode = "";
                            String seasonCode = "";
                            material.setDefaultStockUnitId(defaultSellUnitId);
                            material.setDefaultPriceCurrencyId(currencyId);
                            material.setMatTypeConsig(consignment);
                            material.setSupplierId(supplierId);
                            material.setGondolaCode(gondolaId);
                            material.setBuyUnitId(defaultSellUnitId);
                            material.setProcessStatus(1);
                            material.setDefaultCostCurrencyId(currencyId);
                            try {
                                HSSFRow row = sheet.getRow(r);
                                int cells = 0;
                                //if number of cell is static
                                if (NUM_CELL > 0) {
                                    cells = NUM_CELL;
                                } else { //number of cell is dinamyc
                                    cells = row.getPhysicalNumberOfCells();
                                }
                                tdColor = "#FFF;";
                                // ambil jumlah kolom yang sebenarnya
                                NUM_CELL = cells;
                                html += "<tr>";
                                int caseValue = 0;
                                int errCell = 0;

                                for (int c = 0; c < cells; c++) {
                                    HSSFCell cell = row.getCell((short) c);
                                    String value = null;
                                    boolean check = false;
                                    col = c;
                                    if (cell != null) {
                                        switch (cell.getCellType()) {
                                            case HSSFCell.CELL_TYPE_FORMULA:
                                                value = String.valueOf(cell.getCellFormula());
                                                caseValue = 1;
                                                break;
                                            case HSSFCell.CELL_TYPE_NUMERIC:
                                                value = Formater.formatNumber(cell.getNumericCellValue(), "###");
                                                caseValue = 2;
                                                break;
                                            case HSSFCell.CELL_TYPE_STRING:
                                                value = String.valueOf(cell.getStringCellValue());
                                                caseValue = 3;
                                                break;
                                            default:
                                                value = String.valueOf(cell.getStringCellValue() != null ? cell.getStringCellValue() : "");
                                        }
                                    }
                                    if (templateType == 1 ) {
                                        // details
                                        if (r > 0 && c == 1){
                                            material.setMaterialDescription(value);
                                        }
                                        // sku
                                        if (r > 0 && c == 3){
                                            material.setSku(value);
                                        }
                                        // material
                                        if (r > 0 && c == 4){
                                            material.setName(value);
                                        }
                                        // Unit (unit)
                                        if (r>0 && c == 7){
                                            try {
                                                unit = PstUnit.fetchByCode(value);
                                                if (unit.getOID() != 0){
                                                    value = unit.getName();
                                                    material.setDefaultStockUnitId(unit.getOID());
                                                } else {
                                                    unit.setCode(value);
                                                    unit.setName(value);
                                                    unit.setBaseUnitId(0);
                                                    try {
                                                        long oid = PstUnit.insertExc(unit);
                                                        material.setDefaultStockUnitId(oid);
                                                    } catch (Exception exc){
                                                    }
                                                }
                                            } catch(Exception e){
                                                //on exception create new unit
                                            }
                                        }
                                        //barcode
                                        if (r>0 && c == 8){
                                            material.setBarCode(value);
                                        }
                                        //size
                                        if (r>0 && c == 9){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=1 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapSize = new MaterialTypeMapping();
                                                        mapSize.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(1);
                                                        type.setMasterCode(value);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapSize = new MaterialTypeMapping();
                                                            mapSize.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){}
                                            } 
                                        }
                                        //brand/merk
                                        if (r>0 && c == 10){
                                            try {
                                                merk = PstMerk.fetchByName(value);
                                                if (merk.getOID() != 0) {
                                                    material.setMerkId(merk.getOID());
                                                } else {
                                                    merk.setCode(value);
                                                    merk.setName(value);
                                                    merk.setStatus(1);
                                                    try {
                                                        long oid = PstMerk.insertExc(merk);
                                                        material.setMerkId(oid);
                                                    } catch (Exception exc){
                                                    }
                                                }
                                            } catch(Exception e){
                                                //on exception create new merk
                                            }
                                        }
                                        //sub category
                                        if (r>0 && c == 11){
                                            try {
                                                subCat = PstSubCategory.fetchByName(value);
                                                if (subCat.getOID() != 0){
                                                    material.setSubCategoryId(subCat.getOID());
                                                } else {
                                                    subCat.setCode(value);
                                                    subCat.setName(value);
                                                }
                                            } catch (Exception e){
                                                //on exception create new sub cat
                                            }
                                        }
                                        //category
                                        if (r>0 && c == 14){
                                            try {
                                                cat = PstCategory.fetchByName(value);
                                                if (cat.getOID() != 0){
                                                    value = cat.getName();
                                                    oidCategory = cat.getOID();
                                                    material.setCategoryId(oidCategory);
                                                } else {
                                                    cat.setCode(value);
                                                    cat.setName(value);
                                                    cat.setPointPrice(1);
                                                }
                                            } catch(Exception e){
                                                //on exception create new category


                                            }
                                        }
                                        //season
                                        if (r>0 && c == 15){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=3 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapSeason = new MaterialTypeMapping();
                                                        mapSeason.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(3);
                                                        type.setMasterCode(value);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapSeason = new MaterialTypeMapping();
                                                            mapSeason.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){

                                                }
                                            }
                                        }
                                        //item price
                                        if (r>0 && c == 16){
                                            try {
                                                itemPrice = Double.parseDouble(value);
                                                material.setDefaultPrice(itemPrice);
                                            } catch(Exception e){
                                                System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + e.toString() );
                                            }
                                        }

                                        //color
                                        if (r>0 && c == 17){
                                            try {
                                                color = PstColor.fetchByName(value);
												String codeColor = value.substring(0, Math.min(value.length(), 3));
                                                if (color.getOID() != 0){
                                                    material.setPosColor(color.getOID());
                                                } else {
                                                    color.setColorCode(codeColor);
                                                    color.setColorName(value);
                                                    try {
                                                        long oid = PstColor.insertExc(color);
                                                        material.setPosColor(oid);
                                                    } catch (Exception exc){
                                                    }
                                                }
                                            } catch(Exception e){

                                            }
                                        }
                                        //style
                                        if (r>0 && c == 18){
                                            if (!value.equals("-")){

                                                try {
                                                    value = material.getSku().substring(0, Math.min(material.getSku().length(),7));
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=4 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapStyle = new MaterialTypeMapping();
                                                        mapStyle.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(4);
                                                        type.setMasterCode(value);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapStyle = new MaterialTypeMapping();
                                                            mapStyle.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){

                                                }
                                            }
                                        }
                                        //profit
                                        if (r>0 && c == 19){
                                            try {
                                                material.setProfit(Double.valueOf(value));
                                                double hargaBeli = itemPrice - (itemPrice * (material.getProfit() / 100));
                                                material.setDefaultCost(hargaBeli);
                                                material.setAveragePrice(hargaBeli);
                                            } catch (Exception exc){

                                            }
                                        }
                                        //promosi
                                        if (r>0 && c == 20){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=5 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapPromosi = new MaterialTypeMapping();
                                                        mapPromosi.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(5);
                                                        type.setMasterCode(value);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapPromosi = new MaterialTypeMapping();
                                                            mapPromosi.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){

                                                }
                                            }
                                        }
                                        //suplier
                                        if (r>0 && c == 21){
                                            try {
                                                con = PstContactList.fetchByCode(value);
                                                value = "("+ value + ") "+ con.getPersonName();
                                                material.setSupplierId(con.getOID());
                                            } catch(Exception e){
                                                System.out.println("location is not available=>"+e.toString());
                                            }
                                            /* change color if nothing employee with emp num */
                                            if (con == null || con.getOID() == 0){
                                                tdColor = "#DC143C;";
                                                errCell++;
                                            }
                                        }
                                    } else if (templateType == 2){
                                        // SKU (itemno)
                                        if (r > 0 && c == 0){
                                            material.setSku(value);
                                        }
                                        //style
                                        if (r>0 && c == 1){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=4 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapStyle = new MaterialTypeMapping();
                                                        mapStyle.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(4);
                                                        type.setMasterCode(value);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapStyle = new MaterialTypeMapping();
                                                            mapStyle.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){

                                                }
                                            }
                                        }
                                        // material name
                                        if (r > 0 && c == 2){
                                            material.setName(value);
                                        }
                                        //color code
                                        if (r > 0 && c == 3){
                                            colorCode = value;
                                        }
                                        //color name
                                        if (r>0 && c == 4){
                                            try {
                                                color = PstColor.fetchByCode(colorCode);
                                                if (color.getOID() != 0){
													color.setColorName(value);
													try {
                                                        //long oid = PstColor.updateExc(color);
                                                        material.setPosColor(color.getOID());
                                                    } catch (Exception exc){
                                                    }
                                                } else {
                                                    color.setColorCode(colorCode);
                                                    color.setColorName(value);
                                                    try {
                                                        long oid = PstColor.insertExc(color);
                                                        material.setPosColor(oid);
                                                    } catch (Exception exc){
                                                    }
                                                }
                                            } catch(Exception e){

                                            }
                                        }
                                        //size code
                                        if (r>0 && c == 5){
                                            sizeCode = value;
                                        }
                                        //size
                                        if (r>0 && c == 6){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=1 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapSize = new MaterialTypeMapping();
                                                        mapSize.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(1);
                                                        type.setMasterCode(sizeCode);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapSize = new MaterialTypeMapping();
                                                            mapSize.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){}
                                            } 
                                        }
                                        //season code
                                        if (r>0 && c == 7){
                                            seasonCode = value;
                                        }
                                        //season
                                        if (r>0 && c == 8){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=3 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapSeason = new MaterialTypeMapping();
                                                        mapSeason.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(3);
                                                        type.setMasterCode(seasonCode);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapSeason = new MaterialTypeMapping();
                                                            mapSeason.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){

                                                }
                                            }
                                        }
                                        //brand
                                        if (r>0 && c == 9){
                                            try {
                                                merk = PstMerk.fetchByName(value);
                                                if (merk.getOID() != 0) {
                                                    material.setMerkId(merk.getOID());
                                                } else {
                                                    merk.setCode(value);
                                                    merk.setName(value);
                                                    merk.setStatus(1);
                                                    try {
                                                        long oid = PstMerk.insertExc(merk);
                                                        material.setMerkId(oid);
                                                    } catch (Exception exc){
                                                    }
                                                }
                                            } catch(Exception e){
                                                //on exception create new merk
                                            }
                                        }
                                        //category
                                        if (r>0 && c == 10){
                                            try {
                                                cat = PstCategory.fetchByName(value);
                                                if (cat.getOID() != 0){
                                                    value = cat.getName();
                                                    oidCategory = cat.getOID();
                                                    material.setCategoryId(oidCategory);
                                                } else {
                                                    cat.setCode(value);
                                                    cat.setName(value);
                                                    cat.setPointPrice(1);
                                                    try {
                                                        oidCategory = PstCategory.insertExc(cat);
                                                        material.setCategoryId(oidCategory);
                                                    } catch (Exception exc){
                                                    }
                                                }
                                            } catch(Exception e){
                                                //on exception create new category
                                            }
                                        }
                                        //gender
                                        if (r>0 && c == 11){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=2 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapGender = new MaterialTypeMapping();
                                                        mapGender.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(2);
                                                        type.setMasterCode(value);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapGender = new MaterialTypeMapping();
                                                            mapGender.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){

                                                }
                                            }
                                        }
                                        // sub cat
                                        if (r>0 && c == 12){
                                            try {
                                                subCat = PstSubCategory.fetchByName(value);
                                                if (subCat.getOID() != 0){
                                                    material.setSubCategoryId(subCat.getOID());
                                                } else {
                                                    subCat.setCode(value);
                                                    subCat.setName(value);
                                                    subCat.setCategoryId(oidCategory);
                                                    try {
                                                        long oid = PstSubCategory.insertExc(subCat);
                                                        material.setSubCategoryId(oid);
                                                    } catch (Exception exc){
                                                    }
                                                }
                                            } catch (Exception e){
                                                //on exception create new sub cat
                                            }
                                        }
                                        // barcode
                                        if (r>0 && c == 14){
                                            material.setBarCode(value);
                                        }
                                        //item price
                                        if (r>0 && c == 17){
                                            try {
                                                itemPrice = Double.parseDouble(value);
                                                material.setDefaultPrice(itemPrice);
                                            } catch(Exception e){
                                                System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + e.toString() );
                                            }
                                        }
                                        if (r>0 && c == 18){
                                            material.setMaterialDescription(value);
                                        }
                                        //profit
                                        if (r>0 && c == 19){
                                            try {
                                                material.setProfit(Double.valueOf(value));
                                                double hargaBeli = itemPrice - (itemPrice * (material.getProfit() / 100));
                                                material.setDefaultCost(hargaBeli);
                                                material.setCurrBuyPrice(hargaBeli);
                                            } catch (Exception exc){

                                            }
                                        }
                                        //promosi
                                        if (r>0 && c == 20){
                                            if (!value.equals("-")){
                                                try {
                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"=5 AND "
                                                                    + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]+"='"+value+"'";
                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                    if (listType.size()>0){
                                                        type = (MasterType) listType.get(0);
                                                        mapPromosi = new MaterialTypeMapping();
                                                        mapPromosi.setTypeId(type.getOID());
                                                    } else {
                                                        type = new MasterType();
                                                        type.setTypeGroup(5);
                                                        type.setMasterCode(value);
                                                        type.setMasterName(value);
                                                        type.setDescription("automatic create from register excel");
                                                        try {
                                                            long oid = PstMasterType.insertExc(type);
                                                            mapPromosi = new MaterialTypeMapping();
                                                            mapPromosi.setTypeId(oid);
                                                        } catch (Exception exc){}
                                                    }
                                                } catch(Exception e){

                                                }
                                            }
                                        }
                                        //supplier
                                        if (r>0 && c == 21){
                                            try {
                                                con = PstContactList.fetchByCode(value);
                                                value = "("+ value + ") "+ con.getPersonName();
                                                material.setSupplierId(con.getOID());
                                            } catch(Exception e){
                                                System.out.println("location is not available=>"+e.toString());
                                            }
                                            /* change color if nothing employee with emp num */
                                            if (con == null || con.getOID() == 0){
                                                tdColor = "#DC143C;";
                                                errCell++;
                                            }
                                        }

                                    }
                                    /* Proses menampilkan data ke html table */
                                    if (r == 0){ /* Baris Header table */
                                        html += "<td style=\"background-color:#DDD;\"><strong>"+ value + "</strong></td>";
                                    } else {
                                        if (value.equals("NULL")){
                                            html += "<td style=\"background-color:"+tdColor+"\">-</td>";
                                        } else {
                                            html +="<td style=\"background-color:"+tdColor+"\">"+value+"</td>";
                                        }
                                    }


                                }
                                if (r > 0 && material != null){
                                    try {
                                        Material mat = PstMaterial.fetchBySkuBarcode(material.getBarCode());
                                        if (mat.getOID()!=0){
                                            oidMat = mat.getOID();
                                            material.setOID(oidMat);
                                            checkItem = true;
                                        }
                                    } catch (Exception exc){

                                    }
                                }
                                /*End For Cols*/
                                html +="</tr>";
                                if (material != null && errCell == 0 && r > 0 && !material.getSku().equals("")){

                                    if (templateType == 1 ) {
                                        if (cat.getOID() == 0){
                                            try {
                                                oidCategory = PstCategory.insertExc(cat);
                                                material.setCategoryId(oidCategory);
                                            } catch (Exception exc){
                                            }
                                        }
                                        if (subCat.getOID() == 0){
                                            subCat.setCategoryId(oidCategory);
                                            try {
                                                long oid = PstSubCategory.insertExc(subCat);
                                                material.setSubCategoryId(oid);
                                            } catch (Exception exc){
                                            }
                                        }
                                    }

                                    material.setLastUpdate(new Date());
                                    try {
                                        if (oidMat != 0){
                                            oidMat = PstMaterial.updateExc(material);
                                            succUpdate++;
                                            String sql = " DELETE FROM " + PstMaterialMappingType.TBL_MATERIAL_TYPE_MAPPING + " WHERE " + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + " = " + oidMat;
											PstDataCustom.insertDataForSyncAllLocation(sql);
                                            DBHandler.execUpdate(sql);
                                        } else {
                                            oidMat = PstMaterial.insertExc(material);
                                            succInsert++;
                                        }
                                    } catch (Exception exc){
                                        errInsert++;
                                        System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                    }
                                    if (oidMat!=0){
                                        
                                        try {
                                            mapGender.setMaterialId(oidMat);
                                            PstMaterialMappingType.insertExc(mapGender);
                                        } catch (Exception exc){
                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                        }

                                        try {
                                            mapSize.setMaterialId(oidMat);
                                            PstMaterialMappingType.insertExc(mapSize);
                                        } catch (Exception exc){
                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                        }

                                        try {
                                            mapSeason.setMaterialId(oidMat);
                                            PstMaterialMappingType.insertExc(mapSeason);
                                        } catch (Exception exc){
                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                        }

                                        try {
                                            mapStyle.setMaterialId(oidMat);
                                            PstMaterialMappingType.insertExc(mapStyle);
                                        } catch (Exception exc){
                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                        }

                                        try {
                                            mapPromosi.setMaterialId(oidMat);
                                            PstMaterialMappingType.insertExc(mapPromosi);
                                        } catch (Exception exc){
                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                        }

                                        try {
                                            
                                            if (location.size() > 0){
                                                for (int lc = 0; lc < location.size(); lc++){
                                                    MatMappLocation mapLoc = new MatMappLocation();
                                                    mapLoc.setLocationId(Long.valueOf(location.get(lc)));
                                                    mapLoc.setMaterialId(oidMat);
													boolean checkOidMapLoc = PstMatMappLocation.checkOID(mapLoc.getLocationId(), mapLoc.getMaterialId());
													if (!checkOidMapLoc){
														try {
															PstMatMappLocation.insertExc(mapLoc);
														} catch (Exception exc){}
													}
                                                    
                                                    
                                                    try {
                                                        loc = PstLocation.fetchExc(Long.valueOf(location.get(lc)));
                                                    }catch(Exception exc){
                                                        
                                                    }
                                                    
                                                    
                                                    if (loc.getParentLocationId() != 0){
                                                        ksg = PstKsg.fetchByCodeAndLocation(etalaseCode, loc.getParentLocationId());
                                                        if (ksg.getOID() != 0){
                                                            MatMappKsg matMappKsg = new MatMappKsg();
                                                            matMappKsg.setMaterialId(oidMat);
                                                            matMappKsg.setKsgId(ksg.getOID());
															boolean checkOidMapKsg = PstMatMappKsg.checkOID(matMappKsg.getKsgId(), matMappKsg.getMaterialId());
															if (!checkOidMapKsg){
																try {
																	PstMatMappKsg.insertExc(matMappKsg);
																} catch (Exception exc){}
															}
															
                                                        } else {
                                                            ksg.setCode(etalaseCode);
                                                            ksg.setName(etalaseName);
                                                            ksg.setLocationId(loc.getParentLocationId());
                                                            long oidKsg = PstKsg.insertExc(ksg);
                                                            MatMappKsg matMappKsg = new MatMappKsg();
                                                            matMappKsg.setMaterialId(oidMat);
                                                            matMappKsg.setKsgId(oidKsg);
                                                            PstMatMappKsg.insertExc(matMappKsg);
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception exc){

                                        }
                                        
                                        /* membuat pos_material_stock untuk barang baru*/
                                        if (!checkItem){
                                            try {
                                                Vector listLocation = PstLocation.listAll();
                                                if (listLocation.size()>0){
                                                    for (int l = 0; l < listLocation.size(); l++){
                                                        Location locat = (Location) listLocation.get(l);
                                                        MaterialStock matStock = new MaterialStock();
                                                        matStock.setPeriodeId(1);
                                                        matStock.setMaterialUnitId(oidMat);
                                                        matStock.setLocationId(locat.getOID());
                                                        try {
                                                            PstMaterialStock.insertExc(matStock);
                                                        } catch (Exception exc){}
                                                    }
                                                }
                                            } catch (Exception exc){

                                            }
                                        }


                                        PriceTypeMapping typeMapping = new PriceTypeMapping();
                                        boolean isUpdate = false;
                                        Vector listPriceType = PstPriceType.list(0, 0, "", "");
                                        if (listPriceType.size()>0){
                                            Vector listStandardRate = PstStandartRate.listCurrStandard();
                                            if (listStandardRate.size()>0){
                                                for (int ty = 0; ty < listPriceType.size();ty++){
                                                    PriceType priceType = (PriceType) listPriceType.get(ty);
                                                    for (int rt = 0; rt < listStandardRate.size();rt++){
                                                        Vector temp = (Vector) listStandardRate.get(rt);
                                                        CurrencyType curr = (CurrencyType) temp.get(0);
                                                        StandartRate stdarRate = (StandartRate) temp.get(1);
                                                        try {
                                                            typeMapping = PstPriceTypeMapping.fetchExc(priceType.getOID(), oidMat, stdarRate.getOID());
                                                            isUpdate = true;
                                                        } catch (Exception exc){}
                                                        typeMapping.setPriceTypeId(priceType.getOID());
                                                        typeMapping.setMaterialId(oidMat);
                                                        typeMapping.setStandartRateId(stdarRate.getOID());
                                                        if (currency.getOID() == curr.getOID()){
                                                            typeMapping.setPrice(itemPrice);
                                                        } else {
                                                            if (curr.getOID() != 1){
                                                                try {
                                                                    Vector stRateCurr = PstStandartRate.listCurrStandard(curr.getOID());
                                                                    Vector vect = (Vector) stRateCurr.get(0);
                                                                    StandartRate rate = (StandartRate) vect.get(1);
                                                                    double price = itemPrice / rate.getSellingRate();
                                                                    typeMapping.setPrice(price);
                                                                } catch (Exception exc){}
                                                            } else {
                                                                try {
                                                                    Vector stRateCurr = PstStandartRate.listCurrStandard(currency.getOID());
                                                                    Vector vect = (Vector) stRateCurr.get(0);
                                                                    StandartRate rate = (StandartRate) vect.get(1);
                                                                    double price = itemPrice * rate.getSellingRate();
                                                                    typeMapping.setPrice(price);
                                                                } catch (Exception exc){}
                                                            }
                                                        }

                                                        try {
                                                            if (isUpdate){
                                                                PstPriceTypeMapping.updateExc(typeMapping);
                                                            } else {
                                                                PstPriceTypeMapping.insertExc(typeMapping);
                                                            }
                                                        } catch (Exception exc){}

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception exc){
                                System.out.println("=> Can't get data ..sheet : " + i + ", row : " + r + ", cell : " + col + " => Exception e : " + exc.toString());
                            }
                        } //end of sheet
                            html += "</table>";
                            html += "<div>&nbsp;</div>";
                            if (errInsert > 0){
                                html +="<div>Gagal insert data : "+errInsert+"</div>";
                            } else if (succInsert > 0){
                                html +="<div>Sukses insert data : "+succInsert+"</div>";
                            } else if (succUpdate > 0){
                                html +="<div>Sukses update data : "+succUpdate+"</div>";
                            }

                    }
					File fileToSave = new File(path + FilenameUtils.getName(item.getName()));
					Files.copy(inStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

//            TextLoader uploader = new TextLoader();
//            ByteArrayInputStream inStream = null;
//            
//            uploader.uploadText(config, request, response);
//            Object obj = uploader.getTextFile("file");
//            byte byteText[] = null;
//            byteText = (byte[]) obj;
//            inStream = new ByteArrayInputStream(byteText);

            
            
        } catch (Exception exc){
            
        }
        
        
        return html;
    }
	
    public static void startUpload(ServletConfig config, HttpServletRequest request, HttpServletResponse response, JspWriter output, int typeTemp) {
        try {
            final List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            Runnable r = new Runnable() {
                public void run() {
                    running = true;
                    while (running) {
                        currentProgress = 0;
                        String html = "";
                        ImportRegister importRegister = new ImportRegister();
                        int NUM_HEADER = 2;
                        int NUM_CELL = 0;
                        String tdColor = "#FFF;";

                        long defaultSellUnitId = Long.valueOf(PstSystemProperty.getValueByName("DEFAULT_SELL_UNIT_ID"));
                        long currencyId = Long.valueOf(PstSystemProperty.getValueByName("DEFAULT_PRICE_CURRENCY_ID"));
                        String etalaseName = PstSystemProperty.getValueByName("DEFAULT_ETALASE_NAME_REGISTER");
                        String etalaseCode = PstSystemProperty.getValueByName("DEFAULT_ETALASE_CODE_REGISTER");

                        int templateType = 0;
                        List<String> location = new ArrayList<String>();
                        long supplierId = 0;
                        long gondolaId = 0;
                        int consignment = 0;
                        try {
                            for (FileItem item : multiparts) {
                                if (item.isFormField()) {
                                    String fieldName = item.getFieldName();
                                    String fieldValue = item.getString();
                                    if (fieldName.equals("template")) {
                                        templateType = Integer.valueOf(fieldValue);
                                    }
                                    if (templateType == 1) {
                                        if (fieldName.equals("location")) {
                                            location.add(fieldValue);
                                        } else if (fieldName.equals("supplier")) {
                                            supplierId = Long.valueOf(fieldValue);
                                        } else if (fieldName.equals("consignment")) {
                                            consignment = Integer.valueOf(fieldValue);
                                        }
                                    } else if (templateType == 2) {
                                        if (fieldName.equals("location2")) {
                                            location.add(fieldValue);
                                        } else if (fieldName.equals("supplier2")) {
                                            supplierId = Long.valueOf(fieldValue);
                                        } else if (fieldName.equals("consignment2")) {
                                            consignment = Integer.valueOf(fieldValue);
                                        }
                                    }

                                } else {
                                    String fieldName = item.getFieldName();
                                    String fileName = FilenameUtils.getName(item.getName());

                                    String path = PstSystemProperty.getValueByName("IMG_CACHE");

                                    InputStream inStream = item.getInputStream();
                                    POIFSFileSystem fs = new POIFSFileSystem(inStream);

                                    HSSFWorkbook wb = new HSSFWorkbook(fs);
                                    System.out.println("creating workbook");

                                    int numOfSheets = wb.getNumberOfSheets();
                                    int succInsert = 0;
                                    int errInsert = 0;
                                    int succUpdate = 0;
                                    for (int i = 0; i < 1; i++) {
                                        int r = 0;
                                        int col = 0;
                                        HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(i);
                                        html += "<strong> Sheet name : " + sheet.getSheetName() + "</strong>";

                                        int rows = sheet.getPhysicalNumberOfRows();
                                        countTotalData = rows - 1;
                                        int start = (i == 0) ? 0 : NUM_HEADER;
                                        String barcode = "";
                                        html += "<table class=\"table table-bordered\">";
                                        for (r = start; r < rows; r++) {
                                            if (r != 0) {
                                                currentProgress = importRegister.getCurrentProgress() + 1;
                                            }
                                            long oidMat = 0;
                                            boolean checkItem = false;
                                            Location loc = null;
                                            Unit unit = null;
                                            Category cat = null;
                                            Merk merk = null;
                                            MasterGroup group = null;
                                            MasterType type = null;
                                            Color color = null;
                                            SubCategory subCat = null;
                                            CurrencyType currency = null;
                                            try {
                                                currency = PstCurrencyType.fetchExc(currencyId);
                                            } catch (Exception exc) {

                                            }
                                            Ksg ksg = null;
                                            ContactList con = null;
                                            MaterialTypeMapping mapSize = null;
                                            MaterialTypeMapping mapGender = null;
                                            MaterialTypeMapping mapSeason = null;
                                            MaterialTypeMapping mapStyle = null;
                                            MaterialTypeMapping mapPromosi = null;
                                            Material material = new Material();
                                            double itemPrice = 0;
                                            long oidCategory = 0;

                                            String colorCode = "";
                                            String sizeCode = "";
                                            String seasonCode = "";
                                            material.setDefaultStockUnitId(defaultSellUnitId);
                                            material.setDefaultPriceCurrencyId(currencyId);
                                            material.setMatTypeConsig(consignment);
                                            material.setSupplierId(supplierId);
                                            material.setGondolaCode(gondolaId);
                                            material.setBuyUnitId(defaultSellUnitId);
                                            material.setProcessStatus(1);
                                            material.setDefaultCostCurrencyId(currencyId);
                                            try {
                                                HSSFRow row = sheet.getRow(r);
                                                int cells = 0;
                                                //if number of cell is static
                                                if (NUM_CELL > 0) {
                                                    cells = NUM_CELL;
                                                } else { //number of cell is dinamyc
                                                    cells = row.getPhysicalNumberOfCells();
                                                }
                                                tdColor = "#FFF;";
                                                // ambil jumlah kolom yang sebenarnya
                                                NUM_CELL = cells;
                                                html += "<tr>";
                                                int caseValue = 0;
                                                int errCell = 0;

                                                for (int c = 0; c < cells; c++) {
                                                    HSSFCell cell = row.getCell((short) c);
                                                    String value = null;
                                                    boolean check = false;
                                                    col = c;
                                                    if (cell != null) {
                                                        switch (cell.getCellType()) {
                                                            case HSSFCell.CELL_TYPE_FORMULA:
                                                                value = String.valueOf(cell.getCellFormula());
                                                                caseValue = 1;
                                                                break;
                                                            case HSSFCell.CELL_TYPE_NUMERIC:
                                                                value = Formater.formatNumber(cell.getNumericCellValue(), "###");
                                                                caseValue = 2;
                                                                break;
                                                            case HSSFCell.CELL_TYPE_STRING:
                                                                value = String.valueOf(cell.getStringCellValue());
                                                                caseValue = 3;
                                                                break;
                                                            default:
                                                                value = String.valueOf(cell.getStringCellValue() != null ? cell.getStringCellValue() : "");
                                                        }
                                                    } else {
                                                        value = "";
                                                    }
                                                    if (templateType == 1) {
                                                        // details
                                                        if (r > 0 && c == 1) {
                                                            material.setMaterialDescription(value);
                                                        }
                                                        // sku
                                                        if (r > 0 && c == 3) {
                                                            material.setSku(value);
                                                        }
                                                        // material
                                                        if (r > 0 && c == 4) {
                                                            material.setName(value);
                                                        }
                                                        // Unit (unit)
                                                        if (r > 0 && c == 7) {
                                                            try {
                                                                unit = PstUnit.fetchByCode(value);
                                                                if (unit.getOID() != 0) {
                                                                    value = unit.getName();
                                                                    material.setDefaultStockUnitId(unit.getOID());
                                                                } else {
                                                                    unit.setCode(value);
                                                                    unit.setName(value);
                                                                    unit.setBaseUnitId(0);
                                                                    try {
                                                                        long oid = PstUnit.insertExc(unit);
                                                                        material.setDefaultStockUnitId(oid);
                                                                    } catch (Exception exc) {
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                //on exception create new unit
                                                            }
                                                        }
                                                        //barcode
                                                        if (r > 0 && c == 8) {
                                                            material.setBarCode(value);
                                                        }
                                                        //size
                                                        if (r > 0 && c == 9) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=1 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapSize = new MaterialTypeMapping();
                                                                        mapSize.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(1);
                                                                        type.setMasterCode(value);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapSize = new MaterialTypeMapping();
                                                                            mapSize.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        }
                                                        //brand/merk
                                                        if (r > 0 && c == 10) {
                                                            try {
                                                                merk = PstMerk.fetchByName(value);
                                                                if (merk.getOID() != 0) {
                                                                    material.setMerkId(merk.getOID());
                                                                } else {
                                                                    merk.setCode(value);
                                                                    merk.setName(value);
                                                                    merk.setStatus(1);
                                                                    try {
                                                                        long oid = PstMerk.insertExc(merk);
                                                                        material.setMerkId(oid);
                                                                    } catch (Exception exc) {
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                //on exception create new merk
                                                            }
                                                        }
                                                        //sub category
                                                        if (r > 0 && c == 11) {
                                                            try {
                                                                subCat = PstSubCategory.fetchByName(value);
                                                                if (subCat.getOID() != 0) {
                                                                    material.setSubCategoryId(subCat.getOID());
                                                                } else {
                                                                    subCat.setCode(value);
                                                                    subCat.setName(value);
                                                                }
                                                            } catch (Exception e) {
                                                                //on exception create new sub cat
                                                            }
                                                        }
                                                        //category
                                                        if (r > 0 && c == 14) {
                                                            try {
                                                                cat = PstCategory.fetchByName(value);
                                                                if (cat.getOID() != 0) {
                                                                    value = cat.getName();
                                                                    oidCategory = cat.getOID();
                                                                    material.setCategoryId(oidCategory);
                                                                } else {
                                                                    cat.setCode(value);
                                                                    cat.setName(value);
                                                                    cat.setPointPrice(1);
                                                                }
                                                            } catch (Exception e) {
																//on exception create new category

                                                            }
                                                        }
                                                        //season
                                                        if (r > 0 && c == 15) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=3 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapSeason = new MaterialTypeMapping();
                                                                        mapSeason.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(3);
                                                                        type.setMasterCode(value);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapSeason = new MaterialTypeMapping();
                                                                            mapSeason.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }
                                                        //item price
                                                        if (r > 0 && c == 16) {
                                                            try {
                                                                itemPrice = Double.parseDouble(value);
                                                                material.setDefaultPrice(itemPrice);
                                                            } catch (Exception e) {
                                                                System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + e.toString());
                                                            }
                                                        }

                                                        //color
                                                        if (r > 0 && c == 17) {
                                                            try {
                                                                color = PstColor.fetchByName(value);
                                                                String codeColor = value.substring(0, Math.min(value.length(), 3));
                                                                if (color.getOID() != 0) {
                                                                    material.setPosColor(color.getOID());
                                                                } else {
                                                                    color.setColorCode(codeColor);
                                                                    color.setColorName(value);
                                                                    try {
                                                                        long oid = PstColor.insertExc(color);
                                                                        material.setPosColor(oid);
                                                                    } catch (Exception exc) {
                                                                    }
                                                                }
                                                            } catch (Exception e) {

                                                            }
                                                        }
                                                        //style
                                                        if (r > 0 && c == 18) {
                                                            if (!value.equals("-")) {

                                                                try {
                                                                    value = material.getSku().substring(0, Math.min(material.getSku().length(), 7));
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=4 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapStyle = new MaterialTypeMapping();
                                                                        mapStyle.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(4);
                                                                        type.setMasterCode(value);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapStyle = new MaterialTypeMapping();
                                                                            mapStyle.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }
                                                        //profit
                                                        if (r > 0 && c == 19) {
                                                            try {
                                                                material.setProfit(Double.valueOf(value));
                                                                double hargaBeli = itemPrice - (itemPrice * (material.getProfit() / 100));
                                                                material.setDefaultCost(hargaBeli);
                                                                material.setAveragePrice(hargaBeli);
                                                            } catch (Exception exc) {

                                                            }
                                                        }
                                                        //promosi
                                                        if (r > 0 && c == 20) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=5 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapPromosi = new MaterialTypeMapping();
                                                                        mapPromosi.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(5);
                                                                        type.setMasterCode(value);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapPromosi = new MaterialTypeMapping();
                                                                            mapPromosi.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }
                                                        //suplier
                                                        if (r > 0 && c == 21) {
                                                            try {
                                                                con = PstContactList.fetchByCode(value);
                                                                value = "(" + value + ") " + con.getPersonName();
                                                                material.setSupplierId(con.getOID());
                                                            } catch (Exception e) {
                                                                System.out.println("location is not available=>" + e.toString());
                                                            }
                                                            /* change color if nothing employee with emp num */
                                                            if (con == null || con.getOID() == 0) {
                                                                tdColor = "#DC143C;";
                                                                errCell++;
                                                            }
                                                        }
                                                    } else if (templateType == 2) {
                                                        // SKU (itemno)
                                                        if (r > 0 && c == 0) {
                                                            material.setSku(value);
                                                        }
                                                        //style
                                                        if (r > 0 && c == 1) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=4 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapStyle = new MaterialTypeMapping();
                                                                        mapStyle.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(4);
                                                                        type.setMasterCode(value);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapStyle = new MaterialTypeMapping();
                                                                            mapStyle.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }
                                                        // material name
                                                        if (r > 0 && c == 2) {
                                                            material.setName(value);
                                                        }
                                                        //color code
                                                        if (r > 0 && c == 3) {
                                                            colorCode = value;
                                                        }
                                                        //color name
                                                        if (r > 0 && c == 4) {
                                                            try {
                                                                color = PstColor.fetchByCode(colorCode);
                                                                if (color.getOID() != 0) {
                                                                    color.setColorName(value);
                                                                    try {
                                                                        //long oid = PstColor.updateExc(color);
                                                                        material.setPosColor(color.getOID());
                                                                    } catch (Exception exc) {
                                                                    }
                                                                } else {
                                                                    color.setColorCode(colorCode);
                                                                    color.setColorName(value);
                                                                    try {
                                                                        long oid = PstColor.insertExc(color);
                                                                        material.setPosColor(oid);
                                                                    } catch (Exception exc) {
                                                                    }
                                                                }
                                                            } catch (Exception e) {

                                                            }
                                                        }
                                                        //size code
                                                        if (r > 0 && c == 5) {
                                                            sizeCode = value;
                                                        }
                                                        //size
                                                        if (r > 0 && c == 6) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=1 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapSize = new MaterialTypeMapping();
                                                                        mapSize.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(1);
                                                                        type.setMasterCode(sizeCode);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapSize = new MaterialTypeMapping();
                                                                            mapSize.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        }
                                                        //season code
                                                        if (r > 0 && c == 7) {
                                                            seasonCode = value;
                                                        }
                                                        //season
                                                        if (r > 0 && c == 8) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=3 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapSeason = new MaterialTypeMapping();
                                                                        mapSeason.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(3);
                                                                        type.setMasterCode(seasonCode);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapSeason = new MaterialTypeMapping();
                                                                            mapSeason.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }
                                                        //brand
                                                        if (r > 0 && c == 9) {
                                                            try {
                                                                merk = PstMerk.fetchByName(value);
                                                                if (merk.getOID() != 0) {
                                                                    material.setMerkId(merk.getOID());
                                                                } else {
                                                                    merk.setCode(value);
                                                                    merk.setName(value);
                                                                    merk.setStatus(1);
                                                                    try {
                                                                        long oid = PstMerk.insertExc(merk);
                                                                        material.setMerkId(oid);
                                                                    } catch (Exception exc) {
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                //on exception create new merk
                                                            }
                                                        }
                                                        //category
                                                        if (r > 0 && c == 10) {
                                                            try {
                                                                cat = PstCategory.fetchByName(value);
                                                                if (cat.getOID() != 0) {
                                                                    value = cat.getName();
                                                                    oidCategory = cat.getOID();
                                                                    material.setCategoryId(oidCategory);
                                                                } else {
                                                                    cat.setCode(value);
                                                                    cat.setName(value);
                                                                    cat.setPointPrice(1);
                                                                    try {
                                                                        oidCategory = PstCategory.insertExc(cat);
                                                                        material.setCategoryId(oidCategory);
                                                                    } catch (Exception exc) {
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                //on exception create new category
                                                            }
                                                        }
                                                        //gender
                                                        if (r > 0 && c == 11) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=2 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapGender = new MaterialTypeMapping();
                                                                        mapGender.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(2);
                                                                        type.setMasterCode(value);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapGender = new MaterialTypeMapping();
                                                                            mapGender.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }
                                                        // sub cat
                                                        if (r > 0 && c == 12) {
                                                            try {
                                                                subCat = PstSubCategory.fetchByName(value);
                                                                if (subCat.getOID() != 0) {
                                                                    material.setSubCategoryId(subCat.getOID());
                                                                } else {
                                                                    subCat.setCode(value);
                                                                    subCat.setName(value);
                                                                    subCat.setCategoryId(oidCategory);
                                                                    try {
                                                                        long oid = PstSubCategory.insertExc(subCat);
                                                                        material.setSubCategoryId(oid);
                                                                    } catch (Exception exc) {
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                //on exception create new sub cat
                                                            }
                                                        }
                                                        // barcode
                                                        if (r > 0 && c == 14) {
                                                            material.setBarCode(value);
                                                        }
                                                        //item price
                                                        if (r > 0 && c == 17) {
                                                            try {
                                                                itemPrice = Double.parseDouble(value);
                                                                material.setDefaultPrice(itemPrice);
                                                            } catch (Exception e) {
                                                                System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + e.toString());
                                                            }
                                                        }
                                                        if (r > 0 && c == 18) {
                                                            material.setMaterialDescription(value);
                                                        }
                                                        //profit
                                                        if (r > 0 && c == 19) {
                                                            try {
                                                                material.setProfit(Double.valueOf(value));
                                                                double hargaBeli = itemPrice - (itemPrice * (material.getProfit() / 100));
                                                                material.setDefaultCost(hargaBeli);
                                                                material.setCurrBuyPrice(hargaBeli);
                                                            } catch (Exception exc) {

                                                            }
                                                        }
                                                        //promosi
                                                        if (r > 0 && c == 20) {
                                                            if (!value.equals("-")) {
                                                                try {
                                                                    String whereType = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=5 AND "
                                                                            + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME] + "='" + value + "'";
                                                                    Vector listType = PstMasterType.list(0, 0, whereType, "");
                                                                    if (listType.size() > 0) {
                                                                        type = (MasterType) listType.get(0);
                                                                        mapPromosi = new MaterialTypeMapping();
                                                                        mapPromosi.setTypeId(type.getOID());
                                                                    } else {
                                                                        type = new MasterType();
                                                                        type.setTypeGroup(5);
                                                                        type.setMasterCode(value);
                                                                        type.setMasterName(value);
                                                                        type.setDescription("automatic create from register excel");
                                                                        try {
                                                                            long oid = PstMasterType.insertExc(type);
                                                                            mapPromosi = new MaterialTypeMapping();
                                                                            mapPromosi.setTypeId(oid);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }
                                                        //supplier
                                                        if (r > 0 && c == 21) {
                                                            try {
                                                                con = PstContactList.fetchByCode(value);
                                                                value = "(" + value + ") " + con.getPersonName();
                                                                material.setSupplierId(con.getOID());
                                                            } catch (Exception e) {
                                                                System.out.println("location is not available=>" + e.toString());
                                                            }
                                                            /* change color if nothing employee with emp num */
                                                            if (con == null || con.getOID() == 0) {
                                                                tdColor = "#DC143C;";
                                                                errCell++;
                                                            }
                                                        }

                                                    }
                                                    /* Proses menampilkan data ke html table */
                                                    if (r == 0) { /* Baris Header table */

                                                        html += "<td class='th' style=\"background-color:#DDD;\"><strong>" + value + "</strong></td>";
                                                    } else {
                                                        if (value.equals("NULL")) {
                                                            html += "<td class='th' style=\"background-color:" + tdColor + "\">-</td>";
                                                        } else {
                                                            html += "<td class='th' style=\"background-color:" + tdColor + "\">" + value + "</td>";
                                                        }
                                                    }

                                                }
                                                if (r > 0 && material != null) {
                                                    try {
                                                        Material mat = PstMaterial.fetchBySkuBarcode(material.getBarCode());
                                                        if (mat.getOID() != 0) {
                                                            oidMat = mat.getOID();
                                                            material.setOID(oidMat);
                                                            checkItem = true;
                                                        }
                                                    } catch (Exception exc) {

                                                    }
                                                }
                                                /*End For Cols*/
                                                html += "</tr>";
                                                if (material != null && errCell == 0 && r > 0 && !material.getSku().equals("")) {

                                                    if (templateType == 1) {
                                                        if (cat.getOID() == 0) {
                                                            try {
                                                                oidCategory = PstCategory.insertExc(cat);
                                                                material.setCategoryId(oidCategory);
                                                            } catch (Exception exc) {
                                                            }
                                                        }
                                                        if (subCat.getOID() == 0) {
                                                            subCat.setCategoryId(oidCategory);
                                                            try {
                                                                long oid = PstSubCategory.insertExc(subCat);
                                                                material.setSubCategoryId(oid);
                                                            } catch (Exception exc) {
                                                            }
                                                        }
                                                    }

                                                    material.setLastUpdate(new Date());
                                                    try {
                                                        if (oidMat != 0) {
                                                            oidMat = PstMaterial.updateExc(material);
                                                            succUpdate++;
                                                            String sql = " DELETE FROM " + PstMaterialMappingType.TBL_MATERIAL_TYPE_MAPPING + " WHERE " + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + " = " + oidMat;
                                                            PstDataCustom.insertDataForSyncAllLocation(sql);
                                                            DBHandler.execUpdate(sql);
                                                        } else {
                                                            oidMat = PstMaterial.insertExc(material);
                                                            succInsert++;
                                                        }
                                                        //lakukan proses simpan ke vendor
                                                        CtrlMatVendorPrice ctrlMatVendorPrice = new CtrlMatVendorPrice(null);
                                                        int ccc = ctrlMatVendorPrice.actionInsertDefaultSupplier(0, supplierId, material);
                                                    } catch (Exception exc) {
                                                        errInsert++;
                                                        System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                                    }
                                                    if (oidMat != 0) {

                                                        try {
                                                            mapGender.setMaterialId(oidMat);
                                                            PstMaterialMappingType.insertExc(mapGender);
                                                        } catch (Exception exc) {
                                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                                        }

                                                        try {
                                                            mapSize.setMaterialId(oidMat);
                                                            PstMaterialMappingType.insertExc(mapSize);
                                                        } catch (Exception exc) {
                                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                                        }

                                                        try {
                                                            mapSeason.setMaterialId(oidMat);
                                                            PstMaterialMappingType.insertExc(mapSeason);
                                                        } catch (Exception exc) {
                                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                                        }

                                                        try {
                                                            mapStyle.setMaterialId(oidMat);
                                                            PstMaterialMappingType.insertExc(mapStyle);
                                                        } catch (Exception exc) {
                                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                                        }

                                                        try {
                                                            mapPromosi.setMaterialId(oidMat);
                                                            PstMaterialMappingType.insertExc(mapPromosi);
                                                        } catch (Exception exc) {
                                                            System.out.println("com.dimata.posbo.excel.importexcel.ImportRegister.drawImport() " + exc.toString());
                                                        }

                                                        try {

                                                            if (location.size() > 0) {
                                                                for (int lc = 0; lc < location.size(); lc++) {
                                                                    MatMappLocation mapLoc = new MatMappLocation();
                                                                    mapLoc.setLocationId(Long.valueOf(location.get(lc)));
                                                                    mapLoc.setMaterialId(oidMat);
                                                                    boolean checkOidMapLoc = PstMatMappLocation.checkOID(mapLoc.getLocationId(), mapLoc.getMaterialId());
                                                                    if (!checkOidMapLoc) {
                                                                        try {
                                                                            PstMatMappLocation.insertExc(mapLoc);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }

                                                                    try {
                                                                        loc = PstLocation.fetchExc(Long.valueOf(location.get(lc)));
                                                                    } catch (Exception exc) {

                                                                    }

                                                                    if (loc.getParentLocationId() != 0) {
                                                                        ksg = PstKsg.fetchByCodeAndLocation(etalaseCode, loc.getParentLocationId());
                                                                        if (ksg.getOID() != 0) {
                                                                            MatMappKsg matMappKsg = new MatMappKsg();
                                                                            matMappKsg.setMaterialId(oidMat);
                                                                            matMappKsg.setKsgId(ksg.getOID());
                                                                            boolean checkOidMapKsg = PstMatMappKsg.checkOID(matMappKsg.getKsgId(), matMappKsg.getMaterialId());
                                                                            if (!checkOidMapKsg) {
                                                                                try {
                                                                                    PstMatMappKsg.insertExc(matMappKsg);
                                                                                } catch (Exception exc) {
                                                                                }
                                                                            }

                                                                        } else {
                                                                            ksg.setCode(etalaseCode);
                                                                            ksg.setName(etalaseName);
                                                                            ksg.setLocationId(loc.getParentLocationId());
                                                                            long oidKsg = PstKsg.insertExc(ksg);
                                                                            MatMappKsg matMappKsg = new MatMappKsg();
                                                                            matMappKsg.setMaterialId(oidMat);
                                                                            matMappKsg.setKsgId(oidKsg);
                                                                            PstMatMappKsg.insertExc(matMappKsg);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } catch (Exception exc) {

                                                        }

                                                        /* membuat pos_material_stock untuk barang baru*/
                                                        if (!checkItem) {
                                                            try {
                                                                Vector listLocation = PstLocation.listAll();
                                                                if (listLocation.size() > 0) {
                                                                    for (int l = 0; l < listLocation.size(); l++) {
                                                                        Location locat = (Location) listLocation.get(l);
                                                                        MaterialStock matStock = new MaterialStock();
                                                                        matStock.setPeriodeId(1);
                                                                        matStock.setMaterialUnitId(oidMat);
                                                                        matStock.setLocationId(locat.getOID());
                                                                        try {
                                                                            PstMaterialStock.insertExc(matStock);
                                                                        } catch (Exception exc) {
                                                                        }
                                                                    }
                                                                }
                                                            } catch (Exception exc) {

                                                            }
                                                        }

                                                        PriceTypeMapping typeMapping = new PriceTypeMapping();
                                                        boolean isUpdate = false;
                                                        Vector listPriceType = PstPriceType.list(0, 0, "", "");
                                                        if (listPriceType.size() > 0) {
                                                            Vector listStandardRate = PstStandartRate.listCurrStandard();
                                                            if (listStandardRate.size() > 0) {
                                                                for (int ty = 0; ty < listPriceType.size(); ty++) {
                                                                    PriceType priceType = (PriceType) listPriceType.get(ty);
                                                                    for (int rt = 0; rt < listStandardRate.size(); rt++) {
                                                                        Vector temp = (Vector) listStandardRate.get(rt);
                                                                        CurrencyType curr = (CurrencyType) temp.get(0);
                                                                        StandartRate stdarRate = (StandartRate) temp.get(1);
                                                                        try {
                                                                            typeMapping = PstPriceTypeMapping.fetchExc(priceType.getOID(), oidMat, stdarRate.getOID());
                                                                            isUpdate = true;
                                                                        } catch (Exception exc) {
                                                                        }
                                                                        typeMapping.setPriceTypeId(priceType.getOID());
                                                                        typeMapping.setMaterialId(oidMat);
                                                                        typeMapping.setStandartRateId(stdarRate.getOID());
                                                                        if (currency.getOID() == curr.getOID()) {
                                                                            typeMapping.setPrice(itemPrice);
                                                                        } else {
                                                                            if (curr.getOID() != 1) {
                                                                                try {
                                                                                    Vector stRateCurr = PstStandartRate.listCurrStandard(curr.getOID());
                                                                                    Vector vect = (Vector) stRateCurr.get(0);
                                                                                    StandartRate rate = (StandartRate) vect.get(1);
                                                                                    double price = itemPrice / rate.getSellingRate();
                                                                                    typeMapping.setPrice(price);
                                                                                } catch (Exception exc) {
                                                                                }
                                                                            } else {
                                                                                try {
                                                                                    Vector stRateCurr = PstStandartRate.listCurrStandard(currency.getOID());
                                                                                    Vector vect = (Vector) stRateCurr.get(0);
                                                                                    StandartRate rate = (StandartRate) vect.get(1);
                                                                                    double price = itemPrice * rate.getSellingRate();
                                                                                    typeMapping.setPrice(price);
                                                                                } catch (Exception exc) {
                                                                                }
                                                                            }
                                                                        }

                                                                        try {
                                                                            if (isUpdate) {
                                                                                PstPriceTypeMapping.updateExc(typeMapping);
                                                                            } else {
                                                                                PstPriceTypeMapping.insertExc(typeMapping);
                                                                            }
                                                                        } catch (Exception exc) {
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (Exception exc) {
                                                System.out.println("=> Can't get data ..sheet : " + i + ", row : " + r + ", cell : " + col + " => Exception e : " + exc.toString());
                                            }
                                        } //end of sheet
                                        html += "</table>";
                                        //html += "<div>&nbsp;</div>";
                                        if (errInsert > 0) {
                                            html += "<div>Gagal insert data : " + errInsert + "</div>";
                                        } else if (succInsert > 0) {
                                            html += "<div>Sukses insert data : " + succInsert + "</div>";
                                        } else if (succUpdate > 0) {
                                            html += "<div>Sukses update data : " + succUpdate + "</div>";
                                        }

                                    }
                                    running = false;
                                    table = html;
                                    File fileToSave = new File(path + FilenameUtils.getName(item.getName()));
                                    Files.copy(inStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    running = false;
                                    table = html;
                                }
                            }
                            running = false;
                            table = html;

				//            TextLoader uploader = new TextLoader();
                            //            ByteArrayInputStream inStream = null;
                            //            
                            //            uploader.uploadText(config, request, response);
                            //            Object obj = uploader.getTextFile("file");
                            //            byte byteText[] = null;
                            //            byteText = (byte[]) obj;
                            //            inStream = new ByteArrayInputStream(byteText);
                        } catch (Exception exc) {
                            running = false;
                        }
                    }
                }
            };

            Thread t = new Thread(r);
            t.start();
        } catch (Exception exc) {

        }

    }

	public static void startUploadReceiveItem(ServletConfig config, HttpServletRequest request, HttpServletResponse response, JspWriter output, int typeTemp){
		try {
			final List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				Runnable r = new Runnable() {
				public void run() {
					running = true;	
					while(running){
						currentProgress = 0;
						String html = "";
						ImportRegister importRegister = new ImportRegister();
						int NUM_HEADER = 2;
						int NUM_CELL = 0;
						String tdColor = "#FFF;";

						long defaultSellUnitId = Long.valueOf(PstSystemProperty.getValueByName("DEFAULT_SELL_UNIT_ID"));
						long currencyId = Long.valueOf(PstSystemProperty.getValueByName("DEFAULT_PRICE_CURRENCY_ID"));
						String etalaseName = PstSystemProperty.getValueByName("DEFAULT_ETALASE_NAME_REGISTER");
						String etalaseCode = PstSystemProperty.getValueByName("DEFAULT_ETALASE_CODE_REGISTER");
						Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");
						int templateType = 0;
						List<String> location = new ArrayList<String>();
						long receiveId = 0;
						try {
							for (FileItem item : multiparts) {
								if (item.isFormField()) {
									String fieldName = item.getFieldName();
									String fieldValue = item.getString();
									if (fieldName.equals("receive_material_id")){
										receiveId = Long.valueOf(fieldValue);
									} else if (fieldName.equals("template")){
										templateType = Integer.valueOf(fieldValue);
									}
								} else {
									String fieldName = item.getFieldName();
									String fileName = FilenameUtils.getName(item.getName());

									String path = PstSystemProperty.getValueByName("IMG_CACHE");

									InputStream inStream = item.getInputStream();
									POIFSFileSystem fs = new POIFSFileSystem(inStream);

									HSSFWorkbook wb = new HSSFWorkbook(fs);
									System.out.println("creating workbook");

									int numOfSheets = wb.getNumberOfSheets();
									int succInsert = 0;
									int errInsert = 0;
									int succUpdate = 0;
									for (int i = 0; i < 1; i++) {
										int r = 0;
										int col = 0;
										HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(i);
										html += "<strong> Sheet name : " + sheet.getSheetName() + "</strong>";

										int rows = sheet.getPhysicalNumberOfRows();
										countTotalData = rows-1;
										int start = (i == 0) ? 0 : NUM_HEADER;
										String barcode = "";
										String sku = "";
										html += "<table class=\"table table-bordered\">";
										
										MatReceive matReceive = new MatReceive();
										try {
											matReceive = PstMatReceive.fetchExc(receiveId);
										} catch (Exception exc){}
										
										if (matReceive.getOID()>0){
											for (r = start; r < rows; r++) {
												if (r!= 0){
													currentProgress = importRegister.getCurrentProgress() + 1;
												}
												long oidMat = 0;
												boolean checkItem = false;
												int qty = 0;
												Material material = new Material();

												try {
													HSSFRow row = sheet.getRow(r);
													int cells = 0;
													//if number of cell is static
													if (NUM_CELL > 0) {
														cells = NUM_CELL;
													} else { //number of cell is dinamyc
														cells = row.getPhysicalNumberOfCells();
													}
													tdColor = "#FFF;";
													// ambil jumlah kolom yang sebenarnya
													NUM_CELL = cells;
													html +="<tr>";
													int caseValue = 0;
													int errCell = 0;

													for (int c = 0; c < cells; c++) {
														HSSFCell cell = row.getCell((short) c);
														String value = null;
														boolean check = false;
														col = c;
														if (cell != null) {
															switch (cell.getCellType()) {
																case HSSFCell.CELL_TYPE_FORMULA:
																	value = String.valueOf(cell.getCellFormula());
																	caseValue = 1;
																	break;
																case HSSFCell.CELL_TYPE_NUMERIC:
																	value = Formater.formatNumber(cell.getNumericCellValue(), "###");
																	caseValue = 2;
																	break;
																case HSSFCell.CELL_TYPE_STRING:
																	value = String.valueOf(cell.getStringCellValue());
																	caseValue = 3;
																	break;
																default:
																	value = String.valueOf(cell.getStringCellValue() != null ? cell.getStringCellValue() : "");
															}
														}
														if (templateType == 1 ) {
															// sku
															if (r > 0 && c == 3){
																try {
																	material = PstMaterial.fetchBySku(value);
																	sku = value;
																} catch (Exception exc){

																}
															}

															//qty
															if (r>0 && c == 6){
																try {
																	qty = Integer.valueOf(value);
																} catch (Exception exc){

																}
															}
														} else if (templateType == 2){
															// SKU (itemno)
															if (r > 0 && c == 0){
																try {
																	material = PstMaterial.fetchBySku(value);
																	sku = value;
																} catch (Exception exc){

																}
															}

															//qty
															if (r>0 && c == 16){
																try {
																	qty = Integer.valueOf(value);
																} catch (Exception exc){

																}
															}

														}
													}
													/* Proses menampilkan data ke html table */
													if (r == 0){ /* Baris Header table */
														html += "<td style=\"background-color:#DDD;\"><strong>No</strong></td>"
																+ "<td style=\"background-color:#DDD;\"><strong>SKU</strong></td>"
																+ "<td style=\"background-color:#DDD;\"><strong>Barcode</strong></td>"
																+ "<td style=\"background-color:#DDD;\"><strong>Nama Barang</strong></td>"
																+ "<td style=\"background-color:#DDD;\"><strong>Qty</strong></td>"
																+ "<td style=\"background-color:#DDD;\"><strong>Warna</strong></td>";
														if (listGroupMapping.size() > 0) {
															for (int xx = 0; xx < listGroupMapping.size(); i++) {
																MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(i);
																MasterGroup masterGroup = new MasterGroup();
																try {
																	masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
																} catch (Exception exc) {
																}
																html += "<td style=\"background-color:#DDD;\"><strong>"+masterGroup.getNamaGroup()+"</strong></td>";
															}
														}
													} else {
														if (r>0 && material.getOID()==0){
															tdColor = "#DC143C;";
															errCell++;
														} else if (qty == 0){
															tdColor = "#DC143C;";
															errCell++;
														}
														Color color = new Color();
														try {
															color = PstColor.fetchExc(material.getPosColor());
														} catch (Exception exc) {

														}

														html += "<td style=\"background-color:"+tdColor+"\">"+r+"</td>"
																+ "<td style=\"background-color:"+tdColor+"\">"+sku+"</td>"
																+ "<td style=\"background-color:"+tdColor+"\">"+material.getBarCode()+"</td>"
																+ "<td style=\"background-color:"+tdColor+"\">"+material.getName()+"</td>"
																+ "<td style=\"background-color:"+tdColor+"\">"+qty+"</td>"
																+ "<td style=\"background-color:"+tdColor+"\">"+color.getColorName()+"</td>";
														if (listGroupMapping.size() > 0) {
															for (int xx = 0; xx < listGroupMapping.size(); xx++) {
																MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(xx);
																MasterGroup masterGroup = new MasterGroup();
																try {
																	masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
																} catch (Exception exc) {
																}
																long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(), material.getOID());
																MasterType masterType = new MasterType();
																String typeName = "";
																try {
																	masterType = PstMasterType.fetchExc(oidMapping);
																	typeName = masterType.getMasterName();
																} catch (Exception exc) {
																	typeName = "-";
																}
																html += "<td style=\"background-color:"+tdColor+"\">"+typeName+"</td>";
															}
														}
													}
													if (r > 0 && material != null && qty > 0){
														try {
															MatReceiveItem matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matReceive.getInvoiceSupplier(), 0, material.getOID());
															matReceiveItem.setReceiveMaterialId(matReceive.getOID());
															matReceiveItem.setMaterialId(material.getOID());
															matReceiveItem.setUnitId(material.getDefaultStockUnitId());
															matReceiveItem.setExpiredDate(material.getExpiredDate());
															matReceiveItem.setCost(material.getDefaultCost());
															matReceiveItem.setCurrencyId(matReceive.getCurrencyId());
															matReceiveItem.setQty(qty);
															matReceiveItem.setTotal(matReceiveItem.getCost() * matReceiveItem.getQty());
															matReceiveItem.setResidueQty(qty);
															matReceiveItem.setCurrBuyingPrice(matReceiveItem.getTotal());
															matReceiveItem.setUnitKonversi(material.getDefaultStockUnitId());
															matReceiveItem.setColorId(material.getPosColor());
															if (matReceiveItem.getOID()>0){
																PstMatReceiveItem.updateExc(matReceiveItem);
																succUpdate++;
															} else {
																PstMatReceiveItem.insertExc(matReceiveItem);
																succInsert++;
															}
															
														} catch (Exception exc){

														}
													}
													/*End For Cols*/
													html +="</tr>";
												} catch (Exception exc){
													System.out.println("=> Can't get data ..sheet : " + i + ", row : " + r + ", cell : " + col + " => Exception e : " + exc.toString());
												}
											}
										} else {
											html += "<tr>"
														+ "<td>Receive not found!</td>"
													+ "<tr>";
										}
										 //end of sheet
										html += "</table>";
										html += "<div>&nbsp;</div>";
										if (errInsert > 0){
											html +="<div>Gagal insert data : "+errInsert+"</div>";
										} else if (succInsert > 0){
											html +="<div>Sukses insert data : "+succInsert+"</div>";
										} else if (succUpdate > 0){
											html +="<div>Sukses update data : "+succUpdate+"</div>";
										}

									}
									running = false;
									table = html;
									File fileToSave = new File(path + FilenameUtils.getName(item.getName()));
									Files.copy(inStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
									running = false;
									table = html;
								}
							}
							running = false;
							table = html;

				//            TextLoader uploader = new TextLoader();
				//            ByteArrayInputStream inStream = null;
				//            
				//            uploader.uploadText(config, request, response);
				//            Object obj = uploader.getTextFile("file");
				//            byte byteText[] = null;
				//            byteText = (byte[]) obj;
				//            inStream = new ByteArrayInputStream(byteText);



						} catch (Exception exc){
							running = false;
						}
					}
				}
			};
				
			Thread t = new Thread(r);
			t.start();
		} catch (Exception exc){
			
		}
		
		
		
	}
	
	public String getTable() {

        return table;

    }
	
	public double getTotalData() {

        return countTotalData;

    }
	
	public double getCurrentProgress() {

        return currentProgress;

    }
	
	public boolean isRunning(){
		return running;
	}
}
