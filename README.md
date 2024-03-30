# Political Speech
Process statistics about political speeches.
### Requirements
- Java 17, Gradle, Static File Server(nginx)

### Configuration
- You can change variables from `application.properties` under resources

### To run project
```
gradlew bootRun
```

### To run tests
```
gradlew test
```

### Endpoint
```
GET /evaluate?url1=https://example.com/valid-speeches-1.csv
Host: localhost:8080

- Yuu can use nginx easily for host static files
```
### Swagger and ApiDoc addresses:
```
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```
- Example Swagger Request:
  ![img.png](img.png)
- Example Postman Request:
- ![img_1.png](img_1.png)
- 
### Restrictions
- Url format should be `/evaluate?url1=...&url2=...&urln=...` Other query-params will be ignored.
- Url schema should be `http` or `https`. Other protocols (`file`, `ftp`...) give error.
- CSV file schema should be `Speaker ; Topic ; Date ; Words` And all fields are required (not-null)
- Date format should be `yyyy-MM-dd`. Other formats give parser error.
- `Words` should be greater than `0`