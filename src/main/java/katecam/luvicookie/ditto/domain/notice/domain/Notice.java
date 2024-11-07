package katecam.luvicookie.ditto.domain.notice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "member_id", nullable = false)
    private Integer memberId;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Notice(String title, String content, Integer memberId, LocalDateTime createdAt){
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public void updateNotice(NoticeUpdateRequest noticeUpdateRequest){
        this.title = noticeUpdateRequest.getTitle();
        this.content = noticeUpdateRequest.getContent();
    }
}
