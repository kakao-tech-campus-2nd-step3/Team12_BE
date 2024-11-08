package katecam.luvicookie.ditto.domain.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileResponse {
    public Integer id;
    public String fileName;
    public String fileUrl;
}
