package com.jonbore.database.generation.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author bo.zhou
 * @date 2021/1/10 下午4:35
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "key-word")
public class KeyWord {
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "des")
    private String describe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
