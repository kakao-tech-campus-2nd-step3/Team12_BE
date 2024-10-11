package katecam.luvicookie.ditto.domain.member.service;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.memberDTO;
import katecam.luvicookie.ditto.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateMember(memberDTO memberDTO, Long memberId){
        log.info(String.valueOf(memberId));
        log.info(memberDTO.getEmail());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당사용자가없습니다"));
        member.authorizeUser();
        member.setEmail(memberDTO.getEmail());
    }


}
