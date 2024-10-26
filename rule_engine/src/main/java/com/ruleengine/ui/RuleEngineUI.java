package com.ruleengine.ui;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RuleEngineUI extends JFrame {
    private JTextArea ruleTextArea;
    private JTable rulesTable;
    private JTextField dataField;
    private JLabel resultLabel;
    private DefaultTableModel tableModel;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RuleEngineUI() {
        setTitle("Rule Engine");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel ruleEntryPanel = new JPanel();
        ruleEntryPanel.setLayout(new BorderLayout());

        ruleTextArea = new JTextArea(3, 50);
        ruleEntryPanel.add(new JLabel("Enter Rule:"), BorderLayout.NORTH);
        ruleEntryPanel.add(new JScrollPane(ruleTextArea), BorderLayout.CENTER);

        JButton createRuleButton = new JButton("Create Rule");
        createRuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRule();
            }
        });
        ruleEntryPanel.add(createRuleButton, BorderLayout.SOUTH);

        add(ruleEntryPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Rule Name", "Rule Expression"};
        tableModel = new DefaultTableModel(columnNames, 0);
        rulesTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(rulesTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel evaluationPanel = new JPanel();
        evaluationPanel.setLayout(new GridLayout(2, 1));

        JPanel dataPanel = new JPanel();
        dataPanel.add(new JLabel("Enter Data for Evaluation (JSON):"));
        dataField = new JTextField(30);
        dataPanel.add(dataField);

        JButton evaluateButton = new JButton("Evaluate");
        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evaluateRule();
            }
        });
        dataPanel.add(evaluateButton);

        evaluationPanel.add(dataPanel);

        resultLabel = new JLabel("Result: ");
        evaluationPanel.add(resultLabel);

        add(evaluationPanel, BorderLayout.SOUTH);
    }

    private void createRule() {
        String ruleText = ruleTextArea.getText();
        if (ruleText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a rule.");
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/api/rules/create");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (Writer writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write("\"" + ruleText + "\"");  // Writing the rule as a JSON string
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                loadRules();  
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create rule.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadRules() {
        
    }

    private void evaluateRule() {
        String jsonData = dataField.getText();
        if (jsonData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter data for evaluation.");
            return;
        }

        try {
            Map<String, Object> userData = objectMapper.readValue(jsonData, Map.class);

            URL url = new URL("http://localhost:8080/api/rules/evaluate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (Writer writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(objectMapper.writeValueAsString(userData));
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                resultLabel.setText("Result: " + connection.getInputStream().toString());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to evaluate rule.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RuleEngineUI ui = new RuleEngineUI();
            ui.setVisible(true);
        });
    }
}
