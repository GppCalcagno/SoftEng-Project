package it.polimi.ingsw.card.leadereffect;

import StubGiovanni.PlayerStub;
import StubGiovanni.RequestedResourcesStub;
import it.polimi.ingsw.producible.Coins;
import it.polimi.ingsw.producible.Resources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountLeaderTest {
    @Test
    void doSpecialAbilityTestActivated() {
        PlayerStub playerTest = new PlayerStub("Giocatore1");
        Resources coin = new Coins();
        RequestedResourcesStub requirementsLeader = new RequestedResourcesStub(coin, 1);
        DiscountLeader leaderActionTest = new DiscountLeader();

        leaderActionTest.doSpecialAbility(playerTest);
        assertTrue(leaderActionTest.getActivated());
    }

    @Test
    void doSpecialAbilityTestAdd() {
        PlayerStub playerTest = new PlayerStub("Giocatore2");
        Resources coin = new Coins();
        RequestedResourcesStub requirementsLeader = new RequestedResourcesStub(coin, 1);
        DiscountLeader leaderActionTest = new DiscountLeader();

        leaderActionTest.doSpecialAbility(playerTest);
        assertTrue(playerTest.getLeaderCardEffectDiscount().contains(coin));
    }
}