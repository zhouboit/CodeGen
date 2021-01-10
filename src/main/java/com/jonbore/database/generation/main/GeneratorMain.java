package com.jonbore.database.generation.main;


import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.process.ConnectionSelect;

/**
 * @author bo.zhou
 * @date 2021/01/01 下午4:55
 */
public class GeneratorMain {
    public static void main(String[] args) {

        Configuration configuration = new Configuration(
                "jdbc:mysql://localhost:3306/activity?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
                "com.mysql.jdbc.Driver",
                "root",
                "123456",
                "activity",
                "",
                "MAN_%",
                "/Users/bo.zhou/code/CodeGeneration/src/main/java/com/jonbore/database/test",
                "com.baidu.service",
                true
        );

        ConnectionSelect.build(configuration);
    }
}
