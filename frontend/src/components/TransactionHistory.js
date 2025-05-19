import React, { useState } from 'react';
import { Card, Table, Badge, Pagination } from 'react-bootstrap';
import { FaHistory, FaArrowDown, FaArrowUp } from 'react-icons/fa';

function TransactionHistory({ transactions }) {
    const [currentPage, setCurrentPage] = useState(1);
    const transactionsPerPage = 10;
    const indexOfLastTransaction = currentPage * transactionsPerPage;
    const indexOfFirstTransaction = indexOfLastTransaction - transactionsPerPage;
    const currentTransactions = transactions.slice(indexOfFirstTransaction, indexOfLastTransaction);
    const paginate = (pageNumber) => setCurrentPage(pageNumber);
    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleString();
    };

    return (
        <Card className="mb-4">
            <Card.Header>
                <h5 className="mb-0">
                    <FaHistory className="me-2" /> Transaction History
                </h5>
            </Card.Header>
            <Card.Body className="p-0">
                {transactions.length === 0 ? (
                    <div className="p-4 text-center">
                        <p className="mb-0">No transactions yet. Start trading to see your history.</p>
                    </div>
                ) : (
                    <>
                        <div className="table-responsive">
                            <Table hover>
                                <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Type</th>
                                    <th>Asset</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Total</th>
                                    <th>Profit/Loss</th>
                                </tr>
                                </thead>
                                <tbody>
                                {currentTransactions.map((transaction) => (
                                    <tr key={transaction.id}>
                                        <td>{formatDate(transaction.timestamp)}</td>
                                        <td>
                                            <Badge bg={transaction.type === 'BUY' ? 'primary' : 'danger'}>
                                                {transaction.type === 'BUY' ?
                                                    <><FaArrowDown size={10} /> BUY</> :
                                                    <><FaArrowUp size={10} /> SELL</>
                                                }
                                            </Badge>
                                        </td>
                                        <td><strong>{transaction.symbol}</strong></td>
                                        <td>{transaction.quantity}</td>
                                        <td>${transaction.price.toFixed(2)}</td>
                                        <td>${(transaction.quantity * transaction.price).toFixed(2)}</td>
                                        <td>
                                            {transaction.type === 'SELL' && (
                                                <Badge bg={transaction.profitLoss >= 0 ? 'success' : 'danger'}>
                                                    {transaction.profitLoss >= 0 ? '+' : '-'}${Math.abs(transaction.profitLoss).toFixed(2)}
                                                </Badge>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        </div>

                        {/* Pagination */}
                        {transactions.length > transactionsPerPage && (
                            <div className="d-flex justify-content-center p-3">
                                <Pagination>
                                    <Pagination.Prev
                                        onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                                        disabled={currentPage === 1}
                                    />

                                    {Array.from({ length: Math.ceil(transactions.length / transactionsPerPage) }).map((_, index) => (
                                        <Pagination.Item
                                            key={index + 1}
                                            active={index + 1 === currentPage}
                                            onClick={() => paginate(index + 1)}
                                        >
                                            {index + 1}
                                        </Pagination.Item>
                                    ))}

                                    <Pagination.Next
                                        onClick={() => setCurrentPage(prev => Math.min(prev + 1, Math.ceil(transactions.length / transactionsPerPage)))}
                                        disabled={currentPage === Math.ceil(transactions.length / transactionsPerPage)}
                                    />
                                </Pagination>
                            </div>
                        )}
                    </>
                )}
            </Card.Body>
        </Card>
    );
}

export default TransactionHistory;