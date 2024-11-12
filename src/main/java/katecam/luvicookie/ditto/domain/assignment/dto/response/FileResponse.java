package katecam.luvicookie.ditto.domain.assignment.dto.response;

import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileResponse {
    public Integer id;
    public String fileName;
    public String fileUrl;

    public static FileResponse from(AssignmentFile assignmentFile){
        return FileResponse.builder()
                .id(assignmentFile.getId())
                .fileName(assignmentFile.getFileName())
                .fileUrl(assignmentFile.getFileUrl())
                .build();
    }
}
