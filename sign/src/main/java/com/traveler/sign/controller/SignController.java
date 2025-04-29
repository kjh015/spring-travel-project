package com.traveler.sign.controller;


import com.traveler.sign.dto.SignInResultDto;
import com.traveler.sign.dto.SignUpResultDto;
import com.traveler.sign.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sign-api")
public class SignController {
    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    @PostMapping("/sign-in")
    public SignInResultDto signIn(@RequestParam(required = true) String loginId, @RequestParam(required = true) String password) throws RuntimeException{
        logger.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", loginId);
        SignInResultDto signInResultDto = signService.signIn(loginId, password);


        if(signInResultDto.getCode() == 0){
            logger.info("[signIn] 정상적으로 로그인되었습니다. id : {}, token : {}", loginId, signInResultDto.getToken());
        }
        return signInResultDto;
    }

    @PostMapping("/sign-up")
    public SignUpResultDto signUp(@RequestParam(required = true) String loginId, @RequestParam(required = true) String password,
                                  @RequestParam(required = true) String email, @RequestParam(required = true) String nickname,
                                  @RequestParam(required = true) String gender, @RequestParam(required = true) String role){
        logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, email : {}, role : {}", loginId, email, role);
        SignUpResultDto signUpResultDto = signService.signUp(loginId, password, email, nickname, gender, role);
        logger.info("[signUp] 회원가입을 완료했습니다. id : {}", loginId);
        return signUpResultDto;
    }

    @GetMapping("/exception")
    public void exceptionTest() throws RuntimeException{
        throw new RuntimeException("접근이 금지되었습니다.");
    }

    @ExceptionHandler(value=RuntimeException.class)
    public Map<String, String> ExceptionHandler(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        logger.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return map;
    }



}
