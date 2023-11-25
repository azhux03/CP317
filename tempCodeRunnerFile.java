import java.util.HashMap;
import java.util.Map;

interface GradeCalculator {
    Map<String, Double> calculateFinalGrades(Map<String, Map<String, Double[]>> courseData);
}

class SimpleGradeCalculator implements GradeCalculator {
    @Override
    public Map<String, Double> calculateFinalGrades(Map<String, Map<String, Double[]>> courseData) {
        Map<String, Double> finalGrades = new HashMap<>();

        for (Map.Entry<String, Map<String, Double[]>> entry : courseData.entrySet()) {
            String studentId = entry.getKey();
            Map<String, Double[]> courses = entry.getValue();

            double total = 0;

            for (Double[] grades : courses.values()) {
                total += (grades[0] + grades[1] + grades[2]) * 0.2 + grades[3] * 0.4;
            }

            finalGrades.put(studentId, total); // Round to two decimal places
        }

        return finalGrades;
    }
}
