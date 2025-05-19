import React from 'react';
import { Card, Table, Badge } from 'react-bootstrap';
import { FaCoins } from 'react-icons/fa';

function AssetHoldings({ assets, cryptoPrices, onSelectCrypto }) {
    const calculateTotalValue = () => {
        return assets.reduce((total, asset) => {
            const crypto = cryptoPrices.find(c => c.symbol === asset.symbol);
            if (crypto) {
                return total + (asset.quantity * crypto.price);
            }
            return total;
        }, 0);
    };

    const getCurrentPrice = (symbol) => {
        const crypto = cryptoPrices.find(c => c.symbol === symbol);
        return crypto ? crypto.price : 0;
    };

    const calculateAssetValue = (asset) => {
        const price = getCurrentPrice(asset.symbol);
        return asset.quantity * price;
    };

    const calculateProfitLoss = (asset) => {
        const currentValue = calculateAssetValue(asset);
        const purchaseValue = asset.averagePurchasePrice * asset.quantity;
        return currentValue - purchaseValue;
    };

    const calculateProfitLossPercentage = (asset) => {
        const purchaseValue = asset.averagePurchasePrice * asset.quantity;
        if (purchaseValue === 0) return 0;

        const profitLoss = calculateProfitLoss(asset);
        return (profitLoss / purchaseValue) * 100;
    };

    return (
        <Card className="mb-4">
            <Card.Header>
                <div className="d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">
                        <FaCoins className="me-2" /> Your Assets
                    </h5>
                    <span>
            Total Value: ${calculateTotalValue().toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </span>
                </div>
            </Card.Header>
            <Card.Body className="p-0">
                {assets.length === 0 ? (
                    <div className="p-4 text-center">
                        <p className="mb-0">You don't own any cryptocurrencies yet.</p>
                    </div>
                ) : (
                    <div className="table-responsive">
                        <Table hover>
                            <thead>
                            <tr>
                                <th>Asset</th>
                                <th>Quantity</th>
                                <th>Avg. Price</th>
                                <th>Current Value</th>
                                <th>Profit/Loss</th>
                            </tr>
                            </thead>
                            <tbody>
                            {assets.map((asset) => {
                                const profitLoss = calculateProfitLoss(asset);
                                const profitLossPercentage = calculateProfitLossPercentage(asset);
                                const currentValue = calculateAssetValue(asset);

                                return (
                                    <tr
                                        key={asset.symbol}
                                        onClick={() => {
                                            const crypto = cryptoPrices.find(c => c.symbol === asset.symbol);
                                            if (crypto) onSelectCrypto(crypto);
                                        }}
                                        className="asset-row"
                                    >
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <div className="crypto-icon me-2">
                                                    {asset.symbol.charAt(0)}
                                                </div>
                                                <div>
                                                    <div><strong>{asset.symbol}</strong></div>
                                                    <small>{asset.name}</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td>{asset.quantity}</td>
                                        <td>${asset.averagePurchasePrice.toFixed(2)}</td>
                                        <td>${currentValue.toFixed(2)}</td>
                                        <td>
                                            <Badge bg={profitLoss >= 0 ? "success" : "danger"}>
                                                {profitLoss >= 0 ? "+" : "-"}${Math.abs(profitLoss).toFixed(2)}
                                                <br />
                                                ({Math.abs(profitLossPercentage).toFixed(2)}%)
                                            </Badge>
                                        </td>
                                    </tr>
                                );
                            })}
                            </tbody>
                        </Table>
                    </div>
                )}
            </Card.Body>
        </Card>
    );
}

export default AssetHoldings;