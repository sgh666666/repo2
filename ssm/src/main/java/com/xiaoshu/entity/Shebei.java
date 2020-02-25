package com.xiaoshu.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Shebei implements Serializable {
    @Id
    private Integer id;

    private Integer sbtypeid;

    private String sname;

    private String sbram;

    private String color;

    private Integer price;

    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    @Transient
    private Sbtype sbtype;
    private static final long serialVersionUID = 1L;

    public Sbtype getSbtype() {
        return sbtype;
    }

    public void setSbtype(Sbtype sbtype) {
        this.sbtype = sbtype;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return sbtypeid
     */
    public Integer getSbtypeid() {
        return sbtypeid;
    }

    /**
     * @param sbtypeid
     */
    public void setSbtypeid(Integer sbtypeid) {
        this.sbtypeid = sbtypeid;
    }

    /**
     * @return sname
     */
    public String getSname() {
        return sname;
    }

    /**
     * @param sname
     */
    public void setSname(String sname) {
        this.sname = sname == null ? null : sname.trim();
    }

    /**
     * @return sbram
     */
    public String getSbram() {
        return sbram;
    }

    /**
     * @param sbram
     */
    public void setSbram(String sbram) {
        this.sbram = sbram == null ? null : sbram.trim();
    }

    /**
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color
     */
    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    /**
     * @return price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sbtypeid=").append(sbtypeid);
        sb.append(", sname=").append(sname);
        sb.append(", sbram=").append(sbram);
        sb.append(", color=").append(color);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", createtime=").append(createtime);
        sb.append("]");
        return sb.toString();
    }
}