package hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.apache.log4j.Priority;

import java.util.HashMap;

public class o_sspecial_unlock_handling extends BaseHullMod {

    private static HashMap<ShipAPI.HullSize, Float> mag = new HashMap<ShipAPI.HullSize, Float>();
    static {
        mag.put(ShipAPI.HullSize.FRIGATE, 10f);
        mag.put(ShipAPI.HullSize.DESTROYER, 10f);
        mag.put(ShipAPI.HullSize.CRUISER, 10f);
        mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 10f);
    }

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        Global.getLogger(this.getClass()).log(Priority.INFO, id);
        if (stats.getVariant() != null) {
            float mod = (Float) mag.get(hullSize);
            stats.getMaxSpeed().modifyPercent(id, mod);
            stats.getAcceleration().modifyPercent(id, mod);
            stats.getDeceleration().modifyPercent(id, mod);
            stats.getMaxTurnRate().modifyPercent(id, mod);
            stats.getTurnAcceleration().modifyPercent(id, mod);
            stats.getZeroFluxSpeedBoost().modifyPercent(id, mod);
            stats.getMaxBurnLevel().modifyFlat(id, 1f);
        }

    }

    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "" + ((Float) mag.get(ShipAPI.HullSize.FRIGATE)).intValue();
        if (index == 1) return "" + ((Float) mag.get(ShipAPI.HullSize.DESTROYER)).intValue();
        if (index == 2) return "" + ((Float) mag.get(ShipAPI.HullSize.CRUISER)).intValue();
        if (index == 3) return "" + ((Float) mag.get(ShipAPI.HullSize.CAPITAL_SHIP)).intValue();
        return null;
    }
}
