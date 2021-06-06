/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Dimata
 */

import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class TempPostDoc extends Entity {
    private int docType;
    private long docId;

    /**
     * @return the docType
     */
    public int getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(int docType) {
        this.docType = docType;
    }

    /**
     * @return the docId
     */
    public long getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(long docId) {
        this.docId = docId;
    }

}
