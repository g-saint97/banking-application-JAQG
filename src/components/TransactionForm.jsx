import React, { useState } from 'react';
import axios from 'axios';

const TransactionForm = ({ isOpen, onClose, accountNumber, onTransactionCompleted }) => {
  const [transactionType, setTransactionType] = useState('DEPOSIT');
  const [toAccount, setToAccount] = useState('');
  const [toAccountSortCode, setToAccountSortCode] = useState('');
  const [amount, setAmount] = useState('');
  const [sortCode, setSortCode] = useState('');

    const sortCodeData = async() => {
        try {
            const response = await axios.get('http://localhost:8080/sortCode');
            setSortCode(response.data);
        } catch ( error) {
            console.error("Something went wrong", error)
        }
        console.log(sortCode)
    }
    sortCodeData();


  const handleTransaction = async () => {
    const requestObject = {
        type: transactionType,
        fromAccount: transactionType !== 'DEPOSIT' ? accountNumber : null,
        fromAccountSortCode: transactionType !== 'DEPOSIT' ? sortCode : null,
        toAccount: transactionType !== 'WITHDRAWAL' ? toAccount || accountNumber : null,
        toAccountSortCode: transactionType !== 'WITHDRAWAL' ? toAccountSortCode || sortCode : null,
        amount: parseFloat(amount),
    };

    try {
        await axios.post('http://localhost:8080/transaction', requestObject);
        alert('Transaction successful!');

        // Notify parent component
        onTransactionCompleted();
        onClose();
    } catch (error) {
        console.error('Error processing transaction:', error);
        alert('Failed to process transaction.');
    }
};

  return isOpen ? (
    <div className="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex items-center justify-center z-50">
      <div className="bg-white p-8 rounded-md shadow-lg max-w-lg w-full">
        <h2 className="text-2xl font-semibold mb-4">Make a Transaction</h2>

        <div className="mb-4">
          <label className="block text-gray-700">Transaction Type</label>
          <select
            value={transactionType}
            onChange={(e) => setTransactionType(e.target.value)}
            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
          >
            <option value="DEPOSIT">Deposit</option>
            <option value="WITHDRAWAL">Withdrawal</option>
            <option value="TRANSFER">Transfer</option>
          </select>
        </div>

        {transactionType === 'TRANSFER' && (
          <>
            <div className="mb-4">
              <label className="block text-gray-700">To Account</label>
              <input
                type="text"
                value={toAccount}
                onChange={(e) => setToAccount(e.target.value)}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
              />
            </div>

            <div className="mb-4">
              <label className="block text-gray-700">To Account Sort Code</label>
              <input
                type="text"
                value={toAccountSortCode}
                onChange={(e) => setToAccountSortCode(e.target.value)}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
              />
            </div>
          </>
        )}

        <div className="mb-4">
          <label className="block text-gray-700">Amount</label>
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
          />
        </div>

        <div className="flex justify-end">
          <button
            onClick={onClose}
            className="mr-2 px-4 py-2 text-gray-700 bg-gray-200 rounded-md shadow-sm hover:bg-gray-300"
          >
            Cancel
          </button>
          <button
            onClick={handleTransaction}
            className="px-4 py-2 text-white bg-blue-600 rounded-md shadow-sm hover:bg-blue-700"
          >
            Submit
          </button>
        </div>
      </div>
    </div>
  ) : null;
};

export default TransactionForm;
