/*
 * IjEngineController.java
 *
 * Created on January 22, 2005, 6:10 AM
 */

package com.dimata.ij.session.engine;

// import core java package

import java.util.Vector;
import java.util.Date;

// import dimata package
import com.dimata.util.*;
import com.dimata.util.lang.*;

// qdep package
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_IJGeneral;

// import ij package
import com.dimata.ij.iaiso.*;
import com.dimata.ij.ibosys.*;
import com.dimata.ij.entity.configuration.*;

/**
 * This class will run journal process, trigger by action from jsp
 *
 * @author Administrator
 */
public class IjEngineController {

    /**
     * @param objIjEngineParam
     * @return
     * @created by Edhy
     */
    public int runIJJournalProcess(IjEngineParam objIjEngineParam) {
        int result = 0;

        PstIjConfiguration objPstIjConfiguration = new PstIjConfiguration();
        IjConfiguration objIjConfiguration = objPstIjConfiguration.getObjIJConfiguration(objIjEngineParam.getIBoSystem(), 0, 0);
        objIjEngineParam.setSIjImplBo(objIjConfiguration.getSIjImplClass());

        SessJournal objSessJournal = new SessJournal();
        result = objSessJournal.generateJournal(objIjEngineParam);

        return result;
    }


    /**
     * Used to posting list of journal to AISO, update IJ journal reference and update Bo Doc Status
     *
     * @param vListIjJournal
     * @param objIjEngineParam
     * @created by Edhy
     * @algoritm :
     * - looping / iterate as long as ListIjJornal count
     * - each iterate, do process below :
     *      - posting journal to AISO
     *      - update ij journal with data reference got from AISO
     *      - update Bo Document status to POSTED if process above success
     */
    public void runAisoJournalProcess(Vector vListIjJournal, IjEngineParam objIjEngineParam) {
        if (vListIjJournal != null && vListIjJournal.size() > 0) {
            int iListIjJournalCount = vListIjJournal.size();
            for (int i = 0; i < iListIjJournalCount; i++) {
                IjJournalMain objIjJournalMain = (IjJournalMain) vListIjJournal.get(i);
                runAisoJournalProcess(objIjJournalMain, objIjEngineParam);
            }
        }
    }


    /**
     * Used to posting list of journal to AISO, update IJ journal reference and update Bo Doc Status
     *
     * @param objIjJournalMain
     * @param objIjEngineParam
     * @created by Edhy
     * @algoritm :
     * - looping / iterate as long as ListIjJornal count
     * - each iterate, do process below :
     *      - posting journal to AISO
     *      - update ij journal with data reference got from AISO
     *      - update Bo Document status to POSTED if process above success
     */
    private void runAisoJournalProcess(IjJournalMain objIjJournalMain, IjEngineParam objIjEngineParam) {
        // check apakah data journal sudah merupakan last version dari object di BO        
        PstIjJournalMain objPstIjJournalMain = new PstIjJournalMain();
        boolean bLastVersionOfJournalObject = objPstIjJournalMain.isLastVersionObject(objIjJournalMain);

        // jika IJJournal sama dengan object terakhir di BO 
        if (bLastVersionOfJournalObject) {
            SessPosting objSessPosting = new SessPosting();
            objSessPosting.postingAisoJournal(objIjJournalMain, objIjEngineParam);
        }

        // jika bukan yang terakhir, generate ulang IJJournal dengan data terakhir dari BO
        else {
            SessJournal objSessJournal = new SessJournal();
            System.out.println("bookType : " + objIjEngineParam.getLBookType());
            long lResult = objSessJournal.generateIjJournalImmediately(objIjJournalMain, objIjEngineParam);
        }
    }

}
