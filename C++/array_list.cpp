#include <iostream>
#include <vector>
#include <stdexcept>
#include <cmath>
using namespace std;


class ArrayList {
private:
	int capacity = 1;
	int *data;
	int growth_factor = 2;
	
	void resize() {
		capacity *= growth_factor;
		int *tmp = new int[capacity];
		for (int i=0; i<size; i++) {
			tmp[i] = data[i];
		}
		delete[] data;
		data = tmp;
	}
public:
	int size;
	int length() {
		return size;
	}
	ArrayList(vector<int> initial) {
		size = 0;
		capacity = 1;
		data = new int[capacity];

		for (int e: initial) {
			append(e); 
		}
	}
	~ArrayList() {
		delete[] data;
	}

	void append(int n){
		if (capacity < size) {
			resize();
			data[size] = n;
			size += 1;
		}	else{
			data[size] = n;
			size += 1;
		}
	}

	void print() {
		cout << "[";
		for (int i=0; i<size-1; i++){
			cout << data[i] << ", ";
		}
		cout << data[size-1] << "]" << endl;
	}

	int& operator[] (int i) {
    	if (0 <= i and i < size) {
        	return data[i];
    	} else {
        	throw out_of_range("IndexError");
    	}
	}

	void insert(int val, int index) {
		append(data[size+1]); 
		for (int i=size; i>index; i--) {
			data[i] = data[i-1];
		}
		data[index] = val;

	}	

	void remove(int index){
		for (int i = index+1; i < size; i++) {
			data[i-1] = data[i];	
		}
		size -= 1;
		if (capacity/size > 4) {
			shrink_to_fit();
		}
	}

	int pop(int in){
		int a = data[in];
		remove(in);
		if (capacity/size > 4) {
			shrink_to_fit();
		}
		return a;
	}

	int pop(){
		int a = data[size-1];
		remove(data[size-1]);	
		if (capacity/size > 4) {
			shrink_to_fit();
		}
		return a;
	}

	void shrink_to_fit() {
		int i=0;
		while (capacity/size>2) {
			capacity = capacity/2; 
		}
	}
}; 

bool is_prime(int n) {
    if (n == 1) {
        return false;
    }

    for (int d=2; d<n; d++) {
        if (n % d == 0) {
            return false;
        }
    }
    return true;
}





int main() {
	ArrayList a({});
	int i = 0;
	while (a.size < 10){
		i += 1;
		if (is_prime(i)) {
			a.append(i);
		}
	}
	cout << "first " << a.size << " primes:" << endl; 
	a.print();
	a.insert(6, 3);
	a.print();
	a.remove(3);
	a.print();
	cout << "popped: " << a.pop(6) << endl;
	a.print();
	cout << "last element popped:" << a.pop() << endl;
	a.print();
	a.insert(0,35);
	a.print();
}