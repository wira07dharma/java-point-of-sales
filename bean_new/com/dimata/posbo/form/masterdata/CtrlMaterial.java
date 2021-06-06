package com.dimata.posbo.form.masterdata;

/* java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;

/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;

/* project package */
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.system.entity.*;

//integrasi dengan BO
import com.dimata.ObjLink.BOPos.CatalogLink;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.interfaces.BOPos.I_Catalog;
import com.dimata.common.entity.payment.*;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.system.AppValue;
import com.dimata.common.form.payment.FrmPriceType;
import com.dimata.pos.session.masterCashier.SessMasterCashier;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.session.warehouse.SessMinimumStock;
import com.dimata.qdep.entity.I_DocStatus;

public class CtrlMaterial extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_DELETE_RESTRICT = 4;

    // ini untuk membedakan material
    // untuk hanoman,material sendiri, atau yang lain
    public int DESIGN_MATERIAL_FOR = DESIGN_HANOMAN;
    public int multiLanguageName = 0;
    public static int DESIGN_MATERIAL = 0;
    public static int DESIGN_HANOMAN = 1;

    public static String[][] resultText = {
        {"Berhasil ...", "Tidak dapat diproses ...", "Kode material sudah ada ...", "Data tidak lengkap ...", "Material tidak bisa dihapus, masih dipakai modul lain ..."},
        {"Succes ...", "Can not process ...", "Material code already exist ...", "Data incomplate ...", "Cannot delete, material still used by another module"}
    };

    private int start;
    private String msgString;
    private Material material;
    Material prevMaterial = null;
    private PstMaterial pstMaterial;
    private FrmMaterial frmMaterial;
    private Vector listPriceMapping = new Vector(1, 1);
    private Vector listDiscountMapping = new Vector(1, 1);
    int language = LANGUAGE_DEFAULT;

    private DiscountMapping discountMapping = new DiscountMapping();
    DiscountMapping prevDiscountMapping;

    PstMaterialStock pstMaterialStock;
    PstPriceTypeMapping pstPriceTypeMapping;
    private PriceTypeMapping priceTypeMapping;
    PriceTypeMapping prevPriceTypeMapping;
    FrmPriceType frmPricetype = new FrmPriceType();
    FrmMaterialStock frmMaterialStock;
    private MaterialStock materialStock;
    MaterialStock prevMaterialStock;

    private HttpServletRequest req;
    Date dateLog = new Date();

    public CtrlMaterial(HttpServletRequest request) {
        msgString = "";
        material = new Material();
        try {
            pstMaterial = new PstMaterial(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmMaterial = new FrmMaterial(request, material);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMaterial.addError(frmMaterial.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_CODE_EXIST]);
                return resultText[language][RSLT_CODE_EXIST];
            case I_DBExceptionInfo.DEL_RESTRICTED:
                return resultText[language][RSLT_DELETE_RESTRICT];

            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_CODE_EXIST;
            case I_DBExceptionInfo.DEL_RESTRICTED:
                return RSLT_DELETE_RESTRICT;

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

    public Material getMaterial() {
        return material;
    }

    public FrmMaterial getForm() {
        return frmMaterial;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public void setListPriceMapping(Vector result) {
        this.listPriceMapping = result;
    }

    public Vector getListPriceMapping() {
        return this.listPriceMapping;
    }

    public void setListDiscountMapping(Vector result) {
        this.listDiscountMapping = result;
    }

    public Vector getListDiscountMapping() {
        return this.listDiscountMapping;
    }

    // add by fitra 22-04-2014
    public int action(int cmd, long oidMaterial) throws Exception {
        return action(cmd, oidMaterial, 0, "");
    }

    public int action(int cmd, long oidMaterial, long userID, String nameUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        // proses men set program yang untk hanoman / material sendiri.
        try {
            String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
            DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
        } catch (Exception e) {
        }
        // - selesai

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:

                if (oidMaterial != 0) {
                    try {
                        material = PstMaterial.fetchExc(oidMaterial);
                    } catch (Exception exc) {
                    }
                    // update by fitra
                    try {
                        prevMaterial = PstMaterial.fetchExc(oidMaterial);
                    } catch (Exception exc) {
                    }
                }

                frmMaterial.requestEntityObject(material);
                //System.out.println("isi dari item type"+material.getItemType());
                material.setLastUpdate(new Date());

                if (frmMaterial.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                /**
                 * ini di gunakan untuk proses penggabungan category dan merk
                 * jika type switch nya automatic
                 */
                String goodName = material.getName();
                if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {
                    try {
                        Merk merk = PstMerk.fetchExc(material.getMerkId());
                        goodName = merk.getName() + Material.getSeparate() + goodName;
                    } catch (Exception e) {
                    }
                    try {
                        Category categ = PstCategory.fetchExc(material.getCategoryId());
                        goodName = categ.getName() + Material.getSeparate() + goodName;
                    } catch (Exception e) {
                    }
                }
                material.setName(goodName);
                // end ------------------

                if (material.getOID() == 0) {
                    try {
                        long oid = pstMaterial.insertExc(this.material);

                        //add by fitra
                        if (oid != 0) {

                            insertHistoryMaterial(userID, nameUser, cmd, oid);

                        }

                        // integrasi data material dengan
                        // data yang ada di hanoman
                        if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                            if (oid != 0) {
                                goodName = material.getName();
                                if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {
                                    String forDiff = "";
                                    try {
                                        Merk merk = PstMerk.fetchExc(material.getMerkId());
                                        forDiff = merk.getName() + Material.getSeparate();
                                    } catch (Exception e) {
                                    }
                                    try {
                                        Category categ = PstCategory.fetchExc(material.getCategoryId());
                                        forDiff = categ.getName() + Material.getSeparate() + forDiff;
                                    } catch (Exception e) {
                                    }
//                                    try {
//                                        goodName = goodName.substring(forDiff.length(), goodName.length());
//                                    } catch (Exception e) {
//                                    }
                                }

                                CatalogLink clink = new CatalogLink();
                                clink.setCatalogId(oid);
                                clink.setName(goodName);
                                clink.setItemCategoryId(material.getCategoryId());

                                clink.setPriceRp(material.getDefaultPrice());
                                clink.setPriceUsd(0);

                                clink.setCostRp(material.getDefaultCost());
                                clink.setCostUsd(0);

                                clink.setDescription("");
                                clink.setCode(material.getSku());
                                clink.setStores(new Vector(1, 1));

                                I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();
                                i_catalog.insertCatalog(clink);
                            }
                            //----- end
                        }

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

                        // add by fitra
                        int cmdHistory = Command.UPDATE;
                        long oid = pstMaterial.updateExc(this.material);

                        if (oid != 0) {
                            insertHistoryMaterial(userID, nameUser, cmdHistory, oid);
                        }

                        if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                            //integrasi POS to BO
                            if (oid != 0) {
                                goodName = material.getName();
                                if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {
                                    String forDiff = "";
                                    try {
                                        Merk merk = PstMerk.fetchExc(material.getMerkId());
                                        forDiff = merk.getName() + Material.getSeparate();
                                    } catch (Exception e) {
                                    }
                                    try {
                                        Category categ = PstCategory.fetchExc(material.getCategoryId());
                                        forDiff = categ.getName() + Material.getSeparate() + forDiff;
                                    } catch (Exception e) {
                                    }
//                                    try {
//                                        goodName = goodName.substring(forDiff.length(), goodName.length());
//                                    } catch (Exception e) {
//                                    }
                                }

                                CatalogLink clink = new CatalogLink();
                                clink.setCatalogId(oid);
                                clink.setName(goodName);
                                clink.setItemCategoryId(material.getCategoryId());

                                clink.setPriceRp(material.getDefaultPrice());
                                clink.setPriceUsd(0);

                                clink.setCostRp(material.getDefaultCost());
                                clink.setCostUsd(0);

                                clink.setDescription("");
                                clink.setCode(material.getSku());
                                clink.setStores(new Vector(1, 1));

                                I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();
                                i_catalog.updateCatalog(clink);
                            }
                            //----- end
                        }

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMaterial != 0) {
                    try {
                        material = PstMaterial.fetchExc(oidMaterial);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMaterial != 0) {
                    try {
                        material = PstMaterial.fetchExc(oidMaterial);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMaterial != 0) {
                    try {
                        long oid = PstMaterial.deleteExc(oidMaterial);

                        try {
                            prevMaterial = PstMaterial.fetchExc(oidMaterial);
                        } catch (Exception exc) {
                        }

                        if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                            //
                            CatalogLink clink = new CatalogLink();
                            clink.setCatalogId(oidMaterial);
                            clink.setName(material.getName());
                            clink.setItemCategoryId(material.getCategoryId());
                            clink.setPriceRp(material.getDefaultPrice());
                            clink.setPriceUsd(0);
                            clink.setCostRp(material.getDefaultCost());
                            clink.setCostUsd(0);
                            clink.setDescription("");
                            clink.setCode(material.getSku());
                            clink.setStores(new Vector(1, 1));
                            I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();
                            i_catalog.deleteCatalog(clink);
                        }

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

    // update by fitra 20-04-2014

    public int action(int cmd, long oidMaterial, Vector vectDataPrice, Vector vectDataDisc, Vector locations, long userID, String nameUser, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        // proses men set program yang untk hanoman / material sendiri.
        try {
            String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
            DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
        } catch (Exception e) {
        }
        //int multiLanguageName = 0;

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                try {
                    PstMaterialStock.refreshCatalogStock();
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                if (oidMaterial != 0) {
                    try {
                        material = PstMaterial.fetchExc(oidMaterial);
                    } catch (Exception exc) {
                    }

                    try {
                        prevMaterial = PstMaterial.fetchExc(oidMaterial);
                    } catch (Exception exc) {
                    }
                }

                frmMaterial.requestEntityObject(material);

                material.setLastUpdate(new Date());

                /**
                 * ini pengecekan bila oid unit tidak ada karena oid unit harus
                 * ada
                 */
                if (material.getDefaultStockUnitId() == 0) {
                    long oidUnit = PstUnit.getOidUnit();
                    material.setDefaultStockUnitId(oidUnit);
                    material.setBuyUnitId(oidUnit);
                    /*try{
                     String sql = "UPDATE "+PstMaterial.TBL_MATERIAL+" SET "+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]+"="+oidUnit+
                     " , "+PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]+"="+oidUnit;
                     DBHandler.execUpdate(sql);
                     }catch(Exception e){} */
                }

                if (frmMaterial.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                //update for litama by dewok 2018-02-06
                int cekTypeOfBusinessDetail = Integer.parseInt(AppValue.getValueByKey("TYPE_OF_BUSINESS_DETAIL"));
                if (cekTypeOfBusinessDetail == 2) {
                    if (material.getGondolaCode() == 0) {
                        msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                        return RSLT_FORM_INCOMPLETE;
                    }
                }

                /**
                 * ini di gunakan untuk proses penggabungan category dan merk
                 * jika type switch nya automatic
                 */
                String goodName = material.getName();
                if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {
                    try {
                        Merk merk = PstMerk.fetchExc(material.getMerkId());
                        goodName = merk.getName() + Material.getSeparate() + goodName;
                    } catch (Exception e) {
                    }
                    try {
                        Category categ = PstCategory.fetchExc(material.getCategoryId());
                        goodName = categ.getName() + Material.getSeparate() + goodName;
                    } catch (Exception e) {
                    }
                }
                material.setName(goodName);
                // end ------------------

                boolean isInsert = true;
                if (material.getOID() == 0) {
                    isInsert = true;

                    try {
                        material.setProses(Material.IS_PROCESS_INSERT_UPDATE);

                        //automatic create SKU
                        int otomaticSku = 0;
                        int typeBussinessDetail = 0;
                        try {
                            otomaticSku = Integer.parseInt(AppValue.getValueByKey("SKU_GENERATE_OTOMATIC"));
                            typeBussinessDetail = Integer.parseInt(AppValue.getValueByKey("TYPE_OF_BUSINESS_DETAIL"));
                        } catch (Exception ex) {

                        }

                        if (otomaticSku == 1) {
                            String kodeSub = "";
                            try {
                                SubCategory subCategory = PstSubCategory.fetchExc(material.getSubCategoryId());
                                kodeSub = subCategory.getCode();
                            } catch (Exception e) {
                            }
                            if (typeBussinessDetail == 2) {
                                String codeSku = generateCodeItemLitama();
                                //String sku = PstMaterial.getCodeOtomaticGenerateSkuLitama(codeSku);
                                String sku = generateNewCodeSkuLitamaIfExistV2(codeSku);
                                material.setSku(sku);
                                material.setBarCode(material.getSku());
                            } else {
                                material.setSku(PstMaterial.getCodeOtomaticGenerateSku(kodeSub));
                            }
                        }

                        long oid = pstMaterial.insertExc(this.material);
                        oidMaterial = oid;
                        //add by fitra 22-04-2014
                        if (oid != 0) {
                            insertHistoryMaterial(userID, nameUser, cmd, oid);
                        }

                        if (oid != 0) {
                            double qty_stok_satuan = FRMQueryString.requestDouble(request, "qty_stok_satuan");
                            double qty_stok_berat = FRMQueryString.requestDouble(request, "qty_stok_berat");
                            double harga_beli = FRMQueryString.requestDouble(request, "harga_beli");
                            double harga_jual = FRMQueryString.requestDouble(request, "harga_jual");
                            double rate_penjualan = FRMQueryString.requestDouble(request, "rate_penjualan");
                            double faktor_jual = FRMQueryString.requestDouble(request, "faktor_jual");
                            double uphet_persentase = FRMQueryString.requestDouble(request, "uphet_persentase");
                            double uphet_value = FRMQueryString.requestDouble(request, "uphet_value");
                            double uphet_persentase_total = FRMQueryString.requestDouble(request, "uphet_persentase_total");
                            double uphet_value_total = FRMQueryString.requestDouble(request, "uphet_value_total");
                            double ongkos = FRMQueryString.requestDouble(request, "ongkos");
                            MaterialDetail materialDetail = new MaterialDetail();
                            materialDetail.setOID(0);
                            materialDetail.setMaterialId(oidMaterial);
                            materialDetail.setQty(qty_stok_satuan);
                            materialDetail.setBerat(qty_stok_berat);
                            materialDetail.setHargaJual(harga_jual);
                            materialDetail.setHargaBeli(harga_beli);
                            materialDetail.setRate(rate_penjualan);
                            materialDetail.setFaktorJual(faktor_jual);
                            materialDetail.setUphetValue(uphet_value);
                            materialDetail.setUphetPersentase(uphet_persentase);
                            materialDetail.setUphetValueTot(uphet_value_total);
                            materialDetail.setUphetPersentaseTot(uphet_persentase_total);
                            materialDetail.setOngkos(ongkos);
                            try {
                                long oidMaterialDetail = PstMaterialDetail.insertExc(materialDetail);
                                insertHistoryMaterialDetail(userID, nameUser, cmd, oid, materialDetail, null);
                            } catch (Exception ex) {
                            }                            
                        }
                        // digunakan untuk mengupdate data yang
                        // ada di hanoman material
                        if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                            PstMaterial.insertOutlet(locations, oid);
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    isInsert = false;
                    try {
                        // add by fitra 22-04-2014
                        int cmdHistory = Command.UPDATE;
                        material.setProses(Material.IS_PROCESS_INSERT_UPDATE);
                        material.setProcessStatus(PstMaterial.UPDATE);

                        //added by dewok 2018-01-12
                        int typeOfBusinessDetail = Integer.parseInt(AppValue.getValueByKey("TYPE_OF_BUSINESS_DETAIL"));
                        if (typeOfBusinessDetail == 2) {
                            //generate item new sku
                            String codeSku = generateCodeItemLitama();
                            String sku = material.getSku();
                            String lastDigitSku = material.getSku().substring(sku.length() - 7);
                            material.setSku(codeSku + lastDigitSku);
                            material.setBarCode(material.getSku());
                        }

                        long oid = pstMaterial.updateExc(this.material);
                        // add by fitra 22-04-2014
                        if (oid != 0) {
                            insertHistoryMaterial(userID, nameUser, cmdHistory, oid);
                        }
                        // digunakan untuk mengupdate data yang
                        // ada di hanoman material
                        if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                            PstMaterial.updateOutlet(locations, oid);
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                CatalogLink clink = new CatalogLink();
                //integrasi POS to BO
                if (material.getOID() != 0) {
                    goodName = material.getName();
                    if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {
                        String forDiff = "";
                        if (material.getMaterialSwitchType() == Material.WITH_USE_SWITCH_MERGE_AUTOMATIC) {
                            try {
                                Merk merk = PstMerk.fetchExc(material.getMerkId());
                                forDiff = merk.getName() + Material.getSeparate();
                            } catch (Exception e) {
                            }
                            try {
                                Category categ = PstCategory.fetchExc(material.getCategoryId());
                                forDiff = categ.getName() + Material.getSeparate() + forDiff;
                            } catch (Exception e) {
                            }
                        }
//                        try {
//                            goodName = goodName.substring(forDiff.length(), goodName.length());
//                        } catch (Exception e) {
//                        }
                    }

                    if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {

                        clink.setCatalogId(material.getOID());
                        clink.setName(goodName);
                        clink.setItemCategoryId(material.getCategoryId());

                        clink.setCostRp(material.getDefaultCost());
                        clink.setCostUsd(0);

                        clink.setDescription("");
                        clink.setCode(material.getSku());
                        clink.setStores(locations);
                    }
                }
                //----- end
                System.out.println(" oidMaterial >>>>>>>> : " + oidMaterial);
                /* manipulate price mapping */

                // disini tempat membuat history Type Harga
                if (oidMaterial != 0) {
                    if (vectDataPrice != null && vectDataPrice.size() > 0) {
                        for (int i = 0; i < vectDataPrice.size(); i++) {
                            Vector vect = (Vector) vectDataPrice.get(i);
                            if (vect != null && vect.size() > 0) {
                                for (int j = 0; j < vect.size(); j++) {
                                    Vector vect2 = (Vector) vect.get(j);
                                    if (vect2 != null && vect2.size() == 3) {
                                        long oidPrice = 0L;
                                        try {
                                            oidPrice = Long.parseLong((String) vect2.get(0));
                                        } catch (Exception exc) {;
                                        }
                                        double value = 0.0d;
                                        try {
                                            value = Double.parseDouble((String) vect2.get(1));
                                        } catch (Exception exc) {;
                                        }
                                        long oidStandard = 0;
                                        try {
                                            oidStandard = Long.parseLong((String) vect2.get(2));
                                        } catch (Exception exc) {;
                                        }
                                        PriceTypeMapping priceTypeMap = new PriceTypeMapping();
                                        if (value >= 0) {
                                            priceTypeMap.setPrice(value);
                                            priceTypeMap.setPriceTypeId(oidPrice);
                                            priceTypeMap.setStandartRateId(oidStandard);
                                            priceTypeMap.setMaterialId(oidMaterial);

                                            // ini proses pengecekan jika ada integrasi dengan hanoman
                                            if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                                                // ================================================
                                                // Integration between POS & BO -- Eka
                                                // default price type
                                                String strInit = PstSystemProperty.getValueByName("INTEGRATION_PRICE_MAP");
                                                String where = PstPriceType.fieldNames[PstPriceType.FLD_CODE] + "='" + strInit + "'";
                                                long oidPrcType = 0;
                                                Vector vct = PstPriceType.list(0, 0, where, null);
                                                if (vct != null && vct.size() > 0) {
                                                    PriceType pt = (PriceType) vct.get(0);
                                                    oidPrcType = pt.getOID();
                                                }

                                                if (oidPrcType == oidPrice) {
                                                    //default IDR ambil dari master,
                                                    strInit = PstSystemProperty.getValueByName("INTEGRATION_CURR_RP");
                                                    where = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] + "='" + strInit + "'";
                                                    long oidRp = 0;
                                                    vct = PstCurrencyType.list(0, 0, where, null);
                                                    if (vct != null && vct.size() > 0) {
                                                        CurrencyType unit = (CurrencyType) vct.get(0);
                                                        oidRp = unit.getOID();
                                                    }
                                                    StandartRate stRateRp = PstStandartRate.getActiveStandardRate(oidRp);
                                                    if (stRateRp.getOID() == oidStandard) {
                                                        clink.setPriceRp(value);
                                                    }

                                                    //default USD ambil dari master
                                                    strInit = PstSystemProperty.getValueByName("INTEGRATION_CURR_USD");
                                                    where = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] + "='" + strInit + "'";
                                                    long oidUsd = 0;
                                                    vct = PstCurrencyType.list(0, 0, where, null);
                                                    if (vct != null && vct.size() > 0) {
                                                        CurrencyType unit = (CurrencyType) vct.get(0);
                                                        oidUsd = unit.getOID();
                                                    }
                                                    StandartRate stRateUsd = PstStandartRate.getActiveStandardRate(oidUsd);
                                                    if (stRateUsd.getOID() == oidStandard) {
                                                        clink.setPriceUsd(value);
                                                    }
                                                }
                                                //end integration
                                                //==========================================================
                                            }

                                            boolean checkOID = PstPriceTypeMapping.checkOID(priceTypeMap.getPriceTypeId(), priceTypeMap.getMaterialId(), priceTypeMap.getStandartRateId());
                                            if (checkOID) {
                                                // add by fitra 22-04-1990
                                                if (oidPrice != 0) {
                                                    try {
                                                        priceTypeMapping = PstPriceTypeMapping.fetchExc(oidPrice, oidMaterial, oidStandard);
                                                        //form input

                                                        priceTypeMapping.setPrice(FRMQueryString.requestDouble(request, "price_" + i + j));

                                                        //frmPriceTypeMapping.requestEntityObject(priceTypeMapping);
                                                    } catch (Exception exc) {
                                                    }

                                                    try {
                                                        prevPriceTypeMapping = PstPriceTypeMapping.fetchExc(oidPrice, oidMaterial, oidStandard);
                                                    } catch (Exception exc) {
                                                    }

                                                }

                                                try {
                                                    // add by fitra
                                                    int cmdHistory = Command.UPDATE;
                                                    PstPriceTypeMapping.updateExc(priceTypeMapping);

                                                    if (oidMaterial != 0) {

                                                        insertHistoryPrice(userID, nameUser, cmdHistory, oidPrice, oidMaterial, oidStandard);

                                                    }

                                                } catch (Exception err) {
                                                    err.printStackTrace();
                                                    System.out.println("errr>>>>>>>>>>> update mapping : " + err.toString());
                                                }
                                            } else {
                                                try {
                                                    // add by fitra
                                                    PstPriceTypeMapping.insertExc(priceTypeMap);

                                                    if (oidPrice != 0) {
                                                        try {
                                                            priceTypeMapping = PstPriceTypeMapping.fetchExc(oidPrice, oidMaterial, oidStandard);
                                                        //form input

                                                            priceTypeMapping.setPrice(FRMQueryString.requestDouble(request, "price_" + i + j));

                                                            //frmPriceTypeMapping.requestEntityObject(priceTypeMapping);
                                                        } catch (Exception exc) {
                                                        }

                                                        try {
                                                            prevPriceTypeMapping = PstPriceTypeMapping.fetchExc(0, oidMaterial, oidStandard);;
                                                        } catch (Exception exc) {
                                                        }

                                                    }

                                                    if (oidPrice != 0) {

                                                        insertHistoryPrice1(userID, nameUser, cmd, oidPrice, oidMaterial, oidStandard);

                                                    }

                                                } catch (Exception err) {
                                                    err.printStackTrace();
                                                    System.out.println("errr>>>>>>>>>>> insert mapping : " + err.toString());
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    setListPriceMapping(oidMaterial);
                }

                ///
                if (oidMaterial != 0) {
                    double qty_stok_satuan = FRMQueryString.requestDouble(request, "qty_stok_satuan");
                    double qty_stok_berat = FRMQueryString.requestDouble(request, "qty_stok_berat");
                    double harga_beli = FRMQueryString.requestDouble(request, "harga_beli");
                    double harga_jual = FRMQueryString.requestDouble(request, "harga_jual");
                    double rate_penjualan = FRMQueryString.requestDouble(request, "rate_penjualan");
                    double faktor_jual = FRMQueryString.requestDouble(request, "faktor_jual");
                    double uphet_persentase = FRMQueryString.requestDouble(request, "uphet_persentase");
                    double uphet_value = FRMQueryString.requestDouble(request, "uphet_value");
                    double uphet_persentase_total = FRMQueryString.requestDouble(request, "uphet_persentase_total");
                    double uphet_value_total = FRMQueryString.requestDouble(request, "uphet_value_total");
                    double ongkos = FRMQueryString.requestDouble(request, "ongkos");
                    MaterialDetail materialDetail = new MaterialDetail();
                    MaterialDetail prevMatDet = new MaterialDetail();
                    long oidMaterialDetailId = PstMaterialDetail.checkOIDMaterialDetailId(oidMaterial);
                    try {
                        prevMatDet = PstMaterialDetail.fetchExc(oidMaterialDetailId);
                    } catch (Exception e) {
                    }
                    materialDetail.setOID(oidMaterialDetailId);
                    materialDetail.setMaterialId(oidMaterial);
                    materialDetail.setQty(qty_stok_satuan);
                    materialDetail.setBerat(qty_stok_berat);
                    materialDetail.setHargaJual(harga_jual);
                    materialDetail.setHargaBeli(harga_beli);
                    materialDetail.setRate(rate_penjualan);
                    materialDetail.setFaktorJual(faktor_jual);
                    materialDetail.setUphetValue(uphet_value);
                    materialDetail.setUphetPersentase(uphet_persentase);
                    materialDetail.setUphetValueTot(uphet_value_total);
                    materialDetail.setUphetPersentaseTot(uphet_persentase_total);
                    materialDetail.setOngkos(ongkos);
                    try {
                        if (oidMaterialDetailId != 0) {
                            long oidMaterialDetail = PstMaterialDetail.updateExc(materialDetail);
                            insertHistoryMaterialDetail(userID, nameUser, cmd, material.getOID(), materialDetail, prevMatDet);
                            //added by dewok 2018-01-12
                            //update data item di penerimaan (jewelry)
                            int typeOfBusinessDetail = Integer.parseInt(AppValue.getValueByKey("TYPE_OF_BUSINESS_DETAIL"));
                            if (typeOfBusinessDetail == 2) {                                                                
                                //cari data penerimaan yg memiliki item yg di update
                                Vector listItemRec = PstMatReceiveItem.list(0, 0, "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + " = '" + oidMaterial + "'", "");
                                if (!listItemRec.isEmpty()) {
                                    MatReceiveItem mri = (MatReceiveItem) listItemRec.get(0);
                                    MatReceive mr = PstMatReceive.fetchExc(mri.getReceiveMaterialId());
                                    if (mr.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                        mri.setQtyEntry(qty_stok_satuan);
                                        mri.setQty(qty_stok_satuan);
                                        mri.setBerat(qty_stok_berat);
                                        //hitung harga beli/harga pokok
                                        //cek tipe item emas or berlian                                    
                                        double totalBeli = 0;
                                        if (mr.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS) {
                                            double qty = qty_stok_satuan;
                                            double berat = qty_stok_berat;
                                            double hel = mr.getHel();
                                            double nilaiTukar = mr.getNilaiTukar();
                                            totalBeli = (qty * berat * hel * (nilaiTukar / 100));
                                        } else if (mr.getReceiveItemType() == Material.MATERIAL_TYPE_BERLIAN) {
                                            totalBeli = (mri.getCost() * mr.getTransRate());
                                        }
                                        mri.setTotal(totalBeli);
                                        PstMatReceiveItem.updateExc(mri);
                                    }
                                }
                            }
                        } else {
                            long oidMaterialDetail = PstMaterialDetail.insertExc(materialDetail);
                            insertHistoryMaterialDetail(userID, nameUser, cmd, material.getOID(), materialDetail, null);
                        }
                    } catch (Exception ex) {
                    }
                }

                if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                    //integration between BO and POS
                    try {
                        if (isInsert) {
                            /*System.out.println("-");
                             System.out.println("-");
                             System.out.println("Inserting catalog to hanoman ...");
                             System.out.println("clink.getCatalogId() : " + clink.getCatalogId());
                             System.out.println("clink.setName : " + clink.getName());
                             System.out.println("clink.setCode : " + clink.getCode());
                             System.out.println("clink.setStores : " + clink.getStores());
                             System.out.println("clink.getPriceRp : " + clink.getPriceRp());
                             System.out.println("clink.getPriceUsd : " + clink.getPriceUsd()); */

                            I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();

                            // System.out.println("i_catalog : " + i_catalog);
                            i_catalog.insertCatalog(clink);

                            // System.out.println("----> iserting catalog success");
                        } else {
                            /* System.out.println("-");
                             System.out.println("-");
                             System.out.println("updating catalog on hanoman ...");
                             System.out.println("clink.getCatalogId() : " + clink.getCatalogId());
                             System.out.println("clink.setName : " + clink.getName());
                             System.out.println("clink.setCode : " + clink.getCode());
                             System.out.println("clink.setStores : " + clink.getStores());
                             System.out.println("clink.getPriceRp : " + clink.getPriceRp());
                             System.out.println("clink.getPriceUsd : " + clink.getPriceUsd()); */

                            I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();

                            // System.out.println("i_catalog : " + i_catalog);
                            i_catalog.updateCatalog(clink);
                        }

                        //System.out.println("----> end process inserting catalog ");
                    } catch (Exception e) {
                        System.out.println("Exception exx :" + e.toString());
                    }
                    //==============================
                }

                /* manupalate data discount */
                if (oidMaterial != 0) {
                    if (vectDataDisc != null && vectDataDisc.size() > 0) {
                        for (int i = 0; i < vectDataDisc.size(); i++) {
                            Vector vect = (Vector) vectDataDisc.get(i);

                            for (int j = 0; j < vect.size(); j++) {
                                Vector vect2 = (Vector) vect.get(j);

                                long oidDiscType = 0;
                                try {
                                    oidDiscType = Long.parseLong((String) vect2.get(0));
                                } catch (Exception exc) {
                                }
                                long oidCurrType = 0;
                                try {
                                    oidCurrType = Long.parseLong((String) vect2.get(1));
                                } catch (Exception exc) {
                                }
                                double valuePct = 0;
                                try {
                                    valuePct = Double.parseDouble((String) vect2.get(2));
                                } catch (Exception exc) {
                                }
                                double value = 0;
                                try {
                                    value = Double.parseDouble((String) vect2.get(3));
                                } catch (Exception exc) {
                                }
                                DiscountMapping discMapping = new DiscountMapping();
                                discMapping.setCurrencyTypeId(oidCurrType);
                                discMapping.setDiscountPct(valuePct);
                                discMapping.setDiscountTypeId(oidDiscType);
                                discMapping.setDiscountValue(value);
                                discMapping.setMaterialId(oidMaterial);

                                boolean checkOID = PstDiscountMapping.checkOID(oidDiscType, oidMaterial, oidCurrType);
                                if (!checkOID) {
                                    try {
                                        PstDiscountMapping.insertExc(discMapping);
                                        // add by fitra 22-04-1990
                                        if (oidDiscType != 0) {

                                            insertHistoryDiscount(userID, nameUser, cmd, oidDiscType, oidMaterial);

                                        }

                                    } catch (Exception err) {
                                        err.printStackTrace();
                                        System.out.println("err di insert to disc mapping " + err.toString());
                                    }
                                } else {
                                    try {
                                        PstDiscountMapping.updateExc(discMapping);
                                    } catch (Exception err) {
                                        err.printStackTrace();
                                        System.out.println("err di update to disc mapping " + err.toString());
                                    }
                                }
                            }
                        }

                    }
                    setListDiscMapping(oidMaterial);
                }

                //SessMinimumStock sessMasterCashier = new SessMinimumStock();
                SessMinimumStock.loadMaterialRequestSearchByHash();

                break;

            case Command.EDIT:
                if (oidMaterial != 0) {
                    try {
                        material = PstMaterial.fetchExc(oidMaterial);
                        setListPriceMapping(oidMaterial);
                        setListDiscMapping(oidMaterial);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMaterial != 0) {
                    try {
                        material = PstMaterial.fetchExc(oidMaterial);
                        setListPriceMapping(oidMaterial);
                        setListDiscMapping(oidMaterial);

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMaterial != 0) {
                    try {
                        long oid = 0; //PstMaterial.deleteExc(oidMaterial);
                        material = PstMaterial.fetchExc(oidMaterial);

                        try {
                            prevMaterial = PstMaterial.fetchExc(oidMaterial);
                        } catch (Exception exc) {
                        }

                        if (SessMaterial.readyDataToDelete(oidMaterial)) {
                            material.setBarCode("" + oidMaterial);
                            int typeOfBusinessDetail = Integer.parseInt(AppValue.getValueByKey("TYPE_OF_BUSINESS_DETAIL"));
                            if (typeOfBusinessDetail != 2) {
                                material.setSku("" + oidMaterial);
                            }
                            material.setProcessStatus(PstMaterial.DELETE);
                            material.setLastUpdate(new Date());
                            oid = PstMaterial.updateExc(material);
                            PstDiscountMapping.deleteByMaterialId(oidMaterial);
                            PstPriceTypeMapping.deleteByMaterialId(oidMaterial);
                            //delete Disc Qty
                            PstDiscountQtyMapping.deleteByMaterialId(oidMaterial);
                            
                            //updated by dewok 2018-09-12 : hapus item dari database untuk litama
                            if (typeOfBusinessDetail == 2) {
                                PstMaterial.deleteExc(oidMaterial);
                            }
                            if (DESIGN_MATERIAL_FOR == DESIGN_HANOMAN) {
                                //
                                clink = new CatalogLink();
                                clink.setCatalogId(oidMaterial);
                                clink.setName(material.getName());
                                clink.setItemCategoryId(material.getCategoryId());
                                clink.setPriceRp(material.getDefaultPrice());
                                clink.setPriceUsd(0);
                                clink.setCostRp(material.getDefaultCost());
                                clink.setCostUsd(0);
                                clink.setDescription("");
                                clink.setCode(material.getSku());
                                clink.setStores(new Vector(1, 1));
                                I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();
                                i_catalog.deleteCatalog(clink);
                            }
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            setListPriceMapping(oidMaterial);
                            setListDiscMapping(oidMaterial);
                            frmMaterial.addError(FrmMaterial.FRM_FIELD_MATERIAL_ID, "");
                            msgString = "Hapus data gagal, Data sudah pernah di pakai untuk transaksi."; //FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
                setListPriceMapping(oidMaterial);
                setListDiscMapping(oidMaterial);
                break;

        }
        return excCode;
    }

    public void setListPriceMapping(long oidMaterial) {
        String wh = PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] + " = " + oidMaterial;
        listPriceMapping = PstPriceTypeMapping.list(0, 0, wh, "");
        setListPriceMapping(listPriceMapping);
    }

    public void setListDiscMapping(long oidMaterial) {
        String wh = PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_MATERIAL_ID] + " = " + oidMaterial;
        listDiscountMapping = PstDiscountMapping.list(0, 0, wh, "");
        setListDiscountMapping(listDiscountMapping);
    }

    /**
     * ini di gunakan untuk men set data minimum stok per location
     */
    public void setMinimumStockPerLocation(Vector vLocation, long materialId, int cmd, long periodeId, long userID, String nameUser, HttpServletRequest request) {
        try {
            if (vLocation != null && vLocation.size() > 0) {
                for (int k = 0; k < vLocation.size(); k++) {
                    Location location = (Location) vLocation.get(k);
                    double minQty = FRMQueryString.requestDouble(req, "" + location.getOID() + "");
                    double minOptimum = FRMQueryString.requestDouble(req, "optimum_" + location.getOID() + "");
                    //getQtyOptimum()
                    System.out.println("minQty : " + minQty);
                    long oidMatStock = PstMaterialStock.fetchByMaterialLocation(materialId, location.getOID());
                    if (oidMatStock != 0) {

                        materialStock = PstMaterialStock.fetchExc(oidMatStock);

                        materialStock.setQtyMin(minQty);
                        materialStock.setQtyOptimum(minOptimum);

                        materialStock.setQtyOptimum(FRMQueryString.requestDouble(request, "optimum_" + location.getOID()));
                        materialStock.setQtyMin(FRMQueryString.requestDouble(request, "" + location.getOID()));
                        try {   // add by fitra
                            int cmdHistory = Command.UPDATE;
                            if (materialStock != null && materialStock.getOID() != 0) {

                                //frmMaterialStock.requestEntityObject(materialStock);
                                try {
                                    prevMaterialStock = PstMaterialStock.fetchExc(materialStock.getOID());
                                } catch (Exception exc) {
                                }

                            }
                            PstMaterialStock.updateExc(materialStock);

                            insertHistoryLocation(userID, nameUser, cmdHistory, oidMatStock, materialId);

                        } catch (Exception e) {
                            System.out.println("error " + e);
                        }
                    } else {
                        MaterialStock materialStock = new MaterialStock();
                        materialStock.setQtyMin(minQty);
                        materialStock.setQtyOptimum(minOptimum);
                        materialStock.setPeriodeId(periodeId);
                        materialStock.setMaterialUnitId(materialId);
                        materialStock.setLocationId(location.getOID());
                        try {
                            PstMaterialStock.insertExc(materialStock);

                            //add by fitra
                            if (oidMatStock != 0) {
                                insertHistoryLocation(userID, nameUser, cmd, oidMatStock, materialId);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Err save minim stock : " + e.toString());
        }
    }

    /**
     * gadnyana ini di gunakan untuk insert data material yang dengan link ke
     * hanoman, data yang di dapat dari upload dari excel
     */
    public static void insertCatalogManual(long oidMaterial, Vector locations) {
        try {
            Material material = PstMaterial.fetchExc(oidMaterial);

            // ini untuk link ke hanoman
            CatalogLink clink = new CatalogLink();
            clink.setCatalogId(oidMaterial);
            clink.setName(material.getName());
            clink.setItemCategoryId(material.getCategoryId());
            clink.setCostRp(material.getDefaultCost());
            clink.setCostUsd(0);
            clink.setDescription("");
            clink.setCode(material.getSku());
            clink.setStores(locations);

            I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();
            i_catalog.insertCatalog(clink);

        } catch (Exception e) {
            System.out.println("");
        }
    }

    public static void updateCatalogPriceManual(long oidMaterial, double price) {
        try {
            Material material = PstMaterial.fetchExc(oidMaterial);

            // ini untuk link ke hanoman
            CatalogLink clink = new CatalogLink();
            clink.setCatalogId(material.getOID());
            clink.setName(material.getName());
            clink.setItemCategoryId(material.getCategoryId());
            clink.setCostRp(material.getDefaultCost());
            clink.setCostUsd(0);
            clink.setDescription("");
            clink.setCode(material.getSku());
            clink.setPriceRp(price);

            I_Catalog i_catalog = (I_Catalog) Class.forName(I_Catalog.classNameHanoman).newInstance();
            i_catalog.updateCatalog(clink);

        } catch (Exception e) {
            System.out.println("");
        }
    }

       // add by fitra 20-04-2014
    /**
     * Insert Data ke log History
     */
    public void insertHistoryMaterial(long userID, String nameUser, int cmd, long oid) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.material.getLogDetail(prevMaterial));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
    
    //added by dewok 2018-02-09
    public void insertHistoryMaterialDetail(long userID, String nameUser, int cmd, long oid, MaterialDetail newMd, MaterialDetail prevMd) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(newMd.getLogDetail(prevMd));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    // add by fitra 20-04-2014
    public void insertHistoryPrice(long userID, String nameUser, int cmd, long oid, long oidMat, long Curr) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos Material Price");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(priceTypeMapping.getPstClassName());
            logSysHistory.setLogDocumentId(oidMat);
            logSysHistory.setLogDetail(priceTypeMapping.getLogDetail(prevPriceTypeMapping));

            if ((!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE)) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    // add by fitra 20-04-2014
    public void insertHistoryPrice1(long userID, String nameUser, int cmd, long oid, long oidMat, long Curr) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos Material Price");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(priceTypeMapping.getPstClassName());
            logSysHistory.setLogDocumentId(oidMat);
            logSysHistory.setLogDetail(priceTypeMapping.getLogDetail1(prevPriceTypeMapping));

            if ((!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE)) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

   // add by fitra 20-04-2014
    public void insertHistoryDiscount(long userID, String nameUser, int cmd, long oid, long oidMat) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos Material Price");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(priceTypeMapping.getPstClassName());
            logSysHistory.setLogDocumentId(oidMat);
            logSysHistory.setLogDetail(discountMapping.getLogDetail(prevDiscountMapping, oid));

            if ((!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE)) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {
            System.out.println("error " + e);
        }
    }

    public void insertHistoryLocation(long userID, String nameUser, int cmd, long oid, long oidMat) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos Material Price");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oidMat);
            logSysHistory.setLogDetail(materialStock.getLogDetail(prevMaterialStock));

            if ((!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE)) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {
            System.out.println("error " + e);
        }
    }

     
     
     public int actionSaveAllMaterial(int cmd,HttpServletRequest request, int recordMaterial, int multiLanguageName) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:

                //update material name
                for(int i=0; i<recordMaterial; i++){   
                    long materialId= FRMQueryString.requestLong(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MATERIAL_ID]+"_"+i);
                    if(materialId!=0){
                        Material materialUpdate = new Material();
                        try {
                            materialUpdate = PstMaterial.fetchExc(materialId);
                        } catch (Exception exc) {
                        }

                        if(multiLanguageName==1){
                            String name1 =FRMQueryString.requestString(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]+"_"+i);
                            String name2 =FRMQueryString.requestString(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME_1]+"_"+i);
                            String name3= FRMQueryString.requestString(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME_2]+"_"+i);

                            String nameAll = name1+Material.getSeparateLanguage()+name2+Material.getSeparateLanguage()+name3;

                            materialUpdate.setName(nameAll);
                        }else{
                            materialUpdate.setName(FRMQueryString.requestString(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]+"_"+i)); 
                        }

                        materialUpdate.setBarCode(FRMQueryString.requestString(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BARCODE]+"_"+i));
                        materialUpdate.setCategoryId(FRMQueryString.requestLong(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CATEGORY_ID]+"_"+i));
                        materialUpdate.setMerkId(FRMQueryString.requestLong(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MERK_ID]+"_"+i));
                        materialUpdate.setSku(FRMQueryString.requestString(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]+"_"+i));
                        materialUpdate.setDefaultStockUnitId(FRMQueryString.requestLong(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_STOCK_UNIT_ID]+"_"+i));
                        materialUpdate.setSubCategoryId(FRMQueryString.requestLong(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SUB_CATEGORY_ID]+"_"+i));
                        materialUpdate.setGondolaCode(FRMQueryString.requestLong(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_GONDOLA_CODE]+"_"+i));
                        Date newDate = new Date();
                        materialUpdate.setLastUpdate(newDate);
                        try{
                            long oid = pstMaterial.updateExc(materialUpdate);
                        }catch (DBException dbexc) {
                            excCode = dbexc.getErrorCode();
                            msgString = getSystemMessage(excCode);
                            return getControlMsgId(excCode);
                        } catch (Exception exc) {
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                        }
                    }
                }

        }
        return rsCode;
    }

    public int actionSaveAllMaterialJewelry(int cmd, HttpServletRequest request, int recordMaterial, long userID, String nameUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                //update material name
                for (int i = 0; i < recordMaterial; i++) {
                    long materialId = FRMQueryString.requestLong(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MATERIAL_ID] + "_" + i);
                    if (materialId != 0) {                        
                        MaterialDetail md = new MaterialDetail();
                        MaterialDetail prevMd = new MaterialDetail();
                        long oidMaterialDetailId = PstMaterialDetail.checkOIDMaterialDetailId(materialId);
                        try {
                            material = PstMaterial.fetchExc(materialId);
                            md = PstMaterialDetail.fetchExc(oidMaterialDetailId);
                            prevMd = PstMaterialDetail.fetchExc(oidMaterialDetailId);
                        } catch (Exception exc) {
                        }

                        md.setHargaJual(FRMQueryString.requestDouble(request, "harga_jual_"+i));
                        md.setFaktorJual(FRMQueryString.requestDouble(request, "faktor_jual_"+i));
                        md.setUphetValue(FRMQueryString.requestDouble(request, "uphet_value_"+i));
                        md.setUphetPersentase(FRMQueryString.requestDouble(request, "uphet_persentase_"+i));
                        md.setUphetValueTot(FRMQueryString.requestDouble(request, "uphet_value_total_"+i));
                        md.setUphetPersentaseTot(FRMQueryString.requestDouble(request, "uphet_persentase_total_"+i));
                        
                        try {
                            long oid = PstMaterialDetail.updateExc(md);
                            insertHistoryMaterialDetail(userID, nameUser, Command.UPDATE, materialId, md, prevMd);

                            if (true/*DESIGN_MATERIAL_FOR == DESIGN_HANOMAN*/) {
                                String multiLokJual[] = FRMQueryString.requestStringValues(request, "lokasi_jual_" + i);
                                Vector locations = new Vector();
                                if (multiLokJual != null) {
                                    for (int j = 0; j < multiLokJual.length; j++) {
                                        locations.add(String.valueOf(multiLokJual[j]));
                                    }
                                }
                                PstMaterial.updateOutlet(locations, materialId);
                            }
                            
                            //cek sudah punya data price type mapping
                            Vector listPriceTypeMapping = PstPriceTypeMapping.list(0, 0, "" + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] + " = " + materialId, "");
                            if (listPriceTypeMapping.isEmpty()) {
                                Vector listPriceType = PstPriceType.list(0, 0, "", "");
                                for (int pt = 0; pt < listPriceType.size(); pt++) {
                                    PriceType type = (PriceType) listPriceType.get(pt);
                                    Vector listRate = PstStandartRate.list(0, 0, "" + PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS] + " = 1", "");
                                    for (int rate = 0; rate < listRate.size(); rate++) {
                                        StandartRate sr = (StandartRate) listRate.get(rate);
                                        PriceTypeMapping mapping = new PriceTypeMapping();
                                        mapping.setMaterialId(materialId);
                                        mapping.setPriceTypeId(type.getOID());
                                        mapping.setStandartRateId(sr.getOID());
                                        PstPriceTypeMapping.insertExc(mapping);
                                    }                                    
                                }
                            }
                            msgString = "Data berhasil disimpan";
                        } catch (Exception exc) {
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                        }
                    }
                }

        }
        return rsCode;
    }

    public String generateCodeItemLitama() throws Exception {
        String codeSku = "";
        if (material.getKepemilikanId() != 0) {
            ContactList contactList = PstContactList.fetchExc(material.getKepemilikanId());
            codeSku = contactList.getContactCode();
        }
        if (material.getCategoryId() != 0) {
            Category category = PstCategory.fetchExc(material.getCategoryId());
            codeSku = codeSku + category.getCode();
        }
        if (material.getMerkId() != 0) {
            Merk merk = PstMerk.fetchExc(material.getMerkId());
            codeSku = codeSku + merk.getCode();
        }
        if (material.getPosKadar() != 0) {
            Kadar kadar = PstKadar.fetchExc(material.getPosKadar());
            codeSku = codeSku + kadar.getKodeKadar();
        }
        if (material.getSupplierId() != 0) {
            ContactList contactList = PstContactList.fetchExc(material.getSupplierId());
            codeSku = codeSku + contactList.getContactCode();
        }
        if (material.getPosColor() != 0) {
            Color color = PstColor.fetchExc(material.getPosColor());
            codeSku = codeSku + color.getColorCode();
        }
        return codeSku;
    }
    
    public boolean cekSkuExist(String sku) {
        Vector listItem = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " = '" + sku + "'", null);
        if (!listItem.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public String generateNewCodeSkuLitamaIfExist(String itemCode) {
        String where = PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " <> " + Material.MATERIAL_TYPE_EMAS_LANTAKAN;
        int item = PstMaterial.getCount(where);
        String year = Formater.formatDate(new Date(), "yyyy");
        year = year.substring(year.length() - 2, year.length());
        for (int i = 1; i <= item; i++) {
            String lastCode = PstMaterial.getCounterLitama(i);
            String whereSku = where + " AND RIGHT(" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) = '" + year + lastCode + "'";
            Vector listItem = PstMaterial.list(0, 0, whereSku, null);
            if (listItem.isEmpty()) {
                return itemCode + year + lastCode;
            }
        }
        String safeItemCode = itemCode + year + PstMaterial.getCounterLitama(item + 1);
        if (cekSkuExist(safeItemCode)) {
            safeItemCode = PstMaterial.getCodeOtomaticGenerateSkuLitama(itemCode);
        }
        return safeItemCode;
    }
    
    public String generateNewCodeSkuLitamaIfExistV2(String itemCode) {
        String year = Formater.formatDate(new Date(), "yyyy");
        year = year.substring(year.length() - 2, year.length());
        int lastCounter = PstMaterial.getLastCounterLitamaByYear(year);
        String lastCode = PstMaterial.getCounterLitama(lastCounter);
        return itemCode + year + lastCode;
    }
        
}
