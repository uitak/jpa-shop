package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * JPA Criteria
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> order = query.from(Order.class);
        Join<Object, Object> member = order.join("member", JoinType.INNER);

        //주문 상태 검색
        List<Predicate> criteria = new ArrayList<>();
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(order.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(member.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        //query.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        query.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Order> orderTypedQuery = em.createQuery(query).setMaxResults(1000);
        return orderTypedQuery.getResultList();
    }
}
