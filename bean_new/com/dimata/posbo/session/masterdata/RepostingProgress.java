/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.masterdata;

import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class RepostingProgress {
    
    private int sumMatRePosting=0;
    private String noteSumMatReposting = "";
    private String noteUpdateStock = "";
    private int sumMatRePostingDone=0;
    private Vector matReposting = new Vector(1,1); 

    /**
     * @return the sumMatRePosting
     */
    public int getSumMatRePosting() {
        return sumMatRePosting;
    }

    /**
     * @param sumMatRePosting the sumMatRePosting to set
     */
    public void setSumMatRePosting(int sumMatRePosting) {
        this.sumMatRePosting = sumMatRePosting;
    }

    /**
     * @return the noteSumMatReposting
     */
    public String getNoteSumMatReposting() {
        return noteSumMatReposting;
    }

    /**
     * @param noteSumMatReposting the noteSumMatReposting to set
     */
    public void setNoteSumMatReposting(String noteSumMatReposting) {
        this.noteSumMatReposting = noteSumMatReposting;
    }

    /**
     * @return the noteUpdateStock
     */
    public String getNoteUpdateStock() {
        return noteUpdateStock;
    }

    /**
     * @param noteUpdateStock the noteUpdateStock to set
     */
    public void setNoteUpdateStock(String noteUpdateStock) {
        this.noteUpdateStock = noteUpdateStock;
    }

    /**
     * @return the sumMatRePostingDone
     */
    public int getSumMatRePostingDone() {
        return sumMatRePostingDone;
    }

    /**
     * @param sumMatRePostingDone the sumMatRePostingDone to set
     */
    public void setSumMatRePostingDone(int sumMatRePostingDone) {
        this.sumMatRePostingDone = sumMatRePostingDone;
    }

    /**
     * @return the matReposting
     */
    public Vector getMatReposting() {
        return matReposting;
    }

    /**
     * @param matReposting the matReposting to set
     */
    public void setMatReposting(Vector matReposting) {
        this.matReposting = matReposting;
    }
    
    
   
    
}
