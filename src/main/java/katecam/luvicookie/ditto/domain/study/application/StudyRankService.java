package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.assignment.application.AssignmentService;
import katecam.luvicookie.ditto.domain.attendance.application.AttendanceService;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyRankInfo;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyRankListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StudyRankService {

    private final StudyRepository studyRepository;
    private final AssignmentService assignmentService;
    private final AttendanceService attendanceService;

    @Transactional(readOnly = true)
    public StudyRankListResponse getStudyRankings(Pageable pageable) {
        List<StudyRankInfo> studyRankInfoList = studyRepository.findAll()
                .stream()
                .map(study -> {
                    Integer studyId = study.getId();
                    Integer totalAssignmentCount = assignmentService.getTotalAssignmentCount(studyId);
                    Integer submissionCount = assignmentService.getSubmissionCount(studyId);
                    Integer totalAttendanceDateCount = attendanceService.getTotalAttendanceDateCount(studyId);
                    Integer studyAttendanceCount = attendanceService.getStudyAttendanceCount(studyId);

                    return StudyRankInfo.builder()
                            .name(study.getName())
                            .totalAssignmentCount(totalAssignmentCount)
                            .completedAssignmentCount(submissionCount)
                            .totalAttendanceDateCount(totalAttendanceDateCount)
                            .attendedDateCount(studyAttendanceCount)
                            .build();
                })
                .toList();

        int previousItemCount = pageable.getPageNumber() * pageable.getPageSize();
        AtomicInteger index = new AtomicInteger(previousItemCount + 1);

        List<StudyRankResponse> sortedStudyRankInfo = studyRankInfoList.stream()
                .sorted(Comparator.comparingDouble(this::getRankScore).reversed())
                .skip(previousItemCount)
                .limit(pageable.getPageSize())
                .map(studyRankInfo -> new StudyRankResponse(index.getAndIncrement(), studyRankInfo))
                .toList();

        return StudyRankListResponse.from(new PageImpl<>(sortedStudyRankInfo, pageable, studyRankInfoList.size()));
    }

    private double getRankScore(StudyRankInfo studyRankInfo) {
        double rankScore = 0.0;
        if (studyRankInfo.completedAssignmentCount() != 0) {
            rankScore += Math.pow(studyRankInfo.completedAssignmentCount(), 2) / studyRankInfo.totalAssignmentCount();
        }

        if (studyRankInfo.attendedDateCount() != 0) {
            rankScore += Math.pow(studyRankInfo.attendedDateCount(), 2) / studyRankInfo.totalAttendanceDateCount();
        }

        return rankScore;
    }

}
