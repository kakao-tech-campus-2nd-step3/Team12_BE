package katecam.luvicookie.ditto.domain.assignment.application;

import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentFileRepository;
import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentRepository;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.assignment.dto.*;
import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final StudyRepository studyRepository;
    private final AssignmentFileRepository assignmentFileRepository;
    private final AwsFileService awsFileService;

    public AssignmentCreateResponse create(AssignmentRequest assignmentRequest, Integer studyId) {
        //teammate.role이 팀장인지 아닌지
        Assignment assignment = assignmentRequest.toEntity();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        assignment.setStudy(study);
        assignmentRepository.save(assignment);
        return AssignmentCreateResponse.from(assignment);
    }

    @Transactional
    public AssignmentResponse update(Integer assignmentId, AssignmentRequest assignmentRequest) {
        //teammate.role이 팀장인지 아닌지
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        assignment.updateAssignment(assignmentRequest);
        return AssignmentResponse.from(assignment);
    }

    public void delete(Integer assignmentId) {
        //teammate.role이 팀장인지 아닌지
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

    public AssignmentFileResponse getAssignmentFiles(Integer assignmentId, Member member){
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        List<AssignmentFile> files = assignmentFileRepository.findAllByAssignmentAndMember(assignment, member);
        return AssignmentFileResponse.from(files);
    }

    /*public isTeamReader(TeamMate teamMate){

    }*/

    public AssignmentFileResponse uploadAssignments(Member member, Integer assignmentId, MultipartFile file) throws IOException {

        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        FileResponse fileResponse = awsFileService.saveAssignment(file);

        AssignmentFile assignmentFile = new AssignmentFile(fileResponse.getFileName(), assignment, member, fileResponse.getFileUrl());
        assignmentFileRepository.save(assignmentFile);
        return AssignmentFileResponse.from(assignmentFile);
    }

    public ResponseEntity<byte[]> download(Integer fileId) throws IOException {
        AssignmentFile assignmentFile = assignmentFileRepository.findById(fileId).orElseThrow(() -> new GlobalException(ErrorCode.FILE_NOT_FOUND));
        return awsFileService.downloadFile(assignmentFile.getFileName());
    }

}
