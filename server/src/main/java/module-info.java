module com.cb.qa.server {
    requires vertx.rx.java2;
    requires io.reactivex.rxjava2;
    requires vertx.core;

    exports com.cb.qa.verticle;
}