package at.technikum.swkom.dms.paperlessREST.repository;

import at.technikum.swkom.dms.paperlessREST.entity.PDFentry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PDFentryRepository extends JpaRepository<PDFentry, Long> {
    Optional<PDFentry> findByFileURL(String fileURL);
}
//this automatically gives us all the crud operation we will need