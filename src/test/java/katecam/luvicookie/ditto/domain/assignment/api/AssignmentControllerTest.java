package katecam.luvicookie.ditto.domain.assignment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.luvicookie.ditto.ControllerTestWebMvcConfig;
import katecam.luvicookie.ditto.domain.assignment.dto.request.AssignmentRequest;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AssignmentControllerTest extends ControllerTestWebMvcConfig {

    @Autowired
    public AssignmentControllerTest(WebApplicationContext context, ObjectMapper objectMapper) {
        super(context, objectMapper);
    }

    @Test
    @DisplayName("전체 과제 목록 조회 테스트")
    void getAssignments() throws Exception {
        Integer studyId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/assignments")
                        .param("studyId", studyId.toString())
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제 단일 조회 테스트")
    void getAssignment() throws Exception {
        Integer assignmentId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/assignments/{assignmentId}", assignmentId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제 생성 테스트")
    void createAssignment() throws Exception {
        Integer studyId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        AssignmentRequest request = new AssignmentRequest("과제 제목", "과제 설명", "2024-12-31 12:00");

        mockMvc.perform(post("/api/assignments")
                        .param("studyId", studyId.toString())
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제 수정 테스트")
    void updateAssignment() throws Exception {
        Integer assignmentId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        AssignmentRequest request = new AssignmentRequest("수정된 과제 제목", "수정된 과제 설명", "2024-12-31 12:00");

        mockMvc.perform(put("/api/assignments/{assignmentId}", assignmentId)
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제 삭제 테스트")
    void deleteAssignment() throws Exception {
        Integer assignmentId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(delete("/api/assignments/{assignmentId}", assignmentId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("과제 파일 제출 테스트")
    void submitAssignment() throws Exception {
        Integer assignmentId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        MockMultipartFile file = new MockMultipartFile("file", "test-file.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());

        mockMvc.perform(multipart("/api/assignments/files/{assignmentId}", assignmentId)
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제 제출 파일 조회 테스트")
    void getAssignmentFiles() throws Exception {
        Integer assignmentId = 1;
        Integer memberId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/assignments/files/{assignmentId}/{memberId}", assignmentId, memberId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제 전체 제출 파일 조회 테스트")
    void getAllAssignmentFiles() throws Exception {
        Integer assignmentId = 1;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/assignments/files/{assignmentId}", assignmentId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("과제 파일 다운로드 테스트")
    void downloadAssignment() throws Exception {
        Integer fileId = 3;
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);

        mockMvc.perform(get("/api/assignments/files/download/{fileId}", fileId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }
}
