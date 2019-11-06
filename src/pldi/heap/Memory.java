package pldi.heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pldi.collect.OutOfMemoryException;

public class Memory 
{
	public List<Generation> generations;

	public Set<Integer> rememberedSet = new HashSet<Integer>(); // Pointers from older generations into new generations

	public Map<Integer, Integer> fwdTable = new HashMap<Integer, Integer>();
	
	public Index index = new Index();

	public Memory()
	{
		generations = new ArrayList<Generation>();
	}

	// Updates Remembered Set memory.mut(obj.addr, pointer)
	
	public void mut(int from, int to)
	{
		if (gen(from).comp(to) < 1)
			rememberedSet.add(to);
		else
			if (rememberedSet.contains(to))
				rememberedSet.remove(to);
	}

	// Sweep 
	public void sweep(Generation g)
	{
		for (int i = 0; i <= g.id; i++)
		{
			generations.get(i).sweep();
		}
	}
	
	// Promote survivors
	public void pmote(Generation g)
	{
		
		for (int i = g.id; i >= 0; i--)
		{
			generations.get(i).pmote();
		}
		
	}
	
	// Unmark generation
	public void umark(Generation g)
	{
		for (int i = 0; i <= g.id; i++)
		{
			generations.get(i).umark();
		}
	}
	
	public void add(int size)
	{

		int sum = 0;

		for (Generation g : generations)
		{
			sum = sum + g.size;
		}

		generations.add(new Generation(generations.size(), sum, size, this));
	}

	public Generation gen(int addr)
	{	
		int sum = 0;

		for (Generation g : generations)
		{
			sum = sum + g.size;

			if (addr < sum)
				return g;
		}

		return null;
	}

	public void w(int addr, int value)
	{
		Generation g = gen(addr); g.heap[addr - g.addr] = value;

		// System.out.println("Write " + addr + ":" + value);
	}

	public  int r(int addr)
	{
		Generation g = gen(addr); int value = g.heap[addr - g.addr];
		
		// System.out.println(" Read " + addr + ":" + value);
		
		return value;
	}

	public int allocate(Generation g, int size)
	{
		Chunk c = null;
		
		try {
			c = g.alloc(size);
		} catch (OutOfMemoryException e) {
			e.printStackTrace();
		}
		
		return c.addr;
	}

	
	@Override
	public String toString() 
	{
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Generations: \n");
		
		for (Generation g : generations)
		{
			sb.append(g.toString());
		}

		return sb.toString();
	}
	


}
