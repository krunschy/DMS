package at.technikum.swkom.dms.ElasticSearch.repository;

import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDFDocumentRepository extends ElasticsearchRepository<PDFDocument, String> {
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"fileName\", \"fileContent\"]}}")
    List<PDFDocument> searchByNameAndContent(String searchText);
}
