import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import lunalib.lunaRefit.LunaRefitManager;
import org.apache.log4j.Priority;
import settings.Settings_Mod;
import unlock.Panel_Unlock;

import java.util.Set;

public class ModPlugin extends BaseModPlugin {

    @Override
    public void onNewGame() {
        super.onNewGame();
    }

    @Override
    public void onApplicationLoad() throws Exception {
        Settings_Mod.Init();
        Settings_Mod.Init_CreateData();
        LunaRefitManager.addRefitButton(new Panel_Unlock());
        Global.getLogger(this.getClass()).log(Priority.DEBUG, Global.getSettings().getHullModSpec("o_sspecial_unlock_weapon"));
    }
}
