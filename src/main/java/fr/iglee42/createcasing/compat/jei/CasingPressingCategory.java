package fr.iglee42.createcasing.compat.jei;

import com.simibubi.create.compat.jei.category.PressingCategory;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import fr.iglee42.createcasing.compat.jei.animations.AnimatedCasingPress;
import fr.iglee42.createcasing.recipe.CasingPressingRecipe;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CasingPressingCategory extends PressingCategory {

    public CasingPressingCategory(Info<PressingRecipe> info) {
        super(info);
    }

    @Override
    public void draw(PressingRecipe recipe, IRecipeSlotsView view, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 61, 41);
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, 52, 54);

        BlockState pressState = getPressState(recipe);
        new AnimatedCasingPress(pressState).draw(graphics, getBackground().getWidth() / 2 - 17, 22);
    }

    @Nullable
    private static BlockState getPressState(PressingRecipe recipe) {
        if (recipe instanceof CasingPressingRecipe cpr) {
            ResourceLocation pressFilter = cpr.getPressFilter();
            if (pressFilter != null) {
                Block block = BuiltInRegistries.BLOCK.get(pressFilter);
                if (block != Blocks.AIR) {
                    return block.defaultBlockState();
                }
            }
        }
        return null;
    }
}
