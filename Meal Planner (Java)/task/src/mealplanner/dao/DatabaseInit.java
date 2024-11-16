package mealplanner.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DatabaseInit {


    public void init() throws DAOException {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DataSource.getConnection();
            stmt = connection.createStatement();
            createMealsTable(stmt);
            createIngredientsTable(stmt);
            createMealPlanTable(stmt);
        } catch (SQLException | DAOException sqle) {
            log.error("Errore durante l'esecuzione della query", sqle);
            throw new DAOException(sqle);
        } finally {
            DataSource.close(stmt);
            DataSource.close(connection);
        }
    }

    private void createMealsTable(Statement statement) throws SQLException {
        //statement.executeUpdate("drop table if exists meals cascade");
        statement.executeUpdate("create table if not exists meals (" +
                "meal_id integer PRIMARY KEY," +
                "meal varchar(255)," +
                "category varchar(255)" +
                ");"
        );
    }

    private void createIngredientsTable(Statement statement) throws SQLException {
        //statement.executeUpdate("drop table if exists ingredients");
        statement.executeUpdate("create table if not exists ingredients (" +
                "ingredient_id integer PRIMARY KEY," +
                "meal_id integer," +
                "ingredient varchar(255)," +
                "constraint fk_meal_ingredients foreign key (meal_id) references meals(meal_id) ON UPDATE CASCADE ON DELETE CASCADE" +
                ");"
        );
    }

    private void createMealPlanTable(Statement statement) throws SQLException {
        statement.executeUpdate("""
                create table if not exists plan (
                    meal_option varchar(255),
                    meal_category varchar(255),
                    meal_id integer,
                    constraint fk_meal_plan foreign key (meal_id) references meals(meal_id) on update cascade on delete cascade
                );
""");
    }

}
