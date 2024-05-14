package control.lms;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class StatisticController {

    public Button SwapBtn;
    public NumberAxis yAxis;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private void initialize() {
        // Initialize the chart
    }

    public void populateChart(ResultSet resultSet, boolean isBooksBorrowed) throws SQLException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        while (resultSet.next()) {
            String monthYear = resultSet.getString("Month") + "/" + resultSet.getString("Year");
            int value = isBooksBorrowed ? resultSet.getInt("TotalBooks") : resultSet.getInt("TotalAmount");
            series.getData().add(new XYChart.Data<>(monthYear, value));
        }

        barChart.getData().add(series);
    }

    @FXML
    private void toggleChart() throws SQLException {
        // Clear existing data
        barChart.getData().clear();

        // Determine the new chart type based on the current state
        boolean isBooksBorrowed = barChart.getYAxis().getLabel().equals("Total Books Borrowed");
        String yAxisLabel = isBooksBorrowed ? "Total Books Borrowed" : "Total Amount";

        // Update chart type and fetch data accordingly
        barChart.getYAxis().setLabel(yAxisLabel);
        // Fetch data based on the new chart type (books borrowed or total amount)
        ResultSet resultSet = fetchData(isBooksBorrowed);
        try {
            populateChart(resultSet, isBooksBorrowed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        // Execute the SQL query and return the ResultSet
        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // Get your database connection
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public void highlightButton(MouseEvent event){
        SwapBtn.setStyle("-fx-background-color: #0E49B5; -fx-background-radius: 20;");
    }
    @FXML
    private void resetButtonColor(MouseEvent event){
        SwapBtn.setStyle("-fx-background-color: #153E90; -fx-background-radius: 20;");
    }
}
