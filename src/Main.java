import java.sql.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Main extends Application{

    enum MyColors{
        colorArray(Color.GREEN, Color.BLUE, Color.PURPLE,
                Color.AQUAMARINE, Color.BROWN, Color.CADETBLUE,
                Color.CHARTREUSE, Color.CHOCOLATE, Color.CORAL,
                Color.CORNFLOWERBLUE, Color.CRIMSON,
                Color.CYAN, Color.DARKBLUE,
                Color.RED, Color.DARKCYAN, Color.DARKGOLDENROD,
                Color.DARKGRAY, Color.DARKKHAKI, Color.DARKMAGENTA,
                Color.DARKOLIVEGREEN,
                Color.DARKORANGE, Color.DARKORCHID,
                Color.DARKRED, Color.DARKSALMON, Color.DARKSEAGREEN,
                Color.DARKSLATEBLUE, Color.DARKTURQUOISE);


        private Color[] array;

        private MyColors(Color... colorArray){
            this.array = colorArray;
        }


        public Color[] getColor(){

            return array;
        }


    }

    public Connection conn;
    private Canvas myCanvas;
    private static String totalGpa;



    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("Demonstration");

        BorderPane root = new BorderPane();


        Label nameLabel = new Label("Student Name: ");
        Label lastNameLabel = new Label("Student last name: ");
        Label sexLabel = new Label ("Student sex: ");
        Label studentIdLabel = new Label ("EMPLID: ");

        TextField nameTextField = new TextField("Name");
        TextField lastNameTextField = new TextField("lastName");
        TextField sexTextField = new TextField("sex");
        TextField studentIdTextField = new TextField("EMPLID");

        Button startButton = new Button("add student");
        Button drawButton = new Button("Draw chart");

        Label courseTitleLabel = new Label("Course title: ");
        Label departmentLabel = new Label("Department: ");
        Label courseIdLabel = new Label("Course ID: ");

        TextField courseTitleTextField = new TextField("Course Title");
        TextField departmentTextField = new TextField("Department");
        TextField courseIdTextField = new TextField("Course ID");

        Label yearLabel = new Label("Year: ");
        Label semesterLabel = new Label("Semester: ");
        Label gpaLabel = new Label("GPA: ");

        TextField yearTextField = new TextField("Year");
        TextField semesterTextField = new TextField("Semester");
        TextField gpaTextField  = new TextField("GPA");



        double width = 700;
        double height = 800;


        FlowPane studentPane = new FlowPane(nameLabel, nameTextField, lastNameLabel, lastNameTextField,
                sexLabel, sexTextField, studentIdLabel,studentIdTextField,courseTitleLabel,courseTitleTextField,departmentLabel,departmentTextField,
                courseIdLabel,courseIdTextField,yearLabel,yearTextField, semesterLabel, semesterTextField,
                gpaLabel, gpaTextField);
        FlowPane buttonPane = new FlowPane(startButton,drawButton);


        studentPane.setVgap(100);
        studentPane.setHgap(50);


        root.setTop(studentPane);
        root.setBottom(buttonPane);


        BorderPane.setMargin(studentPane, new Insets(20, 0, 0, 0));
        BorderPane.setMargin(buttonPane, new Insets(200,200,250,300));

        Scene myScene = new Scene(root, width, height);
        primaryStage.setScene(myScene);
        primaryStage.show();

        startButton.setOnAction(action -> {
            System.out.println("done");


            String nameInput = nameTextField.getText().trim();
            String lastNameInput = lastNameTextField.getText().trim();
            String sexInput = sexTextField.getText().trim();
            String studentIdInput = studentIdTextField.getText().trim();
            String courseTitleInput = courseTitleTextField.getText().trim();
            String departmentInput = departmentTextField.getText().trim();
            String courseIdInput = courseIdTextField.getText().trim();
            String yearInput = yearTextField.getText().trim();
            String semesterInput = semesterTextField.getText().trim();
            String gpaInput = gpaTextField.getText().trim();
            System.out.println(courseTitleInput);

            if (studentIdInput.matches("\\d+") && courseIdInput.matches("\\d+")
                    && semesterInput.matches("[a-zA-Z]+")
                    && nameInput.matches("[A-Za-z]+")
                    && lastNameInput.matches("[a-zA-Z]+")
                    && courseTitleInput.matches("[a-zA-Z]+")
                    && departmentInput.matches("[a-zA-Z]+")
                    && yearInput.matches("\\d+")
                    && gpaInput.matches("[A|B|C|D|E|F]") && sexInput.matches("[F|M|f|m]")){

                Main.insertInStudent(conn,nameInput, lastNameInput, sexInput,courseTitleInput,
                        departmentInput,Integer.parseInt(studentIdInput),Integer.parseInt(yearInput),
                        semesterInput, gpaInput, Integer.parseInt(courseIdInput));



            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING, "Check your input");
                alert.setHeaderText("Invalid input");
                alert.show();
            }


        });

        drawButton.setOnAction(action -> {

            PieChartGraphDisplay chart;

            String anotherGPA = "";

            try {
                conn = null;

                Class.forName("com.mysql.cj.jdbc.Driver");

                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mysql?useSSL=false", "root", "password");
                if(conn != null) {
                    anotherGPA = Main.showValues(conn);
                    System.out.println(anotherGPA);
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                ex.printStackTrace();
            } catch (Exception ex) {
                System.out.println("Exception " + ex.getMessage());
                ex.printStackTrace();
            }


            BorderPane root2 = new BorderPane();
            Scene myScene2 = new Scene(root2, width, height);
            Stage secondStage = new Stage();
            secondStage.setScene(myScene2);

            if(!anotherGPA.equals("")) {
                chart = new PieChartGraphDisplay(MyColors.colorArray.getColor(),anotherGPA);
            } else {

                chart = new PieChartGraphDisplay(MyColors.colorArray.getColor(),totalGpa);

            }

            root2.getChildren().remove(myCanvas);
            myCanvas = new Canvas(width, height);
            GraphicsContext gc = myCanvas.getGraphicsContext2D();
            root2.setCenter(myCanvas);

            System.out.println(anotherGPA+" first");


            if(!anotherGPA.equals("")){
                chart.draw(gc,anotherGPA,width,height);
                System.out.println(anotherGPA);
            } else {
                chart.draw(gc,totalGpa,width,height);
            }

            secondStage.show();


        });


    }

    public static void insertInStudent(Connection conn, String name, String last, String theSex, String title,
                                       String department, int studId, int year, String semester, String gpa,
                                       int cId) {

        String studentName = name;
        String studentLast = last;
        String studentSex = theSex;

        String courseTitle = title;
        String dept = department;
        int courId = cId;
        int sID = studId;
        int theYear = year;
        String currentSemester = semester;
        String studentGpa = gpa;

        int i = 0;

        try {
            conn  = null;

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mysql?useSSL=false", "root", "password");

            if (conn != null) {
                System.out.println("We have connected to our database");
                

                PreparedStatement create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Students " + "(StudentID INT UNSIGNED NOT NULL, " +
                        " PRIMARY KEY (StudentID), firstName varchar(255), " + "lastName varchar(255), sex varchar(255))");
                create.executeUpdate();

                PreparedStatement create3 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Courses" + "(courseID INT UNSIGNED NOT NULL, " +
                        " PRIMARY KEY (courseID), courseTitle varchar(255)," + "department varchar(255), uniqueID INT UNSIGNED NOT NULL)");
                create3.executeUpdate();

                Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                stmt.executeUpdate("INSERT  INTO Students " + "(StudentID,firstName,lastName,sex) " +
                        "VALUES ('"+sID+"','"+studentName+"', '"+studentLast+"','"+studentSex+"')");

                Statement getUniqueID = conn.createStatement();
                ResultSet rSet = getUniqueID.executeQuery("SELECT StudentID FROM Students");
                while(rSet.next()) {
                    i = rSet.getInt("StudentID");
                }
                i++;

                Statement stmt2 = conn.createStatement();
                stmt2.executeUpdate("INSERT INTO Courses" + "(courseID,courseTitle,department,uniqueID) " +
                        "VALUES ('"+courId+"','"+courseTitle+"', '"+dept+"','"+i+"')");

                PreparedStatement create2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Classes(classCode INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
                        "PRIMARY KEY (classCode),courseID INT UNSIGNED NOT NULL, StudentID INT UNSIGNED NOT NULL,  " +
                        "Year INT UNSIGNED NOT NULL, semester varchar(255), GPA CHAR(1)," +
                        "FOREIGN KEY(StudentID) REFERENCES Students(StudentID)," +
                        "FOREIGN KEY(courseID) REFERENCES Courses(courseID))");
                create2.executeUpdate();



                create2.executeUpdate("INSERT INTO Classes" +
                        "(courseID, StudentID, Year, semester, GPA ) " +
                        "VALUES ((SELECT courseID FROM Courses WHERE courseTitle = '"+courseTitle+"' && department = '"+dept+"' && uniqueID = '"+i+"')," +
                        "(SELECT StudentID FROM Students WHERE firstName = '"+studentName+"'),'"+theYear+"','"+currentSemester+"','"+studentGpa+"') ");
                totalGpa = Main.showValues(conn);

                conn.close();

            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public static String showValues(Connection conn) {

        String rs = "";

        try {

            Statement stmt = conn.createStatement();
            ResultSet rSet = stmt.executeQuery("SELECT GPA FROM Classes");
            rs = Main.showResult("Classes", rSet);
        } catch (SQLException ex) {
            System.out.println("SQLEXception: " + ex.getMessage());
            ex.printStackTrace();
        }

        return rs;
    }

    public static void showColumns(Connection conn) {

        try {
            Statement stmt = conn.createStatement();
            ResultSet rSet = stmt.executeQuery("SHOW COLUMNS FROM Classes");
            Main.showResult("Classes", rSet);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public static String showResult(String tableName, ResultSet rSet) {

        String resultString = "";

        try {

            ResultSetMetaData rsmd = rSet.getMetaData();
            int numColumns = rsmd.getColumnCount();

            while(rSet.next()) {

                for (int colNum = 1; colNum <= numColumns; colNum++) {
                    String column = rSet.getString(colNum);
                    if(column != null) {
                        resultString += column;
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }

        return resultString;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
