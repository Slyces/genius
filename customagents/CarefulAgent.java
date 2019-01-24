package customagents;

import negotiator.*;
import negotiator.actions.Action;

import java.util.List;
import java.util.ArrayList;

public class CarefulAgent extends Agent {

    private List<Bid> acceptableBids;
    @Override
    public void init() {
        super.init();
        acceptableBids = new ArrayList<Bid>();
        BidIterator iter = new BidIterator(this.utilitySpace.getDomain());
        while (iter.hasNext()) {
            Bid bid = iter.next();
            try {
                if (getUtility(bid) >= utilitySpace.getReservationValue() && (Math.random() <= getUtility(bid)))
                    this.acceptableBids.add(bid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Action chooseAction() {
        return null;
    }
}
