package com.jonbore.database.generation.entity.xml;

import com.google.common.collect.Lists;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bo.zhou
 * @date 2021/1/10 下午4:38
 */
public class KeyWordsFactory {
    public static Words loadMappings() {
        try {
            JAXBContext context = JAXBContext.newInstance(Words.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Words) unmarshaller.unmarshal(KeyWordsFactory.class.getResourceAsStream("/column-keywords.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, List<String>> getMapping() {
        Words words = loadMappings();
        Map<String, List<String>> keyWords = new HashMap<>();
        if (words != null && words.getWordsTypeList() != null && !words.getWordsTypeList().isEmpty()) {
            for (WordsType wordsType : words.getWordsTypeList()) {
                List<String> key = Lists.newArrayList();
                for (KeyWord keyWord : wordsType.getKeyWordList()) {
                    key.add(keyWord.getName());
                }
                keyWords.put(wordsType.getName(), key);
            }
        }
        return keyWords;
    }
}
