package com.wt.wtchat.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 平台
 *
 * @author Zed
 */
@Entity
@Table(name = "platform")
public class Platform implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", length = 10)
    private int id;
    @Column(name = "name", length = 10)
    private String name;
    @Column(name = "code", length = 10)
    private String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
