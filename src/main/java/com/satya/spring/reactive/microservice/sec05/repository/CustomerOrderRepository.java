package com.satya.spring.reactive.microservice.sec05.repository;

import com.satya.spring.reactive.microservice.sec05.dto.OrderDetails;
import com.satya.spring.reactive.microservice.sec05.entity.CustomerOrder;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrder, UUID> {

  @Query(
      """
        SELECT
            p.*
        FROM
            customer c
        INNER JOIN customer_order co ON c.id = co.customer_id
        INNER JOIN product p ON co.product_id = p.id
        WHERE
            c.name = :name
      """)
  Flux<ProductRepository> getProductOrderedByCustomer(String name);

  @Query(
      """
        SELECT
            co.order_id,
            c.name AS customer_name,
            p.description AS product_name,
            co.amount,
            co.order_date
        FROM
            customer c
        INNER JOIN customer_order co ON c.id = co.customer_id
        INNER JOIN product p ON p.id = co.product_id
        WHERE
            p.description = :description
        ORDER BY co.amount DESC
      """)
  Flux<OrderDetails> getOrdersDetailsByDescription(String description);
}
