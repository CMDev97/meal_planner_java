package mealplanner.dao;

import lombok.extern.slf4j.Slf4j;
import mealplanner.model.Meal;
import mealplanner.model.TypeMeal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DAOMeals implements IDAOMeals {
    private static final String INSERT_MEAL_SQL = "INSERT INTO meals (meal_id, meal, category) VALUES (?, ?, ?)";
    private final DAOIngredients daoIngredients;

    public DAOMeals() {
        this.daoIngredients = new DAOIngredients();
    }


    @Override
    public List<Meal> findAll(TypeMeal typeMeal, boolean ordered) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Meal> meals = new ArrayList<>();

        String query = "SELECT m.meal_id, m.category, m.meal FROM meals m ";

        if (typeMeal != null) {
            String where = String.format("where category like '%s'", typeMeal);
            query += where;
        }

        if (ordered) {
            query += " ORDER BY m.meal ASC";
        }


        try {
            connection = DataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                long mealId = resultSet.getLong("meal_id");
                String mealType = resultSet.getString("category");
                String mealName = resultSet.getString("meal");

                Meal meal = new Meal(mealId, TypeMeal.valueOf(mealType.toUpperCase()), mealName);

                List<String> ingredients = daoIngredients.getIngredients(mealId);
                meal.setIngredients(ingredients);
                meals.add(meal);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        DataSource.close(resultSet);
        DataSource.close(statement);
        DataSource.close(connection);
        return meals;
    }


    @Override
    public void makePersistent(Meal meal) throws DAOException {
        Connection connection = null;
        PreparedStatement mealStatement = null;
        try {
            connection = DataSource.getConnection();
            connection.setAutoCommit(false);

            long id = count()+1;
            mealStatement = connection.prepareStatement(INSERT_MEAL_SQL);
            mealStatement.setLong(1, id);
            mealStatement.setString(2, meal.getName());
            mealStatement.setString(3, meal.getType().toString());
            mealStatement.executeUpdate();

            daoIngredients.makePersistent(connection, id, meal.getIngredients());

            connection.commit();
        } catch (DAOException | SQLException e) {
            DataSource.rollback(connection);
        }
        DataSource.close(mealStatement);
        DataSource.close(connection);
    }

    @Override
    public Meal findById(Long id) throws DAOException {
        Connection connection = null;
        PreparedStatement mealStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getConnection();
            mealStatement = connection.prepareStatement("SELECT * from meals where meal_id = ?");
            mealStatement.setLong(1, id);
            resultSet = mealStatement.executeQuery();
            if (resultSet.next()) {
                long mealId = resultSet.getLong("meal_id");
                String mealType = resultSet.getString("category");
                String mealName = resultSet.getString("meal");
                TypeMeal typeMeal = TypeMeal.valueOf(mealType.toUpperCase());
                return new Meal(mealId, typeMeal, mealName);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        DataSource.close(resultSet);
        DataSource.close(mealStatement);
        DataSource.close(connection);
        return null;
    }

    private Long count() throws DAOException {
        String query = "SELECT count(*) FROM meals";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return  resultSet.getLong(1);
            }
            throw new DAOException("No records found");
        } catch (SQLException | DAOException e) {
            throw new DAOException(e);
        }
    }

}
