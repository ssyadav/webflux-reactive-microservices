package com.satya.spring.reactive.microservice.sec07;


import com.satya.spring.reactive.microservice.sec07.dto.CalculatorResponse;
import com.satya.spring.reactive.microservice.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec05ErrorResponseTest extends AbstractWebClient {


    private WebClient webClient = createWebClient();


    @Test
    public void testHandlingErrors() {
        this.webClient
                .get()
                .uri("/lec05/calculator/{a}/{b}", 10,20)
                .header("operation", "@")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                //.onErrorReturn(new CalculatorResponse(0,0,null, 0.0))
                .doOnError(WebClientResponseException.class, ex -> log.error("Exception: {}", ex.getResponseBodyAs(ProblemDetail.class)))
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new CalculatorResponse(0,0,null, 0.0))
                .onErrorReturn(WebClientResponseException.BadRequest.class, new CalculatorResponse(0,0,null, -1.0))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void testExchange() {
        this.webClient
                .get()
                .uri("/lec05/calculator/{a}/{b}", 10,20)
                .header("operation", "@")
                .exchangeToMono(this::decode)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private Mono<CalculatorResponse> decode(ClientResponse clientResponse) {
//        clientResponse.cookies()
//        clientResponse.headers()

        log.info("Status code: {}", clientResponse.statusCode());
        if(clientResponse.statusCode().isError()){
            return clientResponse.bodyToMono(ProblemDetail.class)
                    .doOnNext(pd -> log.error("Error: {}", pd))
                    .then(Mono.empty());
        }

        return clientResponse.bodyToMono(CalculatorResponse.class);
    }

}
