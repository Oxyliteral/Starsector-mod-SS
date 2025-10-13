package hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.mission.FleetSide;
import org.apache.log4j.Priority;

import java.util.HashMap;

public class o_sspecial_unlock_ecm extends BaseHullMod {

    private static HashMap<ShipAPI.HullSize, Float> mag = new HashMap<ShipAPI.HullSize, Float>();
    static {
        mag.put(ShipAPI.HullSize.FRIGATE, 100f);
        mag.put(ShipAPI.HullSize.DESTROYER, 100f);
        mag.put(ShipAPI.HullSize.CRUISER, 100f);
        mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 100f);
    }
    public static int ECM = 1;

    public static int DP_PER_ECM = 50;

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (stats.getVariant() != null) {
            float mod = (Float) mag.get(hullSize);
            stats.getSensorStrength().modifyPercent(id, mod);
            stats.getSensorProfile().modifyMult(id, 0.5f);
        }

    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        var playerFleet = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER);
        var max = playerFleet.getMaxStrength();
        var strength = playerFleet.getCurrStrength();
        var ecm = (max - strength) / DP_PER_ECM * ECM;

        if (ecm > 0) {
            for (var mod : ship.getVariant().getHullMods()) {
                if (mod.startsWith(this.getClass().getSimpleName())) {
                    ship.getMutableStats().getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(mod, ecm);
                }

            }

        }
    }

    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "" + ((Float) mag.get(ShipAPI.HullSize.FRIGATE)).intValue();
        if (index == 1) return "" + ((Float) mag.get(ShipAPI.HullSize.DESTROYER)).intValue();
        if (index == 2) return "" + ((Float) mag.get(ShipAPI.HullSize.CRUISER)).intValue();
        if (index == 3) return "" + ((Float) mag.get(ShipAPI.HullSize.CAPITAL_SHIP)).intValue();
        if (index == 4) return "" + ECM;
        if (index == 5) return "" + DP_PER_ECM;
        return null;
    }
}
