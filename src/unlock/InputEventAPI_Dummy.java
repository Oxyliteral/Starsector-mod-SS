package unlock;

import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.input.InputEventClass;
import com.fs.starfarer.api.input.InputEventType;
import com.fs.starfarer.api.ui.PositionAPI;

public class InputEventAPI_Dummy implements InputEventAPI {

    public boolean MouseDownEvent;

    public boolean MouseUpEvent;

    public int X;

    public int Y;

    public InputEventAPI_Dummy(int x, int y, boolean mouseDownEvent, boolean mouseUpEvent) {
        X = x;
        Y = y;
        MouseDownEvent = mouseDownEvent;
        MouseUpEvent = mouseUpEvent;
    }

    @Override
    public int getEventValue() {
        return 0;
    }

    @Override
    public int getX() {
        return X;
    }

    @Override
    public int getY() {
        return Y;
    }

    @Override
    public int getDX() {
        return 0;
    }

    @Override
    public int getDY() {
        return 0;
    }

    @Override
    public InputEventClass getEventClass() {
        return null;
    }

    @Override
    public void logEvent() {

    }

    @Override
    public boolean isConsumed() {
        return false;
    }

    @Override
    public void consume() {

    }

    @Override
    public boolean isRepeat() {
        return false;
    }

    @Override
    public InputEventType getEventType() {
        return null;
    }

    @Override
    public boolean isMouseEvent() {
        return false;
    }

    @Override
    public boolean isKeyboardEvent() {
        return false;
    }

    @Override
    public boolean isKeyUpEvent() {
        return false;
    }

    @Override
    public boolean isKeyDownEvent() {
        return false;
    }

    @Override
    public boolean isMouseUpEvent() {
        return MouseUpEvent;
    }

    @Override
    public boolean isMouseDownEvent() {
        return MouseDownEvent;
    }

    @Override
    public boolean isLMBDownEvent() {
        return false;
    }

    @Override
    public boolean isLMBEvent() {
        return false;
    }

    @Override
    public boolean isRMBEvent() {
        return false;
    }

    @Override
    public boolean isLMBUpEvent() {
        return false;
    }

    @Override
    public boolean isRMBDownEvent() {
        return false;
    }

    @Override
    public boolean isRMBUpEvent() {
        return false;
    }

    @Override
    public boolean isMouseMoveEvent() {
        return false;
    }

    @Override
    public boolean isMouseScrollEvent() {
        return false;
    }

    @Override
    public char getEventChar() {
        return 0;
    }

    @Override
    public boolean isAltDown() {
        return false;
    }

    @Override
    public boolean isCtrlDown() {
        return false;
    }

    @Override
    public boolean isShiftDown() {
        return false;
    }

    @Override
    public boolean isUnmodified() {
        return false;
    }

    @Override
    public boolean isDoubleClick() {
        return false;
    }

    @Override
    public boolean isModifierKey() {
        return false;
    }

    @Override
    public boolean isControlDownEvent(String controlEnumName) {
        return false;
    }

    @Override
    public boolean isControlUpEvent(String controlEnumName) {
        return false;
    }

    @Override
    public boolean isControlActivated(String enumName) {
        return false;
    }
}
