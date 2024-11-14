package katecam.luvicookie.ditto.domain.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.luvicookie.ditto.ControllerTestConfig;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void createStudy() {

    }

    @Test
    @DisplayName("스터디 삭제 테스트")
    void deleteStudy() {
    }

    @Test
    @DisplayName("스터디 정보 수정 테스트")
    void updateStudy() {
    }

    @Test
    @DisplayName("스터디 프로필 이미지 수정 테스트")
    void updateStudyProfileImage() {
    }

}