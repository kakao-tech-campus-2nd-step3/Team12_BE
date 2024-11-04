package katecam.luvicookie.ditto.domain.member.application;

import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.memberRequestDTO;
import katecam.luvicookie.ditto.domain.member.dto.profileImageDTO;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
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
    public Member registerMember(memberRequestDTO memberDTO){
        log.info(String.valueOf(memberDTO.getEmail()));
        log.info(memberDTO.getEmail());

        Member member = memberRepository.findByEmail(memberDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당사용자가없습니다"));
        member.authorizeUser();

        member.setDescription(memberDTO.getDescription());
        member.setContact(memberDTO.getContact());
        member.setNickname(memberDTO.getNickname());

        return member;
    }

    @Transactional
    public Member updateMember(memberRequestDTO memberDTO, Integer memberId){

        Member member = findMemberById(memberId);
        member.authorizeUser();

        if(memberDTO.getName() != null) member.setName(memberDTO.getName());
        if(memberDTO.getDescription() != null) member.setDescription(memberDTO.getDescription());
        if(memberDTO.getContact() != null) member.setContact(memberDTO.getContact());
        if(memberDTO.getNickname() != null) member.setNickname(memberDTO.getNickname());

        return member;
    }

    @Transactional
    public Member updateProfileImage(profileImageDTO profileImageDTO, Integer memberId){

        Member member = findMemberById(memberId);
        member.authorizeUser();

        member.setProfileImage(profileImageDTO.getProfileImage());
        return member;
    }

    public Member findMemberById(Integer memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
