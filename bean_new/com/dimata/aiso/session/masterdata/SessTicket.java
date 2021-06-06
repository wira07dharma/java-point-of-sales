/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.masterdata;

import com.dimata.aiso.entity.masterdata.PstTicketRate;
import com.dimata.aiso.entity.search.SrcTicket;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class SessTicket {

    public static synchronized Vector listTicket(int start, int recordToGet, SrcTicket srcTicket){
	Vector vResult = new Vector(1,1);
	try{
	    String whClause = stringWhClause(srcTicket);
	    String orderBy = stringOrderBy(srcTicket);
	    vResult = PstTicketRate.list(start, recordToGet, whClause, orderBy);
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized int getCountTicket(SrcTicket srcTicket){
	int iResult = 0;
	try{
	    String whClause = stringWhClause(srcTicket);
	    iResult = PstTicketRate.getCount(whClause);
	}catch(Exception e){}
	return iResult;
    }
    
    public static synchronized String stringWhClause(SrcTicket srcTicket){
	String sResult = "";
	try{
	    if(srcTicket.getCarrierId() != 0){
		sResult = PstTicketRate.fieldNames[PstTicketRate.FLD_CARRIER_ID]+" = "+srcTicket.getCarrierId();
	    }
	    
	    if(srcTicket.getRouteId() != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+PstTicketRate.fieldNames[PstTicketRate.FLD_ROUTE_ID]+" = "+srcTicket.getRouteId();
		}else{
		    sResult = PstTicketRate.fieldNames[PstTicketRate.FLD_ROUTE_ID]+" = "+srcTicket.getRouteId();
		}
	    }
	    
	    if(srcTicket.getClassId() != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+PstTicketRate.fieldNames[PstTicketRate.FLD_CLASS_ID]+" = "+srcTicket.getClassId();
		}else{
		    sResult = PstTicketRate.fieldNames[PstTicketRate.FLD_CLASS_ID]+" = "+srcTicket.getClassId();		    
		}
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized String stringOrderBy(SrcTicket srcTicket){
	String sResult = "";
	try{
	    switch(srcTicket.getOrderBy()){
		case 0:
		    sResult = PstTicketRate.fieldNames[PstTicketRate.FLD_CARRIER_ID];
		    break;
		case 1:
		    sResult = PstTicketRate.fieldNames[PstTicketRate.FLD_ROUTE_ID];
		    break;
		case 2:
		    sResult = PstTicketRate.fieldNames[PstTicketRate.FLD_CLASS_ID];
		    break;
	    }
	}catch(Exception e){}
	return sResult;
    }
}
