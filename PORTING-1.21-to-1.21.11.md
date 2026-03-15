# Porting Farmers Delight From 1.21 to 1.21.11

This document records the work that was done on the `1.21.11` branch to move Farmers Delight forward from the `1.21` branch. It is meant to be used as a practical migration guide for Farmers Delight addon and extension mods.

The branch-level port was done in these commits:

1. `72acb447` Update Gradle and dependencies for 1.21.11 port
2. `70d4fdc6` Update NeoForge and dependency versions to resolve build issues
3. `2e107f11` Bulk-replace usages of `ResourceLocation`, `javax.annotation.Nullable`, and other various legacy methods with the updated variants
4. `a1a862fa` Port client rendering and UI code to 1.21.11 APIs
5. `53e6a18f` Port event hooks and block entity rendering to 1.21.11 APIs
6. `903111ea` Port cooking systems and containers to updated NeoForge APIs
7. `0309d5c8` Port block and item logic to 1.21.11 API changes
8. `8f9f67c2` Port mod and integrations to NeoForge 1.21.11 APIs
9. `cec119e8` Complete 1.21.11 port fixes

## Scope

The diff from `1.21` to `1.21.11` touched three main areas:

- Build and dependency setup moved to the 1.21.11 NeoForge toolchain.
- Core code was updated for renamed or redesigned Minecraft and NeoForge APIs.
- Data generation, recipe book integration, rendering, and recipe handling were rewritten where the old hooks no longer matched 1.21.11.

This was not a tiny compatibility patch. The port included broad API migration plus some structural rewrites where the previous implementation model no longer fit.

## 1. Build and dependency updates

The first step was getting the project onto the 1.21.11 toolchain.

- Updated `minecraft_version` from `1.21.1` to `1.21.11`.
- Updated `neo_version` from `21.1.219` to `21.11.38-beta`.
- Updated Parchment mappings to the 1.21.11 line.
- Updated Gradle wrapper from `8.8` to `9.4.0`.
- Updated the `net.neoforged.moddev` plugin from `1.0.0` to `2.0.140`.
- Split datagen runs into `serverData` and `clientData`.
- Added a convenience `runData` task that depends on `runClientData`.

Relevant files:

- [build.gradle](/Users/jhein/GitHub/FarmersDelight/build.gradle)
- [gradle.properties](/Users/jhein/GitHub/FarmersDelight/gradle.properties)
- [gradle/wrapper/gradle-wrapper.properties](/Users/jhein/GitHub/FarmersDelight/gradle/wrapper/gradle-wrapper.properties)

### Extension mod takeaway

Start by updating your toolchain before touching gameplay code. A large part of the later compile noise only becomes meaningful once the dependency graph is correct.

## 2. Temporary compatibility shims for lagging dependencies

Two runtime dependencies were not fully caught up to 1.21.11:

- JEI
- CraftTweaker

The project handled that by:

- Keeping their APIs on the compile classpath.
- Gating runtime jars behind explicit Gradle properties.
- Storing patched jars in `runtime-libs/`.
- Adding a `compatShim` source set that restores old `ResourceLocation` classes expected by lagging dependencies.

Relevant files:

- [build.gradle](/Users/jhein/GitHub/FarmersDelight/build.gradle)
- [src/compatShim/java/net/minecraft/resources/ResourceLocation.java](/Users/jhein/GitHub/FarmersDelight/src/compatShim/java/net/minecraft/resources/ResourceLocation.java)
- [src/compatShim/java/net/minecraft/ResourceLocationException.java](/Users/jhein/GitHub/FarmersDelight/src/compatShim/java/net/minecraft/ResourceLocationException.java)

### Extension mod takeaway

If your addon depends on libraries still compiled against 1.21.1-era names, do not block the whole port on them. Isolate the compatibility layer and make runtime enablement explicit.

## 3. Global identifier and nullness migration

The broadest mechanical change was moving from older identifier and nullness conventions to the 1.21.11 equivalents.

### Identifier migration

Most code changed from `ResourceLocation` factories to `Identifier` factories:

- `ResourceLocation.fromNamespaceAndPath(...)` became `Identifier.fromNamespaceAndPath(...)` in newer code.
- Registry lookups moved to `BuiltInRegistries.ITEM.getValue(Identifier.parse(key))` style access.
- Many helper methods and data builders now use string ids or `Identifier` instead of the older `ResourceLocation` entry points.

### Nullness migration

The port replaced older null annotations with newer ones:

- `javax.annotation.Nullable` was replaced with `org.jspecify.annotations.Nullable`.
- Some datagen classes also use `@NullMarked`.

### Other mechanical API replacements

