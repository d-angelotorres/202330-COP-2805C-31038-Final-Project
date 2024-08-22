import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class FibonacciClient extends JFrame implements ActionListener {
    private JTextField numberTextField;
    private JLabel resultLabel;

    public FibonacciClient() {
        super("Fibonacci Client");

        numberTextField = new JTextField(10);
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(this);

        resultLabel = new JLabel("Result will be shown here");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter a positive number: "));
        panel.add(numberTextField);
        panel.add(calculateButton);
        panel.add(resultLabel);

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        try {
            int number = Integer.parseInt(numberTextField.getText());

            if (number < 0) {
                resultLabel.setText("Please enter a positive number.");
            } else {
                try {
                    int result = calculateFibonacci(number);
                    resultLabel.setText("Fibonacci(" + number + ") = " + result);
                } catch (IOException e) {
                    resultLabel.setText("Failed to connect to the server. Please try again later.");
                }
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please enter a valid positive number.");
        }
    }

    private int calculateFibonacci(int number) throws IOException {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            dos.writeInt(number);
            return dis.readInt();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FibonacciClient());
    }
}
