package fr.iglee42.createcasing.recipe;

import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class CasingMixingRecipe extends MixingRecipe {

    private final IRecipeTypeInfo typeInfo;
    @Nullable
    private final ResourceLocation mixerFilter;

    public CasingMixingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeParams params, @Nullable ResourceLocation mixerFilter) {
        super(params);
        this.typeInfo = typeInfo;
        this.mixerFilter = mixerFilter;
    }

    public boolean matchesMixer(ResourceLocation mixerBlockId) {
        return mixerFilter == null || mixerFilter.equals(mixerBlockId);
    }

    @Nullable
    public ResourceLocation getMixerFilter() {
        return mixerFilter;
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
