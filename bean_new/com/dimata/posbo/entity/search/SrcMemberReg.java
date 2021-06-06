
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.posbo.entity.search; 
 
/* package java */ 
import java.util.Date;

public class SrcMemberReg{ 


	
        private String barcode = "";

	private String name = "";

	private long groupmember = 0;

	private int status;

        /** Holds value of property religion. */
        private long religion = 0;
        
        /** Holds value of property birthDateFrom. */
        private Date birthDateFrom;
        
        /** Holds value of property birthDateTo. */
        private Date birthDateTo;
        
        /** Holds value of property regDateFrom. */
        private Date regDateFrom;
        
        /** Holds value of property regDateTo. */
        private Date regDateTo;
        
        /** Holds value of property allBirthDate. */
        private boolean allBirthDate = true;
        
        /** Holds value of property allRegDate. */
        private boolean allRegDate = true;
        
        /** Holds value of property sortBy. */
        private int sortBy;

        private String memberCode="";
        
	public String getBarcode(){ 
		return barcode; 
	} 

	public void setBarcode(String barcode){ 
		if ( barcode == null ) {
			barcode = ""; 
		} 
		this.barcode = barcode; 
	} 

	public String getName(){ 
		return name; 
	} 

	public void setName(String name){ 
		if ( name == null ) {
			name = ""; 
		} 
		this.name = name; 
	} 

	public long getGroupmember(){ 
		return groupmember; 
	} 

	public void setGroupmember(long groupmember){ 
		this.groupmember = groupmember; 
	} 

	public int getStatus(){ 
		return status; 
	} 

	public void setStatus(int status){ 
		this.status = status; 
	} 

        /** Getter for property agama.
         * @return Value of property agama.
         *
         */
        public long getReligion() {
            return this.religion;
        }
        
        /** Setter for property agama.
         * @param agama New value of property agama.
         *
         */
        public void setReligion(long religion) {
            this.religion = religion;
        }
        
        /** Getter for property birthDateFrom.
         * @return Value of property birthDateFrom.
         *
         */
        public Date getBirthDateFrom() {
            return this.birthDateFrom;
        }
        
        /** Setter for property birthDateFrom.
         * @param birthDateFrom New value of property birthDateFrom.
         *
         */
        public void setBirthDateFrom(Date birthDateFrom) {
            if(birthDateFrom==null)
                birthDateFrom = new Date();            
            this.birthDateFrom = birthDateFrom;
        }
        
        /** Getter for property birthDateTo.
         * @return Value of property birthDateTo.
         *
         */
        public Date getBirthDateTo() {
            return this.birthDateTo;
        }
        
        /** Setter for property birthDateTo.
         * @param birthDateTo New value of property birthDateTo.
         *
         */
        public void setBirthDateTo(Date birthDateTo) {
            if(birthDateTo==null)
                birthDateTo = new Date();
            this.birthDateTo = birthDateTo;
        }
        
        /** Getter for property regDateFrom.
         * @return Value of property regDateFrom.
         *
         */
        public Date getRegDateFrom() {
            return this.regDateFrom;
        }
        
        /** Setter for property regDateFrom.
         * @param regDateFrom New value of property regDateFrom.
         *
         */
        public void setRegDateFrom(Date regDateFrom) {
            if(regDateFrom==null)
                regDateFrom = new Date();
                
            this.regDateFrom = regDateFrom;
        }
        
        /** Getter for property regDateTo.
         * @return Value of property regDateTo.
         *
         */
        public Date getRegDateTo() {
            return this.regDateTo;
        }
        
        /** Setter for property regDateTo.
         * @param regDateTo New value of property regDateTo.
         *
         */
        public void setRegDateTo(Date regDateTo) {
            if(regDateTo==null)
                regDateTo = new Date();
                
            this.regDateTo = regDateTo;
        }
        
        /** Getter for property allBirthDate.
         * @return Value of property allBirthDate.
         *
         */
        public boolean isAllBirthDate() {
            return this.allBirthDate;
        }
        
        /** Setter for property allBirthDate.
         * @param allBirthDate New value of property allBirthDate.
         *
         */
        public void setAllBirthDate(boolean allBirthDate) {
            this.allBirthDate = allBirthDate;
        }
        
        /** Getter for property allRegDate.
         * @return Value of property allRegDate.
         *
         */
        public boolean isAllRegDate() {
            return this.allRegDate;
        }
        
        /** Setter for property allRegDate.
         * @param allRegDate New value of property allRegDate.
         *
         */
        public void setAllRegDate(boolean allRegDate) {
            this.allRegDate = allRegDate;
        }
        
        /** Getter for property sortBy.
         * @return Value of property sortBy.
         *
         */
        public int getSortBy() {
            return this.sortBy;
        }
        
        /** Setter for property sortBy.
         * @param sortBy New value of property sortBy.
         *
         */
        public void setSortBy(int sortBy) {
            this.sortBy = sortBy;
        }
        
        /** Getter for property codeSupplier.
         * @return Value of property codeSupplier.
         *
         */
        public String getCodeSupplier() {
            return this.codeSupplier;
        }
        
        /** Setter for property codeSupplier.
         * @param codeSupplier New value of property codeSupplier.
         *
         */
        public void setCodeSupplier(String codeSupplier) {
            if(codeSupplier==null)
                codeSupplier = "";
            this.codeSupplier = codeSupplier;
        }
        
        /** Getter for property contactPerson.
         * @return Value of property contactPerson.
         *
         */
        public String getContactPerson() {
            return this.contactPerson;
        }
        
        /** Setter for property contactPerson.
         * @param contactPerson New value of property contactPerson.
         *
         */
        public void setContactPerson(String contactPerson) {
            if(contactPerson==null)
                contactPerson = "";
            
            this.contactPerson = contactPerson;
        }
        
        /** Getter for property companyName.
         * @return Value of property companyName.
         *
         */
        public String getCompanyName() {
            return this.companyName;
        }
        
        /** Setter for property companyName.
         * @param companyName New value of property companyName.
         *
         */
        public void setCompanyName(String companyName) {
            if(companyName==null)
                companyName = "";
            this.companyName = companyName;
        }
        
        public static final int MEMBER_NAME = 0;
        public static final int MEMBER_BARCODE = 1;
        public static final int TIPE_MEMBER = 2;
        
        public static final String[][] sortByName ={
            {"Nama member","Barcode","Tipe member"},
            {"Member name","Barcode","Member type"}
        };
        
        /** Holds value of property codeSupplier. */
        private String codeSupplier = "";
        
        /** Holds value of property contactPerson. */
        private String contactPerson = "";
        
        /** Holds value of property companyName. */
        private String companyName = "";

    /**
     * @return the memberCode
     */
    public String getMemberCode() {
        return memberCode;
    }

    /**
     * @param memberCode the memberCode to set
     */
    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }
        
}
