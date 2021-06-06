/**
 * @author	    : gedhy
 * @version	    : 01
 */

package com.dimata.qdep.entity;

public interface I_PstDocType {

    /**
    * this method used to check if data with type "iDocType" already exist in db or not
    * return 'true' if data already exist and otherwise 'false'
	*/                                                       
    public boolean existDocType(int iSysType, int iDocType);

    /**
    * this method used to compose a documentType from its 'System Type' and 'Document Name'
    * exp : 'System Material' and 'Regular Purchase Order'
    */
    public int composeDocumentType(int iSystemType, int iDocType);

    /**
    * this method used to get 'System Type' from specify documentType
    * exp : return long refers to 'System Material'
    */
    public long getSystemIndex(int intDocumentType);

    /**
    * this method used to get 'Document Name' from specify documentType
    * exp : return int refers to 'Regular Purchase Order'
    */
    public int getDocTypeIndex(int intDocumentType);

    /**
    * this method used to get document code from specify documentType
    * exp : return text 'PO'
    */
    public String getDocCode(int docType);

    /**
    * this method used to get document title from specify documentType
    * exp : return text 'Purchase Order'
    */
    public String getDocTitle(int docType);

}
