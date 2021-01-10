package com.jonbore.database.generation.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author bo.zhou
 * @date 2021/1/10 下午4:31
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "words")
public class Words {
    @XmlElement(name = "type")
    private List<WordsType> wordsTypeList;

    public List<WordsType> getWordsTypeList() {
        return wordsTypeList;
    }

    public void setWordsTypeList(List<WordsType> wordsTypeList) {
        this.wordsTypeList = wordsTypeList;
    }
}
