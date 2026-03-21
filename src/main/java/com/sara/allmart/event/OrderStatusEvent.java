package com.sara.allmart.event;

import com.sara.allmart.entity.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderStatusEvent extends ApplicationEvent {

    private final Order order;
    private final String statusType;

    public OrderStatusEvent(Object source, Order order, String statusType) {
        super(source);
        this.order = order;
        this.statusType = statusType;
    }
}