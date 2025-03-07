package at.technikum.swkom.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DocumentManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentManagementSystemApplication.class, args);
	}

	@GetMapping
	public String hello(){
		return "hello World";
	}
}
