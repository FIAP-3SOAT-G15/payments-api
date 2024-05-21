# Microsserviço de pagamentos

Administração de pagamentos.

Consulte a documentação completa no [repositório principal do grupo](https://github.com/FIAP-3SOAT-G15/tech-challenge).

### Análise estática

Projeto no SonarCloud: [https://sonarcloud.io/project/overview?id=FIAP-3SOAT-G15_payments-api](https://sonarcloud.io/project/overview?id=FIAP-3SOAT-G15_payments-api)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FIAP-3SOAT-G15_payments-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FIAP-3SOAT-G15_payments-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=FIAP-3SOAT-G15_payments-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=FIAP-3SOAT-G15_payments-api)

### Executar localmente

```bash
docker compose up
```

### Mappers

```
mvn clean compile
```

### Testes

```
mvn clean verify
```

Testes de integração:

```
mvn clean verify -DskipITs=false
```

### ktlint

```
mvn antrun:run@ktlint-format
```
