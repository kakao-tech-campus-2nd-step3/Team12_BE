package katecam.luvicookie.ditto.domain.assignment.application;

import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentFileRepository;
import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentRepository;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.assignment.dto.response.*;
import katecam.luvicookie.ditto.domain.assignment.dto.request.AssignmentRequest;
import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.studymember.application.StudyMemberService;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyMemberResponse;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final StudyRepository studyRepository;
    private final AssignmentFileRepository assignmentFileRepository;
    private final AwsFileService awsFileService;
    private final StudyMemberService studyMemberService;
    private final MemberService memberService;

    @Transactional
    public AssignmentCreateResponse create(AssignmentRequest assignmentRequest, Integer studyId, Member member) {
        studyMemberService.validateStudyLeader(studyId, member);
        Assignment assignment = assignmentRequest.toEntity();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        assignment.setStudy(study);
        assignmentRepository.save(assignment);
        return AssignmentCreateResponse.from(assignment);
    }

    @Transactional
    public AssignmentResponse update(Integer assignmentId, AssignmentRequest assignmentRequest, Member member) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        studyMemberService.validateStudyLeader(assignment.getStudy().getId(), member);
        assignment.updateAssignment(assignmentRequest);
        return AssignmentResponse.from(assignment);
    }

    @Transactional
    public void delete(Integer assignmentId, Member member) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        studyMemberService.validateStudyLeader(assignment.getStudy().getId(), member);

        if(assignmentRepository.existsById(assignmentId)){
            assignmentRepository.deleteById(assignmentId);
        }
    }

    public AssignmentListResponse getAssignments(Pageable pageable, Integer studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        Page<AssignmentResponse> assignmentResponses = assignmentRepository.findAllByStudy(pageable, study)
                .map(AssignmentResponse::from);
        return AssignmentListResponse.from(assignmentResponses);
    }

    public AssignmentResponse getAssignment(Integer assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        return AssignmentResponse.from(assignment);
    }

    public AssignmentFileResponse getAssignmentFiles(Integer assignmentId, Integer memberId){
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        Member searchMember = memberService.findMemberById(memberId);
        List<AssignmentFile> files = assignmentFileRepository.findAllByAssignmentAndMember(assignment, searchMember);
        return AssignmentFileResponse.from(files);
    }


    public AssignmentFileListResponse getAllAssignmentFiles(Integer assignmentId, Member member, Pageable pageable){
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        Page<AssignmentFileResponse> fileResponses = assignmentFileRepository.findAllByAssignment(pageable, assignment).map(AssignmentFileResponse::from);
        return AssignmentFileListResponse.from(fileResponses);
    }

    @Transactional
    public AssignmentFileResponse uploadAssignments(Member member, Integer assignmentId, MultipartFile file) throws IOException {

        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        FileResponse fileResponse = awsFileService.saveAssignment(file);

        AssignmentFile assignmentFile = new AssignmentFile(fileResponse.fileName(), assignment, member, fileResponse.fileUrl());
        assignmentFileRepository.save(assignmentFile);
        return AssignmentFileResponse.from(assignmentFile);
    }

    public ResponseEntity<byte[]> download(Integer fileId) throws IOException {
        AssignmentFile assignmentFile = assignmentFileRepository.findById(fileId).orElseThrow(() -> new GlobalException(ErrorCode.FILE_NOT_FOUND));
        return awsFileService.downloadFile(assignmentFile.getFileName());
    }

    public double getSubmissionRate(Integer studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        List<Assignment> allByStudy = assignmentRepository.findAllByStudy(study);
        List<StudyMemberResponse> memberList = studyMemberService.getStudyMemberList(studyId);

        double totalSubmissionRate = 0.0;

        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (StudyMemberResponse memberResponse : memberList) {
            LocalDateTime joinedAt = LocalDateTime.parse(memberResponse.joinedAt(), format1);

            long totalAssignment = allByStudy.stream()
                    .filter(assignment -> joinedAt.isBefore(assignment.getDeadline()))
                    .count();

            long submitCount = allByStudy.stream()
                    .filter(assignment -> joinedAt.isBefore(assignment.getDeadline()))
                    .filter(assignment -> assignmentFileRepository.existsByAssignmentAndMemberId(assignment, memberResponse.member().id()))
                    .count();

            if (totalAssignment > 0) {
                totalSubmissionRate += (double) submitCount / (double) totalAssignment;
            }
        }

        return totalSubmissionRate / memberList.size();
    }


    public Integer getTotalAssignmentCount(Integer studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        List<Assignment> allByStudy = assignmentRepository.findAllByStudy(study);
        return allByStudy.size();
    }

}
