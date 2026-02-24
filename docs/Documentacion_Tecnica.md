# Documentación Técnica del Proyecto

## Sistema de Gestión Financiera Personal

---

## 1. Descripción del Proyecto

### 1.1 Objetivo

Desarrollar una aplicación de escritorio en Java para gestión financiera personal que permita administrar gastos e inversiones, cumpliendo requisitos académicos de arquitectura MVC, persistencia real en base de datos, validaciones y diseño escalable.

### 1.2 Requisitos Obligatorios

| Requisito | Descripción |
|-----------|-------------|
| Aplicación de escritorio Java | Ejecutable local en PC |
| Arquitectura MVC | Separación por capas (Model-View-Controller) |
| 3 ABML/CRUD obligatorios | Gasto, Inversión, Cuenta Financiera |
| Persistencia real a BD | Base de datos SQL Server |
| Interfaz gráfica Swing | GUI principal obligatoria |
| Validaciones | Campos, montos positivos, formatos |
| JPA/Hibernate | Acceso a datos |
| Diseño desacoplado | Interfaces e inyección por constructor |
| Código escalable | Adaptable a cambios futuros |

### 1.3 Funcionalidades Principales

- Sistema de login de usuario
- Gestión de gastos (ABML)
- Gestión de inversiones (ABML)
- Gestión de cuentas financieras (ABML)
- Dashboard financiero con gráficos
- Validaciones en UI y lógica de negocio

### 1.4 Funcionalidades Extra

- Dashboard con gráficos JavaFX
- Exportación de reportes PDF y Excel
- Sistema multi-moneda configurable en BD

---

## 2. Tecnologías Utilizadas

### 2.1 Stack Principal

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17+ | Lenguaje de programación |
| Swing | JDK Built-in | Interfaz gráfica principal |
| JavaFX | 21.0.1 | Dashboard y gráficos |
| JPA/Hibernate | 6.4.0.Final | Persistencia a base de datos |
| SQL Server | 2019+ | Base de datos relacional |
| Maven | 3.9+ | Gestión de dependencias |

### 2.2 Dependencias Principales (pom.xml)

```xml
<!-- Persistencia -->
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
</dependency>
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.0.Final</version>
</dependency>
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.6.1.jre11</version>
</dependency>
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.1.0</version>
</dependency>

<!-- Validación -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>

<!-- Reportes -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
    <type>pom</type>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>

<!-- JavaFX -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.1</version>
</dependency>

<!-- Utilidades -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.11</version>
</dependency>
```

---

## 3. Arquitectura del Sistema

### 3.1 Patrón MVC con Capas

