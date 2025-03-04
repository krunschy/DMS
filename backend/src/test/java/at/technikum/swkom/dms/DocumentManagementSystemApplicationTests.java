package at.technikum.swkom.dms;

import at.technikum.swkom.dms.ElasticSearch.repository.PDFDocumentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;

@SpringBootTest(properties = {
		"spring.data.elasticsearch.repositories.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(DocumentManagementSystemApplicationTests.TestConfig.class)
class DocumentManagementSystemApplicationTests {

	@TestConfiguration
	static class TestConfig {
		@Bean
		public PDFDocumentRepository pdfDocumentRepository() {
			// Return a Mockito mock instance as a dummy bean.
			return Mockito.mock(PDFDocumentRepository.class);
		}
	}

	@Test
	void contextLoads() {
		// Your test to simply load the context
	}
}
