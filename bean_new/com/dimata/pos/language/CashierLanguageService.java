/*
 * CashierLanguageService.java
 *
 * Created on February 7, 2005, 5:45 PM
 */

package com.dimata.pos.language;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

/**
 *
 * @author  wpradnyana
 */
public class CashierLanguageService
{
    
    public static final int FROM_PROPERTIES=0;
    public static final int FROM_CLASS=1;
    public static String DICTIONARY_FILE = System.getProperty("java.home") + System.getProperty("file.separator") + "dimata" +
    System.getProperty("file.separator") + "cashier_dictionary";  
    /** Creates a new instance of CashierLanguageService */
    public CashierLanguageService (String moduleName,String localeName)
    {
         languages = getLanguages (); 
         defaultLocale = new Locale(localeName); 
         setDictionarySources (FROM_PROPERTIES); 
         String locale = getDefaultLocale ().getLanguage (); 
         DICTIONARY_FILE = DICTIONARY_FILE+"_"+moduleName+"_"+locale+".properties";
         
         switch(getDictionarySources ()){
            case FROM_PROPERTIES:
                try{
                    InputStream inStream = new FileInputStream(DICTIONARY_FILE); 
                    getDictionary ().load (inStream); 
                    
                }catch(Exception e){
                    e.printStackTrace ();  
                }
                 break;
             case FROM_CLASS:
                 break;
         }
    }
    private int dictionarySources = 0; 
    Hashtable languages = null;
    Properties dictionary = null; 
    Locale defaultLocale = null; 
    public static CashierLanguageService getInstance(String moduleName,String localeName) throws FileNotFoundException{
        return new CashierLanguageService(moduleName,localeName);  
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main (String[] args)
    {
        // TODO code application logic here
        CashierLanguageService service = null;
        try{
            service = CashierLanguageService.getInstance ("invoice","in");
            String credit = service.getDictionary ().getProperty ("invoice.title");
            System.out.println (credit); 
        }catch(Exception e){
            
        }
        
        System.exit (0); 
    }
    
    /**
     * Getter for property languages.
     * @return Value of property languages.
     */
    public Hashtable getLanguages()
    {
        if(languages==null){
            languages = new Hashtable(); 
        }
        return languages;
    }
    
    /**
     * Setter for property languages.
     * @param languages New value of property languages.
     */
    public void setLanguages(Hashtable languages)
    {
        this.languages = languages;
    }
    
    /**
     * Getter for property dictionarySources.
     * @return Value of property dictionarySources.
     */
    public int getDictionarySources ()
    {
        return dictionarySources;
    }
    
    /**
     * Setter for property dictionarySources.
     * @param dictionarySources New value of property dictionarySources.
     */
    public void setDictionarySources (int dictionarySources)
    {
        this.dictionarySources = dictionarySources;
    }
    
    /**
     * Getter for property properties.
     * @return Value of property properties.
     */
    public Properties getDictionary()
    {
        if(dictionary==null){
            dictionary= new Properties(); 
        }
        return dictionary;
    }
    
    /**
     * Setter for property properties.
     * @param properties New value of property properties.
     */
    public void setDictionary(Properties dictionary)
    {
        this.dictionary = dictionary;
    }
    
    /**
     * Getter for property defaultLocale.
     * @return Value of property defaultLocale.
     */
    public Locale getDefaultLocale()
    {
        if(defaultLocale==null){
            defaultLocale = new Locale("in"); 
        }
        return defaultLocale;
    }
    
    /**
     * Setter for property defaultLocale.
     * @param defaultLocale New value of property defaultLocale.
     */
    public void setDefaultLocale(Locale defaultLocale)
    {
        this.defaultLocale = defaultLocale;
    }
    
}
