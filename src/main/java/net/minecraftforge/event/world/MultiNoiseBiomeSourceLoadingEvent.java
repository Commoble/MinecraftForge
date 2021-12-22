package net.minecraftforge.event.world;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate.ParameterPoint;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

/**
 * Event for modifying multi-noise biome sources.
 * This event is fired whenever a MultiNoiseBiomeSource is deserialized during dimension loading.
 * This occurs after datapacks and worldgen registries load, but before chunk generators are constructed
 * (and before the biome source itself is constructed).
 * Standard tag wrappers are not resolvable at this time.
 * 
 * Best practice for use of this event:
 * Additions during {@link EventPriority#HIGH}
 * Removals during {@link EventPriority#NORMAL}
 * Modifications during {@link EventPriority#LOW}
 * 
 * This event is cancellable; cancelling the event will prevent the biome source from being modified.
 * 
 * This event is fired on the {@link MinecraftForge.EVENT_BUS}.
 */
public class MultiNoiseBiomeSourceLoadingEvent extends Event
{
    private final List<Pair<ParameterPoint, Supplier<Biome>>> parameters;
    private final @Nullable ResourceLocation name;
    private final RegistryAccess registries;
    
    public MultiNoiseBiomeSourceLoadingEvent(List<Pair<ParameterPoint, Supplier<Biome>>> parameters, ResourceLocation name, RegistryAccess registries)
    {
        this.parameters = parameters;
        this.name = name;
        this.registries = registries;
    }

    /**
     * @return Mutable parameter list that can be modified as needed.
     */
    public List<Pair<ParameterPoint, Supplier<Biome>>> getParameters()
    {
        return parameters;
    }

    /**
     * Retrieves the name of this biome source.
     * For vanilla biome sources and preset-based biome sources, this will be the name of the preset (typically minecraft:overworld/nether/end).
     * Non-preset biome sources may specify the preset ID in the biome source json object with a "name" field.
     * When present, this should generally match the ID of the dimension this biome source belongs to.
     * If this is not present, this indicates that a datapack has defined this biome source and has not specified a name.
     * @return Biome source name, if present.
     */
    public @Nullable ResourceLocation getName()
    {
        return name;
    }

    /**
     * Retrieves the worldgen/dynamic registries, for reading biomes and other worldgen data.
     * @return Worldgen/dynamic registries
     */
    public RegistryAccess getRegistries()
    {
        return registries;
    }    
}
