package settings;

import com.fs.starfarer.api.Global;

public class Settings_Mod {

    public static float Unlock_Panel_GetWidth() {
        return Global.getSettings().getScreenWidth() * 0.5f;
    }

    public static float Unlock_Panel_GetHeight() {
        return Global.getSettings().getScreenHeight() * 0.5f;
    }

    public static String Unlock_Panel_Weapons_GetId(int index) {
        return "o_sspecial_unlock_weapon_" + (index + 1);
    }

    public static Object[][] Unlock_Panel_GetButtonParams() {
        // { "String:name", "int:max", "int:storypointcost" }
        return new Object[][]
        {
                { "Weapons", "o_sspecial_unlock_weapon_%d", 10, 0, "", null },
                { "Flux Capacity", "o_sspecial_unlock_flux_c_%d", 3, 0, "", null },
                { "Flux Vents", "o_sspecial_unlock_flux_d_%d", 3, 0, "", null },
                { "Logistics", "o_sspecial_unlock_logistic_%d", 4, 0, "", null },
                { "OP !(SP Use)!", "o_sspecial_unlock_op_%d", 4, 4, "o_sspecial_unlock_op_%d_hidden", new Object[] { "o_sspecial_unlock_op_%d", -1 }},
                { "Hull", "o_sspecial_unlock_hull_%d", 0, 0, "", null },
        };
    }

    public static String Unlock_Panel_GetStoryPointWarning() {
        return "!(%d SP)!";
    }

    public static String Unlock_Panel_GetStoryPointMessage() {
        return "Spent %d Story Points on improving a ship!";
    }
}
