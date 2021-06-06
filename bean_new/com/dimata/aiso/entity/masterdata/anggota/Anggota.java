/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author HaddyPuutraa (PKL) Create Kamis, 21 Pebruari 2013
 */
public class Anggota extends Entity {

    private String noAnggota = "";
    private String name = "";
    private int sex = 0;
    private String birthPlace = "";
    private Date birthDate = null;
    private String agencies = "";
    private String officeAddress = "";

    private String addressPermanent = "";
    private String idCard = "";
    private Date expiredDateKtp = null;
    private String telepon = "";
    private String handPhone = "";
    private String email = "";

    private Date tanggalRegistrasi = new Date();
    
    private Date lastUpdate = null;
	

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    //tambahan tanggal 26 Pebruari 2013
    private String noNpwp = "";
    private int status;

    //update tanggal 27 Pebruari 2013 oleh Hadi
    private long addressOfficeCity;

    //update tanggal 5 maret 2013 oleh Hadi
    private String noRekening = "";

    //join dengan tb_vocation
    private long vocationId = 0;

    //join dengan tb_position
    private long positionId = 0;

    //join dengan tb_city
    private long addressCityPermanentId = 0;

    //join dengan tb_province
    private long addressProvinceId = 0;

    //join dengan tb_regency
    private long addressPermanentRegencyId = 0;

    //join dengan tb_subregency
    private long addressPermanentSubRegencyId = 0;

    //join dengan tb_ward
    private long wardId = 0;

    //update tanggal 8 Maret oleh Hadi untu geoAddressPermanent Anggota
    private String geoAddressPermanent = "";

    //update tanggal 4 July 2017 by dewok++
    private int hubunganKeluarga = 0;
    private String keteranganKeluarga = "";
    private long relasiId = 0;

    //update tanggal 11 July 2017 by dewok++
    private long penjaminId = 0;
    
    private String noKartuKeluarga = "";
    private String fotoAnggota = "";
	private long assignLocation = 0;

    public Anggota() {
    }

    /**
     * @return the noAnggota
     */
    public String getNoAnggota() {
        return noAnggota;
    }

    /**
     * @param name the name to set
     */
    public void setNoAnggota(String noAnggota) {
        if (noAnggota == null) {
            noAnggota = "";
        }
        this.noAnggota = noAnggota;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
    }

