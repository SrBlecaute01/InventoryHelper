<h1 align="center">InventoryHelper</h1>

<p align="center">
    <a target="_blank" href="https://jitpack.io/#SrBlecaute01/InventoryHelper" alt="Downloads">
        <img src=https://img.shields.io/jitpack/v/github/SrBlecaute01/InventoryHelper?label=Snapshots&color=lime_green/>
    </a>
    <a target="_blank" href="https://github.com/SrBlecaute01/InventoryHelper/releases" alt="Downloads">
        <img src=https://img.shields.io/github/downloads/SrBlecaute01/InventoryHelper/total?color=lime_green/>
    </a>
    <a target="_blank" href="https://app.codacy.com/gh/SrBlecaute01/InventoryHelper/dashboard" alt="Codacy">
        <img src="https://app.codacy.com/project/badge/Grade/7062d74db06d4935a5e69a7a539e4116"/>
    </a>
</p>

<p align="center">A helper for creating inventories in Minecraft with greater ease, efficiency and flexibility.</p>

## Some features

-  Creation of simple or paginated inventories.
-  Possibility to create multiple patterns in the same inventory.
-  Properties for each inventory.
-  Integration with objects.
-  Inventory update methods.

## Summary

For a better understanding of the features this project offers, please check the [documentation](https://github.com/SrBlecaute01/InventoryHelper/wiki).

-  [Introduction](https://github.com/SrBlecaute01/InventoryHelper#enabling-inventoryhelper)
-  [Simple inventories](https://github.com/SrBlecaute01/InventoryHelper#creating-simple-inventory)
-  [Paginated inventories](https://github.com/SrBlecaute01/InventoryHelper#creating-paginated-inventory)
-  [Download](https://github.com/SrBlecaute01/InventoryHelper#download)

## Enabling InventoryHelper

Before creating inventories with InventoryHelper, it must be activated. Its activation can be done as follows:

```java
InventoryHelper.enable(plugin)
```

You can and should use this method when your plugin starts, like the following example:

```java
public class MainClass extends JavaPlugin {

    @Override
    public void onEnable() {
        InventoryHelper.enable(this);
    }
}
```
## Creating simple inventory

After starting InventoryHelper you can start building your inventories. All inventories are built from [InventoryBuilder](https://github.com/SrBlecaute01/InventoryHelper/blob/master/src/main/java/br/com/blecaute/inventory/InventoryBuilder.java). It has a range of methods for placing items or objects depending on their use.

```java
new InventoryBuilder(title, lines);
```

You can also use inventory configuration to get started with InventoryBuilder. It can provide a better amount of resources for creating the inventory.

```java
InventoryConfiguration configuration = InventoryConfiguration.builder(title, lines).build();
```
After instantiating the inventory configuration you can use it as follows:

```java
new InventoryBuilder(configuration);
```
**NOTE**: You can also use the static methods like ``InventoryBuilder.of(title, lines)`` or ``InventoryBuilder.of(configuration)`` for the builder creation.

After that, you can put the items in your inventory:

```java
Inventory inventory = InventoryBuilder.of("Simple inventory", 3)
    .withItem(13, ItemUtil.getItem(Material.REDSTONE, "§cClick me"), click -> {
        Player player = click.getPlayer();
        player.sendMessage("§cHi " + player.getName());
    }).build();
```

**NOTE**: You can directly open the inventory for the player with the ``build(player...)`` method. This method will build the inventory and automatically open it for the player.

## Creating paginated inventory

Paginated inventories are a convenient way to show various items to players. They can be easily created using InventoryBuilder. 

Paginated inventories work with PaginatedConfiguration. In these settings it is possible to define the buttons, amount of objects on each page and among others.
```java
PaginatedConfiguration paginatedConfiguration = PaginatedConfiguration.builder("identifier")
    .size(15) // Size of objets in each page.
    .start(11) // The slot to start placing of items. 
    .end(34) // The slot to stop placing of items.
    .validator(slot -> (slot > 15 && slot < 20) || (slot > 24 && slot < 29)) // Skip the slots that meet this requirement.
    .button(Button.of(ButtonType.PREVIOUS_PAGE, 48, previousButtonItem)) // Set the button to go back to the page.
    .button(Button.of(ButtonType.NEXT_PAGE, 50, nextButtonItem)) //Set the button to pass the page.
    .build(); // build configuration

InventoryBuilder.of(inventoryConfiguration)
    .withItems(paginatedConfiguration, items, click -> {
        Material type = click.getItemStack().getType();
        click.getPlayer().sendMessage("You clicked in " + type.name());
    }).build(player);
```

<p align="center">
    In the end you will get something like this:
    <br>
    <a href="" alt="Paginated"><img src="https://imgur.com/rUrZtaa.gif"></a>
</p>

## Download

<a target="_blank" href="https://jitpack.io/#SrBlecaute01/InventoryHelper" alt="Downloads">
    <img src=https://img.shields.io/jitpack/v/github/SrBlecaute01/InventoryHelper?label=Snapshots&color=blue/>
</a>
<a target="_blank" href="https://github.com/SrBlecaute01/InventoryHelper/releases" alt="Downloads">
    <img src=https://img.shields.io/github/downloads/SrBlecaute01/InventoryHelper/total?color=blue/>
</a>
<p>
    <br>
    You can see the latest release <a target="_blank" alt="latest" href="https://github.com/SrBlecaute01/InventoryHelper/releases/latest">here</a>
    <br>
</p>

**Maven**
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.SrBlecaute01</groupId>
        <artifactId>InventoryHelper</artifactId>
        <version>1.5.0</version>
    </dependency>
</dependencies>
```

**Gradle**
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.SrBlecaute01:InventoryHelper:1.5.0'
}
```
