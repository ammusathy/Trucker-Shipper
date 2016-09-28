package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 5/7/2016.
 */
public class ShipmentResponse {

    private SourceResponse source;
    private DestinationResponse destinationResponse;

    public ShipmentResponse() {
    }

    public ShipmentResponse(SourceResponse source, DestinationResponse destinationResponse) {

        this.source = source;
        this.destinationResponse = destinationResponse;
    }

    public SourceResponse getSource() {

        return source;
    }

    public void setSource(SourceResponse source) {
        this.source = source;
    }

    public DestinationResponse getDestinationResponse() {
        return destinationResponse;
    }

    public void setDestinationResponse(DestinationResponse destinationResponse) {
        this.destinationResponse = destinationResponse;
    }
}
