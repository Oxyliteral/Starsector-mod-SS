package hullmods;

import java.util.HashMap;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class o_sspecial_unlock_flux_c extends BaseHullMod {

    public static final int FLUX_PER_OP = 200;

    public int getValueIncrease(HullSize hullSize) {
        var spec = Global.getSettings().getHullModSpec(this.getClass().getSimpleName());
        int cost = 0;
        if (spec != null) {
            cost = spec.getCostFor(hullSize);
        }
        return FLUX_PER_OP * cost;
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFluxCapacity().modifyFlat(id, getValueIncrease(hullSize));
    }

    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + getValueIncrease(HullSize.FRIGATE);
        if (index == 1) return "" + getValueIncrease(HullSize.DESTROYER);
        if (index == 2) return "" + getValueIncrease(HullSize.CRUISER);
        if (index == 3) return "" + getValueIncrease(HullSize.CAPITAL_SHIP);
        return null;
    }
}










