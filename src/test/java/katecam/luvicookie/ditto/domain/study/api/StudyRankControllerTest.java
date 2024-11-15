package katecam.luvicookie.ditto.domain.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.luvicookie.ditto.ControllerTestConfig;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyRankControllerTest extends ControllerTestConfig {

    @Autowired
    public StudyRankControllerTest(WebApplicationContext context, ObjectMapper objectMapper) {
        super(context, objectMapper);
    }

    @Test
    @DisplayName("스터디 랭킹 조회 테스트")
    void getStudyRankings() throws Exception {
        Integer page = 0;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/rankings")
                        .param("page", String.valueOf(page))
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

}