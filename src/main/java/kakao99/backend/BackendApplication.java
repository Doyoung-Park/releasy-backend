package kakao99.backend;

import kakao99.backend.issue.repository.IssueSearchRepository;
import kakao99.backend.issue.repository.MemberDocumentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableElasticsearchRepositories(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = IssueSearchRepository.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MemberDocumentRepository.class)
        }
)
public class BackendApplication {


    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}

