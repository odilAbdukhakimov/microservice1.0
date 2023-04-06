package uz.pdp.b24productservice.common.handle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.b24productservice.common.exception.RecordNotFoundException;
import uz.pdp.b24productservice.common.response.ErrorResponse;
import uz.pdp.b24productservice.controller.dto.ProductResponse;

@RestControllerAdvice
public class CommonHandler {
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(
            RecordNotFoundException e
    ) {
        return new ErrorResponse(
                e.getMessage()
        );
    }
}
