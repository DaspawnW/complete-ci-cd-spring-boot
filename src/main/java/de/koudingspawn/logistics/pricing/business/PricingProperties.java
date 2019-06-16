package de.koudingspawn.logistics.pricing.business;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pricing")
public class PricingProperties {

    private long fixPrice;
    private double kilometerPrice;
    private double minutePrice;

    public long getFixPrice() {
        return fixPrice;
    }

    public void setFixPrice(long fixPrice) {
        this.fixPrice = fixPrice;
    }

    public double getKilometerPrice() {
        return kilometerPrice;
    }

    public void setKilometerPrice(double kilometerPrice) {
        this.kilometerPrice = kilometerPrice;
    }

    public double getMinutePrice() {
        return minutePrice;
    }

    public void setMinutePrice(double minutePrice) {
        this.minutePrice = minutePrice;
    }
}
