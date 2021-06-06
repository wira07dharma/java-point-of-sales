/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.ChainAddCost;
import com.dimata.posbo.entity.masterdata.ChainMainMaterial;
import com.dimata.posbo.entity.masterdata.ChainPeriod;
import com.dimata.posbo.entity.masterdata.PstChainAddCost;
import com.dimata.posbo.entity.masterdata.PstChainMainMaterial;
import com.dimata.posbo.entity.masterdata.PstChainPeriod;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 *
 * @author IanRizky
 */
public class CtrlChainMainMaterial extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };
    private int start;
    private String msgString;
    private ChainMainMaterial entChainMainMaterial;
    private PstChainMainMaterial pstChainMainMaterial;
    private FrmChainMainMaterial frmChainMainMaterial;
    int language = LANGUAGE_DEFAULT;

    public CtrlChainMainMaterial(HttpServletRequest request) {
        msgString = "";
        entChainMainMaterial = new ChainMainMaterial();
        try {
            pstChainMainMaterial = new PstChainMainMaterial(0);
        } catch (Exception e) {;
        }
        frmChainMainMaterial = new FrmChainMainMaterial(request, entChainMainMaterial);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmChainMainMaterial.addError(frmChainMainMaterial.FRM_FIELD_CHAIN_MAIN_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public ChainMainMaterial getChainMainMaterial() {
        return entChainMainMaterial;
    }

    public FrmChainMainMaterial getForm() {
        return frmChainMainMaterial;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidChainMainMaterial) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidChainMainMaterial != 0) {
                    try {
                        entChainMainMaterial = PstChainMainMaterial.fetchExc(oidChainMainMaterial);
                    } catch (Exception exc) {
                    }
                }

                frmChainMainMaterial.requestEntityObject(entChainMainMaterial);

                if (frmChainMainMaterial.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                //CEK APAKAH ADA DISTRIBUSI UNTUK PERIODE SELANJUTNYA
                if (this.entChainMainMaterial.getMaterialType() == PstChainMainMaterial.TYPE_NEXT_COST) {
                    if (oidChainMainMaterial == 0) {
                        //CEK APAKAH SUDAH ADA ITEM DISTRIBUSI DI PERIODE YG SAMA
                        int count = PstChainMainMaterial.getCount(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_PERIOD_ID] + " = " + entChainMainMaterial.getChainPeriodId() + " AND " + PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_MATERIAL_TYPE] + " = " + PstChainMainMaterial.TYPE_NEXT_COST);
                        if (count > 0) {
                            msgString = "Material distribusi sudah ada untuk periode ini !";
                            return RSLT_FORM_INCOMPLETE;
                        }
                    }

                    //CEK VALIDASI
                    if (this.entChainMainMaterial.getPeriodDistribution() <= 0) {
                        msgString = "Jumlah periode untuk item distribusi tidak boleh " + this.entChainMainMaterial.getPeriodDistribution() + " !";
                        return RSLT_FORM_INCOMPLETE;
                    }

                    //CEK PERIODE SELANJUTNYA SUDAH TERSEDIA
                    int nextPeriod = countNextPeriodExist(this.entChainMainMaterial.getChainPeriodId());
                    if (nextPeriod < this.entChainMainMaterial.getPeriodDistribution()) {
                        msgString = "Jumlah periode slanjutnya tidak cukup untuk " + this.entChainMainMaterial.getPeriodDistribution() + " periode distribusi !";
                        return RSLT_FORM_INCOMPLETE;
                    }
                }

                //PEMBULATAN
                entChainMainMaterial.setCostPct(Double.valueOf(new DecimalFormat("#.##").format(entChainMainMaterial.getCostPct())));
                entChainMainMaterial.setSalesValue(Double.valueOf(new DecimalFormat("#.##").format(entChainMainMaterial.getSalesValue())));

                if (entChainMainMaterial.getOID() == 0) {
                    try {
                        long oid = pstChainMainMaterial.insertExc(this.entChainMainMaterial);
                        updateItemDistribution(oid);
                        updatePerhitunganPeriod(this.entChainMainMaterial.getChainPeriodId());
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstChainMainMaterial.updateExc(this.entChainMainMaterial);
                        updateItemDistribution(oid);
                        updatePerhitunganPeriod(this.entChainMainMaterial.getChainPeriodId());
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidChainMainMaterial != 0) {
                    try {
                        entChainMainMaterial = PstChainMainMaterial.fetchExc(oidChainMainMaterial);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidChainMainMaterial != 0) {
                    try {
                        entChainMainMaterial = PstChainMainMaterial.fetchExc(oidChainMainMaterial);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidChainMainMaterial != 0) {
                    try {
                        ChainMainMaterial cmm = PstChainMainMaterial.fetchExc(oidChainMainMaterial);
                        deleteCostDistribution(oidChainMainMaterial);
                        long oid = PstChainMainMaterial.deleteExc(oidChainMainMaterial);
                        updatePerhitunganPeriod(cmm.getChainPeriodId());
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default:

        }
        return rsCode;
    }

    public int countNextPeriodExist(long chainPeriodId) {
        try {
            ChainPeriod CurrentPeriod = PstChainPeriod.fetchExc(chainPeriodId);
            return PstChainPeriod.getCount(PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + CurrentPeriod.getChainMainId()
                    + " AND " + PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX] + " > " + CurrentPeriod.getIndex());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public synchronized void updatePerhitunganPeriod(long chainPeriodId) {
        try {
            //UPDATE PERSENTASE TOTAL COST
            updateTotalCostPeriod(chainPeriodId);
            //UPDATE PERSENTASE TOTAL COST PERIODE SELANJUTNYA (JIKA ADA)
            updateTotalCostNextPeriod(chainPeriodId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void updateTotalCostPeriod(long chainPeriodId) {
        //CARI GRAND TOTAL SALES PERIODE
        double grandTotalSales = PstChainMainMaterial.sumTotalSalesPeriod(chainPeriodId);
        //CARI GRAND TOTAL COST PERIODE
        double grandTotalCostPeriod = PstChainAddCost.sumTotalCostPeriod(chainPeriodId);

        //UPDATE DATA COST
        Vector<ChainAddCost> listCost = PstChainAddCost.list(0, 0, PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_PERIOD_ID] + " = " + chainPeriodId, "");
        for (ChainAddCost cost : listCost) {
            if (cost.getCostType() == PstChainAddCost.COST_TYPE_REFERENCED && cost.getProductDistributionId() > 0) {
                try {
                    ChainMainMaterial productDistribution = PstChainMainMaterial.fetchExc(cost.getProductDistributionId());
                    double costValue = productDistribution.getCostValue() / productDistribution.getPeriodDistribution();
                    double roundedCostValue = Double.valueOf(new DecimalFormat("#.##").format(costValue));
                    cost.setStockValue(roundedCostValue);
                    PstChainAddCost.updateExc(cost);
                } catch (DBException | NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        //UPDATE DATA PRODUCT
        Vector<ChainMainMaterial> listProduct = PstChainMainMaterial.list(0, 0, PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_PERIOD_ID] + " = " + chainPeriodId, "");
        for (ChainMainMaterial product : listProduct) {
            try {
                if (product.getCostType() == PstChainMainMaterial.COST_TYPE_AUTO) {
                    //HITUNG PERSENTASE TOTAL COST
                    double totalCostPct = 0;
                    if (grandTotalSales == 0) {
                        totalCostPct = 100;
                    } else {
                        totalCostPct = (product.getStockQty() * product.getSalesValue()) / grandTotalSales * 100;
                    }
                    double roundedTotalCostPct = Double.valueOf(new DecimalFormat("#.##").format(totalCostPct));
                    product.setCostPct(roundedTotalCostPct);
                }
                //HITUNG COST VALUE
                double subTotalCostProduct = (product.getCostPct() / 100) * grandTotalCostPeriod;
                double costValue = subTotalCostProduct / product.getStockQty();
                double roundedCostValue = Double.valueOf(new DecimalFormat("#.##").format(costValue));
                product.setCostValue(roundedCostValue);

                PstChainMainMaterial.updateExc(product);
            } catch (NumberFormatException | DBException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public synchronized void updateTotalCostNextPeriod(long chainPeriodId) {
        try {
            ChainPeriod currentPeriod = PstChainPeriod.fetchExc(chainPeriodId);
            //CARI PERIODE SELANJUTNYA 
            Vector<ChainPeriod> listNextPeriod = PstChainPeriod.list(0, 0,
                    PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + currentPeriod.getChainMainId()
                    + " AND " + PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX] + " > " + currentPeriod.getIndex(), PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX]);
            for (ChainPeriod period : listNextPeriod) {
                updateTotalCostPeriod(period.getOID());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void updateItemDistribution(long productDistributionId) {
        //DELETE COST DISTRIBUTION
        deleteCostDistribution(productDistributionId);
        //GENERATE COST DISTRUBUTION (IF AVAILABLE)
        int generated = generateCostDistribution(productDistributionId);
    }

    public synchronized void deleteCostDistribution(long productDistributionId) {
        Vector<ChainAddCost> listCostDistribution = PstChainAddCost.list(0, 0, PstChainAddCost.fieldNames[PstChainAddCost.FLD_PRODUCT_DISTRIBUTION_ID] + " = " + productDistributionId, "");
        for (ChainAddCost cac : listCostDistribution) {
            try {
                PstChainAddCost.deleteExc(cac.getOID());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public synchronized int generateCostDistribution(long productDistributionId) {
        int itemGenerated = 0;
        try {
            ChainMainMaterial productDistribution = PstChainMainMaterial.fetchExc(productDistributionId);
            //CEK PERIODE DISTRIBUTION
            if (productDistribution.getMaterialType() == PstChainMainMaterial.TYPE_NEXT_COST && productDistribution.getPeriodDistribution() > 0) {
                //CARI PERIODE SELANJUTNYA
                ChainPeriod currentPeriod = PstChainPeriod.fetchExc(productDistribution.getChainPeriodId());
                String wherePeriod = PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + currentPeriod.getChainMainId()
                        + " AND " + PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX] + " > " + currentPeriod.getIndex();
                Vector<ChainPeriod> listNextPeriod = PstChainPeriod.list(0, 0, wherePeriod, PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX]);

                if (listNextPeriod.size() >= productDistribution.getPeriodDistribution()) {
                    double costValue = productDistribution.getCostValue() / productDistribution.getPeriodDistribution();
                    double roundedCostValue = Double.valueOf(new DecimalFormat("#.##").format(costValue));
                    for (ChainPeriod period : listNextPeriod) {
                        ChainAddCost costDistribution = new ChainAddCost();
                        costDistribution.setChainPeriodId(period.getOID());
                        costDistribution.setMaterialId(productDistribution.getMaterialId());
                        costDistribution.setStockQty(productDistribution.getStockQty());
                        costDistribution.setStockValue(roundedCostValue);
                        costDistribution.setCostType(PstChainAddCost.COST_TYPE_REFERENCED);
                        costDistribution.setProductDistributionId(productDistributionId);
                        PstChainAddCost.insertExc(costDistribution);

                        itemGenerated++;
                        if (itemGenerated == productDistribution.getPeriodDistribution()) {
                            break;
                        }
                    }
                }
            }
        } catch (DBException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return itemGenerated;
    }
}
