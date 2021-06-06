/* 
 * Form Name  	:  FrmMatVendorPrice.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.masterdata.MatVendorPrice;

public class FrmMatVendorPrice extends FRMHandler implements I_FRMInterface, I_FRMType  {

	private MatVendorPrice matVendorPrice;
    private HttpServletRequest req;

	public static final String FRM_NAME_VENDOR_PRICE =  "FRM_NAME_VENDOR_PRICE" ;

	public static final int FRM_FIELD_MATERIAL_ID = 0;
	public static final int FRM_FIELD_BUYING_UNIT_ID = 1;
	public static final int FRM_FIELD_VENDOR_ID = 2;
	public static final int FRM_FIELD_VENDOR_PRICE_CODE = 3;
	public static final int FRM_FIELD_VENDOR_PRICE_BARCODE = 4;
	public static final int FRM_FIELD_PRICE_CURRENCY = 5;
        public static final int FRM_FIELD_ORG_BUYING_PRICE = 6;
        public static final int FRM_FIELD_LAST_DISCOUNT = 7;
        public static final int FRM_FIELD_LAST_VAT = 8;
        public static final int FRM_FIELD_CURR_BUYING_PRICE = 9;
	public static final int FRM_FIELD_DESCRIPTION = 10;
        public static final int FRM_FIELD_LAST_COST_CARGO = 11;

    //add Discount1 , discount2
        public static final int FRM_FIELD_LAST_DISCOUNT_1 = 12;
        public static final int FRM_FIELD_LAST_DISCOUNT_2 = 13;

    public static String[] fieldNames = {
	"FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_BUYING_ID",
        "FRM_FIELD_VENDOR_ID",
	"FRM_FIELD_VENDOR_PRICE_CODE",
        "FRM_FIELD_VENDOR_PRICE_BARCODE",
	"FRM_FIELD_PRICE_CURRENCY",
        "FRM_FIELD_ORG_BUYING_PRICE",
	"FRM_FIELD_LAST_DISCOUNT",
        "FRM_FIELD_LAST_VAT",
        "FRM_FIELD_CURR_BUYING_PRICE",
        "FRM_FIELD_PRICE_DESCRIPTION",
        "FRM_FIELD_LAST_COST_CARGO",
         //add Discount_1 & discount 2
        "FRM_FIELD_LAST_DISCOUNT_1",
        "FRM_FIELD_LAST_DISCOUNT_2"
    } ;

	public static int[] fieldTypes = {
	TYPE_LONG,
	TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_FLOAT,
	TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_FLOAT,
        //ADD TYPE DISC1 & DISC2
        TYPE_FLOAT,
        TYPE_FLOAT
    } ;

	public FrmMatVendorPrice(){
	}

	public FrmMatVendorPrice(MatVendorPrice matVendorPrice){
		this.matVendorPrice = matVendorPrice;
	}

	public FrmMatVendorPrice(HttpServletRequest request, MatVendorPrice matVendorPrice){
		super(new FrmMatVendorPrice(matVendorPrice), request);
		this.matVendorPrice = matVendorPrice;
        req = request;
	}

	public String getFormName() { return FRM_NAME_VENDOR_PRICE; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; } 

	public MatVendorPrice getEntityObject(){ return matVendorPrice; }

	public void requestEntityObject(MatVendorPrice matVendorPrice) {
		try{
			this.requestParam();
			matVendorPrice.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
			matVendorPrice.setVendorId(getLong(FRM_FIELD_VENDOR_ID));
			matVendorPrice.setBuyingUnitId(getLong(FRM_FIELD_BUYING_UNIT_ID));
			matVendorPrice.setVendorPriceBarcode(getString(FRM_FIELD_VENDOR_PRICE_BARCODE));
			matVendorPrice.setVendorPriceCode(getString(FRM_FIELD_VENDOR_PRICE_CODE));
			matVendorPrice.setPriceCurrency(getLong(FRM_FIELD_PRICE_CURRENCY));
			matVendorPrice.setLastDiscount(getDouble(FRM_FIELD_LAST_DISCOUNT));
			matVendorPrice.setLastVat(getDouble(FRM_FIELD_LAST_VAT));
                        matVendorPrice.setCurrBuyingPrice(getDouble(FRM_FIELD_CURR_BUYING_PRICE));
                        matVendorPrice.setOrgBuyingPrice(getDouble(FRM_FIELD_ORG_BUYING_PRICE));
			matVendorPrice.setDescription(getString(FRM_FIELD_DESCRIPTION));
                        matVendorPrice.setLastCostCargo(getDouble(FRM_FIELD_LAST_COST_CARGO ));
                        //ADD DISC1 & DISC2
                        matVendorPrice.setLastDiscount1(getDouble(FRM_FIELD_LAST_DISCOUNT_1));
                        matVendorPrice.setLastDiscount2(getDouble(FRM_FIELD_LAST_DISCOUNT_2));
        }catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}


    // ini di pakai untuk menyimpan data vendor price
    // ini di buat pass pada saat simple catalog
    public MatVendorPrice getRequestForInsert(long oidMaterial) {
        try{
            long oidVendor = FRMQueryString.requestLong(req, "hidden_vendor_id");
            double dblRp = FRMQueryString.requestDouble(req, fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]);
            double dblUsd = FRMQueryString.requestDouble(req, fieldNames[FrmMatVendorPrice.FRM_FIELD_ORG_BUYING_PRICE]);

            matVendorPrice.setMaterialId(oidMaterial);
            matVendorPrice.setVendorId(oidVendor);
            matVendorPrice.setCurrBuyingPrice(dblRp);
            matVendorPrice.setOrgBuyingPrice(dblUsd);

        }catch(Exception e){
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
        return matVendorPrice;
    }
}
