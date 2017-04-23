package text;
/**
 * Stack interface
 * 
 * @param <T> type of the elements of the stack
 */
public interface Stack<T> {
	
	/**
	 * 
	 * @return true if empty, otherwise false
	 */
	boolean isEmpty();
	
	/**
	 * 
	 * @return the number of elements on the stack
	 */
	int size();
	
	/**
	 * Pushes an element on the stack
	 * 
	 * @param element
	 */
	void push(T element);
	
	/**
	 * 
	 * @return the top element of the stack and removing it from the stack
	 */
	T pop();
	
	/**
	 * 
	 * @return the top element of the stack without removing it
	 */
	T top();
}