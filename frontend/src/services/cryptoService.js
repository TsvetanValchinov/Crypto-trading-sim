const TOP_CRYPTO_PAIRS = [
  'BTC/USD', 'ETH/USD', 'SOL/USD', 'XRP/USD', 'ADA/USD',
  'DOT/USD', 'DOGE/USD', 'AVAX/USD', 'LINK/USD', 'LTC/USD',
  'MATIC/USD', 'BCH/USD', 'ATOM/USD', 'UNI/USD', 'ALGO/USD',
  'FIL/USD', 'XLM/USD', 'TRX/USD', 'ETC/USD', 'AAVE/USD'
];

const generateMockCryptoData = () => {
  return TOP_CRYPTO_PAIRS.map(pair => {
    const symbol = pair.split('/')[0];
    const basePrice = getBasePrice(symbol);
    const currentPrice = (basePrice + (Math.random() - 0.5) * basePrice * 0.02).toFixed(2);
    const priceChange = ((Math.random() - 0.5) * 5).toFixed(2);

    return {
      symbol,
      name: getCryptoName(symbol),
      price: parseFloat(currentPrice),
      priceChange: parseFloat(priceChange),
      volume: Math.floor(Math.random() * 10000000)
    };
  });
};

const getBasePrice = (symbol) => {
  const prices = {
    'BTC': 40000,
    'ETH': 2500,
    'SOL': 120,
    'XRP': 0.50,
    'ADA': 0.35,
    'DOT': 7,
    'DOGE': 0.07,
    'AVAX': 30,
    'LINK': 8,
    'LTC': 70,
    'MATIC': 0.80,
    'BCH': 350,
    'ATOM': 9,
    'UNI': 6,
    'ALGO': 0.15,
    'FIL': 5,
    'XLM': 0.10,
    'TRX': 0.08,
    'ETC': 20,
    'AAVE': 80
  };
  return prices[symbol] || 10;
};

const getCryptoName = (symbol) => {
  const names = {
    'BTC': 'Bitcoin',
    'ETH': 'Ethereum',
    'SOL': 'Solana',
    'XRP': 'Ripple',
    'ADA': 'Cardano',
    'DOT': 'Polkadot',
    'DOGE': 'Dogecoin',
    'AVAX': 'Avalanche',
    'LINK': 'Chainlink',
    'LTC': 'Litecoin',
    'MATIC': 'Polygon',
    'BCH': 'Bitcoin Cash',
    'ATOM': 'Cosmos',
    'UNI': 'Uniswap',
    'ALGO': 'Algorand',
    'FIL': 'Filecoin',
    'XLM': 'Stellar',
    'TRX': 'TRON',
    'ETC': 'Ethereum Classic',
    'AAVE': 'Aave'
  };
  return names[symbol] || symbol;
};

let cryptoData = generateMockCryptoData();
let subscribers = [];

setInterval(() => {
  cryptoData = cryptoData.map(crypto => {
    const priceChange = (Math.random() - 0.5) * crypto.price * 0.01;
    const newPrice = Math.max(0.01, crypto.price + priceChange);

    return {
      ...crypto,
      price: parseFloat(newPrice.toFixed(2)),
      priceChange: parseFloat((priceChange / crypto.price * 100).toFixed(2))
    };
  });

  subscribers.forEach(callback => callback(cryptoData));
}, 3000);

const connectToKrakenWS = () => {
  console.log('Would connect to Kraken WebSocket API here');

  return {
    subscribe: (callback) => {
      subscribers.push(callback);
      callback(cryptoData);

      return () => {
        subscribers = subscribers.filter(cb => cb !== callback);
      };
    }
  };
};
const getCurrentPrice = (symbol) => {
  const crypto = cryptoData.find(c => c.symbol === symbol);
  return crypto ? crypto.price : null;
};

const cryptoService = {
  connectToKrakenWS,
  getCurrentPrice,
  TOP_CRYPTO_PAIRS
};
export default cryptoService;
