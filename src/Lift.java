public class Lift {
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
        } catch (InterruptedException e) {
            System.out.println("Lift is broken!");
        }
    }

    Lift() {
        currentFloor = 0;
    }

}