Examples that appeared throughout the port:

- `noCollission()` to `noCollision()`
- `FMLEnvironment.dist` to `FMLEnvironment.getDist()`
- registry access methods updated to newer `lookupOrThrow` / `getOrThrow` forms
- `output.getCraftingRemainingItem()` to `output.getItem().getCraftingRemainder()`

### Extension mod takeaway

Do one mostly-mechanical sweep early. It removes a large number of dead errors and makes the remaining real migration issues much easier to see.

## 4. Event registration moved away from older subscriber patterns

The port reduced reliance on static `@EventBusSubscriber` registration for mod-bus work and moved several hooks into explicit listener registration in the mod entrypoint.

Changes included:

- Registering listeners directly from the mod constructor.
- Hooking capability registration from block entity classes onto the mod event bus.
- Registering client listeners explicitly when on the client.
- Updating default component modification code to newer item registry access patterns.

Relevant files:

- [src/main/java/vectorwing/farmersdelight/FarmersDelight.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/FarmersDelight.java)
- [src/main/java/vectorwing/farmersdelight/common/event/CommonModBusEvents.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/event/CommonModBusEvents.java)

### Extension mod takeaway

If your addon registers custom capabilities, menu screens, renderers, or component modifiers, expect to rewire listener registration even when the underlying behavior stays the same.

## 5. Recipe system changes were one of the biggest porting areas

This was the most important functional migration for addon authors.

### Custom recipes now use explicit `RecipeInput`

The cooking pot recipe implementation changed from `Recipe<RecipeWrapper>` to `Recipe<CookingPotRecipeInput>`.

That required:

- Adding a custom `RecipeInput` record for the cooking pot.
- Updating `matches` and `assemble` to accept the new input type.
- Exposing `placementInfo()`.
- Returning the updated generic serializer and recipe type signatures.

Relevant files:

- [src/main/java/vectorwing/farmersdelight/common/crafting/CookingPotRecipe.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/crafting/CookingPotRecipe.java)
- [src/main/java/vectorwing/farmersdelight/common/crafting/CookingPotRecipeInput.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/crafting/CookingPotRecipeInput.java)

### Recipe book integration now uses displays and categories, not the old enum extension approach

The old recipe-book category extension path was removed. The replacement was:

- Register custom `RecipeBookCategory` entries.
- Register a custom `RecipeDisplay.Type`.
- Implement a `CookingPotRecipeDisplay`.
- Return `recipeBookCategory()` and `display()` from the custom recipe.
- Remove old client `RecipeBookCategories` enum extension entries from `enumextensions.json`.

Relevant files:

- [src/main/java/vectorwing/farmersdelight/common/crafting/display/CookingPotRecipeDisplay.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/crafting/display/CookingPotRecipeDisplay.java)
- [src/main/java/vectorwing/farmersdelight/common/registry/ModRecipeBookCategories.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/registry/ModRecipeBookCategories.java)
- [src/main/java/vectorwing/farmersdelight/common/registry/ModRecipeDisplays.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/registry/ModRecipeDisplays.java)
- [src/main/resources/META-INF/enumextensions.json](/Users/jhein/GitHub/FarmersDelight/src/main/resources/META-INF/enumextensions.json)

### Recipe serializers changed for custom crafting recipes

`SimpleCraftingRecipeSerializer` was replaced with `CustomRecipe.Serializer` for custom recipe types like:

- dough
- food serving

Relevant file:

- [src/main/java/vectorwing/farmersdelight/common/registry/ModRecipeSerializers.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/registry/ModRecipeSerializers.java)

### Extension mod takeaway

If your addon adds custom Farmers Delight workstations or recipe types, expect to migrate:

- recipe input wrappers
- serializer generics
- recipe-book category registration
- recipe displays
- menu placement logic

## 6. Menu and server-side recipe placement changed

`CookingPotMenu` had to be rewritten for newer recipe-book placement behavior.

Important changes:

- It no longer uses the old `RecipeWrapper` path.
- It now extends the newer `RecipeBookMenu` form.
- It implements placement through `ServerPlaceRecipe.placeRecipe(...)`.
- It uses `StackedItemContents` instead of the old stacked contents type.
- Slot clearing and item movement logic were updated to current methods like `setByPlayer(...)`.

Relevant file:

- [src/main/java/vectorwing/farmersdelight/common/block/entity/container/CookingPotMenu.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/block/entity/container/CookingPotMenu.java)

### Extension mod takeaway

Any addon menu that integrates with the recipe book or server-side auto-placement should be reviewed line by line. This is not an area where search-and-replace is enough.

## 7. Client GUI and recipe book code had to be reworked

