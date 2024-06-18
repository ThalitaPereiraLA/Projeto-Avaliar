# Alterações Feitas:
- Adicionei o Maven um gerenciador de dependências para facilitar a distribuição da aplicação, ou seja, agora qualquer um pode simplesmente clonar o repositório e instalar as dependências e utilizar.

- Criei um docker-compose para a criação do banco MariaDB, para utilizá-lo é só digitar:

```bash
$ docker-compose up
```

- Atualizei Dependências

- Agora o Jasper pode gerar relatórios dinâmicos basta que forneça um arquivo .jrfxml e ele irá buildar um novo .jasper

- Além disso adicionei uma função para criar as tabelas no banco assim que conectar algo que estava faltando antes.

<img src="https://i.imgur.com/o6y18Vk.png" alt="Aplicação">