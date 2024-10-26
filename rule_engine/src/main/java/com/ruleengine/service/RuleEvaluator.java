package com.ruleengine.service;

import java.util.List;
import java.util.Map;

import com.ruleengine.model.Node;

import org.springframework.stereotype.Service;

@Service
public class RuleEvaluator {

    public boolean evaluate(Node ast, Map<String, Object> userData) {
        return evaluateNode(ast, userData);
    }

    private boolean evaluateNode(Node node, Map<String, Object> userData) {
        String nodeType = node.getValue(); 


        if ("AND".equalsIgnoreCase(nodeType)) {
            for (Node child : node.getChildren()) {
                if (!evaluateNode(child, userData)) {
                    return false; 
                }
            }
            return true; 

        } else if ("OR".equalsIgnoreCase(nodeType)) {
            for (Node child : node.getChildren()) {
                if (evaluateNode(child, userData)) {
                    return true; 
                }
            }
            return false;

        } else if ("condition".equalsIgnoreCase(nodeType)) {
            return evaluateCondition(node, userData);
        } else {
            throw new IllegalArgumentException("Unsupported node type: " + nodeType);
        }
    }

    private boolean evaluateCondition(Node conditionNode, Map<String, Object> userData) {
        String field = conditionNode.getChildren().get(0).getValue(); 
        String operator = conditionNode.getChildren().get(1).getValue(); 
        Object value = conditionNode.getChildren().get(2).getValue(); 

        Object userValue = userData.get(field);
        if (userValue == null) {
            return false; 
        }

        // Perform comparison based on the operator
        switch (operator) {
            case ">":
                return compare((Comparable) userValue, (Comparable) value) > 0;
            case "<":
                return compare((Comparable) userValue, (Comparable) value) < 0;
            case "=":
            case "==":
                return userValue.equals(value);
            case "!=":
                return !userValue.equals(value);
            case ">=":
                return compare((Comparable) userValue, (Comparable) value) >= 0;
            case "<=":
                return compare((Comparable) userValue, (Comparable) value) <= 0;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    private int compare(Comparable a, Comparable b) {
        return a.compareTo(b);
    }
}
