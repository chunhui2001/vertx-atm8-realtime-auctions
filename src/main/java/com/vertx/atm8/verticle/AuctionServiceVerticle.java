package com.vertx.atm8.verticle;

import com.vertx.atm8.handler.AuctionHandler;
import com.vertx.atm8.model.auction.AuctionAuctionValidation;
import com.vertx.atm8.repository.AuctionRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class AuctionServiceVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);

    router.route(HttpMethod.PATCH, "/api/*").handler(jwtAuthHandler());
    router.mountSubRouter("/api", auctionApiRouter());

    vertx.createHttpServer().requestHandler(router::accept).listen(8081);
  }

  private JWTAuthHandler jwtAuthHandler() {
    JWTAuthOptions jwtAuthOptions = new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setPath("jceks/keystore.jceks")
//        .setPath("ssh_keys/server-keystore.jks")
        .setType("jceks")
        .setPassword("secret"));
    JWTAuth authProvider = JWTAuth.create(vertx, jwtAuthOptions);

    return JWTAuthHandler.create(authProvider);
  }

  private Router auctionApiRouter() {
    AuctionRepository repository = new AuctionRepository(vertx.sharedData());
    AuctionAuctionValidation validator = new AuctionAuctionValidation(repository);
    AuctionHandler auctionHandler = new AuctionHandler(repository, validator);

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.route().consumes("application/json");
    router.route().produces("application/json");

    router.route("/auctions/:id").handler(auctionHandler::initAuctionInSharedData);
    router.get("/auctions/:id").handler(auctionHandler::handleGetAuction);
    router.patch("/auctions/:id").handler(auctionHandler::handleChangeAuction);

    return router;
  }
}
