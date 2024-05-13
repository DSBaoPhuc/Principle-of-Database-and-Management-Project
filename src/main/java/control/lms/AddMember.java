package control.lms;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class AddMember {

    public TextField addMemberName;
    public TextField addMemberGender;
    public TextField addMemberEmail;
    public TextField addMemberPhoneNumber;
    public TextField addMemberAddress;
    public Button saveBtn;
    public Button cleanBtn;

    Connection connection = null;

    public void save(MouseEvent event) throws SQLException {
        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        String name = addMemberName.getText();
        String gender = addMemberGender.getText();
        String email = addMemberEmail.getText();
        String phone = addMemberPhoneNumber.getText();
        String address = addMemberAddress.getText();

        if (name.equals("") || gender.equals("") || email.equals("") || phone.equals("") || address.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("PLEASE FILL ALL DATA");
            alert.showAndWait();
        } else if (!isValidEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email address");
            alert.showAndWait();
        } else {
            try {
                saveToDatabase(name, gender, email, phone, address);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Fail to save!");
                alert.showAndWait();
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    private void saveToDatabase(String name, String gender, String email, String phone, String address) throws SQLException {
        String query = "INSERT INTO Members (Librarian_ID, Fullname, Gender, Email, Phone, Address) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, 1);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, gender);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, phone);
        preparedStatement.setString(6, address);


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
            alert.setContentText("Failed to add Member");
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

    public void highlightCleanButton(MouseEvent event) {
        cleanBtn.setStyle("-fx-background-color: #0E49B5; -fx-background-radius: 20;");
    }

    public void resetCleanButtonColor(MouseEvent event) {
        cleanBtn.setStyle("-fx-background-color: #153E90; -fx-background-radius: 20;");
    }

    public void clean() {
        addMemberName.setText("");
        addMemberGender.setText("");
        addMemberEmail.setText("");
        addMemberPhoneNumber.setText("");
        addMemberAddress.setText("");
    }
}
