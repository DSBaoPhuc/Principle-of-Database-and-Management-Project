package control.lms;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddRecord implements Initializable {

    public TextField addBookID;
    public TextField addMemberID;
    public TextField addBorrowDate;
    public TextField addReturned;
    public Button saveBtn;
    public Button cleanBtn;
    public TextField addFinedate;
    Connection connection = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void save(MouseEvent event) throws SQLException {
        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        String bookIdStr = addBookID.getText();
        String memberIdStr = addMemberID.getText();
        String borrowDateStr = addBorrowDate.getText();
        String fineDateStr = addFinedate.getText();
        String returnedStr = addReturned.getText();

        if(bookIdStr.equals("") || memberIdStr.equals("") || borrowDateStr.equals("") || fineDateStr.equals("") || returnedStr.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("PLEASE FILL ALL DATA");
            alert.showAndWait();
        }
        else {
            try {
                int bookID = Integer.parseInt(bookIdStr);
                int memberID = Integer.parseInt(memberIdStr);
                int returned = Integer.parseInt(returnedStr);
                LocalDate borrowDate = LocalDate.parse(borrowDateStr);
                LocalDate returnDate = LocalDate.parse(fineDateStr);
                saveToDatabase(bookID, memberID, borrowDate, returnDate, returned);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("INVALID Book ID, Member ID or Returned");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveToDatabase(int bookID, int memberID, LocalDate borrowDate, LocalDate fineDate, int returned) throws Exception {
        String checkCopiesQuery = "SELECT CopiesAvailable FROM Books WHERE BookID = ?";

        PreparedStatement checkCopiesStatement = connection.prepareStatement(checkCopiesQuery);
        checkCopiesStatement.setInt(1, bookID);
        ResultSet copiesResultSet = checkCopiesStatement.executeQuery();

        if (copiesResultSet.next()) {
            int copiesAvailable = copiesResultSet.getInt("CopiesAvailable");
            if (copiesAvailable == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("No copies available for this book.");
                alert.showAndWait();
                return;
            }
        }

        String borrowingQuery = "INSERT INTO Borrowings (BookID, MemberID, BorrowDate, Returned) VALUES (?, ?, ?, ?)";
        String finesQuery = "INSERT INTO Fines (BorrowingID, Amount, FineDate, Paid) VALUES (?, ?, ?, ?)";
        String updateCopiesQuery = "UPDATE Books SET CopiesAvailable = CopiesAvailable - 1 WHERE BookID = ?";

        PreparedStatement borrowingStatement = connection.prepareStatement(borrowingQuery, Statement.RETURN_GENERATED_KEYS);

        borrowingStatement.setInt(1, bookID);
        borrowingStatement.setInt(2, memberID);
        borrowingStatement.setDate(3, java.sql.Date.valueOf(borrowDate));
        borrowingStatement.setInt(4, returned);

        int borrowingRowsAffected = borrowingStatement.executeUpdate();

        if (borrowingRowsAffected > 0) {
            ResultSet generatedKeys = borrowingStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int borrowingID = generatedKeys.getInt(1);
                int amount = 0;
                int paid = 0;

                PreparedStatement finesStatement = connection.prepareStatement(finesQuery);
                finesStatement.setInt(1, borrowingID);
                finesStatement.setInt(2, amount);
                finesStatement.setDate(3, java.sql.Date.valueOf(fineDate));
                finesStatement.setInt(4, paid);
                int finesRowsAffected = finesStatement.executeUpdate();

                if (finesRowsAffected > 0) {
                    PreparedStatement updateCopiesStatement = connection.prepareStatement(updateCopiesQuery);
                    updateCopiesStatement.setInt(1, bookID);
                    updateCopiesStatement.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Added successfully");
                    alert.showAndWait();
                    clean();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add with fine information");
                    alert.showAndWait();
                }
                finesStatement.close();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to add");
            alert.showAndWait();
        }
        borrowingStatement.close();
    }


    public void clean() throws Exception {
        addBookID.setText("");
        addMemberID.setText("");
        addBorrowDate.setText("");
        addFinedate.setText("");
        addReturned.setText("");
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
}
