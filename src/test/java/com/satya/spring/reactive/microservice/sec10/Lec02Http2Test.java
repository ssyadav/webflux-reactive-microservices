package com.satya.spring.reactive.microservice.sec10;


import com.satya.spring.reactive.microservice.sec10.dto.Product;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class Lec02Http2Test extends AbstractWebClient{

  // private final WebClient client = createWebClient(); for method testConcurrentRequest
  // testConcurrentRequest260 testConcurrentRequest501

  private final WebClient client =
      createWebClient(
          b -> {
            var poolSize = 1;
            var provider =
                ConnectionProvider.builder("custom")
                    .lifo()
                    .maxConnections(poolSize)
                    .build();

            var httpClient = HttpClient.create(provider)
                    .protocol(HttpProtocol.H2C)
                    .compress(true).keepAlive(true);
            b.clientConnector(new ReactorClientHttpConnector(httpClient));
          });

    /**
     * http2 establish only one connection and process all the request on that connection
     *
     */
    @Test
    public void testConcurrentRequest501() {
        int MAX = 20000;
        Flux.range(1, MAX)
                .flatMap(this::getProduct, MAX)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(MAX, l.size()))
                .expectComplete()
                .verify();
    }

    private Mono<Product> getProduct(int id) {
        return this.client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);

    }
}