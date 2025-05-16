package com.example.backend.Crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class KrakenWebSocketService {

    private WebSocket webSocket;
    private final ObjectMapper objectMapper;
    private BiConsumer<String, BigDecimal> priceUpdateHandler;
    private final ConcurrentHashMap<String, BigDecimal> buyPrices = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BigDecimal> sellPrices = new ConcurrentHashMap<>();

    private final List<String> pairs = Arrays.asList(
            "BTC/USD", "ETH/USD", "XRP/USD", "DOGE/USD", "ADA/USD",
            "DOT/USD", "UNI/USD", "LTC/USD", "LINK/USD", "BCH/USD",
            "XLM/USD", "EOS/USD", "TRX/USD", "XTZ/USD", "ATOM/USD",
            "AAVE/USD", "SNX/USD", "GRT/USD", "YFI/USD", "COMP/USD"
    );

    String KrakenURL = "wss://ws.kraken.com/v2";
    //String SubscriptionMsg = "{ \"event\":\"subscribe\", \"subscription\":{\"name\":\"trade\"},\"pair\":[\"XBT/USD\"] }";

    public KrakenWebSocketService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPriceUpdateHandler(BiConsumer<String, BigDecimal> priceUpdateHandler) {
        this.priceUpdateHandler = priceUpdateHandler;
    }

    @PostConstruct
    public void connect() {
        HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(URI.create(KrakenURL), new WebSocket.Listener() {

            @Override
            public void onOpen(WebSocket ws) {
                webSocket = ws;
                pairs.forEach(pair -> {
                    String msg = String.format("""
                            {
                                "method": "subscribe",
                                "params": {
                                  "channel": "ticker",
                                  "symbol": ["%s"]
                                  }
                            }""", pair);
                    ws.sendText(msg, true);
                });
                WebSocket.Listener.super.onOpen(ws);
            }

            @Override
            public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last) {
                handleMessage(data.toString());
                return WebSocket.Listener.super.onText(ws, data, last);
            }

            @Override
            public void onError(WebSocket ws, Throwable error) {
                System.err.println("Error: " + error.getMessage());
                reconnect();
            }

            @Override
            public CompletionStage<?> onClose(WebSocket ws, int statusCode, String reason) {
                System.out.println("Closed: " + reason);
                reconnect();
                return WebSocket.Listener.super.onClose(ws,
                        statusCode, reason);
            }
        });
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Shutdown").thenRun(() -> System.out.println("WebSocket closed"));
        }
    }

    public void reconnect() {
        if(webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Reconnecting").thenRun(this::connect);
        }
        else {
            connect();
        }
    }

    private  void handleMessage(String message) {
        try{
            JsonNode jsonNode = objectMapper.readTree(message);
            if(jsonNode.has("channel") && "ticker".equals(jsonNode.get("channel").asText()) &&
                    jsonNode.has("data") && jsonNode.get("data").isArray()){
                for(JsonNode entry : jsonNode.get("data")){
                    String symbol = jsonNode.get("symbol").asText();

                    if(entry.has("bid")){
                        BigDecimal bid = new BigDecimal(entry.get("bid").asText());
                        buyPrices.put(symbol, bid);
                    }

                    if(entry.has("ask")){
                        BigDecimal ask = new BigDecimal(entry.get("ask").asText());
                        sellPrices.put(symbol, ask);
                    }

                    if(priceUpdateHandler != null && entry.has("last")){
                        priceUpdateHandler.accept(symbol, new BigDecimal(entry.get("last").asText()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse Kraken message " + e.getMessage());
        }
    }

    public  BigDecimal getBuyPrice(String symbol) {
        return buyPrices.getOrDefault(symbol, BigDecimal.ZERO);
    }

    public  BigDecimal getSellPrice(String symbol) {
        return sellPrices.getOrDefault(symbol, BigDecimal.ZERO);
    }

    public ConcurrentMap<String, BigDecimal> getBuyPrices() {
        return buyPrices;
    }

    public ConcurrentMap<String, BigDecimal> getSellPrices() {
        return sellPrices;
    }


}
