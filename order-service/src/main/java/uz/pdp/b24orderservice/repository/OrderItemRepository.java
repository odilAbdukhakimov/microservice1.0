package uz.pdp.b24orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.b24orderservice.entity.OrderEntity;
import uz.pdp.b24orderservice.entity.OrderItemEntity;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {
    List<OrderItemEntity> findAllByOrderEntity(OrderEntity orderEntity);
}
