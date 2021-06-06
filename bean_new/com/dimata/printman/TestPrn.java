

package com.dimata.printman;

import java.util.Vector;

/* 
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 */


public class TestPrn {
            public static String COL_SEPARATOR = "|";
            final static int PI_COL_START  = 1 ;
            final static int PI_COL_WIDTH_BAR_CODE = 12;
            final static int PI_COL_WIDTH_DESC	  = 16;
            final static int PI_COL_WIDTH_COLOR	  =  3;
            final static int PI_COL_WIDTH_SIZE_TYPE = 1;
            final static int PI_COL_WIDTH_SIZE	  =  3;
            final static int PI_COL_WIDTH_QTY	  =  4;
            final static int PI_COL_WIDTH_PRICE	  = 9;
            final static int PI_COL_WIDTH_DISC_ITM =  0;
            final static int PI_COL_WIDTH_AMOUNT   = 10;

            final static int PI_COL_START_BAR_CODE = PI_COL_START;
            final static int PI_COL_START_DESC	  = PI_COL_START_BAR_CODE + PI_COL_WIDTH_BAR_CODE +1;
            final static int PI_COL_START_COLOR	  =  PI_COL_START_DESC +PI_COL_WIDTH_DESC +1;
            final static int PI_COL_START_SIZE_TYPE = PI_COL_START_COLOR +  PI_COL_WIDTH_COLOR +1;
            final static int PI_COL_START_SIZE	  =  PI_COL_START_SIZE_TYPE + PI_COL_WIDTH_SIZE_TYPE+1;
            static int PI_COL_START_QTY	  =  PI_COL_START_SIZE;
            static int PI_COL_START_PRICE	  = PI_COL_START_QTY + PI_COL_WIDTH_QTY +1;
            static int PI_COL_START_DISC_ITM =  PI_COL_START_PRICE + PI_COL_WIDTH_PRICE+1 ;
            static int PI_COL_START_AMOUNT   = PI_COL_START_DISC_ITM + PI_COL_WIDTH_DISC_ITM+1;

            static int PI_MAX_WIDTH =   PI_COL_START_AMOUNT +  PI_COL_WIDTH_AMOUNT;

	public static void updatePI_COLS(int maxSize){
            PI_COL_START_QTY	  =  PI_COL_START_SIZE + (PI_COL_WIDTH_SIZE +1)* maxSize ;
            PI_COL_START_PRICE	  = PI_COL_START_QTY + PI_COL_WIDTH_QTY +1;
            if(PI_COL_START_DISC_ITM==0)
		            PI_COL_START_DISC_ITM =  PI_COL_START_PRICE;
                else
		            PI_COL_START_DISC_ITM =  PI_COL_START_PRICE + PI_COL_WIDTH_PRICE +1;
            PI_COL_START_AMOUNT   = PI_COL_START_DISC_ITM + PI_COL_WIDTH_DISC_ITM +1;
            PI_MAX_WIDTH =   PI_COL_START_AMOUNT +  PI_COL_WIDTH_AMOUNT;
	}


