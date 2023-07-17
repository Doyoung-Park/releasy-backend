package kakao99.backend.issue.dto;

import kakao99.backend.entity.Member;

import java.util.Date;
import java.util.List;

public class ProjectDTO {
    private int id;

    private String name;

    private List<Member> users; // 담당자

    private List<IssueDTO> issues;  // 이슈 리스트

    private String status;
    private String listPosition;
    private int priority;
    private List<Integer> userIds;  // ??? -> 발행자 id??
    private Date createdAt;
}
