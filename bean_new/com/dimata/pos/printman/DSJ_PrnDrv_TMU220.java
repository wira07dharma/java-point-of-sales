

package com.dimata.pos.printman;
/* 
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 */
public class DSJ_PrnDrv_TMU220 extends OpenPorts implements I_DSJ_PrinterDriver {
    
    public boolean initPrinter(PrnConfig prnConfig){
        OpenPorts.setDefaultPort(prnConfig.getPrnPort());
        try {
            if(parallelPort!=null){
                outputStream = parallelPort.getOutputStream();
                return true;
            }
            if(serialPort!=null){
                outputStream = serialPort.getOutputStream();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(" ECX in initPrinter : "+ e);
            return false;
        }
    }
    
    public void setPageWidth(int pgWidth){
    }
    
    public void setPageLength(int pgLength){
        try{
           // cmdESC(0x43);
            //outputStream.write(pgLength);
        }catch(Exception e){}
    }
    
    public void setLeftMargin(int lftMgn){
        try{
           // cmdESC(0x6C);
           // outputStream.write(lftMgn);
        }catch(Exception e){}
    }
    
    public void setRightMargin(int rghMgn){
        try{
            //cmdESC(0x51);
            //outputStream.write(rghMgn);
        }catch(Exception e){}
    }
    
    public void setTopMargin(int topMgn){
    }
    
    public void setButtomMargin(int buttomMgn){
    }
    
    
    
    public boolean println(String str){
        try{
            outputStream.write(str.getBytes());
            lineFeed();
            return true;
        }catch(Exception e){
            //System.out.println(" ! EXC : DSJ_PrnDrv_LX300 > println =  "+e.toString());
            return false;}
    }
    
    public void lineFeed(){
        try{
            outputStream.write(0x0A);
        }catch(Exception e){
            //System.out.println(" ! EXC : DSJ_PrnDrv_LX300 > lineFeed =  "+e.toString());
        }
    }
    
    synchronized public void endPrinting(){
        super.endPrint();
    }
    
    public void resetCPI(){
       // setCPI(0);
    }
    
    private void cancelCondensed(){
        try{
          //  cmdESC(0x12);
        } catch (Exception exc){
            //System.out.println(" ! EXC : DSJ_PrnDrv_LX300 > cancelCondensed =  "+exc.toString());
        }
        
    }
    
    
    public void setCPI(int level){
        try{
            /*
            int hex= 0x50;
            switch (level){
                case 0:
                    hex= 0x50;
                    cmdESC(hex);
                    break; // 10 cpi , 80 character
                    
                case 1:
                    hex= 0x4D;
                    cmdESC(hex);
                    break; // 12 cpi , 96 character
                    
                case 2:
                    hex= 0x0F;
                    outputStream.write(hex);
                    break; // condensed mode or 137 caracter / page
                    
                case 3:
                    hex= 0x0F;
                    cmdESC(hex);
                    break; // condensed mode ?
                    
                default:hex= 0x0F;
                
            }*/
        } catch (Exception exc){
            //System.out.println(" ! EXC : DSJ_PrnDrv_LX300 > setCPI =  "+exc.toString());
        }
        
    }
    
    public boolean isPrinterTimedOut(){
        try {
            if(parallelPort!=null){
                return parallelPort.isPrinterTimedOut();
            }
            if(serialPort!=null){
                return false; // to be implemented !!!1
            }
        } catch (Exception e) {
            //System.out.println("-------- : "+e.toString());
        }
        return true; // if any error then assume paper out
        
    }
    
    public boolean isPaperOut(){
        try {
            if(parallelPort!=null){
                return parallelPort.isPaperOut();
            }
            if(serialPort!=null){
                return false; // to be implemented !!!1
            }
        } catch (Exception e) {
            //System.out.println("-------- : "+e.toString());
        }
        return true; // if any error then assume paper out
        
    }
    
    private void cmdESC(int cmd ) throws Exception{
        byte ab = Byte.parseByte(""+cmd);
        byte[] cmdb = {27, ab};
        outputStream.write(cmdb);
    }
    
    private void cmdESC(int cmd1, int cmd2 ) throws Exception{
        byte ab1 = Byte.parseByte(""+cmd1);
        byte ab2 = Byte.parseByte(""+cmd2);
        byte[] cmdb = {27, ab1,ab2};
        outputStream.write(cmdb);
    }
    
    public void formFeed(){
        try{
            outputStream.write(0x0C);
        }catch(Exception e){
            //System.out.println(" ! EXC : DSJ_PrnDrv_LX300 > formFeed =  "+e.toString());
        }
    }
    
    public void setFont(int typeFont){
        try{
            byte ftn = Byte.parseByte(""+typeFont);
            byte[] bt = {0x6B, ftn };
            outputStream.write(bt);
        }catch(Exception e){
            //System.out.println(" ! EXC : DSJ_PrnDrv_LX300 > setFont =  "+e.toString());
        }
    }
    
    public void setVtSpacing(int level) {
        try{
           /* int hex = 0x30;
            switch (level){
                case 0:
                    hex= 0x30;
                    cmdESC(hex);
                    break; // 1 / 8 spacing
                    
                case 1:
                    hex= 0x31;
                    cmdESC(hex);
                    break; // 7 / 72 spacing
                    
                case 2:
                    hex= 0x32;
                    cmdESC(hex);
                    break; // 1 / 6 spacing 
                    
                default:hex= 0x30;
                
            }*/            
        }catch(Exception e){
            //System.out.println(" ! EXC : DSJ_PrnDrv_LX300 > setFont =  "+e.toString());
        }
    }

    public void setVtSpacing(int level, int n) {
        try{
            /*
            int hex = 0x33;
            switch(level){
                case 0:
                    hex= 0x33;
                    cmdESC(hex);
                    break; // n / 216 spacing
                case 1:
                    hex= 0x41;
                    cmdESC(hex);
                    break; // n / 216 spacing                    
            }
            
            byte ftn = Byte.parseByte(""+n);
            outputStream.write(ftn);*/
        }catch(Exception e){}
    }

    private byte getByte(String txt){
        if(txt!=null&&txt.length()>0){
            byte[] value = txt.getBytes();
            return value[0];
        }else{
            return 0;
        }
    }   
    
    public void setOpenCash ()
    {
        try{
            //cmdESC(0x70,0x30);
            byte cmdEsc = 27;//ESC
            byte txtP = getByte("p"); 
            byte cmd0 = (byte)0;
            byte cmd100 = (byte)100;
            byte cmd250 = (byte)250;
            byte[] cmd = {cmdEsc,txtP,cmd0,cmd100,cmd250};

            outputStream.write(cmd);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("err in open cash : "+e.toString());
        }
    }
    
    public static void main (String []args){
        //int a = 150;
        try{
            char a = 250;
            char b = 100;
            System.out.println (a);
            byte c = (byte)27;
            
            System.out.println (b);
            System.out.println (c);
        }catch(Exception e){
            e.printStackTrace ();
        }
        
    }
}
