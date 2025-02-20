import React, { useState } from "react";
import { uploadMetadata } from "../services/pdfService"; // Import service

const UploadPDFButton = () => {
    const [selectedFile, setSelectedFile] = useState(null);

    const handleFileChange = (event) => {
        const file = event.target.files[0]; // Get the selected file
        if (!file) return;

        // Extract metadata
        const metadata = {
            name: file.name,
            size: (file.size / 1024).toFixed(2) + " KB", // Convert bytes to KB
            uploadTime: new Date().toISOString(), // Store as ISO string
        };

        setSelectedFile(metadata);
    };

    const handleUploadClick = async () => {
        if (!selectedFile) {
            alert("Please select a PDF first!");
            return;
        }

        try {
            await uploadMetadata(selectedFile); // Call the service
            window.location.reload();
        } catch (error) {
            console.error("Error uploading metadata:", error);
            alert("Failed to upload metadata.");
        }
    };

    return (
        <div>
            <input type="file" accept="application/pdf" onChange={handleFileChange} />
            <button onClick={handleUploadClick}>Upload PDF</button>

        </div>
    );
};

export default UploadPDFButton;
