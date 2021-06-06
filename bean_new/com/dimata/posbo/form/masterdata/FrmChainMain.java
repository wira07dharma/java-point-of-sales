/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.ChainMain;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author IanRizky
 */
public class FrmChainMain  extends FRMHandler implements I_FRMInterface, I_FRMType {

	private ChainMain entChainMain;
	public static final String FRM_NAME_CHAINMAIN = "FRM_NAME_CHAINMAIN";
	public static final int FRM_FIELD_CHAIN_MAIN_ID = 0;
	public static final int FRM_FIELD_CHAIN_LOCATION = 1;
	public static final int FRM_FIELD_CHAIN_DATE = 2;
	public static final int FRM_FIELD_CHAIN_NOTE = 3;
	public static final int FRM_FIELD_CHAIN_STATUS = 4;
	public static final int FRM_FIELD_CHAIN_TITLE = 5;

	public static String[] fieldNames = {
		"FRM_FIELD_CHAIN_MAIN_ID",
		"FRM_FIELD_CHAIN_LOCATION",
		"FRM_FIELD_CHAIN_DATE",
		"FRM_FIELD_CHAIN_NOTE",
		"FRM_FIELD_CHAIN_STATUS",
		"FRM_FIELD_CHAIN_TITLE"
	};

	public static int[] fieldTypes = {
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT,
		TYPE_STRING
	};

	public FrmChainMain() {
	}

	public FrmChainMain(ChainMain entChainMain) {
		this.entChainMain = entChainMain;
	}

	public FrmChainMain(HttpServletRequest request, ChainMain entChainMain) {
		super(new FrmChainMain(entChainMain), request);
		this.entChainMain = entChainMain;
	}

	public String getFormName() {
		return FRM_NAME_CHAINMAIN;
	}

	public int[] getFieldTypes() {
		return fieldTypes;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public int getFieldSize() {
		return fieldNames.length;
	}

	public ChainMain getEntityObject() {
		return entChainMain;
	}

	public void requestEntityObject(ChainMain entChainMain) {
		try {
			this.requestParam();
			entChainMain.setChainLocation(getLong(FRM_FIELD_CHAIN_LOCATION));
			entChainMain.setChainDate(Formater.formatDate(getString(FRM_FIELD_CHAIN_DATE), "yyyy-MM-dd"));
			entChainMain.setChainNote(getString(FRM_FIELD_CHAIN_NOTE));
			entChainMain.setChainStatus(getInt(FRM_FIELD_CHAIN_STATUS));
			entChainMain.setChainTitle(getString(FRM_FIELD_CHAIN_TITLE));
		} catch (Exception e) {
			System.out.println("Error on requestEntityObject : " + e.toString());
		}
	}

}
