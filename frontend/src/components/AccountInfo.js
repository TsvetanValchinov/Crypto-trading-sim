import React from 'react';
import { Card } from 'react-bootstrap';
import { FaWallet } from 'react-icons/fa';

function AccountInfo({ balance }) {
    return (
        <Card className="mb-4 account-info-card">
            <Card.Body>
                <div className="d-flex align-items-center">
                    <div className="account-icon">
                        <FaWallet size={24} />
                    </div>
                    <div className="ms-3">
                        <h6 className="mb-0">Available Balance</h6>
                        <h3 className="mb-0">${balance.toLocaleString()}</h3>
                    </div>
                </div>
            </Card.Body>
        </Card>
    );
}

export default AccountInfo;