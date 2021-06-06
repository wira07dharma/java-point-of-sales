/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Mar 4, 2006
 * Time: 12:57:24 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.excel.upload;

/**
 * class ini digunakan untuk upload excel catalog
 * dengan format standar
 */
public class UploadPosExcel {


    /**
     * fungsi untuk upload catalog
     * @param path
     * @return
     */
    public static int uploadCatalog(String path){
        String sql = "";
        try{

        }catch(Exception e){
            System.out.println("==== >>>> Err upload catalog : "+e.toString());
        }
        return 0;
    }

    /**
     * method untuk upload supplier
     * upload
     * @param path
     * @return
     */
    public static int uploadSupplier(String path){
        try{

        }catch(Exception e){}
        return 0;
    }

    /**
     * method untuk upload customer
     * @param path
     * @return
     */
    public static int uploadCustomer(String path){
        try{

        }catch(Exception e){}
        return 0;
    }
}
