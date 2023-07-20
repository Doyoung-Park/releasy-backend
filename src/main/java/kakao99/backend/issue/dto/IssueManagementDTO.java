package kakao99.backend.issue.dto;

/*
projectMock = {
  id: 1,
  name: 'Project 1',
  users: [
    { id: 1, name: 'User 1', email: 'user1@example.com', avatarUrl: 'avatar1url' },
    { id: 2, name: 'User 2', email: 'user2@example.com', avatarUrl: 'avatar2url' },
  ],
  issues: [
    {
      id: 1,
      title: 'Issue 1',
      type: 'task',
      description: 'Description for Issue 1',
      status: 'backlog',
      listPosition: 1,
      comments : [
        {user : {id :"1",name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 1"}
    ],
      priority: 23,
      userIds: [1],
      createdAt: '2023-06-28',
    },
    {
      id: 2,
      title: 'Issue 2',
      type: 'task',
      description: 'Description for Issue 2',
      status: 'backlog',
      listPosition: 2,
      priority: 50,
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 2"}
    ],
      userIds: [2],
      createdAt: '2023-06-29',
    },
    //... more issues as needed
    {
      id: 3,
      title: 'Issue 3',
      type: 'task',
      description: 'Description for Issue 1',
      status: 'backlog',
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 3"}
    ],
      listPosition: 3,
      priority: 90,
      userIds: [1],
      createdAt: '2023-06-28',
    },
    {
      id: 4,
      title: 'Issue 4',
      type: 'task',
      description: 'Description for Issue 2',
      status: 'backlog',
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 4"}
    ],
      listPosition: 4,
      priority: 99,
      userIds: [2],
      createdAt: '2023-06-29',
    },
    //... more issues as needed
    {
      id: 5,
      title: 'Issue 5',
      type: 'task',
      description: 'Description for Issue 1',
      status: 'inprogress',
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 5"}
    ],
      listPosition: 1,
      priority: 73,
      userIds: [1],
      createdAt: '2023-06-28',
    },
    {
      id: 6,
      title: 'Issue 6',
      type: 'task',
      description: 'Description for Issue 2',
      status: 'inprogress',
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 6"}
    ],
      listPosition: 2,
      priority: 10,
      userIds: [2],
      createdAt: '2023-06-29',
    },
    //... more issues as needed
    {
      id: 7,
      title: 'Issue 7',
      type: 'task',
      description: 'Description for Issue 2',
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 7"}
    ],
      status: 'done',
      listPosition: 1,
      priority: 55,
      userIds: [2],
      createdAt: '2023-06-29',
    },
    {
      id: 8,
      title: 'Issue 8',
      type: 'task',
      description: 'Description for Issue 2',
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29", body:"댓글 8"}  // 댓글 작성자
    ],
      status: 'done',
      listPosition: 2,
      priority: 35,
      userIds: [2],
      createdAt: '2023-06-29',
    },
  ],
};
 */

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Memo;
import kakao99.backend.memo.dto.MemoDTO;


import javax.xml.stream.events.Comment;
import java.util.Date;
import java.util.List;

public class IssueManagementDTO {


    private int id; // 이슈
    private String title;
    private String type;
    private String description;
    private List<MemoDTO> comments; // userNickname, profileImg

    /*
    {
      id: 7,
      title: 'Issue 7',
      type: 'task',
      description: 'Description for Issue 2',
      comments : [
        {user : {name:"park", avatarUrl:'avatar1url'}, createdAt:"2023-06-29",body:"댓글 7"}
    ]
    */



}
