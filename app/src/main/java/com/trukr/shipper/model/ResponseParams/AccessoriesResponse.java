package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 5/7/2016.
 */
public class AccessoriesResponse {
    private String StopsinTransit, NoOfStraps, HazardousMaterial, BorderCrossing;

    public AccessoriesResponse() {
    }

    public AccessoriesResponse(String stopsinTransit, String noOfStraps, String hazardousMaterial, String borderCrossing) {

        StopsinTransit = stopsinTransit;
        NoOfStraps = noOfStraps;
        HazardousMaterial = hazardousMaterial;
        BorderCrossing = borderCrossing;
    }

    public String getStopsinTransit() {

        return StopsinTransit;
    }

    public void setStopsinTransit(String stopsinTransit) {
        StopsinTransit = stopsinTransit;
    }

    public String getNoOfStraps() {
        return NoOfStraps;
    }

    public void setNoOfStraps(String noOfStraps) {
        NoOfStraps = noOfStraps;
    }

    public String getHazardousMaterial() {
        return HazardousMaterial;
    }

    public void setHazardousMaterial(String hazardousMaterial) {
        HazardousMaterial = hazardousMaterial;
    }

    public String getBorderCrossing() {
        return BorderCrossing;
    }

    public void setBorderCrossing(String borderCrossing) {
        BorderCrossing = borderCrossing;
    }
}
