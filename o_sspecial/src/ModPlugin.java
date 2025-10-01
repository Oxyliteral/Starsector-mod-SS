import com.fs.starfarer.api.BaseModPlugin;
import lunalib.lunaRefit.LunaRefitManager;

public class ModPlugin extends BaseModPlugin {

    @Override
    public void onNewGame() {
        super.onNewGame();
    }

    @Override
    public void onApplicationLoad() throws Exception {
        LunaRefitManager.addRefitButton(new Panel_Unlock());
    }
}
