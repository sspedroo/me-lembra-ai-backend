## Me Lembra Ai
Esse projeto é uma API que contém funcionalidades para cadastrar usuários, autenticar eles e enviar emails para eles automaticamente.
Contém autenticação JWT com token e refresh token e o banco de dados é o H2 em memória para fins somente de demonstração.
Contém Swagger como documentação da API.

O usuário tem o seguinte fluxo
- Criar sua conta
- Validar a sua conta atráves de um token recebido no email cadastrado
- Realizar o login
- Criar o email/lembrete que deseja receber

Caso queira testar a aplicação, basta clonar e configurar qual será o email responsável pelo envio de emails e executar o projeto. <br><br>
O endereço do swagger será: localhost:10000/swagger-ui.html <br> 
O endereço do Banco de Dados será: localhost:10000/h2-console
