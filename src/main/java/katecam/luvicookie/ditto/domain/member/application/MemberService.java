package katecam.luvicookie.ditto.domain.member.application;

import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberCreateRequest;
import katecam.luvicookie.ditto.domain.member.dto.response.MemberUpdateRequest;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AwsFileService awsFileService;

    @Transactional
    public Member registerMember(MemberCreateRequest memberDTO){
        log.info(String.valueOf(memberDTO.email()));
        log.info(memberDTO.email());

        Member member = memberRepository.findByEmail(memberDTO.email())
                .orElseThrow(() -> new IllegalArgumentException("해당사용자가없습니다"));
        member.authorizeUser();

        member.setDescription(memberDTO.description());
        member.setContact(memberDTO.contact());
        member.setNickname(memberDTO.nickname());

        return member;
    }

    @Transactional
    public Member updateMember(MemberUpdateRequest memberDTO, Member member){
        member.authorizeUser();

        if(memberDTO.name() != null) member.setName(memberDTO.name());
        if(memberDTO.description() != null) member.setDescription(memberDTO.description());
        if(memberDTO.contact() != null) member.setContact(memberDTO.contact());
        if(memberDTO.nickname() != null) member.setNickname(memberDTO.nickname());

        return member;
    }

    @Transactional
    public Member updateProfileImage(MultipartFile profileImage, Integer memberId){
        Member member = findMemberById(memberId);
        member.authorizeUser();

        try {
            String imageUrl = awsFileService.saveMemberProfileImage(profileImage);
            member.setProfileImage(imageUrl);
        } catch (IOException exception) {
            throw new GlobalException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return member;
    }

    public Member findMemberById(Integer memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
