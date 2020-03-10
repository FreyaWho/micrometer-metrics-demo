package micrometer.application;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private String orderId;
    private Long userId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
