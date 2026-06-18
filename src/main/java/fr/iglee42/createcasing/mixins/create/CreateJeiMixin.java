package fr.iglee42.createcasing.mixins.create;

import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.category.*;
import fr.iglee42.createcasing.casings.CasingSet;
import fr.iglee42.createcasing.casings.CasingSets;
import fr.iglee42.createcasing.config.ModConfigs;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(value = CreateJEI.class,remap = false)
public class CreateJeiMixin {

    @Shadow @Final private List<CreateRecipeCategory<?>> allCategories;

    @Inject(method = "registerRecipeCatalysts",at = @At("RETURN"))
    private void inject(IRecipeCatalystRegistration registration, CallbackInfo ci){

        for (CreateRecipeCategory<?> c : this.allCategories) {
            if (c instanceof DeployingCategory)
                CasingSets.getSets().stream().filter(set-> Objects.nonNull(set.getDeployer())).filter(CasingSet::doesGenerateDeployer)
                        .forEach(set->registration.addRecipeCatalyst(set.getDeployer(),c.getRecipeType()));

            if (c instanceof SawingCategory || c instanceof BlockCuttingCategory)
                CasingSets.getSets().stream().filter(set-> Objects.nonNull(set.getSaw())).filter(CasingSet::doesGenerateSaw)
                        .forEach(set->registration.addRecipeCatalyst(set.getSaw(),c.getRecipeType()));

            if (c instanceof ProcessingViaFanCategory)
                CasingSets.getSets().stream().filter(set-> Objects.nonNull(set.getEncasedFan())).filter(CasingSet::doesGenerateEncasedFan)
                        .forEach(set->registration.addRecipeCatalyst(set.getEncasedFan(),c.getRecipeType()));

            if ((c instanceof PressingCategory || c instanceof PackingCategory) && ModConfigs.common().kinetics.customPressesUseStandardRecipes.get())
                CasingSets.getSets().stream().filter(CasingSet::doesGeneratePress).filter(set-> Objects.nonNull(set.getPress()))
                        .forEach(set->registration.addRecipeCatalyst(new ItemStack(set.getPress()),c.getRecipeType()));

            if (c instanceof MixingCategory && ModConfigs.common().kinetics.customMixersUseStandardRecipes.get())
                CasingSets.getSets().stream().filter(CasingSet::doesGenerateMixer).filter(set-> Objects.nonNull(set.getMixer()))
                        .forEach(set->registration.addRecipeCatalyst(new ItemStack(set.getMixer()),c.getRecipeType()));
        }
    }

}
