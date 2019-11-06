package pldi.objects;

import pldi.heap.Memory;

public class BUL extends AbstractRunTimeObject
{
	public BUL(Memory heap) 
	{
		super(heap);
	}

	public BUL(Memory heap, int addr) {
		super(heap, addr);
	}

	public boolean b;

	@Override
	public int[] pack() 
	{
		int a[] = {tag().getTag(), b ? 1 : 0}; return a;
	}
	
	public void unpack()
	{
		b = heap.r(addr + 1) == 1;
	}
	
	@Override
	public Tag tag() 
	{
		return Tag.BUL;
	}
	
	@Override
	public String toString() 
	{
		return "(" + "BUL: " + b + ")";
	}



		
	
}
