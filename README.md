# Kafka Producer e Consumer com Docker Compose

Este projeto foi desenvolvido como parte de um bootcamp da empresa **Deal** em parceria com a **Dio**, e implementa um
sistema simples de **producer** e **consumer** utilizando o Apache Kafka para a comunicação entre serviços.

## Descrição do Projeto

Este repositório contém três serviços principais:

1. **Producer**: Um serviço que envia mensagens para um tópico no Kafka.
2. **Consumer**: Um serviço que consome as mensagens enviadas para o tópico pelo producer.
3. **Kafka Broker**: O broker de mensagens que gerencia os tópicos e as mensagens enviadas entre producer e consumer.

O ambiente é totalmente containerizado usando **Docker Compose** para facilitar a configuração do Kafka e dos serviços
de producer e consumer.

## Tecnologias Utilizadas

- **Java 21** (para os serviços de producer e consumer)
- **Spring Boot** (para simplificar a criação dos serviços)
- **Apache Kafka** (como broker de mensagens)
- **Docker** e **Docker Compose** (para orquestração dos containers)
- **Gradle** (para gerenciamento de dependências)

## Estrutura do Projeto

- `sb-kafka-producer/`: Contém o código-fonte do serviço de Producer, responsável por enviar mensagens ao Kafka.
- `sb-kafka-consumer/`: Contém o código-fonte do serviço de Consumer, responsável por consumir as mensagens do Kafka.
- `docker-compose.yml`: Arquivo de configuração do Docker Compose para subir o Kafka, Producer e Consumer.
- `README.md`: Documento explicativo sobre o projeto.

## Pré-requisitos

Certifique-se de ter instalado:

- **Docker**: [Instalar Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Instalar Docker Compose](https://docs.docker.com/compose/install/)

## Como Rodar o Projeto

Siga as etapas abaixo para subir o ambiente e rodar os serviços do Kafka, Producer e Consumer:

### Passo 1: Clone o repositório e acesse a pasta

```bash
git clone https://github.com/thoggs/bc-sb-kafka.git && cd bc-sb-kafka
```

### Passo 2: Rodar os containers com Docker Compose

Execute o comando abaixo para subir o ambiente Kafka, Producer e Consumer:

```bash
docker-compose up -d
```

Isso irá subir os seguintes containers:

- **Kafka Broker** (`Portas 9092 e 9093`)
    - `9092`: Porta usada para tráfego de mensagens entre producer e consumer.
    - `9093`: Porta usada pelo controller listener do Kafka.

- **Producer** (`Porta 9090`)
    - O serviço producer estará ouvindo nessa porta.

- **Consumer** (`Porta 8080`)
    - O serviço consumer estará rodando nessa porta.

### Passo 3: Enviar Mensagens

O serviço **Producer** envia mensagens para o Kafka por meio de uma requisição POST para o endpoint `/api/v1/payment`.

#### Exemplo de Requisição:

- **Endpoint**: `/api/v1/payment`
- **Método**: `POST`
- **Corpo da Requisição (JSON)**:

```json
{
  "number": "2",
  "value": "200",
  "description": "Debito de compra"
}
```

### Passo 4: Consumir Mensagens e Ver Logs

O serviço **Consumer** escutará automaticamente o tópico no Kafka e processará as mensagens enviadas pelo Producer.

Para visualizar os logs do **Consumer** e verificar as mensagens que estão sendo consumidas, execute o seguinte comando:

```bash
docker logs -f sb-kafka-consumer-app --since 1m
```

O `-f` significa "follow", ou seja, os logs serão atualizados em tempo real, permitindo que você acompanhe a execução do
**Consumer** enquanto ele processa as mensagens, e o `--since 1m` exibe apenas os logs dos últimos 60 segundos.

### Passo 5: Parar o Ambiente

Para parar todos os containers, basta rodar:

```bash
docker-compose down
```

## Configuração do Docker Compose

Aqui está uma visão geral do `docker-compose.yml`:

```yaml
services:
  kafka:
    image: apache/kafka:3.8.0
    container_name: kafka
    ports:
      - "9092:9092"
      - "9093:9093"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka:9093
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_LOG_DIRS: /var/lib/kafka/data
      KAFKA_METADATA_LOG_DIRS: /var/lib/kafka/meta
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
    networks:
      - bootcamp-network

  springboot-producer:
    container_name: sb-kafka-producer-app
    build:
      context: .
      dockerfile: sb-kafka-producer/Dockerfile
      args:
        JAVA_VERSION: 21
        GRADLE_VERSION: 8.8
    ports:
      - '9090:9090'
    depends_on:
      - kafka
    networks:
      - bootcamp-network

  springboot-consumer:
    container_name: sb-kafka-consumer-app
    build:
      context: .
      dockerfile: sb-kafka-consumer/Dockerfile
      args:
        JAVA_VERSION: 21
        GRADLE_VERSION: 8.8
    ports:
      - '8080:8080'
    depends_on:
      - kafka
    networks:
      - bootcamp-network

networks:
  bootcamp-network:
    driver: bridge
```

## Spring Boot `application.properties`

### Producer

```properties
# Porta do Spring Boot do Producer
server.port=${SERVER_PORT:9090}

# Nome da aplicação Spring Boot
spring.application.name=sb-kafka-producer

# Kafka Bootstrap Servers (onde o Producer enviará mensagens)
spring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS_CONFIG:kafka:9092}

# Tópico Kafka onde o Producer enviará mensagens
topics.payment.request.topic=${KAFKA_PAYMENT_REQUEST_TOPIC:pagamento.request.topic.v1}
```

### Consumer

```properties
# Porta do Spring Boot do Consumer
server.port=${SERVER_PORT:8080}

# Nome da aplicação Spring Boot
spring.application.name=sb-kafka-consumer

# Kafka Bootstrap Servers (onde o Consumer consumirá mensagens)
spring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS_CONFIG:kafka:9092}

# Tópico Kafka onde o Consumer consumirá mensagens
topics.payment.request.topic=${KAFKA_PAYMENT_REQUEST_TOPIC:pagamento.request.topic.v1}

# Grupo de Consumidores do Kafka
spring.kafka.consumer.group-id=${KAFKA_CONSUMER_GROUP_ID:pagamento-request-consumer-1}
```

## Observações

Este projeto foi desenvolvido durante um **bootcamp** na **Deal** em parceria com a **Dio** e tem como objetivo
demonstrar o funcionamento básico
de um sistema de mensageria utilizando o Kafka. Ele pode ser expandido para incluir mais funcionalidades e uma
arquitetura mais complexa conforme necessário.

## Contribuições

Sinta-se à vontade para abrir **Issues** ou enviar **Pull Requests** se quiser contribuir com melhorias no projeto.

## License

Project license [Apache-2.0](https://opensource.org/license/apache-2-0)
