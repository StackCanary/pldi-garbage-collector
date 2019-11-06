package pldi.objects;

import java.util.Map;

import pldi.heap.Memory;

public class WPT extends AbstractRunTimeObject {

	public WPT(Memory heap) {
		super(heap);
	}

	public int p;

	public WPT(Memory heap, int addr) {
		super(heap, addr);
	}

	@Override
	public int[] pack() {
		int a[] = { tag().getTag(), p };
		return a;
	}

	@Override
	public void unpack() {
		p = heap.r(addr + 1);
	}

	
	@Override
	public Tag tag() 
	{
		return Tag.WPT;
	}
	
	@Override
	public String toString() 
	{
		return "(" + "WPT: " + p + ")";
	}

}
