import java.sql.SQLException;
import java.util.Scanner;

public class StudentRegistration {
    public void registration() throws SQLException {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Please enter your name: ");
        String name = userInput.nextLine();

        System.out.println("Please enter your student id (6 Digit): ");
        String studentID = userInput.nextLine();

        System.out.println("Please enter your email: ");
        String email = userInput.nextLine();

        System.out.println("Please enter your password: ");
        String password = userInput.nextLine();

        DatabaseHandle databaseHandle = new DatabaseHandle();
        databaseHandle.db_update("INSERT INTO student(name, student_id, email, password) VALUES (?, ?, ?, ?)", name, studentID, email, password);

    }
}
