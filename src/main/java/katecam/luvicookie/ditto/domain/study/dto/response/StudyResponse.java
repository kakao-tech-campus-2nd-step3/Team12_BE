package katecam.luvicookie.ditto.domain.study.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyLeaderResponse;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record StudyResponse (
        @JsonProperty("id")
        Integer studyId,

        String name,
        String description,

        @JsonProperty("created_at")
        String createdAt,

        @JsonProperty("is_open")
        Boolean isOpen,

        String topic,

        @JsonProperty("profile_image")
        String profileImage,

        @JsonProperty("study_leader_info")
        StudyLeaderResponse studyLeaderResponse

) {
    public static StudyResponse from(Study study, StudyLeaderResponse studyLeaderResponse) {
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
                .studyLeaderResponse(studyLeaderResponse)
                .build();
    }

}