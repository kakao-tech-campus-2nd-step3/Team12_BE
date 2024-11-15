package katecam.luvicookie.ditto.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.luvicookie.ditto.ControllerTestWebMvcConfig;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberCreateRequest;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberUpdateRequest;
import katecam.luvicookie.ditto.fixture.MemberFixture;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestWebMvcConfig {

    @Autowired
    public MemberControllerTest(WebApplicationContext context, ObjectMapper objectMapper) {
        super(context, objectMapper);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("홍길동", "gilldong@kakao.com", "010-1234-5678", "길동이", "홍길동은 조선 연산군 때 충청도 일대를 중심으로 활동한 도적떼의 우두머리다.");

        mockMvc.perform(post("/api/auth")
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 정보 조회 테스트")
    void getUserInfo() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    void updateUserInfo() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        MemberUpdateRequest request = new MemberUpdateRequest("수정된 이름", "010-5678-1234", "수정된 닉네임", "수정된 설명");

        mockMvc.perform(put("/api/users")
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void updateProfileImage() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test-image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        mockMvc.perform(multipart(HttpMethod.PUT,"/api/users/profileImage")
                        .file(profileImage)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 스터디 목록 조회 테스트")
    void getUserStudyList() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/users/studies")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }
}
