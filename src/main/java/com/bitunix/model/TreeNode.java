package com.bitunix.model;

import com.bitunix.util.MerkelTreeUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * TreeNode  update
 */
public class TreeNode {

    private String encryptUid;

    private String merkleLeaf;

    private String nonce;

    private Map<String, String> balances = new HashMap<>();

    private Integer height; // 高度

    private Integer type; // 类型：1为左节点，2为右节点，3为根节点

    Map<String, Integer> precisions = new HashMap<>(); // 币种余额精度

    /**
     * 验证self节点
     *
     * @return {@link boolean }
     */
    public boolean validateSelf() {
        String merkelNodeLeaf = MerkelTreeUtil.createMerkelNodeLeaf(this);
        return merkleLeaf.equals(merkelNodeLeaf) && StringUtils.isNotBlank(encryptUid) && StringUtils.isNotBlank(nonce)
                && MapUtils.isNotEmpty(balances) && MapUtils.isNotEmpty(precisions) && null != height;
    }

    /**
     * 验证Path节点数据
     *
     * @return {@link boolean }
     */
    public boolean validatePath() {
        return validateBalances() && StringUtils.isNotBlank(merkleLeaf) && type <= 3 && type >= 1 && null != height;
    }

    /**
     * 验证资产集合
     *
     * @return {@link boolean }
     */
    public boolean validateBalances() {
        if (MapUtils.isEmpty(balances)) {
            return false;
        }

        for (String decimal : balances.values()) {
            if (decimal == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * validateEqualsBalances
     *
     * @return {@link boolean }
     */
    public boolean validateEqualsBalances(TreeNode oldRoot) {
        Map<String, String> oldBalances = oldRoot.getBalances();
        for (Map.Entry<String, String> entry : balances.entrySet()) {
            if (new BigDecimal(oldBalances.get(entry.getKey())).compareTo(new BigDecimal(entry.getValue())) != 0) {
                return false;
            }
        }
        return true;
    }

    public String getEncryptUid() {
        return encryptUid;
    }

    public void setEncryptUid(String encryptUid) {
        this.encryptUid = encryptUid;
    }

    public String getMerkleLeaf() {
        return merkleLeaf;
    }

    public void setMerkleLeaf(String merkleLeaf) {
        this.merkleLeaf = merkleLeaf;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Map<String, String> getBalances() {
        return balances;
    }

    public void setBalances(Map<String, String> balances) {
        this.balances = balances;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, Integer> getPrecisions() {
        return precisions;
    }

    public void setPrecisions(Map<String, Integer> precisions) {
        this.precisions = precisions;
    }
}
