package katecam.luvicookie.ditto.domain.study.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

import static katecam.luvicookie.ditto.domain.study.domain.Study.MAX_STUDY_NAME_LENGTH;
import static katecam.luvicookie.ditto.domain.study.domain.Study.MAX_STUDY_TOPIC_LENGTH;

@Getter
public class StudyCriteria {

    @Size(max = MAX_STUDY_TOPIC_LENGTH, message = "스터디 주제를 {max}자 이하로 입력해주세요.")
    private String topic;

    @Size(max = MAX_STUDY_NAME_LENGTH, message = "스터디명을 {max}자 이하로 입력해주세요.")
    private String name;

    private Boolean isOpen;


}
