
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Router {

    private HashMap<Router, Integer> distances;
    private String name;
    public Router(String name) {
        this.distances = new HashMap<>();
        this.name = name;
    }


    private void broadcastDistances() throws InterruptedException {
        for (Neighbor neighbor : Network.getNeighbors(this)) {
            Message fwdTableMsg = new Message(
                    this, neighbor.router, new HashMap<>(distances)
            );
            Network.sendDistanceMessage(fwdTableMsg);
        }
    }

    public void onInit() throws InterruptedException {
        distances.put(this, 0);
        for (Neighbor neighbor : Network.getNeighbors(this)) {
            distances.put(neighbor.router, neighbor.cost);
        }
        broadcastDistances();
    }

    public void onDistanceMessage(Message message) throws InterruptedException {
        boolean distancesChanged = false;
        for (HashMap.Entry<Router, Integer> entry : message.distances.entrySet()) {
            int newDistance = entry.getValue() + distances.getOrDefault(message.sender, Integer.MAX_VALUE);
            if (newDistance < distances.getOrDefault(entry.getKey(), Integer.MAX_VALUE)) {
                distances.put(entry.getKey(), newDistance);
                distancesChanged = true;
            }
            if (distancesChanged)
                broadcastDistances();
        }
    }


    public void dumpDistanceTable() {
        System.out.println("router: " + this);
        for(Router r : distances.keySet()){
            System.out.println("\t" + r + "\t" + distances.get(r));
        }
    }

    @Override
    public String toString(){
        return "Router: " + name;
    }
}
