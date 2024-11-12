package katecam.luvicookie.ditto.domain.member.dto.request;

public record MemberUpdateRequest(
        String name,
        String contact,
        String nickname,
        String description
){ }
