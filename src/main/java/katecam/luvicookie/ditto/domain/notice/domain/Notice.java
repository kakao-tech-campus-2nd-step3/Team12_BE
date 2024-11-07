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
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    @Builder
    public Notice(String title, String content, Study study, Member member){
        this.title = title;
        this.content = content;
        this.study = study;
        this.member = member;
    }

    public void updateNotice(NoticeUpdateRequest noticeUpdateRequest){
        if(title!=null)
            this.title = noticeUpdateRequest.getTitle();
        if(content!=null)
            this.content = noticeUpdateRequest.getContent();
    }
}
