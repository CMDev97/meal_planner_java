package mealplanner.view;

import mealplanner.model.Meal;
import mealplanner.model.MealPlan;
import mealplanner.model.TypeMeal;
import mealplanner.utils.Constant;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TextView {
    private final Scanner scanner;

    public TextView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printMealsPlan(Map<String, List<MealPlan>> planMap) {
        if (planMap == null || planMap.isEmpty()) {
            System.out.println("Meal plan doesn’t exist.");
            return;
        }
        for (String day : Constant.DAYS_OF_WEEK) {
            System.out.println(day);
            List<MealPlan> mealPlanList = planMap.get(day);
            for (MealPlan mealPlan : mealPlanList) {
                System.out.println(mealPlan.getTypeMeal().toString().toLowerCase()+ ": " + mealPlan.getMeal().getName());
            }
            System.out.println();
        }
    }

    public void printNoMealsPlanForSave() {
        System.out.println("Unable to save. Plan your meals first.");
    }

    public void printResultPlan(String day) {
        System.out.printf("Yeah! We planned the meals for %s.%n", day);
    }

    public void printDay(String day) {
        System.out.println(day);
    }

    public Meal readMealSelected(TypeMeal typeMeal, String day, List<Meal> meals) {
        showNamesOfMeal(meals);
        System.out.printf("Choose the %s for %s from the list above:%n", typeMeal.toString().toLowerCase(), day);
        while (true) {
            String mealName = scanner.nextLine();
            Meal found = meals.stream().filter(m -> m.getName().toLowerCase().equals(mealName)).findFirst().orElse(null);
            if (found != null) {
                return found;
            }
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
        }
    }

    public void showNamesOfMeal(List<Meal> meals) {
        for (Meal meal : meals) {
            System.out.println(meal.getName());
        }
    }

    public void showListOfMeal(List<Meal> meals, TypeMeal typeMeal) {
        if (meals.isEmpty()) {
            System.out.println("No meals found.");
            return;
        }
        System.out.printf("Category: %s", typeMeal.toString().toLowerCase());
        System.out.println();
        System.out.println();

        for (Meal meal : meals) {
            System.out.println("Name: " + meal.getName());
            System.out.println("Ingredients: ");
            for (String ingredient : meal.getIngredients()) {
                System.out.println(ingredient);
            }
            System.out.println();
        }
    }

    public String readFilename() {
        System.out.println("Input a filename:");
        return scanner.nextLine();
    }

    public String readOptionMenu() {
        System.out.println("What would you like to do (add, show, plan, list plan, save, exit)?");
        String value = this.scanner.nextLine();
        if (!"add".equals(value) && !"show".equals(value) && !"exit".equals(value) && !"plan".equals(value) &&
                !"list plan".equals(value) && !"save".equals(value)
        ) {
            return "";
        }
        return value;
    }

    public List<String> readIngredients() {
        System.out.println("Input the ingredients:");
        while (true) {
            String ingredients = this.scanner.nextLine();
            if (!ingredients.matches("^([a-zA-Z0-9]+( [a-zA-Z0-9]+)*)(, ?[a-zA-Z0-9]+( [a-zA-Z0-9]+)*)*$")){
                System.out.println("Wrong format. Use letters only!");
                continue;
            }
            return List.of(ingredients.split(","));
        }
    }

    public String readMealName() {
        System.out.println("Input the meal's name: ");
        while (true) {
            String name = this.scanner.nextLine();
            if (!name.matches("^([a-zA-Z]+( [a-zA-Z]+)*)$")) {
                System.out.println("Wrong format. Use letters only!");
                continue;
            }
            return name;
        }
    }

    public void printError(Exception e) {
        System.out.printf("Error! %s%n", e.getMessage());
    }

    public void showResultSaveMeal() {
        System.out.println("The meal has been added!");
    }

    public TypeMeal readTypeNewMeal() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        return readTypeMeal();
    }

    public TypeMeal realTypeShowMeal() {
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        return readTypeMeal();
    }

    private TypeMeal readTypeMeal() {
        while (true) {
            String mealName = this.scanner.nextLine();
            if (mealName.equals("breakfast")) {
                return TypeMeal.BREAKFAST;
            } else if (mealName.equals("lunch")) {
                return TypeMeal.LUNCH;
            } else if (mealName.equals("dinner")) {
                return TypeMeal.DINNER;
            }
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
        }
    }

}
