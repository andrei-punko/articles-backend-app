package by.andd3dfx.persistence.dao;

import by.andd3dfx.dto.ArticleSearchCriteria;
import by.andd3dfx.dto.DeleteArticleSearchCriteria;
import by.andd3dfx.persistence.entities.Article;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public List<Article> findByCriteria(ArticleSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> plan = cq.from(Article.class);

        List<Predicate> predicates = buildPredicates(criteria, cb, plan);
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public boolean existsByCriteria(ArticleSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Article> plan = cq.from(Article.class);

        List<Predicate> predicates = buildPredicates(criteria, cb, plan);
        cq.select(cb.count(plan));
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult() > 0;
    }

    @Override
    public void deleteByCriteria(DeleteArticleSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Article> cq = cb.createCriteriaDelete(Article.class);
        Root<Article> plan = cq.from(Article.class);

        List<Predicate> predicates = buildPredicates(criteria, cb, plan);
        cq.where(predicates.toArray(new Predicate[0]));

        em.createQuery(cq).executeUpdate();
    }

    private List<Predicate> buildPredicates(ArticleSearchCriteria criteria, CriteriaBuilder cb, Root plan) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getTitle() != null) {
            predicates.add(cb.equal(plan.get("title"), criteria.getTitle()));
        }
        if (criteria.getSummary() != null) {
            predicates.add(cb.equal(plan.get("summary"), criteria.getSummary()));
        }
        if (criteria.getTimestamp() != null) {
            LocalDateTime timestamp = LocalDateTime.parse(criteria.getTimestamp());
            predicates.add(cb.greaterThan(plan.get("timestamp"), timestamp));
        }
        return predicates;
    }

    private List<Predicate> buildPredicates(DeleteArticleSearchCriteria criteria, CriteriaBuilder cb, Root plan) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getTitle() != null) {
            predicates.add(cb.equal(plan.get("title"), criteria.getTitle()));
        }
        return predicates;
    }
}
