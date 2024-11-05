package katecam.luvicookie.ditto.domain.study.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import katecam.luvicookie.ditto.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "study")
public class Study extends BaseTimeEntity {

    public static final int MAX_STUDY_NAME_LENGTH = 127;
    private static final int MAX_STUDY_DESCRIPTION_LENGTH = 255;
    public static final int MAX_STUDY_TOPIC_LENGTH = 25;
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

    @Column(name = "invite_token", nullable = true, length = MAX_STUDY_INVITE_TOKEN_LENGTH)
    private String inviteToken;

    @Builder
    public Study(String name, String description, Boolean isOpen, String topic, String profileImage) {
        this.name = name;
        this.description = description;
        this.isOpen = isOpen;
        this.topic = topic;
        this.profileImage = profileImage;
    }

}