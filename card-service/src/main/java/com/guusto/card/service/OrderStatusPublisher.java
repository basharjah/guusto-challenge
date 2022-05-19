package com.guusto.card.service;

import com.guusto.common.dto.OrderRequestDto;
import com.guusto.common.event.OrderEvent;
import com.guusto.common.event.OrderStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {

    private final Sinks.Many<OrderEvent> orderSinks;

    public OrderStatusPublisher(Sinks.Many<OrderEvent> orderSinks) {
        this.orderSinks = orderSinks;
    }


    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus){
        OrderEvent orderEvent=new OrderEvent(orderRequestDto,orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }
}
