import java.util.Random;

public class TaskGenerator implements Runnable {
    /**
     * Колличество вызовов лифта
     */
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

    /**
     * Иммитирует вызов лифта на определенном этаже
     */
    public static int generateTask() {
        Random r = new Random();
        return r.nextInt(Lift.getNumOfFloors() + 1);
    }

    /**
     * Иммитирует случайное нажатие кнопки лифта
     * Возвращает случайное число не равное переданному параметру
     */
    public static int generateTask(int exception) {
        Random r = new Random();
        int res = r.nextInt(Lift.getNumOfFloors() + 1);
        while (res == exception) res = r.nextInt(Lift.getNumOfFloors() + 1);
        return res;
    }
}
