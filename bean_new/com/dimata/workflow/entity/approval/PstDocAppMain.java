package com.dimata.workflow.entity.approval; 

/* package java */ 
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
import com.dimata.harisma.entity.employee.*;

import com.dimata.workflow.entity.search.*;
import com.dimata.workflow.session.approval.*;
import com.dimata.workflow.entity.status.*;

public class PstDocAppMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Approval, I_Workflow {

	public static final  String TBL_WF_DOC_APP_MAIN = "wf_doc_app_main";

	public static final  int FLD_DOCAPP_MAIN_OID = 0;
	public static final  int FLD_DOCUMENT_OID = 1;
	public static final  int FLD_DOCTYPE_TYPE = 2;
	public static final  int FLD_IS_REVISED = 3;

	public static final  String[] fieldNames = {
		"DOCAPP_MAIN_OID",
		"DOCUMENT_OID",
		"DOCTYPE_TYPE",
        "IS_REVISED"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_INT,
        TYPE_INT
	 }; 

	public PstDocAppMain(){
	}

	public PstDocAppMain(int i) throws DBException { 
		super(new PstDocAppMain()); 
	}

	public PstDocAppMain(String sOid) throws DBException { 
		super(new PstDocAppMain(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDocAppMain(long lOid) throws DBException { 
		super(new PstDocAppMain(0)); 
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
		return TBL_WF_DOC_APP_MAIN;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDocAppMain().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DocAppMain docappmain = fetchExc(ent.getOID()); 
		ent = (Entity)docappmain; 
		return docappmain.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DocAppMain) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DocAppMain) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DocAppMain fetchExc(long oid) throws DBException{ 
		try{ 
			DocAppMain docappmain = new DocAppMain();
			PstDocAppMain pstDocAppMain = new PstDocAppMain(oid); 
			docappmain.setOID(oid);

			docappmain.setDocumentOid(pstDocAppMain.getlong(FLD_DOCUMENT_OID));
			docappmain.setDoctypeType(pstDocAppMain.getInt(FLD_DOCTYPE_TYPE));
			docappmain.setIsRevised(pstDocAppMain.getboolean(FLD_IS_REVISED));

			return docappmain; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppMain(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DocAppMain docappmain) throws DBException{ 
		try{ 
			PstDocAppMain pstDocAppMain = new PstDocAppMain(0);

			pstDocAppMain.setLong(FLD_DOCUMENT_OID, docappmain.getDocumentOid());
			pstDocAppMain.setInt(FLD_DOCTYPE_TYPE, docappmain.getDoctypeType());
			pstDocAppMain.setboolean(FLD_IS_REVISED, docappmain.getIsRevised());

			pstDocAppMain.insert(); 
			docappmain.setOID(pstDocAppMain.getlong(FLD_DOCAPP_MAIN_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppMain(0),DBException.UNKNOWN); 
		}
		return docappmain.getOID();
	}

	public static long updateExc(DocAppMain docappmain) throws DBException{ 
		try{ 
			if(docappmain.getOID() != 0){ 
				PstDocAppMain pstDocAppMain = new PstDocAppMain(docappmain.getOID());

				pstDocAppMain.setLong(FLD_DOCUMENT_OID, docappmain.getDocumentOid());
				pstDocAppMain.setInt(FLD_DOCTYPE_TYPE, docappmain.getDoctypeType());
				pstDocAppMain.setboolean(FLD_IS_REVISED, docappmain.getIsRevised());

				pstDocAppMain.update(); 
				return docappmain.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppMain(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{
            PstDocAppDetail.deleteByDocAppMain(oid);
			PstDocAppMain pstDocAppMain = new PstDocAppMain(oid);
			pstDocAppMain.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppMain(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_WF_DOC_APP_MAIN; 
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
				DocAppMain docappmain = new DocAppMain();
				resultToObject(rs, docappmain);
				lists.add(docappmain);
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

	private static void resultToObject(ResultSet rs, DocAppMain docappmain){
		try{
			docappmain.setOID(rs.getLong(PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCAPP_MAIN_OID]));
			docappmain.setDocumentOid(rs.getLong(PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCUMENT_OID]));
			docappmain.setDoctypeType(rs.getInt(PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCTYPE_TYPE]));
			docappmain.setIsRevised(rs.getBoolean(PstDocAppMain.fieldNames[PstDocAppMain.FLD_IS_REVISED]));
		}catch(Exception e){ }
	}

	public static boolean checkOID(long docappMainOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_DOC_APP_MAIN + " WHERE " + 
						PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCAPP_MAIN_OID] + " = " + docappMainOid;

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
			String sql = "SELECT COUNT("+ PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCAPP_MAIN_OID] + ") FROM " + TBL_WF_DOC_APP_MAIN;
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
			  	   DocAppMain docappmain = (DocAppMain)list.get(ls);
				   if(oid == docappmain.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    /**
    * This method used to fetch DocAppMain OID
    */
    public static long fetchDocAppMainOid(long docOid){
        long result = 0;
        try{
            String whereClause = fieldNames[FLD_DOCUMENT_OID] + " = " + docOid;
            Vector vectDocAppMain = list(0,0,whereClause,"");
            if(vectDocAppMain!=null && vectDocAppMain.size()>0){
                DocAppMain docAppMain = (DocAppMain)vectDocAppMain.get(0);
                result = docAppMain.getOID();
            }
        }catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!PstDocAppMain.fetchDocAppMainOid.err : "+e.toString());
        }
        return result;
    }

    /**
    * This method used to insert DocAppMain
    * Triggered by "insert command" of main system.
    */
    public static long insertDocAppMain(long docOid, int docType){
        long result = 0;
        PstDocAppMain pstDocAppMain = new PstDocAppMain();
        DocAppMain docAppMain = new DocAppMain();
        docAppMain.setDocumentOid(docOid);
        docAppMain.setDoctypeType(docType);
        try{
	        result = pstDocAppMain.insertExc(docAppMain);
        }catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!PstDocAppMain.insertDocAppMain.err : "+e.toString());
        }
        return result;
    }

    /**
    * This method used to insert DocAppDetail
    * Triggered by "insert command" of main system.
    */
    public static void approveDocAppDetail(long docAppMainId, Vector vectAppStatus){
        if(vectAppStatus!=null && vectAppStatus.size()>0){
            for(int i=0; i<vectAppStatus.size(); i++){
                Vector vectTemp = (Vector)vectAppStatus.get(i);
                long detOid = Long.parseLong(String.valueOf(vectTemp.get(0)));
                long mapOid = Long.parseLong(String.valueOf(vectTemp.get(1)));
                int appStatus = Integer.parseInt(String.valueOf(vectTemp.get(2)));
                long empOid = Long.parseLong(String.valueOf(vectTemp.get(3)));

                //System.out.println("detOID : "+detOid);
                //System.out.println("mapOid : "+mapOid);
                //System.out.println("appStatus : "+appStatus);
                //System.out.println("empOid : "+empOid);


		        PstDocAppDetail pstDocAppDetail = new PstDocAppDetail();
		        DocAppDetail docAppDetail = new DocAppDetail();
		        docAppDetail.setOID(detOid);
		        docAppDetail.setDocappMainOid(docAppMainId);
		        docAppDetail.setAppMappingOid(mapOid);
		        docAppDetail.setEmployeeOid(empOid);
		        docAppDetail.setAppStatus(appStatus);
		        try{
                    // uncheck
                    if(docAppDetail.getAppStatus()==0){
	                    if(docAppDetail.getOID()!=0){
					        pstDocAppDetail.deleteExc(docAppDetail);
                            //System.out.println("------------ D E L E T E -----------");
	                    }
                    // checked
                    }else{
	                    if(docAppDetail.getOID()==0){
					        pstDocAppDetail.insertExc(docAppDetail);
                            //System.out.println("------------ I N S E R T -----------");
	                    }
                    }
		        }catch(Exception e){
		            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!PstDocAppMain.approveDocAppDetail.err : "+e.toString());
		        }

            }
        }
    }

    /**
    * This method used to delete DocAppMain and its descendant
    * Triggered by "delete command" of main system.
    */
    public static void deleteDocApproval(long docAppMainId){
        try{
	        PstDocAppDetail pstDocAppDetail = new PstDocAppDetail();
	        long result = deleteExc(docAppMainId);
        }catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!PstDocAppMain.deleteDocApproval.err : "+e.toString());
        }
    }

    /**
    * this method used to get approval default owned by specified document
    */
    public static Vector getApprovalDefault(int docType, long departmentId, long sectionId, long positionId){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
        boolean availableSection = sectionId!=0 ? true : false;
        try{
            String sql = "SELECT TYP." + PstAppType.fieldNames[PstAppType.FLD_NAME] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_TITLE] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DEPARTMENT_OID] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID] +
						 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_INDEX] +
						 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DOC_STATUS_OID_BEFORE] +
                         " FROM " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " INNER JOIN " + PstAppType.TBL_WF_APP_TYPE + " AS TYP " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APPTYPE_OID] +
                         " = TYP." + PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID] +
                         " WHERE MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DOCTYPE] + " = " + docType +
						 " GROUP BY MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APPTYPE_OID] +
                         ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_INDEX] +
            			 " ORDER BY MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_INDEX];
            //System.out.println("#############################PstDocAppMain.getApprovalDefault.sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector vectTemp = new Vector(1,1);

                AppType appType = new AppType();
                appType.setName(rs.getString(1));
                vectTemp.add(appType);

                AppMapping appMapping = new AppMapping();
                appMapping.setOID(rs.getLong(2));
                appMapping.setAppTitle(rs.getString(3));
                appMapping.setDepartmentOid(rs.getLong(4));
                appMapping.setSectionId(rs.getLong(5));
                appMapping.setPositionOid(rs.getLong(6));
                appMapping.setAppIndex(rs.getInt(7));
                appMapping.setDocStatusOidBefore(rs.getLong(8));
                vectTemp.add(appMapping);

                boolean approvable = false;
                if(availableSection){
                    approvable = appMapping.getDepartmentOid()==departmentId && appMapping.getSectionId()==sectionId && appMapping.getPositionOid()==positionId;
                }else{
                    approvable = appMapping.getDepartmentOid()==departmentId && appMapping.getPositionOid()==positionId;
                }

                if(approvable){
                    vectTemp.add(String.valueOf(I_Approval.STATUS_APPROVABLE));
                }else{
                    vectTemp.add(String.valueOf(I_Approval.STATUS_READONLY));
                }

                Vector listEmployee = PstEmployee.listEmployee(appMapping.getDepartmentOid(),(availableSection==true ? appMapping.getSectionId() : 0),appMapping.getPositionOid());
                vectTemp.add(listEmployee);

                result.add(vectTemp);
            }
        }catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!PstDocAppMain.getApprovalDefault.err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        	return result;
        }
    }

    /**
    * this method used to get approval default owned by specified document
    */
    public static Vector getApprovalDefault(int docType){
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT TYP." + PstAppType.fieldNames[PstAppType.FLD_NAME] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_TITLE] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DEPARTMENT_OID] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID] +
                         " FROM " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " INNER JOIN " + PstAppType.TBL_WF_APP_TYPE + " AS TYP " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APPTYPE_OID] +
                         " = TYP." + PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID] +
                         " WHERE MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DOCTYPE] + " = " + docType +
						 " GROUP BY MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APPTYPE_OID] +
                         ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_INDEX] +
            			 " ORDER BY MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_INDEX];
            //System.out.println("#############################PstDocAppMain.getApprovalDefault.sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector vectTemp = new Vector(1,1);

                AppType appType = new AppType();
                appType.setName(rs.getString(1));
                vectTemp.add(appType);

                AppMapping appMapping = new AppMapping();
                appMapping.setOID(rs.getLong(2));
                appMapping.setAppTitle(rs.getString(3));
                appMapping.setDepartmentOid(rs.getLong(4));
                appMapping.setSectionId(rs.getLong(5));
                appMapping.setPositionOid(rs.getLong(6));
                vectTemp.add(appMapping);

                result.add(vectTemp);
            }
        }catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!PstDocAppMain.getApprovalDefault.err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        	return result;
        }
    }

    /**
    * this method used to get Approval specified by documentOID and documentType
    * @return ---> vector of appropriate approval
	* 	-appTypeName
	* 	-appMappingId
	* 	-appMappingTitle
	* 	-docAppDetailStatus
	* 	-employeeId
	* 	-employeeName
	* 	-docAppDetailId
	* 	-accessStatus
    */
    public Vector getApproval(int iCommand, long docOid, int docType, long deptId, long secId, long positionId, long empId, Vector vectAppStatus, int ownIndex, int documentStatus){
        Vector result = new Vector(1,1);

		// transaction on DocAppMain and DocAppDetail
        long mainId = fetchDocAppMainOid(docOid);
        switch(iCommand){
	        case Command.SAVE    :
                 insertDocAppMain(docOid,docType);
            	 break;

	        case Command.APPROVE :
            	 approveDocAppDetail(mainId,vectAppStatus);
            	 break;

	        case Command.DELETE  :
            	 deleteDocApproval(mainId);
            	 break;
        }

        // if document status is revised ---> delete all approval detail
        if(documentStatus==I_DocStatus.DOCUMENT_STATUS_REVISED){
			PstDocAppDetail.deleteByDocAppMain(mainId);
        }

		// get vector of current approval based on docOid and docType
        String whClsCurrent = PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_MAIN_OID] + " = " + mainId;
        String ordClsCurrent = PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_APP_STATUS];
        Vector vectDocAppDetail = PstDocAppDetail.list(0,0,whClsCurrent,ordClsCurrent);

		// get vector of default approval mapping based on docType
        Vector vectAppDefault = getApprovalDefault(docType,deptId,secId,positionId);

        boolean validApprove = true;
        if(vectAppDefault!=null && vectAppDefault.size()>0){
            for(int i=0; i<vectAppDefault.size(); i++){
                boolean indexApprove = false;
                Vector vectTemp = (Vector)vectAppDefault.get(i);

                AppType appType = (AppType)vectTemp.get(0);
                AppMapping appMapping = (AppMapping)vectTemp.get(1);
	            int appAccess = Integer.parseInt(String.valueOf(vectTemp.get(2)));

                if(appMapping.getAppIndex()==1){
                    indexApprove = true;
                }

	            Vector vectResult = new Vector(1,1);
	            vectResult.add(appType.getName());
	            vectResult.add(String.valueOf(appMapping.getOID()));
	            vectResult.add(appMapping.getAppTitle());

                // vectDocAppDetail not null
	            if(vectDocAppDetail!=null && vectDocAppDetail.size()>0){
                   //System.out.println("-----------------------------------------------------------vectDocAppDetail not null");
                   boolean empAvailable = false;
	               for(int j=0; j<vectDocAppDetail.size(); j++){
	                  DocAppDetail docAppDetail = (DocAppDetail)vectDocAppDetail.get(j);
				 	  if(appMapping.getOID()==docAppDetail.getAppMappingOid() && docAppDetail.getAppStatus()==I_Approval.STATUS_APPROVE){
                    	 //System.out.println("------------------------- Employee Available");
                    	 empAvailable = true;
                    	 indexApprove = true;
	                     vectResult.add(String.valueOf(docAppDetail.getAppStatus()));
	                     vectResult.add(String.valueOf(docAppDetail.getEmployeeOid()));
	                     vectResult.add(PstEmployee.getEmployeeName(docAppDetail.getEmployeeOid()));
                         if(appMapping.getAppIndex()==ownIndex){
                         	vectResult.add(String.valueOf(docAppDetail.getOID()));
                         }else{
                         	vectResult.add(String.valueOf("0"));
                         }
	                     break;
	                  }
	               }
                   if(!empAvailable){
	                   vectResult.add(String.valueOf(I_Approval.STATUS_NOT_APPROVE));
	                   if(appAccess==I_Approval.STATUS_APPROVABLE){
                    	  //System.out.println("------------------------- Employee not Available");
		                  Vector listEmployee = (Vector)vectTemp.get(3);
		                  if(listEmployee!=null && listEmployee.size()>0){
		                    for(int k=0; k<listEmployee.size(); k++){

	                            /*int intDocStatus = I_DocStatus.DOCUMENT_STATUS_DRAFT;
	                            try{
	                            	PstDocStatus pstDocStatus = new PstDocStatus();
	                                DocStatus docStatus = new DocStatus();
									docStatus = pstDocStatus.fetchExc(appMapping.getDocStatusOidBefore());
	                                intDocStatus = docStatus.getType();
	                            }catch(Exception e){
	                                System.out.println("Err : "+e.toString());
	                            }
	
	                            if(documentStatus==intDocStatus){
		                        	indexApprove = true;
	                            }*/

	                        	indexApprove = true;
		                        Vector emp = (Vector)listEmployee.get(k);
	                            if(Long.parseLong(String.valueOf(emp.get(0)))==empId){
		                            vectResult.add(String.valueOf(emp.get(0)));
		                            vectResult.add(String.valueOf(emp.get(1)));
			                        vectResult.add(String.valueOf("0"));
		                            break;
                                }
		                    }
		                  }
	                   }else{
                    	  indexApprove = false;
		                  vectResult.add("0");
		                  vectResult.add("-");
	                      vectResult.add(String.valueOf("0"));
	                   }
                   }

                // vectDocAppDetail null
	            }else{
                   //System.out.println("-----------------------------------------------------------vectDocAppDetail null");
                   vectResult.add(String.valueOf(I_Approval.STATUS_NOT_APPROVE));
                   if(appAccess==I_Approval.STATUS_APPROVABLE){
	                  Vector listEmployee = (Vector)vectTemp.get(3);
	                  if(listEmployee!=null && listEmployee.size()>0){
                   	    //System.out.println("------------------------- Employee Available");
	                    for(int k=0; k<listEmployee.size(); k++){

                            /*int intDocStatus = I_DocStatus.DOCUMENT_STATUS_DRAFT;
                            try{
                            	PstDocStatus pstDocStatus = new PstDocStatus();
                                DocStatus docStatus = new DocStatus();
								docStatus = pstDocStatus.fetchExc(appMapping.getDocStatusOidBefore());
                                intDocStatus = docStatus.getType();
                            }catch(Exception e){
                                System.out.println("Err : "+e.toString());
                            }

                            if(documentStatus==intDocStatus){
	                        	indexApprove = true;
                            }*/

                        	indexApprove = true;
	                        Vector emp = (Vector)listEmployee.get(k);
                            if(Long.parseLong(String.valueOf(emp.get(0)))==empId){
	                            vectResult.add(String.valueOf(emp.get(0)));
	                            vectResult.add(String.valueOf(emp.get(1)));
			                    vectResult.add(String.valueOf("0"));
                            	break;
                            }
	                    }
	                  }
                   }else{
                      indexApprove = false;
	                  vectResult.add("0");
	                  vectResult.add("-");
	                  vectResult.add(String.valueOf("0"));
                   }
	            }

                // untuk menyimpan akumulasi approval dari index sebelumnya
                validApprove = validApprove && indexApprove;

                // untuk mendapatkan nilai approval dari index berikutnya
                boolean approveByNextIndex = false;
                if(i<vectAppDefault.size()-1){
                    approveByNextIndex = approveByNextIndex((Vector)vectAppDefault.get(i+1),vectDocAppDetail);
                }

                // approvable jika hak aksesnya 'approvable' && akumulasi approval sebelumnya adalah 'true' && approval berikutnya 'false'
                if(appAccess==I_Approval.STATUS_APPROVABLE && validApprove && !approveByNextIndex){
		            vectResult.add(String.valueOf(I_Approval.STATUS_APPROVABLE));
                }else{
					vectResult.add(String.valueOf(I_Approval.STATUS_READONLY));
                }

                result.add(vectResult);
            }
        }
        return result;
    }


    /**
    * this method used to check if specify document ever have 'revised'
    */
	public static boolean isRevised(long docId){
        boolean result = false;
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT " + PstDocAppMain.fieldNames[PstDocAppMain.FLD_IS_REVISED] +
                		 " FROM " + PstDocAppMain.TBL_WF_DOC_APP_MAIN +
                         " WHERE " + PstDocAppMain.fieldNames[PstDocAppMain.FLD_DOCUMENT_OID] +
                         " = " + docId;
            //System.out.println("###########################PstDocAppMain.isRevised.sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = rs.getBoolean(1);
            }
        }catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!PstDocAppMain.isRevised.err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }





    /*--------------------------- start implements I_Approval ------------------------*/
    /**
    *
    */
    public Vector listDefaultApproval(int docType){
        Vector result = new Vector(1,1);
        Vector vectAppDefault = this.getApprovalDefault(docType,0,0,0);
        if(vectAppDefault!=null && vectAppDefault.size()>0){
            for(int i=0; i<vectAppDefault.size(); i++){
				Vector vectTemp = (Vector)vectAppDefault.get(i);
                AppMapping appMapping = (AppMapping)vectTemp.get(1);
                result.add(String.valueOf(appMapping.getOID()));
            }
        }
        return result;
    }

    /**
    * this method used to check index of user in list of approval
    * if return is 0 ---> user have not approval access
    */
    public int getUserApprovalIndex(int sysName, int docType, long departmentId, long sectionId, long positionId){
        int result = 0;

        SrcAppMapping srcappmapping = new SrcAppMapping();
		srcappmapping.setSystemType(sysName);
		srcappmapping.setAppTypeOid(SessAppMapping.FRM_FIELD_ALL);
		srcappmapping.setDocTypeType(docType);
		srcappmapping.setDepartmentOid(departmentId);
		srcappmapping.setSectionId(sectionId);
		srcappmapping.setPositionOid(positionId);

		Vector vectApproval = SessAppMapping.searchAppMapping(srcappmapping,0,0);
		if(vectApproval!=null && vectApproval.size()>0){
			for(int i=0; i<vectApproval.size(); i++){
				Vector vectTemp = (Vector)vectApproval.get(i);
				AppMapping appMapping = (AppMapping)vectTemp.get(0);
                result = appMapping.getAppIndex();
                break;
			}
        }
        return result;
    }

    /**
    * this method used to check index of user in list of approval
    * if return is 0 ---> user have not approval access
    */
    public int getUserApprovalIndex(String className, long docOid, int sysName, int docType, long departmentId, long sectionId, long positionId){
        int result = 0;

        // get status of current document specified docOid
        int currDocStatus = 0;
        try{
            I_Document i_document = (I_Document) Class.forName(className).newInstance();
			currDocStatus = i_document.getDocumentStatus(docOid);
        }catch(Exception e){
            System.out.println("PstDocAppMapping.getUserApprovalIndex() err : "+e.toString());
        }

        SrcAppMapping srcappmapping = new SrcAppMapping();
		srcappmapping.setSystemType(sysName);
		srcappmapping.setAppTypeOid(SessAppMapping.FRM_FIELD_ALL);
		srcappmapping.setDocTypeType(docType);
		srcappmapping.setDepartmentOid(departmentId);
		srcappmapping.setSectionId(sectionId);
		srcappmapping.setPositionOid(positionId);

		Vector vectApproval = SessAppMapping.searchAppMapping(srcappmapping,0,0);
		if(vectApproval!=null && vectApproval.size()>0){
			for(int i=0; i<vectApproval.size(); i++){
				Vector vectTemp = (Vector)vectApproval.get(i);
				AppMapping appMapping = (AppMapping)vectTemp.get(0);
                result = appMapping.getAppIndex();
                break;
			}
        }
        return result;
    }

    /**
    * this method used to get approval Oid
    */
    public long getUserApprovalId(int sysName, int docType, long departmentId, long sectionId, long positionId, int ownIndex){
        long result = 0;

        SrcAppMapping srcappmapping = new SrcAppMapping();
		srcappmapping.setSystemType(sysName);
		srcappmapping.setAppTypeOid(SessAppMapping.FRM_FIELD_ALL);
		srcappmapping.setDocTypeType(docType);
		srcappmapping.setDepartmentOid(departmentId);
		srcappmapping.setSectionId(sectionId);
		srcappmapping.setPositionOid(positionId);

		Vector vectApproval = SessAppMapping.searchAppMapping(srcappmapping,0,0);
		if(vectApproval!=null && vectApproval.size()>0){
			for(int i=0; i<vectApproval.size(); i++){
				Vector vectTemp = (Vector)vectApproval.get(i);
				AppMapping appMapping = (AppMapping)vectTemp.get(0);
                if(appMapping.getAppIndex()==ownIndex){
                	result = appMapping.getOID();
                    break;
                }
			}
        }

        return result;
    }

    /**
    * this method used to check approval value for next Index
    * return 'true' ---> if document have approve by next index
    * return 'false' ---> otherwise
    */
    public static boolean approveByNextIndex(Vector vectDefaultNextIndex, Vector vectAppDetail){
        boolean result = false;
        if(vectDefaultNextIndex!=null){
            AppMapping appMapping = (AppMapping)vectDefaultNextIndex.get(1);
			if(vectAppDetail!=null && vectAppDetail.size()>0){
               for(int j=0; j<vectAppDetail.size(); j++){
                  DocAppDetail docAppDetail = (DocAppDetail)vectAppDetail.get(j);
			 	  if(appMapping.getOID()==docAppDetail.getAppMappingOid()){
                	 result = true;
                     break;
                  }
               }
            }
        }
        return result;
    }

    /**
    * this method used to get approval and document status
    * return : Vector that content of
    * 	- 'html of approval'
    *   - 'html of document status'
    *   - 'current document status'
    */
    public Vector getStatusApproval(int iCommand, long docOid, int docType, int printOrientation, long deptId, long secId, long positionId,
        long empId, Vector vectAppStatus, int ownIndex, String className, long appMappingId, String formName,String attrForm,
        String hiddenFormName, String headerName, int docStatus){

        PstDocStatus pstDS = new PstDocStatus();
        //System.out.println("######################################### DOCUMENT STATUS : "+pstDS.getDocStatusName(docType,docStatus));

        Vector vectResult = new Vector(1,1);
        boolean updatable = false;

        /**
        * list of action command : proceed,closed,canceled
        * untuk 'revised' diperlakukan khusus karena sejajar dengan 'draft'
        */
        Vector listAction = PstAppMapping.listAction(appMappingId);
        boolean actionCommandExist = false;
        if(listAction!=null && listAction.size()>0){
            for(int i=0; i<listAction.size(); i++){
                if(docStatus==Integer.parseInt(String.valueOf(listAction.get(i))) && docStatus!=I_DocStatus.DOCUMENT_STATUS_REVISED){
                    actionCommandExist = true;
                }
            }
        }

        //System.out.println("---------------> actionCommandExist : "+actionCommandExist);


        /**
        * start generate 'html of approval'
        */
        PstDocAppMain pstDocAppMain = new PstDocAppMain();
        Vector vectApproval = pstDocAppMain.getApproval(iCommand,docOid,docType,deptId,secId,positionId,empId,vectAppStatus,ownIndex,docStatus);
        String result = "";
        if(printOrientation == I_Approval.PRINT_HORIZONTAL){ //---> if print layout is horizontal
	           result = "<table width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">" +
                        "<tr>";
                        //PstDocAppMain pstDocAppMain = new PstDocAppMain();
                        //Vector vectApproval = pstDocAppMain.getApproval(iCommand,docOid,docType,deptId,secId,positionId,empId,vectAppStatus,ownIndex,docStatus);
					    if(vectApproval!=null && vectApproval.size()>0){
					  	    for(int i=0; i<vectApproval.size(); i++){
								Vector vectTemp = (Vector)vectApproval.get(i);
								String appName  = String.valueOf(vectTemp.get(0))=="" ? "&nbsp;" : String.valueOf(vectTemp.get(0))+",";
								long mapOid     = Long.parseLong(String.valueOf(vectTemp.get(1)));
								String mapTitle = String.valueOf(vectTemp.get(2))=="" ? "&nbsp;" : String.valueOf(vectTemp.get(2));
								int appStatus   = Integer.parseInt(String.valueOf(vectTemp.get(3)));
								long empOid     = Long.parseLong(String.valueOf(vectTemp.get(4)));
								String empName  = String.valueOf(vectTemp.get(5))=="" ? "&nbsp;" : String.valueOf(vectTemp.get(5));
								long detailOid  = Long.parseLong(String.valueOf(vectTemp.get(6)));
								int appAccess   = Integer.parseInt(String.valueOf(vectTemp.get(7)));
								String strDisabled = "";
                                if(actionCommandExist || appAccess==I_Approval.STATUS_READONLY){
									strDisabled = "disabled";
                                }

                                if(mapOid==appMappingId && appAccess==I_Approval.STATUS_APPROVABLE){
                                    updatable = true;
                                }

                        		result = result + "<td width=\""+(100/vectApproval.size())+"%\" valign=\"top\">" +
										 "<table width=\"100%\">" +
									 	 "<tr><td align=\"center\">"+appName+"</td></tr>" +
									 	 "<tr><td align=\"center\"><input type=\"hidden\" name=\"map_"+mapOid+"\" value=\""+mapOid+"\">"+mapTitle+"</td></tr>" +
									 	 "<tr><td align=\"center\"><input type=\"hidden\" name=\"det_"+mapOid+"\" value=\""+detailOid+"\"><input type=\"hidden\" name=\"cb_"+mapOid+"\" value=\""+((appStatus==I_Approval.STATUS_APPROVE && updatable) ? "1" : "0")+"\">"+
                                         "<input type=\"checkbox\" name=\"cmbb_"+mapOid+"\" value=\"1\""+((appStatus==I_Approval.STATUS_NOT_APPROVE)? " " : " checked ")+strDisabled+" onClick=\"javascript:checkBoxAction('"+mapOid+"')\"></td></tr>" +
									 	 "<tr><td align=\"center\"><input type=\"hidden\" name=\"emp_"+mapOid+"\" value=\""+empOid+"\">"+empName+"</td></tr>" +
									 	 "</table>" +
									 	 "</td>";
						    }
					    }
                        result = result + "</tr>" +
                        				  "</table>";

        }else{ //---> if print layout is vertical
			   result = "<table width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";
                        //PstDocAppMain pstDocAppMain = new PstDocAppMain();
                        //Vector vectApproval = pstDocAppMain.getApproval(iCommand,docOid,docType,deptId,secId,positionId,empId,vectAppStatus,ownIndex,docStatus);
					    if(vectApproval!=null && vectApproval.size()>0){
					  	    for(int i=0; i<vectApproval.size(); i++){
								Vector vectTemp = (Vector)vectApproval.get(i);
								String appName  = String.valueOf(vectTemp.get(0))=="" ? "&nbsp;" : String.valueOf(vectTemp.get(0))+",";
								long mapOid     = Long.parseLong(String.valueOf(vectTemp.get(1)));
								String mapTitle = String.valueOf(vectTemp.get(2))=="" ? "&nbsp;" : String.valueOf(vectTemp.get(2));
								int appStatus   = Integer.parseInt(String.valueOf(vectTemp.get(3)));
								long empOid     = Long.parseLong(String.valueOf(vectTemp.get(4)));
								String empName  = String.valueOf(vectTemp.get(5))=="" ? "&nbsp;" : String.valueOf(vectTemp.get(5));
								long detailOid  = Long.parseLong(String.valueOf(vectTemp.get(6)));
								int appAccess   = Integer.parseInt(String.valueOf(vectTemp.get(7)));
								String strDisabled = "";
                                if(actionCommandExist || appAccess==I_Approval.STATUS_READONLY){
									strDisabled = "disabled";
                                }

                                if(mapOid==appMappingId && appAccess==I_Approval.STATUS_APPROVABLE){
                                    updatable = true;
                                }

                                result = result + "<tr>" +
						                          "<td>" +
						                            "<table width=\"100%\">" +
						                              "<tr>" +
						                                "<td align=\"center\">"+appName+"</td>"+
						                              "</tr>" +
						                              "<tr>" +
						                                "<td align=\"center\">" +
						                                  "<input type=\"hidden\" name=\"map_"+mapOid+"\" value=\""+mapOid+"\">"+mapTitle +
                                                        "</td>" +
						                              "</tr>" +
						                              "<tr>" +
						                                "<td align=\"center\">" +
						                                  "<input type=\"hidden\" name=\"det_"+mapOid+"\" value=\""+detailOid+"\"><input type=\"hidden\" name=\"cb_"+mapOid+"\" value=\""+((appStatus==I_Approval.STATUS_APPROVE && updatable) ? "1" : "0")+"\">"+
						                                  "<input type=\"checkbox\" name=\"cmbb_"+mapOid+"\" value=\"1\""+((appStatus==I_Approval.STATUS_NOT_APPROVE)? " " : " checked ")+strDisabled+" onClick=\"javascript:checkBoxAction('"+mapOid+"')\">"+
						                                "</td>" +
						                              "</tr>" +
						                              "<tr>" +
						                                "<td align=\"center\">" +
                                                          "<input type=\"hidden\" name=\"emp_"+mapOid+"\" value=\""+empOid+"\">"+empName +
						                                "</td>" +
						                              "</tr>" +
						                            "</table>" +
						                          "</td>" +
						                        "</tr>";

								if(i<vectApproval.size()-1){ //---> untuk spasi antar approval
				                    result = result + "<tr>" +
				                      					"<td>&nbsp;</td>" +
				                    				  "</tr>";
                                }

                      		}
                        }
                    	result = result + "</table>";
        }
        vectResult.add(result);
        /**
        * end generate 'html of approval'
        */


        /**
        * start generate 'html of document status'
        */
        PstDocStatus pstDocStatus = new PstDocStatus();
        Vector vectStatus = pstDocStatus.getDocumentStatusForUser(updatable,className,formName,attrForm,hiddenFormName,headerName,docStatus,docOid,docType,appMappingId,empId,actionCommandExist);
        String strDocStatus = String.valueOf(vectStatus.get(0));
        vectResult.add(strDocStatus);
        /**
        * end generate 'html of document status'
        */

        /**
        * start add 'current document status'
        */
        String currDocIndex = String.valueOf(vectStatus.get(1));
        vectResult.add(currDocIndex);
        /**
        * end add 'current document status'
        */

        return vectResult;
    }

    /**
    * this method used to create vector of Approval for specified document
    */
    public Vector getVectorApproval(long documentId, int docType){
        Vector result = new Vector(1,1);

		// get vector of default approval mapping based on docType
        Vector vectAppDefault = getApprovalDefault(docType);

		// get vector of current approval based on docOid and docType
        long mainId = fetchDocAppMainOid(documentId);
        String whClsCurrent = PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_DOCAPP_MAIN_OID] + " = " + mainId;
        String ordClsCurrent = PstDocAppDetail.fieldNames[PstDocAppDetail.FLD_APP_STATUS];
        Vector vectDocAppDetail = PstDocAppDetail.list(0,0,whClsCurrent,ordClsCurrent);

        try{
        if(vectAppDefault!=null && vectAppDefault.size()>0){
            int maxDefaultSize = vectAppDefault.size();
            for(int i=0; i<maxDefaultSize; i++){
                Vector vectTemp = (Vector)vectAppDefault.get(i);
                AppType appType = (AppType)vectTemp.get(0);
                AppMapping appMapping = (AppMapping)vectTemp.get(1);


	            Vector vectResult = new Vector(1,1);
	            vectResult.add(appType.getName());
	            vectResult.add(appMapping.getAppTitle());

                boolean empAvailable = false;
                int maxDetailSize = 0;
	            if(vectDocAppDetail!=null && vectDocAppDetail.size()>0){
                    maxDetailSize = vectDocAppDetail.size();
                    for(int j=0; j<maxDetailSize; j++){
                        DocAppDetail docAppDetail = (DocAppDetail)vectDocAppDetail.get(j);
                        if(appMapping.getOID()==docAppDetail.getAppMappingOid()){
                            vectResult.add(PstEmployee.getEmployeeName(docAppDetail.getEmployeeOid()));
                            empAvailable = true;
                            break;
                        }
                    }
                }
                if(!empAvailable || maxDetailSize==0){
                    long deptId = appMapping.getDepartmentOid();
                    long sectId = appMapping.getSectionId();
                    long postId = appMapping.getPositionOid();
                    Vector listEmployee = PstEmployee.listEmployee(deptId,sectId,postId);
                    //System.out.println("listEmployee.size() : "+listEmployee.size());
                    if(listEmployee!=null && listEmployee.size()==1){
                        Vector vt = (Vector)listEmployee.get(0);
						vectResult.add(String.valueOf(vt.get(1)));
                    }else{
                    	vectResult.add("");
                    }
                }


                result.add(vectResult);
            }
        }
        }catch(Exception e){
            System.out.println("Exception : "+e.toString());
        }

        return result;
    }
    /*--------------------------- end implements I_Approval ------------------------*/


    /*--------------------------- end implements I_Workflow ------------------------*/
    public long generateNewApproval(long documentId, int docType){
     	long result = insertDocAppMain(documentId,docType);
        if(result!=0){
            return documentId;
        }
        return result;
    }
    /*--------------------------- end implements I_Workflow ------------------------*/


    public static void main(String args[]){
		PstDocAppMain pstDocAppMain = new PstDocAppMain();
        Vector vectApproval = pstDocAppMain.getVectorApproval(504404214635018262L,196611);
		if(vectApproval!=null && vectApproval.size()>0){
    		for(int i=0; i<vectApproval.size(); i++){
        		Vector vectTemp = (Vector)vectApproval.get(i);
                String approvalName = String.valueOf(vectTemp.get(0));
                String approvalTitle = String.valueOf(vectTemp.get(1));
                String approvalPerson = String.valueOf(vectTemp.get(2));
	        	//System.out.println("approvalName : "+approvalName);
	        	//System.out.println("approvalTitle : "+approvalTitle);
	        	//System.out.println("approvalPerson : "+approvalPerson);
	        	//System.out.println("");
    		}
        }
    }
}
