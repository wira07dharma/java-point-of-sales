/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.search.SrcMaterialRepostingStock;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.util.Formater;
import java.util.ArrayList;
import java.util.HashMap;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_DocType;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class SessProduction {

    private String productionNumber = "";
    private String dateFrom = "";
    private String dateTo = "";
    private String multiLocationId = "";
    private String multiStatus = "";
    private String batchNumber = "";
    private String multiTemplateId = "";
    private int viewType = 0;

    public String getProductionNumber() {
        return productionNumber;
    }

    public void setProductionNumber(String productionNumber) {
        this.productionNumber = productionNumber;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getMultiLocationId() {
        return multiLocationId;
    }

    public void setMultiLocationId(String multiLocationId) {
        this.multiLocationId = multiLocationId;
    }

    public String getMultiStatus() {
        return multiStatus;
    }

    public void setMultiStatus(String multiStatus) {
        this.multiStatus = multiStatus;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getMultiTemplateId() {
        return multiTemplateId;
    }

    public void setMultiTemplateId(String multiTemplateId) {
        this.multiTemplateId = multiTemplateId;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public static int viewTypeDoc = 0;
    public static int viewTypeBatch = 1;
    public static String[] viewTypeTitle = {"View Document", "View Batch"};

    public static ArrayList listProduction(SessProduction sessProduction) {
        ArrayList lists = new ArrayList();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT ";
            if (sessProduction.getViewType() == viewTypeDoc) {
                sql += ""
                        + " pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                        + ", pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]
                        + ", pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                        + ", pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS]
                        + ", pro." + PstProduction.fieldNames[PstProduction.FLD_REMARK]
                        + ", loc1." + PstLocation.fieldNames[PstLocation.FLD_NAME]
                        + ", loc2." + PstLocation.fieldNames[PstLocation.FLD_NAME]
                        + "";
            }
            if (sessProduction.getViewType() == viewTypeBatch) {
                sql += ""
                        + " pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                        + ", pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]
                        + ", grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER]
                        + ", per." + PstChainPeriod.fieldNames[PstChainPeriod.FLD_TITLE]
                        + ", grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_START]
                        + ", grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_END]
                        + "";
            }

            sql += ""
                    + " FROM " + PstProduction.TBL_PRODUCTION + " pro "
                    + " LEFT JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " grp "
                    + " ON grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + " = pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + " LEFT JOIN " + PstChainPeriod.TBL_CHAINPERIOD + " per "
                    + " ON per." + PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_PERIOD_ID]
                    + " = grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_CHAIN_PERIOD_ID]
                    + " LEFT JOIN " + PstChainMain.TBL_CHAIN_MAIN + " chm "
                    + " ON chm." + PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_MAIN_ID]
                    + " = per." + PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID]
                    + " LEFT JOIN " + PstLocation.TBL_P2_LOCATION + " loc1 "
                    + " ON loc1." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " = pro." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID]
                    + " LEFT JOIN " + PstLocation.TBL_P2_LOCATION + " loc2 "
                    + " ON loc2." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " = pro." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID]
                    + "";

            String where = "";
            if (!sessProduction.getProductionNumber().isEmpty() && sessProduction.getProductionNumber() != null) {
                where += where.length() > 0 ? " AND " : "";
                where += " pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER] + " = '" + sessProduction.getProductionNumber() + "'";
            }

            if (!sessProduction.getBatchNumber().isEmpty() && sessProduction.getBatchNumber() != null) {
                where += where.length() > 0 ? " AND " : "";
                where += " grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + sessProduction.getBatchNumber() + "'";
            }

            int dateStart = 0;
            if (!sessProduction.getDateFrom().isEmpty() && sessProduction.getDateFrom() != null) {
                dateStart = 1;
            }
            int dateEnd = 0;
            if (!sessProduction.getDateTo().isEmpty() && sessProduction.getDateTo() != null) {
                dateEnd = 1;
            }
            if (dateStart == 1 && dateEnd == 0) {
                where += where.length() > 0 ? " AND " : "";
                where += " DATE(pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + ") >= '" + sessProduction.getDateFrom() + "'";
            } else if (dateStart == 0 && dateEnd == 1) {
                where += where.length() > 0 ? " AND " : "";
                where += " DATE(pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + ") <= '" + sessProduction.getDateTo() + "'";
            } else if (dateStart == 1 && dateEnd == 1) {
                where += where.length() > 0 ? " AND " : "";
                where += " DATE(pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + ") BETWEEN '"
                        + "" + sessProduction.getDateFrom() + "' AND '" + sessProduction.getDateTo() + "'";
            }

            if (!sessProduction.getMultiLocationId().isEmpty() && sessProduction.getMultiLocationId() != null) {
                where += where.length() > 0 ? " AND " : "";
                where += "(";
                where += " loc1." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " IN (" + sessProduction.getMultiLocationId() + ")";
                where += " OR loc2." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " IN (" + sessProduction.getMultiLocationId() + ")";
                where += ")";
            }

            if (!sessProduction.getMultiStatus().isEmpty() && sessProduction.getMultiStatus() != null) {
                where += where.length() > 0 ? " AND " : "";
                where += " pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " IN (" + sessProduction.getMultiStatus() + ")";
            }

            if (!sessProduction.getMultiTemplateId().isEmpty() && sessProduction.getMultiTemplateId() != null) {
                where += where.length() > 0 ? " AND " : "";
                where += " chm." + PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_MAIN_ID] + " IN (" + sessProduction.getMultiTemplateId() + ")";
            }

            sql += where.length() > 0 ? " WHERE " + where : "";

            if (sessProduction.getViewType() == viewTypeDoc) {
                sql += " GROUP BY pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID];
                sql += " ORDER BY pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " DESC ";
            }

            if (sessProduction.getViewType() == viewTypeBatch) {
                sql += " GROUP BY grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID];
                sql += " ORDER BY grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_START] + " DESC ";
            }

            sql += " LIMIT 0,20 ";

            dbrs = DBHandler.execQueryResult(sql);
            try (ResultSet rs = dbrs.getResultSet()) {
                while (rs.next()) {
                    HashMap<String, Object> data = new HashMap<>();
                    if (sessProduction.getViewType() == viewTypeDoc) {
                        data.put("PRODUCTION_ID", rs.getLong("pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]));
                        data.put("NUMBER", rs.getString("pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]));
                        data.put("DATE", rs.getDate("pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]));
                        data.put("STATUS", rs.getInt("pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS]));
                        data.put("REMARK", rs.getString("pro." + PstProduction.fieldNames[PstProduction.FLD_REMARK]));
                        data.put("LOCATION_FROM", rs.getString("loc1." + PstLocation.fieldNames[PstLocation.FLD_NAME]));
                        data.put("LOCATION_TO", rs.getString("loc2." + PstLocation.fieldNames[PstLocation.FLD_NAME]));
                    }
                    if (sessProduction.getViewType() == viewTypeBatch) {
                        data.put("PRODUCTION_ID", rs.getLong("pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]));
                        data.put("NUMBER", rs.getString("pro." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]));
                        data.put("BATCH", rs.getString("grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER]));
                        data.put("PERIOD", rs.getString("per." + PstChainPeriod.fieldNames[PstChainPeriod.FLD_TITLE]));
                        data.put("START", rs.getTimestamp("grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_START]));
                        data.put("END", rs.getTimestamp("grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_END]));
                    }
                    lists.add(data);
                }
            }
            return lists;
        } catch (DBException | SQLException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static String listOptionBatchNumber() {
        return listOptionBatchNumber("");
    }

    public static String listOptionBatchNumber(String selectedValue) {
        String html = "";
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT(" + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + ")" + " FROM " + PstProductionGroup.TBL_PRODUCTION_GROUP;
            if (!selectedValue.isEmpty()) {
                sql += " WHERE " + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + selectedValue + "'";
            }
            sql += " ORDER BY " + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " ASC ";

            dbrs = DBHandler.execQueryResult(sql);
            try (ResultSet rs = dbrs.getResultSet()) {
                while (rs.next()) {
                    html += "<option " + (selectedValue.endsWith(rs.getString(1)) ? "selected" : "") + " value='" + rs.getString(1) + "'>" + rs.getString(1) + "</option>";
                }
            }
        } catch (DBException | SQLException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return html;
    }

    public synchronized static String generateProductionCode(Production production) {
        String code = "";
        try {
            if (production.getProductionDate() == null) {
                return "";
            }
            if (production.getLocationFromId() == 0) {
                return "";
            }
            if (production.getLocationToId() == 0) {
                return "";
            }
            Location locFrom = PstLocation.fetchExc(production.getLocationFromId());
            Location locTo = PstLocation.fetchExc(production.getLocationToId());

            String where = ""
                    + " DATE(" + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + ") = '" + Formater.formatDate(production.getProductionDate(), "yyyy-MM-dd") + "'"
                    + " AND " + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID] + " = '" + production.getLocationFromId() + "'"
                    + " AND " + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID] + " = '" + production.getLocationToId() + "'"
                    + "";
            if (production.getOID() != 0) {
                where += " AND " + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID] + " <> '" + production.getOID() + "'";
            }
            int count = PstProduction.getCount(where);
            String zeroCode = "000";
            boolean ok = true;
            while (ok) {
                String zero = zeroCode.substring(0, zeroCode.length() - String.valueOf(count).length());
                code = locFrom.getCode() + "-" + Formater.formatDate(production.getProductionDate(), "yyyyMMdd") + "-PD-" + locTo.getCode() + "-" + zero + (count + 1);
                //CHECK CODE EXIST
                int check = PstProduction.getCount(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER] + " = '" + code + "'");
                if (check == 0) {
                    ok = false;
                } else {
                    count++;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return code;
    }

    public synchronized static String generateBatchNumber(ProductionGroup group) {
        String batchNumber = "";
        try {
            Production p = PstProduction.fetchExc(group.getProductionId());
            String dateNow = Formater.formatDate(p.getProductionDate(), "yyyyMMdd");
            int count = PstProductionGroup.getCount(PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " LIKE '" + dateNow + "%'"
                    + " AND " + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_PARENT_ID] + " = 0");
            String zeroCode = "000";
            boolean ok = true;
            while (ok) {
                String zero = zeroCode.substring(0, zeroCode.length() - String.valueOf(count).length());
                batchNumber = dateNow + "." + zero + (count + 1);
                //CHECK CODE EXIST
                int check = PstProductionGroup.getCount(PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + batchNumber + "'");
                if (check == 0) {
                    ok = false;
                } else {
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return batchNumber;
    }

    public static long getParentGroupId(ProductionGroup group) {
        long parentId = 0;
        try {
            ChainPeriod period = PstChainPeriod.fetchExc(group.getChainPeriodId());
            Vector<ChainPeriod> listPeriod = PstChainPeriod.list(0, 1, PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + period.getChainMainId()
                    + " AND " + PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX] + " < " + period.getIndex(), "");
            for (ChainPeriod cp : listPeriod) {
                parentId = cp.getOID();
                break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return parentId;
    }

    public static double sumTotalSalesProductGroupPeriod(long groupId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " SUM(" + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY] + " * " + PstProductionProduct.fieldNames[PstProductionProduct.FLD_SALES_VALUE] + ")"
                    + " FROM " + PstProductionProduct.TBL_PRODUCTION_PRODUCT
                    + " WHERE " + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = " + groupId
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (DBException | SQLException e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static double sumTotalCostGroupPeriod(long groupId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY] + " * " + PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_VALUE] + ")"
                    + " FROM " + PstProductionCost.TBL_PRODUCTION_COST
                    + " WHERE " + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID] + " = " + groupId
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double total = 0;
            while (rs.next()) {
                total = rs.getDouble(1);
            }
            rs.close();
            return total;
        } catch (DBException | SQLException e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public synchronized static void updatePerhitunganGroupPeriod(long groupId) {
        try {
            //UPDATE PERSENTASE TOTAL COST
            updateTotalCostGroupPeriod(groupId);
            //UPDATE PERSENTASE TOTAL COST PERIODE SELANJUTNYA (JIKA ADA)
            updateTotalCostNextGroupPeriod(groupId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized static void updateTotalCostGroupPeriod(long groupId) {
        //CARI GRAND TOTAL SALES PERIODE
        double grandTotalSales = SessProduction.sumTotalSalesProductGroupPeriod(groupId);
        //CARI GRAND TOTAL COST PERIODE
        double grandTotalCostPeriod = SessProduction.sumTotalCostGroupPeriod(groupId);

        //UPDATE DATA COST
        Vector<ProductionCost> listCost = PstProductionCost.list(0, 0, PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID] + " = " + groupId, "");
        for (ProductionCost cost : listCost) {
            if (cost.getCostType() == PstProductionCost.COST_TYPE_REFERENCED && cost.getProductDistributionId() > 0) {
                try {
                    ProductionProduct productDistribution = PstProductionProduct.fetchExc(cost.getProductDistributionId());
                    double costValue = productDistribution.getCostValue() / productDistribution.getPeriodDistribution();
                    double roundedCostValue = Double.valueOf(new DecimalFormat("#.##").format(costValue));
                    cost.setStockValue(roundedCostValue);
                    PstProductionCost.updateExc(cost);
                } catch (DBException | NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        //UPDATE DATA PRODUCT
        Vector<ProductionProduct> listProduct = PstProductionProduct.list(0, 0, PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = " + groupId, "");
        for (ProductionProduct product : listProduct) {
            try {
                if (product.getCostType() == PstProductionProduct.COST_TYPE_AUTO) {
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

                PstProductionProduct.updateExc(product);
            } catch (NumberFormatException | DBException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public synchronized static void updateTotalCostNextGroupPeriod(long groupId) {
        try {
            ProductionGroup group = PstProductionGroup.fetchExc(groupId);
            //CARI GROUP PERIODE SELANJUTNYA
            Vector<ProductionGroup> listNextGroup = PstProductionGroup.list(0, 0,
                    PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + group.getBatchNumber() + "'"
                    + " AND " + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " > " + group.getIndex(), PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " ASC ");
            for (ProductionGroup pg : listNextGroup) {
                updateTotalCostGroupPeriod(pg.getOID());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized static void updateItemDistribution(long productDistributionId) {
        //DELETE COST DISTRIBUTION
        deleteCostDistribution(productDistributionId);
        //GENERATE COST DISTRUBUTION (IF AVAILABLE)
        int generated = generateCostDistribution(productDistributionId);
    }

    public synchronized static void deleteCostDistribution(long productDistributionId) {
        Vector<ProductionCost> listCostDistribution = PstProductionCost.list(0, 0, PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCT_DISTRIBUTION_ID] + " = " + productDistributionId, "");
        for (ProductionCost pc : listCostDistribution) {
            try {
                PstProductionCost.deleteExc(pc.getOID());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public synchronized static int generateCostDistribution(long productDistributionId) {
        int itemGenerated = 0;
        try {
            ProductionProduct productDistribution = PstProductionProduct.fetchExc(productDistributionId);
            //CEK PERIODE DISTRIBUTION
            if (productDistribution.getMaterialType() == PstProductionProduct.TYPE_NEXT_COST && productDistribution.getPeriodDistribution() > 0) {
                //CARI GROUP PERIODE SELANJUTNYA
                ProductionGroup currentGroup = PstProductionGroup.fetchExc(productDistribution.getProductionGroupId());
                String whereGroup = PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + currentGroup.getBatchNumber() + "'"
                        + " AND " + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " > " + currentGroup.getIndex();
                Vector<ProductionGroup> listNextPeriod = PstProductionGroup.list(0, 0, whereGroup, PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX]);
                if (listNextPeriod.size() > 0) {
                    double costValue = productDistribution.getCostValue() / productDistribution.getPeriodDistribution();
                    double roundedCostValue = Double.valueOf(new DecimalFormat("#.##").format(costValue));
                    for (ProductionGroup group : listNextPeriod) {
                        ProductionCost costDistribution = new ProductionCost();
                        costDistribution.setProductionGroupId(group.getOID());
                        costDistribution.setMaterialId(productDistribution.getMaterialId());
                        costDistribution.setStockQty(productDistribution.getStockQty());
                        costDistribution.setStockValue(roundedCostValue);
                        costDistribution.setCostType(PstProductionCost.COST_TYPE_REFERENCED);
                        costDistribution.setProductDistributionId(productDistributionId);
                        PstProductionCost.insertExc(costDistribution);

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

    public static int countNextPeriodExist(long groupId) {
        try {
            ProductionGroup group = PstProductionGroup.fetchExc(groupId);
            return PstProductionGroup.getCount(PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + group.getBatchNumber() + "'"
                    + " AND " + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " > " + group.getIndex());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static long getItemDistributionId(long groupId) {
        long itemDistributionId = 0;
        DBResultSet dbrs = null;
        try {
            ProductionGroup currentGroup = PstProductionGroup.fetchExc(groupId);

            String sql = "SELECT "
                    + "  pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID]
                    + " , pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION]
                    + " FROM " + PstProductionGroup.TBL_PRODUCTION_GROUP + " grp "
                    + "  INNER JOIN " + PstChainPeriod.TBL_CHAINPERIOD + " per "
                    + "    ON per." + PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_PERIOD_ID] + " = grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_CHAIN_PERIOD_ID]
                    + "  INNER JOIN " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " pro "
                    + "    ON pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + "  INNER JOIN " + PstMaterial.TBL_MATERIAL + " mat "
                    + "    ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + " WHERE "
                    + "  pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_TYPE] + " = " + PstProductionProduct.TYPE_NEXT_COST
                    + "  AND pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION] + " > 0 "
                    + "  AND grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + currentGroup.getBatchNumber() + "' "
                    + "  AND grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " < " + currentGroup.getIndex()
                    + " ORDER BY grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " DESC "
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int checkPeriodDistribution = 0;
            while (rs.next()) {
                checkPeriodDistribution++;
                int period = rs.getInt("pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION]);
                if (period >= checkPeriodDistribution) {
                    itemDistributionId = rs.getLong("pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID]);
                    break;
                }
            }
            rs.close();
        } catch (DBException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBResultSet.close(dbrs);
        }
        return itemDistributionId;
    }

    public static ArrayList<HashMap> getAllItemDistributionLastBatch(long groupId) {
        ArrayList<HashMap> list = new ArrayList<>();
        DBResultSet dbrs = null;
        try {
            ProductionGroup currentGroup = PstProductionGroup.fetchExc(groupId);

            String sql = "SELECT "
                    + "  pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION]
                    + " , pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID]
                    + " , pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + " , pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]
                    + " , pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE]
                    + " FROM " + PstProductionGroup.TBL_PRODUCTION_GROUP + " grp "
                    + "  INNER JOIN " + PstChainPeriod.TBL_CHAINPERIOD + " per "
                    + "    ON per." + PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_PERIOD_ID] + " = grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_CHAIN_PERIOD_ID]
                    + "  INNER JOIN " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " pro "
                    + "    ON pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + "  INNER JOIN " + PstMaterial.TBL_MATERIAL + " mat "
                    + "    ON mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + " WHERE "
                    + "  pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_TYPE] + " = " + PstProductionProduct.TYPE_NEXT_COST
                    + "  AND pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION] + " > 0 "
                    + "  AND grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + currentGroup.getBatchNumber() + "' "
                    + "  AND grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " < " + currentGroup.getIndex()
                    + " ORDER BY grp." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " DESC "
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int checkPeriodDistribution = 0;
            while (rs.next()) {
                checkPeriodDistribution++;
                HashMap<String, String> map = new HashMap<>();
                int period = rs.getInt("pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION]);
                if (period >= checkPeriodDistribution) {
                    //cek apakah sudah ada cost sesuai product distribution
                    long productId = rs.getLong("pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID]);
                    int count = PstProductionCost.getCount(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID] + " = " + groupId + " AND " + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCT_DISTRIBUTION_ID] + " = " + productId);
                    if (count > 0) {
                        continue;
                    }
                    map.put("PRODUCT_ID", "" + productId);
                    map.put("MATERIAL_ID", rs.getString("pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]));
                    map.put("QTY", rs.getString("pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]));
                    double cost = rs.getDouble("pro." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE]);
                    map.put("VALUE", "" + cost / period);
                    list.add(map);
                }
            }
            rs.close();
        } catch (DBException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static void getDataMaterial(SrcStockCard srcStockCard) {
        getDataMaterialCost(srcStockCard);
        getDataMaterialProduct(srcStockCard);
    }

    public static void getDataMaterialCost(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT"
                    + " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                    + " ,P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]
                    + " ,P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID]
                    + " ,SUM(PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY] + ") AS SUM_QTY "
                    + " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + " FROM " + PstProduction.TBL_PRODUCTION + " AS P "
                    + " INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS PG "
                    + " ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + " = P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + " INNER JOIN " + PstProductionCost.TBL_PRODUCTION_COST + " AS PC "
                    + " ON PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID]
                    + " = PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
                    + " ON PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID]
                    + " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND ";
                }
                if (srcStockCard.getWarehouseLocationId() != 0) {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID] + " IN (" + srcStockCard.getLocationId() + "," + srcStockCard.getWarehouseLocationId() + ")";
                } else {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID] + " = " + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause += " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                }
            } else { // data privous
                if (whereClause.length() > 0) {
                    whereClause += " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                    + " ,P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];
            sql = sql + " ORDER BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                java.util.Date date = DBHandler.convertDate(rs.getDate(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]), rs.getTime(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]));
                stockCardReport.setDate(date);
                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_PROD);
                stockCardReport.setTransaction_type(PstProduction.PRODUCTION_COST);
                stockCardReport.setDocCode(rs.getString(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]));
                double qtyBase = 1;//PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setLocationId(rs.getLong(PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID]));

                if (srcStockCard.getLanguage() == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {
                    stockCardReport.setKeterangan("Biaya Produksi");
                } else {
                    stockCardReport.setKeterangan("Production Cost");
                }

                PstStockCardReport.insertExc(stockCardReport);
            }

        } catch (DBException | SQLException | com.dimata.posbo.db.DBException e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
    }

    public static void getDataMaterialProduct(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT"
                    + " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                    + " ,P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]
                    + " ,SUM(PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY] + ") AS SUM_QTY "
                    + " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + " FROM " + PstProduction.TBL_PRODUCTION + " AS P "
                    + " INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS PG "
                    + " ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + " = P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + " INNER JOIN " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " AS PP "
                    + " ON PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]
                    + " = PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
                    + " ON PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND ";
                }
                if (srcStockCard.getWarehouseLocationId() != 0) {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID] + " IN (" + srcStockCard.getLocationId() + "," + srcStockCard.getWarehouseLocationId() + ")";
                } else {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID] + " = " + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause += " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                }
            } else { // data privous
                if (whereClause.length() > 0) {
                    whereClause += " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause += " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                    + " ,P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];
            sql = sql + " ORDER BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                java.util.Date date = DBHandler.convertDate(rs.getDate(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]), rs.getTime(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]));
                stockCardReport.setDate(date);
                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_PROD);
                stockCardReport.setTransaction_type(PstProduction.PRODUCTION_PRODUCT);
                stockCardReport.setDocCode(rs.getString(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]));
                double qtyBase = 1;//PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);

                if (srcStockCard.getLanguage() == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {
                    stockCardReport.setKeterangan("Hasil Produksi");
                } else {
                    stockCardReport.setKeterangan("Production Result");
                }

                PstStockCardReport.insertExc(stockCardReport);
            }

        } catch (DBException | SQLException | com.dimata.posbo.db.DBException e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
    }

    public static void getQtyStockMaterialRepostingCost(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT"
                    + " SUM(PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY] + ") AS SUM_QTY "
                    + " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + " FROM " + PstProductionCost.TBL_PRODUCTION_COST + " AS PC "
                    + " INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS PG "
                    + " ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + " = PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID]
                    + " INNER JOIN " + PstProduction.TBL_PRODUCTION + " AS P "
                    + " ON P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + " = PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
                    + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID];

            String whereClause = "";
            if (srcMaterialRepostingStock.getMaterialId() != 0) {
                whereClause = "PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
            }

            if (srcMaterialRepostingStock.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                } else {
                    whereClause = "P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                }
            }

            if ((srcMaterialRepostingStock.getDateFrom() != null) && (srcMaterialRepostingStock.getDateTo() != null)) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            } else { // jika tanggal end date = null;
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
                for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                    + " ,P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];
            sql = sql + " ORDER BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];

            System.out.println("Sql Production Cost : "+ sql);;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double qtyMaterial = 0;
            double qtyAll = 0;
            //-- added by dewok 2018 for jewelry
            double beratAll = 0;
            //--
            while (rs.next()) {
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcMaterialRepostingStock.setQty(qtyAll);
            }
            if (qtyAll == 0) {
                srcMaterialRepostingStock.setQty(0);
            }
            if (beratAll == 0) {
                srcMaterialRepostingStock.setBerat(0);
            }
        } catch (Exception e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
    }

    public static void getQtyStockMaterialRepostingProduct(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT"
                    + " SUM(PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY] + ") AS SUM_QTY "
                    + " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + " FROM " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " AS PP "
                    + " INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS PG "
                    + " ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + " = PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]
                    + " INNER JOIN " + PstProduction.TBL_PRODUCTION + " AS P "
                    + " ON P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + " = PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
                    + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID];

            String whereClause = "";
            if (srcMaterialRepostingStock.getMaterialId() != 0) {
                whereClause = "PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
            }

            if (srcMaterialRepostingStock.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                } else {
                    whereClause = "P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                }
            }

            if ((srcMaterialRepostingStock.getDateFrom() != null) && (srcMaterialRepostingStock.getDateTo() != null)) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            } else { // jika tanggal end date = null;
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
                for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                    + " ,P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];
            sql = sql + " ORDER BY P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER];

            System.out.println("Sql Production Product : "+ sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double qtyMaterial = 0;
            double qtyAll = 0;
            //-- added by dewok 2018 for jewelry
            double beratAll = 0;
            //--
            while (rs.next()) {
                qtyMaterial = rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcMaterialRepostingStock.setQty(qtyAll);
            }
            if (qtyAll == 0) {
                srcMaterialRepostingStock.setQty(0);
            }
            if (beratAll == 0) {
                srcMaterialRepostingStock.setBerat(0);
            }
        } catch (Exception e) {
            System.out.println("err getDataMaterial : " + e.toString());
        }
    }

    public synchronized static void updateAllAveragePriceProductFromProduction(long productionId) {
        DBResultSet dbrs = null;
        try {
            Production production = PstProduction.fetchExc(productionId);
            
            String sql = "SELECT "
                    + "  m." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                    + "  ,m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "  ,m." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
                    + "  ,SUM(pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY] + ") AS " + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]
                    + "  ,SUM(pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE] + ") AS " + PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE]
                    + "  ,p." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID]
                    + "  ,p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
                    + " FROM "
                    + "  " + PstProduction.TBL_PRODUCTION + " AS p "
                    + "  INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS pg "
                    + "    ON pg." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + "    = p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + "  INNER JOIN " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " AS pp "
                    + "    ON pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]
                    + "    = pg." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + "  INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS m "
                    + "    ON m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "    = pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + " WHERE p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID] + " = " + productionId
                    + " GROUP BY m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            try (ResultSet rs = dbrs.getResultSet()) {
                SessPosting posting = new SessPosting();
                while (rs.next()) {
                    try {
                        long materialId = rs.getLong("m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]);
                        double averagePrice = rs.getDouble("m." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                        double qtyProduct = rs.getDouble("" + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]);
                        double costValue = rs.getDouble("" + PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE]);
                        double qtyStockAfterPosted = SessPosting.sumQtyStockAllLocation(materialId);
                        double qtyStockBeforePosted = qtyStockAfterPosted - qtyProduct;
                        
                        if (qtyStockBeforePosted < 0) qtyStockBeforePosted = 0;
                        double newAveragePrice = ((qtyStockBeforePosted * averagePrice) + (qtyProduct * costValue)) / (qtyStockBeforePosted + qtyProduct);
                        
                        Material prevMat = PstMaterial.fetchExc(materialId);
                        Material newMat = PstMaterial.fetchExc(materialId);
                        newMat.setAveragePrice(newAveragePrice);
                        PstMaterial.updateExc(newMat);
                        
                        //UPDATE HISTORY AVERAGE PRICE
                        String log = newMat.getLogDetail(prevMat);
                        if (!log.isEmpty()) {
                            posting.insertHistoryMaterial(0, "", "Posting Production Doc " + production.getProductionNumber(), newMat, log);
                        }
                        
                    } catch (SQLException | com.dimata.posbo.db.DBException e) {
                    }
                }
                rs.close();
            }
        } catch (DBException | SQLException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static ArrayList getItemProduct(long productionId) {
        ArrayList list = new ArrayList();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + "  m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "  ,m." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                    + "  ,m." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                    + "  ,m." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
                    + "  ,pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]
                    + "  ,pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE]
                    + " FROM "
                    + "  " + PstMaterial.TBL_MATERIAL + " AS m "
                    + "  INNER JOIN " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " AS pp "
                    + "    ON pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + "    = m." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "  INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS pg "
                    + "    ON pg." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + "    = pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]
                    + "  INNER JOIN " + PstProduction.TBL_PRODUCTION + " AS p "
                    + "    ON p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + "    = pg." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + " WHERE p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID] + " = " + productionId
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JSONObject object = new JSONObject();
                try {
                    object.put("MATERIAL_ID", rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                    object.put("SKU", rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                    object.put("NAME", rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                    object.put("AVERAGE_PRICE", rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                    object.put("QTY", rs.getDouble(PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]));
                    object.put("COST", rs.getDouble(PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE]));
                } catch (JSONException ex) {
                }
                list.add(object);
            }
            rs.close();
        } catch (DBException | SQLException e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static int getDocReceiveNotClosed(long materialId, java.util.Date dateDocument) {
        int docNumber = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + ") "
                    + " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " AS rm "
                    + "  INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS rmi "
                    + "    ON rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]
                    + "    = rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
                    + " WHERE rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]
                    + " NOT IN (" + I_DocStatus.DOCUMENT_STATUS_CLOSED + "," + I_DocStatus.DOCUMENT_STATUS_CANCELLED + "," + I_DocStatus.DOCUMENT_STATUS_POSTED + ")"
                    + "  AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " < '" + Formater.formatDate(dateDocument, "yyyy-MM-dd HH:mm:ss") + "'"
                    + "  AND rmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + materialId
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                docNumber = rs.getInt(1);
            }
            rs.close();
        } catch (DBException | SQLException e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return docNumber;
    }

    public static int getDocProductionNotClosed(long materialId, java.util.Date dateDocument) {
        int docNumber = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID] + ") "
                    + " FROM " + PstProduction.TBL_PRODUCTION + " AS p "
                    + "  INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS pg "
                    + "    ON pg." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
                    + "    = p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
                    + "  INNER JOIN " + PstProductionCost.TBL_PRODUCTION_COST + " AS pc "
                    + "    ON pc." + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID]
                    + "    = pg." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + "  INNER JOIN " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " AS pp "
                    + "    ON pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]
                    + "    = pg." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + " WHERE p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS]
                    + " NOT IN (" + I_DocStatus.DOCUMENT_STATUS_CLOSED + "," + I_DocStatus.DOCUMENT_STATUS_CANCELLED + "," + I_DocStatus.DOCUMENT_STATUS_POSTED + ")"
                    + "  AND p." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE] + " < '" + Formater.formatDate(dateDocument, "yyyy-MM-dd HH:mm:ss") + "'"
                    + "  AND ( "
                    + "    pc." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID] + " = " + materialId
                    + "    OR pp." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID] + " = " + materialId
                    + "  )"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                docNumber = rs.getInt(1);
            }
            rs.close();
        } catch (DBException | SQLException e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return docNumber;
    }

}
