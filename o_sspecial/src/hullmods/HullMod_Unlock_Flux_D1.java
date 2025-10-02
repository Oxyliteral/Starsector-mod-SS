package hullmods;

import java.util.HashMap;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class HullMod_Unlock_Flux_D1 extends BaseHullMod {

    public static HashMap<HullSize, Float> mag = new HashMap<HullSize, Float>();
    static {
        mag.put(HullSize.FRIGATE, 100f);
        mag.put(HullSize.DESTROYER, 250f);
        mag.put(HullSize.CRUISER, 400f);
        mag.put(HullSize.CAPITAL_SHIP, 600f);
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        float dissipation = (Float) mag.get(hullSize);
        stats.getFluxDissipation().modifyFlat(id, dissipation);
    }

    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
        if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
        if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
        if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
        return null;
    }


}
