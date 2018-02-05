package com.alphalion.crawl.mapper.entity;

import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Id;
import lombok.Data;

/**
 * 功能描述:实体类
 * 
 * @author SongBaoYu
 * @date 2018年01月11日 17:28:34
 */


@Table(name="business_date")
@Data
public class BusinessDateEntity {

    //property
	@Id
	private Integer id;
	private Date business_date;
	private Date next_business_date;
	private Date create_time;
	private String created_by;

	
}