The cooking pot screen port was not just a method rename.

Main changes:

- `CookingPotScreen` now extends `AbstractRecipeBookScreen`.
- The recipe-book button position is supplied through the new screen API.
- Tooltip rendering moved to `setTooltipForNextFrame(...)` and `setComponentTooltipForNextFrame(...)`.
- GUI texture blits now use `RenderPipelines.GUI_TEXTURED`.
- The dedicated recipe-book component now works with `RecipeDisplay`, `GhostSlots`, `RecipeCollection`, and `StackedItemContents`.
- Search-category registration moved to `RegisterRecipeBookSearchCategoriesEvent`.

Relevant files:

- [src/main/java/vectorwing/farmersdelight/client/gui/CookingPotScreen.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/client/gui/CookingPotScreen.java)
- [src/main/java/vectorwing/farmersdelight/client/gui/CookingPotRecipeBookComponent.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/client/gui/CookingPotRecipeBookComponent.java)
- [src/main/java/vectorwing/farmersdelight/client/event/ClientSetupEvents.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/client/event/ClientSetupEvents.java)

### Extension mod takeaway

If your addon has a custom Farmers Delight workstation screen, follow the new recipe-book screen architecture instead of trying to preserve the old one.

## 8. Item rendering moved to special model renderers and item JSON composition

The skillet renderer changed substantially.

Old approach:

- custom item property
- custom `BlockEntityWithoutLevelRenderer`
- renderer registered through client extensions

New approach:

- register a special model renderer in `RegisterSpecialModelRendererEvent`
- add a `SpecialModelRenderer` implementation
- define the item in JSON as a composite model
- use a `minecraft:condition` on `minecraft:has_component`
- render the extra ingredient model only when the custom component exists

Relevant files:

- [src/main/java/vectorwing/farmersdelight/client/renderer/SkilletSpecialRenderer.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/client/renderer/SkilletSpecialRenderer.java)
- [src/main/resources/assets/farmersdelight/items/skillet.json](/Users/jhein/GitHub/FarmersDelight/src/main/resources/assets/farmersdelight/items/skillet.json)
- [src/main/java/vectorwing/farmersdelight/client/event/ClientSetupEvents.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/client/event/ClientSetupEvents.java)

### Extension mod takeaway

If your addon has dynamic item rendering, check whether it should move from custom item-extension rendering to the newer special-model pipeline.

## 9. Block and item registration had to supply ids through properties

One of the more invasive codebase-wide changes was the block registration rewrite.

The port added a custom registration helper in `ModBlocks` that:

- captures the `ResourceKey<Block>` being registered
- applies that key through `properties.setId(...)`
- wraps most block property creation through `idProps(...)`

This pattern was then used across the block registry so block definitions align with the updated expectations in 1.21.11.

Relevant files:

- [src/main/java/vectorwing/farmersdelight/common/registry/ModBlocks.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/registry/ModBlocks.java)
- [src/main/java/vectorwing/farmersdelight/common/registry/ModItems.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/registry/ModItems.java)

### Extension mod takeaway

If your addon registers many custom blocks or block items, expect registration helpers to need structural changes, not just import fixes.

## 10. Datagen changed heavily

The datagen port had two major parts.

### GatherData events were split and modernized

The old single `GatherDataEvent` flow was replaced with separate server and client event handlers.

The new setup:

- registers shared server providers from both event paths
- uses `event.createDatapackRegistryObjects(...)`
- uses `event.createBlockAndItemTags(...)`
- gets resources through `event.getResourceManager(PackType.SERVER_DATA)`

Relevant file:

- [src/main/java/vectorwing/farmersdelight/data/DataGenerators.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/DataGenerators.java)

### Blockstates and item models moved off the old provider helpers

The old `ItemModelProvider` and blockstate/model helper approach was replaced with a lightweight custom JSON writer:

- `JsonAssetProvider` collects JSON into a map and writes stable outputs.
- `BlockStates` now builds JSON directly.
- `ItemModels` now builds JSON directly.

This was likely done because the older helper APIs no longer fit the 1.21.11 asset/model pipeline cleanly enough.

Relevant files:

- [src/main/java/vectorwing/farmersdelight/data/JsonAssetProvider.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/JsonAssetProvider.java)
- [src/main/java/vectorwing/farmersdelight/data/BlockStates.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/BlockStates.java)
- [src/main/java/vectorwing/farmersdelight/data/ItemModels.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/ItemModels.java)

### Recipe datagen moved to the new `RecipeProvider.Runner` style

The recipe generator changed from a direct provider to a runner that creates a custom provider instance.

The port added `FDRecipeProvider` to centralize:

