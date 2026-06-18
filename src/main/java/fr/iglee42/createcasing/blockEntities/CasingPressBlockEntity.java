package fr.iglee42.createcasing.blockEntities;

import java.util.Optional;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;

import fr.iglee42.createcasing.config.ModConfigs;
import fr.iglee42.createcasing.registries.AllCasingRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CasingPressBlockEntity extends MechanicalPressBlockEntity {

    public CasingPressBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected boolean matchStaticFilters(RecipeHolder<? extends Recipe<?>> recipe) {
        if (!ModConfigs.common().kinetics.customPressesUseStandardRecipes.get())
            return false;
        return super.matchStaticFilters(recipe);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Optional<RecipeHolder<PressingRecipe>> getRecipe(ItemStack item) {
        Optional<RecipeHolder<PressingRecipe>> assemblyRecipe =
            SequencedAssemblyRecipe.getRecipe(level, item,
                (net.minecraft.world.item.crafting.RecipeType) AllCasingRecipeTypes.CASING_PRESSING_TYPE.get(),
                PressingRecipe.class);
        if (assemblyRecipe.isPresent())
            return assemblyRecipe;

        ResourceLocation pressBlockId = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        Optional<RecipeHolder<PressingRecipe>> result = AllCasingRecipeTypes.findCasingPressing(new SingleRecipeInput(item), level, pressBlockId);
        if (result.isPresent())
            return result;

        if (ModConfigs.common().kinetics.customPressesUseStandardRecipes.get()) {
            return level.getRecipeManager()
                .getRecipeFor((RecipeType<PressingRecipe>) (RecipeType<?>) AllRecipeTypes.PRESSING.getType(),
                    new SingleRecipeInput(item), level);
        }

        return Optional.empty();
    }
}
