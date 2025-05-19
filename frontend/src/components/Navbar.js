import React from 'react';
import { Navbar as BootstrapNavbar, Container, Button } from 'react-bootstrap';
import { FaCoins, FaSync } from 'react-icons/fa';

function Navbar({ balance, onReset }) {
    return (
        <BootstrapNavbar bg="dark" variant="dark" expand="lg">
            <Container>
                <BootstrapNavbar.Brand href="#">
                    <FaCoins className="me-2" /> Crypto Trading Platform
                </BootstrapNavbar.Brand>

                <div className="d-flex align-items-center">
                    <div className="me-3 text-light">
                        <span className="balance-label">Balance:</span>
                        <span className="balance-amount">${balance.toLocaleString()}</span>
                    </div>

                    <Button
                        variant="outline-warning"
                        size="sm"
                        onClick={onReset}
                        title="Reset account balance"
                    >
                        <FaSync className="me-1" /> Reset Account
                    </Button>
                </div>
            </Container>
        </BootstrapNavbar>
    );
}

export default Navbar;

