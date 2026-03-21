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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEmailListener {

    private final EmailService emailService;
    private final OrderRepository orderRepository;
    @Async
    @Transactional // added this so the thread gets its own database session
    @EventListener
    public void handleOrderStatusEvent(OrderStatusEvent event) {

        // re-fetch the order directly from the database to avoid closed session errors
        Order order = orderRepository.findById(event.getOrder().getId()).orElse(null);

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