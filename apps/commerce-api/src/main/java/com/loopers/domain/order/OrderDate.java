package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
public class OrderDate {

    @Column(name = "order_date")
    private LocalDateTime value;

    protected OrderDate() {}
    public static OrderDate of(LocalDateTime value) {
        if (value == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 날짜는 비어있을 수 없습니다.");
        }

        OrderDate orderDate = new OrderDate();
        orderDate.value = value;
        return orderDate;
    }
} 
