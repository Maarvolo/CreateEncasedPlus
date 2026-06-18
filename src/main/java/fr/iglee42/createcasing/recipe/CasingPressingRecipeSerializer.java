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

public class CasingPressingRecipeSerializer implements RecipeSerializer<CasingPressingRecipe> {

    private final MapCodec<CasingPressingRecipe> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, CasingPressingRecipe> streamCodec;

    public CasingPressingRecipeSerializer(IRecipeTypeInfo typeInfo) {
        this.codec = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                ProcessingRecipeParams.CODEC.forGetter(r -> (ProcessingRecipeParams) r.getParams()),
                ResourceLocation.CODEC.optionalFieldOf("press")
                    .forGetter(r -> Optional.ofNullable(r.getPressFilter()))
            ).apply(instance, (params, pressOpt) ->
                new CasingPressingRecipe(typeInfo, params, pressOpt.orElse(null))
            )
        );

        this.streamCodec = StreamCodec.of(
            (buf, recipe) -> {
                ProcessingRecipeParams.STREAM_CODEC.encode(buf, (ProcessingRecipeParams) recipe.getParams());
                buf.writeBoolean(recipe.getPressFilter() != null);
                if (recipe.getPressFilter() != null)
                    ResourceLocation.STREAM_CODEC.encode(buf, recipe.getPressFilter());
            },
            buf -> {
                ProcessingRecipeParams params = ProcessingRecipeParams.STREAM_CODEC.decode(buf);
                ResourceLocation pressFilter = buf.readBoolean() ? ResourceLocation.STREAM_CODEC.decode(buf) : null;
                return new CasingPressingRecipe(typeInfo, params, pressFilter);
            }
        );
    }

    @Override
    public MapCodec<CasingPressingRecipe> codec() {
        return codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CasingPressingRecipe> streamCodec() {
        return streamCodec;
    }
}
