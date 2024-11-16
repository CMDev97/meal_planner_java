package mealplanner.dao;

import mealplanner.model.Meal;
import mealplanner.model.TypeMeal;

import java.util.List;

public interface IDAOMeals {

    List<Meal> findAll(TypeMeal typeMeal, boolean ordered) throws DAOException;

    void makePersistent(Meal t) throws DAOException;

    Meal findById(Long id) throws DAOException;

}
