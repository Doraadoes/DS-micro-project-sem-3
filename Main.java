
import java.util.*;

class WebRequest {
    int id;
    String url;
    String method;
    int priority;

    WebRequest(int id, String url, String method, int priority) {

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty!");
        }

        if (!Arrays.asList("GET", "POST", "PUT", "DELETE")
                .contains(method)) {
            throw new IllegalArgumentException("Invalid HTTP method!");
        }

        if (priority != 1 && priority != 2) {
            throw new IllegalArgumentException("Invalid priority!");
        }

        this.id = id;
        this.url = url;
        this.method = method;
        this.priority = priority;
    }

    public String toString() {
        return "ID: " + id + " | URL: " + url +
               " | Method: " + method +
               " | Priority: " + priority;
    }
}


class RequestScheduler {

    PriorityQueue<WebRequest> highPriority =
        new PriorityQueue<>(
            Comparator.comparingInt(r -> r.id)
        );

    Queue<WebRequest> normalPriority = new LinkedList<>();

    ArrayList<WebRequest> completed = new ArrayList<>();

    void addRequest(WebRequest request) {

        if (request == null) {
            throw new NullPointerException("Request cannot be null!");
        }

        if (request.priority == 1) {
            highPriority.add(request);
        } else {
            normalPriority.add(request);
        }

        System.out.println("Request added successfully!");
    }

    void processRequest() {
        WebRequest request;

        if (!highPriority.isEmpty()) {
            request = highPriority.poll();
        } else if (!normalPriority.isEmpty()) {
            request = normalPriority.poll();
        } else {
            System.out.println("No pending requests!");
            return;
        }

        completed.add(request);

        System.out.println("\nProcessing: " + request);
        System.out.println("Request completed!");
    }

    void showPending() {
        System.out.println("\n--- HIGH PRIORITY ---");

        if (highPriority.isEmpty())
            System.out.println("No requests.");
        else
            highPriority.forEach(System.out::println);

        System.out.println("\n--- NORMAL PRIORITY ---");

        if (normalPriority.isEmpty())
            System.out.println("No requests.");
        else
            normalPriority.forEach(System.out::println);
    }

    void showCompleted() {
        System.out.println("\n--- COMPLETED REQUESTS ---");

        if (completed.isEmpty()) {
            System.out.println("No completed requests.");
            return;
        }

        completed.forEach(System.out::println);
    }

    void showCount() {
        System.out.println("Pending: " +
            (highPriority.size() + normalPriority.size()));

        System.out.println("Completed: " + completed.size());
    }
}


public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        RequestScheduler scheduler = new RequestScheduler();

        int id = 1;
        int choice = 0;

        try {

            do {
                System.out.println("\n===== WEB SERVER REQUEST SCHEDULER =====");
                System.out.println("1. Add Request");
                System.out.println("2. Process Next Request");
                System.out.println("3. View Pending Requests");
                System.out.println("4. View Completed Requests");
                System.out.println("5. View Request Count");
                System.out.println("6. Exit");

                System.out.print("\nEnter choice: ");

                try {
                    choice = sc.nextInt();
                    sc.nextLine();

                } catch (InputMismatchException e) {
                    System.out.println("Error: Please enter a valid number!");
                    sc.nextLine();
                    continue;
                }

                try {

                    switch (choice) {

                        case 1:
                            System.out.print("Enter URL: ");
                            String url = sc.nextLine();

                            System.out.print("Enter HTTP Method: ");
                            String method = sc.nextLine().trim().toUpperCase();

                            System.out.println("1. High Priority");
                            System.out.println("2. Normal Priority");
                            System.out.print("Enter priority: ");

                            int priority = sc.nextInt();
                            sc.nextLine();

                            WebRequest request = new WebRequest(
                                id, url, method, priority
                            );

                            scheduler.addRequest(request);
                            id++;
                            break;

                        case 2:
                            scheduler.processRequest();
                            break;

                        case 3:
                            scheduler.showPending();
                            break;

                        case 4:
                            scheduler.showCompleted();
                            break;

                        case 5:
                            scheduler.showCount();
                            break;

                        case 6:
                            System.out.println("Scheduler closed.");
                            break;

                        default:
                            System.out.println("Invalid choice!");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Error: Priority must be a number!");
                    sc.nextLine();

                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());

                } catch (NullPointerException e) {
                    System.out.println("Error: Invalid request!");

                } catch (Exception e) {
                    System.out.println("Unexpected Error: " + e.getMessage());
                }

            } while (choice != 6);

        } catch (NoSuchElementException e) {
            System.out.println("\nInput unavailable. Closing scheduler.");

        } finally {
            sc.close();
            System.out.println("Application terminated.");
        }
    }
}
