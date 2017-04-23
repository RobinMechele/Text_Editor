package text;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StackLLTest {

private StackLL<Integer> stackie;
	
	@Before
	public void setUp() throws Exception
	{
		stackie = new StackLL<Integer>(5000);
	}
	
	@Test
	public void testIsEmpty() {	
		assertEquals(true,stackie.isEmpty());
		stackie.push(1);
		assertEquals(false,stackie.isEmpty());
	}	

	@Test
	public void testSize() {
		assertEquals(0,stackie.size());
		stackie.push(2);
		assertEquals(1,stackie.size());
	}

	@Test
	public void testPushAndPop() {
		stackie.push(1);
		assertEquals(new Integer(1), stackie.pop());
		
		stackie.push(2);
		stackie.push(3);
		
		assertEquals(new Integer(3),stackie.pop());
		assertEquals(new Integer(2),stackie.pop());
		assertEquals(0,stackie.size());
		
	}

	@Test
	public void testTop() {
		stackie.push(1);
		assertEquals(new Integer(1),stackie.top());
		assertEquals(1,stackie.size());
	}
	
	@Test(expected = StackEmptyException.class)
	public void testPopWhenEmpty()
	{
		stackie.pop();
	}

	@Test
	public void testMaxsize()
	{
		StackLL<Integer> maxstack = new StackLL<Integer>(3);
		assertEquals(0,maxstack.size());
		maxstack.push(2);
		assertEquals(1,maxstack.size());
		maxstack.push(4);
		maxstack.push(4);
		maxstack.push(500);
		assertEquals(3, maxstack.size());
		assertEquals(new Integer(500),maxstack.pop());
		assertEquals(new Integer(4),maxstack.pop());
		assertEquals(new Integer(4),maxstack.pop());
		assertEquals(0, maxstack.size());
		StackLL<Integer> maxstack2 = new StackLL<Integer>(5);
		maxstack2.push(4);
		maxstack2.push(4);
		maxstack2.push(500);
		maxstack2.push(6);
		maxstack2.push(4);
		assertEquals(new Integer(4),maxstack2.pop());
		assertEquals(new Integer(6),maxstack2.pop());
		assertEquals(new Integer(500),maxstack2.pop());
		assertEquals(new Integer(4),maxstack2.pop());
		assertEquals(new Integer(4),maxstack2.pop());
		assertEquals(0, maxstack2.size());
	}
}
