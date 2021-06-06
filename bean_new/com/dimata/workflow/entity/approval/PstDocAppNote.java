
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

package com.dimata.workflow.entity.approval; 

/* package java */ 
import java.sql.*
;import java.util.*
;
/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

/* package workflow */

public class PstDocAppNote extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_WF_DOC_APP_NOTE = "wf_doc_app_note";

	public static final  int FLD_DOC_APP_NOTE_OID = 0;
	public static final  int FLD_DOCAPP_MAIN_OID = 1;
	public static final  int FLD_NOTE_DATE = 2;
	public static final  int FLD_DESCRIPTION = 3;

	public static final  String[] fieldNames = {
		"DOC_APP_NOTE_OID",
		"DOCAPP_MAIN_OID",
		"NOTE_DATE",
		"DESCRIPTION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_STRING
	 }; 

	public PstDocAppNote(){
	}

	public PstDocAppNote(int i) throws DBException { 
		super(new PstDocAppNote()); 
	}

	public PstDocAppNote(String sOid) throws DBException { 
		super(new PstDocAppNote(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstDocAppNote(long lOid) throws DBException { 
		super(new PstDocAppNote(0)); 
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
		return TBL_WF_DOC_APP_NOTE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstDocAppNote().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		DocAppNote docappnote = fetchExc(ent.getOID()); 
		ent = (Entity)docappnote; 
		return docappnote.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((DocAppNote) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((DocAppNote) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static DocAppNote fetchExc(long oid) throws DBException{ 
		try{ 
			DocAppNote docappnote = new DocAppNote();
			PstDocAppNote pstDocAppNote = new PstDocAppNote(oid); 
			docappnote.setOID(oid);

			docappnote.setDocappMainOid(pstDocAppNote.getlong(FLD_DOCAPP_MAIN_OID));
			docappnote.setNoteDate(pstDocAppNote.getDate(FLD_NOTE_DATE));
			docappnote.setDescription(pstDocAppNote.getString(FLD_DESCRIPTION));

			return docappnote; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppNote(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(DocAppNote docappnote) throws DBException{ 
		try{ 
			PstDocAppNote pstDocAppNote = new PstDocAppNote(0);

			pstDocAppNote.setLong(FLD_DOCAPP_MAIN_OID, docappnote.getDocappMainOid());
			pstDocAppNote.setDate(FLD_NOTE_DATE, docappnote.getNoteDate());
			pstDocAppNote.setString(FLD_DESCRIPTION, docappnote.getDescription());

			pstDocAppNote.insert(); 
			docappnote.setOID(pstDocAppNote.getlong(FLD_DOC_APP_NOTE_OID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppNote(0),DBException.UNKNOWN); 
		}
		return docappnote.getOID();
	}

	public static long updateExc(DocAppNote docappnote) throws DBException{ 
		try{ 
			if(docappnote.getOID() != 0){ 
				PstDocAppNote pstDocAppNote = new PstDocAppNote(docappnote.getOID());

				pstDocAppNote.setLong(FLD_DOCAPP_MAIN_OID, docappnote.getDocappMainOid());
				pstDocAppNote.setDate(FLD_NOTE_DATE, docappnote.getNoteDate());
				pstDocAppNote.setString(FLD_DESCRIPTION, docappnote.getDescription());

				pstDocAppNote.update(); 
				return docappnote.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppNote(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstDocAppNote pstDocAppNote = new PstDocAppNote(oid);
			pstDocAppNote.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstDocAppNote(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_WF_DOC_APP_NOTE; 
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
				DocAppNote docappnote = new DocAppNote();
				resultToObject(rs, docappnote);
				lists.add(docappnote);
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

	private static void resultToObject(ResultSet rs, DocAppNote docappnote){
		try{
			docappnote.setOID(rs.getLong(PstDocAppNote.fieldNames[PstDocAppNote.FLD_DOC_APP_NOTE_OID]));
			docappnote.setDocappMainOid(rs.getLong(PstDocAppNote.fieldNames[PstDocAppNote.FLD_DOCAPP_MAIN_OID]));
			docappnote.setNoteDate(rs.getDate(PstDocAppNote.fieldNames[PstDocAppNote.FLD_NOTE_DATE]));
			docappnote.setDescription(rs.getString(PstDocAppNote.fieldNames[PstDocAppNote.FLD_DESCRIPTION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long docAppNoteOid){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_WF_DOC_APP_NOTE + " WHERE " + 
						PstDocAppNote.fieldNames[PstDocAppNote.FLD_DOC_APP_NOTE_OID] + " = " + docAppNoteOid;

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
			String sql = "SELECT COUNT("+ PstDocAppNote.fieldNames[PstDocAppNote.FLD_DOC_APP_NOTE_OID] + ") FROM " + TBL_WF_DOC_APP_NOTE;
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
			  	   DocAppNote docappnote = (DocAppNote)list.get(ls);
				   if(oid == docappnote.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
}
