package org.lotus.trialmod.client.spell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.api.SpellStage;
import org.lotus.trialmod.spell.capability.PlayerSpellData;
import org.lotus.trialmod.spell.registry.SpellRegister;

import java.util.ArrayList;
import java.util.List;

public class ClientSpellTicker {
    private static final List<PlayerSpellData> SPELL_DATA = new ArrayList<>();
    private static final Minecraft MC = Minecraft.getInstance();

    public static void onStage(ResourceLocation id, SpellStage stage,
                               long stageStartTick, long cooldownStartTick) {
    	TrialMod.LOGGER.debug("spell {} stage {} start tick {} cooldown start tick {}", id, stage, stageStartTick, cooldownStartTick);
    	
    	PlayerSpellData oldData = get(id);
    	Spell spell = SpellRegister.get(id);
    	
    	if (MC.player == null || spell == null) return;
    	
    	switch (stage) {
		case  COOLDOWN: {
			if (oldData != null) stage = oldData.stage();
		} case ACTIVE: {
				if (oldData != null) {
					if (!oldData.stage().equals(SpellStage.ACTIVE)) spell.effect(MC.player);
					if (oldData.stage().equals(SpellStage.CASTING)) spell.clientCastingEnd(MC.player);
				}
		} case END, CANCEL: {
			if (oldData != null) {
				if (oldData.stage().equals(SpellStage.END)) spell.clientEnd(MC.player);
				else spell.clientCancel(MC.player);
			}
		}
		default:
			break;
		}
    	
    	add(new PlayerSpellData(
    			id, stage, stageStartTick, cooldownStartTick
    	));
    }

    public static void tick() {
        if (MC.player == null || MC.level == null) return;

        long current = MC.level.getGameTime();

        for (int i = SPELL_DATA.size() - 1; i >= 0; i--) {
            var data = SPELL_DATA.get(i);
            var spell = SpellRegister.get(data.id());
            if (spell == null) {
                TrialMod.LOGGER.error("spell id {} not found", data.id());
                remove(data.id());
                continue;
            }

            int execute = (int) (current - data.startTick());
            
            switch (data.stage()) {
			case CASTING: tickCasting(spell, MC.player, execute);
			case ACTIVE: tickActive(spell, MC.player, execute);
			default: {}
            }
        }
    }
    
    private static void tickCasting(Spell spell, LocalPlayer player, int  execute) {
    	if (spell.getCastTicks() > 0 && execute < spell.getCastTicks()) {
    		spell.clientCastingTick(player, execute);
    	}
    }
    
    private static void tickActive(Spell spell, LocalPlayer player, int execute) {
    	if (spell.getDurationTicks() > 0 && execute < spell.getDurationTicks()) {
    		spell.clientTick(player, execute);
    	}
    }

    private static void add(PlayerSpellData data) {
        remove(data.id());
        SPELL_DATA.add(data);
    }

    private static PlayerSpellData get(ResourceLocation id) {
        return SPELL_DATA.stream()
                .filter(i -> i.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    private static void remove(ResourceLocation id) {
        SPELL_DATA.removeIf(i -> i.id().equals(id));
    }
    
    public static List<PlayerSpellData> getSpellData() {
		return SPELL_DATA;
	}
}
