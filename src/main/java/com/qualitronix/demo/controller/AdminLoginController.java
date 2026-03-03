package com.qualitronix.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin")
public class AdminLoginController {

    private final String ADMIN_USER = "ferramentaria";
    private final String ADMIN_PASS = "123";
    private boolean loggedIn = false;

    // Recebe JSON no corpo da requisição
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        if (request.getUsuario().equals(ADMIN_USER) && request.getSenha().equals(ADMIN_PASS)) {
            loggedIn = true;
            return "Login OK";
        } else {
            return "Usuário ou senha incorretos";
        }
    }

    @GetMapping("/check")
    public boolean checkLogged() {
        return loggedIn;
    }

    @PostMapping("/logout")
    public String logout() {
        loggedIn = false;
        return "Logout OK";
    }

    // Classe interna pra JSON
    public static class LoginRequest {
        private String usuario;
        private String senha;

        // Getters e setters
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }
}