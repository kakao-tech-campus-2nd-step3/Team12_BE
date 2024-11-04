package katecam.luvicookie.ditto.domain.notice.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeUpdateRequest;
import katecam.luvicookie.ditto.global.entity.BaseTimeEntity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Table(name = "notice")
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teammate_id", nullable = false)
    private TeamMate teamMate;


    @Builder
    public Notice(String title, String content, TeamMate teamMate){
        this.title = title;
        this.content = content;
        this.teamMate = teamMate;
    }

    public void updateNotice(NoticeUpdateRequest noticeUpdateRequest){
        if(title!=null)
            this.title = noticeUpdateRequest.getTitle();
        if(content!=null)
            this.content = noticeUpdateRequest.getContent();
    }
}
