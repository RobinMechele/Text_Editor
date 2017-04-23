package text;

/**
 * 
 *Singly linked list
 *
 *@param <T> type of the parameter
 */
public class LinkedList<T> {
	private Node head;
	private int size;
	
	public LinkedList()
	{
		head = null;
		size = 0;
	}
	
	/**
	 * constructor for a linked list with one element.
	 * @param element
	 */
	public LinkedList(T element)
	{
		head = new Node(element);
		size = 1;
	}
	/**
	 * prepends the element to the linked list
	 * @param element
	 */
	public void prepend(T element)
	{
		Node newNode = new Node(element, head);
		head = newNode;
		size++;
	}
	
	
	/**
	 * finds the element in the list
	 * @param element to find
	 * @return true if present in the list otherwise false
	 */
	public boolean find(T element)
	{
		Node cursor = head;
		do 
		{
			if(cursor.get().equals(element))
			{
				return true;	
			}
			
		}
		while (cursor.next() != null);
		return false;
	}
	
	private LinkedList(Node node)
	{
		head = node;
		size = count(node ,0);
	}
	
	/**
	 * 
	 * @return the last element of the list
	 */
	public T last()
	{
		return lastRecursive(head).get();
	}
	
	private  Node lastRecursive(Node current)
	{
		if(current.next() == null) 
			{
				return current;
			}
		return lastRecursive(current.next());
	}
	
	public T first()
	{
		if(head == null) return null;
		return head.get();
	}
	
	public int size()
	{
		return size;
	}
	
	public boolean isEmpty()
	{
		return size==0;
	}
	
	/**
	 * 
	 * removing the last element of the list
	 * 
	 */
	public void removeLast()
	{
		Node cursor = head;
		int counter=this.size-1;
		while(counter>1)
		{
			cursor = cursor.next();
			counter--;
		}
		cursor.toNull();
		size--;
	}
	
	/**
	 * 
	 * @return the linked list without the head element
	 */
	public LinkedList<T> tail()
	{
		return new LinkedList<T>(head.next());
	}
	
	private int count(Node current, int total)
		{
			if(current == null)
			{
				return total;
			}
			return count(current.next(), ++total);
		}
	
	private class Node
	{
		private T element;
		private Node next;
		
		public Node(T element)
		{
			this.element = element;
			this.next = null;		
		}
	
		public Node(T element, Node next)
		{
			this.element = element;			
			this.next = next;
		}
		
		public T get()
		{
			return element;
		}
		
		public Node next()
		{
			return next;
		}
		
		public void toNull()
		{
			this.next = null;
		}
		
	}
}