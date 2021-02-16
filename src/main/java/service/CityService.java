package service;

import DTO.CalculatedDistanceDTO;
import DTO.CityDTO;
import model.CityEntity;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.core.Response;
import java.util.List;

public interface CityService {
    /** Getting all cities
     *
     * @return
     */
    List<CityDTO> getCities();

    List<CityEntity> getCitiesByName(List<String> names);

    /** Getting the distance between cities, using the crowflight method
     *
     * @param fromCities
     * @param toCities
     * @return
     */
    List<CalculatedDistanceDTO> getDistancesCrowflightMethod(List<String> fromCities, List<String> toCities);

    /** Adding multiple cities
     *
     * @param form
     * @return
     */
    Response insertCities(MultipartFormDataInput form);
}
