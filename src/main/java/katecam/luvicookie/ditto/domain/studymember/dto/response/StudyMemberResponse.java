package katecam.luvicookie.ditto.domain.studymember.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import katecam.luvicookie.ditto.domain.member.dto.memberResponseDTO;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;

public record StudyMemberResponse(
    @JsonProperty("member")
    MemberResponse member,
    @JsonProperty("role")
    String role,
    @JsonProperty("joined_at")
    String joinedAt
) {

    public StudyMemberResponse(StudyMember studyMember) {
        this(new MemberResponse(studyMember.getMember()), studyMember.getRole().getValue(), studyMember.getJoinedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}