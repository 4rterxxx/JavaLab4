import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LiftController implements Runnable {
    private boolean flagOfWork;
    private Lift lift1, lift2, lift3;
    private Queue<Integer> requests;

    public void run() {
        while (flagOfWork){
            this.distributeTasks();
        }
        this.distributeTasks();
    }
    /** Распределяет все запросы среди лифтов */
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
    /** Добавляет запрос в конец очереди запросов */

    public void addRequest(int task) {
        requests.add(task);
    }

    /**
     * Заканчивает поток
     */
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