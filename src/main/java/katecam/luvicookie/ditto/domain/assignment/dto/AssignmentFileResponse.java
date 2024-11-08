package katecam.luvicookie.ditto.domain.assignment.dto;

import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class AssignmentFileResponse {
    List<FileResponse> files;

    public AssignmentFileResponse(List<FileResponse> fileNames) {
        this.files = fileNames;
    }

    public static AssignmentFileResponse from(List<AssignmentFile> assignmentFiles) {
        List<FileResponse> fileResponses = assignmentFiles.stream()
                .map(assignmentFile -> FileResponse.builder()
                                        .fileName(assignmentFile.getFileName())
                                        .id(assignmentFile.getId())
                                        .build()
                ).toList();
        return AssignmentFileResponse.builder()
                .files(fileResponses)
                .build();
    }
}
