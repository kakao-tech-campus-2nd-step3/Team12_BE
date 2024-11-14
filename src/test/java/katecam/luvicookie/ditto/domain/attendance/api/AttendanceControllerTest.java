package katecam.luvicookie.ditto.domain.attendance.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.luvicookie.ditto.ControllerTestConfig;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceCreateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateUpdateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceUpdateRequest;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AttendanceControllerTest extends ControllerTestConfig {

    @Autowired
    public AttendanceControllerTest(WebApplicationContext context, ObjectMapper objectMapper) {
        super(context, objectMapper);
    }

    @Test
    @DisplayName("스터디 출석 테스트")
    void createAttendance() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;
        Integer dateId = 2;
        String code = "true_";

        AttendanceCreateRequest request = new AttendanceCreateRequest(code, dateId);

        mockMvc.perform(post("/api/attendance")
                        .param("study_id", String.valueOf(studyId))
                        .content(toJsonString(request).getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("출석 여부 목록 조회 테스트")
    void getAttendanceList() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;
        Integer memberId = 1;

        mockMvc.perform(get("/api/attendance")
                        .param("study_id", String.valueOf(studyId))
                        .param("member_id", String.valueOf(memberId))
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("출석 여부 수정 테스트")
    void updateAttendance() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;
        Integer memberId = 3;
        Integer dateId = 2;
        Boolean isAttended = true;

        AttendanceUpdateRequest request = new AttendanceUpdateRequest(dateId, isAttended);

        mockMvc.perform(put("/api/attendance")
                        .param("study_id", String.valueOf(studyId))
                        .param("member_id", String.valueOf(memberId))
                        .content(toJsonString(request).getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("출석일자 생성 테스트")
    void createAttendanceDate() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        String requestJson = "{\"start_time\":\"2025-11-11 11:00\",\"time_interval\":5}";
        Integer studyId = 1;

        mockMvc.perform(post("/api/attendance/date")
                        .param("study_id", String.valueOf(studyId))
                        .content(requestJson.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("출석일자 목록 조회 테스트")
    void getAttendanceDateList() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;

        mockMvc.perform(get("/api/attendance/date")
                        .param("study_id", String.valueOf(studyId))
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("출석일자 수정 테스트")
    void updateAttendanceDate() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;
        Integer dateId = 1;
        Integer intervalMinutes = 5;
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(intervalMinutes);

        AttendanceDateUpdateRequest request = new AttendanceDateUpdateRequest(dateId, startTime, intervalMinutes);

        mockMvc.perform(put("/api/attendance/date")
                        .param("study_id", String.valueOf(studyId))
                        .content(toJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("출석일자 삭제 테스트")
    void deleteAttendanceDate() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;
        String requestJson = "{\"start_time\":\"2024-11-11 11:00\"}";

        mockMvc.perform(delete("/api/attendance/date")
                        .param("study_id", String.valueOf(studyId))
                        .content(requestJson.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("출석 코드 요청 테스트")
    void requestAttendanceCode() throws Exception {
        Member member = new MemberFixture(1);
        String accessToken = JwtConstants.JWT_TYPE + TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        Integer studyId = 1;
        Integer dateId = 1;

        mockMvc.perform(get("/api/attendance/code")
                        .param("study_id", String.valueOf(studyId))
                        .param("date_id", String.valueOf(dateId))
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }
}