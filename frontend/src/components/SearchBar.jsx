import React, { useState } from "react";
import { searchPDFs } from "../services/PDFService";
const SearchBar = ({ onSearchResults, onReset }) => {
    const [searchText, setSearchText] = useState("");

    const handleSearchChange = (event) => {
        setSearchText(event.target.value);
    };

    const handleSearchClick = () => {
        searchPDFs(searchText)
            .then((data) => {
                onSearchResults(data);
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
