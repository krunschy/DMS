import React, { useEffect, useState } from "react";
import { listPDFs, deletePDF } from "../services/PDFService.js";
import PDFEditorComponent from "./PDFEditorComponent.jsx";

const ListPDFComponent = () => {
    const [pdfentries, setPDFentries] = useState([]);
    const [selectedPDF, setSelectedPDF] = useState(null);

    useEffect(() => {
        fetchPDFs();
    }, []);

    const fetchPDFs = () => {
        listPDFs()
            .then((response) => {
                setPDFentries(response.data);
            })
            .catch((error) => {
                console.error("Error fetching PDFs:", error);
            });
    };

    const handleDelete = (pdfId) => {
        deletePDF(pdfId)
            .then(() => {
                setSelectedPDF(null);
                fetchPDFs();
            })
            .catch((error) => {
                console.error("Error deleting PDF:", error);
            });
    };

    return (
        <div style={{ display: "flex" }}>
            <div style={{ flex: 1, paddingRight: "20px" }}>
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
                        <tr key={pdf.id} onClick={() => setSelectedPDF(pdf)} style={{ cursor: "pointer" }}>
                            <td>{pdf.fileName}</td>
                            <td>{pdf.uploadDate}</td>
                            <td>{pdf.fileSize}</td>
                            <td>
                                <button className="btn btn-danger" onClick={() => handleDelete(pdf.id)}>
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {/* PDF Editor */}
            <PDFEditorComponent selectedPDF={selectedPDF} onUpdate={fetchPDFs} />
        </div>
    );
};

export default ListPDFComponent;
