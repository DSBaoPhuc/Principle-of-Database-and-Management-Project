package control.lms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #7900FF; -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 10px;";
    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 10px;";
    @FXML
    public Button btnHome;
    public Button btnManageBook;
    public Button btnManageMember;
    public Button btnViewRecord;
    public Button btnSignOut;
    public StackPane contentArea;


    //Home button
    @FXML
    private void highlightbtnHome(MouseEvent event) {
        btnHome.setStyle(HOVERED_BUTTON_STYLE);
    }
    @FXML
    private void resetbtnHome(MouseEvent event) {
        btnHome.setStyle(IDLE_BUTTON_STYLE);
    }

    // Manage book button
    @FXML
    private void highlightbtnManageBook(MouseEvent event) {
        btnManageBook.setStyle(HOVERED_BUTTON_STYLE);
    }
    @FXML
    private void resetbtnManageBook(MouseEvent event) {
        btnManageBook.setStyle(IDLE_BUTTON_STYLE);
    }

    @FXML
    private void highlightbtnManageMember(MouseEvent event) {
        btnManageMember.setStyle(HOVERED_BUTTON_STYLE);
    }
    @FXML
    private void resetbtnManageMember(MouseEvent event) {
        btnManageMember.setStyle(IDLE_BUTTON_STYLE);
    }

    // View Record button
    @FXML
    private void highlightbtnViewRecord(MouseEvent event) {
        btnViewRecord.setStyle(HOVERED_BUTTON_STYLE);
    }
    @FXML
    private void resetbtnViewRecord(MouseEvent event) {
        btnViewRecord.setStyle(IDLE_BUTTON_STYLE);
    }

    @FXML
    private void highlightbtnSignOut(MouseEvent event) {
        btnSignOut.setStyle(HOVERED_BUTTON_STYLE);
    }
    @FXML
    private void resetbtnSignOut(MouseEvent event) {
        btnSignOut.setStyle(IDLE_BUTTON_STYLE);
    }

    public void handleSignOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btnSignOut.getScene().getWindow(); // Get the current stage
        stage.getIcons().add(new Image("C:\\Users\\Admin\\Documents\\LMS - PDM Project\\src\\main\\resources\\imgs\\image.png"));
        stage.setTitle("Home Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void homePage (javafx.event.ActionEvent actionEvent) throws Exception{
        Parent fxml = FXMLLoader.load(getClass().getResource("pie_chart.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    public void manageBook (javafx.event.ActionEvent actionEvent) throws Exception{
        Parent fxml = FXMLLoader.load(getClass().getResource("ManageBook.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    public void manageMember (javafx.event.ActionEvent actionEvent) throws Exception{
        Parent fxml = FXMLLoader.load(getClass().getResource("ManageMember.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    public void viewRecord (javafx.event.ActionEvent actionEvent) throws Exception{
        Parent fxml = FXMLLoader.load(getClass().getResource("ViewRecord.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }
}