```
┌─────────────────────────────────────────────────────────────────┐
│                         VIEW (Swing/FX)                        │
│   LoginView  │  GastoView  │  DashboardFX  │  Reportes        │
└──────────┬────────────┬──────────────┬───────────┬─────────────┘
           │            │              │           │
           ▼            ▼              ▼           ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CONTROLLER                                │
│  LoginController  │  GastoController  │  DashboardController   │
│         └──────────────┬──────────────────┘                    │
└────────────────────────┼────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                        SERVICE                                 │
│  IGastoService    │  IInversionService  │  IDashboardService   │
│         └────────────────┬─────────────────┘                   │
└─────────────────────────┼───────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                      REPOSITORY                                │
│  GastoRepository  │  InversionRepository  │  CuentaRepository │
│         └──────────────────┬──────────────────┘                  │
└───────────────────────────┼─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    JPA / HIBERNATE                              │
│                    EntityManager                               │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SQL SERVER                                   │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2 Regla de Flujo

**El sistema debe cumplir el flujo estricto:**

```
View ──► Controller ──► Service ──► Repository ── Base de Datos
```

**No se permiten accesos cruzados entre capas.**

### 3.3 Reglas de Arquitectura

| Regla | Descripción |
|-------|-------------|
| Dependencias permitidas | View → Controller → Service → Repository → Base de Datos |
| No accesos cruzados | No se permiten accesos directos entre capas no consecutivas |
| Entidades independientes | Las entidades no dependen de servicios |
| Controller sin lógica | Controllers no contienen lógica de negocio |
| Service independiente | Services no dependen de UI |
| Repository datos | Repository solo accede a datos |

#### Dependencias permitidas por paquete

| Paquete | Depende de | No depende de |
|---------|------------|---------------|
| **view.swing** | controller, model.dto | service, repository, model.entity |
| **view.javafx** | controller, model.dto | service, repository, model.entity |
| **controller** | service, model.dto | repository, model.entity |
| **service** | repository, validator, model.entity, model.dto | view, controller |
| **repository** | model.entity | service, controller, view |
| **validator** | model.dto, util | repository, service, view |
| **security** | model.entity, util | view, controller, service |
| **exception** | util | (mínimo acoplamiento) |
| **report** | service, model.dto | repository, view |
| **config** | (configuración pura) | (ninguna capa de negocio) |

#### Objetivos de Diseño

| Objetivo | Descripción |
|----------|-------------|
| **Escalabilidad** | El sistema puede crecer sin modificar estructura existente |
| **Desacoplamiento** | Cada paquete funciona independientemente |
| **Reutilización** | Componentes reutilizables en diferentes contextos |
| **Independencia BD** | Repository permite cambiar SQL Server por otra BD |
| **Mantenibilidad** | Cambios localizados sin efectos secundarios |

---

## 4. Estructura de Paquetes

### 4.1 Namespace Base

```
com.finanzasapp
```

### 4.2 Árbol de Paquetes

```
src/main/java/com/finanzasapp/
│
├── Main.java                                    # Punto de entrada aplicación
│
├── config/                                       # Configuración JPA, BD y framework
│   ├── JpaConfig.java                           # Configuración EntityManager
│   ├── DatabaseConfig.java                     # Configuración conexión BD
│   └── FxConfig.java                            # Configuración JavaFX
│
├── controller/                                   # Coordinación entre View y Service
│   ├── LoginController.java                     # Controlador de autenticación
│   ├── GastoController.java                     # Controlador de gastos
│   ├── InversionController.java                 # Controlador de inversiones
│   ├── CuentaFinancieraController.java          # Controlador de cuentas
│   ├── DashboardController.java                 # Controlador del dashboard
│   └── MainController.java                      # Controlador principal
│
├── model/                                       
│   ├── entity/                                  # Entidades JPA persistentes
│   │   ├── Usuario.java
│   │   ├── Gasto.java
│   │   ├── Inversion.java
│   │   ├── CuentaFinanciera.java
│   │   ├── CategoriaGasto.java
│   │   ├── CategoriaInversion.java
│   │   ├── TipoCuenta.java
│   │   ├── Moneda.java
│   │   ├── Movimiento.java
│   │   └── HistorialValor.java
│   │
│   ├── dto/                                     # Objetos de transferencia de datos
│   │   ├── UsuarioDTO.java
│   │   ├── GastoDTO.java
│   │   ├── InversionDTO.java
│   │   ├── CuentaFinancieraDTO.java
│   │   ├── ResumenFinancieroDTO.java
│   │   └── EstadisticasDTO.java
│   │
│   └── enums/                                   # Enumeraciones del sistema
│       ├── TipoCategoria.java                   # FIJO, VARIABLE, OCASIONAL
│       ├── NivelRiesgo.java                     # BAJO, MEDIO, ALTO
│       └── TipoMovimiento.java                  # ENTRADA, SALIDA
│
├── repository/                                  # Interfaces de acceso a datos
│   ├── impl/                                      # Implementaciones
│   │   ├── UsuarioRepositoryImpl.java
│   │   ├── GastoRepositoryImpl.java
│   │   ├── InversionRepositoryImpl.java
│   │   ├── CuentaFinancieraRepositoryImpl.java
│   │   ├── CategoriaGastoRepositoryImpl.java
│   │   ├── CategoriaInversionRepositoryImpl.java
│   │   ├── TipoCuentaRepositoryImpl.java
│   │   ├── MonedaRepositoryImpl.java
│   │   ├── MovimientoRepositoryImpl.java
│   │   └── HistorialValorRepositoryImpl.java
│   │
│   ├── UsuarioRepository.java
│   ├── GastoRepository.java
│   ├── InversionRepository.java
│   ├── CuentaFinancieraRepository.java
│   ├── CategoriaGastoRepository.java
│   ├── CategoriaInversionRepository.java
│   ├── TipoCuentaRepository.java
│   ├── MonedaRepository.java
│   ├── MovimientoRepository.java
│   └── HistorialValorRepository.java
│
├── service/                                      # Lógica de negocio y transacciones
│   ├── interfaces/
│   │   ├── IUsuarioService.java
│   │   ├── IGastoService.java
│   │   ├── IInversionService.java
│   │   ├── ICuentaFinancieraService.java
│   │   ├── IAutenticacionService.java
│   │   ├── IReporteService.java
│   │   └── IDashboardService.java
│   │
│   └── impl/
│       ├── UsuarioServiceImpl.java
│       ├── GastoServiceImpl.java
│       ├── InversionServiceImpl.java
│       ├── CuentaFinancieraServiceImpl.java
│       ├── AutenticacionServiceImpl.java
│       ├── ReporteServiceImpl.java
│       └── DashboardServiceImpl.java
│
├── view/
│   ├── swing/                                   # Interfaz gráfica principal (OBLIGATORIA)
│   │   ├── LoginView.java                      # Ventana de login
│   │   ├── MainView.java                       # Ventana principal
│   │   ├── GastoView.java                      # Gestión de gastos
│   │   ├── InversionView.java                  # Gestión de inversiones
│   │   ├── CuentaFinancieraView.java           # Gestión de cuentas
│   │   └── componentes/                        # Componentes reutilizables
│   │       ├── TablaGenerica.java
│   │       ├── FormularioPanel.java
│   │       ├── MensajeDialogo.java
│   │       └── ValidacionLabel.java
│   │
│   └── javafx/                                  # Interfaz avanzada (OPCIONAL)
│       ├── DashboardView.java
│       ├── GraficoPieView.java
│       └── GraficoLineaView.java
│
├── exception/                                    # Excepciones personalizadas
│   ├── GlobalExceptionHandler.java
│   ├── ValidacionException.java
│   ├── AutenticacionException.java
│   ├── RecursoNoEncontradoException.java
│   ├── PersistenciaException.java
│   └── ReglaNegocioException.java
│
├── validator/                                    # Validaciones de datos reutilizables
│   ├── UsuarioValidator.java
│   ├── GastoValidator.java
│   ├── InversionValidator.java
│   ├── CuentaFinancieraValidator.java
│   └── CampoValidatorUtil.java
│
├── security/                                     # Autenticación y gestión de usuarios
│   ├── EncriptadorContrasena.java              # Hash BCrypt
│   ├── SesionUsuario.java                       # Gestión de sesión
│   └── PermisoUsuario.java                     # Roles y permisos
│
├── util/                                         # Funciones auxiliares
│   ├── ValidadorCampos.java
│   ├── FormatoMoneda.java
│   ├── FechaUtil.java
│   ├── ConstantesApp.java
│   └── ResourceBundleUtil.java
│
└── report/                                       # Generación de reportes
    ├── ReporteService.java
    ├── ReportePDF.java
    └── ReporteExcel.java
