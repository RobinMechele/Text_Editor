package text;

/**
 * Implements a Stack based on a Linked List
 *
 * @param <T> type of the elements on the stack
 */
public class StackLL<T> implements Stack<T> {
	private LinkedList<T> ll;
	private int maxsize;
	
	/**
	 * Creates an empty stack
	 * @param i 
	 */
	public StackLL(int maxsize){
		ll = new LinkedList<T>();
		this.maxsize = maxsize;
		
	}
	
	@Override
	public boolean isEmpty() {
		return ll.isEmpty();
	}

	@Override
	public int size() {
		return ll.size();
	}

	@Override
	public void push(T element) {
		ll.prepend(element);
		if(ll.size()>maxsize){
			ll.removeLast();
		}

	}

	@Override
	public T pop()  {
		if (size() == 0) throw new StackEmptyException();
		T element = ll.first();
		ll = ll.tail();
		return element;
	}

	@Override
	public T top() {
		return ll.first();
	}

}