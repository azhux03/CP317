import java.io.IOException;
import java.util.Map;

public class GradeFormatter {
    public static void main(String[] args) {
        String nameFilePath = "NameFile.txt";
        String courseFilePath = "CourseFile.txt";
        String outputFilePath = "OutputFile.txt";

        GradeCalculator gradeCalculator = new SimpleGradeCalculator();
        FileProcessor fileProcessor = new FileProcessor(gradeCalculator);

        try {
            Map<String, String> nameData = fileProcessor.readNameFile(nameFilePath);
            Map<String, Map<String, Double[]>> courseData = fileProcessor.readCourseFile(courseFilePath);
            Map<String, Double> finalGrades = fileProcessor.getGradeCalculator().calculateFinalGrades(courseData);

            fileProcessor.writeOutputFile(nameData, finalGrades, courseData, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
