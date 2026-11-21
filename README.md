# App_Achados_Perdidos

Estrutura de pastas do app Android (Kotlin + Jetpack Compose).
Pacote base: `br.edu.ifpb.app_achados_perdidos`

## Estrutura

```
App_Achados_Perdidos/
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/libs.versions.toml          ← catálogo de versões das dependências
├── README.md
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml        ← permissões e cleartext
        ├── res/
        │   ├── drawable/              ← placeholder e imagem de erro (Coil)
        │   ├── values/                ← strings.xml, colors, themes
        │   └── xml/network_security_config.xml
        └── java/br/edu/ifpb/app_achados_perdidos/
            │
            ├── MainActivity.kt        ← só hospeda a navegação
            │
            ├── data/
            │   ├── remote/
            │   │   ├── AchadosApi.kt          ← interface Retrofit (itens, upload, geocode)
            │   │   └── RetrofitClient.kt      ← instância do Retrofit, BASE_URL
            │   ├── dto/
            │   │   ├── ItemRequest.kt
            │   │   ├── ItemResponse.kt
            │   │   ├── UploadResponse.kt
            │   │   └── GeocodeResponse.kt
            │   ├── mapper/
            │   │   └── ItemMapper.kt          ← DTO ⇄ modelo de domínio
            │   └── repository/
            │       └──