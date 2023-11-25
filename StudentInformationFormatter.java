import java.io.IOException;
import java.util.Map;
import javax.swing.SwingUtilities;

public class StudentInformationFormatter {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}
