package org.remind.melembraai.domain.sender.templates;

import org.remind.melembraai.domain.email.model.RememberEmail;

public final class EmailsTemplates {

    public static String emailTemplate(RememberEmail email) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f4;
                    margin: 0;
                    padding: 0;
                }
                .email-container {
                    background-color: #ffffff;
                    margin: 20px auto;
                    padding: 20px;
                    max-width: 600px;
                    border-radius: 8px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                .email-header {
                    background-color: #007BFF;
                    color: #ffffff;
                    padding: 10px;
                    text-align: center;
                    border-top-left-radius: 8px;
                    border-top-right-radius: 8px;
                }
                .email-body {
                    padding: 20px;
                    line-height: 1.6;
                    color: #333333;
                }
                .email-footer {
                    text-align: center;
                    padding: 20px;
                    font-size: 12px;
                    color: #888888;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="email-header">
                    <h1>%s</h1>
                </div>
                <div class="email-body">
                    <p>%s</p>
                </div>
                <div class="email-footer">
                    <p>Enviado automaticamente por,<br>Me Lembra Ai</p>
                    <p>Para cancelar o seu cadastro, por gentileza acesse o nosso site.</p>
                    <p>&copy; 2024 Me Lembra Ai. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(email.getTitle(), email.getContent());
    }


//    public static String WelcomeEmail(String username, String activationCode) {
//        String template = """
//                Olá %s,
//
//                Seja bem-vindo ao Melembraai! Fomos criados para te lembrar algo que deseja te mandando um email previamente configurado por você.
//
//                Por gentileza, informe o código abaixo para ativar sua conta.
//
//                Código de ativação: %s
//
//                PS: O código expira em 24 horas.
//
//                Atenciosamente,
//                Equipe Me lembra ai""";
//        return String.format(template, username, activationCode);
//    }

    public static String WelcomeEmail(String username, String activationCode) {
        String template = """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Bem-vindo ao Me lembra ai</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f4f4f4;
                    color: #333333;
                }
                .container {
                    max-width: 600px;
                    margin: 20px auto;
                    background-color: #F0F3BD;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                }
                .header {
                    text-align: center;
                    background-color: #333;
                    padding: 10px;
                    border-radius: 10px 10px 0 0;
                    color: white;
                }
                .header h1 {
                    margin: 0;
                    font-size: 24px;
                }
                .content {
                    padding: 20px;
                }
                .content h2 {
                    font-size: 20px;
                    color: #333;
                }
                .content p {
                    font-size: 16px;
                    line-height: 1.5;
                }
                .activation-code {
                    font-size: 18px;
                    font-weight: bold;
                    color: #333;
                    background-color: #f9f9f9;
                    padding: 10px;
                    border: 1px solid #ddd;
                    margin: 20px 0;
                    text-align: center;
                }
                .footer {
                    text-align: center;
                    font-size: 14px;
                    color: #777777;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Bem-vindo ao Me Lembra Ai!</h1>
                </div>
                <div class="content">
                    <h2>Olá, %s!</h2>
                    <p>Seja bem-vindo ao <strong>Me Lembra Ai</strong>! Fomos criados para te ajudar a lembrar de algo importante enviando um e-mail previamente configurado por você.</p>
                    <p>Por gentileza, informe o código abaixo para ativar sua conta.</p>
                    <div class="activation-code">%s</div>
                    <p><em>PS: O código expira em 24 horas.</em></p>
                </div>
                <div class="footer">
                    <p>Atenciosamente,<br>Equipe Melembraai</p>
                </div>
            </div>
        </body>
        </html>
        """;
        return String.format(template, username, activationCode);
    }


    public static String ResendVerificationEmail(String username, String activationCode) {
        String template = """
                Olá %s,

                Aqui está um novo código de ativação para sua conta.
                
                Código de ativação: %s

                PS: O código expira em 24 horas.
                
                Atenciosamente,
                Equipe Me lembra ai""";
        return String.format(template, username, username, activationCode);
    }
}
