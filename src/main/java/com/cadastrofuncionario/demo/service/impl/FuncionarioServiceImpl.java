package com.cadastrofuncionario.demo.service.impl;

import org.springframework.stereotype.Service;

import com.cadastrofuncionario.demo.service.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{
      private static final String AES_KEY = "TOKEN_SECURITY_MOGLIX_AES_KEY_IN_JWT";

    @Override
    public String encrypt(String data) {
        AES aes = new AES(AES_KEY);
        return aes.encrypt(data);
    }

    @Override
    public String decrypt(String data) {
        AES aes = new AES(AES_KEY);
		return aes.decrypt(data);
    }
    
}
