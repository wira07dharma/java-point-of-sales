/* 
 * Form Name  	:  FrmDailyRate.java 
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

package com.dimata.common.form.payment;

/* java package */ 
import javax.servlet.http.*; 

/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.common.entity.payment.*;

public class FrmDailyRate extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DailyRate dailyRate;

	public static final String FRM_NAME_DAILYRATE		=  "FRM_NAME_DAILYRATE" ;

	public static final int FRM_FIELD_DAILY_RATE_ID			=  0 ;
	public static final int FRM_FIELD_CURRENCY_TYPE_ID			=  1 ;
	public static final int FRM_FIELD_SELLING_RATE			=  2 ;
	public static final int FRM_FIELD_ROSTER_DATE			=  3 ;
        //matStockOpname.setStockOpnameTime(getString(FRM_FIELD_STOCK_OPNAME_TIME));

	public static String[] fieldNames = {
		"FRM_FIELD_DAILY_RATE_ID",  "FRM_FIELD_CURRENCY_TYPE_ID",
		"FRM_FIELD_SELLING_RATE",  "FRM_FIELD_ROSTER_DATE"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG + ENTRY_REQUIRED,
		TYPE_FLOAT + ENTRY_REQUIRED,  TYPE_DATE
	} ;

	public FrmDailyRate(){
	}
	public FrmDailyRate(DailyRate dailyRate){
		this.dailyRate = dailyRate;
	}

	public FrmDailyRate(HttpServletRequest request, DailyRate dailyRate){
		super(new FrmDailyRate(dailyRate), request);                                                
		this.dailyRate = dailyRate;
	}

	public String getFormName() { return FRM_NAME_DAILYRATE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DailyRate getEntityObject(){ return dailyRate; }

	public void requestEntityObject(DailyRate dailyRate) {
		try{
			this.requestParam();
			dailyRate.setCurrencyTypeId(getLong(FRM_FIELD_CURRENCY_TYPE_ID));
			dailyRate.setSellingRate(getDouble(FRM_FIELD_SELLING_RATE));                        
			dailyRate.setRosterDate(getDate(FRM_FIELD_ROSTER_DATE));
            //dailyRate.setRosterDate(new Date());
            //System.out.println(" dailyRate.getDate : "+dailyRate.getRosterDate());
        }catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
