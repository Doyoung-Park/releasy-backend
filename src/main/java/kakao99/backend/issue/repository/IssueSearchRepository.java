package kakao99.backend.issue.repository;

import kakao99.backend.document.IssueDocument;
import kakao99.backend.document.MemberDocument;
import kakao99.backend.entity.Issue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueSearchRepository extends ElasticsearchRepository<IssueDocument, Long>{

    //IssueDocument save(IssueDocument document);
    List<IssueDocument> findIssueDocumentByTitleContaining(String title);
    //List<MemberDocument> findMemberDocumentByProjectId(Long id);

}
