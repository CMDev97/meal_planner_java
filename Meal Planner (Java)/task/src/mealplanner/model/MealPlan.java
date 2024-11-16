package mealplanner.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealPlan {
    private String day;
    private TypeMeal typeMeal;
    private Meal meal;

    public MealPlan(String day, TypeMeal typeMeal, Meal meal) {
        this.day = day;
        this.typeMeal = typeMeal;
        this.meal = meal;
    }

}
