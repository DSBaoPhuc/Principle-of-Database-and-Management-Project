package control.lms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageMember implements Initializable {
    public TableView<Member> memberTable;
    public TableColumn<Member, String> memberIdCol;
    public TableColumn<Member, String> librarianIDCol;
    public TableColumn<Member, String> nameCol;
    public TableColumn<Member, String> genderCol;
    public TableColumn<Member, String> emailCol;
    public TableColumn<Member, String> phoneCol;
    public TableColumn<Member, String> addressCol;
    public ImageView addMember;
    public ImageView refreshTable;
    public ImageView removeMemberBtn;
    public ImageView updateMemberBtn;
    public TextField searchBar;
    public ImageView searchMember;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Member member = null;

    ObservableList<Member> memberList = FXCollections.observableArrayList();

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
        librarianIDCol.setCellValueFactory(new PropertyValueFactory<>("librarianID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    public void refreshTable() throws SQLException {
        try {
            memberList.clear();

            query = "SELECT * FROM Members";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                memberList.add(new Member(
                        resultSet.getInt("MemberID"),
                        resultSet.getInt("Librarian_ID"),
                        resultSet.getString("Fullname"),
                        resultSet.getString("Gender"),
                        resultSet.getString("Email"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Address")));
                memberTable.setItems(memberList);
            }

        } catch (SQLException ex){
            Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    public void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("addMember.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Add Member");
            stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\add_user.png"));
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public void removeMember(MouseEvent event) {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Remove Member");
            alert.setContentText("Are you sure you want to remove the selected member?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        String query = "DELETE FROM Members WHERE MemberID = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, selectedMember.getMemberId());
                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        refreshTable();
                    } catch (SQLException ex) {
                        Logger.getLogger(ManageMember.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Member Selected");
            alert.setContentText("Please select a member to remove.");
            alert.showAndWait();
        }
    }

    public void updateMember(MouseEvent event) {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateMember.fxml"));
                Parent parent = loader.load();

                UpdateMember controller = loader.getController();

                controller.initData(selectedMember);

                Stage stage = new Stage();
                stage.setTitle("Update Member");
                stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\edit.png"));
                stage.setScene(new Scene(parent));
                stage.showAndWait();

                refreshTable();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Member Selected");
            alert.setContentText("Please select a member to update.");
            alert.showAndWait();
        }
    }

    public void searchMember(MouseEvent event) {
        try {
            String keyword = searchBar.getText().trim();

            if (keyword.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a keyword to search.");
                alert.showAndWait();
                return;
            }

            memberList.clear();

            String query = "SELECT * FROM Members WHERE Fullname LIKE ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + keyword + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                memberList.add(new Member(
                        resultSet.getInt("MemberID"),
                        resultSet.getInt("Librarian_ID"),
                        resultSet.getString("Fullname"),
                        resultSet.getString("Gender"),
                        resultSet.getString("Email"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Address")));
            }

            if (memberList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("No member found with the given keyword.");
                alert.showAndWait();
            }

            memberTable.setItems(memberList);
        } catch (SQLException ex) {
            Logger.getLogger(ManageMember.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

