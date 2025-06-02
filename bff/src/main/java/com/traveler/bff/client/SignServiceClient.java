package com.traveler.bff.client;

import com.traveler.bff.dto.service.SignDto;
import com.traveler.bff.dto.service.SignInRequestDto;
import com.traveler.bff.dto.service.SignInResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "sign")
public interface SignServiceClient {
    @PostMapping("/sign/sign-in")
    ResponseEntity<SignInResultDto> signIn(@RequestBody SignInRequestDto signIn);

    @PostMapping("/sign/sign-up")
    ResponseEntity<String> signUp(@RequestBody SignDto signUp);

    @PostMapping("/sign/update")
    ResponseEntity<String> updateMember(@RequestBody SignDto data);

    @PostMapping(value="/sign/sign-out", consumes = "application/json")
    ResponseEntity<String> logout(@RequestHeader("Cookie") String cookieHeader);

    @PostMapping("/sign/withdraw")
    ResponseEntity<String> withdraw(@RequestHeader("Cookie") String cookieHeader);

    @PostMapping("/sign/refresh")
    Map<String, String> refreshToken(@RequestHeader("Cookie") String cookieHeader);

    @PostMapping("/sign/test")
    ResponseEntity<String> testAccessToken();

    @PostMapping("/sign/nickname")
    String getNicknameByLoginId(@RequestBody Map<String, String> data);

    @PostMapping("/sign/nickname-id")
    String getNicknameById(@RequestBody Long id);
    @PostMapping("/sign/id-nickname")
    Long getIdByNickname(@RequestBody String nickname);

    @GetMapping("/sign/nickname-list")
    Map<Long, String> getNicknameList(@RequestBody Set<Long> IDs);
}
