package model.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import root.model.Model;
import root.model.action.move.PathFinder;
import root.model.component.Field;

public class AStar implements PathFinder {

	/**
	 * Find the path using the A* algorithm from start vertex to end vertex or NULL
	 * if no path exists.
	 * 
	 * @param graph
	 *            Graph to search.
	 * @param start
	 *            Start vertex.
	 * @param goal
	 *            Goal vertex.
	 * 
	 * @return List of Edges to get from start to end or NULL if no path exists.
	 */
	public List<Field> findPath(Model dataModel, Field start, Field goal) {

		// final int size = graph.getVertices().size(); // used to size data structures
		// appropriately

		// Estimated total cost from start to goal through y.
		final Map<Field, Double> fScore = new HashMap<Field, Double>();

		final Comparator<Field> comparator = (o1, o2) -> {
			return Double.compare(fScore.get(o1), fScore.get(o2));
		};
		// The set of nodes already evaluated.
		final Set<Field> closedSet = new HashSet<Field>();
		// The set of tentative nodes to be evaluated, initially containing
		// the start node
		final PriorityQueue<Field> openSet = new PriorityQueue<Field>(comparator);

		Field starting_node = start;
		openSet.add(starting_node);

		// The map of navigated nodes.
		final Map<Field, Field> cameFrom = new HashMap<Field, Field>();

		// Cost from start along best known path.
		final Map<Field, Integer> gScore = new HashMap<Field, Integer>();
		gScore.put(starting_node, 0);

		for (Field v : dataModel.getFields())
			fScore.put(v, (double) Integer.MAX_VALUE);

		Field goalNode = goal;

		fScore.put(starting_node, heuristicCostEstimate(dataModel, starting_node, goalNode));

		// Comparator<Field> comparator = (f1, f2) -> ((Double) fScore.get(f1)).compareTo(fScore.get(f2));

		while (!openSet.isEmpty()) {

			final Field current = openSet.remove();

			if (current.equals(goal))
				return reconstructPath(cameFrom, goalNode);

			closedSet.add(current);
			for (Field neighbor : dataModel.getFreeNeighbours(current, 1)) { // current is Field should be field

				// Ignore the neighbor which is already evaluated.
				if (closedSet.contains(neighbor))
					continue;

				// final int tenativeGScore = gScore.get(current) + distanceBetween(current,
				// neighbor); // length of this
				// path.

				final int tenativeGScore = gScore.get(current) + 1;

				var alreadyFound = openSet.contains(neighbor);

				if (!alreadyFound || tenativeGScore < gScore.get(neighbor)) {
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, tenativeGScore);
					double estimatedFScore = gScore.get(neighbor)
							+ heuristicCostEstimate(dataModel, neighbor, goalNode);
					fScore.put(neighbor, estimatedFScore);

					if(alreadyFound){
						openSet.remove(neighbor);
					}

					openSet.add(neighbor);
					// Discover a new node
					// else if (tenativeGScore >= gScore.get(neighbor))
					// 	continue;
				} 
				// fScore has changed, re-sort the list
				// Used at the time when openSet was impelmented as a simple list. 
				// Collections.sort(openSet, comparator);
			}
		}

		return null;
	}

	protected double heuristicCostEstimate(Model dataModel, Field start, Field goal) {

		/*
		 * axial hex distance function hex_distance(a, b): return (abs(a.q - b.q) +
		 * abs(a.q + a.r - b.q - b.r) + abs(a.r - b.r)) / 2
		 * 
		 */

		// Point2D a = start.getStoragePosition();
		// Point2D b = start.getStoragePosition();

		// return (Math.abs(a.getX() - b.getX()) + Math.abs(a.getX() + a.getY() - b.getX() - b.getY())
		// 		+ Math.abs(a.getY() + b.getY())) / 2;
		return dataModel.distance(start, goal);

	}

	private List<Field> reconstructPath(Map<Field, Field> cameFrom, Field current) {
		final List<Field> totalPath = new ArrayList<Field>();

		while (current != null) {

			final Field previous = current;

			current = cameFrom.get(current);

			if (current != null) {

				// final Field edge = current.getEdge(previous);
				totalPath.add(previous);

			}

		}

		Collections.reverse(totalPath);
		return totalPath;
	}

}
