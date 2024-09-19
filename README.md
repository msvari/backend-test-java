## API de gestão de estacionamentos - Spring Boot
API REST para gerenciar um estacionamento de carros e motos.

## Tecnologias Utilizadas
  - Java 21
  - Spring Boot 3.3.3
       - Spring Data JPA
       - Spring Validation
       - Spring Web
       - Spring Security
  - JWT
  - H2 Database
  - Lombok
  - IText para geração de relatórios.
  - OpenAPI
  - Flyway
 
## Endpoints da API
## Autenticação
 - # Registrar Usuário
    `POST /auth/register`

         `curl --location 'http://localhost:8080/auth/register' \
        --header 'Content-Type: application/json' \
        --header 'Cookie: JSESSIONID=C893D8C31AB84FC86B3A000503B8C6F5' \
        --data '{
            "login": "park",
            "password": "park",
            "role": "ADMIN"
        }'`  

- # Login
   `POST /auth/login`

      `curl --location 'http://localhost:8080/auth/login' \
      --header 'Content-Type: application/json' \
      --header 'Cookie: JSESSIONID=B263D8C31DE84FC86B3A000503B8C9A6' \
      --data '{
          "login": "park",
          "password": "park"
      }'`

- # Swagger com a documentação dos endpoints
  [Swagger da API REST](http://localhost:8080/swagger-ui/index.html)

## Configuração para Execução
## Pré-requisitos
- Java 21
- Gradle

## Como Executar
- Clone o repositório:
  `git clone https://github.com/adamrenan/park.git`
  `cd park`

- Build o projeto:
  `./gradlew clean build`

- Rode a aplicação:
  `./gradlew bootRun`

## Como executar 2:
- Executar diretamente pela IDE de preferência (Intellij, Eclipse, etc.)
- Clicar com botão direito na classe ParkApplication.java e selecionar 'Run ParkApplication.main()'

## Acesse o H2 Console no browser:

- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:test
- Usuário: sa
- Senha: 123456

## Autenticação JWT
  - Após registrar um usuário, use o endpoint /auth/login para obter o token JWT. Inclua esse token no header Authorization para enviar request para os endpoints da API:
    `Authorization: Bearer <token>`


## Questionário de competências
## 1 - GraphQL (Implementação BFF - Backend For Frontend)

  Pergunta 1: Explique o que é o GraphQL e como ele se diferencia de uma API REST tradicional.

    GraphQL é uma linguagem de consulta de API que define as especificações de como uma aplicação cliente deve solicitar dados de um servidor remoto. Você pode usar GraphQL em suas chamadas de API sem depender da aplicação do lado do servidor para definir a solicitação. Ambos permitem a troca de dados entre diferentes serviços baseado em HTTP e com modelo cliente servidor. A principal 
    diferença entre ambos é que a API REST exige que as solicitações do cliente sigam uma estratura fixa para receber os recurso, enquanto
    que o GraphQL a estrtura de dados enviada pelo cliente e flexível. GraphQL opera em um único endpoint usando HTTP
  
  Pergunta 2: Descreva como você implementaria o uso do GraphQL como BFF (Backend for Frontend) neste projeto de gerenciamento de estacionamento. Forneça exemplos práticos.

    Implementaria a interface GraphQLQueryResolver para implementação das consultas de cada endpoint implementado atualmente com a API REST, definindo os dados de forma flexível com os schemas.
  
  Pergunta 3: Quais são os benefícios de utilizar GraphQL em relação à flexibilidade das consultas? Cite possíveis desafios ao utilizá-lo.

    Ao contrário da API REST, com o GraphQL você não precisa implementar uma quantidade relativamente maior de código para retornar os mesmos dados que um correspondente API REST, pois os dados são  retornados por demanda (ex: podemos consultar apenas o endereço de uma pessoa sem ter que receber todos os dados de pessoa na requisição).

## 2 - Banco de Dados (Nível Básico)

  Pergunta 1: Explique os principais conceitos de um banco de dados relacional, como tabelas, chaves primárias e estrangeiras.

    Tabela : estrutura de dados que organiza os dados em formato de colunas e registros/linhas. Cada coluna da tabela representa 
              uma propriedade da entidade aqui representada. As linhas são as instâncias ou conjunto de dados armazenados. Além
              disso, chave primária, chaves estrangeiras e restrições define uma tabela.

    Chave-primária : é definida em uma coluna da tabela, identificador único e não nulo, dos registros de uma tabela

    Chave estrangeira : é definida em uma ou mais colunas de uma tabela, identificador único que referencia o valor de uma chave primária correspondente em outra tabela. Ao contrário da chave primária, permite valores null

  Pergunta 2: No contexto de uma aplicação de gerenciamento de estacionamento, como você organizaria a modelagem de dados para suportar as funcionalidades de controle de entrada e saída de veículos?

    Implementando um entidade central Park de registros de entrada e saída de veículos, ligada aos repectivos estacionamentos, salvado a data e hora de entrada e saída.

  Pergunta 3: Quais seriam as vantagens e desvantagens de utilizar um banco de dados NoSQL neste projeto?

    Vantagens do NoSQL : 

      Ideal para grandes volumes de dados, com facilidade de distribuição dos dados entre múltiplos servidores (escalabilidade horizontal).
      Alta performance para leitura de gravação em grandes volumes de dados.
      Flexibilidade da estrutura dos dados, salvos sem schemas fixos, facilitando possíveis migrações e mudanças de requisitos.
      Alta disponibilidade e torelância a falhas.
      Diferentes modelos de dados (documentos, chave-valor, colunas, grafos).

    Desvantagens do NoSQL : 

      Inconsistência temporárias de dados, em detrimento a escalabilidade e performance.
      Consulta de dados limitadas (queries menos complexas).
      Redundância de dados e deplicidades, com possíveis aumentos de consumo de espaço.   


## 3 - Agilidade (Nível Básico)

  Pergunta 1: Explique o conceito de metodologias ágeis e como elas impactam o desenvolvimento de software.

    São abordagens de gerenciamento de projetos priorizando flexibilização, entregas rápidas e com melhoria
    contínua dos processos. As principais características são : entrega contínua e incremental, iterações curtas, feedback rápido 
    e frequente, times auto-organizados e multifuncionais, colaboração e comunicação constante, adaptabilidade e resposta a mudanças

  Pergunta 2: No desenvolvimento deste projeto, como você aplicaria princípios ágeis para garantir entregas contínuas e com qualidade ?

    Seria interessante a escolha de Kanban ou Scrum, com timebox previamente definidos das tarefas pré definidas e organizadas pelo
    PO, priorizadas no backlog, sequindo os fluxos de upstream e downstream, sempre com reuniões diárias de acompanhamento do andamento
    das tarefas, com constantes reuniões de revisões e melhorias dos processos.

  Pergunta 3: Qual a importância da comunicação entre as equipes em um ambiente ágil? Dê exemplos de boas práticas.

    Quanto mais clara, concisa e rápida a comunicação, mais rápidos e assertivos serão os feedback para realização e implementação
    de mudanças e dos requisitos, aumentando a velocidade das entregas de valores, diminuindo dos custos e tornando a entrega do produto
    mais assertiva.

## 4 - DevOps (Nível Básico)

  Pergunta 1: O que é DevOps e qual a sua importância para o ciclo de vida de uma aplicação ?

    DevOps é o estreitamento e a agregação entre o desenvolvimento e o operacional necessário a estrutura e suporte as entregas
    implementadas pelo desenvolvimento, visa o multidiciplinamento entre as áeras.

  Pergunta 2: Descreva como você integraria práticas de DevOps no desenvolvimento desta aplicação de estacionamento. Inclua exemplos de CI/CD.

    Algumas boas práticas seriam o monitoramento contínuo das aplicações, com New Relic ou Grafana por exemplo, com automações de CI/CD com
    Docker, Jenkins, Git, Kubernetes.

  Pergunta 3: Cite as ferramentas que você usaria para automatizar o processo de deploy e monitoramento da aplicação.

      Poderiamos usar o Jenkins para CI/CD, integrado ao repositório de código e fazendo entregas na nuvem, com uso de 
    Kubernetes ou AWS por exemplo.