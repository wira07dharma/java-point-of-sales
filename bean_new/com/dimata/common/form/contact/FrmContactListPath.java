
package com.dimata.common.form.contact;

/**
 *
 * @author Witar
 */
import com.dimata.common.entity.contact.ContactListPath;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmContactListPath extends FRMHandler implements I_FRMInterface, I_FRMType {
  
    private ContactListPath entContactListPath;
    public static final String FRM_NAME_CONTACTLISTPATH = "FRM_NAME_CONTACTLISTPATH";
    public static final int FRM_FIELD_CONTACT_LIST_PATH = 0;
    public static final int FRM_FIELD_CONTACT_ID = 1;
    public static final int FRM_FIELD_ADDRESS = 2;
    public static final int FRM_FIELD_LATITUDE = 3;
    public static final int FRM_FIELD_LONGITUDE = 4;


    public static String[] fieldNames = {
        "FRM_FIELD_CONTACT_LIST_PATH",
        "FRM_FIELD_CONTACT_ID",
        "FRM_FIELD_ADDRESS",
        "FRM_FIELD_LATITUDE",
        "FRM_FIELD_LONGITUDE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public FrmContactListPath() {
    }

    public FrmContactListPath(ContactListPath entContactListPath) {
        this.entContactListPath = entContactListPath;
    }

    public FrmContactListPath(HttpServletRequest request, ContactListPath entContactListPath) {
        super(new FrmContactListPath(entContactListPath), request);
        this.entContactListPath = entContactListPath;
    }

    public String getFormName() {
        return FRM_NAME_CONTACTLISTPATH;
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

    public ContactListPath getEntityObject() {
        return entContactListPath;
    }

    public void requestEntityObject(ContactListPath entContactListPath) {
        try {
            this.requestParam();
                entContactListPath.setOID(getLong(FRM_FIELD_CONTACT_LIST_PATH));
                entContactListPath.setContactId(getLong(FRM_FIELD_CONTACT_ID));
                entContactListPath.setAddress(getString(FRM_FIELD_ADDRESS));
                entContactListPath.setLatitude(getFloat(FRM_FIELD_LATITUDE));
                entContactListPath.setLongitude(getFloat(FRM_FIELD_LONGITUDE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}