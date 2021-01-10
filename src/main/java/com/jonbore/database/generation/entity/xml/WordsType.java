package com.jonbore.database.generation.entity.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author bo.zhou
 * @date 2021/1/10 下午4:32
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "type")
public class WordsType {
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "key-word")
    private List<KeyWord> keyWordList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KeyWord> getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(List<KeyWord> keyWordList) {
        this.keyWordList = keyWordList;
    }
}
