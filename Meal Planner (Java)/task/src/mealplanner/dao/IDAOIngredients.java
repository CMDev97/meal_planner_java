package mealplanner.dao;

import java.sql.Connection;
import java.util.List;

public interface IDAOIngredients {

    List<String> getIngredients(Long mealId) throws DAOException;

    void makePersistent(Connection connection, Long mealId, List<String> ingredient) throws DAOException;

}
