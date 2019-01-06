package model.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Point2D;
import model.Model;
import model.component.Field;

public class AStar implements PathFinder {

	private Model data_model;

	public AStar(Model data_model) {
		this.data_model = data_model;
	}

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
	public List<Field> findPath(Field start, Field goal) {

		// final int size = graph.getVertices().size(); // used to size data structures
		// appropriately

		final Set<Field> closedSet = new HashSet<Field>(); // The set of nodes already
															// evaluated.
		final List<Field> openSet = new ArrayList<Field>(); // The set of tentative nodes to be
															// evaluated, initially containing
															// the start node

		Field starting_node = start;
		openSet.add(starting_node);

		final Map<Field, Field> cameFrom = new HashMap<Field, Field>(); // The
																		// map
																		// of
																		// navigated
																		// nodes.

		final Map<Field, Integer> gScore = new HashMap<Field, Integer>(); // Cost from start along
																			// best known path.
		gScore.put(starting_node, 0);

		// Estimated total cost from start to goal through y.
		final Map<Field, Double> fScore = new HashMap<Field, Double>();

		for (Field v : this.data_model.getFields())
			fScore.put(v, (double) Integer.MAX_VALUE);

		Field goal_node = goal;

		fScore.put(starting_node, heuristicCostEstimate(starting_node, goal_node));

		final Comparator<Field> comparator = new Comparator<Field>() {

			public int compare(Field o1, Field o2) {

				if (fScore.get(o1) < fScore.get(o2))
					return -1;
				if (fScore.get(o2) < fScore.get(o1))
					return 1;

				return 0;
			}
		};

		while (!openSet.isEmpty()) {

			final Field current = openSet.get(0);

			if (current.equals(goal))
				return reconstructPath(cameFrom, goal_node);

			openSet.remove(0);
			closedSet.add(current);

			for (Field neighbour : this.data_model.getFreeNeighbours(current)) { // current is Field should be field

				if (closedSet.contains(neighbour))
					continue; // Ignore the neighbor which is already evaluated.

				// final int tenativeGScore = gScore.get(current) + distanceBetween(current,
				// neighbor); // length of this
				// path.

				final int tenativeGScore = gScore.get(current) + 1;

				if (!openSet.contains(neighbour))
					openSet.add(neighbour); // Discover a new node
				else if (tenativeGScore >= gScore.get(neighbour))
					continue;

				// This path is the best until now. Record it!
				cameFrom.put(neighbour, current);
				gScore.put(neighbour, tenativeGScore);
				final double estimatedFScore = gScore.get(neighbour) + heuristicCostEstimate(neighbour, goal_node);
				fScore.put(neighbour, estimatedFScore);

				// fScore has changed, re-sort the list
				Collections.sort(openSet, comparator);
			}
		}

		return null;
	}

	protected double heuristicCostEstimate(Field start, Field goal) {

		/*
		 * axial hex distance function hex_distance(a, b): return (abs(a.q - b.q) +
		 * abs(a.q + a.r - b.q - b.r) + abs(a.r - b.r)) / 2
		 * 
		 */

		Point2D a = start.getStoragePosition();
		Point2D b = start.getStoragePosition();

		return (Math.abs(a.getX() - b.getX()) + Math.abs(a.getX() + a.getY() - b.getX() - b.getY())
				+ Math.abs(a.getY() + b.getY())) / 2;

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
