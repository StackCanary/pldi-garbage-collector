package pldi.objects;

import java.util.ArrayList;
import java.util.List;

import pldi.heap.Memory;

public abstract class AbstractRunTimeObject 
{
	
	public int addr; public int indx; public Memory heap;
	
	public AbstractRunTimeObject(Memory heap)
	{
		this.heap = heap;
	}
	
	public AbstractRunTimeObject(Memory heap, int addr)
	{
		this.heap = heap;
		this.addr = addr;
	}

	public enum Tag
	{
		INT(0), BUL(1), CTR(2), CON(3), FUN(4), NUL(5), IND(6), WPT(7);
		
		final int tag;
		
		Tag(int tag)
		{
			this.tag = tag;
		}
		
		public int getTag()
		{
			return this.tag;
		}
		
		public static Tag toTag(int value)
		{
			switch(value)
			{
			case 0: return INT;
			case 1: return BUL;
			case 2: return CTR;
			case 3: return CON;
			case 4: return FUN;
			case 5: return NUL;
			case 6: return IND;
			case 7: return WPT;
			}
			
			return null;
			
		}
	}
	
	public void commit()
	{
		
		int a = addr;
		
		for (Integer i : pack())
		{
			heap.w(a, i); a++;
		}
	}
	
	public abstract int[] pack();
	
	public abstract void unpack();
		
	public abstract Tag tag();
	
	// TODO get children pointers
	public List<Integer> pointers() {
		return new ArrayList<Integer>();
	}
	
	public void allocate()
	{
		this.addr = heap.allocate(heap.generations.get(0), pack().length);
		this.indx = this.heap.index.indexOfAddr(this.addr);
	}
	
	public void resetaddr()
	{
		this.addr = this.heap.index.getPtrFromIndex(this.indx);
	}


	// Deserialize Object from memory 
	public static AbstractRunTimeObject deserialize(Memory heap, int addr)
	{
		switch(Tag.toTag(heap.r(addr)))
		{
		case BUL: return new BUL(heap, addr);
		case CON: return new CON(heap, addr);
		case CTR: return new CTR(heap, addr);
		case FUN: return new FUN(heap, addr);
		case IND: return new IND(heap, addr); 
		case INT: return new INT(heap, addr); 
		case NUL: return new NUL(heap, addr);
		case WPT: return new WPT(heap, addr);
		}
		
		return null;
	}
	
	public int index()
	{
		return this.heap.index.indexOfAddr(this.addr);
	}
	
	public void alloc_commit()
	{
		this.allocate(); this.commit();
	}
	
}
