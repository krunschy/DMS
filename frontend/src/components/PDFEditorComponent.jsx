import React, { useState } from "react";
import { updatePDF } from "../services/PDFService.js"; // Ensure this function exists

const PDFEditorComponent = ({ selectedPDF, onUpdate }) => {
    const [editedContent, setEditedContent] = useState(selectedPDF?.fileContent || "");

    // Update state when a new PDF is selected
    React.useEffect(() => {
        setEditedContent(selectedPDF?.fileContent || "");
    }, [selectedPDF]);

    const handleSave = () => {
        if (!selectedPDF) return;

        const updatedPDF = {
            ...selectedPDF,
            fileContent: editedContent,
        };

        updatePDF(updatedPDF)
            .then(() => {
                onUpdate(); // Refresh the list after saving
            })
            .catch((error) => {
                console.error("Error updating PDF:", error);
            });
    };

    return (
        <div style={{ flex: 1, padding: "20px", borderLeft: "1px solid #ddd" }}>
            <h3>PDF Content</h3>
            {selectedPDF ? (
                <>
                    <textarea
                        style={{ width: "100%", height: "300px" }}
                        value={editedContent}
                        onChange={(e) => setEditedContent(e.target.value)}
                    />
                    <button className="btn btn-primary" onClick={handleSave} style={{ marginTop: "10px" }}>
                        Save Changes
                    </button>
                </>
            ) : (
                <p>Select a PDF to edit.</p>
            )}
        </div>
    );
};

export default PDFEditorComponent;