    /**
     * @return the sex
     */
    public int getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * @return the birthPlace
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * @param birthPlace the birthPlace to set
     */
    public void setBirthPlace(String birthPlace) {
        if (birthPlace == null) {
            birthPlace = "";
        }
        this.birthPlace = birthPlace;
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return the agencies
     */
    public String getAgencies() {
        return agencies;
    }

    /**
     * @param agencies the agencies to set
     */
    public void setAgencies(String agencies) {
        if (agencies == null) {
            agencies = "";
        }
        this.agencies = agencies;
    }

    /**
     * @return the officeAddress
     */
    public String getOfficeAddress() {
        return officeAddress;
    }

    /**
     * @param officeAddress the officeAddress to set
     */
    public void setOfficeAddress(String officeAddress) {
        if (officeAddress == null) {
            officeAddress = "";
        }
        this.officeAddress = officeAddress;
    }

    /**
     * @return the addressOfficeCity
     */
    public long getAddressOfficeCity() {
        return addressOfficeCity;
    }

    /**
     * @param addressOfficeCity the addressOfficeCity to set
     */
    public void setAddressOfficeCity(long addressOfficeCityId) {
        this.addressOfficeCity = addressOfficeCityId;
    }

    /**
     * @return the addressPermanent
     */
    public String getAddressPermanent() {
        return addressPermanent;
    }

    /**
     * @param addressPermanent the addressPermanent to set
     */
    public void setAddressPermanent(String addressPermanent) {
        if (addressPermanent == null) {
            addressPermanent = "";
        }
        this.addressPermanent = addressPermanent;
    }

    /**
     * @return the idCard
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * @param idCard the idCard to set
     */
    public void setIdCard(String idCard) {
        if (idCard == null) {
            idCard = "";
        }
        this.idCard = idCard;
    }

    /**
     * @return the expiredDateKtp
     */
    public Date getExpiredDateKtp() {
        return expiredDateKtp;
    }

    /**
     * @param expiredDateKtp the expiredDateKtp to set
     */
    public void setExpiredDateKtp(Date expiredDateKtp) {
        this.expiredDateKtp = expiredDateKtp;
    }

    /**
     * @return the telepon
     */
    public String getTelepon() {
        return telepon;
    }

    /**
     * @param telepon the telepon to set
     */
    public void setTelepon(String telepon) {
        if (telepon == null) {
            telepon = "";
        }
        this.telepon = telepon;
    }

    /**
     * @return the handPhone
     */
    public String getHandPhone() {
        return handPhone;
    }

    /**
     * @param handPhone the handPhone to set
     */
    public void setHandPhone(String handPhone) {
        if (handPhone == null) {
            handPhone = "";
        }
        this.handPhone = handPhone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        if (email == null) {
            email = "";
        }
        this.email = email;
    }

    /**
     * @return the vocationId
     */
    public long getVocationId() {
        return vocationId;
    }

    /**
     * @param vocationId the vocationId to set
     */
    public void setVocationId(long vocationId) {
        this.vocationId = vocationId;
    }

    /**
     * @return the positionId
     */
    public long getPositionId() {
        return positionId;
    }

    /**
     * @param positionId the positionId to set
     */
    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    /**
     * @return the addressCityPermanentId
     */
    public long getAddressCityPermanentId() {
        return addressCityPermanentId;
    }

    /**
     * @param addressCityPermanentId the addressCityPermanentId to set
     */
    public void setAddressCityPermanentId(long addressCityPermanentId) {
        this.addressCityPermanentId = addressCityPermanentId;
    }

    /**
     * @return the addressProvinceId
     */
    public long getAddressProvinceId() {
        return addressProvinceId;
    }

    /**
     * @param addressProvinceId the addressProvinceId to set
     */
    public void setAddressProvinceId(long addressProvinceId) {
        this.addressProvinceId = addressProvinceId;
    }

    /**
     * @return the addressPermanentRegencyId
     */
    public long getAddressPermanentRegencyId() {
        return addressPermanentRegencyId;
    }

    /**
     * @param addressPermanentRegencyId the addressPermanentRegencyId to set
     */
    public void setAddressPermanentRegencyId(long addressPermanentRegencyId) {
        this.addressPermanentRegencyId = addressPermanentRegencyId;
    }

    /**
     * @return the addressPermanentSubRegencyId
     */
    public long getAddressPermanentSubRegencyId() {
        return addressPermanentSubRegencyId;
    }

    /**
     * @param addressPermanentSubRegencyId the addressPermanentSubRegencyId to
     * set
     */
    public void setAddressPermanentSubRegencyId(long addressPermanentSubRegencyId) {
        this.addressPermanentSubRegencyId = addressPermanentSubRegencyId;
    }

    /**
     * @return the wardId
     */
    public long getWardId() {
        return wardId;
    }

    /**
     * @param wardId the wardId to set
     */
    public void setWardId(long wardId) {
        this.wardId = wardId;
    }

    //tambahan tanggal 26 Pebruari 2013
    /**
     * @return the noNpwp
     */
    public String getNoNpwp() {
        return noNpwp;
    }

    /**
     * @param noNpwp the noNpwp to set
     */
    public void setNoNpwp(String noNpwp) {
        this.noNpwp = noNpwp;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    //tambahn tanggal 5 maret
    /**
     * @return the noRekening
     */
    public String getNoRekening() {
        return noRekening;
    }

    /**
     * @param noRekening the noRekening to set
     */
    public void setNoRekening(String noRekening) {
        if (noRekening == null) {
            noRekening = "";
        }
        this.noRekening = noRekening;
    }

    /**
     * @return the geoAddressPermanent
     */
    public String getGeoAddressPermanent() {
        return geoAddressPermanent;
    }

    /**
     * @param geoAddressPermanent the geoAddressPermanent to set
     */
    public void setGeoAddressPermanent(String geoAddressPermanent) {
        if (geoAddressPermanent == null) {
            geoAddressPermanent = "";
        }
        this.geoAddressPermanent = geoAddressPermanent;
    }

    /**
     * @return the tanggalRegistrasi
     */
    public Date getTanggalRegistrasi() {
        return tanggalRegistrasi;
    }

    /**
     * @param tanggalRegistrasi the tanggalRegistrasi to set
     */
    public void setTanggalRegistrasi(Date tanggalRegistrasi) {
        this.tanggalRegistrasi = tanggalRegistrasi;
    }

    public int getHubunganKeluarga() {
        return hubunganKeluarga;
    }

    public void setHubunganKeluarga(int hubunganKeluarga) {
        this.hubunganKeluarga = hubunganKeluarga;
    }

    public String getKeteranganKeluarga() {
        return keteranganKeluarga;
    }

    public void setKeteranganKeluarga(String keteranganKeluarga) {
        this.keteranganKeluarga = keteranganKeluarga;
    }

    public long getRelasiId() {
        return relasiId;
    }

    public void setRelasiId(long relasiId) {
        this.relasiId = relasiId;
    }

    public long getPenjaminId() {
        return penjaminId;
    }

    public void setPenjaminId(long penjaminId) {
        this.penjaminId = penjaminId;
    }

    public String getNoKartuKeluarga() {
        return noKartuKeluarga;
    }

    public void setNoKartuKeluarga(String noKartuKeluarga) {
        this.noKartuKeluarga = noKartuKeluarga;
    }

    public String getFotoAnggota() {
        return fotoAnggota;
    }

    public void setFotoAnggota(String fotoAnggota) {
        this.fotoAnggota = fotoAnggota;
    }

	/**
	 * @return the assignLocation
	 */
	public long getAssignLocation() {
		return assignLocation;
	}

	/**
	 * @param assignLocation the assignLocation to set
	 */
	public void setAssignLocation(long assignLocation) {
		this.assignLocation = assignLocation;
	}

	
	
}
