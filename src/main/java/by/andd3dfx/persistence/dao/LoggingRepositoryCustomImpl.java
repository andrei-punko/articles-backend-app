package by.andd3dfx.persistence.dao;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.helper.DateTimeHelper;
import by.andd3dfx.persistence.entities.LoggedRecord;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoggingRepositoryCustomImpl implements LoggingRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public List<LoggedRecord> findByCriteria(LoggingSearchCriteria criteria) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LoggedRecord> cq = builder.createQuery(LoggedRecord.class);
        Root<LoggedRecord> from = cq.from(LoggedRecord.class);

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getTimestamp() != null) {
            LocalDateTime timestamp = DateTimeHelper.convertZStringToLocalDateTime(criteria.getTimestamp());
            predicates.add(builder.greaterThan(from.get("timestamp"), timestamp));
        }

        final List<Order> orderList = Arrays.asList(builder.desc(from.get("timestamp")));
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(orderList);

        return em.createQuery(cq).getResultList();
    }
}
