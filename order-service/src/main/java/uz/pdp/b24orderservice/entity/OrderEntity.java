package uz.pdp.b24orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.EAGER)
    private List<OrderItemEntity> orderItemEntityList;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public boolean isStatusInProcess(){
        return OrderStatus.IN_PROCESS == status;
    }
}
