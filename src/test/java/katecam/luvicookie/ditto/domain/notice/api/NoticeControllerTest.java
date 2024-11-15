package katecam.luvicookie.ditto.domain.notice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.luvicookie.ditto.ControllerTestWebMvcConfig;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeCreateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeUpdateRequest;
import katecam.luvicookie.ditto.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NoticeControllerTest extends ControllerTestWebMvcConfig {

    @Autowired
    public NoticeControllerTest(WebApplicationContext context, ObjectMapper objectMapper) {
        super(context, objectMapper);
    }

    @Test
    @DisplayName("공지 생성 테스트")
    void createNotice() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        Integer studyId = 1;
        NoticeCreateRequest request = new NoticeCreateRequest("테스트 공지 제목", "테스트 공지 내용");

        mockMvc.perform(post("/api/notices")
                        .param("studyId", studyId.toString())
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("공지 조회 테스트")
    void getNotice() throws Exception {
        Integer noticeId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/notices/{noticeId}", noticeId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("공지 목록 조회 테스트")
    void getNoticeList() throws Exception {
        Integer studyId = 1;
        Integer page = 0;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/notices")
                        .param("page", String.valueOf(page))
                        .param("studyId", String.valueOf(studyId))
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("공지 수정 테스트")
    void updateNotice() throws Exception {
        Integer noticeId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        NoticeUpdateRequest request = new NoticeUpdateRequest("수정된 공지 제목", "수정된 공지 내용");

        mockMvc.perform(put("/api/notices/{noticeId}", noticeId)
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("공지 삭제 테스트")
    void deleteNotice() throws Exception {
        Integer noticeId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(delete("/api/notices/{noticeId}", noticeId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNoContent());
    }
}
