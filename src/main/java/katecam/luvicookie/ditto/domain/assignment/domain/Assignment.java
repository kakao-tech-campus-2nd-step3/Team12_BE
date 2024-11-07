package katecam.luvicookie.ditto.domain.assignment.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentRequest;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.entity.BaseTimeEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Table(name = "assignment")
public class Assignment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Builder
    public Assignment(String title, String content, Study study, LocalDateTime deadline){
        this.title = title;
        this.content = content;
        this.study = study;
        this.deadline = deadline;
    }

    public void updateAssignment(AssignmentRequest assignmentRequest){
        if(assignmentRequest.getTitle() != null)
            this.title = assignmentRequest.getTitle();
        if(assignmentRequest.getContent() != null)
            this.content = assignmentRequest.getContent();
        if(assignmentRequest.getDeadline() != null){
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.deadline = LocalDateTime.parse(assignmentRequest.getDeadline(), format1);
        }
    }

}
