/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.TicketRate;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmTicketRate extends FRMHandler implements I_FRMInterface, I_FRMType{

	public static final String FRM_TICKET_RATE = "FRM_TICKET_RATE";
	
	public static final int FRM_CARRIER_ID = 0;
	public static final int FRM_ROUTE_ID = 1;
	public static final int FRM_CLASS_ID = 2;
	public static final int FRM_RATE = 3;
	public static final int FRM_NET_RATE_TO_AIRLINE = 4;
	
	public static String[] fieldNames = {
	    "FRM_CARRIER_ID",
	    "ROUTE_ID",
	    "CLASS_ID",
	    "RATE",
	    "NET_RATE_TO_AIRLINE"
	};
	
	public static int[] fieldTypes = {
	    TYPE_LONG,
	    TYPE_LONG,
	    TYPE_LONG,
	    TYPE_FLOAT,	    
	    TYPE_FLOAT	    
	};
	
    private TicketRate objTicketRate;

    public FrmTicketRate(TicketRate objTicketRate) {
        this.objTicketRate = objTicketRate;
    }

    public FrmTicketRate(HttpServletRequest request, TicketRate objTicketRate) {
        super(new FrmTicketRate(objTicketRate), request);
        this.objTicketRate = objTicketRate;
    }

    public String getFormName() {
        return FRM_TICKET_RATE;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public TicketRate getEntityObject() {
        return objTicketRate;
    }

    public void requestEntityObject(TicketRate objTicketRate) {
        try {
            this.requestParam();
            objTicketRate.setCarrierId(this.getLong(FRM_CARRIER_ID));
            objTicketRate.setRouteId(this.getLong(FRM_ROUTE_ID));
            objTicketRate.setClassId(this.getLong(FRM_CLASS_ID));
            objTicketRate.setRate(this.getDouble(FRM_RATE));
            objTicketRate.setNetRateToAirLine(this.getDouble(FRM_NET_RATE_TO_AIRLINE));
            this.objTicketRate = objTicketRate;
        } catch (Exception e) {
            objTicketRate = new TicketRate();
        }
    }
}
