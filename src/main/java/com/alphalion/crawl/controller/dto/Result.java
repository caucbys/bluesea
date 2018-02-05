package com.alphalion.crawl.controller.dto;


public class Result {
    private Object data;                             // 数据
    private String msg;                              // 提示
    private Boolean success = false;                 // 业务操作状态
    private Integer code;
    protected Long total = 0L;                       // 总记录数;

    public static final Boolean RESULT_SUCCESSED = true; // 业务操作失败;
    public static final Boolean RESULT_FAILED = false; // 业务操作成功;

    public Result(String msg, Boolean success) {
        super();
        this.msg = msg;
        this.success = success;
    }

    public Result(Object data, String msg, Boolean success, Long total) {
        super();
        this.data = data;
        this.msg = msg;
        this.success = success;
        this.total = total;
    }

    public Result() {
        super();
    }

    /**
     * 功能描述:返回操作成功
     *
     * @Author:SongBaoYu
     * @Date:2017年1月10日 下午3:18:09
     */
    public static Result successed(Object data) {
        Result result = new Result(data, "操作成功", true, 1L);

        return result;
    }

    /**
     * 功能描述: 返回成功和自定义msg
     *
     * @Author:SongBaoYu
     * @Date:2017年1月16日 上午11:01:47
     */
    public static Result successed(Object data, String msg) {
        Result result = new Result(data, msg, true, 0L);

        return result;
    }

    public static Result successed(Object data, String msg, Long total) {
        Result result = new Result(data, msg, true, total);

        return result;
    }

    /**
     * 功能描述:操作失败
     *
     * @Author:SongBaoYu
     * @Date:2017年1月10日 下午3:18:23
     */
    public static Result failed(String msg) {
        Result result = new Result(msg, false);
        return result;
    }


    /**
     * 功能描述:分页查询操作成功
     *
     * @Author:SongBaoYu
     * @Date:2017年1月10日 下午3:22:02
     */
    public static Result listSuccessed(Object data, Long total) {
        Result result = new Result(data, "查询成功", true, total);

        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
