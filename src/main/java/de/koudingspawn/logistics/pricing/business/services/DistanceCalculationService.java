package de.koudingspawn.logistics.pricing.business.services;

import de.koudingspawn.logistics.pricing.business.to.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class DistanceCalculationService {

    private final String apiKey;
    private final RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(DistanceCalculationService.class);


    public DistanceCalculationService(@Value("${apiKey}") String apiKey, RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    public Optional<Driving> calculateDistance(Coordinates start, Coordinates destination) {
        try {
            String url = requestUrl(start, destination);
            ResponseEntity<DirectionsResponse> response = restTemplate.getForEntity(url, DirectionsResponse.class);

            return extractDriving(response.getBody());
        } catch (RestClientException ex) {
            LOG.error("Failed to calculate distance", ex);
            return Optional.empty();
        }
    }

    private String requestUrl(Coordinates start, Coordinates destination) {
        String startDest = String.format("%s,%s|%s,%s", start.getLat(), start.getLon(), destination.getLat(), destination.getLon());

        return String.format("https://api.openrouteservice.org/directions?api_key=%s&coordinates=%s&profile=driving-car&units=m", apiKey, startDest);
    }

    private Optional<Driving> extractDriving(DirectionsResponse response) {
        if (response == null) {
            return Optional.empty();
        }

        DirectionsResponseRoute[] routes = response.getRoutes();
        if (routes != null && routes.length > 0) {
            DirectionsResponseRouteSummary summary = routes[0].getSummary();
            if (summary != null) {
                return Optional.of(new Driving(summary.getDistance(), summary.getDuration()));
            }
        }

        return Optional.empty();
    }


}
