

package com.dimata.pos.printman;

import java.io.Serializable;
import java.util.Vector;

/*
 * @author ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2003
 */

public class DSJ_PrintObj implements Serializable {
    
    public int getLeftMargin(){
        return leftMargin;
    }
    
    public void setLeftMargin(int leftMargin){
        this.leftMargin = leftMargin;
    }
    
    public int getRightMargin(){
        return rightMargin;
    }
    
    public void setRightMargin(int rightMargin){
        this.rightMargin = rightMargin;
    }
    
    public int getTopMargin(){
        return topMargin;
    }
    
    public Vector getLines() {
        return this.lines;
    }
    
    public void setTopMargin(int topMargin){
        this.topMargin = topMargin;
    }
    
    public int getBottomMargin(){
        return bottomMargin;
    }
    
    public void setBottomMargin(int bottomMargin){
        this.bottomMargin = bottomMargin;
    }
    
    public int setLines(Vector lines) {
        int count = 0;
        if(lines!=null&&lines.size()>0){
            count = lines.size();
        }
        return count;
    } 
    
    public void resetLines(Vector lns) {
        this.lines = new Vector();
        this.lines = lns;
    }
    
    public int addLine(String text) {
        if(lines==null)
            lines = new Vector(1,1);
        String str = "";
        //for(int i=0;i<intCpi;i++){
        //   str = str + " ";
        //}
        
        lines.add(str+text);
        return lines.size();
    }
    
    /** for create line lurus
     * @param text
     * @return
     */    
    public int addRptLine(String text) {
        String str = "";
        for(int i=0;i<getCharacterSelected();i++){
            str = str + text;
        }
        lines.add(str);
        return lines.size();
    }
    
    public int addLine(String text, int col) {
        String str = "";
        for(int i=0;i<col;i++){
            str += " ";
        }
        str = str+text;
        lines.add(str);
        return lines.size();
    }
    
    public void setLine(int index, String text) {
        if(lines==null)
            lines = new Vector(1,1);
        
        if(lines.size()< (index+1)){
            for(int i=lines.size(); i < (index+1); i++)
                addLine("");
        }
        lines.set(index, text);
        
    }
    
    public int addLine(String text, int col, int pos) {
        String str = "";
        int diff = 0;
        if(pos==TEXT_CENTER){
            diff = posTextCenter(col,text.length());
        }
        if(diff>0){
            for(int i=0;i<col;i++){
                str += " ";
            }
        }
        str = str+text;
        lines.add(str);
        return lines.size();
    }
    
    public int addLine(String text, int col, int pos, int cpi) {
        String str = "";
        int diff = 0;
        int diffCol = 0;
        if(cpi!=0&&cpi>col){
            diffCol = cpi - col;
        }
        
        if(pos==TEXT_CENTER){
            diff = posTextCenter(col,text.length(),cpi);
        }
        if(diff>0){
            for(int i=0;i<diff;i++){
                str += " ";
            }
        }
        str = str+text;
        if(diffCol>0){
            for(int i=0;i<diff;i++){
                str += " ";
            }
        }
        lines.add(str);
        return lines.size();
    }
    
    
    public int addLine(String textFirst, int colFirst, String textSecond, int colSecond) {
        String strFirst = "";
        String strSecond = "";
        String str = "";
        for(int i=0;i<colFirst;i++){
            strFirst += " ";
        }
        strFirst = strFirst+textFirst;
        
        for(int j=0;j<colSecond;j++){
            strSecond += " ";
        }
        strSecond = strSecond+textSecond;
        str = strFirst+strSecond;
        lines.add(str);
        return lines.size();
    }
    
    
    public int posTextCenter(int cols, int strLength){
        int colLength = 0;
        int textLength = 0;
        int diffLength = 0;
        if(cols>0&&strLength>0){
            colLength = cols/2;
            textLength = strLength/2;
            if(colLength>textLength){
                diffLength = colLength - textLength;
            }else{
                diffLength = 0;
            }
        }
        return diffLength;
    }
    
