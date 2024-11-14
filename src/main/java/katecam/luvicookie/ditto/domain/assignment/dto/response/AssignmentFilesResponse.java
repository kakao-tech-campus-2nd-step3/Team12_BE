package katecam.luvicookie.ditto.domain.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record AssignmentFilesResponse(
    List<FileResponse> files,
    @JsonProperty("member_id")
    Integer memberId,
    String nickname
){

    public static AssignmentFilesResponse from(List<AssignmentFile> assignmentFiles) {
        List<FileResponse> fileResponses = assignmentFiles.stream()
                .map(FileResponse::from).toList();
        Member member = assignmentFiles.get(0).getMember();
        return AssignmentFilesResponse.builder()
                .files(fileResponses)
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }

    public static AssignmentFilesResponse from(AssignmentFile assignmentFile) {
        List<FileResponse> fileResponses = new ArrayList<>();
        fileResponses.add(FileResponse.from(assignmentFile));
        Member member = assignmentFile.getMember();
        return AssignmentFilesResponse.builder()
                .files(fileResponses)
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }


}