    public static void main(String[] argv){
                Vector vct = DSJ_PrinterXML.getListPrinter();
                
                System.out.println(vct);
                if(0==0)
                    return;
        
   		DSJ_PrintObj obj= new DSJ_PrintObj();
	 	try{
	        DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
//     		DSJ_PrintObj obj= new DSJ_PrintObj();
            String str="0123456789";
            System.out.println(str.substring(3,5));

            obj.setPrnIndex(1);
            obj.setPageLength(30);
            obj.setTopMargin(1);
            obj.setLeftMargin(0);
            obj.setRightMargin(0);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(2); // 12 CPI = 96 char /line

			//int maxNumOfSize = 8; // get from sizes
		   // updatePI_COLS(getMaxSize(getSizeTypes())) ;
            // Proforma Invoice

            //System.out.println("Max PI=" + PI_MAX_WIDTH) ;

            //obj.setLine(0,40, "PROFORMA INVOICE");
            //obj.setLine(1,2, "No. 08080-PI / Tgl. 1 June 2003");
            //obj.setLine(1,60, "Client : Planet Surf");
           // setItemSizeHd(obj,2);
            //set items
            /*obj.setLineRptStr(0,PI_COL_START_BAR_CODE-1, "-",137);// line ke 2
            int[] intCols = {13,40,5,2,40,5,13,15};
            obj.newColumn(8,"|",intCols);
            obj.setColumnValue(4,1,"QTY",obj.TEXT_CENTER);

            int[] intCls = {13,40,5,2,5,5,5,5,5,5,5,5,5,13,15};
            obj.newColumn(15,"|",intCls);
            obj.setColumnValue(0,2,"CODE",obj.TEXT_CENTER);
            obj.setColumnValue(1,2,"DESCR",obj.TEXT_CENTER);
			obj.setColumnValue(2,2,"CLR",obj.TEXT_CENTER);
            obj.setColumnValue(3,2,"",obj.TEXT_CENTER);
            obj.setColumnValue(4,2,"XS",obj.TEXT_CENTER);
            obj.setColumnValue(5,2,"S",obj.TEXT_RIGHT);
            obj.setColumnValue(6,2,"M",obj.TEXT_CENTER);
            obj.setColumnValue(7,2,"L",obj.TEXT_CENTER);
            obj.setColumnValue(8,2,"XL",obj.TEXT_CENTER);
            obj.setColumnValue(9,2,"XXL",obj.TEXT_CENTER);
            obj.setColumnValue(10,2,"",obj.TEXT_CENTER);
            obj.setColumnValue(11,2,"",obj.TEXT_CENTER);
            obj.setColumnValue(12,2,"#QTY",obj.TEXT_CENTER);
            obj.setColumnValue(13,2,"PRICE",obj.TEXT_CENTER);
            obj.setColumnValue(14,2,"AMOUNT",obj.TEXT_CENTER); */


            int[] intColx = {20,20};
            obj.newColumn(2,"|",intColx,0,0,40);
            obj.setColumnValueByline(0,0,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,0,"-",obj.TEXT_CENTER);
            obj.setColumnValue(0,1,"DATE",obj.TEXT_CENTER);
            obj.setColumnValue(1,1,"AMOUNT",obj.TEXT_CENTER);
            obj.setColumnValueByline(0,2,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,2,"-",obj.TEXT_CENTER);

            int[] intClx = {20,20};
            obj.newColumn(2,"|",intClx,0,42,82);
            obj.setColumnValueByline(0,0,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,0,"-",obj.TEXT_CENTER);
            obj.setColumnValue(0,1,"DATE",obj.TEXT_CENTER);
            obj.setColumnValue(1,1,"AMOUNT",obj.TEXT_CENTER);
            obj.setColumnValueByline(0,2,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,2,"-",obj.TEXT_CENTER);

            /*for(int l=0;l<36;l++){
                String line =""+l+ " " ;
	        	obj.addLine(line);
            }*/

	        System.out.println("Start Printing");
                
	        prnSvc.print(obj);
	        prnSvc.running = true;
                prnSvc.run_x();
                 
                
                //RemotePrintTarget prt = new RemotePrintTarget();
                //prt.printObj(obj);
            //Thread thr = new Thread(prnSvc);
            //thr.setDaemon(false);
            //thr.start();


            System.out.println("");
	        System.out.println("Press any key to end");
	        int i = 0;//System.in.read();
	        prnSvc.running=false;
	        System.out.println("Bye");
     } catch (Exception exc){
        System.out.println("Exc : "+ exc);
     }
    }

