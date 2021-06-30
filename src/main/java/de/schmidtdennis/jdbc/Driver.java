package de.schmidtdennis.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Driver {

    private static String DB_URL = "jdbc:h2:~/github/h2/src/main/resources/db/h2DB";

    public void dropTables() {
        String dropTables = "DROP Table IF EXISTS StudentCourses;" +
                "DROP Table IF EXISTS Courses; " +
                "DROP Table IF EXISTS Teachers;" +
                "DROP Table IF EXISTS Students;";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(dropTables)){
            int amount = ps.executeUpdate();
            System.out.println(amount + " Tables dropped..");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createTables() {
        String createTeachers = "CREATE TABLE IF NOT EXISTS Teachers (" +
                "TeacherID int NOT NULL," +
                "TeacherName varchar(255)," +
                "PRIMARY KEY (TeacherID))";

        String createCourses = "CREATE TABLE IF NOT EXISTS Courses (" +
                "CourseID int NOT NULL," +
                "CourseName varchar(255)," +
                "TeacherID int," +
                "PRIMARY KEY (CourseID)," +
                "FOREIGN KEY (TeacherID) REFERENCES Teachers(TeacherID));";

        String createStudents = "CREATE TABLE IF NOT EXISTS Students (" +
                "StudentID int NOT NULL," +
                "StudentName varchar(255)," +
                "PRIMARY KEY (StudentID)" +
                ");";

        String createStudentCourses = "CREATE TABLE IF NOT EXISTS StudentCourses (" +
                "CourseID int NOT NULL," +
                "StudentID int NOT NULL," +
                "FOREIGN KEY (StudentID) REFERENCES Students(StudentID)," +
                "FOREIGN KEY (CourseID) REFERENCES Courses(CourseID)" +
                ");";

        PreparedStatement ps = null;
        try(Connection conn = DriverManager.getConnection(DB_URL)){

            ps = conn.prepareStatement(createTeachers);
            ps.executeUpdate();

            ps = conn.prepareStatement(createCourses);
            ps.executeUpdate();

            ps = conn.prepareStatement(createStudents);
            ps.executeUpdate();

            ps = conn.prepareStatement(createStudentCourses);
            ps.executeUpdate();

            System.out.println("Tables created...");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(ps != null){
                try {
                    ps.close();
                    System.out.println("PrepareStatement closed..");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }

    public void insertValues() {
        String insertIntoTeachers = "INSERT INTO Teachers (TeacherID, TeacherName)" +
                "VALUES (1, 'Jördens'); " +
                "INSERT INTO Teachers VALUES (2, 'Schmidt');" +
                "INSERT INTO Teachers VALUES (3, 'Schiesser');" +
                "INSERT INTO Teachers VALUES(4, 'An');";

        String insertIntoCourses = "INSERT INTO Courses VALUES (1, 'Algo', 2);" +
                "INSERT INTO Courses VALUES (2, 'Biology', 1);" +
                "INSERT INTO Courses VALUES (3, 'Software Engineering', 3);";

        String insertIntoStudents = "INSERT INTO Students " +
                "VALUES (1, 'Alina');" +
                "INSERT INTO Students VALUES (2, 'Tobias');" +
                "INSERT INTO Students VALUES (3, 'Johanna');" +
                "INSERT INTO Students VALUES (4, 'Kevin');" +
                "INSERT INTO Students VALUES (5, 'Elena');";

        String insertIntoStudentCourses = "INSERT INTO StudentCourses VALUES (1, 1);" +
                "INSERT INTO StudentCourses VALUES (1, 1);" +
                "INSERT INTO StudentCourses VALUES (1, 2);" +
                "INSERT INTO StudentCourses VALUES (1, 3);" +
                "INSERT INTO StudentCourses VALUES (1, 4);" +
                "INSERT INTO StudentCourses VALUES (2, 1);" +
                "INSERT INTO StudentCourses VALUES (2, 2);" +
                "INSERT INTO StudentCourses VALUES (2, 3);" +
                "INSERT INTO StudentCourses VALUES (3, 1);" +
                "INSERT INTO StudentCourses VALUES (3, 2);";

        PreparedStatement ps = null;
        try(Connection conn = DriverManager.getConnection(DB_URL)){

            ps = conn.prepareStatement(insertIntoTeachers);
            ps.executeUpdate();

            ps = conn.prepareStatement(insertIntoCourses);
            ps.executeUpdate();

            ps = conn.prepareStatement(insertIntoStudents);
            ps.executeUpdate();

            ps = conn.prepareStatement(insertIntoStudentCourses);
            ps.executeUpdate();

            System.out.println("All rows inserted...");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(ps != null){
                try{
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }



    public void getValues() {

        String dropTables = "SELECT * FROM Courses c INNER JOIN Teachers t ON c.TeacherID=t.TeacherID";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(dropTables)){
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                System.out.print("CourseID: " + resultSet.getInt("Courses.CourseID"));
                System.out.print("; CourseName: " + resultSet.getString("Courses.CourseName"));
                System.out.print("; TeacherName: " + resultSet.getString("Teachers.TeacherName"));
                System.out.println();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void getQuery1() {
        String select = "SELECT sc.StudentID, COUNT(sc.CourseID) AS amount, s.StudentName FROM StudentCourses sc RIGHT OUTER JOIN " +
                "Students" +
                " s ON" +
                " " +
                "s.StudentID=sc.StudentID " +
                "GROUP BY sc.StudentID " +
                "ORDER BY COUNT(sc.CourseID) DESC;";

        try(Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement ps = conn.prepareStatement(select)){
            ResultSet rs = ps.executeQuery();
            System.out.println();
            while(rs.next()){
                System.out.print(rs.getString("StudentName") + " has attended " + rs.getString("amount") + " classes.");
                System.out.println();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
