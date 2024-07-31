package com.bitunix.model;

import com.bitunix.constants.MerkelTreeConstants;
import com.bitunix.util.MerkelTreeUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默克尔树
 */
public class MerKelTree {

    /**
     * build merkelTree root node
     *
     * @param path
     * @param self
     * @return {@link TreeNode }
     */
    public TreeNode buildMerkelTreeRoot(List<TreeNode> path, TreeNode self, Map<String, Integer> precisions) {
        if (path.size() <= 1) {
            throw new IllegalArgumentException("Must be at least two leafs to construct a Merkle tree");
        }

        TreeNode parent = createParentTreeNode(path.get(0), self, precisions);

        for (int i = 1; i < path.size() - 1; i++) {
            self = parent;
            parent = createParentTreeNode(path.get(i), self, precisions);
        }

        return parent;
    }

    /**
     * createParentTreeNode
     *
     * @param sibling
     * @param self
     * @return {@link TreeNode }
     */
    TreeNode createParentTreeNode(TreeNode sibling, TreeNode self, Map<String, Integer> precisions) {
        TreeNode parent;
        if (MerkelTreeConstants.LEFT_NODE.equals(sibling.getType())) {
            // sibling 是左节点
            parent = constructInternalNode(sibling, self, precisions);
        } else {
            // sibling 是右节点
            parent = constructInternalNode(self, sibling, precisions);
        }

        return parent;
    }

    public Map<String, String> getTotalBalances(Map<String, String> left, Map<String, String> right, Map<String, Integer> precisions) {
        Map<String, String> map = new HashMap<>();
        left.forEach((k, v) -> {
            BigDecimal add = new BigDecimal(v).add(new BigDecimal(right.get(k)));
            BigDecimal scaledBigDecimal = getScaledBigDecimal(add, precisions.get(k));
            map.put(k, scaledBigDecimal.toPlainString());
        });
        return map;
    }

    private BigDecimal getScaledBigDecimal(BigDecimal bigDecimal, int newScale) {
        return bigDecimal.setScale(newScale, RoundingMode.DOWN);
    }

    /**
     * constructInternalNode
     *
     * @param left
     * @param right
     * @return
     */
    private TreeNode constructInternalNode(TreeNode left, TreeNode right, Map<String, Integer> precisions) {
        TreeNode parent = new TreeNode();

        parent.setBalances(getTotalBalances(left.getBalances(), right.getBalances(), precisions));

        parent.setHeight(left.getHeight() - 1);
        parent.setMerkleLeaf(MerkelTreeUtil.createMerkelParentLeaf(left, right, parent));
        return parent;
    }

}
