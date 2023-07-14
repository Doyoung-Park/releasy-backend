package kakao99.backend.group.controller;

import kakao99.backend.entity.Group;
import kakao99.backend.group.dto.GroupDTO;
import kakao99.backend.group.dto.GroupNameDTO;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/api/group")
    public ResponseEntity<?> createGroup(@RequestBody GroupDTO groupDTO) {

        return groupService.createGroup(groupDTO);
    }

    @PatchMapping("/api/newGroupName")
    public ResponseEntity<?> updateGroup(@RequestBody GroupNameDTO groupNameDTO) {

        return groupService.updateGroupName(groupNameDTO);
    }

    @DeleteMapping ("/api/group")
    public ResponseEntity<?> deleteGroup(@RequestBody GroupNameDTO groupNameDTO) {

        return groupService.removeGroup(groupNameDTO);
    }
}
