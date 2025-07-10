package ism.atelier.atelier.web.dto;


import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestResponseWeb {
    public static Map<String, Object> response(
            HttpStatus status,
            Object data,
            String type
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("content", data);
        response.put("type", type);
        return response;
    }

    public static <T> Map<String, Object> response2(
            HttpStatus status,
            List<T> data,
            String type
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("content", data);
        response.put("type", type);
        return response;
    }

    public static Map<String, Object> responseError(
            BindingResult bindingResult
    ){
        Map<String, Object> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }

    public static Map<String, Object> responsePaginate(
            HttpStatus status,
            Object data,
            Integer currentPage,
            Integer totalPages,
            Boolean isFirst,
            Boolean isLast,
            String type
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("content", data);
        response.put("currentPage", currentPage);
        response.put("pages", new int[]{totalPages});
        response.put("totalPages", totalPages);
        response.put("isFirst", isFirst);
        response.put("isLast", isLast);
        response.put("type", type);
        return response;
    }
}
