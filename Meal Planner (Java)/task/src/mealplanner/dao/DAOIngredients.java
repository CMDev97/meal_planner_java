package mealplanner.dao;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOIngredients implements IDAOIngredients {


    @Override
    public List<String> getIngredients(Long mealId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> ingredients = new ArrayList<>();
        try {
            connection = DataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM ingredients WHERE meal_id = ?");
            preparedStatement.setLong(1, mealId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ingredients.add(resultSet.getString("ingredient"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        DataSource.close(resultSet);
        DataSource.close(preparedStatement);
        DataSource.close(connection);
        return ingredients;
    }

    @Override
    public void makePersistent(Connection connection, Long mealId, List<String> ingredients) throws DAOException {
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select count(*) from ingredients");

            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1)+1;
            }

            preparedStatement = connection.prepareStatement("INSERT INTO ingredients (ingredient_id, meal_id, ingredient) VALUES (?, ?, ?)");
            for (String ingredient : ingredients) {
                preparedStatement.setLong(1, count);
                preparedStatement.setLong(2, mealId);
                preparedStatement.setString(3, ingredient.trim());
                preparedStatement.addBatch();
                count++;
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            DataSource.rollback(connection);
            throw new DAOException(e);
        }
    }

}
