package com.satya.spring.reactive.microservice.sec02;


import com.satya.spring.reactive.microservice.sec02.dto.OrderDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

public class Lec01DatabaseClientTest extends AbstractTest{

    public static final Logger log = LoggerFactory.getLogger(Lec01DatabaseClientTest.class);

    @Autowired private DatabaseClient databaseClient;


    @Test
    public void getOrdersDetailsByDescription() {
    String query =
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
        """;

    this.databaseClient.sql(query)
            .bind("description", "iphone 20")
            .fetch()
            .all()
            .doOnNext(c -> log.info("customer: {}", c))
            .as(StepVerifier::create)
            .assertNext(c -> Assertions.assertEquals(975, c.get("amount")))
            .assertNext(c -> Assertions.assertEquals(950, c.get("amount")))
            .expectComplete()
            .verify();
    }

    @Test
    public void getOrdersDetailsByDescriptionMapProperties() {
        String query =
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
                """;

        this.databaseClient.sql(query)
                .bind("description", "iphone 20")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(c -> log.info("customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals(975, c.amount()))
                .assertNext(c -> Assertions.assertEquals(950, c.amount()))
                .expectComplete()
                .verify();
    }
}
