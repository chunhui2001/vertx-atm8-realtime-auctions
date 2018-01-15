package com.vertx.atm8.model.auction;

import com.vertx.atm8.repository.AuctionRepository;

public class AuctionAuctionValidation implements AuctionValidation {

  private final AuctionRepository repository;

  public AuctionAuctionValidation(AuctionRepository repository) {
    this.repository = repository;
  }

  public boolean validate(Auction auction) {
    AuctionPriceAuctionValidation priceValidator = new AuctionPriceAuctionValidation(this.repository);
    AuctionEndingTimeAuctionValidation endingTimeValidator = new AuctionEndingTimeAuctionValidation(this.repository);

    return priceValidator.validate(auction) && endingTimeValidator.validate(auction);
  }
}
