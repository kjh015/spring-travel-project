package com.traveler.bff.client;

import com.traveler.bff.dto.service.PasswordDto;
import com.traveler.bff.dto.service.SignDto;
import com.traveler.bff.dto.service.SignInRequestDto;
import com.traveler.bff.dto.service.SignInResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "sign")
public interface SignServiceClient {
    @PostMapping("/sign/sign-in")
    ResponseEntity<SignInResultDto> signIn(@RequestBody SignInRequestDto signIn);

    @PostMapping("/sign/sign-up")
    ResponseEntity<String> signUp(@RequestBody SignDto signUp);

    @GetMapping("/sign/check-duplicate")
    ResponseEntity<?> checkDuplicate(@RequestParam("type") String type, @RequestParam("value") String value);

    @PostMapping("/sign/update")
    ResponseEntity<String> updateMember(@RequestBody SignDto data);

    @PostMapping("/sign/update-password")
    ResponseEntity<String> updatePassword(@RequestBody PasswordDto data);

    @PostMapping(value="/sign/sign-out", consumes = "application/json")
    ResponseEntity<String> logout(@RequestHeader("Cookie") String cookieHeader);

    @PostMapping("/sign/withdraw")
    ResponseEntity<String> withdraw(@RequestHeader("Cookie") String cookieHeader);

    @PostMapping("/sign/refresh")
    Map<String, String> refreshToken(@RequestHeader("Cookie") String cookieHeader);

    @PostMapping("/sign/detail")
    ResponseEntity<?> getMemberDetail(@RequestParam String loginId);

    @PostMapping("/sign/list")
    ResponseEntity<?> getMemberList();

    @PostMapping("/sign/delegate")
    ResponseEntity<?> delegateAdmin(@RequestParam String loginId);
    @PostMapping("/sign/test")
    ResponseEntity<String> testAccessToken();

    @PostMapping("/sign/nickname")
    String getNicknameByLoginId(@RequestBody Map<String, String> data);

    @PostMapping("/sign/nickname-id")
    String getNicknameById(@RequestBody Long id);
    @PostMapping("/sign/id-nickname")
    Long getIdByNickname(@RequestBody String nickname);

    @PostMapping("/sign/nickname-list")
    Map<Long, String> getNicknameList(@RequestBody List<Long> IDs);
}
