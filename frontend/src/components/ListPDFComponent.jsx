import React, { useEffect, useState } from "react";
import { listPDFs, deletePDF } from "../services/PDFService.js"; // Make sure to import the deletePDF function

const ListPDFComponent = () => {
    const [pdfentries, setPDFentries] = useState([]);

    // Fetch the PDFs when the component mounts
    useEffect(() => {
        fetchPDFs();
    }, []);

    // Function to fetch the PDFs
    const fetchPDFs = () => {
        listPDFs()
            .then((response) => {
                setPDFentries(response.data);
            })
            .catch((error) => {
                console.error("Error fetching PDFs:", error);
            });
    };

    // Function to handle the delete of a PDF
    const handleDelete = (pdfId) => {
        deletePDF(pdfId)
            .then(() => {
                // Refresh the list after deletion
                fetchPDFs();
            })
            .catch((error) => {
                console.error("Error deleting PDF:", error);
            });
    };

    return (
        <div className="container">
            <table className="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>File Name</th>
                    <th>Upload Date</th>
                    <th>File Size</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {pdfentries.map((pdf) => (
                    <tr key={pdf.id}>
                        <td>{pdf.fileName}</td>
                        <td>{pdf.uploadDate}</td>
                        <td>{pdf.fileSize}</td>
                        <td>
                            <button
                                className="btn btn-danger"
                                onClick={() => handleDelete(pdf.id)}>
                                Delete
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ListPDFComponent;