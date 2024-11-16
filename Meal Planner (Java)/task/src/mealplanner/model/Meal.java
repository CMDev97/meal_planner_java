package mealplanner.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Meal {
    private Long id;
    private TypeMeal type;
    private String name;
    private List<String> ingredients;

    public Meal(TypeMeal type, String name, List<String> ingredients) {
        this.type = type;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Meal(Long id, TypeMeal type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        return builder.toString();
    }
}