    public int posTextCenter(int cols, int strLength, int cpi){
        int colLength = 0;
        int textLength = 0;
        int diffLength = 0;
        int colDiff = 0;
        if(cpi!=0&&cpi>cols){
            colDiff = cpi - cols;
        }
        if(cols>0&&strLength>0&&colDiff>0){
            colLength = colDiff + cols/2;
            textLength = colDiff + strLength/2;
            if(colLength>textLength){
                diffLength = colLength - textLength;
            }else{
                diffLength = 0;
            }
        }
        return diffLength;
    }
    
    public int addLine(String textFirst, int colFirst, String textSecond, int colSecond, int pos) {
        String strFirst = "";
        String strSecond = "";
        String str = "";
        int diff = 0;
        if(pos==TEXT_CENTER){
            diff = posTextCenter(colFirst,textFirst.length());
        }
        if(diff>0){
            for(int i=0;i<diff;i++){
                strFirst += " ";
            }
        }
        strFirst = strFirst+textFirst;
        
        if(pos==TEXT_CENTER){
            diff = posTextCenter(colSecond,textSecond.length());
        }
        if(diff>0){
            for(int j=0;j<diff;j++){
                strSecond += " ";
            }
        }
        strSecond = strSecond+textSecond;
        str = strFirst+strSecond;
        lines.add(str);
        return lines.size();
    }
    
    public void setLine(int index, int col, String text, int maxLen) {
        String str="";
        if(text==null)
            str ="";
        
        if(text.length()>maxLen)
            str = text.substring(0, maxLen-1);
        else
            str = text;
        
        setLine(index,col,str);
    }
    
    
    public void setLine(int index, int col, String text) {
        if(lines==null)
            lines = new Vector(1,1);
        
        if(lines.size()< (index+1)){
            for(int i=lines.size(); i < (index+1); i++)
                addLine("");
        }
        
        String old = (String) ( (lines.get(index)==null) ? "": lines.get(index));
        String str ="";
        int dif= col - old.length();
        
        if(dif>0){
            // add space if old text is shorter then col to set
            for(int c=0; c<dif;c++)
                old=old+" ";
            str = old + text;
        }  else {
            int ltxt = text.length();
            int lold = old.length();
            
            if(lold > (col + ltxt)){
                String nible = old.substring(col+ltxt,  lold);
                str = old.substring(0,col) + text+ nible; // replace text from column / index = col and append nible
            }else {
                str = old.substring(0,col) + text; // replace text from column / index = col
            }
        }
        
        setLine(index, str);
    }
    
    public void setLineRptStr(int index, int col, String text, int strRepeat) {
        String str = text;
        if (text==null){
            text = "";
        }else{
            if( (strRepeat>1) && (text.length()>0)){
                for( int i=1;i<strRepeat;i++){
                    str= str + text;
                }
            }
        }
        setLine(index, col, str);
    }
    
    
    public void setLineRightAlignRptStr(int index, int col, String text, int strRepeat) {
        String str = text;
        if (text==null){
            text = "";
        }else{
            if( (strRepeat>1) && (text.length()>0)){
                for( int i=1;i<strRepeat;i++){
                    str= str + text;
                }
            }
        }
        setLineRightAlign(index, col, str) ;
    }
    
