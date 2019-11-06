package pldi.heap;

import pldi.collect.OutOfMemoryException;

public class Chunk {

	int addr;
	int size;

	Chunk next;
	Chunk prev;
	
	boolean head = false;

	public Chunk() 
	{
		this.head = true; 
	}

	public Chunk(int addr, int size) {
		this.addr = addr;
		this.size = size;
	}

	// Insert list in front of this node
	public void insert_next(Chunk node) {
		node.next = next;
		node.prev = this;

		if (next != null)
			next.prev = node;

		this.next = node;
	}
	
	// Insert list behind this node
	public void insert_prev(Chunk node) {
		node.prev = prev;
		node.next = this;
		
		if (prev != null)
			prev.next = node;
		
		this.prev = node;
	}

	public void delete() {
		
		if (next != null)
			next.prev = prev;

		if (prev != null)
			prev.next = next;
		
		next = null;
		prev = null;
	}

	public Chunk split(int size) {
		
		this.addr += size;
		this.size -= size;
		
		return new Chunk(this.addr - size, size);
	}

	// Coalesce right
	public void coalesce_next() {
		if (next != null) {
			if (this.addr + size == next.addr) {
				this.size += next.size;
				next.delete();
			}
		}
	}
	
	public static void copy(Memory heap, Chunk src, Chunk dst)
	{
		assert(src.size == dst.size);
		
		for (int i = 0; i < src.size; i++)
		{
			heap.w(dst.addr + i, heap.r(src.addr + i));
		}
	}
	
	public static void release(Chunk root, Chunk node)
	{
		if (root.next == null)
		{
			root.insert_next(node); return;
		}
		
		Chunk curr = root.next;
		
		
		while(curr != null)
		{
			
			if (node.addr < curr.addr)
			{
				curr.insert_prev(node); 
				
				node.coalesce_next();
				
				if (node.prev != null && !node.prev.head)
					node.prev.coalesce_next();
					
				
				return;
			}
			
			if (curr.next == null)
			{
				curr.insert_next(node); 
				
				curr.coalesce_next();
				
				if (curr.prev != null && !node.prev.head)
					curr.prev.coalesce_next();
				
				return;
			}
			
			curr = curr.next;
			
		}
		
	}
	
	public static Chunk allocate(Chunk root, int size) throws OutOfMemoryException
	{
		Chunk curr = root.next;
		
		while(curr != null)
		{
			if (size <= curr.size)
			{
				return curr.split(size);
			}
			
			curr = curr.next;
		}
		
		throw new OutOfMemoryException();

	}
	
	public static Chunk mkRoot(int addr, int size)
	{
		Chunk root = new Chunk(); 
		Chunk next = new Chunk(addr, size);
		
		root.insert_next(next);
		
		return root;
	}
	
	@Override
	public String toString() 
	{
		
		String node = "[" + this.addr + ":" + this.size + "]";
		
		if (head)
			node = "[HEAD]";
		
		String prep = (prev == null ? "(" : "");
		
		if (next != null)
			return prep + (node + " " + next.toString());
		else 
			return prep + (node + ")");
	}
	
	public String toStringNode()
	{
		return "[" + this.addr + ":" + this.size + "]";
	}
	
}
