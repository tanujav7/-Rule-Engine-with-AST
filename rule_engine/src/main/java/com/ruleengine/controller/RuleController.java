package com.ruleengine.controller;

import com.ruleengine.model.Node;
import com.ruleengine.model.Rule;
import com.ruleengine.service.RuleEvaluator;
import com.ruleengine.service.RuleParser;
import com.ruleengine.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleParser ruleParser;

    @Autowired
    private RuleEvaluator ruleEvaluator;

    @GetMapping("/myendpoint")
    public String myEndpoint() {
        return "Hello, World!";
    }
    
    @PostMapping("/create")
    public ResponseEntity<Rule> createRule(@RequestBody String ruleString) {
        System.out.println("Received rule string: " + ruleString);
        
        try {
            Node rootNode = ruleParser.parseRule(ruleString);
            String ruleAst = rootNode.toString(); 
            Rule savedRule = ruleService.saveRule("New Rule", ruleString, ruleAst);
            return ResponseEntity.ok(savedRule);
        } catch (Exception e) {
            System.out.println("Error creating rule: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace to the logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Rule>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Map<String, Object> userData) {
        Node ruleAst = ruleParser.parseRule("((age>30 AND department='Sales') OR (age<25 AND department='Marketing')) AND (salary > 50000 OR experience > 5)"); 
        boolean isEligible = ruleEvaluator.evaluate(ruleAst, userData);
        return ResponseEntity.ok(isEligible);
    }
}
