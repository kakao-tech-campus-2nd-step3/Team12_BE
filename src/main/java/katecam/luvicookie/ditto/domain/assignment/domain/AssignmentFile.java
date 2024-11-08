package katecam.luvicookie.ditto.domain.assignment.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.global.entity.BaseTimeEntity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Table(name = "assignment_file")
public class AssignmentFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public AssignmentFile(String fileName, Assignment assignment, Member member){
        this.fileName = fileName;
        this.assignment = assignment;
        this.member = member;
    }

}
