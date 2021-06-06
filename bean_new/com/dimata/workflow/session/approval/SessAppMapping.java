/*
 * Session Name  	:  SessAppDocMapping.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/
package com.dimata.workflow.session.approval;

import java.util.*;
import java.sql.*;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.workflow.entity.approval.*;
import com.dimata.workflow.entity.search.*;
import com.dimata.harisma.entity.masterdata.*;

public class SessAppMapping {
	public static final String SESS_SRC_APPMAPPING = "SESSION_SRC_APPMAPPING";
	public static final int FRM_FIELD_ALL = -1;

    public static final int SORT_BY_APPTITLE	= 0;
    public static final int SORT_BY_APPTYPE = 1;
    public static final int SORT_BY_DOCTYPE = 2;
    public static final int SORT_BY_DEPARMENT = 3;
    public static final int SORT_BY_SECTION = 4;
    public static final int SORT_BY_POSITION = 5;

    public static String[] sortFieldNames = {
        "Approval Title",
        "Approval Type",
        "Document Type",
        "Department",
        "Section",
        "Position"
    };

    /**
    * this method used to list all AppMapping data
    */
    public static Vector searchAppMapping(SrcAppMapping srcappmapping, int start, int recordToGet){
     DBResultSet dbrs = null;
     Vector result = new Vector(1,1);
     if(srcappmapping==null){
		srcappmapping = new SrcAppMapping();
     }

     boolean usingSection = srcappmapping.getSectionId()!=0 ? true : false;
     try{
			String sql = "SELECT MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_TITLE] +
                		 ", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_INDEX] +
                		 ", APP." + PstAppType.fieldNames[PstAppType.FLD_NAME] +
                		 ", DOC." + PstDocType.fieldNames[PstDocType.FLD_NAME] +
                		 ", DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT];
                         if(usingSection){
               		 	 	sql = sql + ", SEC." + PstSection.fieldNames[PstSection.FLD_SECTION];
            			 }
                		 sql = sql + ", POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION] +
 		                 " FROM " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " LEFT JOIN " + PstDocType.TBL_WF_DOC_TYPE + " AS DOC " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DOCTYPE] +
                         " = DOC." + PstDocType.fieldNames[PstDocType.FLD_TYPE] +
                         " LEFT JOIN " + PstAppType.TBL_WF_APP_TYPE + " AS APP " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APPTYPE_OID] +
                         " = APP." + PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID] +
                         " LEFT JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DEPARTMENT_OID] +
                         " = DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID];
                         if(usingSection){
                         sql = sql + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " AS SEC " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID] +
                         " = SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID];
                		 }
                         sql = sql + " LEFT JOIN " + PstPosition.TBL_HR_POSITION + " AS POS " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID] +
                         " = POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];

	        String systypecondition = "((DOC."+PstDocType.fieldNames[PstDocType.FLD_TYPE]+" & "+PstDocType.STR_FILTER_SYSTEM+")='"+
                                       ((srcappmapping.getSystemType() << PstDocType.SHIFT_SYSTEM) & PstDocType.FILTER_SYSTEM)+"')";

	        String apptitlecondition = "";
	        if(srcappmapping.getAppTitle()!="" && srcappmapping.getAppTitle().length()>0){
	             apptitlecondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_APP_TITLE]+" LIKE '%"+srcappmapping.getAppTitle()+"%') ";
	        }

            String apptypecondition = "";
	        if(srcappmapping.getAppTypeOid()!=FRM_FIELD_ALL){
	             apptypecondition = "(APP."+PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID]+" = "+srcappmapping.getAppTypeOid()+") ";
	        }

            String doctypecondition = "";
	        if(srcappmapping.getDocTypeType()!=FRM_FIELD_ALL){
	             doctypecondition = "(DOC."+PstDocType.fieldNames[PstDocType.FLD_TYPE]+" = "+srcappmapping.getDocTypeType()+") ";
	        }

            String departmentcondition = "";
	        if(srcappmapping.getDepartmentOid()!=FRM_FIELD_ALL){
	             departmentcondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_DEPARTMENT_OID]+" = "+srcappmapping.getDepartmentOid()+") ";
	        }

            String sectioncondition = "";
	        if(srcappmapping.getSectionId()!=FRM_FIELD_ALL){
	             sectioncondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID]+" = "+srcappmapping.getSectionId()+") ";
	        }

            String positioncondition = "";
	        if(srcappmapping.getPositionOid()!=FRM_FIELD_ALL){
	             positioncondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID]+" = "+srcappmapping.getPositionOid()+") ";
	        }

            String ordercondition = " ORDER BY DOC." + PstDocType.fieldNames[PstDocType.FLD_TYPE] +
                				 	", MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_INDEX];

	        /*String ordercondition = "";
            switch(srcappmapping.getSortBy()){
	            case SORT_BY_APPTITLE :
                     ordercondition = " ORDER BY "+ prevorderby +" MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_TITLE];
                	 break;
	            case SORT_BY_APPTYPE :
                     ordercondition = " ORDER BY "+ prevorderby +" APP." + PstAppType.fieldNames[PstAppType.FLD_NAME];
                	 break;
	            case SORT_BY_DOCTYPE :
                     ordercondition = " ORDER BY "+ prevorderby +" DOC." + PstDocType.fieldNames[PstDocType.FLD_TYPE];
                	 break;
	            case SORT_BY_DEPARMENT :
                     ordercondition = " ORDER BY "+ prevorderby +" DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT];
                	 break;
	            case SORT_BY_SECTION :
                     ordercondition = " ORDER BY "+ prevorderby +" SEC." + PstSection.fieldNames[PstSection.FLD_SECTION];
                	 break;
	            case SORT_BY_POSITION :
                     ordercondition = " ORDER BY "+ prevorderby +" POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION];
                	 break;
            }*/

	        String allCondition = "";
	        if((systypecondition!=null)&&(systypecondition.length()>0)){
	             allCondition = systypecondition;
	        }

            if((apptitlecondition!=null)&&(apptitlecondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ apptitlecondition;
	            }
	            else{
	                allCondition = apptitlecondition;
	            }
            }

            if((apptypecondition!=null)&&(apptypecondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ apptypecondition;
	            }
	            else{
	                allCondition = apptypecondition;
	            }
            }

            if((doctypecondition!=null)&&(doctypecondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ doctypecondition;
	            }
	            else{
	                allCondition = doctypecondition;
	            }
            }

            if((departmentcondition!=null)&&(departmentcondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ departmentcondition;
	            }
	            else{
	                allCondition = departmentcondition;
	            }
            }

            if((sectioncondition!=null)&&(sectioncondition.length()>0)&&usingSection){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ sectioncondition;
	            }
	            else{
	                allCondition = sectioncondition;
	            }
            }

            if((positioncondition!=null)&&(positioncondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ positioncondition;
	            }
	            else{
	                allCondition = positioncondition;
	            }
            }

	        if(allCondition.length()>0){
	       			sql = sql + " WHERE  "+allCondition +ordercondition;
	        }
	        else{
					sql = sql + " " +ordercondition;
	        }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
						if(start == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " + start + ","+ recordToGet ;

                        break;

                 case DBHandler.DBSVR_POSTGRESQL :
						if(start == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " +recordToGet + " OFFSET "+ start ;

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

            //System.out.println("SQL Search : "+sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();


            while(rs.next()) {
                Vector vectTemp = new Vector(1,1);
				AppMapping appmapping = new AppMapping();
                AppType appType = new AppType();
                DocType docType = new DocType();
                Department department = new Department();
                Section section = new Section();
                Position position = new Position();

                appmapping.setOID(rs.getLong(1));
                appmapping.setAppTitle(rs.getString(2));
                appmapping.setAppIndex(rs.getInt(3));
                vectTemp.add(appmapping);

                appType.setName(rs.getString(4));
                vectTemp.add(appType);

                docType.setName(rs.getString(5));
                vectTemp.add(docType);

                department.setDepartment(rs.getString(6));
                vectTemp.add(department);

                if(usingSection){
	                section.setSection(rs.getString(7));
	                vectTemp.add(section);
	
	                position.setPosition(rs.getString(8));
	                vectTemp.add(position);
                }else{
	                section.setSection("");
	                vectTemp.add(section);
	
	                position.setPosition(rs.getString(7));
	                vectTemp.add(position);
                }

				result.add(vectTemp);
            }

        }catch(Exception e) {
	            System.out.println(e);            
	    }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /**
    * this method used to count all AppDocMapping data
    */
	public static int getCountSearch(SrcAppMapping srcappmapping){
     DBResultSet dbrs = null;
     int result = 0;
     if(srcappmapping==null){
		srcappmapping = new SrcAppMapping();
     }

     boolean usingSection = srcappmapping.getSectionId()!=0 ? true : false;
     try{
			String sql = "SELECT COUNT(MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APP_MAPPING_OID] + ") " +
 		                 " FROM " + PstAppMapping.TBL_WF_APP_MAPPING + " AS MAP " +
                         " LEFT JOIN " + PstDocType.TBL_WF_DOC_TYPE + " AS DOC " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DOCTYPE] +
                         " = DOC." + PstDocType.fieldNames[PstDocType.FLD_TYPE] +
                         " LEFT JOIN " + PstAppType.TBL_WF_APP_TYPE + " AS APP " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_APPTYPE_OID] +
                         " = APP." + PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID] +
                         " LEFT JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_DEPARTMENT_OID] +
                         " = DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID];
                         if(usingSection){
                         sql = sql + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " AS SEC " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID] +
                         " = SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID];
            			 }
                         sql = sql + " LEFT JOIN " + PstPosition.TBL_HR_POSITION + " AS POS " +
                         " ON MAP." + PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID] +
                         " = POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];

	        String systypecondition = "((DOC."+PstDocType.fieldNames[PstDocType.FLD_TYPE]+" & "+PstDocType.STR_FILTER_SYSTEM+")='"+
                                       ((srcappmapping.getSystemType() << PstDocType.SHIFT_SYSTEM) & PstDocType.FILTER_SYSTEM)+"')";

	        String apptitlecondition = "";
	        if(srcappmapping.getAppTitle()!="" && srcappmapping.getAppTitle().length()>0){
	             apptitlecondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_APP_TITLE]+" LIKE '%"+srcappmapping.getAppTitle()+"%') ";
	        }

            String apptypecondition = "";
	        if(srcappmapping.getAppTypeOid()!=FRM_FIELD_ALL){
	             apptypecondition = "(APP."+PstAppType.fieldNames[PstAppType.FLD_APPTYPE_OID]+" = "+srcappmapping.getAppTypeOid()+") ";
	        }

            String doctypecondition = "";
	        if(srcappmapping.getDocTypeType()!=FRM_FIELD_ALL){
	             doctypecondition = "(DOC."+PstDocType.fieldNames[PstDocType.FLD_TYPE]+" = "+srcappmapping.getDocTypeType()+") ";
	        }

            String departmentcondition = "";
	        if(srcappmapping.getDepartmentOid()!=FRM_FIELD_ALL){
	             departmentcondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_DEPARTMENT_OID]+" = "+srcappmapping.getDepartmentOid()+") ";
	        }

            String sectioncondition = "";
	        if(srcappmapping.getSectionId()!=FRM_FIELD_ALL){
	             sectioncondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_SECTION_ID]+" = "+srcappmapping.getSectionId()+") ";
	        }

            String positioncondition = "";
	        if(srcappmapping.getPositionOid()!=FRM_FIELD_ALL){
	             positioncondition = "(MAP."+PstAppMapping.fieldNames[PstAppMapping.FLD_POSITION_OID]+" = "+srcappmapping.getPositionOid()+") ";
	        }

	        String allCondition = "";
	        if((systypecondition!=null)&&(systypecondition.length()>0)){
	             allCondition = systypecondition;
	        }

            if((apptitlecondition!=null)&&(apptitlecondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ apptitlecondition;
	            }
	            else{
	                allCondition = apptitlecondition;
	            }
            }

            if((apptypecondition!=null)&&(apptypecondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ apptypecondition;
	            }
	            else{
	                allCondition = apptypecondition;
	            }
            }

            if((doctypecondition!=null)&&(doctypecondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ doctypecondition;
	            }
	            else{
	                allCondition = doctypecondition;
	            }
            }

            if((departmentcondition!=null)&&(departmentcondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ departmentcondition;
	            }
	            else{
	                allCondition = departmentcondition;
	            }
            }

            if((sectioncondition!=null)&&(sectioncondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ sectioncondition;
	            }
	            else{
	                allCondition = sectioncondition;
	            }
            }

            if((positioncondition!=null)&&(positioncondition.length()>0)){
	            if((allCondition.length()>0)){
	                allCondition = allCondition +" AND "+ positioncondition;
	            }
	            else{
	                allCondition = positioncondition;
	            }
            }

	        if(allCondition.length()>0){
	       			sql = sql + " WHERE  "+allCondition;
	        }

            //System.out.println("SQL Count : "+sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()) {
                result = rs.getInt(1);
            }

        }catch(Exception e) {
	            System.out.println(e);            
	    }finally{
            DBResultSet.close(dbrs);
            return result;
        }
	}

}

