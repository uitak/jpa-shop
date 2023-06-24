package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격

    private int count; //주문 수량

    // 생성 메서드 - - - -
    // 파라미터에서 item에 가격이 있기 때문에 orderPrice를 따로 받지 않아도 될 것이라 생각할 수 있지만,
    // item에 할인이나 쿠폰등이 적용될 수도 있기 때문에 따로 입력받는 것이 좋다.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //비즈니스 로직 ========
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직 ========
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}