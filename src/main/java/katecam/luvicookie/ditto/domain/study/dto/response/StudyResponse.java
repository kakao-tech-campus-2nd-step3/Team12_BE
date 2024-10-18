package katecam.luvicookie.ditto.domain.study.dto.response;

import katecam.luvicookie.ditto.domain.study.domain.Study;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class StudyResponse {

    private Integer studyId;
    private String name;
    private String description;
    private LocalDate createdAt;
    private Boolean isOpen;
    private String topic;
    private String profileImage;

    public static StudyResponse from(Study study) {
        return StudyResponse.builder()
                .studyId(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .createdAt(study.getCreatedAt())
                .isOpen(study.getIsOpen())
                .topic(study.getTopic())
                .profileImage(study.getProfileImage())
                .build();
    }

}