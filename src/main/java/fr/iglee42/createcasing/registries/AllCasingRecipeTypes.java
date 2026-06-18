package fr.iglee42.createcasing.registries;

import java.util.Optional;

import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import fr.iglee42.createcasing.CreateCasing;
import fr.iglee42.createcasing.recipe.CasingMixingRecipe;
import fr.iglee42.createcasing.recipe.CasingMixingRecipeSerializer;
import fr.iglee42.createcasing.recipe.CasingPressingRecipe;
import fr.iglee42.createcasing.recipe.CasingPressingRecipeSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllCasingRecipeTypes {

    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER =
        DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, CreateCasing.MODID);
    private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER =
        DeferredRegister.create(Registries.RECIPE_TYPE, CreateCasing.MODID);

    public static final ResourceLocation CASING_PRESSING_ID = CreateCasing.asResource("casing_pressing");
    public static final ResourceLocation CASING_MIXING_ID = CreateCasing.asResource("casing_mixing");

    public static final DeferredHolder<RecipeType<?>, RecipeType<?>> CASING_PRESSING_TYPE =
        TYPE_REGISTER.register("casing_pressing", () -> RecipeType.simple(CASING_PRESSING_ID));

    public static final DeferredHolder<RecipeType<?>, RecipeType<?>> CASING_MIXING_TYPE =
        TYPE_REGISTER.register("casing_mixing", () -> RecipeType.simple(CASING_MIXING_ID));

    private static final IRecipeTypeInfo CASING_PRESSING_TYPE_INFO = new IRecipeTypeInfo() {
        @Override
        public ResourceLocation getId() {
            return CASING_PRESSING_ID;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends RecipeSerializer<?>> T getSerializer() {
            return (T) CASING_PRESSING_SERIALIZER.get();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
            return (RecipeType<R>) CASING_PRESSING_TYPE.get();
        }
    };

    private static final IRecipeTypeInfo CASING_MIXING_TYPE_INFO = new IRecipeTypeInfo() {
        @Override
        public ResourceLocation getId() {
            return CASING_MIXING_ID;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends RecipeSerializer<?>> T getSerializer() {
            return (T) CASING_MIXING_SERIALIZER.get();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
            return (RecipeType<R>) CASING_MIXING_TYPE.get();
        }
    };

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CASING_PRESSING_SERIALIZER =
        SERIALIZER_REGISTER.register("casing_pressing",
            () -> new CasingPressingRecipeSerializer(CASING_PRESSING_TYPE_INFO));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CASING_MIXING_SERIALIZER =
        SERIALIZER_REGISTER.register("casing_mixing",
            () -> new CasingMixingRecipeSerializer(CASING_MIXING_TYPE_INFO));

    public static IRecipeTypeInfo getCasingPressingInfo() {
        return CASING_PRESSING_TYPE_INFO;
    }

    public static IRecipeTypeInfo getCasingMixingInfo() {
        return CASING_MIXING_TYPE_INFO;
    }

    @SuppressWarnings("unchecked")
    public static Optional<RecipeHolder<PressingRecipe>> findCasingPressing(SingleRecipeInput inv, Level level, ResourceLocation pressBlockId) {
        var all = level.getRecipeManager()
            .getAllRecipesFor((RecipeType<PressingRecipe>) CASING_PRESSING_TYPE.get());
        for (var holder : all) {
            if (holder.value() instanceof CasingPressingRecipe cpr) {
                if (!cpr.matchesPress(pressBlockId))
                    continue;
            }
            if (holder.value().matches(inv, level))
                return Optional.of(holder);
        }
        return Optional.empty();
    }

    public static void register(IEventBus modEventBus) {
        SERIALIZER_REGISTER.register(modEventBus);
        TYPE_REGISTER.register(modEventBus);
    }
}
