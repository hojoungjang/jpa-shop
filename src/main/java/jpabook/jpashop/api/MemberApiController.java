package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/api/v1/members")
    public List<Member> getMembersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public ResponseData getMembersV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getUsername()))
                .toList();
        return new ResponseData(collect);
    }

    @Data
    @AllArgsConstructor
    public static class ResponseData<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    public static class MemberDto {
        private String name;
    }
}
