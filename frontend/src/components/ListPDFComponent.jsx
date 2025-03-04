import React, { useEffect, useState } from "react";
import { listPDFs, deletePDF } from "../services/PDFService.js";
import PDFEditorComponent from "./PDFEditorComponent.jsx";
import SearchBar from "./SearchBar.jsx";  // Import SearchBar

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

    const handleSearchResults = (results) => {
        setPDFentries(results);  // Update the displayed PDFs with search results
    };

    const handleDelete = (pdfId) => {
        deletePDF(pdfId)
            .then(() => {
                setSelectedPDF(null);
                fetchPDFs();  // Refresh list after deleting
            })
            .catch((error) => {
                console.error("Error deleting PDF:", error);
            });
    };

    return (
        <div style={{ display: "flex", flex: 1 }}>
            <div style={{flex: 1, paddingRight: "20px"}}>
                <div style={{marginBottom: '20px'}}>
                    <SearchBar onSearchResults={handleSearchResults}/>
                </div>
                <table className="table table-striped table-bordered" style={{width: "100%"}}>
                    <thead>
                    <tr>
                        <th>File Name</th>
                        <th>Upload Date</th>
                        <th>File Size</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pdfentries.map((pdf) => (
                        <tr key={pdf.id} onClick={() => setSelectedPDF(pdf)} style={{cursor: "pointer"}}>
                            <td>{pdf.fileName}</td>
                            <td>{pdf.uploadDate}</td>
                            <td>{pdf.fileSize}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {/* PDF Editor */}
            <div style={{flex: 2}}>
                <PDFEditorComponent selectedPDF={selectedPDF} onUpdate={fetchPDFs} handleDelete={handleDelete} />
            </div>
        </div>
    );
};

export default ListPDFComponent;
