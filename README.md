# Documentação da API de Produtos

Esta documentação detalha como utilizar o endpoint de consulta de produtos, que oferece recursos avançados de filtragem, ordenação e paginação.

## Endpoint de Consulta

- **Método:** `GET`
- **URL:** `/produtos`
- **Descrição:** Retorna uma lista paginada de produtos com base nos parâmetros de consulta fornecidos.

---

## Parâmetros de Consulta (Query Params)

Você pode combinar quantos parâmetros quiser para criar consultas complexas.

### 1. Paginação

Controle qual página e quantos itens você deseja receber.

| Parâmetro | Descrição                                           | Exemplo                                        |
|:----------|:----------------------------------------------------|:-----------------------------------------------|
| `page`    | O número da página que você deseja (começa em `0`). | `?page=1` (para a segunda página)              |
| `size`    | A quantidade de itens por página.                   | `?size=5` (para receber 5 produtos por página) |

### 2. Ordenação (Sorting)

Ordene os resultados com base em qualquer campo da entidade `Produto`.

| Parâmetro | Descrição                                                                     | Exemplo                                                                |
|:----------|:------------------------------------------------------------------------------|:-----------------------------------------------------------------------|
| `sort`    | O campo pelo qual ordenar, seguido por vírgula e a direção (`asc` ou `desc`). | `?sort=preco,desc` (ordena por preço, do mais caro para o mais barato) |
|           |                                                                               | `?sort=nome,asc` (ordena por nome, em ordem alfabética)                |

### 3. Filtros de Texto

Filtros para campos de texto como `nome` e `categoria`. A busca **ignora acentos e maiúsculas/minúsculas**.

- **Sintaxe:** `?nome=caderno`
- **Exemplo:** `http://localhost:8080/produtos?categoria=Informática`
    - *Retorna todos os produtos cuja categoria contenha "Informática".*

### 4. Filtros Numéricos e de Data

Para campos como `preco`, `estoque` e `dataCadastro`, você pode usar operadores especiais.

- **Sintaxe:** `?campo=operador:valor`
- **Formato da Data:** `YYYY-MM-DD`

| Operador | Significado                              | Exemplo de Uso                                         |
|:---------|:-----------------------------------------|:-------------------------------------------------------|
| `eq`     | Igual a (padrão)                         | `?preco=eq:350.00` ou `?preco=350.00`                  |
| `ne`     | Diferente de                             | `?categoria=ne:Móveis`                                 |
| `gt`     | Maior que (Greater Than)                 | `?estoque=gt:100` (estoque > 100)                      |
| `gte`    | Maior ou igual a (Greater Than or Equal) | `?preco=gte:500` (preço >= 500)                        |
| `lt`     | Menor que (Less Than)                    | `?estoque=lt:30` (estoque < 30)                        |
| `lte`    | Menor ou igual a (Less Than or Equal)    | `?dataCadastro=lte:2025-03-31` (cadastrados até 31/03) |

### 5. Filtros com Lógica (E / OU)

#### Lógica "E" (AND) - O Padrão

Para aplicar múltiplos filtros onde **TODAS** as condições devem ser verdadeiras, simplesmente separe os parâmetros com o caractere `&`.

- **Sintaxe:** `?filtro1=valor1&filtro2=valor2`
- **Exemplo:** `http://localhost:8080/produtos?categoria=Informática&estoque=lte:30`
    - *Busca produtos que são da categoria "Informática" **E** possuem estoque menor ou igual a 30.*

#### Lógica "OU" (OR)

Para buscar um termo em múltiplos campos ao mesmo tempo, separe os nomes dos campos com um `!`.

- **Sintaxe:** `?campo1!campo2=valor`
- **Exemplo:** `http://localhost:8080/produtos?nome!categoria=Mesa`
    - *Retorna produtos onde o `nome` **OU** a `categoria` contenham "Mesa".*

---

## Como Interpretar o Retorno (JSON)

O retorno da API é um objeto de página (`Page`) do Spring Data, que contém não só os dados, mas também informações sobre a paginação.

```json
{
  "content": [ /* lista de produtos */ ],
  "pageable": { /* objeto de paginação */ },
  "last": false,
  "totalPages": 2,
  "totalElements": 8,
  "first": true,
  "size": 4,
  "number": 0,
  "numberOfElements": 4,
  "sort": { /* objeto de ordenação */ },
  "empty": false
}
```

### Principais Campos:

| Campo              | Explicação                                                                                          |
|:-------------------|:----------------------------------------------------------------------------------------------------|
| `content`          | **A lista de resultados (produtos) para a página atual.** Este é o dado principal que você procura. |
| `totalElements`    | **O número total de produtos que correspondem ao seu filtro em TODAS as páginas.**                  |
| `totalPages`       | **O número total de páginas disponíveis** com base no `totalElements` e no `size` da página.        |
| `number`           | **O número da página atual** (lembrando que começa em 0).                                           |
| `size`             | **O tamanho máximo desta página** (o valor que você passou no parâmetro `size`).                    |
| `numberOfElements` | **Quantos produtos estão de fato na lista `content`** desta página.                                 |
| `first`            | É `true` se esta for a primeira página.                                                             |
| `last`             | É `true` se esta for a última página.                                                               |
| `empty`            | É `true` se a lista `content` desta página estiver vazia.                                           |

### Campos de `pageable` e `sort`

Esses objetos descrevem **a requisição que você fez** e o estado dela.

| Campo                           | Explicação                                                                          |
|:--------------------------------|:------------------------------------------------------------------------------------|
| `pageable`                      | Um objeto que espelha os parâmetros de paginação que você enviou.                   |
| `pageable.pageNumber`           | O número da página que você pediu.                                                  |
| `pageable.pageSize`             | O tamanho da página que você pediu.                                                 |
| `pageable.sort`                 | Um objeto descrevendo a ordenação que você pediu.                                   |
| **`sort` (no nível principal)** | A mesma coisa que `pageable.sort`, descrevendo a ordenação aplicada na resposta.    |
| `sort.sorted`                   | **É `true` se você passou o parâmetro `sort` na URL.**                              |
| `sort.unsorted`                 | **É `true` se você NÃO passou o parâmetro `sort` na URL.**                          |
| `sort.empty`                    | É basicamente o mesmo que `unsorted`. É `true` quando nenhuma ordenação foi pedida. |