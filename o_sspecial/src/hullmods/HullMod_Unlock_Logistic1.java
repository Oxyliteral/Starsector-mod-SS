package hullmods;

import java.util.HashMap;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class HullMod_Unlock_Logistic1 extends BaseHullMod {


    private static HashMap<HullSize, Float> mag = new HashMap<HullSize, Float>();
    static {
        mag.put(HullSize.FRIGATE, 100f);
        mag.put(HullSize.DESTROYER, 100f);
        mag.put(HullSize.CRUISER, 100f);
        mag.put(HullSize.CAPITAL_SHIP, 100f);
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (stats.getVariant() != null) {
            float mod = (Float) mag.get(hullSize);
            stats.getCargoMod().modifyPercent(id, mod);
            stats.getFuelMod().modifyPercent(id, mod);
            stats.getMaxCrewMod().modifyPercent(id, mod);
            stats.getSuppliesPerMonth().modifyPercent(id, mod);
            stats.getFuelUseMod().modifyPercent(id, mod);
            stats.getMinCrewMod().modifyPercent(id, mod);
            stats.getPeakCRDuration().modifyPercent(id, mod);
            var percent = stats.getCRPerDeploymentPercent().computeEffective(stats.getVariant().getHullSpec().getCRToDeploy());
            stats.getCRPerDeploymentPercent().modifyFlat(id, -percent * 0.5f);
        }

    }

    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
        if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
        if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
        if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
        return null;
    }

}




