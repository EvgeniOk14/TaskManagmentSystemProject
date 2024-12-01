package com.example.taskmanagmentsystemproject.models.token;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tokens")
public class JwtToken
{
    @Id
    private String id; // идентификатор токена в базе данных в таблице "tokens"
    private String token; // токен
    private Integer userId;  // связь с пользователем
    private Date expirationDate; // дата истечения срока действия токена
    private String secretKey; // секретный ключ (добавлено для хранения секретного ключа)
}
