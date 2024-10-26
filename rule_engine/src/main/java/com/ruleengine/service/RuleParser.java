package com.ruleengine.service;

import com.ruleengine.model.Node;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class RuleParser {

    public Node parseRule(String ruleString) {
        Stack<Node> stack = new Stack<>();
        String[] tokens = ruleString.split("(?=\\()|(?<=\\))|(?=AND)|(?=OR)|(?=>)|(?=<=)|(?>=)|(?!=)|(?==)|(?<=')|(?=')");

        for (String token : tokens) {
            token = token.trim();
            if (token.isEmpty()) continue; // Skip empty tokens

            if (token.equals("AND") || token.equals("OR")) {
                Node operatorNode = new Node(token);
                operatorNode.addChild(stack.pop()); // Add the right child
                operatorNode.addChild(stack.pop()); // Add the left child
                stack.push(operatorNode); // Push back the operator node
            } else {
                // Create a new condition node
                Node conditionNode = new Node(token);
                stack.push(conditionNode); // Push the condition to the stack
            }
        }
        return stack.pop();
    }
}
