package katecam.luvicookie.ditto.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberCreateRequest(
    @NotBlank(message = "이름을 입력해주세요.")
    String name,
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,
    @NotBlank(message = "연락처를 입력해주세요.")
    String contact,
    @NotBlank(message = "닉네임을 입력해주세요.")
    String nickname,
    @NotBlank(message = "자기소개를 입력해주세요.")
    String description
){ }
