import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the Starbucks abstract class.
 * Provides a simple structure to store Starbucks locations
 * and retrieve the nearest location based on coordinates.
 * 
 * Author: Shane Paton
 */
public class StudentStarbucks extends Starbucks {

    class Node {
        Locations loc;
        Node left, right;
        boolean isLatSplit;

        Node(Locations loc, boolean isLatSplit) {
            this.loc = loc;
            this.isLatSplit = isLatSplit;
        }
    }

    public Node buildKDTree(List<Locations> locations, boolean isLatSplit) {
        if (locations == null || locations.isEmpty())
            return null;

        // selction sort based on lat or lng
        for (int i = 0; i < locations.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < locations.size(); j++) {
                double valJ = isLatSplit ? locations.get(j).lat : locations.get(j).lng;
                double valMin = isLatSplit ? locations.get(minIndex).lat : locations.get(minIndex).lng;
                if (valJ < valMin) {
                    minIndex = j;
                }
            }
            Locations temp = locations.get(i);
            locations.set(i, locations.get(minIndex));
            locations.set(minIndex, temp);
        }

        int mid = locations.size() / 2;
        Locations median = locations.get(mid);

        Node node = new Node(median, isLatSplit);

        // Recurse on left and right halves
        node.left = buildKDTree(locations.subList(0, mid), !isLatSplit);
        node.right = buildKDTree(locations.subList(mid + 1, locations.size()), !isLatSplit);

        return node;
    }

    private Node root;

    /**
     * Builds a data structure containing all provided Starbucks locations.
     *
     * If two locations have coordinates such that both
     * |x1 - x2| <= 0.00001 and |y1 - y2| <= 0.00001,
     * only one should be added to avoid duplicate entries.
     * Make sure that you are implementing this in your implementation.
     *
     * @param allLocations An array of Locations objects representing all Starbucks
     *                     locations.
     */
    @Override
    public void build(Locations[] allLocations) {
        List<Locations> locations = createList(allLocations);
        this.root = buildKDTree(locations, false);
    }

    /**
     * Creates a List that holds all of the locations that are unique.
     * 
     * @param allLocations An array of Locations objects representing all Starbucks
     *                     locations.
     * @return List<Locations> holding all unique locaionts in an arraylist
     */
    private List<Locations> createList(Locations[] allLocations) {
        List<Locations> linkedList = new ArrayList<Locations>();
        for (Locations location : allLocations) {
            boolean hasLocation = false;
            for (Locations linkedLocation : linkedList) {
                // check it they are a duplicate{
                if ((Math.abs(location.lat - linkedLocation.lat) <= .00001
                        && Math.abs(location.lng - linkedLocation.lng) <= .00001)) {
                    hasLocation = true;
                    break;
                }
            }
            if (!hasLocation) {
                linkedList.add(location);
            }
        }
        return linkedList;
    }

    /**
     * Finds and returns a DEEP copy of the Starbucks location nearest
     * to the given longitude and latitude.
     *
     * @param lng The longitude of the query point, in degrees.
     * @param lat The latitude of the query point, in degrees.
     * @return A deep copy of the nearest Locations object, or null if no locations
     *         exist.
     */
    @Override
    public Locations getNearest(double lng, double lat) {
        if (root == null)
            return null;

        Locations target = new Locations();
        target.lat = lat;
        target.lng = lng;

        Locations nearest = findNearest(root, target, root.loc, distance(root.loc, target));
        return new Locations(nearest);
    }

    /**
     * Finds the nearest location using the kd tree by picking the lat/lng that is
     * closer to the target and keeping 100% accurate by adding in the other branch
     * if the lat/lng is within the distance of the current best distance.
     * 
     * @param node     The node being compared.
     * @param target   The locations we are searching around.
     * @param best     The closest locations so far.
     * @param bestDist The closest distance so far.
     * @return The closest Location to the target.
     */
    private Locations findNearest(Node node, Locations target, Locations best, double bestDist) {
        if (node == null)
            return best;
        // get distence from current root and set it to best if its better
        double currentDist = Starbucks.distance(node.loc.lng, node.loc.lat, target.lng, target.lat);
        if (currentDist < bestDist) {
            bestDist = currentDist;
            best = node.loc;
        }
        // go left based on islatsplit and lat/lng
        boolean goLeft = node.isLatSplit
                ? target.lat < node.loc.lat
                : target.lng < node.loc.lng;
        Node first = goLeft ? node.left : node.right;
        Node second = goLeft ? node.right : node.left;
        best = findNearest(first, target, best, bestDist);
        bestDist = Starbucks.distance(best.lng, best.lat, target.lng, target.lat); // refresh bestDist
        // Now prune if needed based on axis distance
        double axisDiff = node.isLatSplit
                ? Math.abs(node.loc.lat - target.lat)
                : Math.abs(node.loc.lng - target.lng);
        // Convert axisDiff in degrees to rough km using 112km per degree
        double kmCutoff = axisDiff * 112;
        if (kmCutoff < bestDist) {
            best = findNearest(second, target, best, bestDist);
        }
        return best;
    }

    /**
     * Returns the distance between to locations using the method in starbucks.java.
     * 
     * @param a The fist locations.
     * @param b The other locations.
     * @return The total distance between the two locations.
     */
    private double distance(Locations a, Locations b) {
        return Starbucks.distance(a.lng, a.lat, b.lng, b.lat);
    }

}
