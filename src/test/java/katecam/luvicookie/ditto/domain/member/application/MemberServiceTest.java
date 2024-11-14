package katecam.luvicookie.ditto.domain.member.application;

import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.domain.Role;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberUpdateRequest;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AwsFileService awsFileService;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("사용자 조회 성공")
    void findMemberById() {
        Member member = Member.builder()
                .name("홍길동")
                .email("gilldongHong@kakao.com")
                .contact("010-1234-1234")
                .description("홍길동은 조선 연산군 때 충청도 일대를 중심으로 활동한 도적떼의 우두머리다. 조선왕조실록과 몇몇 문헌에 그의 행적에 대해 간략히 적혀 있다.")
                .nickname("길동이")
                .role(Role.USER)
                .build();

        Integer memberId = 1;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        Member memberResponse = memberService.findMemberById(memberId);

        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getName()).isEqualTo(member.getName());

    }

    @Test
    @DisplayName("회원가입 성공")
    void registerMemberSuccess() {
        /*Member member = Member.builder()
                .name("홍길동")
                .email("gilldongHong@kakao.com")
                .role(Role.GUEST)
                .build();

        memberRepository.save(member);
        given(memberRepository.findByEmail("gilldongHong@kakao.com"))
                .willReturn(Optional.of(member));

        MemberCreateRequest request = new MemberCreateRequest("홍길동", "gilldongHong@kakao.com", "010-1234-1234", "길동이", "홍길동은 조선 연산군 때 충청도 일대를 중심으로 활동한 도적떼의 우두머리다. 조선왕조실록과 몇몇 문헌에 그의 행적에 대해 간략히 적혀 있다.");

        given(memberService.registerMember(request))
                .willReturn(imageUrl);

        assertDoesNotThrow(() -> memberService.registerMember(request));
        verify(awsFileService).saveStudyProfileImage(null);
        verify(studyRepository).save(any());
        verify(studyMemberService).createStudyMember(any(), eq(member), eq(StudyMemberRole.LEADER), eq(""));

        */
    }

    @Test
    @DisplayName("회원가입 실패")
    void registerMemberFail() {
    }

    @Test
    @DisplayName("사용자 정보 수정 성공")
    void updateMemberSuccess() {
        Member member = Member.builder()
                .name("홍길동")
                .email("gilldongHong@kakao.com")
                .contact("010-1234-1234")
                .description("홍길동은 조선 연산군 때 충청도 일대를 중심으로 활동한 도적떼의 우두머리다. 조선왕조실록과 몇몇 문헌에 그의 행적에 대해 간략히 적혀 있다.")
                .nickname("길동이")
                .role(Role.USER)
                .build();

        MemberUpdateRequest request = new MemberUpdateRequest("일론 머스크", "010-1971-0628", "테크노킹", "일론 리브 머스크(영어: Elon Reeve Musk, 1971년 6월 28일~)는 남아프리카 공화국 출신 미국의 기업인이다. 페이팔의 전신이 된 온라인 결제 서비스 회사 X.com, 민간 우주기업 스페이스X를 창립했고, 전기자동차 기업 테슬라의 회장이기도 하다.");

        assertDoesNotThrow(() -> memberService.updateMember(request, member));

        assertEquals("일론 머스크", member.getName());
        assertEquals("010-1971-0628", member.getContact());
        assertEquals("테크노킹", member.getNickname());
        assertEquals("일론 리브 머스크(영어: Elon Reeve Musk, 1971년 6월 28일~)는 남아프리카 공화국 출신 미국의 기업인이다. 페이팔의 전신이 된 온라인 결제 서비스 회사 X.com, 민간 우주기업 스페이스X를 창립했고, 전기자동차 기업 테슬라의 회장이기도 하다.", member.getDescription());

    }

    /*@Test
    @DisplayName("사용자 정보 수정 실패")
    void updateMemberFail() {
        Member member = Member.builder()
                .name("홍길동")
                .email("gilldongHong@kakao.com")
                .contact("010-1234-1234")
                .description("홍길동은 조선 연산군 때 충청도 일대를 중심으로 활동한 도적떼의 우두머리다. 조선왕조실록과 몇몇 문헌에 그의 행적에 대해 간략히 적혀 있다.")
                .nickname("길동이")
                .role(Role.USER)
                .build();

        MemberUpdateRequest request = new MemberUpdateRequest("일론 머스크", "010-1971-0628", "테크노킹", "일론 리브 머스크(영어: Elon Reeve Musk, 1971년 6월 28일~)는 남아프리카 공화국 출신 미국의 기업인이다. 페이팔의 전신이 된 온라인 결제 서비스 회사 X.com, 민간 우주기업 스페이스X를 창립했고, 전기자동차 기업 테슬라의 회장이기도 하다.");


    }*/

    @Test
    @DisplayName("프로필 이미지 수정 성공")
    void updateProfileImageSuccess() throws IOException {
        Member member = Member.builder()
                .name("홍길동")
                .role(Role.USER)
                .build();

        Integer memberId = 1;

        String imageUrl = "https://test_domain.com/image.jpg";

        MultipartFile image = new MockMultipartFile(
                "test",
                "spring.png",
                "image/png",
                new FileInputStream(new File(imageUrl))
        );

        given(awsFileService.saveMemberProfileImage(any()))
                .willReturn(imageUrl);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.ofNullable(member));

        given(memberService.updateProfileImage(image, memberId))
                .willReturn(member);


        assertDoesNotThrow(() -> memberService.updateProfileImage(null, memberId));
        verify(awsFileService).saveMemberProfileImage(null);
        verify(memberService).updateProfileImage(null, memberId);

    }

    @Test
    @DisplayName("프로필 이미지 수정 실패")
    void updateProfileImageFail() {
    }


}
