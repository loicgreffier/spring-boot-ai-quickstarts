# Agent Instructions

## Repository Structure

- `chat-*` modules: chat completion code samples (e.g. conversation memory, RAG, streaming).
- `mcp-server-*` modules: Model Context Protocol (MCP) server code samples (e.g. HTTP, stdio).

Each quickstart is an independent Spring Boot module.
A module typically contains an application class (runner), `config`, `controller`, `data`, `model`, `repository`, and `service` package under `io.github.loicgreffier`.

Each `chat-*` module also contains a `ui` folder: a small standalone Angular front-end for the quickstart. The `mcp-server-*` modules have no UI.

## Commands

### Java

- Build with `mvn clean package`.
- Run the tests with `mvn test`.
- Run `mvn spotless:apply` before committing to apply Palantir Java Format.

### Angular

- Build with `npm run build` (or `mvn clean package -Pproduction` to bundle the UI into the Spring Boot app).
- Run the dev server with `npm start` (served on `http://localhost:4200`).
- Run `npm run lint -- --fix` before committing to lint and format the code.

## Coding Standards

### Java

- Target Java 25.
- Never use `var`. Always declare variables with their explicit type.
- Code follows Palantir Java Format.
- Add minimal Javadoc to every method in production code (`src/main`), including `@Override` methods, with descriptions for parameters and return values. Start each description with an uppercase letter.

### Angular

- Target Angular 22 and TypeScript 6.
- Use standalone components, signals, and the `inject()` function (no `NgModule`).
- Code follows ESLint and Prettier rules.
- Add minimal JSDoc to every method, with descriptions for parameters using `@param`. Start each description with an uppercase letter.

## Testing Standards

### Java

- Name test classes `<ClassName>Test`.
- Name unit tests with the "should..." convention (e.g. `shouldGetEpisodes`).
- Use JUnit Jupiter assertions (`org.junit.jupiter.api.Assertions`).
- Use Mockito with `@ExtendWith(MockitoExtension.class)`.
- Mock collaborators such as repositories and vector stores with Mockito's `@Mock`/`@InjectMocks`.

### Angular

- No tests are written for the Angular UI.
