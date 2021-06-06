package com.dimata.posbo.excel.upload;

import com.dimata.posbo.entity.warehouse.MatStockOpname;
import com.dimata.posbo.entity.warehouse.PstMatStockOpname;
import com.dimata.posbo.entity.warehouse.MatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem;

import java.util.Vector;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Nov 9, 2006
 * Time: 10:03:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadOpnameExcel {

    public static Vector uploadDataOpname(MatStockOpname matStockOpname, Vector vect){
        Vector vStatus = new Vector();
        try{
            if(vect!=null && vect.size()!=0){
                matStockOpname.setStockOpnameDate(new Date());
                long oidMain = PstMatStockOpname.insertExc(matStockOpname);
                for(int i=0;i<vect.size();i++){
                    System.out.println("******* inserting index "+i);
                    MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem)vect.get(i);
                    matStockOpnameItem.setStockOpnameId(oidMain);
                    try{
                        PstMatStockOpnameItem.insertExc(matStockOpnameItem);
                    }catch(Exception e){
                        vStatus.add(matStockOpnameItem);
                    }
                }
                PstMatStockOpname.StockCorrection(oidMain,matStockOpname.getLocationId());
            }
        }catch(Exception e){
            System.out.println("==>>>> error proses insert data opname");
        }
        return vStatus;
    }

    public static Vector uploadDataOpnameCheckTime(MatStockOpname matStockOpname, Vector vect){
        Vector vStatus = new Vector();
        try{
            if(vect!=null && vect.size()!=0){
                //matStockOpname.setStockOpnameDate(new Date());
                long oidMain = PstMatStockOpname.insertExc(matStockOpname);
                for(int i=0;i<vect.size();i++){
                    System.out.println("******* inserting index "+i);
                    MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem)vect.get(i);
                    matStockOpnameItem.setStockOpnameId(oidMain);
                    try{
                        PstMatStockOpnameItem.insertExc(matStockOpnameItem);
                    }catch(Exception e){
                        vStatus.add(matStockOpnameItem);
                    }
                }
                PstMatStockOpname.StockCorrection(oidMain,matStockOpname.getLocationId());
            }
        }catch(Exception e){
            System.out.println("==>>>> error proses insert data opname");
        }
        return vStatus;
    }

}
