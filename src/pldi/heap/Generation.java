package pldi.heap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pldi.collect.OutOfMemoryException;
import pldi.objects.AbstractRunTimeObject;

public class Generation 
{
	public int id; 
	int heap[];
	int hptr; 
	int addr;
	int size;
	
	private Memory memory;
	
	Chunk head;
	
	Map<Integer, Chunk> allocated = new HashMap<Integer, Chunk>(); Set<Integer> marked = new HashSet<Integer>();

	
	public Generation(int id, int addr, int size, Memory memory)
	{
		this.id   = id; 
		this.size = size;
		this.addr = addr;
		this.heap = new int[size]; 
		
		this.head = Chunk.mkRoot(addr, size);
		
		this.memory = memory;
	}
	
	public Chunk alloc(int size) throws OutOfMemoryException
	{
		Chunk chunk = Chunk.allocate(head, size);
		
		// System.out.println("Alloc " + chunk.addr + ":" + chunk.size);
		
		allocated.put(chunk.addr, chunk);

		return chunk;
	}
	
	public void free(int addr)
	{
		
		Chunk node = allocated.get(addr);
		
		allocated.remove(node.addr);
		
		// System.out.println("Freeing " + node.addr + " " + node.size + " RTO: " + AbstractRunTimeObject.deserialize(memory, node.addr));

		Chunk.release(head, node);
		
		// System.out.println("List is now " + head);
		
	}
	
	public int comp(int addr)
	{
		if (addr < this.addr)  
			return -1;
		else if (addr < this.addr + size)
			return 0;
		else
			return 1;
	}

	public void mmark(Integer addr)
	{
		marked.add(addr);
	}
	
	// Collect unmarked (unreachable objects)
	public void sweep()
	{
		List<Integer> bin = new ArrayList<Integer>();
		
		for (int a : allocated.keySet())
			if (!marked.contains(a)) 		
				bin.add(a);
		
		for (int a : bin)
		{
			AbstractRunTimeObject rto = AbstractRunTimeObject.deserialize(memory, a); rto.unpack();
			
			System.out.println("Collected " + rto.index() + ":" + rto.toString());
		}
		
		System.out.println();
						
		for (int a : bin)
			free(a);
		
	}

	// Promote uncollected objects to older generation
	public void pmote()
	{
		List<Integer> bin = new ArrayList<Integer>();
		
		if (id + 1 < this.memory.generations.size())
		{
			Generation tenured = this.memory.generations.get(id + 1);

			for (Chunk src_chunk : allocated.values())
			{
				try 
				{
					
					Chunk dst_chunk = tenured.alloc(src_chunk.size);
					Chunk.copy(memory, src_chunk, dst_chunk);
					memory.index.forward(src_chunk.addr, dst_chunk.addr);
					bin.add(src_chunk.addr);
					
				} catch (OutOfMemoryException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		
		for (int a : bin)
			free(a);
	}
	
	// Reset marked
	public void umark()
	{
		marked.clear();
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Gen " + id);
		sb.append(" with Base Addr " + addr);
		sb.append(" of Size " + size + " contains:\n");
		
		if (allocated.keySet().isEmpty()) 
			sb.append("    Nothing\n");
		
		for (int a : allocated.keySet())
		{
			AbstractRunTimeObject rto = AbstractRunTimeObject.deserialize(memory, a);
			
			rto.unpack();
			
			sb.append("    " + rto.index() + ":" + rto.toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}
		
}
