package control.lms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewRecord implements Initializable {

    public TableView <BorrowAndFine> recordTable;
    public TableColumn <BorrowAndFine, String> bookIdCol;
    public TableColumn <BorrowAndFine, String> memberIdCol;
    public TableColumn <BorrowAndFine, String> borrowDateCol;
    public TableColumn <BorrowAndFine, String> returnDateCol;
    public TableColumn <BorrowAndFine, String> returnedCol;
    public TableColumn <BorrowAndFine, String> amountCol;
    public TableColumn <BorrowAndFine, String> fineDateCol;
    public TableColumn <BorrowAndFine, String> paidCol;
    public ImageView borrowBtn;
    public ImageView returnBtn;
    public TableColumn <BorrowAndFine, String> memberNameCol;
    public TableColumn <BorrowAndFine, String> borrowingIdCol;
    public ImageView refreshTableBtn;
    public ImageView checkFineBtn;
    public ImageView updateBorrowBtn;
    public TextField searchBar;
    public ImageView searchName;
    public ImageView updateRecordBtn;
    public ImageView statisticBtn;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ObservableList<BorrowAndFine> BorrowAndFineList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void load() throws SQLException{
        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        refreshTable();

        borrowingIdCol.setCellValueFactory(new PropertyValueFactory<>("borrowingID"));
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        memberIdCol.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        memberNameCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnedCol.setCellValueFactory(new PropertyValueFactory<>("returned"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        fineDateCol.setCellValueFactory(new PropertyValueFactory<>("fineDate"));
        paidCol.setCellValueFactory(new PropertyValueFactory<>("paid"));
    }

    @FXML
    private void refreshTable() {
        try {
            BorrowAndFineList.clear();

            query = "SELECT B.BorrowingID, B.BookID, B.MemberID, B.BorrowDate, B.ReturnDate, B.Returned, " +
                    "F.Amount, F.FineDate, F.Paid, M.FullName " +
                    "FROM Borrowings B " +
                    "JOIN Fines F ON B.BorrowingID = F.BorrowingID " +
                    "JOIN Members M ON B.MemberID = M.MemberID";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                LocalDate fineDate = resultSet.getDate("FineDate").toLocalDate();

                double fineAmount = 0.0;
                if (LocalDate.now().isAfter(fineDate)) {
                    long daysBetween = ChronoUnit.DAYS.between(fineDate, LocalDate.now());
                    fineAmount = daysBetween * 5.0;
                }

                String updateFinesAmountQuery = "UPDATE Fines SET Amount = ? WHERE BorrowingID = ?";
                PreparedStatement updateFinesAmountStatement = connection.prepareStatement(updateFinesAmountQuery);
                updateFinesAmountStatement.setDouble(1, fineAmount);
                updateFinesAmountStatement.setInt(2, resultSet.getInt("BorrowingID"));
                updateFinesAmountStatement.executeUpdate();

                BorrowAndFineList.add(new BorrowAndFine(
                        resultSet.getInt("BorrowingID"),
                        resultSet.getInt("BookID"),
                        resultSet.getInt("MemberID"),
                        resultSet.getString("FullName"),
                        resultSet.getString("BorrowDate"),
                        resultSet.getString("ReturnDate"),
                        resultSet.getInt("Returned"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("FineDate"),
                        resultSet.getInt("Paid")
                ));
                recordTable.setItems(BorrowAndFineList);
            }

        } catch (SQLException ex){
            Logger.getLogger(ViewRecord.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    public void getBorrowView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("addRecord.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Add Record");
            stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\borrow.png"));
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ViewRecord.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public void returnBook(MouseEvent event) {
        BorrowAndFine selectedRecord = recordTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            try {
                String updateBorrowingsQuery = "UPDATE Borrowings SET Returned = 1, ReturnDate = ? WHERE BorrowingID = ?";
                PreparedStatement updateBorrowingsStatement = connection.prepareStatement(updateBorrowingsQuery);
                updateBorrowingsStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                updateBorrowingsStatement.setInt(2, selectedRecord.getBorrowingID());
                updateBorrowingsStatement.executeUpdate();

                String checkCopiesQuery = "SELECT CopiesAvailable, TotalCopies FROM Books WHERE BookID = ?";
                PreparedStatement checkCopiesStatement = connection.prepareStatement(checkCopiesQuery);
                checkCopiesStatement.setInt(1, selectedRecord.getBookID());
                ResultSet copiesResultSet = checkCopiesStatement.executeQuery();

                if (copiesResultSet.next()) {
                    int copiesAvailable = copiesResultSet.getInt("CopiesAvailable");
                    int totalCopies = copiesResultSet.getInt("TotalCopies");
                    if (copiesAvailable + 1 > totalCopies) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("The number of available copies will exceed the total number of copies for this book.");
                        alert.showAndWait();
                        return;
                    }
                }

                String updateCopiesAvailableQuery = "UPDATE Books SET CopiesAvailable = CopiesAvailable + 1 WHERE BookID = ?";
                PreparedStatement updateCopiesAvailableStatement = connection.prepareStatement(updateCopiesAvailableQuery);
                updateCopiesAvailableStatement.setInt(1, selectedRecord.getBookID());
                updateCopiesAvailableStatement.executeUpdate();

                String updateFinesQuery = "UPDATE Fines SET Paid = 1 WHERE BorrowingID = ?";
                PreparedStatement updateFinesStatement = connection.prepareStatement(updateFinesQuery);
                updateFinesStatement.setInt(1, selectedRecord.getBorrowingID());
                updateFinesStatement.executeUpdate();

                refreshTable();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Book returned successfully");
                alert.showAndWait();
            } catch (SQLException ex) {
                Logger.getLogger(ViewRecord.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a record to return.");
            alert.showAndWait();
        }
    }

    public void searchName(MouseEvent event) {
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

            BorrowAndFineList.clear();

            String query = "SELECT B.BorrowingID, B.BookID, B.MemberID, B.BorrowDate, B.ReturnDate, B.Returned, " +
                    "F.Amount, F.FineDate, F.Paid, M.FullName " +
                    "FROM Borrowings B " +
                    "JOIN Fines F ON B.BorrowingID = F.BorrowingID " +
                    "JOIN Members M ON B.MemberID = M.MemberID " +
                    "WHERE M.FullName LIKE ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + keyword + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BorrowAndFineList.add(new BorrowAndFine(
                        resultSet.getInt("BorrowingID"),
                        resultSet.getInt("BookID"),
                        resultSet.getInt("MemberID"),
                        resultSet.getString("FullName"),
                        resultSet.getString("BorrowDate"),
                        resultSet.getString("ReturnDate"),
                        resultSet.getInt("Returned"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("FineDate"),
                        resultSet.getInt("Paid")
                ));
            }

            if (BorrowAndFineList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("No record found with the given keyword.");
                alert.showAndWait();
            }

            recordTable.setItems(BorrowAndFineList);
        } catch (SQLException ex) {
            Logger.getLogger(ViewRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showStatistic(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("borrowedStatistic.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Monthly Borrowed Books Statistics");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\stats.png"));

            // Get the controller associated with the FXML
            StatisticController statisticController = loader.getController();

            // Fetch initial data for the chart (books borrowed by default)
            ResultSet resultSet = fetchData(true);
            statisticController.populateChart(resultSet, true);

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ViewRecord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private ResultSet generateStatisticData() throws SQLException {
//        String query = "SELECT MONTH(BorrowDate) AS Month, YEAR(BorrowDate) AS Year, COUNT(*) AS TotalBooks " +
//                "FROM Borrowings " +
//                "GROUP BY MONTH(BorrowDate), YEAR(BorrowDate) " +
//                "ORDER BY YEAR(BorrowDate), MONTH(BorrowDate)";
//
//        PreparedStatement preparedStatement = connection.prepareStatement(query);
//        return preparedStatement.executeQuery();
//    }

    private ResultSet fetchData(boolean isBooksBorrowed) throws SQLException {
        String query;
        if (isBooksBorrowed) {
            query = "SELECT MONTH(BorrowDate) AS Month, YEAR(BorrowDate) AS Year, COUNT(*) AS TotalBooks " +
                    "FROM Borrowings " +
                    "GROUP BY MONTH(BorrowDate), YEAR(BorrowDate) " +
                    "ORDER BY YEAR(BorrowDate), MONTH(BorrowDate)";
        } else {
            query = "SELECT MONTH(FineDate) AS Month, YEAR(FineDate) AS Year, SUM(Amount) AS TotalAmount " +
                    "FROM Fines " +
                    "GROUP BY MONTH(FineDate), YEAR(FineDate) " +
                    "ORDER BY YEAR(FineDate), MONTH(FineDate)";
        }
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

}
