package katecam.luvicookie.ditto.domain.assignment.application;

import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentRepository;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentListResponse;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentRequest;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentResponse;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final StudyRepository studyRepository;

    public void create(AssignmentRequest assignmentRequest, Integer studyId) {
        //teammate.role이 팀장인지 아닌지
        Assignment assignment = assignmentRequest.toEntity();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        assignment.setStudy(study);
        assignmentRepository.save(assignment);
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

    /*public isTeamReader(TeamMate teamMate){

    }*/
}
