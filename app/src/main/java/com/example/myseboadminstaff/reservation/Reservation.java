package com.example.myseboadminstaff.reservation;

import com.example.myseboadminstaff.asset.Asset;
import com.google.firebase.Timestamp;

import java.util.List;

public class Reservation {
    private Timestamp dateEnd;
    private Timestamp dateStart;

    private List<Asset> equipment;

    private String eventName;
    private String eventOrganization;
    private String id;
    private String userId;
    private String phoneNumber;

    private String pickUpName;
    private String pickUpId;
    private String pickUpPhone;
    private Timestamp pickUpDate;

    private String returnName;
    private String returnIdStaff;
    private String returnPhone;
    private String note;
    private Timestamp returnTheDate;

    private int status;

    private String letterPath;
    private String idPath;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_REJECT = 1;
    public static final int STATUS_ACCEPT = 2;
    public static final int STATUS_PICKUP = 3;
    public static final int STATUS_RETURN = 4;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Timestamp dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Timestamp getDateStart() {
        return dateStart;
    }

    public void setDateStart(Timestamp dateStart) {
        this.dateStart = dateStart;
    }

    public List<Asset> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Asset> equipment) {
        this.equipment = equipment;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventOrganization() {
        return eventOrganization;
    }

    public void setEventOrganization(String eventOrganization) {
        this.eventOrganization = eventOrganization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPickUpName() {
        return pickUpName;
    }

    public void setPickUpName(String pickUpName) {
        this.pickUpName = pickUpName;
    }

    public String getPickUpId() {
        return pickUpId;
    }

    public void setPickUpId(String pickUpId) {
        this.pickUpId = pickUpId;
    }

    public String getPickUpPhone() {
        return pickUpPhone;
    }

    public void setPickUpPhone(String pickUpPhone) {
        this.pickUpPhone = pickUpPhone;
    }

    public Timestamp getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Timestamp pickUpDate) {
        this.pickUpDate = pickUpDate;
    }


    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    public String getReturnPhone() {
        return returnPhone;
    }

    public void setReturnPhone(String returnPhone) {
        this.returnPhone = returnPhone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReturnIdStaff() {
        return returnIdStaff;
    }

    public void setReturnIdStaff(String returnIdStaff) {
        this.returnIdStaff = returnIdStaff;
    }

    public Timestamp getReturnTheDate() {
        return returnTheDate;
    }

    public void setReturnTheDate(Timestamp returnTheDate) {
        this.returnTheDate = returnTheDate;
    }

    public String getLetterPath() {
        return letterPath;
    }

    public void setLetterPath(String letterPath) {
        this.letterPath = letterPath;
    }

    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }
}
