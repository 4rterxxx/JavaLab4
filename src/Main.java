import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {
        TaskGenerator taskGenerator = new TaskGenerator();
        Thread thread = new Thread(taskGenerator);
        thread.start();
    }
}

class LiftController implements Runnable {
    private boolean flagOfWork;
    private Lift lift1, lift2, lift3;
    private Queue<Integer> requests;

    public void run() {
        while (flagOfWork){
            this.distributeTasks();
        }
    }

    public void distributeTasks() {
        while (!requests.isEmpty()) {
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
            requests.poll();
        }
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            System.out.println("Lift controller is broken!");
        }
    }

    public void addRequest(int task) {
        requests.add(task);
    }
    public void switchOffThread(){
        flagOfWork = false;
    }
    LiftController() {
        lift1 = new Lift();
        lift2 = new Lift();
        lift3 = new Lift();
        flagOfWork = true;
        requests = new ConcurrentLinkedQueue<>();
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
        currentFloor++;
    }

    public void goDown() {
        currentFloor--;
    }

    public void goTo(int goalFloor) {
        while (this.currentFloor > goalFloor) {
            goDown();
        }
        while (this.currentFloor < goalFloor) {
            goUp();
        }
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            System.out.println("Lift is broken!");
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
        Thread thread = new Thread(controller);
        thread.start();
        for (int i = 0; i < numOfTasks; ++i) {
            try {
                controller.addRequest(generateTask());
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.out.println("Task was not generated!");
            }

        }
        controller.switchOffThread();
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
