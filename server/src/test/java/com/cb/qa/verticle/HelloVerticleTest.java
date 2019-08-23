package com.cb.qa.verticle;

import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class HelloVerticleTest {

    @Test
    @DisplayName("Test hello api")
    public void helloTest(Vertx vertx, VertxTestContext testContext){
        WebClient webClient = WebClient.create(vertx);
        vertx.deployVerticle(HelloVerticle.class.getName());

        JsonObject expectedResult = new JsonObject().put("message", "hello");

        webClient.get(8080, "localhost", "/")
                .as(BodyCodec.jsonObject())
                .send(testContext.succeeding(resp -> {
                    testContext.verify(() -> {
                        assertThat(resp.statusCode()).isEqualTo(200);
                        assertThat(resp.body()).isEqualTo(expectedResult);
                        testContext.completeNow();
                    });
                }));
    }
}
