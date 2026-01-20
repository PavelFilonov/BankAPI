package com.example.bankapi.repository;

import com.example.bankapi.entity.Card;
import com.example.bankapi.entity.Card_;
import com.example.bankapi.entity.auth.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends BaseRepository<Card> {

    static Specification<Card> byOwnerIdSpec(Long ownerId) {
        return (root, query, cb) -> cb.equal(root.join(Card_.OWNER).get(User_.ID), ownerId);
    }

    static Specification<Card> byOwnerLoginSpec(String ownerLogin) {
        return (root, query, cb) -> cb.equal(root.join(Card_.OWNER).get(User_.LOGIN), ownerLogin);
    }

}

