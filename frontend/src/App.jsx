import React from 'react';
import './App.css';
import ListPDFComponent from './components/ListPDFComponent.jsx';
import UploadButton from './components/UploadButton.jsx';
import SearchBar from './components/SearchBar.jsx';

function App() {
    return (
        <>
            <div style={{textAlign: 'center', marginBottom: '20px'}}>
                <h2>List of PDFs</h2>
            </div>

            <div style={{marginBottom: '20px', display: "flex", width: "100%"}}>
                <ListPDFComponent/>
            </div>

            <div>
                <UploadButton/>
            </div>
        </>
    );
}

export default App;
