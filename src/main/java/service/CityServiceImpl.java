package service;

import DAO.CityDAO;
import DTO.CalculatedDistanceDTO;
import DTO.CityDTO;
import XML.CitiesXML;
import XML.CityXML;
import model.CalculatorMethod;
import model.CityEntity;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CityServiceImpl implements CityService {

    private static final Long EARTH_RADIUS = 6372795L;

    @EJB
    private CityDAO cityDAO;

    public List<CityDTO> getCities() {
        return convertEntitiesToCityDTO(cityDAO.findAll());
    }

    @Override
    public List<CityEntity> getCitiesByName(List<String> names) {
        return cityDAO.findByName(names);
    }

    public List<CalculatedDistanceDTO> getDistancesCrowflightMethod(List<String> fromCities, List<String> toCities) {
        List<CalculatedDistanceDTO> distanceDTOS = new ArrayList<>();

        List<CityEntity> cities = cityDAO.findByName(fromCities);

        for (int i = 0; i < fromCities.size(); i++) {
            CalculatedDistanceDTO distance = new CalculatedDistanceDTO();

            String fromName = fromCities.get(i);
            String toName = toCities.get(i);

            CityEntity from = findCityByCityList(fromName, cities);
            CityEntity to = findCityByCityList(toName, cities);

            distance.setMethod(CalculatorMethod.CROWFLIGHT);

            try {
                distance.setDistance(calculateCrowflight(from, to));

                distance.setFrom(from.getName());
                distance.setTo(to.getName());
            } catch (Exception e) {
                distance.setFrom(fromName);
                distance.setTo(toName);
            }

            distanceDTOS.add(distance);
        }

        return distanceDTOS;
    }

    public Response insertCities(MultipartFormDataInput form) {
        CitiesXML citiesXML;
        try {
            InputStream inputStream = form.getFormDataPart("cities", InputStream.class, null);

            citiesXML = JAXB.unmarshal(inputStream, CitiesXML.class);
        } catch (Exception e) {
            return Response.status(400).entity("Invalid data").build();
        }

        List<CityEntity> cities = convertCitiesXMLToEntity(citiesXML.getCity());

        if (!cityDAO.insertCities(cities)) {
            return Response.status(400).entity("Failed to write data").build();
        }

        return Response.ok().build();
    }

    /** Getting the distance between cities, using the crowflight method
     *
     * @param from
     * @param to
     * @return
     */
    private Double calculateCrowflight(CityEntity from, CityEntity to) {
        double latFrom = coordinateToRadians(from.getLatitude());
        double longFrom = coordinateToRadians(from.getLongitude());
        double latTo = coordinateToRadians(to.getLatitude());
        double longTo = coordinateToRadians(to.getLongitude());

        return EARTH_RADIUS * Math.acos(Math.sin(latFrom) * Math.sin(latTo) + Math.cos(latFrom) * Math.cos(latTo) * Math.cos(longTo - longFrom));
    }

    /** coordinate to radians
     *
     * @param coordinate
     * @return
     */
    private Double coordinateToRadians(Double coordinate) {
        return coordinate * Math.PI / 180;
    }

    /** find city by city list
     *
     * @param name
     * @param cities
     * @return
     */
    private static CityEntity findCityByCityList(String name, List<CityEntity> cities) {
        for (CityEntity city : cities) {
            if (city.getName().equalsIgnoreCase(name)) {
                return city;
            }
        }
        return null;
    }

    public List<CityDTO> convertEntitiesToCityDTO(List<CityEntity> cityEntities) {
        List<CityDTO> cityDTOS = new ArrayList<>();

        for (CityEntity cityEntity : cityEntities) {
            cityDTOS.add(convertEntityToCityDTO(cityEntity));
        }

        return cityDTOS;
    }

    private CityDTO convertEntityToCityDTO(CityEntity cityEntity) {
        CityDTO cityDTO = new CityDTO();

        cityDTO.setId(cityEntity.getId());
        cityDTO.setName(cityEntity.getName());

        return cityDTO;
    }

    private List<CityEntity> convertCitiesXMLToEntity(List<CityXML> citiesDTO) {
        List<CityEntity> cities = new ArrayList<>();

        for (CityXML cityXML : citiesDTO) {
            cities.add(convertCityXMLToEntity(cityXML));
        }

        return cities;
    }

    private CityEntity convertCityXMLToEntity(CityXML cityDTO) {
        CityEntity cityEntity = new CityEntity();

        cityEntity.setName(cityDTO.getName());
        cityEntity.setLatitude(cityDTO.getLatitude());
        cityEntity.setLongitude(cityDTO.getLongitude());

        return cityEntity;
    }
}