```

### 4.3 Dependencias entre Paquetes

```
                    ┌────────────────────────────────────┐
                    │              VIEW                  │
                    │  (view.swing, view.javafx)        │
                    │   Solo presentación y UI          │
                    └──────────────┬───────────────────┘
                                   │
                    ┌──────────────▼───────────────────┐
                    │           CONTROLLER              │
                    │   Coordina flujo UI ↔ Service    │
                    └──────────────┬───────────────────┘
                                   │
                    ┌──────────────▼───────────────────┐
                    │            SERVICE                │
                    │  Lógica de negocio + transacciones│
                    │  + validación (usa validator)     │
                    └──────────────┬───────────────────┘
                                   │
                    ┌──────────────▼───────────────────┐
                    │          REPOSITORY               │
                    │   Acceso a datos JPA             │
                    └──────────────┬───────────────────┘
                                   │
                    ┌──────────────▼───────────────────┐
                    │         BASE DE DATOS             │
                    │           SQL Server              │
                    └──────────────────────────────────┘

    ┌─────────────┐     ┌──────────────┐     ┌───────────┐
    │   SECURITY  │────►│    MODEL     │◄────│  VALIDATOR│
    │ (independiente)   │  (entidades)  │     │(utilitario)│
    └─────────────┘     └──────────────┘     └───────────┘

    ┌─────────────┐     ┌──────────────┐
    │   EXCEPTION │◄────│   SERVICE    │
    │(manejo err) │     │   y REPO    │
    └─────────────┘     └──────────────┘

    ┌─────────────┐
    │    REPORT   │
    │(consume Service)│
    └─────────────┘
