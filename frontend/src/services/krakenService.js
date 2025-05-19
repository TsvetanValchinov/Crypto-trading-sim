import axios from 'axios';

const TOP_CRYPTO_PAIRS = [
    'BTC/USD', 'ETH/USD', 'SOL/USD', 'XRP/USD', 'ADA/USD',
    'DOT/USD', 'DOGE/USD', 'AVAX/USD', 'LINK/USD', 'LTC/USD',
    'MATIC/USD', 'BCH/USD', 'ATOM/USD', 'UNI/USD', 'ALGO/USD',
    'FIL/USD', 'XLM/USD', 'TRX/USD', 'ETC/USD', 'AAVE/USD'
];

const getCryptoName = (symbol) => {
    const names = {
        'BTC': 'Bitcoin', 'ETH': 'Ethereum', 'SOL': 'Solana', 'XRP': 'Ripple',
        'ADA': 'Cardano', 'DOT': 'Polkadot', 'DOGE': 'Dogecoin', 'AVAX': 'Avalanche',
        'LINK': 'Chainlink', 'LTC': 'Litecoin', 'MATIC': 'Polygon', 'BCH': 'Bitcoin Cash',
        'ATOM': 'Cosmos', 'UNI': 'Uniswap', 'ALGO': 'Algorand', 'FIL': 'Filecoin',
        'XLM': 'Stellar', 'TRX': 'TRON', 'ETC': 'Ethereum Classic', 'AAVE': 'Aave'
    };
    return names[symbol] || symbol;
};

let cryptoData = [];
let subscribers = [];
let socket = null;

const fetchInitialPrices = async () => {
    try {
        const response = await axios.get('wss://ws.kraken.com/v2', {
            params: {
                pair: TOP_CRYPTO_PAIRS.join(',')
            }
        });

        if (response.data.error && response.data.error.length > 0) {
            console.error('Kraken API error:', response.data.error);
            return [];
        }

        const result = response.data.result || {};

        return TOP_CRYPTO_PAIRS.map(pair => {
            const pairData = result[pair] ||
                result[pair.replace('/', '')] ||
                result[pair.replace('BTC', 'XBT')] ||
                result[pair.replace('BTC/', 'XBT/')];

            const symbol = pair.split('/')[0];

            if (!pairData) {
                console.warn(`No data found for ${pair}`);
                return {
                    symbol,
                    name: getCryptoName(symbol),
                    price: 0,
                    priceChange: 0,
                    volume: 0
                };
            }
            const price = pairData.c ? parseFloat(pairData.c[0]) : 0;

            return {
                symbol,
                name: getCryptoName(symbol),
                price,
                priceChange: 0, // Will be filled by WebSocket updates
                volume: pairData.v ? parseFloat(pairData.v[1]) : 0 // 24h volume
            };
        });
    } catch (error) {
        console.error('Error fetching initial prices:', error);
        return [];
    }
};

const connectToKrakenWS = async () => {
    cryptoData = await fetchInitialPrices();
    subscribers.forEach(callback => callback(cryptoData));

    try {
        const wsUrl = 'wss://ws.kraken.com/v2';
        socket = new WebSocket(wsUrl);

        socket.onopen = () => {
            console.log('Connected to Kraken WebSocket v2');
            const subscriptionMsg = {
                method: 'subscribe',
                params: {
                    channel: 'ticker',
                    symbol: TOP_CRYPTO_PAIRS
                }
            };

            socket.send(JSON.stringify(subscriptionMsg));
        };

        socket.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                if (data.channel === 'ticker' && data.type === 'update' && Array.isArray(data.data)) {
                    data.data.forEach(tickerUpdate => {
                        const pair = tickerUpdate.symbol;
                        const symbol = pair.split('/')[0];
                        cryptoData = cryptoData.map(crypto => {
                            if (crypto.symbol === symbol) {
                                return {
                                    ...crypto,
                                    price: tickerUpdate.last || crypto.price,
                                    priceChange: tickerUpdate.change_pct || 0,
                                    volume: tickerUpdate.volume || crypto.volume
                                };
                            }
                            return crypto;
                        });
                    });
                    subscribers.forEach(callback => callback(cryptoData));
                }
            } catch (error) {
                console.error('Error processing WebSocket message:', error);
            }
        };

        socket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        socket.onclose = (event) => {
            console.log(`Disconnected from Kraken WebSocket: ${event.code} ${event.reason}`);
            // Attempt to reconnect after a delay
            setTimeout(() => connectToKrakenWS(), 5000);
        };

        return {
            subscribe: (callback) => {
                subscribers.push(callback);
                callback(cryptoData);
                return () => {
                    subscribers = subscribers.filter(cb => cb !== callback);
                };
            }
        };
    } catch (error) {
        console.error('Error connecting to WebSocket:', error);
        return startRESTAPIPolling();
    }
};

const startRESTAPIPolling = async () => {
    cryptoData = await fetchInitialPrices();
    subscribers.forEach(callback => callback(cryptoData));
    const pollingInterval = setInterval(async () => {
        try {
            const updatedData = await fetchInitialPrices();
            cryptoData = updatedData.map(newData => {
                const existingData = cryptoData.find(item => item.symbol === newData.symbol);

                if (existingData && existingData.price > 0) {
                    const priceChangePct = ((newData.price - existingData.price) / existingData.price) * 100;

                    return {
                        ...newData,
                        priceChange: parseFloat(priceChangePct.toFixed(2))
                    };
                }

                return newData;
            });

            subscribers.forEach(callback => callback(cryptoData));
        } catch (error) {
            console.error('Error polling prices:', error);
        }
    }, 10000);

    return {
        subscribe: (callback) => {
            subscribers.push(callback);
            callback(cryptoData);
            return () => {
                subscribers = subscribers.filter(cb => cb !== callback);
                if (subscribers.length === 0) {
                    clearInterval(pollingInterval);
                }
            };
        }
    };
};
const getCurrentPrice = (symbol) => {
    const crypto = cryptoData.find(c => c.symbol === symbol);
    return crypto ? crypto.price : null;
};

const disconnect = () => {
    if (socket && socket.readyState === WebSocket.OPEN) {
        const unsubscribeMsg = {
            method: 'unsubscribe',
            params: {
                channel: 'ticker',
                symbol: TOP_CRYPTO_PAIRS
            }
        };

        socket.send(JSON.stringify(unsubscribeMsg));
        socket.close();
    }
};

const krakenService = {
    connectToKrakenWS,
    getCurrentPrice,
    TOP_CRYPTO_PAIRS,
    disconnect
};
export default krakenService;