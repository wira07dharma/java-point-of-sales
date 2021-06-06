/*
 * SessReportDistribution.java
 *
 * Created on March 4, 2004, 2:32 PM
 */

package com.dimata.posbo.session.warehouse;

import java.util.*;
import java.sql.*;
import com.dimata.posbo.db.*;
import com.dimata.util.*;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.common.entity.location.*;

/**
 *
 * @author  gadnyana
 */
public class SessReportDistribution {
    
    /** Creates a new instance of SessReportDistribution */
    public SessReportDistribution() {
    }
    
    /** gadnyana
     * untuk mencari report distribution yang berdasarkan invoice
     * @param srcMatReceive
     * @return
     */    
    public static Vector reportDistribution(SrcMatReceive srcMatReceive){
        DBResultSet dbrs = null;
        Vector list = new Vector(1,1);
        try{
            String sql = "SELECT "+
            " M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ // SKU
            " ,M."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ // NAME+
            " ,RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+ // QTY+
            " ,U."+PstUnit.fieldNames[PstUnit.FLD_CODE]+
            " ,L."+PstLocation.fieldNames[PstLocation.FLD_NAME]+" AS LOC_"+PstLocation.fieldNames[PstLocation.FLD_NAME]+ // NAME+
            " ,DM."+PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE]+ // DISTRIBUTION_DATE+
            " ,DD."+PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_QTY]+ // QTY+
            " AS DD_"+PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_QTY]+
            " FROM "+PstMatDistributionDetail.TBL_MAT_DISTRIBUTION_DETAIL+" AS DD "+ // DISTRIBUTION_DETAIL
            " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M "+ // MATERIAL
            " ON DD."+PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_MATERIAL_ID]+
            " = M."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ // MATERIAL_ID            
            " INNER JOIN "+PstCategory.TBL_CATEGORY+" AS C "+ // category
            " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+
            " = C."+PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+ // MATERIAL_ID            
            " INNER JOIN "+PstSubCategory.TBL_SUB_CATEGORY+" AS SC "+ // sub category
            " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]+
            " = M."+PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]+ // MATERIAL_ID            
            " INNER JOIN "+PstUnit.TBL_P2_UNIT+" AS U "+ // UNIT ID
            " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]+
            " = U."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]+ // UNIT_ID
            " INNER JOIN "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM+" AS RMI "+ // RECEIVE_MATERIAL_ITEM
            " ON M."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
            " = RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]+ // MATERIAL_ID
            " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS L ON "+ // LOCATION
            " DD."+PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_LOCATION_ID]+
            " = L."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+ // LOCATION_ID
            " INNER JOIN "+PstMatDistribution.TBL_DISTRIBUTION+" AS DM "+
            " ON DD."+PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_MATERIAL_ID]+ // DISTRIBUTION_MATERIAL_ID
            " = DM."+PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_MATERIAL_ID]+ // DISTRIBUTION_MATERIAL_ID
            " WHERE DM."+PstMatDistribution.fieldNames[PstMatDistribution.FLD_INVOICE_SUPPLIER]+
            " = '"+srcMatReceive.getInvoiceSupplier()+"'"+ 
            " AND DM."+PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE]+
            " BETWEEN '"+Formater.formatDate(srcMatReceive.getReceivefromdate(),"yyyy-MM-dd")+"' "+
            " AND '"+Formater.formatDate(srcMatReceive.getReceivetodate(),"yyyy-MM-dd")+"'"+
            " GROUP BY "+PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE]+
            ",M."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+",L."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+ // LOCATION_ID
            " ORDER BY C."+PstCategory.fieldNames[PstCategory.FLD_CODE]+
            " , SC."+PstSubCategory.fieldNames[PstSubCategory.FLD_CODE]+
            " , M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ 
            " , L."+PstLocation.fieldNames[PstLocation.FLD_CODE]+
            " , DM."+PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE];
            
            //System.out.println("==>> SQL DISTR : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()){
                Vector rowx = new Vector(1,1);
                Material material = new Material();
                Unit unit = new Unit();
                Location location = new Location();
                MatDistribution matDistribution = new MatDistribution();
                MatDistributionDetail matDistributionDetail = new MatDistributionDetail();
                MatReceiveItem matReceiveItem = new MatReceiveItem();
            
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                rowx.add(material);
                
                matReceiveItem.setQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
                rowx.add(matReceiveItem);
                
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                rowx.add(unit);
                
                location.setName(rs.getString("LOC_"+PstLocation.fieldNames[PstLocation.FLD_NAME]));
                rowx.add(location);
                
                matDistribution.setDistributionDate(rs.getDate(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE]));
                rowx.add(matDistribution);
                
                matDistributionDetail.setQty(rs.getDouble("DD_"+PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_QTY]));
                rowx.add(matDistributionDetail);
                
                list.add(rowx);
            }
            rs.close();
        }catch(Exception e){}finally{
            DBResultSet.close(dbrs);
        }
        return list;
    }
}
