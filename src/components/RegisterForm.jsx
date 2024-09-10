import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Link } from "react-router-dom";

const RegisterForm = () => {
  const [formData, setFormData] = useState({
    fullName: ""
  });

  const navigate = useNavigate(); // initializing useNavigate

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmitCustomer = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8080/customer",
        formData
      );
      const { id } = response.data;

      // Prompt user with their Customer ID
      alert(`Registration successful! Your Customer ID is: ${id}`);

      console.log("User Created:", response.data);
      setFormData({
        fullName: ""
      });

      navigate("/login");
    } catch (error) {
      console.error("Something went wrong:", error);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <form 
        className="w-full max-w-sm bg-white p-8 rounded-lg shadow-lg space-y-4"
        onSubmit={handleSubmitCustomer}
      >
        <h3 className="text-2xl font-semibold mb-6 text-gray-800 text-center">Register</h3>
        <div>
          <label 
            htmlFor="fullName" 
            className="block text-gray-700 text-sm font-semibold mb-2"
          >
            Full Name
          </label>
          <input
            type="text"
            id="fullName"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          Register
        </button>
        <p className="mt-4 text-center text-gray-600">
          Have an account?{' '}
          <Link to="/login" className="text-blue-600 hover:text-blue-700">
            Log in
          </Link>
        </p>
      </form>
    </div>
  );
};

export default RegisterForm;
