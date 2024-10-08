package katecam.luvicookie.ditto.domain.study.dto.response;

import katecam.luvicookie.ditto.domain.study.domain.Study;
import lombok.Builder;

@Builder
public class StudyResponse {

    private Integer studyId;
    private String name;
    private String description;
    private Boolean isOpen;
    private String topic;
    private String profileImage;

    public static StudyResponse toResponse(Study study) {
        return StudyResponse.builder()
                .studyId(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .isOpen(study.getIsOpen())
                .topic(study.getTopic())
                .profileImage(study.getProfileImage())
                .build();
    }

}
