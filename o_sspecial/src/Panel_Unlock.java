import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.ui.*;
import lunalib.lunaUI.elements.LunaTextfield;
import lunalib.lunaRefit.BaseRefitButton;
import lunalib.lunaUI.elements.LunaToggleButton;
import settings.Settings_Mod;

import java.util.ArrayList;
import java.lang.String;

public class Panel_Unlock extends BaseRefitButton {

    public ArrayList<LunaToggleButton> Toggle_Weapons = new ArrayList<>();


    @Override
    public void initPanel(CustomPanelAPI backgroundPanel, FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        float width = getPanelWidth(member, variant);
        float height = getPanelHeight(member, variant);

        TooltipMakerAPI element = backgroundPanel.createUIElement(width, height, false);
        backgroundPanel.addUIElement(element);

        LunaTextfield weaponsField = new LunaTextfield("Weapons", false, Global.getSettings().getBasePlayerColor(), element, width * 0.25f, height * 0.1f);
        weaponsField.getPosition().inTL(0f,0f);
        LunaTextfield logisticsField = new LunaTextfield("Logistics", false, Global.getSettings().getBasePlayerColor(), element, width * 0.25f, height * 0.1f);
        logisticsField.getPosition().inTL(width * 0.25f,0f);
        LunaTextfield opField = new LunaTextfield("OP", false, Global.getSettings().getBasePlayerColor(), element, width * 0.25f, height * 0.1f);
        opField.getPosition().inTL(width * 0.5f,0f);

        for (int i = Toggle_Weapons.size() - 1; i >= 0; i--) {
            element.removeComponent(Toggle_Weapons.get(i).getElementPanel());
            Toggle_Weapons.remove(i);
        }
        var weapons = variant.getHullSpec().getAllWeaponSlotsCopy();
        float buttonHeight = Math.min(height * 0.1f, Math.max(height * 0.01f, height * 0.9f / (float)weapons.size()));
        for (int i = 0; i < weapons.size(); i++) {
            WeaponSlotAPI weapon = weapons.get(i);
            String mod = Settings_Mod.Unlock_Panel_Weapons_GetId(i);
            LunaToggleButton toggle = new LunaToggleButton(variant.hasHullMod(mod), element, width * 0.25f, buttonHeight);
            toggle.getPosition().inTL(0f, buttonHeight * (Toggle_Weapons.size()) + height * 0.1f);
            String text = "(" + Settings_Mod.Unlock_Panel_Weapons_GetOpCost() + " OP): " + weapon.getSlotSize().toString().substring(0, 2) + "_" + weapon.getWeaponType().toString().substring(0, 3);
            toggle.changeStateText(text, text);
            toggle.centerText();
            Toggle_Weapons.add(toggle);
        }
    }

    public void CheckToggle(FleetMemberAPI member, ShipVariantAPI variant) {
        for (int i = 0; i < Toggle_Weapons.size(); i++) {
            String mod = Settings_Mod.Unlock_Panel_Weapons_GetId(i);
            if (Toggle_Weapons.get(i).getValue()) {
                variant.addMod(mod);
            }
            else if (variant.hasHullMod(mod)) {
                variant.removeMod(mod);
            }
        }
        refreshVariant();
    }

    @Override
    public float getPanelWidth(FleetMemberAPI member, ShipVariantAPI variant) {
        return Settings_Mod.Unlock_Panel_GetWidth();
    }

    @Override
    public float getPanelHeight(FleetMemberAPI member, ShipVariantAPI variant) {
        return Settings_Mod.Unlock_Panel_GetHeight();
    }

    @Override
    public String getButtonName(FleetMemberAPI member, ShipVariantAPI variant) {
        return super.getButtonName(member, variant);
    }

    @Override
    public String getIconName(FleetMemberAPI member, ShipVariantAPI variant) {
        return super.getIconName(member, variant);
    }

    @Override
    public void addTooltip(TooltipMakerAPI tooltip, FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        super.addTooltip(tooltip, member, variant, market);
    }

    @Override
    public boolean hasPanel(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return true;
    }

    @Override
    public boolean hasTooltip(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return true;
    }

    @Override
    public boolean shouldShow(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return true;
    }

    @Override
    public boolean isClickable(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return true;
    }

    @Override
    public void onPanelClose(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        CheckToggle(member, variant);
    }
}
