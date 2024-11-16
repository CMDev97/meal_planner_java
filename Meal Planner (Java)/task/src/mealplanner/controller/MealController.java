package mealplanner.controller;

import mealplanner.dao.*;
import mealplanner.model.Meal;
import mealplanner.model.MealPlan;
import mealplanner.model.TypeMeal;
import mealplanner.utils.Constant;
import mealplanner.view.TextView;

import java.util.*;
import java.util.stream.Collectors;

public class MealController {
    private final TextView textView;
    private final IDAOMeals daoMeals;
    private final IDAOMealPlan daoMealPlan;
    private final IDAOIngredients daoIngredients;
    private final IDAOPlanFile daoPlanFile;
    private final DatabaseInit databaseInit;

    public MealController(TextView textView, IDAOMeals daoMeals, IDAOMealPlan daoMealPlan, IDAOIngredients daoIngredients, IDAOPlanFile daoPlanFile) {
        this.textView = textView;
        this.daoMeals = daoMeals;
        this.daoMealPlan = daoMealPlan;
        this.daoIngredients = daoIngredients;
        this.daoPlanFile = daoPlanFile;
        this.databaseInit = new DatabaseInit();
    }

    public void startApplication() {
        this.initializeDatabase();
        try {
            while (true) {
                String option = textView.readOptionMenu();
                if (option.equals("exit")) {
                    System.out.println("Bye!");
                    break;
                } else if (option.equals("add")) {
                    this.actionAddMeal();
                } else if (option.equals("show")) {
                    this.actionListOfMeal();
                } else if (option.equals("plan")) {
                    this.actionCreateWeekPlan();
                } else if (option.equals("list plan")) {
                    this.actionListPlanMeal();
                } else if (option.equals("save")) {
                    this.actionSavePlanMeal();
                }
            }
        } catch (DAOException e) {
            this.textView.printError(e);
        }
    }

    private void actionSavePlanMeal() throws DAOException {
        // retrieve meal plan
        Map<String, List<MealPlan>> weekPlan = this.daoMealPlan.findWeekPlan();
        if (weekPlan == null || weekPlan.isEmpty()) {
            this.textView.printNoMealsPlanForSave();
            return;
        }
        // read filename from user input
        String filename = this.textView.readFilename();

        // flat map list MealPlan
        List<MealPlan> onlyMealPlan = weekPlan.values().stream().flatMap(Collection::stream).toList();
        List<String> ingredients = new ArrayList<>();
        for (MealPlan mealPlan : onlyMealPlan) {
            // retrieve ingredients from meal id
            List<String> tmpList = this.daoIngredients.getIngredients(mealPlan.getMeal().getId());
            ingredients.addAll(tmpList);
        }
        // create shopping list
        Map<String, Integer> shoppingList = this.getIngredientsCount(ingredients);
        // save on file shopping list
        this.daoPlanFile.save(filename, shoppingList);
        System.out.println("Saved!");
    }

    private void actionListPlanMeal() throws DAOException {
        // retrieve meal plan week
        Map<String, List<MealPlan>> weekPlan = this.daoMealPlan.findWeekPlan();

        // show week plan
        this.textView.printMealsPlan(weekPlan);
    }

    private void actionAddMeal() throws DAOException {
        // read meal information
        TypeMeal typeMeal = this.textView.readTypeNewMeal();
        String mealName = this.textView.readMealName();
        List<String> mealIngredient = this.textView.readIngredients();
        // create object meal with data read
        Meal meal = new Meal(typeMeal, mealName, mealIngredient);
        // save meal on database
        this.daoMeals.makePersistent(meal);
        // show status action
        this.textView.showResultSaveMeal();
    }

    private void actionListOfMeal() throws DAOException {
        // read filter category meal
        TypeMeal typeMeal = this.textView.realTypeShowMeal();
        // get result from dao
        List<Meal> meals = this.daoMeals.findAll(typeMeal, false);
        // show result of meals retrieved on database
        this.textView.showListOfMeal(meals, typeMeal);
    }

    private void actionCreateWeekPlan() throws DAOException {
        // Retrieve one time all necessary data
        List<Meal> breakfastMeal = this.daoMeals.findAll(TypeMeal.BREAKFAST, true);
        List<Meal> lunchMeal = this.daoMeals.findAll(TypeMeal.LUNCH, true);
        List<Meal> dinnerMeal = this.daoMeals.findAll(TypeMeal.DINNER, true);

        // Initialize list of all days plan
        Map<String, List<MealPlan>> plans = new TreeMap<>();

        // Retrieve days plan information
        for (String day : Constant.DAYS_OF_WEEK) {
            List<MealPlan> planDay = actionCreateDayPlan(day, breakfastMeal, lunchMeal, dinnerMeal);
            plans.put(day, planDay);
        }

        // Call dao for save day plan
        this.daoMealPlan.makePersistent(plans.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));

        // Print details plan
        this.textView.printMealsPlan(plans);
    }

    private List<MealPlan> actionCreateDayPlan(String day, List<Meal> breakfastMeal, List<Meal> lunchMeal, List<Meal> dinnerMeal) throws DAOException {
        List<MealPlan> mealPlans = new LinkedList<>();

        this.textView.printDay(day);
        // create and add meal plan for breakfast
        Meal mealBreakfast = this.textView.readMealSelected(TypeMeal.BREAKFAST, day, breakfastMeal);
        mealPlans.add(new MealPlan(day, TypeMeal.BREAKFAST, mealBreakfast));

        // create and add meal plan for Lunch
        Meal mealLunch = this.textView.readMealSelected(TypeMeal.LUNCH, day, lunchMeal);
        mealPlans.add(new MealPlan(day, TypeMeal.LUNCH, mealLunch));

        // create and add meal plan for Dinner
        Meal mealDinner = this.textView.readMealSelected(TypeMeal.DINNER, day, dinnerMeal);
        mealPlans.add(new MealPlan(day, TypeMeal.DINNER, mealDinner));

        this.textView.printResultPlan(day);

        return mealPlans;
    }

    private Map<String, Integer> getIngredientsCount(List<String> ingredients) {
        Map<String, Integer> ingredientsCount = new HashMap<>();
        for (String ingredient : ingredients) {
            Integer count = ingredientsCount.getOrDefault(ingredient, 0);
            ingredientsCount.put(ingredient, count+1);
        }
        return ingredientsCount;
    }

    private void initializeDatabase() {
        try {
            this.databaseInit.init();
        } catch (DAOException e) {
            System.out.println("Database initialization failed!");
            System.exit(1);
        }
    }




}
