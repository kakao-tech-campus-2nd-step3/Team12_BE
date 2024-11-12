package katecam.luvicookie.ditto.domain.studymember.api;


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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studies/{studyId}/members")
@RequiredArgsConstructor
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    @GetMapping
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(@LoginUser Member member, @PathVariable Integer studyId) {
        studyMemberService.validateStudyMember(studyId, member);
        List<StudyMemberResponse> memberResponseList = studyMemberService.getStudyMemberList(studyId);
        return ResponseEntity.ok(memberResponseList);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<StudyMemberResponse> putStudyMember(@LoginUser Member member, @PathVariable Integer studyId, @PathVariable Integer memberId, @Valid @RequestBody StudyMemberRoleRequest studyMemberRequest) {
        studyMemberService.validateStudyLeader(studyId, member);
        StudyMemberResponse memberResponse = studyMemberService.updateStudyMember(studyId, memberId, StudyMemberRole.from(studyMemberRequest.role()));
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdrawFromStudy(@LoginUser Member member, @PathVariable Integer studyId) {
        studyMemberService.deleteStudyMember(studyId, member.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteStudyMember(@LoginUser Member member, @PathVariable Integer studyId, @PathVariable Integer memberId) {
        studyMemberService.validateStudyLeader(studyId, member);
        studyMemberService.deleteStudyMember(studyId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/apply")
    public ResponseEntity<List<StudyMemberApplyResponse>> getStudyMemberRequest(@LoginUser Member member, @PathVariable Integer studyId) {
        studyMemberService.validateStudyLeader(studyId, member);
        List<StudyMemberApplyResponse> memberResponseList = studyMemberService.getStudyMemberApplyList(studyId);
        return ResponseEntity.ok(memberResponseList);
    }

    @PostMapping("/apply")
    public ResponseEntity<Object> postStudyMemberRequest(@LoginUser Member member, @PathVariable Integer studyId, @Valid @RequestBody StudyMemberApplyRequest request) {
        StudyMemberResponse memberResponse = studyMemberService.createStudyMember(studyId, member, StudyMemberRole.APPLICANT, request.message());
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("/apply/{memberId}")
    public ResponseEntity<Object> putStudyMemberRequest(@LoginUser Member member, @PathVariable Integer studyId, @PathVariable Integer memberId, @Valid @RequestBody StudyMemberRoleRequest studyMemberRequest) {
        studyMemberService.validateStudyLeader(studyId, member);
        StudyMemberResponse memberResponse = studyMemberService.updateStudyMember(studyId, memberId, StudyMemberRole.from(studyMemberRequest.role()));
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/invites")
    public ResponseEntity<StudyInviteResponse> getMemberInvite(@LoginUser Member member, @PathVariable int studyId) {
        studyMemberService.validateStudyLeader(studyId, member);
        StudyInviteResponse studyInviteResponse = studyMemberService.getStudyInviteToken(studyId);
        return ResponseEntity.ok(studyInviteResponse);
    }

    @PostMapping("/invites")
    public ResponseEntity<StudyMemberResponse> postMemberInvite(@LoginUser Member member, @PathVariable int studyId, @Valid @RequestBody StudyMemberInviteRequest request) {
        StudyMemberResponse memberResponse = studyMemberService.joinStudyMember(studyId, member, request.token());
        return ResponseEntity.ok(memberResponse);
    }

}
