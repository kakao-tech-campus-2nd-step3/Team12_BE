package katecam.luvicookie.ditto.domain.studymember.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;
import java.time.format.DateTimeFormatter;
public record StudyMemberApplyResponse(
    @JsonProperty("member")
    MemberResponse member,
    @JsonProperty("created_at")
    String createdAt,
    @JsonProperty("message")
    String message
) {

    public StudyMemberApplyResponse(StudyMember studyMember) {
        this(new MemberResponse(studyMember.getMember()), studyMember.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), studyMember.getMessage());
    }
}
