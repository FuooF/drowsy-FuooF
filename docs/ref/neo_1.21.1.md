# NeoForge 1.21.1 完整文档（中文总结）

> 本文档基于 [NeoForge 官方文档 1.21 - 1.21.1 版本](https://docs.neoforged.net/docs/1.21.1/gettingstarted/) 全面整理，涵盖全部章节内容。注意：该版本已不再维护，最新版本请参考官方最新文档。

---

## 目录

1. [入门指南 (Getting Started)](#1-入门指南-getting-started)
   - [环境搭建](#环境搭建)
   - [Mod 文件 (Mod Files)](#mod-文件)
   - [Mod 结构组织 (Structuring Your Mod)](#mod-结构组织)
   - [版本管理 (Versioning)](#版本管理)
2. [核心概念 (Concepts)](#2-核心概念-concepts)
   - [注册表 (Registries)](#注册表)
   - [逻辑端与物理端 (Sides)](#逻辑端与物理端)
   - [事件系统 (Events)](#事件系统)
3. [方块 (Blocks)](#3-方块-blocks)
   - [基本方块创建](#基本方块创建)
   - [方块状态 (Blockstates)](#方块状态)
   - [方块行为管线](#方块行为管线)
4. [物品 (Items)](#4-物品-items)
   - [创建物品](#创建物品)
   - [交互管线 (Interaction Pipeline)](#交互管线)
   - [数据组件 (Data Components)](#数据组件)
   - [工具与护甲 (Tools & Armor)](#工具与护甲)
   - [状态效果与药水 (Mob Effects & Potions)](#状态效果与药水)
5. [方块实体 (Block Entities)](#5-方块实体-block-entities)
   - [创建与注册](#创建与注册)
   - [数据存储](#数据存储)
   - [Ticker 系统](#ticker-系统)
   - [同步机制](#同步机制)
   - [方块实体渲染器 (BER/BEWLR)](#方块实体渲染器)
6. [资源 (Resources)](#6-资源-resources)
   - [客户端资源 (Assets)](#客户端资源)
     - [国际化 (I18n)](#国际化)
     - [模型 (Models)](#模型)
     - [纹理 (Textures)](#纹理)
     - [声音 (Sounds)](#声音)
     - [粒子 (Particles)](#粒子)
   - [服务端资源 (Data)](#服务端资源)
     - [进度 (Advancements)](#进度)
     - [数据加载条件 (Conditions)](#数据加载条件)
     - [伤害类型 (Damage Types)](#伤害类型)
   - [数据生成 (Data Generation)](#数据生成)
7. [物品栏与传输 (Inventories & Transfers)](#7-物品栏与传输)
   - [容器 (Containers)](#容器)
   - [能力系统 (Capabilities)](#能力系统)
8. [数据存储 (Data Storage)](#8-数据存储)
   - [NBT 格式](#nbt-格式)
   - [Codec 编解码系统](#codec-编解码系统)
   - [数据附件 (Data Attachments)](#数据附件)
   - [存档数据 (Saved Data)](#存档数据)
9. [GUI 系统](#9-gui-系统)
   - [菜单 (Menus)](#菜单)
   - [屏幕 (Screens)](#屏幕)
10. [世界生成 (Worldgen)](#10-世界生成)
    - [生物群系修改器 (Biome Modifiers)](#生物群系修改器)
11. [网络通信 (Networking)](#11-网络通信)
12. [高级主题 (Advanced Topics)](#12-高级主题)
    - [访问转换器 (Access Transformers)](#访问转换器)
13. [杂项 (Miscellaneous)](#13-杂项)
    - [配置系统 (Configuration)](#配置系统)
    - [其他](#其他)

---

## 1. 入门指南 (Getting Started)

### 环境搭建

**前置要求：**
- 熟悉 Java 编程语言（面向对象、多态、泛型、函数式特性）
- 安装 JDK 21 和 64 位 JVM（推荐 Microsoft OpenJDK 构建版本）
- 选择 IDE（官方支持 IntelliJ IDEA 和 Eclipse，也可使用 VSCode、Vim 等）
- 熟悉 Git 和 GitHub

**搭建步骤：**
1. 打开 MDK 仓库（[ModDevGradle](https://github.com/NeoForgeMDKs/MDK-1.21-ModDevGradle) 或 [NeoGradle](https://github.com/NeoForgeMDKs/MDK-1.21-NeoGradle)），点击 "Use this template" 并克隆
2. 使用 IDE 导入 Gradle 项目（首次会下载 Minecraft 和 NeoForge 依赖，可能耗时较长）
3. 修改 Gradle 文件后需重新加载项目

**构建与测试：**
- 运行 `gradlew build` 构建 mod，输出 JAR 在 `build/libs`
- 使用运行配置或 `gradlew runClient`/`gradlew runServer` 进行测试
- **服务端测试注意：**
  - 首次运行需接受 EULA（编辑 `eula.txt`）
  - 需将 `server.properties` 中 `online-mode` 设为 `false` 才能加入
  - **始终在专用服务器环境中测试你的 mod**，包括仅客户端 mod

### Mod 文件

#### `gradle.properties`

用于存放 mod 公共属性，Gradle 构建时会将这些值内联到各处。

| 属性 | 说明 | 示例 |
|------|------|------|
| `org.gradle.jvmargs` | Gradle 的 JVM 参数（非 Minecraft） | `org.gradle.jvmargs=-Xmx3G` |
| `minecraft_version` | Minecraft 版本 | `minecraft_version=1.20.6` |
| `minecraft_version_range` | Minecraft 版本范围（Maven 版本范围） | `[1.20.6,1.21)` |
| `neo_version` | NeoForge 版本 | `neo_version=20.6.62` |
| `neo_version_range` | NeoForge 版本范围 | `[20.6.62,20.7)` |
| `loader_version_range` | 加载器版本范围 | `[1,)` |
| `mod_id` | Mod ID（见下文） | `mod_id=examplemod` |
| `mod_name` | 显示名称 | `mod_name=Example Mod` |
| `mod_license` | 许可证 | `mod_license=MIT` |
| `mod_version` | Mod 版本 | `mod_version=1.0` |
| `mod_group_id` | 组 ID（见下文） | `mod_group_id=com.example.examplemod` |
| `mod_authors` | 作者 | `mod_authors=ExampleModder` |
| `mod_description` | 描述（支持 `\n`） | 多行描述字符串 |

**Mod ID：** 用于区分不同 mod，只能包含小写字母、数字和下划线，长度 2-64 字符。两个相同 ID 的 mod 会导致游戏无法加载。

**Group ID：** 应设为顶级包名，与 Java 包结构一致。

#### `neoforge.mods.toml`

位于 `src/main/resources/META-INF/neoforge.mods.toml`，定义 mod 元数据和加载方式。

**非 Mod 特定属性：**

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `modLoader` | string | **必填** | 语言加载器，如 `"javafml"` 或 `"lowcodefml"` |
| `loaderVersion` | string | **必填** | 加载器版本范围，当前为 `"[1,)"` |
| `license` | string | **必填** | 许可证 |
| `showAsResourcePack` | boolean | `false` | 是否作为独立资源包显示 |
| `showAsDataPack` | boolean | `false` | 是否作为独立数据包显示 |
| `services` | array | `[]` | 使用的服务列表 |
| `properties` | table | `{}` | 替换属性表 |
| `issueTrackerURL` | string | 无 | Issue 跟踪 URL |

**Mod 特定属性（`[[mods]]`）：**

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `modId` | string | **必填** | Mod ID |
| `namespace` | string | modId 值 | 命名空间覆盖 |
| `version` | string | `"1"` | 版本号 |
| `displayName` | string | modId 值 | 显示名称 |
| `description` | string | `MISSING DESCRIPTION` | 描述（支持翻译） |
| `logoFile` | string | 无 | Logo 图片文件名 |
| `logoBlur` | boolean | `true` | Logo 是否模糊 |
| `updateJSONURL` | string | 无 | 更新检查 JSON URL |
| `credits` | string | 无 | 致谢 |
| `authors` | string | 无 | 作者 |
| `displayURL` | string | 无 | 显示页面 URL |
| `enumExtensions` | string | 无 | 枚举扩展 JSON 路径 |

**Features 系统：** 使用 `[[features.<modid>]]` 要求特定软件/硬件条件：
- `javaVersion`：Java 版本范围
- `openGLVersion`：OpenGL 版本范围

**依赖配置（`[[dependencies.<modid>]]`）：**

| 属性 | 说明 |
|------|------|
| `modId` | 依赖的 mod ID |
| `type` | `required`/`optional`/`incompatible`/`discouraged` |
| `reason` | 用户可见的依赖说明 |
| `versionRange` | Maven 版本范围 |
| `ordering` | `NONE`/`BEFORE`/`AFTER`（加载顺序，注意循环依赖） |
| `side` | `BOTH`/`CLIENT`/`SERVER` |
| `referralUrl` | 依赖下载链接 |

**访问转换器配置（`[[accessTransformers]]`）：** 指定 AT 文件路径。

**Mixin 配置（`[[mixins]]`）：** 指定 Mixin 配置文件路径。

#### Mod 入口点

**`javafml` + `@Mod`：** Java 语言加载器，使用 `@Mod` 注解定义入口点类。

```java
@Mod("examplemod")
public class ExampleMod {
    public ExampleMod(IEventBus modBus, ModContainer container) {
        // 初始化逻辑
    }
}
```

构造参数可选类型：`IEventBus`（mod 事件总线）、`ModContainer`、`FMLModContainer`、`Dist`（物理端）。

使用 `dist` 参数可限制加载端：

```java
@Mod(value = "examplemod", dist = Dist.CLIENT)
public class ExampleModClient { ... }
```

**`lowcodefml`：** 无需代码入口点，用于分发纯数据包/资源包的 mod。

### Mod 结构组织

**包命名：**
- 顶级包应为唯一标识（域名、邮箱等），如 `com.example`
- 下一级为 mod id，如 `com.example.examplemod`
- 确保包名唯一，避免模块冲突

**子包组织：**
- **按功能分组：** `block`、`item`、`entity` 等
- **按逻辑分组：** 将相关功能的所有类放在一起，如 `feature.crafting_table`
- 客户端代码放 `client` 子包，数据生成放 `data` 子包
- 仅服务端代码放 `server` 子包

**类命名规范：**
- 使用后缀标识类型：`PowerRingItem`、`NotDirtBlock`、`OvenMenu`
- 选择一种方法并保持一致

### 版本管理

**Minecraft 版本：** 使用语义化版本（Semver）`major.minor.patch`，如 1.20.2。快照使用 `YYwWWa` 格式（如 `23w01a`）。预发布版使用 `-preX` 后缀，候选发布版使用 `-rcX` 后缀。

**NeoForge 版本：** 改编的 Semver 系统：
- 主版本 = Minecraft 次版本号
- 次版本 = Minecraft 补丁版本号
- 补丁版本 = NeoForge 自身版本
- 如 NeoForge 20.2.59 表示 Minecraft 1.20.2 的第 60 个版本

**Mod 版本策略：**
- **语义化版本：** `major.minor.patch`（如 Supplementaries 2.6.31）
- **缩减版（2段）：** `major.minor`（小 mod 常用）
- **扩展版（4段）：** `major.api.minor.patch`（如 Mekanism 10.4.5.19）或 `major.minor.patch.hotfix`
- **Alpha/Beta/Release：** 经典阶段标记
- **包含 Minecraft 版本：** 如 `jei-1.20.2-16.0.0.28`
- **包含 Mod 加载器：** 如 `jei-neoforge-1.20.2-16.0.0.28`
- 版本号需兼容 Maven 版本范围

---

## 2. 核心概念 (Concepts)

### 注册表

**什么是注册表：** 注册表是将 mod 对象（方块、物品、实体等）告知游戏的过程。注册表本质上是映射 ID（`ResourceLocation`）到注册对象（单例）的包装。

**注册方法：**

1. **`DeferredRegister`（推荐）：**

```java
public static final DeferredRegister<Block> BLOCKS = 
    DeferredRegister.create(BuiltInRegistries.BLOCK, ExampleMod.MOD_ID);

public static final DeferredHolder<Block, Block> EXAMPLE_BLOCK = 
    BLOCKS.register("example_block", () -> new Block(...));

// 在 Mod 构造器中注册
public ExampleMod(IEventBus modBus) {
    BLOCKS.register(modBus);
}
```

`DeferredHolder<R, T>` 是 `Supplier<T>` 的子类，通过 `#get()` 获取实际对象。

2. **`RegisterEvent`：**

```java
@SubscribeEvent
public static void register(RegisterEvent event) {
    event.register(BuiltInRegistries.BLOCK, registry -> {
        registry.register(
            ResourceLocation.fromNamespaceAndPath(MODID, "example_block"),
            new Block(...)
        );
    });
}
```

**查询注册表：**
- `BuiltInRegistries.BLOCK.get(ResourceLocation)` - 按 ID 获取对象
- `BuiltInRegistries.BLOCK.getKey(block)` - 获取对象的 ID
- `BuiltInRegistries.BLOCK.containsKey(ResourceLocation)` - 检查是否存在
- **注意：只在注册完成后查询！**

**自定义注册表：**

```java
public static final ResourceKey<Registry<Spell>> SPELL_REGISTRY_KEY = 
    ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("yourmodid", "spells"));

public static final Registry<Spell> SPELL_REGISTRY = 
    new RegistryBuilder<>(SPELL_REGISTRY_KEY)
        .sync(true)
        .defaultKey(ResourceLocation.fromNamespaceAndPath("yourmodid", "empty"))
        .maxId(256)
        .create();

// 在 NewRegistryEvent 中注册
@SubscribeEvent
public static void registerRegistries(NewRegistryEvent event) {
    event.register(SPELL_REGISTRY);
}
```

**数据包注册表（Datapack Registry）：**
- 在关卡加载时从数据包 JSON 加载（而非启动时）
- 世界生成注册表（`worldgen/biome`）、`neoforge/biome_modifier` 等
- 通过 `RegistryAccess` 获取（`ServerLevel#registryAccess()` 或 `Minecraft.getInstance().getConnection()#registryAccess()`）

**自定义数据包注册表：**

```java
@SubscribeEvent
public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
    event.dataPackRegistry(SPELL_REGISTRY_KEY, Spell.CODEC, Spell.CODEC);
}
```

**数据包注册表的数据生成：** 使用 `RegistrySetBuilder` 和 `DatapackBuiltinEntriesProvider`。

### 逻辑端与物理端

**物理端（Physical Side）：**
- **物理客户端：** 启动 Minecraft 启动器进入游戏，包含所有渲染功能
- **物理服务端：** 专用服务器 JAR，缺少客户端类（访问会崩溃）

**逻辑端（Logical Side）：**
- **逻辑服务端：** 游戏逻辑运行的地方（时间、实体 tick、数据管理）
- **逻辑客户端：** 负责显示渲染，代码在 `net.minecraft.client` 包中，单独渲染线程

**关键区别：**
- 多人游戏：物理客户端连接物理服务端
- 单人游戏：物理客户端内启动逻辑服务端，逻辑客户端连接本地逻辑服务端

**执行端特定操作：**
- `Level#isClientSide()`：检查**逻辑端**（最常用）
- `FMLEnvironment.dist`：检查**物理端**（`Dist.CLIENT` / `Dist.DEDICATED_SERVER`）
- `@Mod(dist = Dist.CLIENT)`：限制 mod 类加载端

**重要：始终用专用服务器测试 mod！** `NoClassDefFoundError` 和 `ClassNotFoundException` 是最常见的错误。

### 事件系统

**事件总线：**
- `NeoForge.EVENT_BUS`（游戏总线）：主要事件总线
- mod 事件总线：每个 mod 启动时创建，传入 mod 构造器

**注册事件处理器：**

1. **`IEventBus#addListener`：**
```java
NeoForge.EVENT_BUS.addListener(YourMod::onLivingJump);
```

2. **`@SubscribeEvent`：**
```java
public class EventHandler {
    @SubscribeEvent
    public static void onLivingJump(LivingJumpEvent event) { ... }
}
NeoForge.EVENT_BUS.register(EventHandler.class);
```

3. **`@EventBusSubscriber`（需 1.21.1.180+）：**
```java
@EventBusSubscriber(modid = "yourmodid")
public class EventHandler {
    @SubscribeEvent
    public static void onLivingJump(LivingJumpEvent event) { ... }
}
```

**事件选项：**
- **可取消事件：** 实现 `ICancellableEvent`，使用 `#setCanceled`/`#isCanceled`
- **三态/结果：** `TriState`（FALSE/TRUE/DEFAULT）或 `Result` 枚举
- **优先级：** `HIGHEST`、`HIGH`、`NORMAL`、`LOW`、`LOWEST`
- **端特定事件：** 通过 `@EventBusSubscriber(value = Dist.CLIENT)` 或 `FMLEnvironment.dist` 判断

**Mod 生命周期事件顺序：**
1. Mod 构造器调用
2. `@EventBusSubscriber` 调用
3. `FMLConstructModEvent` 触发
4. 注册事件（`NewRegistryEvent`、`DataPackRegistryEvent.NewRegistry`、`RegisterEvent`）
5. `FMLCommonSetupEvent` 触发
6. 端特定设置（`FMLClientSetupEvent`/`FMLDedicatedServerSetupEvent`）
7. `InterModComms` 处理
8. `FMLLoadCompleteEvent` 触发

**InterModComms：** 允许 mod 间发送消息实现兼容性。`InterModEnqueueEvent` 期间发送，`InterModProcessEvent` 期间接收。

---

## 3. 方块 (Blocks)

### 基本方块创建

**核心概念：** 游戏中每个方块只有一个实例（单例），世界由对该方块的引用组成。

**使用 `DeferredRegister.Blocks`：**

```java
public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("yourmodid");
public static final DeferredBlock<Block> MY_BLOCK = BLOCKS.register("my_block", () -> new Block(...));
```

**BlockBehaviour.Properties 关键方法：**

| 方法 | 说明 | 参考值 |
|------|------|--------|
| `destroyTime` | 破坏时间 | 石头 1.5, 泥土 0.5, 黑曜石 50, 基岩 -1 |
| `explosionResistance` | 爆炸抗性 | 石头 6.0, 黑曜石 1200, 基岩 3600000 |
| `sound` | 音效类型 | 默认 `SoundType.STONE` |
| `lightLevel` | 光照等级 (0-15) | 萤石 `state -> 15` |
| `friction` | 摩擦力 | 默认 0.6, 冰 0.98 |

**注意：** `BlockItem` 需要单独注册。方块是世界的物理存在，物品栏中的是 `BlockItem`。

### 方块状态

**Blockstate 属性类型：**
- `IntegerProperty`：整数值（不支持负数）
- `BooleanProperty`：布尔值
- `EnumProperty<E>`：枚举值
- `DirectionProperty`：方向（`Direction`）

**实现步骤：**

```java
public class MyBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    
    public MyBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(POWERED, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 根据放置上下文返回合适的 blockstate
    }
}
```

**使用 Blockstate：**
- `Block#defaultBlockState()` 获取默认状态
- `BlockState#getValue(Property)` 获取属性值
- `BlockState#setValue(Property, value)` 设置属性值（**BlockState 不可变**）
- `Level#getBlockState(BlockPos)` 从世界获取
- `Level#setBlock(BlockPos, BlockState, int flags)` 设置方块

**`Level#setBlock` 更新标志（可位或组合）：**
- `UPDATE_NEIGHBORS`：更新相邻方块
- `UPDATE_CLIENTS`：同步到客户端
- `UPDATE_INVISIBLE`：不同步到客户端（覆盖 `UPDATE_CLIENTS`）
- `UPDATE_IMMEDIATE`：客户端主线程强制重新渲染
- `UPDATE_KNOWN_SHAPE`：停止邻居更新递归
- `UPDATE_SUPPRESS_DROPS`：禁止旧方块掉落
- `UPDATE_ALL` = `UPDATE_NEIGHBORS | UPDATE_CLIENTS`
- `UPDATE_ALL_IMMEDIATE` = `UPDATE_NEIGHBORS | UPDATE_CLIENTS | UPDATE_IMMEDIATE`

**何时用 Blockstate vs BlockEntity：** 有限状态（几百以内）用 blockstate；无限/近乎无限状态用 block entity。

### 方块行为管线

**方块放置流程：**
1. 前提检查（非旁观模式、功能标志、世界边界等）
2. `BlockBehaviour#canBeReplaced`（当前位置方块是否可替换）
3. `Block#getStateForPlacement`（根据上下文返回不同状态）
4. `BlockBehaviour#canSurvive`（是否能存活）
5. `Level#setBlock` 设置方块（触发 `BlockBehaviour#onPlace`）
6. `Block#setPlacedBy` 调用

**方块破坏流程：**
- **"开始"阶段：** 触发 `PlayerInteractEvent.LeftClickBlock`，调用 `Block#attack`
- **"挖掘中"阶段（每 tick）：** 调用 `Block#getDestroyProgress`，更新裂纹覆盖层
- **"实际破坏"阶段：**
  1. `Item#canAttackBlock` 检查
  2. `Player#blockActionRestricted` 检查
  3. `BlockEvent.BreakEvent` 触发
  4. `IBlockExtension#canHarvestBlock` 检查
  5. `IBlockExtension#onDestroyedByPlayer`（调用 `Block#playerWillDestroy`、`Block#onRemove`）
  6. `Block#destroy`
  7. `Block#playerDestroy` / `Block#dropResources`
  8. `BlockDropsEvent` 触发
  9. `Block#popExperience`

**Tick 类型：**
- **服务端 Tick：** `BlockBehaviour#tick`，通过 `Level#scheduleTick` 调度
- **客户端 Tick：** `Block#animateTick`，每帧调用
- **天气 Tick：** `Block#handlePrecipitation`，仅服务端，下雨时有 1/16 概率
- **随机 Tick：** 需 `BlockBehaviour.Properties#randomTicks()` 启用，由 `randomTickSpeed` 游戏规则控制

---

## 4. 物品 (Items)

### 创建物品

**物品的本质：** Item（单例模板） -> ItemStack（带数据的实例）。Item 持有默认数据组件，ItemStack 可修改这些组件。

**创建基本物品：**

```java
public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExampleMod.MOD_ID);
public static final Supplier<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties());
```

**Item.Properties 方法：**
- `stacksTo`：最大堆叠数（默认 64）
- `durability`：耐久度（默认 0=无耐久，设置后自动锁定堆叠为 1）
- `craftRemainder`：合成剩余物
- `fireResistant`：防火
- `setNoRepair`：禁用铁砧修复
- `rarity`：稀有度（`COMMON`/`UNCOMMON`/`RARE`/`EPIC`）
- `food`：食物属性

**食物属性（FoodProperties）：**
- `nutrition`：饥饿值恢复（半饥饿点计数）
- `saturationMod`：饱和度修饰符
- `alwaysEdible`：满饥饿值仍可食用
- `fast`：快速食用
- `effect`：食用时施加效果
- `usingConvertsTo`：使用后转换物品

**`DeferredRegister.Items` 辅助方法：**
- `registerItem(name, factory, properties)`：注册物品
- `registerSimpleItem(name, properties)`：使用 `Item::new` 注册
- `registerSimpleBlockItem(name, block, properties)`：注册方块物品

**ItemStack：**
- `getItem()`：获取物品类型
- `getCount()`/`setCount()`/`shrink()`：堆叠数量
- `getComponents()`：获取数据组件
- `ItemStack.EMPTY`：空堆叠
- **ItemStack 是可变的！** 需要时使用 `#copy()` 克隆

**JSON 表示：**
```json
{
    "id": "minecraft:dirt",
    "count": 4,
    "components": { "minecraft:enchantment_glint_override": true }
}
```

**创造模式标签页：**
- 添加到已有标签页：监听 `BuildCreativeModeTabContentsEvent`
- 自定义标签页：注册 `CreativeModeTab`

### 交互管线

**右键点击流程：**
1. `InputEvent.InteractionKeyMappingTriggered` 触发
2. 非旁观/功能标志等检查
3. **如果看向实体：**
   - `PlayerInteractEvent.EntityInteractSpecific`
   - `Entity#interactAt`
   - `PlayerInteractEvent.EntityInteract`
   - `Entity#interact`
   - `Item#interactLivingEntity`（如果实体是 `LivingEntity`）
4. **如果看向方块：**
   - `PlayerInteractEvent.RightClickBlock`
   - `IItemExtension#onItemUseFirst`
   - `UseItemOnBlockEvent` -> `Block#useItemOn`
   - `Block#useWithoutItem`（仅主手）
   - `Item#useOn`
5. `Item#use`
6. 以上流程对副手重复执行

**返回类型：**
- `InteractionResult`：`SUCCESS`/`CONSUME`/`CONSUME_PARTIAL`/`PASS`/`FAIL`
- `ItemInteractionResult`：额外有 `PASS_TO_DEFAULT_BLOCK_INTERACTION`/`SKIP_DEFAULT_BLOCK_INTERACTION`
- `InteractionResultHolder<T>`：包装 `InteractionResult` + 额外上下文
- `#sidedSuccess()`：服务端返回 `SUCCESS`，客户端返回 `CONSUME`
- `SUCCESS`：成功，挥动手臂
- `CONSUME`：成功，不挥动手臂
- `FAIL`：失败，终止管线
- `PASS`：继续管线（默认）

### 数据组件

**DataComponentType：** 键值对映射，存储 `ItemStack` 上的数据。组件值必须实现 `hashCode` 和 `equals`，建议使用 record。

**创建自定义数据组件：**

```java
public static final DeferredRegister.DataComponents REGISTRAR = 
    DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "examplemod");

public static final DeferredHolder<DataComponentType<?>, DataComponentType<ExampleRecord>> BASIC_EXAMPLE = 
    REGISTRAR.registerComponentType("basic", builder -> builder
        .persistent(BASIC_CODEC)
        .networkSynchronized(BASIC_STREAM_CODEC));
```

Builder 三个设置：
- `persistent`：磁盘读写 Codec
- `networkSynchronized`：网络传输 StreamCodec（若未指定则包装 Codec）
- `cacheEncoding`：缓存编码结果
- **至少需要 `persistent` 或 `networkSynchronized` 之一**

**组件映射操作：**
- `DataComponentMap`：只读，`#get`/`#getOrDefault`
- `PatchedDataComponentMap`：可写，`#set`/`#remove`/`#update`
- 使用原型（默认值）+ 补丁映射模式

**DataComponentHolder 接口：**
- `DataComponentHolder`：只读方法代理
- `MutableDataComponentHolder`：可写方法，提供 `#set`/`#remove`/`#update`

**为物品添加默认组件：**
- `Item.Properties#component(type, value)`
- `ModifyDefaultComponentsEvent`（对原版/其他 mod 物品）

### 工具与护甲

**自定义工具套装：**

```java
public static final Tier COPPER_TIER = new SimpleTier(
    MyBlockTags.INCORRECT_FOR_COPPER_TOOL, // 不正确挖掘标签
    200,  // 耐久度
    5f,   // 挖掘速度
    1.5f, // 攻击伤害加成
    20,   // 附魔性
    () -> Ingredient.of(Tags.Items.INGOTS_COPPER) // 修复材料
);
```

**工具类层次结构：**
- `TieredItem` -> `DiggerItem`（`AxeItem`/`HoeItem`/`PickaxeItem`/`ShovelItem`）、`SwordItem`

**挖掘等级标签系统：**
- `needs_<material>_tool`：需要此工具才能采集
- `incorrect_for_<material>_tool`：使用错误工具不掉落

**自定义工具（`Tool` 数据组件）：**
- 包含 `Tool.Rule` 列表（匹配方块集合、挖掘速度、是否正确掉落）
- 设置 `DataComponents#TOOL`
- 覆写 `IItemExtension#canPerformAction`

**ItemAbility：** 抽象物品能力（挖掘、斧子剥皮/刮锈/去蜡、剪刀收集/雕刻、铲子压平、剑横扫、锄耕、盾格挡、钓鱼竿抛投）。使用 `ItemAbility#get` 创建自定义能力。

**护甲：**

```java
public static final Holder<ArmorMaterial> COPPER_ARMOR_MATERIAL = 
    ARMOR_MATERIALS.register("copper", () -> new ArmorMaterial(
        defenseMap,  // 各部位防御值
        20,          // 附魔性
        SoundEvents.ARMOR_EQUIP_GENERIC,
        () -> Ingredient.of(Tags.Items.INGOTS_COPPER),
        List.of(new ArmorMaterial.Layer(...)), // 纹理层
        0, // 韧性
        0  // 击退抗性
    ));
```

### 状态效果与药水

**术语：**
- `MobEffect`：状态效果（单例，注册对象）
- `MobEffectInstance`：效果实例（含持续时间、放大器等属性）
- `Potion`：效果实例集合
- 药水物品：药水瓶、喷溅药水、滞留药水、药箭

**创建 MobEffect：**

```java
public class MyMobEffect extends MobEffect {
    public MyMobEffect(MobEffectCategory category, int color) { super(category, color); }
    
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) { return true; }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return tickCount % 2 == 0;
    }
}
```

**InstantenousMobEffect：** 应用于一 tick 的即时效果（如瞬间治疗/伤害）。

**MobEffect 事件：**
- `MobEffectEvent.Applicable`：检查效果是否可应用
- `MobEffectEvent.Added`：效果添加后触发
- `MobEffectEvent.Expired`：效果过期触发
- `MobEffectEvent.Remove`：效果被移除触发（非过期方式）

**药水注册与酿造：**

```java
@SubscribeEvent
public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
    event.getBuilder().addMix(Potions.AWKWARD, Items.FEATHER, MY_POTION);
}
```

---

## 5. 方块实体 (Block Entities)

### 创建与注册

**Block Entity vs Block State：** 有限状态用 blockstate，无限/近乎无限状态用 block entity。

```java
// 方块实体类
public class MyBlockEntity extends BlockEntity {
    public MyBlockEntity(BlockPos pos, BlockState state) {
        super(MY_BLOCK_ENTITY.get(), pos, state);
    }
}

// 注册
public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = 
    DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExampleMod.MOD_ID);

public static final Supplier<BlockEntityType<MyBlockEntity>> MY_BLOCK_ENTITY = 
    BLOCK_ENTITY_TYPES.register("my_block_entity",
        () -> BlockEntityType.Builder.of(MyBlockEntity::new, MyBlocks.MY_BLOCK_1.get(), MyBlocks.MY_BLOCK_2.get())
            .build(null));

// 关联的方块类需实现 EntityBlock
public class MyEntityBlock extends Block implements EntityBlock {
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MyBlockEntity(pos, state);
    }
}
```

### 数据存储

使用 `#loadAdditional`/`#saveAdditional` 方法读写 NBT：

```java
@Override
public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    this.value = tag.getInt("value");
}

@Override
public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putInt("value", this.value);
}
```

- 保留标签名：`id`、`x`、`y`、`z`、`NeoForgeData`、`neoforge:attachments`
- 修改数据后需调用 `#setChanged()` 标记为脏数据

### Ticker 系统

```java
// 在方块类中
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, MY_BLOCK_ENTITY.get(), MyBlockEntity::tick);
}

// 在方块实体类中
public static void tick(Level level, BlockPos pos, BlockState state, MyBlockEntity blockEntity) {
    // 每 tick 执行的逻辑
}
```

### 同步机制

**1. 区块加载时同步：**
覆写 `getUpdateTag` 和 `handleUpdateTag`

**2. 方块更新时同步：**

```java
@Override
public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
}
// 在服务端触发：level.sendBlockUpdated(pos, oldState, newState, flags)
```

**3. 自定义数据包：** 使用 `PacketDistrubtor#sendToPlayersTrackingChunk`

### 方块实体渲染器

**BER（BlockEntityRenderer）：**

```java
public class MyBlockEntityRenderer implements BlockEntityRenderer<MyBlockEntity> {
    @Override
    public void render(MyBlockEntity be, float partialTick, PoseStack stack, 
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // 渲染逻辑
    }
}

// 注册
@SubscribeEvent
public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(MyBlockEntities.MY_BLOCK_ENTITY.get(), MyBlockEntityRenderer::new);
}
```

**BEWLR（BlockEntityWithoutLevelRenderer）：** 用于物品的特殊渲染（无世界上下文）。

```java
public class MyBEWLR extends BlockEntityWithoutLevelRenderer {
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transform, 
                             PoseStack poseStack, MultiBufferSource bufferSource, 
                             int packedLight, int packedOverlay) { ... }
}

// 通过 RegisterClientExtensionsEvent 注册
@SubscribeEvent
public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
    event.registerItem(new MyClientItemExtensions(), MyItems.ITEM_1, MyItems.ITEM_2);
}
```

---

## 6. 资源 (Resources)

### 概述

- **Assets（客户端资源）：** 纹理、模型、翻译、声音、粒子定义等，通过资源包加载
- **Data（服务端资源）：** 进度、战利品表、配方、标签、世界生成等，通过数据包加载
- `pack.mcmeta` 文件在现代 NeoForge 中自动生成，无需手动创建
- 资源可通过 IDE 的外部资源区域查看

### 数据生成

**Datagen 运行方式：** 通过 Data 运行配置启动，在 `GatherDataEvent` 中注册数据提供器。

```java
@SubscribeEvent
public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput output = generator.getPackOutput();
    // ... 注册各种数据提供器
    generator.addProvider(event.includeServer(), new MyRecipeProvider(output, lookupProvider));
}
```

**命令行参数：**
- `--mod <modid>`：指定运行的 mod
- `--output <path>`：输出目录
- `--existing <path>`：检查已有文件的目录
- `--includeClient`/`--includeServer`/`--includeDev`/`--includeReports`/`--all`：生成模式

### 客户端资源

#### 国际化

**Component 系统：**
- `Component.literal()`：直接显示文本
- `Component.translatable()`：可翻译文本，支持变量插值（使用 `%s`）
- `MutableComponent`：可修改组件（设置样式）
- **永远在客户端解析 Component，不要服务端翻译**

**样式（Style）：**
- `withColor(color)`、`withBold(true)`、`withItalic(true)`、`withUnderlined(true)`、`withStrikethrough(true)`、`withObfuscated(true)`
- 合并样式：`style1.applyTo(style2)`

**点击/悬停事件：**
- ClickEvent：`OPEN_URL`、`OPEN_FILE`、`RUN_COMMAND`、`SUGGEST_COMMAND`、`CHANGE_PAGE`、`COPY_TO_CLIPBOARD`
- HoverEvent：`SHOW_TEXT`、`SHOW_ITEM`、`SHOW_ENTITY`

**语言文件：** 位于 `assets/<modid>/lang/<language>.json`。翻译键通常格式为 `registry.modid.name`。

#### 模型

**JSON 模型根属性：**
- `loader`：NeoForge 自定义模型加载器
- `parent`：父模型
- `ambientocclusion`：环境光遮蔽（默认 true）
- `render_type`：渲染类型（见下文）
- `gui_light`："front" 或 "side"
- `textures`：纹理变量映射（`#变量名` 引用）
- `elements`：立方体元素列表
- `overrides`：物品覆盖模型（基于浮点值切换）
- `display`：不同视角的变换选项

**NeoForge 渲染类型：**
- `minecraft:solid`：完全固体
- `minecraft:cutout`：完全透明/不透明
- `minecraft:cutout_mipped`：带 mipmap 的 cutout
- `minecraft:cutout_mipped_all`：物品也带 mipmap
- `minecraft:translucent`：半透明
- `minecraft:tripwire`：绊线特殊渲染
- 可注册自定义渲染类型：`RegisterNamedRenderTypesEvent`

**Blockstate 文件：**
- `variants`：每个 blockstate 一个模型
- `multipart`：基于条件组合模型（如栅栏）

**着色（Tinting）：** 通过 `RegisterColorHandlersEvent.Block` 和 `RegisterColorHandlersEvent.Item` 注册颜色处理器。

**额外面数据（`neoforge_data`）：** 可为面和元素添加颜色、方块光、天空光、环境光遮蔽设置。

**Root Transforms：** 使用 `transform` 属性在渲染前对几何体应用变换。

**覆盖（Overrides）：** 通过 `ItemProperties#register` 注册自定义属性。

#### 纹理

- PNG 格式，位于 `textures` 文件夹
- 建议使用 2 的幂尺寸（16×16、32×32 等）
- `.mcmeta` 文件可设置缩放方式（stretch/tile/nine_slice）和动画

**动画纹理：** 各帧纵向排列在同一个 PNG 中，通过 `animation` 子对象控制：
- `frames`：帧播放顺序
- `frametime`：每帧停留时间
- `interpolate`：是否插值
- `width`/`height`：每帧尺寸

#### 声音

**术语：**
- **SoundEvent：** 代码中的声音触发器（需注册）
- **SoundSource（声音类别）：** master/block/player 等
- **声音定义：** `sounds.json` 中映射 sound event 到声音文件
- **仅支持 `.ogg` 格式；使用单声道以获得距离衰减效果**

**注册 SoundEvent：**
```java
public static final DeferredHolder<SoundEvent, SoundEvent> MY_SOUND = SOUND_EVENTS.register(
    "my_sound",
    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("examplemod", "my_sound"))
);
```

**`sounds.json` 声音对象属性：**
- `name`：声音文件位置
- `volume`：音量（0.0-1.0）
- `pitch`：音高（0.0-2.0）
- `weight`：随机选择权重
- `stream`：流式播放（适合长音频）
- `attenuation_distance`：衰减距离
- `preload`：预加载
- 合并规则：除非 `replace: true`，否则多个资源包的声音定义会合并

**播放声音的方法：**
- `Level#playSound`：最常用，服务端向非发起者玩家发送
- `Level#playLocalSound`：仅客户端播放
- `Entity#playSound`：实体位置播放
- `Player#playSound`：向自身播放

#### 粒子

**注册粒子类型：**

```java
public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MY_PARTICLE = 
    PARTICLE_TYPES.register("my_particle", () -> new SimpleParticleType(false));
```

**ParticleProvider：** 客户端类，负责创建 Particle 实例。

**粒子定义 JSON：** `assets/<namespace>/particles/<name>.json`，包含 `textures` 数组。

**生成粒子：**
- 通用代码：`Level#addParticle`（推荐）
- 客户端：`Minecraft.getInstance().particleEngine#add(Particle)`
- 服务端：`ServerLevel#sendParticles`

### 服务端资源

#### 进度

**JSON 规范（部分字段）：**
- `parent`：父进度 ID
- `display`：显示设置（图标、标题、描述、框架类型、背景、是否弹窗/通告/隐藏）
- `criteria`：触发条件映射
- `requirements`：条件逻辑（AND/OR 列表）
- `rewards`：奖励（经验、配方、战利品表、函数）
- `neoforge:conditions`：加载条件

**自定义条件触发器：**
- 继承 `SimpleCriterionTrigger<T>` 实现触发逻辑
- 实现 `SimpleCriterionTrigger.SimpleInstance` 定义条件实例（使用 Codec）
- 使用 `#trigger(ServerPlayer, Predicate)` 触发

#### 数据加载条件

**内置条件：**
- `neoforge:true` / `neoforge:false`：恒真/恒假
- `neoforge:not`：取反
- `neoforge:and` / `neoforge:or`：逻辑与/或
- `neoforge:mod_loaded`：检查 mod 是否加载
- `neoforge:item_exists`：检查物品是否注册
- `neoforge:tag_empty`：检查标签是否为空

可以自定义条件：实现 `ICondition` 接口并注册 `MapCodec`。

#### 伤害类型

**伤害类型 JSON（数据包注册表）：**
- `message_id`：死亡消息 ID
- `scaling`：伤害缩放（`never`/`when_caused_by_living_non_player`/`always`）
- `exhaustion`：饥饿消耗
- `effects`：伤害音效（`hurt`/`thorns`/`drowning`/`burning`/`poking`/`freezing`）
- `death_message_type`：死亡消息类型（`default`/`fall_variants`/`intentional_game_design`）

**创建 DamageSource：**

```java
DamageSource damageSource = new DamageSource(
    registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(EXAMPLE_DAMAGE),
    null, // direct entity
    causer, // causing entity
    null  // damage source position
);
```

---

## 7. 物品栏与传输

### 容器

**Container 接口：** 定义 `#getItem`、`#setItem`、`#removeItem` 等方法。

**基本实现：** 使用 `NonNullList<ItemStack>` 作为后端存储。

**重要子类/接口：**
- `SimpleContainer`：带监听器的基本容器
- `BaseContainerBlockEntity`：方块实体容器基类（同时是 `MenuProvider` 和 `Nameable`）
- `WorldlyContainer`：按方向暴露不同槽位

**ItemStack 上的容器：** 使用 `minecraft:container` 数据组件（`ItemContainerContents`）。

**玩家物品栏（Player Inventory）：**
- `items` 列表：36 个主物品栏槽位（含 9 个快捷栏）
- `armor` 列表：4 个护甲槽位
- `offhand` 列表：1 个副手槽位

### 能力系统

**NeoForge 提供的 Capability：**
- `IItemHandler`：物品栏操作（BLOCK/ENTITY/ENTITY_AUTOMATION/ITEM）
- `IFluidHandler`：流体容器操作（BLOCK/ENTITY/ITEM）
- `IEnergyStorage`：能量存储（基于 RedstoneFlux API）

**创建自定义 Capability：**

```java
public static final BlockCapability<IItemHandler, @Nullable Direction> ITEM_HANDLER_BLOCK = 
    BlockCapability.createSided(
        ResourceLocation.fromNamespaceAndPath("mymod", "item_handler"),
        IItemHandler.class
    );
```

**查询 Capability：**
- 方块：`level.getCapability(CAP, pos, context)`
- 实体：`entity.getCapability(CAP, context)`
- 物品：`stack.getCapability(CAP, context)`

**注册 Capability（`RegisterCapabilitiesEvent`）：**
```java
event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MY_BE_TYPE, (be, side) -> be.myHandler);
event.registerEntity(Capabilities.ItemHandler.ENTITY, MY_ENTITY_TYPE, (entity, ctx) -> entity.myHandler);
event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), MY_ITEM);
```

**BlockCapabilityCache：** 对频繁查询的 capability 进行缓存以提高性能。

```java
this.capCache = BlockCapabilityCache.create(
    Capabilities.ItemHandler.BLOCK, level, pos, Direction.NORTH,
    () -> !this.isRemoved(), // 有效性检查
    () -> onCapInvalidate()  // 失效监听器
);
```

**重要：** 当 capability 变化时，必须调用 `level.invalidateCapabilities(pos)`。

---

## 8. 数据存储

### NBT 格式

**NBT 与 JSON 的区别：**
- 字节/短整型/长整型/浮点有类型后缀（b/s/l/f）
- 无布尔类型（用字节 0/1 表示）
- 无 null 值
- 键的引号可选
- NBT 列表仅允许单一类型
- 特殊数组类型：`[B;...]`（字节数组）、`[I;...]`（整数数组）、`[L;...]`（长整型数组）
- 尾部逗号允许

**代码中使用 NBT：**
- `CompoundTag` 为主要操作对象
- `putInt`/`putString`/`putDouble` 等写方法
- `getInt`/`getString`/`getDouble` 等读方法
- 数字类型不存在时返回 0，字符串返回 `""`，复杂类型抛异常
- `contains(key)` 检查存在性
- `ListTag` 需指定列表类型

### Codec 编解码系统

**DynamicOps 类型：**
- `JsonOps.INSTANCE`：标准 JSON
- `JsonOps.COMPRESSED`：压缩 JSON
- `NbtOps.INSTANCE`：NBT 格式
- `RegistryOps`：处理注册表条目

**内置 Codec：**
- 基本类型：`BOOL`/`BYTE`/`SHORT`/`INT`/`LONG`/`FLOAT`/`DOUBLE`/`STRING`/`BYTE_BUFFER`
- 其他：`INT_STREAM`/`LONG_STREAM`/`PASSTHROUGH`/`EMPTY`
- 原版/NeoForge 特定：`ResourceLocation#CODEC`、`CompoundTag#CODEC` 等

**创建 Codec：**
- **Records：** 使用 `RecordCodecBuilder#create`，`#group` 定义字段，`#apply` 构造对象
- **Transformers：** `#xmap`/`#flatComapMap`/`#comapFlatMap`/`#flatXMap` 用于类型转换
- **Range Codecs：** `#intRange`/`#floatRange`/`#doubleRange` 限制数值范围
- **Defaults：** `#orElse`/`#orElseGet` 设置默认值
- **Unit：** `#unit` 返回固定值
- **Lazy：** `#lazyInitialized` 延迟初始化
- **List：** `#listOf` 列表编解码
- **Map：** `#unboundedMap` 映射编解码（键必须可转为字符串）
- **Pair：** `#pair` 对编解码
- **Either：** `#either`/`#xor` 二选一编解码
- **Recursive：** `#recursive` 递归引用
- **Dispatch：** `#dispatch` 基于类型的多态编解码

### 数据附件

用于在方块实体、区块、实体上附加数据。

**创建附件类型：**

```java
private static final Supplier<AttachmentType<ItemStackHandler>> HANDLER = ATTACHMENT_TYPES.register(
    "handler", () -> AttachmentType.serializable(() -> new ItemStackHandler(1)).build());

private static final Supplier<AttachmentType<Integer>> MANA = ATTACHMENT_TYPES.register(
    "mana", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
```

**使用：**
- `chunk.getData(HANDLER)`：获取或创建默认值
- `chunk.hasData(HANDLER)`：检查是否有数据
- `chunk.setData(MANA, value)`：设置数据（自动标记为脏）
- 修改通过 `getData` 获取的数据需手动调用 `setUnsaved(true)`

**同步到客户端：** 需自行发送数据包。
**玩家死亡时复制数据：** 设置 `copyOnDeath` 或通过 `PlayerEvent.Clone` 手动处理。

### 存档数据

用于关卡级别的全局数据存储。

```java
public class ExampleSavedData extends SavedData {
    public static ExampleSavedData create() { return new ExampleSavedData(); }
    
    public static ExampleSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) { ... }
    
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) { ... return tag; }
}

// 附加到关卡
netherDataStorage.computeIfAbsent(
    new Factory<>(ExampleSavedData::create, ExampleSavedData::load), "example");
```

- 跨维度的数据应附加到主世界（Overworld），因为它是唯一永远不会完全卸载的维度

---

## 9. GUI 系统

### 菜单

**MenuType：** 菜单的工厂注册对象。

```java
public static final Supplier<MenuType<MyMenu>> MY_MENU = REGISTER.register(
    "my_menu", () -> new MenuType(MyMenu::new, FeatureFlags.DEFAULT_FLAGS));
```

**`IContainerFactory`：** 如需从服务端发送额外数据到客户端菜单，使用此扩展。

**AbstractContainerMenu：**
- 两个构造器：一个用于服务端初始化，一个用于客户端初始化（默认参数）
- 必须实现 `#stillValid`（检查菜单是否仍然有效）和 `#quickMoveStack`（Shift+点击物品转移）
- 使用 `ContainerLevelAccess` 管理世界和位置访问
- 数据同步：`Slot`（ItemStack 同步）、`DataSlot`（整数同步，实际限制为 short，NeoForge 已修补为完整整数）
- `ContainerData`：多个整数的索引查找接口

**打开菜单：**
```java
serverPlayer.openMenu(new SimpleMenuProvider(
    (containerId, playerInventory, player) -> new MyMenu(containerId, playerInventory),
    Component.translatable("menu.title.examplemod.mymenu")));
```

- 方块实现：覆写 `BlockBehaviour#useWithoutItem` 和 `BlockBehaviour#getMenuProvider`
- 实体实现：覆写 `Mob#mobInteract` 并实现 `MenuProvider`

### 屏幕

**屏幕组件：**
- `GuiGraphics`：渲染工具（彩色矩形、字符串、纹理、物品、提示框、裁剪）
- `Renderable`：可渲染对象（`#render` 方法）
- `GuiEventListener`：用户交互处理（鼠标、键盘事件）
- `ContainerEventHandler`：管理子组件交互（拖拽、聚焦）
- `NarratableEntry`：辅助功能叙述

**基本屏幕：**
- `#init`：初始化（添加组件、预计算坐标）
- `#tick`：每帧客户端逻辑
- `#render`：渲染（背景 -> 组件 -> 提示框）
- `#onClose`/`#removed`：关闭处理

**AbstractContainerScreen：**

```java
public class MyContainerScreen extends AbstractContainerScreen<MyMenu> {
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.label, this.labelX, this.labelY, 0x404040);
    }
}

// 注册
@SubscribeEvent
public static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(MY_MENU.get(), MyContainerScreen::new);
}
```

**`blitSprite`：** 特殊纹理渲染方法，纹理相对 `textures/gui/sprites`，支持三种缩放方式（stretch/tile/nine_slice）。

---

## 10. 世界生成

### 生物群系修改器

**内置 Biome Modifier 类型：**

| 类型 | 功能 |
|------|------|
| `neoforge:none` | 无操作（用于禁用其他修改器） |
| `neoforge:add_features` | 向生物群系添加地物（如树木、矿石） |
| `neoforge:remove_features` | 从生物群系移除地物 |
| `neoforge:add_spawns` | 添加实体生成 |
| `neoforge:remove_spawns` | 移除实体生成 |
| `neoforge:add_spawn_costs` | 添加生成成本（减少实体聚集） |
| `neoforge:remove_spawn_costs` | 移除生成成本 |
| `neoforge:add_carvers` | 添加洞穴/峡谷雕刻器（旧版洞穴系统） |
| `neoforge:remove_carvers` | 移除洞穴/峡谷雕刻器 |

**JSON 文件位置：** `data/<modid>/neoforge/biome_modifier/<path>.json`

**GenerationStep.Decoration 步骤（按生成顺序）：**
`raw_generation` -> `lakes` -> `local_modifications` -> `underground_structures` -> `surface_structures` -> `strongholds` -> `underground_ores` -> `underground_decoration` -> `fluid_springs` -> `vegetal_decoration` -> `top_layer_modification`

**创建自定义 BiomeModifier：**
- 实现 `BiomeModifier` 接口（`#modify` 和 `#codec` 方法）
- 注册 `MapCodec` 到 `NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS`
- 修改阶段（Phase）：`BEFORE_EVERYTHING`、`ADD`、`REMOVE`、`MODIFY`、`AFTER_EVERYTHING`

**数据生成：** 通过 `RegistrySetBuilder` + `DatapackBuiltinEntriesProvider` 生成。

---

## 11. 网络通信

**网络通信两个主要目标：**
1. 确保客户端与服务端视图同步
2. 让客户端告知服务端玩家的变化（如按键）

**核心流程：**
- 监听 `RegisterPayloadHandlersEvent`
- 注册 Payload（自定义数据包）、读取器和处理器

**Payload 相关子页面：**
- 注册 Payloads
- Stream Codecs（网络流编解码器）
- 配置任务（Configuration Tasks）
- 实体网络同步

---

## 12. 高级主题

### 访问转换器

**AT 文件添加方式：**

```groovy
// build.gradle
minecraft {
    accessTransformers {
        file('src/main/resources/META-INF/accesstransformer.cfg')
    }
}
```

**修改后需刷新 Gradle 项目使其生效。**

**访问修饰符（由宽到窄）：**
- `public`：所有类可访问
- `protected`：包内和子类可访问
- `default`：仅包内可访问
- `private`：仅类内可访问

**final 修饰符：**
- `+f`：添加 final
- `-f`：移除 final

**AT 指令格式：**
- 类：`<修饰符> <完全限定类名>`
- 字段：`<修饰符> <完全限定类名> <字段名>`
- 方法：`<修饰符> <完全限定类名> <方法名>(<参数类型描述符>)<返回类型描述符>`

**类型描述符：**
B(byte)、C(char)、D(double)、F(float)、I(int)、J(long)、S(short)、Z(boolean)、`[`(数组)、`L<class>;`(引用类型)、`V`(void)

**注意：** 访问转换只影响直接目标，不会自动转换覆写方法。安全的转换目标包括 final 方法、static 方法和 private 方法。

---

## 13. 杂项

### 配置系统

**使用 TOML 格式和 NightConfig 库。**

**创建配置：**

```java
public static final ExampleConfig CONFIG;
public static final ModConfigSpec CONFIG_SPEC;

static {
    Pair<ExampleConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ExampleConfig::new);
    CONFIG = pair.getLeft();
    CONFIG_SPEC = pair.getRight();
}

public class ExampleConfig {
    public final ModConfigSpec.ConfigValue<String> welcomeMessage;
    
    private ExampleConfig(ModConfigSpec.Builder builder) {
        welcomeMessage = builder.define("welcome_message", "Hello from the config!");
    }
}
```

**ConfigValue 类型：**
- `#define`：基本值定义
- `#defineInRange`：范围值（`Comparable<T>`）
- `#defineInList`：白名单值
- `#defineList`：列表值
- `#defineEnum`：枚举值
- Boolean 值：特殊定义方法

**配置类型（ModConfig.Type）：**
- `STARTUP`：启动时立即读取，不同步（可能导致客户端/服务端不同步，应避免用于控制内容注册）
- `CLIENT`：仅客户端配置，不同步
- `COMMON`：通用配置，客户端和服务端都有，不同步
- `SERVER`：服务端配置，可被世界级配置覆盖，**会同步到客户端**

**注册配置：**
```java
container.registerConfig(ModConfig.Type.COMMON, ExampleConfig.CONFIG_SPEC);
```

**配置事件：** `ModConfigEvent.Loading` 和 `ModConfigEvent.Reloading`

**配置屏幕：**
```java
container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
```

---

> **本文档基于 NeoForge 官方文档 1.21 - 1.21.1 版本整理，该版本已不再维护。**  
> **最新文档请访问：[https://docs.neoforged.net/](https://docs.neoforged.net/)**  
> **官方 Discord：**[https://discord.neoforged.net/](https://discord.neoforged.net/)
