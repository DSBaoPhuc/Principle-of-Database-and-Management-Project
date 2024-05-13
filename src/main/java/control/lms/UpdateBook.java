package control.lms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateBook {

    public TextField updateBookTitle;
    public TextField updateBookAuthor;
    public TextField updateBookGenre;
    public TextField updateBookPubYear;
    public TextField updateBookCopyAvail;
    public TextField updateBookTotalCopy;
    public Button saveBtn;
    public Button cancelBtn;
    @FXML
    private TextField titleField;

    private Book book;

    public void initData(Book book) {
        this.book = book;
        updateBookTitle.setText(book.getTitle());
        updateBookAuthor.setText(book.getAuthor());
        updateBookGenre.setText(book.getGenre());
        updateBookPubYear.setText(String.valueOf(book.getPublicationYear()));
        updateBookCopyAvail.setText(String.valueOf(book.getCopiesAvailable()));
        updateBookTotalCopy.setText(String.valueOf(book.getTotalCopies()));
    }

    @FXML
    private void updateBook() {
        String newTitle = updateBookTitle.getText();
        String newAuthor = updateBookAuthor.getText();
        String newGenre = updateBookGenre.getText();
        int newPubYear = Integer.parseInt(updateBookPubYear.getText());
        int newCopyAvail = Integer.parseInt(updateBookCopyAvail.getText());
        int newTotalCopy = Integer.parseInt(updateBookTotalCopy.getText());

        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setGenre(newGenre);
        book.setPublicationYear(newPubYear);
        book.setCopiesAvailable(newCopyAvail);
        book.setTotalCopies(newTotalCopy);

        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String query = "UPDATE Books SET Title=?, Author=?, Genre=?, PublicationYear=?, CopiesAvailable=?, TotalCopies=? WHERE BookID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, newAuthor);
            preparedStatement.setString(3,newGenre);
            preparedStatement.setInt(4, newPubYear);
            preparedStatement.setInt(5, newCopyAvail);
            preparedStatement.setInt(6, newTotalCopy);
            preparedStatement.setInt(7, book.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();

            closeUpdateBookView();
        } catch (SQLException ex) {
            Logger.getLogger(ManageBook.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeUpdateBookView();
    }

    public void handleSaveBtn(MouseEvent event) {
        updateBook();
    }

    public void handleCancelBtn(MouseEvent event) {
        closeUpdateBookView();
    }

    private void closeUpdateBookView() {
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
