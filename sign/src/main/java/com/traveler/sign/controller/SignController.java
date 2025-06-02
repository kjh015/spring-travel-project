package com.traveler.sign.controller;


import com.traveler.sign.dto.SignDto;
import com.traveler.sign.dto.SignInRequestDto;
import com.traveler.sign.dto.SignInResultDto;
import com.traveler.sign.service.CustomSignException;
import com.traveler.sign.service.InvalidTokenException;
import com.traveler.sign.service.SignService;
import com.traveler.sign.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/sign")
public class SignController {
    private final SignService signService;
    private final TokenService tokenService;

    public SignController(SignService signService, TokenService tokenService) {
        this.signService = signService;
        this.tokenService = tokenService;
    }

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    @PostMapping("/sign-in")
    public SignInResultDto signIn(@RequestBody SignInRequestDto signIn, HttpServletResponse response) throws RuntimeException{
        logger.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", signIn.getLoginId());
        SignInResultDto signInResultDto = signService.signIn(signIn.getLoginId(), signIn.getPassword());

        // RefreshToken을 HttpOnly 쿠키로 설정
        ResponseCookie cookie = ResponseCookie.from("refreshToken", signInResultDto.getRefreshToken())
                .httpOnly(true)
                .secure(false) // HTTPS만 허용할 경우 true
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return signInResultDto;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignDto signUp){
        logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, email : {}, role : {}", signUp.getLoginId(), signUp.getEmail(), signUp.getRole());
        signService.signUp(signUp.getLoginId(), signUp.getPassword(), signUp.getEmail(), signUp.getNickname(), signUp.getGender(), signUp.getRole());
        logger.info("[signUp] 회원가입을 완료했습니다. id : {}", signUp.getLoginId());
        return ResponseEntity.ok("회원가입 완료");
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateMember(@RequestBody SignDto data){
        signService.updateMember(data);
        return ResponseEntity.ok("회원수정 완료");
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        logger.info("[signOut] 로그아웃 시작");

        String refreshToken = tokenService.getCookieValue(request, "refreshToken");
        logger.info("[signOut] refreshToken: {}",  refreshToken);
        signService.signOut(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        logger.info("[signOut] 로그아웃 완료");
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = tokenService.getCookieValue(request, "refreshToken");
        signService.withdraw(refreshToken);
        
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok("회원탈퇴 완료");
    }



    /*
     * 현재: 로그인 -> accessToken, refreshToken 생성 후 DB 저장 및 프론트에 전달.
     * 프론트에서 accessToken과 refreshToken을 받는다 -> accessToken이 유효하면 통과
     *                                            -> accessToken이 만료됐으면 -> 401 -> 프론트에서 refreshToken를 보냄 -> refreshToken이 유효 및 DB에 있으면 새로운 accessToken 발행하고 통과
     *                                                                                                           -> refreshToken도 만료 시 재로그인 요청
     * */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        logger.info("[refresh] accessToken 재발급 수행");
        String refreshToken = tokenService.getCookieValue(request, "refreshToken");
        try {
            String newAccessToken = tokenService.reissueAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (InvalidTokenException e) {
            logger.info("[refresh] accessToken 재발급 실패");
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/test")
    public String testAccessToken(){
        logger.info("[tokenTest]: test success");
        return "test success";
    }

    @PostMapping("/nickname")
    public ResponseEntity<?> getNicknameByLoginId(@RequestBody Map<String, String> data){
        return ResponseEntity.ok(signService.getNicknameByLoginId(data.get("loginId")));
    }

    @PostMapping("/nickname-id")
    public ResponseEntity<?> getNicknameById(@RequestBody Long id){
        return ResponseEntity.ok(signService.getNicknameById(id));
    }
    @PostMapping("/id-nickname")
    public ResponseEntity<?> getIdByNickname(@RequestBody String nickname){
        return ResponseEntity.ok(signService.getIdByNickname(nickname));
    }

    @PostMapping("/nickname-list")
    public ResponseEntity<?> getNicknameList(@RequestBody Set<Long> IDs){
        return ResponseEntity.ok(signService.getNicknameList(IDs));
    }





    @ExceptionHandler(CustomSignException.class)
    public ResponseEntity<String> ExceptionHandler(CustomSignException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }





}
