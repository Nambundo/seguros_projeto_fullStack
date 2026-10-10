# InsuranceHub — Full Stack (Java + Spring Boot + React + SQL Server)

Sistema de gestão de **clientes, apólices e sinistros** de seguros.

**Stack:** Java 17, Spring Boot 3.3, Spring Data JPA/Hibernate, Bean Validation, SQL Server, React 18, Axios, Vite.

```
React → Axios → REST/JSON → Controller → Service → Repository → JPA/Hibernate → SQL Server
```

---

## Como rodar (passo a passo)

### Pré-requisitos
| Ferramenta | Versão | Como conferir |
|---|---|---|
| JDK | **17 ou superior** | `java -version` |
| Maven | 3.9+ (o IntelliJ já traz um embutido) | `mvn -v` |
| Node.js | **18 ou superior** | `node -v` |
| Docker Desktop | qualquer recente (só se for usar o SQL Server em container) | `docker -v` |

### 1) Banco de dados (SQL Server)
Na pasta `backend`:
```bash
docker compose up -d
```
Isso sobe o SQL Server (porta 1433) **e cria o banco `InsuranceHub` sozinho** (serviço `db-init`).
Espere uns 30 s na primeira vez. Para conferir: `docker compose logs db-init` deve mostrar `Banco InsuranceHub pronto.`

> Sem Docker? Use seu SQL Server local, rode `CREATE DATABASE InsuranceHub;` e ajuste as variáveis
> `DB_URL`, `DB_USER`, `DB_PASSWORD` (veja `application.properties`).

### 2) Backend (terminal 1)
```bash
cd backend
mvn spring-boot:run
```
Pronto quando aparecer `Started InsurancehubApplication`. As tabelas são criadas automaticamente.
Teste: abra http://localhost:8080/api/clientes → deve mostrar `[]`.

### 3) Frontend (terminal 2)
```bash
cd frontend
npm install
npm run dev
```
Abra **http://localhost:5173**.

### 4) Roteiro rápido para ver tudo funcionando
1. Aba **Clientes** → cadastre um cliente (CPF válido, ex.: `529.982.247-25`).
2. Aba **Apólices** → crie uma apólice para esse cliente.
3. Aba **Sinistros** → abra um sinistro nessa apólice.
4. Aba **Painel** → veja os totais.

---

## Problemas comuns

| Sintoma | Causa provável / solução |
|---|---|
| Backend: `Cannot open database "InsuranceHub"` | O banco não foi criado. `docker compose logs db-init`; se falhou, crie manualmente: `CREATE DATABASE InsuranceHub;` (DBeaver, Azure Data Studio, SSMS). |
| Backend: `Login failed for user 'sa'` | Senha diferente. Ajuste `DB_PASSWORD` ou use `YourStrong!Passw0rd` (a do compose). |
| Backend: `Connection refused` na 1433 | Container ainda subindo ou parado: `docker compose ps`. |
| `error: release version 17 not supported` | JDK antigo. Instale JDK 17+ e confira `JAVA_HOME`. |
| Front mostra "Não foi possível conectar ao backend" | Backend fora do ar, ou porta diferente de 8080 (defina `VITE_API_URL` em `frontend/.env`). |
| Erro de CORS no console | O front precisa rodar em `localhost:5173` (a porta é fixa). Para outra origem: variável `CORS_ORIGINS`. |
| Mac com chip Apple (M1/M2/…) | A imagem do SQL Server é x86: ative a emulação/Rosetta no Docker Desktop. |
| Já rodou a versão antiga e há dados nela | O formato de status mudou (agora é enum). Apague o volume: `docker compose down -v` e suba de novo. |

---

## API

| Recurso | Endpoints |
|---|---|
| Clientes | `GET/POST /api/clientes` · `GET/PUT/DELETE /api/clientes/{id}` |
| Apólices | `GET/POST /api/apolices` · `GET/PUT/DELETE /api/apolices/{id}` · filtro `?clienteId=` |
| Sinistros | `GET/POST /api/sinistros` · `GET/PUT/DELETE /api/sinistros/{id}` · filtro `?apoliceId=` |
| Painel | `GET /api/dashboard` |

Exemplos prontos para colar no IntelliJ/VS Code: `backend/requests.http`.

**Formato de erro (todas as falhas seguem o mesmo padrão):**
```json
{ "status": 400, "erro": "Dados inválidos", "campos": { "cpf": "CPF inválido" } }
```
| HTTP | Quando |
|---|---|
| 400 | Campo inválido / JSON malformado (data fora de AAAA-MM-DD, status inexistente) |
| 404 | Registro não existe |
| 409 | Duplicidade (CPF, e-mail, nº da apólice) ou registro em uso (excluir cliente com apólices, apólice com sinistros) |
| 422 | Regra de negócio violada (abaixo) |

### Regras de negócio
- CPF validado (dígitos verificadores) e salvo só com números; e-mail salvo em minúsculas.
- Apólice: `dataFim` > `dataInicio`; número único (salvo em maiúsculas); prêmio e cobertura > 0.
- Sinistro: só pode ser aberto em apólice **ATIVA**; a data deve estar dentro da vigência e não pode ser futura; o valor solicitado não pode passar da cobertura.

---


## Próximos passos sugeridos
Autenticação (Spring Security + JWT), paginação (`Pageable`), migrações Flyway, testes de integração com Testcontainers, Dockerfile para backend/frontend, e documentação OpenAPI (springdoc).
