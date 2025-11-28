# Usuarios API – Prueba Técnica Banco de Chile

API REST para la gestión de usuarios, desarrollada con **Spring Boot 3, siguiendo una arquitectura hexagonal ligera,
 con persistencia en H2, autenticación JWT, validaciones configurables, manejo centralizado de errores y pruebas unitarias.

El proyecto cumple con los requisitos solicitados en la prueba técnica, incluyendo:

- Validación de correo y contraseña mediante expresiones regulares configurables.
- Persistencia del token JWT junto al usuario.
- Autenticación JWT en todos los endpoints excepto registro/login.
- Base de datos en memoria (H2).
- Endpoints REST completos (CRUD + login).
- Pruebas unitarias en casos de uso.
- Swagger.
- Arquitectura limpia y desacoplada.

---

## 🚀 Tecnologías utilizadas

| Componente        | Tecnología |
|------------------|------------|
| Lenguaje         | Java 17 |
| Framework        | Spring Boot 3.3.4 |
| Base de datos    | H2 in-memory |
| Persistencia     | Spring Data JPA + Hibernate |
| Seguridad        | Spring Security + JWT (jjwt) |
| Validaciones     | Jakarta Validation (@Valid) |
| Documentación    | Swagger (springdoc-openapi) |
| Testing          | JUnit 5 + Mockito + AssertJ |
| Build            | Maven |
| Arquitectura     | Hexagonal ligera (puertos y adaptadores) |

---

## 📁 Estructura del Proyecto (Arquitectura Hexagonal)

La estructura está organizada de forma clara siguiendo el patrón de arquitectura hexagonal:

src/main/java/com/josebejar/evaluacion
├── dominio/
│ ├── modelo/ → Entidades del dominio (Usuario, Telefono)
│ ├── puerto/
│ │ ├── entrada/ → Use cases (interfaces)
│ │ └── salida/ → Interfaces hacia infraestructura (repositorios, token provider)
│
├── application/
│ └── service/ → Implementación de casos de uso
│
└── infraestructura/
├── web/ → Controladores REST y DTOs
├── persistencia/jpa/ → JPA Entities + Adaptadores a BD
├── seguridad/jwt/ → JWT Provider + Filtro
├── config/ → Configuración (Security, OpenAPI, etc.)
├── exception/ → Manejo global de errores


Arquitectura basada en:

- Separación clara entre dominio, lógica de aplicación e infraestructura.
- Uso de puertos (interfaces) para desacoplar casos de uso de infraestructura.
- Implementaciones concretas (adaptadores) en infraestructura.
- Controladores REST mapean requests → commands → casos de uso.

---

## Configuración Principal

## JWT

application.properties

app.jwt.secret=EstaClaveEsMuyLargaYSegura_ParaFirmarTokensJWT_1234567890
app.jwt.expiration-ms=86400000  # 24 horas

## VALIDACION DE CORREO Y CONTRASEÑA

app.regex.correo=^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$
app.regex.contrasena=^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$

## BASE DE DATOS EN MEMORIA H2

spring.datasource.url=jdbc:h2:mem:usuariosdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console


## CONSOLA H2 DISPONIBLE 
http://localhost:8080/h2-console

## JDBC URL:
jdbc:h2:mem:usuariosdb


## COMO EJECUTAR EL PROYECTO 

git clone https://github.com/JoseBejar/prueba-tecnica-banco-chile.git

cd usuarios-api

mvn clean install
mvn spring-boot:run

---

## Documentación de Endpoints

Todos los endpoints se encuentran bajo el path base:

/api/usuarios

## LOGIN

/api/login


Los únicos endpoints públicos son:

- POST /api/usuarios (registro)
- POST /api/login (autenticación)
- Swagger
- H2 console

El resto requiere token JWT.


### Seguridad JWT

