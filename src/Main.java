public class Main {
    public static void main(String[] args) {
        TaskGenerator taskGenerator = new TaskGenerator();
        Thread thread = new Thread(taskGenerator);
        thread.start();
    }
}


