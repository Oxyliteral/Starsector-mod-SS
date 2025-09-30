package hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import settings.Settings_Mod;

public class HullMod_Unlock extends BaseHullMod {

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        var player = Global.getSector().getPlayerFleet();
        if (player == null) {
            return;
        }
        ShipVariantAPI variant = stats.getVariant();
        var weapons = variant.getHullSpec().getAllWeaponSlotsCopy();
        for (int i = 0; i < weapons.size(); i++) {
            String mod = Settings_Mod.Unlock_Panel_Weapons_GetId(i);
            if (!variant.hasHullMod(mod)) {
                String weaponSlot = weapons.get(i).getId();
                String weaponId = variant.getWeaponId(weaponSlot);
                if (weaponId == null || weaponId == "")
                    continue;
                variant.addMod(mod);
            }
        }
    }
}
