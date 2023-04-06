package uz.pdp.inventoryservice.common.handle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.inventoryservice.common.exception.AlreadyExistException;
import uz.pdp.inventoryservice.common.exception.ProductAmountException;
import uz.pdp.inventoryservice.common.exception.ProductNotFoundException;
import uz.pdp.inventoryservice.common.exception.RecordNotFoundException;
import uz.pdp.inventoryservice.common.response.ErrorResponse;

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
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse notFound(
            AlreadyExistException e
    ) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Boolean productNotFound(
            ProductNotFoundException e
    ) {
        return false;
    }

    @ExceptionHandler(ProductAmountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Boolean productAmountNotFound(
            ProductAmountException e
    ) {
        return false;
    }
}
