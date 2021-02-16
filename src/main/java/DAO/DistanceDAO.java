package DAO;

import model.DistanceEntity;

import java.util.List;

public interface DistanceDAO {
    /** Getting all the distances
     *
     * @return
     */
    List<DistanceEntity> findAll();

    /** Search for the distance between cities by city name
     *
     * @param from
     * @param to
     * @return
     */
    List<DistanceEntity> findByFromAndTo(List<String> from, List<String> to);

    /** Adding multiple distances.
     *
     * @param distanceEntities
     */
    boolean insertDistances(List<DistanceEntity> distanceEntities);
}
