package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
/**
 * MenuInfo 菜单表实体类  保存菜单的的基本信息信息， 对应连接数据库表名 menu，直接存放数据表中的内容供接口使用
 * Integer id 菜单表  menu 主键 id
 * Integer pid  父节点ID  所属于的父菜单的id
 * String name 菜单名  记录FXML的文件名，用于导航加载FX面板
 * String name 菜单标题 菜单显示的标题
 */
@Entity
@Table(	name = "menu",
        uniqueConstraints = {
        })//连接到名称为menu的数据表
public class MenuInfo {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userTypeIds;
    private Integer pid;

    @Size(max = 40)
    private String name;


    @Size(max = 40)
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserTypeIds() {
        return userTypeIds;
    }

    public void setUserTypeIds(String userTypeIds) {
        this.userTypeIds = userTypeIds;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}