import React, { useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { UserContext } from '../App';
import AccountInfo from './AccountInfo';
import CreateAccountModal from './CreateAccountModal';
import AddIcon from '@mui/icons-material/Add';

const BankDashBoard = () => {
  const { user } = useContext(UserContext);
  const [userData, setUserData] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    const fetchUserInfo = async () => {
      if (user && user.id) {
        try {
          const response = await axios.get(`http://localhost:8080/customer/${user.id}`);
          console.log("User Found:", response.data);
          
          const parsedFullName = JSON.parse(response.data.fullName).fullName;
          const userData = { ...response.data, fullName: parsedFullName };

          setUserData(userData);
          localStorage.setItem('userData', JSON.stringify(userData));
        } catch (error) {
          console.error("Something went wrong:", error);
        }
      }
    };

    const storedUserData = localStorage.getItem('userData');
    if (storedUserData) {
      setUserData(JSON.parse(storedUserData));
    } else {
      fetchUserInfo();
    }
  }, [user]);

  const handleAccountCreated = async () => {
    if (userData) {
      try {
        const response = await axios.get(`http://localhost:8080/account`);
        const filteredAccounts = response.data.filter(account => account.customer === userData.id && account.balance > 0);
        const updatedUserData = { ...userData, accounts: filteredAccounts }
        setUserData(updatedUserData);
        localStorage.setItem("userData", JSON.stringify(updatedUserData));
      } catch (error) {
        console.error('Error fetching accounts:', error);
      }
    }
  };

  const handleDeleteAccount = async (accountNumber) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this account?");
    if (confirmDelete) {
      try {
        await axios.delete(`http://localhost:8080/account/${accountNumber}`);
        handleAccountCreated(); // Fetch accounts after deletion
        alert("Account deleted")
        location.reload();
      } catch (error) {
        console.error('Error deleting account:', error);
      }
    }
  };

  return (
    <div className="bg-gray-100 w-full relative">
      <div className="max-w-7xl mx-auto">
        <div className='flex justify-between pt-4'>
          <button
            onClick={() => {
              localStorage.removeItem('userData'); // Clear localStorage
              window.location.href = '/'; // Redirect to register
            }}
            className="absolute top-4 right-4 hover:underline text-black px-4 py-2 rounded-md"
          >
            Logout
          </button>
          <h1 className="text-3xl font-semibold mb-6 text-gray-800">
            Welcome, {userData?.fullName || 'User'}
          </h1>
        </div>
        
        <div className='flex justify-between pt-5'>
          <p className="text-lg font-medium text-gray-600">Your Accounts</p>
          <button
            onClick={() => setIsModalOpen(true)}
            className="px-4 py-2 bg-blue-600 text-white font-medium rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 mb-6"
          >
            <AddIcon />
          </button>
        </div>
        
        {userData ? (
          <div>
            <div className="mb-6">              
              <AccountInfo 
                customerId={userData.id} 
                accounts={userData.accounts || []} 
                onDeleteAccount={handleDeleteAccount} 
              />
            </div>
          </div>
        ) : (
          <p className="text-gray-500">Loading user data...</p>
        )}
      </div>
      
      {isModalOpen && (
        <>
          <div className="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm z-40" />
          <CreateAccountModal 
            isOpen={isModalOpen} 
            onClose={() => setIsModalOpen(false)} 
            customerId={userData?.id} 
            onAccountCreated={handleAccountCreated} // Ensure accounts are refreshed after creation
          />
        </>
      )}
    </div>
  );
};

export default BankDashBoard;
