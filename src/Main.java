import javax.swing.*;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        TaskGenerator taskGenerator = new TaskGenerator();
        taskGenerator.start();
    }
}

class LiftController extends Thread {
    private Lift lift1, lift2;
    private Queue<Integer> requests;

    public void run() {
        this.distributeTasks();
    }
    public synchronized void distributeTasks(){
        while (!requests.isEmpty()){
            System.out.println(requests.peek() + " " + lift1.getCurrentFloor() + " " + lift2.getCurrentFloor());
            if (Math.abs(requests.peek() - lift1.getCurrentFloor()) > Math.abs(requests.peek() - lift2.getCurrentFloor())){
                lift2.goTo(requests.peek());
            }
            else{
                lift1.goTo(requests.peek());
            }
            System.out.println(requests.peek() + " " + lift1.getCurrentFloor() + " " + lift2.getCurrentFloor());
            requests.remove();
        }
    }
    public synchronized void addRequest(int task){
        requests.add(task);
    }
    LiftController() {
        lift1 = new Lift();
        lift2 = new Lift();
        requests = new LinkedList<>();
    }

}

class Lift {
    private int currentFloor;
    private int goalFloor;

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getGoalFloor() {
        return goalFloor;
    }

    public void setGoalFloor(int goalFloor) {
        this.goalFloor = goalFloor;
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

    public void goTo(int goalFloor){
        while(this.currentFloor>goalFloor) {
            goDown();
            currentFloor = goalFloor;
        }
        while(this.currentFloor<goalFloor) {
            goUp();
            currentFloor = goalFloor;
        }

    }
    Lift() {
        currentFloor = 0;
        goalFloor = 0;
    }

}

class TaskGenerator extends Thread{
    public void run() {
        LiftController controller = new LiftController();
        controller.start();
        for (int i=0;i<10;++i){
            try {
                controller.addRequest(generateTask());
                Thread.sleep(1000);
                controller.distributeTasks();
            }
            catch (InterruptedException e){
                System.out.println("Task was not generated!");
            }

        }
    }
    public static int generateTask(){
        Random r = new Random();
        return r.nextInt(10);
    }
}
