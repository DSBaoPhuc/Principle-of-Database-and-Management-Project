package control.lms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
//            stage.initStyle(StageStyle.UTILITY);
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

            // Show the confirmation dialog and handle the user's response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Remove the book from the database
                        String query = "DELETE FROM Books WHERE BookID = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, selectedBook.getId());
                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        // Refresh the table to reflect the changes
                        refreshTable();
                    } catch (SQLException ex) {
                        Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else {
            // If no book is selected, show an error message
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
                // Load the update book view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateBook.fxml"));
                Parent parent = loader.load();

                // Get the controller for the update book view
                UpdateBook controller = loader.getController();

                // Pass the selected book's details to the controller
                controller.initData(selectedBook);

                // Create a new stage for the update book view
                Stage stage = new Stage();
                stage.setTitle("Update Book");
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
            // If no book is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book to update.");
            alert.showAndWait();
        }
    }
}