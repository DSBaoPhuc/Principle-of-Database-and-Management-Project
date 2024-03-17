package control.lms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ForgotPassController {
    @FXML
    private Hyperlink backToLoginLink;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;

    Stage stage;

    @FXML
    private void handleForgotPass(ActionEvent event) {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        if(username.isEmpty() || email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter All Fields");
            alert.showAndWait();
            return;
        }

        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "123";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {

            String sql = "SELECT * FROM Login_Account WHERE username = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if username exists
            if (resultSet.next()) {
                // Retrieve password from database
                String dbemail = resultSet.getString("email");

                // Compare passwords
                if (email.equals(dbemail)) {
                    String dbPassword = resultSet.getString("password");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Forgot Password");
                    alert.setHeaderText(null);
                    alert.setContentText("Your Password is: "+dbPassword);
                    alert.setOnCloseRequest(e -> {
                        // Hide the Forgot Password window
                        ((Stage) usernameTextField.getScene().getWindow()).close();
                    });
                    alert.showAndWait();
                } else {
                    // Passwords don't match
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect, please check your Username or Email");
                    alert.showAndWait();
                }
            } else {
                // Username not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect, please check your Username or Email");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBackToLogin(ActionEvent event) {
        backToLoginLink.getScene().getWindow().hide();
    }
}
