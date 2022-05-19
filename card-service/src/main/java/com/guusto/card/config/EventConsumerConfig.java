package com.guusto.card.config;

import com.guusto.common.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler handler;


    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer(){
        //listen payment-event-topic
        //will check payment status
        //if payment status completed -> complete the card
        //if payment status failed -> cancel the card

        return (paymentEvent)-> handler.updateOrder(paymentEvent.getPaymentRequestDto().getOrderId(),po->{
            po.setPaymentStatus(paymentEvent.getPaymentStatus());
        });
    }
}
