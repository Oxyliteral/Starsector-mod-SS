package settings;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Fonts;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import lunalib.backend.util.ReflectionUtils;
import org.apache.log4j.Priority;
import org.json.JSONObject;
import unlock.HullModSpecAPI_Copy;

import java.util.ArrayList;

public class Settings_Mod {

    public static JSONObject[][] Data;

    public static JSONObject[][] Unlock_GetData() {
        return Data;
    }

    public static void Init() {
        try {
            var csv = Global.getSettings().getMergedSpreadsheetDataForMod("id", "data/config/unlocks.csv", "o_sspecial");
            Data = new JSONObject[csv.length() + 1][];
            for (int i = 0; i < csv.length(); i++) {
                var obj = csv.getJSONObject(i);
                String type = obj.getString("type");
                if (type.isEmpty()) {
                    continue;
                }
                int row = Integer.parseInt(type);
                int col = 0;
                String tier = obj.getString("tier");
                if (!tier.isEmpty()) {
                    col = Integer.parseInt(tier);
                }
                if (row > 0 && row < Data.length) {
                    if (Data[row] == null) {
                        Data[row] = new JSONObject[obj.getInt("max_init") + 1];
                    }
                    if (Data[row][col] == null) {
                        Data[row][col] = obj;
                        if (col > 0 && Data[row][0] != null) {
                            Merge(obj, Data[row][0], false);
                        }
                    }
                    else {
                        Merge(Data[row][col], obj, true);
                    }
                }
            }
        }
        catch (Exception e) {
            Global.getLogger(Settings_Mod.class).log(Priority.ERROR, e);
        }
    }

    public static void Init_CreateData() {
        try {
            for (int row = 1; row < Data.length; row++) {
                if (Data[row] == null)
                    continue;
                for (int col = 1; col < Data[row].length; col++) {
                    if (Data[row][0] == null) {
                        break;
                    }
                    var original = Data[row][0].getString("id");
                    var hullMod = Global.getSettings().getHullModSpec(original);
                    if (hullMod == null) {
                        continue;
                    }
                    var id = original + "_" + col;
                    var dupe = DuplicateHullModSpec(hullMod);
                    dupe.setId(id);
                    Global.getSettings().putSpec(hullMod.getClass(), id, dupe);
                    var data = Data[row][col];
                    if (data == null) {
                        data = Data[row][0];
                    }
                    var show = data.getString("show");
                    if (!show.isEmpty() && Boolean.parseBoolean(show)) {
                        id += "_show";
                        dupe = DuplicateHullModSpec(hullMod);
                        dupe.setHiddenEverywhere(false);
                        dupe.setId(id);
                        Global.getSettings().putSpec(dupe.getClass(), id, dupe);

                    }
                }
            }
        }
        catch (Exception e) {
            Global.getLogger(Settings_Mod.class).log(Priority.ERROR, e);
        }
    }

    public static HullModSpecAPI DuplicateHullModSpec(HullModSpecAPI original) {
        var spec = InstantiateHullModSpec(original);
        CopyHullModSpec(original, spec);
        return spec;
    }

    public static void CopyHullModSpec(HullModSpecAPI original, HullModSpecAPI spec) {
        spec.setDisplayName(original.getDisplayName());
        spec.setId(original.getId());
        spec.setTier(original.getTier());
        spec.setRarity(original.getRarity());
        spec.setManufacturer(original.getManufacturer());
        spec.getTags().addAll(original.getTags());
        spec.getUITags().addAll(original.getUITags());
        spec.setBaseValue(original.getBaseValue());
        spec.setAlwaysUnlocked(original.isAlwaysUnlocked());
        spec.setHidden(original.isHidden());
        spec.setHiddenEverywhere(original.isHiddenEverywhere());
        spec.setFrigateCost(original.getFrigateCost());
        spec.setDestroyerCost(original.getDestroyerCost());
        spec.setCruiserCost(original.getCruiserCost());
        spec.setCapitalCost(original.getCapitalCost());
        spec.setEffectClass(original.getEffectClass());
        spec.setDescriptionFormat(original.getDescriptionFormat());
        spec.setSModEffectFormat(original.getSModEffectFormat());
        spec.setSpriteName(original.getSpriteName());
    }

    public static HullModSpecAPI InstantiateHullModSpec(HullModSpecAPI original) {
        return (HullModSpecAPI)ReflectionUtils.INSTANCE.instantiate(original.getClass());
    }

