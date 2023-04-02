package daily_bill;

//导入所需的类
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class BillDoc {
	private static String doc_suffix = "-露露美食(18665316526).xlsx";
	
	private String make_filename(String path,String provider,String suffix)
	{
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("-MMdd");
        String date = fmt.format(LocalDateTime.now());
        String filename = path;
       
        if(path.substring(path.length()-1).equals("\\"))
        	filename = path + provider +date+suffix;
        else
        {
        	filename = path +"\\"+ provider +date+suffix;
        }
		return filename;
	}
	public void Write(String path,BillList list)
	{
		int i_write_bills = 0;
		for( String provider: list.get_providers())//int i=0;i<list.get_bill_num();i++)
		{
			Bill bill = list.get_bill(provider);
			try (// 创建工作簿对象并创建一个新的工作表
					Workbook workbook = new XSSFWorkbook()) 
			{
	            Sheet sheet = workbook.createSheet("订单列表");
	            
	            // 创建一个行对象并在其单元格中写入数据
	            Row row = sheet.createRow(0);//header
	            // cell = null; //= row.createCell(0);
	            int col = 0;
	            for(BillHeader header:bill.get_header())
	            //for(col=0;col<bill.get_header_size();col++)
	            {
	            	sheet.autoSizeColumn(col);
	            	if(header.Name.equals("详细地址"))
            		{
	                	sheet.setColumnWidth(col, 10000);//sheet.getColumnWidth(col)*37/10);
            		}
	            	else if(header.Name.equals("数量"))
            		{
	                	sheet.setColumnWidth(col, 1000);//sheet.getColumnWidth(col)*37/10);
            		}
	            	else if(header.Name.equals("商品"))
            		{
	                	sheet.setColumnWidth(col, 6000);//sheet.getColumnWidth(col)*37/10);
            		}
	            	else
	            	{
	            		//sheet.setColumnWidth(col, 3000);
	            	}
	            	Cell cell = row.createCell(col);
	                cell.setCellValue(header.Value);
	                col++;
	            }
	          
	            int total_product_num = 0;
	            //Float total_product_cost = 0.0F;
	            
	            XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
                DataFormat format = workbook.createDataFormat();
                //DecimalFormat fm = new DecimalFormat("0.00");
	            for(int i = 0; i<bill.get_record_count();i++)
	            {
	            	row = sheet.createRow(i+1);
	            	
	            	Float product_cost = 0.0F;
	            	int product_num = 0;
	            	col=0;
	            	for(BillHeader header:bill.get_header())
	            	//for(col=0;col<bill.get_header_size();col++)
		            {
	            		String field = bill.get_record_field(i,col);
	            		/*if(field == null)
	            		{
	            			//col++;
	            			field = "";
	            		}*/
	            		Cell cell = row.createCell(col++);
	            		//System.out.println(header.Name);
	            		//if(field instanceof Number)
	            		if(header.Name.equals("数量"))
	            		{
	            			product_num = Float.valueOf(field).intValue();
	            			total_product_num += product_num;
	            			//cellStyle.setDataFormat(format.getFormat("0_ "));
	            			//cell.setCellStyle(cellStyle);
	            			cell.setCellValue(product_num);//fm.format(product_num));
	            		}
	            		else if(header.Name.equals("运费"))
	            		{
	            			cellStyle.setDataFormat(format.getFormat("0.00_ "));
	    	                cell.setCellStyle(cellStyle);
	            			cell.setCellValue(Float.valueOf(field));//fm.format(product_num));
	            		}
	            		else if(header.Name.equals("商品编码"))
	            		{
	            			//System.out.println(provider+field);
	            			if(header.Value.equals("1688商品链接/1688商品id"))
	            				cell.setCellValue(field.replaceFirst("-(\\S*)", ""));//fm.format(product_num));
	            			else if(header.Value.equals("分销or现货（默认或者填1走分销，0走现货）"))
	            			{
	            				if(field.isEmpty())
	            					cell.setCellValue("1");
	            				else
	            					cell.setCellValue(field.substring(field.length()-1));
	            			}
	            			else
	            				cell.setCellValue(field);
	            		}
	            		else if(header.Name.equals("买家留言"))
	            		{
	            			//System.out.println(provider);
	            			cell.setCellValue("发货电话:18665316526 发货人:露露");
	            		}
	            		else if(header.Name.equals("发货人姓名"))
	            		{
	            			//System.out.println(provider);
	            			cell.setCellValue("露露");
	            		}
	            		else if(header.Name.equals("发货人手机"))
	            		{
	            			//System.out.println(provider);
	            			cell.setCellValue("18665316526");
	            		}
	            		else if(header.Name.equals("商品成本价"))
	            		{
	            			
	            			boolean valid_val = true;
	            			if(field.isBlank() || field.isEmpty())
	            			{
	            				valid_val = false;
	            			}
	            			else
	            			{
	            				for(int k=0;k<field.length();k++)
	                			{
	                				if(field.charAt(k) !='.' && (field.charAt(k)<'0' || field.charAt(k)>'9'))
	                				{
	                					valid_val = false;
	                					System.out.println("团【"+provider+"】 invalid 商品成本价:"+ field);
	                					break;
	                				}
	                			}
	            			}
	            			if(valid_val)
	                		{
	            				product_cost = product_num* Float.valueOf(field);
	            				//total_product_cost += product_cost;
	                		}
	                		else
	                		{
	                			product_cost = 0F;
	                		}
	            			
	            			//field = Float.toString(product_cost);
	            			cellStyle.setDataFormat(format.getFormat("0.00_ "));
	    	                cell.setCellStyle(cellStyle);
	    	                cell.setCellValue(product_cost);
	            		}
	            		else
	            		{
	            			cell.setCellValue(field);
	            		}
		            }
	            }
	            //合计行
	            row = sheet.createRow(bill.get_record_count()+1);
	            col = 0;
	            for(BillHeader header:bill.get_header())
	            {
	            	if(header.Name.equals("商品成本价")||header.Name.equals("运费"))
	            	{
	            		CellRangeAddress cra = new CellRangeAddress(1, bill.get_record_count(), col, col);
	            		Cell cell = row.createCell(col);
	            		cell.setCellFormula("SUM(" + cra.formatAsString() + ")");
	            		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	            		evaluator.evaluateAll();
	            		//double cellValue = evaluator.evaluate(cell).getNumberValue();
	            		//cell.setCellValue(cellValue);
	            		//System.out.println(cellValue.toString()); 
	            		
			            cellStyle.setDataFormat(format.getFormat("0.00_ "));
			            //cell.setCellValue(0);
		                cell.setCellStyle(cellStyle);
	            	}
	            	col++;
	            }

                
	            // 创建一个文件输出流并将工作簿写入Excel文件
	            String suffix = "("+total_product_num+")"+doc_suffix;
	            String file_name = make_filename(path,provider,suffix);

	            FileOutputStream fileOut = new FileOutputStream(file_name);
	            workbook.write(fileOut);
	            fileOut.close();
	
	            // 关闭工作簿
	            workbook.close();
	            System.out.println("生成文件"+file_name+"  包含记录"+ bill.get_record_count() +"条。");
	            i_write_bills++;
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("共生成文件"+i_write_bills+"个。");
	}
	
	
	private String get_provider_id(String col_val)
	{
		//String regex = "/【(\\S*)】/g";
		String provider_id = "UNKnown";
		//if(col_val.matches("【(\\S*)】"))
			//System.out.println(col_val.toString());
		for(String retv : col_val.split("【|】"))
		{
			provider_id = retv;
		}
		if(provider_id.length()>10)
		{
			provider_id = "UNKnown";
		}
		provider_id = provider_id.replaceFirst("W", "");//remove the first 'W'
		provider_id = provider_id.replaceFirst("-(\\S*)", "");//remove the branch like '-01'
		provider_id = provider_id.replaceFirst("A(\\S*)", "阿里");//1688 all in one
		provider_id = provider_id.replaceFirst("H(\\S*)", "惠农");//HN all in one
		//System.out.println(col_val.toString());
		/*if(col_val.contains("【"))
    	{
    		int iB = col_val.lastIndexOf("【");
    		int iE = col_val.indexOf("】",iB);
    		if(iE-iB>10)
    		{
    			iE = iB+10;
    		}
    		provider_id = col_val.substring(iB+1,iE);
    		provider_id = provider_id.toUpperCase();
    		if(provider_id.contains("W"))
    		{
    			iB = provider_id.indexOf("W",0);
    			iE = provider_id.length();
    			provider_id = provider_id.substring(iB+1,iE);
    		}
    		//System.out.println(provider_id);
    		//System.out.println(Float.valueOf(provider_id).intValue());
    	}*/
		return provider_id;
	}
	
	public void Read(String path,BillList list)
	{
		//System.out.println(path);
		File file = new File(path);
		File[] fs = file.listFiles();
		if(fs == null)
			return;
		
		for(File f:fs) 
		{
			if(f.isDirectory() || !f.isFile())
				continue;
			String filename = f.toString();
			if(filename.contains("xls")||filename.contains("xlsx"))
				System.out.println("原始文件"+filename);
			else
				continue;
			try 
			{
	            // 创建工作簿对象并加载Excel文件
	            FileInputStream excelFile = new FileInputStream(new File(filename));
	            Workbook workbook = new XSSFWorkbook(excelFile);
	
	            // 获取工作表对象
	            Sheet sheet = workbook.getSheet("商品列表");//workbook.getSheetAt(1);
	            if(sheet!=null)
	            {
	            	
	            	HashMap<String,Integer> doc_header_info = new HashMap<String,Integer>();
		            //System.out.println("total row:"+sheet.getLastRowNum());
		            Row row = sheet.getRow(0);//head set_billheader_pos
		            
		            for (Cell cell:row) 
		            {
		            	doc_header_info.put(cell.toString(), cell.getColumnIndex());
		            }
		            if(doc_header_info.get("团购标题")==null)
		            {
		            	// 关闭工作簿和文件输入流
			            workbook.close();
			            excelFile.close();
	            		continue;
		            }
		            //System.out.println(doc_header_info);
	            	// 循环读取工作表中的每一行和每一列
		            for (int i=1;i<=sheet.getLastRowNum();i++)
		            {
		            	row = sheet.getRow(i);
		            	Cell cell;
		            	//if(comment_pos>=0)
		            	if(doc_header_info.get("团长备注")!=null)
		            	{
			            	cell = row.getCell(doc_header_info.get("团长备注").intValue());
			            	if(cell.toString().contains("已排"))
			            	{
			            		System.out.println(i+"行："+
			            				row.getCell(doc_header_info.get("商品").intValue()).toString()
			            				+"已排");
			            		continue;
			            	}
		            	}
		            	if(doc_header_info.get("数量")!=null)
		            	{
		            		cell = row.getCell(doc_header_info.get("数量").intValue());
		            		//System.out.println(i+"count"+cell.toString());
			            	if(Float.valueOf(cell.toString()).intValue() == 0)
			            	{
			            		//System.out.println(i+"已取消");
			            		continue;
			            	}
		            	}
		            	
		            	Bill bill = list.add(get_provider_id(row.getCell(doc_header_info.get("团购标题")).toString()));
		            	BillHeader[] bill_header = bill.get_header();
		            	String[] bill_rcd = new String[bill_header.length];
		            	
		            	for(int j=0;j<bill_header.length;j++)
		            	{
		            		//System.out.println(bill_header[j].Name);
		            		Integer idx  = doc_header_info.get(bill_header[j].Name);
		            		//if(bill_header[j].Name.equals("商品编码"))
		            			//System.out.println("商品编码:"+row.getCell(idx.intValue()));
		            		if(idx != null)
		            		{
			            		cell = row.getCell(idx.intValue());
			            		bill_rcd[j] = cell.toString();
		            		}
		            		else {
		            			//bill_rcd[j] = " ";
		            		}
		            	}
		            	bill.insert_record(bill_rcd);
		            }
		            
	            }
	            else
	            {
	            	System.out.println("原始文件无效");
	            }
	            // 关闭工作簿和文件输入流
	            workbook.close();
	            excelFile.close();
			}catch (IOException e) {
	            e.printStackTrace();
	        }
        } 
		
	}
	public void merge(String path,String to) 
	{
		//System.out.println(path);
		File file = new File(path);
		//File file_to = File(to);
		File[] fs = file.listFiles();
		if(fs == null)
			return;
		
		// 创建工作簿对象并加载Excel文件
		//System.out.println(to);

		Workbook workbook_to = new XSSFWorkbook();
		Sheet sheet_to = workbook_to.createSheet("订单列表");
		int to_row_num = 0;
		int to_col_num = 0;
		Row row_to = sheet_to.createRow(to_row_num++);
		for(BillHeader header:BillList.get_header("KT"))
		{
			Cell cell_to = row_to.createCell(to_col_num++);
			cell_to.setCellValue(header.Value);
		}
		
		for(File f:fs) 
		{
			if(f.isDirectory() || !f.isFile())
				continue;
			String filename = f.toString();
			
			// 创建工作簿对象并加载Excel文件
			try
			{
				FileInputStream excelFile= new FileInputStream(new File(filename));
				Workbook workbook;
				
				if(filename.contains("xlsx"))
				{
					workbook = new XSSFWorkbook(excelFile);
					
				}
				else if(filename.contains("xls"))
				{
					workbook = new HSSFWorkbook(excelFile);
				}
				else
				{
					System.out.println("回单:"+filename+":无效");
					continue;
				}
				System.out.print("回单:"+filename+":");
				// 获取工作表对象
	            Sheet sheet = workbook.getSheetAt(0);//workbook.getSheet("订单列表");//
	            // 循环读取工作表中的每一行和每一列
	            for (int i=1;i<=sheet.getLastRowNum();i++)//表头不读
	            {
	            	Row row = sheet.getRow(i);
	            	if(row == null)
	            		continue;
	            	row_to = sheet_to.createRow(to_row_num++);
	            	
	            	//to_col_num = 0;
	            	for(Cell cell:row)
	            	{
	            		
	    				Cell cell_to = row_to.createCell(cell.getColumnIndex());
	    				switch(cell.getCellType())
	    				{
	    				case FORMULA:
	    					cell_to.setCellFormula(cell.getCellFormula());
	    					break;
	    				case STRING:
	    					cell_to.setCellValue(cell.getStringCellValue());
	    					break;
	    				case NUMERIC:
	    					cell_to.setCellValue(cell.getNumericCellValue());
	    					break;
	    				case BOOLEAN:
	    					cell_to.setCellValue(cell.getBooleanCellValue());
	    					break;
	    				case BLANK:
	    					cell_to.setCellValue("");
	    					break;
	    				default:
	    					
	    						break;
	    						
	    				}
	    				//cell_to.setCellValue(cell.toString());
	            	}
	            }
	            System.out.println("有效单"+sheet.getLastRowNum()+"条");
            
	            
	            // 关闭工作簿和文件输入流
	            workbook.close();
	            excelFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(make_filename(to,"回单",doc_suffix));
			workbook_to.write(fileOut);
			fileOut.close();
			workbook_to.close();
			System.out.println("生成回单"+make_filename(to,"回单",doc_suffix)+":"+(to_row_num-1)+"条");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
