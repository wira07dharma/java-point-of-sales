/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class AjaxUploadFile extends HttpServlet {

    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.jSONObject = new JSONObject();
        this.jSONArray = new JSONArray();

        int directPenerimaan = FRMQueryString.requestInt(request, "direct_penerimaan");
        long materialId = FRMQueryString.requestLong(request, "hidden_material_id");
        String lokasiSimpan = PstSystemProperty.getValueByName("PROP_IMGCACHE");
        long userId = FRMQueryString.requestLong(request, "user_id");
        String userName = FRMQueryString.requestString(request, "user_name");
        //lokasiSimpan = "D:\\Dimata IT Solutions\\Dewa\\PROJECT\\2017\\Prochain\\dProchain_new_theme\\dProchain_20161124\\jsp_new\\imgcache";
        String message = "";
        int statusSimpan = 0;
        int statusHapus = 0;
        if (materialId != 0 && !lokasiSimpan.isEmpty()) {
            if (ServletFileUpload.isMultipartContent(request)) {
                try {
                    List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                    for (FileItem item : multiparts) {
                        if (item.getSize() > 0) {
                            if (!item.isFormField()) {
                                String typeFile = item.getName().substring(item.getName().lastIndexOf("."), item.getName().length());
                                if (typeFile.equals(".jpg") || typeFile.equals(".JPG") || typeFile.equals(".jpeg") || typeFile.equals(".JPEG")) {
                                    Material m = PstMaterial.fetchExc(materialId);
                                    Material prevM = PstMaterial.fetchExc(materialId);
                                    File lastFoto = new File(lokasiSimpan + File.separator + m.getMaterialImage());
                                    String tgl = Formater.formatDate(new Date(), "yyyyMMdd_HHmmss");
                                    String namaFoto = "" + materialId + "_" + tgl + ".jpg";
                                    try {
                                        item.write(new File(lokasiSimpan + File.separator + namaFoto));
                                        m.setMaterialImage(namaFoto);
                                        PstMaterial.updateExc(m);
                                        insertHistoryMaterial(userId, userName, Command.EDIT, materialId, m, prevM);
                                        statusSimpan = 1;
                                        lastFoto.delete();
                                        statusHapus = 1;
                                        if (statusSimpan == 1 && statusHapus == 1) {
                                            message += "Upload foto berhasil. ";
                                        }
                                    } catch (Exception e) {
                                        message += e.getMessage();
                                    }
                                } else {
                                    message += "Tipe file bukan '.jpg'. ";
                                }
                            } else {
                                if (item.getFieldName().equals("file")) {
                                    message += "Tipe input tidak sesuai. ";
                                }
                            }
                        } else {
                            message += "File kosong (ukuran file = 0). ";
                        }
                    }
                } catch (FileUploadException e) {
                    message += e.getMessage();
                } catch (DBException e) {
                    message += e.getMessage();
                }
            } else {
                message += "Tipe file bukan multipart. ";
            }
        } else if (materialId == 0) {
            message += "Id material kosong. ";
        } else if (lokasiSimpan.isEmpty()) {
            message += "Lokasi simpan kosong. ";
        }
        try {
            this.jSONObject.put("FRM_FIELD_RETURN_MESSAGE", "" + message);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }
        //response.getWriter().print(this.jSONObject);
        response.sendRedirect("master/material/material_main.jsp?command=" + Command.EDIT
                + "&hidden_material_id=" + materialId + "&message_upload=" + message
                + "&status_upload=" + statusSimpan + "&direct_penerimaan=" + directPenerimaan);
    }

    //added by dewok 2018-02-09
    public void insertHistoryMaterial(long userID, String nameUser, int cmd, long oid, Material material, Material prevMaterial) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(material.getLogDetail(prevMaterial));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
