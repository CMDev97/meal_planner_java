package mealplanner.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class DAOPlanFile implements IDAOPlanFile {


    @Override
    public void save(String filename, Map<String, Integer> ingredientsCount) throws DAOException {
        // create file if not exists
        createFileIfNotExists(filename);
        try (FileWriter fw = new FileWriter(filename, false)) {
            for (Map.Entry<String, Integer> entry : ingredientsCount.entrySet()) {
                String ingredientName = entry.getKey();
                String ingredientQuantity = (entry.getValue() > 1) ? " x".concat(String.valueOf(entry.getValue())) : "";
                fw.write(ingredientName + ingredientQuantity + "\n");
            }
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }

    private void createFileIfNotExists(String filename) throws DAOException {
        try {
            // Creazione dell'oggetto File
            File file = new File(filename);

            // Controlla se il file esiste
            if (!file.exists()) {
                // Se il file non esiste, lo crea
                if (file.createNewFile()) {
                    //System.out.println("File created: " + file.getName());
                } else {
                   throw new DAOException("File not created: " + file.getName());
                }
            }
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }


}
