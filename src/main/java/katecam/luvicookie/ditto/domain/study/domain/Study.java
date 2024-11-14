package katecam.luvicookie.ditto.domain.study.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
@Table(name = "study")
public class Study extends BaseTimeEntity {

    public static final int MAX_STUDY_NAME_LENGTH = 127;
    public static final int MAX_STUDY_TOPIC_LENGTH = 25;
    private static final int MAX_STUDY_DESCRIPTION_LENGTH = 255;
    private static final int MAX_STUDY_PROFILE_IMAGE_LENGTH = 255;
    private static final int MAX_STUDY_INVITE_TOKEN_LENGTH = 48;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = MAX_STUDY_NAME_LENGTH)
    private String name;

    @Column(name = "description", nullable = false, length = MAX_STUDY_DESCRIPTION_LENGTH)
    private String description;

    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    @Column(name = "topic", nullable = false, length = MAX_STUDY_TOPIC_LENGTH)
    private String topic;

    @Column(name = "profile_image", nullable = false, length = MAX_STUDY_PROFILE_IMAGE_LENGTH)
    private String profileImage;

    @Column(name = "invite_token", insertable = false, updatable = false)
    private String inviteToken;

    @Builder
    public Study(String name, String description, Boolean isOpen, String topic, String profileImage) {
        this.name = name;
        this.description = description;
        this.isOpen = isOpen;
        this.topic = topic;
        this.profileImage = profileImage;
    }

    public void update(Study study) {
        this.name = study.getName();
        this.description = study.getDescription();
        this.isOpen = study.getIsOpen();
        this.topic = study.getTopic();
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}