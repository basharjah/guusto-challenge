package com.guusto.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private Integer userId;
    private Integer amount;
    private Integer quantity;
    private Integer orderId;
}
