import React from 'react';
import { Card, Table, Badge } from 'react-bootstrap';
import { FaArrowUp, FaArrowDown } from 'react-icons/fa';

function CryptoPriceTable({ cryptoPrices, onSelectCrypto }) {
    return (
        <Card className="mb-4">
            <Card.Header>
                <h5 className="mb-0">Live Cryptocurrency Prices</h5>
            </Card.Header>
            <Card.Body className="p-0">
                <div className="table-responsive crypto-table">
                    <Table hover>
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Symbol</th>
                            <th>Price (USD)</th>
                            <th>24h Change</th>
                            <th>Volume</th>
                        </tr>
                        </thead>
                        <tbody>
                        {cryptoPrices.map((crypto, index) => (
                            <tr
                                key={crypto.symbol}
                                onClick={() => onSelectCrypto(crypto)}
                                className="crypto-row"
                            >
                                <td>{index + 1}</td>
                                <td>
                                    <div className="d-flex align-items-center">
                                        <div className="crypto-icon me-2">
                                            {crypto.symbol.charAt(0)}
                                        </div>
                                        {crypto.name}
                                    </div>
                                </td>
                                <td><strong>{crypto.symbol}</strong></td>
                                <td>${crypto.price.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</td>
                                <td>
                                    <Badge bg={crypto.priceChange >= 0 ? "success" : "danger"}>
                                        {crypto.priceChange >= 0 ? <FaArrowUp size={10} /> : <FaArrowDown size={10} />}{' '}
                                        {Math.abs(crypto.priceChange).toFixed(2)}%
                                    </Badge>
                                </td>
                                <td>${(crypto.volume).toLocaleString()}</td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                </div>
            </Card.Body>
            <Card.Footer className="text-muted">
                <small>Click on a cryptocurrency to trade</small>
            </Card.Footer>
        </Card>
    );
}

export default CryptoPriceTable;