
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.workflow.entity.status; 

/* package java */ 
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

/* package workflow */
import com.dimata.workflow.entity.approval.*;

public class PstDocStatus extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_DocStatus {

	public static final  String TBL_WF_DOC_STATUS = "wf_doc_status";

	public static final  int FLD_DOC_STATUS_OID = 0;
	public static final  int FLD_TYPE = 1;
	public static final  int FLD_NAME = 2;
	public static final  int FLD_DESCRIPTION = 3;
	public static final  int FLD_DOCTYPE = 4;

	public static final  String[] fieldNames = {
		"DOC_STATUS_OID",
		"TYPE",
		"NAME",
		"DESCRIPTION",
		"DOCTYPE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
        TYPE_INT
	 }; 

	public PstDocStatus(){
	}

	public PstDocStatus(int i) throws DBException { 
		super(new PstDocStatus()); 
	}

	public PstDocStatus(String sOid) throws DBException { 
		super(new PstDocStatus(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDocStatus(long lOid) throws DBException { 
		super(new PstDocStatus(0)); 
		String sOid = "0"; 
		try { 
			sOid = String.valueOf(lOid); 
		}catch(Exception e) { 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	} 

	public int getFieldSize(){ 
		return fieldNames.length; 
	}

	public String getTableName(){ 
		return TBL_WF_DOC_STATUS;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDocStatus().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DocStatus docstatus = fetchExc(ent.getOID()); 
		ent = (Entity)docstatus; 
		return docstatus.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DocStatus) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DocStatus) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DocStatus fetchExc(long oid) throws DBException{ 
		try{ 
			DocStatus docstatus = new DocStatus();
			PstDocStatus pstDocStatus = new PstDocStatus(oid); 
			docstatus.setOID(oid);

			docstatus.setType(pstDocStatus.getInt(FLD_TYPE));
			docstatus.setName(pstDocStatus.getString(FLD_NAME));
			docstatus.setDescription(pstDocStatus.getString(FLD_DESCRIPTION));
			docstatus.setDocType(pstDocStatus.getInt(FLD_DOCTYPE));

			return docstatus; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocStatus(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DocStatus docstatus) throws DBException{ 
		try{ 
			PstDocStatus pstDocStatus = new PstDocStatus(0);

			pstDocStatus.setInt(FLD_TYPE, docstatus.getType());
			pstDocStatus.setString(FLD_NAME, docstatus.getName());
			pstDocStatus.setString(FLD_DESCRIPTION, docstatus.getDescription());
			pstDocStatus.setInt(FLD_DOCTYPE, docstatus.getDocType());

			pstDocStatus.insert(); 
			docstatus.setOID(pstDocStatus.getlong(FLD_DOC_STATUS_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocStatus(0),DBException.UNKNOWN); 
		}
		return docstatus.getOID();
	}

	public static long updateExc(DocStatus docstatus) throws DBException{ 
		try{ 
			if(docstatus.getOID() != 0){ 
				PstDocStatus pstDocStatus = new PstDocStatus(docstatus.getOID());

				pstDocStatus.setInt(FLD_TYPE, docstatus.getType());
				pstDocStatus.setString(FLD_NAME, docstatus.getName());
				pstDocStatus.setString(FLD_DESCRIPTION, docstatus.getDescription());
				pstDocStatus.setInt(FLD_DOCTYPE, docstatus.getDocType());

				pstDocStatus.update(); 
				return docstatus.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocStatus(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDocStatus pstDocStatus = new PstDocStatus(oid);
			pstDocStatus.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocStatus(0),DBException.UNKNOWN); 
		}
		return oid;
	}

	public static Vector listAll(){ 
		return list(0, 500, "",""); 
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_WF_DOC_STATUS; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
						if(limitStart == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

                        break;

                 case DBHandler.DBSVR_POSTGRESQL :
						if(limitStart == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;

                        break;

                 case DBHandler.DBSVR_SYBASE :
                    	break;

                 case DBHandler.DBSVR_ORACLE :
                    	break;

                 case DBHandler.DBSVR_MSSQL :
                    	break;

                default:
                    ;
            }

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				DocStatus docstatus = new DocStatus();
				resultToObject(rs, docstatus);
				lists.add(docstatus);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}

	private static void resultToObject(ResultSet rs, DocStatus docstatus){
		try{
			docstatus.setOID(rs.getLong(PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID]));
			docstatus.setType(rs.getInt(PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE]));
			docstatus.setName(rs.getString(PstDocStatus.fieldNames[PstDocStatus.FLD_NAME]));
			docstatus.setDescription(rs.getString(PstDocStatus.fieldNames[PstDocStatus.FLD_DESCRIPTION]));
			docstatus.setDocType(rs.getInt(PstDocStatus.fieldNames[PstDocStatus.FLD_DOCTYPE]));
		}catch(Exception e){ }
	}

	public static boolean checkOID(long docStatusOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_DOC_STATUS + " WHERE " + 
						PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] + " = " + docStatusOid;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] + ") FROM " + TBL_WF_DOC_STATUS;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while(rs.next()) { count = rs.getInt(1); }

			rs.close();
			return count;
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
	}


	/* This method used to find current data */
	public static int findLimitStart( long oid, int recordToGet, String whereClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   DocStatus docstatus = (DocStatus)list.get(ls);
				   if(oid == docstatus.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    /**
    * this method used to check availability of document status
    * return TRUE if available otherwise FALSE
    */
    public static boolean cekDocStatus(int docType, int statusType, long oid){
        DBResultSet dbrs = null;
        boolean bool = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_DOC_STATUS +
                	     " WHERE " + fieldNames[FLD_DOCTYPE] + " = " + docType +
                         " AND " + fieldNames[FLD_TYPE] + " = " + statusType +
                         " AND " + fieldNames[FLD_DOC_STATUS_OID] + " <> '" + oid + "'";
            //System.out.println("#####################PstDocStatus.cekDocStatus.sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
				bool = true;
            }
    	}catch(Exception e){
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.cekDocStatus.err : "+e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return bool;
    	}
    }

    /**
    * this method used to generate string refers to 'DOCUMENT STATUS'
    * DOCUMENT STATUS ---> status that change automatically defend on trigger of approval
    * @vectDocStatus ---> akan digunakan pada pengembangn selajutnya jika data 'DOCUMENT STATUS' akan dijadikan dynamic dan ngambil dari db
    * untuk sementara, DOCUMENT STATUS kita hard coded
    */
    public static String getDocumentStatus(Vector vectDocStatus){
        String result = "";

        // mengisi vectDocStatus secara hard coded
		Vector vectDocumentStatus = new Vector(1,1);
        vectDocumentStatus.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
        vectDocumentStatus.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
        vectDocumentStatus.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
        vectDocumentStatus.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_POSTED));

        // generate string dari vecDocStatus yang ada
        if(vectDocumentStatus!=null && vectDocumentStatus.size()>0){
            int maxDocStatus = vectDocumentStatus.size();
            for(int i=0; i<maxDocStatus; i++){
        		result = result + String.valueOf(vectDocumentStatus.get(i));
                if(i<maxDocStatus-1){
                    result = result + ",";
                }
            }
        }
        return result;
    }

    /**
    * this method used to get 'document status' for specify document type
    */
    public static Vector getStatusOfDocument(int docType){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT " + PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] +
						 ", " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
						 ", " + PstDocStatus.fieldNames[PstDocStatus.FLD_NAME] +
						 ", " + PstDocStatus.fieldNames[PstDocStatus.FLD_DESCRIPTION] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                	     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_DOCTYPE] +
                         " = " + docType +
                	     " ORDER BY " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE];
            //System.out.println("###########################PstDocStatus.getStatusOfDocument.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                DocStatus docStatus = new DocStatus();
                docStatus.setOID(rs.getLong(1));
                docStatus.setType(rs.getInt(2));
                docStatus.setName(rs.getString(3));
                docStatus.setDescription(rs.getString(4));
                result.add(docStatus);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getStatusOfDocument.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * this method used to get 'action status' for specify document type
    */
    public static Vector getStatusOfAction(int docType){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT " + PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] +
						 ", " + PstDocStatus.fieldNames[PstDocStatus.FLD_NAME] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                	     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_DOCTYPE] +
                         " = " + docType +
                         " AND " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
                         " NOT IN (" + getDocumentStatus(new Vector()) + ") " +
                	     " ORDER BY " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE];
            //System.out.println("###########################PstDocStatus.getStatusOfAction.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                DocStatus docStatus = new DocStatus();
                docStatus.setOID(rs.getLong(1));
                docStatus.setName(rs.getString(2));
                result.add(docStatus);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getStatusOfAction.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * this method used to get indexStatus depend on appMappingId
    */
	public static int getStatusIndexBefore(long appMappingId){
        int result = 0;
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT ST." + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS + " AS ST " +
                         " INNER JOIN " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " ON ST." + PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] +
                         " = MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DOC_STATUS_OID_BEFORE] +
                	     " WHERE MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                         " = " + appMappingId;
            //System.out.println("###########################PstDocStatus.getStatusIndexBefore.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = rs.getInt(1);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getStatusIndexBefore.sql : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * this method used to get indexStatus depend on appMappingId
    */
	public static int getStatusIndexAfter(long appMappingId){
        int result = 0;
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT ST." + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS + " AS ST " +
                         " INNER JOIN " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " ON ST." + PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] +
                         " = MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DOC_STATUS_OID_AFTER] +
                	     " WHERE MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                         " = " + appMappingId;
            //System.out.println("###########################PstDocStatus.getStatusIndexAfter.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = rs.getInt(1);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getStatusIndexAfter.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * this method used to get 'action status' depend on appMappingId and current 'document status'
    */
	public static Vector listActionStatus(long appMappingId, int currStatus){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT ST." + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
						 ", ST." + PstDocStatus.fieldNames[PstDocStatus.FLD_NAME] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS + " AS ST " +
                         " INNER JOIN " + PstChangesStatus.TBL_WF_CHANGES_STATUS + " AS CS " +
                         " ON ST." + PstDocStatus.fieldNames[PstDocStatus.FLD_DOC_STATUS_OID] +
                         " = CS." + PstChangesStatus.fieldNames[PstChangesStatus.FLD_DOC_STATUS_OID] +
                         " INNER JOIN " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " ON CS." + PstChangesStatus.fieldNames[PstChangesStatus.FLD_APP_MAPPING_OID] +
                         " = MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                	     " WHERE MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                         " = " + appMappingId;
            //System.out.println("#############################PstDocStatus.listActionStatus.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector vectTemp = new Vector(1,1);

                int idxStatus = rs.getInt(1);
                switch(currStatus){
	                case I_DocStatus.DOCUMENT_STATUS_DRAFT :
						 if(idxStatus==I_DocStatus.DOCUMENT_STATUS_CANCELLED){
			                vectTemp.add(String.valueOf(idxStatus));
			                vectTemp.add(String.valueOf(rs.getString(2)));
		                    result.add(vectTemp);
                    	 }
                    	 break;

	                case I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED :
						 if(idxStatus==I_DocStatus.DOCUMENT_STATUS_REVISED || idxStatus==I_DocStatus.DOCUMENT_STATUS_CANCELLED){
			                vectTemp.add(String.valueOf(idxStatus));
			                vectTemp.add(String.valueOf(rs.getString(2)));
		                    result.add(vectTemp);
                    	 }
                    	 break;

	                case I_DocStatus.DOCUMENT_STATUS_FINAL :
						 if(idxStatus==I_DocStatus.DOCUMENT_STATUS_REVISED || idxStatus==I_DocStatus.DOCUMENT_STATUS_PROCEED || idxStatus==I_DocStatus.DOCUMENT_STATUS_CANCELLED){
			                vectTemp.add(String.valueOf(idxStatus));
			                vectTemp.add(String.valueOf(rs.getString(2)));
		                    result.add(vectTemp);
                    	 }
                    	 break;

	                case I_DocStatus.DOCUMENT_STATUS_PROCEED :
						 if(idxStatus==I_DocStatus.DOCUMENT_STATUS_CLOSED){
			                vectTemp.add(String.valueOf(idxStatus));
			                vectTemp.add(String.valueOf(rs.getString(2)));
		                    result.add(vectTemp);
                    	 }
                    	 break;
                }

            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.listActionStatus.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }


