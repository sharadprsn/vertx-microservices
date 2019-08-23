package com.cb.qa.verticle;

import io.reactivex.Single;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class HelloVerticleTest {

    @BeforeAll
    public static void setUp(Vertx vertx){
        vertx.deployVerticle(HelloVerticle.class.getName());
    }

    @Test
    @DisplayName("Test hello api")
    public void helloTest(Vertx vertx, VertxTestContext testContext){
        WebClient webClient = WebClient.create(vertx);

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

    @Test
    @Description("Test concurrent requests")
    public void multiRequestTest(Vertx vertx, VertxTestContext testContext){

        WebClient webClient = WebClient.create(vertx);

        HttpRequest<JsonObject> request1 = webClient.get(8080,"localhost", "/luke")
                .as(BodyCodec.jsonObject());
        HttpRequest<JsonObject> request2 = webClient.get(8080, "localhost","/laila")
                .as(BodyCodec.jsonObject());

        Single<JsonObject> s1 = request1.rxSend().map(HttpResponse::body);

        Single<JsonObject> s2 = request2.rxSend().map(HttpResponse::body);

        Single.zip(s1,s2, (luke,laila) ->{
            return  new JsonObject()
                    .put("luke", luke.getValue("message"))
                    .put("laila", laila.getValue("message"));
        }).subscribe(result -> {
            System.out.println(result);
            testContext.verify(() -> {
                assertThat(result).isNotEmpty();
                testContext.completeNow();
            });
        });
    }
}
