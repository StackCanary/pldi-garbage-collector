package pldi.collect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import pldi.heap.Generation;
import pldi.heap.Memory;
import pldi.objects.AbstractRunTimeObject;
import pldi.objects.AbstractRunTimeObject.Tag;

public class Collector 
{	
	
	Memory heap;
	
	public Collector(Memory heap)
	{
		this.heap = heap;
	}
	
	
	public long gcCollect(Generation g, List<Integer> stack)
	{
		List<Integer> roots = new ArrayList<Integer>(stack);
		
		roots.addAll(heap.rememberedSet);
		
		long a = System.nanoTime();
				
		mmark(g, stack);  // The marking phase
		heap.sweep(g);    // The sweeping phase
		heap.pmote(g);    // The promotion phase
		heap.umark(g);    // The unmarking phase
		
		long b = System.nanoTime();
		
		return b - a;
	}
	
	public void mmark(Generation gen, List<Integer> roots)
	{
		Stack<Integer> stack = new Stack<Integer>(); Set<Integer> visited = new HashSet<Integer>();

		for (Integer i : roots)
		{
			stack.push(i);
		}

		while(!stack.isEmpty())
		{
			int idex = stack.pop();
			int addr = heap.index.getPtrFromIndex(idex);

			// If i belongs to a higher generation then skip
	 		if (heap.gen(addr).id > gen.id)
				continue;

			// If i has been visited then skip
			if (visited.contains(idex))
				continue;

			// Add to visited set
			visited.add(idex);

			// Mark memory for collection
			heap.gen(addr).mmark(addr);

			// Push children
			
			AbstractRunTimeObject rto = AbstractRunTimeObject.deserialize(heap, addr); rto.unpack();
			

			for (int i : rto.pointers())
			{
				stack.push(i);
			}

		}
	}
	
	
	
}
