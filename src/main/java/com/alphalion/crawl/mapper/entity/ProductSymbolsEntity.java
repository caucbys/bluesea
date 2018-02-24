package com.alphalion.crawl.mapper.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 功能描述:实体类
 *
 * @author SongBaoYu
 * @date 2018年01月10日 13:28:55
 */


@Table(name = "product_symbols")
@Data
public class ProductSymbolsEntity {

    //property
    @Id
    private Long id;
    private Long product_id;
    private String symbol;
    @ApiModelProperty(allowableValues = "ISIN,CUSIP,SYMBOL,SEDOL")
    private String type_of_symbol;
    @ApiModelProperty(example = "2018-02-21")
    private Date create_time;
    @ApiModelProperty(example = "2018-01-18")
    private Date business_from_date;
    @ApiModelProperty(example = "4821-12-27")
    private Date business_thru_date;
    @ApiModelProperty(example = "2018-01-18")
    private Date process_in_date;
    @ApiModelProperty(example = "4821-12-27")
    private Date process_out_date;
    @ApiModelProperty(example = "reference,db,system")
    private String update_by;
    @ApiModelProperty(example = "Quantex,newFound")
    private String update_reason;


}
