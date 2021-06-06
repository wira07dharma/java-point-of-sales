/**
 * @author	    : gedhy
 * @version	    : 01
 */

package com.dimata.qdep.entity;

import java.util.*;

public interface I_Approval {

    /**
    * declaration of identifier to hold name of class that implements this Interface
    */
    public static final String APPROVAL_CLASSNAME = "com.dimata.workflow.entity.approval.PstDocAppMain";

    /**
    * declaration of identifier to hold accessibility status of approval
    */
    public static final int STATUS_READONLY   = 0;
    public static final int STATUS_APPROVABLE = 1;

    /**
    * declaration of identifier to hold status of each existing approval, 'CHECKED' or 'UNCHECKED'
    */
    public static final int STATUS_NOT_APPROVE = 0;
    public static final int STATUS_APPROVE 	= 1;

    /**
    * declaration of identifier to hold display type of approval, 'HORIZONTAL' or 'VERTICAL'
    */
    public static final int PRINT_HORIZONTAL = 0;
    public static final int PRINT_VERTICAL   = 1;

    /**
    * this method used to get default approval defend on document type
    */
    public Vector listDefaultApproval(int docType);

    /**
    * this method used to get index of approval that related to current user on USER SESSION
    */
    public int getUserApprovalIndex(int sysName, int docType, long departmentId, long sectionId, long positionId);

    /**
    * this method used to get index of approval that related to current user on USER SESSION with check document status first
    */
    public int getUserApprovalIndex(String className, long docOid, int sysName, int docType, long departmentId, long sectionId, long positionId);

    /**
    * this method used to get approvalOid for specify user
    */
    public long getUserApprovalId(int sysName, int docType, long departmentId, long sectionId, long positionId, int ownIndex);

    /**
    * this method used to get approval and document status
    * return : Vector of 'html of approval', 'html of document status' and 'current document status'
    */
    public Vector getStatusApproval(int iCommand, long docOid, int docType, int printOrientation, long deptId, long secId, long positionId,
                                    long empId, Vector vectAppStatus, int ownIndex, String className, long appMappingId, String formName,
                                    String attrForm, String hiddenFormName, String headerName, int docStatus);

    /**
    * this method used to get vector approval of specified document
    * return  : vector of vector that consist : 'app name', 'app title', 'app person'
    * example : 'CREATED BY', 'PURCHASING CLERK', 'EDHY PUTRA'
    */
    public Vector getVectorApproval(long documentId, int docType);

}
