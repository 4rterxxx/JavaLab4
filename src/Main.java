import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        TaskGenerator taskGenerator = new TaskGenerator();
        Thread thread = new Thread(taskGenerator);
        thread.start();
    }
}

class LiftController implements Runnable {
    private Lift lift1, lift2, lift3;
    private Queue<Integer> requests;

    public void run() {
        this.distributeTasks();
    }

    public synchronized void distributeTasks() {
        while (!requests.isEmpty()) {
            System.out.println(requests.size());
            System.out.println("lifti " + Thread.currentThread());
            System.out.println("Текущее состояние: " + lift1.getCurrentFloor() + " " + lift2.getCurrentFloor() + " " + lift3.getCurrentFloor());
            System.out.println("Поступил запрос с " + requests.peek() + " этажа");
            int minDistance = Math.min(lift1.getDistance(requests.peek()), Math.min(lift2.getDistance(requests.peek()), lift3.getDistance(requests.peek())));
            if (minDistance == lift1.getDistance(requests.peek())) {
                System.out.println("Отправили 1ый лифт");
                lift1.goTo(requests.peek());
                lift1.goTo(TaskGenerator.generateTask(lift1.getCurrentFloor()));
                System.out.println("Отвезли пользователя на " + lift1.getCurrentFloor() + " этаж");
            } else if (minDistance == lift2.getDistance(requests.peek())) {
                System.out.println("Отправили 2ой лифт");
                lift2.goTo(requests.peek());
                lift2.goTo(TaskGenerator.generateTask(lift2.getCurrentFloor()));
                System.out.println("Отвезли пользователя на " + lift2.getCurrentFloor() + " этаж");

            } else {
                System.out.println("Отправили 3ий лифт");
                lift3.goTo(requests.peek());
                lift3.goTo(TaskGenerator.generateTask(lift3.getCurrentFloor()));
                System.out.println("Отвезли пользователя на " + lift3.getCurrentFloor() + " этаж");
            }
            System.out.println();
            requests.remove();
        }
    }

    public synchronized void addRequest(int task) {
        requests.add(task);
    }

    LiftController() {
        lift1 = new Lift();
        lift2 = new Lift();
        lift3 = new Lift();
        requests = new LinkedList<>();
    }

}

class Lift {
    private static final int numOfFloors = 20;
    private int currentFloor;

    public static int getNumOfFloors() {
        return numOfFloors;
    }

    public int getDistance(int goalFloor) {
        return Math.abs(goalFloor - currentFloor);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }


    public void goUp() {
        try {
            Thread.sleep(1000);
            currentFloor++;
        } catch (InterruptedException e) {
            System.out.println("Lift is broken!");
        }

    }

    public void goDown() {
        try {
            Thread.sleep(1000);
            currentFloor--;
        } catch (InterruptedException e) {
            System.out.println("Lift is broken!");
        }
    }

    public void goTo(int goalFloor) {
        while (this.currentFloor > goalFloor) {
            goDown();
            currentFloor = goalFloor;
        }
        while (this.currentFloor < goalFloor) {
            goUp();
            currentFloor = goalFloor;
        }

    }

    Lift() {
        currentFloor = 0;
    }

}

class TaskGenerator implements Runnable {
    private static final int numOfTasks = 10;

    public void run() {
        LiftController controller = new LiftController();
        for (int i = 0; i < numOfTasks; ++i) {
            System.out.println("Taski " + Thread.currentThread());
            try {
                controller.addRequest(generateTask());
                Thread.sleep(1000);
                new Thread(controller).start();
            } catch (InterruptedException e) {
                System.out.println("Task was not generated!");
            }

        }
    }

    public static int generateTask() {
        Random r = new Random();
        return r.nextInt(Lift.getNumOfFloors() + 1);
    }

    public static int generateTask(int exception) {
        Random r = new Random();
        int res = r.nextInt(Lift.getNumOfFloors() + 1);
        while (res == exception) res = r.nextInt(Lift.getNumOfFloors() + 1);
        return res;
    }
}
