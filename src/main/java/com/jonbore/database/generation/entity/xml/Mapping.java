package com.jonbore.database.generation.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mapping")
public class Mapping {
    @XmlAttribute(name = "jdbc")
    private String jdbc;
    @XmlAttribute(name = "java")
    private String java;
    @XmlAttribute(name = "mybatis")
    private String mybatis;

    public String getJdbc() {
        return jdbc;
    }

    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    public String getJava() {
        return java;
    }

    public void setJava(String java) {
        this.java = java;
    }


    public String getMybatis() {
        return mybatis;
    }

    public void setMybatis(String mybatis) {
        this.mybatis = mybatis;
    }
}
