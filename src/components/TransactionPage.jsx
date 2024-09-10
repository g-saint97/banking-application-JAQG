import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const TransactionsPage = () => {
  const { accountNumber } = useParams();
  const [transactions, setTransactions] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/account/${accountNumber}`); // add account number variable
        const account = response.data;

        if (account && account.transactions) {
          setTransactions(account.transactions);
        } else {
          setTransactions([]);
        }
      } catch (error) {
        console.error('Error fetching transactions:', error);
      }
    };

    fetchTransactions();
  }, [accountNumber]);

  return (
    <div className="max-w-7xl mx-auto mt-10 p-6 bg-white shadow-lg rounded-lg">
      <h1 className="text-3xl font-semibold mb-6 text-gray-800">Transactions for Account {accountNumber}</h1>
      <button
        onClick={() => {
          navigate("/dashboard");
        }}
        className="absolute top-4 right-4 hover:underline text-white px-4 py-2 rounded-md"
      >
        Back to dashboard
      </button>

      {transactions.length > 0 ? (
        <ul className="space-y-4">
          {transactions.map((transaction, index) => (
            <li key={index} className="border-b border-gray-200 p-4">
              <p className="text-lg font-medium text-gray-700">Date: {transaction.time}</p>
              <p className="text-lg font-medium text-gray-700">Type: {transaction.type}</p>
              <p className="text-lg font-medium text-gray-700">Amount: ${transaction.amount.toFixed(2)}</p>
              {transaction.toAccount && (
                <p className="text-lg font-medium text-gray-700">To Account: {transaction.toAccount}</p>
              )}
              {transaction.fromAccount && (
                <p className="text-lg font-medium text-gray-700">From Account: {transaction.fromAccount}</p>
              )}
              <p className="text-lg font-medium text-gray-700">
                {transaction.fromAccountSortCode
                  ? `From Sort Code: ${transaction.fromAccountSortCode}`
                  : `To Sort Code: ${transaction.toAccountSortCode}`}
              </p>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-gray-500">No transactions available.</p>
      )}
    </div>
  );
};

export default TransactionsPage;
