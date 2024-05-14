package control.lms;

import java.util.ResourceBundle;
import java.sql.*;
import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class ManageMembers implements Initializable {

    public TableView memberTable;
    public TableColumn memberIdCol;
    public TableColumn librarianIDCol;
    public TableColumn nameCol;
    public TableColumn genderCol;
    public TableColumn emailCol;
    public TableColumn phoneCol;
    public TableColumn addressCol;
    public ImageView addMember;
    public ImageView refreshTable;
    public ImageView removeMemberBtn;
    public ImageView updateMemberBtn;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Member member = null;

    ObservableList<Member> MemberList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDate() throws SQLException {
        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        refreshTable();

        memberIdCol.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        librarianIDCol.setCellValueFactory(new PropertyValueFactory<>("librarianId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    public void refreshTable() throws SQLException {
        try {
            MemberList.clear();

            query = "SELECT * FROM Members";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                MemberList.add(new Member(
                        resultSet.getInt("MemberID"),
                        resultSet.getInt("LibrarianID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Gender"),
                        resultSet.getString("Email"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("Address")));
                memberTable.setItems(MemberList);
            }

        } catch (SQLException ex){
            Logger.getLogger(ManageMembers.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    //coded
    public void getAddView(javafx.scene.input.MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("addMember.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Add Member");
            stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\add.png"));
            stage.setScene(scene);
//            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ManageMembers.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    //coded
    public void refreshTable(MouseEvent event) {
        try {
            MemberList.clear();

            query = "SELECT * FROM Members";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                MemberList.add(new Member(
                        resultSet.getInt("MemberID"),
                        resultSet.getInt("LibrarianID "),
                        resultSet.getString("Name"),
                        resultSet.getString("Gender"),
                        resultSet.getString("Email"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("Address")));
                memberTable.setItems(MemberList);
            }

        } catch (SQLException ex){
            Logger.getLogger(ManageMembers.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    // coded
    public void removeMember(MouseEvent event) {
        Member selectedMember = (Member) memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Remove Member");
            alert.setContentText("Are you sure you want to remove this member out of the table?");

            // Show the confirmation dialog and handle the user's response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Remove the member from the database
                        String query = "DELETE FROM Member WHERE MemberID = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, selectedMember.getMemberId());
                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        // Refresh the table to reflect the changes
                        refreshTable();
                    } catch (SQLException ex) {
                        Logger.getLogger(ManageMembers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else {
            // If no member is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot Find This Member");
            alert.setContentText("Please select a member to remove.");
            alert.showAndWait();
        }
    }
    //coded
    public void updateMember(MouseEvent event) {
        Member selectedMember = (Member) memberTable.getSelectionModel().getSelectedItem();

        if (selectedMember != null) {
            try {
                // Load the update book view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateMember.fxml"));
                Parent parent = loader.load();

                // Get the controller for the update book view
                UpdateMembers controller = loader.getController();

                // Pass the selected member's details to the controller
                controller.initData(selectedMember);

                // Create a new stage for the update member view
                Stage stage = new Stage();
                stage.setTitle("Update Member");
                stage.setScene(new Scene(parent));
                stage.showAndWait();

                // Refresh the table after the update operation
                refreshTable();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // If no member is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot Find This Member");
            alert.setContentText("Please select a member to update.");
            alert.showAndWait();
        }
    }
}
