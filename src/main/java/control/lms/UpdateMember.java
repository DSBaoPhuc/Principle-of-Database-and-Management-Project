package control.lms;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateMember {

    public TextField updateMemberName;
    public TextField updateMemberGender;
    public TextField updateMemberEmail;
    public TextField updateMemberPhone;
    public TextField updateMemberAddress;
    public Button saveBtn;
    public Button cancelBtn;
    private Member member;

    public void initData(Member member) {
        this.member = member;
        updateMemberName.setText(member.getName());
        updateMemberGender.setText(member.getGender());
        updateMemberEmail.setText(member.getEmail());
        updateMemberPhone.setText(String.valueOf(member.getPhoneNumber()));
        updateMemberAddress.setText(member.getAddress());
    }

    public void handleSaveBtn(MouseEvent event) {
        updateMember();
    }

    private void updateMember() {
        String newName = updateMemberName.getText();
        String newGender = updateMemberGender.getText();
        String newEmail = updateMemberEmail.getText();
        String newPhone = updateMemberPhone.getText();
        String newAddress = updateMemberAddress.getText();

        member.setName(newName);
        member.setGender(newGender);
        member.setEmail(newEmail);
        member.setPhoneNumber(newPhone);
        member.setAddress(newAddress);


        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String query = "UPDATE Members SET Librarian_ID=?, Fullname=?, Gender=?, Email=?, Phone=?, Address=? WHERE MemberID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, newName);
            preparedStatement.setString(3, newGender);
            preparedStatement.setString(4,newEmail);
            preparedStatement.setString(5, newPhone);
            preparedStatement.setString(6, newAddress);
            preparedStatement.setInt(7, member.getMemberId());
            preparedStatement.executeUpdate();

            preparedStatement.close();

            closeUpdateMemberView();
        } catch (SQLException ex) {
            Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeUpdateMemberView();
    }

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

    public void handleCancelBtn(MouseEvent event) {
        closeUpdateMemberView();
    }
}
