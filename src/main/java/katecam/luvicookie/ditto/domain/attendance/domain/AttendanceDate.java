package katecam.luvicookie.ditto.domain.attendance.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@Table(name = "attendance_date")
public class AttendanceDate extends BaseTimeEntity {

    public static final int ATTENDANCE_CODE_LENGTH = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "code", nullable = false, insertable = false, updatable = false, length = ATTENDANCE_CODE_LENGTH)
    private String code;

    @Builder
    public AttendanceDate(Study study, LocalDateTime startTime, LocalDateTime deadline) {
        this.study = study;
        this.startTime = startTime;
        this.deadline = deadline;
    }

    public boolean isDifferentCode(String code) {
        return !this.code.equals(code);
    }

    public boolean isUnableToAttend() {
        LocalDateTime currentTime = LocalDateTime.now();
        return !(currentTime.isAfter(startTime) && currentTime.isBefore(deadline));
    }

}
