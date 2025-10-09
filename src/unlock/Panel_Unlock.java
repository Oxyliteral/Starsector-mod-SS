package unlock;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Sounds;
import com.fs.starfarer.api.ui.*;
import lunalib.backend.ui.components.util.TooltipHelper;
import lunalib.lunaRefit.BaseRefitButton;
import org.apache.log4j.Priority;
import org.json.JSONObject;
import settings.Settings_Mod;
import java.lang.String;

public class Panel_Unlock extends BaseRefitButton {

    public LunaToggleButton_Unlock[] CategoryButtons;

    public LunaToggleButton_Unlock[][] HullModButtons;

    public CustomPanelAPI BackgroundPanel;

    @Override
    public void initPanel(CustomPanelAPI backgroundPanel, FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        float width = getPanelWidth(member, variant);
        float height = getPanelHeight(member, variant);
        var color = Global.getSettings().getBasePlayerColor();

        BackgroundPanel = backgroundPanel;

        var categories = backgroundPanel.createUIElement(width, height * 0.25f, true);

        var toolTipText = backgroundPanel.createUIElement(width, height * 0.1f, true);
        var textField = toolTipText.addTextField(width, 0f);
        textField.setText(Global.getSettings().getString("o_sspecial", "unlock_panel_tooltip"));

        var Data = Settings_Mod.Unlock_GetData();
        int weaponMax = variant.getHullSpec().getAllWeaponSlotsCopy().size();
        try {
            CategoryButtons = new LunaToggleButton_Unlock[Data.length];
            HullModButtons = new LunaToggleButton_Unlock[Data.length][];
            for (int row = 1; row < Data.length; row++) {
                if (Data[row] == null)
                    continue;
                var obj = Data[row][0];
                if (obj == null) {
                    continue;
                }
                var type = obj.getInt("type");
                var name = obj.getString("name");
                var id = obj.getString("id");
                var categoryButton = new LunaToggleButton_Unlock(false, categories, Global.getSettings().computeStringWidth(name, Fonts.DEFAULT_SMALL) + 10f, height * 0.075f);
                categoryButton.getPosition().inTL(0f, 0f);
                if (CategoryButtons[row - 1] != null) {
                    var prevPanel = CategoryButtons[row - 1].getElementPanel();
                    if (prevPanel.getPosition().getX() + prevPanel.getPosition().getWidth() + categoryButton.getPosition().getWidth() < width) {
                        categoryButton.getPosition().rightOfMid(prevPanel, 0f);
                    }
                    else {
                        UIComponentAPI prevFirstPanel = null;
                        for (int i = row - 1; i >= 1; i--) {
                            var button = CategoryButtons[i];
                            if (button != null && button.getPosition().getX() == 0f) {
                                prevFirstPanel = button.getElementPanel();
                                break;
                            }
                        }
                        if (prevFirstPanel != null) {
                            categoryButton.getPosition().belowLeft(prevFirstPanel, 0f);
                        }
                    }
                }
                categoryButton.changeStateText(name, name);
                categoryButton.centerText();
                categoryButton.setCustomData("open", false);
                categoryButton.addTooltip(obj.getString("description"), width, TooltipMakerAPI.TooltipLocation.BELOW);
                CategoryButtons[row] = categoryButton;

                var categoryPanel = backgroundPanel.createUIElement(width, height * 0.75f, true);
                float[] outWidth = new float[1];
                Object[] params = Settings_Mod.Unlock_Panel_GetTableHeader(obj, categoryPanel, width, outWidth);
                float addWidth = outWidth[0];
                var table = categoryPanel.beginTable(color, color, color, height * 0.1f, params);

                int max = obj.getInt("max");
                if (type == 3 && max > weaponMax) {
                    max = weaponMax;
                }
                max++;
                HullModButtons[row] = new LunaToggleButton_Unlock[max];
                for (int i = 1; i < max; i++) {
                    var data_unlock = obj;
                    if (Data[row][i] != null) {
                        data_unlock = Data[row][i];
                    }
                    var hullModId = id + "_" + i;
                    var hullMod = Global.getSettings().getHullModSpec(hullModId);
                    int hullModType = 0;
                    var stringData = data_unlock.getString("type_mod");
                    if (!stringData.isEmpty()) {
                        hullModType = Integer.parseInt(stringData);
                    }
                    categoryPanel.addRow(Settings_Mod.Unlock_Panel_GetTableValues(data_unlock, variant, i, hullMod));
                    var tooltip = new TooltipHelper(hullMod.getDisplayName() + "\n" + hullMod.getDescription(variant.getHullSize()), width);
                    categoryPanel.addTooltipToAddedRow(tooltip, TooltipMakerAPI.TooltipLocation.BELOW);
                    var toggleButton = new LunaToggleButton_Unlock(variant.hasHullMod(hullModId), categoryPanel, addWidth - 3f, height * 0.1f);
                    toggleButton.getPosition().inTL(3f, height * 0.1f * (i - 1) + height * 0.05f + 2f);
                    if (type == 3 || (variant.hasHullMod(hullModId) && hullModType != 0)) {
                        toggleButton.setToggleable(false);
                        toggleButton.setHoverable(false);
                        String locked = Global.getSettings().getString("o_sspecial", "unlock_panel_button_locked");
                        toggleButton.changeStateText(locked, locked);
                    }
                    else {
                        toggleButton.changeStateText(Global.getSettings().getString("o_sspecial", "unlock_panel_button_enabled"),
                                Global.getSettings().getString("o_sspecial", "unlock_panel_button_disabled"));
                    }
                    if (toggleButton.getParagraph() != null) {
                        toggleButton.getParagraph().autoSizeToWidth(addWidth - 3f);
                    }
                    toggleButton.setCustomData("data_unlock", data_unlock);
                    toggleButton.setCustomData("id", hullModId);
                    toggleButton.setCustomData("tier", i);
                    HullModButtons[row][i] = toggleButton;
                }
                categoryPanel.addTable("No Data Found", 0, 0f);
                table.getPosition().inTL(0f, 0f);

                categoryButton.setCustomData("categoryPanel", categoryPanel);
            }
            backgroundPanel.addUIElement(categories);
            backgroundPanel.addUIElement(toolTipText).inTL(0f, height);
        }
        catch (Exception e) {
            Global.getLogger(Settings_Mod.class).log(Priority.ERROR, e);
        }
    }

