package com.alphalion.crawl.mapper.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 功能描述:实体类
 *
 * @author SongBaoYu
 * @date 2018年01月10日 13:36:02
 */


@Table(name = "product")
@Data
public class ProductEntity {

    @Id
    private Long id;

    private Long product_id;

    private String market_code;

    private String Integerernal_number;

    private String trading_status;

    private Integer record_status;

    private String asset_type;

    private String security_type;

    private String security_subtype;

    private String sic_code;

    private String gics_code;

    private String issue_name;

    private String issue_category;

    private String short_name;

    private String bloomberg;

    private String country_of_issue;

    private String country_of_trade;

    private String when_issued;

    private java.sql.Date when_issued_date;

    private String adp;

    private String long_description;

    private Integer exchange_id;

    @Deprecated
    private String company_long_name;

    private String currency_code;

    @Deprecated
    private String unit;

    private String source;

    /**
     * 不是常规的创建时间，含义类似股票的上市时间
     */
    private Timestamp create_time;

    private Timestamp update_time;

    private String update_source;

    private String update_reason;

    private java.sql.Date business_from_date;

    private java.sql.Date business_thru_date;

    private java.sql.Date process_in_date;

    private java.sql.Date process_out_date;

    private String update_by;


    public void date(java.sql.Date from, java.sql.Date thru, java.sql.Date in, java.sql.Date out, Timestamp create) {
        business_from_date = from;
        business_thru_date = thru;
        process_in_date = in;
        process_out_date = out;
    }

    public java.sql.Date businessFromDate() {
        return business_from_date;
    }

    public java.sql.Date businessThruDate() {
        return business_thru_date;
    }

    public void setProductId(long productId, String updateBy) {
        product_id = productId;
        update_by = updateBy;
    }
}
