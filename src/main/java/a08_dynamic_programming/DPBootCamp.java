package a08_dynamic_programming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.junit.Assert;

import util.Point;

/**
 * <ul>
 * <li>When DP is implemented recursively the cache is typically a dynamic data structure such as a
 * hash table or a BST; when it is implemented iteratively the cache is usually a one- or
 * multidimensional array.</li>
 * <li>All recursive algorithms can be implemented iteratively, although sometimes the code to do so
 * is much more complex. Each recursive call adds a new layer to the stack, which means that if your
 * algorithm recurses to a depth of n, it uses at least O(n) memory.</li>
 * <li>Dynamic programming is mostly just a matter of taking a recursive algorithm and finding the
 * overlapping subproblems (that is, the repeated calls). You then cache those results for future
 * recursive calls.</li>
 * <li>The best algorithms for performing aggregation queries on log file data are often streaming
 * algorithms.</li>
 * </ul>
 * 
 * @author lchen
 *
 */
public class DPBootCamp {

	/**
	 * You are climbing a stair case. It takes n steps to reach to the top.
	 * 
	 * Each time you can climb 1 to k steps. In how many distinct ways can you climb to the top?
	 * 
	 * Time complexity is O(kn), the space complexity is O(n)
	 * 
	 * @param n
	 * @param k
	 * @return
	 */
	public static int climbStairs(int n, int k) {
		return climbStairs(n, k, new int[n + 1]);
	}

	private static int climbStairs(int n, int k, int[] memo) {
		if (n < 0)
			return 0;
		else if (n == 0)
			return 1; // use one or zero?
		else if (n == 1)
			return 1;

		if (memo[n] == 0) {
			// for (int i = 1; i <= k; i++) {
			// use n - i >= 0 to reduce call times!
			for (int i = 1; i <= k && n - i >= 0; i++) {
				memo[n] += climbStairs(n - i, k, memo);
			}
		}

		return memo[n];
	}

	/**
	 * Given a non-empty array containing only positive integers, find if the array can be partitioned
	 * into two subsets such that the sum of elements in both subsets is equal.
	 * 
	 * Note: Each of the array element will not exceed 100. The array size will not exceed 200. Example
	 * 1:
	 * 
	 * Input: [1, 5, 11, 5]
	 * 
	 * Output: true
	 * 
	 * Base case: dp[0][0] is true; (zero number consists of sum 0 is true)
	 * 
	 * Transition function: For each number, if we don't pick it, dp[i][j] = dp[i-1][j], which means if
	 * the first i-1 elements has made it to j, dp[i][j] would also make it to j (we can just ignore
	 * nums[i]). If we pick nums[i]. dp[i][j] = dp[i-1][j-nums[i]], which represents that j is composed
	 * of the current value nums[i] and the remaining composed of other previous numbers. <br>
	 * Thus, the transition function is dp[i][j] = dp[i-1][j] || dp[i-1][j-nums[i]]
	 * 
	 * @author lchen
	 *
	 *
	 */
	public boolean canPartitionArray(int[] nums) {
		int subgroups = 2;
		int sum = Arrays.stream(nums).sum();

		if (sum % subgroups != 0)
			return false;

		int target = sum / subgroups;
		boolean[] dp = new boolean[target + 1];
		dp[0] = true;

		for (int num : nums) {
			for (int i = target; i >= num; i--) {
				dp[i] = dp[i] || dp[i - num]; // not pick it or pick it!
			}
			// System.out.println(num + ": " + Arrays.toString(dp));
		}

		return dp[target];
	}

	/**
	 * Write a program that takes a final score and scores for individual plays, and returns the number
	 * of combinations of plays that result in the final score.
	 * 
	 * @author lchen
	 * 
	 */
	public static int combinationsForFinalScore(int finalScore, List<Integer> playScores) {
		int[] dp = new int[finalScore + 1];
		dp[0] = 1; // One way to reach 0.

		for (int i = 0; i < playScores.size(); ++i) {
			for (int j = 1; j <= finalScore; ++j) {
				int withoutThisPlay = i - 1 >= 0 ? dp[j] : 0;
				int withThisPlay = j >= playScores.get(i) ? dp[j - playScores.get(i)] : 0;
				dp[j] = withoutThisPlay + withThisPlay;
			}
		}

		return dp[finalScore];
	}

