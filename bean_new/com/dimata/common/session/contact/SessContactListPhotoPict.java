/* 
 * Session Name  	:  SessContactListPhotoPict.java */
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.session.contact;

/* java package */ 
import java.sql.*;
import com.dimata.util.blob.*;

/* qdep package */ 
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;




/* project package */
import com.dimata.common.entity.contact.*;
import com.dimata.common.session.system.*; 

public class SessContactListPhotoPict{
	public static final String SESKEY_CONTACTLISTPHOTO_PICT 	= "CONTACTLISTPHOTO_OID";
	private static final String FILE_SEPARATOR 			= System.getProperty("file.separator");
	private static String IMGCACHE_RELPATH           	= "imgcache" + FILE_SEPARATOR;
	private static String IMGCACHE_ABSPATH     	= SessSystemProperty.PROP_IMGCACHE;
	private static final String IMG_PREFIX          	= "contactListPhoto.";

	public SessContactListPhotoPict() {
	}

	 public static int updateImage(Object obj, long oid) {
		 DBResultSet dbrs = null;
		 try { 
			 if(obj == null) return -1;
			 PreparedStatement pstmt =  null;
			 byte b[] = null;
			 b = (byte[])obj;

			 String sql = "UPDATE " + PstContactListPhoto.TBL_P2_CONTACT_LIST_PHOTO +" SET "+
							PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_PHOTO] +" = ? WHERE "+
							PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID] + " = ?";

             System.out.println("oid : "+oid);
			 System.out.println(sql);
			 dbrs = DBHandler.getPSTMTConnection(sql);
			 pstmt = dbrs.getPreparedStatement();
			 pstmt.setBytes(1, b);
			 pstmt.setLong(2, oid);
			 DBHandler.execUpdatePreparedStatement(pstmt);
			 ImageLoader.deleteChace(IMGCACHE_ABSPATH + IMG_PREFIX + oid);

		 }catch(Exception e) {
			 System.out.println(e.toString());
		 }finally{
			 DBResultSet.closePstmt(dbrs); 
		 }
		 return 0; 
	 }

	 public static int deleteImage(long oid) {
		 boolean exist = false; 
		 try{
			 String sql = " UPDATE "+ PstContactListPhoto.TBL_P2_CONTACT_LIST_PHOTO + " SET " +
			    		PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_PHOTO] + " = '' "+
			           "WHERE "+ PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID] + " = '" + oid + "'";
			 //System.out.println(sql);
			 DBHandler.execUpdate(sql);
			 ImageLoader.deleteChace(IMGCACHE_ABSPATH + IMG_PREFIX + oid);

			 return FRMMessage.MSG_DELETED;
		 }catch(Exception e) {
		 	System.out.println(e.toString());
		 }
		 return -1;

	 }

	 public static String fetchImage(long oid) {
		 int resCode = -1;
		 String absImgPath = IMGCACHE_ABSPATH + IMG_PREFIX + oid;
		 java.io.File flImg = new java.io.File(absImgPath);
		 if(flImg.exists()){
			 System.out.println("....."+IMGCACHE_RELPATH + IMG_PREFIX + oid);
			 return IMGCACHE_RELPATH + IMG_PREFIX + oid;
		 }
		 if(queryImage(oid) == 0){
			 System.out.println("....."+IMGCACHE_RELPATH + IMG_PREFIX + oid);
			 return IMGCACHE_RELPATH + IMG_PREFIX + oid;
		 }else
			 return "";

	 }

	 private static int queryImage(long oid) {
                DBResultSet dbrs = null;
		 try{
			 String sql = " SELECT " + PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_PHOTO] +
			          " FROM " + PstContactListPhoto.TBL_P2_CONTACT_LIST_PHOTO +
			          " WHERE "+ PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID] + " = " + oid ;

			 //System.out.println(sql);
			 //ResultSet rs = DBHandler.execQuery(sql);
                         dbrs = DBHandler.execQueryResult(sql);
                         ResultSet rs = dbrs.getResultSet();
			 java.io.InputStream ins = null;
			 while(rs.next()) {
				 ins = rs.getBinaryStream(1); 
				 return ImageLoader.writeCache(ins, IMGCACHE_ABSPATH + IMG_PREFIX + oid, true);
			 }
                         rs.close();
		 }catch(Exception e) {
			 System.out.println(e.toString());
		 }finally {
                        DBResultSet.close(dbrs);
                 }
		 return -1;

	 }
}
