package kakao99.backend.group.service;


import jakarta.transaction.Transactional;
import kakao99.backend.entity.Group;
import kakao99.backend.group.dto.GroupDTO;
import kakao99.backend.group.dto.GroupNameDTO;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;


    public ResponseEntity<?> createGroup(GroupDTO groupDTO) {
        Group group = groupDTO.toEntity();
        groupRepository.save(group);
        ResponseMessage message = new ResponseMessage(200, "그룹 생성 완료");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateGroupName(GroupNameDTO groupNameDTO){
        try {
            Optional<Group> optionalGroup = groupRepository.findByCode(groupNameDTO.getCode());
            Group group = optionalGroup.get();
            group.updateName(groupNameDTO.getName());
        }
        catch(Exception e){
            System.out.println("코드에 맞는 그룹이 존재하지 않습니다.");
            ResponseMessage message = new ResponseMessage(500, "그룹 수정 실패");

            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseMessage message = new ResponseMessage(200, "그룹 수정 완료");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> removeGroup(GroupNameDTO groupNameDTO) {
        try {
            Optional<Group> optionalGroup = groupRepository.findByCode(groupNameDTO.getCode());
            Group group = optionalGroup.get();
            group.deleteGroup();
        }
        catch(Exception e){
            System.out.println("코드에 맞는 그룹이 존재하지 않습니다.");
            ResponseMessage message = new ResponseMessage(500, "프로젝트 삭제 실패");

            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseMessage message = new ResponseMessage(200, "그룹 삭제 완료");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
