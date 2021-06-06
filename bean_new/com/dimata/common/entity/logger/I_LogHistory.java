/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.logger;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author AGUS
 */
public interface I_LogHistory {
    /**
     * get detail perubahan dokumen/data yg dibandingkan dengan dokument/data sebelumnya
     * @param prevDoc
     * @return : String dari data yang berubah saja
     */
    public String getLogDetail(Entity prevDoc); 
}
