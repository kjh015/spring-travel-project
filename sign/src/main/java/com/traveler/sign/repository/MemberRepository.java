package com.traveler.sign.repository;

import com.traveler.sign.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query("SELECT m.nickname FROM Member m WHERE m.loginId = :loginId")
    Optional<String> findNicknameByLoginId(String loginId);
    @Query("SELECT m.nickname FROM Member m WHERE m.id = :id")
    Optional<String> findNicknameById(Long id);
    @Query("SELECT m.id FROM Member m WHERE m.nickname = :nickname")
    Optional<Long> findIdByNickname(String nickname);
}
