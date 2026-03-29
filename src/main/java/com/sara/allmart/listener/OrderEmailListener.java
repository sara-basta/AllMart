package com.sara.allmart.listener;

import com.sara.allmart.entity.Order;
import com.sara.allmart.event.OrderStatusEvent;
import com.sara.allmart.repository.OrderRepository;
import com.sara.allmart.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEmailListener {

    private final EmailService emailService;
    private final OrderRepository orderRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleOrderStatusEvent(OrderStatusEvent event) {

        Order order = orderRepository.findByIdWithUser(event.getOrder().getId()).orElse(null);

        if (order == null) {
            log.error("Could not find order to send email!");
            return;
        }

        String status = event.getStatusType();

        log.info("Async Thread caught event: Order #{} changed to {}", order.getId(), status);

        switch (status) {
            case "CONFIRMED", "PAID":
                emailService.sendOrderConfirmation(order);
                break;
            case "SHIPPED":
                emailService.sendShippingUpdate(order);
                break;
            case "DELIVERED":
                emailService.sendDeliveryConfirmation(order);
                break;
            case "CANCELLED":
                emailService.sendCancellationNotice(order);
                break;
            default:
                log.warn("No email configured for status: {}", status);
        }
    }
}