/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.form.billing;

/**
 *
 * @author Wiweka
 */
import javax.servlet.http.*;

// qdep package
import com.dimata.qdep.form.*;

// project package
import com.dimata.pos.entity.billing.*;

public class  FrmBillDetailCode extends FRMHandler implements I_FRMInterface, I_FRMType  {

    	private BillDetailCode billDetailCode;

	public static final String FRM_NAME_BILL_DETAIL_CODE	=  "FRM_NAME_BILL_DETAIL_CODE" ;

	public static final int FRM_FIELD_CASH_BILL_DETAIL_CODE_ID       =  0;
        public static final int FRM_FIELD_CASH_BILL_DETAIL_ID         =  1;
	public static final int FRM_FIELD_STOCK_CODE             =  2;
        public static final int FRM_FIELD_STOCK_VALUE  =  3;


	public static String[] fieldNames =
        {
            "FRM_FIELD_CASH_BILL_DETAIL_CODE_ID",
            "FRM_FIELD_CASH_BILL_DETAIL_ID",
            "FRM_FIELD_STOCK_CODE",
            "FRM_FIELD_STOCK_VALUE"

	} ;

	public static int[] fieldTypes =
        {
		TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_FLOAT
	} ;

	public FrmBillDetailCode ()
        {
	}

        public FrmBillDetailCode (BillDetailCode billDetailCode)
        {
		this.billDetailCode= billDetailCode;
	}

	public FrmBillDetailCode (HttpServletRequest request, BillDetailCode billDetailCode)
        {
		super(new FrmBillDetailCode(billDetailCode), request);
		this.billDetailCode = billDetailCode;
	}

	public String getFormName() { return FRM_NAME_BILL_DETAIL_CODE; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public BillDetailCode getEntityObject(){ return billDetailCode; }

	public void requestEntityObject(BillDetailCode billDetailCode)
        {
		try
                {
			this.requestParam();
                        //billDetailCode.setOID(getLong(FRM_FIELD_CASH_BILL_DETAIL_ID));
                        billDetailCode.setSaleItemId(getLong(FRM_FIELD_CASH_BILL_DETAIL_ID));
                        billDetailCode.setStockCode(getString(FRM_FIELD_STOCK_CODE));
                        billDetailCode.setValue(getDouble(FRM_FIELD_STOCK_VALUE));


		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}

}
