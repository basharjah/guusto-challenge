package com.guusto.balance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction {
    @Id
    @Column
    private Integer orderId;
    @Column
    private int userId;
    @Column
    private int amount;
    @Column
    private int quantity;
}
