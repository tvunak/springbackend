package com.example.restexampletv.repositories;

import com.example.restexampletv.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    long getIdByUserId(long id);
    Basket getByUserId(long id);
}
