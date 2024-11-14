package katecam.luvicookie.ditto.domain.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.luvicookie.ditto.ControllerTestConfig;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyUpdateRequest;
import katecam.luvicookie.ditto.fixture.MemberFixture;
import katecam.luvicookie.ditto.global.util.file.FileTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyControllerTest extends ControllerTestConfig {

    @Autowired
    public StudyControllerTest(WebApplicationContext context, ObjectMapper objectMapper) {
        super(context, objectMapper);
    }

    @Test
    @DisplayName("스터디 목록 조회 테스트")
    void getStudyList() throws Exception {
        Integer page = 0;
        StudyCriteria studyCriteria = StudyCriteria.builder()
                .name("테스트")
                .isOpen(true)
                .build();

        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/studies")
                        .param("page", String.valueOf(page))
                        .param("name", studyCriteria.name())
                        .param("is_open", Boolean.toString(studyCriteria.isOpen()))
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("스터디 정보 조회 테스트")
    void getStudy() throws Exception {
        Integer studyId = 1;

        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/studies/{id}", studyId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("스터디 생성 테스트")
    void createStudy() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        StudyCreateRequest request = new StudyCreateRequest("테스트 스터디", "테스트 스터디입니다.", true, "테스트 주제");
        MockPart mockPart = new MockPart("request", toJsonString(request).getBytes(StandardCharsets.UTF_8));
        mockPart.getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        MockMultipartFile profileImage = FileTestUtil.getTestImageFile();

        mockMvc.perform(multipart(HttpMethod.POST, "/api/studies")
                        .file(profileImage)
                        .part(mockPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("스터디 삭제 테스트")
    void deleteStudy() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;

        mockMvc.perform(delete("/api/studies/{studyId}", studyId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("스터디 정보 수정 테스트")
    void updateStudy() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;

        StudyUpdateRequest request = new StudyUpdateRequest("NEW STUDY", "NEW NEW NEW", false, "NEW");

        mockMvc.perform(put("/api/studies/{studyId}", studyId)
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("스터디 프로필 이미지 수정 테스트")
    void updateStudyProfileImage() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;

        MockMultipartFile profileImage = FileTestUtil.getTestImageFile();

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/studies/{studyId}/profileImage", studyId)
                        .file(profileImage)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

}
