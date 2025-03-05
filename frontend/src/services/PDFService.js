import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8081/api/PDFentries";

export const listPDFs = () => {
    return axios.get(REST_API_BASE_URL);
}

export const deletePDF = (pdfId) => {
    return axios.delete(`${REST_API_BASE_URL}/${pdfId}`);
};

export const uploadPDF = (file) => {
    const date = new Date();

    const formData = new FormData();
    formData.append("pdfFile", file);
    formData.append("fileName", file.name);
    formData.append("fileSize", (file.size / 1024).toFixed(2) + " KB");
    formData.append("uploadDate", `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()}`);

    return axios.post("http://localhost:8081/api/PDFentries", formData)
        .then(response => response.data)
        .catch(error => {
            console.error("Upload error:", error.response ? error.response.data : error.message);
            throw error;
        });
};

export const updatePDF = (pdf) => {
    return axios.put(`${REST_API_BASE_URL}/${pdf.id}`, pdf);
};

export const searchPDFs = (searchText) => {
    return axios.get(`${REST_API_BASE_URL}/search`, {
        params: { query: searchText }
    })
        .then((response) => {
            return response.data;
        })
        .catch((error) => {
            console.error("Error searching PDFs:", error.response ? error.response.data : error.message);
            throw error;
        });
};
