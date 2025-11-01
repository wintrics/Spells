package org.lotus.trialmod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;

@Mod(TrialMod.MODID)
public class TrialMod {
    public static final String MODID = "trialmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TrialMod() {
        LanguageAdapter.registerMod(MODID);
    }
}
