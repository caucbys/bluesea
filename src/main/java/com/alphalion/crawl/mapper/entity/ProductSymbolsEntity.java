package com.alphalion.crawl.mapper.entity;

import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Id;
import lombok.Data;

/**
 * 功能描述:实体类
 * 
 * @author SongBaoYu
 * @date 2018年01月10日 13:28:55
 */


@Table(name="product_symbols")
@Data
public class ProductSymbolsEntity {

    //property
	@Id
	private Long id;
	private Long product_id;
	private String symbol;
	private String type_of_symbol;
	private Date create_time;
	private Date business_from_date;
	private Date business_thru_date;
	private Date process_in_date;
	private Date process_out_date;
	private String update_by;
	private String update_reason;

	
}
