<h1 align="center">InventoryHelper</h1>

<p align="center">
    <a href="https://jitpack.io/#SrBlecaute01/InventoryHelper" alt="Downloads">
        <img src=https://img.shields.io/jitpack/v/github/SrBlecaute01/InventoryHelper?label=Snapshots&color=lime_green/>
    </a>
<<<<<<< Updated upstream
    <a href="" alt="Downloads">
        <img src=https://img.shields.io/github/downloads/SrBlecaute01/InventoryHelper/total?color=lime_green/>
    </a>
    <a href="" alt="Codacy">
=======
    <a href="https://github.com/SrBlecaute01/InventoryHelper/releases" alt="Downloads">
        <img src=https://img.shields.io/github/downloads/SrBlecaute01/InventoryHelper/total?color=lime_green/>
    </a>
    <a href="https://app.codacy.com/project/badge/Grade/7062d74db06d4935a5e69a7a539e4116" alt="Codacy">
>>>>>>> Stashed changes
        <img src="https://app.codacy.com/project/badge/Grade/7062d74db06d4935a5e69a7a539e4116"/>
    </a>
</p>

<<<<<<< Updated upstream
Um auxiliar para criação de inventários no minecraft com maior facilidade 
e flexibilidade  afim de promover integração com objetos.
=======
<p align="center">
    Um auxiliar para criação de inventários no minecraft com maior facilidade 
    e flexibilidade  afim de promover integração com objetos.
</p>
>>>>>>> Stashed changes

## Recursos

- Criação de inventários paginados.
- Flexbilidade com objetos.
- Ações individuais para cada item.
- Propiedades para cada inventário.

## Download
![jitpack](https://img.shields.io/jitpack/v/github/SrBlecaute01/InventoryHelper?label=Snapshots&color=lime_green)
![Downloads](https://img.shields.io/github/downloads/SrBlecaute01/InventoryHelper/total?color=lime_green)

Versão mais recente: [Release](https://github.com/SrBlecaute01/InventoryHelper/releases/latest)

**Maven**
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
<dependencies>
    <dependency>
        <groupId>com.github.SrBlecaute01</groupId>
        <artifactId>InventoryHelper</artifactId>
        <version>${VERSION}</version>
    </dependency>
</dependencies>
```

**Gradle**
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.SrBlecaute01:InventoryHelper:1.0'
}
```

## Começando

Após adicionar o repositório ao seu projeto com maven ou gradle
o InventoryHelper precisa ser ativado para o registro dos eventos.

```java
public class SimpleInventoryExample extends JavaPlugin {

    @Override
    public void onEnable() {
        // enable inventory helper
        InventoryHelper.enable(this);
    }
}
```

Após isso o InventoryBuilder já pode ser utilizado. Caso o helper não seja iniciado corretamente,
ao iniciar uma instância do InventoryBuilder ele lançará o [InventoryBuilderException](https://github.com/SrBlecaute01/InventoryHelper/blob/master/src/main/java/br/com/blecaute/inventory/exception/InventoryBuilderException.java)
devido ao não registro do helper.

**Exemplo da construção de um inventário simples**

```java
public class SimpleInventoryExample extends JavaPlugin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new InventoryBuilder<>("Custom inventory", 3)
                .withItem(13, getItem(), click -> {
                    player.closeInventory();
                    player.sendMessage("§eYou clicked in item ;).");

                }).build(player);
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§eClick");

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
```
<<<<<<< Updated upstream
Você pode ver mais exemplos como esse clicando [aqui](https://github.com/SrBlecaute01/InventoryHelper/tree/master/src/examples/java).
=======
Você pode ver mais exemplos como esse clicando [aqui](https://github.com/SrBlecaute01/InventoryHelper/tree/master/src/examples).
>>>>>>> Stashed changes
