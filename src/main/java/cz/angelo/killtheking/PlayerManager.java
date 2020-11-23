package cz.angelo.killtheking;

import java.util.UUID;

public class PlayerManager {

    public UUID uuid;
    public boolean ingame;
    public int cooldown;
    public int coinsearned;
    public boolean isdead;
    public double chance;
    public boolean isKing;

    PlayerManager(UUID uuid, boolean ingame, int coinsearned, boolean isdead, double chance, boolean isKing){
        this.uuid = uuid;
        this.ingame = ingame;
        this.coinsearned = coinsearned;
        this.isdead = isdead;
        this.chance = chance;
        this.isKing = isKing;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isIngame() {
        return ingame;
    }

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCoinsearned() {
        return coinsearned;
    }

    public void setCoinsearned(int coinsearned) {
        this.coinsearned = coinsearned;
    }

    public boolean isIsdead() {
        return isdead;
    }

    public void setIsdead(boolean isdead) {
        this.isdead = isdead;
    }

    public double getChance(){ return this.chance; }

    public void setChance(double chance){ this.chance = chance; }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        this.isKing = king;
    }

}
