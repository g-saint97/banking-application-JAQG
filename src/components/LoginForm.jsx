import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../App";
import axios from "axios";
import {Link} from "react-router-dom";

const LoginForm = () => {
  const [formData, setFormData] = useState({ customerID: "" });
  const { setUser } = useContext(UserContext);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmitCustomer = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.get(
        `http://localhost:8080/customer/${formData.customerID}`,
        formData
      );

      console.log("Login Successful:", response.data);
      setUser(response.data);
      setFormData({ customerID: "" });
      navigate("/dashboard");
    } catch (error) {
      console.error("Something went wrong:", error);
      
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-lg max-w-sm w-full">
        <h3 className="text-2xl font-semibold mb-6 text-gray-800 text-center">Login</h3>
        <form onSubmit={handleSubmitCustomer}>
          <div className="mb-4">
            <label htmlFor="customerId" className="block text-gray-700 text-sm font-medium mb-2">Customer ID</label>
            <input
              type="text"
              id="customerId"
              name="customerID"
              value={formData.customerID}
              onChange={handleChange}
              required
              className="border border-gray-300 rounded-md w-full px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <button
            type="submit"
            className="w-full px-4 py-2 bg-indigo-600 text-white font-medium rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Login
          </button>
        <p className="mt-4 text-center text-gray-600">
          Don't have an account?{' '}
          <Link to="/" className="text-blue-600 hover:text-blue-700">
            Register here
          </Link>
        </p>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
