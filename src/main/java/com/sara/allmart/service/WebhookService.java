package com.sara.allmart.service;

import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.repository.OrderRepository;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebhookService {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final OrderRepository orderRepository;

    public WebhookService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void processStripeEvent(String payload, String sigHeader) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        if ("payment_intent.succeeded".equals(event.getType())) {
            handlePaymentSuccess(event);
        } else {
            log.info("Unhandled Stripe event type: {}", event.getType());
        }
    }

    private void handlePaymentSuccess(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().deserializeUnsafe();

            if (paymentIntent == null) {
                log.error("PaymentIntent was null after deserialization.");
                return;
            }

            if (paymentIntent.getMetadata() == null || !paymentIntent.getMetadata().containsKey("orderId")) {
                log.warn("No 'orderId' found in Stripe metadata. Ignoring event.");
                return;
            }

            String orderIdStr = paymentIntent.getMetadata().get("orderId");

            try {
                Long orderId = Long.parseLong(orderIdStr);
                Order order = orderRepository.findById(orderId).orElse(null);

                if (order != null) {
                    order.setPaid(true);
                    order.setStatus(OrderStatus.PAID);
                    orderRepository.save(order);
                    log.info("SUCCESS: Order {} is now PAID.", orderId);
                } else {
                    log.error("Order {} was not found in the database.", orderId);
                }
            } catch (NumberFormatException e) {
                log.error("Could not parse Order ID '{}' into a Long.", orderIdStr);
            }

        } catch (EventDataObjectDeserializationException e) {
            log.error("Failed to deserialize PaymentIntent: {}", e.getMessage());
        }
    }
}