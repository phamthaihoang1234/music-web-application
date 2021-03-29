package com.codegym.music.exception;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SongNotFoundAdvice {

  Logger logger = LoggerFactory.getLogger(SongNotFoundAdvice.class);

  @ResponseBody
  @ExceptionHandler(SongNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView employeeNotFoundHandler(HttpServletRequest req, SongNotFoundException ex) {
    logger.error("AnhNBT Error Request: " + req.getRequestURL() + " raised " + ex);
    ModelAndView mav = new ModelAndView("errors/error-404");
    mav.addObject("exception", ex);
    mav.addObject("url", req.getRequestURL());
    return mav;
  }
}
