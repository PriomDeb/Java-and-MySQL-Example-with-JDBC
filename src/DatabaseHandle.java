import java.sql.*;
import java.util.Scanner;

public class DatabaseHandle {
    Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/cse310_db", "root", "");


    public DatabaseHandle() throws SQLException {
    }


    public void db_update(String updateQuery, String name, String studentID, String email, String password){
        try{
            PreparedStatement preparedStatement = connect.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, studentID);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
            System.out.println("\nYour student account is successfully registered in the database.");
            System.out.println("-----------------------------------------------------------------------------");
            connect.close();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    public void student_login(String email, String password){
        if (email != null && password != null){
            try{
                String query = "SELECT name, student_id FROM student WHERE email=? AND password=?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                // Verify login
                if (resultSet.next()){
                    System.out.println("\n-----------------------------------------------------------------------------");
                    String studentName = resultSet.getString("name");
                    String studentId = resultSet.getString("student_id");
                    System.out.println("Hi, " + studentName + "(" + studentId + "). You have successfully logged in.");

                    // Check if the student already in any section
                    String sectionQuery = "SELECT section_students.section_id FROM section_students INNER JOIN student ON section_students.student_id = student.student_id WHERE student.email=?";
                    PreparedStatement preparedStatement1 = connect.prepareStatement(sectionQuery);
                    preparedStatement1.setString(1, email);
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    if (resultSet1.next()){
                        System.out.println("You have already entered section " + resultSet1.getString("section_students.section_id") + ".");

                    }
                    else {

                        Scanner menuOption = new Scanner(System.in);

                        // Enter any section
                        while (true){
                            this.showAvailableSeats();
                            System.out.println("\nPlease enter 1/2 to advise a section. Enter 0 to logout.");
                            int selected = menuOption.nextInt();

                            if (selected == 1){
                                System.out.println("\nYou are now being registered to Section-01.");

                                // Update database table section_students
                                if (this.enterSection(studentId, selected, studentName)){
                                    break;
                                }

                            }
                            else if (selected == 2){
                                System.out.println("\nYou are now being registered to Section-02.");

                                // Update database table section_students
                                if (this.enterSection(studentId, selected, studentName)){
                                    break;
                                }


                            }
                            else if (selected == 0){
                                System.out.println("\n-----------------------------------------------------------------------------");
                                System.out.println("You are being redirected to main menu.");
                                System.out.println("-----------------------------------------------------------------------------\n");
                                break;
                            }
                            else {
                                System.out.println("\nInvalid section number selected.\nPlease enter a valid section number.");
                                System.out.println("-----------------------------------------------------------------------------\n");
                            }
                        }
                    }
                    connect.close();




                } else {
                    System.out.println("No user found.");
                }

            }
            catch (Exception e){
                System.err.println(e);
            }
        }
        else {
            System.out.println("No email and password is entered.");
        }
    }

    public boolean enterSection(String studentId, int section, String name) throws SQLException {
        if (this.seatAvailable(section)){
            this.sectionRegistrationMessage(name, studentId, String.valueOf(section));
            String adviseQuery = "INSERT INTO section_students (student_id, section_id) VALUES (?, ?)";
            PreparedStatement preparedStatement3 = connect.prepareStatement(adviseQuery);
            preparedStatement3.setString(1, studentId);
            preparedStatement3.setInt(2, section);
            preparedStatement3.executeUpdate();

            String query = "UPDATE sections SET seats = seats - 1 WHERE id=?";
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, section);
            preparedStatement.executeUpdate();

            return true;


        } else {
            System.out.println("No seat available.");
            return false;
        }

    }

    public boolean seatAvailable(int section) throws SQLException {
        String query = "SELECT seats FROM sections WHERE id=?";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, section);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            int seat = resultSet.getInt("seats");
            if (seat <= 0){
                return false;
            }
        } else {
            return true;
        }

        return true;

    }

    public void sectionRegistrationMessage(String name, String studentId, String section){
        System.out.println("Registering to Section-" + section + " for " + name + "(" + studentId + ") is successfully completed.");
        System.out.println("Redirecting to homepage.");
    }


    public void facultyLogin(String email, String password) throws SQLException {
        String query = "SELECT name FROM faculty WHERE email=? AND password=?";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            System.out.println(resultSet.getString("name"));

            Scanner menuOption = new Scanner(System.in);
            while (true){
                System.out.println("\n-----------------------------------------------------------------------------");
                System.out.println("Faculty Dashboard");
                System.out.println("1. Section-01");
                System.out.println("2. Section-02");
                System.out.println("0. Logout");
                System.out.println("\nEnter 0/1/2: ");
                System.out.println("-----------------------------------------------------------------------------");

                int selected = menuOption.nextInt();
                if (selected == 1){
                    this.showSectionDetails("1");
                } else if (selected == 2) {
                    this.showSectionDetails("2");
                } else if (selected == 0) {
                    System.out.println("Redirecting to homepage.");
                    break;
                } else {
                    System.out.println("Invalid option selected.");
                }

            }

        } else {
            System.out.println("No user found.");
        }
    }

    public void showSectionDetails(String section) throws SQLException {
        System.out.println("Showing the information of Section-" + section);
        String query = "SELECT section.student_id, student.name FROM section_students section INNER JOIN student on section.student_id = student.student_id WHERE section.section_id=?";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, section);
        ResultSet resultSet = preparedStatement.executeQuery();

        Scanner control = new Scanner(System.in);

        System.out.println("    Name    SID");
        while (resultSet.next()){
            System.out.println(resultSet.getString("student.name") + " " + resultSet.getString("section.student_id"));
        }
    }


    // See available slots in section
    public void showAvailableSeats() throws SQLException {
        System.out.println("\nPlease select a section: ");
        String seeSectionQuery = "SELECT * FROM sections";
        PreparedStatement preparedStatement2 = connect.prepareStatement(seeSectionQuery);
        ResultSet resultSet2 = preparedStatement2.executeQuery(seeSectionQuery);

        while (resultSet2.next()){
            int id = resultSet2.getInt("id");
            String section = resultSet2.getString("section");
            String dateTime = resultSet2.getString("date_time");
            int seat = resultSet2.getInt("seats");

            System.out.println(id + " " + section + " " + dateTime + " " + seat + " Seats Remaining");
        }
    }

}
