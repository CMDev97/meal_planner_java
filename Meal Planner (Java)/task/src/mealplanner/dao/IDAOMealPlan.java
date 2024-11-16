package mealplanner.dao;

import mealplanner.model.MealPlan;

import java.util.List;
import java.util.Map;

public interface IDAOMealPlan {

    void makePersistent(List<MealPlan> mealPlans) throws DAOException;

    Map<String, List<MealPlan>> findWeekPlan() throws DAOException;

}
