package de.koudingspawn.logistics.pricing.business.api;

import de.koudingspawn.logistics.pricing.business.exception.PricingNotFoundException;
import de.koudingspawn.logistics.pricing.business.services.PricingService;
import de.koudingspawn.logistics.pricing.business.to.PricingRequest;
import de.koudingspawn.logistics.pricing.business.to.PricingResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/pricing")
public class PricingApi {

    private final PricingService pricingService;

    public PricingApi(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping
    public PricingResponse calculatePrice(@RequestBody PricingRequest pricingRequest) {
        return pricingService.calculatePrice(pricingRequest).orElseThrow(PricingNotFoundException::new);
    }

}
