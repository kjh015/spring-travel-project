package com.traveler.sign.controller;


import com.traveler.sign.dto.SignInResultDto;
import com.traveler.sign.dto.SignUpResultDto;
import com.traveler.sign.service.InvalidTokenException;
import com.traveler.sign.service.SignService;
import com.traveler.sign.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sign-api")
public class SignController {
    private final SignService signService;
    private final TokenService tokenService;

    public SignController(SignService signService, TokenService tokenService) {
        this.signService = signService;
        this.tokenService = tokenService;
    }

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    @PostMapping("/sign-in")
    public SignInResultDto signIn(@RequestParam(required = true) String loginId, @RequestParam(required = true) String password) throws RuntimeException{
        logger.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", loginId);
        SignInResultDto signInResultDto = signService.signIn(loginId, password);

        if(signInResultDto.getCode() == 0){
            logger.info("[signIn] 정상적으로 로그인되었습니다. id : {}, accessToken : {}, refreshToken : {}", loginId, signInResultDto.getAccessToken(), signInResultDto.getRefreshToken());
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

    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken) {
        logger.info("[signOut] 로그아웃");
        String accessToken = bearerToken.replace("Bearer ", "");
        signService.signOut(accessToken);
        return ResponseEntity.ok("로그아웃 완료");
    }

    /*
     * 현재: 로그인 -> accessToken, refreshToken 생성 후 DB 저장 및 프론트에 전달.
     * 프론트에서 accessToken과 refreshToken을 받는다 -> accessToken이 유효하면 통과
     *                                            -> accessToken이 만료됐으면 -> 401 -> 프론트에서 refreshToken를 보냄 -> refreshToken이 유효 및 DB에 있으면 새로운 accessToken 발행하고 통과
     *                                                                                                           -> refreshToken도 만료 시 재로그인 요청
     * */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        logger.info("[refresh] accessToken 재발급 수행");
        String refreshToken = request.get("refreshToken");
        try {
            String newAccessToken = tokenService.reissueAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (InvalidTokenException e) {
            logger.info("[refresh] accessToken 재발급 실패");
            return ResponseEntity.status(401).body(e.getMessage());
        }
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
