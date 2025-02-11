package com.example.taskmanagmentsystemproject.models.token;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "tokens")
public class JwtToken
{
    //region Fields
    @Id
    private String id; // идентификатор токена в базе данных в таблице "tokens"
    private String token; // токен
    private Integer userId;  // связь с пользователем
    private Date expirationDate; // дата истечения срока действия токена
    private String secretKey; // секретный ключ (добавлено для хранения секретного ключа)
    //endRegion

    //region Constructors
    public JwtToken(String token, Integer userId, Date expirationDate, String secretKey) {
        this.token = token;
        this.userId = userId;
        this.expirationDate = expirationDate;
        this.secretKey = secretKey;
    }
    public JwtToken()
    {
        // default constructor
    }
    //endRegion

    //region Getters
    public String getId()
    {
        return id;
    }

    public String getToken()
    {
        return token;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public Date getExpirationDate()
    {
        return expirationDate;
    }

    public String getSecretKey()
    {
        return secretKey;
    }
    //endRegion

    //region Setters
    public void setId(String id)
    {
        this.id = id;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public void setExpirationDate(Date expirationDate)
    {
        this.expirationDate = expirationDate;
    }

    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }
    //endRegion
}

