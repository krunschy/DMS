import React, { useState } from "react";
import { searchPDFs } from "../services/PDFService";  // Import the searchPDFs service function

const SearchBar = ({ onSearchResults, onReset }) => {
    const [searchText, setSearchText] = useState("");

    const handleSearchChange = (event) => {
        setSearchText(event.target.value);  // Update state with search input
    };

    const handleSearchClick = () => {
        searchPDFs(searchText)  // Call searchPDFs with the search text
            .then((data) => {
                onSearchResults(data);  // Pass the search results to the parent component
            })
            .catch((error) => {
                console.error("Error searching PDFs:", error);
            });
    };

    return (
        <div style={{ display: "flex", alignItems: "center" }}>
            <input
                type="text"
                placeholder="Search..."
                value={searchText}
                onChange={handleSearchChange}
                style={{ padding: "10px", fontSize: "16px", width: "200px", marginRight: "10px" }}
            />
            <button
                onClick={handleSearchClick}
                style={{ padding: "10px", fontSize: "16px", marginRight: "10px" }}
            >
                Search
            </button>
            <button
                onClick={onReset}
                style={{ padding: "10px", fontSize: "16px" }}
            >
                Reset
            </button>
        </div>
    );
};

export default SearchBar;
