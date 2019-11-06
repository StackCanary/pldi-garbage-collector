package pldi.heap;

import java.util.HashMap;
import java.util.Map;

/*
 * A table of indexes to pointers
 */
public class Index 
{
	
	Map<Integer, Integer> idx_to_ptr = new HashMap<Integer, Integer>();
	Map<Integer, Integer> ptr_to_idx = new HashMap<Integer, Integer>();
	
	int nxt = 0;
	
	public Index()
	{
		
	}
	
	public int indexOfAddr(int ptr)
	{
		if (ptr_to_idx.containsKey(ptr))
			return ptr_to_idx.get(ptr);
		
		ptr_to_idx.put(ptr, nxt);
		idx_to_ptr.put(nxt, ptr); 
		
		return nxt++;
	}
	
	public int getPtrFromIndex(int idx)
	{
		
		if (!idx_to_ptr.containsKey(idx)) 
		{
			System.out.println("Could not find idx " + idx) ;
		}
		
		return idx_to_ptr.get(idx);
	}

	public void forward(int src, int dst)
	{
		int idx = ptr_to_idx.get(src);
		
		idx_to_ptr.put(idx, dst);
		
		ptr_to_idx.remove(src);
		
		ptr_to_idx.put(dst, idx);
	}

}
