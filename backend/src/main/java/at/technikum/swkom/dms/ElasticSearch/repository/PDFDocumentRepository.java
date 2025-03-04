package at.technikum.swkom.dms.ElasticSearch.repository;

import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDFDocumentRepository extends ElasticsearchRepository<PDFDocument, String> {
}
