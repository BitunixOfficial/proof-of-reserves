package com.bitunix;

import com.alibaba.fastjson.JSONObject;
import com.bitunix.model.MerkleTreePath;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 验证器
 *
 * @author bitunix
 */
public class BitunixMerkleValidator {

    private static final String MERKLE_TREE_PATH_FILE_NAME = "merkel_tree_path.json";

    public static void main(String[] args) {
        System.out.println("validation start.....");
        String merkleJsonFile = getMerkleJsonFile();
        if (StringUtils.isBlank(merkleJsonFile)) {
            System.out.println("Merkel tree path validation failed, empty merkle tree path file");
            return;
        }

        // 获得默克尔树验证路径
        MerkleTreePath merkleTreePath = JSONObject.parseObject(merkleJsonFile, MerkleTreePath.class);

        // 默克尔树参数验证
        if (!validate(merkleTreePath)) {
            System.out.println("merkle tree path parameter error");
            return;
        }

        // 验证根哈希是否一致
        if (merkleTreePath.validate()) {
            System.out.println("Consistent with the Merkle tree root hash. The verification succeeds");
        } else {
            System.out.println("Inconsistent with the Merkle tree root hash. The verification fails");
        }
    }

    /**
     * validate
     *
     * @param merkleTreePath
     * @return
     */
    private static boolean validate(MerkleTreePath merkleTreePath) {
        // self节点不能为空 并且path节点也不能为空
        if (merkleTreePath.getSelf() == null || CollectionUtils.isEmpty(merkleTreePath.getPath())) {
            return false;
        }

        // 验证self数据是否一致
        if (!merkleTreePath.getSelf().validateSelf()) {
            return false;
        }

        // 验证path参数验证
        if (!merkleTreePath.getPath().get(0).validatePath()) {
            return false;
        }

        return merkleTreePath.getPath().get(0).getType().intValue() != merkleTreePath.getSelf().getType().intValue();
    }


    private static String getMerkleJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            Files.readAllLines(Path.of(MERKLE_TREE_PATH_FILE_NAME), StandardCharsets.UTF_8).forEach(builder::append);
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(MERKLE_TREE_PATH_FILE_NAME + " file does not exist");
        }
    }
}