    public void CheckToggle(FleetMemberAPI member, ShipVariantAPI variant) {
        if (HullModButtons == null)
            return;
        var player = Global.getSector().getPlayerPerson();
        if (player == null)
            return;
        boolean upgrade = false;
        try {
            for (int row = 0; row < HullModButtons.length; row++) {
                if (HullModButtons[row] == null)
                    continue;
                for (int col = 0; col < HullModButtons[row].length; col++) {
                    var button = HullModButtons[row][col];
                    if (button == null)
                        continue;
                    var data = button.getCustomData("data_unlock");
                    if (data instanceof JSONObject unlockData) {
                        int spCost = 0;
                        var stringData = unlockData.getString("cost_sp");
                        if (!stringData.isEmpty()) {
                            spCost = Integer.parseInt(stringData);
                        }
                        if (Global.getSector().getPlayerPerson().getStats().getStoryPoints() < spCost)
                            continue;
                        stringData = unlockData.getString("require");
                        if (!stringData.isEmpty() && !variant.hasHullMod(stringData)) {
                            continue;
                        }
                        stringData = unlockData.getString("require_tier");
                        if (!stringData.isEmpty()) {
                            var tier = button.getCustomData("tier");
                            if (tier != null) {
                                var tierReq = Settings_Mod.Unlock_Panel_GetRequireTier((Integer)tier, stringData);
                                if (tierReq > 0) {
                                    var tierReqMod = "o_sspecial_unlock_tier_" + tierReq;
                                    if (!variant.hasHullMod(tierReqMod)) {
                                        continue;
                                    }
                                }
                            }
                        }
                        var id = (String)button.getCustomData("id");
                        int hullModType = 0;
                        int type = 0;
                        boolean show = false;
                        stringData = unlockData.getString("type_mod");
                        if (!stringData.isEmpty()) {
                            hullModType = Integer.parseInt(stringData);
                        }
                        stringData = unlockData.getString("type");
                        if (!stringData.isEmpty()) {
                            type = Integer.parseInt(stringData);
                        }
                        stringData = unlockData.getString("show");
                        if (!stringData.isEmpty()) {
                            show = Boolean.parseBoolean(stringData);
                        }
                        boolean modified = false;
                        boolean isUpgrade = type == 10;
                        if (button.getValue()) {
                            if (!variant.hasHullMod(id)) {
                                if (hullModType == 0) {
                                    variant.addMod(id);
                                }
                                else {
                                    variant.addPermaMod(id, spCost > 0);
                                }
                                modified = true;
                            }
                            if (modified) {
                                if (spCost > 0) {
                                    player.getStats().spendStoryPoints(spCost, true, null, true, Settings_Mod.Unlock_Panel_GetStoryPointMessage().formatted(spCost));
                                    Global.getSoundPlayer().playUISound(Sounds.STORY_POINT_SPEND, 1f, 1f);
                                }
                                if (show) {
                                    variant.addPermaMod(id + "_show", spCost > 0);
                                }
                                if (isUpgrade) {
                                    upgrade = true;
                                }
                            }
                        }
                        else {
                            if (variant.hasHullMod(id) && hullModType == 0) {
                                variant.removeMod(id);
                                if (show && variant.hasHullMod(id + "_show")) {
                                    variant.removePermaMod(id + "_show");
                                }
                            }
                        }
                    }
                }
            }
            if (upgrade) {
                UpgradeHull(member, variant);
            }
            refreshVariant();
        }
        catch (Exception e) {
            Global.getLogger(Settings_Mod.class).log(Priority.ERROR, e);
        }
    }

