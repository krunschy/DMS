import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8081/api/PDFentries";

export const listPDFs = () => {
    return axios.get(REST_API_BASE_URL);
}

// Function to delete a PDF by ID
export const deletePDF = (pdfId) => {
    return axios.delete(`${REST_API_BASE_URL}/${pdfId}`);
};

export const uploadMetadata = (file) => {
    const date = new Date();
    const formattedDate = `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()}`;

    const metadata = {
        fileName: file.name,
        uploadDate: formattedDate,   // upload date as a string in dd.mm.yyyy format
        fileSize: file.size.toString(),  // file size as a string
    };

    return axios.post(REST_API_BASE_URL, metadata)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            throw error;
        });
};