    public static DSJ_PrintObj getTestObj(){
   		DSJ_PrintObj obj= new DSJ_PrintObj();
	 	try{
            String str="0123456789";
            System.out.println(str.substring(3,5));

            obj.setPrnIndex(1);
            obj.setPageLength(30);
            obj.setTopMargin(1);
            obj.setLeftMargin(0);
            obj.setRightMargin(0);
            obj.setObjDescription("TEST A");
            obj.setCpiIndex(2); // 12 CPI = 96 char /line

			//int maxNumOfSize = 8; // get from sizes
		   // updatePI_COLS(getMaxSize(getSizeTypes())) ;
            // Proforma Invoice

            //System.out.println("Max PI=" + PI_MAX_WIDTH) ;

            //obj.setLine(0,40, "PROFORMA INVOICE");
            //obj.setLine(1,2, "No. 08080-PI / Tgl. 1 June 2003");
            //obj.setLine(1,60, "Client : Planet Surf");
           // setItemSizeHd(obj,2);
            //set items
            /*obj.setLineRptStr(0,PI_COL_START_BAR_CODE-1, "-",137);// line ke 2
            int[] intCols = {13,40,5,2,40,5,13,15};
            obj.newColumn(8,"|",intCols);
            obj.setColumnValue(4,1,"QTY",obj.TEXT_CENTER);

            int[] intCls = {13,40,5,2,5,5,5,5,5,5,5,5,5,13,15};
            obj.newColumn(15,"|",intCls);
            obj.setColumnValue(0,2,"CODE",obj.TEXT_CENTER);
            obj.setColumnValue(1,2,"DESCR",obj.TEXT_CENTER);
			obj.setColumnValue(2,2,"CLR",obj.TEXT_CENTER);
            obj.setColumnValue(3,2,"",obj.TEXT_CENTER);
            obj.setColumnValue(4,2,"XS",obj.TEXT_CENTER);
            obj.setColumnValue(5,2,"S",obj.TEXT_RIGHT);
            obj.setColumnValue(6,2,"M",obj.TEXT_CENTER);
            obj.setColumnValue(7,2,"L",obj.TEXT_CENTER);
            obj.setColumnValue(8,2,"XL",obj.TEXT_CENTER);
            obj.setColumnValue(9,2,"XXL",obj.TEXT_CENTER);
            obj.setColumnValue(10,2,"",obj.TEXT_CENTER);
            obj.setColumnValue(11,2,"",obj.TEXT_CENTER);
            obj.setColumnValue(12,2,"#QTY",obj.TEXT_CENTER);
            obj.setColumnValue(13,2,"PRICE",obj.TEXT_CENTER);
            obj.setColumnValue(14,2,"AMOUNT",obj.TEXT_CENTER); */


            int[] intColx = {20,20};
            obj.newColumn(2,"|",intColx,0,0,40);
            obj.setColumnValueByline(0,0,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,0,"-",obj.TEXT_CENTER);
            obj.setColumnValue(0,1,"DATE",obj.TEXT_CENTER);
            obj.setColumnValue(1,1,"AMOUNT",obj.TEXT_CENTER);
            obj.setColumnValueByline(0,2,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,2,"-",obj.TEXT_CENTER);

            int[] intClx = {20,20};
            obj.newColumn(2,"|",intClx,0,42,82);
            obj.setColumnValueByline(0,0,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,0,"-",obj.TEXT_CENTER);
            obj.setColumnValue(0,1,"DATE",obj.TEXT_CENTER);
            obj.setColumnValue(1,1,"AMOUNT",obj.TEXT_CENTER);
            obj.setColumnValueByline(0,2,"-",obj.TEXT_CENTER);
            obj.setColumnValueByline(1,2,"-",obj.TEXT_CENTER);

	    return obj;
     } catch (Exception exc){
        System.out.println("Exc : "+ exc);
     }
     return obj;
    }


    
public static void setItemSeparate(DSJ_PrintObj obj, int line, int maxSize){
            obj.setLine(line,PI_COL_START_BAR_CODE-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_DESC-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_COLOR-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_SIZE_TYPE-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_SIZE-1,COL_SEPARATOR);
            for(int i =  0 ; i <  maxSize; i++){
            	obj.setLine(line,PI_COL_START_SIZE+i*(PI_COL_WIDTH_SIZE+1)-1,COL_SEPARATOR);
            }
            obj.setLine(line,PI_COL_START_QTY-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_PRICE-1,COL_SEPARATOR);
            if(PI_COL_WIDTH_DISC_ITM!=0)
	            obj.setLine(line,PI_COL_START_DISC_ITM-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_AMOUNT-1,COL_SEPARATOR);
            obj.setLine(line,PI_MAX_WIDTH,COL_SEPARATOR);
    }

