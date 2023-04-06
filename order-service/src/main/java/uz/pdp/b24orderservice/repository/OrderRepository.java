package uz.pdp.b24orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.b24orderservice.entity.OrderEntity;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByUsername(String username);

    @Modifying
    @Query(value = "update order_entity e set e.status =:status where e.id =:orderId", nativeQuery = true)
    void update(@Param("status") String orderStatus, @Param("orderId") int orderId);
}
