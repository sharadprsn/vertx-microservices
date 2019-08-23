package com.cb.qa.app;

import com.cb.qa.verticle.HelloVerticle;
import io.vertx.core.Launcher;

public class Server {

    public static void main(String[] args) {
        Launcher.executeCommand("run", HelloVerticle.class.getName());
    }
}
