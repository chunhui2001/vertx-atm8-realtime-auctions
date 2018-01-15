package com.vertx.atm8;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start() {
    vertx.deployVerticle("com.vertx.atm8.verticle.SockJSBridgeVerticle", res -> {
      if (res.succeeded()) {
        logger.info("Successful SockJSBridgeVerticle deployment id is: " + res.result());
      } else {
        logger.error("Failed SockJSBridgeVerticle deployment failed!");
      }
    });

    vertx.deployVerticle("com.vertx.atm8.verticle.AuctionServiceVerticle", res -> {
      if (res.succeeded()) {
        logger.info("Successful AuctionServiceVerticle deployment id is: " + res.result());
      } else {
        logger.error("Failed AuctionServiceVerticle deployment failed!");
      }
    });

    vertx.deployVerticle("com.vertx.atm8.verticle.AuctionFrontendVerticle", res -> {
      if (res.succeeded()) {
        logger.info("Successful AuctionFrontendVerticle deployment id is: " + res.result());
      } else {
        logger.error("Failed AuctionFrontendVerticle deployment failed!");
      }
    });
  }

}
