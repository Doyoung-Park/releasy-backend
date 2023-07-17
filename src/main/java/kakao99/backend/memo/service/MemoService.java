package kakao99.backend.memo.service;

import kakao99.backend.memo.dto.MemoCreationResponseDTO;
import kakao99.backend.memo.dto.UpdateMemoDTO;
import kakao99.backend.memo.dto.memoDTO;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Memo;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;

    private final MemoRepository memoRepository;


    @Transactional
    public MemoCreationResponseDTO createMemo(Long memberId, Long issueId, String content, Date createAt){


        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Issue> issue = issueRepository.findById(issueId);


        //메모 생성
        Memo newmemo = Memo.createMemo(member.get(), issue.get(),content,createAt);
        memoRepository.save(newmemo);

        MemoCreationResponseDTO response = new MemoCreationResponseDTO();
        response.setMemoId(newmemo.getId());
        response.setMemberNickname(member.get().getNickname());

        return response;
    }

    public List<memoDTO>  getAllMemo(Long issueId){
        List<Memo> memos = memoRepository.findByIssueIdAndIsActiveTrue(issueId);

        List<memoDTO> MemoDTOList = new ArrayList<>();
        for (Memo memo : memos) {
            memoDTO memodto = memoDTO.MemoDTO(memo.getId()  ,memo.getIssue().getId(),memo.getMember().getNickname()
                    ,memo.getMemo_content() ,memo.getCreatedAt() ,memo.getUpdatedAt());

            MemoDTOList.add(memodto);
        }

        return MemoDTOList;
    }


    @Transactional
    public ResponseEntity<?> updateMemo(Long id, UpdateMemoDTO obj){

        try {
            Optional<Memo> findmemo = memoRepository.findById(id);
            Memo memo = findmemo.get();
            memo.updateMemo(obj.getContent(),obj.getUpdatedAt());
        }
        catch(Exception e){
            System.out.println("댓글 작성자가 아닙니다.");
        }

        return new ResponseEntity<>("프로젝트 수정", HttpStatus.OK);

    }


    public List<Memo> findRelease(Long id) {
        return memoRepository.findByIssueIdAndIsActiveTrue(id);
    }


    @Transactional
    public ResponseEntity<?> deleteMemo(Long id){

        try {
            Optional<Memo> findmemo = memoRepository.findById(id);
            Memo memo = findmemo.get();
            memo.deleteMemo();
        }
        catch(Exception e){
            System.out.println("작성된 댓글이 존재하지 않습니다.");
        }

        return new ResponseEntity<>("댓글 삭제", HttpStatus.OK);

    }
}

