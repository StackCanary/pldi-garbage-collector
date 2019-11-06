package pldi.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pldi.heap.Memory;

public class IND extends AbstractRunTimeObject
{

	public IND(Memory heap) {
		super(heap);
		// TODO Auto-generated constructor stub
	}

	public int p;

	@Override
	public int[] pack() 
	{
		int a[] = {tag().getTag(), p}; return a;
	}

	@Override
	public void unpack() {
		p = heap.r(addr + 1);
	}

	@Override
	public Tag tag() 
	{
		return Tag.IND;
	}

	@Override
	public List<Integer> pointers() {

		List<Integer> pointers = new ArrayList<Integer>();

		pointers.add(p);

		return pointers;
	}

	public IND(Memory heap, int addr) {
		super(heap, addr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() 
	{
		return "(" + "IND: " + p + ")";
	}

}
