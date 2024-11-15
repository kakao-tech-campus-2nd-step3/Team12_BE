package katecam.luvicookie.ditto.domain.notification.application;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notification.dao.NotificationRepository;
import katecam.luvicookie.ditto.domain.notification.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getNotificationList(Member member) {
        return notificationRepository.findAllByMember(member)
                .stream()
                .map(NotificationResponse::new)
                .toList();
    }
}
