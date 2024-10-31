package katecam.luvicookie.ditto.domain.notice.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeUpdateRequest;
import lombok.*;

import java.time.LocalDate;

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
    @Column(name = "writerid", nullable = false)
    private Integer writer_id;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "createdat", nullable = false)
    private LocalDate createdAt;

    @Builder
    public Notice(String title, String content, Integer writer_id, LocalDate createdAt){
        this.title = title;
        this.content = content;
        this.writer_id = writer_id;
        this.createdAt = createdAt;
    }

    public void updateNotice(NoticeUpdateRequest noticeUpdateRequest){
        this.title = noticeUpdateRequest.getTitle();
        this.content = noticeUpdateRequest.getContent();
    }
}
