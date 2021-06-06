/*
 * Session Name  	:  SessMemberReg.java
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
package com.dimata.posbo.session.masterdata;/* java package */

import java.util.*;

import com.dimata.posbo.entity.search.*;
import com.dimata.harisma.entity.masterdata.Religion;
import com.dimata.harisma.entity.masterdata.PstReligion;
import com.dimata.common.entity.contact.*;
import com.dimata.hanoman.entity.masterdata.Contact;
import com.dimata.hanoman.entity.masterdata.PstContact;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.util.*;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstPendingOrder;
import java.sql.ResultSet;


  public class  SessMemberReg  {
    public static final String SESS_SRC_MEMBERREG = "SESSION_SRC_MEMBERREG";
    public static final String SESS_SRC_TRAVEL = "SESSION_SRC_TRAVEL";
    public static final String SESSION_SRC_MEMBERCOMPANY = "SESSION_SRC_MEMBERCOMPANY";
    public static final String SESSION_SRC_GUIDE = "SESSION_SRC_GUIDE";
    
    private static Vector logicParser(String text) {

        Vector vector = new Vector(1, 1);
        if (text != null && text.length() > 0) {
            vector = LogicParser.textSentence(text);
            for (int i = 0; i < vector.size(); i++) {
                String code = (String) vector.get(i);
                if (((vector.get(vector.size() - 1)).equals(LogicParser.SIGN))
                        && ((vector.get(vector.size() - 1)).equals(LogicParser.ENGLISH))) {
                    vector.remove(vector.size() - 1);
                }
            }
        }//

        return vector;
    }

    // add by fitra
    public static Vector searchContact(SrcMemberReg srcmemberreg, int start, int recordToGet){

		Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = " SELECT MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_REGDATE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_EMAIL]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_PERSON_NAME]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_TELP_MOBILE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_HOME_COUNTRY]+
                                     
                                
                                     " FROM "+ PstContact.TBL_CONTACT + " AS MBR INNER JOIN "+
                                     PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG ON MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+ 
                                     " = ASG."+ PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+ " INNER JOIN " +
                                     PstContactClass.TBL_CONTACT_CLASS + " AS CLS ON ASG."+ 
                                     PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + " =  CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + 
                                      " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                                      " = " + PstContactClass.CONTACT_TYPE_TRAVEL_AGENT;
                                     

			
                        
                        
                        
                        
                        
                        
                        
                        
                          if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstContact.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }
                /**
                 * Ari wiweka 2-130714
                 * untuk pencarian berdasarkan nama dan company
                 */
               /* if (srcmemberreg.getName() != "") {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " LIKE '%" + srcmemberreg.getReligion()+"%'";
                }*/
                if (!srcmemberreg.getCompanyName().equals("")) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " LIKE '%" + srcmemberreg.getCompanyName()+"%'";
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

             
                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }
                
                
                

               

                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_BARCODE) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.TIPE_MEMBER) {
                    sql = sql + " ORDER BY GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                            ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
                }

            }

            if (recordToGet != 0) {
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;
                    case DBHandler.DBSVR_POSTGRESQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                        break;
                    case DBHandler.DBSVR_SYBASE:
                        break;
                    case DBHandler.DBSVR_ORACLE:
                        break;
                    case DBHandler.DBSVR_MSSQL:
                        break;

                    default:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                }
                // sql = sql + " LIMIT " + start + "," + recordToGet;
            }
                        
                        
                        
                        
                        

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {
                                //Vector tempo = new Vector(1, 1);
				Contact contact = new Contact();
                                //Negara negara = new Negara();
                                
                                //contact.setOID(rs.getLong(PstContact.fieldNames[PstContact.FLD_CONTACT_ID]));

			contact.setContactCode(rs.getString(PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]));
                        contact.setContactType(rs.getInt(PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]));
                        contact.setRegdate(rs.getDate(PstContact.fieldNames[PstContact.FLD_REGDATE]));
                        contact.setEmail(rs.getString(PstContact.fieldNames[PstContact.FLD_EMAIL]));
			
                        contact.setHomeCountry(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_COUNTRY]));
			
                       
			contact.setPersonName(rs.getString(PstContact.fieldNames[PstContact.FLD_PERSON_NAME]));

                        contact.setTelpMobile(rs.getString(PstContact.fieldNames[PstContact.FLD_TELP_MOBILE]));
                        contact.setOID(rs.getLong(PstContact.fieldNames[PstContact.FLD_CONTACT_ID]));
                         //contact.setIdNegara(rs.getLong(PstContact.fieldNames[PstContact.FLD_ID_NEGARA]));
                        
                        //tempo.add(contact);
                        
                        lists.add(contact);

                        }
                        return lists;


		}
                        catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);

		}

			return  lists;

	}
    
    
    
     // add by fitra
    public static Vector searchCompanyMember(SrcMemberReg srcmemberreg, int start, int recordToGet){
    
		Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = " SELECT MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_REGDATE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_EMAIL]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_PERSON_NAME]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_TELP_MOBILE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_HOME_COUNTRY]+
                                     
                                
                                     " FROM "+ PstContact.TBL_CONTACT + " AS MBR INNER JOIN "+
                                     PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG ON MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+ 
                                     " = ASG."+ PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+ " INNER JOIN " +
                                     PstContactClass.TBL_CONTACT_CLASS + " AS CLS ON ASG."+ 
                                     PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + " =  CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + 
                                      " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                                      " = " + PstContactClass.CONTACT_TYPE_COMPANY;
                                     

			
                        
                        
                        
                        
                        
                        
                        
                        
                          if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstContact.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }
                /**
                 * Ari wiweka 2-130714
                 * untuk pencarian berdasarkan nama dan company
                 */
               /* if (srcmemberreg.getName() != "") {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " LIKE '%" + srcmemberreg.getReligion()+"%'";
                }*/
                if (!srcmemberreg.getCompanyName().equals("")) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " LIKE '%" + srcmemberreg.getCompanyName()+"%'";
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

             
                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }
                
                
                

               

                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_BARCODE) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.TIPE_MEMBER) {
                    sql = sql + " ORDER BY GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                            ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
                }

            }

            if (recordToGet != 0) {
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;
                    case DBHandler.DBSVR_POSTGRESQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                        break;
                    case DBHandler.DBSVR_SYBASE:
                        break;
                    case DBHandler.DBSVR_ORACLE:
                        break;
                    case DBHandler.DBSVR_MSSQL:
                        break;

                    default:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                }
                // sql = sql + " LIMIT " + start + "," + recordToGet;
            }
                        
                        
                        
                        
                        

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {
                                //Vector tempo = new Vector(1, 1);
				Contact contact = new Contact();
                                //Negara negara = new Negara();
                                
                                //contact.setOID(rs.getLong(PstContact.fieldNames[PstContact.FLD_CONTACT_ID]));

			contact.setContactCode(rs.getString(PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]));
                        contact.setContactType(rs.getInt(PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]));
                        contact.setRegdate(rs.getDate(PstContact.fieldNames[PstContact.FLD_REGDATE]));
                        contact.setEmail(rs.getString(PstContact.fieldNames[PstContact.FLD_EMAIL]));
			
                        contact.setHomeCountry(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_COUNTRY]));
			
                       
			contact.setPersonName(rs.getString(PstContact.fieldNames[PstContact.FLD_PERSON_NAME]));

                        contact.setTelpMobile(rs.getString(PstContact.fieldNames[PstContact.FLD_TELP_MOBILE]));
                        contact.setOID(rs.getLong(PstContact.fieldNames[PstContact.FLD_CONTACT_ID]));
                         //contact.setIdNegara(rs.getLong(PstContact.fieldNames[PstContact.FLD_ID_NEGARA]));
                        
                        //tempo.add(contact);
                        
                        lists.add(contact);

                        }
                        return lists;


		}
                        catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);

		}

			return  lists;

	}
    
    
    
    
    // add by fitra
    public static Vector searchGuide(SrcMemberReg srcmemberreg, int start, int recordToGet){

		Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = " SELECT MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_REGDATE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_EMAIL]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_PERSON_NAME]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_TELP_MOBILE]+
                                     ", MBR."+ PstContact.fieldNames[PstContact.FLD_HOME_COUNTRY]+
                                     
                                
                                    
                                     " FROM "+ PstContact.TBL_CONTACT + " AS MBR INNER JOIN "+
                                     PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG ON MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+ 
                                     " = ASG."+ PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+ " INNER JOIN " +
                                     PstContactClass.TBL_CONTACT_CLASS + " AS CLS ON ASG."+ 
                                     PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + " =  CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + 
                                      " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                                      " = " + PstContactClass.CONTACT_TYPE_GUIDE;
                                     

			
                        
                        
                        
                        
                        
                        
                        
                        
                          if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstContact.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }
                /**
                 * Ari wiweka 2-130714
                 * untuk pencarian berdasarkan nama dan company
                 */
               /* if (srcmemberreg.getName() != "") {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " LIKE '%" + srcmemberreg.getReligion()+"%'";
                }*/
                if (!srcmemberreg.getCompanyName().equals("")) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " LIKE '%" + srcmemberreg.getCompanyName()+"%'";
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

             
                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }
                
                
                

               

                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_BARCODE) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.TIPE_MEMBER) {
                    sql = sql + " ORDER BY GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                            ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
                }

            }

            if (recordToGet != 0) {
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;
                    case DBHandler.DBSVR_POSTGRESQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                        break;
                    case DBHandler.DBSVR_SYBASE:
                        break;
                    case DBHandler.DBSVR_ORACLE:
                        break;
                    case DBHandler.DBSVR_MSSQL:
                        break;

                    default:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                }
                // sql = sql + " LIMIT " + start + "," + recordToGet;
            }
                        
                        
                        
                        
                        

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {
                                //Vector tempo = new Vector(1, 1);
				Contact contact = new Contact();
                                //Negara negara = new Negara();
                                
                                //contact.setOID(rs.getLong(PstContact.fieldNames[PstContact.FLD_CONTACT_ID]));

			contact.setContactCode(rs.getString(PstContact.fieldNames[PstContact.FLD_CONTACT_CODE]));
                        contact.setContactType(rs.getInt(PstContact.fieldNames[PstContact.FLD_CONTACT_TYPE]));
                        contact.setRegdate(rs.getDate(PstContact.fieldNames[PstContact.FLD_REGDATE]));
                        contact.setEmail(rs.getString(PstContact.fieldNames[PstContact.FLD_EMAIL]));
			
                        contact.setHomeCountry(rs.getString(PstContact.fieldNames[PstContact.FLD_HOME_COUNTRY]));
			
                       
			contact.setPersonName(rs.getString(PstContact.fieldNames[PstContact.FLD_PERSON_NAME]));

                        contact.setTelpMobile(rs.getString(PstContact.fieldNames[PstContact.FLD_TELP_MOBILE]));
                        contact.setOID(rs.getLong(PstContact.fieldNames[PstContact.FLD_CONTACT_ID]));
                         //contact.setIdNegara(rs.getLong(PstContact.fieldNames[PstContact.FLD_ID_NEGARA]));
                        
                        //tempo.add(contact);
                        
                        lists.add(contact);

                        }
                        return lists;


		}
                        catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);

		}

			return  lists;

	}
    
    
    
  
    
    
    
    

    public static Vector searchMemberReg(SrcMemberReg srcmemberreg, int start, int recordToGet) {

        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {

            String sql = " SELECT MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS] +
                    ", RLG." + PstReligion.fieldNames[PstReligion.FLD_RELIGION] +
                    ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] +
                    ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE] +
                    ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_NATIONALITY] +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " LEFT JOIN " + PstReligion.TBL_HR_RELIGION + " AS RLG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                    " = RLG." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID] +
                    " LEFT JOIN " + PstMemberGroup.TBL_MEMBER_GROUP + " AS GRP " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                    " = GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] +
                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_MEMBER + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE;

            /*"ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = (SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " FROM " + PstContactClass.TBL_CONTACT_CLASS +
            " WHERE " +*/


            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }
                
                //
                vectData = logicParser(srcmemberreg.getMemberCode());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }
                /**
                 * Ari wiweka 2-130714
                 * untuk pencarian berdasarkan nama dan company
                 */
               /* if (srcmemberreg.getName() != "") {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " LIKE '%" + srcmemberreg.getReligion()+"%'";
                }*/
                if (!srcmemberreg.getCompanyName().equals("")) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " LIKE '%" + srcmemberreg.getCompanyName()+"%'";
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

                if (!srcmemberreg.isAllBirthDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getBirthDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getBirthDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (srcmemberreg.getStatus() > -1) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                            " = " + srcmemberreg.getStatus();
                }

                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_BARCODE) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.TIPE_MEMBER) {
                    sql = sql + " ORDER BY GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                            ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
                }

            }

            if (recordToGet != 0) {
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;
                    case DBHandler.DBSVR_POSTGRESQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                        break;
                    case DBHandler.DBSVR_SYBASE:
                        break;
                    case DBHandler.DBSVR_ORACLE:
                        break;
                    case DBHandler.DBSVR_MSSQL:
                        break;

                    default:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                }
                // sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                MemberReg member = new MemberReg();
                member.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                member.setContactCode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE]));
                member.setOID(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]));
                member.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                member.setHomeAddr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]));
                member.setHomeTelp(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]));
                member.setTelpMobile(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]));
                member.setHomeFax(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX]));
                member.setHomeTown(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN]));
                member.setMemberBirthDate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE]));
                member.setMemberGroupId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID]));
                member.setMemberBarcode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE]));
                member.setMemberCounter(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER]));
                member.setMemberIdCardNumber(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER]));
                member.setMemberLastUpdate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE]));
                member.setMemberReligionId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID]));
                member.setMemberSex(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX]));
                member.setMemberStatus(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS]));
                member.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));
                member.setMemberCreditLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT]));
                member.setMemberConsigmentLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT]));
                member.setNationality(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_NATIONALITY]));

                temp.add(member);

                Religion religion = new Religion();
                religion.setReligion(rs.getString(PstReligion.fieldNames[PstReligion.FLD_RELIGION]));
                temp.add(religion);

                MemberGroup mGroup = new MemberGroup();
                mGroup.setCode(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE]));
                mGroup.setName(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME]));
                mGroup.setGroupType(rs.getInt(PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE]));
                temp.add(mGroup);

                result.add(temp);
            }
        } catch (Exception e) {
            System.out.println("err di search member : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    
    
    public static Vector searchMemberDeliveryOrder(SrcMemberReg srcmemberreg, int start, int recordToGet) {

        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {

            String sql = " SELECT MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_NATIONALITY] +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_CUSTOMER + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE;

            /*"ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = (SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " FROM " + PstContactClass.TBL_CONTACT_CLASS +
            " WHERE " +*/


            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }
                
                //
                vectData = logicParser(srcmemberreg.getMemberCode());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }
                /**
                 * Ari wiweka 2-130714
                 * untuk pencarian berdasarkan nama dan company
                 */
               /* if (srcmemberreg.getName() != "") {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " LIKE '%" + srcmemberreg.getReligion()+"%'";
                }*/
                if (!srcmemberreg.getCompanyName().equals("")) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " LIKE '%" + srcmemberreg.getCompanyName()+"%'";
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

                if (!srcmemberreg.isAllBirthDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getBirthDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getBirthDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (srcmemberreg.getStatus() > -1) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                            " = " + srcmemberreg.getStatus();
                }

                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_BARCODE) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.TIPE_MEMBER) {
                    sql = sql + " ORDER BY GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                            ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
                }

            }

            if (recordToGet != 0) {
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;
                    case DBHandler.DBSVR_POSTGRESQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                        break;
                    case DBHandler.DBSVR_SYBASE:
                        break;
                    case DBHandler.DBSVR_ORACLE:
                        break;
                    case DBHandler.DBSVR_MSSQL:
                        break;

                    default:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                }
                // sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                MemberReg member = new MemberReg();
                member.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                member.setContactCode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE]));
                member.setOID(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]));
                member.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                member.setHomeAddr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]));
                member.setHomeTelp(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]));
                member.setTelpMobile(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]));
                member.setHomeFax(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX]));
                member.setHomeTown(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN]));
                member.setMemberBirthDate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE]));
                member.setMemberGroupId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID]));
                member.setMemberBarcode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE]));
                member.setMemberCounter(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER]));
                member.setMemberIdCardNumber(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER]));
                member.setMemberLastUpdate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE]));
                member.setMemberReligionId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID]));
                member.setMemberSex(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX]));
                member.setMemberStatus(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS]));
                member.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));
                member.setMemberCreditLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT]));
                member.setMemberConsigmentLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT]));
                member.setNationality(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_NATIONALITY]));

                temp.add(member);

                result.add(temp);
            }
        } catch (Exception e) {
            System.out.println("err di search member : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    
    
    
    
    public static Vector searchWaitingList(SrcMemberReg srcmemberreg, int start, int recordToGet) {

        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {

    String sql = " SELECT MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT] +
            ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_NATIONALITY] +
            " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
            " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
            " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
            " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
            " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
            " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
            " = " + PstContactClass.CONTACT_TYPE_WAITING_LIST + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
            " != " + PstMemberReg.DELETE;
            
            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }
                
                //
                vectData = logicParser(srcmemberreg.getMemberCode());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }
                /**
                 * Ari wiweka 2-130714
                 * untuk pencarian berdasarkan nama dan company
                 */
                if (!srcmemberreg.getCompanyName().equals("")) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " LIKE '%" + srcmemberreg.getCompanyName()+"%'";
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

                if (!srcmemberreg.isAllBirthDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getBirthDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getBirthDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (srcmemberreg.getStatus() > -1) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                            " = " + srcmemberreg.getStatus();
                }

                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_BARCODE) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.TIPE_MEMBER) {
                    sql = sql + " ORDER BY GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                            ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
                }

            }

            if (recordToGet != 0) {
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;
                    case DBHandler.DBSVR_POSTGRESQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                        break;
                    case DBHandler.DBSVR_SYBASE:
                        break;
                    case DBHandler.DBSVR_ORACLE:
                        break;
                    case DBHandler.DBSVR_MSSQL:
                        break;

                    default:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                }
                // sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                MemberReg member = new MemberReg();
                member.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                member.setContactCode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE]));
                member.setOID(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]));
                member.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                member.setHomeAddr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]));
                member.setHomeTelp(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]));
                member.setTelpMobile(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]));
                member.setHomeFax(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX]));
                member.setHomeTown(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN]));
                member.setMemberBirthDate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE]));
                member.setMemberGroupId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID]));
                member.setMemberBarcode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE]));
                member.setMemberCounter(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER]));
                member.setMemberIdCardNumber(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER]));
                member.setMemberLastUpdate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE]));
                member.setMemberReligionId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID]));
                member.setMemberSex(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX]));
                member.setMemberStatus(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS]));
                member.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));
                member.setMemberCreditLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT]));
                member.setMemberConsigmentLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT]));
                member.setNationality(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_NATIONALITY]));

                temp.add(member);

                result.add(temp);
            }
        } catch (Exception e) {
            System.out.println("err di search member : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    /**
     * add opie-eyek agar member yang tampil di sales order adalah member yang tidak expired date nya habis
     * @param srcmemberreg
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector searchMemberRegToOrder(SrcMemberReg srcmemberreg, int start, int recordToGet) {

        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Date dateNow = new Date();
        try {

            String sql = " SELECT MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS] +
                    ", RLG." + PstReligion.fieldNames[PstReligion.FLD_RELIGION] +
                    ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME] +
                    ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE] +
                    ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT] +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " LEFT JOIN " + PstReligion.TBL_HR_RELIGION + " AS RLG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                    " = RLG." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID] +
                    " INNER JOIN " + PstMemberGroup.TBL_MEMBER_GROUP + " AS GRP " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                    " = GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] +
                    //update opie-eyek 20130913
                    " INNER JOIN " + PstMemberRegistrationHistory.TBL_MEMBER_REGISTRATION_HISTORY + " AS MR " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = MR." + PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID] +

                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_MEMBER + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE+" AND "+
                    "MR."+ PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]+ ">= '"+Formater.formatDate(dateNow, "yyyy-mm-dd hh:mm:ss")+"' ";

            /*"ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = (SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " FROM " + PstContactClass.TBL_CONTACT_CLASS +
            " WHERE " +*/


            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }
                /**
                 * Ari wiweka 2-130714
                 * untuk pencarian berdasarkan nama dan company
                 */
                if (srcmemberreg.getCompanyName() != "") {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " LIKE '%" + srcmemberreg.getCompanyName()+"%'";
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

                if (!srcmemberreg.isAllBirthDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getBirthDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getBirthDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (srcmemberreg.getStatus() > -1) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                            " = " + srcmemberreg.getStatus();
                }

                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_BARCODE) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                } else if (srcmemberreg.getSortBy() == srcmemberreg.TIPE_MEMBER) {
                    sql = sql + " ORDER BY GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] +
                            ", GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
                }

            }

            if (recordToGet != 0) {
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;
                    case DBHandler.DBSVR_POSTGRESQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                        break;
                    case DBHandler.DBSVR_SYBASE:
                        break;
                    case DBHandler.DBSVR_ORACLE:
                        break;
                    case DBHandler.DBSVR_MSSQL:
                        break;

                    default:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                }
                // sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                MemberReg member = new MemberReg();
                member.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                member.setContactCode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE]));
                member.setOID(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]));
                member.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                member.setHomeAddr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]));
                member.setHomeTelp(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]));
                member.setTelpMobile(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]));
                member.setHomeFax(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_FAX]));
                member.setHomeTown(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TOWN]));
                member.setMemberBirthDate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE]));
                member.setMemberGroupId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID]));
                member.setMemberBarcode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE]));
                member.setMemberCounter(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_COUNTER]));
                member.setMemberIdCardNumber(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_ID_CARD_NUMBER]));
                member.setMemberLastUpdate(rs.getDate(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_LAST_UPDATE]));
                member.setMemberReligionId(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID]));
                member.setMemberSex(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_SEX]));
                member.setMemberStatus(rs.getInt(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS]));
                member.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));
                member.setMemberCreditLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CREDIT_LIMIT]));
                member.setMemberConsigmentLimit(rs.getDouble(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_CONSIGMENT_LIMIT]));


                temp.add(member);

                Religion religion = new Religion();
                religion.setReligion(rs.getString(PstReligion.fieldNames[PstReligion.FLD_RELIGION]));
                temp.add(religion);

                MemberGroup mGroup = new MemberGroup();
                mGroup.setCode(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE]));
                mGroup.setName(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME]));
                mGroup.setGroupType(rs.getInt(PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE]));
                temp.add(mGroup);

                result.add(temp);
            }
        } catch (Exception e) {
            System.out.println("err di search member : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static Vector searchSupplier(SrcMemberReg srcmemberreg, int start, int recordToGet) {

        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {

            String sql = " SELECT MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_NR] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_FAX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_SUPPLIER + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE;

            /*" WHERE ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = (SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " FROM " + PstContactClass.TBL_CONTACT_CLASS +
            " WHERE " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
            " = " + PstContactClass.CONTACT_TYPE_SUPPLIER + ") AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
            " != " + PstMemberReg.DELETE;*/


            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getCodeSupplier());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getCompanyName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getContactPerson());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' OR " +
                                    " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] +
                                    " LIKE '%" + str.trim() + "%')  ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }


                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " ,MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " ,MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME];
                } else {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE];
                }

            }

            if (recordToGet != 0) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                ContactList member = new ContactList();
                member.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                member.setContactCode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE]));
                member.setOID(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]));
                member.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                member.setPersonLastname(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME]));
                member.setTelpNr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_NR]));
                member.setFax(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_FAX]));
                member.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));


                result.add(member);
            }
        } catch (Exception e) {
            System.out.println("err di search supplier : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    
    public static Vector searchKepemilikan(SrcMemberReg srcmemberreg, int start, int recordToGet) {

        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {

            String sql = " SELECT MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_NR] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_FAX] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS] +
                    ", MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_GUIDE + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE;

            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getCodeSupplier());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getCompanyName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getContactPerson());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' OR " +
                                    " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] +
                                    " LIKE '%" + str.trim() + "%')  ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }


                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " ,MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " ,MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME];
                } else {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE];
                }

            }

            if (recordToGet != 0) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                ContactList member = new ContactList();
                member.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                member.setContactCode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE]));
                member.setOID(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]));
                member.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                member.setPersonLastname(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME]));
                member.setTelpNr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_NR]));
                member.setFax(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_FAX]));
                member.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));


                result.add(member);
            }
        } catch (Exception e) {
            System.out.println("err di search supplier : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    
    
     // add by fitra
     public static int countContact(int limitStart,int recordToGet, String whereClause, String order){

		//Vector lists = new Vector(); 
                
                int count = 0;     

		DBResultSet dbrs = null;

		try {

			String sql = " SELECT COUNT(" +
                                     " MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+")" +                                
                                     " FROM "+ PstContact.TBL_CONTACT + " AS MBR INNER JOIN "+
                                     PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG ON MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+ 
                                     " = ASG."+ PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+ " INNER JOIN " +
                                     PstContactClass.TBL_CONTACT_CLASS + " AS CLS ON ASG."+ 
                                     PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID]+" = CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];
                                     

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while (rs.next()) {
                        count = rs.getInt(1);
                       }


		}
                        catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);
                         return  count;
		}

			

	}
    
    
       // add by fitra
     public static int countCompanyMember(int limitStart,int recordToGet, String whereClause, String order){
    
		//Vector lists = new Vector(); 
                
                int count = 0;     

		DBResultSet dbrs = null;

		try {

			String sql = " SELECT COUNT(" +
                                     " MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+")" +                                
                                     " FROM "+ PstContact.TBL_CONTACT + " AS MBR INNER JOIN "+
                                     PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG ON MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+ 
                                     " = ASG."+ PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+ " INNER JOIN " +
                                     PstContactClass.TBL_CONTACT_CLASS + " AS CLS ON ASG."+ 
                                     PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID]+" = CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];
                                     

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while (rs.next()) {
                        count = rs.getInt(1);
                       }


		}
                        catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);
                         return  count;
		}

			

	}
    
     
    
    // add by fitra
   public static int countGuide(int limitStart,int recordToGet, String whereClause, String order){

		//Vector lists = new Vector(); 
                
                int count = 0;     

		DBResultSet dbrs = null;

		try {

			String sql = " SELECT COUNT(" +
                                     " MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+")" +                                
                                     " FROM "+ PstContact.TBL_CONTACT + " AS MBR INNER JOIN "+
                                     PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG ON MBR."+ PstContact.fieldNames[PstContact.FLD_CONTACT_ID]+ 
                                     " = ASG."+ PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+ " INNER JOIN " +
                                     PstContactClass.TBL_CONTACT_CLASS + " AS CLS ON ASG."+ 
                                     PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+ " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];
                                     

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while (rs.next()) {
                        count = rs.getInt(1);
                       }


		}
                        catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);
                         return  count;
		}

			

	}
    
    
     
     
    
    
    
    
    //count vendor price on searching set supplier
    // by 20120510
    public static int countSearchSupplier(SrcMemberReg srcmemberreg) {

        int count = 0;
        DBResultSet dbrs = null;
        try {

            String sql = " SELECT  COUNT(" +
                    " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ") " +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_SUPPLIER + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE;

            /*" WHERE ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = (SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " FROM " + PstContactClass.TBL_CONTACT_CLASS +
            " WHERE " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
            " = " + PstContactClass.CONTACT_TYPE_SUPPLIER + ") AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
            " != " + PstMemberReg.DELETE;*/


            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getCodeSupplier());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getCompanyName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getContactPerson());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' OR " +
                                    " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] +
                                    " LIKE '%" + str.trim() + "%')  ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }


                if (srcmemberreg.getSortBy() == srcmemberreg.MEMBER_NAME) {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                            " ,MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                            " ,MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME];
                } else {
                    sql = sql + " ORDER BY MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE];
                }

            }

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("err di search supplier : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return count;
        }
    }

    public static int getCountSearch(SrcMemberReg srcmemberreg) {

        int count = 0;
        DBResultSet dbrs = null;
        try {

            String sql = " SELECT COUNT(" +
                    " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ") " +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    //update opie jadi dari inner jadi left 23-06-2012
                    //" INNER JOIN " + PstReligion.TBL_HR_RELIGION + " AS RLG " +
                    " LEFT JOIN " + PstReligion.TBL_HR_RELIGION + " AS RLG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                    " = RLG." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID] +
                    " INNER JOIN " + PstMemberGroup.TBL_MEMBER_GROUP + " AS GRP " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                    " = GRP." + PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] +
                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_MEMBER + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE;

            /*" WHERE ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = (SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " FROM " + PstContactClass.TBL_CONTACT_CLASS +
            " WHERE " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
            " = " + PstContactClass.CONTACT_TYPE_MEMBER + ") AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
            " != " + PstMemberReg.DELETE;*/


            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getBarcode());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getName());
                String strWhereName = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhereName = strWhereName + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhereName = strWhereName + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhereName = strWhereName + str.trim();
                        }
                    }
                    strWhereName = strWhereName + ")";
                }

                //
                vectData = logicParser(srcmemberreg.getMemberCode());
                String strWhereCode = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhereCode = strWhereCode + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhereCode = strWhereCode + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhereCode = strWhereCode + str.trim();
                        }
                    }
                    strWhereCode = strWhereCode + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }
                
                if(strWhereName!=null&&strWhereName.length()>0){
                    sql=sql+" AND "+strWhereName;
                }
                
                if(strWhereCode!=null && strWhereCode.length()>0){
                    sql=sql+" AND "+strWhereCode;
                }
                
                if (srcmemberreg.getReligion() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_RELIGION_ID] +
                            " = " + srcmemberreg.getReligion();
                }

                if (srcmemberreg.getGroupmember() != 0) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] +
                            " = " + srcmemberreg.getGroupmember();
                }

                if (!srcmemberreg.isAllBirthDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BIRTH_DATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getBirthDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getBirthDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (!srcmemberreg.isAllRegDate()) {
                    sql = sql + " AND (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_REGDATE] +
                            " BETWEEN '" + Formater.formatDate(srcmemberreg.getRegDateFrom(), "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(srcmemberreg.getRegDateTo(), "yyyy-MM-dd") + "') ";
                }

                if (srcmemberreg.getStatus() > -1) {
                    sql = sql + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_STATUS] +
                            " = " + srcmemberreg.getStatus();
                }

            }


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("err di count member : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return count;
        }


    }

    public static int getCountSupplier(SrcMemberReg srcmemberreg) {

        int count = 0;
        DBResultSet dbrs = null;
        try {

            String sql = " SELECT COUNT(" +
                    " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    ") " +
                    " FROM " + PstMemberReg.TBL_CONTACT_LIST + " AS MBR " +
                    " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS ASG " +
                    " ON MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                    " = ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS CLS " +
                    " ON ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    " = CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " WHERE CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " = " + PstContactClass.CONTACT_TYPE_SUPPLIER + " AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
                    " != " + PstMemberReg.DELETE;

            /*" WHERE ASG." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
            " = (SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
            " FROM " + PstContactClass.TBL_CONTACT_CLASS +
            " WHERE " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
            " = " + PstContactClass.CONTACT_TYPE_SUPPLIER + ") AND MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] +
            " != " + PstMemberReg.DELETE;*/


            if (srcmemberreg != null) {
                Vector vectData = logicParser(srcmemberreg.getCodeSupplier());
                String strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getCompanyName());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] +
                                    " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }

                vectData = logicParser(srcmemberreg.getContactPerson());
                strWhere = "";
                if (vectData != null && vectData.size() > 0) {
                    strWhere = strWhere + "(";
                    for (int i = 0; i < vectData.size(); i++) {
                        String str = (String) vectData.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strWhere = strWhere + " (MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                                    " LIKE '%" + str.trim() + "%' OR " +
                                    " MBR." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] +
                                    " LIKE '%" + str.trim() + "%')  ";
                        } else {
                            strWhere = strWhere + str.trim();
                        }
                    }
                    strWhere = strWhere + ")";
                }

                if (strWhere != null && strWhere.length() > 0) {
                    sql = sql + " AND " + strWhere;
                }


            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("err di count supplier : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return count;
        }
    }

    /**
     * ini
     * @param oidMemberReg
     * @return
     */
    public static boolean readyDataToDelete(long oidMemberReg) {
        boolean status = true;
        try {
            // ini untuk pengecekan data di penjualan
            String where = PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] + "=" + oidMemberReg;
            Vector vList = PstBillMain.list(0, 0, where, "");
            if (vList != null && vList.size() > 0) {
                status = false;
            } else {
                where = PstPendingOrder.fieldNames[PstPendingOrder.FLD_MEMBER_ID] + "=" + oidMemberReg;
                vList = PstPendingOrder.list(0, 0, where, "");
                if (vList != null && vList.size() > 0) {
                    status = false;
                } else {
                    where = PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_ID] + "=" + oidMemberReg;
                    vList = PstMemberPoin.list(0, 0, where, "");
                    if (vList != null && vList.size() > 0) {
                        status = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("SessLocation - readyDataToDelete : " + e.toString());
        }
        return status;
    }
}
