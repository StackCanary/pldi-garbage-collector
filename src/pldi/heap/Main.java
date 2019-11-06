package pldi.heap;

import java.util.ArrayList;
import java.util.List;

import pldi.collect.Collector;
import pldi.objects.BUL;
import pldi.objects.CON;
import pldi.objects.CTR;
import pldi.objects.FUN;
import pldi.objects.IND;
import pldi.objects.INT;
import pldi.objects.NUL;
import pldi.objects.WPT;

public class Main 
{
	public static void main(String... strings)
	{
		// Create a Heap
		Memory memory = new Memory();
		
		// Create Generations for heap 
		memory.add(512);      // Gen 0
		memory.add(512 * 2); // Gen 1
		memory.add(512 * 4); // Gen 2
		
		List<Integer> stack = new ArrayList<Integer>(); // A stack of addresses (call stacks) (Our roots)
		
		// Unreachable (ctr int bul) 
		
		INT int_a = new INT(memory); int_a.i = 42; int_a.alloc_commit();
		BUL bul_a = new BUL(memory); bul_a.alloc_commit();
		CTR ctr_a = new CTR(memory); 
		ctr_a.pointers.add(int_a.index()); 
		ctr_a.pointers.add(bul_a.index());
		ctr_a.alloc_commit();
		
		
		WPT wpt_e = new WPT(memory); wpt_e.p = ctr_a.index(); wpt_e.alloc_commit();
		
		
		// Cons (Ctr int bul) 'nul  
		
		INT int_b = new INT(memory); int_b.alloc_commit();
		BUL bul_b = new BUL(memory); bul_b.alloc_commit();
		
		CTR ctr_b = new CTR(memory); 
		ctr_b.pointers.add(int_b.index()); 
		ctr_b.pointers.add(bul_b.index()); 
		ctr_b.alloc_commit();

		NUL nul_b = new NUL(memory); nul_b.alloc_commit();
		
		CON con_b = new CON(memory); 
		con_b.p1 = ctr_b.index(); 
		con_b.p2 = nul_b.index(); 
		con_b.alloc_commit();
		
		INT int_c = new INT(memory); int_c.alloc_commit();
		BUL bul_c = new BUL(memory); bul_c.alloc_commit();
		CTR ctr_c = new CTR(memory); 
		ctr_c.pointers.add(int_c.index()); 
		ctr_c.pointers.add(bul_c.index());
		ctr_c.alloc_commit();
		
		// (Cons (Ctr int bul) (Cons (ctr int bul )' bul) 
		
		CON con_c = new CON(memory);
		con_c.p1 = ctr_c.index(); 
		con_c.p2 = con_b.index();
		con_c.alloc_commit();

		// ctr (ind (f int))

		INT int_d1 = new INT(memory); int_d1.alloc_commit();
		FUN fun_d1 = new FUN(memory); fun_d1.f = 'f'; fun_d1.pointers.add(int_d1.index()); fun_d1.alloc_commit(); 
		IND ind_d1 = new IND(memory); ind_d1.p = fun_d1.index(); ind_d1.alloc_commit();
		
		int ind_d1_index = ind_d1.index();

		CTR ctr_d = new CTR(memory); ctr_d.pointers.add(ind_d1.index()); ctr_d.alloc_commit();
		
		
		// Ind (Int)

		INT int_g = new INT(memory); int_g.alloc_commit();
		IND ind_g = new IND(memory); ind_g.p = int_g.index(); ind_g.alloc_commit(); 

		stack.add(con_c.index());
		stack.add(ctr_d.index());
		stack.add(wpt_e.index());
		stack.add(ind_g.index());
				    

		System.out.println(memory);
				
		Collector collector = new Collector(memory);
		
		collector.gcCollect(memory.generations.get(0), stack);
		
		System.out.println(memory);
		 

		//  A substantial collection
		
		INT int_f1 = new INT(memory); int_f1.alloc_commit();
		FUN fun_f1 = new FUN(memory); fun_f1.f = 'g'; fun_f1.pointers.add(int_f1.index()); fun_f1.alloc_commit();
		
		ind_d1.resetaddr();
		
		ind_d1.p = fun_f1.index(); ind_d1.commit();
		
		
				
		ind_g.resetaddr();
		
		INT int_h = new INT(memory); int_h.i = 99; int_h.alloc_commit();
		
		ind_g.p = int_h.index(); int_g.commit();
		
		stack.add(int_h.index());
		
		System.out.println(memory);

		collector.gcCollect(memory.generations.get(1), stack);
				
		System.out.println(memory);
		
		
		while(stack.size() > 1)
		{
			stack.remove(stack.size() - 1);
		}
		
		System.out.println(stack);
		
		
		System.out.println(memory);

		collector.gcCollect(memory.generations.get(2), stack);
				
		System.out.println(memory);
		
		System.out.println();

		System.out.println();
		
		
		
				
	}
	
}
