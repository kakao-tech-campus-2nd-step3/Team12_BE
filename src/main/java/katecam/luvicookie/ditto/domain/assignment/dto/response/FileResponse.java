package katecam.luvicookie.ditto.domain.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import lombok.Builder;

@Builder
public record FileResponse (
        Integer id,
        @JsonProperty("file_name")
        String fileName,
        @JsonProperty("file_url")
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
