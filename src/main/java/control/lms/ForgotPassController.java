package control.lms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;

public class ForgotPassController {
    @FXML
    private Hyperlink backToLoginLink;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;

    @FXML
    private Button OKbtn;

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

        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {

            String sql = "SELECT * FROM Login_Account WHERE username = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String dbemail = resultSet.getString("email");

                if (email.equals(dbemail)) {
                    String dbPassword = resultSet.getString("password");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Forgot Password");
                    alert.setHeaderText(null);
                    alert.setContentText("Your Password is: "+dbPassword);
                    alert.setOnCloseRequest(e -> {
                        ((Stage) usernameTextField.getScene().getWindow()).close();
                    });
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect, please check your Username or Email");
                    alert.showAndWait();
                }
            } else {
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

    public void highlightButton(MouseEvent event){
        OKbtn.setStyle("-fx-background-color: #0E49B5; -fx-background-radius: 20;");
    }
    @FXML
    private void resetButtonColor(MouseEvent event){
        OKbtn.setStyle("-fx-background-color: #153E90; -fx-background-radius: 20;");
    }
}