    private void setLineRightAlign(int index, int col, String text) {
        if(lines==null)
            lines = new Vector(1,1);
        
        if(lines.size()< (index+1)){
            for(int i=lines.size(); i < (index+1); i++)
                addLine("");
        }
        
        String old = (String) ( (lines.get(index)==null) ? "": lines.get(index));
        int ltxt = text.length();
        int lold = old.length();
        
        String str ="";
        
        int dif = col - lold - ltxt; // 74 - 96 - 9 = -7
        
        if(dif>0){
            // add space if old text is shorter then length txt to set
            for(int c=0; c<dif;c++)
                old=old+" ";
            str = old + text;
        }  else {
            if(lold > col){
                String nible = old.substring(col+ltxt,lold);
                str = old.substring(0,col) + text + nible; // replace text from column / index = col and append nible
            }else {
                if(lold<=0)
                    str = str + text;
                else
                    if((col-ltxt)>0)
                        str = old.substring(0,col-ltxt) + text; // replace text from column / index = col
                    else
                        str = old + text;
            }
        }
        int cnt = str.length();
        setLine(index, str);
    }
    
   /* public void setLineRightAlign(int index, int col, String text) {
        if(lines==null)
               lines = new Vector(1,1);
    
        if(lines.size()< (index+1)){
          for(int i=lines.size(); i < (index+1); i++)
            addLine("");
        }
    
        String old = (String) ( (lines.get(index)==null) ? "": lines.get(index));
        int ltxt = text.length();
        int lold = old.length();
    
                String str ="";
    
        int dif = col - lold - ltxt; // 74 - 96 - 9 = -7
    
        if(dif>0){
            // add space if old text is shorter then length txt to set
            for(int c=0; c<dif;c++)
                old=old+" ";
            str = old + text;
        }  else {
            if(lold > col){
              String nible = old.substring(col+1,lold);
              str = old.substring(0,col-ltxt+1);// + text + nible; // replace text from column / index = col and append nible
            }else {
              str = old.substring(0,col-ltxt) + text; // replace text from column / index = col
            }
        }
        setLine(index, str);
    }*/
    public int addHeight(int rows) {
        String str = "";
        for(int i=0;i<rows;i++){
            str += "\n";
        }
        lines.add(str);
        return lines.size();
    }
    
    public void setLineRightAlign(int index, int col, String text, int maxLen) {
        String str="";
        if(text==null)
            str ="";
        
        if(text.length()>maxLen)
            str = text.substring(0, maxLen);
        else{
            int cnt = maxLen - text.length();
            if(cnt>0){
                for(int i=0;i<cnt;i++)
                    str = str + " ";
                
                str = str + text;
            }else{
                str = text;
            }
        }
        
        setLineRightAlign(index,col,str);
    }
    
    public void setLineCenterAlign(int index, int colStart, int colWdth, String text) {
        String str = "";
        if (text!=null){
            if(colWdth<0)
                colWdth = -colWdth;
            int lnTxt= text.length();
            
            if(colWdth > lnTxt ){
                int colDif = (colWdth - lnTxt) / 2;
                for( int i=0;i<colDif;i++){
                    str= str + " ";
                }
                str=str+text;
            } else {
                if(colWdth<lnTxt){
                    str = text.substring(0,colWdth);
                } else
                    str=text;
            }
        }
        setLine(index, colStart, str) ;
    }
    
    public boolean isPaperFit(){
        if(lines.size()==this.getPageLength()){
            return true;
        }
        return false;
    }
    
    // set header if paper is not fit
    public void setHeader(int index){
        if(lines!=null && lines.size()>0){
            String str = (String) ( (lines.get(index)==null) ? "": lines.get(index));
            header.set(index,str);
        }
    }
    
    // set header if paper is not fit
    public void setHeader(int stIndex, int endIdx){
        if(lines!=null && lines.size()>0){
            int hd = header.size();
            for(int i=0; i < lines.size(); i++){
                if(( i >= stIndex) && (i <= endIdx)){
                    header.add("");
                    String str = (String) ( (lines.get(i)==null) ? "": lines.get(i));
                    header.set(hd,str);
                    hd++;
                }
            }
        }
    }
    
    public Vector getHeader(){
        return header;
    }
    
    //limit to use header
    public void endHeader(int index){
        this.indexEndHeader = index;
    }
    
    //limit to use header
    public int getIndexEndHeader(){
        return this.indexEndHeader;
    }
    
