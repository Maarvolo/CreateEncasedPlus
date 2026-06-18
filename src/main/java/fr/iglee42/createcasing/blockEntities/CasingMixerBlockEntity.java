package fr.iglee42.createcasing.blockEntities;

import java.util.List;

import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;

import fr.iglee42.createcasing.config.ModConfigs;
import fr.iglee42.createcasing.recipe.CasingMixingRecipe;
import fr.iglee42.createcasing.registries.AllCasingRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CasingMixerBlockEntity extends MechanicalMixerBlockEntity {

    private static final Object casingMixingRecipesKey = new Object();

    public CasingMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected Object getRecipeCacheKey() {
        return casingMixingRecipesKey;
    }

    @Override
    protected boolean matchStaticFilters(RecipeHolder<? extends Recipe<?>> recipe) {
        Recipe<?> r = recipe.value();
        if (r.getType() == AllCasingRecipeTypes.CASING_MIXING_TYPE.get())
            return true;
        if (!ModConfigs.common().kinetics.customMixersUseStandardRecipes.get())
            return false;
        if (r.getType() == AllRecipeTypes.MIXING.getType())
            return true;
        return r instanceof CraftingRecipe && !(r instanceof ShapedRecipe)
            && AllConfigs.server().recipes.allowShapelessInMixer.get()
            && r.getIngredients().size() > 1
            && !MechanicalPressBlockEntity.canCompress(r)
            && !AllRecipeTypes.shouldIgnoreInAutomation(recipe);
    }

    @Override
    protected List<Recipe<?>> getMatchingRecipes() {
        ResourceLocation mixerBlockId = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        List<Recipe<?>> list = super.getMatchingRecipes();
        list.removeIf(r -> {
            if (r instanceof CasingMixingRecipe cmr && !cmr.matchesMixer(mixerBlockId))
                return true;
            if (!ModConfigs.common().kinetics.customMixersUseStandardRecipes.get()
                && r.getType() == AllRecipeTypes.MIXING.getType())
                return true;
            return false;
        });
        return list;
    }
}