    public static Object InvokeMethod(Object object, Class<?> returnType, String methodName, Object... args) {
        ArrayList<Class<?>> argTypes = new ArrayList<>();
        for (Object arg : args) {
            argTypes.add(arg.getClass());
        }
        var method = ReflectionUtils.INSTANCE.getMethod(methodName, object.getClass(), returnType, argTypes.size(), argTypes);
        Object returnValue = null;
        if (method != null) {
            returnValue = method.invoke(object, args);
        }
        return returnValue;
    }

    public static void Merge(JSONObject original, JSONObject merge, boolean replace) {
        try {
            var keys = merge.keys();
            while (keys.hasNext()) {
                var key = (String)keys.next();
                if (replace || ((String)original.get(key)).isEmpty()) {
                    var value = merge.getString(key);
                    if (!value.isEmpty()) {
                        original.put(key, merge.get(key));
                    }
                }
            }
        }
        catch (Exception e) {
            Global.getLogger(Settings_Mod.class).log(Priority.ERROR, e);
        }
    }

    public static Object[] Unlock_Panel_GetTableHeader(JSONObject obj, TooltipMakerAPI panel, float maxWidth, float[] outWidth) {
        String[] headers = Unlock_Panel_GetTableHeaders();
        Object[] tableHeaders = new Object[headers.length * 2];
        float currWidth = 0f;
        for (int i = 0; i < tableHeaders.length; i += 2) {
            var head = headers[i / 2];
            tableHeaders[i] = head;
            float addWidth = Global.getSettings().computeStringWidth(head, Fonts.DEFAULT_SMALL);
            tableHeaders[i + 1] = addWidth;
            currWidth += addWidth;
        }
        currWidth = (maxWidth - currWidth) / (headers.length + 1);
        for (int i = 1; i < tableHeaders.length; i += 2) {
             var addWidth = (float)tableHeaders[i] + currWidth;
             tableHeaders[i] = addWidth;
        }
        outWidth[0] = (float)tableHeaders[1];
        return tableHeaders;
    }

    public static String[] Unlock_Panel_GetTableHeaders() {
        String[] headers = new String[] { "unlock_panel_header_enabled", "unlock_panel_header_tier",
                "unlock_panel_header_sprite", "unlock_panel_header_name",
                "unlock_panel_header_cost_op", "unlock_panel_header_cost_sp",
                "unlock_panel_header_description", "unlock_panel_header_require", "unlock_panel_header_require_tier" };
        for (int i = 0; i < headers.length; i++) {
            headers[i] = Global.getSettings().getString("o_sspecial", headers[i]);
        }
        return headers;
    }

    public static Object[] Unlock_Panel_GetTableValues(JSONObject obj, ShipVariantAPI variant, int row, HullModSpecAPI hullMod) {
        try {
            String name = "error";
            String cost = "error";
            String description = "error";
            if (hullMod != null) {
                name = hullMod.getDisplayName();
                int tempCost = hullMod.getCostFor(variant.getHullSize());;
                cost = (tempCost == 0) ? "" : "" + tempCost;
                description = hullMod.getDescription(variant.getHullSize());
            }
            String tierReq = "";
            var tierRequire = Unlock_Panel_GetRequireTier(row, obj.getString("require_tier"));
            if (tierRequire > 0) {
                tierReq += tierRequire;
            }
            String costSp = obj.getString("cost_sp");
            if (!costSp.isEmpty() && Integer.parseInt(costSp) <= 0) {
                costSp = "";
            }
            return new Object[] {
                    "",
                    "" + row,
                    obj.getString("sprite"),
                    name,
                    cost,
                    costSp,
                    description,
                    obj.getString("require"),
                    tierReq
            };
        }
        catch (Exception e) {
            Global.getLogger(Settings_Mod.class).log(Priority.ERROR, e);
        }
        return new Object[0];
    }

    public static int Unlock_Panel_GetRequireTier(int row, String requireTier) {
        if (requireTier.isEmpty()) {
            return 0;
        }
        if (requireTier.charAt(0) == '+') {
            int value = Integer.parseInt(requireTier.substring(1));
            return row + value;
        }
        else if (requireTier.charAt(0) == '-') {
            int value = Integer.parseInt(requireTier.substring(1));
            return row - value;
        }
        return Integer.parseInt(requireTier);
    }

    public static float Unlock_Panel_GetWidth() {
        return Global.getSettings().getScreenWidth() * 0.5f;
    }

    public static float Unlock_Panel_GetHeight() {
        return Global.getSettings().getScreenHeight() * 0.5f;
    }

    public static String Unlock_Panel_Weapons_GetId(int index) {
        return "o_sspecial_unlock_weapon_" + (index + 1);
    }

    public static String Unlock_Panel_GetStoryPointMessage() {
        return "Spent %d Story Points on improving a ship!";
    }
}