    public void setFont(int typeFont) {
        this.font = typeFont;
    }
    
    public int getFont() {return this.font;}
    
    public int getLineSpacing(){ return lineSpacing; }
    
    public void setLineSpacing(int lineSpacing){ this.lineSpacing = lineSpacing; }
    
    public int getPageLength(){ return pageLength; }
    
    public void setPageLength(int pageLength){ this.pageLength = pageLength; }
    
    public int getPageWidth(){ return pageWidth; }
    
    public void setPageWidth(int pageWidth){ this.pageWidth = pageWidth; }
    
    public String getObjDescription(){ return objDescription; }
    
    public void setObjDescription(String objDescription){ this.objDescription = objDescription; }
    
    public int getPrnIndex(){ return prnIndex; }
    
    public void setPrnIndex(int prnIndex){ this.prnIndex = prnIndex; }
    
    public int getCpiIndex(){ return cpiIndex; }
    
    public void setCpiIndex(int cpiIndex){ this.cpiIndex = cpiIndex; }
    
    public void setVerticalTab(int lineVerTab){ this.lineVtTab = lineVerTab; }
    
    //create new laine blank before add value from project
    public void newLine(int line, int widthCol){
        String str = "";
        for(int i =  0 ; i < widthCol; i++){
            str = str + " ";
        }
        setLine(line,0,str);
    }
    
    //create new laine blank before add value from project
    public void newColumn(int maxCol, String text){
        this.maxCol = maxCol;
        this.strBts = text;
        this.arrayCols = null;
        if(text==null)
            text = "";
        
        if(text.length()>0)
            text = text.substring(0, 1);
        
        int intCpi = getCharacterSelected();
        int minCols = getLeftMargin() + getRightMargin();
        int line = 0;
        if(lines!=null && lines.size()>0)
            line = lines.size()+1;
        else
            line = 0;
        
        newLine(line,intCpi);
        int chr = ((intCpi-minCols)/ maxCol);
        int intMnd = (intCpi-minCols) % maxCol;
        int start = getLeftMargin()-1;
        this.arrayCols = new float[maxCol+1];
        for (int k = 0; k <= maxCol; ++k) {
            if(k==maxCol){
                setLine(line,start+intMnd,text);
                arrayCols[k] = start+intMnd;
            }else{
                setLine(line,start+1,text);
                arrayCols[k] = start+1;
            }
            start = start + chr;
        }
        System.out.println(lines.get(line));
    }
    
    //create new laine blank before add value from project
    public void newColumn(int maxCol, String text, int[] cols){
        this.maxCol = maxCol;
        this.strBts = text;
        this.arrayCols = null;
        if(text.length()>0)
            text = text.substring(0, 1);
        
        int intCpi = getCharacterSelected();
        int minCols = getLeftMargin() + getRightMargin();
        
        int line = 0;
        if(lines!=null && lines.size()>0)
            line = lines.size();
        else
            line = 0;
        
        newLine(line,intCpi);
        int start = getLeftMargin()-1;
        this.arrayCols = new float[maxCol+1];
        for (int k = 0; k <= maxCol; ++k) {
            int cnt = 0;
            
            if(k==maxCol){
                setLine(line,(intCpi-getRightMargin()-1),text);
                arrayCols[k] = (intCpi - getRightMargin()-1);
            }else{
                setLine(line,start+1,text);
                cnt = cols[k];
                arrayCols[k] = start+1;
            }
            start = start + cnt;
        }
    }
    
