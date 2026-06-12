package com.traveler.member.domain.member.repository;

import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.enums.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.roles WHERE m.id = :id")
    Optional<Member> findByIdWithRoles(Long id);

    @Query("select m from Member m join fetch m.roles where m.loginId = :loginId")
    Optional<Member> findByLoginIdWithRoles(String loginId);

    Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);
}
