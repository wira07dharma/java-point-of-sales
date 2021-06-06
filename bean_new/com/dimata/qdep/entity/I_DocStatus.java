/**
 * @author	    : gedhy
 * @version	    : 01
 */

package com.dimata.qdep.entity;

import java.util.*;

public interface I_DocStatus {

    /**
    * declaration of identifier to hold name of class that implement this Interface
    */
    public static final String DOCTSTATUS_CLASSNAME = "com.dimata.workflow.entity.status.PstDocStatus";

    /**
    * declaration of identifier to handle index of each document status
    */
    public static final int DOCUMENT_STATUS_DRAFT 			= 0;
    public static final int DOCUMENT_STATUS_TO_BE_APPROVED  = 1;
    public static final int DOCUMENT_STATUS_FINAL 			= 2;
    public static final int DOCUMENT_STATUS_REVISED 		= 3;
    public static final int DOCUMENT_STATUS_PROCEED			= 4;
    public static final int DOCUMENT_STATUS_CLOSED 			= 5;
    public static final int DOCUMENT_STATUS_CANCELLED 		= 6;
    public static final int DOCUMENT_STATUS_POSTED	 		= 7;

    /**
    * declaration of identifier to handle index of each payment status
    */    
    //public static final int PAYMENT_STATUS_POSTED_NOT_CLEARED   = 7;    // ini disetarakan dengan DOCUMENT_STATUS_POSTED, karena di IJ tidak tahu apakah payment ini perlu clearing ato tidak.    
    public static final int PAYMENT_STATUS_POSTED_CLEARED       = 8;
    public static final int PAYMENT_STATUS_POSTED_CLOSED        = 9;
    //
     public static final int DOCUMENT_STATUS_APPROVED        = 10;
    
    
    /**
    * declaration of identifier to explain document status above
    */
    public static final String[] fieldDocumentStatus = {
		"Draft",
		"To Be Approved",
		"Final",
		"Revised",
                "Proceed",
                "Closed",
                "Cancelled",
                "Posted",   // khusus utk dokumen payment, sebenarnya namanya "POSTED NOT CLEARED"
                "Posted Cleared",
                "Posted Closed",
                "Approved"
    };

    /**
    * this method used to get document status for specify docType
    * return vector of document status (index and name)
	*/                                                       
    public Vector getDocStatusFor(int docType);

    /**
    * this method used to get name of document status
    * return String status' name
	*/                                                       
    public String getDocStatusName(int indexStatus);

    /**
    * this method used to get name of document status
    * return String status' name
	*/                                                       
    public String getDocStatusName(int docType, int indexStatus);

}
