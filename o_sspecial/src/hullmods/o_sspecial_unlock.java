package hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import settings.Settings_Mod;

public class o_sspecial_unlock extends BaseHullMod {

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        var player = Global.getSector().getPlayerFleet();
        if (player == null) {
            return;
        }
        ShipVariantAPI variant = stats.getVariant();
        var spec = variant.getHullSpec();
        if (spec != null) {
            for (var tag : spec.getTags()) {
                if (!variant.hasTag(tag)) {
                    variant.addTag(tag);
                }
            }
        }
        var weapons = variant.getHullSpec().getAllWeaponSlotsCopy();
        for (int i = 0; i < weapons.size(); i++) {
            String mod = Settings_Mod.Unlock_Panel_Weapons_GetId(i);
            String weaponSlot = weapons.get(i).getId();
            String weaponId = variant.getWeaponId(weaponSlot);
            if (weaponId == null || weaponId.isEmpty()) {
                if (variant.hasHullMod(mod)) {
                    variant.removeMod(mod);
                }
            }
            else {
                if (!variant.hasHullMod(mod)) {
                    variant.addMod(mod);
                }
            }
        }
    }
}
