package at.technikum.swkom.dms.paperlessREST.exception;

import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }
}