	/**
	 * We partition a row of numbers A into at most K adjacent (non-empty) groups, then our score is the
	 * sum of the average of each group. What is the largest score we can achieve?
	 * 
	 * Note that our partition must use every number in A, and that scores are not necessarily integers.
	 * 
	 * <pre>
	Example:
	Input: 
	A = [9,1,2,3,9]
	K = 3
	Output: 20
	Explanation: 
	The best choice is to partition A into [9], [1, 2, 3], [9]. The answer is 9 + (1 + 2 + 3) / 3 + 9 = 20.
	We could have also partitioned A into [9, 1], [2], [3, 9], for example.
	That partition would lead to a score of 5 + 2 + 6 = 13, which is worse.
	 * </pre>
	 */
	public double largestSumOfAverages(int[] A, int K) {
		int N = A.length;
		double[] P = new double[N + 1];
		for (int i = 0; i < N; i++)
			P[i + 1] = P[i] + A[i];

		// starts with base case
		double[] dp = new double[N];
		for (int i = 0; i < N; i++)
			dp[i] = (P[N] - P[i]) / (N - i);

		// sum up to K - 1 times
		for (int k = 0; k < K - 1; k++)
			for (int i = 0; i < N; i++)
				for (int j = i + 1; j < N; j++)
					dp[i] = Math.max(dp[i], (P[j] - P[i]) / (j - i) + dp[j]);

		return dp[0];
	}

	/**
	 * Given a triangle, find the minimum path sum from top to bottom. Each step you may move to
	 * adjacent numbers on the row below.
	 */
	public int minPathSum(int[][] grid) {
		int[] dp = new int[grid[0].length];
		for (int i = grid.length - 1; i >= 0; i--) {
			for (int j = grid[0].length - 1; j >= 0; j--) {
				if (i == grid.length - 1 && j != grid[0].length - 1)
					dp[j] = grid[i][j] + dp[j + 1];
				else if (j == grid[0].length - 1 && i != grid.length - 1)
					dp[j] = grid[i][j] + dp[j];
				else if (j != grid[0].length - 1 && i != grid.length - 1)
					dp[j] = grid[i][j] + Math.min(dp[j], dp[j + 1]);
				else
					dp[j] = grid[i][j];
			}
		}
		return dp[0];
	}

