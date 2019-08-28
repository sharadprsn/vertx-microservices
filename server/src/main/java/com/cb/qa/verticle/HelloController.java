package com.cb.qa.verticle;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;

public class HelloController extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(req -> {
                    EventBus bus = vertx.eventBus();
                    Single<JsonObject> abs1 = bus.<JsonObject>rxRequest("hello", "luke")
                            .map(Message::body);
                    Single<JsonObject> abs2 = bus.<JsonObject>rxRequest("hello", "laila")
                            .map(Message::body);

                    Single.zip(abs1, abs2, (luke, laila) -> {
                        return new JsonObject()
                                .put("Luke", luke.getString("message")
                                        + " from "
                                        + luke.getString("served-by"))
                                .put("Leia", laila.getString("message")
                                        + " from "
                                        + laila.getString("served-by"));
                    }).subscribe(x -> req.response().end(x.encodePrettily()),
                            t -> {
                                t.printStackTrace();
                                req.response().setStatusCode(500).end(t.getMessage());
                            });
                }).listen(8082);
    }
}
