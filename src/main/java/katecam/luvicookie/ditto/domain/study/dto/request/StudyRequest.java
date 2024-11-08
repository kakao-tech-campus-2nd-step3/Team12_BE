package katecam.luvicookie.ditto.domain.study.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import lombok.Getter;

import static katecam.luvicookie.ditto.domain.study.domain.Study.MAX_STUDY_NAME_LENGTH;
import static katecam.luvicookie.ditto.domain.study.domain.Study.MAX_STUDY_TOPIC_LENGTH;

@Getter
public class StudyRequest {

    @NotBlank(message = "스터디명을 입력해주세요.")
    @Size(max = MAX_STUDY_NAME_LENGTH, message = "스터디명을 {max}자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "스터디 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "스터디 모집 여부를 입력해주세요.")
    private Boolean isOpen;

    @NotBlank(message = "스터디 주제를 입력해주세요.")
    @Size(max = MAX_STUDY_TOPIC_LENGTH, message = "스터디 주제를 {max}자 이하로 입력해주세요.")
    private String topic;

    public Study toEntity() {
        return Study.builder()
                .name(name)
                .description(description)
                .isOpen(isOpen)
                .topic(topic)
                .build();
    }

    public Study toEntity(String imageUrl) {
        return Study.builder()
                .name(name)
                .description(description)
                .isOpen(isOpen)
                .topic(topic)
                .profileImage(imageUrl)
                .build();
    }

}
