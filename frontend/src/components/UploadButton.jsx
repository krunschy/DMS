import React, { useState } from "react";
import { uploadPDF } from "../services/PDFService";

const UploadPDFButton = () => {
    const [selectedFile, setSelectedFile] = useState(null);

    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (!file) return;

        setSelectedFile(file);
    };

    const handleUploadClick = async () => {
        if (!selectedFile) {
            alert("Please select a PDF first!");
            return;
        }

        try {
            await uploadPDF(selectedFile);
            window.location.reload();
        } catch (error) {
            console.error("Error uploading file:", error);
            alert("Failed to upload file.");
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