- item tag to ingredient conversion through registry lookups
- shorthand builder helpers for shaped and shapeless recipes
- output delegation

Relevant files:

- [src/main/java/vectorwing/farmersdelight/data/Recipes.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/Recipes.java)
- [src/main/java/vectorwing/farmersdelight/data/FDRecipeProvider.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/FDRecipeProvider.java)

### Recipe builder usage changed

Recipe registration code was updated to use the new provider helpers and newer ingredient patterns:

- tag ingredients are often resolved through `output.ingredient(...)`
- some old manual `Ingredient.fromValues(...)` patterns became `CompoundIngredient.of(...)`
- some tool requirements moved from item abilities to tag-based tool selection such as `ItemTags.AXES`, `ItemTags.PICKAXES`, and `ItemTags.SHOVELS`

Relevant files:

- [src/main/java/vectorwing/farmersdelight/data/recipe/CookingRecipes.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/recipe/CookingRecipes.java)
- [src/main/java/vectorwing/farmersdelight/data/recipe/CraftingRecipes.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/recipe/CraftingRecipes.java)
- [src/main/java/vectorwing/farmersdelight/data/recipe/CuttingRecipes.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/recipe/CuttingRecipes.java)

### Extension mod takeaway

If your addon has substantial datagen, expect to port the generator architecture, not just the recipe contents.

## 11. Damage type registration moved into bootstrap

The stove burn damage type was moved into a proper bootstrap registration flow.

Changes:

- added `ModDamageTypes.bootstrap(BootstrapContext<DamageType>)`
- registered damage types through the datagen registry builder
- updated damage source lookup to the newer registry access path
- moved the generated `stove_burn.json` into generated resources

Relevant files:

- [src/main/java/vectorwing/farmersdelight/common/registry/ModDamageTypes.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/common/registry/ModDamageTypes.java)
- [src/main/java/vectorwing/farmersdelight/data/DataGenerators.java](/Users/jhein/GitHub/FarmersDelight/src/main/java/vectorwing/farmersdelight/data/DataGenerators.java)

### Extension mod takeaway

If your addon defines custom damage types, make sure they are bootstrapped and represented in the updated data registration flow.

## 12. Generated resources were intentionally refreshed

The port regenerated a large amount of JSON under `src/generated/resources`, including:

- recipes
- item models
- blockstates
- data caches
- damage type data

This was part of the port, not unrelated churn. In practice, many structural code changes only became complete after rerunning datagen against the 1.21.11 code.

### Extension mod takeaway

After code compiles, rerun datagen early. Do not postpone it until the end or you will miss data-format issues that look like runtime bugs.

## 13. What extension mods should port in the same order

For Farmers Delight addons, this order should minimize churn:

1. Update Gradle, NeoForge, mappings, Java, and direct dependencies.
2. Add temporary compatibility handling for lagging libraries if needed.
3. Do a mechanical sweep for `Identifier`, nullness annotations, registry lookup APIs, and renamed helpers.
4. Fix registration wiring for events, capabilities, serializers, and client hooks.
5. Port custom recipes to `RecipeInput`, updated serializers, and recipe displays.
6. Port menus and recipe-book integration.
7. Port custom screens and client rendering.
8. Port datagen architecture and rerun all generated assets.
9. Fix any remaining integration-specific compile or runtime issues.

## 14. Checklist for Farmers Delight extension mods

- Update your mod to NeoForge 1.21.11 and matching mappings first.
- Audit every direct use of `ResourceLocation`; replace with `Identifier` where the current API expects it.
- Replace old nullable annotations with `org.jspecify.annotations.Nullable` where required.
- Check every custom recipe type for `RecipeInput`, `PlacementInfo`, display support, and serializer generics.
- Check every custom menu that uses recipe-book placement.
- Replace any old recipe-book enum extension logic with registered categories and displays.
- Review item and block registration helpers for new property/id expectations.
- Revisit dynamic item rendering; special model renderers may now be the correct path.
- Update datagen to current `GatherDataEvent.Server` and `GatherDataEvent.Client` flows.
- Regenerate blockstates, item models, recipes, tags, and any custom registry-backed data.
- If JEI, CraftTweaker, or other integrations lag behind, isolate that problem instead of blocking the base port.

## 15. Practical warning

The hardest parts of this port were not the trivial renames. The real work was in the systems where 1.21.11 changed the expected architecture:

- recipe book integration
- custom recipe inputs and displays
- menu placement
- datagen
- special item rendering

If an addon only uses Farmers Delight items, tags, and simple JSON recipes, the port should be light. If it adds custom workstations, screens, recipes, renderers, or integrations, expect a port closer to the main mod’s migration work.
