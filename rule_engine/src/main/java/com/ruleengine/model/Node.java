package com.ruleengine.model;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String value;  
    private List<Node> children;  

        public Node() {
        this.children = new ArrayList<>();
    }

    
    public Node(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    // Getter 
    public String getValue() {
        return value;
    }

    // Setter 
    public void setValue(String value) {
        this.value = value;
    }

    // Getter 
    public List<Node> getChildren() {
        return children;
    }

   
    public void addChild(Node child) {
        this.children.add(child);
    }

    
    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                ", children=" + children +
                '}';
    }
}
