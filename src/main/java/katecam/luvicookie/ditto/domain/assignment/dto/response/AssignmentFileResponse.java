package katecam.luvicookie.ditto.domain.assignment.dto.response;

import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public record AssignmentFileResponse (
    List<FileResponse> files
){

    public static AssignmentFileResponse from(List<AssignmentFile> assignmentFiles) {
        List<FileResponse> fileResponses = assignmentFiles.stream()
                .map(FileResponse::from).toList();
        return AssignmentFileResponse.builder()
                .files(fileResponses)
                .build();
    }

    public static AssignmentFileResponse from(AssignmentFile assignmentFile) {
        List<FileResponse> fileResponses = new ArrayList<>();
        fileResponses.add(FileResponse.from(assignmentFile));
        return AssignmentFileResponse.builder()
                .files(fileResponses)
                .build();
    }


}
