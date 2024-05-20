# Microsserviço de pagamentos

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
