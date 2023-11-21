import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
                    data.put(parts[0], parts[1]);
                }
            }
        }

        return data;
    }

    public Map<String, Map<String, Double[]>> readCourseFile(String filePath) throws IOException {
        Map<String, Map<String, Double[]>> data = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    String studentId = parts[0];
                    String courseCode = parts[1];
                    Double[] grades = new Double[]{
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            Double.parseDouble(parts[4]),
                            Double.parseDouble(parts[5].replace(",", "")) // Remove commas from the last part
                    };
                    String compositeKey = studentId + "-" + courseCode; //for combination:remove "-" + courseCode, and adjust writeoutputfile
                    if (data.containsKey(compositeKey)) {
                        data.get(compositeKey).put(courseCode, grades);
                    } else {
                        Map<String, Double[]> courses = new HashMap<>();
                        courses.put(courseCode, grades);
                        data.put(compositeKey, courses);
                    }
                }
            }
        }

        return data;
    }

    public void writeOutputFile(Map<String, String> nameData, Map<String, Double> finalGrades,
                                Map<String, Map<String, Double[]>> courseData, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Student ID              Student Name    Course Code    Final Grade (test 1,2,3-3x20%, final exam 40%)\n");
            for (Map.Entry<String, Double> entry : finalGrades.entrySet()) {
                String compositeKey = entry.getKey();
                String studentId = compositeKey.split("-")[0];  // Extract student ID from composite key
                String courseCode = compositeKey.split("-")[1]; // Extract course code from composite key
                String studentName = nameData.get(studentId);
                Double finalGrade = entry.getValue();

                String outputLine = String.format("%9s %26s %14s %14.1f\n", studentId, studentName, courseCode, finalGrade);
                writer.write(outputLine);
            }
        }
    }
}
