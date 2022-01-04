package com.lee.mht.system.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/02 22:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode implements Serializable {
    private Integer id;
    private String label;
    private List<TreeNode> children;

    public TreeNode(Integer id, String label){
        this.id = id;
        this.label = label;
    }
}
