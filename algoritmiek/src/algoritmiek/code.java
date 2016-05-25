package algoritmiek;

import java.io.InputStream;
import java.util.*;

class Solution
{
	public Solution(){ }   
	// Implement the run method to return the answer to the problem posed by the inputstream.

	public static String run(InputStream in) {
		int start = 0;
		int end = 0;
		int n = 0;
		int m = 0;
		Scanner sc = new Scanner(in);
		n = sc.nextInt();
		m = sc.nextInt();
		start = sc.nextInt();
		end = sc.nextInt();
		if (start == end){
			sc.close();
			return 0+"";
		}
		if (n == 1){
		  sc.close();
		  return 0+"";
		}
		if (n == 0){
		  sc.close();
		  return -1+"";
		}
		if (m == 0 && n != 1){
		  sc.close();
		  return -1+"";
		}
		Queue<Vertex> queue  = new QueueLinkedList<>();
		//Stack stack = new Stack(m);
		ArrayList<Vertex> Vertexs = new ArrayList<Vertex>();
	//	ArrayList<Vertex> Vertexs2 = new ArrayList<Vertex>();
		for (int x = 0; x < n; x++){
			Vertexs.add(new Vertex(x+1, new ArrayList<Edge>()));
		}
			boolean[] visited = new boolean[n];
		boolean[] traveled = new boolean[m];
		for (int i = 0; i < m; i++){
			int cur = sc.nextInt();
			int next = sc.nextInt();
			Edge dest = new Edge(sc.nextInt(), i+1, Vertexs.get(next-1), Vertexs.get(cur-1)); 
			Vertexs.get(cur-1).addNeighbour(dest);
		}
		Vertex currVertex = Vertexs.get(start-1);
		currVertex.disstart = 0;
		((QueueLinkedList<Vertex>) queue).enqueue(currVertex);
		while (!queue.isEmpty()){
			currVertex = ((QueueLinkedList<Vertex>) queue).dequeue();
			List<Edge> children = currVertex.getNeighbours();
			if (!visited[currVertex.getvalue() -1]){
			for (int i = 0; i < children.size(); i++){
						int temp = children.get(i).value + children.get(i).getCurrent().disstart + children.size();
						if (temp < children.get(i).getDestination().disstart){
							children.get(i).getDestination().disstart = temp;
					    ((QueueLinkedList<Vertex>) queue).enqueue(children.get(i).getDestination());
				    	traveled[children.get(i).id-1] = true;
				    	
				}
			}
			visited[currVertex.getvalue()-1] = true;
		}
		}
		sc.close();
		if (Vertexs.get(end-1).getdis() == 1000000000){
		  return -1+"";
		}
		return Vertexs.get(end-1).getdis()+"";
	}

}
class Vertex
{
	int value;
	int disstart;
	ArrayList<Edge> adjacent;

	public Vertex(int val, ArrayList<Edge> adj){
		value = val;
		adjacent = adj;
		disstart = 1000000000;
	}
	public int getvalue(){
		return value;
	}
	public void setValue(int val){
		value = val;
	}
	public int getdis(){
		return disstart;
	}
	public void setdis(int val){
		disstart = val;
	}
	public ArrayList<Edge> getNeighbours(){
		return adjacent;
	}
	public void setNeighbours(ArrayList<Edge> adj){
		adjacent = adj;
	}
	public void addNeighbour(Edge nd){
		adjacent.add(nd);
	}
}
class Edge
{
	int value;
	int id;
	Vertex adjacent;
	Vertex current;

	public Edge(int val, int idee, Vertex adj, Vertex curr){
		value = val;
		id = idee;
		adjacent = adj;
		current = curr;
	}
	public int getvalue(){
		return value;
	}
	public void setValue(int val){
		value = val;
	}
	public Vertex getDestination(){
		return adjacent;
	}
	public void setDestination(Vertex adj){
		adjacent = adj;
	}
	public Vertex getCurrent(){
		return current;
	}
	public void setCurrent(Vertex adj){
		current = adj;
	}
}
class Stack {
	private int maxSize;
	private Vertex[] stackArray;
	private int top;
	public Stack(int s) {
		maxSize = s;
		stackArray = new Vertex[maxSize];
		top = -1;
	}
	public void push(Vertex j) {
		stackArray[++top] = j;
	}
	public Vertex pop() {
		return stackArray[top--];
	}
	public Vertex peek() {
		return stackArray[top];
	}
	public boolean isEmpty() {
		return (top == -1);
	}
	public boolean isFull() {
		return (top == maxSize - 1);
	}
}
class QueueLinkedList<E> implements Queue<E> {

    private int total;

    private Node first, last;

    private class Node {
        private E ele;
        private Node next;
    }

    public QueueLinkedList() { }

    public QueueLinkedList<E> enqueue(E ele)
    {
        Node current = last;
        last = new Node();
        last.ele = ele;

        if (total++ == 0) first = last;
        else current.next = last;

        return this;
    }

    public E dequeue()
    {
        if (total == 0) throw new java.util.NoSuchElementException();
        E ele = first.ele;
        first = first.next;
        if (--total == 0) last = null;
        return ele;
    }


	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		return (total == 0);
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("hiding")
	@Override
	public <E> E[] toArray(E[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(E e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean offer(E e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E remove() {
		// TODO Auto-generated method stub
		return null;
	}

}