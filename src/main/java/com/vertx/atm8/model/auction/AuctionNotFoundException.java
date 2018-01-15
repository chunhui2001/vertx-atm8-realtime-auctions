package com.vertx.atm8.model.auction;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class AuctionNotFoundException extends RuntimeException {

  private static final Logger logger = LoggerFactory.getLogger(AuctionNotFoundException.class);

  public static AuctionNotFoundException create(String auctionId) {
    logger.error(auctionId + ", not found AuctionNotFoundException");
    return new AuctionNotFoundException(auctionId);
  }


  public AuctionNotFoundException(String auctionId) {
    super("Auction not found: " + auctionId);
  }
}
