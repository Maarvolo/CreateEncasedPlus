package fr.iglee42.createcasing.compat.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import fr.iglee42.createcasing.CreateCasing;
import fr.iglee42.createcasing.casings.CasingSet;
import fr.iglee42.createcasing.casings.CasingSets;
import fr.iglee42.createcasing.registries.AllCasingRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = CreateCasing.asResource("jei");
    private static final ResourceLocation CASING_PRESSING_UID = CreateCasing.asResource("casing_pressing");
    private static final ResourceLocation CASING_MIXING_UID = CreateCasing.asResource("casing_mixing");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerCategories(IRecipeCategoryRegistration registration) {
        ItemStack pressIcon = findFirstCustomPressIcon();
        RecipeType pressJeiType = RecipeType.createRecipeHolderType(CASING_PRESSING_UID);

        CreateRecipeCategory.Info<PressingRecipe> pressInfo = new CreateRecipeCategory.Info<>(
            pressJeiType,
            Component.translatable("create_encasedplus.jei.casing_pressing"),
            registration.getJeiHelpers().getGuiHelper().createBlankDrawable(177, 70),
            registration.getJeiHelpers().getGuiHelper().createDrawableItemStack(pressIcon),
            this::getCasingPressingRecipes,
            findCasingPressCatalysts()
        );

        registration.addRecipeCategories(new CasingPressingCategory(pressInfo));

        ItemStack mixerIcon = findFirstCustomMixerIcon();
        RecipeType mixerJeiType = RecipeType.createRecipeHolderType(CASING_MIXING_UID);

        CreateRecipeCategory.Info<BasinRecipe> mixerInfo = new CreateRecipeCategory.Info<>(
            mixerJeiType,
            Component.translatable("create_encasedplus.jei.casing_mixing"),
            registration.getJeiHelpers().getGuiHelper().createBlankDrawable(177, 123),
            registration.getJeiHelpers().getGuiHelper().createDrawableItemStack(mixerIcon),
            this::getCasingMixingRecipes,
            findCasingMixerCatalysts()
        );

        registration.addRecipeCategories(new CasingMixingCategory(mixerInfo));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ItemStack> stacks = new ArrayList<>();
        CreateCasing.hidedItems.stream().filter(i -> i != Items.AIR).forEach(i -> {
            ItemStack stack = new ItemStack(i);
            if (!stack.isEmpty())
                stacks.add(stack);
        });
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, stacks);

        RecipeType pressJeiType = RecipeType.createRecipeHolderType(CASING_PRESSING_UID);
        registration.addRecipes(pressJeiType, getCasingPressingRecipes());

        RecipeType mixerJeiType = RecipeType.createRecipeHolderType(CASING_MIXING_UID);
        registration.addRecipes(mixerJeiType, getCasingMixingRecipes());
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        RecipeType pressJeiType = RecipeType.createRecipeHolderType(CASING_PRESSING_UID);

        CasingSets.getSets().stream()
            .filter(CasingSet::doesGeneratePress)
            .filter(set -> Objects.nonNull(set.getPress()))
            .map(set -> new ItemStack(set.getPress()))
            .forEach(stack -> registration.addRecipeCatalyst(stack, pressJeiType));

        RecipeType mixerJeiType = RecipeType.createRecipeHolderType(CASING_MIXING_UID);

        CasingSets.getSets().stream()
            .filter(CasingSet::doesGenerateMixer)
            .filter(set -> Objects.nonNull(set.getMixer()))
            .map(set -> new ItemStack(set.getMixer()))
            .forEach(stack -> registration.addRecipeCatalyst(stack, mixerJeiType));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<RecipeHolder<PressingRecipe>> getCasingPressingRecipes() {
        List<RecipeHolder<PressingRecipe>> list = new ArrayList<>();
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.level != null) {
            net.minecraft.world.item.crafting.RecipeType mcType =
                (net.minecraft.world.item.crafting.RecipeType) AllCasingRecipeTypes.CASING_PRESSING_TYPE.get();
            List raw = mc.level.getRecipeManager().getAllRecipesFor(mcType);
            for (Object obj : raw) {
                list.add((RecipeHolder<PressingRecipe>) obj);
            }
        }
        return list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<RecipeHolder<BasinRecipe>> getCasingMixingRecipes() {
        List<RecipeHolder<BasinRecipe>> list = new ArrayList<>();
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.level != null) {
            net.minecraft.world.item.crafting.RecipeType mcType =
                (net.minecraft.world.item.crafting.RecipeType) AllCasingRecipeTypes.CASING_MIXING_TYPE.get();
            List raw = mc.level.getRecipeManager().getAllRecipesFor(mcType);
            for (Object obj : raw) {
                list.add((RecipeHolder<BasinRecipe>) obj);
            }
        }
        return list;
    }

    private static ItemStack findFirstCustomPressIcon() {
        return CasingSets.getSets().stream()
            .filter(CasingSet::doesGeneratePress)
            .filter(set -> Objects.nonNull(set.getPress()))
            .findFirst()
            .map(set -> new ItemStack(set.getPress()))
            .orElse(ItemStack.EMPTY);
    }

    private static ItemStack findFirstCustomMixerIcon() {
        return CasingSets.getSets().stream()
            .filter(CasingSet::doesGenerateMixer)
            .filter(set -> Objects.nonNull(set.getMixer()))
            .findFirst()
            .map(set -> new ItemStack(set.getMixer()))
            .orElse(ItemStack.EMPTY);
    }

    private static List<Supplier<? extends ItemStack>> findCasingPressCatalysts() {
        List<Supplier<? extends ItemStack>> list = new ArrayList<>();
        CasingSets.getSets().stream()
            .filter(CasingSet::doesGeneratePress)
            .filter(set -> Objects.nonNull(set.getPress()))
            .forEach(set -> list.add(() -> new ItemStack(set.getPress())));
        return list;
    }

    private static List<Supplier<? extends ItemStack>> findCasingMixerCatalysts() {
        List<Supplier<? extends ItemStack>> list = new ArrayList<>();
        CasingSets.getSets().stream()
            .filter(CasingSet::doesGenerateMixer)
            .filter(set -> Objects.nonNull(set.getMixer()))
            .forEach(set -> list.add(() -> new ItemStack(set.getMixer())));
        return list;
    }
}
