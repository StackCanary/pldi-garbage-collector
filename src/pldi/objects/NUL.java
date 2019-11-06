package pldi.objects;

import pldi.heap.Memory;

public class NUL extends AbstractRunTimeObject
{

	public NUL(Memory heap) {
		super(heap);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] pack() 
	{
		int a[] = {tag().getTag()}; return a;
	}

	@Override
	public void unpack() 
	{

	}
	
	@Override
	public Tag tag() 
	{
		return Tag.NUL;
	}

	public NUL(Memory heap, int addr) {
		super(heap, addr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() 
	{
		return "(" + "NUL" + ")";
	}
}
