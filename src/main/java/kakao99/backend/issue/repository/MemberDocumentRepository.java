package kakao99.backend.issue.repository;

import kakao99.backend.document.IssueDocument;
import kakao99.backend.document.MemberDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberDocumentRepository extends ElasticsearchRepository<MemberDocument, Long> {

    Optional<MemberDocument> findMemberDocumentBy_id(Long id);

}
