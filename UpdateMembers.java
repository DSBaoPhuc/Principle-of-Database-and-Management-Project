package control.lms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateMembers {
    public TextField updateMemberName;
    public TextField updateMemberGender;
    public TextField updateMemberEmail;
    public TextField updateMemberPhoneNumber;
    public TextField updateMemberAddress;
    public Button saveBtn;
    public Button cancelBtn;
    @FXML
    private TextField titleField;

    private Member member;

    // Method to initialize data
    public void initData(Member member) {
        // Store the book object
        this.member = member;
        // Populate the fields with the book's details
        updateMemberName.setText(member.getName());
        updateMemberGender.setText(member.getGender());
        updateMemberEmail.setText(member.getEmail());
        updateMemberPhoneNumber.setText(String.valueOf(member.getPhoneNumber()));
        updateMemberAddress.setText(String.valueOf(member.getAddress()));
    }

    // Method to handle the update button action
    @FXML
    private void updateMember() {
        // Get the updated values from the fields
        String newName = updateMemberName.getText();
        String newGender = updateMemberGender.getText();
        String newEmail = updateMemberEmail.getText();
        String newPhoneNumber = updateMemberEmail.getText();
        String newAddress = updateMemberEmail.getText();

        // Update the book object with the new values
        member.setName(newName);
        member.setGender(newGender);
        member.setEmail(newEmail);
        member.setPhoneNumber(newPhoneNumber);
        member.setAddress(newAddress);

        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        // Update the book details in the database

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String query = "UPDATE Books SET Title=?, Author=?, Genre=?, PublicationYear=?, CopiesAvailable=?, TotalCopies=? WHERE BookID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newGender);
            preparedStatement.setString(3,newEmail);
            preparedStatement.setString(4, newPhoneNumber);
            preparedStatement.setString(5, newAddress);
            preparedStatement.setInt(7, member.getMemberId());
            preparedStatement.executeUpdate();

            // Close the prepared statement
            preparedStatement.close();

            // Close the update book view
            closeUpdateMemberView();
        } catch (SQLException ex) {
            Logger.getLogger(ManageMembers.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Call a method from your ManageBook class to update the book in the database

        // Close the update book view
        closeUpdateMemberView();
    }

    public void handleSaveBtn(MouseEvent event) {
        updateMember();
    }

    public void handleCancelBtn(MouseEvent event) {
        closeUpdateMemberView();
    }

    // Method to close the update member view
    private void closeUpdateMemberView() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void highlightSaveButton(MouseEvent dragEvent) {
        saveBtn.setStyle("-fx-background-color: #0E49B5; -fx-background-radius: 20;");
    }

    public void resetSaveButtonColor(MouseEvent dragEvent) {
        saveBtn.setStyle("-fx-background-color: #153E90; -fx-background-radius: 20;");
    }

    public void highlightCancelButton(MouseEvent event) {
        cancelBtn.setStyle("-fx-background-color: #0E49B5; -fx-background-radius: 20;");
    }

    public void resetCancelButtonColor(MouseEvent event) {
        cancelBtn.setStyle("-fx-background-color: #153E90; -fx-background-radius: 20;");
    }
}
