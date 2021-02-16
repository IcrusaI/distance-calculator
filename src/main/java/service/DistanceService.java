package service;

import DTO.CalculatedDistanceDTO;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.core.Response;
import java.util.List;

public interface DistanceService {
    Response calculateDistance(String method, List<String> fromCities, List<String> toCities);

    /** Getting the distance between cities, using the matrix method
     *
     * @param fromCities
     * @param toCities
     * @return
     */
    List<CalculatedDistanceDTO> getDistancesMatrixMethod(List<String> fromCities, List<String> toCities);

    Response insertCities(MultipartFormDataInput form);
}
