package TypingPractice;
import TypingPractice.TypeFrame;
import javax.swing.*;
import java.io.*;
import java.util.Random;

public class TypingPractice  {
    public TypingPractice() {
        TypeFrame frame = new TypeFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new TypingPractice();
    }




}
