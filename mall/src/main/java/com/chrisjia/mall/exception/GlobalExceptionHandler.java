package com.chrisjia.mall.exception;

import com.chrisjia.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiRestResponse internalExceptionHandler(Exception ex){
        logger.error("Internal Exception: ", ex);
        return ApiRestResponse.fail(ExceptionEnum.INTERNAL_EXCEPTION);
    }

    @ExceptionHandler(ExceptionClass.class)
    @ResponseBody
    public ApiRestResponse mallExceptionHandler(ExceptionClass ex){
        logger.error("mall Exception: ", ex);
        return ApiRestResponse.fail(ex.getCode(), ex.getMsg());
    }

    // for form data input Exception
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ApiRestResponse methodArgumentExceptionHandler(BindException ex){
        logger.error("Form argument not valid Exception: ", ex);
        BindingResult result = ex.getBindingResult();
        return bindingResultHandler(result);
    }
    // for JSON data input Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse methodArgumentExceptionHandler(MethodArgumentNotValidException ex){
        logger.error("JSON argument not valid Exception: ", ex);
        BindingResult result = ex.getBindingResult();
        return bindingResultHandler(result);
    }

    private ApiRestResponse bindingResultHandler(BindingResult result){
        List<String> results = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> errorList = result.getAllErrors();
            for (ObjectError error:
                 errorList) {
                String errorMessage = error.getDefaultMessage();
                results.add(errorMessage);
            }
            return ApiRestResponse.fail(ExceptionEnum.CATEGORY_ARGUMENT_NOT_VALID_EXCEPTION.getCode(),
                    results.toString());
        }
        return ApiRestResponse.fail(ExceptionEnum.CATEGORY_ARGUMENT_NOT_VALID_EXCEPTION);
    }
}
