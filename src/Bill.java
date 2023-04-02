package daily_bill;

import java.util.List;
import java.util.ArrayList;

public class Bill {

	private List<BillHeader> Header;
	// = {"跟团号","商品","规格","数量","商品成本价","收货人","联系电话","省","市","区","详细地址","快递公司","快递单号"};
	private List<BillRecord> Records;
	public Bill()
	{
		this.Header = new ArrayList<BillHeader>();
		this.Records = new ArrayList<BillRecord>();
	}
	public void set_header(BillHeader[] header_info)
	{
		for(BillHeader h:header_info)
		{
			this.Header.add(h);
		}
	}
	public BillHeader[] get_header()
	{
		BillHeader[] h = new BillHeader[this.Header.size()];
		this.Header.toArray(h);
		return h;
	}
	public int get_header_size()
	{
		return this.Header.size();
	}
	public int get_record_count()
	{
		return this.Records.size();
	}
	public boolean insert_record(String[] rcd_data)
	{
		if(rcd_data.length > get_header_size())
			return false;
		
		BillRecord rcd = new BillRecord();
		
		if(rcd.insert(rcd_data))
		{
			return this.Records.add(rcd);
		}
		
		return false;
	}
	
	
	public String get_record_field(int idx,int field)
	{
		if(idx<Records.size())
		{
			return Records.get(idx).get_field(field);
		}
		
		return null;
	}
}