    //create new laine blank before add value from project
    public void newColumn(int maxCol, String text, int[] cols, int line, int wdStart, int wdCols){
        this.maxCol = maxCol;
        this.strBts = text;
        this.arrayCols = null;
        if(text.length()>0)
            text = text.substring(0, 1);
        
        int intCpi = wdCols; //getCharacterSelected();
        int minCols = getLeftMargin(); // + getRightMargin();
        
        if(lines.size() < (line+1)){
            for(int i=lines.size(); i < (line+1); i++)
                newLine(line,intCpi);
        }
        int start = wdStart; //getLeftMargin()-1;
        this.arrayCols = new float[maxCol+1];
        for (int k = 0; k <= maxCol; ++k) {
            int cnt = 0;
            
            if(k==maxCol){
                setLine(line,(intCpi-1),text);
                arrayCols[k] = (intCpi -1);
            }else{
                setLine(line,start+1,text);
                cnt = cols[k];
                arrayCols[k] = start+1;
            }
            start = start + cnt;
        }
    }
    
    private int getTotalArray(int max, int[] cols){
        int cnt = 0;
        for (int k = 0; k <max; ++k){
            cnt = cnt + cols[k];
        }
        return cnt;
    }
    
    private void createSparator(int line){
        for(int i=0;i<=maxCol;i++ ){
            String val1 = String.valueOf(arrayCols[i]);
            int stCol = Integer.parseInt(val1.substring(0,val1.length()-2));
            setLine(line,stCol,strBts);
        }
    }
    
    private String createLines(String str, int wdStart){
        String str1 = str;
        for(int i=0;i<wdStart;i++ ){
            str = str + str1;
        }
        return str;
    }
    
    public void setColumnValueByline(int idx ,int line, String text, int pos){
        try{
            if(lines.size()>0){
                if(lines.size()< (line+1)){
                    for(int i=lines.size(); i < (line+1); i++){
                        addLine("");
                        //createSparator(line);
                    }
                }
                
                String val1 = String.valueOf(arrayCols[idx]);
                int stCol = Integer.parseInt(val1.substring(0,val1.length()-2));
                val1 = String.valueOf(arrayCols[idx+1]);
                int endCol = Integer.parseInt(val1.substring(0,val1.length()-2));
                endCol = endCol - stCol;
                
                text = createLines(text,endCol);
                
                switch(pos){
                    case TEXT_LEFT:
                        setLine(line,stCol,text,endCol+1);
                        break;
                    case TEXT_CENTER:
                        setLineCenterAlign(line,stCol,endCol+1,text);
                        break;
                    case TEXT_RIGHT:
                        setLineRightAlign(line,stCol, text, endCol);
                        break;
                    default:
                }
            }
        }catch(Exception e){
            System.out.println("ERR : setColumnValue "+e.toString());
        }
    }
    
    public void setColumnValue(int idx ,int line, String text, int pos){
        try{
            if(lines.size()>0){
                if(lines.size()< (line+1)){
                    for(int i=lines.size(); i < (line+1); i++){
                        addLine("");
                        createSparator(line);
                    }
                }else{
                    createSparator(line);
                }
                
                String val1 = String.valueOf(arrayCols[idx]);
                int stCol = Integer.parseInt(val1.substring(0,val1.length()-2));
                val1 = String.valueOf(arrayCols[idx+1]);
                int endCol = Integer.parseInt(val1.substring(0,val1.length()-2));
                endCol = endCol - stCol -1;
                
                switch(pos){
                    case TEXT_LEFT:
                        setLine(line,stCol+1,text,endCol+1);
                        break;
                    case TEXT_CENTER:
                        setLineCenterAlign(line,stCol+1,endCol+1,text);
                        break;
                    case TEXT_RIGHT:
                        setLineRightAlign(line,stCol+1, text, endCol);
                        break;
                    default:
                }
            }
        }catch(Exception e){
            System.out.println("ERR : setColumnValue "+e.toString());
        }
    }
    
