package de.koudingspawn.logistics.pricing.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PricingNotFoundException extends RuntimeException {

    public PricingNotFoundException() {
        super("No pricing available for the specified source and destination!");
    }
}
