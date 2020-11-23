#include <iostream>
#include <vector>
using namespace std;

struct Node {
	int value;
	Node* next;

	Node (int n) {
		value = n;
		next = nullptr;
	}

	Node (int n, Node* p) {
		value = n;
		next = p;
	}

};

class LinkedList {
private:
	Node* head;
	Node* tail;
	Node* get_node(int index) {
    	if (index < 0 or index >= size) {
        	throw range_error("IndexError: Index out of range");
    	}

    	Node* current = head;
    	for (int i=0; i<index; i++) {
        	current = current->next;
    	}
   		return current;
}

public:
	int size = 1;
	
	int length(){
		return size;
	}
	
	LinkedList() {
		head = nullptr;
	}

	LinkedList(vector<int> initial) {
		for (int e: initial){
			append(e);
		}
	}

	void append(int val) {
    	if (head == nullptr) {
        head = new Node(val);
        return;
    	}

   		Node* current;
    	current = head;
    	while (current->next != nullptr) {
        	current = current->next;
    	}
    	current->next = new Node(val);
    	size += 1;
	}

	void print() {
		Node* current = head;
		cout << '[';
		while (current->next != nullptr) {
			cout << current->value;
			cout << ',';
			current = current->next;
		}
		cout << current->value << ']' << endl;
	}

	~LinkedList() {
    	Node* current;
    	Node* next;

    	current = head;

    	while (current != nullptr) {
        	next = current->next;
        	delete current;
        	current = next;
    	}
	}

	int& operator[](int index) {
    	return get_node(index)->value;
	}


	void insert(int val, int index) {
		if (index == 0){
			head = new Node(val, head);
		}
		else {
    	Node* prev = get_node(index-1);
    	Node* next = prev->next;
    	prev->next = new Node(val, next);
    	size += 1;
    	}
	}

	void remove(int index) {
		Node* current;
		Node* prev;
		current = head;
		for(int i=0;i<index;i++) {
        	prev = current;
        	current = current->next;
    	}
    	prev->next = current->next;
    	size -= 1;
    }

    int pop(int index) {
    	int a = get_node(index)->value;
    	remove(index);
    	return a;
    }

    int pop() {
    	int a = get_node(size-1)->value;
    	remove(size-1);
    	return a;
    }
};	

int main() {
	LinkedList primes;
	primes.append(2);
	primes.append(3);
	primes.append(5);
	primes.append(7);
	primes.insert(11, 4);
	primes.print();
	cout << "length: " << primes.length() << endl;
	cout << "popped: " << primes.pop(3) << endl; 
	primes.print();
	cout << "last element popped: " << primes.pop() << endl;	
	primes.print();
	LinkedList fib = LinkedList(vector<int> {1, 1, 2, 3, 5, 8});
	fib.print();
}