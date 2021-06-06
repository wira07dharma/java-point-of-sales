
package com.dimata.system.entity;


import java.util.*;


public class SystemProperty extends com.dimata.qdep.entity.Entity
{
    
    private String name = "";
    private String value = "";
    
    
    /** value type: 
     * e.g. TEXT, INTEGER
     */
    private String valType = "";    

    
    /** display type : this type defind how this property will be displayed
     * to editing the data
     * e.g. SINGLE TEXT, MULTY TEXT, DROP DOWN. etc.
     */
    private String disType = "";    
    private String group = "";
    private String note = "";
    
    

    public SystemProperty()
    {
    }



    public String getName(){
        return name;
    }

    public void setName(String name){
        name = name.toUpperCase();        
        this.name = name.replace(' ','_');        
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValueType(){
        return valType;
    }

    public void setValueType(String type){
        this.valType = type;
    }

    
    public String getDisplayType(){
        return disType;
    }

    public void setDisplayType(String type){
        this.disType = type;
    }

    public String getGroup(){
        return group;
    }

    public void setGroup(String gr){
        this.group = gr;
    }

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }


} // end of SystemProperty
