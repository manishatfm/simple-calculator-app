import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Calculator {
    boolean isNewInput = true;
    int boardwidth = 360;
    int borderHeight = 540;
    Color customLightGray = new Color(210, 210, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);

    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    String[] buttonValues = {
        "AC", "+/-", "%", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "√", "="
    };

    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    String A = null;
    String operator = null;
    String B = null;

    Calculator() {
        frame.setSize(boardwidth, borderHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonPanel.setLayout(new GridLayout(5, 4));
        buttonPanel.setBackground(customBlack);
        frame.add(buttonPanel);

        for (String buttonValue : buttonValues) {
            JButton button = new JButton(buttonValue);
            button.setFont(new Font("Arial", Font.PLAIN, 28));
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack, 1));

            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            } else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }

            buttonPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String buttonValue = button.getText();

                    // --- RIGHT SYMBOLS ---
                    if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("=")) {
                            if (A != null && operator != null && !isNewInput) {
                                B = displayLabel.getText();
                                calculateResult();
                                operator = null;
                                B = null;
                                isNewInput = true;
                            }
                        } else { // + - × ÷
                            if (A == null) {
                                A = displayLabel.getText();
                            } else if (!isNewInput) {
                                B = displayLabel.getText();
                                calculateResult();
                            }
                            operator = buttonValue;
                            isNewInput = true;
                        }
                    }

                    // --- TOP SYMBOLS ---
                    else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("AC")) {
                            clearAll();
                            displayLabel.setText("0");
                        } else if (buttonValue.equals("+/-")) {
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay *= -1;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        } else if (buttonValue.equals("%")) {
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay /= 100;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        }
                    }

                    // --- DIGITS & DECIMAL & √ ---
                    else {
                        if (buttonValue.equals(".")) {
                            if (isNewInput) {
                                displayLabel.setText("0.");
                                isNewInput = false;
                            } else if (!displayLabel.getText().contains(".")) {
                                displayLabel.setText(displayLabel.getText() + ".");
                            }
                        } else if ("0123456789".contains(buttonValue)) {
                            if (isNewInput || displayLabel.getText().equals("0")) {
                                displayLabel.setText(buttonValue);
                                isNewInput = false;
                            } else {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        } else if (buttonValue.equals("√")) {
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            if (numDisplay < 0) {
                                displayLabel.setText("Error");
                            } else {
                                displayLabel.setText(removeZeroDecimal(Math.sqrt(numDisplay)));
                            }
                            isNewInput = true;
                        }
                    }
                }
            });
        }

        frame.setVisible(true);
    }

    void calculateResult() {
        double numA, numB;
        try {
            numA = Double.parseDouble(A);
            numB = Double.parseDouble(B);
        } catch (Exception e) {
            displayLabel.setText("Error");
            A = null;
            operator = null;
            return;
        }

        switch (operator) {
            case "+":
                A = removeZeroDecimal(numA + numB);
                break;
            case "-":
                A = removeZeroDecimal(numA - numB);
                break;
            case "×":
                A = removeZeroDecimal(numA * numB);
                break;
            case "÷":
                if (numB == 0) {
                    A = "Error";
                } else {
                    A = removeZeroDecimal(numA / numB);
                }
                break;
        }
        displayLabel.setText(A);
        isNewInput = true;
    }

    void clearAll() {
        A = null;
        operator = null;
        B = null;
        isNewInput = true;
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
