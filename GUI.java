import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GUI extends JFrame {

    private JTextField nameFilePathField;
    private JTextField courseFilePathField;
    private JTextField outputFilePathField;

    public GUI() {
        // Set up the frame
        setTitle("Student Information Formatter");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        nameFilePathField = new JTextField("NameFile.txt");
        courseFilePathField = new JTextField("CourseFile.txt");
        outputFilePathField = new JTextField("OutputFile.txt");
        JButton processButton = new JButton("Process");

        // Set layout
        setLayout(new BorderLayout());

        // Create panels for better organization
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Add file chooser buttons
        JButton chooseNameFileButton = new JButton("Choose Name File");
        chooseNameFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(nameFilePathField);
            }
        });

        JButton chooseCourseFileButton = new JButton("Choose Course File");
        chooseCourseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(courseFilePathField);
            }
        });

        // Add components to the input panel
        inputPanel.add(new JLabel("Name File Path:"));
        inputPanel.add(nameFilePathField);
        inputPanel.add(chooseNameFileButton);
        inputPanel.add(new JLabel("Course File Path:"));
        inputPanel.add(courseFilePathField);
        inputPanel.add(chooseCourseFileButton);
        inputPanel.add(new JLabel("Output File Name:"));
        inputPanel.add(outputFilePathField);

        // Add components to the button panel
        buttonPanel.add(processButton);

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener to the "Process" button
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFiles();
            }
        });
    }

    private void chooseFile(JTextField filePathField) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void processFiles() {
        String nameFilePath = nameFilePathField.getText();
        String courseFilePath = courseFilePathField.getText();
        String outputFilePath = outputFilePathField.getText();

        // Your file processing logic here
        GradeCalculator gradeCalculator = new SimpleGradeCalculator();
        FileProcessor fileProcessor = new FileProcessor(gradeCalculator);

        try {
            Map<String, String> nameData = fileProcessor.readNameFile(nameFilePath);
            Map<String, Map<String, Double[]>> courseData = fileProcessor.readCourseFile(courseFilePath);
            Map<String, Double> finalGrades = fileProcessor.getGradeCalculator().calculateFinalGrades(courseData);

            fileProcessor.writeOutputFile(nameData, finalGrades, courseData, outputFilePath);
            JOptionPane.showMessageDialog(this, "Files processed");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing files: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}
