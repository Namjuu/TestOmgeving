import nl.saxion.app.CsvReader;
import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.util.ArrayList;

public class Application implements Runnable {
    ArrayList<School> schools;

    public static void main(String[] args) {
        SaxionApp.start(new Application(), 1024, 768);
    }

    public void run() {
        //Read schools
        schools = readFile("students.csv");

        boolean proceed = true;
        while (proceed) {
            SaxionApp.clear();
            SaxionApp.printLine("Welcome to the Enschede, Deventer and Apeldoorn schoolsystem!");
            SaxionApp.printLine("------------------------");
            SaxionApp.printLine("1. Print all schoolnames");
            SaxionApp.printLine("2. Get total number of students per year");
            SaxionApp.printLine("3. Draw student overview chart");
            SaxionApp.printLine("0. Exit");

            int choice = SaxionApp.readInt();
            if (choice == 1) {
                ArrayList<String> schoolnames = getUniqueSchoolnames();
                for (String name: schoolnames) {
                    SaxionApp.printLine("- " + name);
                }
            } else if (choice == 2) {
                //Ask until a year between 4 and 6 is entered
                int year = 0;
                while (year < 4 || year > 6) {
                    SaxionApp.printLine("Which year (4, 5 or 6)?");
                    year = SaxionApp.readInt();
                }
                SaxionApp.printLine("In Enschede there are " + getTotalPerCityAndYear("Enschede", year));
                SaxionApp.printLine("In Deventer there are " + getTotalPerCityAndYear("Deventer", year));
                SaxionApp.printLine("In Apeldoorn there are " + getTotalPerCityAndYear("Apeldoorn", year));
            } else if (choice == 3) {
                SaxionApp.printLine("Which city?");
                String city = SaxionApp.readString();
                SaxionApp.printLine("Which type (HAVO / VWO)?");
                String type = SaxionApp.readString();
                drawGraph(city, type);
            } else if (choice == 0) {
                proceed = false;
            }

            SaxionApp.pause();
        }
        SaxionApp.printLine("Bye!");
    }

    /**
     * Read the given file and return a list of Schools
     * @param file
     * @return
     */
    public ArrayList<School> readFile(String file) {
        CsvReader reader = new CsvReader(file);
        reader.skipRow();

        ArrayList<School> schools = new ArrayList<>();

        while (reader.loadRow()) {
            School s = new School();
            s.name = reader.getString(0);
            s.city = reader.getString(1);
            s.type = reader.getString(2);
            s.direction = reader.getString(3);
            s.year4male = reader.getInt(4);
            s.year4female = reader.getInt(5);
            s.year5male = reader.getInt(6);
            s.year5female = reader.getInt(7);
            s.year6male = reader.getInt(8);
            s.year6female = reader.getInt(9);
            schools.add(s);
        }

        return schools;
    }

    /**
     * Get unique schoolnames
     * @return A list with schoolnames
     */
    public ArrayList<String> getUniqueSchoolnames() {
        ArrayList<String> schoolnames = new ArrayList<>();
        for (School s: schools) {
            if (!schoolnames.contains(s.name)) {
                schoolnames.add(s.name);
            }
        }
        return schoolnames;
    }

    /**
     * Calculate the total number of students on a given year in a given city.
     * @param city
     * @param year
     * @return
     */
    public int getTotalPerCityAndYear(String city, int year) {
        int total = 0;
        for (School s: schools) {
            if (s.city.equalsIgnoreCase(city)) {
                if (year == 4) {
                    total = total + s.year4male + s.year4female;
                } else if (year == 5) {
                    total = total + s.year5male + s.year5female;
                } else if (year == 6) {
                    total = total + s.year6male + s.year6female;
                }
            }
        }

        return total;
    }

    public void drawGraph(String city, String type) {
        SaxionApp.clear();
        SaxionApp.drawText("Number of males and females in 4th year of " + type + " in " + city, 0, 10, 24);
        int ypos = 50;
        for (School s: schools) {
            if (s.city.equalsIgnoreCase(city) && s.type.equalsIgnoreCase(type)) {
                drawLine(ypos, s);
                ypos = ypos + 16;
            }
        }
        SaxionApp.pause();
    }

    public void drawLine (int ypos, School s) {
        SaxionApp.drawText(s.name + ", " + s.direction, 0, ypos, 10);

        int xpos = 420;
        SaxionApp.turnBorderOff();
        SaxionApp.setFill(SaxionApp.createColor(133, 206, 228));
        for (int student = 0; student < s.year4male; student++) {
            SaxionApp.drawCircle(xpos, ypos + 5, 4);
            xpos = xpos + 8;
        }
        SaxionApp.setFill(SaxionApp.createColor(219,6,81));
        for (int student = 0; student < s.year4female; student++) {
            SaxionApp.drawCircle(xpos, ypos + 5, 4);
            xpos = xpos + 8;
        }
    }
}






