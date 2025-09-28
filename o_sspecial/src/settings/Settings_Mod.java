package settings;

import com.fs.starfarer.api.Global;

public class Settings_Mod {

    public static float Unlock_Panel_GetWidth() {
        return Global.getSettings().getScreenWidth() * 0.5f;
    }

    public static float Unlock_Panel_GetHeight() {
        return Global.getSettings().getScreenHeight() * 0.5f;
    }

    public static int Unlock_Panel_Weapons_GetOpCost() {
        return 10;
    }

    public static String Unlock_Panel_Weapons_GetId(int index) {
        return "o_sspecial_unlock_weapon_" + (index + 1);
    }
}
