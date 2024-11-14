package katecam.luvicookie.ditto.domain.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record AssignmentFileResponse(
    FileResponse file,
    @JsonProperty("member_id")
    Integer memberId,
    String nickname
){


    public static AssignmentFileResponse from(AssignmentFile assignmentFile) {
        Member member = assignmentFile.getMember();
        return AssignmentFileResponse.builder()
                .file(FileResponse.from(assignmentFile))
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }


}
