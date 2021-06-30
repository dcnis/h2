package de.schmidtdennis;

import de.schmidtdennis.jdbc.Driver;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");
        Driver driver = new Driver();
        driver.dropTables();
        driver.createTables();
        driver.insertValues();
        driver.getValues();

        // Get a list of all students and how many courses each student is enrolled in
        driver.getQuery1();
    }
}
