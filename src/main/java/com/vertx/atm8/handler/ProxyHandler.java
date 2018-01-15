package com.vertx.atm8.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface ProxyHandler extends Handler<RoutingContext> {

  static ProxyHandler create(String host, int port) {
    return new ProxyHandlerImpl(host, port);
  }
}
