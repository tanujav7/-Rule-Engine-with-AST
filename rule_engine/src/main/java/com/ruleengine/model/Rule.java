package com.ruleengine.model;

public class Rule {
    private String name;
    private String ruleString;
    private String ruleAst;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleString() {
        return ruleString;
    }

    public void setRuleString(String ruleString) {
        this.ruleString = ruleString;
    }

    public String getRuleAst() {
        return ruleAst;
    }

    public void setRuleAst(String ruleAst) {
        this.ruleAst = ruleAst;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", ruleString='" + ruleString + '\'' +
                ", ruleAst='" + ruleAst + '\'' +
                '}';
    }
}
