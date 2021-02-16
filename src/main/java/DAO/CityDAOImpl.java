package DAO;

import model.CityEntity;
import org.hibernate.HibernateException;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Singleton
@TransactionManagement(value= TransactionManagementType.BEAN)
public class CityDAOImpl implements CityDAO {

    private final static String FIND_ALL_CITY_QUERY = "SELECT u FROM CityEntity u";
    private final static String FIND_BY_CITY_NAME_QUERY = "SELECT u FROM CityEntity u WHERE u.name in (:names)";

    @PersistenceUnit(unitName = "distanceCalculator")
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<CityEntity> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<CityEntity> cityEntities = entityManager.createQuery(FIND_ALL_CITY_QUERY, CityEntity.class).getResultList();

        entityManager.close();

        return cityEntities;
    }

    @Override
    public List<CityEntity> findByName(List<String> names) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<CityEntity> cityEntities = entityManager
                .createQuery(FIND_BY_CITY_NAME_QUERY, CityEntity.class)
                .setParameter("names", names)
                .getResultList();

        entityManager.close();

        return cityEntities;
    }

    @Override
    public boolean insertCities(List<CityEntity> cityEntities) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        try {

            for (CityEntity city : cityEntities) {
                entityManager.persist(city);
            }

            transaction.commit();

            entityManager.close();

            return true;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            entityManager.close();

            return false;
        }
    }
}
