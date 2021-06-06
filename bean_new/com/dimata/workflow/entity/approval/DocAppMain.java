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

public class DocAppMain extends Entity { 

	private long documentOid;
	private int doctypeType;
    private boolean isRevised;

	public long getDocumentOid(){ 
		return documentOid; 
	} 

	public void setDocumentOid(long documentOid){ 
		this.documentOid = documentOid; 
	} 

	public int getDoctypeType(){ 
		return doctypeType; 
	}

	public void setDoctypeType(int doctypeType){ 
		this.doctypeType = doctypeType; 
	}

    public boolean getIsRevised(){ return isRevised; }

    public void setIsRevised(boolean isRevised){ this.isRevised = isRevised; }
}
