package com.fish.fishthings.datagen;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.register.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipe extends RecipeProvider {

    private static final TagKey<Item> TAG_ATMIUM = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "ingots/allthemodium")
    );

    public ModRecipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ModLoadedCondition wasATMiumLoaded = new ModLoadedCondition("allthemodium");
        NotCondition wasnotATMiumLoaded = new NotCondition(wasATMiumLoaded);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SUPER_TOOL.get())
                .pattern("I I")
                .pattern(" S ")
                .pattern("I I")
                .define('I', TAG_ATMIUM)
                .define('S', Items.STICK)
                .unlockedBy("has_atmium_ingot", has(TAG_ATMIUM))
                .save(recipeOutput.withConditions(wasATMiumLoaded),
                        ResourceLocation.fromNamespaceAndPath(FishThings.MODID, "super_tool_shaped"));
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(Items.STICK),
                Ingredient.of(Tags.Items.INGOTS_NETHERITE),
                RecipeCategory.TOOLS,
                ModItems.SUPER_TOOL.get())
                .unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE))
                .save(recipeOutput.withConditions(wasnotATMiumLoaded),
                        ResourceLocation.fromNamespaceAndPath(FishThings.MODID, "super_tool_smithing"));
    }
}