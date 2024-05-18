import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.net.URL;

import httpService.HttpService;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(App::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // HttpService should be implemented to handle HTTP requests
        HttpService httpService = HttpService.getHttpService();

        // Create the JFrame with proper size and default close operation
        JFrame frame = new JFrame("Simple Llama GUI");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Using BorderLayout for simplicity
        JPanel panel = new JPanel(new BorderLayout());

        // Text area setup
        JTextArea textArea = new JTextArea("Hello World");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        // panel.add(scrollPane, BorderLayout.CENTER);
        
        JTextArea returnArea = new JTextArea("Server Response");
        returnArea.setLineWrap(true);
        returnArea.setWrapStyleWord(true);
        JScrollPane returnScrollPane = new JScrollPane(returnArea);
        returnScrollPane.setSize(800, 200);
        
        // Set the size of the returnScrollPane to match the size of the scrollPane
        returnScrollPane.setPreferredSize(scrollPane.getPreferredSize());
        
        // panel.add(returnScrollPane, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, returnScrollPane, scrollPane);
        splitPane.setResizeWeight(0.5);
        panel.add(splitPane, BorderLayout.CENTER);

        
        // Dropdown menu setup
        String[] options = {"Generate", "Chat"};
        JComboBox<String> dropdown = new JComboBox<>(options);
        
        

        String[] chatOptions = {"More Creative","Balanced","More precise"};
        JComboBox<String> chatDropdown = new JComboBox<>(chatOptions);
        
        

        // JButton URLButton = new JButton("Use Non-Local Server");
        // URLButton.addActionListener(e -> {
        //     if (URLButton.getText().equals("Use Non-Local Server")) {

        //         httpService.setCustomURL(JOptionPane.showInputDialog("Enter the URL of the server"));
        //         URLButton.setText("Using Non-Local Server: " + httpService.getCustomURL());
        //         return;
                
        //     }
        //     else {
        //         httpService.useDefaultURL();
        //         URLButton.setText("Use Non-Local Server");
        //         return;
        //     }
        // });
        
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dropdown, chatDropdown);
        splitPane2.setResizeWeight(0.5);

        // panel.add(URLButton, BorderLayout.SOUTH);

        panel.add(splitPane2, BorderLayout.NORTH);

        // Button setup
        JButton button = new JButton("Send");
        button.addActionListener(e -> {
            HttpService hs = HttpService.getHttpService(dropdown.getSelectedItem().toString());
            hs.setModelTemperature(chatDropdown.getSelectedItem().toString());
            if (dropdown.getSelectedItem().toString().equals("Generate")) {
                String response = hs.getServerResponse(textArea.getText());
                SwingUtilities.invokeLater(() -> returnArea.setText(response));
                return;
            }
            else if (dropdown.getSelectedItem().toString().equals("Chat")) {
            String response = hs.getChatResponse(textArea.getText());
            SwingUtilities.invokeLater(() -> returnArea.setText(response));
            return;
            }
            else {
                JOptionPane.showMessageDialog(frame, "Invalid option selected");
                return;
            }
            });
        panel.add(button, BorderLayout.EAST);

        // Adding panel to frame and making frame visible
        frame.add(panel);
        frame.setVisible(true);
    }
}

