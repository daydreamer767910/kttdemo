package daily_bill;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap; // 引入 HashMap 类
import java.util.Set;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class BillList {
	private HashMap<String ,Bill> bill_list;
	private static BillHeader[] bill_header_KT = {new BillHeader("跟团号","跟团号")
			,new BillHeader("订单号","订单号")
			,new BillHeader("商品","商品")
			,new BillHeader("规格","规格")
			,new BillHeader("数量","数量")
			,new BillHeader("商品成本价","商品成本价")
			,new BillHeader("运费","运费")
			,new BillHeader("收货人","收货人")
			,new BillHeader("联系电话","联系电话")
			,new BillHeader("详细地址","详细地址")
			,new BillHeader("物流公司","物流公司")
			,new BillHeader("物流单号","物流单号")};
			//{"跟团号",{"订单号","商品","规格","数量","商品成本价","运费","收货人","联系电话","详细地址","物流公司","物流单号"};
	private static BillHeader[] bill_header_1688 = {new BillHeader("订单号","订单号")
			,new BillHeader("商品","商品名称")
			,new BillHeader("数量","商品数量(件)")
			,new BillHeader("规格","商品规格1")
			,new BillHeader("规格2","商品规格2")
			,new BillHeader("收货人","收件人-姓名")
			,new BillHeader("联系电话","收件人-手机")
			,new BillHeader("省","收件人-省")
			,new BillHeader("市","收件人-市")
			,new BillHeader("区","收件人-区")
			,new BillHeader("详细地址","收件人-详细地址")
			,new BillHeader("收货地址","收货地址")
			,new BillHeader("买家留言","买家留言")
			,new BillHeader("商品编码","1688商品链接/1688商品id")
			,new BillHeader("商品编码","分销or现货（默认或者填1走分销，0走现货）")};
	// = {"订单号","商品","数量","规格","规格2","收货人","联系电话","省","市","区","详细地址"};
	private static BillHeader[] bill_header_HN = {new BillHeader("商品","*商品名称及规格")
			,new BillHeader("编号","*供应编号")
			,new BillHeader("数量","*数量")
			,new BillHeader("收货人","*收件人姓名")
			,new BillHeader("联系电话","*收件人手机号码")
			,new BillHeader("详细地址","*收件人地址")
			,new BillHeader("备注","备注")
			,new BillHeader("发货人姓名","发货人姓名")
			,new BillHeader("发货人手机","发货人手机")
			,new BillHeader("订单号","第三方订单号")};
	// = {"商品","供应编号","数量","收货人","联系电话","详细地址","备注","发货人姓名","发货人手机","跟团号"};
	public BillList()
	{
		bill_list = new HashMap<String,Bill>();
	}
	
	public static BillHeader[] get_header(String type)
	{
		if(type.contains("阿里"))
			return bill_header_1688;
		else if(type.contains("惠农"))
			return bill_header_HN;
		else
			return bill_header_KT;
	}
	public Bill add(String provider_id)//,String[] rcd)
	{
		Bill bill = bill_list.get(provider_id);
		if(bill==null) {
			bill = new Bill();
			if(provider_id.contains("阿里"))
				bill.set_header(bill_header_1688);
			else if(provider_id.contains("惠农"))
				bill.set_header(bill_header_HN);
			else
				bill.set_header(bill_header_KT);
			bill_list.put(provider_id, bill);
		}
		return bill;
		//return bill.insert_record(rcd);
	}
	
	
	public Set<String> get_providers()
	{
		return bill_list.keySet();
	}
	
	public Bill get_bill(String provider)
	{
		return bill_list.get(provider);
	}
}
