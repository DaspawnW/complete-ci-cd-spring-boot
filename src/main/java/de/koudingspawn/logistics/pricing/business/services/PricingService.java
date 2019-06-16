package de.koudingspawn.logistics.pricing.business.services;

import de.koudingspawn.logistics.pricing.business.PricingProperties;
import de.koudingspawn.logistics.pricing.business.to.Coordinates;
import de.koudingspawn.logistics.pricing.business.to.Driving;
import de.koudingspawn.logistics.pricing.business.to.PricingRequest;
import de.koudingspawn.logistics.pricing.business.to.PricingResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PricingService {

    private final PricingProperties pricingProperties;
    private final DistanceCalculationService distanceCalculationService;
    private final GeocodeService geocodeService;

    public PricingService(PricingProperties pricingProperties, DistanceCalculationService distanceCalculationService, GeocodeService geocodeService) {
        this.pricingProperties = pricingProperties;
        this.distanceCalculationService = distanceCalculationService;
        this.geocodeService = geocodeService;
    }

    public Optional<PricingResponse> calculatePrice(PricingRequest pricingRequest) {

        Optional<Coordinates> sourceCoordinates = geocodeService.getCoordinates(pricingRequest.getSource());
        Optional<Coordinates> destCoordinates = geocodeService.getCoordinates(pricingRequest.getDestination());

        if (sourceCoordinates.isPresent() && destCoordinates.isPresent()) {
            Optional<Driving> driving = distanceCalculationService.calculateDistance(sourceCoordinates.get(), destCoordinates.get());

            if (driving.isPresent()) {
                return Optional.of(mapToResponse(pricingRequest, driving.get()));
            }
        }

        return Optional.empty();
    }

    private PricingResponse mapToResponse(PricingRequest pricingRequest, Driving driving) {
        PricingResponse pricingResponse = new PricingResponse();
        pricingResponse.setDestination(pricingRequest.getDestination());
        pricingResponse.setSource(pricingRequest.getSource());
        pricingResponse.setDriving(driving);
        pricingResponse.setPrice(calculatePrice(driving));

        return pricingResponse;
    }

    private double calculatePrice(Driving driving) {
        return  driving.getDistanceInKilometer() * pricingProperties.getKilometerPrice() +
                driving.getDuration() * pricingProperties.getMinutePrice() +
                pricingProperties.getFixPrice();
    }
}
