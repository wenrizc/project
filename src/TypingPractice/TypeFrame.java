package TypingPractice;

import java.awt.*;
import java.io.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class TypeFrame extends JFrame {
    private String sentence = "";
    private int tryCount = 0;
    private int correctCount = 0;
    private int time = 0;
    private JTextField inputField;
    private int length;
    private Timer timer;
    private JLabel remainingLengthLabel;
    private JLabel timeLabel;
    private JLabel countLabel;
    private JLabel correctLabel;
    private JLabel sentenceLabel;

    public TypeFrame() {
        setTitle("Typing Practice");
        setSize(1200, 500);
        setLayout(null);

        // 初始化按钮
        JButton button1 = new JButton("Start practice");
        button1.setBounds(100, 20, 200, 50);
        JButton button2 = new JButton("Stop practice");
        button2.setBounds(320, 20, 200, 50);

        // 初始化标签
        countLabel = new JLabel("Try Count: " + tryCount);
        correctLabel = new JLabel("Correct Count: " + correctCount);
        sentenceLabel = new JLabel("Sentence: " + sentence);
        timeLabel = new JLabel("Time: " + time);
        remainingLengthLabel = new JLabel("Remaining length: " + length);

        // 初始化输入框
        inputField = new JTextField();
        inputField.setBounds(100, 100, 1000, 50);
        inputField.setEnabled(false);
        
        // 添加输入监听器
        inputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleInputChange();
            }
            @Override
            public void removeUpdate(DocumentEvent e) { handleInputChange(); }
            @Override
            public void changedUpdate(DocumentEvent e) {
                handleInputChange();
            }
        });

        // 设置组件位置
        sentenceLabel.setBounds(100, 180, 1000, 50);
        timeLabel.setBounds(100, 240, 1000, 50);
        countLabel.setBounds(100, 300, 1000, 50);
        correctLabel.setBounds(100, 360, 1000, 50);
        remainingLengthLabel.setBounds(100, 420, 1000, 50);

        // 添加按钮事件
        button1.addActionListener(e -> startPractice());
        button2.addActionListener(e -> stopPractice());

        // 添加组件到窗口
        add(button1);
        add(button2);
        add(inputField);
        add(timeLabel);
        add(sentenceLabel);
        add(countLabel);
        add(correctLabel);
        add(remainingLengthLabel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void handleInputChange() {
        String userInput = getInputText();
        inputField.getHighlighter().removeAllHighlights();
        int currentLength = sentence.length();
        
        for (int i = 0; i < Math.min(userInput.length(), sentence.length()); i++) {
            if (userInput.charAt(i) != sentence.charAt(i)) {
                highlightCharacter(i);
                currentLength = sentence.length() - i;
                break;
            }
        }
        
        if (userInput.length() > sentence.length()) {
            highlightCharacter(sentence.length());
            currentLength = 0;
        }
        
        length = currentLength;
        remainingLengthLabel.setText("Remaining length: " + length);
    }

    private void startPractice() {
        sentence = sentence();
        length = sentence.length();
        clearInputField();
        inputField.setEnabled(true);
        time = 0;

        // 更新所有显示
        updateLabels();

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
                SwingUtilities.invokeLater(() -> timeLabel.setText("Time: " + time));
            }
        }, 1000, 1000);
    }

    private void updateLabels() {
        sentenceLabel.setText("Sentence: " + sentence);
        remainingLengthLabel.setText("Remaining length: " + length);
        countLabel.setText("Try Count: " + tryCount);
        correctLabel.setText("Correct Count: " + correctCount);
        timeLabel.setText("Time: " + time);
    }

    private void stopPractice() {
        if (timer != null) {
            timer.cancel();
        }
        check();
        inputField.setEnabled(false);
        updateLabels();
    }

    private String getInputText() {
        return inputField.getText();
    }

    private String sentence() {
        String sentence = "";
        try {
            // 使用相对路径或配置文件存储路径
            File file = new File("src/TypingPractice/莎士比亚全集英文版.txt");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "文本文件不存在！");
                return "Sample text for typing practice.";
            }
            
            Random random = new Random();
            int rand = random.nextInt(1000) + 100;
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                for (int i = 0; i < rand; i++) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                }
                String line = br.readLine();
                if (line != null) {
                    sentence = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Sample text for typing practice.";
        }
        return sentence.replaceAll("^[\\p{Punct}\\s]+|[\\p{Punct}\\s]+$", "");
    }

    private void highlightCharacter(int index) {
        Highlighter highlighter = inputField.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        try {
            highlighter.addHighlight(index, index + 1, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void clearInputField() {
        inputField.setText("");
        inputField.getHighlighter().removeAllHighlights();
    }

    private void check() {
        if (getInputText().equals(sentence)) {
            correctCount++;
            JOptionPane.showMessageDialog(this, "Correct! Time used: " + time + " seconds");
        } else {
            JOptionPane.showMessageDialog(this, "Wrong! Please try again.");
        }
        tryCount++;
    }

}