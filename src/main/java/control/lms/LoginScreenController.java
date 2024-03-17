package control.lms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;

public class LoginScreenController{
    Stage stage;
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        if(username.isEmpty() || password.isEmpty()){
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
                String dbPassword = resultSet.getString("password");

                // Compare passwords
                if (password.equals(dbPassword)) {
                    // Passwords match, login successful
                    if(username.equals("ITDSIU21109")){
                        username = "Phuc";
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Welcome");
                        alert.setHeaderText(null);
                        alert.setContentText("Welcome "+username);
                        alert.showAndWait();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) loginButton.getScene().getWindow(); // Get the current stage
                        stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\image.png"));
                        stage.setTitle("Home Screen");
                        stage.setScene(scene);
                        stage.show();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Welcome");
                        alert.setHeaderText(null);
                        alert.setContentText("Welcome "+username);
                        alert.showAndWait();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) loginButton.getScene().getWindow(); // Get the current stage
                        stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\image.png"));
                        stage.setTitle("Home Screen");
                        stage.setScene(scene);
                        stage.show();
                    }
                } else {
                    // Passwords don't match
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect, please check your Username or Password");
                    alert.showAndWait();
                }
            } else {
                // Username not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect, please check your Username or Password");
                alert.showAndWait();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgotPass.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\image.png"));
        stage.setScene(scene);
        stage.show();
    }
}