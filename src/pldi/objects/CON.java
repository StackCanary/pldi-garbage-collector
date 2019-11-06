package pldi.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pldi.heap.Memory;

public class CON extends AbstractRunTimeObject 
{
	public CON(Memory heap, int addr) {
		super(heap, addr);
	}

	public CON(Memory heap) 
	{
		super(heap);
	}

	public int p1;
	public int p2;
	
	@Override
	public int[] pack() 
	{
		int a[] = {tag().getTag(), p1, p2}; return a;
	}
	
	public void unpack()
	{
		p1 = heap.r(addr + 1);
		p2 = heap.r(addr + 2);
	}
	
	@Override
	public List<Integer> pointers() {
		
		List<Integer> p = new ArrayList<Integer>();
		
		p.add(p1);
		p.add(p2);
		
		return p;
	}
	
	@Override
	public Tag tag() 
	{
		return Tag.CON;
	}
	
	@Override
	public String toString() 
	{
		return "(" + "CON " + " " + p1 + ", " + p2 + ")";   
	}

}
