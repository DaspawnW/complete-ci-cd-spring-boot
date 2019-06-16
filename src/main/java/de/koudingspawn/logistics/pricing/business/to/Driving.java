package de.koudingspawn.logistics.pricing.business.to;

public class Driving {

    private double distance;
    private double duration;

    public Driving(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getDistanceInKilometer() {
        return distance / 1000;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public double getDurationInMinutes() {
        return duration / 60;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
