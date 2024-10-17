package katecam.luvicookie.ditto.domain.member.service;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.memberDTO;
import katecam.luvicookie.ditto.domain.member.dto.memberRequestDTO;
import katecam.luvicookie.ditto.domain.member.dto.profileImageDTO;
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
    public Member updateMember(memberRequestDTO memberDTO, Integer userId){

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당사용자가없습니다"));
        member.authorizeUser();

        //if(memberDTO.getName() != null) member.setName(memberDTO.getName());
        //if(memberDTO.getEmail() != null) member.setEmail(memberDTO.getEmail());
        if(memberDTO.getDescription() != null) member.setDescription(memberDTO.getDescription());
        if(memberDTO.getContact() != null) member.setContact(memberDTO.getContact());
        if(memberDTO.getNickname() != null) member.setNickname(memberDTO.getNickname());

        return member;
    }

    @Transactional
    public Member updateProfileImage(profileImageDTO profileImageDTO, Integer userId){

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당사용자가없습니다"));
        member.authorizeUser();

        member.setProfileImage(profileImageDTO.getProfileImage());
        return member;
    }

}
