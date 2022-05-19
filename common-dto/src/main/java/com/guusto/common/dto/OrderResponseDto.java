package com.guusto.common.dto;

import com.guusto.common.event.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Integer userId;
    private Integer orderId;
    private OrderStatus orderStatus;
}
