package fr.iglee42.createcasing.recipe;

import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class CasingPressingRecipe extends PressingRecipe {

    private final IRecipeTypeInfo typeInfo;
    @Nullable
    private final ResourceLocation pressFilter;

    public CasingPressingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeParams params, @Nullable ResourceLocation pressFilter) {
        super(params);
        this.typeInfo = typeInfo;
        this.pressFilter = pressFilter;
    }

    public boolean matchesPress(ResourceLocation pressBlockId) {
        return pressFilter == null || pressFilter.equals(pressBlockId);
    }

    @Nullable
    public ResourceLocation getPressFilter() {
        return pressFilter;
    }

    @Override
    public RecipeType<?> getType() {
        return typeInfo.getType();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return typeInfo.getSerializer();
    }
}
