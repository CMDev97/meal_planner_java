package mealplanner.dao;

import java.util.Map;

public interface IDAOPlanFile {


    void save(String filename, Map<String, Integer> ingredientsCount) throws DAOException;

}
