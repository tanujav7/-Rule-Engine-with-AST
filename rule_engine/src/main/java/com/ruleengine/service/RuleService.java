package com.ruleengine.service;

import com.ruleengine.model.Rule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {

    private List<Rule> rules = new ArrayList<>();

    public Rule saveRule(String name, String ruleString, String ruleAst) {
        // Create a new Rule object
        Rule rule = new Rule();
        rule.setName(name);
        rule.setRuleString(ruleString);
        rule.setRuleAst(ruleAst);
        
        rules.add(rule);
        return rule;
    }

    public List<Rule> getAllRules() {
        // Return all the saved rules
        return rules;
    }
}
