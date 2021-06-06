/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jun 3, 2005
 * Time: 3:42:22 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import java.util.Vector;

public class SessCategory {
    /**
     *
     * @param categoryId
     * @return
     */
    //public static final String nameCategory = "";
    public static String code = "";
    
    public static boolean readyDataToDelete(long categoryId) {
        boolean status = true;
        try {
            String where = PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + categoryId;
            Vector vlist = PstMaterial.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("SessCategory - readyDataToDelete : " + e.toString());
        }
        return status;
    }
    
    public static boolean readyDataToDeleteCheckInduk(long categoryId) {
        boolean status = true;
        try {
            String where = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID] + "=" + categoryId;
            Vector vlist = PstCategory.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("SessCategory - readyDataToDelete : " + e.toString());
        }
        return status;
    }
    
    
    public static String getCategoryName(long categoryId) {
        try {
            String where = PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "=" + categoryId;
            Vector vlist = PstCategory.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                 for(int i=0; i<vlist.size(); i++) {
                    Category category = (Category) vlist.get(i);
                    if(code.equals("")){
                        code = category.getName();
                    }else{
                        code = ""+category.getName()+" / "+ code;
                    }
                    if(category.getCatParentId()!=0){
                        getCategoryName(category.getCatParentId());
                    }else{
                        return code;
                    }
                 }
            }
        } catch (Exception e) {
            System.out.println("SessCategory - readyDataToDelete : " + e.toString());
        }
        return code;
    }

    public static String cleanCategoryName(long categoryId) {
        code="";
        return code;
    }
    
}
