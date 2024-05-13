package control.lms;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Pie_chartController implements Initializable {

    @FXML
    private PieChart bookPieChart;

    @FXML
    private HBox chartContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPieChart();
    }

    private void initPieChart() {

        final String DB_URL = "jdbc:sqlserver://LAPTOP-0BHK1N3R;databasename=Library_Management_System;encrypt=false";
        final String USERNAME = "sa";
        final String PASSWORD = "321";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)){
            String query = "SELECT Genre, SUM(CopiesAvailable) AS TOTAL_Copies FROM Books GROUP BY Genre";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String genre = resultSet.getString("Genre");
                int totalCopies = resultSet.getInt("TOTAL_Copies");
                bookPieChart.getData().add(new PieChart.Data(genre, totalCopies));
            }

            for (PieChart.Data data : bookPieChart.getData()) {
                data.setName(data.getName() + " (" + (int)data.getPieValue() + ")");
            }


            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
