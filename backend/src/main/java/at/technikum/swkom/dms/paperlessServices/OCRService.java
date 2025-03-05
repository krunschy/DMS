package at.technikum.swkom.dms.paperlessServices;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.FileOutputStream;

@Service
public class OCRService {

    public static String performOCR(byte[] pdfData) {
        try {
            BufferedImage image = convertPDFToImage(pdfData);

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
            tesseract.setLanguage("eng");
            return tesseract.doOCR(image);
        } catch (Exception e) {
            throw new RuntimeException("OCR failed", e);
        }
    }

    private static BufferedImage convertPDFToImage(byte[] pdfData) throws IOException {
        File tempPdf = File.createTempFile("pdf", ".pdf");
        try (FileOutputStream out = new FileOutputStream(tempPdf)) {
            out.write(pdfData);
        }

        String outputImagePath = "/app/temp.png";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "gs", "-dNOPAUSE", "-dBATCH", "-sDEVICE=png16m",
                "-r500", //better dpi, so tesseract doesnt read gibberish
                "-sOutputFile=" + outputImagePath, tempPdf.getAbsolutePath()
        );

        processBuilder.inheritIO();

        Process process = processBuilder.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Ghostscript conversion failed with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            throw new IOException("Ghostscript conversion interrupted", e);
        }

        File outputImageFile = new File(outputImagePath);
        return ImageIO.read(outputImageFile);
    }

}