```

---

## 5. Responsabilidades por Capa

### 5.1 Model / Entity

| Responsabilidad | Restricciones |
|-----------------|---------------|
| Representación de objetos del dominio | POJO puro |
| Mapeo objeto-relacional (JPA) | Solo anotaciones@Entity, @Table, @Column |
| Validaciones básicas de integridad | @NotNull, @Positive, @Size |
| Sin lógica de negocio | Solo getters/setters |
| Sin dependencias de otras capas | Reutilizable en cualquier entorno |

### 5.2 Repository

| Responsabilidad | Restricciones |
|-----------------|---------------|
| Acceso a datos persistence | Interfaz + Implementación |
| Consultas JPQL/SQL | Solo operaciones CRUD |
| Abstracción de la base de datos | Cambiar BD sin afectar Service |
| Sin lógica de negocio | Delegar a Service |

### 5.3 Service

| Responsabilidad | Restricciones |
|-----------------|---------------|
| **Toda la lógica del sistema** | Ninguna lógica en otras capas |
| Validaciones de negocio | Campos obligatorios, reglas específicas |
| Transacciones | @Transactional obligatorio |
| Coordinar múltiples repositories | Usar interfaces |

### 5.4 Controller

| Responsabilidad | Restricciones |
|-----------------|---------------|
| Recibir eventos de View | Solo flujo y coordinación |
| Traducir acciones a llamadas Service | Sin lógica de negocio |
| Devolver respuesta a View | No acceder directamente a Repository |
| Manejo de navegación | Delega a ExceptionHandler |

### 5.5 View

| Responsabilidad | Restricciones |
|-----------------|---------------|
| Renderizado de interfaz | Solo presentación |
| Captura de input del usuario | Sin lógica de negocio |
| Validaciones básicas de formato | Formato email, campos vacíos, números |
| Mostrar mensajes al usuario | Siempre a través de Controller |

### 5.6 Exception

| Responsabilidad | Restricciones |
|-----------------|---------------|
| Manejo centralizado de errores | Jerarquía personalizada |
| Propagación controlada | Entre capas |
| Mensajes amigables al usuario | Traducibles |

---

## 6. Reglas de Negocio del Sistema

> **Nota Importante**: Las reglas de negocio deben implementarse **exclusivamente** en la capa Service.
> No deben existir validaciones de negocio en Controller ni en View.

### 6.1 Principios Generales

| Principio | Descripción |
|-----------|-------------|
| Consistencia | Toda operación financiera debe ser consistente y trazable |
| Montes positivos | No se permiten montos negativos |
| Trazabilidad | Todas las operaciones deben registrar fecha y usuario |
| Soft delete | No se eliminan datos críticos físicamente |
| Movimientos | Las operaciones financieras deben generar movimientos asociados |
| Validación previa | Las validaciones se ejecutan antes de persistir datos |
| Atomicidad | Las operaciones compuestas deben ser atómicas |

### 6.2 Reglas por Entidad

#### 6.2.1 Usuario

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Username único | Username obligatorio y único en el sistema | Validación en Service + Constraint UNIQUE en BD |
| Password hash | Password almacenado en hash BCrypt | `EncriptadorContrasena.hash(password)` |
| Estado activo | Usuario puede estar activo o inactivo | Campo `activo` booleano |
| Eliminación | No se elimina físicamente el usuario | Soft delete (activo = false) |
| Email | Formato válido si se proporciona | Validación con regex |

#### 6.2.2 CuentaFinanciera

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Propietario | Siempre pertenece a un usuario | FK obligatoria a Usuario |
| Moneda | Moneda obligatoria | FK obligatoria a Moneda |
| Saldo inicial | El saldo inicial es 0 | DEFAULT 0 en BD |
| Actualización | Saldo se actualiza únicamente mediante movimientos | Solo método `actualizarSaldo()` en Service |
| Saldo negativo | No se permite si la configuración lo impide | Validación en Service antes de операцию |
| Fecha apertura | Debe registrar fecha de creación | Campo obligatorio |

#### 6.2.3 Gasto

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Monto | Monto mayor a 0 obligatorio | Validación `@Positive` + Service |
| Asociado | Debe estar asociado a usuario y cuenta | FK obligatorias |
| Saldo | Debe descontar saldo de cuenta | Transacción: gasto + descuento |
| Movimiento | Debe generar movimiento tipo SALIDA automáticamente | Crear Movimiento en misma transacción |
| Fecha | Debe registrar fecha | Campo obligatorio |
| Saldo suficiente | No puede registrarse si cuenta no tiene saldo (si aplica) | Validación previa en Service |

#### 6.2.4 Inversion

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Monto inicial | Monto mayor a 0 obligatorio | Validación `@Positive` + Service |
| Fecha inicio | Debe registrar fecha de inicio | Campo obligatorio |
| Estado | Puede estar activa o finalizada | Campo `activa` booleano |
| Rentabilidad | Actualizar valor actual recalcula rentabilidad | Método `calcularRentabilidad()` |
| Movimientos | Puede generar movimientos financieros | Opcional según tipo |
| Historial | Puede registrar historial de valores | Tabla HistorialValor |
| Rendimiento | Debe permitir cálculo de rendimiento | `(valorActual - montoInicial) / montoInicial * 100` |

#### 6.2.5 Movimiento

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Representación | Representa toda modificación de saldo | Entidad independiente |
| Tipo | Puede ser ENTRADA o SALIDA | Enum obligatorio |
| Monto | Debe tener monto positivo | Validación `@Positive` |
| Cuenta | Debe estar asociado a cuenta | FK obligatoria |
| Relación opcional | Puede vincular gasto o inversión | FKs opcionales |
| Inmutabilidad | No puede modificarse luego de creado | Solo operaciones CREATE y READ |

#### 6.2.6 CategoriaGasto

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Nombre | Nombre único | Constraint UNIQUE |
| Eliminación | No puede eliminarse si tiene gastos asociados | ON DELETE RESTRICT |

#### 6.2.7 CategoriaInversion

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Nombre | Nombre único | Constraint UNIQUE |
| Riesgo | Debe definir nivel de riesgo | Enum obligatorio |
| Eliminación | No puede eliminarse si tiene inversiones asociadas | ON DELETE RESTRICT |

#### 6.2.8 Moneda

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Código ISO | Código ISO único | Constraint UNIQUE |
| Predeterminada | Solo una moneda puede ser predeterminada | Service valida antes de setear |
| Estado | Puede activarse o desactivarse | Campo `activa` booleano |
| Tipo de cambio | El tipo de cambio puede actualizarse | Método `actualizarTipoCambio()` |

#### 6.2.9 TipoCuenta

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Nombre | Nombre único | Constraint UNIQUE |
| Reglas | Define reglas de depósitos y retiros | Campos booleanos |
| Estado | Puede activarse o desactivarse | Campo `activo` booleano |
| Eliminación | No puede eliminarse si está en uso | ON DELETE RESTRICT |

#### 6.2.10 HistorialValor

| Regla | Descripción | Implementación |
|-------|-------------|----------------|
| Evolución | Registra evolución temporal de inversiones | Tabla histórica |
| Inmutabilidad | No puede modificarse ni eliminarse | Solo CREATE y READ |
| Nuevos registros | Solo se pueden agregar nuevos registros | Sin UPDATE/DELETE |

### 6.3 Reglas Transaccionales Obligatorias

Las siguientes operaciones **deben ejecutarse dentro de una única transacción**:

| Operación | Pasos de la Transacción |
|-----------|------------------------|
| Registrar gasto | 1. Validar datos → 2. Descontar saldo cuenta → 3. Crear movimiento SALIDA → 4. Persistir gasto |
| Registrar inversión | 1. Validar datos → 2. (Opcional) descontar de cuenta → 3. Crear movimiento → 4. Persistir inversión |
| Actualizar valor inversión | 1. Actualizar valorActual → 2. Recalcular rentabilidad → 3. Registrar en HistorialValor |
| Cambiar moneda predeterminada | 1. Buscar moneda actual → 2. Setear predeterminada = false → 3. Setear nueva = true |

**Si falla cualquier paso, se revierte toda la operación** (Rollback automático).

### 6.4 Validaciones Obligatorias del Sistema

| Tipo de Validación | Descripción | Capa |
|--------------------|-------------|------|
| Campos NOT NULL | Verificar campos obligatorios antes de persistir | Service + Entity |
| Rangos numéricos | Verificar valores dentro de rangos permitidos | Service |
| Formato email | Validar formato de email con regex | Service |
| Consistencia FK | Verificar que las FK existan | Service |
| Reglas financieras | Verificar reglas antes de modificar saldo | Service |
| Validaciones UI | Formato, campos vacíos | View |

### 6.5 Principios de Implementación

| Principio | Descripción |
|-----------|-------------|
| Lógica centralizada | Toda lógica de negocio en Service |
| Excepciones personalizadas | Usar excepciones específicas para errores de negocio |
| Transacciones | Operaciones críticas anotadas con `@Transactional` |
| Reutilización | Reglas reutilizables mediante servicios o utilidades |

### 6.6 Diagrama de Flujo de Operaciones Transaccionales

```
┌─────────────────────────────────────────────────────────────┐
│                    REGISTRAR GASTO                         │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │ 1. Validar datos   │
              │ - Monto > 0        │
              │ - Cuenta existe    │
              │ - Saldo suficiente │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │ 2. Descontar saldo │
              │   de cuenta         │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │ 3. Crear movimiento│
              │   tipo SALIDA       │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │ 4. Persistir gasto  │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │    COMMIT /         │
              │    ROLLBACK         │
              └─────────────────────┘
