package katecam.luvicookie.ditto.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.notification.domain.Notification;

import java.time.format.DateTimeFormatter;

public record NotificationResponse(
        @JsonProperty("title")
        String title,
        @JsonProperty("content")
        String content,
        @JsonProperty("image_url")
        String imageUrl,
        @JsonProperty("url")
        String url,
        @JsonProperty("created_at")
        String createdAt
) {

    public NotificationResponse(Notification notification) {
        this(notification.getTitle(), notification.getContent(), notification.getImageUrl(), notification.getUrl(), notification.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}