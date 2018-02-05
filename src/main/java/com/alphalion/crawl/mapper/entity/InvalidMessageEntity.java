package com.alphalion.crawl.mapper.entity;

import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Id;
import lombok.Data;

/**
 * 功能描述:实体类
 * 
 * @author SongBaoYu
 * @date 2018年01月10日 13:35:20
 */


@Table(name="invalid_message")
@Data
public class InvalidMessageEntity {

    //property
	@Id
	private Long id;
	private String source;
	private String target;
	private String reason;
	private String class_name;
	private String content;
	private Date create_time;
	private Date update_time;
	private String create_by;
	private String update_by;
	private String status;
	private String remark;
	private String invalid_value;
	private Boolean modified;
	private Integer company_id;

	
}
