package com.vertx.atm8.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.math.BigDecimal;

public class SockJSBridgeVerticle  extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(SockJSBridgeVerticle.class);

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.route("/eventbus/*").handler(eventBusHandler());

    vertx.createHttpServer().requestHandler(router::accept).listen(8082);
  }

  private SockJSHandler eventBusHandler() {

    BridgeOptions options = new BridgeOptions()
      .addOutboundPermitted(new PermittedOptions().setAddressRegex("auction\\.[0-9]+"));

    SockJSHandler sockJSHandler = null;

    Handler<BridgeEvent> bridgeEventHandler = event -> {

      if (event.type() == BridgeEventType.SOCKET_CREATED) {
        logger.info("A socket was created");
      }

        /* JsonObject rawMessage = event.getRawMessage();

        // put some headers
        event.setRawMessage(rawMessage);

        JsonObject body = new JsonObject(rawMessage.getString("body"));

        // update body and send to client
        // TODO

        event.socket().write(rawMessage.encode()); */

      if (event.type() != BridgeEventType.SOCKET_PING) {
        System.out.println(event.type() + ", event.type()");
      }


      event.complete(true);

    };

    return SockJSHandler.create(vertx).bridge(options, bridgeEventHandler);

  }
}