### Endpoints públicos
- POST /api/usuarios
- POST /api/login
- /swagger-ui/**
- /v3/api-docs/**
- /h2-console/**

### Endpoints protegidos
Todos los demás requieren header:


El token se genera y persiste cuando:

- Se registra un usuario
- Se hace login

---

## 1. Registrar usuario

**POST** /api/usuarios`  
**Auth:** Público  
**Descripción:** Crea un usuario, valida correo/contraseña y genera token JWT.

### Body request

json:
{
  "nombre": "Jose Luis",
  "correo": "jose@test.com",
  "contrasena": "Password123",
  "telefonos": [
    {
      "numero": "1234567",
      "codigoCiudad": "1",
      "codigoPais": "57"
    }
  ]
}

## RESPUESTA EXISTOSA

{
  "id": "ee1bfcc3-00f8-4c1e-be6f-c4ccde5d0f38",
  "nombre": "Jose Luis",
  "correo": "jose@test.com",
  "creado": "2025-11-26T21:18:36",
  "modificado": "2025-11-26T21:18:36",
  "ultimoLogin": "2025-11-26T21:18:36",
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "activo": true,
  "telefonos": [
    {
      "id": 1,
      "numero": "1234567",
      "codigoCiudad": "1",
      "codigoPais": "57"
    }
  ],
  "contraseña": "Password123"
}

## Uso del Token JWT después de crear un usuario

POST /api/usuarios

## La API genera un token JWT y lo retorna dentro del campo:

"token": "eyJhbGciOiJIUzI1NiJ9..."

## Este token representa la identidad del usuario dentro del sistema y es obligatorio para consumir 
## el resto de los endpoints protegidos de la API.

## Debes incluir el token en el header Authorization de la siguiente forma:

Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkLWVmZy0xMjM0... 

## DIAGRAMA DE FLUJO DE TOKEN 

sequenceDiagram
    Cliente->>API: POST /api/usuarios
    API-->>Cliente: token JWT
    Cliente->>API: GET /api/usuarios/{id} (Authorization: Bearer TOKEN)
    API-->>Cliente: datos protegidos


## Swagger

http://localhost:8080/swagger-ui/index.html

## OpenAPI JSON

http://localhost:8080/v3/api-docs


## DIAGRAMA GENERAL DE ARQUITECTURA HEXAGONAL LIGERA

**** LO PUEDES REVISAR EN https://mermaid.live/  PARA PODER VER EL DIAGRAMA DE ARQUITECTURA 


flowchart LR
    A[Cliente REST - Postman o Front] --> B[Controladores REST - Infraestructura]

    B --> C[Casos de uso <br/> application.service]
    C --> D[Puertos de entrada <br/> dominio.puerto.entrada]

    C --> E[Puertos de salida <br/> dominio.puerto.salida]
    E --> F[Adaptador JPA <br/> infraestructura.persistencia.jpa]
    E --> G[Adaptador JWT <br/> infraestructura.seguridad.jwt]

    F --> H[(Base de datos H2 <br/> usuarios / telefonos)]
    G --> I[Tokens JWT]

    style A fill:#fdf5e6,stroke:#333,stroke-width:1px
    style B fill:#e6f7ff,stroke:#333,stroke-width:1px
    style C fill:#e8f5e9,stroke:#333,stroke-width:1px
    style D fill:#ffffff,stroke:#333,stroke-width:1px,stroke-dasharray: 3 3
    style E fill:#ffffff,stroke:#333,stroke-width:1px,stroke-dasharray: 3 3
    style F fill:#fff3e0,stroke:#333,stroke-width:1px
    style G fill:#f3e5f5,stroke:#333,stroke-width:1px
    style H fill:#ffe0e0,stroke:#333,stroke-width:1px
    style I fill:#f0f4c3,stroke:#333,stroke-width:1px
	
	
	
	
## DIAGRAMA DE SECUENCIA - REGISTRO DE USUARIO

sequenceDiagram
    participant C as Cliente REST
    participant API as Usuarios API
    participant Ctrl as UsuarioController
    participant UC as RegistrarUsuarioService
    participant RepoPort as UsuarioRepositoryPort
    participant Jwt as JwtTokenProvider
    participant JPA as UsuarioJpaRepository
    participant DB as BD H2

    C->>API: POST /api/usuarios (JSON usuario)
    API->>Ctrl: crearUsuario(request)
    Ctrl->>UC: registrar(command)

    UC->>RepoPort: buscarPorCorreo(correo)
    RepoPort->>JPA: findByCorreo(correo)
    JPA-->>RepoPort: Optional<UsuarioEntity>
    RepoPort-->>UC: Optional<Usuario>

    UC->>UC: validar correo y contraseña (regex)

    alt correo ya existe
        UC-->>Ctrl: error "El correo ya está registrado"
        Ctrl-->>API: 409 Conflict
    else
        UC->>Jwt: generarToken(id, correo)
        Jwt-->>UC: token JWT

        UC->>RepoPort: guardar(usuario)
        RepoPort->>JPA: save()
        JPA->>DB: INSERT usuarios / telefonos

        UC-->>Ctrl: usuario creado
        Ctrl-->>API: 201 Created + UsuarioResponse
    end



##  SCRIPT SQL 

**SE ENCONTRARA EN LA RUTA : 
**src/main/resources/schema.sql

CREATE TABLE usuarios (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(100),
    correo VARCHAR(150) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    creado TIMESTAMP NOT NULL,
    modificado TIMESTAMP NOT NULL,
    ultimo_login TIMESTAMP NOT NULL,
    token VARCHAR(500),
    activo BOOLEAN NOT NULL
);

CREATE TABLE telefonos (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    numero VARCHAR(30),
    codigo_ciudad VARCHAR(10),
    codigo_pais VARCHAR(10),
    usuario_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);






