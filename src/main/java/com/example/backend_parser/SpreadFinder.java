package com.example.backend_parser;

import com.example.backend_parser.calculations.SpreadCalculator;
import com.example.backend_parser.models.*;
import com.example.backend_parser.service.TelegramService;
import com.example.backend_parser.splitter.Splitter;
import com.example.backend_parser.utils.BanListUtils;
import com.example.backend_parser.utils.BlackListUtils;
import com.example.backend_parser.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class SpreadFinder {

    private static final int MIN_SPREAD = 2;
    private static final int MAX_SPREAD = 200;

    public static void findSpreads() throws InterruptedException {
        Splitter.split();

        parseTokensAndSendSpread();
    }

    private static void parseTokensAndSendSpread() throws InterruptedException {
        List<String> exchangesNames = new ArrayList<>();
        List<ExchangePair> exchangePairs = new ArrayList<>(parseExchangesPairs(exchangesNames));

        for (ExchangePair pair : exchangePairs) {
            for (Token token : pair.exchange1.getTokens()) {
                for (Token token1 : pair.exchange2.getTokens()) {
                    if (token.getBase().equalsIgnoreCase(token1.getBase())) {
                        defineSpread(token, token1, pair.exchange1, pair.exchange2);
                    }
                }
            }
        }
    }

    private static void defineSpread(Token token1, Token token2, Exchange exchange1, Exchange exchange2)
            throws InterruptedException {
        double spread = SpreadCalculator.calculateSpread(token1, token2);
        if (spread > MIN_SPREAD && spread < MAX_SPREAD) {
            if (BlackListUtils.tokenInBlackList(token1, token2, exchange1, exchange2)
                    || BanListUtils.tokenInBanList(token1, exchange1)
                    || BanListUtils.tokenInBanList(token2, exchange2)) {
                return;
            }
            BlackListUtils.addToBlackList(token1.getSymbol(), exchange1.getBlackList());
            BlackListUtils.addToBlackList(token2.getSymbol(), exchange2.getBlackList());

            String chain = "ETH";
//            chain = findAChainWithMinFee(token1, token2, exchange1, exchange2);
//            if(chain == null) {
//                return;
//            }

            String formattedMessage = MessageUtils.getFormattedMessage(token1, token2, exchange1, exchange2, spread, chain);
            if (token1.getBid() > token2.getAsk()) {
                formattedMessage = MessageUtils.getFormattedMessage(token2, token1, exchange2, exchange1, spread, chain);
            }

//            if(!isSpreadStillExists(token1, token2, exchange1, exchange2)) {
//                return;
//            }

            TelegramService.sendMessage(formattedMessage);
            Thread.sleep(1000);
        }
    }

    private static boolean isSpreadStillExists(Token token1, Token token2, Exchange exchange1, Exchange exchange2) {
        Token tokenWithPrice1 = exchange1.getBaseExchange().getOrderBookForToken(token1);
        Token tokenWithPrice2 = exchange2.getBaseExchange().getOrderBookForToken(token2);

        double spread = SpreadCalculator.calculateSpread(tokenWithPrice1, tokenWithPrice2);

        if (spread > MIN_SPREAD && spread < MAX_SPREAD) {
            return true;
        }

        return false;
    }

    private static String findAChainWithMinFee(Token token1, Token token2, Exchange exchange1, Exchange exchange2) {
        List<ChainAndFee> chainsAndFees = new ArrayList<>();
        List<Chain> chains1 = token1.getChains();
        List<Chain> chains2 = token2.getChains();

        for(Chain chain1 : chains1) {
            for(Chain chain2 : chains2) {
                if(chain1.getName().equalsIgnoreCase(chain2.getName()) || chain1.getName().equals("") || chain2.getName().equals("")) {
                    if(token1.getBid() > token2.getAsk()) {
                        System.out.println("Option1 " + chain2.getName() + " " + chain1.isDepositEnabled() + " " + chain2.isWithdrawalEnabled());

                        if(chain1.isDepositEnabled() && chain2.isWithdrawalEnabled()) {
                            ChainAndFee chainAndFee = getFinalFee(token2, exchange2, chain2);
                            double amountOfTokens = exchange2.getBaseExchange().getMinAmount() / token2.getAsk();
                            double newAsk = exchange2.getBaseExchange().getMinAmount()/(amountOfTokens - chainAndFee.getFee());
                            if(token1.getBid() > newAsk && SpreadCalculator.percentBetweenPrices(token1.getBid(), newAsk) > MIN_SPREAD) {
                                chainsAndFees.add(chainAndFee);
                            }
                        }
                    }
                    else {
                        System.out.println("Option2 " + chain2.getName() + " " + chain2.isDepositEnabled() + " " + chain1.isWithdrawalEnabled() + " " + chain1);

                        if(chain1.isWithdrawalEnabled() && chain2.isDepositEnabled()) {
                            ChainAndFee chainAndFee = getFinalFee(token1, exchange1, chain1);
                            double amountOfTokens = exchange1.getBaseExchange().getMinAmount() / token1.getAsk();
                            double newAsk = exchange1.getBaseExchange().getMinAmount()/(amountOfTokens - chainAndFee.getFee());
                            if(token2.getBid() > newAsk && SpreadCalculator.percentBetweenPrices(token2.getBid(), newAsk) > MIN_SPREAD) {
                                chainsAndFees.add(chainAndFee);
                            }
                        }

                    }
                }
            }
        }
        if(chainsAndFees.isEmpty()) {
            return null;
        }
        return findMinFeeObject(chainsAndFees);
    }

    private static ChainAndFee getFinalFee(Token token, Exchange exchange, Chain chain) {
        double tokenAmount = exchange.getBaseExchange().getMinAmount() / token.getAsk();

        double percentFee = chain.getFeePercent() * tokenAmount;
        double finalFee = percentFee + chain.getFee();

        return new ChainAndFee(chain.getName(), finalFee);
    }

    private static String findMinFeeObject(List<ChainAndFee> chainAndFees) {
        if (chainAndFees == null || chainAndFees.isEmpty()) {
            return null;
        }

        ChainAndFee minFeeObject = chainAndFees.get(0);

        for (ChainAndFee chainAndFee : chainAndFees) {
            if (chainAndFee.getFee() < minFeeObject.getFee()) {
                minFeeObject = chainAndFee;
            }
        }

        return minFeeObject.getName();
    }

    private static List<ExchangePair> parseExchangesPairs(List<String> blackExchangesList) {
        List<ExchangePair> exchangesList = new ArrayList<>();

        for (Exchange exchange1 : Splitter.exchanges) {
            blackExchangesList.add(exchange1.getName());
            for (Exchange exchange2 : Splitter.exchanges) {
                if (blackExchangesList.contains(exchange2.getName())) {
                    continue;
                }
                exchangesList.add(new ExchangePair(exchange1, exchange2));
            }
        }

        return exchangesList;
    }
}