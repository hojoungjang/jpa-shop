package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItems(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
         Order order = new Order();
         order.setMember(member);
         order.setDelivery(delivery);
         for (OrderItem orderItem : orderItems) {
             // order.getOrderItems().add(orderItem);
             order.addOrderItems(orderItem);
         }
         order.setStatus(OrderStatus.ORDER);
         order.setOrderDate(LocalDateTime.now());
         return order;
    }

    // 비즈니스 로직
    public void cancel() {
        if (this.delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송이 완료된 제품은 취소가 불가능 합나다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        orderItems.forEach(orderItem -> {
            orderItem.cancel();
        });
    }

    // 조회 로직
    public int getTotalPrice() {
        int total = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getOrderPrice() * orderItem.getCount();
        }
        return total;

        // 스트림
        // return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
