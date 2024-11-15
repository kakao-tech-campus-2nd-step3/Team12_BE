package katecam.luvicookie.ditto.domain.studymember.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.studymember.application.StudyMemberService;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.domain.studymember.dto.request.StudyMemberApplyRequest;
import katecam.luvicookie.ditto.domain.studymember.dto.request.StudyMemberInviteRequest;
import katecam.luvicookie.ditto.domain.studymember.dto.request.StudyMemberRoleRequest;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyInviteResponse;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyMemberApplyResponse;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyMemberResponse;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyMemberRoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studies/{studyId}/members")
@RequiredArgsConstructor
@Tag(name = "스터디 멤버", description = "스터디 멤버를 관리하는 API 입니다.")
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    @GetMapping
    @Operation(summary = "조회", description = "스터디에 가입한 회원을 모두 조회합니다.")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(@LoginUser Member member, @PathVariable Integer studyId) {
        studyMemberService.validateStudyMember(studyId, member);
        List<StudyMemberResponse> memberResponseList = studyMemberService.getStudyMemberList(studyId);
        return ResponseEntity.ok(memberResponseList);
    }

    @GetMapping("/role")
    @Operation(summary = "조회 - 역할", description = "스터디 멤버의 역할을 조회합니다.")
    public ResponseEntity<StudyMemberRoleResponse> getStudyMemberRole(@LoginUser Member member, @PathVariable Integer studyId) {
        StudyMemberRoleResponse role = studyMemberService.getStudyMemberRole(studyId, member.getId());
        return ResponseEntity.ok(role);
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "수정", description = "스터디 멤버의 역할을 수정합니다.")
    public ResponseEntity<StudyMemberResponse> putStudyMember(@LoginUser Member member, @PathVariable Integer studyId, @PathVariable Integer memberId, @Valid @RequestBody StudyMemberRoleRequest studyMemberRequest) {
        studyMemberService.validateStudyLeader(studyId, member);
        StudyMemberResponse memberResponse = studyMemberService.updateStudyMember(studyId, memberId, StudyMemberRole.from(studyMemberRequest.role()));
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping
    @Operation(summary = "탈퇴", description = "스터디로부터 탈퇴합니다.")
    public ResponseEntity<Void> withdrawFromStudy(@LoginUser Member member, @PathVariable Integer studyId) {
        studyMemberService.deleteStudyMember(studyId, member.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "삭제", description = "스터디 멤버를 퇴출합니다.")
    public ResponseEntity<Void> deleteStudyMember(@LoginUser Member member, @PathVariable Integer studyId, @PathVariable Integer memberId) {
        studyMemberService.validateStudyLeader(studyId, member);
        studyMemberService.deleteStudyMember(studyId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/apply")
    @Operation(summary = "가입 신청 조회", description = "스터디에 가입 신청을 한 회원 목록을 조회합니다.")
    public ResponseEntity<List<StudyMemberApplyResponse>> getStudyMemberRequest(@LoginUser Member member, @PathVariable Integer studyId) {
        studyMemberService.validateStudyLeader(studyId, member);
        List<StudyMemberApplyResponse> memberResponseList = studyMemberService.getStudyMemberApplyList(studyId);
        return ResponseEntity.ok(memberResponseList);
    }

    @PostMapping("/apply")
    @Operation(summary = "가입 승인", description = "회원의 스터디 가입 신청을 승인합니다.")
    public ResponseEntity<Object> postStudyMemberRequest(@LoginUser Member member, @PathVariable Integer studyId, @Valid @RequestBody StudyMemberApplyRequest request) {
        StudyMemberResponse memberResponse = studyMemberService.createStudyMember(studyId, member, StudyMemberRole.APPLICANT, request.message());
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("/apply/{memberId}")
    @Operation(summary = "", description = "")
    public ResponseEntity<Object> putStudyMemberRequest(@LoginUser Member member, @PathVariable Integer studyId, @PathVariable Integer memberId, @Valid @RequestBody StudyMemberRoleRequest studyMemberRequest) {
        studyMemberService.validateStudyLeader(studyId, member);
        StudyMemberResponse memberResponse = studyMemberService.updateStudyMember(studyId, memberId, StudyMemberRole.from(studyMemberRequest.role()));
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/invites")
    @Operation(summary = "", description = "")
    public ResponseEntity<StudyInviteResponse> getMemberInvite(@LoginUser Member member, @PathVariable int studyId) {
        studyMemberService.validateStudyLeader(studyId, member);
        StudyInviteResponse studyInviteResponse = studyMemberService.getStudyInviteToken(studyId);
        return ResponseEntity.ok(studyInviteResponse);
    }

    @PostMapping("/invites")
    @Operation(summary = "", description = "")
    public ResponseEntity<StudyMemberResponse> postMemberInvite(@LoginUser Member member, @PathVariable int studyId, @Valid @RequestBody StudyMemberInviteRequest request) {
        StudyMemberResponse memberResponse = studyMemberService.joinStudyMember(studyId, member, request.token());
        return ResponseEntity.ok(memberResponse);
    }

}
