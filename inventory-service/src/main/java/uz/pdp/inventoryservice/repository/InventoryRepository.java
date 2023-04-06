package uz.pdp.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.inventoryservice.entity.InventoryEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {
    Optional<InventoryEntity> findByProductId(Integer productId);
    InventoryEntity getByProductId(Integer productId);

    List<InventoryEntity> findAllByProductIdIn(Collection<Integer> productId);
}
