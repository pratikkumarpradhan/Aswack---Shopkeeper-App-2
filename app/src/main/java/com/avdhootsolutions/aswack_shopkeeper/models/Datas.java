package com.avdhootsolutions.aswack_shopkeeper.models;

import com.google.gson.annotations.SerializedName;
import com.google.protobuf.Any;

import java.util.ArrayList;

public class Datas {
    @SerializedName("status")
    Boolean status = false;

    @SerializedName("message")
    String message = "";

    @SerializedName("data")
    ArrayList data = new ArrayList<Any>();

    @SerializedName("review")
    ReviewClass reviewClass = new ReviewClass();

    public ReviewClass getReviewClass() {
        return reviewClass;
    }

    public void setReviewClass(ReviewClass reviewClass) {
        this.reviewClass = reviewClass;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList<Any> data) {
        this.data = data;
    }
}
