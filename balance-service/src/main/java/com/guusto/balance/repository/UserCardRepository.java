package com.guusto.balance.repository;

import com.guusto.balance.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardRepository extends JpaRepository<UserCard,Integer> {
}
