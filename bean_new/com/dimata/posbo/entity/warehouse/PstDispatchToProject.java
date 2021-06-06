/**
 * User: wardana
 * Date: Mar 23, 2004
 * Time: 1:02:53 PM
 * Version: 1.0
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.I_Document;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import org.json.JSONObject;

public class PstDispatchToProject extends DBHandler implements I_DBInterface, I_DBType,
        I_PersintentExc, I_Language, I_Document {

    public static final String TBL_DISPATCH_TO_PROJECT = "dispatch_to_project";

    public static final int FLD_DISPATCH_MATERIAL_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_PROJECT_ID = 2;
    public static final int FLD_LOCATION_TYPE = 3;
    public static final int FLD_DISPATCH_DATE = 4;
    public static final int FLD_DISPATCH_CODE = 5;
    public static final int FLD_DISPATCH_STATUS = 6;
    public static final int FLD_REMARK = 7;
    public static final int FLD_DISPATCH_CODE_COUNTER = 8;
    public static final int FLD_DISPATCH_TO = 9;
    public static final int FLD_INVOICE_SUPPLIER = 10;

    public static final String[] stFieldNames = {
        "DISPATCH_MATERIAL_ID",
        "LOCATION_ID",
        "PROJECT_ID",
        "LOCATION_TYPE",
        "DISPATCH_DATE",
        "DISPATCH_CODE",
        "DISPATCH_STATUS",
        "REMARK",
        "DISPATCH_CODE_COUNTER",
        "DISPATCH_TO",
        "INVOICE_SUPPLIER"
    };

    public static final int[] iFieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING
    };

    public static final int FLD_TYPE_DISPATCH_LOCATION = 0;

    public static final String stFieldTypeDispatchLocation = "Project";

    public PstDispatchToProject() {
    }

    public PstDispatchToProject(int i) throws DBException {
        super(new PstDispatchToProject());
    }

    public PstDispatchToProject(String stOid) throws DBException {
        super(new PstDispatchToProject(0));
        if (!locate(stOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstDispatchToProject(long lOid) throws DBException {
        super(new PstDispatchToProject(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return stFieldNames.length;
    }

    public String getTableName() {
        return TBL_DISPATCH_TO_PROJECT;
    }

    public String[] getFieldNames() {
        return stFieldNames;
    }

    public int[] getFieldTypes() {
        return iFieldTypes;
    }

    public String getPersistentName() {
        return new PstDispatchToProject().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        DispatchToProject objDispatchToProject = fetchExc(ent.getOID());
        ent = objDispatchToProject;
        return ent.getOID();
    }

    public static DispatchToProject fetchExc(long lOid) throws DBException {
        try {
            DispatchToProject objDspToProject = new DispatchToProject();
            PstDispatchToProject objPstDspToProject = new PstDispatchToProject(lOid);

            objDspToProject.setOID(lOid);
            objDspToProject.setlLocationId(objPstDspToProject.getlong(FLD_LOCATION_ID));
            objDspToProject.setlProjectId(objPstDspToProject.getlong(FLD_PROJECT_ID));
            objDspToProject.setiLocationType(objPstDspToProject.getInt(FLD_LOCATION_TYPE));
            objDspToProject.setDtDispatchDate(objPstDspToProject.getDate(FLD_DISPATCH_DATE));
            objDspToProject.setStDispatchCode(objPstDspToProject.getString(FLD_DISPATCH_CODE));
            objDspToProject.setiDispatchStatus(objPstDspToProject.getInt(FLD_DISPATCH_STATUS));
            objDspToProject.setStRemark(objPstDspToProject.getString(FLD_REMARK));
            objDspToProject.setiDispatchCodeCounter(objPstDspToProject.getInt(FLD_DISPATCH_CODE_COUNTER));
            objDspToProject.setlDispatchTo(objPstDspToProject.getlong(FLD_DISPATCH_TO));
            objDspToProject.setStInvoiceSupplier(objPstDspToProject.getString(FLD_INVOICE_SUPPLIER));

            return objDspToProject;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDispatchToProject(0), DBException.UNKNOWN);
        }
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((DispatchToProject) ent);
    }

    public static long updateExc(DispatchToProject objDspToProject) throws DBException {
        try {
            if (objDspToProject.getOID() != 0) {
                PstDispatchToProject objPstDspToProject =
                        new PstDispatchToProject(objDspToProject.getOID());

                objPstDspToProject.setLong(FLD_LOCATION_ID, objDspToProject.getlLocationId());
                objPstDspToProject.setLong(FLD_PROJECT_ID, objDspToProject.getlProjectId());
                objPstDspToProject.setInt(FLD_LOCATION_TYPE, objDspToProject.getiLocationType());
                objPstDspToProject.setDate(FLD_DISPATCH_DATE, objDspToProject.getDtDispatchDate());
                objPstDspToProject.setString(FLD_DISPATCH_CODE, objDspToProject.getStDispatchCode());
                objPstDspToProject.setInt(FLD_DISPATCH_STATUS, objDspToProject.getiDispatchStatus());
                objPstDspToProject.setString(FLD_REMARK, objDspToProject.getStRemark());
                objPstDspToProject.setInt(FLD_DISPATCH_CODE_COUNTER, objDspToProject.getiDispatchCodeCounter());
                objPstDspToProject.setLong(FLD_DISPATCH_TO, objDspToProject.getlDispatchTo());
                objPstDspToProject.setString(FLD_INVOICE_SUPPLIER, objDspToProject.getStInvoiceSupplier());
                objPstDspToProject.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(objPstDspToProject.getUpdateSQL());
                return objDspToProject.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDispatchToProject(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long deleteExc(Entity ent) throws Exception {
        return deleteExt(ent.getOID());
    }

    public static long deleteExt(long lOid) throws DBException {
        try {
            PstDispatchToProject objPstDspToProject = new PstDispatchToProject(lOid);
            //delete detail item before delete parent
           // PstDispatchToProjectItem.deleteExcByParent(lOid);

            objPstDspToProject.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(objPstDspToProject.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDispatchToProject(0), DBException.UNKNOWN);
        }

        return lOid;
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DispatchToProject) ent);
    }

    public static long insertExc(DispatchToProject objDspToProject) throws DBException {
        try {
            //System.out.println(".............masuk insert.........");
            PstDispatchToProject objPstDspToProject = new PstDispatchToProject(0);
            objPstDspToProject.setLong(FLD_LOCATION_ID, objDspToProject.getlLocationId());
            objPstDspToProject.setLong(FLD_PROJECT_ID, objDspToProject.getlProjectId());
            objPstDspToProject.setInt(FLD_LOCATION_TYPE, objDspToProject.getiLocationType());
            objPstDspToProject.setDate(FLD_DISPATCH_DATE, objDspToProject.getDtDispatchDate());
            objPstDspToProject.setString(FLD_DISPATCH_CODE, objDspToProject.getStDispatchCode());
            objPstDspToProject.setInt(FLD_DISPATCH_STATUS, objDspToProject.getiDispatchStatus());
            objPstDspToProject.setString(FLD_REMARK, objDspToProject.getStRemark());
            objPstDspToProject.setInt(FLD_DISPATCH_CODE_COUNTER, objDspToProject.getiDispatchCodeCounter());
            objPstDspToProject.setLong(FLD_DISPATCH_TO, objDspToProject.getlDispatchTo());
            objPstDspToProject.setString(FLD_INVOICE_SUPPLIER, objDspToProject.getStInvoiceSupplier());

            objPstDspToProject.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(objPstDspToProject.getInsertSQL());
            objDspToProject.setOID(objPstDspToProject.getlong(FLD_DISPATCH_MATERIAL_ID));
        }
        catch (DBException dbe) {
            throw dbe;
        }
        catch (Exception e) {
            throw new DBException(new PstDispatchToProject(0), DBException.UNKNOWN);
        }
        return objDspToProject.getOID();
    }

    public int getDocumentStatus(long documentId) {
        try{
            DispatchToProject objDispatchToProject = fetchExc(documentId);
            return objDispatchToProject.getiDispatchStatus();
        }
        catch(DBException dbe){
            dbe.printStackTrace();
        }
        return 0;
    }

    public int setDocumentStatus(long documentId, int indexStatus) {
        return indexStatus;
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         DispatchToProject dispatchToProject = PstDispatchToProject.fetchExc(oid);
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_MATERIAL_ID], dispatchToProject.getOID());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_ID], dispatchToProject.getlLocationId());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_PROJECT_ID], dispatchToProject.getlProjectId());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_TYPE], dispatchToProject.getiLocationType());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_DATE], dispatchToProject.getDtDispatchDate());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_CODE], dispatchToProject.getStDispatchCode());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_STATUS], dispatchToProject.getiDispatchStatus());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_REMARK], dispatchToProject.getStRemark());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_CODE_COUNTER], dispatchToProject.getiDispatchCodeCounter());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_TO], dispatchToProject.getlDispatchTo());
         object.put(PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_INVOICE_SUPPLIER], dispatchToProject.getStInvoiceSupplier());
      }catch(Exception exc){}
      return object;
   }
}
