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

public class ManageBook implements Initializable {
    public TableView <Book> bookTable;
    public TableColumn <Book, String> idCol;
    public TableColumn <Book, String> titleCol;
    public TableColumn <Book, String> authorCol;
    public TableColumn <Book, String> genreCol;
    public TableColumn <Book, String> PublicationYear;
    public TableColumn <Book, String> CopyAvailCol;
    public TableColumn <Book, String> TotalCopyCol;
    public ImageView addBook;
    public ImageView refreshTable;
    public ImageView removeBookBtn;
    public ImageView updateBookBtn1;
    public TextField searchBar;
    public ImageView searchBook;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Book book = null;

    ObservableList<Book> BookList = FXCollections.observableArrayList();

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

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        PublicationYear.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        CopyAvailCol.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));
        TotalCopyCol.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
    }

    public void refreshTable() throws SQLException {
        try {
            BookList.clear();

            query = "SELECT * FROM Books";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                BookList.add(new Book(
                        resultSet.getInt("BookID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getString("Genre"),
                        resultSet.getInt("PublicationYear"),
                        resultSet.getInt("CopiesAvailable"),
                        resultSet.getInt("TotalCopies")));
                bookTable.setItems(BookList);
            }

        } catch (SQLException ex){
            Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE,null,ex);
        }
    }


    public void getAddView(javafx.scene.input.MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("addBook.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Add Book");
            stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\add.png"));
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public void removeBook(MouseEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Remove Book");
            alert.setContentText("Are you sure you want to remove the selected book?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        String query = "DELETE FROM Books WHERE BookID = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, selectedBook.getId());
                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        refreshTable();
                    } catch (SQLException ex) {
                        Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book to remove.");
            alert.showAndWait();
        }
    }

    public void updateBook(MouseEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateBook.fxml"));
                Parent parent = loader.load();

                UpdateBook controller = loader.getController();

                controller.initData(selectedBook);

                Stage stage = new Stage();
                stage.setTitle("Update Book");
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
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book to update.");
            alert.showAndWait();
        }
    }

    public void searchBook(MouseEvent event) {
        String keyword = searchBar.getText().trim();

        if (!keyword.isEmpty()) {
            try {
                BookList.clear();

                String query = "SELECT * FROM Books WHERE Title LIKE ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "%" + keyword + "%");
                resultSet = preparedStatement.executeQuery();

                if (!resultSet.isBeforeFirst()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("No books found with the provided keyword.");
                    alert.showAndWait();
                } else {
                    while (resultSet.next()) {
                        BookList.add(new Book(
                                resultSet.getInt("BookID"),
                                resultSet.getString("Title"),
                                resultSet.getString("Author"),
                                resultSet.getString("Genre"),
                                resultSet.getInt("PublicationYear"),
                                resultSet.getInt("CopiesAvailable"),
                                resultSet.getInt("TotalCopies")));
                    }
                    bookTable.setItems(BookList);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a keyword to search for books.");
            alert.showAndWait();
        }
    }
}