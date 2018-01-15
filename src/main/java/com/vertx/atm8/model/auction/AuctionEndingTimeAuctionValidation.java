package com.vertx.atm8.model.auction;

import com.vertx.atm8.repository.AuctionRepository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AuctionEndingTimeAuctionValidation implements AuctionValidation {

  private final AuctionRepository repository;

  public AuctionEndingTimeAuctionValidation(AuctionRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean validate(Auction auction) {

    Auction auctionDatabase = repository.getById(auction.getId())
      .orElseThrow(() -> {

        return new AuctionNotFoundException(auction.getId());
      });

    System.out.println(auctionDatabase.getEndingTime().isAfter(ZonedDateTime.now(ZoneOffset.UTC)) + ", AuctionEndingTimeAuctionValidation");

    return auctionDatabase.getEndingTime().isAfter(ZonedDateTime.now(ZoneOffset.UTC));
  }
}
