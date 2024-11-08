package katecam.luvicookie.ditto.domain.study.dto.response;

import katecam.luvicookie.ditto.domain.study.domain.Study;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class StudyResponse {

    private Integer studyId;
    private String name;
    private String description;
    private String createdAt;
    private Boolean isOpen;
    private String topic;
    private String profileImage;

    public static StudyResponse from(Study study) {
        return StudyResponse.builder()
                .studyId(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .createdAt(
                        study.getCreatedAt()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .isOpen(study.getIsOpen())
                .topic(study.getTopic())
                .profileImage(study.getProfileImage())
                .build();
    }

}