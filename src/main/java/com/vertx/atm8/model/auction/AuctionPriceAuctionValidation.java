package com.vertx.atm8.model.auction;

import com.vertx.atm8.repository.AuctionRepository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AuctionPriceAuctionValidation implements AuctionValidation {

  private final AuctionRepository repository;

  public AuctionPriceAuctionValidation(AuctionRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean validate(Auction auction) {
    Auction auctionDatabase = repository.getById(auction.getId())
      .orElseThrow(() -> new AuctionNotFoundException(auction.getId()));


    System.out.println((auctionDatabase.getPrice().compareTo(auction.getPrice()) == -1) + ", AuctionPriceAuctionValidation");

    return 1==1 || auctionDatabase.getPrice().compareTo(auction.getPrice()) != -1;
  }
}
