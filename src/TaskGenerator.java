import java.util.Random;

public class TaskGenerator implements Runnable {
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
