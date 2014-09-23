learn basics of Java Web Development
===

add this line in your tomcat's `context.xml`

```
<context>
...
...

     <Resource name="jdbc/studydb" auth="Container" type="javax.sql.DataSource"
                    maxActive="100" maxIdle="30" maxWait="10000"
                                   username="study" password="study" driverClassName="com.mysql.jdbc.Driver"
                                                  url="jdbc:mysql://localhost:3306/studydb"/>
...
...
</context>
```

