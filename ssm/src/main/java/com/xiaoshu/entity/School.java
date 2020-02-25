package com.xiaoshu.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class School implements Serializable {
    @Id
    private Integer id;

    private String sname;

    private Integer areaid;

    private String phone;

    private String address;

    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createtime;
    @Transient
    private Areas areas;

    private static final long serialVersionUID = 1L;

    public School() {
    }

    public School(String sname, Integer areaid, String phone, String address, String status, Date createtime) {
        this.sname = sname;
        this.areaid = areaid;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.createtime = createtime;
    }

    public Areas getAreas() {
        return areas;
    }

    public void setAreas(Areas areas) {
        this.areas = areas;
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
     * @return areaid
     */
    public Integer getAreaid() {
        return areaid;
    }

    /**
     * @param areaid
     */
    public void setAreaid(Integer areaid) {
        this.areaid = areaid;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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
        sb.append(", sname=").append(sname);
        sb.append(", areaid=").append(areaid);
        sb.append(", phone=").append(phone);
        sb.append(", address=").append(address);
        sb.append(", status=").append(status);
        sb.append(", createtime=").append(createtime);
        sb.append("]");
        return sb.toString();
    }
}