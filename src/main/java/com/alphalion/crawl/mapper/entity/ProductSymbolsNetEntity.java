package com.alphalion.crawl.mapper.entity;

import com.alphalion.crawl.application.util.SymbolUtil;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * 功能描述:实体类
 *
 * @author SongBaoYu
 * @date 2018年01月10日 20:39:51
 */


@Table(name = "product_symbols_net")
@Data
public class ProductSymbolsNetEntity {

    @Id
    private Long id;
    private String cusip;
    private String isin;
    private String symbol;
    private String sedol;
    private String invalid_value;
    @Transient
    private Long product_id;


    public ProductSymbolsNetEntity() {
    }

    public ProductSymbolsNetEntity(String cusip, String isin, String symbol, String sedol) {
        this.cusip = cusip;
        this.isin = isin;
        this.symbol = symbol;
        this.sedol = sedol;
        this.invalid_value=cusip;
    }

    public String getCusip() {
        if (SymbolUtil.checkISIN(this.isin)) {
            if (!SymbolUtil.checkCUSIP(this.cusip)) {
                this.cusip = this.isin.substring(2, 11);
            }
        }
        return cusip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductSymbolsNetEntity that = (ProductSymbolsNetEntity) o;
        return Objects.equals(cusip, that.cusip) &&
                Objects.equals(isin, that.isin) &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(sedol, that.sedol);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cusip, isin, symbol, sedol);
    }


}
