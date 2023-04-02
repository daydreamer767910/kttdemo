package daily_bill;

import java.io.IOException;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(args.length);
		if(args.length!=3)
		{
			System.out.println("input params error!!");
			System.out.println("usage: java -jar dailybill.jar $1 $2 $3");
			return;
		}
		BillDoc doc = new BillDoc();
		if(args[0].equals("paidan"))
		{
			BillList list = new BillList();
			
			//System.out.println(args[1]);
			doc.Read(args[1], list);
			//list.show_list();
			//list.show_keys();
			doc.Write(args[2],list);
		}
		else if(args[0].equals("huidan"))
		{
			doc.merge(args[1], args[2]);
		}
	}

}
