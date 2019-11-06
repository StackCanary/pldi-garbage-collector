package pldi.objects;

import pldi.heap.Memory;

public class INT extends AbstractRunTimeObject {
	public INT(Memory heap) {
		super(heap);
		// TODO Auto-generated constructor stub
	}

	public int i;

	@Override
	public int[] pack() {
		int a[] = { tag().getTag(), i };
		return a;
	}

	@Override
	public void unpack() {
		i = heap.r(addr + 1);
	}
	
	@Override
	public Tag tag() {
		return Tag.INT;
	}

	public INT(Memory heap, int addr) {
		super(heap, addr);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() 
	{
		return "(" + "INT: " + i + ")";
	}
}
