package kakao99.backend.entity;

import kakao99.backend.release.dto.NoteTreeDTO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NoteTreeNode {
    private String name;
    private NoteTreeDTO attributes;
    private List<NoteTreeNode> children;


    public NoteTreeNode(NoteTreeDTO note) {
        this.name = note.getVersion();
        this.attributes = note;
        this.children = new ArrayList<>();
    }

    public void addChild(NoteTreeNode childNode) {
        children.add(childNode);
    }

    // Getter methods

}
