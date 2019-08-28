package com.cb.qa.verticle;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.Optional;

public class HelloVerticle extends AbstractVerticle {
    @Override
    public void start() {

        Router router = Router.router(vertx);
        router.get("/").handler(this::hello);
        router.get("/:name").handler(this::hello);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    @Override
    public void stop() {

    }


    private void hello(RoutingContext routingContext) {
        JsonObject resp = new JsonObject();
        Optional<String> requestParam = Optional.ofNullable(routingContext.pathParam("name"));
        resp.put("message", ("hello " + requestParam.orElse("")).trim());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(resp.encode());
    }


    private void helloEvent(RoutingContext routingContext) {
        JsonObject resp = new JsonObject();
        Optional<String> requestParam = Optional.ofNullable(routingContext.pathParam("name"));
        resp.put("message", ("hello " + requestParam.orElse("")).trim());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(resp.encode());
    }
}
