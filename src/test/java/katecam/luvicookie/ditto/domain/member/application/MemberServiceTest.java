package katecam.luvicookie.ditto.domain.member.application;

import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberCreateRequest;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberUpdateRequest;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.willThrow;


@ExtendWith(MockitoExtension.class)
@DisplayName("멤버 서비스 테스트")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AwsFileService awsFileService;

    @InjectMocks
    private MemberService memberService;

    @Nested
    @DisplayName("멤버 등록")
    class RegisterMember {

        @Test
        @DisplayName("멤버 등록 성공")
        void registerMemberSuccess() {
            MemberCreateRequest request =
                    new MemberCreateRequest("홍길동", "email@example.com", "010-1111-2222", "닉네임", "자기소개");

            Member member = Member.builder()
                    .email("email@example.com")
                    .build();

            given(memberRepository.findByEmail(request.email()))
                    .willReturn(Optional.of(member));

            Member registeredMember = memberService.registerMember(request);

            assertThat(registeredMember).isNotNull();
            assertThat(registeredMember.getDescription()).isEqualTo(request.description());
            assertThat(registeredMember.getContact()).isEqualTo(request.contact());
            assertThat(registeredMember.getNickname()).isEqualTo(request.nickname());
        }

        @Test
        @DisplayName("멤버 등록 실패 - 사용자 없음")
        void registerMemberFail_UserNotFound() {
            MemberCreateRequest request =
                    new MemberCreateRequest("홍길동", "email@example.com", "010-1111-2222", "닉네임", "자기소개");

            given(memberRepository.findByEmail(request.email()))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> memberService.registerMember(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당사용자가없습니다");
        }
    }

    @Nested
    @DisplayName("멤버 정보 수정")
    class UpdateMember {

        @Test
        @DisplayName("멤버 정보 수정 성공")
        void updateMemberSuccess() {
            Member member = Member.builder()
                    .name("기존 이름")
                    .build();

            MemberUpdateRequest request = new MemberUpdateRequest("새 이름", "새 설명", "새 연락처", "새 닉네임");

            Member updatedMember = memberService.updateMember(request, member);

            assertThat(updatedMember.getName()).isEqualTo(request.name());
            assertThat(updatedMember.getDescription()).isEqualTo(request.description());
            assertThat(updatedMember.getContact()).isEqualTo(request.contact());
            assertThat(updatedMember.getNickname()).isEqualTo(request.nickname());
        }
    }

    @Nested
    @DisplayName("프로필 이미지 수정")
    class UpdateProfileImage {

        @Test
        @DisplayName("프로필 이미지 수정 성공")
        void updateProfileImageSuccess() throws IOException {
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();
            Integer memberId = 1;
            MultipartFile profileImage = any(); // 이미지 파일 모킹

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(awsFileService.saveMemberProfileImage(profileImage)).willReturn("http://test.com/profile-image.jpg");

            Member result = memberService.updateProfileImage(profileImage, memberId);

            assertThat(result.getProfileImage()).isEqualTo("http://test.com/profile-image.jpg");
            verify(awsFileService).saveMemberProfileImage(profileImage);
        }

        @Test
        @DisplayName("프로필 이미지 수정 실패 - 파일 업로드 오류")
        void updateProfileImageFail_FileUploadError() throws IOException {
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();
            Integer memberId = 1;
            MultipartFile profileImage = any(); // 이미지 파일 모킹

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            willThrow(IOException.class).given(awsFileService).saveMemberProfileImage(profileImage);

            assertThatThrownBy(() -> memberService.updateProfileImage(profileImage, memberId))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.FILE_UPLOAD_FAILED.getMessage());
        }
    }

    @Nested
    @DisplayName("멤버 조회")
    class FindMember {

        @Test
        @DisplayName("멤버 조회 성공")
        void findMemberByIdSuccess() {
            Integer memberId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            Member foundMember = memberService.findMemberById(memberId);

            assertThat(foundMember).isNotNull();
            assertThat(foundMember.getName()).isEqualTo(member.getName());
        }

        @Test
        @DisplayName("멤버 조회 실패 - 사용자 없음")
        void findMemberByIdFail_UserNotFound() {
            Integer memberId = 1;

            given(memberRepository.findById(memberId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> memberService.findMemberById(memberId))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    }
}