    /**
    * this method used to set document status depend on user action
    * and get result
	*/                                                       
    public Vector getDocumentStatusForUser(boolean updatable, String className, String formName, String attrForm, String hiddenFormName, String headerName, int currDocumentStatus, long docId, int docType, long appMappingId, long empId, boolean exist){
        Vector vectResult = new Vector(1,1);
	    String strComboStatus = "";

        //System.out.println("----------->updatable : "+updatable);
        //System.out.println("----------->currDocumentStatus : "+currDocumentStatus);

        // if "action" status is exist like : proceed, closed, cancelled
        if(exist){

	        //---> generate html of document status
	        if(updatable){

                try{
					I_Document i_document = (I_Document) Class.forName(className).newInstance();
		            currDocumentStatus = i_document.setDocumentStatus(docId,currDocumentStatus);
                }catch(Exception e){
                    System.out.println("------------------ err update process");
                }

		        // generate combobox of 'action' list
		        Vector vectProcessStatus = listActionStatus(appMappingId,currDocumentStatus);
		        if(vectProcessStatus!=null && vectProcessStatus.size()>0){
			        strComboStatus = strComboStatus + "<select name=\""+formName+"\" class=\"formElemen\" "+attrForm+">" +
		            				  				  "<option value=\"-1\">change status ...</option>";
		            int maxV = vectProcessStatus.size();
		            for(int i=0; i<maxV; i++){
		                Vector vectTemp = (Vector)vectProcessStatus.get(i);
		                strComboStatus = strComboStatus + "<option value=\""+String.valueOf(vectTemp.get(0))+"\">"+String.valueOf(vectTemp.get(1))+"</option>";
		            }
					strComboStatus = strComboStatus + "</select>";
		        }
	        }

        // if status isn't "action" status like : draft, to be approved, final or revised
		}else{
            //System.out.println("------------------------> MASUK ELSE");

	        //---> generate html of document status
	        if(updatable){
	            //System.out.println("------------------------> MASUK UPDATEABLE");
		        // trigger to change document status
		        try{
					I_Document i_document = (I_Document) Class.forName(className).newInstance();
                    // ngecek apakah approval di "checked"
			        if(PstDocAppDetail.isDocumentApproved(docId,appMappingId)){
				        int documentStatus = getStatusIndexAfter(appMappingId);
			            currDocumentStatus = i_document.setDocumentStatus(docId,documentStatus);
                    // ngecek apakah approval tidak di "checked"
		            }else{
                        // apakah current statusnya ---> REVISED
                        if(currDocumentStatus==I_DocStatus.DOCUMENT_STATUS_REVISED){
				            //System.out.println("------------------------> MASUK REVISED");
   		            		currDocumentStatus = i_document.setDocumentStatus(docId,currDocumentStatus);
		                    PstDocAppMain pstDocAppMain = new PstDocAppMain();
		                    DocAppMain docAppMain = new DocAppMain();
                            try{
			                   docAppMain = pstDocAppMain.fetchExc(PstDocAppMain.fetchDocAppMainOid(docId));
			                   docAppMain.setIsRevised(true);
			                   pstDocAppMain.updateExc(docAppMain);
                            }catch(Exception e){
                            }
                        // apakah current statusnya ---> REVISED
                        }else{
					        int documentStatus = getStatusIndexBefore(appMappingId);
	                        if(documentStatus==I_DocStatus.DOCUMENT_STATUS_DRAFT){
					            //System.out.println("------------------------> MASUK DRAFT");
	                            // dokumen sudah pernah di revisi
	                            if(PstDocAppMain.isRevised(docId)){
									currDocumentStatus = i_document.setDocumentStatus(docId,I_DocStatus.DOCUMENT_STATUS_REVISED);
	                            }else{
									currDocumentStatus = i_document.setDocumentStatus(docId,documentStatus);
	                            }
	                        }
                        }


                        // if uncheck approval with current status "to be approve" and docAppMain's revised is true
                        /*if(documentStatus==I_DocStatus.DOCUMENT_STATUS_DRAFT && PstDocAppMain.isRevised(docId)){
							currDocumentStatus = i_document.setDocumentStatus(docId,I_DocStatus.DOCUMENT_STATUS_REVISED);
                        }else{
		            		//currDocumentStatus = documentStatus==I_DocStatus.DOCUMENT_STATUS_DRAFT ? I_DocStatus.DOCUMENT_STATUS_DRAFT : currDocumentStatus;
   		            		currDocumentStatus = i_document.setDocumentStatus(docId,currDocumentStatus);
                            // if current status is "revised" then set docAppMain's revised to "true"
                        }*/
		            }
		        }catch(Exception e){
		            System.out.println("Err : "+ e.toString());
		        }

	        }else{
                // apakah current statusnya ---> REVISED
                if(currDocumentStatus==I_DocStatus.DOCUMENT_STATUS_REVISED){
                    try{
						I_Document i_document = (I_Document) Class.forName(className).newInstance();
				        //System.out.println("------------------------> MASUK REVISED");
	         			currDocumentStatus = i_document.setDocumentStatus(docId,currDocumentStatus);
	          		    PstDocAppMain pstDocAppMain = new PstDocAppMain();
	              		DocAppMain docAppMain = new DocAppMain();
	                    try{
			              docAppMain = pstDocAppMain.fetchExc(PstDocAppMain.fetchDocAppMainOid(docId));
			              docAppMain.setIsRevised(true);
			              pstDocAppMain.updateExc(docAppMain);
	                    }catch(Exception exc){
	                    }
                    }catch(Exception e){
                    }
                }
            }

	        // generate combobox of 'action' list
	        Vector vectProcessStatus = listActionStatus(appMappingId,currDocumentStatus);
	        if(vectProcessStatus!=null && vectProcessStatus.size()>0){
		        strComboStatus = strComboStatus + "<select name=\""+formName+"\" class=\"formElemen\" "+attrForm+">" +
	            				  				  "<option value=\"-1\">Change status ...</option>";
	            int maxV = vectProcessStatus.size();
	            for(int i=0; i<maxV; i++){
	                Vector vectTemp = (Vector)vectProcessStatus.get(i);
	                strComboStatus = strComboStatus + "<option value=\""+String.valueOf(vectTemp.get(0))+"\">"+String.valueOf(vectTemp.get(1))+"</option>";
	            }
				strComboStatus = strComboStatus + "</select>";
	        }

		}


        PstDocStatus pstDocStatus = new PstDocStatus();
        String statusName = pstDocStatus.getDocStatusName(docType,currDocumentStatus);
        String result = "<table width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">" +
                             "<tr>" +
                                "<td align=\"center\">" +
								    "<input type=\"hidden\" name=\""+hiddenFormName+"\" value=\""+(currDocumentStatus==I_DocStatus.DOCUMENT_STATUS_REVISED ? I_DocStatus.DOCUMENT_STATUS_DRAFT : currDocumentStatus)+"\">"+headerName+" : "+statusName +
							    "</td>" +
                             "</tr>";
						     if(strComboStatus!="" && strComboStatus.length()>0){
	                              result = result + "<tr>" +
    	                          "<td align=\"center\">"+strComboStatus+"</td>" +
        	                      "</tr>";
						     }
                        	 result = result + "</table>";
        vectResult.add(result);

        //---> generate html of document status
        vectResult.add(String.valueOf(currDocumentStatus));

        return vectResult;  
    }