    public void setColumnRepeatValue(int idx ,int line, String text, int pos){
        try{
            if(lines.size()>0){
                if(lines.size()< (line+1)){
                    for(int i=lines.size(); i < (line+1); i++){
                        addLine("");
                        createSparator(line);
                    }
                }else{
                    createSparator(line);
                }
                
                String val1 = String.valueOf(arrayCols[idx]);
                int stCol = Integer.parseInt(val1.substring(0,val1.length()-2));
                val1 = String.valueOf(arrayCols[idx+1]);
                int endCol = Integer.parseInt(val1.substring(0,val1.length()-2));
                endCol = endCol - stCol -1;
                
                String str = "";
                for(int k=0;k<stCol;k++){
                    str = str + text; 
                }
                text = str;
                
                switch(pos){
                    case TEXT_LEFT:
                        setLine(line,stCol+1,text,endCol+1);
                        break;
                    case TEXT_CENTER:
                        setLineCenterAlign(line,stCol+1,endCol+1,text);
                        break;
                    case TEXT_RIGHT:
                        setLineRightAlign(line,stCol+1, text, endCol);
                        break;
                    default:
                }
            }
        }catch(Exception e){
            System.out.println("ERR : setColumnValue "+e.toString());
        }
    }
    
    
    public int getCharacterSelected(){
        int intCpi = cpiChar[PRINTER_10_CPI];
        switch(getCpiIndex()){
            case PRINTER_10_CPI:
                intCpi = cpiChar[PRINTER_10_CPI];
                break;
            case PRINTER_12_CPI:
                intCpi = cpiChar[PRINTER_12_CPI];
                break;
            case PRINTER_CONDENSED_MODE:
                intCpi = cpiChar[PRINTER_CONDENSED_MODE];
                break;
            case PRINTER_40_COLUMN:
                intCpi = cpiChar[PRINTER_40_COLUMN];
                break;                
            default:
                intCpi = getCpiIndex();// jika diluar array yang ada jumlah column per line diset langsung dari setCpiIndex
                break;
                
        }
        return intCpi;
    }
    
    public int getSkipLineIsPaperFix(){ return skipLineIsPaperFix; }
    
    public void setSkipLineIsPaperFix(int skipLineIsPaperFix){ this.skipLineIsPaperFix = skipLineIsPaperFix; }
    
    public int getSpacing(){ return spacing; }
    public void setSpacing(int spacingx){ this.spacing= spacingx; }
    public int getVerticalSpacing(){ return lineVerticalSpacing; }
    public void setVerticalSpacing(int lineVtSpacing){ this.lineVerticalSpacing = lineVtSpacing; }
    public int getVerticalTab(){ return lineVtTab; }
    
    
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private Vector lines = new Vector(1,1);	//vector of String document has to print.
    private Vector header = new Vector(1,1);	//vector of String document has header to print.
    private int pageLength;
    private int pageWidth;
    private String objDescription;
    private int prnIndex;
    private int cpiIndex;  // 0= lowest, 1 ...
    private int font;
    private int indexEndHeader;
    private int skipLineIsPaperFix;
    
    
    // index of printer
    private int lineSpacing;
    private int lineVerticalSpacing;
    private int spacing = 0;
    private int lineVtTab;
    
    
    /* type font */
    public final static int ROMAN = 0;
    public final static int SANS_SERIF = 1;
    
    /* */
    public final static int LINE = 0;
    public final static int COORDINAT = 1;
    public final static int GRAPH = 2;
    
    public static final int PAPER_NORMAL = 0;
    public static final int PAPER_CONTINUE = 1;
    

    public static final int TEXT_LEFT = 0;
    public static final int TEXT_CENTER = 1;
    public static final int TEXT_RIGHT = 2;
    
    static float arrayCols[] = null;
    static int maxCol = 0;
    static String strBts = "";
    
    static int[] cpiChar = {80,163,137,40};// 96, 163
    public static final int PRINTER_10_CPI = 0; // 80 CHARACTER;
    public static final int PRINTER_12_CPI = 1; // 96 CHARACTER;
    public static final int PRINTER_CONDENSED_MODE = 2; //137 CHARACTER;
    public static final int PRINTER_40_COLUMN = 3; //40 CHARACTER;

    
    
}
