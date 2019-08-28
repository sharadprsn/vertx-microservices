package com.cb.qa.verticle;

import com.cb.qa.hello.HelloMicroService;
import io.vertx.core.cli.annotations.Description;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class HellServiceTest {

    @BeforeAll
    public static void setup(Vertx vertx) {
        vertx.deployVerticle(HelloController.class.getName());
        vertx.deployVerticle(HelloMicroService.class.getName());
    }

    @Test
    @Description("check the flow")
    public void flowChecker(Vertx vertex, VertxTestContext vertxTestContext) {
        WebClient webClient = WebClient.create(vertex);

        webClient.get(8082, "localhost", "/")
                .as(BodyCodec.jsonObject())
                .send(vertxTestContext.succeeding(resp -> {
                    vertxTestContext.verify(() -> {
                        assertThat(resp.statusCode()).isEqualTo(200);
                        vertxTestContext.completeNow();
                    });
                }));
    }
}
