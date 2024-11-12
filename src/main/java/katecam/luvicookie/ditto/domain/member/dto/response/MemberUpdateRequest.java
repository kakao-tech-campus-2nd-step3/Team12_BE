package katecam.luvicookie.ditto.domain.member.dto.response;

public record MemberUpdateRequest(
        String name,
        String contact,
        String nickname,
        String description
){ }
