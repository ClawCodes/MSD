package assignment09;

public class Main {
    public static void main(String[] args) {
        int numProblemSizes = 10;
        int[] problemSizes = new int[numProblemSizes];
        for (int i = 0; i < problemSizes.length; i++) {
            problemSizes[i] = (int) Math.pow(2, i + 5);
        }

        // Bulk BSP construction
//        BulkConstructionTimer bcT = new BulkConstructionTimer(problemSizes, 100);
//        TimerTemplate.Result[] bulkResults = bcT.run();
//        for (int i = 0; i < bulkResults.length; i++) {
//            System.out.println("Bulk BSP construction results: " + bulkResults[i]);
//        }

         // Empty BSP construction
//        EmptyConstructionTimer ecT = new EmptyConstructionTimer(problemSizes, 100);
//        TimerTemplate.Result[] emptyResults = ecT.run();
//        for (int i = 0; i < emptyResults.length; i++) {
//            System.out.println("Empty BSP construction results: " + emptyResults[i]);
//        }

        CollisionTimer ecC = new CollisionTimer(problemSizes, 10);
        CollisionTimer.bspCollision = true;
        TimerTemplate.Result[] bspCollisionResults = ecC.run();
        for (int i = 0; i < bspCollisionResults.length; i++) {
            System.out.println("BSP collision results: " + bspCollisionResults[i]);
        }

        CollisionTimer.bspCollision = false;
        TimerTemplate.Result[] nonBspCollisionResults = ecC.run();
        for (int i = 0; i < nonBspCollisionResults.length; i++) {
            System.out.println("Non-BSP collision results: " + nonBspCollisionResults[i]);
        }
    }
}
