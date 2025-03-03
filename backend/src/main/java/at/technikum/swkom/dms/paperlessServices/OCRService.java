package at.technikum.swkom.dms.paperlessServices;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.io.FileOutputStream;

@Service
public class OCRService {

    public static String performOCR(byte[] pdfData) {
        try {
            // Step 1: Convert PDF to an image using Ghostscript or PDFBox
            BufferedImage image = convertPDFToImage(pdfData);

            // Step 2: Perform OCR using Tesseract
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata"); // Path in Docker
            tesseract.setLanguage("eng");
            return tesseract.doOCR(image);
        } catch (Exception e) {
            throw new RuntimeException("OCR failed", e);
        }
    }

    private static BufferedImage convertPDFToImage(byte[] pdfData) throws IOException {
        // Create a temporary file for the PDF data
        File tempPdf = File.createTempFile("pdf", ".pdf");
        try (FileOutputStream out = new FileOutputStream(tempPdf)) {
            out.write(pdfData);
        }

        // Step 1: Use Ghostscript to convert PDF to image
        String outputImagePath = "/app/temp.png";  // Specify a full path for the image output

        // Use ProcessBuilder to execute the Ghostscript command
        ProcessBuilder processBuilder = new ProcessBuilder(
                "gs", "-dNOPAUSE", "-dBATCH", "-sDEVICE=png16m", // Use png16m for better image quality
                "-r500", // Increase DPI to 300 (adjust as needed)
                "-sOutputFile=" + outputImagePath, tempPdf.getAbsolutePath()
        );

        processBuilder.inheritIO();  // Optional: Use to see Ghostscript output in your console

        Process process = processBuilder.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Ghostscript conversion failed with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            throw new IOException("Ghostscript conversion interrupted", e);
        }

        // Step 2: Read the converted image
        File outputImageFile = new File(outputImagePath);
        return ImageIO.read(outputImageFile);
    }

}