```

---

## 7. Entidades del Sistema

### 7.1 Entidades Principales (CRUD)

#### Usuario
| Atributo | Tipo | Restricciones | Descripción |
|----------|------|---------------|-------------|
| id | Long | PK, AutoIncrement | Identificador único |
| username | String | NOT NULL, UNIQUE, 50 max | Nombre de usuario |
| password | String | NOT NULL, 255 max | Contraseña encriptada |
| email | String | NULL, 100 max | Correo electrónico |
| fechaRegistro | LocalDateTime | NOT NULL | Fecha de registro |
| activo | Boolean | NOT NULL, DEFAULT true | Estado de cuenta |

#### Gasto
| Atributo | Tipo | Restricciones | Descripción |
|----------|------|---------------|-------------|
| id | Long | PK, AutoIncrement | Identificador único |
| usuario | Usuario | NOT NULL, FK | Usuario propietario |
| cuenta | CuentaFinanciera | NOT NULL, FK | Cuenta afectada |
| categoria | CategoriaGasto | NOT NULL, FK | Clasificación |
| monto | BigDecimal | NOT NULL, > 0, 15/2 | Cantidad del gasto |
| descripcion | String | NULL, 500 max | Detalle adicional |
| fecha | LocalDateTime | NOT NULL | Fecha del gasto |
| moneda | Moneda | NULL, FK | Moneda utilizada |

#### Inversion
| Atributo | Tipo | Restricciones | Descripción |
|----------|------|---------------|-------------|
| id | Long | PK, AutoIncrement | Identificador único |
| usuario | Usuario | NOT NULL, FK | Usuario propietario |
| cuenta | CuentaFinanciera | NULL, FK | Cuenta asociada |
| categoria | CategoriaInversion | NOT NULL, FK | Tipo de inversión |
| montoInicial | BigDecimal | NOT NULL, > 0, 15/2 | Capital invertido |
| valorActual | BigDecimal | NULL, 15/2 | Valor actualizado |
| fechaInicio | LocalDateTime | NOT NULL | Inicio inversión |
| fechaFin | LocalDateTime | NULL | Fin previsto |
| rentabilidad | BigDecimal | NULL, 5/2 (%) | Porcentaje rendimiento |
| activa | Boolean | NOT NULL, DEFAULT true | Estado activo |

#### CuentaFinanciera
| Atributo | Tipo | Restricciones | Descripción |
|----------|------|---------------|-------------|
| id | Long | PK, AutoIncrement | Identificador único |
| usuario | Usuario | NOT NULL, FK | Usuario propietario |
| nombre | String | NOT NULL, 100 max | Nombre descriptivo |
| tipoCuenta | TipoCuenta | NOT NULL, FK | Clasificación cuenta |
| saldoActual | BigDecimal | NOT NULL, 15/2, DEFAULT 0 | Saldo disponible |
| moneda | Moneda | NOT NULL, FK | Moneda de la cuenta |
| fechaApertura | LocalDateTime | NOT NULL | Creación cuenta |

### 7.2 Entidades de Categorización

#### CategoriaGasto
| Atributo | Tipo | Restricciones |
|----------|------|---------------|
| id | Long | PK, AutoIncrement |
| nombre | String | NOT NULL, UNIQUE, 50 max |
| descripcion | String | NULL, 200 max |
| icono | String | NULL, 50 max |
| tipo | Enum | NOT NULL (FIJO/VARIABLE/OCASIONAL) |

#### CategoriaInversion
| Atributo | Tipo | Restricciones |
|----------|------|---------------|
| id | Long | PK, AutoIncrement |
| nombre | String | NOT NULL, UNIQUE, 50 max |
| descripcion | String | NULL, 200 max |
| nivelRiesgo | Enum | NOT NULL (BAJO/MEDIO/ALTO) |
| rendimientoPromedio | BigDecimal | NULL, 5/2 (%) |

#### TipoCuenta
| Atributo | Tipo | Restricciones |
|----------|------|---------------|
| id | Long | PK, AutoIncrement |
| nombre | String | NOT NULL, UNIQUE, 30 max |
| descripcion | String | NULL, 200 max |
| permiteDepositos | Boolean | NOT NULL, DEFAULT true |
| permiteRetiros | Boolean | NOT NULL, DEFAULT true |
| activo | Boolean | NOT NULL, DEFAULT true |

### 7.3 Entidades de Configuración

#### Moneda
| Atributo | Tipo | Restricciones |
|----------|------|---------------|
| id | Long | PK, AutoIncrement |
| codigoISO | String | NOT NULL, UNIQUE, 3 max |
| nombre | String | NOT NULL, 50 max |
| simbolo | String | NULL, 5 max |
| tipoCambioBase | BigDecimal | NULL, 10/4 |
| activa | Boolean | NOT NULL, DEFAULT true |
| predeterminada | Boolean | NOT NULL, DEFAULT false |

### 7.4 Entidades Auxiliares

#### Movimiento
| Atributo | Tipo | Restricciones |
|----------|------|---------------|
| id | Long | PK, AutoIncrement |
| cuenta | CuentaFinanciera | NOT NULL, FK |
| tipo | Enum | NOT NULL (ENTRADA/SALIDA) |
| monto | BigDecimal | NOT NULL, 15/2 |
| fecha | LocalDateTime | NOT NULL |
| referencia | String | NULL, 200 max |
| gasto | Gasto | NULL, FK |
| inversion | Inversion | NULL, FK |

#### HistorialValor
| Atributo | Tipo | Restricciones |
|----------|------|---------------|
| id | Long | PK, AutoIncrement |
| inversion | Inversion | NOT NULL, FK |
| valor | BigDecimal | NOT NULL, 15/2 |
| fechaRegistro | LocalDateTime | NOT NULL |

---

## 7. Relaciones entre Entidades

### 7.1 Resumen de Relaciones

| Entidad 1 | Relación | Entidad 2 | Tipo | FK en |
|----------|----------|-----------|------|-------|
| Usuario | 1-N | Gasto | Uno a Muchos | Gasto |
| Usuario | 1-N | Inversion | Uno a Muchos | Inversion |
| Usuario | 1-N | CuentaFinanciera | Uno a Muchos | CuentaFinanciera |
| CuentaFinanciera | N-1 | TipoCuenta | Muchos a Uno | CuentaFinanciera |
| CuentaFinanciera | N-1 | Moneda | Muchos a Uno | CuentaFinanciera |
| Gasto | N-1 | CategoriaGasto | Muchos a Uno | Gasto |
| Gasto | N-1 | CuentaFinanciera | Muchos a Uno | Gasto |
| Inversion | N-1 | CategoriaInversion | Muchos a Uno | Inversion |
| Inversion | 1-N | HistorialValor | Uno a Muchos | HistorialValor |
| Movimiento | N-1 | CuentaFinanciera | Muchos a Uno | Movimiento |

> **Nota**: La relación Movimiento → Usuario existe implícitamente a través de CuentaFinanciera (Movimiento → CuentaFinanciera → Usuario).

### 7.2 Diagrama de Relaciones

```
                    ┌──────────────┐
                    │   USUARIO   │
                    │     (1)     │
                    └──────┬───────┘
                           │ 1:N
         ┌─────────────────┼─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
