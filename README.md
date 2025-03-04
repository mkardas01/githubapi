## Opis
Aplikacja Spring Boot do przeglądania repozytoriów GitHub użytkownika. Pobiera listę repozytoriów (z wyłączeniem forków) wraz z ich gałęziami.

## Wymagania

- Java 17
- Maven
- Token dostępu do GitHub API

## Konfiguracja

1. Ustaw token GitHub (wybierz jedną z opcji):

   a. Zmienna środowiskowa:
   ```bash
   export GITHUB_TOKEN=twój_token_github
   ```

   b. W pliku `application.properties`:
   ```properties
   github.token=twój_token_github
   ```

2. Domyślny port aplikacji to 8080 (można zmienić w `application.properties`)

## Uruchomienie
```bash
mvn spring-boot:run
```

## API

### Pobierz repozytoria użytkownika

```
GET /api/users/{username}/repositories
```

#### Przykładowe zapytania

```bash
# Używając curl
curl -X GET http://localhost:8080/api/users/octocat/repositories

# Używając http
http GET http://localhost:8080/api/users/octocat/repositories

# Używając Postman
GET http://localhost:8080/api/users/octocat/repositories
```

#### Przykładowa odpowiedź (200 OK)

```json
[
    {
        "name": "example-repo",
        "owner": {
            "login": "octocat"
        },
        "branches": [
            {
                "name": "main",
                "commit": {
                    "sha": "abc123def456"
                }
            }
        ],
        "fork": false
    }
]
```

#### Błędy

- Użytkownik nie istnieje (404):
```json
{
    "status": 404,
    "message": "User not found"
}
```

- Przekroczony limit API (403):
```json
{
    "status": 403,
    "message": "API rate limit exceeded..."
}
```


## Struktura projektu

```
src/
├── main/
│   ├── java/
│   │   └── com/example/githubapi/
│   │       ├── config/
│   │       │   └── WebClientConfig.java
│   │       ├── controller/
│   │       │   └── GithubController.java
│   │       ├── exception/
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── model/
│   │       │   ├── Branch.java
│   │       │   ├── Repository.java
│   │       │   └── ErrorResponse.java
│   │       ├── service/
│   │       │   └── GithubService.java
│   │       └── GithubapiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/example/githubapi/
            └── GithubapiApplicationTests.java
```

## Testy

Aplikacja zawiera testy integracyjne sprawdzające:
- Pobieranie repozytoriów dla istniejącego użytkownika (używając "octocat")
- Obsługę nieistniejącego użytkownika
- Poprawność struktury odpowiedzi


## Funkcjonalności

- Pobieranie listy repozytoriów użytkownika (bez forków)
- Dla każdego repozytorium pobierane są informacje o gałęziach
- Reaktywne przetwarzanie danych
- Obsługa błędów (404, 403, 500)
- Automatyczna konfiguracja WebClient z tokenem GitHub
- Logowanie błędów API

## Uwagi

- Token GitHub jest wymagany do uniknięcia limitów API (60 zapytań/h bez tokena, 5000 zapytań/h z tokenem)
- Aplikacja używa reaktywnego podejścia dla lepszej wydajności
- Wszystkie odpowiedzi są w formacie JSON
