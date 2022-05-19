package com.guusto.card.controller;

import com.guusto.card.entity.PurchaseOrder;
import com.guusto.card.service.OrderService;
import com.guusto.common.dto.OrderRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guusto-service")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/buy-gift")
    public List<PurchaseOrder> createOrder(@RequestBody OrderRequestDto orderRequestDto){
         orderService.createOrder(orderRequestDto);
        return getOrders();
    }

    @GetMapping
    public List<PurchaseOrder> getOrders(){
        return orderService.getAllOrders();
    }
}
