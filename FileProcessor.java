import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import java.util.TreeMap;

class FileProcessor {
    private final GradeCalculator gradeCalculator;

    public FileProcessor(GradeCalculator gradeCalculator) {
        this.gradeCalculator = gradeCalculator;
    }

    public GradeCalculator getGradeCalculator() {
        return gradeCalculator;
    }

    public Map<String, String> readNameFile(String filePath) throws IOException {
        Map<String, String> data = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String studentId = parts[0].trim();
                    String studentName = parts[1].trim();

                    if (isValidStudentId(studentId) && isValidStudentName(studentName)) {
                        data.put(studentId, studentName);
                    } else {
                        // Display a message dialog for incorrect format
                        showMessageDialog("Invalid format in the Name File: " + line);
                    }
                } else {
                    // Display a message dialog for incorrect format
                    showMessageDialog("Incorrect format in the Name File: " + line);
                }
            }
        }

        return data;
    }

    private boolean isValidStudentId(String studentId) {
        return studentId.matches("\\d{9}");
    }

    private boolean isValidStudentName(String studentName) {
        return studentName.matches("[A-Za-z ]+");
    }


    public Map<String, Map<String, Double[]>> readCourseFile(String filePath) throws IOException {
        Map<String, Map<String, Double[]>> data = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s?", -1);
                if (parts.length == 6) {
                    String studentId;
                    String courseCode;
                    if(!parts[0].matches("\\d{9}")){
                        studentId = "invalid student id " + parts[0];
                        showMessageDialog("Invalid student id: " + parts[0]);
                    }
                    else{
                        studentId = parts[0];
                    }
                    if(!parts[1].matches("^[A-Z]{2}\\d{3}$")){
                        courseCode = "invalid code " + parts[1];
                        showMessageDialog("Invalid code: " + parts[1]);
                    }
                    else{
                        courseCode = parts[1];
                    }
                    Double[] grades = new Double[]{
                        parseDoubleOrDefault(parts[2]),
                        parseDoubleOrDefault(parts[3]),
                        parseDoubleOrDefault(parts[4]),
                        parseDoubleOrDefault(parts[5].replace(",", "")) // Remove commas from the last part
                    };
                    String compositeKey = studentId + "-" + courseCode; // Combination:remove "-" + courseCode, and adjust writeoutputfile
                    if (data.containsKey(compositeKey)) {
                        data.get(compositeKey).put(courseCode, grades);
                    } else {
                        Map<String, Double[]> courses = new HashMap<>();
                        courses.put(courseCode, grades);
                        data.put(compositeKey, courses);
                    }
                } else {
                    // Display a message dialog for incorrect format
                    showMessageDialog("Incorrect format in the Course File: " + line);
                }
            }
        }

        return data;
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Format Error", JOptionPane.ERROR_MESSAGE);
    }

    private Double parseDoubleOrDefault(String value) {
    try {
        double parsedValue = Double.parseDouble(value);
        return Math.max(parsedValue, 0.0); // Set to 0 if the value is negative
    } catch (NumberFormatException e) {
        // Handle the case where the value cannot be parsed as a Double
        return 0.0; // Default the grade if it does not exist
    }
}

    public void writeOutputFile(Map<String, String> nameData, Map<String, Double> finalGrades,
                                Map<String, Map<String, Double[]>> courseData, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Use TreeMap to automatically sort by student ID
            // writer.write("Student ID              Student Name    Course Code    Final Grade (test 1,2,3-3x20%, final exam 40%)\n");
            Map<String, Double> sortedFinalGrades = new TreeMap<>(finalGrades);
            for (Map.Entry<String, Double> entry : sortedFinalGrades.entrySet()) {
                String compositeKey = entry.getKey();
                String studentId = compositeKey.split("-")[0];  // Extract student ID from composite key
                String courseCode = compositeKey.split("-", -1)[1]; // Extract course code from composite key
                String studentName = nameData.get(studentId);
                Double finalGrade = entry.getValue();

                // String outputLine = String.format("%9s %26s %14s %14.1f\n", studentId, studentName, courseCode, finalGrade);
                String outputLine = String.format("%s, %s, %s, %.1f\n", studentId, studentName, courseCode, finalGrade);
                writer.write(outputLine);
            }
        }
    }
}