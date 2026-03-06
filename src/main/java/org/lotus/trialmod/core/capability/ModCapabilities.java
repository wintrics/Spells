package org.lotus.trialmod.core.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.lotus.trialmod.spell.capability.ISpellData;

public class ModCapabilities {
    public static final Capability<ISpellData> SPELL_DATA = CapabilityManager.get(new CapabilityToken<>(){});
}
