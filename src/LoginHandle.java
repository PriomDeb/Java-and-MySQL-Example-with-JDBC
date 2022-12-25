import java.sql.SQLException;
import java.util.Scanner;

public class LoginHandle {

    public void login(boolean student, boolean faculty) throws SQLException {
        Scanner userInput = new Scanner(System.in);

        System.out.println("\n-----------------------------------------------------------------------------");
        System.out.println("Please enter your login credentials.\n");

        System.out.println("Please enter your email: ");
        String email = userInput.nextLine();

        System.out.println("Please enter your password: ");
        String password = userInput.nextLine();

        DatabaseHandle databaseHandle = new DatabaseHandle();

        if (student){
            databaseHandle.student_login(email, password);
        }
        else if (faculty){
            databaseHandle.facultyLogin(email, password);
        }

    }


}
