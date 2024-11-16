package mealplanner.dao;

import mealplanner.model.Meal;
import mealplanner.model.MealPlan;
import mealplanner.model.TypeMeal;

import java.sql.*;
import java.util.*;

public class DAOMealPlan implements IDAOMealPlan {
    private final IDAOMeals daoMeals;

    public DAOMealPlan() {
        this.daoMeals = new DAOMeals();
    }

    @Override
    public void makePersistent(List<MealPlan> mealPlans) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        Statement deleteStatement = null;
        try {
            connection = DataSource.getConnection();
            connection.setAutoCommit(false);

            deleteStatement = connection.createStatement();
            deleteStatement.executeUpdate("DELETE FROM plan");

            statement = connection.prepareStatement("INSERT INTO plan (meal_option, meal_category, meal_id) VALUES (?, ?, ?)");

            for (MealPlan mealPlan : mealPlans) {
                statement.setString(1, mealPlan.getDay());
                statement.setString(2, mealPlan.getTypeMeal().toString());
                statement.setLong(3, mealPlan.getMeal().getId());
                statement.addBatch();
            }

            statement.executeBatch();

            connection.commit();
        } catch (SQLException e) {
            DataSource.rollback(connection);
            throw new DAOException(e);
        }
        DataSource.close(deleteStatement);
        DataSource.close(statement);
        DataSource.close(connection);
    }

    @Override
    public Map<String, List<MealPlan>> findWeekPlan() throws DAOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Map<String, List<MealPlan>> mealPlans = new TreeMap<>();
        try {
            connection = DataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM plan");
            while (resultSet.next()) {
                String day = resultSet.getString("meal_option");
                String type = resultSet.getString("meal_category");
                Long id = resultSet.getLong("meal_id");
                TypeMeal typeMeal = TypeMeal.valueOf(type);

                Meal meal = this.daoMeals.findById(id);

                if (meal != null) {
                    MealPlan plan = new MealPlan(day, typeMeal, meal);
                    addMealAtPlan(mealPlans, day, plan);
                }

            }
            return mealPlans;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }


    private void addMealAtPlan(Map<String, List<MealPlan>> mealPlans, String day, MealPlan mealPlan) {
        List<MealPlan> planList = mealPlans.getOrDefault(day, null);
        if (planList == null) {
            planList = new LinkedList<>();
            mealPlans.put(day, planList);
        }
        planList.add(mealPlan);
    }
}