	/**
	 * Given a 2D binary matrix filled with 0's and 1's, find the largest square containing only 1's and
	 * return its area.
	 */
	public int maximalSquare(char[][] matrix) {
		int rows = matrix.length, cols = rows > 0 ? matrix[0].length : 0;
		int[][] dp = new int[rows + 1][cols + 1];
		int maxLen = 0;
		// starts with 1 instead of 0 in favor of coding
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= cols; j++) {
				if (matrix[i - 1][j - 1] == '1') {
					dp[i][j] = Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1]) + 1;
					maxLen = Math.max(maxLen, dp[i][j]);
				}
			}
		}
		return maxLen * maxLen;
	}

	// we can also use 1D array with the equation: dp[j] = min(dp[j−1],dp[j],prev)
	public int maximalSquare2(char[][] matrix) {
		int rows = matrix.length, cols = rows > 0 ? matrix[0].length : 0;
		int[] dp = new int[cols + 1];
		int prev = 0, maxLen = 0;
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= cols; j++) {
				int temp = dp[j];
				if (matrix[i - 1][j - 1] == '1') {
					dp[j] = Math.min(Math.min(prev, dp[j - 1]), dp[j]) + 1;
					maxLen = Math.max(maxLen, dp[j]);
				} else {
					dp[j] = 0;
				}
				prev = temp;
			}
		}
		return maxLen * maxLen;
	}

	/**
	 * Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle containing only 1's
	 * and return its area.
	 */
	public int maximalRectangle(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
			return 0;
		int[] heights = new int[matrix[0].length];
		// first row heights
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[0][i] == '1')
				heights[i] = 1;
		}
		int maxArea = maxAreaInLine(heights);
		// rest row heights
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == '1')
					heights[j] += 1;
				else
					heights[j] = 0;
			}
			maxArea = Math.max(maxArea, maxAreaInLine(heights));
		}
		return maxArea;
	}

	private int maxAreaInLine(int[] heights) {
		if (heights == null || heights.length == 0)
			return 0;
		Stack<Integer> stack = new Stack<>();
		int maxArea = 0;
		for (int i = 0; i <= heights.length; i++) {
			int height = i == heights.length ? 0 : heights[i];
			if (stack.isEmpty() || height >= heights[stack.peek()]) {
				stack.push(i);
			} else {
				int tp = stack.pop();
				maxArea = Math.max(maxArea, heights[tp] * (stack.isEmpty() ? i : i - 1 - stack.peek()));
				i--; // keep trying until!
			}
		}
		return maxArea;
	}

	/**
	 * Given n, how many structurally unique BST's (binary search trees) that store values 1 ... n?
	 * 
	 * Example:
	 * 
	 * <pre>
	Input: 3
	Output: 5
	Explanation:
	Given n = 3, there are a total of 5 unique BST's:
	
	   1         3     3      2      1
	    \       /     /      / \      \
	     3     2     1      1   3      2
	    /     /       \                 \
	   2     1         2                 3
	 * </pre>
	 */
	public int numTrees(int n) {
		if (n < 1)
			return 0;
		int[] dp = new int[n + 1];
		// Base cases: n == 0 (empty tree) or n == 1 (only a root)
		dp[0] = dp[1] = 1;
		// F(i, n) = G(i-1) * G(n-i) 1 <= i <= n
		for (int i = 2; i <= n; i++) {
			for (int j = 1; j <= i; j++) {
				dp[i] += dp[j - 1] * dp[i - j];
			}
		}
		return dp[n];
	}

	/**
	 * A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).
	 * 
	 * The robot can only move either down or right at any point in time. The robot is trying to reach
	 * the bottom-right corner of the grid (marked 'Finish' in the diagram below).
	 * 
	 * How many possible unique paths are there?
	 * 
	 * @param grid
	 * @return
	 */
	public int findHowManyUniquePathsInGrid(int m, int n) {
		int[] dp = new int[n];
		dp[0] = 1;
		for (int i = 0; i < m; i++) {
			for (int j = 1; j < n; j++) {
				dp[j] += dp[j - 1];
			}
		}
		return dp[n - 1];
	}

	// use traditional 2 dimensional array
	public int findHowManyUniquePathsInGrid2(int m, int n) {
		int[][] grid = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (i == 0 || j == 0)
					grid[i][j] = 1;
				else
					grid[i][j] = grid[i - 1][j] + grid[i][j - 1];
			}
		}
		return grid[m - 1][n - 1];
	}

	/**
	 * 
	 * Now consider if some obstacles are added to the grids. How many unique paths would there be?
	 * 
	 * An obstacle and empty space is marked as 1 and 0 respectively in the grid.
	 * 
	 * 
	 * @param obstacleGrid
	 * @return
	 */
	public int findHowManyUniquePathsInGridWithObstacles(int[][] obstacleGrid) {
		int width = obstacleGrid[0].length;
		int[] dp = new int[width];
		dp[0] = 1;
		for (int[] row : obstacleGrid) {
			for (int j = 0; j < width; j++) {
				if (row[j] == 1)
					dp[j] = 0;
				else if (j > 0)
					dp[j] += dp[j - 1];
			}
		}
		return dp[width - 1];
	}

	/**
	 * Design an algorithm to find a path in a Maze/Grid
	 * 
	 * @param maze
	 * @return
	 */
	public List<Point> findOnePathInGrid(boolean[][] maze) {
		if (maze == null || maze.length == 0)
			return null;
		List<Point> path = new ArrayList<>();
		Set<Point> visitedPoints = new HashSet<>();
		if (findOnePathInGrid(maze, maze.length - 1, maze[0].length - 1, path, visitedPoints))
			return path;
		return null;
	}

	private boolean findOnePathInGrid(boolean[][] maze, int row, int col, List<Point> path, Set<Point> visitedPoints) {
		if (col < 0 || row < 0 || !maze[row][col]) // Out of bounds or not available
			return false;
		Point p = new Point(row, col);
		if (visitedPoints.contains(p)) // Already visited this cell
			return false;
		boolean isAtOrigin = (row == 0) && (col == 0);
		// If there's a path from the start to my current location, add my location.
		if (isAtOrigin || findOnePathInGrid(maze, row, col - 1, path, visitedPoints)
				|| findOnePathInGrid(maze, row - 1, col, path, visitedPoints)) {
			path.add(p);
			return true;
		}
		visitedPoints.add(p); // Cache result
		return false;
	}

	/**
	 * There is a ball in a maze with empty spaces and walls. The ball can go through empty spaces by
	 * rolling up, down, left or right, but it won't stop rolling until hitting a wall. When the ball
	 * stops, it could choose the next direction.
	 * 
	 * Given the ball's start position, the destination and the maze, determine whether the ball could
	 * stop at the destination.
	 * 
	 * The maze is represented by a binary 2D array. 1 means the wall and 0 means the empty space. You
	 * may assume that the borders of the maze are all walls. The start and destination coordinates are
	 * represented by row and column indexes.
	 * 
	 * <pre>
	Example 1
	
	Input 1: a maze represented by a 2D array
	
	0 0 1 0 0
	0 0 0 0 0
	0 0 0 1 0
	1 1 0 1 1
	0 0 0 0 0
	
	Input 2: start coordinate (rowStart, colStart) = (0, 4)
	Input 3: destination coordinate (rowDest, colDest) = (4, 4)
	
	Output: true
	Explanation: One possible way is : left -> down -> left -> down -> right -> down -> right.
	 * </pre>
	 * 
	 * @param maze
	 * @param start
	 * @param destination
	 * @return
	 */
	public boolean hasPathInMaze(int[][] maze, int[] start, int[] destination) {
		if (start[0] == destination[0] && start[1] == destination[1])
			return true;
		int m = maze.length, n = maze[0].length;
		int[][] dirs = new int[][] { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
		boolean[][] visited = new boolean[m][n];
		Queue<int[]> queue = new LinkedList<>();
		visited[start[0]][start[1]] = true;
		queue.offer(start);
		while (!queue.isEmpty()) {
			int[] p = queue.poll();
			for (int[] dir : dirs) {
				int x = p[0], y = p[1];
				// keep rolling on this direction until hit a wall!
				while (x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0) {
					x += dir[0];
					y += dir[1];
				}
				// back to empty space
				x -= dir[0];
				y -= dir[1];
				if (visited[x][y])
					continue;
				if (x == destination[0] && y == destination[1])
					return true;
				queue.offer(new int[] { x, y });
				visited[x][y] = true;
			}
		}
		return false;
	}

	/**
	 * Find the shortest distance for the ball to stop at the destination. The distance is defined by
	 * the number of empty spaces traveled by the ball from the start position (excluded) to the
	 * destination (included). If the ball cannot stop at the destination, return -1.
	 * 
	 * Use Dijkstra Algorithm with PriorityQueue to track which is the unvisited node at the shortest
	 * distance from the start node.
	 * 
	 * Time complexity: O(mn*log(mn)); Space complexity: O(mn)
	 */
	public int shortestDistanceInMaze(int[][] maze, int[] start, int[] destination) {
		int m = maze.length, n = maze[0].length;
		int[][] lens = new int[m][n];
		for (int i = 0; i < m * n; i++)
			lens[i / n][i % n] = Integer.MAX_VALUE;

		int[][] dirs = new int[][] { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
		Queue<int[]> queue = new PriorityQueue<>((a, b) -> (a[2] - b[2]));
		queue.offer(new int[] { start[0], start[1], 0 });

		while (!queue.isEmpty()) {
			int[] p = queue.poll();
			if (lens[p[0]][p[1]] <= p[2]) // already found shorter route
				continue;
			lens[p[0]][p[1]] = p[2];
			for (int[] dir : dirs) {
				int x = p[0], y = p[1], l = p[2];
				while (x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0) {
					x += dir[0];
					y += dir[1];
					l++;
				}
				x -= dir[0];
				y -= dir[1];
				l--;
				queue.offer(new int[] { x, y, l });
			}
		}

		return lens[destination[0]][destination[1]] == Integer.MAX_VALUE ? -1 : lens[destination[0]][destination[1]];
	}

	/**
	 * Given an unsorted array of integers, find the length of the longest consecutive elements
	 * sequence.
	 * 
	 * For example, Given [100, 4, 200, 1, 3, 2], The longest consecutive elements sequence is [1, 2, 3,
	 * 4]. Return its length: 4.
	 * 
	 * Your algorithm should run in O(n) complexity.
	 */
	public int longestConsecutiveSequence(int[] nums) {
		Set<Integer> set = new HashSet<>();
		for (int num : nums)
			set.add(num);
		int longestStreak = 0;
		for (int num : nums) {
			// only check the beginning number of the sequence
			if (!set.contains(num - 1)) {
				int currentNum = num;
				int currentStreak = 1;
				// loop until reach the end of the sequence
				while (set.contains(currentNum + 1)) {
					currentNum += 1;
					currentStreak += 1;
				}
				longestStreak = Math.max(longestStreak, currentStreak);
			}
		}
		return longestStreak;
	}

	/**
	 * Given an unsorted array of integers, find the number of longest increasing subsequence.
	 * 
	 * <pre>
	Example 1:
	Input: [1,3,5,4,7]
	Output: 2
	Explanation: The two longest increasing subsequence are [1, 3, 4, 7] and [1, 3, 5, 7].
	
	Example 2:
	Input: [2,2,2,2,2]
	Output: 5
	Explanation: The length of longest continuous increasing subsequence is 1, 
	and there are 5 subsequences' length is 1, so output 5.
	 * </pre>
	 * 
	 * The idea is to use two arrays len[n] and cnt[n] to record the maximum length of Increasing
	 * Subsequence and the corresponding number of these sequence which ends with nums[i], respectively.
	 * O(n^2) complexity.
	 */
	public int longestIncreasingSequence(int[] nums) {
		int result = 0, maxLen = 0;
		// lengths[i] = length of longest ending in nums[i]
		// counts[i] = number of longest ending in nums[i]
		int[] lengths = new int[nums.length];
		int[] counts = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			lengths[i] = counts[i] = 1;
			for (int j = 0; j < i; j++) {
				if (nums[i] > nums[j]) {
					// nums[i] can be appended to a longest sequence ending at nums[j].
					int newLen = lengths[j] + 1;
					if (lengths[i] == newLen)
						counts[i] += counts[j];
					else if (lengths[i] < newLen) {
						lengths[i] = newLen;
						counts[i] = counts[j];
					}
				}
			}
			if (maxLen == lengths[i])
				result += counts[i];
			else if (maxLen < lengths[i]) {
				maxLen = lengths[i];
				result = counts[i];
			}
		}
		return result;
	}

	/**
	 * Given two words word1 and word2, find the minimum number of steps required to convert word1 to
	 * word2. (each operation is counted as 1 step.)
	 * 
	 * You have the following 3 operations permitted on a word:
	 * 
	 * a) Insert a character b) Delete a character c) Replace a character
	 * 
	 * @author lchen
	 *
	 */
	public int minimumEditDistance(String s, String t) {
		int[][] distances = new int[s.length()][t.length()];
		for (int[] row : distances)
			Arrays.fill(row, -1);
		return computeEditDistance(s, s.length() - 1, t, t.length() - 1, distances);
	}

	private int computeEditDistance(String w1, int i, String w2, int j, int[][] distances) {
		if (i < 0)
			return j + 1;
		else if (j < 0)
			return i + 1;
		if (distances[i][j] == -1) {
			if (w1.charAt(i) == w2.charAt(j)) {
				distances[i][j] = computeEditDistance(w1, i - 1, w2, j - 1, distances);
			} else {
				int insert = computeEditDistance(w1, i, w2, j - 1, distances);
				int delete = computeEditDistance(w1, i - 1, w2, j, distances);
				int replace = computeEditDistance(w1, i - 1, w2, j - 1, distances);
				distances[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
			}
		}
		return distances[i][j];
	}

	/**
	 * A magic index in an array A[1...n-1] is defined to be an index such that A[i] = i. Given a sorted
	 * array of integers those could be not distinct, write a method to find a magic index, if one
	 * exists, in array A.
	 * 
	 */
	public int magicIndexWithDups(int[] array) {
		return magicIndexWithDups(array, 0, array.length - 1);
	}

	private int magicIndexWithDups(int[] array, int start, int end) {
		if (end < start) {
			return -1;
		}

		int midIndex = (start + end) / 2;
		int midValue = array[midIndex];
		if (midValue == midIndex)
			return midIndex;

		// Search Left
		int leftIndex = Math.min(midIndex - 1, midValue);
		int left = magicIndexWithDups(array, 0, leftIndex);
		if (left >= 0)
			return left;

		// Search Right
		int rightIndex = Math.max(midIndex + 1, midValue);
		int right = magicIndexWithDups(array, rightIndex, end);
		return right;
	}

	/**
	 * Write a recursive function to multiply two positive integers without using the * operator (or /
	 * operator). You can use addition, subtraction, and bit shifting, but you should minimize the
	 * number of those operations.
	 */
	public int multiplyByUsingAddition(int a, int b) {
		int smaller = a > b ? b : a;
		int bigger = a > b ? a : b;
		return minProductRecursive(smaller, bigger);
	}

	private int minProductRecursive(int smaller, int bigger) {
		if (smaller == 0)
			return 0;
		if (smaller == 1)
			return bigger;

		int s = smaller >> 1; // Divide by 2
		int halfProduct = minProductRecursive(s, bigger);

		if ((smaller & 1) == 0)
			return halfProduct + halfProduct;
		else
			return halfProduct + halfProduct + bigger;
	}

	public List<String> generateParentheses(int count) {
		char[] str = new char[count * 2];
		List<String> list = new ArrayList<>();
		generateParentheses(list, count, count, str, 0);
		return list;
	}

	private void generateParentheses(List<String> list, int leftRem, int rightRem, char[] str, int index) {
		if (leftRem < 0 || rightRem < leftRem)
			return; // invalid state

		if (leftRem == 0 && rightRem == 0) {
			list.add(String.copyValueOf(str));
		} else {
			str[index] = '('; // Add left and recurse
			generateParentheses(list, leftRem - 1, rightRem, str, index + 1);
			str[index] = ')'; // Add right and recurse
			generateParentheses(list, leftRem, rightRem - 1, str, index + 1);
		}
	}

	/**
	 *
	 * Given a triangle, find the minimum path sum from top to bottom. Each step you may move to
	 * adjacent numbers on the row below.
	 * 
	 * <pre>
	For example, given the following triangle
	[
	     [2],
	    [3,4],
	   [6,5,7],
	  [4,1,8,3]
	]
	The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11).
	 * </pre>
	 * 
	 * Note: Bonus point if you are able to do this using only O(n) extra space, where n is the total
	 * number of rows in the triangle.
	 * 
	 * <br />
	 * 
	 * Use 'Bottom-up' DP, the min pathsum at the ith node on the kth row would be the lesser of the
	 * pathsums of its two children plus the value of itself.
	 * 
	 * i.e. minLens[k][i] = min(minLens[k+1][i], minLens[k+1][i+1]) + triangle[k][i];
	 * 
	 * @author lchen
	 *
	 */
	public int minimumTotalInTriangle(List<List<Integer>> triangle) {
		int[] minLens = new int[triangle.size() + 1];
		for (int layer = triangle.size() - 1; layer >= 0; layer--) {
			for (int i = 0; i < triangle.get(layer).size(); i++) {
				minLens[i] = Math.min(minLens[i], minLens[i + 1]) + triangle.get(layer).get(i);
			}
		}
		return minLens[0];
	}

	/**
	 * We have two integer sequences A and B of the same non-zero length.
	 * 
	 * We are allowed to swap elements A[i] and B[i]. Note that both elements are in the same index
	 * position in their respective sequences.
	 * 
	 * At the end of some number of swaps, A and B are both strictly increasing. (A sequence is strictly
	 * increasing if and only if A[0] < A[1] < A[2] < ... < A[A.length - 1].)
	 * 
	 * Given A and B, return the minimum number of swaps to make both sequences strictly increasing. It
	 * is guaranteed that the given input always makes it possible.
	 * 
	 * <pre>
	Example:
	Input: A = [1,3,5,4], B = [1,2,3,7]
	Output: 1
	Explanation: 
	Swap A[3] and B[3].  Then the sequences are:
	A = [1, 3, 5, 7] and B = [1, 2, 3, 4]
	which are both strictly increasing.
	 * </pre>
	 */
	public int minSwap(int[] A, int[] B) {
		int swapRecord = 1, fixRecord = 0;
		for (int i = 1; i < A.length; i++) {
			if (A[i - 1] >= B[i] || B[i - 1] >= A[i]) {
				// The ith manipulation should be same as the i-1th manipulation fixRecord = fixRecord;
				swapRecord++;
			} else if (A[i - 1] >= A[i] || B[i - 1] >= B[i]) {
				// The ith manipulation should be the opposite of the i-1th manipulation
				int temp = swapRecord;
				swapRecord = fixRecord + 1;
				fixRecord = temp;
			} else {
				// Either swap or fix is OK. Let's keep the minimum one
				int min = Math.min(swapRecord, fixRecord);
				swapRecord = min + 1;
				fixRecord = min;
			}
		}
		return Math.min(swapRecord, fixRecord);
	}

	public int minSwap2(int[] A, int[] B) {
		// n: natural, s: swapped
		int n1 = 0, s1 = 1;
		for (int i = 1; i < A.length; ++i) {
			int n2 = Integer.MAX_VALUE, s2 = Integer.MAX_VALUE;
			// if a1 < a2 and b1 < b2, then it is allowed to have both of these columns natural (unswapped), or
			// both of these columns swapped. This possibility leads to n2 = min(n2, n1) and s2 = min(s2, s1 +
			// 1).
			if (A[i - 1] < A[i] && B[i - 1] < B[i]) {
				n2 = Math.min(n2, n1);
				s2 = Math.min(s2, s1 + 1);
			}
			// if a1 < b2 and b1 < a2. This means that it is allowed to have exactly one of these columns
			// swapped. This possibility leads to n2 = min(n2, s1) or s2 = min(s2, n1 + 1).
			if (A[i - 1] < B[i] && B[i - 1] < A[i]) {
				n2 = Math.min(n2, s1);
				s2 = Math.min(s2, n1 + 1);
			}
			n1 = n2;
			s1 = s2;
		}
		return Math.min(n1, s1);
	}

	public double champagneTower(int poured, int queryRow, int queryGlass) {
		double[][] A = new double[102][102];
		A[0][0] = (double) poured;
		for (int r = 0; r <= queryRow; r++) {
			for (int c = 0; c <= r; c++) {
				double q = (A[r][c] - 1.0) / 2.0;
				if (q > 0) {
					A[r + 1][c] += q;
					A[r + 1][c + 1] += q;
				}
			}
		}
		return Math.min(1, A[queryRow][queryGlass]);
	}

	public int countByBoundedMax(int[] A, int bound) {
		int ans = 0, cur = 0;
		for (int x : A) {
			cur = x <= bound ? cur + 1 : 0;
			ans += cur;
		}
		return ans;
	}

	public int numSubarrayBoundedMax(int[] A, int min, int max) {
		int i = 0, count = 0, result = 0;
		for (int j = 0; j < A.length; j++) {
			if (A[j] >= min && A[j] <= max) {
				count = (j - i) + 1;
				result += count;
			} else if (A[j] < min) {
				result += count;
			} else {
				i = j + 1;
				count = 0;
			}

		}
		return result;
	}

	public boolean isScramble(String s1, String s2) {
		if (s1.equals(s2))
			return true;

		int[] count = new int[26];
		for (int i = 0; i < s1.length(); i++) {
			count[s1.charAt(i) - 'a']++;
			count[s2.charAt(i) - 'a']--;
		}
		for (int i = 0; i < count.length; i++) {
			if (count[i] != 0)
				return false;
		}

		for (int i = 1; i < s1.length(); i++) {
			if (isScramble(s1.substring(0, i), s2.substring(0, i)) && isScramble(s1.substring(i), s2.substring(i)))
				return true;
			if (isScramble(s1.substring(0, i), s2.substring(s2.length() - i))
					&& isScramble(s1.substring(i), s2.substring(0, s2.length() - i)))
				return true;
		}

		return false;
	}

	/**
	 * Given s1, s2, s3, find whether s3 is formed by the interleaving of s1 and s2.
	 * 
	 * <pre>
	Example 1:
	
	Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
	Output: true
	Example 2:
	
	Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
	Output: false
	 * </pre>
	 */
	public boolean isInterleave(String s1, String s2, String s3) {
		if (s3.length() != s1.length() + s2.length())
			return false;
		boolean dp[][] = new boolean[s1.length() + 1][s2.length() + 1];

		dp[0][0] = true;
		for (int i = 1; i < dp.length; i++)
			dp[i][0] = dp[i - 1][0] && s1.charAt(i - 1) == s3.charAt(i - 1);
		for (int j = 1; j < dp[0].length; j++)
			dp[0][j] = dp[0][j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);

		for (int i = 1; i <= s1.length(); i++) {
			for (int j = 1; j <= s2.length(); j++) {
				dp[i][j] = (dp[i - 1][j] && s1.charAt(i - 1) == s3.charAt(i + j - 1))
						|| (dp[i][j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1));
			}
		}
		return dp[s1.length()][s2.length()];

	}

	public int numDistinct(String s, String t) {
		if (s == null || t == null)
			return 0;
		int[][] dp = new int[s.length() + 1][t.length() + 1];

		// if both t and s are ""
		dp[0][0] = 1;
		// always 1 if t is "" and s can be any char
		for (int i = 1; i < dp.length; i++)
			dp[i][0] = 1;
		// always 0 if s is "" and t can be any char
		for (int j = 1; j < dp[0].length; j++)
			dp[0][j] = 0;

		// main process goes here!
		for (int i = 1; i < dp.length; i++) {
			for (int j = 1; j < dp[0].length; j++) {
				// carry forward previous number
				dp[i][j] = dp[i - 1][j];
				if (s.charAt(i - 1) == t.charAt(j - 1))
					// increase when also match
					dp[i][j] += dp[i - 1][j - 1];
			}
		}

		return dp[s.length()][t.length()];
	}

	// Scanning from left to right, our force decays by 1 every iteration, and resets to N if we meet an
	// 'R', so that force[i] is higher (than force[j]) if and only if dominoes[i] is closer (looking
	// leftward) to 'R' (than dominoes[j]).
	public String pushDominoes(String S) {
		int N = S.length();
		char[] A = S.toCharArray();
		int[] forces = new int[N];

		int force = 0;
		for (int i = 0; i < N; i++) {
			force = A[i] == 'R' ? N : A[i] == 'L' ? 0 : Math.max(force - 1, 0);
			forces[i] += force;
		}

		force = 0;
		for (int i = N - 1; i >= 0; i--) {
			force = A[i] == 'L' ? N : A[i] == 'R' ? 0 : Math.max(force - 1, 0);
			forces[i] -= force;
		}

		StringBuilder ans = new StringBuilder();
		for (int f : forces) {
			ans.append(f > 0 ? 'R' : f < 0 ? 'L' : '.');
		}
		return ans.toString();
	}

	public static void main(String[] args) {
		DPBootCamp solution = new DPBootCamp();
		Assert.assertEquals(climbStairs(4, 2), 5);
		Assert.assertEquals(climbStairs(10, 5), 464);
		List<Integer> playScores = Arrays.asList(2, 3, 7);
		Assert.assertTrue(4 == combinationsForFinalScore(12, playScores));
		Assert.assertTrue(1 == combinationsForFinalScore(5, playScores));
		Assert.assertTrue(solution.canPartitionArray(new int[] { 1, 5, 11, 5 }));

		int[] array = new int[] { -10, -5, 2, 2, 2, 3, 4, 7, 9, 12, 13 };
		assert solution.magicIndexWithDups(array) == 2;

		System.out.println(solution.multiplyByUsingAddition(8, 15));
		System.out.println(solution.generateParentheses(3));

		int[] A = { 2, 1, 4, 3 };
		System.out.println(solution.countByBoundedMax(A, 3));
		System.out.println(solution.countByBoundedMax(A, 2 - 1));
	}

}
