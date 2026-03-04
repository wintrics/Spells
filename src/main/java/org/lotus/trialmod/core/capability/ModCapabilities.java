package org.lotus.trialmod.core.capability;

import org.lotus.trialmod.spell.capability.ISpellData;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class ModCapabilities {
    private ModCapabilities(){}

    public static final Capability<ISpellData> SPELL_DATA = CapabilityManager.get(new CapabilityToken<>(){});
}
