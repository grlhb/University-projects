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

class CircLinkedList {
private:
	Node* head;
	Node* get_node(int index) {
    	if (index < 0) {
			index = size + index; //for å håndtere negative indekser
		}
    	Node* current = head;
    	for (int i=0; i<index; i++) {
        	current = current->next;
    	}
   		return current;
	}

public:
	int size;
	int length() {
		return size;
	}

	CircLinkedList() {
		head = nullptr;
	}

	CircLinkedList(int n) {
		size = 0;
		head = nullptr;
		for (int i=1; i<n+1; i++) {
			append(i);
		}
	}

	void append(int val) {
    	if (head == nullptr) {
        	head = new Node(val);
        	head->next = head;
    	}
    	else {
    		Node* current;
    		current = head;
    		while (current->next != head) {
        		current = current->next;
    		}
    		current->next = new Node(val);
    		current->next->next = head;
    	}
   	    size += 1;
	}

	int& operator[](int index) {
    	return get_node(index)->value;
	}

	void print() {
		Node* current = head;
		cout << '[';
		while (current->next != head) {
			cout << current->value;
			cout << ',';
			current = current->next;
		}
		cout << current->value << ','<< '.' << '.' << '.' << ']' << endl;
		}

	
    void remove(int index) {
    	Node* prev = get_node(index-1);
    	Node* current = prev->next;
    	prev->next = current->next;
    	if(index%size==0) {
    		head = current->next;
		}
		size -= 1;
    	delete current;
    }
    	
	vector<int> josephus_sequence(int k) {
		vector<int> d;
		int i = k - 1;
		while (size > 1) {
			d.push_back(get_node(i)->value);
			remove(i);
			i += k - 1;
			i %= size;
		}
		return d;
	}	

};

void last_man_standing(int n,int k){
	CircLinkedList j(n);
	j.josephus_sequence(k);
	cout << "Vinner: " << j[0] << endl;
}

int main() {
CircLinkedList clist;
clist.append(0);
clist.append(2);
clist.append(4);
clist.print();
CircLinkedList j(7);
j.print();
j.remove(0);
j.print();
last_man_standing(68, 7);
CircLinkedList n; 
n.append(1);
n.print();
n.remove(0);
n.print();
}