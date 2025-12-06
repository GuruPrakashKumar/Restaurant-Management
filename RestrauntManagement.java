import java.util.*;

class MenuItem {
    int id;
    String name;
    double price;

    MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class Table {
    int number;
    int capacity;
    boolean booked;

    Table(int number, int capacity) {
        this.number = number;
        this.capacity = capacity;
        this.booked = false;
    }
}

class Order {
    int tableNumber;
    List<MenuItem> items = new ArrayList<>();

    Order(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    void addItem(MenuItem item) {
        items.add(item);
    }

    double getTotal() {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.price;
        }
        return total;
    }
}

public class RestaurantManagementSystem {

    private static final Scanner sc = new Scanner(System.in);
    private static final List<MenuItem> menu = new ArrayList<>();
    private static final List<Table> tables = new ArrayList<>();
    private static double totalCollection = 0.0;

    public static void main(String[] args) {
        initMenu();
        initTables();
        runApp();
    }

    private static void initMenu() {
        menu.add(new MenuItem(1, "Margherita Pizza", 250.0));
        menu.add(new MenuItem(2, "Farmhouse Pizza", 350.0));
        menu.add(new MenuItem(3, "Veg Burger", 120.0));
        menu.add(new MenuItem(4, "French Fries", 90.0));
        menu.add(new MenuItem(5, "Cold Coffee", 80.0));
        menu.add(new MenuItem(6, "Gulab Jamun", 60.0));
    }

    private static void initTables() {
        tables.add(new Table(1, 2));
        tables.add(new Table(2, 2));
        tables.add(new Table(3, 4));
        tables.add(new Table(4, 4));
        tables.add(new Table(5, 6));
    }

    private static void runApp() {
        int choice;
        do {
            printMainMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> showMenu();
                case 2 -> showTableStatus();
                case 3 -> bookTable();
                case 4 -> placeOrderAndGenerateBill();
                case 5 -> showTotalCollection();
                case 0 -> System.out.println("Exiting... Thank you for visiting!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private static void printMainMenu() {
        System.out.println("\n===== RESTAURANT MANAGEMENT SYSTEM =====");
        System.out.println("1. Show Menu");
        System.out.println("2. Show Table Status");
        System.out.println("3. Book a Table");
        System.out.println("4. Place Order & Generate Bill");
        System.out.println("5. Show Total Collection");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    private static void showMenu() {
        System.out.println("\n-------- MENU --------");
        for (MenuItem item : menu) {
            System.out.printf("%d. %-20s Rs. %.2f%n", item.id, item.name, item.price);
        }
        System.out.println("----------------------");
    }

    private static void showTableStatus() {
        System.out.println("\n------- TABLE STATUS -------");
        for (Table t : tables) {
            System.out.printf("Table %d (Capacity: %d) - %s%n",
                    t.number,
                    t.capacity,
                    t.booked ? "BOOKED" : "AVAILABLE");
        }
        System.out.println("----------------------------");
    }

    private static void bookTable() {
        showTableStatus();
        int tableNo = readInt("Enter table number to book: ");

        Table table = findTableByNumber(tableNo);
        if (table == null) {
            System.out.println("Invalid table number.");
            return;
        }

        if (table.booked) {
            System.out.println("Table is already booked.");
        } else {
            table.booked = true;
            System.out.println("Table " + table.number + " booked successfully.");
        }
    }

    private static void placeOrderAndGenerateBill() {
        showTableStatus();
        int tableNo = readInt("Enter table number for order: ");

        Table table = findTableByNumber(tableNo);
        if (table == null) {
            System.out.println("Invalid table number.");
            return;
        }

        if (!table.booked) {
            System.out.println("Table is not booked yet. Please book the table first.");
            return;
        }

        Order order = new Order(tableNo);

        boolean ordering = true;
        while (ordering) {
            showMenu();
            int itemId = readInt("Enter menu item ID to add (0 to stop): ");

            if (itemId == 0) {
                ordering = false;
                break;
            }

            MenuItem item = findMenuItemById(itemId);
            if (item == null) {
                System.out.println("Invalid item ID. Try again.");
            } else {
                order.addItem(item);
                System.out.println(item.name + " added to order.");
            }
        }

        if (order.items.isEmpty()) {
            System.out.println("No items ordered. Cancelling bill generation.");
            return;
        }

        printBill(order);
        double billAmount = order.getTotal();
        totalCollection += billAmount;

        // After billing, free the table
        table.booked = false;
        System.out.println("Table " + table.number + " is now AVAILABLE.");
    }

    private static void printBill(Order order) {
        System.out.println("\n=========== BILL ===========");
        System.out.println("Table Number: " + order.tableNumber);
        System.out.println("Items:");
        for (MenuItem item : order.items) {
            System.out.printf(" - %-20s Rs. %.2f%n", item.name, item.price);
        }
        System.out.println("----------------------------");
        System.out.printf("Total Amount: Rs. %.2f%n", order.getTotal());
        System.out.println("============================");
    }

    private static void showTotalCollection() {
        System.out.printf("%nTotal Restaurant Collection: Rs. %.2f%n", totalCollection);
    }

    private static Table findTableByNumber(int tableNo) {
        for (Table t : tables) {
            if (t.number == tableNo) return t;
        }
        return null;
    }

    private static MenuItem findMenuItemById(int id) {
        for (MenuItem item : menu) {
            if (item.id == id) return item;
        }
        return null;
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
