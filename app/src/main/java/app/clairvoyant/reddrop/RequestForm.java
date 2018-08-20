package app.clairvoyant.reddrop;

public class RequestForm {
    private String contactPersonName, contactPersonPhone, donationAddress, bloodGroup, unitsOfBlood;

    public RequestForm() {
    }

    public RequestForm(String contactPersonName, String contactPersonPhone, String donationAddress, String bloodGroup, String unitsOfBlood) {
        this.contactPersonName = contactPersonName;
        this.contactPersonPhone = contactPersonPhone;
        this.donationAddress = donationAddress;
        this.bloodGroup = bloodGroup;
        this.unitsOfBlood = unitsOfBlood;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getDonationAddress() {
        return donationAddress;
    }

    public void setDonationAddress(String donationAddress) {
        this.donationAddress = donationAddress;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUnitsOfBlood() {
        return unitsOfBlood;
    }

    public void setUnitsOfBlood(String unitsOfBlood) {
        this.unitsOfBlood = unitsOfBlood;
    }
}
