// src/components/SearchBar.js
import React, { useState } from "react";

const SearchBar = () => {
    const [searchText, setSearchText] = useState("");

    const handleSearchChange = (event) => {
        setSearchText(event.target.value);  // Update the state with input value
    };

    const handleSearchClick = () => {
        alert(`You searched for: ${searchText}`);  // Show the text in an alert
    };

    return (
        <div>
            <input
                type="text"
                placeholder="Search..."
                value={searchText}
                onChange={handleSearchChange}
                style={{ padding: "10px", fontSize: "16px", width: "200px", marginRight: "10px" }}
            />
            <button onClick={handleSearchClick}>Search</button>
        </div>
    );
};

export default SearchBar;
