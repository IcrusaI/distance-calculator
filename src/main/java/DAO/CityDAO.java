package DAO;

import model.CityEntity;

import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
public interface CityDAO {

    /** Getting all cities
     *
     * @return
     */
    List<CityEntity> findAll();

    /** Search by city name
     *
     * @param name
     * @return
     */
    List<CityEntity> findByName(List<String> name);

    /** Adding multiple cities
     *
     * @param cityEntities
     */
    boolean insertCities(List<CityEntity> cityEntities);
}
