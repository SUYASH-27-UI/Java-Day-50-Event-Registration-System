import java.util.*;
import java.io.*;

class Registration {

    int id;
    String name;
    String event;

    Registration(int id, String name, String event) {
        this.id = id;
        this.name = name;
        this.event = event;
    }

    void display() {
        System.out.println("\nRegistration ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Event: " + event);
    }
}

public class Main {

    static ArrayList<Registration> registrations =
            new ArrayList<>();

    static HashMap<String, Integer> capacity =
            new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "registrations.txt";

    static void setupEvents() {

        capacity.put("Python Workshop", 3);
        capacity.put("Java Seminar", 4);
        capacity.put("AI Conference", 5);
    }

    static void loadData() {

        try (BufferedReader br =
                new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", 3);

                if (data.length == 3) {

                    registrations.add(
                        new Registration(
                            Integer.parseInt(data[0]),
                            data[1],
                            data[2]
                        )
                    );
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("New registration database created.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading registrations.");
        }
    }

    static void saveData() {

        try (BufferedWriter bw =
                new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Registration r : registrations) {

                bw.write(
                    r.id + "|" + r.name + "|" + r.event
                );

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving registrations.");
        }
    }

    static void displayEvents() {

        System.out.println("\n===== AVAILABLE EVENTS =====");

        for (String event : capacity.keySet()) {

            int registered = 0;

            for (Registration r : registrations) {
                if (r.event.equals(event)) {
                    registered++;
                }
            }

            int available = capacity.get(event) - registered;

            System.out.println(
                event + " | Available Seats: " + available
            );
        }
    }

    static void registerStudent() {

        displayEvents();

        System.out.print("\nEnter Student Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Event Name: ");
        String event = sc.nextLine();

        if (name.isBlank() || name.contains("|")
                || event.contains("|")) {
            System.out.println("Invalid input!");
            return;
        }

        if (!capacity.containsKey(event)) {
            System.out.println("Event not found!");
            return;
        }

        int registered = 0;

        for (Registration r : registrations) {

            if (r.event.equals(event)) {
                registered++;
            }

            if (r.name.equalsIgnoreCase(name)
                    && r.event.equalsIgnoreCase(event)) {
                System.out.println("Already registered!");
                return;
            }
        }

        if (registered >= capacity.get(event)) {
            System.out.println("Event is fully booked!");
            return;
        }

        int id = 1001;

        for (Registration r : registrations) {
            if (r.id >= id) {
                id = r.id + 1;
            }
        }

        Registration r = new Registration(id, name, event);

        registrations.add(r);

        saveData();

        System.out.println("Registration successful!");
        System.out.println("Registration ID: " + id);
    }

    static void displayRegistrations() {

        if (registrations.isEmpty()) {
            System.out.println("No registrations available.");
            return;
        }

        System.out.println("\n===== ALL REGISTRATIONS =====");

        for (Registration r : registrations) {
            r.display();
        }
    }

    static void searchRegistration() {

        System.out.print("Enter Registration ID: ");

        int id;

        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID!");
            return;
        }

        for (Registration r : registrations) {

            if (r.id == id) {
                r.display();
                return;
            }
        }

        System.out.println("Registration not found!");
    }

    static void cancelRegistration() {

        System.out.print("Enter Registration ID: ");

        int id;

        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID!");
            return;
        }

        boolean removed = registrations.removeIf(
            r -> r.id == id
        );

        if (removed) {
            saveData();
            System.out.println("Registration cancelled!");
        } else {
            System.out.println("Registration not found!");
        }
    }

    public static void main(String[] args) {

        setupEvents();
        loadData();

        int choice = 0;

        do {

            System.out.println("\n===== EVENT MANAGEMENT =====");
            System.out.println("1. View Events");
            System.out.println("2. Register Student");
            System.out.println("3. Display Registrations");
            System.out.println("4. Search Registration");
            System.out.println("5. Cancel Registration");
            System.out.println("6. Exit");

            System.out.print("Enter Choice: ");

            try {

                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        displayEvents();
                        break;

                    case 2:
                        registerStudent();
                        break;

                    case 3:
                        displayRegistrations();
                        break;

                    case 4:
                        searchRegistration();
                        break;

                    case 5:
                        cancelRegistration();
                        break;

                    case 6:
                        System.out.println("Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                System.out.println("Something went wrong!");
            }

        } while (choice != 6);

        sc.close();
    }
}
