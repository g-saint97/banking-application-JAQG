import React, { useState } from 'react';
import axios from 'axios';

const CreateAccountModal = ({ isOpen, onClose, customerId, onAccountCreated }) => {
  const [formData, setFormData] = useState({
    customerId: customerId || 0,
    accountName: '',
    openingBalance: 0,
  });

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/account', formData);
      console.log('Account Created:', response.data);
      onAccountCreated(); // Notify parent component
      onClose();
      alert("Account created!")
      location.reload();
    } catch (error) {
      console.error('Error creating account:', error);
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white w-full max-w-md mx-4 md:mx-0 rounded-lg shadow-lg p-6">
        <h2 className="text-2xl font-semibold mb-4">Create New Account</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-medium mb-2" htmlFor="accountName">
              Account Name
            </label>
            <input
              type="text"
              id="accountName"
              name="accountName"
              value={formData.accountName}
              onChange={handleChange}
              className="border border-gray-300 rounded-md w-full px-3 py-2"
              required
            />
          </div>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-medium mb-2" htmlFor="openingBalance">
              Opening Balance
            </label>
            <input
              type="number"
              id="openingBalance"
              name="openingBalance"
              value={formData.openingBalance}
              onChange={handleChange}
              className="border border-gray-300 rounded-md w-full px-3 py-2"
              required
            />
          </div>
          <div className="flex justify-between">
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-600 text-white font-medium rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Create
            </button>
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-300 text-gray-700 font-medium rounded-md shadow-sm hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateAccountModal;
