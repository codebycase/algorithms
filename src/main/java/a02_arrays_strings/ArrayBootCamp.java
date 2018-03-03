package a02_arrays_strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ArrayBootCamp {
	/**
	 * Write an function, left rotate an array of size n by d.
	 */
	public void leftRotate(int[] nums, int d, int n) {
		for (int i = 0; i < d; i++) {
			int j = 0, temp = nums[0];
			for (j = 0; j < n - 1; j++) {
				nums[j] = nums[j + 1];
			}
			nums[j] = temp;
		}
	}

	/**
	 * Reorder an array of integers so that even entries appear first. Solve it without allocating
	 * additional storage.
	 * 
	 */
	public void evenOddSort(int[] nums) {
		int even = 0, odd = nums.length - 1;
		while (even < odd) {
			if (nums[even] % 2 == 0) {
				even++;
			} else {
				// only swap for even num!
				if (nums[odd] % 2 == 0) {
					int temp = nums[even];
					nums[even] = nums[odd];
					nums[odd] = temp;
				}
				odd--;
			}
		}
	}

	/**
	 * Given an unsorted array nums, reorder it in-place such that nums[0] <= nums[1] >= nums[2] <=
	 * nums[3]....
	 * 
	 * For example, given nums = [3, 5, 2, 1, 6, 4], one possible answer is [1, 6, 2, 5, 3, 4].
	 * 
	 * @author lchen
	 *
	 */
	public void wiggleSort(int[] nums) {
		for (int i = 1; i < nums.length; i++) {
			if (i % 2 == 0) {
				if (i != 0 && nums[i - 1] < nums[i])
					swap(nums, i - 1, i);
			} else {
				if (nums[i - 1] > nums[i])
					swap(nums, i - 1, i);
			}
		}
	}

	private void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}

	/**
	 * Write a program that takes an array A and an index i into A, and rearrange the elements such
	 * that all elements less than A[i] (the "pivot") appear first, followed by elements equal to
	 * the pivot, followed by elements greater than the pivot.
	 */
	public enum Color {
		RED, WHITE, BLUE
	};

	public int[] increaseOne(int[] nums) {
		if (nums == null || nums.length == 0)
			return nums;
		int i = nums.length - 1;
		nums[i]++;
		while (i > 0 && nums[i] == 10) {
			nums[i - 1]++;
			nums[i] = 0;
			i--;
		}
		if (nums[0] == 10) {
			nums[0] = 1;
			nums = Arrays.copyOf(nums, nums.length + 1);
		}
		return nums;
	}

	public void dutchFlagPartion(int pivotIndex, List<Color> colors) {
		Color pivot = colors.get(pivotIndex);
		/**
		 * Keep the following invariants during partitions: <br>
		 * bottom group: colors.subList(0, smaller). <br>
		 * middle group: colors.subList(smaller, equal). <br>
		 * unclassified group: A.suList(equal, larger). <br>
		 * top group: A.subList(larger, colors.size()).
		 */
		int smaller = 0, equal = 0, larger = colors.size() - 1;
		while (equal <= larger) {
			if (colors.get(equal).ordinal() < pivot.ordinal()) {
				Collections.swap(colors, smaller++, equal++);
			} else if (colors.get(equal).ordinal() == pivot.ordinal()) {
				equal++;
			} else {
				Collections.swap(colors, equal, larger--);
			}
		}
	}

	/**
	 * Write a program which takes an array of n integers, where A[i] denotes the maximum you can
	 * advance from index i, and returns whether it is possible to advance to the last index
	 * starting from the beginning of the array.
	 */
	public boolean canReachEnd(int[] maxAdvancedSteps) {
		int furthestReachSoFar = 0, lastIndex = maxAdvancedSteps.length - 1;
		for (int i = 0; i <= furthestReachSoFar && furthestReachSoFar < lastIndex; i++) {
			furthestReachSoFar = Math.max(furthestReachSoFar, maxAdvancedSteps[i] + i);
		}
		return furthestReachSoFar >= lastIndex;
	}

	/**
	 * Write a program that takes an integer argument and returns all the primes between 1 and
	 * itself.
	 */
	public List<Integer> generatePrimes(int n) {
		List<Integer> primes = new ArrayList<>();
		boolean[] flags = new boolean[n + 1];
		Arrays.fill(flags, true);

		flags[0] = false;
		flags[1] = false;

		for (int p = 2; p <= n; p++) {
			if (flags[p]) {
				primes.add(p);
				for (int i = p * p; i <= n; i += p) {
					flags[i] = false; // sieve p's multipliers
				}
			}
		}

		return primes;
	}

	/**
	 * Given numRows, generate the first numRows of Pascal's triangle.
	 * 
	 * For example, given numRows = 5, Return
	 * 
	 * <pre>
	[
	     [1],
	    [1,1],
	   [1,2,1],
	  [1,3,3,1],
	 [1,4,6,4,1]
	]
	 * </pre>
	 * 
	 * @param numRows
	 * @return
	 */
	public List<List<Integer>> generatePascalTriangle(int numRows) {
		List<List<Integer>> result = new ArrayList<>();
		for (int i = 0; i < numRows; i++) {
			List<Integer> currRow = new ArrayList<>();
			for (int j = 0; j <= i; j++) {
				if (0 < j && j < i)
					currRow.add(result.get(i - 1).get(j - 1) + result.get(i - 1).get(j));
				else
					currRow.add(1);
			}
			result.add(currRow);
		}
		return result;
	}

	/**
	 * Given an array of numbers nums, in which exactly two elements appear only once and all the
	 * other elements appear exactly twice. Find the two elements that appear only once.
	 * 
	 * For example:
	 * 
	 * Given nums = [1, 2, 1, 3, 2, 5], return [3, 5].
	 * 
	 * Note: The order of the result is not important. So in the above example, [5, 3] is also
	 * correct. Your algorithm should run in linear runtime complexity. Could you implement it using
	 * only constant space complexity?
	 * 
	 */
	public int[] singleNumberIII(int[] nums) {
		int diff = 0;
		for (int num : nums) {
			diff ^= num;
		}

		// diff &= -diff;
		diff &= ~(diff - 1);

		int[] result = { 0, 0 };
		for (int num : nums) {
			if ((num & diff) == 0)
				result[0] ^= num;
			else
				result[1] ^= num;
		}

		return result;
	}

	public void nextPermutation(int[] nums) {
		if (nums == null || nums.length < 1)
			return;

		int position = nums.length - 2;
		while (position >= 0 && nums[position] >= nums[position + 1]) {
			position--;
		}

		if (position == -1) {
			reverse(nums, 0, nums.length - 1);
			return;
		}

		for (int i = nums.length - 1; i > position; i--) {
			if (nums[i] > nums[position]) {
				swap(nums, position, i);
				break;
			}
		}

		reverse(nums, position + 1, nums.length - 1);
	}

	// i + 1 is the inversion position
	public void nextPermutation2(int[] nums) {
		int n = nums.length;
		if (n < 1)
			return;

		int i = n - 1;
		while (i > 0 && nums[i - 1] >= nums[i]) {
			i--;
		}

		// no next, return the smallest
		if (i == 0) {
			reverse(nums, 0, n - 1);
			return;
		}

		int j = n - 1;
		while (nums[j] <= nums[i - 1]) {
			j--;
		}

		swap(nums, j, i - 1);
		reverse(nums, i, n - 1);
	}

	private void reverse(int[] nums, int start, int end) {
		while (start < end) {
			swap(nums, start++, end--);
		}
	}

	public List<Integer> randomSubset(int n, int k) {
		Map<Integer, Integer> changedElements = new HashMap<>();
		Random randIdxGen = new Random();

		for (int i = 0; i < k; i++) {
			int randIdx = i + randIdxGen.nextInt(n - i);
			Integer ptr1 = changedElements.get(randIdx);
			Integer ptr2 = changedElements.get(i);
			if (ptr1 == null && ptr2 == null) {
				changedElements.put(randIdx, i);
				changedElements.put(i, randIdx);
			} else if (ptr1 == null && ptr2 != null) {
				changedElements.put(randIdx, ptr2);
				changedElements.put(i, randIdx);
			} else if (ptr1 != null && ptr2 == null) {
				changedElements.put(i, ptr1);
				changedElements.put(randIdx, i);
			} else {
				changedElements.put(i, ptr1);
				changedElements.put(randIdx, ptr2);
			}
		}

		List<Integer> result = new ArrayList<>(k);
		for (int i = 0; i < k; i++) {
			result.add(changedElements.get(i));
		}
		return result;
	}

	public int nonuniformRandomNumber(List<Integer> values, List<Double> probabilities) {
		List<Double> prefixSumOfProbabilities = new ArrayList<>();
		prefixSumOfProbabilities.add(0.0);
		for (double p : probabilities) {
			prefixSumOfProbabilities.add(prefixSumOfProbabilities.get(prefixSumOfProbabilities.size() - 1) + p);
		}

		Random r = new Random();
		// Get a random number in [0.0,1.0).
		final double uniform01 = r.nextDouble();
		// Find the index of the interval that uniform01 lies in.
		int it = Collections.binarySearch(prefixSumOfProbabilities, uniform01);
		if (it < 0) {
			// We want the index of the first element in the array which is greater than the key.
			//
			// When a key is not present in the array, Collections.binarySearch() returns the
			// negative of 1 plus the smallest index whose entry is greater than the key.
			//
			// Therefore, if the return value is negative, by taking its absolute value minus 1 and
			// substracting 1 from it, we get the desired index.
			final int intervalIdx = (Math.abs(it) - 1) - 1;
			return values.get(intervalIdx);
		} else {
			// We have it >= 0, i.e., uniform01 equals an entry in prefixSumOfProbabilities.
			//
			// Because we uniform01 is a random double, the probability of it equalling an endpoint
			// in prefixSumOfProbabilities is exceedingly low.
			return values.get(it);
		}
	}

	public void solveSudoku(char[][] board) {
		doSolve(board, 0, 0);
	}

	private boolean doSolve(char[][] board, int row, int col) {
		for (int i = row; i < 9; i++, col = 0) { // Note: must reset col here!
			for (int j = col; j < 9; j++) {
				if (board[i][j] != '.')
					continue;
				for (char num = '1'; num <= '9'; num++) {
					if (isValid(board, i, j, num)) {
						board[i][j] = num;
						if (doSolve(board, i, j + 1))
							return true;
						board[i][j] = '.';
					}
				}
				return false;
			}
		}
		return true;
	}

	private boolean isValid(char[][] board, int row, int col, char num) {
		// Block no. is i/3, first element is i/3*3
		int blockRow = (row / 3) * 3, blockCol = (col / 3) * 3;
		for (int i = 0; i < 9; i++)
			if (board[i][col] == num || board[row][i] == num || board[blockRow + i / 3][blockCol + i % 3] == num)
				return false;
		return true;
	}

	// Approach1: Simulation Spiral
	public List<Integer> spiralOrder(int[][] matrix) {
		List<Integer> result = new ArrayList<>();
		if (matrix.length == 0)
			return result;
		int[][] shift = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
		int m = matrix.length, n = matrix[0].length;
		boolean[][] seen = new boolean[m][n];
		int row = 0, col = 0, dir = 0;
		for (int i = 0; i < m * n; i++) {
			result.add(matrix[row][col]);
			seen[row][col] = true;
			int nextRow = row + shift[dir][0], nextCol = col + shift[dir][1];
			if (nextRow < 0 || nextRow >= m || nextCol < 0 || nextCol >= n || seen[nextRow][nextCol]) {
				dir = (dir + 1) % 4;
				nextRow = row + shift[dir][0];
				nextCol = col + shift[dir][1];
			}
			row = nextRow;
			col = nextCol;
		}
		return result;
	}

	// Approach2: Layer-by-Layer
	public List<Integer> spiralOrder2(int[][] matrix) {
		List<Integer> result = new ArrayList<>();
		if (matrix.length == 0)
			return result;
		int r1 = 0, r2 = matrix.length - 1;
		int c1 = 0, c2 = matrix[0].length - 1;
		while (r1 <= r2 && c1 <= c2) {
			for (int c = c1; c <= c2; c++)
				result.add(matrix[r1][c]);
			for (int r = r1 + 1; r <= r2; r++)
				result.add(matrix[r][c2]);
			if (r1 < r2 && c1 < c2) {
				for (int c = c2 - 1; c > c1; c--)
					result.add(matrix[r2][c]);
				for (int r = r2; r > r1; r--) {
					result.add(matrix[r][c1]);
				}
			}
			c1++;
			c2--;
			r1++;
			r2--;
		}
		return result;
	}

	public static void main(String[] args) {
		ArrayBootCamp camp = new ArrayBootCamp();

		int[] nums = { 1, 2, 3, 4, 5, 6, 7 };
		camp.leftRotate(nums, 2, 7);
		assert Arrays.toString(nums).equals("[ 3, 4, 5, 6, 7, 1, 2]");

		nums = new int[] { 3, 5, 2, 1, 6, 4, 9, 7 };
		camp.evenOddSort(nums);
		assert Arrays.toString(nums).equals("[4, 6, 2, 1, 5, 3, 9, 7]");

		nums = new int[] { 3, 5, 2, 1, 6, 4 };
		camp.wiggleSort(nums);
		assert Arrays.toString(nums).equals("[3, 5, 1, 6, 2, 4]");

		List<Color> colors = Arrays.asList(Color.RED, Color.WHITE, Color.BLUE, Color.RED, Color.BLUE, Color.WHITE,
				Color.WHITE);
		camp.dutchFlagPartion(1, colors);
		assert colors.toString().equals("[RED, RED, WHITE, WHITE, WHITE, BLUE, BLUE]");

		nums = new int[] { 3, 5, 9 };
		assert Arrays.toString(camp.increaseOne(nums)).equals("[3, 6, 0]");
		nums = new int[] { 9, 9, 9 };
		assert Arrays.toString(camp.increaseOne(nums)).equals("[1, 0, 0, 0]");

		assert camp.canReachEnd(new int[] { 3, 3, 1, 0, 2, 0, 1 });
		assert !camp.canReachEnd(new int[] { 3, 2, 0, 0, 2, 0, 1 });

		assert camp.generatePrimes(18).toString().equals("[2, 3, 5, 7, 11, 13, 17]");
		nums = new int[] { 1, 2, 1, 3, 2, 5 };
		assert Arrays.toString(camp.singleNumberIII(nums)).equals("[5, 3]");

		nums = new int[] { 6, 2, 1, 5, 4, 3, 0 };
		camp.nextPermutation(nums);
		assert Arrays.toString(nums).toString().equals("[6, 2, 3, 0, 1, 4, 5]");

		System.out.println(camp.randomSubset(10, 3));
	}

}
