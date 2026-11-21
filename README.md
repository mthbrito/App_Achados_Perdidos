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
            │       └── ItemRepository.kt      ← único ponto que chama a API
            │
            ├── domain/
            │   └── model/
            │       ├── Item.kt
            │       ├── TipoItem.kt            ← PERDIDO, ENCONTRADO
            │       └── StatusItem.kt          ← ATIVO, DEVOLVIDO
            │
            ├── ui/
            │   ├── navigation/
            │   │   └── NavGraph.kt            ← rotas e telas
            │   ├── theme/                     ← Color, Theme, Type (gerado)
            │   ├── components/
            │   │   ├── ItemCard.kt            ← card da lista (Coil)
            │   │   ├── MiniMapa.kt            ← osmdroid
            │   │   └── ImagemItem.kt          ← AsyncImage com placeholder/erro
            │   ├── home/
            │   │   ├── HomeScreen.kt
            │   │   └── HomeViewModel.kt
            │   ├── detalhes/
            │   │   ├── DetalhesScreen.kt
            │   │   └── DetalhesViewModel.kt
            │   └── formulario/
            │       ├── FormularioScreen.kt    ← criar e editar
            │       └── FormularioViewModel.kt
            │
            └── util/
                ├── Resultado.kt               ← Sucesso / Erro / Carregando
                └── ArquivoUtil.kt             ← Uri → MultipartBody.Part
```

## Responsabilidade de cada camada

| Pasta | Função |
|---|---|
| **data/remote** | Retrofit: interface com GET, POST, PUT, PATCH e DELETE |
| **data/dto** | Objetos que espelham o JSON da API (mesmos campos do `ItemRequest`/`ItemResponse`) |
| **data/mapper** | Converte DTO em modelo do app, no mesmo padrão do `ItemMapper` da API |
| **data/repository** | Chama o Retrofit e trata erros; é o que o ViewModel usa |
| **domain/model** | Modelos usados pelas telas, sem dependência de rede |
| **ui/** | Telas (Compose) e ViewModels, uma subpasta por tela |
| **ui/components** | Peças reutilizáveis (card, mapa, imagem com Coil) |
| **util** | Funções de apoio |

Fluxo: **Tela → ViewModel → Repository → Retrofit → API**.
A tela nunca fala direto com o Retrofit.

## Onde entra cada tecnologia

| Tecnologia | Onde |
|---|---|
| **Retrofit** | `data/remote` |
| **Coil** | `ui/components/ImagemItem.kt` e `ItemCard.kt` |
| **osmdroid** | `ui/components/MiniMapa.kt` |
| **ViewModel + StateFlow** | `ui/*/…ViewModel.kt` |
| **Navigation Compose** | `ui/navigation/NavGraph.kt` |

## Ordem sugerida de criação

1. `domain/model` e `data/dto`
2. `data/remote` (Retrofit) e `data/repository`
3. `ui/home` (lista com Coil)
4. `ui/detalhes` e `ui/formulario`
5. `ui/components/MiniMapa` (osmdroid)