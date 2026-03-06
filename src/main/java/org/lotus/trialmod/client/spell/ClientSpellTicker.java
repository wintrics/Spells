package org.lotus.trialmod.client.spell;

import net.minecraft.client.Minecraft;
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
        if (stage.equals(SpellStage.CASTING) || stage.equals(SpellStage.COOLDOWN)) {
            add(new PlayerSpellData(
                    id, stage, stageStartTick, cooldownStartTick
            ));
        } else if (stage.equals(SpellStage.ACTIVE)) {
            PlayerSpellData old = get(id);
            Spell spell = SpellRegister.get(id);
            spell.effect(MC.player);
            if (old != null && old.stage().equals(SpellStage.CASTING)
                    && MC.player != null) {
                spell.clientCastingEnd(MC.player);
            }
            add(new PlayerSpellData(
                    id, stage, stageStartTick, cooldownStartTick
            ));
        } else if (stage.equals(SpellStage.END) || stage.equals(SpellStage.CANCEL)) {
            PlayerSpellData old = get(id);
            if (old != null && MC.player != null) {
                Spell spell = SpellRegister.get(id);
                if (spell != null) {
                    if (old.stage().equals(SpellStage.CANCEL)) spell.clientCancel(MC.player);
                    else spell.clientEnd(MC.player);
                }
            }
        }
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

            if (data.stage().equals(SpellStage.CASTING)) {
                if (spell.getCastTicks() > 0 && execute < spell.getCastTicks()) {
                    spell.clientCastingTick(MC.player, execute);
                }
                // TODO: добавить обработку
            } else {
                if (spell.getDurationTicks() > 0 && execute < spell.getDurationTicks()) {
                    spell.clientTick(MC.player, execute);
                }
                // TODO: добавить обработку
            }
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