    public void Clear() {
        CategoryButtons = null;
        HullModButtons = null;
    }

    @Override
    public void advance(FleetMemberAPI member, ShipVariantAPI variant, Float amount, MarketAPI market) {
        if (CategoryButtons == null)
            return;
        if (BackgroundPanel == null)
            return;
        boolean closePrev = false;
        for (LunaToggleButton_Unlock button : CategoryButtons) {
            if (button == null)
                continue;
            if (!button.getValue())
                continue;
            Object isOpen = button.getCustomData("open");
            if (isOpen instanceof Boolean open && !open) {
                closePrev = true;
                break;
            }
        }
        for (int row = 0; row < CategoryButtons.length; row++) {
            var categoryButton = CategoryButtons[row];
            if (categoryButton == null)
                continue;
            if (categoryButton.getCustomData("categoryPanel") instanceof TooltipMakerAPI categoryPanel) {
                if (categoryButton.getCustomData("open") instanceof Boolean open && open) {
                    if (closePrev || !categoryButton.getValue()) {
                        BackgroundPanel.removeComponent(categoryPanel);
                        categoryPanel.setOpacity(0f);
                        categoryPanel.getExternalScroller().setOpacity(0f);
                        categoryButton.setCustomData("open", false);
                        if (categoryButton.getValue()) {
                            categoryButton.Click();
                        }
                    }
                }
                else if (categoryButton.getValue()) {
                    BackgroundPanel.addUIElement(categoryPanel).inTL(0f, 0f);
                    categoryPanel.setOpacity(1f);
                    categoryPanel.getExternalScroller().setOpacity(1f);
                    categoryButton.setCustomData("open", true);
                }
            }
        }
    }

    public void UpgradeHull(FleetMemberAPI member, ShipVariantAPI variant) {
        if (variant.getHullSize() == ShipAPI.HullSize.CAPITAL_SHIP)
            return;
        var playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null)
            return;
        String[] hulls = new String[] { "o_sspecial_starfarer_destroyer", "o_sspecial_starfarer_cruiser", "o_sspecial_starfarer_capital" };
        int index = 0;
        if (variant.getHullSize() == ShipAPI.HullSize.DESTROYER) {
            index = 1;
        }
        else if (variant.getHullSize() == ShipAPI.HullSize.CRUISER) {
            index = 2;
        }
        var newVariant = Global.getSettings().createEmptyVariant("", Global.getSettings().getHullSpec(hulls[index]));
        Global.getSector().getPlayerFleet().getFleetData().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, newVariant));
        var slots = variant.getFittedWeaponSlots().toArray();
        for (int i = slots.length - 1; i >= 0; i--) {
            String weaponSlot = (String)slots[i];
            String weaponId = variant.getWeaponId(weaponSlot);
            if (weaponId != null && !weaponId.isEmpty()) {
                playerFleet.getCargo().addWeapons(weaponId, 1);
                variant.clearSlot(weaponSlot);
            }
        }
        playerFleet.removeFleetMemberWithDestructionFlash(member);
        Global.getSector().getCampaignUI().showCoreUITab(CoreUITabId.FLEET);
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
        return Global.getSettings().getString("o_sspecial", "unlock_panel_name");
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
        return variant.hasHullMod("o_sspecial_unlock");
    }

    @Override
    public boolean hasTooltip(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return false;
    }

    @Override
    public boolean shouldShow(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return variant.hasHullMod("o_sspecial_unlock");
    }

    @Override
    public boolean isClickable(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return variant.hasHullMod("o_sspecial_unlock");
    }

    @Override
    public void onPanelClose(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        CheckToggle(member, variant);
        Clear();
    }
}
