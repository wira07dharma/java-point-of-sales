/**
 * Created on 	: 3:00 PM
 * @author	    : gedhy
 * @version	    : 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.workflow.entity.approval; 

/* package qdep */
import com.dimata.qdep.entity.*;

public class AppMapping extends Entity { 

	private String appTitle = "";
	private long appTypeOid;
	private int docTypeType;
	private long departmentOid;
	private long positionOid;
	private long sectionId;
    private int appIndex;
    private long appConditionOid;
    private long docStatusOidBefore;
    private long docStatusOidAfter;

	public String getAppTitle(){
		return appTitle;
	} 

	public void setAppTitle(String appTitle){
		this.appTitle = appTitle;
	} 

	public long getAppTypeOid(){
		return appTypeOid;
	} 

	public void setAppTypeOid(long appTypeOid){
		this.appTypeOid = appTypeOid;
	} 

	public int getDocTypeType(){
		return docTypeType;
	} 

	public void setDocTypeType(int docTypeType){
		this.docTypeType = docTypeType;
	} 

	public long getDepartmentOid(){ 
		return departmentOid; 
	} 

	public void setDepartmentOid(long departmentOid){ 
		this.departmentOid = departmentOid; 
	} 

	public long getPositionOid(){ 
		return positionOid; 
	} 

	public void setPositionOid(long positionOid){ 
		this.positionOid = positionOid; 
	} 

	public long getSectionId(){ 
		return sectionId; 
	} 

	public void setSectionId(long sectionId){ 
		this.sectionId = sectionId; 
	} 

	public int getAppIndex(){
		return appIndex;
	} 

	public void setAppIndex(int appIndex){
		this.appIndex = appIndex;
	} 

	public long getAppConditionOid(){
		return appConditionOid;
	} 

	public void setAppConditionOid(long appConditionOid){
		this.appConditionOid = appConditionOid;
	} 

    public long getDocStatusOidBefore(){ return docStatusOidBefore; }

    public void setDocStatusOidBefore(long docStatusOidBefore){ this.docStatusOidBefore = docStatusOidBefore; }

    public long getDocStatusOidAfter(){ return docStatusOidAfter; }

    public void setDocStatusOidAfter(long docStatusOidAfter){ this.docStatusOidAfter = docStatusOidAfter; }
}
