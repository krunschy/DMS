https://www.youtube.com/watch?v=KuM6OtuaYRs&list=PLGRDMO4rOGcNLnW1L2vgsExTBg-VPoZHr&ab_channel=JavaGuides

Dem bin ich vor allem gefolgt für sprint 3. Also die routes sind

(http://localhost:8081)
/api/PDFentries
  GET: holt alle
  POST: neuer Eintrag, braucht json im body
/api/PDFentries/{Id von Eintrag}
  GET: holt den Eintrag
  PUT: updated den Eintrag, braucht json im body
  DELETE: löscht den Eintrag


check die datenbank mit
docker exec -it dms-db psql -U user -d dmsdb
select * from pdfs;


"npm run dev" um das frontent zu entwickeln
"npm run build" before docker compose

./mvnw clean package -Papi -DskipTests
./mvnw clean package -Pocr -DskipTests

   um das backend zu baun, skiptests is wichtig, weils sonst keine db hat, und das baut den ocr worker und api service separat


next chatgpgt query
Now it'S time to tackle the frontend. Right now I confirmed with post man that get http://localhost:8081/api/PDFentries does indeed correctly deliver the file content, so it's but a matter of displaying it.
right now my site, built with react looks like this:
import React from 'react';
import './App.css';
import ListPDFComponent from './components/ListPDFComponent.jsx';
import UploadButton from './components/UploadButton.jsx';
import SearchBar from './components/SearchBar.jsx';

function App() {
return (
<>
<div style={{ textAlign: 'center', marginBottom: '20px' }}>
<h2>List of PDFs</h2>
</div>

            <div style={{ marginBottom: '20px' }}>
                <SearchBar />
            </div>

            <div style={{ marginBottom: '20px' }}>
                <ListPDFComponent />
            </div>

            <div>
                <UploadButton />
            </div>
        </>
    );
}

export default App;

my relevant component here is the list

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

I want to build a new component, which is like a text field that sits to the right of the list. Upon clicking on an entry within the list I want the textfield to display the pdf.fileContent. This should be editable and thereshould be a button with "Save Change" to update the database:
