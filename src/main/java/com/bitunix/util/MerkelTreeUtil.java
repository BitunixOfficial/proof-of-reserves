package com.bitunix.util;

import com.alibaba.fastjson.JSONObject;
import com.bitunix.constants.MerkelTreeConstants;
import com.bitunix.model.TreeNode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MerkelTreeUtils
 */
public class MerkelTreeUtil {

    /**
     * 生成叶子节点hash
     *
     * @param treeNode
     */
    public static String createMerkelNodeLeaf(TreeNode treeNode) {
        String merkelHash = treeNode.getEncryptUid() +
                MerkelTreeConstants.COMMA +
                treeNode.getNonce() +
                MerkelTreeConstants.COMMA +
                JSONObject.toJSONString(treeNode.getBalances());
        return hashData(merkelHash);
    }

    /**
     * 生成父节点hash
     *
     * @param left
     * @param right
     * @param parent
     * @return {@link String }
     */
    public static String createMerkelParentLeaf(TreeNode left, TreeNode right, TreeNode parent) {
        String merkelHash = left.getMerkleLeaf() +
                right.getMerkleLeaf() +
                MerkelTreeConstants.COMMA +
                JSONObject.toJSONString(parent.getBalances()) +
                MerkelTreeConstants.COMMA +
                (parent.getHeight());
        return hashData(merkelHash);
    }

    /**
     * 将字符串进行hash
     *
     * @param str
     * @return
     */
    public static String hashData(String str) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(str.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
