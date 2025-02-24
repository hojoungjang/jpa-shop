package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.create(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setUsername(request.getName());

        Long id = memberService.create(member);
        return new CreateMemberResponse(id);
    }

    @PatchMapping("/api/v2/members/{id}")
    public PatchMemberResponse patchMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid PatchMemberRequest request) {
        memberService.patch(id, request.getName());
        Member member = memberService.findOne(id);
        return new PatchMemberResponse(member.getId(), member.getUsername());
    }

    @Data
    public static class PatchMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class PatchMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    public static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    public static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
