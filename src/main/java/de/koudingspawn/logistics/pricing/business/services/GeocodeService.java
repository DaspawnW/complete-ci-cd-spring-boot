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
public class GeocodeService {

    private final String apiKey;
    private final RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(GeocodeService.class);

    public GeocodeService(@Value("${apiKey}") String apiKey, RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    public Optional<Coordinates> getCoordinates(Address address) {
        try {
            ResponseEntity<GeocodeApiResponse> response = restTemplate.getForEntity(requestUrl(address), GeocodeApiResponse.class);

            return extractCoordinates(response.getBody());
        } catch (RestClientException ex) {
            LOG.error("Geocode request failed", ex);
            return Optional.empty();
        }
    }

    private Optional<Coordinates> extractCoordinates(GeocodeApiResponse response) {
        if (response == null) {
            return Optional.empty();
        }

        GeocodeApiResponseFeatures[] features = response.getFeatures();
        if (features != null && features.length > 0) {
            GeocodeApiResponseFeaturesGeometry geometry = features[0].getGeometry();

            if (geometry != null && geometry.getCoordinates() != null) {
                return Optional.of(new Coordinates(geometry.getLat(), geometry.getLon()));
            }
        }

        return Optional.empty();
    }

    private String requestUrl(Address address) {
        String addressAsString = getAddress(address);
        return String.format("https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s&layers=address", apiKey, addressAsString);
    }

    private String getAddress(Address address) {
        return String.format("%s+%s+%s+%s", address.getStreet(), address.getZipCode(), address.getCity(), address.getCountry());
    }
}
