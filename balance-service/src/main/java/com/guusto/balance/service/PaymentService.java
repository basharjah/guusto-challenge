package com.guusto.balance.service;

import com.guusto.balance.entity.UserBalance;
import com.guusto.balance.entity.UserCard;
import com.guusto.balance.entity.UserTransaction;
import com.guusto.balance.repository.UserBalanceRepository;
import com.guusto.balance.repository.UserCardRepository;
import com.guusto.balance.repository.UserTransactionRepository;
import com.guusto.common.dto.OrderRequestDto;
import com.guusto.common.dto.PaymentRequestDto;
import com.guusto.common.event.OrderEvent;
import com.guusto.common.event.PaymentEvent;
import com.guusto.common.event.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {
    @Autowired
    private UserCardRepository userCardRepository;
    @Autowired
    private UserBalanceRepository userBalanceRepository;
    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @PostConstruct
    public void initUserBalanceInDB() {
        userBalanceRepository.saveAll(Stream.of(new UserBalance(1, 100),
                new UserBalance(2, 0),
                new UserBalance(3, 90)).collect(Collectors.toList()));
    }
    @PostConstruct
    public  void initUserCardInDB(){
        userCardRepository.saveAll(Stream.of(new UserCard(1,0),
                new UserCard(2,0),
                new UserCard(3,0)).collect(Collectors.toList()));
    }

    /**
     * // get the user id
     * // check the balance availability
     * // if balance sufficient -> Payment completed and deduct amount price DB and add quantity of gift
     * // if payment not sufficient -> cancel card event and update the balance in DB
     **/
    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(),
                orderRequestDto.getUserId(), orderRequestDto.getAmount(),orderRequestDto.getQuantity());

        return userBalanceRepository.findById(orderRequestDto.getUserId())
                .filter(ub -> ub.getBalance() > orderRequestDto.getAmount()* orderRequestDto.getQuantity())
                .map(ub -> {
                    ub.setBalance(ub.getBalance() - orderRequestDto.getAmount()* orderRequestDto.getQuantity());
                    userCardRepository.findById(orderRequestDto.getUserId()).ifPresent(userCard -> userCard.setGift(userCard.getGift()+ orderRequestDto.getQuantity()));
                    userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(), orderRequestDto.getUserId(), orderRequestDto.getAmount(), orderRequestDto.getQuantity()));
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse( new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));


    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {

        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId())
                .ifPresent(ut->{
                    userTransactionRepository.delete(ut);
                    userTransactionRepository.findById(ut.getUserId())
                            .ifPresent(ub->{ub.setQuantity(ub.getQuantity()+ut.getQuantity());
                                                           ub.setAmount(ub.getAmount()+ut.getAmount());});
                });
    }
}