    public static Vector getSizeTypes(){
        Vector sizeTypes = new Vector(1,1);
        Vector aType = new Vector(1,1);
        aType.add("XS");aType.add("S");aType.add("M");aType.add("L");aType.add("XL");aType.add("XXL");
        sizeTypes.add(aType);
        aType = new Vector(1,1);
        aType.add("26");aType.add("28");aType.add("30");aType.add("32");aType.add("34");aType.add("36");;aType.add("38");;aType.add("40");
        sizeTypes.add(aType);

        return sizeTypes;
    }

    public static int getMaxSize(Vector sizeTypes){
        int maxSize =0;
        for(int i =  0 ; i <  sizeTypes.size(); i++){
            Vector ast = (Vector) sizeTypes.get(i);
            int lng = ast.size();
            if(lng>maxSize)
                maxSize= lng;
        }
        return maxSize;
    }

    public static void setItemSizeLn(DSJ_PrintObj obj, int line, String sizeTypeCode, Vector sizes){
            obj.setLine(line,PI_COL_START_SIZE_TYPE,sizeTypeCode);
            for(int i =  0 ; i <  sizes.size(); i++){
            	obj.setLine(line,PI_COL_START_SIZE+i*(PI_COL_WIDTH_SIZE+1), (String)sizes.get(i));
            }
    }


    public static void setItemSizeHd(DSJ_PrintObj obj, int line){

            obj.setLineRptStr(line,PI_COL_START_BAR_CODE-1, "-",PI_MAX_WIDTH+1);// line ke 2
            line=line+1;
            obj.setLine(line,PI_COL_START_BAR_CODE-1,COL_SEPARATOR); // line ke 3
            obj.setLine(line,PI_COL_START_DESC-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_COLOR-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_SIZE_TYPE-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_SIZE-1,COL_SEPARATOR);
            obj.setLineCenterAlign(line,PI_COL_START_SIZE,PI_COL_START_QTY-1-PI_COL_START_SIZE,"QTY");
            obj.setLine(line,PI_COL_START_QTY-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_PRICE-1,COL_SEPARATOR);
            if(PI_COL_WIDTH_DISC_ITM!=0)
	            obj.setLine(line,PI_COL_START_DISC_ITM-1,COL_SEPARATOR);
            obj.setLine(line,PI_COL_START_AMOUNT-1,COL_SEPARATOR);
            obj.setLine(line,PI_MAX_WIDTH,COL_SEPARATOR);

        Vector sizeTypes =  getSizeTypes();
        int maxSize =getMaxSize(sizeTypes);

        for(int i =  0 ; i <  sizeTypes.size(); i++){
			setItemSeparate(obj, line+1+i, maxSize);
            Vector ast = (Vector) sizeTypes.get(i);
			setItemSizeLn(obj, line+i+1, ""+(i+1), ast );
        }

        line++;
            obj.setLineCenterAlign(line,PI_COL_START_BAR_CODE,PI_COL_WIDTH_BAR_CODE,"CODE");
            obj.setLineCenterAlign(line,PI_COL_START_DESC,PI_COL_WIDTH_DESC,"DESCR");
            obj.setLineCenterAlign(line,PI_COL_START_COLOR,PI_COL_WIDTH_COLOR,"CLR");
            obj.setLineCenterAlign(line,PI_COL_START_QTY,PI_COL_WIDTH_QTY,"#QTY");
            obj.setLineCenterAlign(line,PI_COL_START_PRICE,PI_COL_WIDTH_PRICE,"PRICE");
            if(PI_COL_WIDTH_DISC_ITM!=0)
	            obj.setLineCenterAlign(line,PI_COL_START_DISC_ITM,PI_COL_WIDTH_DISC_ITM,"DISC");
            obj.setLineCenterAlign(line,PI_COL_START_AMOUNT,PI_COL_WIDTH_AMOUNT,"AMOUNT");

    }

}