┌─────────────────┐ ┌─────────────┐ ┌──────────────────┐
│    GASTO        │ │  INVERSION  │ │CUENTA_FINANCIERA│
│     (*)        │ │    (*)      │ │       (*)        │
└────────┬────────┘ └──────┬──────┘ └────────┬─────────┘
         │                 │                 │
         │                 │                 │
         └────────┬────────┘ ┌───────────────┘
                  │          │
                  ▼          ▼
         ┌─────────────────────────────┐
         │      MOVIMIENTO            │
         │          (*)                │
         └─────────────┬───────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
   ┌─────────┐   ┌─────────┐   ┌──────────┐
   │CATEGORIA │   │CATEGORIA│   │  TIPO    │
   │_GASTO    │   │_INVERSION│  │ CUENTA   │
   │   (1)    │   │   (1)    │  │  (1)     │
   └──────────┘   └──────────┘  └──────────┘
                           
              ┌──────────┐ 
              │ MONEDA   │ 
              │  (1)     │ 
              └──────────┘
                    
        ┌──────────────────────────────────┐
        │        HISTORIAL_VALOR           │
        │              (*)                 │
        └──────────────────────────────────┘
```

### 7.3 Reglas de Integridad Referencial

| Relación | On Delete | On Update |
|----------|------------|------------|
| Usuario → Gasto | CASCADE | CASCADE |
| Usuario → Inversion | CASCADE | CASCADE |
| Usuario → CuentaFinanciera | CASCADE | CASCADE |
| Gasto → CategoriaGasto | RESTRICT | CASCADE |
| Gasto → CuentaFinanciera | RESTRICT | CASCADE |
| Gasto → Moneda | SET NULL | CASCADE |
| Inversion → CategoriaInversion | RESTRICT | CASCADE |
| Inversion → CuentaFinanciera | SET NULL | CASCADE |
| Inversion → HistorialValor | CASCADE | CASCADE |
| CuentaFinanciera → TipoCuenta | RESTRICT | CASCADE |
| CuentaFinanciera → Moneda | RESTRICT | CASCADE |
| Movimiento → CuentaFinanciera | CASCADE | CASCADE |
| Movimiento → Gasto | SET NULL | CASCADE |
| Movimiento → Inversion | SET NULL | CASCADE |

---

## 8. Principios de Diseño Aplicados

### 8.1 Principios SOLID

| Principio | Aplicación |
|-----------|------------|
| **S**ingle Responsibility | Cada clase tiene una responsabilidad única |
| **O**pen/Closed | Abierto para extensión, cerrado para modificación |
| **L**iskov Substitution | Sustitución de implementaciones por interfaces |
| **I**nterface Segregation | Interfaces pequeñas y específicas |
| **D**ependency Inversion | Depender de abstracciones, no de concreciones |

### 8.2 Otros Principios

| Principio | Aplicación |
|-----------|------------|
| DRY (Don't Repeat Yourself) | Código reutilizable en utilitarios |
| KISS (Keep It Simple, Stupid) | Soluciones simples y directas |
| YAGNI (You Aren't Gonna Need It) | Solo implementar lo necesario |
| Loose Coupling | Bajo acoplamiento entre capas |
| High Cohesion | Alta cohesión dentro de cada capa |

---

## 9. Justificación de Decisiones de Diseño

### 9.1 Decisiones Clave

| Decisión | Justificación |
|----------|---------------|
| **BigDecimal para montos** | Evita errores de punto flotante en cálculos financieros |
| **Soft delete (activo)** | Mantiene integridad referencial sin borrar datos |
| **LocalDateTime** | API moderna Java 8+, requiere Hibernate 5.2+ |
| **Enums como VARCHAR** | Más legible que integers en BD |
| **Usuario como raíz** | Todas las operaciones dependen de autenticación |
| **RESTRICT en catálogos** | Protege integridad de datos maestros |
| **HistorialValor con CASCADE** | Mantiene consistencia al eliminar inversiones |

### 9.2 Patrones de Diseño Utilizados

| Patrón | Capa | Propósito |
|--------|------|-----------|
| Repository | Repository | Abstraer acceso a datos |
| Factory | Service | Crear instancias complejas |
| DTO | Model | Transferir datos entre capas |
| Strategy | Service | Algoritmos intercambiables |
| Singleton | Config | Una sola instancia de config |

---

## 10. Configuración de Base de Datos

### 10.1 Connection String

```
jdbc:sqlserver://localhost:1433;database=FinanzasDB;encrypt=false;trustServerCertificate=true
```

### 10.2 Credenciales Recomendadas

| Propiedad | Valor |
|-----------|-------|
| Usuario | sa |
| Contraseña | (configurable en persistence.xml) |
| Base de datos | FinanzasDB |

### 10.3 Configuración Hibernate (persistence.xml)

```xml
<property name="jakarta.persistence.jdbc.url" 
    value="jdbc:sqlserver://localhost:1433;database=FinanzasDB"/>
