package com.example.example5test.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {
    public String handlerException(Exception exception){
      log.error("Failed to return response", exception);
      return "error";
    }
}
