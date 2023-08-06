package kakao99.backend.issue.service;

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.IssueParentChild;
import kakao99.backend.entity.TreeNode;
import kakao99.backend.issue.dto.TreeDTO;
import kakao99.backend.issue.repository.IssueParentChildRepository;
import kakao99.backend.issue.repository.IssueRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class TreeService {

    private final IssueParentChildRepository issueParentChildRepository;

    private final IssueRepository issueRepository;


    private TreeNode buildTree(TreeDTO treeDTO, List<IssueParentChild> issueParentChildList) {
        TreeNode node = new TreeNode(treeDTO);


        for (IssueParentChild issueParentChild : issueParentChildList) {
            if (issueParentChild.getParentIssue().getId().equals(treeDTO.getId())) {
                if (issueParentChild.getIsActive()) {
                    Issue childIssue = issueParentChild.getChildIssue();
                    TreeDTO childIssueDTO = TreeDTO.fromIssueAndIsChild(childIssue); // convert Issue to IssueDTO
                    TreeNode childNode = buildTree(childIssueDTO, issueParentChildList);
                    node.addChild(childNode);
                }
            }
        }

        return node;

    }


    public List<TreeNode> getWholeTreeFromMidNode(Long midNodeId) {
        Issue midNodeIssue = issueRepository.findById(midNodeId)
                .orElseThrow(() -> new IllegalArgumentException("No issue with the id: " + midNodeId));

        // Get the highest parent node
        Issue currentIssue = midNodeIssue;
        while(true) {
            List<IssueParentChild> parentChildRelations = issueParentChildRepository.findByChildIssueAndIsActive(currentIssue,true);
            if(parentChildRelations.isEmpty()) {
                break;
            }
            currentIssue = parentChildRelations.get(0).getParentIssue();
        }

        // Now currentIssue should be the root of the tree. We build the tree from the root.
        List<IssueParentChild> issueParentChildList = issueParentChildRepository.findAll();
        List<TreeNode> allNodes = new ArrayList<>();
        TreeNode rootNode = buildTree(TreeDTO.fromIssueAndIsChild(currentIssue), issueParentChildList);
        traverseTreeAndFindRoot(rootNode, allNodes);

        return allNodes;
    }


    private void traverseTreeAndFindRoot(TreeNode node, List<TreeNode> allNodes) {
        allNodes.add(node);
        for (TreeNode child : node.getChildren()) {
            traverseTreeAndFindRoot(child, allNodes);
        }
    }

}