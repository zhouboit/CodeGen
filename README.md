# CodeGen
根据MySQL数据库结构生成工程CURD基础代码
## 使用
    1.com.jonbore.database.generation.main.GeneratorMain 运行入库
    2.运行参数
    Configuration configuration = new Configuration(
                    "jdbc:mysql://localhost:3306/activity?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
                    "com.mysql.jdbc.Driver",
                    "root",
                    "123456",
                    "activity",
                    "",
                    "MAN_%",
                    "/Users/bo.zhou/code/CodeGeneration/src/main/java/com/jonbore/database/test",
                    "com.XXX.service",
                    true
            );
    String url //数据库连接URL
    String driver //数据库驱动
    String username //数据库连接用户名
    String password //数据库连接用户密码
    String database //要生产代码表结构所在的数据库名称
    String table //需要生成的表名，为空则生成所有的 table1,table2,...
    String tableNameExp //数据库表的模糊匹配，作为关键字 like的参数
    String home //生成目标代码存储位置
    String parentPackage //生成基础代码工程包路径
    Boolean cleanHome //每次运行生成工具是否清楚上次的生成文件
## 生成代码工程POM依赖
    多模块工程
### 父级工程POM
    <dependencyManagement>
            <dependencies>
                <!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-alibaba-dependencies -->
                <dependency>
                    <groupId>com.alibaba.cloud</groupId>
                    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                    <version>2.2.3.RELEASE</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
                <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies -->
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>Hoxton.SR3</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
                <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies -->
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-dependencies</artifactId>
                    <version>2.2.5.RELEASE</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
                <dependency>
                    <groupId>io.springfox</groupId>
                    <artifactId>springfox-swagger2</artifactId>
                    <version>2.8.0</version>
                </dependency>
                <dependency>
                    <groupId>io.springfox</groupId>
                    <artifactId>springfox-swagger-ui</artifactId>
                    <version>2.8.0</version>
                </dependency>
                <dependency>
                    <groupId>io.springfox</groupId>
                    <artifactId>springfox-bean-validators</artifactId>
                    <version>2.8.0</version>
                </dependency>
                <dependency>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok</artifactId>
                    <version>1.18.8</version>
                </dependency>
                <dependency>
                    <groupId>com.alibaba</groupId>
                    <artifactId>druid-spring-boot-starter</artifactId>
                    <version>1.1.10</version>
                </dependency>
                <dependency>
                    <groupId>org.mybatis.spring.boot</groupId>
                    <artifactId>mybatis-spring-boot-starter</artifactId>
                    <version>2.0.1</version>
                </dependency>
                <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
                <dependency>
                    <groupId>com.alibaba</groupId>
                    <artifactId>fastjson</artifactId>
                    <version>1.2.75</version>
                </dependency>
            </dependencies>
        </dependencyManagement>
### 子模块POM依赖
    <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <exclusions>
                    <exclusion>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-logging</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-log4j2</artifactId>
            </dependency>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jdbc</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-jdbc</artifactId>
            </dependency>
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <exclusions>
                    <exclusion>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-logging</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-bean-validators</artifactId>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
            </dependency>
        </dependencies>

