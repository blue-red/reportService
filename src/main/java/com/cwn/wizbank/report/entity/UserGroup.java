package com.cwn.wizbank.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * 用户组
 * @author jack.wang 2018-04-26
 */
@Table(name = "usergroup")
@Entity
public class UserGroup {

    @Id
    @Column(name = "usg_ent_id")
    private Long usgEntId;

    @Column(name = "usg_display_bil")
    private String usgDisplayBil;

    @Column(name = "usg_role")
    private String usgRole;

    @Column(name = "usg_ent_id_root")
    private Integer usgEntIdRoot;

    public Long getUsgEntId() {
        return usgEntId;
    }

    public void setUsgEntId(Long usgEntId) {
        this.usgEntId = usgEntId;
    }

    public String getUsgDisplayBil() {
        return usgDisplayBil;
    }

    public void setUsgDisplayBil(String usgDisplayBil) {
        this.usgDisplayBil = usgDisplayBil;
    }

    public String getUsgRole() {
        return usgRole;
    }

    public void setUsgRole(String usgRole) {
        this.usgRole = usgRole;
    }

    public Integer getUsgEntIdRoot() {
        return usgEntIdRoot;
    }

    public void setUsgEntIdRoot(Integer usgEntIdRoot) {
        this.usgEntIdRoot = usgEntIdRoot;
    }
}
