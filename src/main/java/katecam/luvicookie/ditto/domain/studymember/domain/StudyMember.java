package katecam.luvicookie.ditto.domain.studymember.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "study_member")
public class StudyMember extends BaseTimeEntity {

    private static final int MAX_STUDY_MEMBER_MESSAGE_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @JoinColumn(name = "study_id", nullable = false)
    private Integer studyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "role", nullable = false)
    private StudyMemberRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "message", nullable = true, length = MAX_STUDY_MEMBER_MESSAGE_LENGTH)
    private String message;

    @Builder
    StudyMember(Integer studyId, Member member, StudyMemberRole role, LocalDateTime joinedAt, String message) {
        this.studyId = studyId;
        this.member = member;
        this.role = role;
        this.joinedAt = joinedAt;
        this.message = message;
    }

    public void setRole(StudyMemberRole role) {
        this.role = role;
    }
}
