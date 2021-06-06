/*
 * Form Name  	:  FrmLocation.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	: lkarunia
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.form.logger;

import javax.servlet.http.*;
import com.dimata.qdep.form.*;
import com.dimata.common.entity.logger.DocLogger;

public class FrmDocLogger extends FRMHandler implements I_FRMInterface, I_FRMType {
    private DocLogger docLogger;

    public static final String FRM_NAME_DOC_LOGGER = "FRM_DOC_LOGGER";

    public static final int FRM_FIELD_DOC_TYPE = 0;
    public static final int FRM_FIELD_DOC_NUMBER = 1;
    public static final int FRM_FIELD_DATE = 2;
    public static final int FRM_FIELD_DESCRIPTION = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_DOC_TYPE",
        "FRM_FIELD_DOC_NUMBER",
        "FRM_FIELD_DATE",
        "FRM_FIELD_DESCRIPTION"
    };

    public static int[] fieldTypes = {
        TYPE_INT + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_STRING
    };

    public FrmDocLogger() {
    }

    public FrmDocLogger(DocLogger docLogger) {
        this.docLogger = docLogger;
    }

    public FrmDocLogger(HttpServletRequest request, DocLogger docLogger) {
        super(new FrmDocLogger(docLogger), request);
        this.docLogger = docLogger;
    }

    public String getFormName() {
        return FRM_NAME_DOC_LOGGER;
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

    public DocLogger getEntityObject() {
        return docLogger;
    }

    public void requestEntityObject(DocLogger docLogger) {
        try {
            this.requestParam();
            docLogger.setDocType(getInt(FRM_FIELD_DOC_TYPE));
            docLogger.setDocNumber(getString(FRM_FIELD_DOC_NUMBER));
            docLogger.setDescription(getString(FRM_FIELD_DESCRIPTION));
            docLogger.setDocDate(getDate(FRM_FIELD_DATE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
