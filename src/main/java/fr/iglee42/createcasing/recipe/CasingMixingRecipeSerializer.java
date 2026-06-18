package fr.iglee42.createcasing.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Optional;

public class CasingMixingRecipeSerializer implements RecipeSerializer<CasingMixingRecipe> {

    private final MapCodec<CasingMixingRecipe> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, CasingMixingRecipe> streamCodec;

    public CasingMixingRecipeSerializer(IRecipeTypeInfo typeInfo) {
        this.codec = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                ProcessingRecipeParams.CODEC.forGetter(r -> (ProcessingRecipeParams) r.getParams()),
                ResourceLocation.CODEC.optionalFieldOf("mixer")
                    .forGetter(r -> Optional.ofNullable(r.getMixerFilter()))
            ).apply(instance, (params, mixerOpt) ->
                new CasingMixingRecipe(typeInfo, params, mixerOpt.orElse(null))
            )
        );

        this.streamCodec = StreamCodec.of(
            (buf, recipe) -> {
                ProcessingRecipeParams.STREAM_CODEC.encode(buf, (ProcessingRecipeParams) recipe.getParams());
                buf.writeBoolean(recipe.getMixerFilter() != null);
                if (recipe.getMixerFilter() != null)
                    ResourceLocation.STREAM_CODEC.encode(buf, recipe.getMixerFilter());
            },
            buf -> {
                ProcessingRecipeParams params = ProcessingRecipeParams.STREAM_CODEC.decode(buf);
                ResourceLocation mixerFilter = buf.readBoolean() ? ResourceLocation.STREAM_CODEC.decode(buf) : null;
                return new CasingMixingRecipe(typeInfo, params, mixerFilter);
            }
        );
    }

    @Override
    public MapCodec<CasingMixingRecipe> codec() {
        return codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CasingMixingRecipe> streamCodec() {
        return streamCodec;
    }
}
