import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; 
import DeleteOutlined from '@mui/icons-material/DeleteOutlined';
import Options from "@mui/icons-material/ArrowDownwardSharp";
import TransactionForm from './TransactionForm';
import axios from "axios";

const AccountInfo = ({ accounts, onDeleteAccount, customerId }) => {
  const [openDropdown, setOpenDropdown] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [accountsState, setAccountsState] = useState(accounts || []);
  const navigate = useNavigate(); 

    const fetchUpdateAccountData = async () => {
      try {
        const customerResp = await axios.get(`http://localhost:8080/customer/${customerId}`);
        const customerAccounts = customerResp.data.accounts;

        const fetchedAccounts = await Promise.all(
          customerAccounts.map(async (accountNumber) => {
            const response = await axios.get(`http://localhost:8080/account/${accountNumber}`);
            return response.data;
          })
        );

        setAccountsState(fetchedAccounts);
        console.log(fetchedAccounts);
      } catch (error) {
        console.error('Error fetching updated account data:', error);
      }
    };
    useEffect(() => {
      if (customerId) {
        fetchUpdateAccountData();
      }
    }, [customerId]);

  const handleViewTransactions = (accountNumber) => {
    navigate(`/transactions/${accountNumber}`);
  };

  const handleDropdownToggle = (accountNumber) => {
    setOpenDropdown(openDropdown === accountNumber ? null : accountNumber);
  };

  const handleOpenModal = (accountNumber) => {
    setSelectedAccount(accountNumber);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedAccount(null);
  };

  const handleCompletedTransaction = () => {
      fetchUpdateAccountData();
  }

  const updateAccountBalance = (transaction) => {
    setAccountsState(prevAccounts => 
      prevAccounts.map(account => {
        if (account.number === transaction.fromAccount) {
          return {
            ...account,
            balance: account.balance - transaction.amount
          };
        }
        if (account.number === transaction.toAccount) {
          return {
            ...account,
            balance: account.balance + transaction.amount
          };
        }
        return account;
      })
    );
  };

  return (
    <div className="max-w-4xl mx-auto mt-10">
      {accountsState.length > 0 ? (
        <ul key={accountsState} className="space-y-4">
          <>
          {accountsState.map((account) => (
            <li 
              key={account.id || account.number}
              className="flex justify-between relative border-b border-gray-200 p-4 m-4 w-full bg-white shadow-lg rounded-lg"
            >
              <div>
                <p className="text-xl font-semibold text-gray-700">
                  Account Name: <span className="font-normal">{account.name}</span>
                </p>
                <p className="text-xl font-semibold text-gray-700">
                  Opening Balance: <span className="font-normal">${account.openingBalance?.toFixed(2)}</span>
                </p>
                <p className="text-xl font-semibold text-gray-700">
                  Current Balance: <span className="font-normal">${account.balance?.toFixed(2)}</span>
                </p>
              </div>
              <div>
                <button
                  onClick={() => handleDropdownToggle(account.number)}
                  className="mt-2 px-4 py-2 hover:underline text-black font-medium rounded-md shadow-sm hover:focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
                >
                  <Options />
                </button>
                {openDropdown === account.number && (
                  <div className="absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded-md shadow-lg z-10">
                    <button
                      onClick={() => onDeleteAccount(account.number)}
                      className="block px-4 py-2 text-black font-medium rounded-md hover:bg-red-100 hover:text-red-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 w-full text-left"
                    >
                      <DeleteOutlined className="inline mr-2" /> Delete Account
                    </button>
                    <button
                      onClick={() => handleViewTransactions(account.number)}
                      className="block px-4 py-2 text-black font-medium rounded-md hover:bg-blue-100 hover:text-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 w-full text-left"
                    >
                      View Transactions
                    </button>
                    <button
                      onClick={() => handleOpenModal(account.number)}
                      className="block px-4 py-2 text-black font-medium rounded-md hover:bg-green-100 hover:text-green-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 w-full text-left"
                    >
                      Make a Transaction
                    </button>
                  </div>
                )}
              </div>
            </li>
          ))}
        </>
        </ul>
      ) : (
        <p className="text-center text-gray-500 text-lg">No accounts available.</p>
      )}

      {selectedAccount && (
        <TransactionForm
          isOpen={isModalOpen}
          onClose={handleCloseModal}
          accountNumber={selectedAccount}
          onTransaction={updateAccountBalance}
          onTransactionCompleted={handleCompletedTransaction}
        />
      )}
    </div>
  );
};

export default AccountInfo;
