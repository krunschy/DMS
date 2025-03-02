package at.technikum.swkom.dms.paperlessREST.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResurceNotFoundException extends RuntimeException{

    public ResurceNotFoundException(String message){
        super(message);
    }
}
