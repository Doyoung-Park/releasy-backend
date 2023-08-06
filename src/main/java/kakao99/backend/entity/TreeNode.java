package kakao99.backend.entity;

import kakao99.backend.issue.dto.TreeDTO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TreeNode {
    private String name;
    private TreeDTO attributes;
    private List<TreeNode> children;


    public TreeNode(TreeDTO issue) {
        this.name = issue.getTitle();
        this.attributes = issue;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode childNode) {
        children.add(childNode);
    }

    // Getter methods

}
