package daily_bill;
import java.util.List;
import java.util.ArrayList;
//import java.util.Iterator;

public class BillRecord {
	private List<String> RecordFields;

	
	public BillRecord(){
		RecordFields = new ArrayList<String>();

	}
	
	public boolean insert(String[] fields)
	{
		boolean bret = false;
		for(String f:fields)
		{
			bret = RecordFields.add(f);
			if(!bret)
				break;
		}
		return bret;
	}
	
	public int get_size()
	{
		return RecordFields.size();
	}
	
	
	public String get_field(int idx)
	{
		return RecordFields.get(idx);
	}
}
