package pldi.objects;

import java.util.ArrayList;
import java.util.List;

import pldi.heap.Memory;

public class FUN extends AbstractRunTimeObject 
{
	
	public FUN(Memory heap) {
		super(heap);
		// TODO Auto-generated constructor stub
	}

	public char f; public int n; public List<Integer> pointers = new ArrayList<Integer>();
	
	@Override
	public int[] pack() 
	{
		int a[] = new int[pointers.size() + 3];

		for (int i = 0; i < pointers.size(); i++)
		{
			a[i + 3] = pointers.get(i);
		}
		
		a[0] = tag().getTag(); a[1] = f; a[2] = pointers.size();
				
		return a;
	}
	
	@Override
	public void unpack() 
	{
		// TODO Auto-generated method stub
		
		f = (char) heap.r(addr + 1);
		n = heap.r(addr + 2); pointers = new ArrayList<Integer>();
		
		for (int i = 0; i < n; i++)
		{
			pointers.add( heap.r(addr + i + 3) );
		}

	}
	
	@Override
	public Tag tag() 
	{
		return Tag.FUN;
	}
	
	public FUN(Memory heap, int addr) {
		super(heap, addr);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() 
	{
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("(FUN " + f +  " n=" + pointers.size() + " :p=");

		for (int p : this.pointers)
		{
			sb.append(p + " ");
		}

		sb.append(")");

		return sb.toString();
	}
	
	@Override
	public List<Integer> pointers() 
	{
		return pointers;
	}
}
