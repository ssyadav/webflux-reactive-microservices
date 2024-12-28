package com.satya.spring.reactive.microservice.sec03;

import com.satya.spring.reactive.microservice.sec03.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {"sec=sec03", "logging.level.org.springframework.r2dbc=DEBUG"})
public class CustomerServiceTest {

  public static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

  @Autowired private WebTestClient webTestClient;

  @Test
  public void testGetAllCustomers() {
    log.info("CustomerServiceTest.testGetAllCustomers: ");
    this.webTestClient
        .get()
        .uri("/customers")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectHeader()
        .contentType("text/event-stream;charset=UTF-8")
        .expectBodyList(CustomerDto.class)
        .value(customer -> log.info("CustomerServiceTest.testGetAllCustomers: {}", customer))
        .hasSize(10);
  }

  @Test
  public void testGetAllCustomersPaginated() {
    log.info("CustomerServiceTest.testGetAllCustomersPaginated: ");
    this.webTestClient
            .get()
            .uri("/customers/pageable?pageNo=1&pageSize=3")
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(CustomerDto.class)
            .value(customer -> log.info("CustomerServiceTest.testGetAllCustomersPaginated: {}", customer))
            .hasSize(3);
  }

  @Test
  public void testGetAllCustomersPaginated_1() {
    log.info("CustomerServiceTest.testGetAllCustomersPaginated_1: ");
    this.webTestClient
            .get()
            .uri("/customers/pageable?pageNo=3&pageSize=2")
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody()
            .consumeWith(response -> log.info("CustomerServiceTest.testGetAllCustomersPaginated_1: {}",
                    new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.length()").isEqualTo(2)
            .jsonPath("$[0].id").isEqualTo(5)
            .jsonPath("$[1].id").isEqualTo(6);
  }

  @Test
  public void testGetCustomersById() {
    log.info("CustomerServiceTest.testGetAllCustomersById: ");
    this.webTestClient
            .get()
            .uri("/customers/1")
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody()
            .consumeWith(response -> log.info("CustomerServiceTest.testGetAllCustomersById: {}",
                    new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("sam")
            .jsonPath("$.email").isEqualTo("sam@gmail.com");
  }

  @Test
  public void testCreateAndDeleteCustomers() {
    log.info("CustomerServiceTest.testSaveCustomers: ");

    // create
    var dto = new CustomerDto(null, "satya", "satya@gmail.com");
    this.webTestClient
            .post()
            .uri("/customers")
            .bodyValue(dto)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .consumeWith(response -> log.info("CustomerServiceTest.testCreateCustomers: {}",
                    new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isEqualTo(11)
            .jsonPath("$.name").isEqualTo("satya")
            .jsonPath("$.email").isEqualTo("satya@gmail.com");

    // delete
    this.webTestClient
            .delete()
            .uri("/customers/11")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody().isEmpty();
  }

  @Test
  public void testUpdateCustomers() {
    log.info("CustomerServiceTest.testUpdateCustomers: ");

    // update
    var dto = new CustomerDto(null, "satya", "satya@gmail.com");
    this.webTestClient
        .put()
        .uri("/customers/10")
        .bodyValue(dto)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .consumeWith(
            response ->
                log.info(
                    "CustomerServiceTest.testUpdateCustomers: {}",
                    new String(Objects.requireNonNull(response.getResponseBody()))))
        .jsonPath("$.id").isEqualTo(10)
        .jsonPath("$.name").isEqualTo("satya")
        .jsonPath("$.email").isEqualTo("satya@gmail.com");
    }

  @Test
  public void testCustomersNotFound() {
    // get
    log.info("CustomerServiceTest.testCustomersNotFound: ");
    this.webTestClient
        .get()
        .uri("/customers/100")
        .exchange()
        .expectStatus().is4xxClientError()
        .expectBody()
        .isEmpty();

    // delete
    this.webTestClient
            .delete()
            .uri("/customers/200")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .isEmpty();


    //update/put
    var dto = new CustomerDto(null, "satya", "satya@gmail.com");
    this.webTestClient
            .put()
            .uri("/customers/200")
            .bodyValue(dto)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody().isEmpty();
  }
}