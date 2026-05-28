//package com.technokratos.bookingservice.controller;
//
//import com.technokratos.bookingservice.dto.dtos.UserDto;
//import com.technokratos.bookingservice.service.interfaces.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.NoHandlerFoundException;
//import org.springframework.web.servlet.resource.NoResourceFoundException;
//
//import java.security.Principal;
//
//@ControllerAdvice
//@RequiredArgsConstructor
//public class GlobalExceptionHandler {
//
//    private final UserService userService;
//
//    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleNotFoundError(Exception ex, Principal principal, Model model) {
//
//        if (principal != null) {
//            UserDto user = userService.getUserByEmail(principal.getName());
//            model.addAttribute("userRole", user.getRole());
//            model.addAttribute("userName", user.getName());
//        }
//        return "error/404";
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public String handleAccessDeniedException(AccessDeniedException ex, Model model, Principal principal) {
//
//        if (principal != null) {
//            UserDto user = userService.getUserByEmail(principal.getName());
//            model.addAttribute("userRole", user.getRole());
//            model.addAttribute("userName", user.getName());
//        }
//
//        return "error/403";
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleAllExceptions(Exception ex, Model model, Principal principal) {
//
//        if (principal != null) {
//            UserDto user = userService.getUserByEmail(principal.getName());
//            model.addAttribute("userRole", user.getRole());
//            model.addAttribute("userName", user.getName());
//        }
//
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error/500";
//    }
//
//}