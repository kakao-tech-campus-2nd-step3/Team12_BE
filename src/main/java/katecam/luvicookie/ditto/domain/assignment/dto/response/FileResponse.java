package katecam.luvicookie.ditto.domain.assignment.dto.response;

import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import lombok.Builder;
import lombok.Getter;

@Builder
public record FileResponse (
        Integer id,
        String fileName,
        String fileUrl
){
    public static FileResponse from(AssignmentFile assignmentFile){
        return FileResponse.builder()
                .id(assignmentFile.getId())
                .fileName(assignmentFile.getFileName())
                .fileUrl(assignmentFile.getFileUrl())
                .build();
    }
}
