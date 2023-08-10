package kakao99.backend.release.repository;

import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.entity.ReleaseNote;
import kakao99.backend.release.dto.CreateReleaseDTO;

import java.util.ArrayList;

public interface ReleaseRepositoryCustom {

    ReleaseNote createReleaseNoteWithImage(CreateReleaseDTO createReleaseDTO, Member member, Project project, ArrayList<String> imgUrlList);
}
