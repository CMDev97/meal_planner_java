package mealplanner;

import mealplanner.controller.MealController;
import mealplanner.dao.*;
import mealplanner.view.TextView;

import java.util.*;

public class Main {

    public static void main(String[] args) throws DAOException {
        Scanner scanner = new Scanner(System.in);
        TextView view = new TextView(scanner);
        IDAOMeals daoMeals = new DAOMeals();
        IDAOMealPlan daoMealPlan = new DAOMealPlan();
        IDAOIngredients daoIngredients = new DAOIngredients();
        IDAOPlanFile daoPlanFile = new DAOPlanFile();

        MealController controller = new MealController(view, daoMeals, daoMealPlan, daoIngredients, daoPlanFile);

        controller.startApplication();

        scanner.close();
    }

}