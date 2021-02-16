package service;

import DAO.DistanceDAO;
import DTO.CalculatedDistanceDTO;
import XML.CitiesXML;
import XML.CityXML;
import XML.DistanceXML;
import XML.DistancesXML;
import model.CalculatorMethod;
import model.CityEntity;
import model.DistanceEntity;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DistanceServiceImpl implements DistanceService {
    @EJB
    private DistanceDAO distanceDAO;

    @EJB
    private CityService cityService;

    public Response calculateDistance(String method, List<String> fromCities, List<String> toCities) {
        CalculatorMethod calculatorMethod;

        try {
            calculatorMethod = CalculatorMethod.getByType(method);
        } catch (IllegalArgumentException e) {
            return javax.ws.rs.core.Response.status(400)
                    .entity("Incorrect method. Support: crowflight, distance_matrix, all.")
                    .build();
        }

        if (fromCities.size() == 0 || toCities.size() == 0) {
            return javax.ws.rs.core.Response.status(400).entity("Incorrect list cities. Required minimum 1 city.").build();
        }

        if (fromCities.size() != toCities.size()) {
            return javax.ws.rs.core.Response.status(400).entity("Incorrect list cities. fromCity and toCity must be the same length.")
                    .build();
        }

        List<CalculatedDistanceDTO> distances = new ArrayList<>();
        switch (calculatorMethod) {
            case CROWFLIGHT:
                distances.addAll(cityService.getDistancesCrowflightMethod(fromCities, toCities));
                break;
            case DISTANCE_MATRIX:
                distances.addAll(getDistancesMatrixMethod(fromCities, toCities));
                break;
            case ALL:
                distances.addAll(getDistancesMatrixMethod(fromCities, toCities));
                distances.addAll(cityService.getDistancesCrowflightMethod(fromCities, toCities));
                break;
        }

        return javax.ws.rs.core.Response.status(200)
                .entity(distances)
                .build();
    }

    public List<CalculatedDistanceDTO> getDistancesMatrixMethod(List<String> fromCities, List<String> toCities) {
        List<CalculatedDistanceDTO> distanceDTOS = new ArrayList<>();

        List<DistanceEntity> distances = distanceDAO.findByFromAndTo(fromCities, toCities);

        for (int i = 0; i < fromCities.size(); i++) {
            CalculatedDistanceDTO distance = new CalculatedDistanceDTO();

            String fromName = fromCities.get(i);
            String toName = toCities.get(i);

            DistanceEntity distanceEntity = findDistanceByCityNamesList(fromName, toName, distances);

            distance.setMethod(CalculatorMethod.DISTANCE_MATRIX);

            try {
                distance.setDistance(distanceEntity.getDistance());

                distance.setFrom(distanceEntity.getFromCity().getName());
                distance.setTo(distanceEntity.getToCity().getName());
            } catch (Exception e) {
                distance.setFrom(fromName);
                distance.setTo(toName);
            }

            distanceDTOS.add(distance);
        }

        return distanceDTOS;
    }

    public Response insertCities(MultipartFormDataInput form) {
        DistancesXML distancesXML;
        try {
            InputStream inputStream = form.getFormDataPart("distances", InputStream.class, null);

            distancesXML = JAXB.unmarshal(inputStream, DistancesXML.class);
        } catch (Exception e) {
            return Response.status(400).entity("Invalid data").build();
        }

        List<DistanceEntity> distances = convertDistancesXMLToEntity(distancesXML.getDistance());

        if (!distanceDAO.insertDistances(distances)) {
            return Response.status(400).entity("Failed to write data").build();
        }

        return Response.ok().build();
    }
    private static DistanceEntity findDistanceByCityNameList(String name, List<DistanceEntity> distanceEntities) {
        for (DistanceEntity distance : distanceEntities) {
            if (distance.getFromCity().getName().equalsIgnoreCase(name) || distance.getToCity().getName().equalsIgnoreCase(name)) {
                return distance;
            }
        }
        return null;
    }

    private static DistanceEntity findDistanceByCityNamesList(String fromName, String toName, List<DistanceEntity> distanceEntities) {
        for (DistanceEntity distance : distanceEntities) {
            if (distance.getFromCity().getName().equalsIgnoreCase(fromName) && distance.getToCity().getName().equalsIgnoreCase(toName)) {
                return distance;
            } else if (distance.getFromCity().getName().equalsIgnoreCase(toName) && distance.getToCity().getName().equalsIgnoreCase(fromName)) {
                return distance;
            }
        }
        return null;
    }

    private List<DistanceEntity> convertDistancesXMLToEntity(List<DistanceXML> distanceXMLS) {
        List<DistanceEntity> distances = new ArrayList<>();

        List<String> citiesList = new ArrayList<>();

        for (DistanceXML distanceXML : distanceXMLS) {
            citiesList.add(distanceXML.getFromCity());
            citiesList.add(distanceXML.getToCity());
        }

        List<CityEntity> cityEntities = cityService.getCitiesByName(citiesList);

        for (DistanceXML distanceXML : distanceXMLS) {
            DistanceEntity distanceEntity = new DistanceEntity();

            for (CityEntity cityEntity : cityEntities) {
                if (cityEntity.getName().equalsIgnoreCase(distanceXML.getFromCity())) {
                    distanceEntity.setFromCity(cityEntity);
                }
                if (cityEntity.getName().equalsIgnoreCase(distanceXML.getToCity())) {
                    distanceEntity.setToCity(cityEntity);
                }

                if (distanceEntity.getToCity() != null && distanceEntity.getFromCity() != null) {
                    break;
                }
            }
            distanceEntity.setDistance(distanceXML.getDistance());

            distances.add(distanceEntity);
        }

        return distances;
    }
}
