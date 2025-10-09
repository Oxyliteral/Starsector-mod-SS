package unlock;

import com.fs.starfarer.api.ModSpecAPI;
import com.fs.starfarer.api.combat.HullModEffect;
import com.fs.starfarer.api.combat.HullModFleetEffect;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;

import java.util.Set;

public class HullModSpecAPI_Copy implements HullModSpecAPI {

    public String Id;

    public HullModSpecAPI Copy;

    public HullModSpecAPI_Copy(String id, HullModSpecAPI copy) {
        Id = id;
        Copy = copy;
    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public void setId(String id) {
        Id = id;
    }

    @Override
    public HullModEffect getEffect() {
        return Copy.getEffect();
    }

    @Override
    public HullModFleetEffect getFleetEffect() {
        return Copy.getFleetEffect();
    }

    @Override
    public boolean isAlwaysUnlocked() {
        return Copy.isAlwaysUnlocked();
    }

    @Override
    public boolean isHidden() {
        return Copy.isHidden();
    }

    @Override
    public boolean isHiddenEverywhere() {
        return Copy.isHiddenEverywhere();
    }

    @Override
    public void setHidden(boolean isHidden) {
        Copy.setHidden(isHidden);
    }

    @Override
    public void setHiddenEverywhere(boolean isHiddenEverywhere) {
        Copy.setHiddenEverywhere(isHiddenEverywhere);
    }

    @Override
    public void setAlwaysUnlocked(boolean isStarting) {
        Copy.setAlwaysUnlocked(isStarting);
    }

    @Override
    public String getEffectClass() {
        return Copy.getEffectClass();
    }

    @Override
    public void setEffectClass(String effectClass) {
        Copy.setEffectClass(effectClass);
    }

    @Override
    public String getDisplayName() {
        return Copy.getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        Copy.setDisplayName(displayName);
    }

    @Override
    public String getDescriptionFormat() {
        return Copy.getDescriptionFormat();
    }

    @Override
    public void setDescriptionFormat(String descriptionFormat) {
        Copy.setDescriptionFormat(descriptionFormat);
    }

    @Override
    public int getFrigateCost() {
        return Copy.getFrigateCost();
    }

    @Override
    public void setFrigateCost(int frigateCost) {
        Copy.setFrigateCost(frigateCost);
    }

    @Override
    public int getDestroyerCost() {
        return Copy.getDestroyerCost();
    }

    @Override
    public void setDestroyerCost(int destroyerCost) {
        Copy.setDestroyerCost(destroyerCost);
    }

    @Override
    public int getCruiserCost() {
        return Copy.getCruiserCost();
    }

    @Override
    public void setCruiserCost(int cruiserCost) {
        Copy.setCruiserCost(cruiserCost);
    }

    @Override
    public int getCapitalCost() {
        return Copy.getCapitalCost();
    }

    @Override
    public void setCapitalCost(int capitalCost) {
        Copy.setCapitalCost(capitalCost);
    }

    @Override
    public int getTier() {
        return Copy.getTier();
    }

    @Override
    public void setTier(int tier) {
        Copy.setTier(tier);
    }

    @Override
    public String getSpriteName() {
        return Copy.getSpriteName();
    }

    @Override
    public void setSpriteName(String spriteName) {
        Copy.setSpriteName(spriteName);
    }

    @Override
    public int getCostFor(ShipAPI.HullSize size) {
        return Copy.getCostFor(size);
    }

    @Override
    public Set<String> getTags() {
        return Copy.getTags();
    }

    @Override
    public void addTag(String tag) {
        Copy.addTag(tag);
    }

    @Override
    public boolean hasTag(String tag) {
        return Copy.hasTag(tag);
    }

    @Override
    public float getBaseValue() {
        return Copy.getBaseValue();
    }

    @Override
    public void setBaseValue(float baseValue) {
        Copy.setBaseValue(baseValue);
    }

    @Override
    public float getRarity() {
        return Copy.getRarity();
    }

    @Override
    public void setRarity(float rarity) {
        Copy.setRarity(rarity);
    }

    @Override
    public String getDescription(ShipAPI.HullSize size) {
        return Copy.getDescription(size);
    }

    @Override
    public String getManufacturer() {
        return Copy.getManufacturer();
    }

    @Override
    public Set<String> getUITags() {
        return Copy.getUITags();
    }

    @Override
    public void addUITag(String tag) {
        Copy.addUITag(tag);
    }

    @Override
    public boolean hasUITag(String tag) {
        return Copy.hasUITag(tag);
    }

    @Override
    public void setManufacturer(String manufacturer) {
        Copy.setManufacturer(manufacturer);
    }

    @Override
    public String getSModDescription(ShipAPI.HullSize hullSize) {
        return Copy.getSModDescription(hullSize);
    }

    @Override
    public void setSModEffectFormat(String sModEffectFormat) {
        Copy.setSModEffectFormat(sModEffectFormat);
    }

    @Override
    public String getSModEffectFormat() {
        return Copy.getSModEffectFormat();
    }

    @Override
    public ModSpecAPI getSourceMod() {
        return Copy.getSourceMod();
    }
}
