module com.cb.qa.app {
    requires vertx.rx.java2;
    requires io.reactivex.rxjava2;
    requires vertx.core;
    requires com.cb.qa.hello;

    exports com.cb.qa.verticle;
}