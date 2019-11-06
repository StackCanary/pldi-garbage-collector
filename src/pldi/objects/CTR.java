package pldi.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Pack200.Unpacker;

import pldi.heap.Memory;

public class CTR extends AbstractRunTimeObject
{
	public ArrayList<Integer> pointers = new ArrayList<Integer>();

	
	public CTR(Memory heap, int addr) {
		super(heap, addr); 
	}

	public CTR(Memory heap) {
		super(heap);
	}


	@Override
	public int[] pack() 
	{
		int a[] = new int[pointers.size() + 2];

		a[1] = pointers.size();

		for (int i = 0; i < pointers.size(); i++)
		{
			a[i + 2] = pointers.get(i);
		}

		a[0] = tag().getTag();

		return a;
	}
	
	

	@Override
	public void unpack()
	{
		int n = heap.r(addr + 1); 
		
		pointers = new ArrayList<Integer>();
		
		for (int i = 0; i < n; i++)
		{
			pointers.add( heap.r(addr + i + 2) );
		}

	}


	@Override
	public Tag tag()
	{
		return Tag.CTR;
	}

	@Override
	public List<Integer> pointers() {
		return pointers;
	}

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();

		sb.append("(CTR n=" + pointers.size() + " :p=");

		for (int p : this.pointers)
		{
			sb.append(p + " ");
		}

		sb.append(")");

		return sb.toString();
	}


}
