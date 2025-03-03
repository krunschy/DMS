import React, { useEffect, useState } from "react";
import { updatePDF } from "../services/PDFService.js";

const PDFEditorComponent = ({ selectedPDF, onUpdate, handleDelete }) => {
    const [editedContent, setEditedContent] = useState(selectedPDF?.fileContent || "");
    const [isOCRProcessing, setIsOCRProcessing] = useState(!selectedPDF?.fileContent);
    const [isEditing, setIsEditing] = useState(false); // Track if the user wants to edit despite OCR processing

    // Update state when a new PDF is selected
    useEffect(() => {
        setEditedContent(selectedPDF?.fileContent || "");
        setIsOCRProcessing(!selectedPDF?.fileContent);
        setIsEditing(false); // Reset editing state when a new PDF is selected
    }, [selectedPDF]);

    const handleSave = () => {
        if (!selectedPDF) return;

        const updatedPDF = {
            ...selectedPDF,
            fileContent: editedContent,
        };

        updatePDF(updatedPDF)
            .then(() => {
                onUpdate();
            })
            .catch((error) => {
                console.error("Error updating PDF:", error);
            });
    };

    const handleDeleteClick = () => {
        if (selectedPDF) {
            handleDelete(selectedPDF.id);
        }
    };

    // Show the edit button if OCR is processing
    const handleEditAnyways = () => {
        setIsEditing(true);
    };

    if (!selectedPDF) {
        return <div style={{ flex: 1, padding: "20px" }}></div>; // Display nothing when no PDF is selected
    }

    return (
        <div style={{ flex: 1, padding: "20px", borderLeft: "1px solid #ddd" }}>
            <h3>PDF Content</h3>
            {isOCRProcessing && !isEditing ? (
                <>
                    <p>The File is empty at this point. OCR might still be processing. If this is the case, simply refresh the page.</p>
                    <button
                        className="btn btn-secondary"
                        onClick={handleEditAnyways}
                    >
                        Edit anyways
                    </button>
                </>
            ) : (
                <>
                    {/* Textarea and buttons are enabled if editing */}
                    <textarea
                        style={{ width: "100%", height: "300px", maxWidth: "1200px" }}
                        value={editedContent}
                        onChange={(e) => setEditedContent(e.target.value)}
                    />
                    <div style={{ marginTop: "10px" }}>
                        <button
                            className="btn btn-primary"
                            onClick={handleSave}
                            style={{ marginRight: "10px" }}
                        >
                            Save Changes
                        </button>
                        <button
                            className="btn btn-danger"
                            onClick={handleDeleteClick}
                        >
                            Delete
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default PDFEditorComponent;