<property name="jakarta.persistence.jdbc.driver" 
    value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
<property name="hibernate.dialect" 
    value="org.hibernate.dialect.SQLServerDialect"/>
<property name="hibernate.hbm2ddl.auto" value="update"/>
<property name="hibernate.show_sql" value="false"/>
<property name="hibernate.format_sql" value="true"/>
<property name="hibernate.hikari.connectionProvider" 
    value="org.hibernate.hikari.internal.HikariCPConnectionProvider"/>
<property name="hibernate.hikari.maximumPoolSize" value="20"/>
<property name="hibernate.bytecode.provider" value="javassist"/>
<property name="hibernate.physical_naming_strategy" 
    value="org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"/>
```

> **Nota**: La configuración completa está en `src/main/resources/META-INF/persistence.xml`

---

## 14. Mantenimiento y Extensibilidad

### 11.1 Cómo Agregar Nueva Entidad

1. Crear clase Entity en `model/entity/`
2. Crear interfaz Repository en `repository/`
3. Crear implementación en `repository/impl/`
4. Crear interfaz Service en `service/interfaces/`
5. Crear implementación en `service/impl/`
6. Crear Controller en `controller/`
7. Crear View en `view/swing/`
8. Registrar en flujo principal

### 11.2 Cómo Agregar Nueva Moneda

1. Insertar registro en tabla `monedas`
2. La aplicación la detectará automáticamente
3. Estará disponible para usar en cuentas y gastos

---

## 13. Decisiones Arquitectónicas Definitivas

> **Fecha**: 23/02/2026
> Estas decisiones son obligatorio контракт de desarrollo.

### 13.1 View → Entities o DTOs

| Decisión | Descripción |
|----------|-------------|
| Views reciben **SOLO DTOs** | Las Views NO pueden usar Entities directamente |
| Flujo obligatorio | `Entity → Service → DTO → Controller → View` |
| Motivo | Mantener separación MVC estricta, evitar acoplamiento con persistencia |

### 13.2 Repository

| Decisión | Descripción |
|----------|-------------|
| Estructura | **Interfaz + Implementación** en `repository/impl/` |
| Propósito | Permite cambiar tecnología de persistencia, cumple inversión de dependencias |

### 13.3 Configuración JPA

| Decisión | Descripción |
|----------|-------------|
| Método | **AMBOS**: persistence.xml + JpaConfig.java |
| persistence.xml | Define unidad de persistencia |
| JpaConfig.java | Centraliza creación de EntityManagerFactory |

### 13.4 Versión Java

| Decisión | Descripción |
|----------|-------------|
| Compatibilidad | **Java 17+** (Java 22 no obligatorio) |

### 13.5 Base de Datos

| Decisión | Descripción |
|----------|-------------|
| Creación BD | Automática por Hibernate |
| Configuración | `hibernate.hbm2ddl.auto = update` |
| Scripts | No hay scripts SQL iniciales |

### 13.6 Categorías

| Decisión | Descripción |
|----------|-------------|
| Gestión | **CRUD por usuario** |
| Precarga | No se precargan datos |
| Requisito | Usuario crea categorías antes de registrar gastos/inversiones |

---

## 14. Glosario de Términos

| Término | Definición |
|---------|------------|
| ABML | Alta, Baja, Modificación, Listado (equivalente a CRUD) |
| CRUD | Create, Read, Update, Delete |
| DTO | Data Transfer Object - Objeto para transferir datos entre capas |
| Entity | Clase que representa una tabla en BD |
| FK | Foreign Key - Clave foránea |
| JPA | Java Persistence API - API de persistencia Java |
| LAZY | Tipo de carga diferida de Hibernate |
| MVC | Model-View-Controller - Patrón arquitectónico |
| PK | Primary Key - Clave primaria |
| Soft Delete | Eliminación lógica (marcar activo=false) |

---

## 15. Versiones del Documento

| Versión | Fecha | Descripción |
|---------|-------|-------------|
| 1.0 | 23/02/2026 | Creación inicial del documento |
| | | - Descripción del proyecto |
| | | - Arquitectura MVC |
| | | - Entidades del sistema |
| | | - Relaciones entre entidades |
| | | - Estructura de paquetes |
| 1.1 | 23/02/2026 | Agregado sección Reglas de Negocio |
| | | - Reglas generales del sistema |
| | | - Reglas por entidad (Usuario, CuentaFinanciera, Gasto, etc.) |
| | | - Reglas transaccionales obligatorias |
| | | - Validaciones obligatorias |
| | | - Principios de implementación |
| 1.2 | 23/02/2026 | Actualizada estructura de paquetes |
| | | - Namespace actualizado a com.finanzasapp |
| | | - Agregados paquetes: validator, security |
| | | - Reglas de arquitectura detalladas |
| | | - Dependencias permitidas por paquete |
| | | - Objetivos de diseño (escalabilidad, desacoplamiento) |
| 1.3 | 23/02/2026 | Corrección de errores en documentación |
| | | - Corregida numeración de secciones (6.x → 7.x) |
| | | - Agregada nota sobre relación Movimiento → Usuario |
| | | - Corregido error tipográfico en sección 11.2 |
| 1.4 | 23/02/2026 | Decisiones arquitectónicas definitivas |
| | | - Views usan SOLO DTOs (no Entities) |
| | | - Repository: interfaz + impl/ |
| | | - JPA: persistence.xml + JpaConfig.java |
| | | - Java 17+ (no obligatorio 22) |
| | | - BD: Hibernate hbm2ddl=update (sin scripts) |
| | | - Categorías: CRUD por usuario (sin precarga) |
| 1.5 | 23/02/2026 | Actualización dependencias pom.xml |
| | | - JavaFX 21.0.1 |
| | | - mssql-jdbc 12.6.1.jre11 |
| | | - Agregadas: HikariCP, Hibernate Validator, Logback |
| | | - Jakarta Persistence 3.1.0 |

---

*Documento generado para el Trabajo Final de Ingeniería de Software*