    /* ---------------------- start implements I_DocStatus ----------------*/
    /**
    * this method used to get all status depend on document type
    * @return : vector of index and docStatus' name
    */
    public Vector getDocStatusFor(int docType){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
						 ", " + PstDocStatus.fieldNames[PstDocStatus.FLD_NAME] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                	     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_DOCTYPE] + " = " + docType +
                	     " ORDER BY " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE];
            //System.out.println("###################################PstDocStatus.getDocStatusFor.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector vectTemp = new Vector(1,1);
				vectTemp.add(String.valueOf(rs.getInt(1)));
                vectTemp.add(String.valueOf(rs.getString(2)));
                result.add(vectTemp);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getDocStatusFor.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * this method used to get name of document status specify by status index
    */
	public String getDocStatusName(int indexStatus){
        String result = "";
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT " + PstDocStatus.fieldNames[PstDocStatus.FLD_NAME] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                	     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + indexStatus;
            //System.out.println("###################################PstDocStatus.getDocStatusName.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = rs.getString(1);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getDocStatusName.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * this method used to get name of document status specify by doctype and status index
    */
	public String getDocStatusName(int docType, int indexStatus){
        String result = "";
        DBResultSet dbrs = null;
		try{
			String sql = "SELECT " + PstDocStatus.fieldNames[PstDocStatus.FLD_NAME] +
                		 " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                	     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_DOCTYPE] + " = " + docType +
                	     " AND " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + indexStatus;
            //System.out.println("###################################PstDocStatus.getDocStatusName.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = rs.getString(1);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getDocStatusName.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }
    /* ---------------------- start implements I_DocStatus ----------------*/

 /* ---------------------- start implements I_DocStatus ----------------*/
    /**
    * this method used to get all status on search qty lost opname
    * @return : vector of index and docStatus' name
    * by Mirahu
    * 20110805
    */
    public static Vector getDocStatusForQtyLostOpname(){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
	try{
	String sql = "SELECT DISTINCT " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
                     " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                     " OR " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                     " OR " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                     " ORDER BY " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE];
            //System.out.println("###################################PstDocStatus.getDocStatusFor.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                 Vector vectTemp = new Vector(1,1);
		vectTemp.add(String.valueOf(rs.getInt(1)));
                result.add(vectTemp);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getDocStatusFor.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }

    /**
    * this method used to get all status on search qty lost opname
    * @return : vector of index and docStatus' name
    * by Mirahu
    * 20110805
    */
    public static Vector getDocStatusForQtyLostOpnameNew(){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
	try{
	String sql = "SELECT DISTINCT " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
                     " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                     " ORDER BY " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE];
            //System.out.println("###################################PstDocStatus.getDocStatusFor.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                 Vector vectTemp = new Vector(1,1);
		vectTemp.add(String.valueOf(rs.getInt(1)));
                result.add(vectTemp);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getDocStatusFor.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }
    
     /**
    * this method used to get all status on search qty lost opname
    * @return : vector of index and docStatus' name
    * by Mirahu
    * 20110805
    */
    public static Vector getDocStatusForQtyReposting(){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
	try{
	String sql = "SELECT DISTINCT " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] +
                     " FROM " + PstDocStatus.TBL_WF_DOC_STATUS +
                     " WHERE " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                     " OR " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                     " ORDER BY " + PstDocStatus.fieldNames[PstDocStatus.FLD_TYPE];
            //System.out.println("###################################PstDocStatus.getDocStatusFor.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                 Vector vectTemp = new Vector(1,1);
		vectTemp.add(String.valueOf(rs.getInt(1)));
                result.add(vectTemp);
            }
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocStatus.getDocStatusFor.err : " + e.toString());
    	}finally{
        	DBResultSet.close(dbrs);
            return result;
    	}
    }
    

}
