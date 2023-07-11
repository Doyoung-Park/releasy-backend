package kakao99.backend.project.repository;


import kakao99.backend.entity.Group;
import kakao99.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Project;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project save (Project project);

    Optional<Project> findById(Long id);


    List<Project> findAll();


    List<Project> findAllByGroupCode(String code);


//    Project findById(Long ProjectId);

//    @Query("select m from Project m join fetch m.group")
//    List<Project> findAllMemberInGroup();


    List<Project> findAllByGroupCodeAndIsActive(String code, String isActive);

}
