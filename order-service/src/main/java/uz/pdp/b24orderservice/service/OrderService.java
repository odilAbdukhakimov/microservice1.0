package uz.pdp.b24orderservice.service;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uz.pdp.b24orderservice.common.exception.RecordNotFoundException;
import uz.pdp.b24orderservice.config.KafKaConsumerConfig;
import uz.pdp.b24orderservice.controller.dto.OrderInventoryRequest;
import uz.pdp.b24orderservice.controller.dto.OrderItemRequest;
import uz.pdp.b24orderservice.controller.dto.OrderRequest;
import uz.pdp.b24orderservice.controller.dto.OrderResponse;
import uz.pdp.b24orderservice.entity.OrderEntity;
import uz.pdp.b24orderservice.entity.OrderItemEntity;
import uz.pdp.b24orderservice.entity.OrderStatus;
import uz.pdp.b24orderservice.repository.OrderItemRepository;
import uz.pdp.b24orderservice.repository.OrderRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestTemplate restTemplate;

    private final KafkaConsumer<String, String> consumer;

    private static final String PRODUCT_URL = "http://INVENTORY-SERVICE/inventory/check";

    public OrderResponse createOrder(
            OrderRequest orderRequest
    ) {
        Timestamp createdDate = Timestamp.valueOf(LocalDateTime.now());
        OrderEntity orderEntity = OrderEntity.builder()
                .createdAt(createdDate)
                .updatedAt(createdDate)
                .username(orderRequest.getUsername())
                .status(OrderStatus.CREATED)
                .build();
        OrderEntity createdOrderEntity = orderRepository.save(orderEntity);
        createOrderItem(createdOrderEntity, orderRequest.getOrderItemList());
        Boolean isOrderSuccess = isProductAvailableInInventoryService(
                OrderInventoryRequest.builder()
                        .orderID(createdOrderEntity.getId())
                        .orderItemList(orderRequest.getOrderItemList())
                        .build()
        );
        OrderEntity completedOrderEntity = setOrderStatusAfterInventoryServiceResponse(createdOrderEntity, isOrderSuccess);
        List<OrderItemEntity> orderItemEntityList = orderItemRepository.findAllByOrderEntity(completedOrderEntity);
        return OrderResponse.from(completedOrderEntity, orderItemEntityList);
    }

    private void createOrderItem(OrderEntity orderEntity, List<OrderItemRequest> orderItemList) {
        orderItemList.forEach(orderItem -> {
            OrderItemEntity orderItemEntity = OrderItemEntity.of(orderItem, orderEntity);
            orderItemRepository.save(orderItemEntity);
        });
    }

    private OrderEntity setOrderStatusAfterInventoryServiceResponse(OrderEntity orderEntity, Boolean statusState) {
        if (statusState == null) {
            orderEntity.setStatus(OrderStatus.IN_PROCESS);
        } else if (!statusState) {
            orderEntity.setStatus(OrderStatus.ERROR);
        } else {
            orderEntity.setStatus(OrderStatus.SUCCESS);
        }
        return orderRepository.save(orderEntity);
    }

    private Boolean isProductAvailableInInventoryService(OrderInventoryRequest orderInventoryRequest) {
        try {
            HttpEntity<?> orderItems = new HttpEntity<>(orderInventoryRequest);
            return restTemplate.exchange(PRODUCT_URL, HttpMethod.POST, orderItems, Boolean.class).getBody();
        } catch (HttpClientErrorException v) {
            if (v.getStatusCode().is4xxClientError()) {
                return false;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OrderResponse> getOrderList(String username) {
        List<OrderEntity> orderEntityList = orderRepository.findAllByUsername(username);
        return orderEntityList.stream().map(order -> OrderResponse.from(order, orderItemRepository.findAllByOrderEntity(order))).toList();
    }

    public void getOrderStatus(int orderId){
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new RecordNotFoundException(""));
        if (orderEntity.isStatusInProcess()){
            // Create a Kafka consumer instance

            // Subscribe to a Kafka topic
            consumer.subscribe(Collections.singleton("inventory_service"));

            // Manually assign a specific partition to the consumer
            TopicPartition partition = new TopicPartition("inventory_service", 0);
            consumer.assign(Collections.singleton(partition));

            // Poll for new messages and process them
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message: key=%s, value=%s, partition=%d, offset=%d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                }
            }
        }
    }

}
