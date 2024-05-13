package control.lms;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddBook implements Initializable {
    public TextField addBookTitle;
    public TextField addBookAuthor;
    public TextField addBookGenre;
    public TextField addBookPubYear;
    public TextField addBookCopyAvail;
    public TextField addBookTotalCopy;
    public Button saveBtn;
    public Button cleanBtn;

    Connection connection = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void clean() throws SQLException {
        addBookTitle.setText("");
        addBookAuthor.setText("");
        addBookGenre.setText("");
        addBookPubYear.setText("");
        addBookCopyAvail.setText("");
        addBookTotalCopy.setText("");
    }

    public void save(MouseEvent event) throws SQLException {
        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        String title = addBookTitle.getText();
        String author = addBookAuthor.getText();
        String genre = addBookGenre.getText();
        String pubYearStr = addBookPubYear.getText();
        String copyAvailStr = addBookCopyAvail.getText();
        String totalCopyStr = addBookTotalCopy.getText();

        if(title.equals("") || author.equals("") || genre.equals("") || pubYearStr.equals("") || copyAvailStr.equals("") || totalCopyStr.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("PLEASE FILL ALL DATA");
            alert.showAndWait();
        }
        else {
            try {
                int pubYear = Integer.parseInt(pubYearStr);
                int copyAvail = Integer.parseInt(copyAvailStr);
                int totalCopy = Integer.parseInt(totalCopyStr);
                saveToDatabase(title, author, genre, pubYear, copyAvail, totalCopy);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("INVALID QUANTITY OR STATUS");
                alert.showAndWait();
            }
        }
    }

    private void saveToDatabase(String title, String author, String genre , int pubYear, int copyAvail, int totalCopy) throws SQLException {
        String query = "INSERT INTO Books (Title, Author, Genre, PublicationYear, CopiesAvailable, TotalCopies ) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, author);
        preparedStatement.setString(3, genre);
        preparedStatement.setInt(4, pubYear);
        preparedStatement.setInt(5, copyAvail);
        preparedStatement.setInt(6, totalCopy);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Book added successfully");
            alert.showAndWait();
            clean();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to add book");
            alert.showAndWait();
        }
        preparedStatement.close();
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
