package hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

import java.util.HashMap;

public class o_sspecial_unlock_cr extends BaseHullMod {

    private static HashMap<ShipAPI.HullSize, Float> mag = new HashMap<ShipAPI.HullSize, Float>();
    static {
        mag.put(ShipAPI.HullSize.FRIGATE, 100f);
        mag.put(ShipAPI.HullSize.DESTROYER, 100f);
        mag.put(ShipAPI.HullSize.CRUISER, 100f);
        mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 100f);
    }

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (stats.getVariant() != null) {
            float mod = (Float) mag.get(hullSize);
            stats.getPeakCRDuration().modifyPercent(id, mod);
            stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, mod);
            stats.getCRPerDeploymentPercent().modifyMult(id, 0.5f);
            stats.getCRLossPerSecondPercent().modifyMult(id, 0.5f);
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
