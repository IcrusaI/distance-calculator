package DAO;

import model.DistanceEntity;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Singleton
@TransactionManagement(value= TransactionManagementType.BEAN)
public class DistanceDAOImpl implements DistanceDAO {

    private final static String FIND_ALL_DISTANCE_QUERY = "SELECT u FROM DistanceEntity u";

    @PersistenceUnit(unitName = "distanceCalculator")
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<DistanceEntity> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<DistanceEntity> distanceEntities = entityManager.createQuery(FIND_ALL_DISTANCE_QUERY, DistanceEntity.class).getResultList();

        entityManager.close();

        return distanceEntities;
    }

    @Override
    public List<DistanceEntity> findByFromAndTo(List<String> from, List<String> to) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<DistanceEntity> distanceCriteria = cb.createQuery(DistanceEntity.class);
        Root<DistanceEntity> distanceRoot = distanceCriteria.from(DistanceEntity.class);
        distanceCriteria.select(distanceRoot);

        ArrayList<Predicate> predicates = new ArrayList<>();
        for (int i = 0; i < from.size(); i++) {
            Predicate fromPredicate1 = cb.equal(distanceRoot.get("fromCity").get("name"), from.get(i));
            Predicate toPredicate1 = cb.equal(distanceRoot.get("toCity").get("name"), to.get(i));

            Predicate fromPredicate2 = cb.equal(distanceRoot.get("fromCity").get("name"), to.get(i));
            Predicate toPredicate2 = cb.equal(distanceRoot.get("toCity").get("name"), from.get(i));

            predicates.add(cb.or(cb.and(fromPredicate1, toPredicate1), cb.and(fromPredicate2, toPredicate2)));
        }

        distanceCriteria.where(cb.or(predicates.toArray(new Predicate[0])));

        List<DistanceEntity> distanceEntities = entityManager.createQuery(distanceCriteria)
                .getResultList();


        entityManager.close();

        return distanceEntities;
    }

    @Override
    public boolean insertDistances(List<DistanceEntity> distanceEntities) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        try {
            for (DistanceEntity distanceEntity : distanceEntities) {
                entityManager.persist(distanceEntity);
            }
            transaction.commit();

            entityManager.close();

            return true;

        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            entityManager.close();

            return false;
        }

    }
}
