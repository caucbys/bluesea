package com.alphalion.crawl.mapper.entity;

import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Id;
import lombok.Data;

/**
 * 功能描述:实体类
 * 
 * @author SongBaoYu
 * @date 2018年01月11日 21:39:08
 */


@Table(name="product_settlement_detail")
@Data
public class ProductSettlementDetailEntity {

    //property
	@Id
	private Integer id;
	private Long product_id;
	private String primary_custodian;
	private String dtc_indicator;
	private String nsc_indicator;
	private String sweep_destination;
	private String sweep_symbol;
	private Boolean hard_to_borrow;
	private String settlement_currency;
	private Date create_time;
	private Date business_from_date;
	private Date business_thru_date;
	private Date process_in_date;
	private Date process_out_date;
	private String update_by;

	
}
