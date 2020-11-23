import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class Oblig3 {

	static int[] selectionSort(int[] a) {
		for (int i = 0; i < a.length - 1; i++) {
			System.out.print("i: " + i + "array: ");
			for (int v : a) {
				System.out.print(v + " ");
			}
			int s = i;
			for (int j = i + 1; j < a.length; j++) {
				if (a[j] < a[s]) {
					s = j;
				}
			}
			if (i != s) {
				int tmp = a[s];
				a[s] = a[i];
				a[i] = tmp;

			}
		}
		return a;
	}

	static int[] insertionSort(int[] a) {
		for (int i = 1; i < a.length - 1; i++) {
			int j = i;
			while (a[j - 1] > a[j]) {
				int tmp = a[j];
				a[j] = a[j - 1];
				a[j - 1] = tmp;
				j--;
			}
		}
		return a;
	}

	static int quickSortPartition(int[] a, int x, int y) {
		int r = ThreadLocalRandom.current().nextInt(x, y + 1);
		int tmp = a[r];
		a[r] = a[y];
		a[y] = tmp;
		int p = a[y];
		int l = x;
		r = y - 1;
		while (l <= r) {
			while (l <= r && a[l] <= p) {
				l++;
			}
			while (l <= r && a[r] >= p) {
				r--;
			}
			if (l < r) {
				tmp = a[r];
				a[r] = a[l];
				a[l] = tmp;
			}
		}
		tmp = a[l];
		a[l] = a[y];
		a[y] = tmp;
		return l; 
	}

	static int[] quickSort(int[] a, int x, int y) {
		while (x < y) {
			int l = quickSortPartition(a, x, y);
			if (l - x < y - l) {
				quickSort(a, x, l - 1);
				x = l + 1;
			} else {
				quickSort(a, l + 1, y);
				y = l - 1;
			}
		}
		return a;
	}

	static int[] mergeSort(int[] a) {
		if (a.length == 1) {
			return a;
		}
		int[] a1 = new int[a.length/2];
		int[] a2 = new int[a.length - a.length/2];

		for (int i = 0; i < a.length/2; i++) {
			a1[i] = a[i];
			a2[i] = a[a.length/2 + i];
		}
		a2[a2.length - 1] = a[a.length - 1];

		a1 = mergeSort(a1);
		a2 = mergeSort(a2);

		return merge(a1, a2);
	}


	static int[] merge(int[] a1, int[] a2) {
		int[] a = new int[a1.length + a2.length];
		int i = 0;
		int j = 0;
		while (i < a1.length && j < a2.length) {
			if (a1[i] < a2[j]) {
				a[i + j] = a1[i];
				i++;
			} else {
				a[i + j] = a2[j];
				j++;
			}
		}
		while (i < a1.length) {
			a[i + j] = a1[i];
			i++;
		}
		while (j < a2.length) {
			a[i + j] = a2[j];
			j++;
		}
		return a;
	}

	static ArrayList<Integer> bucketSort(int[] a) {
		ArrayList<Integer> r = new ArrayList<Integer>();
		
		int max = 0;
		for (int i : a) {
			if (i > max) {
				max = i;
			}
		}
		ArrayList<ArrayList<Integer>> b = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < max; i ++) {
			b.add(new ArrayList<Integer>());
		}

		for (int i : a) {
			b.get(i - 1).add(i);
		}

		for (int i = 0; i < b.size(); i++) {
			for (int j = 0; j < b.get(i).size(); j++) {
				r.add(b.get(i).get(j));
			}
		}
		return r;
	}
}
