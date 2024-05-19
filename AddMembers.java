package control.lms;

import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
public class AddMembers implements Initializable {
    public TextField addMemberName;
    public TextField addMemberGender;
    public TextField addMemberEmail;
    public TextField addMemberPhoneNumber;
    public TextField addMemberAddress;
    public Button saveBtn;
    public Button cleanBtn;

    int memberID;
    private boolean update;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Member member = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void clean() throws SQLException {
        addMemberName.setText("");
        addMemberGender.setText("");
        addMemberEmail.setText("");
        addMemberPhoneNumber.setText("");
        addMemberAddress.setText("");
    }
    public void save(MouseEvent event) throws SQLException {
        //need to fixed this source to Database
        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        String name = addMemberName.getText();
        String gender = addMemberGender.getText();
        String email = addMemberEmail.getText();
        String phoneNumber = addMemberPhoneNumber.getText();
        String address = addMemberAddress.getText();
        if(name.equals("") || gender.equals("") || email.equals("") || address.equals("") || phoneNumber.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("PLEASE FILL ALL DATA FOR NEW MEMBER");
            alert.showAndWait();
        }
        else {
            try {
                saveToDatabase(name, gender, email, phoneNumber, address);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("INVALID QUANTITY OR STATUS");
                alert.showAndWait();
            }
        }
    }

    private void saveToDatabase(String name, String gender, String email, String phoneNumber, String address) throws SQLException {
        String query = "INSERT INTO Mebers (Name, Gender, Email, Phone Number, Address ) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, gender);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, phoneNumber);
        preparedStatement.setString(5, address);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Member added successfully");
            alert.showAndWait();
            clean();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to add member");
            alert.showAndWait();
        }
        preparedStatement.close();
    }

    public void highlightSaveButton(MouseEvent event) {
        saveBtn.setStyle("-fx-background-color: #0E49B5; -fx-background-radius: 20;");
    }

    public void resetSaveButtonColor(MouseEvent event) {
        saveBtn.setStyle("-fx-background-color: #153E90; -fx-background-radius: 20;");
    }
    //what is this function mean
    public void clean(MouseEvent event) {
    }

    public void highlightCleanButton(MouseEvent event) {
        cleanBtn.setStyle("-fx-background-color: #0E49B5; -fx-background-radius: 20;");
    }

    public void resetCleanButtonColor(MouseEvent event) {
        cleanBtn.setStyle("-fx-background-color: #153E90; -fx-background-radius: 20;");
    }
}
