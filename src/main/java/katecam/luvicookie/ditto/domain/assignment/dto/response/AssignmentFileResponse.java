package katecam.luvicookie.ditto.domain.assignment.dto.response;

import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record AssignmentFileResponse (
    List<FileResponse> files,
    Integer memberId,
    String nickname
){

    public static AssignmentFileResponse from(List<AssignmentFile> assignmentFiles) {
        List<FileResponse> fileResponses = assignmentFiles.stream()
                .map(FileResponse::from).toList();
        Member member = assignmentFiles.get(0).getMember();
        return AssignmentFileResponse.builder()
                .files(fileResponses)
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }

    public static AssignmentFileResponse from(AssignmentFile assignmentFile) {
        List<FileResponse> fileResponses = new ArrayList<>();
        fileResponses.add(FileResponse.from(assignmentFile));
        Member member = assignmentFile.getMember();
        return AssignmentFileResponse.builder()
                .files(fileResponses)
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }


}
