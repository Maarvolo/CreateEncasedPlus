package fr.iglee42.createcasing.compat.jei;

import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import fr.iglee42.createcasing.compat.jei.animations.AnimatedCasingMixer;
import fr.iglee42.createcasing.recipe.CasingMixingRecipe;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CasingMixingCategory extends BasinCategory {

    public CasingMixingCategory(Info<BasinRecipe> info) {
        super(info, false);
    }

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView view, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 81, 68);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 136, 32);

        BlockState mixerState = getMixerState(recipe);
        new AnimatedCasingMixer(mixerState).draw(graphics, getBackground().getWidth() / 2 + 3, 34);
    }

    @Nullable
    private static BlockState getMixerState(BasinRecipe recipe) {
        if (recipe instanceof CasingMixingRecipe cmr) {
            ResourceLocation mixerFilter = cmr.getMixerFilter();
            if (mixerFilter != null) {
                Block block = BuiltInRegistries.BLOCK.get(mixerFilter);
                if (block != Blocks.AIR) {
                    return block.defaultBlockState();
                }
            }
        }
        return null;
    }
}
