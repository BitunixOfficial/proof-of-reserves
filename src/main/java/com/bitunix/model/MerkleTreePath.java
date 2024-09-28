package com.bitunix.model;

import java.util.List;

/**
 * MerkleTreePath
 */
public class MerkleTreePath {

    private List<TreeNode> path;
    private TreeNode self;

    public List<TreeNode> getPath() {
        return path;
    }

    public TreeNode getSelf() {
        return self;
    }

    public void setPath(List<TreeNode> path) {
        this.path = path;
    }

    public void setSelf(TreeNode self) {
        this.self = self;
    }

    /**
     * validate
     *
     * @return {@link boolean  }
     */
    public boolean validate() {
        TreeNode newRoot = new MerKelTree().buildMerkelTreeRoot(path, self, self.getPrecisions());
        TreeNode oldRoot = path.get(path.size() - 1);

        System.out.printf("Generated Root MerkelLeaf : %s, merkel_tree_path Root MerkelLeaf in file: %s%n", newRoot.getMerkleLeaf(), oldRoot.getMerkleLeaf());

        return newRoot.getMerkleLeaf().equals(oldRoot.getMerkleLeaf());
    }

}
