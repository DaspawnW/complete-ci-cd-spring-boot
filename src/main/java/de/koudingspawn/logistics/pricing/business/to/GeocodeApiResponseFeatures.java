package de.koudingspawn.logistics.pricing.business.to;

public class GeocodeApiResponseFeatures {
    private String type;
    private GeocodeApiResponseFeaturesGeometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeocodeApiResponseFeaturesGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeocodeApiResponseFeaturesGeometry geometry) {
        this.geometry = geometry;
    }
}
