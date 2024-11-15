package katecam.luvicookie.ditto.domain.study.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import katecam.luvicookie.ditto.domain.study.domain.Study;

import static katecam.luvicookie.ditto.domain.study.domain.Study.MAX_STUDY_NAME_LENGTH;
import static katecam.luvicookie.ditto.domain.study.domain.Study.MAX_STUDY_TOPIC_LENGTH;

public record StudyUpdateRequest(
        @NotBlank(message = "스터디명을 입력해주세요.")
        @Size(max = MAX_STUDY_NAME_LENGTH, message = "스터디명을 {max}자 이하로 입력해주세요.")
        String name,

        @NotBlank(message = "스터디 설명을 입력해주세요.")
        String description,

        @NotNull(message = "스터디 모집 여부를 입력해주세요.")
        @JsonProperty("is_open")
        Boolean isOpen,

        @NotBlank(message = "스터디 주제를 입력해주세요.")
        @Size(max = MAX_STUDY_TOPIC_LENGTH, message = "스터디 주제를 {max}자 이하로 입력해주세요.")
        String topic
) {
    public Study toEntity() {
        return Study.builder()
                .name(name)
                .description(description)
                .isOpen(isOpen)
                .topic(topic)
                .build();
    }

}
