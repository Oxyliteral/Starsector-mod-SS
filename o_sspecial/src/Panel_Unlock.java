import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Sounds;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.ui.*;
import lunalib.lunaUI.elements.LunaElement;
import lunalib.lunaUI.elements.LunaTextfield;
import lunalib.lunaRefit.BaseRefitButton;
import lunalib.lunaUI.elements.LunaToggleButton;
import settings.Settings_Mod;

import java.util.ArrayList;
import java.lang.String;

public class Panel_Unlock extends BaseRefitButton {

    public Object[][] Buttons;

    @Override
    public void initPanel(CustomPanelAPI backgroundPanel, FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        float width = getPanelWidth(member, variant);
        float height = getPanelHeight(member, variant);

        TooltipMakerAPI element = backgroundPanel.createUIElement(width, height, false);
        backgroundPanel.addUIElement(element);

        Object[][] settings = Settings_Mod.Unlock_Panel_GetButtonParams();
        Buttons = new Object[settings.length][];
        float fieldWidth = width / settings.length;
        for (int i = 0; i < settings.length; i++) {
            String header = (String)settings[i][0];
            LunaTextfield field = new LunaTextfield(header, false, Global.getSettings().getBasePlayerColor(), element, fieldWidth, height * 0.1f);
            field.getPosition().inTL(i * fieldWidth, 0f);
            int max = (int)settings[i][2];
            var weapons = variant.getHullSpec().getAllWeaponSlotsCopy();
            if (header.equals("Weapons")) {
                if (weapons.size() < max) {
                    max = weapons.size();
                }
            }
            int storyPointCost = (int)settings[i][3];
            float buttonHeight = Math.min(height * 0.1f, Math.max(height * 0.01f, height * 0.9f / (float)max));
            Buttons[i] = new Object[max];
            for (int x = 0; x < max; x++) {
                String mod = ((String)settings[i][1]).formatted((x + 1));
                LunaToggleButton toggle = new LunaToggleButton(variant.hasHullMod(mod), element, fieldWidth, buttonHeight);
                toggle.getPosition().inTL(i * fieldWidth, x * buttonHeight + height * 0.1f);
                int opCost = 0;
                var hullModSpec = Global.getSettings().getHullModSpec(mod);
                if (hullModSpec != null) {
                    opCost = hullModSpec.getCostFor(variant.getHullSize());
                    toggle.addTooltip(hullModSpec.getDescription(variant.getHullSize()),width, TooltipMakerAPI.TooltipLocation.BELOW, "");
                }
                String text = "(" + opCost + " OP)";
                if (header.equals("Weapons")) {
                    WeaponSlotAPI weapon = weapons.get(x);
                    text += ":" + weapon.getSlotSize().toString().substring(0, 2) + "_" + weapon.getWeaponType().toString().substring(0, 3);
                    toggle.setCustomData("weaponslot", weapon.getId());
                }
                String toggledText = text;
                if (storyPointCost > 0) {
                    text = "(" + storyPointCost + " SP)";
                    toggledText = Settings_Mod.Unlock_Panel_GetStoryPointWarning().formatted(storyPointCost);
                    toggle.setCustomData("storypointcost", "" + storyPointCost);
                }
                toggle.changeStateText(toggledText, text);
                toggle.centerText();
                toggle.setCustomData("hullmod", mod);
                String addHullMod = (String)settings[i][4];
                if (!addHullMod.isEmpty()) {
                    toggle.setCustomData("addhullmod", addHullMod.formatted((x + 1)));
                }
                if (settings[i][5] != null) {
                    Object[] requirements = (Object[])settings[i][5];
                    int index = x + (int)requirements[1];
                    if (index >= 0) {
                        String req = ((String)requirements[0]).formatted(index);
                        toggle.setCustomData("requirementhullmod", req);
                    }
                }
                Buttons[i][x] = toggle;
            }
        }
    }

    public void CheckToggle(FleetMemberAPI member, ShipVariantAPI variant) {
        if (Buttons == null)
            return;
        var player = Global.getSector().getPlayerPerson();
        if (player == null)
            return;
        for (int i = 0; i < Buttons.length; i++) {
            for (int x = 0; x < Buttons[i].length; x++) {
                if (Buttons[i][x] instanceof LunaToggleButton e) {
                    String mod = (String) e.getCustomData("hullmod");
                    String requirementMod = "";
                    Object requirementHullModData = e.getCustomData("requirementhullmod");
                    if (requirementHullModData != null) {
                        requirementMod = (String)requirementHullModData;
                    }
                    if (e.getValue() && !requirementMod.isEmpty() && !variant.hasHullMod(requirementMod)) {
                        continue;
                    }
                    String addHullMod = "";
                    Object addHullModData = e.getCustomData("addhullmod");
                    if (addHullModData != null) {
                        addHullMod = (String)addHullModData;
                    }
                    Object storyPointCostData = e.getCustomData("storypointcost");
                    int storyPointCost = 0;
                    if (storyPointCostData != null) {
                        storyPointCost = Integer.parseInt((String)storyPointCostData);
                    }
                    if (variant.hasHullMod(mod)) {
                        if (storyPointCost <= 0 && !e.getValue()) {
                            variant.removeMod(mod);
                            String weaponSlot = "";
                            Object weaponSlotData = e.getCustomData("weaponslot");
                            if (weaponSlotData != null) {
                                weaponSlot = (String)weaponSlotData;
                            }
                            if (!weaponSlot.isEmpty()) {
                                String weaponId = variant.getWeaponId(weaponSlot);
                                if (weaponId != null && !weaponId.isEmpty()) {
                                    player.getFleet().getCargo().addWeapons(weaponId, 1);
                                    variant.clearSlot(weaponSlot);
                                }
                            }
                        }
                    }
                    else if (e.getValue()) {
                        if (player.getStats().getStoryPoints() < storyPointCost)
                            continue;
                        if (storyPointCost > 0) {
                            player.getStats().spendStoryPoints(storyPointCost, true, null, true, Settings_Mod.Unlock_Panel_GetStoryPointMessage().formatted(storyPointCost));
                            variant.addPermaMod(mod, true);
                            Global.getSoundPlayer().playUISound(Sounds.STORY_POINT_SPEND, 1f, 1f);
                        }
                        else {
                            variant.addMod(mod);
                        }
                        if (!addHullMod.isEmpty()) {
                            variant.addMod(addHullMod);
                        }
                    }
                }
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
