import React, { useState, useEffect } from 'react';
import { Card, Form, Button, InputGroup, Row, Col, Alert } from 'react-bootstrap';
import { FaExchangeAlt, FaArrowDown, FaArrowUp } from 'react-icons/fa';

function TradeForm({ selectedCrypto, assets, balance, onBuy, onSell }) {
    const [quantity, setQuantity] = useState('');
    const [tradeType, setTradeType] = useState('buy');
    const [totalCost, setTotalCost] = useState(0);
    const [error, setError] = useState('');
    const [asset, setAsset] = useState(null);

    useEffect(() => {
        if (selectedCrypto && quantity) {
            const total = selectedCrypto.price * parseFloat(quantity);
            setTotalCost(total);
        } else {
            setTotalCost(0);
        }
    }, [selectedCrypto, quantity]);

    useEffect(() => {
        if (selectedCrypto && assets) {
            const foundAsset = assets.find(a => a.symbol === selectedCrypto.symbol);
            setAsset(foundAsset);
        } else {
            setAsset(null);
        }
    }, [selectedCrypto, assets]);

    const handleSubmit = (e) => {
        e.preventDefault();
        setError('');
        if (!selectedCrypto) {
            setError('Please select a cryptocurrency first');
            return;
        }
        if (!quantity || parseFloat(quantity) <= 0) {
            setError('Please enter a valid quantity');
            return;
        }
        if (tradeType === 'buy') {
            if (totalCost > balance) {
                setError('Insufficient balance for this purchase');
                return;
            }
            onBuy(selectedCrypto.symbol, quantity);
        } else {
            if (!asset || parseFloat(quantity) > asset.quantity) {
                setError('Insufficient crypto balance for this sale');
                return;
            }
            onSell(selectedCrypto.symbol, quantity);
        }
        setQuantity('');
    };

    return (
        <Card className="mb-4">
            <Card.Header>
                <div className="d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Trade Crypto</h5>
                    {selectedCrypto && (
                        <div className="selected-crypto">
                            {selectedCrypto.name} ({selectedCrypto.symbol})
                        </div>
                    )}
                </div>
            </Card.Header>
            <Card.Body>
                {!selectedCrypto ? (
                    <Alert variant="info">
                        Please select a cryptocurrency from the table to start trading
                    </Alert>
                ) : (
                    <Form onSubmit={handleSubmit}>
                        {error && <Alert variant="danger">{error}</Alert>}

                        <Form.Group className="mb-3">
                            <div className="d-flex justify-content-center mb-3 trade-toggle">
                                <Button
                                    variant={tradeType === 'buy' ? 'primary' : 'outline-primary'}
                                    className="me-2"
                                    onClick={() => setTradeType('buy')}
                                >
                                    <FaArrowDown className="me-1" /> Buy
                                </Button>
                                <Button
                                    variant={tradeType === 'sell' ? 'danger' : 'outline-danger'}
                                    onClick={() => setTradeType('sell')}
                                >
                                    <FaArrowUp className="me-1" /> Sell
                                </Button>
                            </div>
                        </Form.Group>

                        <Row className="mb-3">
                            <Col>
                                <Form.Group>
                                    <Form.Label>Quantity</Form.Label>
                                    <InputGroup>
                                        <Form.Control
                                            type="number"
                                            step="0.00000001"
                                            min="0"
                                            value={quantity}
                                            onChange={(e) => setQuantity(e.target.value)}
                                            placeholder="0.00"
                                        />
                                        <InputGroup.Text>{selectedCrypto.symbol}</InputGroup.Text>
                                    </InputGroup>
                                </Form.Group>
                            </Col>
                        </Row>

                        <Row className="mb-3">
                            <Col>
                                <Form.Group>
                                    <Form.Label>Unit Price</Form.Label>
                                    <InputGroup>
                                        <InputGroup.Text>$</InputGroup.Text>
                                        <Form.Control
                                            value={selectedCrypto.price.toFixed(2)}
                                            disabled
                                        />
                                    </InputGroup>
                                </Form.Group>
                            </Col>
                        </Row>

                        <Row className="mb-3">
                            <Col>
                                <Form.Group>
                                    <Form.Label>Total {tradeType === 'buy' ? 'Cost' : 'Received'}</Form.Label>
                                    <InputGroup>
                                        <InputGroup.Text>$</InputGroup.Text>
                                        <Form.Control
                                            value={totalCost.toFixed(2)}
                                            disabled
                                        />
                                    </InputGroup>
                                </Form.Group>
                            </Col>
                        </Row>

                        {tradeType === 'buy' ? (
                            <div className="mb-3 trade-info">
                                <small>Available balance: ${Number(balance).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</small>
                            </div>
                        ) : (
                            <div className="mb-3 trade-info">
                                <small>
                                    Available {selectedCrypto.symbol}: {asset ? asset.quantity : 0}
                                </small>
                            </div>
                        )}

                        <div className="d-grid">
                            <Button
                                variant={tradeType === 'buy' ? 'primary' : 'danger'}
                                type="submit"
                            >
                                <FaExchangeAlt className="me-1" />
                                {tradeType === 'buy' ? 'Buy' : 'Sell'} {selectedCrypto.symbol}
                            </Button>
                        </div>
                    </Form>
                )}
            </Card.Body>
        </Card>
    );
}

export default TradeForm;