package kakao99.backend.release.service;

import kakao99.backend.entity.NoteTreeNode;
import kakao99.backend.entity.ReleaseNote;
import kakao99.backend.entity.ReleaseNoteParentChild;
import kakao99.backend.entity.TreeNode;
import kakao99.backend.release.dto.NoteTreeDTO;
import kakao99.backend.release.repository.ReleaseParentChildRepository;
import kakao99.backend.release.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;

import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteTreeService {

    private final ReleaseParentChildRepository releaseParentChildRepository;

    private final ReleaseRepository releaseRepository;


    private NoteTreeNode buildTree(NoteTreeDTO treeDTO, List<ReleaseNoteParentChild> releaseNoteParentChildList) {
        NoteTreeNode node = new NoteTreeNode(treeDTO);


        for (ReleaseNoteParentChild releaseNoteParentChild : releaseNoteParentChildList) {
            if (releaseNoteParentChild.getParentNote().getId().equals(treeDTO.getId())) {
                if (releaseNoteParentChild.getIsActive()) {
                    ReleaseNote childNote = releaseNoteParentChild.getChildNote();
                    NoteTreeDTO childNoteDTO = NoteTreeDTO.fromNoteAndIsChild(childNote); // convert Issue to IssueDTO
                    NoteTreeNode childNode = buildTree(childNoteDTO, releaseNoteParentChildList);
                    node.addChild(childNode);
                }
            }
        }

        return node;

    }




    public List<NoteTreeNode> getTreesForProject(Long projectId) {
        // Fetch all root nodes for the given project
        List<ReleaseNote> rootNodes = releaseRepository.findRootNodesByProjectId(projectId);

        // Fetch the entire list of parent-child relationships
        List<ReleaseNoteParentChild> releaseNoteParentChildList = releaseParentChildRepository.findAll();

        // Create a list to store all the trees
        List<NoteTreeNode> trees = new ArrayList<>();

        // For each root node, build a tree and add it to the list
        for (ReleaseNote rootNode : rootNodes) {
            // Convert the root node to a NoteTreeDTO
            NoteTreeDTO rootNodeDTO = NoteTreeDTO.fromNoteAndIsChild(rootNode);

            // Build the tree from the root node
            trees.add(buildTree(rootNodeDTO, releaseNoteParentChildList));
        }

        return trees;
    }

}