package at.technikum.swkom.dms.repository;

import at.technikum.swkom.dms.entity.PDFentry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PDFentryRepository extends JpaRepository<PDFentry, Long> {
}
//this automatically gives us all the crud operation we will need