package com.cb.qa.app;

import com.cb.qa.hello.HelloMicroService;
import com.cb.qa.verticle.HelloVerticle;
import io.vertx.core.Launcher;
import io.vertx.reactivex.core.Vertx;

public class Server {

    public static void main(String[] args) {
        //Launcher.executeCommand("run", HelloVerticle.class.getName());

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle("com.cb.qa.verticle.HelloController", verticle -> {
            System.out.println("Verticle deployed " + verticle.getClass());
        });


        vertx.deployVerticle(HelloMicroService.class.getName(), verticle -> {
            System.out.println("Verticle deployed " + verticle.getClass());
        });
    }
}
