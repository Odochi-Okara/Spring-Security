package com.example.springsecurity.filters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JWTDataSource {
	private String secretKey;
	private String tokenPrefix;
	private Long expirationDate;
}