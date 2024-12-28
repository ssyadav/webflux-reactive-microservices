package com.satya.spring.reactive.microservice.sec10;


import com.satya.spring.reactive.microservice.sec10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec01HttpConnectionPoolingTest extends AbstractWebClient{

  // private final WebClient client = createWebClient(); for method testConcurrentRequest
  // testConcurrentRequest260 testConcurrentRequest501

  private final WebClient client =
      createWebClient(
          b -> {
            var poolSize = 501;
            var provider =
                ConnectionProvider.builder("custom")
                    .lifo()
                    .maxConnections(poolSize)
                    .pendingAcquireMaxCount(poolSize * 5)
                    .build();

            var httpClient = HttpClient.create(provider).compress(true).keepAlive(true);
            b.clientConnector(new ReactorClientHttpConnector(httpClient));
          });

    /**
     * we can configure the flatmap request processing count by passing the max parameter in the flatmap as below
     * .flatMap(this::getProduct, MAX)
     * so now till 500 hundred request it will take 5 sec to process but when we increase the MAX count to 501 it will take 10 sec
     * because webclient can process 500 request concurrently. so 500 request will be processed in 5 sec and remaining 1 will take another 5 sec
     * we can configure that as well
     *
     */
    @Test
    public void testConcurrentRequest501() {
        int MAX = 501;
        Flux.range(1, MAX)
                .flatMap(this::getProduct, MAX)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(MAX, l.size()))
                .expectComplete()
                .verify();
    }

    /**
     * flatmap can process 256 request concurrently and
     * if we send the 260 request then it will take 10 sec as 256 will be processed in 5 sec and
     * remaining 4 will take another 5 sec
     * @throws InterruptedException
     */
    @Test
    public void testConcurrentRequest260() {
        int MAX = 260;
        Flux.range(1, MAX)
                .flatMap(this::getProduct)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(MAX, l.size()))
                .expectComplete()
                .verify();
    }

    /**
     * run the test and see the connection in the terminal with the command:
     * netstat -an| grep -w 127.0.0.1.7070
     * this will show the same number of connection as the number of request configure in (int MAX=3) in this method
     * @throws InterruptedException
     */
    @Test
    public void testConcurrentRequest() throws InterruptedException {
        int MAX = 3;
        Flux.range(1, MAX)
                .flatMap(this::getProduct)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(MAX, l.size()))
                .expectComplete()
                .verify();

        Thread.sleep(Duration.ofMinutes(1));
    }

    private Mono<Product> getProduct(int id) {
        return this.client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(p -> log.info("received: {}", p));

    }
}