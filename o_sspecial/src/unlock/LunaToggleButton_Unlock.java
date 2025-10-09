package unlock;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import kotlin.Unit;
import lunalib.lunaUI.elements.LunaElement;
import lunalib.lunaUI.elements.LunaToggleButton;

import java.util.ArrayList;

public class LunaToggleButton_Unlock extends LunaElement {

    protected boolean Toggled;
    protected boolean Toggleable;
    protected boolean Hoverable;

    protected String EnabledText = "Enabled";
    protected String DisabledText = "Disabled";

    public LunaToggleButton_Unlock(boolean toggled, TooltipMakerAPI tooltip, float width, float height) {
        super(tooltip, width, height);
        Toggled = toggled;
        Init();
        changeStateText(EnabledText, DisabledText);
    }

    protected void Init() {
        Toggleable = true;
        Hoverable = true;
        var color = Global.getSettings().getBasePlayerColor();
        String text = Toggled ? EnabledText : DisabledText;
        addText(text, color, color, new ArrayList<String>());
        centerText();

        onHoverEnter(this::Action_OnHoverEnter);
        onHoverExit(this::Action_OnHoverExit);
        onClick(this::Action_OnClick);
    }

    public boolean getValue() {
        return Toggled;
    }

    public boolean getToggleable() {
        return Toggleable;
    }

    public boolean getHoverable() {
        return Hoverable;
    }

    public void setToggleable(boolean toggleable) {
        Toggleable = toggleable;
    }

    public void setHoverable(boolean hoverable) {
        Hoverable = hoverable;
    }

    public Unit Action_OnHoverEnter(InputEventAPI api) {
        if (Hoverable) {
            playScrollSound();
            this.setBorderColor(Global.getSettings().getDarkPlayerColor().brighter());
        }
        return null;
    }

    public Unit Action_OnHoverExit(InputEventAPI api) {
        if (Hoverable) {
            this.setBorderColor(Global.getSettings().getDarkPlayerColor());
        }
        return null;
    }

    public Unit Action_OnClick(InputEventAPI api) {
        if (Toggleable) {
            Toggled = !Toggled;
            playClickSound();
            String text = EnabledText;
            var color = Global.getSettings().getDarkPlayerColor().darker();
            if (!Toggled) {
                text = DisabledText;
                color = color.darker();
            }
            changeText(text, new ArrayList<String>());
            setBackgroundColor(color);
            centerText();
        }
        return null;
    }

    public void changeStateText(String enabledText, String disabledText)
    {
        EnabledText = enabledText;
        DisabledText = disabledText;
        String text = EnabledText;
        var color = Global.getSettings().getDarkPlayerColor().darker();
        if (!Toggled) {
            text = DisabledText;
            color = color.darker();
        }
        changeText(text, new ArrayList<String>());
        setBackgroundColor(color);
        centerText();
    }

    public void Click() {
        var list = new ArrayList<InputEventAPI>();
        list.add(new InputEventAPI_Dummy((int) this.getPosition().getCenterX(), (int) this.getPosition().getCenterY(), true, false));
        list.add(new InputEventAPI_Dummy((int) this.getPosition().getCenterX(), (int) this.getPosition().getCenterY(), false, true));
        this.processInput(list);
    }
}
